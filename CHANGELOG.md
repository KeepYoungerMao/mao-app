# Changelog

所有对本项目的重要更改都将记录在此文件中。

格式基于 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.1.0/)

并且本项目遵循 [语义化版本](https://semver.org/lang/zh-CN/)

## [Unreleased]

## [1.0.0] - 2026-08-13

### Added

- 初始可用版本；
- 提供可用的数据库初始化脚本，包含MySQL和PostgreSQL；
- 提供完善的用户JWT认证授权功能；
- 提供系统操作日志记录功能；
- 提供系统运行指标信息记录功能；
- 提供流水号日志记录功能；
- 提供用户管理功能；
- 提供请求参数校验功能；
- 提供邮件发送功能；

### Changed

- 创建 BaseRepository 实现 CoroutineCrudRepository 和 自定义 PageableRepository，提升分页查询开发效率
- 使用 spring-security + oauth2-resource-server 提供 JWT认证授权功能；
- 使用 spring-validation对请求参数进行校验，自定义用户名、手机号、身份证号校验器；
- 使用 spring-mail启用邮件通知功能，在用户创建、用户密码重置时发生邮件通知；
- 使用 Caffeine对用户角色权限信息进行缓存，减少token体积；
- 用户角色更新，角色权限更新，用户状态更新，用户密码变更，都将立即对token生效；
- 将流水号traceId记录至上下文中，让其可以在协程中流转，并存入MDC，方便日志查找；
- 增加用户操作API；
- 增加服务信息API；
- 使用`@OperationLog`注解对操作日志进行细分，使用`filter`对操作日志进行记录；