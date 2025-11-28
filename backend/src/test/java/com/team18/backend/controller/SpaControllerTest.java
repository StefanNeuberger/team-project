package com.team18.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SpaController.class)
class SpaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void forward_shouldForwardToIndexHtml_whenRootPath() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/index.html"));
    }

    @Test
    void forward_shouldForwardToIndexHtml_whenSingleLevelPath() throws Exception {
        mockMvc.perform(get("/shop"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/index.html"));
    }

    @Test
    void forward_shouldForwardToIndexHtml_whenNestedPath() throws Exception {
        mockMvc.perform(get("/shop/123"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/index.html"));
    }

    @Test
    void forward_shouldForwardToIndexHtml_whenDeeplyNestedPath() throws Exception {
        mockMvc.perform(get("/shop/123/items/456"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/index.html"));
    }

    @Test
    void forward_shouldForwardToIndexHtml_whenPathWithHyphens() throws Exception {
        mockMvc.perform(get("/my-shop/123"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/index.html"));
    }

    @Test
    void forward_shouldForwardToIndexHtml_whenPathWithUnderscores() throws Exception {
        mockMvc.perform(get("/my_shop/123"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/index.html"));
    }
}

