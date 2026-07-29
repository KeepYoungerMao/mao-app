<div style="text-align: center">

# 🚀 MAO-APP

**基于 Kotlin 与 Spring WebFlux 的响应式高性能后端服务**

[English](README.md) | [简体中文](README.zh.md)

</div>

---

## 📌 项目简介

本项目是一个采用最新技术栈打造的**全响应式（Reactive）后端服务框架/系统**。旨在探讨与实践现代响应式架构、端到端全链路加密认证以及高性能数据库交互。

项目已完全开源，欢迎大家 Star、Fork 交流与提 Issue 切磋技术！

### ✨ 核心特性
* **前沿技术栈**：基于最新 JDK 25 与 Kotlin 2.4+，探索极速构建体验。
* **全响应式架构**：基于 Spring WebFlux + R2DBC，实现低延迟、高并发的非阻塞 I/O 处理。
* **端到端安全认证**：
  * 基于 **RSA-OAEP (SHA-256)** 实现前端密码加盐加密，拒绝明文传输。
  * 基于 **JWT (Bearer Token)** + **Refresh Token** 实现无状态双 Token 续签机制。

---

## 🛠️ 技术栈与依赖

### 核心架构

[![jdk-25]][jdk-25-home]
[![kotlin]][kotlin-home]
[![gradle]][gradle-home]
[![spring-boot]][spring-boot-home]
[![webflux]][webflux-home]
[![r2dbc]][r2dbc-home]
[![license]][license-home]

[jdk-25]: https://img.shields.io/badge/JDK-25-blue?logo=openjdk
[jdk-25-home]: https://openjdk.org/projects/jdk/25/
[kotlin]: https://img.shields.io/badge/Kotlin-2.4.0-7F52FF?logo=kotlin
[kotlin-home]: https://kotlinlang.org
[gradle]: https://img.shields.io/badge/Gradle-9.6.1-02303A?logo=gradle
[gradle-home]: https://gradle.org
[spring-boot]: https://img.shields.io/badge/SpringBoot-4.1.0-6DB33F?logo=springboot
[spring-boot-home]: https://spring.io/projects/spring-boot
[webflux]: https://img.shields.io/badge/WebFlux-Reactive-6DB33F?logo=spring
[webflux-home]: https://docs.spring.io/spring-framework/reference/web/webflux.html
[r2dbc]: https://img.shields.io/badge/R2DBC-Reactive-6DB33F?logo=spring
[r2dbc-home]: https://r2dbc.io/
[license]: https://img.shields.io/badge/License-MIT-yellow.svg
[license-home]: https://opensource.org/licenses/MIT

### 依赖版本清单

| 类别           | 依赖组件                                | 版本                |
|:-------------|:------------------------------------|:------------------|
| **语言与核心**    | `kotlin-reflect` / `kotlin-stdlib`  | `2.4.0`           |
| **响应式异步**    | `reactor-kotlin-extensions`         | `1.3.1`           |
|              | `kotlinx-coroutines-reactor`        | `1.10.2`          |
| **Web & 存储** | `spring-boot-starter-webflux`       | `4.1.0`           |
|              | `spring-boot-starter-data-r2dbc`    | `4.1.0`           |
|              | `r2dbc-mysql` / `mysql-connector-j` | `1.4.2` / `9.7.0` |
| **工具与对象映射**  | `mappie-api`                        | `2.4.0-2.4.2`     |
|              | `jackson-module-kotlin`             | `3.1.4`           |

---

## 🔐 认证与安全设计

系统采用了 **RSA-OAEP 动态公钥加密 + 双 Token 认证** 的安全流程：

### 1. 登录交互时序图

```text
[Client/前端]                                    [后端认证服务]
     |                                                 |
     |----- 1. GET /api/v1/auth/public-key --------->|
     |<---- 2. 返回 RSA 公钥 (PEM 格式) ---------------|
     |                                                 |
  [客户端加密: password + ":" + timestamp]
     |                                                 |
     |----- 3. POST /api/v1/auth/token -------------->|
     |<---- 4. 验证并返回 TokenResponse ---------------|
     |                                                 |
  [本地存储 accessToken & refreshToken]
     |                                                 |
     |----- 5. 携带 Header: Authorization 请求 API ->|
```
### 2. 认证 API 规范
#### 🔑 请求 RSA 公钥
- 请求路径： `GET` `/api/v1/auth/public-key`
- 响应示例：
```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "publicKey": "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoQXidHzE..."
  }
}
```
#### 🔓 用户登录获取 Token
- 请求路径： `POST` `/api/v1/auth/token`
- 请求头： `Content-Type: application/json`
- 请求体参数说明：

| 字段名称      | 类型     | 必填 | 说明                                                   |
|:----------|:-------|:---|:-----------------------------------------------------|
| username  | String | 是  | 用户名                                                  |
| password  | String | 是  | 使用 `${password}:${timestamp}` 拼接后经 RSA 加密的 Base64 密文 |
| timestamp | Long   | 是  | 客户端请求生成时的时间戳（毫秒）                                     |

- 请求体示例：
```json
{
  "username": "admin",
  "password": "k3f8S/X9A...== ",
  "timestamp": 1784882384851
}
```
- 响应示例：
```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "accessToken": "eyJhbGciOiJSUzI1NiIs...",
    "refreshToken": "d8e7c1f0-...",
    "expiresIn": 7200,
    "tokenType": "Bearer"
  }
}
```
#### 🔄 刷新 Access Token
- 请求路径： `POST` `/api/v1/auth/token/refresh`
- 请求体示例：
```json
{
  "refreshToken": "d8e7c1f0-..."
}
```
- 响应示例：
```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "accessToken": "eyJhbGciOiJSUzI1NiIs...",
    "refreshToken": "d8e7c1f0-...",
    "expiresIn": 7200,
    "tokenType": "Bearer"
  }
}
```
#### 🛡️ 受保护接口请求说明
对于所有受保护的业务接口，须在 HTTP 请求头中携带 `accessToken`：
```http request
Authorization: Bearer <accessToken>
```
### 3. 前端加密实现规范 (Web Crypto API)
为了保障传输安全，前端禁止传输明文密码。需使用原生 Web Crypto API 进行 RSA-OAEP 加密。
- 加密算法参数：
  - 算法名称： `RSA-OAEP`
  - 密钥长度： `2048` bits
  - Hash算法： `SHA-256`
  - 待加密文本规则： `${password}:${timestamp}` (Example: admin123:1784882384851)
```javascript
/**
 * 将 PEM 或裸 Base64 格式的公钥转换为 ArrayBuffer
 */
function pemToArrayBuffer(pemPublicKey) {
  const b64 = pemPublicKey
    .replace(/-----BEGIN PUBLIC KEY-----/g, '')
    .replace(/-----END PUBLIC KEY-----/g, '')
    .replace(/[\r\n\s]/g, '');

  const binaryString = window.atob(b64);
  const bytes = new Uint8Array(binaryString.length);
  for (let i = 0; i < binaryString.length; i++) {
    bytes[i] = binaryString.charCodeAt(i);
  }
  return bytes.buffer;
}

/**
 * 使用 RSA-OAEP (SHA-256) 加密密码
 * 
 * @param {string} pemPublicKey 后端获取到的 RSA 公钥
 * @param {string} plainPassword 明文密码
 * @param {number} timestamp 当前时间戳 (Date.now())
 * @returns {Promise<string>} Base64 格式的密文
 */
async function encryptPassword(pemPublicKey, plainPassword, timestamp) {
  const payload = `${plainPassword}:${timestamp}`;
  const keyBuffer = pemToArrayBuffer(pemPublicKey);

  const cryptoKey = await window.crypto.subtle.importKey(
    "spki",
    keyBuffer,
    {
      name: "RSA-OAEP",
      hash: "SHA-256"
    },
    false,
    ["encrypt"]
  );

  const encodedPayload = new TextEncoder().encode(payload);
  const encryptedBuffer = await window.crypto.subtle.encrypt(
    { name: "RSA-OAEP" },
    cryptoKey,
    encodedPayload
  );

  return window.btoa(String.fromCharCode(...new Uint8Array(encryptedBuffer)));
}
```
## 🚀 快速开始

### 环境要求
- **JDK 25** 或更高版本
- **MySQL 8.x** / **PostgreSQL 18+** (启用 R2DBC 响应式驱动)
- **Gradle 9.x**
###本地运行步骤
#### 1. 克隆项目
```shell
git clone https://github.com/KeepYoungerMao/mao-app.git
cd mao-app
```
#### 2. 配置数据库
修改 `src/main/resources/application.yml` 中的数据库连接信息：
```yaml
spring:
  r2dbc:
    url: r2dbc:mysql://localhost:3306/your_db
    username: root
    password: your_password
```
#### 3. 编译并启动
```shell
./gradlew bootRun
```
## 💬 技术交流与贡献

这是一个开源共享与技术交流的项目。如果你有任何想法、改进建议或者发现了 Bug：

- 欢迎提交 Issues 进行讨论。
- 欢迎提交 Pull Requests 贡献代码。

如果你觉得这个项目对你有帮助，欢迎点个 ⭐ Star 支持一下！

## 📄 License

本项目基于 **MIT License** 开源。