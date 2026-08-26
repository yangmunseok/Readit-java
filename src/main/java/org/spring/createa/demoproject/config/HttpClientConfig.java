package org.spring.createa.demoproject.config;

import org.spring.createa.demoproject.service.Data4LibraryServiceApi;
import org.spring.createa.demoproject.service.KakaoBookSearchApi;
import org.spring.createa.demoproject.service.NaverBookSearchApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration(proxyBeanMethods = false)
@ImportHttpServices({Data4LibraryServiceApi.class, NaverBookSearchApi.class,
    KakaoBookSearchApi.class})
public class HttpClientConfig {

  @Bean
  RestClientHttpServiceGroupConfigurer groupConfigurer() {
    return groups -> groups.forEachClient((group, clientBuilder) ->
        clientBuilder.requestInterceptor((request, body, execution) -> {
          System.out.println(
              "REQUEST: " +
                  request.getMethod() + " " +
                  request.getURI()
          );

          return execution.execute(request, body);
        })
    );
  }

}
