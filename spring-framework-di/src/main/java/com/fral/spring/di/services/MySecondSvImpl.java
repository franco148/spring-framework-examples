package com.fral.spring.di.services;

import org.springframework.stereotype.Component;

@Component
public class MySecondSvImpl implements MyService {

	@Override
	public String operation() {
		return "Executing some important process - MySecondSvImpl.";
	}

}
