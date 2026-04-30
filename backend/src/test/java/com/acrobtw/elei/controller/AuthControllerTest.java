package com.acrobtw.elei.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

import com.acrobtw.elei.dto.auth.RegisterRequest;
import com.acrobtw.elei.dto.auth.RegisterResponse;
import com.acrobtw.elei.exception.GlobalExceptionHandler;
import com.acrobtw.elei.security.jwt.JwtAuthenticationFilter;
import com.acrobtw.elei.service.AuthenticationService;
import com.acrobtw.elei.service.UserService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = { AuthController.class, GlobalExceptionHandler.class }, excludeAutoConfiguration = {
        SecurityAutoConfiguration.class },
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthenticationService authenticationService;

    @Test
    public void shouldRegisterUserSuccessfully() throws Exception {
        RegisterRequest request = new RegisterRequest("Test", "test@mail.com", "test1234");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

    }

    @Test
    public void shouldReturnBadRequestWhenDataIsInvalid() throws Exception {
        RegisterRequest request = new RegisterRequest("SecondTest", "test2312", "test");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenEmailIsTaken() throws Exception {
        RegisterRequest request = new RegisterRequest("Testy2", "test@mail.com", "test1234");

        Mockito.doThrow(new IllegalArgumentException("This email is taken"))
                .when(authenticationService).register(request);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void shouldReturnTokenOnRegistration() throws Exception {
        RegisterRequest request = new RegisterRequest("Testy2", "test@mail.com", "test1234");
        String jsonRequest = objectMapper.writeValueAsString(request);

        RegisterResponse mockResponse = new RegisterResponse(
            200,
            "Register successfully completed",
            "Testy2",
            "mocked.jwt.token",
            java.time.LocalDateTime.now()
        );

        Mockito.when(authenticationService.register(any(RegisterRequest.class)))
               .thenReturn(mockResponse);

        mockMvc.perform(post("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").exists())
        .andExpect(jsonPath("$.token").isString())
        .andExpect(jsonPath("$.token").value("mocked.jwt.token"))
        .andExpect(jsonPath("$.username").value("Testy2"));
    }
}
