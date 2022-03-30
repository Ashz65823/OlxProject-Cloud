package com.zensar.olx.filter;

import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

//@Configuration
public class OlxCustomeFilter implements GlobalFilter {

	// @Configuration //-->@Configuration is commented and this will help us in not
	// sending header in postman and we can get output on browser.. If its not
	// commented then we can only access apigateway in postman by sending header
	// authorization
	/**
	 * Following method is opportunity for us to do some pre-processing This method
	 * automatically executes
	 * 
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		System.out.println("In Filter----------------->Doing preprocessing");
		/**
		 * Pre-processing logic
		 */

		// This program get executed only when @Configuration is uncommented
		ServerHttpRequest request = exchange.getRequest();
		HttpHeaders headers = request.getHeaders();
		List<String> list = headers.get("Authorization");

		if (list != null) {// If header not present this application will give 401 status which is
							// UNAUTHORIZED
			String authorizationHeaderValue = list.get(0);
			if (authorizationHeaderValue == null) {
				/**
				 * if pre-processing logic fails then don't allows request to process
				 * 
				 */
				ServerHttpResponse responce = exchange.getResponse();
				responce.setStatusCode(HttpStatus.UNAUTHORIZED);
				return responce.setComplete();
			}
		} else {
			/**
			 * if pre-processing logic fails then don't allows request to process
			 * 
			 */
			ServerHttpResponse responce = exchange.getResponse();
			responce.setStatusCode(HttpStatus.UNAUTHORIZED);
			return responce.setComplete();
		}
		/**
		 * After successful pre-processing this method MUST call filter() method on
		 * chain object
		 */
		return chain.filter(exchange);
	}

}
