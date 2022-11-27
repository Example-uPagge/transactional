package dev.struchkov.example.transaction.problems;

import dev.struchkov.example.transaction.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <p><b>Неповторяющееся чтение (non-repeatable reads)</b> — запрос с одними и теми же условиями даёт неодинаковые результаты в рамках транзакции.</p>
 */
public class NonRepeatableRead {

    private static final int ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;

    public static void main(String[] args) {
        try (
                final Connection connection = Repository.getConnection();
                final Statement statement = connection.createStatement()
        ) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(ISOLATION_LEVEL);

            final ResultSet resultSetOne = statement.executeQuery("SELECT * FROM person WHERE id = 1");
            while (resultSetOne.next()) {
                final String balance = resultSetOne.getString("balance");
                System.out.println("[one] Balance: " + balance);
            }

            new OtherTransaction().start();
            Thread.sleep(2000);

            final ResultSet resultSetTwo = statement.executeQuery("SELECT * FROM person WHERE id = 1");
            while (resultSetTwo.next()) {
                final String balance = resultSetTwo.getString("balance");
                System.out.println("[one] Balance: " + balance);
            }

        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static class OtherTransaction extends Thread {
        @Override
        public void run() {
            try (
                    final Connection connection = Repository.getConnection();
                    final Statement statement = connection.createStatement()
            ) {
                connection.setAutoCommit(false);
                connection.setTransactionIsolation(ISOLATION_LEVEL);

                statement.executeUpdate("UPDATE person SET balance = 100000 WHERE id = 1");
                connection.commit();

                final ResultSet resultSetTwo = statement.executeQuery("SELECT * FROM person WHERE id = 1");
                while (resultSetTwo.next()) {
                    final String balance = resultSetTwo.getString("balance");
                    System.out.println("[two] Balance: " + balance);
                }

                connection.commit();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
