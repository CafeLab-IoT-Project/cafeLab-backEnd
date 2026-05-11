package com.cafemetrix.cafelab.production.interfaces.rest;

import com.cafemetrix.cafelab.iam.infrastructure.authorization.sfs.support.CurrentProfileIdResolver;
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
class SuppliersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoffeeproductionContextFacade coffeeproductionContextFacade;

    @MockBean
    private CurrentProfileIdResolver currentProfileIdResolver;

    @Test
    void createSupplierReturnsCreatedWhenBodyIsValid() throws Exception {
        var supplier = supplier(11L, 3L, "Café Verde SAC", "proveedor@cafelab.com", 987654321L,
                "Villa Rica", List.of("Geisha", "Caturra"));

        when(currentProfileIdResolver.resolveProfileId()).thenReturn(Optional.of(3L));
        when(coffeeproductionContextFacade.createSupplier(
                3L, "Café Verde SAC", "proveedor@cafelab.com", 987654321L, "Villa Rica",
                List.of("Geisha", "Caturra"))).thenReturn(11L);
        when(coffeeproductionContextFacade.getAllSuppliers()).thenReturn(List.of(supplier));

        mockMvc.perform(post("/api/v1/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Caf\u00e9 Verde SAC",
                                  "email": "proveedor@cafelab.com",
                                  "phone": 987654321,
                                  "location": "Villa Rica",
                                  "specialties": ["Geisha", "Caturra"]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.userId").value(3))
                .andExpect(jsonPath("$.name").value("Café Verde SAC"))
                .andExpect(jsonPath("$.email").value("proveedor@cafelab.com"))
                .andExpect(jsonPath("$.phone").value(987654321))
                .andExpect(jsonPath("$.location").value("Villa Rica"))
                .andExpect(jsonPath("$.specialties[0]").value("Geisha"));
    }

    @Test
    void createSupplierReturnsBadRequestWhenBodyIsInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Proveedor sin email",
                                  "phone": 987654321,
                                  "location": "Villa Rica",
                                  "specialties": ["Geisha"]
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error de validación"))
                .andExpect(jsonPath("$.errors[0].field").value("email"))
                .andExpect(jsonPath("$.errors[0].message").value("Email es requerido"));

        verifyNoInteractions(currentProfileIdResolver, coffeeproductionContextFacade);
    }

    @Test
    void getAllSuppliersReturnsAuthenticatedUserSuppliers() throws Exception {
        var supplier = supplier(12L, 3L, "Origen Andino", "origen@cafelab.com", 912345678L,
                "Chanchamayo", List.of("Bourbon"));

        when(currentProfileIdResolver.resolveProfileId()).thenReturn(Optional.of(3L));
        when(coffeeproductionContextFacade.getSuppliersByUserId(3L)).thenReturn(List.of(supplier));

        mockMvc.perform(get("/api/v1/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(12))
                .andExpect(jsonPath("$[0].userId").value(3))
                .andExpect(jsonPath("$[0].name").value("Origen Andino"))
                .andExpect(jsonPath("$[0].specialties[0]").value("Bourbon"));
    }

    private Supplier supplier(Long id, Long userId, String name, String email, Long phone, String location, List<String> specialties) {
        var supplier = new Supplier(userId, name, email, phone, location, specialties);
        ReflectionTestUtils.setField(supplier, "id", id);
        return supplier;
    }
}
