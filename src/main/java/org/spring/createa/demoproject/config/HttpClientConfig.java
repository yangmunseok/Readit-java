package org.spring.createa.demoproject.config;

import org.spring.createa.demoproject.service.Data4LibraryServiceApi;
import org.spring.createa.demoproject.service.NaverBookSearchApi;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration(proxyBeanMethods = false)
@ImportHttpServices({Data4LibraryServiceApi.class, NaverBookSearchApi.class})
public class HttpClientConfig {

}
