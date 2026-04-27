package org.spring.createa.demoproject.config;

import org.spring.createa.demoproject.service.Data4LibraryService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration(proxyBeanMethods = false)
@ImportHttpServices(Data4LibraryService.class)
public class HttpClientConfig {

}
