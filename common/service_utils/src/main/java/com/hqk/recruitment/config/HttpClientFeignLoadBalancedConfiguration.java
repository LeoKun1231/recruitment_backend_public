package com.hqk.recruitment.config;


import feign.Client;
import feign.httpclient.ApacheHttpClient;
import org.apache.http.client.HttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.clientconfig.HttpClientFeignConfiguration;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

//解决 Incomplete output stream executing POST http://
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ApacheHttpClient.class)
@ConditionalOnProperty(value = "feign.httpclient.enabled",matchIfMissing = true)
@Import(HttpClientFeignConfiguration.class)
public class HttpClientFeignLoadBalancedConfiguration {
    @Bean
    @ConditionalOnMissingBean(Client.class)
    public Client feignClient(CachingSpringLoadBalancerFactory cachingSpringLoadBalancerFactory, SpringClientFactory clientFactory, HttpClient httpClient){
        ApacheHttpClient apacheHttpClient = new ApacheHttpClient(httpClient);
        return new LoadBalancerFeignClient(apacheHttpClient,cachingSpringLoadBalancerFactory,clientFactory);
    }
}
