package com.wallet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.dto.UserDTO;
import com.wallet.entity.User;
import com.wallet.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {
    private static final Long ID = 1L;
    private static final String EMAIL="email@teste.com";
    private static final String PASSWORD="123456";
    private static final String NAME="User Test";
    private static final String URL="/user";

    @MockBean
    UserService service;

    @Autowired
    MockMvc mvc;

    @Test
    public void testSave() throws Exception {
        BDDMockito.given(service.save(Mockito.any(User.class))).willReturn(getMockUser());

        mvc.perform(MockMvcRequestBuilders.post(URL).content(getJsonPayload(ID,EMAIL,PASSWORD,NAME))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.id").value(ID))
        .andExpect(jsonPath("$.data.email").value(EMAIL))
        .andExpect(jsonPath("$.data.password").value(PASSWORD))
        .andExpect(jsonPath("$.data.name").value(NAME));

    }

    @Test
    public void testSaveInvalidUser() throws JsonProcessingException, Exception{
        mvc.perform(MockMvcRequestBuilders.post(URL).content(getJsonPayload(ID,"a","1123412341","email"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Email Inválido"));
    }

    public User getMockUser(){
        User u = new User();
        u.setId(ID);
        u.setName(NAME);
        u.setPassword(PASSWORD);
        u.setEmail(EMAIL);
        return u;
    }

    public String getJsonPayload(Long id, String email, String password, String name) throws JsonProcessingException {
        UserDTO dto = new UserDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setPassword(password);
        dto.setEmail(email);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(dto);
    }
}
