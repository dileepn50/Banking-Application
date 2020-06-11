package in.co.service.banking.interfaces;

import in.co.api.banking.models.ApiResponse;
import in.co.api.banking.models.Branch;

public interface IHeadOffice {
    ApiResponse<Branch> createBranch(String name, String address, Long headOfficeId);
    ApiResponse<Branch> getBranchById(Long branchId);
}
