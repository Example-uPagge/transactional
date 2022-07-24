package dev.struchkov.example.transaction.repository;

import dev.struchkov.example.transaction.domain.TransactionMoney;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionMoneyRepository extends JpaRepository<TransactionMoney, Long> {
}
