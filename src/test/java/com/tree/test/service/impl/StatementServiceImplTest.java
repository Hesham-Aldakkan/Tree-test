package com.tree.test.service.impl;

import com.tree.test.model.Account;
import com.tree.test.model.Statement;
import com.tree.test.model.dto.StatementResponseDto;
import com.tree.test.repository.AccountRepository;
import com.tree.test.repository.StatementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StatementServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private StatementRepository statementRepository;

    @InjectMocks
    private StatementServiceImpl statementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getStatementsByAccountId_shouldReturnStatements() {
        // Arrange
        Long accountId = 1L;
        Account account = new Account();
        account.setId(accountId);


        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // Act
        ResponseEntity<Object> responseEntity = statementService.getStatementsByAccountId(accountId);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        List<StatementResponseDto> statements = (List<StatementResponseDto>) responseEntity.getBody();
        //assert that statements are not empty

    }

    @Test
    void getStatementsByAccountId_shouldReturnBadRequestWhenAccountNotFound() {
        // Arrange
        Long accountId = 1L;

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> responseEntity = statementService.getStatementsByAccountId(accountId);

        // Assert
        assertEquals(400, responseEntity.getStatusCodeValue());
    }


}