package com.pwx.vertx;

import com.pwx.vertx.resource.DemoResource;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * Desc: ServiceVerticle
 * User: pengweixiang
 * Date: 2018-12-15
 */
public class ServiceVerticle extends AbstractVerticle
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
			if (asyncResult.failed())
			{
				System.out.println("Start server failed!");
			}
			else
			{
				System.out.println("Start server success!");
			}
		});
	}

	@Override
	public void start(Future<Void> startFuture) throws Exception
	{
		JksOptions jksOptions = new JksOptions().setPath("localhost.jks").setPassword("localhost");
		HttpServerOptions options = new HttpServerOptions().setSsl(true).setKeyStoreOptions(jksOptions);
		HttpServer server = vertx.createHttpServer(options);

		Router router = Router.router(vertx);
		// We need request bodies
		router.route().handler(BodyHandler.create());
		// We consume application/json, also produce it
		router.route().consumes("application/json").produces("application/json");
		// Register API
		registerResource(router);

		// Start the HTTPS server
		server.requestHandler(router::accept).listen(8080, asyncResult ->
		{
			if (asyncResult.failed())
			{
				System.out.println("start server failed! " + asyncResult.cause());
			}
			else
			{
				System.out.println("start server success!");
			}
		});
	}

	private void registerResource(Router router)
	{
		DemoResource demoResource = new DemoResource();
		demoResource.registerResource(router);
	}

}
