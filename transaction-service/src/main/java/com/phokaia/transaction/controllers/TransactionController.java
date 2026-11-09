package com.phokaia.transaction.controllers;

import org.springframework.web.bind.annotation.*;
import com.phokaia.transaction.services.TransactionService;

import org.springframework.http.HttpStatus;
import com.phokaia.transaction.dtos.TransactionDTO;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TransactionDTO.Response create(@Valid @RequestBody TransactionDTO.TransferRequest request){
        return transactionService.requestTransaction(request);
    }

    @GetMapping("/{transactionId}")
    public TransactionDTO.Response get(@PathVariable String transactionId){
        return transactionService.getTransaction(transactionId);
    }

    @GetMapping 
    public List<TransactionDTO.Response> getByAccount(@RequestParam String accountCode){
        return transactionService.getTransactionsOfAccount(accountCode);
    }


}
