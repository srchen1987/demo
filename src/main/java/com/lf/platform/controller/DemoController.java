package com.lf.platform.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anywide.dawdler.clientplug.web.annotation.Controller;
import com.anywide.dawdler.clientplug.web.annotation.RequestMapping;
import com.anywide.dawdler.clientplug.web.annotation.RequestMapping.RequestMethod;
import com.anywide.dawdler.clientplug.web.annotation.ResponseBody;
import com.anywide.dawdler.core.result.BaseResult;
import com.anywide.dawdler.jedis.JedisOperator;
import com.anywide.dawdler.jedis.annotation.JedisInjector;

@Controller
@RequestMapping("/demo")
public class DemoController {
	Logger logger = LoggerFactory.getLogger(DemoController.class);
	
	// @RabbitInjector("rabbitmq")
	// private RabbitProvider rabbitProvider;

	@JedisInjector("redis")
	private JedisOperator JedisOperator;


	@ResponseBody
    @RequestMapping(value="/putRedis", method = RequestMethod.GET)
    public BaseResult<Void> putRedis(String key,String value) {
		JedisOperator.set(key, value);
		System.out.println("put Redis key:"+key+" value:"+value);
		logger.info("put Redis key:"+key+" value:"+value);
		return new BaseResult<>(true);
    }

	@ResponseBody
    @RequestMapping(value="/getRedis", method = RequestMethod.GET)
    public BaseResult<String> getRedis(String key) {
		String value = JedisOperator.get(key);
		System.out.println("get Redis key:"+key+" value:"+value);
		logger.info("get Redis key:"+key+" value:"+value);
		BaseResult<String> result = new BaseResult<>(true);
		result.setData(value);
		return result;
    }

}