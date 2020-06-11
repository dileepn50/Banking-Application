package in.co.service.banking.resource;

import in.co.service.banking.service.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import in.co.api.banking.models.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class CustomerController {

    @Autowired
    Customer customer;

    @RequestMapping(value = "/customer/pan_number", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<String>> getPanNumber(@RequestParam(value = "customer_id") Long customerId){
        String panNumber  = customer.getPanNumber(customerId);

        if (panNumber == null)
            return ResponseEntity.badRequest().body(new ApiResponse<>(null, ApiResult.FAILURE, "Error while fetching pan number"));

        return ResponseEntity.ok(new ApiResponse<>(panNumber, ApiResult.SUCCESS, null));
    }

    @RequestMapping(value = "/customer/bank_accounts", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<BankAccountObject>>> getBankAccounts(@RequestParam(value = "customer_id") Long customerId) {
        List<BankAccountObject> bankAccountObjectList = customer.getBankAccounts(customerId);

        if (bankAccountObjectList.size() == 0)
            return ResponseEntity.badRequest().body(new ApiResponse<>(null, ApiResult.FAILURE, "User don't have any accounts"));

        return ResponseEntity.ok(new ApiResponse<>(bankAccountObjectList, ApiResult.SUCCESS, null));
    }

    @RequestMapping(value = "/customer/details", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<CustomerObject>> getCustomerDetails(String panNumber) {
        ApiResponse<CustomerObject> customerObjectApiResponse = customer.getCustomerDetails(panNumber);

        if (customerObjectApiResponse.getResult().name().equalsIgnoreCase(ApiResult.FAILURE.name()))
            return ResponseEntity.badRequest().body(customerObjectApiResponse);

        return ResponseEntity.ok(customerObjectApiResponse);
    }
}
