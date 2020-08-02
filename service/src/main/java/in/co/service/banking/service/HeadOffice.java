package in.co.service.banking.service;

import in.co.api.banking.models.ApiResponse;
import in.co.api.banking.models.ApiResult;
import in.co.api.banking.models.Branch;
import in.co.banking.store.domain.BranchEntity;
import in.co.banking.store.domain.HeadOfficeEntity;
import in.co.banking.store.repositories.BranchRepository;
import in.co.banking.store.repositories.HeadOfficeRepository;
import in.co.service.banking.interfaces.IHeadOffice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class HeadOffice implements IHeadOffice {
    @Autowired
    HeadOfficeRepository headOfficeRepository;

    @Autowired
    BranchRepository branchRepository;

    @Override
    public ApiResponse<Branch> createBranch(String name, String address, Long headOfficeId) {
        Optional<HeadOfficeEntity> optionalHeadOfficeEntity = headOfficeRepository.findById(headOfficeId);

        if(!optionalHeadOfficeEntity.isPresent())
            return new ApiResponse<>(null, ApiResult.FAILURE, "Head office id is not valid");

        BranchEntity branchEntity = new BranchEntity();
        branchEntity.setAddress(address);
        branchEntity.setBranchName(name);
        branchEntity.setHeadOfficeEntity(optionalHeadOfficeEntity.get());

        BranchEntity branchEntity1 = branchRepository.save(branchEntity);

        Branch branch = new Branch();
        branch.setAddress(address);
        branch.setHeadOfficeId(headOfficeId);
        branch.setId(branchEntity1.getId());
        branch.setName(name);

        return new ApiResponse<>(branch, ApiResult.SUCCESS, null);
    }

    @Override
    public ApiResponse<Branch> getBranchById(Long branchId) {
        Optional<BranchEntity> optionalBranchEntity = branchRepository.findById(branchId);

        if(!optionalBranchEntity.isPresent())
            return new ApiResponse<>(null, ApiResult.FAILURE, "Branch id is not vaiid");

        BranchEntity branchEntity = optionalBranchEntity.get();

        Branch branch = new Branch();

        branch.setName(branchEntity.getBranchName());
        branch.setId(branchEntity.getId());
        branch.setHeadOfficeId(branchEntity.getHeadOfficeEntity().getId());
        branch.setAddress(branchEntity.getAddress());

        return new ApiResponse<>(branch, ApiResult.SUCCESS, null);

    }
}
