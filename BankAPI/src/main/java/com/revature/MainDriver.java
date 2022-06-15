package com.revature;

import com.revature.controller.RequestMapper;
import io.javalin.Javalin;
import io.javalin.plugin.metrics.MicrometerPlugin;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

public class MainDriver {

	public static void main(String[] args) {
		PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
		registry.config().commonTags("application","BankAPI");
		
		Javalin app = Javalin.create(config -> {
			config.registerPlugin(new MicrometerPlugin(registry));
		}).start(7500);
		
		RequestMapper requestMapper = new RequestMapper();
		
		requestMapper.configureRoutes(app, registry);

	}

}
