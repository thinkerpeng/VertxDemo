package com.pwx.vertx.resource;

import com.pwx.vertx.common.JSONUtil;
import com.pwx.vertx.entity.ExceptionResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.ValidationException;

/**
 * Desc: resource 基类，所有的resource必须继承此类
 * User: pengweixiang
 * Date: 2018-12-16
 */
public abstract class BaseResource
{
	public final String VERSION = "/v1.0";

	/**
	 * 注册路由方法
	 * @param router router
	 */
	public abstract void registerResource(Router router);

	/**
	 * 无内容返回
	 * @param routingContext routingContext
	 */
	public void sendResponseNoContent(RoutingContext routingContext)
	{
		routingContext.response()
				.putHeader("Content-Type", "application/json")
				.setStatusCode(HttpResponseStatus.NO_CONTENT.code())
				.end();
	}

	/**
	 * 返回正常请求
	 * @param routingContext routingContext
	 * @param jsonStr jsonStr
	 */
	public void sendResponseData(RoutingContext routingContext, String jsonStr)
	{
		routingContext.response()
				.putHeader("Content-Type", "application/json")
				.setStatusCode(HttpResponseStatus.OK.code())
				.end(jsonStr);
	}

	/**
	 * 返回异常信息
	 * @param routingContext routingContext
	 */
	public void sendResponseCustomError(RoutingContext routingContext, String errorCode)
	{
		routingContext.response()
				.putHeader("Content-Type", "application/json")
				.setStatusCode(Integer.parseInt(StringUtils.substring(errorCode, 0, 3)))
				.end(JSONUtil.convertObject2JsonStr(new ExceptionResponse(errorCode, null)));
	}

	/**
	 * 异常处理方法
	 * @param routingContext routingContext
	 */
	public void failedHandler(RoutingContext routingContext)
	{
		HttpServerResponse response = routingContext.response();
		Throwable failure = routingContext.failure();
		if (failure instanceof ValidationException)
		{
			String errorMsg = failure.getMessage();
			response.setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end(errorMsg);
		}
		else if (failure instanceof RuntimeException)
		{
			String errorCode = failure.getMessage();
			response.setStatusCode(Integer.parseInt(StringUtils.substring(errorCode, 0, 3)))
					.end(JSONUtil.convertObject2JsonStr(new ExceptionResponse(errorCode, null)));
		}
		else
		{
			response.setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end("internal error!");
		}
	}
}
