package dev.struchkov.example.transaction.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDto {

    private Long personFrom;
    private Long personTo;
    private Long amount;

}
