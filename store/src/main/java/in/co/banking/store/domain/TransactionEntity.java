package in.co.banking.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import in.co.api.banking.constants.*;
import in.co.api.banking.constants.*;
import sun.security.pkcs11.wrapper.Constants;

import javax.persistence.*;

@Entity
@Table(name = "transaction")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    CustomerEntity customerEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bank_account_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    BankAccountEntity bankAccountEntity;

    @Column(name = "amount")
    Double amount;

    @Column(name = "type")
    String type;
}
