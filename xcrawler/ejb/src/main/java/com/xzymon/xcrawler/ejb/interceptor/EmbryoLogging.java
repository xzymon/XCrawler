package com.xzymon.xcrawler.ejb.interceptor;

import java.io.Serializable;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xzymon.xcrawler.ejb.embryo.InvocationStatefulBean;

public class EmbryoLogging implements Serializable{
	
	private static final long serialVersionUID = -7033345082462346088L;
	private static final Logger logger = LoggerFactory.getLogger(InvocationStatefulBean.class.getSimpleName());
	
	@AroundInvoke
	public Object interceptInvocation(InvocationContext ctx) throws Exception{
		try{
			logger.info("Przechwycenie metody - target: " + ctx.getTarget());
			return ctx.proceed();
		} finally{
			logger.info("Przechwycenie zakończone");
		}
		
	}
}
