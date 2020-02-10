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
package fr.openobject.karaf.labs.tutorial.boot;

import java.util.HashMap;
import java.util.Map;
import org.apache.karaf.boot.runtime.Boot;
import org.apache.karaf.boot.runtime.Config;
import org.apache.karaf.boot.runtime.Datasource;
import org.apache.karaf.boot.runtime.Feature;
import org.apache.karaf.boot.runtime.KarafApplication;
import org.apache.karaf.boot.runtime.Management;
import org.apache.karaf.boot.runtime.Repository;
import org.apache.karaf.boot.runtime.Runtime;
import org.apache.karaf.boot.runtime.Security;

@Runtime(name = "karaf-custom-runtime",
            environment = Runtime.Environment.DYNAMIC,
            jre = Runtime.Jre.JAVA_11,
            frameworks = {
                Runtime.Framework.STANDARD,
                Runtime.Framework.ENTERPRISE},
            repositories = {
                @Repository(name = "camel", url = "org.apache.camel.karaf/apache-camel", version = "3.0.0")
            },
            features = {
                @Feature(name = "http", version = "LATEST"),
                @Feature(name = "http-whiteboard", version = "LATEST")},
            bootPackages = {
                @Boot(name = "jaxrs", version = "1.2.0", pack = Boot.Pack.JAXRS),
                @Boot(name = "jpa", version = "2.1.0", pack = Boot.Pack.JPA),
            }
        )
public class BootRuntime {

    @Config(pid = "my.app.config", policy = Config.Policy.CREATE)
    private Map<String, String> myConfigFile() {
        Map<String, String> data = new HashMap<>();
        data.put("key1", "value1");
        return data;
    }

    @Config(pid = "my.app.config.resource", policy = Config.Policy.UPDATE, resource = "application.properties")
    private Map<String, String> myConfigFileFormResource() {
        Map<String, String> data = new HashMap<>();
        data.put("key1", "value1");
        return data;
    }

    @Datasource(name = "jdbc/my-datasource")
    private Map<Datasource.Property, String> myDatasourceFile() {
        Map<Datasource.Property, String> data = new HashMap<>();
        data.put(Datasource.Property.DatabaseName, "database1");
        data.put(Datasource.Property.DatabaseServer, "localhost");
        data.put(Datasource.Property.DatabasePort, "5432");
        data.put(Datasource.Property.DatabaseUser, "user");
        data.put(Datasource.Property.DatabasePassword, "password");
        return data;
    }

    @Datasource(name = "jdbc/my-datasource-file", resource = "datasource.properties")
    private Map<Datasource.Property, String> myDatasourceFileFromResource() {
        Map<Datasource.Property, String> data = new HashMap<>();
        data.put(Datasource.Property.DatabaseUser, "user");
        data.put(Datasource.Property.DatabasePassword, "password");
        return data;
    }

    @Management(type = Management.Type.Shell)
    private Map<Management.Shell, String> myShellConfig() {
        Map<Management.Shell, String> data = new HashMap<>();
        data.put(Management.Shell.RmiRegistryHost, "127.0.0.1");
        data.put(Management.Shell.RmiRegistryPort, "44444");
        return data;
    }

    @Management(type = Management.Type.Http)
    private Map<Management.Http, String> myHttpConfig() {
        Map<Management.Http, String> data = new HashMap<>();
        data.put(Management.Http.HttpPort, "8181");
        data.put(Management.Http.HttpsPort, "443");
        return data;
    }

    @Security(type = Security.Type.Group)
    private Map<String, Security.Role[]> mySecurityGroupConfig() {
        Map<String, Security.Role[]> data = new HashMap<>();
        data.put("my-group", new Security.Role[]{Security.Role.admin, Security.Role.group, Security.Role.ssh});
        return data;
    }

    @Security(type = Security.Type.User)
    private Map<String, String> mySecurityUserConfig() {
        Map<String, String> data = new HashMap<>();
        data.put("toto", "my-group");
        data.put("titi", "admingroup");
        return data;
    }

    public static void main(String[] args) {
        KarafApplication.run(BootRuntime.class, args);
    }

}
