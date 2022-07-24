package dev.struchkov.example.transaction.service;

import dev.struchkov.example.transaction.domain.Person;
import dev.struchkov.example.transaction.domain.TransactionMoney;
import dev.struchkov.example.transaction.repository.PersonRepository;
import dev.struchkov.example.transaction.repository.TransactionMoneyRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionMoneyService {

    private final TransactionMoneyRepository transactionMoneyRepository;
    private final PersonRepository personRepository;

    @Transactional
    public void sendMoney(@NonNull Long personFromId, @NonNull Long personToId, @NonNull Long amount) {
        final Person personFrom = personRepository.findById(personFromId).orElseThrow();
        final Person personTo = personRepository.findById(personToId).orElseThrow();

        final TransactionMoney transactionMoney = new TransactionMoney();
        transactionMoney.setPersonTo(personTo);
        transactionMoney.setPersonFrom(personFrom);
        transactionMoney.setAmount(amount);

        transactionMoneyRepository.save(transactionMoney);
        personFrom.setBalance(personFrom.getBalance() - amount);
        personTo.setBalance(personTo.getBalance() + amount);
    }

    public void surprise() {
        throw new RuntimeException("Сюрприиииз");
    }

}
