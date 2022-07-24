package dev.struchkov.example.transaction.problems;

import dev.struchkov.example.transaction.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PhantomRead {

    public static void main(String[] args) {
        try(
                final Connection connection = Repository.getConnection();
                final Statement statement = connection.createStatement()
        ) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            final ResultSet resultSet = statement.executeQuery("SELECT count(*) FROM person");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1));
            }

            new OtherTransaction().start();
            Thread.sleep(2000);

            final ResultSet resultSetTwo = statement.executeQuery("SELECT count(*) FROM person");
            while (resultSetTwo.next()) {
                System.out.println(resultSetTwo.getInt(1));
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
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

                statement.executeUpdate("INSERT INTO person(name, balance) values ('test', 100)");
                connection.commit();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
