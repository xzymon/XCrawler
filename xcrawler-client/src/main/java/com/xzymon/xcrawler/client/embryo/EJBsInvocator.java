package com.xzymon.xcrawler.client.embryo;

import javax.naming.Context;
import javax.naming.NamingException;

import com.xzymon.xcrawler.client.util.RemoteBeansUtil;
import com.xzymon.xcrawler.ejb.embryo.InvocationStatefulBean;
import com.xzymon.xcrawler.ejb.embryo.InvocationStatelessBean;
import com.xzymon.xcrawler.ejb.embryo.RemoteInvocationStateful;
import com.xzymon.xcrawler.ejb.embryo.RemoteInvocationStateless;

public class EJBsInvocator {
	private Context context;

	public static final void main(String[] args) throws NamingException {
		EJBsInvocator invocator = new EJBsInvocator();
		System.out.format("Sth%n");
		String sampleString = "AAA";
		invocator.invokeStateful(sampleString);
		invocator.invokeStateless(sampleString);
	}

	public String invokeStateful(String param) throws NamingException{
		System.out.format("Invoking stateful bean with param \"%1$S\"%n", param);
		String result = lookupRemoteStateful().sample(param);
		System.out.format("The result is \"%1$s\"%n", result);
		return result;
	}
	
	public RemoteInvocationStateful lookupRemoteStateful() throws NamingException {
		context = RemoteBeansUtil.getInvocationContext();
		String appName = "xcrawler";
		String moduleName = "xcrawler-ejb";
		String distinctName = "";
		String beanName = InvocationStatefulBean.class.getSimpleName();
		String interfaceName = RemoteInvocationStateful.class.getName();
		String lookupName = String.format("ejb:%1$s/%2$s/%3$s/%4$s!%5$s?stateful",
				appName, moduleName, distinctName, beanName, interfaceName);
		return (RemoteInvocationStateful)context.lookup(lookupName);
	}
	
	public String invokeStateless(String param) throws NamingException{
		System.out.format("Invoking stateless bean with param \"%1$S\"%n", param);
		String result = lookupRemoteStateless().sample(param);
		System.out.format("The result is \"%1$s\"%n", result);
		return result;
	}
	
	public RemoteInvocationStateless lookupRemoteStateless() throws NamingException {
		context = RemoteBeansUtil.getInvocationContext();
		String appName = "xcrawler";
		String moduleName = "xcrawler-ejb";
		String distinctName = "";
		String beanName = InvocationStatelessBean.class.getSimpleName();
		String interfaceName = RemoteInvocationStateless.class.getName();
		String lookupName = String.format("ejb:%1$s/%2$s/%3$s/%4$s!%5$s",
				appName, moduleName, distinctName, beanName, interfaceName);
		return (RemoteInvocationStateless)context.lookup(lookupName);
	}
}
