# Mao-App

自测项目

## 使用架构

`jdk-25`, `kotlin-2.4.0`, `gradle`, `spring-boot-4.1.0`, `web-flux`

库使用：
- `kotlin-reflect`:`2.4.0`
- `kotlin-stdlib`:`2.4.0`
- `reactor-kotlin-extensions`:`1.3.1`
- `kotlin-coroutines-reactor`:`1.10.2`
- `spring-boot-starter-data-r2dbc`:`4.1.0`
- `spring-boot-starter-webflux`:`4.1.0`
- `mysql-connector-j`:`9.7.0`
- `r2dbc-mysql`:`1.4.2`
- `mappie-api`:`2.4.0-2.4.2`
- `jackson-module-kotlin`:`3.1.4`

## 用户登录步骤

```text
[cluster]                        [后端认证服务]
     |                                    |
     |----- 1. 请求公钥 (GET) ------------>|
     |<---- 2. 返回 RSA 公钥 (PEM) --------|
     |                                    |
  [对密码加密: password + ":" + timestamp]
     |                                    |
     |----- 3. 提交登录 (POST) ---------->|
     |<---- 4. 返回 TokenResponse --------|
     |                                    |
  [本地存储 accessToken & refreshToken]
     |                                    |
     |----- 5. 携带 Bearer Token 请求 ---->|
```

### 请求公钥
- **请求路径:** `POST` `/api/v1/auth/public-key`
- **请求头:** `Content-Type: application/json`
- **响应示例:**
```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "publicKey": "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoQXidHzE..."
  }
}
```

### 请求token
- **请求路径:** `POST` `/api/v1/auth/token`
- **请求头:** `Content-Type: application/json`
- **请求体示例:**
```json
{
  "username": "admin",
  "password": "k3f8S/X9A...== (RSA 加密后的 Base64 密文)",
  "timestamp": 1784882384851
}
```
- **请求参数说明：**

| 字段名称      | 类型     | 必填 | 说明                                       |
|:----------|:-------|:---|:-----------------------------------------|
| username  | String | Y  | 用户名                                      |
| password  | String | Y  | 使用`${password}:${timestamp}`进行拼接再加密后的字符串 |
| timestamp | String | Y  | 请求时时间戳                                   |


- **响应示例:**
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
### 刷新token
- **请求路径:** `POST` `/api/v1/auth/token/refresh`
- **请求头:** `Content-Type: application/json`
- **请求体示例:**
```json
{
  "refreshToken": "eyJhbGciOiJSUzI1NiIs..."
}
```
- **响应示例:**
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
### 携带Token进行接口请求
对于所有需要权限校验的业务接口（非 /api/v1/auth/** 路径），
必须在请求头（Request Header）中携带 accessToken。
- **请求头格式：**
```http request
Authorization: Bearer <accessToken>
```
### 前端密码加密实现规范
为了保障密码生命周期安全，禁止直接传输明文密码。
前端需使用浏览器原生的 Web Crypto API 进行 RSA-OAEP 加密。
- 算法参数配置：
  - 算法名称: `RSA-OAEP`
  - 密钥长度: `2048` 位
  - 主哈希算法 (Hash): `SHA-256`
  - 掩码生成函数哈希 (MGF1 Hash): `SHA-256`
  - 待加密明文拼装规则: `${password}:${timestamp}` （例：admin123:1784882384851）
- 前端加密代码示例：
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
 * 使用 RSA-OAEP (SHA-256) 加密文本
 * 
 * @param {string} pemPublicKey 后端获取到的 RSA 公钥 (Base64 或 PEM 格式)
 * @param {string} plainPassword 用户输入的明文密码
 * @param {number} timestamp 当前时间戳 (Date.now())
 * @returns {Promise<string>} 返回 Base64 格式的密文
 */
async function encryptPassword(pemPublicKey, plainPassword, timestamp) {
  // 1. 拼装待加密明文: password:timestamp
  const payload = `${plainPassword}:${timestamp}`;

  // 2. 导入公钥为 WebCrypto CryptoKey 对象
  const keyBuffer = pemToArrayBuffer(pemPublicKey);
  const cryptoKey = await window.crypto.subtle.importKey(
    "spki",
    keyBuffer,
    {
      name: "RSA-OAEP",
      hash: "SHA-256" // 指定使用 SHA-256
    },
    false,
    ["encrypt"]
  );

  // 3. 执行加密
  const encodedPayload = new TextEncoder().encode(payload);
  const encryptedBuffer = await window.crypto.subtle.encrypt(
    { name: "RSA-OAEP" },
    cryptoKey,
    encodedPayload
  );

  // 4. 将加密后的 ArrayBuffer 转为 Base64 字符串
  const encryptedBytes = new Uint8Array(encryptedBuffer);
  let binary = '';
  for (let i = 0; i < encryptedBytes.byteLength; i++) {
    binary += String.fromCharCode(encryptedBytes[i]);
  }
  return window.btoa(binary);
}
```