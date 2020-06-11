package in.co.banking.store.repositories;

import in.co.banking.store.domain.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {
    @Query("FROM BankAccountEntity b where b.customerEntity.id = ?1")
    List<BankAccountEntity> findAllByCustomer(Long customerId);

    Optional<BankAccountEntity> findById(Long id);

    List<BankAccountEntity> findAllByPanNumberAndBranchId(String panNumber, Long branchId);
}
