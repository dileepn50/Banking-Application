package in.co.service.banking.interfaces;

import in.co.api.banking.models.ApiResponse;
import in.co.api.banking.models.BankAccountObject;
import in.co.api.banking.models.Transaction;

import java.util.List;

public interface IBankAccount {
    ApiResponse<String> withdraw(Double amount, Long accountNumber);
    ApiResponse<String> deposit(Double amount, Long accountNumber);
    ApiResponse<List<Transaction>> getTransactionHistory(Long accountNumber);
    ApiResponse<List<Transaction>> getMiniStatement(Long accountNumber);
    ApiResponse<BankAccountObject> createBankAccount(String panNumber, String type, Double amount, Long branchId);
    ApiResponse<BankAccountObject> getAccount(Long accountNUmber);
}
