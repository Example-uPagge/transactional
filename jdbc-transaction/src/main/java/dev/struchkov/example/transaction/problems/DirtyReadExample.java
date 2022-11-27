package dev.struchkov.example.transaction.problems;

import dev.struchkov.example.transaction.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <p><b>«Грязное» чтение (dirty reads)</b> — в результатах запроса появляются промежуточные результаты параллельной транзакции, которая ещё не завершилась.</p>
 */
public class DirtyReadExample {

    private static final int ISOLATION_LEVEL = Connection.TRANSACTION_READ_UNCOMMITTED;

    public static void main(String[] args) throws SQLException, InterruptedException {
        try (
                final Connection connection = Repository.getConnectionH2();
                final Statement statement = connection.createStatement()
        ) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(ISOLATION_LEVEL);

            statement.executeUpdate("UPDATE person SET balance = 100000 WHERE id = 1");

            new OtherTransaction().start();
            Thread.sleep(2000);
            connection.rollback();
        }

    }

    static class OtherTransaction extends Thread {
        @Override
        public void run() {
            try (
                    final Connection connection = Repository.getConnectionH2();
                    final Statement statement = connection.createStatement()
            ) {
                connection.setAutoCommit(false);
                connection.setTransactionIsolation(ISOLATION_LEVEL);

                final ResultSet resultSet = statement.executeQuery("SELECT * FROM person WHERE id = 1");
                while (resultSet.next()) {
                    System.out.println("Balance: " + resultSet.getString("balance"));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
