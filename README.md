# SQLMan [![](https://jitpack.io/v/core-lib/sqlman.svg)](https://jitpack.io/#core-lib/sqlman)
#### &emsp;&emsp;基于Java语言的关系型数据库迭代升级版本化管理与自动化执行插件，兼容主流关系型数据库，支持其方言的所有 DDL / DML / DCL 语法，旨在让SQL脚本成为项目代码的一部分，让数据库升级也纳入版本化管理之中。

## 适用场景
&emsp;&emsp;项目开发中难免会伴随着数据库表结构的迭代升级和表数据更新维护，同时根据不同的开发时期又分为 LOCAL、ALPHA、BETA、PRE-PRODUCTION 以及 PRODUCTION 或更多阶段。
当一位开发者对数据库进行升级后，需要把数据库升级SQL脚本同步给所有开发者进行同样的升级，否则其他开发者将代码运行在未升级的数据库上将会得到意料之外的结果。
当开发者人数越多，其各自的LOCAL数据库版本同步以及数据状态的维护与管理也越发混乱。并且在严格情况下开发者不应该手动升级和维护数据库，应当让程序自动执行以避免人为操作的失误带来不必要的损失。

## 功能特性
* 兼容主流数据库
* 支持全部 DDL / DML / DCL 语法
* 支持多 SQL 语句脚本 one-by-one 或 atomic 执行方式
* 可选脚本执行事务隔离级别
* 支持 Spring 自动配置
* 可集成全 Java 平台框架

## 执行流程
1. 获取数据库升级排它锁，这是一个逻辑锁，用于避免多个 SQLMan 实例同时升级同一个库。
2. 创建数据库版本记录表，如果不存在则创建，存在则不做任何处理。
3. 检测数据库当前最新版本号，即上次已执行的脚本版本号。
4. 加载比当前最新版本号更高的SQL脚本资源，并且按照脚本版本号从低到高排序。
5. 遍历SQL脚本资源，解析SQL脚本语句以执行，并插入当前最新版本记录。
6. 释放数据库升级排它锁。

## 安装步骤
1. 设置 jitpack.io 仓库
    ```xml
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
    ```

2. 添加 SQLMan 依赖
    ```xml
    <dependency>
        <groupId>com.github.core-lib</groupId>
        <artifactId>sqlman</artifactId>
        <version>v1.1.0</version>
    </dependency>
    ```
## Spring-Boot 集成
SQLMan 与 Spring-Boot 集成时，只需要通过配置即可，下面展示的是 SQLMan 所有配置项及其缺省值。
为了方便说明则将其都展示出来，如果缺省配置合适的话，就无需在项目配置文件中加入这些配置。
只需要在项目中加入SQLMan依赖以及在 resource/sqlman/ 目录或子目录下放置SQL脚本即可，SQL脚本的命名规则在文档后面会详细说明。
```yaml
# SQLMan 配置
sqlman:
  # 是否开启
  enabled: true
  # 管理器实现方式，目前支持 jdbc
  manager: jdbc
  # 当项目有多个数据源时，指定对应的数据源 bean 名称
  data-source: dataSource
  # 缺省事务隔离级别
  default-isolation: REPEATABLE_READ
  # 缺省执行模式
  default-mode: DANGER
  # 方言配置
  dialect:
    # 版本记录表表名
    table: schema_version
    # 方言类型
    type: MySQL
  # 脚本配置
  script:
    # 脚本资源提供器
    provider: classpath
    # 脚本位置的 ANT 路径表达式
    location: sqlman/**/*.sql
    # 脚本解析器
    resolver: druid
    # SQL脚本方言
    dialect: MySQL
    # SQL脚本字符集
    charset: UTF-8
    # 命名配置
    naming:
      # SQL脚本命名策略
      strategy: standard
  # 日志配置
  logger:
    # 日志提供器
    supplier: slf4j
    # 日志级别
    level: INFO
```

## Spring-MVC 集成
SQLMan 与 Spring-MVC 集成核心的 bean 为最后面的 SqlVersionManager 并且需要注入对应的数据源 bean 和指定其初始化方法为 upgrade ，其余的注入均有其缺省值，
且缺省值如文中所示。

```xml
<!-- MySQL 数据库方言，表名为 schema_version -->
<bean id="sqlDialectSupport" class="io.sqlman.dialect.MySQLDialectSupport">
    <property name="table" value="schema_version"/>
</bean>

<!-- 标准SQL脚本命名策略 -->
<bean id="sqlNamingStrategy" class="io.sqlman.naming.StandardNamingStrategy"/>

<!-- 加载 sqlman/**/*.sql 路径的脚本，使用标准SQL脚本命名策略 -->
<bean id="sqlSourceProvider" class="io.sqlman.source.ClasspathSourceProvider">
    <property name="scriptLocation" value="sqlman/**/*.sql"/>
    <property name="namingStrategy" ref="sqlNamingStrategy"/>
</bean>

<!-- 使用 Druid SQL解析器，方言为 MySQL，字符集为 UTF-8 -->
<bean id="sqlScriptResolver" class="io.sqlman.script.DruidScriptResolver">
    <property name="dialect" value="MySQL"/>
    <property name="charset" value="UTF-8"/>
</bean>

<!-- 采用 SLF4J 日志实现，日志级别为 INFO -->
<bean id="sqlLoggerSupplier" class="io.sqlman.logger.Slf4jLoggerSupplier">
    <property name="level" value="INFO"/>
</bean>

<!-- 执行升级流程，注意这里需要有 init-method="upgrade" -->
<bean id="sqlVersionManager" class="io.sqlman.version.JdbcVersionManager" init-method="upgrade">
    <property name="dataSource" ref="dataSource"/>
    <property name="dialectSupport" ref="sqlDialectSupport"/>
    <property name="sourceProvider" ref="sqlSourceProvider"/>
    <property name="scriptResolver" ref="sqlScriptResolver"/>
    <property name="loggerSupplier" ref="sqlLoggerSupplier"/>
</bean>
```

## 代码调用
当项目采用的框架不是基于 Spring 家族的，可以参考与 Spring-MVC 集成的思路或采用纯代码调用方式来集成。同样的，只有数据源参数是必选的，其余都有其缺省值，且缺省值如下面代码所示。

```java
// dataSource 为项目的数据源对象
JdbcVersionManager sqlman = new JdbcVersionManager(dataSource);

// MySQL 方言，表名为 schema_version
sqlman.setDialectSupport(new MySQLDialectSupport("schema_version"));

// 加载 sqlman/**/*.sql 路径的脚本，使用标准SQL脚本命名策略
sqlman.setSourceProvider(new ClasspathSourceProvider("sqlman/**/*.sql", new StandardNamingStrategy()));

// 使用 Druid SQL 解析器，方言为 MySQL，字符集为 UTF-8
sqlman.setScriptResolver(new DruidScriptResolver(JdbcUtils.MYSQL, "UTF-8"));

// 采用 SLF4J 日志实现，日志级别为 INFO
sqlman.setLoggerSupplier(new Slf4jLoggerSupplier(SqlLogger.Level.INFO));

// 执行升级流程
sqlman.upgrade();
```

## 命名规则
SQL脚本需要遵循一定的命名规则以配合SQLMan进行版本高低的区分以及执行脚本时采用的指令。

插件内部提供了一个标准的SQL脚本资源命名策略解析器（StandardNamingStrategy），其规则如下：
1. 以 v 开头，不区分大小写。（必选）
2. 紧跟着任意级版本数字，以 . 分隔，例如 1.0.0、2.4.13.8 或 2019.06.13 等。（必选）
3. 指定脚本执行指令列表，以 - 为前缀，例如 -ATOMIC、-READ_COMMITTED 或 -REPEATABLE_READ 等。（可选）
4. 添加脚本备注，以 ! 为前缀，例如 !add-some-column、!drop-useless-tables 等。（可选）
5. 以 .sql 为后缀。（必选）

命名例子：

| SQL脚本名称 | 规则解释 |
| :------- | :------- |
|  v1.0.0.sql                                        | 只有版本号 |
|  v2.4.13.8-ATOMIC.sql                              | 版本号 + 一个指令 |
|  v2.4.13.8-ATOMIC-REPEATABLE_READ.sql              | 版本号 + 多个指令 |
|  v2019.06.13!drop-useless-tables.sql               | 版本号 + 备注 |
|  v2019.06.13-REPEATABLE_READ!init-admin-data.sql   | 版本号 + 指令 + 备注 |

标准命名策略中版本号的高低对比基于版本数字的分段对比，并不是字符串对比。例如：
* 1.0.0 比 0.0.1 高
* 1.2.4 比 1.2.3 高
* 2.3.1 比 2.2.4 高
* 3.23.5.1 比 3.23.5 高
* 2019.06.13 比 2019.06.08 高

## 指令说明

指令在脚本命名中不区分大小写，目前支持的指令及其解释如下：

| 指令名称 | 指令含义 | 指令说明 | 缺省值 |
| :------- | :------- | :------- | :----- |
| ATOMIC | 原子性执行 | 当SQL脚本包含多条SQL语句时，将其置于同一个事务中执行。| 非原子性执行，即一条SQL语句一个事务。|
| READ_UNCOMMITTED | 读未提交隔离级别 | 设置SQL语句执行事务的隔离界别为读未提交 | 依赖数据源的事务隔离级别 |
| READ_COMMITTED | 读已提交隔离级别 | 设置SQL语句执行事务的隔离界别为读已提交 | 依赖数据源的事务隔离级别 |
| REPEATABLE_READ | 可重复读隔离级别 | 设置SQL语句执行事务的隔离界别为可重复读 | 依赖数据源的事务隔离级别 |
| SERIALIZABLE | 串行化隔离级别 | 设置SQL语句执行事务的隔离界别为串行化 | 依赖数据源的事务隔离级别 |
| SAFETY | 安全模式 | 当SQL脚本设置为安全模式，即每条SQL语句执行前会自动备份被操作的表 | 危险模式 |
| DANGER | 危险模式 | 当SQL脚本设置为安全模式，即每条SQL语句执行前不自动备份被操作的表 | 危险模式 |

其中每个SQL脚本的隔离级别只能选取一种，通常情况下依赖隔离级别的脚本需要原子性执行即通过-ATOMIC指令来指定，缺省为one-by-one模式。
同理执行模式也只能在危险模式中选取一种，备份表的名称为 原表名_bak_脚本_版_本_号$语句下标

## 原子模式
* 缺省模式的多SQL语句脚本在执行过程中，当其中某条SQL执行失败后，程序下次启动时将会从该脚本的**失败SQL**开始。
* 原子模式的多SQL语句脚本在执行过程中，当其中某条SQL执行失败后，程序下次启动时将会从该脚本的**首条SQL**开始。

## 执行模式
* 安全模式：即每条SQL语句执行前**会自动备份**被操作的表
* 危险模式：即每条SQL语句执行前**不自动备份**被操作的表

## 注意事项 
由于部分数据库不支持 DDL 语句的失败回滚，例如 MySQL ，所以当一个原子性多SQL语句脚本中包含有 DDL 语句时，
其后面的SQL语句执行失败且进行整体回滚后，其实已成功的 DDL 并没有真正回滚，又由于是原子性脚本，所以程序下次启动时会从该脚本的首条SQL开始执行，
当执行到上次已成功但无法回滚的 DDL 时会失败，或重复执行，为了避免这种情况请尽量将 DDL 语句单独作为一个脚本，或采用缺省的 one-by-one 模式。

## 支持数据库
* MySQL
* Oracle
* SQLServer
* SQLite

后续将会增加更多数据库的支持。

## 版本记录
* v1.1.0
    1. 支持表自动备份
* v1.0.6
    1. Spring Bean 命名规范
* v1.0.5
    1. 第一个正式版本
    2. 增加 README

## 协议声明
[Apache-2.0](http://www.apache.org/licenses/LICENSE-2.0)

## 联系作者
QQ 646742615 不会钓鱼的兔子
