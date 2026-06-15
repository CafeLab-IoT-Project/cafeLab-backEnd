package com.cafemetrix.cafelab.monitoring.interfaces.rest;

import com.cafemetrix.cafelab.monitoring.domain.model.aggregates.EnvironmentThreshold;
import com.cafemetrix.cafelab.monitoring.domain.model.commands.CreateEnvironmentThresholdCommand;
import com.cafemetrix.cafelab.monitoring.domain.model.commands.UpdateEnvironmentThresholdCommand;
import com.cafemetrix.cafelab.monitoring.domain.model.queries.GetEnvironmentThresholdByCoffeeLotIdQuery;
import com.cafemetrix.cafelab.monitoring.domain.services.EnvironmentThresholdCommandService;
import com.cafemetrix.cafelab.monitoring.domain.services.EnvironmentThresholdQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class EnvironmentThresholdsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnvironmentThresholdCommandService thresholdCommandService;

    @MockBean
    private EnvironmentThresholdQueryService thresholdQueryService;

    @Test
    void createThresholdReturnsCreatedWhenBodyIsValid() throws Exception {
        var threshold = environmentThreshold(1L, 4L, 18.5, 24.0, 45.0, 60.0, 10);

        when(thresholdCommandService.handle(any(CreateEnvironmentThresholdCommand.class)))
                .thenReturn(Optional.of(threshold));

        mockMvc.perform(post("/api/v1/environment-thresholds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "coffeeLotId": 4,
                                  "minTemperature": 18.5,
                                  "maxTemperature": 24.0,
                                  "minHumidity": 45.0,
                                  "maxHumidity": 60.0,
                                  "syncIntervalSeconds": 10
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.coffeeLotId").value(4))
                .andExpect(jsonPath("$.minTemperature").value(18.5))
                .andExpect(jsonPath("$.maxTemperature").value(24.0))
                .andExpect(jsonPath("$.minHumidity").value(45.0))
                .andExpect(jsonPath("$.maxHumidity").value(60.0))
                .andExpect(jsonPath("$.syncIntervalSeconds").value(10));
    }

    @Test
    void getThresholdByCoffeeLotIdReturnsThresholdWhenItExists() throws Exception {
        var threshold = environmentThreshold(2L, 8L, 18.0, 22.0, 50.0, 65.0, 15);

        when(thresholdQueryService.handle(new GetEnvironmentThresholdByCoffeeLotIdQuery(8L)))
                .thenReturn(Optional.of(threshold));

        mockMvc.perform(get("/api/v1/environment-thresholds/coffee-lot/8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coffeeLotId").value(8))
                .andExpect(jsonPath("$.minTemperature").value(18.0))
                .andExpect(jsonPath("$.maxHumidity").value(65.0));
    }

    @Test
    void getThresholdByCoffeeLotIdReturnsNotFoundWhenThresholdDoesNotExist() throws Exception {
        when(thresholdQueryService.handle(new GetEnvironmentThresholdByCoffeeLotIdQuery(404L)))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/environment-thresholds/coffee-lot/404"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Configuración de umbrales ambientales no encontrada para el lote con ID: 404"));
    }

    @Test
    void updateThresholdReturnsUpdatedThresholdWhenItExists() throws Exception {
        var threshold = environmentThreshold(3L, 6L, 19.0, 23.0, 48.0, 62.0, 20);

        when(thresholdCommandService.handle(any(UpdateEnvironmentThresholdCommand.class)))
                .thenReturn(Optional.of(threshold));

        mockMvc.perform(put("/api/v1/environment-thresholds/coffee-lot/6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "coffeeLotId": 6,
                                  "minTemperature": 19.0,
                                  "maxTemperature": 23.0,
                                  "minHumidity": 48.0,
                                  "maxHumidity": 62.0,
                                  "syncIntervalSeconds": 20
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coffeeLotId").value(6))
                .andExpect(jsonPath("$.syncIntervalSeconds").value(20));

        verify(thresholdCommandService).handle(
                new UpdateEnvironmentThresholdCommand(6L, 19.0, 23.0, 48.0, 62.0, 20));
    }

    @Test
    void updateThresholdReturnsNotFoundWhenThresholdDoesNotExist() throws Exception {
        when(thresholdCommandService.handle(any(UpdateEnvironmentThresholdCommand.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/environment-thresholds/coffee-lot/77")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "coffeeLotId": 77,
                                  "minTemperature": 19.0,
                                  "maxTemperature": 23.0,
                                  "minHumidity": 48.0,
                                  "maxHumidity": 62.0,
                                  "syncIntervalSeconds": 20
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Configuración de umbrales ambientales no encontrada para el lote con ID: 77"));
    }

    private EnvironmentThreshold environmentThreshold(
            Long id,
            Long coffeeLotId,
            Double minTemperature,
            Double maxTemperature,
            Double minHumidity,
            Double maxHumidity,
            Integer syncIntervalSeconds
    ) {
        var command = new CreateEnvironmentThresholdCommand(
                coffeeLotId, minTemperature, maxTemperature, minHumidity, maxHumidity, syncIntervalSeconds);
        var threshold = new EnvironmentThreshold(command);
        ReflectionTestUtils.setField(threshold, "id", id);
        return threshold;
    }
}
