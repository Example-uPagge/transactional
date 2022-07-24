package dev.struchkov.example.transaction;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Repository {

    public static Connection getConnection() {
        try {
            final String url = "jdbc:postgresql://localhost:5432/test";
            final String user = "postgres";
            final String passwd = "121314Ma";
            return DriverManager.getConnection(url, user, passwd);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static Connection getConnectionH2() {
        try {
            final String url = "jdbc:h2:file:/Users/upagge/IdeaProjects/struchkov/example/spring-boot/spring-boot-transaction/db/test";
            final String user = "sa";
            final String passwd = "pass";
            return DriverManager.getConnection(url, user, passwd);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
