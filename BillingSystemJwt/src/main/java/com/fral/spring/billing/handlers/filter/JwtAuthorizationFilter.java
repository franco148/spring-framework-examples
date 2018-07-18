package com.fral.spring.billing.handlers.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fral.spring.billing.handlers.SimpleGrantedAuthoritiesMixin;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String header = request.getHeader("Authorization");

		if (!requiresAuthentication(header)) {
			chain.doFilter(request, response);
			return;
		}
		
		boolean validToken;
		Claims token = null;
		try {
			token = Jwts.parser()
			.setSigningKey("My.Jwt.Secret.Key".getBytes())
			.parseClaimsJws(header.replace("Bearer ", ""))
			.getBody();
			
			validToken = true;
		} catch (JwtException | IllegalArgumentException e) {
			validToken = false;
		}
		
		UsernamePasswordAuthenticationToken authentication = null;
		if (validToken) {
			String username = token.getSubject();
			Object roles = token.get("authorities");
			
			//This is throwing an exception, because there is not a empty constructor for GrantedAuthority
			Collection<? extends GrantedAuthority> authorities = Arrays.asList(new ObjectMapper()
																.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthoritiesMixin.class)
																.readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));
			
			authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
		}
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}
	
	protected boolean requiresAuthentication(String header) {
		if (header == null || !header.startsWith("Bearer ")) {
			return false;
		}
		
		return true;
	}

}
