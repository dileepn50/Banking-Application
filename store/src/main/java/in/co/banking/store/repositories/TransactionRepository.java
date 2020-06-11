package in.co.banking.store.repositories;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import in.co.banking.store.domain.BankAccountEntity;
import in.co.banking.store.domain.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("FROM TransactionEntity t WHERE t.BankAccountEntity.id = ?1")
    List<TransactionEntity> findAllByAccount(BankAccountEntity bankAccountEntity);

    @Query("FROM TransactionEntity t WHERE t.BankAccountEntity = ?1 ORDER BY id DESC LIMIT 10")
    List<TransactionEntity> getMiniStatement(BankAccountEntity bankAccountEntity);
}
