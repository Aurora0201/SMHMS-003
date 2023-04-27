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



### 添加学生信息

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

| 字段     | 要求                   | 说明                           | 类型    |
| -------- | ---------------------- | ------------------------------ | ------- |
| id       | 发送原始id，不允许修改 | 学生唯一标识符                 | Long    |
| number   | 不为空                 | 好友QQ号                       | String  |
| notes    | 不为空                 | 好友备注名                     | String  |
| selected | false或true            | false -> 未选中 / true -> 选中 | Boolean |

附：请求体格式

```json
{
  "students": [
    {
      "id": 0,
      "number": "string",
      "notes": "string",
      "selected": true
    },
	{
      "id": 1,
      "number": "string",
      "notes": "string",
      "selected": true
	}
  ]
}
```



### 删除学生信息

+ 使用DELETE请求
+ 携带token

**请求体**

| 字段     | 要求                     | 说明                           | 类型    |
| -------- | ------------------------ | ------------------------------ | ------- |
| id       | 发送原始数据，不允许修改 | 学生唯一标识符                 | Long    |
| number   | 发送原始数据，不需要修改 | 好友QQ号                       | String  |
| notes    | 发送原始数据，不需要修改 | 好友备注名                     | String  |
| selected | 发送原始数据，不需要修改 | false -> 未选中 / true -> 选中 | Boolean |

附：请求体格式

```json
{
  "students": [
    {
      "id": 0,
      "number": "string",
      "notes": "string",
      "selected": true
    },
	{
      "id": 1,
      "number": "string",
      "notes": "string",
      "selected": true
	}
  ]
}
```



## Crawler API

在之前的2.0版本中，我们使用了工具类`OriginUtils`来获取数据，在测试阶段问题频出，同时存在致命性问题——无法同时为多位用户同时服务，这是设计时留下的巨大缺陷，工具类使用静态方法来提供服务，但是其与业务功能高度耦合，同时由于selenium包的特性，最终导致了2.0版本中的众多问题，所以在3.0版本中我们使用了`Crawler`组件来替换工具类，在这个版本中我们最大的改进就是可以同时为多个用户提供服务，我们使用`简单工厂`来生产`Crawler`对象，每一个用户都有属于自己的`Crawler`对象，在此之上，我们提供了更多的功能，比如垃圾回收，垃圾回收可以极大的减小服务器的内存压力，自动回收闲置的`Crawler`对象，为下一个用户的使用腾出空间，登录状态记录，`Crawler`不需要像工具类一样在切换模式时需要重复登录，也不需要使用QQ号来验证，只需要登录一次即可



### 生命周期

每一个`Crawler`对象被创建出来后都会有属于自己的生命周期，如下图

![crawler-lifecycle.drawio](https://cdn.jsdelivr.net/gh/Aurora0201/ImageStore@main/img/upgit_20230427_1682567547.png)

在此基础上，为了让用户更直观的了解当前`Crawler`的情况，我们设计了下面的几种状态

![crawler-status.drawio](https://cdn.jsdelivr.net/gh/Aurora0201/ImageStore@main/img/upgit_20230427_1682567476.png)

状态转换的说明， 在用户首次使用或者使用后Crawler被回收时，此时Crawler的状态为**未创建**，当用户点击登录按钮后，后端会创建`Crawler`对象，此时进入**离线**状态，在用户扫描登录，并且登录成功后，会进入**闲置**状态，当用户点击深度搜索或者监听按钮时，就会进入对应的状态



### 前端设计

在理想的路线下应该是这样的

![crawler-front-process.drawio](https://cdn.jsdelivr.net/gh/Aurora0201/ImageStore@main/img/upgit_20230427_1682567530.png)

### Crawler登录

创建并注册，然后激活登录程序

+ 使用GET请求
+ 携带token

后端实现：

+ 创建Crawler并初始化
+ 将Crawler注册到容器中
+ 调用getQuick方法获取登录二维码，返回二进制流到前端
+ 开始检测登录
+ 登录成功后将状态进行更新



### Crawler状态

返回当前Crawler的状态

+ 使用GET请求
+ 携带token

**返回的状态就是上面的五种状态**，在数据层面的表现形式为下表

| 状态         | 说明     |
| ------------ | -------- |
| NOT_CREATED  | 未创建   |
| OFFLINE      | 离线     |
| LEAVE_UNUSED | 闲置     |
| DEEP_SEARCH  | 深度搜索 |
| LISTEN       | 监听     |



### Crawler终止

强行停止当前的Crawler

+ 使用DELETE请求
+ 携带token

