package com.pwx.vertx.resource;

import com.pwx.vertx.common.Constant;
import com.pwx.vertx.common.JSONUtil;
import com.pwx.vertx.entity.ResourceEntity;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Desc:
 * User: pengweixiang
 * Date: 2018-12-16
 */
public class DemoResource extends BaseResource
{
	private final static String INPUTPARAM_ERROR = "400001";

	private final static String INSTANCE_NOT_FOUND = "404001";

	private static List<ResourceEntity> resources = new ArrayList<>();
	static
	{
		resources.add(new ResourceEntity("001", "resource001"));
		resources.add(new ResourceEntity("002", "resource002"));
		resources.add(new ResourceEntity("003", "resource003"));
		resources.add(new ResourceEntity("004", "resource004"));
		resources.add(new ResourceEntity("005", "resource005"));
		resources.add(new ResourceEntity("006", "resource006"));
	}

	@Override
	public void registerResource(Router router)
	{
		router.get(VERSION +"/resource/:resource_id").handler(this::queryResource).failureHandler(this::failedHandler);
		router.get(VERSION + "/resource").handler(this::queryAllResource).failureHandler(this::failedHandler);
		router.post(VERSION + "/resource").handler(this::createResource).failureHandler(this::failedHandler);
	}

	/**
	 * 创建资源实例
	 * @param routingContext routingContext
	 */
	private void createResource(RoutingContext routingContext)
	{
		String bodyString = routingContext.getBodyAsString();
		if (StringUtils.isEmpty(bodyString))
		{
			throw new RuntimeException(INPUTPARAM_ERROR);
		}

		ResourceEntity resource = JSONUtil.convertJsonStr2Object(bodyString, ResourceEntity.class);
		if (Objects.isNull(resource))
		{
			throw new RuntimeException(INPUTPARAM_ERROR);
		}

		routingContext.vertx().executeBlocking(futrue ->
		{
			resources.add(resource);
			futrue.complete();
		},false, asyncResult ->
		{
			if (asyncResult.failed())
			{
				routingContext.fail(asyncResult.cause());
				return;
			}
			sendResponseNoContent(routingContext);
		});
	}

	/**
	 * 查询所有资源实例
	 * @param routingContext routingContext
	 */
	private void queryAllResource(RoutingContext routingContext)
	{
		routingContext.vertx().executeBlocking(future ->
		{
			//query data from DB
			future.complete();
		}, false, asyncResult ->
		{
			if (asyncResult.failed())
			{
				routingContext.fail(asyncResult.cause());
				return;
			}
			sendResponseData(routingContext, JSONUtil.convertObject2JsonStr(resources));
		});
	}

	/**
	 * 查询指定资源实例
	 * @param routingContext routingContext
	 */
	private void queryResource(RoutingContext routingContext)
	{
		String instanceId = routingContext.request().getParam(Constant.RESOURCE_ID);
		List<String> resourceIds =
				resources.stream().map(resourceEntity ->resourceEntity.getId()).collect(Collectors.toList());
		if (!resourceIds.contains(instanceId))
		{
			sendResponseCustomError(routingContext, INSTANCE_NOT_FOUND);
			return;
		}

		routingContext.vertx().executeBlocking(future ->
		{
			//query data from DB
			ResourceEntity resource =
					resources.stream().filter(resourceEntity -> resourceEntity.getId().equals(instanceId))
					.collect(Collectors.toList()).get(0);
			future.complete(resource);
		}, false, asyncResult ->
		{
			if (asyncResult.failed())
			{
				routingContext.fail(asyncResult.cause());
				return;
			}
			sendResponseData(routingContext, JSONUtil.convertObject2JsonStr(asyncResult.result()));
		});
	}


}
