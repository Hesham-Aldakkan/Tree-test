package com.tree.test.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class StatementResponseDto {
    private Long id;
    private Date date;
    private String amount;

    public StatementResponseDto(Long id, Date date, String amount) {
        this.id = id;
        this.date = date;
        this.amount = amount;
    }
}
