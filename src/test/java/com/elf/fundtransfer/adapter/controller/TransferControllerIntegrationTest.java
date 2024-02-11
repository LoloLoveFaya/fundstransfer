package com.elf.fundtransfer.adapter.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
public class TransferControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void transferFunds_Success() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"from\": \"Account A\", \"to\": \"Account B\", \"amount\": 100}")
        );

        result.andExpect(status().isOk());
    }

    @Test
    public void transferFunds_InsufficientBalance() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"from\": \"Account A\", \"to\": \"Account B\", \"amount\": 100}")
        );

        result.andExpect(status().isBadRequest());
    }
}
