package com.Axonix.index.config // 确保包名正确

import android.util.Log
import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

object NetworkClient {
    // 使用 lazy 确保线程安全且只初始化一次
    val client: OkHttpClient by lazy { createSecureClient() }

    private fun createSecureClient(): OkHttpClient {
        return try {
            // 1. 创建信任所有证书的 TrustManager
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })

            // 2. 使用 TLS 协议（比 SSL 更安全）
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // 3. 构建统一客户端（添加通用配置）
            OkHttpClient.Builder()
                .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true } // 跳过主机名验证
                .connectTimeout(30, TimeUnit.SECONDS) // 添加超时配置
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
        } catch (e: Exception) {
            // 记录异常日志（实际项目建议添加）
            Log.e("NetworkClient", "SSL配置失败", e)
            throw RuntimeException("网络初始化失败，请检查SSL配置", e)
        }
    }
}
