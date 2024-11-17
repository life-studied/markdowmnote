---
create: 2023-07-08
---
## 案例exercise-course

```mysql
-- 1. 在数据库exercise中创建课程表stu_course，包含字段课程编号(number)，类型为整数，长度为
-- 11，是主键，自增长，非空、课程名称(name)，类型为字符串，长度为20，非空、学分(score)，类型
-- 为浮点数，小数点后面保留2位有效数字， 长度为5，非空
CREATE DATABASE IF NOT EXISTS exercise DEFAULT CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI;

USE exercise;

CREATE TABLE IF NOT EXISTS stu_course(
	`number` INT(11) AUTO_INCREMENT PRIMARY KEY NOT NULL COMMENT '课程编号',
	name varchar(20) NOT NULL COMMENT '课程名称',
	score double(5,2) NOT NULL COMMENT '学号'
)ENGINE=InnoDB CHARSET=UTF8 COMMENT '课程表';

-- 向课程表中插入一条数据
INSERT INTO stu_course
VALUES(1,'Maths',60.50);

-- 将课程表重命名为course
ALTER TABLE stu_course RENAME AS course;

-- 在课程表中添加字段学时(time)，类型为整数，长度为3，非空
ALTER TABLE course ADD `time` INT(3) NOT NULL COMMENT '学时';

-- 修改课程表学分类型为浮点数，小数点后面保留1位有效数字，长度为3，非空
ALTER TABLE course MODIFY score double(3,1) NOT NULL COMMENT '学分';
```

