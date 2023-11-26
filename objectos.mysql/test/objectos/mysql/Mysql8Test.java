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

import java.io.IOException;
import objectos.fs.LocalFs;
import objectos.util.list.UnmodifiableList;
import org.testng.annotations.Test;

public class Mysql8Test extends AbstractMysqlTest {

  @Override
  public void _beforeClassInit() throws IOException {
    directoryBase = LocalFs.getDirectory(
        System.getProperty("objectos.mysql8.baseDir")
    );

    directoryBinariesClient = LocalFs.getDirectory(
        System.getProperty("objectos.mysql8.binaryDirClient")
    );

    directoryBinariesServer = LocalFs.getDirectory(
        System.getProperty("objectos.mysql8.binaryDirServer")
    );

    release = Release.MYSQL_8;
  }

  @Test
  public void testCase01() throws IOException {
    testCase01(
        Mysql.mysqld(
            Mysql.basedir(directoryBase),

            Mysql.characterSetServer(MysqlCharset.UTF8MB4),

            Mysql.datadir(fs.data),

            Mysql.disabledStorageEngines(
                StorageEngine.ARCHIVE,

                StorageEngine.BLACKHOLE,

                StorageEngine.CSV,

                StorageEngine.EXAMPLE,

                StorageEngine.FEDERATED,

                StorageEngine.MEMORY,

                StorageEngine.MERGE,

                StorageEngine.MRG_MYISAM,

                StorageEngine.PERFORMANCE_SCHEMA
            ),

            Mysql.generalLog(Toggle.ON),

            Mysql.generalLogFile(fs.generalLogPath),

            Mysql.innodbBufferPoolChunkSize(64, Unit.MB),

            Mysql.innodbBufferPoolSize(64, Unit.MB),

            Mysql.logBin(fs.binlog, "binlog"),

            Mysql.logError(fs.errorLogPath),

            Mysql.maxConnections(4),

            Mysql.mysqlx(Toggle.OFF),

            Mysql.serverId(1),

            Mysql.skipNetworking(),

            Mysql.skipSsl(),

            fs.socketOption,

            release.isRelease5_6()
                ? Mysql.noop()
                : Mysql.sqlMode(
                    SqlMode.STRICT_TRANS_TABLES,
                    SqlMode.NO_ZERO_IN_DATE,
                    SqlMode.NO_ZERO_DATE,
                    SqlMode.ERROR_FOR_DIVISION_BY_ZERO,
                    SqlMode.NO_ENGINE_SUBSTITUTION
                ),

            Mysql.tmpdir(fs.root),

            Mysql.waitTimeout(90000)
        ),

        UnmodifiableList.of(
            "[mysqld]",
            "basedir=" + directoryBase.getPath(),
            "character-set-server=utf8mb4",
            "datadir=" + fs.data.getPath(),
            "disabled-storage-engines=ARCHIVE,BLACKHOLE,CSV,EXAMPLE,FEDERATED,MEMORY,MERGE,MRG_MYISAM,PERFORMANCE_SCHEMA",
            "general-log=ON",
            "general-log-file=" + fs.generalLogPath.getPath(),
            "innodb-buffer-pool-chunk-size=64MB",
            "innodb-buffer-pool-size=64MB",
            "log-bin=" + fs.binlog.getPath() + "/binlog",
            "log-error=" + fs.errorLogPath.getPath(),
            "max-connections=4",
            "mysqlx=OFF",
            "server-id=1",
            "skip-networking",
            "skip-ssl",
            "socket=" + fs.socketPath.getPath(),
            "sql-mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION",
            "tmpdir=" + fs.root.getPath(),
            "wait-timeout=90000"
        )
    );
  }

  @Test(dependsOnMethods = "testCase01")
  public void testCase02() throws InterruptedException, IOException {
    testCase02InitializeInsecure();
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
        "db0",
        "db1",
        "db2",
        "information_schema",
        "mysql",
        "performance_schema",
        "sys"
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