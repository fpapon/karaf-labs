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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

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
            new PostgreSQLContainer()
                    .withDatabaseName("foo")
                    .withUsername("foo")
                    .withPassword("secret");

    @Test
    void test() {
        Assertions.assertTrue(postgresqlContainer.isRunning());
    }
}
