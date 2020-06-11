package in.co.service.banking.service;

import in.co.api.banking.constants.Constants.*;
import in.co.api.banking.models.ApiResponse;
import in.co.api.banking.models.ApiResult;
import in.co.api.banking.models.BankAccountObject;
import in.co.api.banking.models.Transaction;
import in.co.banking.store.domain.BankAccountEntity;
import in.co.banking.store.domain.BranchEntity;
import in.co.banking.store.domain.CustomerEntity;
import in.co.banking.store.domain.TransactionEntity;
import in.co.banking.store.repositories.BankAccountRepository;
import in.co.banking.store.repositories.BranchRepository;
import in.co.banking.store.repositories.CustomerRepository;
import in.co.banking.store.repositories.TransactionRepository;
import in.co.service.banking.interfaces.IBankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public abstract class BankAccount implements IBankAccount {
    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    BranchRepository branchRepository;

    public abstract ApiResponse<String> getType(Long accountNumber);

    @Override
    public ApiResponse<String> withdraw(Double amount, Long accountNumber) {
        Optional<BankAccountEntity> optionalBankAccountEntity = bankAccountRepository.findById(accountNumber);

        if (!optionalBankAccountEntity.isPresent())
            return new ApiResponse<>(null, ApiResult.FAILURE, "Account number is not valid");

        BankAccountEntity bankAccountEntity = optionalBankAccountEntity.get();

        if (amount == null || amount <= 0.0)
            return new ApiResponse<>(null, ApiResult.FAILURE, "Amount is not valid");

        Double remainingBalance = bankAccountEntity.getCurrentBalance() - amount;

        if (!(remainingBalance >= bankAccountEntity.getMinimumBalance()))
            return new ApiResponse<>(null, ApiResult.FAILURE, "You have to maintain minimum balance of " + bankAccountEntity.getMinimumBalance());

        bankAccountEntity.setCurrentBalance(remainingBalance);
        bankAccountRepository.save(bankAccountEntity);

        Optional<CustomerEntity> optionalCustomerEntity = customerRepository.findById(bankAccountEntity.getCustomerEntity().getId());

        CustomerEntity customerEntity = new CustomerEntity();
        if (!optionalCustomerEntity.isPresent())
            return new ApiResponse<>(null, ApiResult.FAILURE, "Customer id is not valid");
        customerEntity = optionalCustomerEntity.get();

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAmount(amount);
        transactionEntity.setBankAccountEntity(bankAccountEntity);
        transactionEntity.setCustomerEntity(customerEntity);
        transactionEntity.setType(TransactionType.WITHDRAW.name());

        transactionRepository.save(transactionEntity);

        return new ApiResponse<>("Money withdraw is successful", ApiResult.SUCCESS, null);
    }

    @Override
    public ApiResponse<String> deposit(Double amount, Long accountNumber) {
        if (amount <= 0.0)
            return new ApiResponse<>(null, ApiResult.FAILURE, "Deposit value is not valid ");

        Optional<BankAccountEntity> optionalBankAccountEntity = bankAccountRepository.findById(accountNumber);

        if (!optionalBankAccountEntity.isPresent())
            return new ApiResponse<>(null, ApiResult.FAILURE, "Account number is not valid");
        BankAccountEntity bankAccountEntity = optionalBankAccountEntity.get();

        Double totalBalance = bankAccountEntity.getCurrentBalance() + amount;

        bankAccountEntity.setCurrentBalance(totalBalance);

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setType(TransactionType.DEPOSIT.name());
        transactionEntity.setBankAccountEntity(bankAccountEntity);
        transactionEntity.setAmount(amount);
        transactionEntity.setCustomerEntity(bankAccountEntity.getCustomerEntity());

        transactionRepository.save(transactionEntity);

        return new ApiResponse<>("Money deposit is successful", ApiResult.SUCCESS, null);
    }

    @Override
    public ApiResponse<List<Transaction>> getTransactionHistory(Long accountNumber) {
        Optional<BankAccountEntity> optional = bankAccountRepository.findById(accountNumber);
        BankAccountEntity bankAccountEntity = optional.get();
        List<TransactionEntity> transactionEntityList = transactionRepository.findAllByAccount(bankAccountEntity);

        List<Transaction> transactionList = new ArrayList<>();

        for (TransactionEntity transactionEntity: transactionEntityList) {
            Transaction transactionObject =  new Transaction();
            transactionObject.setAccountNUmber(accountNumber);
            transactionObject.setAmount(transactionEntity.getAmount());
            transactionObject.setType(transactionEntity.getType());
        }

        return new ApiResponse<>(transactionList, ApiResult.SUCCESS, null);
    }

    @Override
    public ApiResponse<List<Transaction>> getMiniStatement(Long accountNumber) {
        Optional<BankAccountEntity> optional = bankAccountRepository.findById(accountNumber);
        BankAccountEntity bankAccountEntity = optional.get();
        List<TransactionEntity> transactionEntityList = transactionRepository.getMiniStatement(bankAccountEntity);

        List<Transaction> transactionList = new ArrayList<>();

        for (TransactionEntity transactionEntity: transactionEntityList) {
            Transaction transactionObject =  new Transaction();
            transactionObject.setAccountNUmber(accountNumber);
            transactionObject.setAmount(transactionEntity.getAmount());
            transactionObject.setType(transactionEntity.getType());
        }

        return new ApiResponse<>(transactionList, ApiResult.SUCCESS, null);
    }

    @Override
    public ApiResponse<BankAccountObject> createBankAccount(String panNumber, String type, Double amount, Long branchId) {
        List<BankAccountEntity> bankAccountEntityList = bankAccountRepository.findAllByPanNumberAndBranchId(panNumber, branchId);

        Optional<BranchEntity> optional = branchRepository.findById(branchId);

        BranchEntity branchEntity = optional.get();
        CustomerEntity customerEntity = new CustomerEntity();

        if (bankAccountEntityList.size() == 0) {
            customerEntity.setPanNumber(panNumber);
            customerEntity.setBranchEntity(branchEntity);
            customerEntity = customerRepository.save(customerEntity);
        }
        else {
            BankAccountEntity bankAccountEntity = bankAccountEntityList.get(0);
            customerEntity = bankAccountEntity.getCustomerEntity();
        }

        BankAccountEntity bankAccountEntity = new BankAccountEntity();

        bankAccountEntity.setCurrentBalance(amount);
        bankAccountEntity.setBranchEntity(branchEntity);
        bankAccountEntity.setCustomerEntity(customerEntity);

        if (type.equalsIgnoreCase(AccountType.SAVING.name())) {
            bankAccountEntity.setType(AccountType.SAVING.name());
            bankAccountEntity.setInterestRate(4.5);
            bankAccountEntity.setMinimumBalance(10000.0);
        }
        else {
            bankAccountEntity.setType(AccountType.CURRENT.name());
            bankAccountEntity.setInterestRate(0.0);
            bankAccountEntity.setMinimumBalance(20000.0);
        }

        BankAccountEntity bankAccountEntityResponse = bankAccountRepository.save(bankAccountEntity);

        BankAccountObject bankAccountObject = new BankAccountObject();
        bankAccountObject.setMinimumBalance(bankAccountEntityResponse.getMinimumBalance());
        bankAccountObject.setInterestRate(bankAccountEntityResponse.getInterestRate());
        bankAccountObject.setCurrentBalance(bankAccountEntityResponse.getCurrentBalance());
        bankAccountObject.setAccountNUmber(bankAccountEntityResponse.getId());

        return new ApiResponse<>(bankAccountObject, ApiResult.SUCCESS, null);
    }

    @Override
    public ApiResponse<BankAccountObject> getAccount(Long accountNUmber) {
        Optional<BankAccountEntity> bankAccountEntityOptional = bankAccountRepository.findById(accountNUmber);

        if (!bankAccountEntityOptional.isPresent())
            return new ApiResponse<>(null, ApiResult.FAILURE, "Account number is invalid");
        BankAccountEntity bankAccountEntity = bankAccountEntityOptional.get();

        BankAccountObject bankAccountObject = new BankAccountObject();
        bankAccountObject.setAccountNUmber(bankAccountEntity.getId());
        bankAccountObject.setCurrentBalance(bankAccountEntity.getCurrentBalance());
        bankAccountObject.setInterestRate(bankAccountEntity.getInterestRate());
        bankAccountObject.setMinimumBalance(bankAccountEntity.getMinimumBalance());

        return new ApiResponse<>(bankAccountObject, ApiResult.SUCCESS, null);
    }

}
