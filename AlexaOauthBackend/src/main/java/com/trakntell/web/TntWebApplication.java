package com.trakntell.web;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableFeignClients
//Used for scheduling task
@EnableScheduling
public class TntWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(TntWebApplication.class, args);
	}


	@Bean
	public PasswordEncoder encoder() {
		return new PasswordEncoder() {

			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				try {
					//return encodedPassword.equals(Utils.md5(rawPassword.toString()));
					return true;
				}
				catch(Exception e) {
					return false;
				}
			}

			@Override
			public String encode(CharSequence rawPassword) {
				try {
					//return Utils.md5(rawPassword.toString());
					return rawPassword.toString();
				}
				catch(Exception e) {
					return null;
				}
			}
		};
	}

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource mainDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	/*@Bean
	@ConfigurationProperties(prefix = "spring.datasourcce.tokenstore")
	public DataSource tokenStoreDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}*/

	@Bean
	public DefaultTokenServices defaultTokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(getTokenStore());
		return defaultTokenServices;
	}

	@Bean
	public TokenStore getTokenStore() {
		return new JdbcTokenStore(mainDataSource());
		//return new JdbcTokenStore(tokenStoreDataSource());
	}

	/*@Bean
	public TokenStore getTokenStore(RedisConnectionFactory connectionFactory) {
		return new RedisTokenStore(connectionFactory);
	}*/

	@Bean
	public TaskScheduler taskScheduler() {
		final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(5);
		return scheduler;
	}


}