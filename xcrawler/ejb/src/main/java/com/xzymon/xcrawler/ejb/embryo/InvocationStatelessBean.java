package com.xzymon.xcrawler.ejb.embryo;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xzymon.xcrawler.ejb.interceptor.EmbryoLogging;

@Stateless
@Remote(RemoteInvocationStateless.class)
public class InvocationStatelessBean implements BusinessInvocationStateless{

	private static final Logger logger = LoggerFactory.getLogger(InvocationStatelessBean.class.getName());
	
	@Override
	@Interceptors(EmbryoLogging.class)
	public String sample(String param) {
		return param + " as stateless";
	}
	
}
