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



### 用户ID获取

在websocket注册时，必须使用id进行注册

+ 使用GET请求
+ 携带token

请求后直接返回用户ID，类型为Long



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

| 状态          | 说明     |
| ------------- | -------- |
| NOT_CREATED   | 未创建   |
| OFFLINE       | 离线     |
| LEAVE_UNUSED  | 闲置     |
| DEEP_SEARCH   | 深度搜索 |
| LISTEN        | 监听     |
| UPDATE_AVATAR | 更新头像 |



### Crawler终止

强行停止当前的Crawler

+ 使用DELETE请求
+ 携带token



### Crawler深度搜索

进入深度搜索模式，搜索设定的好友列表并封装所需的数据，然后发送到Python进行数据处理

+ 使用DELETE请求
+ 携带token

执行成功后返回id

前端应根据Websocket进行信息的更新



### Crawler实时监听

将Crawler状态设置为实时监听模式，剩下的步骤将会由定时任务自动执行

+ 使用GET请求
+ 携带token

执行成功后返回id

前端应根据Websocket进行信息的更新



### Crawler头像更新

更新头像的数据表

+ 使用GET请求
+ 携带token

执行成功后返回id，数据库表会自动更新完成



## WebSocket API

与2.0版本一样，使用WebSocket来实现前后端的通信，但是在这个版本中，会固定返回的信息，不再传递非必要的信息，而是在关键的时间节点发送必要的信息，以触发前端必须事件，在用户登录时，应该直接开启websocket，并在发生错误时强制重启



### 通信过程

从用户首次使用开始模拟，登录平台后，websocket打开，发送**连接成功**提醒，此时前端应该请求Crawler的状态，通过Crawler状态来决定显示的内容，这里假设是`NOT_CREATED`的状态，此时用户点击Crawler登录，同时更新状态为`OFFLINE`，在扫码登录阶段，如果没有检测到登录，应该向前端发送**状态更新**，如果检测到登录也发送**状态更新**，此时Crawler进入`LEAVE_UNUSED`状态，进入模式选择界面，此时用户可以选择深度搜索或者实时监听模式，这里从深度搜索入手，点击深度搜索后，深度搜索启动后发送**状态更新**，深度搜索结束后再次发送**状态更新**，同时在发送一个**数据更新**，此时前端应重新请求数据，深度搜索后用户选择实时监听，点击后，发送**状态更新**，当监听到目标时，发送**数据更新**，告诉前端更新数据，用户使用完毕，终止Crawler，此时发送**状态更新**

**注意：**头像更新是独立于其他状态的一个单独状态，与其他状态互斥，所以只能在闲置状态下调用，在闲置状态下，点击后，发送**状态更新**，头像采集完成后，发送**头像更新**，前端对头像数据进行更新，再发送**状态更新**，这是头像更新的周期



### 深度搜索过程

深度搜索过程中，用户应该知晓其执行过程，在2.0版本中，通过发送每一个人当前探测动态数量来实现百分比显示，但是在3.0版本中，对于Crawler的大幅性能优化，不应再以动态条目为单位，而是以学生为单位来显示进度，所以会发送**学生计数**来计算进度，计数器作为数据部分发送



### 服务器连接

使用WebSocket连接`ws://api.pi1grim.top/ea/api/v3/websocket/{id}`，来建立与服务器的连接，这里的`id`必须是用户id



### 命令表

发送时会获得类似响应体的结构，结构中附带code，message这两项，可以用以判断命令的作用

| 响应码 | 响应信息 | 说明                                                    | 载荷   |
| ------ | -------- | ------------------------------------------------------- | ------ |
| 3000   | 连接成功 | 表示与服务器成功建立连接                                | 用户id |
| 3005   | 更新状态 | 需要跟服务器请求当前Crawler的状态                       | 用户id |
| 3010   | 更新数据 | 需要跟服务器请求新的分析数据                            | 用户id |
| 3015   | 更新头像 | 需要跟服务器请求新的头像数据                            | 用户id |
| 3020   | 学生计数 | 用以计算当前深度搜索百分比，从1开始，到当前搜索的学生数 | 计数器 |



## 头像 API



### 头像获取

+ 使用GET请求
+ 携带token

返回stuId与头像URL的映射
