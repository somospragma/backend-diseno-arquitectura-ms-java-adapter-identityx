package co.com.pragma.identityx.driven_adapters.identityx.api.feign;

import feign.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Configuration
@Slf4j
public class FeignClientConfig {
    @Value("${proxy.host}")
    private String proxyHost;

    @Value("${spring.profiles.active:}")
    private String environment;

    private final KeyStore trustStore;

    public FeignClientConfig(KeyStore trustStore) {
        this.trustStore = trustStore;
    }

    @Bean
    public Client feignClient() throws Exception{
        SSLContext sslContext = createSSLContext();
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        if(!environment.equals("local")){
            try {
                String[] proxyValues = proxyHost.split(":");
                String proxyIp = proxyValues[0];
                int proxyPort = Integer.parseInt(proxyValues[1]);

                if (StringUtils.hasText(proxyIp)) {
                    log.info("Using proxy configuration in Feign Client: {}",proxyHost);
                    return new Client.Proxied(sslSocketFactory, null,
                            new Proxy(Proxy.Type.HTTP,
                                    new InetSocketAddress(proxyIp,proxyPort)));
                }else{
                    log.info("Invalid proxy Ip configuration. Using default client.");
                }
            } catch (NumberFormatException e) {
                log.info("Invalid proxy port configuration. Using default client.");
            } catch (Exception e) {
                log.info("Invalid proxy configuration. Using default client.");
            }
        }
        return new Client.Default(sslSocketFactory, null);
    }

    @Bean
    public RestTemplate restTemplate() throws Exception {
        SSLContext sslContext = createSSLContext();
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        if(!environment.equals("local")){
            log.info(proxyHost);
            try {
                String[] proxyValues = proxyHost.split(":");
                String proxyIp = proxyValues[0];
                int proxyPort = Integer.parseInt(proxyValues[1]);

                if (StringUtils.hasText(proxyIp)) {

                    SimpleClientHttpRequestFactory requestFactory = getSimpleClientHttpRequestFactory(sslSocketFactory);

                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp, proxyPort));
                    requestFactory.setProxy(proxy);
                    log.info("Using proxy configuration in Rest Template: {}",proxyHost);
                    return new RestTemplate(requestFactory);
                }else{
                    log.info("Invalid proxy Ip configuration. Using restTemplate default.");
                }
            } catch (NumberFormatException e) {
                log.info("Invalid proxy port configuration. Using restTemplate default.");
            } catch (Exception e) {
                log.info("Invalid proxy configuration. Using restTemplate default.");
            }
        }
        return new RestTemplate();
    }

    private static SimpleClientHttpRequestFactory getSimpleClientHttpRequestFactory(SSLSocketFactory sslSocketFactory) {
        return new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(HttpURLConnection connection, String httpMethod)
                    throws IOException {
                if (connection instanceof HttpsURLConnection) {
                    ((HttpsURLConnection) connection).setSSLSocketFactory(sslSocketFactory);
                }
                super.prepareConnection(connection, httpMethod);
            }
        };
    }

    private SSLContext createSSLContext() throws
            NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        TrustManagerFactory trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
        return sslContext;
    }
}
