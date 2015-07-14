package com.xzymon.xcrawler.ejb.interceptor;

import java.util.Date;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DownloaderInterceptor implements java.io.Serializable {

	private static final long serialVersionUID = 7195233798810888458L;
	
	public static final Logger logger = LoggerFactory.getLogger(DownloaderInterceptor.class.getSimpleName());
	
	@AroundInvoke
	public Object interception(InvocationContext ctx) throws Exception{
		Date start, end;
		String template = String.format("Method %1$s from class %2$s", ctx.getMethod(), ctx.getTarget());
		long duration;
		
		logger.info(template + " to be invoked...");
		start = new Date();
		try{
			return ctx.proceed();
		} finally {
			end = new Date();
			duration = end.getTime() - start.getTime();
			logger.info(String.format("%1$s finished. Execution time: %2$d ms", template, duration));
		}
	}
}
