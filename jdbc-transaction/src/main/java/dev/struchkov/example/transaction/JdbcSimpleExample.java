package dev.struchkov.example.transaction;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcSimpleExample {

    public static final String INSERT_TRANSACTION_SQL = "INSERT INTO transaction(person_from, person_to, amount) values (?, ?, ?)";
    public static final String UPDATE_BALANCE_PERSON_FROM_SQL = "UPDATE person SET balance = (balance - ?) WHERE id = ?";
    public static final String UPDATE_BALANCE_PERSON_TO_SQL = "UPDATE person SET balance = (balance + ?) WHERE id = ?";

    public static void main(String[] args) {
        final JdbcSimpleExample jdbcSimpleExample = new JdbcSimpleExample();
        jdbcSimpleExample.runNoTransaction(2L, 1L, 100L);
//        jdbcSimpleExample.runWithTransaction(2L, 1L, 100L);
    }

    private void runNoTransaction(Long personIdFrom, Long personIdTo, Long amount) {
        try (final Connection connection = Repository.getConnection()) {
            sendMoney(connection, personIdFrom, personIdTo, amount);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @SneakyThrows
    private void runWithTransaction(Long personIdFrom, Long personIdTo, Long amount) {
        final Connection connection = Repository.getConnection();

        try (connection) {
            connection.setAutoCommit(false);
            sendMoney(connection, personIdFrom, personIdTo, amount);
            connection.commit();
        } catch (RuntimeException | SQLException e) {
            connection.rollback();
        } finally {
            connection.close();
        }
    }

    private void sendMoney(Connection connection, Long personIdFrom, Long personIdTo, Long amount) throws SQLException {
        final PreparedStatement insertTransaction = connection.prepareStatement(INSERT_TRANSACTION_SQL);
        insertTransaction.setLong(1, personIdFrom);
        insertTransaction.setLong(2, personIdTo);
        insertTransaction.setLong(3, amount);

        final PreparedStatement updateBalancePersonTo = connection.prepareStatement(UPDATE_BALANCE_PERSON_TO_SQL);
        updateBalancePersonTo.setLong(1, amount);
        updateBalancePersonTo.setLong(2, personIdTo);

        final PreparedStatement updateBalancePersonFrom = connection.prepareStatement(UPDATE_BALANCE_PERSON_FROM_SQL);
        updateBalancePersonFrom.setLong(1, amount);
        updateBalancePersonFrom.setLong(2, personIdFrom);

        insertTransaction.execute();
        updateBalancePersonTo.execute();
        surprise();
        updateBalancePersonFrom.execute();
    }

    public void surprise() {
        throw new RuntimeException("Сюрприиииз");
    }

}
