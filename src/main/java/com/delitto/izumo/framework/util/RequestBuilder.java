package com.delitto.izumo.framework.util;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Url;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
public class RequestBuilder {
    public static OkHttpClient getProxyClient(String ip, int port) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .proxy(new java.net.Proxy(java.net.Proxy.Type.SOCKS, new InetSocketAddress(ip, port))).hostnameVerifier((s, sslSession) -> true);
        return builder.build();
    }


    public static OkHttpClient getIgnoreSSLClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .hostnameVerifier((s, sslSession) -> true)
                .sslSocketFactory(getSSLContext().getSocketFactory(), getX509TrustManagerInstance())
                ;
        return builder.build();
    }
    public static Retrofit getProxyRetrofit(String baseUrl, String proxyIp, int proxyPort) {
        return new Retrofit.Builder().baseUrl(baseUrl).client(RequestBuilder.getProxyClient(proxyIp, proxyPort)).build();
    }

    public static Retrofit getRetrofit(String baseUrl) {
        return new Retrofit.Builder().baseUrl(baseUrl).build();
    }

    public static Retrofit getIgnoreSSLRetrofit(String baseUrl) {
        return new Retrofit.Builder().baseUrl(baseUrl).client(getIgnoreSSLClient()).build();
    }

    public static X509TrustManager getX509TrustManagerInstance() {
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        return xtm;
    }



    public static SSLContext getSSLContext(){
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");

            sslContext.init(null, new TrustManager[]{getX509TrustManagerInstance()}, new SecureRandom());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return sslContext;
    }

    public static Response<ResponseBody> get(String url) {
        if(StringUtils.isNotBlank(url)) {
            Retrofit simpleRetrofit = RequestBuilder.getRetrofit("https://sample.com");
            IApi api = simpleRetrofit.create(IApi.class);
            Call<ResponseBody> call = api.getFullUrl(url);
            try {
                Response<ResponseBody> response = call.execute();
                if(response.isSuccessful()) {
                    return response;
                }
            } catch (IOException ioe) {
                return null;
            }
        }
        return null;
    }
}

interface IApi {
    @GET()
    Call<ResponseBody> getFullUrl(@Url String url);
}


