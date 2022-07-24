package dev.struchkov.example.transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepeatableReadExample {

    public static final String READ = "SELECT person.balance FROM person WHERE id = ?";
    public static final String UPDATE = "UPDATE person SET balance = ? WHERE id = ?";

    public static void main(String[] args) {
        try {
            final Connection connectionOne = Repository.getConnection();
            final Connection connectionTwo = Repository.getConnection();

            connectionOne.setAutoCommit(false);
            connectionTwo.setAutoCommit(false);

            final int transactionLevel = Connection.TRANSACTION_REPEATABLE_READ;
            connectionOne.setTransactionIsolation(transactionLevel);
            connectionTwo.setTransactionIsolation(transactionLevel);

            final PreparedStatement readOne = connectionOne.prepareStatement(READ);
            readOne.setLong(1, 1);

            final PreparedStatement readTwo = connectionTwo.prepareStatement(READ);
            readTwo.setLong(1, 1);

            final ResultSet resultSetOne = readOne.executeQuery();
            resultSetOne.next();
            final long balanceOne = resultSetOne.getLong(1);

            final ResultSet resultSetTwo = readTwo.executeQuery();
            resultSetTwo.next();
            final long balanceTwo = resultSetTwo.getLong(1);

            final PreparedStatement updateOne = connectionOne.prepareStatement(UPDATE);
            updateOne.setLong(1, balanceOne + 10);
            updateOne.setLong(2, 1);
            updateOne.execute();

            connectionOne.commit();
            connectionOne.close();

            final PreparedStatement updateTwo = connectionTwo.prepareStatement(UPDATE);
            updateTwo.setLong(1, balanceTwo + 5);
            updateTwo.setLong(2, 1);
            updateTwo.execute();

            connectionTwo.commit();
            connectionTwo.close();

        } catch (SQLException e) {
            System.out.println(e);
        }

    }

}
