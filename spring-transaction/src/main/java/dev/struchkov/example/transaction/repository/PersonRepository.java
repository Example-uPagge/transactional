package dev.struchkov.example.transaction.repository;

import dev.struchkov.example.transaction.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query(nativeQuery = true, value = "SELECT person.balance FROM person WHERE id = :id")
    Long getBalance(@Param("id") Long id);

    @Query("SELECT p.balance FROM Person p WHERE p.id = :id")
    Long getBalanceJpql(@Param("id") Long id);

}
