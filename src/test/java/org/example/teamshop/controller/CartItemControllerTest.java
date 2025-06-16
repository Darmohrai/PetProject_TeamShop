package org.example.teamshop.controller;

import org.example.teamshop.model.Client;
import org.example.teamshop.service.SecurityServices.CustomUserDetailsService.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class CartItemControllerTest {

    @Value("${api.path}" + "/client/cart/cart_item/")
    private String apiPath;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private Client testClient;
}
