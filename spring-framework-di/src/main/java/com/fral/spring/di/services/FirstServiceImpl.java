package com.fral.spring.di.services;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Here we can use an spring stereotype like @component which is the general one,
 * or a specific one like @service
 * 
 * @Primary is used when there are more than one implementation of a interface
 * and the one that is annotated with @Primary is going to be taken into account. 
 * 
 * @author Franco
 *
 */
@Primary
@Component("firstService")
public class FirstServiceImpl implements MyService {

	@Override
	public String operation() {
		return "Executing some important process - MyServiceImpl.";
	}

}
