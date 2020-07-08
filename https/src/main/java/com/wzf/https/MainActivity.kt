package com.wzf.https

import android.content.Context
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.util.Log
import com.demo.base.ui.base.BaseLibActivity
import com.demo.base.utils.AppDeviceInfo
import com.hjq.toast.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.security.*
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*

class MainActivity : BaseLibActivity() {



    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initForSave(savedInstanceState: Bundle?) {
        super.initForSave(savedInstanceState)
        ToastUtils.init(this.application)
        showToast(AppDeviceInfo.getAppVersionName(this))
        btn_http.onClick {
            ViewCompat.animate(btn_http).alphaBy(-0.1f).setDuration(10).start()
            doAsync {
                requestHttp()
            }
        }

        btn_https.onClick{
            doAsync {
                requestHttps()
            }
        }

        btn_https_okhttp.onClick {
            doAsync {
                requestHttpsOkhttp()
            }
        }
    }




    private fun requestHttp() {

//       var url = URL("http://192.168.1.55:8080/resource/json.json")
       var url = URL("https://woshimimalltest.mvoicer.com/")
        //http 请求
        val connection:HttpURLConnection = url.openConnection() as HttpURLConnection

        val input = connection.inputStream

        val out = ByteArrayOutputStream()

        var length = 0

        var buffer = ByteArray(32)


        while (length != -1){
            length = input.read(buffer)
            if(length > 0){
                out.write(buffer, 0, length)
            }

        }

       Log.i("requestHttp", out.toString())
    }


    private fun requestHttps() {

//        var url = URL("https://192.168.1.55:8443/resource/json.json")
        var url = URL("https://woshimimalltest.mvoicer.com/")
        //https 请求
        val connection: HttpsURLConnection = url.openConnection() as HttpsURLConnection


        //不配置 主机名校验 Hostname 192.168.1.55 not verified:
        connection.setHostnameVerifier { hostname, session ->
            true
        }
        //不配置证书 报 java.security.cert.CertPathValidatorException: Trust anchor for certification path not found.
        connection.sslSocketFactory = getSocketFactor()



        val input = connection.inputStream

        val out = ByteArrayOutputStream()

        var length = 0

        var buffer = ByteArray(32)


        while (length != -1){
            length = input.read(buffer)
            if(length > 0){
                out.write(buffer, 0, length)
            }

        }

        Log.i("requestHttps", out.toString())
    }

    /**
     * 设置证书
     */
    private fun getSocketFactor(): SSLSocketFactory {
        val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
        val cer: X509Certificate = cf.generateCertificate(assets.open("httpsks.cer")) as X509Certificate

        // 3. 设置证书
        val keyStore:KeyStore? = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
            load(null, null) // 清除默认的签名和密码
            setCertificateEntry("httpsks", cer) //加载我们自己的签名
        }

        //2.设置信任管理器
        var tmf = TrustManagerFactory.getInstance( TrustManagerFactory.getDefaultAlgorithm()).apply {
            init(keyStore)
        }

        //1.创建ssl 上下文对象设置信任管理器
        val context = SSLContext.getInstance("TLS").apply {
            init(null, tmf.trustManagers ,null)
        }
        return context.socketFactory

    }

    private fun requestHttpsOkhttp() {
//        val url = "http://192.168.1.55:8080/resource/json.json"
        val url = "https://woshimimalltest.mvoicer.com/"
        val client = OkHttpClient.Builder()
//                .sslSocketFactory(DefineSocketFactory.getSocketFactory(this, "httpsks.cer", "httpsks"))
                .hostnameVerifier(HostnameVerifier { hostname, session ->  true})
                .build()
        val request =  Request.Builder()
                .url(url)
                .get()
                .build()

        val call = client.newCall(request)
        call.enqueue(object : Callback{
            override fun onResponse(call: Call, response: Response) {
                Log.i("requestHttpsOkhttp", response.body()?.string())
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.i("requestHttpsOkhttp", e.toString())
            }
        })

    }



    object DefineSocketFactory{

        fun getSocketFactory(contextRes: Context, assetCerName: String, alias: String): SSLSocketFactory {
            var context: SSLContext? = null
            try {
                val cf = CertificateFactory.getInstance("X.509")
                val cer = cf.generateCertificate(contextRes.assets.open(assetCerName)) as X509Certificate
                // 3. 设置证书
                val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
                keyStore.load(null, null) // 清除默认的签名和密码
                keyStore.setCertificateEntry(alias, cer) //加载我们自己的签名
                //2.设置信任管理器
                val tmf:TrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                tmf.init(keyStore)
                //1.创建ssl 上下文对象设置信任管理器
                context = SSLContext.getInstance("TLS")
                context!!.init(null, tmf.trustManagers, SecureRandom())
            } catch (e: CertificateException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: KeyStoreException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: KeyManagementException) {
                e.printStackTrace()
            }

            return context!!.socketFactory
        }

    }

}
