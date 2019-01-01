package com.pwx.vertx.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * Desc: JSONUtil
 * Creater: pengweixiang
 * Date: 2018-12-17
 */
public class JSONUtil
{
	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * 将对象转换成json字符串
	 * @param data data
	 * @return jsonString
	 */
	public static String convertObject2JsonStr(Object data)
	{
		String jsonString = null;
		try
		{
			jsonString = mapper.writeValueAsString(data);
		}
		catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}
		return jsonString;
	}

	/**
	 * 将json结果集转化为对象
	 * @param jsonString jsonString
	 * @param classType classType
	 * @param <T> T
	 * @return object
	 */
	public static <T>  T convertJsonStr2Object(String jsonString, Class<T> classType)
	{
		T object = null;
		try
		{
			object = mapper.readValue(jsonString, classType);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return object;
	}

	/**
	 * 将json数据转换成list
	 * @param jsonString jsonString
	 * @param classType classType
	 * @param <T> T
	 * @return list
	 */
	public static <T> List<T> convertJsonStr2List(String jsonString, Class<T> classType)
	{
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, classType);
		List<T> list = null;
		try
		{
			list = mapper.readValue(jsonString, javaType);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return list;
	}
}
