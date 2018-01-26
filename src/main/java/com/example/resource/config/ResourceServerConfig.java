package com.example.resource.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	private static final String RESOURCE_ID = "resource_id";
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(RESOURCE_ID).stateless(false);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
        http.
                anonymous().disable()
                .authorizeRequests()
                .antMatchers("/users/**")
//				.authenticated()
				.access("hasRole('ADMIN')")
                .and().csrf().disable().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
	}


	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		//设置用于解码的非对称加密的公钥
		converter.setVerifierKey(getPubKey());
		return converter;
	}

	private String getPubKey() {
		Resource resource = new ClassPathResource("public_key");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
			System.out.println("本地公钥");
			return br.lines().collect(Collectors.joining("\n"));
		} catch (IOException ioe) {
//			return getKeyFromAuthorizationServer();
			return "";
		}
	}

//	private String getKeyFromAuthorizationServer() {
//		ObjectMapper objectMapper = new ObjectMapper();
//		String pubKey = new RestTemplate().getForObject(resourceServerProperties.getJwt().getKeyUri(), String.class);
//		try {
//			Map map = objectMapper.readValue(pubKey, Map.class);
//			System.out.println("联网公钥");
//			return map.get("value").toString();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

//
//	    http
//				// Just for laughs, apply OAuth protection to only 2 resources
//				.requestMatchers().antMatchers("/","/admin/beans").and()
//           .authorizeRequests()
//           .anyRequest().access("#oauth2.hasScope('read')"); //[4]
//	// @formatter:on



}