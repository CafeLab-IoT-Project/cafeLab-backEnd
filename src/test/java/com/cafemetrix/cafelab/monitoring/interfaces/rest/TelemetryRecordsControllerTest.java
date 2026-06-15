package com.cafemetrix.cafelab.monitoring.interfaces.rest;

import com.cafemetrix.cafelab.monitoring.domain.model.aggregates.TelemetryRecord;
import com.cafemetrix.cafelab.monitoring.domain.model.commands.CreateTelemetryRecordCommand;
import com.cafemetrix.cafelab.monitoring.domain.model.queries.GetTelemetryRecordsByCoffeeLotIdQuery;
import com.cafemetrix.cafelab.monitoring.domain.services.TelemetryRecordCommandService;
import com.cafemetrix.cafelab.monitoring.domain.services.TelemetryRecordQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class TelemetryRecordsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TelemetryRecordCommandService telemetryCommandService;

    @MockBean
    private TelemetryRecordQueryService telemetryQueryService;

    @Test
    void recordTelemetryReturnsCreatedWhenBodyIsValid() throws Exception {
        var timestamp = LocalDateTime.of(2026, 6, 15, 10, 30);
        var record = telemetryRecord(1L, 5L, 20.5, 58.0, timestamp);

        when(telemetryCommandService.handle(any(CreateTelemetryRecordCommand.class)))
                .thenReturn(Optional.of(record));

        mockMvc.perform(post("/api/v1/telemetry-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "coffeeLotId": 5,
                                  "temperature": 20.5,
                                  "humidity": 58.0,
                                  "timestamp": "2026-06-15T10:30:00"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.coffeeLotId").value(5))
                .andExpect(jsonPath("$.temperature").value(20.5))
                .andExpect(jsonPath("$.humidity").value(58.0))
                .andExpect(jsonPath("$.timestamp").value("2026-06-15T10:30:00"));

        verify(telemetryCommandService).handle(new CreateTelemetryRecordCommand(5L, 20.5, 58.0, timestamp));
    }

    @Test
    void getTelemetryHistoryReturnsRecordsForCoffeeLot() throws Exception {
        var timestamp = LocalDateTime.of(2026, 6, 15, 11, 0);
        var record = telemetryRecord(2L, 7L, 19.8, 55.2, timestamp);

        when(telemetryQueryService.handle(new GetTelemetryRecordsByCoffeeLotIdQuery(7L)))
                .thenReturn(List.of(record));

        mockMvc.perform(get("/api/v1/telemetry-records/coffee-lot/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].coffeeLotId").value(7))
                .andExpect(jsonPath("$[0].temperature").value(19.8))
                .andExpect(jsonPath("$[0].humidity").value(55.2));
    }

    @Test
    void getTelemetryHistoryReturnsEmptyListWhenNoRecordsExist() throws Exception {
        when(telemetryQueryService.handle(new GetTelemetryRecordsByCoffeeLotIdQuery(99L)))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/telemetry-records/coffee-lot/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    private TelemetryRecord telemetryRecord(
            Long id,
            Long coffeeLotId,
            Double temperature,
            Double humidity,
            LocalDateTime timestamp
    ) {
        var command = new CreateTelemetryRecordCommand(coffeeLotId, temperature, humidity, timestamp);
        var record = new TelemetryRecord(command);
        ReflectionTestUtils.setField(record, "id", id);
        return record;
    }
}
