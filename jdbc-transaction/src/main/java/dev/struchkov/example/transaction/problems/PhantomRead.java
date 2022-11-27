package dev.struchkov.example.transaction.problems;

import dev.struchkov.example.transaction.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <p><b>Фантомное чтение (phantom reads)</b> — в результатах повторяющегося запроса появляются и исчезают строки, которые в данный момент модифицирует параллельная транзакция.</p>
 */
public class PhantomRead {

    private static final int ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;

    public static void main(String[] args) {
        try(
                final Connection connection = Repository.getConnection();
                final Statement statement = connection.createStatement()
        ) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(ISOLATION_LEVEL);

            final ResultSet resultSet = statement.executeQuery("SELECT count(*) FROM person");
            while (resultSet.next()) {
                final int count = resultSet.getInt(1);
                System.out.println("Count: " + count);
            }

            new OtherTransaction().start();
            Thread.sleep(2000);

            final ResultSet resultSetTwo = statement.executeQuery("SELECT count(*) FROM person");
            while (resultSetTwo.next()) {
                final int count = resultSetTwo.getInt(1);
                System.out.println("Count: " + count);
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

                statement.executeUpdate("INSERT INTO person(id, balance) values (3, 1000)");
                connection.commit();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
