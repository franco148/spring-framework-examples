package com.fral.spring.billing.handlers.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authenticationManager;
	
	

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		
		this.authenticationManager = authenticationManager;
		//by default the interceptor is /login, so we can override it as follows:
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));
	}



	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		String username = obtainUsername(request);// or request.getParameter(username);
		String password = obtainPassword(request);// or request.getParameter(password);

		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}
		
		if (username != null && password != null) {
			logger.info("Username from request parameter (form-data): " + username);
			logger.info("Password from request parameter (form-data): " + password);
		}

		username = username.trim();

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
		
		return authenticationManager.authenticate(authToken);
	}



	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		String userName = authResult.getName(); // or (((User)auth.getPrincipal()).getUsername());
		String token = Jwts.builder()
						   .setSubject(userName)
						   .signWith(SignatureAlgorithm.HS512, "My.Jwt.Secret.Key".getBytes())
						   .compact();
		
		response.addHeader("Authorization", "Bearer ".concat(token));
		
		Map<String, Object> body = new HashMap<>();
		body.put("token", token);
		body.put("user", (User)authResult.getPrincipal());
		body.put("message", String.format("Hi %s, you have logged in successfully!", userName));
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(200);
		response.setContentType("application/json");
	}

}
