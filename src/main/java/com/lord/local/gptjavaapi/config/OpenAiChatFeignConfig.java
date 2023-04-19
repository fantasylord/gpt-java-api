package com.lord.local.gptjavaapi.config;

import com.lord.local.gptjavaapi.remoter.client.OpenAIUserClient;
import com.lord.local.gptjavaapi.remoter.client.OpenChatClient;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Authenticator;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.commons.httpclient.DefaultOkHttpClientFactory;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Configuration
@EnableFeignClients(basePackages = "com.lord.local.gptjavaapi.remoter.*")
@Slf4j
public class OpenAiChatFeignConfig {
    //    @Autowired
//    private ProxyService proxyService;
    @Value("${proxy.host}")
    private String proxyHost;
    @Value("${proxy.port}")
    private Integer proxyPort;
    @Value("#{'${proxy.domains}'.split(',')}")
    private Set<String> domainList;

    @Value("${openai.user.token}")
    private String userToken;

    @Value("${openai.user.key}")
    private String userKey;
    @Value("${proxy.username}")
    private String username;
    @Value("${proxy.password}")
    private String password;

    @Bean
    public RequestInterceptor requestTokenBearerInterceptor() {
        return requestTemplate -> {

            if (requestTemplate.feignTarget().type() == OpenAIUserClient.class) {

                if (StringUtils.isEmpty(userToken)) {
                    log.info("系统尝试获取环境变量加载Token");
                    userToken = System.getenv("openaiusertoken");
                }
                requestTemplate.header("Authorization", "Bearer " + userToken);
            }
            if (requestTemplate.feignTarget().type() == OpenChatClient.class) {
                if (StringUtils.isEmpty(userKey)) {
                    log.info("系统尝试获取环境变量加载key");
                    userKey = System.getenv("openaiuserkey");
                }
                requestTemplate.header("Authorization", "Bearer " + userKey);
            }
//            proxyService.proxyAuthorization(requestTemplate);
        };
    }

    /**
     * 代理需要切换feign底层为okhttp
     *
     * @param builder
     * @return
     */
    @Bean
    public OkHttpClientFactory okHttpClientFactory(OkHttpClient.Builder builder) {
        return new ProxyOkHttpClientFactory(builder);
    }

    class ProxyOkHttpClientFactory extends DefaultOkHttpClientFactory {
        public ProxyOkHttpClientFactory(OkHttpClient.Builder builder) {
            super(builder);
            //如果配置文件中的代理信息为null 则该代理ip配置不生效
            if (proxyHost != null && proxyPort != null && domainList != null) {
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
                List<Proxy> proxyList = new ArrayList<>(1);
                proxyList.add(proxy);
                builder.proxySelector(new ProxySelector() {
                    @Override
                    public List<Proxy> select(URI uri) {

                        for (String domain : domainList) {
                            if (uri.getHost().contains(domain)) {
                                return proxyList;
                            }
                        }
                        return Collections.singletonList(Proxy.NO_PROXY);

                    }

                    @Override
                    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
                    }
                });
                if (!StringUtils.isEmpty(password) && StringUtils.isEmpty(username)) {
                    builder.proxyAuthenticator(new Authenticator() {

                        @Override
                        public Request authenticate(Route route, Response response) throws IOException {
                            //设置代理服务器账号密码
                            String credential = Credentials.basic("admin", "admin");
                            return response.request().newBuilder()
                                    .header("Proxy-Authorization", credential)
                                    .build();
                        }
                    });
                }
            }
        }
    }

    //如果要配置限制域则加上下面
            /*List<Proxy> proxyList = new ArrayList<>(1);
            proxyList.add(proxy);
            builder.proxySelector(new ProxySelector() {
                //限制域
                Set<String> domainList;
                @Override
                public List<Proxy> select(URI uri) {
                    if (uri == null || !domainList.contains(uri.getHost())) {
                        return Collections.singletonList(Proxy.NO_PROXY);
                    }
                    return proxyList;
                }
                @Override
                public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
                }
            });*/
}
