package com.acrobtw.elei.controller;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.acrobtw.elei.config.SecurityConfig;
import com.acrobtw.elei.dto.activity.ActivityCompletionDto;
import com.acrobtw.elei.entity.User;
import com.acrobtw.elei.exception.GlobalExceptionHandler;
import com.acrobtw.elei.security.jwt.JwtAuthenticationFilter;
import com.acrobtw.elei.security.jwt.JwtService;
import com.acrobtw.elei.service.user.UserService;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import tools.jackson.databind.ObjectMapper;


// TODO REFACTOR


@WebMvcTest(controllers = {ActivityController.class, GlobalExceptionHandler.class})
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
public class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @MockitoBean
    private UserService userService;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private UserDetailsService userDetailsService;
    @MockitoBean
    private AuthenticationProvider authenticationProvider;




    @Test
    public void shouldGiveNotValidCompleteActivityExperience() throws Exception {
        Long mockActivityId = 1L;
        ActivityCompletionDto dto = new ActivityCompletionDto(mockActivityId, 2);

        mockMvc.perform(post("/activity/complete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldCompleteActivityWithValidToken() throws Exception {

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("test_user");



        Long mockActivityId = 1L;
        ActivityCompletionDto dto = new ActivityCompletionDto(mockActivityId, 2);

        mockMvc.perform(post("/activity/complete")
                .with(user(mockUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
}
}
