package com.cafemetrix.cafelab.iam.interfaces.rest;

import com.cafemetrix.cafelab.iam.domain.model.aggregates.User;
import com.cafemetrix.cafelab.iam.domain.model.commands.SignInCommand;
import com.cafemetrix.cafelab.iam.domain.services.UserCommandService;
import org.apache.commons.lang3.tuple.ImmutablePair;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserCommandService userCommandService;

    @Test
    void signInReturnsAuthenticatedUserWhenCredentialsAreValid() throws Exception {
        var user = new User("tester@cafelab.com", "hashed-password", "ROLE_USER");
        ReflectionTestUtils.setField(user, "id", 7L);

        when(userCommandService.handle(any(SignInCommand.class)))
                .thenReturn(Optional.of(ImmutablePair.of(user, "jwt-test-token")));

        mockMvc.perform(post("/api/v1/authentication/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "tester@cafelab.com",
                                  "password": "secret123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.email").value("tester@cafelab.com"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"))
                .andExpect(jsonPath("$.token").value("jwt-test-token"));

        verify(userCommandService).handle(new SignInCommand("tester@cafelab.com", "secret123"));
    }

    @Test
    void signInReturnsNotFoundWhenCredentialsAreInvalid() throws Exception {
        when(userCommandService.handle(any(SignInCommand.class))).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/authentication/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "tester@cafelab.com",
                                  "password": "wrong-password"
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Credenciales inv\u00e1lidas"));
    }

    @Test
    void signInReturnsBadRequestWhenJsonIsMalformed() throws Exception {
        mockMvc.perform(post("/api/v1/authentication/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "tester@cafelab.com",
                                  "password":
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.startsWith("No se pudo leer el JSON:")));
    }
}
