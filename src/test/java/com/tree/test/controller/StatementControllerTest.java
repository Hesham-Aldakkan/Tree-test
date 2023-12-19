package com.tree.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tree.test.service.StatementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class StatementControllerTest {

    @Mock
    private StatementService statementService;

    @InjectMocks
    private StatementController statementController;

    @Test
    void getStatements_AdminUserNoFilteringParameters_ReturnsStatements() {
        // Mock authentication to be an admin
        Authentication authentication = mock(Authentication.class);
        when(authentication.getAuthorities()).thenAnswer(invocation -> Collections.singletonList((GrantedAuthority) () -> "ROLE_ADMIN"));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Mock service response
        when(statementService.getStatementsByAccountId(1L)).thenReturn(new ResponseEntity<>("Mocked Response", HttpStatus.OK));

        // Call the controller method
        ResponseEntity response = statementController.getStatements(1L, null, null, null, null);

        // Verify interactions
        verify(statementService, times(1)).getStatementsByAccountId(1L);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Mocked Response", response.getBody());
    }

    @Test
    void getStatements_NonAdminUserWithFilteringParameters_ReturnsForbidden() {
        // Mock authentication to be a non-admin user
        Authentication authentication = mock(Authentication.class);
        when(authentication.getAuthorities()).thenAnswer(invocation -> Collections.singletonList((GrantedAuthority) () -> "ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Call the controller method
        ResponseEntity response = statementController.getStatements(1L, "2022-01-01", null, null, null);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        // The response body is a Map, you can convert it to JSON for more precise assertions
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> responseBody = objectMapper.convertValue(response.getBody(), Map.class);
        assertEquals("403", responseBody.get("status"));
        assertEquals("Forbidden", responseBody.get("error"));
        assertEquals("Access forbidden for non-admin users with filtering parameters.", responseBody.get("message"));
    }

    // Add more test cases for different scenarios...

}