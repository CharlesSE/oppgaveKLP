package com.klp.oppgave.county;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/county")
public class CountyController {

    private final RestTemplate restTemplate;

    private static final String BASE_URL = "https://api.kartverket.no/kommuneinfo/v1/fylker/";

    public CountyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/{countyNumber}")
    public ResponseEntity<String> getCountyName(@PathVariable String countyNumber) {

        if (!countyNumber.matches("\\d{2}")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid county number format. Must be 2 digits.");
        }

        String url = BASE_URL + countyNumber;

        try {
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
            assert response != null;
            String countyName = response.get("fylkesnavn").asText();
            return ResponseEntity.ok(countyName);

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("County number " + countyNumber + " not found.");
            } else {
                return ResponseEntity.status(e.getStatusCode())
                        .body("HTTP error: " + e.getMessage());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}