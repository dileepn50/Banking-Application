package in.co.service.banking.resource;

import in.co.api.banking.models.*;
import in.co.service.banking.service.BankAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Slf4j
public class BankAccountController {

    @Autowired
    BankAccount bankAccount;

    @RequestMapping(value = "/account/withdraw", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<String>> withdraw(@RequestParam("amount") Double amount,
                                                        @RequestParam("account_number") Long accountNUmber) {
        ApiResponse<String> apiResponse = bankAccount.withdraw(amount, accountNUmber);

        if (apiResponse.getResult().name().equalsIgnoreCase(ApiResult.FAILURE.name()))
            return ResponseEntity.badRequest().body(apiResponse);

        return ResponseEntity.ok(apiResponse);

    }

    @RequestMapping(value = "/account/deposit", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<String>> deposit(@RequestParam("amount") Double amount,
                                                       @RequestParam("account_number") Long accountNumber) {
        ApiResponse<String> apiResponse = bankAccount.deposit(amount, accountNumber);

        if (apiResponse.getResult().name().equalsIgnoreCase(ApiResult.FAILURE.name()))
            return ResponseEntity.badRequest().body(apiResponse);

        return ResponseEntity.ok(apiResponse);
    }

    @RequestMapping(value = "/transaction/history", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<Transaction>>> getTransactionHistory(@RequestParam("account_number") Long accountNUmber) {
        return ResponseEntity.badRequest().body(bankAccount.getTransactionHistory(accountNUmber));
    }

    @RequestMapping(value = "/mini/statement", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<Transaction>>> getMiniStatement(@RequestParam("account_number") Long accountNUmber) {
        return ResponseEntity.ok().body(bankAccount.getMiniStatement(accountNUmber));
    }

    @RequestMapping(value = "/account/create", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<BankAccountObject>> createBankAccount(@RequestBody CreateAccountRequest createAccountRequest) {
        return ResponseEntity.ok().body(bankAccount.createBankAccount(createAccountRequest.getPanNumber(), createAccountRequest.getType(), createAccountRequest.getAmount(), createAccountRequest.getBranchId()));
    }

    @RequestMapping(value = "/account/details", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<BankAccountObject>> getAccountDetails(@RequestParam("account_number") Long accountNUmber) {
        ApiResponse<BankAccountObject> bankAccountObjectApiResponse = bankAccount.getAccount(accountNUmber);

        if (bankAccountObjectApiResponse.getResult().name().equalsIgnoreCase(ApiResult.FAILURE.name()))
            return ResponseEntity.badRequest().body(bankAccountObjectApiResponse);

        return ResponseEntity.ok(bankAccountObjectApiResponse);
    }
}
