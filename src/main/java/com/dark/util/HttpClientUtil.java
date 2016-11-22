package com.dark.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class HttpClientUtil {

    private final static Logger LOG = LoggerFactory.getLogger(HttpClientUtil.class);

    private static CloseableHttpClient httpClient = null;
    private static RequestConfig requestConfig = null;

    private final static int socketTimeout = 10000;
    private final static int connectTimeout = 30000;

    static {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            SSLContext sslContext =
                    SSLContexts.custom().useTLS().loadTrustMaterial(trustStore).build();
            sslContext.init(null, new TrustManager[] {new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {}

                @Override
                public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {}

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }}, null);
            SSLConnectionSocketFactory sslSFactory = new SSLConnectionSocketFactory(sslContext,
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            Registry<ConnectionSocketFactory> socketFactoryRegistry =
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.INSTANCE)
                            .register("https", sslSFactory).build();

            PoolingHttpClientConnectionManager connManager =
                    new PoolingHttpClientConnectionManager(socketFactoryRegistry);

            SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
            connManager.setDefaultSocketConfig(socketConfig);

            ConnectionConfig connectionConfig =
                    ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE)
                            .setUnmappableInputAction(CodingErrorAction.IGNORE)
                            .setCharset(Consts.UTF_8).build();
            connManager.setDefaultConnectionConfig(connectionConfig);

            connManager.setMaxTotal(200);
            connManager.setDefaultMaxPerRoute(20);

            HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
                @Override
                public boolean retryRequest(IOException exception, int executionCount,
                        HttpContext context) {
                    if (executionCount >= 3) {
                        return false;
                    }
                    if (exception instanceof InterruptedIOException) {
                        return true;
                    }
                    if (exception instanceof ConnectTimeoutException) {
                        return true;
                    }
                    if (exception instanceof UnknownHostException) {
                        return false;
                    }
                    if (exception instanceof SSLException) {
                        return false;
                    }
                    HttpRequest request = HttpClientContext.adapt(context).getRequest();
                    if (!(request instanceof HttpEntityEnclosingRequest)) {
                        return true;
                    }
                    return false;
                }
            };

            httpClient = HttpClients.custom().setConnectionManager(connManager)
                    .setRetryHandler(retryHandler).build();

            requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout)
                    .setConnectTimeout(connectTimeout).build();
        } catch (Exception e) {
            LOG.error("create httpclient failed", e);
        }
    }

    public static String get(String url) throws IOException {
        if (httpClient == null) {
            throw new IllegalStateException("httpClient is null");
        }

        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);

        CloseableHttpResponse response = null;
        String result = null;

        LOG.info("get, url: {}", url);
        try {
            response = httpClient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
                LOG.info("get finished, return: \n{}", result);
            }
        } catch (IOException e) {
            LOG.error("get error, url: {}", url);
            throw e;
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    public static byte[] getFileData(String url) throws IOException {
        if (httpClient == null || StringUtils.isEmpty(url)) {
            throw new IllegalStateException("httpClient is null or url is empty");
        }

        HttpGet httpGet = new HttpGet(encodeSpecialTag(url));
        httpGet.setConfig(requestConfig);
        httpGet.addHeader("Accept", "text/html");
        httpGet.addHeader("Accept-Charset", "utf-8");
        httpGet.addHeader("Accept-Encoding", "gzip");
        httpGet.addHeader("Accept-Language", "en-US,en");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.160 Safari/537.22");


        CloseableHttpResponse response = null;
        byte[] data = null;

        long start = System.currentTimeMillis();

        LOG.info("get fileData, url: {}", url);
        try {
            response = httpClient.execute(httpGet);

            LOG.info("get fileData, getResponse cost {} ms,response:{}", System.currentTimeMillis() - start,response.toString());

            HttpEntity entity = response.getEntity();

            LOG.info("get fileData, getEntity cost {} ms", System.currentTimeMillis() - start);
            if (entity != null) {
                Header h = response.getFirstHeader("Content-Encoding");
                if (h != null && h.getValue() != null
                        && h.getValue().toLowerCase().contains("gzip")) {
                    GZIPInputStream gzip = new GZIPInputStream(entity.getContent());
                    data = IOUtils.toByteArray(gzip);
                } else {

                    data = IOUtils.toByteArray(entity.getContent());
                }
                LOG.info("get fileData finished, return byte[], length {}, cost {} ms ",
                        data.length, System.currentTimeMillis() - start);
            }
        } catch (IOException e) {
            LOG.error("get fileData error, url: {}", url);
            throw e;
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    public static String encodeSpecialTag(String url) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }

        StringBuilder newUrl = new StringBuilder();
        for (int i = 0; i < url.length(); i++) {
            char c = url.charAt(i);

            switch (c) {
                case ' ':
                    newUrl.append("%20");
                    break;
                case '^':
                    newUrl.append("%5E");
                    break;
                case '`':
                    newUrl.append("%60");
                    break;
                default:
                    newUrl.append(c);
            }
        }

        return newUrl.toString();
    }

    // 发送POST 请求
    public static String post(String url, Map<String, String> params) throws IOException {
        if (httpClient == null) {
            throw new IllegalStateException("httpClient is null");
        }

        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);

        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(formParams, Consts.UTF_8));

        CloseableHttpResponse response = null;
        String result = null;

        LOG.info("post, url: {}, param: {}", url, String.valueOf(params));
        try {
            response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
                LOG.info("post finished, return: \n{}", result);
            }
        } catch (IOException e) {
            LOG.error("post error, url: {}, param: {}", url, String.valueOf(params));
            throw e;
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    public static String invokePost(String targetUrl, Object targetParam) {
        return invokePost(targetUrl, DEFAULT_CONNECTION_TIMEOUT, targetParam, DEFAULT_CHARSET);
    }

    public static String invokePost(String url, int timeout, Object targetParam, String encoding) {
        HttpPost post = new HttpPost(url);
        try {
            post.setHeader("Content-type", "application/json");
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout)
                    .setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
                    .setExpectContinueEnabled(false).build();
            post.setConfig(requestConfig);

            String str1 = JSON.toJSONString(targetParam).replace("\\", "");

            post.setEntity(new StringEntity(str1, encoding));
            if (LOG.isDebugEnabled()) {
                LOG.debug("[HttpClientUtil invokePost] begin invoke url:" + url + " , params:"
                        + str1);
            }
            CloseableHttpResponse response = getInstance().execute(post);
            try {
                HttpEntity entity = response.getEntity();
                try {
                    if (entity != null) {
                        String str = EntityUtils.toString(entity, encoding);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("[HttpClientUtil invokePost] Debug response, target url :"
                                    + url + " , response string :" + str);
                        }
                        return str;
                    }
                } finally {
                    if (entity != null) {
                        entity.getContent().close();
                    }
                }
            } finally {
                if (response != null) {
                    response.close();
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);

            throw new RuntimeException(e);
        } finally {
            post.releaseConnection();
        }
        return "Unknown result";
    }

    public static CloseableHttpClient getInstance() {
        if (httpClient == null) {
            httpClient = HttpClients.custom()
                    .setConnectionManager(new PoolingHttpClientConnectionManager()).build();
        }

        return httpClient;
    }

    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final int DEFAULT_CONNECTION_TIMEOUT = 10000;
}
