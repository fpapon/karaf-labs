/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.openobject.karaf.labs.tutorial.test.container;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContainerPostgreSqlTest {

    @BeforeEach
    void before() {
        postgresqlContainer.start();
    }

    @AfterEach
    void after() {
        postgresqlContainer.stop();
    }

    private PostgreSQLContainer postgresqlContainer =
            new PostgreSQLContainer<>()
                    .withDatabaseName("foo")
                    .withUsername("foo")
                    .withPassword("secret")
                    .withInitScript("init-postgresql.sql");

    private ResultSet performQuery(JdbcDatabaseContainer container, String sql)
            throws SQLException {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(container.getJdbcUrl());
        hikariConfig.setUsername(container.getUsername());
        hikariConfig.setPassword(container.getPassword());

        HikariDataSource ds = new HikariDataSource(hikariConfig);
        Statement statement = ds.getConnection().createStatement();
        statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();
        resultSet.next();
        return resultSet;
    }

    @Test
    void testRunning() {
        assertTrue(postgresqlContainer.isRunning());
    }

    @Test
    public void testSimple() throws SQLException {
        ResultSet resultSet = performQuery(postgresqlContainer, "SELECT 1");
        int resultSetInt = resultSet.getInt(1);
        assertEquals(1, resultSetInt, "A basic SELECT query succeeds");
    }

    @Test
    public void testExplicitInitScript() throws SQLException {
        ResultSet resultSet = performQuery(postgresqlContainer, "SELECT foo FROM test.bar");
        String firstColumnValue = resultSet.getString(1);
        assertEquals(
                "hello world", firstColumnValue, "Value from init script should equal real value");
    }
}
