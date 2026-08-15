package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DemoController.class)
class GreetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void healthShouldReturnUp() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void greetShouldReturnCustomName() throws Exception {
        mockMvc.perform(get("/greet")
                        .param("name", "Daniel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Hola Daniel"));
    }

    @Test
    void greetShouldUseDefaultValue() throws Exception {
        mockMvc.perform(get("/greet"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Hola Mundo"));
    }

    @Test
    void sumShouldReturnCorrectResult() throws Exception {
        mockMvc.perform(get("/sum")
                        .param("a", "10")
                        .param("b", "15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result")
                        .value(25));
    }
}