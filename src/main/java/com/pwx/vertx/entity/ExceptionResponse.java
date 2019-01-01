package com.pwx.vertx.entity;

/**
 * Desc:exception response
 * Creater: pengweixiang
 * Date: 2018-12-16
 */
public class ExceptionResponse
{
	private String code;
	private String msg = "";

	public ExceptionResponse()
	{
	}

	public ExceptionResponse(String code, String msg)
	{
		this.code = code;
		this.msg = msg;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	@Override
	public String toString()
	{
		return "ExceptionResponse{" + "code=" + code + ", msg='" + msg + '\'' + '}';
	}
}
