package com.cafemetrix.cafelab.production.interfaces.rest;

import com.cafemetrix.cafelab.iam.infrastructure.authorization.sfs.support.CurrentProfileIdResolver;
import com.cafemetrix.cafelab.production.domain.model.aggregates.CoffeeLot;
import com.cafemetrix.cafelab.production.domain.model.aggregates.Supplier;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class CoffeeLotsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoffeeproductionContextFacade coffeeproductionContextFacade;

    @MockBean
    private CurrentProfileIdResolver currentProfileIdResolver;

    @Test
    void createCoffeeLotReturnsCreatedWhenBodyIsValid() throws Exception {
        var supplier = supplier(8L, 3L);
        var coffeeLot = coffeeLot(21L, 3L, 8L, "Lote Tarrazu", "Ar\u00e1bica", "Lavado",
                1800, 69.5, "Costa Rica", "green", List.of("Organic"));

        when(currentProfileIdResolver.resolveProfileId()).thenReturn(Optional.of(3L));
        when(coffeeproductionContextFacade.getSupplierById(8L)).thenReturn(Optional.of(supplier));
        when(coffeeproductionContextFacade.createCoffeeLot(
                3L, 8L, "Lote Tarrazu", "Ar\u00e1bica", "Lavado", 1800, 69.5, "Costa Rica",
                "green", List.of("Organic"))).thenReturn(21L);
        when(coffeeproductionContextFacade.getAllCoffeeLots()).thenReturn(List.of(coffeeLot));

        mockMvc.perform(post("/api/v1/coffee-lots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "supplier_id": 8,
                                  "lot_name": "Lote Tarrazu",
                                  "coffee_type": "Ar\u00e1bica",
                                  "processing_method": "Lavado",
                                  "altitude": 1800,
                                  "weight": 69.5,
                                  "origin": "Costa Rica",
                                  "status": "green",
                                  "certifications": ["Organic"]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(21))
                .andExpect(jsonPath("$.userId").value(3))
                .andExpect(jsonPath("$.supplierId").value(8))
                .andExpect(jsonPath("$.lotName").value("Lote Tarrazu"))
                .andExpect(jsonPath("$.weight").value(69.5))
                .andExpect(jsonPath("$.certifications[0]").value("Organic"));
    }

    @Test
    void createCoffeeLotReturnsBadRequestWhenBodyIsInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/coffee-lots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "supplier_id": 8,
                                  "lot_name": "Lote Tarrazu",
                                  "coffee_type": "Ar\u00e1bica",
                                  "processing_method": "Lavado",
                                  "altitude": 1800,
                                  "weight": 0,
                                  "origin": "Costa Rica",
                                  "status": "green",
                                  "certifications": ["Organic"]
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error de validación"))
                .andExpect(jsonPath("$.errors[0].field").value("weight"))
                .andExpect(jsonPath("$.errors[0].message").value("El peso debe ser un número positivo"));

        verifyNoInteractions(currentProfileIdResolver, coffeeproductionContextFacade);
    }

    @Test
    void getAllCoffeeLotsReturnsAuthenticatedUserLots() throws Exception {
        var coffeeLot = coffeeLot(22L, 3L, 8L, "Lote Cusco", "Robusta", "Honey",
                1750, 42.0, "Cusco", "roasted", List.of("Fair Trade"));

        when(currentProfileIdResolver.resolveProfileId()).thenReturn(Optional.of(3L));
        when(coffeeproductionContextFacade.getCoffeeLotsByUserId(3L)).thenReturn(List.of(coffeeLot));

        mockMvc.perform(get("/api/v1/coffee-lots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(22))
                .andExpect(jsonPath("$[0].supplierId").value(8))
                .andExpect(jsonPath("$[0].lotName").value("Lote Cusco"))
                .andExpect(jsonPath("$[0].status").value("roasted"));
    }

    private Supplier supplier(Long id, Long userId) {
        var supplier = new Supplier(userId, "Proveedor", "proveedor@cafelab.com", 999999999L, "Jaén", List.of("Geisha"));
        ReflectionTestUtils.setField(supplier, "id", id);
        return supplier;
    }

    private CoffeeLot coffeeLot(Long id, Long userId, Long supplierId, String lotName, String coffeeType,
                                String processingMethod, Integer altitude, Double weight,
                                String origin, String status, List<String> certifications) {
        var coffeeLot = new CoffeeLot(userId, supplierId, lotName, coffeeType, processingMethod, altitude, weight, origin, status, certifications);
        ReflectionTestUtils.setField(coffeeLot, "id", id);
        return coffeeLot;
    }
}
