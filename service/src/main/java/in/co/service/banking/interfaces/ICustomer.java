package in.co.service.banking.interfaces;

import in.co.api.banking.models.ApiResponse;
import in.co.api.banking.models.BankAccountObject;
import in.co.api.banking.models.CustomerObject;

import java.util.List;

public interface ICustomer {
    String getPanNumber(Long customerId);
    List<BankAccountObject> getBankAccounts(Long customerId);
    ApiResponse<CustomerObject> getCustomerDetails(String panNumber);
}
