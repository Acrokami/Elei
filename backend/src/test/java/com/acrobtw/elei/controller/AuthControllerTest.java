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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

import com.acrobtw.elei.dto.AuthenticationRequest;
import com.acrobtw.elei.exception.GlobalExceptionHandler;
import com.acrobtw.elei.security.jwt.JwtAuthenticationFilter;
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

    @Test
    public void shouldRegisterUserSuccessfully() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("Test", "test@mail.com", "test1234");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

    }

    @Test
    public void shouldReturnBadRequestWhenDataIsInvalid() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("SecondTest", "test2312", "test");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenEmailIsTaken() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("Testy2", "test@mail.com", "test1234");

        Mockito.doThrow(new IllegalArgumentException("This email is taken"))
                .when(userService).registerNewUser(any(), any(), any());

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
