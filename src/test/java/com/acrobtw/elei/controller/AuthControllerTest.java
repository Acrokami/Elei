package com.acrobtw.elei.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

import com.acrobtw.elei.dto.AuthRequest;
import com.acrobtw.elei.exception.GlobalExceptionHandler;
import com.acrobtw.elei.service.UserService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest({AuthController.class, GlobalExceptionHandler.class})
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;


    @Test
    public void shouldRegisterUserSuccessfully() throws Exception {
        AuthRequest request = new AuthRequest("Test", "test@mail.com", "test1234");


        mockMvc.perform(post("/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
    .andExpect(status().isOk());


    }

    @Test
    public void shouldReturnBadRequestWhenDataIsInvalid() throws Exception {
        AuthRequest request = new AuthRequest("SecondTest", "test2312", "test");

        mockMvc.perform(post("/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
    .andExpect(status().isBadRequest());
    }


    @Test
    public void shouldReturnBadRequestWhenEmailIsTaken() throws Exception {
        AuthRequest request = new AuthRequest("Testy2", "test@mail.com", "test1234");

        Mockito.doThrow(new IllegalArgumentException("This email is taken"))
        .when(userService).registerNewUser(any(String.class), any(String.class), any(String.class));

        mockMvc.perform(post("/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
    .andExpect(status().isBadRequest());
    }
}
