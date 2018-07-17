package com.fral.spring.billing.handlers.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
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

		if (username != null && password != null) {
			logger.info("Username from request parameter (form-data): " + username);
			logger.info("Password from request parameter (form-data): " + password);
		} else {
			com.fral.spring.billing.models.entity.User user = null;
			try {
				 user = new ObjectMapper().readValue(request.getInputStream(), com.fral.spring.billing.models.entity.User.class);
				 
				 username = user.getUsername();
				 password = user.getPassword();
				 
				 logger.info("Username from request InputStream (raw): " + username);
				 logger.info("Password from request InputStream (raw): " + password);
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		username = username.trim();

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
		
		return authenticationManager.authenticate(authToken);
	}



	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		String userName = authResult.getName(); // or (((User)auth.getPrincipal()).getUsername());
		
		Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
		Claims claims = Jwts.claims();
		claims.put("authorities", new ObjectMapper().writeValueAsString(roles));
		
		String token = Jwts.builder()
						   .setClaims(claims)
						   .setSubject(userName)
						   .signWith(SignatureAlgorithm.HS512, "My.Jwt.Secret.Key".getBytes())
						   .setIssuedAt(new Date())
						   .setExpiration(new Date(System.currentTimeMillis() + 3600000))
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
