package in.co.service.banking.resource;

import in.co.api.banking.models.ApiResponse;
import in.co.api.banking.models.ApiResult;
import in.co.api.banking.models.Branch;
import in.co.api.banking.models.CreateBranchRequest;
import in.co.service.banking.service.HeadOffice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class HeadOfficeController {

    @Autowired
    HeadOffice headOffice;

    @RequestMapping(value = "/create/branch", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<Branch>> createBranch(@RequestBody CreateBranchRequest createBranchRequest) {
        ApiResponse<Branch> apiResponse = headOffice.createBranch(createBranchRequest.getName(), createBranchRequest.getAddress(), createBranchRequest.getHeadOfficeId());

        if (apiResponse.getResult().name().equalsIgnoreCase(ApiResult.FAILURE.name()))
            return ResponseEntity.badRequest().body(apiResponse);

        return ResponseEntity.ok().body(apiResponse);
    }

    @RequestMapping(value = "/branch/details", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<Branch>> getBranchDetails(@RequestParam("branch_id") Long branchId) {
         ApiResponse<Branch> apiResponse = headOffice.getBranchById(branchId);

         if (apiResponse.getResult().name().equalsIgnoreCase(ApiResult.FAILURE.name()))
             return ResponseEntity.badRequest().body(apiResponse);

         return ResponseEntity.ok().body(apiResponse);
    }
}
