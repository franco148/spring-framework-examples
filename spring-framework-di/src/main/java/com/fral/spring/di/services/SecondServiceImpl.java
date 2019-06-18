package com.fral.spring.di.services;

import org.springframework.stereotype.Component;

@Component("secondService")
public class SecondServiceImpl implements MyService {

	@Override
	public String operation() {
		return "Executing some important process - MySecondSvImpl.";
	}

}
