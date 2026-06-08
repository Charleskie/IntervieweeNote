-- 题目：把学生成绩表按学号行转列，并把语文、数学成绩转换成“及格/不及格/无”三个状态。
-- 答案：先用条件聚合把同一个学生不同科目的分数转成列，再在最外层用 case when 按分数区间映射成结果标签；这种写法适合字段固定的行转列场景。
SELECT t.sno,
(CASE WHEN t.a>=60 THEN '及格'
WHEN t.a<60 THEN '不及格'
ELSE '无' END) '语文',
(CASE WHEN t.b>=60 THEN '及格'
WHEN t.b<60 THEN '不及格'
ELSE '无' END) '数学'
FROM(
SELECT sno,
max(CASE WHEN SUBJECT = '语文' THEN score END) a,
max(CASE WHEN SUBJECT = '数学' THEN score END) b
FROM result GROUP BY sno) t
