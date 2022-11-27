package dev.struchkov.example.transaction.problems;

import dev.struchkov.example.transaction.Repository;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p><b>Потерянное обновление (lost update)</b> — две параллельные транзакции меняют одни и те же данные, при этом итоговый результат обновления предсказать невозможно.</p>
 */
public class LostUpdateExample {

    public static final String READ = "SELECT person.balance FROM person WHERE id = ?";
    public static final String UPDATE = "UPDATE person SET balance = ? WHERE id = ?";

    @SneakyThrows
    public static void main(String[] args) {

        // Начинаем две транзакции.
        final Connection connectionOne = getNewConnection();
        final Connection connectionTwo = getNewConnection();

        // Первая и вторая транзакция запрашивают баланс пользователя.
        // balance = 1000
        final long balanceOne = getBalance(connectionOne);
        final long balanceTwo = getBalance(connectionTwo);

        // Первая транзакция готовится обновить баланс пользователю.
        final PreparedStatement updateOne = connectionOne.prepareStatement(UPDATE);
        updateOne.setLong(1, balanceOne + 10);
        updateOne.setLong(2, 1);
        updateOne.execute();

        // Первая транзакция фиксирует изменения и завершается.
        // Значение balance в базе в этот момент = 1010.
        connectionOne.commit();
        connectionOne.close();

        // Но вторая транзакция ничего не знает про изменения в БД.
        // Значение balanceTwo все еще равно 1000, к этому значению мы добавляем 5.
        final PreparedStatement updateTwo = connectionTwo.prepareStatement(UPDATE);
        updateTwo.setLong(1, balanceTwo + 5);
        updateTwo.setLong(2, 1);
        updateTwo.execute();

        // Вторая транзакция фиксирует свои изменения и завершается.
        // В итоге в БД остается значение 1005, а не 1015, как хотелось бы нам.
        connectionTwo.commit();
        connectionTwo.close();
    }

    private static Connection getNewConnection() throws SQLException {
        final Connection connection = Repository.getConnection();
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        return connection;
    }

    private static long getBalance(Connection connectionOne) throws SQLException {
        final PreparedStatement preparedStatement = connectionOne.prepareStatement(READ);
        preparedStatement.setLong(1, 1);
        final ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        final long balanceOne = resultSet.getLong(1);
        return balanceOne;
    }

}
