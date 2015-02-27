package com.xzymon.xcrawler.ejb.embryo;

import java.io.Serializable;

import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptors;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xzymon.xcrawler.ejb.interceptor.EmbryoLogging;

@Stateful
@Remote(RemoteInvocationStateful.class)
public class InvocationStatefulBean implements BusinessInvocationStateful, Serializable {
	
	private static final long serialVersionUID = -5952547899336013130L;
	private static final Logger logger = LoggerFactory.getLogger(InvocationStatefulBean.class.getName());
	
	@Override
	@Interceptors(EmbryoLogging.class)
	public String sample(String param) {
		return param + " as stateful";
	}
	
}
