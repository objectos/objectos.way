/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectos.mysql;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import objectos.core.io.Read;
import objectos.fs.Directory;
import objectos.fs.LocalFs;
import objectos.fs.ResolvedPath;
import objectos.util.list.UnmodifiableList;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Mysql56Test extends AbstractMysqlTest {

  @Override
  public void _beforeClassInit() throws IOException {
    directoryBase = LocalFs.getDirectory(
        System.getProperty("objectos.mysql56.baseDir")
    );

    directoryBinariesClient = LocalFs.getDirectory(
        System.getProperty("objectos.mysql56.binaryDirClient")
    );

    directoryBinariesServer = LocalFs.getDirectory(
        System.getProperty("objectos.mysql56.binaryDirServer")
    );

    release = Release.MYSQL_5_6;
  }

  @Test
  public void testCase01() throws IOException {
    testCase01(
        Mysql.mysqld(
            Mysql.basedir(directoryBase),

            Mysql.characterSetServer(MysqlCharset.UTF8MB4),

            Mysql.datadir(fs.data),

            Mysql.explicitDefaultsForTimestamp(Toggle.ON),

            Mysql.generalLog(Toggle.ON),

            Mysql.generalLogFile(fs.generalLogPath),

            Mysql.innodbBufferPoolSize(64, Unit.MB),

            Mysql.logBin(fs.binlog, "binlog"),

            Mysql.logError(fs.errorLogPath),

            Mysql.maxConnections(4),

            Mysql.serverId(1),

            Mysql.skipNetworking(),

            Mysql.skipSsl(),

            fs.socketOption,

            Mysql.tmpdir(fs.root),

            Mysql.waitTimeout(90000)
        ),

        UnmodifiableList.of(
            "[mysqld]",
            "basedir=" + directoryBase.getPath(),
            "character-set-server=utf8mb4",
            "datadir=" + fs.data.getPath(),
            "explicit-defaults-for-timestamp=ON",
            "general-log=ON",
            "general-log-file=" + fs.generalLogPath.getPath(),
            "innodb-buffer-pool-size=64MB",
            "log-bin=" + fs.binlog.getPath() + "/binlog",
            "log-error=" + fs.errorLogPath.getPath(),
            "max-connections=4",
            "server-id=1",
            "skip-networking",
            "skip-ssl",
            "socket=" + fs.socketPath.getPath(),
            "tmpdir=" + fs.root.getPath(),
            "wait-timeout=90000"
        )
    );
  }

  @Test(dependsOnMethods = "testCase01")
  public void testCase02() throws InterruptedException, IOException {
    Directory data;
    data = fs.data;

    assertTrue(data.isEmpty());

    String scriptsDir;
    scriptsDir = System.getProperty("objectos.mysql56.scriptsDir");

    if (scriptsDir == null) {
      Assert.fail("objectos.mysql56.scriptsDir property was not set");
    }

    Directory directoryScripts;
    directoryScripts = LocalFs.getDirectory(scriptsDir);

    Process handle;
    handle = Mysql56.install(
        directoryScripts,

        serverConfigurationFile,

        Mysql.basedir(directoryBase),

        Mysql.datadir(data)
    );

    int exitValue;
    exitValue = handle.waitFor();

    assertEquals(exitValue, 0);

    assertFalse(data.isEmpty());

    InputStream inputStream;
    inputStream = handle.getInputStream();

    InputStreamReader reader;
    reader = new InputStreamReader(inputStream, charset);

    UnmodifiableList<String> output;
    output = Read.lines(reader);

    assertTrue(output.contains("PLEASE REMEMBER TO SET A PASSWORD FOR THE MySQL root USER !"));

    ResolvedPath mysqlData;
    mysqlData = data.resolve("mysql");

    assertTrue(mysqlData.exists());
  }

  @Test(dependsOnMethods = "testCase02")
  public void testCase03() throws Exception {
    testCase03Impl();
  }

  @Test(dependsOnMethods = "testCase03")
  public void testCase04() throws Exception {
    testCase04Impl();
  }

  @Test(dependsOnMethods = "testCase04")
  public void testCase05() throws Exception {
    testCase05Impl();
  }

  @Test(dependsOnMethods = "testCase05")
  public void testCase06() throws Exception {
    testCase06Impl(
        "information_schema",
        "db0",
        "db1",
        "db2",
        "mysql",
        "performance_schema",
        "test"
    );
  }

  @Test(dependsOnMethods = "testCase06")
  public void testCase07() throws Exception {
    testCase07Impl();
  }

  @Test(dependsOnMethods = "testCase07")
  public void testCase08() throws Exception {
    testCase08Impl();
  }

  @Test(dependsOnMethods = "testCase08")
  public void testCase09() throws Exception {
    testCase09Impl();
  }

}