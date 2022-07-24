package dev.struchkov.example.transaction.controller;

import dev.struchkov.example.transaction.dto.TransactionDto;
import dev.struchkov.example.transaction.service.TransactionMoneyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/money")
@RequiredArgsConstructor
public class TransactionMoneyController {

    private final TransactionMoneyService service;

    @PostMapping("send")
    public HttpStatus sendMoney(@RequestBody TransactionDto transactionDto) {
        service.sendMoney(
                transactionDto.getPersonFrom(),
                transactionDto.getPersonTo(),
                transactionDto.getAmount()
        );
        return HttpStatus.OK;
    }

}
