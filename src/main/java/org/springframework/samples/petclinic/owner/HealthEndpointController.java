package org.springframework.samples.petclinic.owner;

import org.springframework.boot.health.actuate.endpoint.HealthEndpoint;
import org.springframework.boot.health.contributor.Status;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
class HealthEndpointController {

	private final HealthEndpoint healthEndpoint;

	public HealthEndpointController(HealthEndpoint healthEndpoint) {
		this.healthEndpoint = healthEndpoint;
	}

	// Exposes Kubernetes-compatible liveness endpoint by delegating to Spring Boot
	// Actuator
	@GetMapping("/livez")
	public Map<String, String> livez() {
		Status status = healthEndpoint.healthForPath("liveness").getStatus();
		return Map.of("status", status.getCode());
	}

	// Exposes Kubernetes-compatible readiness endpoint by delegating to Spring Boot
	// Actuator
	@GetMapping("/readyz")
	public Map<String, String> readyz() {
		Status status = healthEndpoint.healthForPath("readiness").getStatus();
		return Map.of("status", status.getCode());
	}

}
