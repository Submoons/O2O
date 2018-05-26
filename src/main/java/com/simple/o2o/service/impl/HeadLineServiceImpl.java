package com.simple.o2o.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.o2o.cache.JedisUtil;
import com.simple.o2o.dao.HeadLineDao;
import com.simple.o2o.entity.HeadLine;
import com.simple.o2o.exceptions.HeadLineOperationException;
import com.simple.o2o.service.HeadLineService;

@Service
public class HeadLineServiceImpl implements HeadLineService{
	@Autowired
	private HeadLineDao headLineDao;
	@Autowired
	private JedisUtil.Keys jedisKeys;
	@Autowired
	private JedisUtil.Strings jedisStrings;
	
	@Override
	@Transactional
	public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException {
		//定义redis的key前缀
		String key = HLLISTKEY;
		//定义接收对象
		List<HeadLine> headLineList = null;
		//定义jackson数据转换操作类
		ObjectMapper mapper = new ObjectMapper();
		//拼接出redis的key
		if(headLineCondition != null && headLineCondition.getEnableStatus() != null){
			key = key + "_" + headLineCondition.getEnableStatus();
		}
		
		//判断key是否存在
		if( !jedisKeys.exists(key) ){
			//不存在，则从数据库里取出数据
			headLineList = headLineDao.queryHeadLine(headLineCondition);
			//将相关实体类集合转换成string，存入redis里面的对应key中
			String jsonString;
			try{
				jsonString = mapper.writeValueAsString(headLineList);
			}catch(JsonProcessingException e){
				e.printStackTrace();
				throw new HeadLineOperationException(e.getMessage());
			}
			jedisStrings.set(key, jsonString);
		}else{
			//若存在，则直接从redis里取出相应的数据
			String jsonString = jedisStrings.get(key);
			//指定要将string转换成的集合类型
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, HeadLine.class);
			try{
				//将相关key对应的value里的string转换成对象的实体类集合
				headLineList = mapper.readValue(jsonString, javaType);
			}catch(JsonParseException e){
				e.printStackTrace();
				throw new HeadLineOperationException(e.getMessage());
			} catch (JsonMappingException e) {
				e.printStackTrace();
				throw new HeadLineOperationException(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				throw new HeadLineOperationException(e.getMessage());
			}
		}
		return headLineList;
	}
	
}
