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
import com.anywide.dawdler.rabbitmq.consumer.Message;
import com.anywide.dawdler.rabbitmq.consumer.annotation.RabbitListener;
import com.anywide.dawdler.rabbitmq.provider.RabbitProvider;
import com.anywide.dawdler.rabbitmq.provider.annotation.RabbitInjector;

@Controller
@RequestMapping("/demo")
public class DemoController {
	Logger logger = LoggerFactory.getLogger(DemoController.class);
	
	@RabbitInjector("rabbitmq")
	private RabbitProvider rabbitProvider;

	@JedisInjector("redis")
	private JedisOperator JedisOperator;

    @ResponseBody
    @RequestMapping(value="/send", method = RequestMethod.GET)
    public BaseResult<Void> send(String message) throws Exception {
		System.out.println("send:"+message);
		logger.info("send:"+message);
		rabbitProvider.publish("", "testQueue", null, message.getBytes()); 
		return new BaseResult<>(true);
    }

	@RabbitListener(fileName = "rabbitmq",queueName = "testQueue")
    public BaseResult<Void> receive(Message message) {
		String messageBody = new String(message.getBody());
		System.out.println("receive:"+messageBody);
		if(messageBody.equals("error")){
			logger.error("receive:"+messageBody);
		}else{
			logger.info("receive:"+messageBody);
		}
		
		return new BaseResult<>(true);
    }


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