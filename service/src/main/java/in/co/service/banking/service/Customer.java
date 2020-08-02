package in.co.service.banking.service;

import in.co.api.banking.models.ApiResponse;
import in.co.api.banking.models.ApiResult;
import in.co.api.banking.models.BankAccountObject;
import in.co.api.banking.models.CustomerObject;
import in.co.banking.store.domain.BankAccountEntity;
import in.co.banking.store.domain.CustomerEntity;
import in.co.banking.store.repositories.BankAccountRepository;
import in.co.banking.store.repositories.CustomerRepository;
import in.co.service.banking.interfaces.ICustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class Customer implements ICustomer {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Override
    public String getPanNumber(Long customerId) {
        Optional<CustomerEntity> optional = customerRepository.findById(customerId);
        CustomerEntity customerEntity = optional.get();
        return customerEntity.getPanNumber();
    }

    @Override
    public List<BankAccountObject> getBankAccounts(Long customerId) {
         List<BankAccountEntity> bankAccountEntityList = bankAccountRepository.findAllByCustomer(customerId);

         List<BankAccountObject> bankAccountObjectList = new ArrayList<>();

         for (BankAccountEntity bankAccountEntity: bankAccountEntityList) {
             BankAccountObject bankAccountObject = new BankAccountObject();
             bankAccountObject.setAccountNUmber(bankAccountEntity.getId());
             bankAccountObject.setCurrentBalance(bankAccountEntity.getCurrentBalance());
             bankAccountObject.setInterestRate(bankAccountEntity.getInterestRate());
             bankAccountObject.setMinimumBalance(bankAccountEntity.getMinimumBalance());

             bankAccountObjectList.add(bankAccountObject);
         }

         return bankAccountObjectList;

    }

    @Override
    public ApiResponse<CustomerObject> getCustomerDetails(String panNumber) {
        CustomerEntity customerEntity = customerRepository.findByPanNumber(panNumber);

        if (customerEntity == null)
            return new ApiResponse<>(null, ApiResult.FAILURE, "Customer with this pan number is not present");

        CustomerObject customerObject = new CustomerObject();
        customerObject.setBranchId(customerEntity.getBranchEntity().getId());
        customerObject.setPanNumber(panNumber);

        return new ApiResponse<>(customerObject, ApiResult.SUCCESS, null);
    }
}
