package com.fral.spring.di.services;

import org.springframework.stereotype.Component;

/**
 * Here we can use an spring stereotype like @component which is the general one,
 * or a specific one like @service
 * 
 * @author Franco
 *
 */
@Component
public class MyServiceImpl implements MyService {

	@Override
	public String operation() {
		return "Executing some important process.";
	}

}
