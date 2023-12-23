package com.tree.test.controller;

import com.tree.test.service.StatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/statements")
public class StatementController {
    private final StatementService statementService;

    @Autowired
    public StatementController(StatementService statementService) {
        this.statementService = statementService;
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Object> getStatements(
            @PathVariable Long accountId,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) String fromAmount,
            @RequestParam(required = false) String toAmount
    ) {
        // Check the user's role
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        // If the user is not an admin and has filtering parameters, return FORBIDDEN
        if (!isAdmin && (fromDate != null || toDate != null || fromAmount != null || toAmount != null)) {
            Map<String, String> response = Map.of(
                    "status", "403",
                    "error", "Forbidden",
                    "message", "Access forbidden for non-admin users with filtering parameters."
            );
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        // If no filtering parameters are provided, call the original method
        if (fromDate == null && toDate == null && fromAmount == null && toAmount == null) {
            return statementService.getStatementsByAccountId(accountId);
        } else {
            // If filtering parameters are provided, call the search method
            return statementService.searchStatements(accountId, fromDate, toDate, fromAmount, toAmount);
        }
    }
}
