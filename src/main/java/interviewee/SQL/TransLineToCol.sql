SELECT t.sno,
(CASE WHEN t.a>=60 THEN '及格'
WHEN t.a<60 THEN '不及格'
ELSE '无' END) '语文',
(CASE WHEN t.b>=60 THEN '及格'
WHEN t.b THEN '不及格'
ELSE '无' END) '数学'
FROM(
SELECT sno,
max(CASE WHEN SUBJECT = '语文' THEN score END) a,
max(CASE WHEN SUBJECT = '数学' THEN score END) b
FROM result GROUP BY sno) t