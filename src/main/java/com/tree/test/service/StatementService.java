package com.tree.test.service;

import com.tree.test.model.dao.Statement;
import com.tree.test.model.dto.StatementResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StatementService {
    public ResponseEntity getStatementsByAccountId(Long accountId);
    public ResponseEntity searchStatements(
            Long accountId, String fromDate, String toDate, String fromAmount, String toAmount);
}
