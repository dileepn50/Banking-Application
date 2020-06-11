package in.co.service.banking.service;

import in.co.api.banking.models.ApiResponse;
import in.co.api.banking.models.ApiResult;
import in.co.banking.store.domain.BankAccountEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrentAccount extends BankAccount {

    @Override
    public ApiResponse<String> getType(Long accountNUmber) {
        Optional<BankAccountEntity> bankAccountEntityOptional = bankAccountRepository.findById(accountNUmber);

        if(!bankAccountEntityOptional.isPresent())
            return new ApiResponse<>(null, ApiResult.FAILURE, "Account number is not valid");

        BankAccountEntity bankAccountEntity = bankAccountEntityOptional.get();

        return new ApiResponse<>(bankAccountEntity.getType(), ApiResult.SUCCESS, null);
    }

}
