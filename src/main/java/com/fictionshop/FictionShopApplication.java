package com.fictionshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;

/*//Security inactive
@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class}
)*/
@SpringBootApplication
@EnableCaching
public class FictionShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(FictionShopApplication.class, args);
	}

	@Bean
	public CacheManager cacheManager(){
		SimpleCacheManager cM = new SimpleCacheManager();
		Cache productsCache = new ConcurrentMapCache("product_list");
		Cache productCache = new ConcurrentMapCache("products");

		cM.setCaches(Arrays.asList(productsCache,productCache));
		return cM;
	}

}
