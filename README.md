[TOC]

# 学生心理健康管理系统

2023网络挑战赛参赛作品，学生心理健康管理系统3.0版本，基于2.0版本的优化和改进，对后端进行重构，对之前冗余的流程进行简化，提高性能，增加新的功能，搭建一个低耦合，高扩展的系统



# 需求分析

## 用户 API

### 用户注册

对于之前繁琐的流程进行简化，合并config和user表，进一步简化CRUD操作，要求如下：

+ 使用POST请求

**请求体**

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



### 用户登录

与之前的流程相同，但是对Redis中存储的数据进行升级，对每个用户提供分布式Session，用以跨设备全局共享变量

+ 使用POST请求

**请求体**

| 字段     | 要求                        | 说明             | 类型   |
| -------- | --------------------------- | ---------------- | ------ |
| username | 至少大于6个字符小于20个字符 | 用户登录时用户名 | String |
| password | 至少8位，包含数字与字母     | 用户登录时密码   | String |

后端实现：

+ 判断是否用户已经登录，如果已经登录则之间返回旧的Token，防止token覆盖导致Session丢失
+ 先判断用户是否存在，不存在则抛出异常
+ 判断账号密码是否匹配，不匹配抛出异常
+ 往Redis中存入属于该用户的Session对象



### 用户配置信息修改

采用多次修改一次提交的方法简化API接口

+ 使用POST请求
+ 在headers携带token

**请求体**

| 字段     | 要求           | 说明             | 类型   |
| -------- | -------------- | ---------------- | ------ |
| password | 符合密码要求   | 用户登录时密码   | String |
| mail     | 合法邮箱格式   | 发送报告时邮箱   | String |
| phone    | 符合手机号要求 | 发送提醒时手机号 | String |
| step     | 0 < step < 255 | 深度搜索时步长   | Byte   |

后端实现：

+ 判断是否存在空字段
+ 更新数据库，返回新的数据



### 用户配置信息获取

+ 使用GET请求
+ 在headers中携带token



后端实现：

+ 从Redis中获取用户的信息
+ 返回到前端



## Session API

对于每一个用户，提供分布式共享Session对象来实现不同设备之间的信息共享，每个Session与token进行绑定，意味着Session仅在用户登录期间有效，并且建议存储的数据应该是更新频率较低或者根本不更新的数据，防止读取到脏数据。对于用户来说Session其实就是一个Map，可以通过Key来获取Value



### 存储数据

+ 使用PUT请求
+ 携带token

**查询字符串**

| 字段  | 要求   | 说明      | 类型   |
| ----- | ------ | --------- | ------ |
| key   | 不为空 | 作为key   | String |
| value | 不为空 | 作为value | String |

后端实现：

+ 从headers中获取token
+ 从tRedis中获取session
+ 往session中存入数据
+ 更新Redis，返回key



### 获取数据

+ 使用GET请求
+ 携带token

**查询字符串**

| 字段 | 要求   | 说明    | 类型   |
| ---- | ------ | ------- | ------ |
| key  | 不为空 | 作为key | String |

直接返回key对应的value，需要注意的是，**key不存在的时候返回null值**



## 学生表 API

之前的版本中，只提供了添加和选择的功能，这个版本会提供更多的操作，给用户更多的自定义空间



### 添加学生

+ 使用POST请求
+ 携带token

**请求体**

| 字段   | 要求   | 说明       | 类型   |
| ------ | ------ | ---------- | ------ |
| number | 不为空 | 好友QQ号   | String |
| notes  | 不为空 | 好友备注名 | String |

后端实现：

+ 对QQ号进行判重，不允许添加重复的用户
+ 插入数据库，添加的学生信息



### 获取学生信息

+ 使用GET请求
+ 携带token





### 修改学生信息

+ 使用PUT请求
+ 携带token

**请求体**

| 字段   | 要求   | 说明       | 类型   |
| ------ | ------ | ---------- | ------ |
| number | 不为空 | 好友QQ号   | String |
| notes  | 不为空 | 好友备注名 | String |
