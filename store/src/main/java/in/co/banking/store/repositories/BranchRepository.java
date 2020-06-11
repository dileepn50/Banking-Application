package in.co.banking.store.repositories;

import in.co.banking.store.domain.BranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchRepository extends JpaRepository<BranchEntity, Long> {

}
