package com.smpark.jdbc.config.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCConnection {

    // HikariDataSource : 커넥션 풀을 관리하는 리소스 라이브러리
    private static final HikariDataSource datasource;

    static {
        try {
            /**
             * Properties
             * 키 - 값을 쌍으로 저장하는 방식이다.
             * 주로 설정 정보나 구성 데이터를 관리하는데 더 유용하게 사용된다.
             */
            Properties props = new Properties();
            /*
             * Properties.load  외부 파일을 읽어오는 역할을 수행한다.
             */
            props.load(JDBCConnection.class.getClassLoader().getResourceAsStream("config.properties"));

            HikariConfig config = new HikariConfig();

            config.setJdbcUrl(props.getProperty("db.url"));
            config.setUsername(props.getProperty("db.username"));
            config.setPassword(props.getProperty("db.password"));

            // connection pool의 최대 값을 10개로 둔다.
            config.setMaximumPoolSize(10);
            // 최소 5개는 사용하겠다.
            config.setMinimumIdle(5);
            // 30000ms 동안 아무런 작업을 하지 않으면 소멸 시기켔다.
            config.setIdleTimeout(30000);

            config.setMaxLifetime(180000);
            //
            config.setConnectionTimeout(2000);

            datasource = new HikariDataSource(config);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return datasource.getConnection();
    }

    public static void close() {
        if (datasource != null) {
            datasource.close();
        }
    }

}