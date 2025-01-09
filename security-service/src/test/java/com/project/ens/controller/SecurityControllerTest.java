package com.project.ens.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ens.dto.NewUserDto;
import com.project.ens.dto.RefreshJwtRequest;
import com.project.ens.model.jwt.JwtRequest;
import com.project.ens.model.jwt.JwtResponse;
import com.project.ens.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SecurityController.class)
public class SecurityControllerTest {

    public static final String PATH_URL = "/api/auth";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private AuthService service;

    private final NewUserDto userDto = new NewUserDto(
            "Test name",
            "Test lastname",
            "test@test.test",
            "test",
            "+9000000"
    );

    private final JwtResponse response = new JwtResponse(
            "Bearer test access",
            "Bearer test refresh"
    );

    @Test
    @WithMockUser
    public void shouldRegisterNewUser() throws Exception {
        when(service.register(any(NewUserDto.class))).thenReturn(true);

        mvc.perform(post(PATH_URL + "/register")
                        .with(csrf())
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    public void shouldLoginUser() throws Exception {
        JwtRequest request = new JwtRequest();
        request.setLogin("test");
        request.setPassword("test");

        when(service.login(any(JwtRequest.class))).thenReturn(response);

        mvc.perform(post(PATH_URL + "/login")
                        .with(csrf())
                        .content(mapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", is(response.getAccessToken())))
                .andExpect(jsonPath("$.refreshToken", is(response.getRefreshToken())));
    }

    @Test
    @WithMockUser
    public void shouldGetNewAccessToken() throws Exception {
        RefreshJwtRequest request = new RefreshJwtRequest();
        request.setRefreshToken("Bearer test refresh");

        when(service.getAccessToken(request.getRefreshToken())).thenReturn(response);

        mvc.perform(post(PATH_URL + "/token")
                        .with(csrf())
                        .header("Authorization", "Bearer test access")
                        .content(mapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", is(response.getAccessToken())))
                .andExpect(jsonPath("$.refreshToken", is(response.getRefreshToken())));
    }

    @Test
    @WithMockUser
    public void shouldGetNewRefreshToken() throws Exception {
        RefreshJwtRequest request = new RefreshJwtRequest();
        request.setRefreshToken("Bearer test refresh");

        when(service.refresh(request.getRefreshToken())).thenReturn(response);

        mvc.perform(post(PATH_URL + "/refresh")
                        .with(csrf())
                        .header("Authorization", "Bearer test access")
                        .content(mapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", is(response.getAccessToken())))
                .andExpect(jsonPath("$.refreshToken", is(response.getRefreshToken())));
    }
}
