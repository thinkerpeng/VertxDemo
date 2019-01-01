package com.pwx.vertx.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Desc:
 * Creater: pengweixiang
 * Date: 2018-12-16
 */
public class ResourceEntity
{
	@JsonProperty("resource_id")
	private String id;

	@JsonProperty("resource_name")
	private String resourceName;

	public ResourceEntity()
	{
	}

	public ResourceEntity(String id, String resourceName)
	{
		this.id = id;
		this.resourceName = resourceName;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getResourceName()
	{
		return resourceName;
	}

	public void setResourceName(String resourceName)
	{
		this.resourceName = resourceName;
	}
}
