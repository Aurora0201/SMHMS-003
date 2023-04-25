[TOC]

# 学生心理健康管理系统

2023网络挑战赛参赛作品，学生心理健康管理系统3.0版本，基于2.0版本的优化和改进，对后端进行重构，对之前冗余的流程进行简化，提高性能，增加新的功能，搭建一个低耦合，高扩展的系统



# 需求分析

## 用户注册

对于之前繁琐的流程进行简化，合并config和user表，进一步简化CRUD操作，要求如下：

+ 使用POST请求

| 字段     | 要求                        | 说明             | 类型   |
| -------- | --------------------------- | ---------------- | ------ |
| username | 至少大于6个字符小于20个字符 | 用户登录时用户名 | String |
| password | 至少8位，包含数字与字母     | 用户登录时密码   | String |
| mail     | 符合邮箱的格式              | 发送报告时邮箱   | String |
| phone    | 符合中国手机号格式          | 发送提醒时手机号 | String |

后端实现：

+ 对字段判空，存在为空抛出异常
+ 对用户名判重，存在则抛出异常
+ 插入数据库，成功返回用户名
