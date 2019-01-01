package com.pwx.vertx;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

/**
 * Desc:main function
 * User: pengweixiang
 * Date: 2018-12-10
 */
public class AppMain
{
	private static final int CORE_NUM = 2;

	public static void main(String[] args)
	{
		Vertx vertx = Vertx.vertx();
		DeploymentOptions deploymentOptions = new DeploymentOptions()
				.setWorker(true)
				.setInstances(CORE_NUM)
				.setWorkerPoolSize(CORE_NUM)
				.setWorkerPoolName("vertx-thread-pool");

		vertx.deployVerticle(ServiceVerticle.class, deploymentOptions, asyncResult ->
		{
			if (asyncResult.succeeded())
			{
				System.out.println("Start server Success");
			}
			else
			{
				System.out.println("Start server failed");
			}
		});
	}
}
