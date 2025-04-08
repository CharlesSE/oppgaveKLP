package com.klp.oppgave.county;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CountyController.class)
class CountyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    private static final String BASE_URL = "https://api.kartverket.no/kommuneinfo/v1/fylker/";

    @Test
    void testGetCountyNameValidCountyNumber() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode mockResponse = objectMapper.readTree("{\"fylkesnavn\":\"Buskerud\"}");
        when(restTemplate.getForObject(BASE_URL + "33", JsonNode.class))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/county/33"))
                .andExpect(status().isOk())
                .andExpect(content().string("Buskerud"));
    }

    @Test
    void testGetCountyNameInvalidCountyNumber() throws Exception {
        when(restTemplate.getForObject(
                eq(BASE_URL + "77"),
                eq(JsonNode.class))
        ).thenThrow(new HttpClientErrorException(
                HttpStatus.NOT_FOUND,
                "Not Found"
        ));

        mockMvc.perform(get("/county/77"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("County number 77 not found."));
    }

    @Test
    void testInputValidationInvalidCountyNumber() throws Exception {
        mockMvc.perform(get("/county/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid county number format. Must be 2 digits."));

        mockMvc.perform(get("/county/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid county number format. Must be 2 digits."));

        mockMvc.perform(get("/county/123"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid county number format. Must be 2 digits."));
    }

    @Test
    void testGetCountyNameServerError() throws Exception {
        when(restTemplate.getForObject(BASE_URL + "99", JsonNode.class))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/county/99"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred: Unexpected error"));
    }
}


