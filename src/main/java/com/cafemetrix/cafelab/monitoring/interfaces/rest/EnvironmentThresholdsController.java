package com.cafemetrix.cafelab.monitoring.interfaces.rest;

import com.cafemetrix.cafelab.monitoring.domain.exceptions.EnvironmentThresholdNotFoundException;
import com.cafemetrix.cafelab.monitoring.domain.model.commands.CreateEnvironmentThresholdCommand;
import com.cafemetrix.cafelab.monitoring.domain.model.commands.UpdateEnvironmentThresholdCommand;
import com.cafemetrix.cafelab.monitoring.domain.model.queries.GetEnvironmentThresholdByCoffeeLotIdQuery;
import com.cafemetrix.cafelab.monitoring.domain.services.EnvironmentThresholdCommandService;
import com.cafemetrix.cafelab.monitoring.domain.services.EnvironmentThresholdQueryService;
import com.cafemetrix.cafelab.monitoring.interfaces.rest.resources.CreateEnvironmentThresholdResource;
import com.cafemetrix.cafelab.monitoring.interfaces.rest.resources.EnvironmentThresholdResource;
import com.cafemetrix.cafelab.monitoring.interfaces.rest.transform.EnvironmentThresholdResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/environment-thresholds", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Environment Thresholds", description = "Management endpoints for IoT sensor safe thresholds per coffee lot")
public class EnvironmentThresholdsController {

    private final EnvironmentThresholdCommandService thresholdCommandService;
    private final EnvironmentThresholdQueryService thresholdQueryService;

    public EnvironmentThresholdsController(EnvironmentThresholdCommandService thresholdCommandService, EnvironmentThresholdQueryService thresholdQueryService) {
        this.thresholdCommandService = thresholdCommandService;
        this.thresholdQueryService = thresholdQueryService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EnvironmentThresholdResource> createThreshold(@RequestBody CreateEnvironmentThresholdResource resource) {
        var command = new CreateEnvironmentThresholdCommand(resource.coffeeLotId(), resource.minTemperature(), resource.maxTemperature(), resource.minHumidity(), resource.maxHumidity());
        var threshold = thresholdCommandService.handle(command);

        if (threshold.isEmpty()) {
            throw new IllegalStateException("No se pudo registrar la configuración de umbrales");
        }

        return new ResponseEntity<>(EnvironmentThresholdResourceFromEntityAssembler.toResourceFromEntity(threshold.get()), HttpStatus.CREATED);
    }

    @GetMapping("/coffee-lot/{coffeeLotId}")
    public ResponseEntity<EnvironmentThresholdResource> getThresholdByCoffeeLotId(@PathVariable Long coffeeLotId) {
        var query = new GetEnvironmentThresholdByCoffeeLotIdQuery(coffeeLotId);
        var threshold = thresholdQueryService.handle(query);

        if (threshold.isEmpty()) {
            throw new EnvironmentThresholdNotFoundException(coffeeLotId);
        }

        return ResponseEntity.ok(EnvironmentThresholdResourceFromEntityAssembler.toResourceFromEntity(threshold.get()));
    }

    @PutMapping(value = "/coffee-lot/{coffeeLotId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EnvironmentThresholdResource> updateThreshold(@PathVariable Long coffeeLotId, @RequestBody CreateEnvironmentThresholdResource resource) {
        var command = new UpdateEnvironmentThresholdCommand(coffeeLotId, resource.minTemperature(), resource.maxTemperature(), resource.minHumidity(), resource.maxHumidity());
        var updatedThreshold = thresholdCommandService.handle(command);

        if (updatedThreshold.isEmpty()) {
            throw new EnvironmentThresholdNotFoundException(coffeeLotId);
        }

        return ResponseEntity.ok(EnvironmentThresholdResourceFromEntityAssembler.toResourceFromEntity(updatedThreshold.get()));
    }
}