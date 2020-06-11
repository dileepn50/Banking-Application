package in.co.banking.store.repositories;

import in.co.banking.store.domain.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    @Query("FROM CustomerEntity c where c.panNumber = ?1")
    CustomerEntity findByPanNumber(String panNumber);
}
