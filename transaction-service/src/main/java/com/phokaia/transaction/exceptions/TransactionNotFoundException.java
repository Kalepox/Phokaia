package com.phokaia.transaction.exceptions;

public class TransactionNotFoundException extends RuntimeException {

    public  TransactionNotFoundException(String id){
        
        super("Transaction "+ id + " not found");

    }

}
