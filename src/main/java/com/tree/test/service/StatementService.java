package com.tree.test.service;

import org.springframework.http.ResponseEntity;

public interface StatementService {
    public ResponseEntity getStatementsByAccountId(Long accountId);
    public ResponseEntity searchStatements(
            Long accountId, String fromDate, String toDate, String fromAmount, String toAmount);
}
