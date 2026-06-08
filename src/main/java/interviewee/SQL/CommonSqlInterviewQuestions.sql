-- 常见 SparkSQL 面试题集合
-- 说明：
-- 1. 语法按 Spark SQL 3.x 编写，适合 SparkSQL / Databricks / Hive Metastore 表场景。
-- 2. 每道题都给出表结构假设、题目和参考 SQL。
-- 3. 涉及窗口函数的题目默认数据库支持 row_number/rank/dense_rank/lag/sum over。
-- 4. 如果日期字段是字符串，实际使用前可先用 to_date()/to_timestamp() 做类型转换。

-- =====================================================================
-- 01. 查询第二高薪水
-- 表：employee(id, name, salary)
-- 题目：返回 employee 表中的第二高薪水；不存在时返回 null。
-- 考点：dense_rank、窗口函数，避免使用 SparkSQL 不常用的 offset。
-- 答案：这道题的核心是先对薪资去重，再按薪资降序做 `dense_rank`，最后取排名为 2 的那一档；用 `MAX(CASE WHEN ...)` 可以在不存在第二高薪时自然返回 `null`。
-- =====================================================================
WITH ranked_salary AS (
    SELECT
        salary,
        DENSE_RANK() OVER (ORDER BY salary DESC) AS ranking
    FROM (
        SELECT DISTINCT salary
        FROM employee
    ) t
)
SELECT
    MAX(CASE WHEN ranking = 2 THEN salary END) AS second_highest_salary
FROM ranked_salary;


-- =====================================================================
-- 02. 查询第 N 高薪水
-- 表：employee(id, name, salary)
-- 题目：查询第 N 高薪水；下面示例查询第 3 高，实际使用时把 3 改成目标 N。
-- 考点：dense_rank、窗口函数。
-- 答案：思路和第二高薪一致，本质上是先给薪资做降序排名，再筛出目标名次；如果业务要求可配置，实际项目里一般把 N 作为参数传入。
-- =====================================================================
WITH ranked_salary AS (
    SELECT
        salary,
        DENSE_RANK() OVER (ORDER BY salary DESC) AS ranking
    FROM employee
)
SELECT MAX(salary) AS nth_highest_salary
FROM ranked_salary
WHERE ranking = 3;


-- =====================================================================
-- 03. 每个部门薪水最高的员工
-- 表：employee(id, name, salary, department_id)
-- 表：department(id, name)
-- 题目：查询每个部门薪水最高的员工，若并列最高则都返回。
-- 考点：rank、partition by。
-- 答案：先按部门分组对员工薪资做降序排名，再筛出排名为 1 的记录即可；这里用 `rank` 而不是 `row_number`，是因为并列最高薪也要一起返回。
-- =====================================================================
WITH ranked_employee AS (
    SELECT
        e.*,
        RANK() OVER (PARTITION BY e.department_id ORDER BY e.salary DESC) AS ranking
    FROM employee e
)
SELECT
    d.name AS department_name,
    r.name AS employee_name,
    r.salary
FROM ranked_employee r
JOIN department d ON r.department_id = d.id
WHERE r.ranking = 1;


-- =====================================================================
-- 04. 每个部门薪水前三的员工
-- 表：employee(id, name, salary, department_id)
-- 表：department(id, name)
-- 题目：查询每个部门薪水排名前三的员工，薪水相同视为同一名次。
-- 考点：dense_rank、TopN per group。
-- 答案：这题是典型的分组 TopN，先按部门分区、按薪资降序做 `dense_rank`，再取排名小于等于 3 的记录；`dense_rank` 能保证同薪资属于同一名次。
-- =====================================================================
WITH ranked_employee AS (
    SELECT
        e.*,
        DENSE_RANK() OVER (PARTITION BY e.department_id ORDER BY e.salary DESC) AS ranking
    FROM employee e
)
SELECT
    d.name AS department_name,
    r.name AS employee_name,
    r.salary,
    r.ranking
FROM ranked_employee r
JOIN department d ON r.department_id = d.id
WHERE r.ranking <= 3
ORDER BY d.name, r.ranking, r.salary DESC;


-- =====================================================================
-- 05. 查找重复邮箱
-- 表：person(id, email)
-- 题目：查询出现次数超过 1 的邮箱。
-- 考点：group by、having。
-- 答案：重复邮箱问题本质是按邮箱分组后统计出现次数，所以直接 `group by + having count(*) > 1` 就能找出所有重复值。
-- =====================================================================
SELECT email
FROM person
GROUP BY email
HAVING COUNT(*) > 1;


-- =====================================================================
-- 06. 删除重复邮箱，只保留 id 最小的一条
-- 表：person(id, email)
-- 题目：删除重复邮箱记录，保留每个 email 对应的最小 id。
-- 考点：row_number 去重。SparkSQL 离线表常用 INSERT OVERWRITE 写回去重结果。
-- 答案：删除重复记录的关键是先用 `row_number` 给同一个邮箱按 id 从小到大编号，再保留编号为 1 的那条；SparkSQL 离线场景通常用 `insert overwrite` 重写整张表。
-- =====================================================================
INSERT OVERWRITE TABLE person
SELECT id, email
FROM (
    SELECT
        id,
        email,
        ROW_NUMBER() OVER (PARTITION BY email ORDER BY id) AS rn
    FROM person
) t
WHERE rn = 1;


-- =====================================================================
-- 07. 查询从未下单的客户
-- 表：customers(id, name)
-- 表：orders(id, customer_id, order_date)
-- 题目：查询没有任何订单的客户。
-- 考点：left join anti join / not exists。
-- 答案：查询从未下单的客户本质是做反连接，`left anti join` 在 SparkSQL 里既直观又高效，语义上就是只保留左表中没有匹配订单的客户。
-- =====================================================================
SELECT c.id, c.name
FROM customers c
LEFT ANTI JOIN orders o ON c.id = o.customer_id;


-- =====================================================================
-- 08. 查询购买过所有商品的客户
-- 表：orders(id, customer_id, product_id)
-- 表：products(id, name)
-- 题目：查询下单覆盖了 products 表中所有商品的客户。
-- 考点：关系除法、count distinct。
-- 答案：这道题是关系除法的典型写法，先统计每个客户买过多少种不同商品，再和商品总数比较；只有覆盖全部商品的客户才满足条件。
-- =====================================================================
SELECT o.customer_id
FROM orders o
GROUP BY o.customer_id
HAVING COUNT(DISTINCT o.product_id) = (
    SELECT COUNT(*) FROM products
);


-- =====================================================================
-- 09. 每日活跃用户数 DAU
-- 表：user_login(user_id, login_time)
-- 题目：按天统计活跃用户数。
-- 考点：date 截断、count distinct。
-- 答案：DAU 统计的关键是先把登录时间归一化到天，再对用户去重计数；如果源字段是字符串，生产里要先做日期类型转换避免隐式转换问题。
-- =====================================================================
SELECT
    TO_DATE(login_time) AS login_date,
    COUNT(DISTINCT user_id) AS dau
FROM user_login
GROUP BY TO_DATE(login_time)
ORDER BY login_date;


-- =====================================================================
-- 10. 次日留存率
-- 表：user_login(user_id, login_date)
-- 题目：统计每天登录用户在次日仍登录的比例。
-- 考点：自连接、留存率。
-- 答案：次日留存的本质是把某天登录用户和下一天同一个用户的登录记录做自连接，再用次日仍登录的人数除以当天活跃人数。
-- =====================================================================
SELECT
    a.login_date,
    COUNT(DISTINCT a.user_id) AS active_users,
    COUNT(DISTINCT b.user_id) AS retained_users,
    COUNT(DISTINCT b.user_id) * 1.0 / COUNT(DISTINCT a.user_id) AS next_day_retention
FROM user_login a
LEFT JOIN user_login b
  ON a.user_id = b.user_id
 AND b.login_date = DATE_ADD(a.login_date, 1)
GROUP BY a.login_date
ORDER BY a.login_date;


-- =====================================================================
-- 11. 连续登录至少 3 天的用户
-- 表：user_login(user_id, login_date)
-- 题目：找出至少连续登录 3 天的用户。
-- 考点：gaps and islands、date - row_number。
-- 答案：连续登录问题常用 gaps and islands 思路，先按用户和日期排序，再用日期减去行号构造连续段标识，最后统计每段长度是否大于等于 3。
-- =====================================================================
WITH distinct_login AS (
    SELECT DISTINCT user_id, login_date
    FROM user_login
),
numbered_login AS (
    SELECT
        user_id,
        login_date,
        ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY login_date) AS rn
    FROM distinct_login
),
login_group AS (
    SELECT
        user_id,
        login_date,
        DATE_SUB(login_date, CAST(rn AS INT)) AS group_key
    FROM numbered_login
)
SELECT user_id
FROM login_group
GROUP BY user_id, group_key
HAVING COUNT(*) >= 3;


-- =====================================================================
-- 12. 查询每个用户最近一次登录记录
-- 表：user_login(id, user_id, login_time, device)
-- 题目：返回每个用户最近一次登录的完整记录。
-- 考点：row_number、partition by。
-- 答案：最近一次登录属于分组取最新记录问题，先按用户分区并按登录时间倒序编号，再取 `row_number = 1` 的那条完整记录即可。
-- =====================================================================
WITH ranked_login AS (
    SELECT
        *,
        ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY login_time DESC, id DESC) AS rn
    FROM user_login
)
SELECT id, user_id, login_time, device
FROM ranked_login
WHERE rn = 1;


-- =====================================================================
-- 13. 按月统计销售额和累计销售额
-- 表：orders(id, order_date, amount)
-- 题目：按月统计销售额，并给出到当月为止的累计销售额。
-- 考点：trunc、sum over。
-- 答案：先把订单聚合到月，再对月销售额做窗口累计求和，这样既能得到每月销售额，也能得到截至当月的累计销售额。
-- =====================================================================
WITH monthly_sales AS (
    SELECT
        TRUNC(order_date, 'MM') AS month,
        SUM(amount) AS month_amount
    FROM orders
    GROUP BY TRUNC(order_date, 'MM')
)
SELECT
    month,
    month_amount,
    SUM(month_amount) OVER (ORDER BY month) AS cumulative_amount
FROM monthly_sales
ORDER BY month;


-- =====================================================================
-- 14. 近 7 天滚动销售额
-- 表：daily_sales(sale_date, amount)
-- 题目：按日期统计当天及过去 6 天的滚动销售额。
-- 考点：窗口函数 rows between。
-- 答案：滚动 7 天销售额适合用窗口函数 `rows between 6 preceding and current row`，它表示当前行向前数 6 行到当前行的累计值。
-- =====================================================================
SELECT
    sale_date,
    amount,
    SUM(amount) OVER (
        ORDER BY sale_date
        ROWS BETWEEN 6 PRECEDING AND CURRENT ROW
    ) AS rolling_7_day_amount
FROM daily_sales
ORDER BY sale_date;


-- =====================================================================
-- 15. 查询每个品类销量最高的商品
-- 表：order_items(order_id, product_id, quantity)
-- 表：products(id, category_id, name)
-- 题目：统计销量，并返回每个品类销量最高的商品；并列时都返回。
-- 考点：聚合后再窗口排序。
-- 答案：先按品类和商品聚合销量，再在每个品类内部按销量降序排名，最后取排名第一的商品；如果并列第一也要保留，可以用 `rank`。
-- =====================================================================
WITH product_sales AS (
    SELECT
        p.category_id,
        p.id AS product_id,
        p.name AS product_name,
        SUM(oi.quantity) AS total_quantity
    FROM products p
    JOIN order_items oi ON p.id = oi.product_id
    GROUP BY p.category_id, p.id, p.name
),
ranked_product AS (
    SELECT
        *,
        RANK() OVER (PARTITION BY category_id ORDER BY total_quantity DESC) AS ranking
    FROM product_sales
)
SELECT category_id, product_id, product_name, total_quantity
FROM ranked_product
WHERE ranking = 1;


-- =====================================================================
-- 16. 查询每门课分数排名
-- 表：scores(student_id, course_id, score)
-- 题目：查询每门课中每个学生的分数排名，分数相同名次相同。
-- 考点：dense_rank。
-- 答案：课程内排名属于典型窗口排序题，按课程分区、按分数降序做 `dense_rank`，既能得到名次，又能正确处理并列分数。
-- =====================================================================
SELECT
    student_id,
    course_id,
    score,
    DENSE_RANK() OVER (PARTITION BY course_id ORDER BY score DESC) AS ranking
FROM scores;


-- =====================================================================
-- 17. 行转列：每个学生的各科成绩
-- 表：scores(student_id, course_name, score)
-- 题目：把语文、数学、英语三门课转成列。
-- 考点：条件聚合、pivot。
-- 答案：行转列本质是条件聚合，对每门课写一个 `case when`，再按学生分组聚合；如果科目非常多或动态变化，真实项目里更适合用 `pivot`。
-- =====================================================================
SELECT
    student_id,
    MAX(CASE WHEN course_name = '语文' THEN score END) AS chinese_score,
    MAX(CASE WHEN course_name = '数学' THEN score END) AS math_score,
    MAX(CASE WHEN course_name = '英语' THEN score END) AS english_score
FROM scores
GROUP BY student_id;


-- =====================================================================
-- 18. 计算每个学生超过自己平均分的课程
-- 表：scores(student_id, course_id, score)
-- 题目：查询每个学生中，高于该学生平均分的课程成绩。
-- 考点：窗口平均值。
-- 答案：先用窗口函数算出每个学生自己的平均分，再筛出单科成绩高于个人平均分的记录，这比先聚合再回表更直接。
-- =====================================================================
WITH score_with_avg AS (
    SELECT
        *,
        AVG(score) OVER (PARTITION BY student_id) AS avg_score
    FROM scores
)
SELECT student_id, course_id, score, avg_score
FROM score_with_avg
WHERE score > avg_score;


-- =====================================================================
-- 19. 查询连续三天及以上高温记录
-- 表：weather(record_date, temperature)
-- 题目：找出温度连续 3 天大于等于 30 度的日期段。
-- 考点：gaps and islands。
-- 答案：连续高温和连续登录是同一类问题，都是先筛出目标日期，再用日期减行号构造连续段，最后找长度至少为 3 的连续区间。
-- =====================================================================
WITH hot_day AS (
    SELECT record_date
    FROM weather
    WHERE temperature >= 30
),
numbered_hot_day AS (
    SELECT
        record_date,
        ROW_NUMBER() OVER (ORDER BY record_date) AS rn
    FROM hot_day
),
hot_group AS (
    SELECT
        record_date,
        DATE_SUB(record_date, CAST(rn AS INT)) AS group_key
    FROM numbered_hot_day
)
SELECT
    MIN(record_date) AS start_date,
    MAX(record_date) AS end_date,
    COUNT(*) AS day_count
FROM hot_group
GROUP BY group_key
HAVING COUNT(*) >= 3;


-- =====================================================================
-- 20. 查询环比增长率
-- 表：monthly_sales(month, amount)
-- 题目：查询每个月销售额相较上月的增长率。
-- 考点：lag。
-- 答案：环比增长率要先取到上一期值，再用 `(当前值 - 上期值) / 上期值` 计算；`lag` 很适合处理这种相邻两期比较。
-- =====================================================================
WITH sales_with_previous AS (
    SELECT
        month,
        amount,
        LAG(amount) OVER (ORDER BY month) AS previous_amount
    FROM monthly_sales
)
SELECT
    month,
    amount,
    previous_amount,
    CASE
        WHEN previous_amount IS NULL OR previous_amount = 0 THEN NULL
        ELSE (amount - previous_amount) * 1.0 / previous_amount
    END AS month_over_month_rate
FROM sales_with_previous
ORDER BY month;


-- =====================================================================
-- 21. 查询中位数
-- 表：numbers(num)
-- 题目：查询 num 的中位数。
-- 考点：row_number、count over，奇偶统一处理。
-- 答案：求中位数的关键是先给数据排序并同时拿到总行数，然后统一处理奇数和偶数行数两种情况，最后对中间位置取平均。
-- =====================================================================
WITH ordered_num AS (
    SELECT
        num,
        ROW_NUMBER() OVER (ORDER BY num) AS rn,
        COUNT(*) OVER () AS cnt
    FROM numbers
)
SELECT AVG(num) AS median
FROM ordered_num
WHERE rn IN (
    CAST(FLOOR((cnt + 1) / 2) AS INT),
    CAST(FLOOR((cnt + 2) / 2) AS INT)
);


-- =====================================================================
-- 22. 查询每个用户首单信息
-- 表：orders(id, user_id, order_time, amount)
-- 题目：返回每个用户的第一笔订单。
-- 考点：row_number。
-- 答案：首单信息本质是分组取最早一条记录，按用户分区、按下单时间和 id 升序编号，再保留第一条即可。
-- =====================================================================
WITH ranked_order AS (
    SELECT
        *,
        ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY order_time, id) AS rn
    FROM orders
)
SELECT id, user_id, order_time, amount
FROM ranked_order
WHERE rn = 1;


-- =====================================================================
-- 23. 查询复购用户
-- 表：orders(id, user_id, order_time)
-- 题目：查询至少下过两单的用户。
-- 考点：group by、having。
-- 答案：复购用户通常定义为购买次数大于等于 2 的用户，所以直接按用户分组后用 `having count(*) >= 2` 就能筛出来。
-- =====================================================================
SELECT user_id
FROM orders
GROUP BY user_id
HAVING COUNT(*) >= 2;


-- =====================================================================
-- 24. 查询用户首次购买后的 30 天内消费金额
-- 表：orders(id, user_id, order_time, amount)
-- 题目：以用户首单时间为起点，统计首单后 30 天内的消费金额。
-- 考点：首单 CTE、自连接/关联过滤。
-- 答案：这题先求每个用户的首单时间，再把后续订单限定在首单后 30 天内，最后按用户汇总消费金额；关键是先把“首单”这个基准时间算准确。
-- =====================================================================
WITH first_order AS (
    SELECT
        user_id,
        MIN(order_time) AS first_order_time
    FROM orders
    GROUP BY user_id
)
SELECT
    o.user_id,
    SUM(o.amount) AS amount_in_30_days
FROM orders o
JOIN first_order f ON o.user_id = f.user_id
WHERE o.order_time >= f.first_order_time
  AND o.order_time < f.first_order_time + INTERVAL 30 DAY
GROUP BY o.user_id;


-- =====================================================================
-- 25. 查询每个用户相邻两次登录的最大间隔天数
-- 表：user_login(user_id, login_date)
-- 题目：对每个用户，计算相邻两次登录之间的最大间隔。
-- 考点：lag、日期差。
-- 答案：先用 `lag` 取出上一次登录日期，再算相邻两次登录的日期差，最后按用户求最大值，就能得到最大登录间隔天数。
-- =====================================================================
WITH login_with_previous AS (
    SELECT
        user_id,
        login_date,
        LAG(login_date) OVER (PARTITION BY user_id ORDER BY login_date) AS previous_login_date
    FROM user_login
)
SELECT
    user_id,
    MAX(DATEDIFF(login_date, previous_login_date)) AS max_login_gap
FROM login_with_previous
WHERE previous_login_date IS NOT NULL
GROUP BY user_id;
