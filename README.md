# SQLMan 
#### &emsp;&emsp;基于Java语言的数据库迭代升级版本化管理与自动执行插件，兼容主流关系型数据库，支持所有数据库方言的 DDL / DML / DCL，旨在让SQL脚本成为项目代码的一部分，让数据库升级也归入版本化管理中。

## 适用场景
&emsp;&emsp;项目开发中难免会伴随着数据库表结构的迭代升级和表数据更新维护，同时根据不同的开发时期又分为 LOCAL、ALPHA、BETA、PRE-PRODUCTION 以及 PRODUCTION 或更多阶段。
当一位开发者对数据库进行升级后，需要把数据库升级SQL脚本同步给所有开发者进行同样的升级，否则其他开发者将代码运行在未升级的数据库上将会得到意料之外的结果。
当开发者人数越多，其各自的LOCAL数据库版本同步以及数据状态的维护与管理也越发混乱。并且在严格情况下开发者不应该手动升级和维护数据库，应当让程序自动执行以避免人为操作的失误带来不必要的损失。

## 功能特性
* 兼容主流数据库
* 支持 DDL / DML / DCL
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

## 使用说明
1. 纯代码调用方式
```java
JdbcVersionManager sqlman = new JdbcVersionManager(dataSource);             // dataSource 为项目的数据源对象
sqlman.setDialectSupport(new MySQLDialectSupport("sqlman_schema_version")); // MySQL 方言，表名为 sqlman_schema_version
sqlman.setScriptResolver(new DruidScriptResolver(JdbcUtils.MYSQL));         // 使用 Druid SQL解析器
sqlman.setSourceProvider(new ClasspathSourceProvider("sqlman/**/*.sql"));   // 加载 sqlman/**/*.sql 路径的脚本
sqlman.setLoggerSupplier(new Slf4jLoggerSupplier(SqlLogger.Level.INFO));    // 采用 SLF4J 日志实现，日志级别为 INFO
sqlman.upgrade();                                                           // 执行升级流程
```
