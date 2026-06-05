package com.cafemetrix.cafelab.monitoring.interfaces.rest;

import com.cafemetrix.cafelab.monitoring.domain.exceptions.EnvironmentThresholdNotFoundException;
import com.cafemetrix.cafelab.monitoring.domain.exceptions.EnvironmentThresholdAlreadyExistsException;
import com.cafemetrix.cafelab.monitoring.domain.exceptions.TelemetryRecordNotFoundException;
import com.cafemetrix.cafelab.shared.interfaces.rest.resources.MessageResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {EnvironmentThresholdsController.class, TelemetryRecordsController.class})
public class MonitoringExceptionHandler {

    @ExceptionHandler(EnvironmentThresholdNotFoundException.class)
    public ResponseEntity<MessageResource> handleEnvironmentThresholdNotFound(EnvironmentThresholdNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResource(exception.getMessage()));
    }

    @ExceptionHandler(EnvironmentThresholdAlreadyExistsException.class)
    public ResponseEntity<MessageResource> handleEnvironmentThresholdAlreadyExists(EnvironmentThresholdAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResource(exception.getMessage()));
    }

    @ExceptionHandler(TelemetryRecordNotFoundException.class)
    public ResponseEntity<MessageResource> handleTelemetryRecordNotFound(TelemetryRecordNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResource(exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MessageResource> handleIllegalArgument(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResource(exception.getMessage()));
    }
}