package com.xzymon.xcrawler.client.util;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class RemoteBeansUtil {
	private static Context ctx = null;
	
	public static Context getInvocationContext(){
		if(ctx==null){
			try{
				final Hashtable<String, String> props = new Hashtable<String, String>();
				props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
				ctx = new InitialContext(props);
			} catch(NamingException ex){
				
			}
		}
		return ctx;
	}
	
	
}
