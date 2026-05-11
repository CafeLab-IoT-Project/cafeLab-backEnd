package com.cafemetrix.cafelab.management.interfaces.rest;

import com.cafemetrix.cafelab.iam.infrastructure.authorization.sfs.support.CurrentProfileIdResolver;
import com.cafemetrix.cafelab.management.domain.model.aggregates.ProductionCostRecord;
import com.cafemetrix.cafelab.management.domain.model.commands.CreateProductionCostRecordCommand;
import com.cafemetrix.cafelab.management.domain.model.support.ProductionCostTotalsCalculator;
import com.cafemetrix.cafelab.management.interfaces.acl.ManagementContextFacade;
import com.cafemetrix.cafelab.production.domain.model.aggregates.CoffeeLot;
import com.cafemetrix.cafelab.production.interfaces.acl.CoffeeproductionContextFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ProductionCostRecordsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManagementContextFacade managementContextFacade;

    @MockBean
    private CoffeeproductionContextFacade coffeeproductionContextFacade;

    @MockBean
    private CurrentProfileIdResolver currentProfileIdResolver;

    @Test
    void createProductionCostRecordReturnsCreatedWhenBodyIsValid() throws Exception {
        var coffeeLot = coffeeLot(8L, 3L);
        var record = productionCostRecord(55L, 3L, coffeeLot);

        when(currentProfileIdResolver.resolveProfileId()).thenReturn(Optional.of(3L));
        when(coffeeproductionContextFacade.getCoffeeLotById(8L)).thenReturn(Optional.of(coffeeLot));
        when(managementContextFacade.createProductionCostRecord(any())).thenReturn(55L);
        when(managementContextFacade.getProductionCostRecordByIdAndUserId(55L, 3L)).thenReturn(Optional.of(record));

        mockMvc.perform(post("/api/v1/production-cost-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "coffeeLotId": 8,
                                  "currency": "PEN",
                                  "totalKg": 20.0,
                                  "marginPercent": 45.0,
                                  "rawMaterialsCost": 120.0,
                                  "laborCost": 40.0,
                                  "transportCost": 15.0,
                                  "storageCost": 5.0,
                                  "processingCost": 25.0,
                                  "otherIndirectCosts": 10.0
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(55))
                .andExpect(jsonPath("$.userId").value(3))
                .andExpect(jsonPath("$.coffeeLotId").value(8))
                .andExpect(jsonPath("$.currency").value("PEN"))
                .andExpect(jsonPath("$.totalCost").value(215.0))
                .andExpect(jsonPath("$.costPerKg").value(10.75))
                .andExpect(jsonPath("$.status").value("registrado"))
                .andExpect(jsonPath("$.reason").value("registrado"));
    }

    @Test
    void createProductionCostRecordReturnsBadRequestWhenBodyIsInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/production-cost-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "coffeeLotId": 8,
                                  "currency": "EUR",
                                  "totalKg": 20.0,
                                  "marginPercent": 45.0,
                                  "rawMaterialsCost": 120.0,
                                  "laborCost": 40.0,
                                  "transportCost": 15.0,
                                  "storageCost": 5.0,
                                  "processingCost": 25.0,
                                  "otherIndirectCosts": 10.0
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error de validación"))
                .andExpect(jsonPath("$.errors[0].field").value("currency"))
                .andExpect(jsonPath("$.errors[0].message").value("Moneda debe ser PEN o USD"));

        verifyNoInteractions(currentProfileIdResolver, managementContextFacade, coffeeproductionContextFacade);
    }

    private CoffeeLot coffeeLot(Long id, Long userId) {
        var lot = new CoffeeLot(userId, 5L, "Lote Costos", "Ar\u00e1bica", "Lavado", 1650, 80.0, "Jun\u00edn", "green", List.of("Organic"));
        ReflectionTestUtils.setField(lot, "id", id);
        return lot;
    }

    private ProductionCostRecord productionCostRecord(Long id, Long userId, CoffeeLot lot) {
        var totals = ProductionCostTotalsCalculator.compute(20.0, 45.0, 120.0, 40.0, 15.0, 5.0, 25.0, 10.0);
        var command =
                new CreateProductionCostRecordCommand(
                        userId,
                        lot.getId(),
                        lot.getLotName(),
                        lot.getCoffeeType(),
                        "PEN",
                        20.0,
                        45.0,
                        120.0,
                        40.0,
                        15.0,
                        5.0,
                        25.0,
                        10.0,
                        totals.totalDirectCost(),
                        totals.totalIndirectCost(),
                        totals.totalCost(),
                        totals.costPerKg(),
                        totals.suggestedPrice(),
                        totals.potentialMargin());
        var record = new ProductionCostRecord(command);
        ReflectionTestUtils.setField(record, "id", id);
        ReflectionTestUtils.setField(record, "createdAt", Date.from(Instant.parse("2026-05-10T13:00:00Z")));
        ReflectionTestUtils.setField(record, "updatedAt", Date.from(Instant.parse("2026-05-10T13:05:00Z")));
        return record;
    }
}
