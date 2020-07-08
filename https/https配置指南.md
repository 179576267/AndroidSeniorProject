# https  配置

### 1.生成keystore
##### 1.生成签名指令
```
keytool -genkeypair -alias httpsks -keyalg RSA -validity 3650 -keystore httpsks.keystore
```

##### 2.生成cer文件

```cmd
keytool -exportcert -alias httpsks -file httpsks.cer -keystore httpsks.keystore
```



### 2.server.xml 修改
```
<Connector port="8443" protocol="org.apache.coyote.http11.Http11Protocol"
               maxThreads="150" SSLEnabled="true" scheme="https" secure="true"
			   keystoreFile="conf/httpsks.keystore" keystorePass="123456"
               clientAuth="false" sslProtocol="TLS" />
```

### 3.kotlin 代码
```kotlin
/**
* 设置证书
*/
private fun getSocketFactor(): SSLSocketFactory {
        val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
        val cer: X509Certificate = cf.generateCertificate(assets.open("httpsks.cer")) as 									X509Certificate
        // 3. 设置证书
        val keyStore:KeyStore? = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
            load(null, null) // 清除默认的签名和密码
            setCertificateEntry("httpsks", cer) //加载我们自己的签名
        }
        //2.设置信任管理器
        var tmf = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()).apply {
            init(keyStore)
        }

        //1.创建ssl 上下文对象设置信任管理器
        val context = SSLContext.getInstance("TLS").apply {
            init(null, tmf.trustManagers ,null)
        }
        return context.socketFactory
    }

//不配置 主机名校验 Hostname 192.168.1.55 not verified:
connection.setHostnameVerifier { hostname, session -> true}
```

