package com.lf.platform.application;


import com.anywide.dawdler.boot.web.annotation.DawdlerBootApplication;
import com.anywide.dawdler.boot.web.starter.DawdlerWebApplication;

@DawdlerBootApplication
public class DemoApplication {
	public static void main(String[] args) throws Throwable { 
		DawdlerWebApplication.run(DemoApplication.class, args);
	} 
}