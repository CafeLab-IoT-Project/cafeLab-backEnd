package com.cafemetrix.cafelab.management.interfaces.rest;

import com.cafemetrix.cafelab.iam.infrastructure.authorization.sfs.support.CurrentProfileIdResolver;
import com.cafemetrix.cafelab.management.domain.model.aggregates.InventoryEntry;
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

import java.time.LocalDateTime;
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
class InventoryEntriesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManagementContextFacade managementContextFacade;

    @MockBean
    private CoffeeproductionContextFacade coffeeproductionContextFacade;

    @MockBean
    private CurrentProfileIdResolver currentProfileIdResolver;

    @Test
    void createInventoryEntryReturnsCreatedWhenBodyIsValid() throws Exception {
        var coffeeLot = coffeeLot(8L, 3L);
        var inventoryEntry =
                inventoryEntry(41L, 3L, 8L, 2.5, LocalDateTime.parse("2026-05-10T08:30:00"), "Cold Brew");

        when(currentProfileIdResolver.resolveProfileId()).thenReturn(Optional.of(3L));
        when(coffeeproductionContextFacade.getCoffeeLotById(8L)).thenReturn(Optional.of(coffeeLot));
        when(managementContextFacade.createInventoryEntry(any())).thenReturn(41L);
        when(managementContextFacade.getInventoryEntryById(41L)).thenReturn(Optional.of(inventoryEntry));

        mockMvc.perform(post("/api/v1/inventory-entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "coffeeLotId": 8,
                                  "quantityUsed": 2.5,
                                  "dateUsed": "2026-05-10T08:30:00",
                                  "finalProduct": "Cold Brew"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(41))
                .andExpect(jsonPath("$.userId").value(3))
                .andExpect(jsonPath("$.coffeeLotId").value(8))
                .andExpect(jsonPath("$.quantityUsed").value(2.5))
                .andExpect(jsonPath("$.dateUsed").value("2026-05-10T08:30:00"))
                .andExpect(jsonPath("$.finalProduct").value("Cold Brew"));
    }

    @Test
    void createInventoryEntryReturnsBadRequestWhenBodyIsInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/inventory-entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "coffeeLotId": 8,
                                  "quantityUsed": 0,
                                  "dateUsed": "2026-05-10T08:30:00",
                                  "finalProduct": "Cold Brew"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error de validación"))
                .andExpect(jsonPath("$.errors[0].field").value("quantityUsed"))
                .andExpect(jsonPath("$.errors[0].message").value("La cantidad debe ser mayor que cero"));

        verifyNoInteractions(currentProfileIdResolver, managementContextFacade, coffeeproductionContextFacade);
    }

    private CoffeeLot coffeeLot(Long id, Long userId) {
        var lot = new CoffeeLot(userId, 5L, "Lote Demo", "Ar\u00e1bica", "Lavado", 1600, 50.0, "Cusco", "green", List.of("Organic"));
        ReflectionTestUtils.setField(lot, "id", id);
        return lot;
    }

    private InventoryEntry inventoryEntry(
            Long id, Long userId, Long coffeeLotId, Double quantityUsed, LocalDateTime dateUsed, String finalProduct) {
        var entry = new InventoryEntry(userId, coffeeLotId, quantityUsed, dateUsed, finalProduct);
        ReflectionTestUtils.setField(entry, "id", id);
        return entry;
    }
}
