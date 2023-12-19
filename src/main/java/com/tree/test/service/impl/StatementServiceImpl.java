package com.tree.test.service.impl;

import com.tree.test.model.dao.Account;
import com.tree.test.model.dao.Statement;
import com.tree.test.model.dto.StatementResponseDto;
import com.tree.test.repository.AccountRepository;
import com.tree.test.repository.StatementRepository;
import com.tree.test.service.StatementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatementServiceImpl implements StatementService {
    private final AccountRepository accountRepository;
    private final StatementRepository statementRepository;

    public StatementServiceImpl(AccountRepository accountRepository, StatementRepository statementRepository) {
        this.accountRepository = accountRepository;
        this.statementRepository = statementRepository;
    }

    @Override
    public ResponseEntity getStatementsByAccountId(Long accountId) {
        try {
            // Check if the account exists
            Optional<Account> accountOptional = accountRepository.findById(accountId); //this could be optimized
            if (accountOptional.isEmpty()) {
                String errorMessage = "Account with ID " + accountId + " not found";
                Map<String, Object> response = Map.of("error", errorMessage);
                return ResponseEntity.badRequest().body(response); // Account not found
            }
            LocalDate staticDate = LocalDate.of(2012,01,01);
            List<StatementResponseDto> statements = statementRepository.findByAccountId(accountId).stream().map(s ->{
                Date newDate = convertToDate(s.getDateField());
                StatementResponseDto statementResponseDto = new StatementResponseDto(s.getId(), newDate, s.getAmount());
                return statementResponseDto;
            })
//                    .filter(s -> s.getDate().after(java.sql.Date.valueOf(LocalDate.now().minusMonths(3))))
                    .filter(s -> s.getDate().after(java.sql.Date.valueOf(staticDate.minusMonths(3))))
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(statements);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            Map<String, Object> response = Map.of("error", errorMessage);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @Override
    public ResponseEntity searchStatements(Long accountId, String fromDate, String toDate, String fromAmount, String toAmount) {
        try {
            // Check if the account exists
            Optional<Account> accountOptional = accountRepository.findById(accountId); //this could be optimized

            if (accountOptional.isEmpty()) {
                String errorMessage = "Account with ID " + accountId + " not found";
                Map<String, Object> response = Map.of("error", errorMessage);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // Account not found
            }

            List<StatementResponseDto> statements = statementRepository.findByAccountId(accountId).stream().map(
                    s -> {
                        Date newDate = convertToDate(s.getDateField());
                        StatementResponseDto statementResponseDto = new StatementResponseDto(s.getId(), newDate, s.getAmount());
                        return statementResponseDto;
                    })
                    .filter(
                            s -> {
                                if (fromDate != null && toDate != null) {
                                    return s.getDate().after(convertToDate(fromDate)) && s.getDate().before(convertToDate(toDate));
                                } else if (fromDate != null) {
                                    return s.getDate().after(convertToDate(fromDate));
                                } else if (toDate != null) {
                                    return s.getDate().before(convertToDate(toDate));
                                } else {
                                    return true;
                                }
                            }
                    )
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(statements);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            Map<String, Object> response = Map.of("error", errorMessage);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    private Date convertToDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }



}
