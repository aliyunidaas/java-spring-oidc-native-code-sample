# 快速开始
# 授权码流
## 第一步：注册应用
![img.png](src/main/resources/static/img/img.png)
![img_1.png](src/main/resources/static/img/img_1.png)
![img_2.png](src/main/resources/static/img/img_2.png)
## 第二步：修改配置
修改application.yaml文件中的 idp 配置
```yaml
custom:
  serverDomain: # 填写当前运行的域名地址
idaas:
    oidc:   
        clientId:  # IDaaS应用中拿到的 client_id
        clientSecret:  # IDaaS应用中拿到的 client_secret
        issuer: # IDaaS应用中拿到的Issuer
        scopes: # IDaaS应用中配置的scopes
        redirectUri: ${custom.serverDomain}/authentication/login #登录 Redirect URI
```
application.yaml中的配置项分别对应IDaaS应用中的配置
![img_3.png](src/main/resources/static/img/img_3.png)
![img_6.png](src/main/resources/static/img/img_6.png)
![img_5.png](src/main/resources/static/img/img_5.png)
其中，redirectUri在当前项目中指向了/authentication/login
## 第三步：在 IDaaS 中添加重定向地址redirect-uri
- 因为要最终需要到LoginController接口中拿到access token令牌，故重定向地址为LoginController内的login方法所指向的uri，将其填写到IDaaS中。
  - http://127.0.0.1:8082/authentication/login

## 第四步：添加授权
### 添加授权
![img_7.png](src/main/resources/static/img/img_7.png)
### 第五步：访问
 有两种方式可以登录访问，可以由SP发起登录，也可以由IdP发起登录，下面依次进行介绍。
### （一）由SP(java-spring-oidc-native-code-sample)发起的登录
整个授权码流程如下：
![img_8.png](src/main/resources/static/img/img_8.png)
1. 访问应用端点: http://127.0.0.1:8082,来到SP首页位置
2. 点击想要的Tab页，如用户信息，此时会跳转到登录认证页面
![img_9.png](src/main/resources/static/img/img_9.png)
填写注册应用中所授权的用户账密
4. 登录认证成功后会跳转回redirect-uri,即 http://127.0.0.1:8082/authentication/login, 并显示一下信息
- 授权码
- 令牌信息（access token id token、refresh token）
- 用户信息
### （二）由IdP(IDaaS)发起的登录
需要在IDaaS应用中的高级配置中填写initLoginUri,不填写将直接跳转到redirectUri
![img_10.png](src/main/resources/static/img/img_10.png)
整个授权码流程如下：
![img_11.png](src/main/resources/static/img/img_11.png)
1. 登录注册应用所授权的IDaaS用户的应用门户，登录认证页面如下所示。
![img_9.png](src/main/resources/static/img/img_9.png)
3. 登录成功后，将会在页面中显示注册的应用，点击该应用
![img_12.png](src/main/resources/static/img/img_12.png)
4. 登录认证成功后会跳转回redirect-uri,即 http://127.0.0.1:8082/authentication/login, 并显示一下信息
- 授权码
- 令牌信息（access token id token、refresh token）
- 用户信息端点信息

# PKCE + 授权码流
与授权码流不同点如下：
1. 注册应用时指定PKCE模式
![img_13.png](src/main/resources/static/img/img_13.png)
2.修改application.yaml配置,添加如下两个参数。值与注册应用时配置的保持一致
```yaml
idaas:
    oidc:
        pkceRequired: true #打开PKCE
        codeChallengeMethod: S256 #加密算法
```
## 访问
### 可以通过SP发起登录，也可以通过IdP发起登录
PKCE可以防止攻击者窃取code，从而通过code去拿到令牌。主要流程如下：
![img_14.png](src/main/resources/static/img/img_14.png)
SP登录与IdP发起的登录可参考授权码流。