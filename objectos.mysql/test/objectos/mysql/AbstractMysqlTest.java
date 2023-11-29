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
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import objectos.concurrent.Concurrent;
import objectos.concurrent.CpuWorkerService;
import objectos.concurrent.FixedCpuWorker;
import objectos.concurrent.IoWorkerService;
import objectos.concurrent.SingleThreadIoWorker;
import objectos.core.io.Charsets;
import objectos.core.io.Read;
import objectos.core.service.Services;
import objectos.core.testing.Next;
import objectos.fs.Directory;
import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;
import objectos.fs.testing.TmpDir;
import objectos.notes.Level;
import objectos.notes.NoOpNoteSink;
import objectos.notes.NoteSink;
import objectos.notes.console.ConsoleNoteSink;
import objectos.util.list.UnmodifiableList;
import org.testng.annotations.BeforeClass;

public abstract class AbstractMysqlTest {

  Charset charset = Charsets.utf8();

  Client client;

  Directory directoryBase;

  Directory directoryBinariesClient;

  Directory directoryBinariesServer;

  MysqlFs fs;

  Release release;

  Server server;

  ConfigurationFile serverConfigurationFile;

  @SuppressWarnings("unused")
  private UnmodifiableList<String> executeInputStreamSource;

  private UnmodifiableList<String> executeStatement;

  private RegularFile full;

  private RegularFile fullBackup;

  private String fullChecksum;

  private UnmodifiableList<String> fullRestore;

  private RegularFile inc0;

  private String inc0Checksum;

  private RegularFile inc1;

  private String inc1Checksum;

  private UnmodifiableList<RegularFile> incrementalBackup;

  private UnmodifiableList<String> incrementalRestore;

  private NoteSink logger;

  private UnmodifiableList<String> setLoginPath;

  @BeforeClass
  public final void _beforeClass() throws Exception {
    charset = Charsets.utf8();

    _beforeClassInit();

    Directory root;
    root = TmpDir.create();

    fs = new MysqlFs(root);

    logger = ConsoleNoteSink.of(Level.TRACE);

    // logger.setSysout(true);
  }

  public abstract void _beforeClassInit() throws IOException;

  public final void testCase01(ConfigurationGroup mysqld,
                               UnmodifiableList<String> expected) throws IOException {
    Directory etc;
    etc = fs.etc;

    ResolvedPath notFound;
    notFound = etc.resolve("mysqld.cnf");

    serverConfigurationFile = Mysql.configurationFile(
        notFound,

        charset,

        mysqld
    );

    RegularFile regularFile;
    regularFile = notFound.toRegularFile();

    UnmodifiableList<String> lines;
    lines = Read.lines(regularFile, charset);

    assertEquals(lines, expected);
  }

  public final void testCase02InitializeInsecure() throws InterruptedException, IOException {
    Directory data;
    data = fs.data;

    assertTrue(data.isEmpty());

    Server server;
    server = Mysql.createServer(
        directoryBinariesServer,

        serverConfigurationFile
    );

    Process initialization;
    initialization = server.initializeInsecure(
        Mysql.skipLogBin()
    );

    initialization.waitFor();

    assertFalse(data.isEmpty());

    RegularFile errorLogFile;
    errorLogFile = fs.getErrorLogFile();

    String errorLog;
    errorLog = Read.string(errorLogFile, Charset.defaultCharset());

    assertTrue(errorLog.contains("Please consider switching off the --initialize-insecure"));

    ResolvedPath mysqlData;
    mysqlData = data.resolve("mysql");

    assertTrue(mysqlData.exists());
  }

  public final void testCase03Impl() throws Exception {
    server = Mysql.createServer(
        directoryBinariesServer,

        serverConfigurationFile
    );

    Services.start(server);

    waitReadyForConnections(0);
  }

  public final void testCase04Impl() throws Exception {
    Directory etc;
    etc = fs.etc;

    ResolvedPath notFound;
    notFound = etc.resolve("mysql.cnf");

    ConfigurationFile clientConfigurationFile;
    clientConfigurationFile = Mysql.configurationFile(
        notFound,

        charset,

        Mysql.client(
            fs.socketOption
        ),

        Mysql.mysql(
            Mysql.binaryMode(),

            Mysql.skipColumnNames()
        ),

        Mysql.mysqldump(
            Mysql.singleTransaction(),

            Mysql.allDatabases(),

            Mysql.events(),

            Mysql.routines(),

            Mysql.flushLogs(),

            Mysql.masterData(2),

            Mysql.deleteMasterLogs()
        )
    );

    LoginPathFile loginPathFile;
    loginPathFile = Mysql.loginPathFile(
        etc.resolve("login.cnf")
    );

    IoWorkerService ioWorker;
    ioWorker = new SingleThreadIoWorker(NoOpNoteSink.of());

    CpuWorkerService worker;
    worker = new FixedCpuWorker(1, 1, NoOpNoteSink.of());

    Services.start(
        ioWorker,

        worker
    );

    client = Mysql.createClient(
        directoryBinariesClient,

        clientConfigurationFile,

        loginPathFile,

        ioWorker,

        logger
    );

    offerSetLoginPathJob(
        LoginPaths.ROOT,

        Mysql.user("root"),

        Mysql.password("")
    );

    assertNotNull(setLoginPath);

    assertTrue(loginPathFile.exists());
  }

  public final void testCase05Impl() throws Exception {
    String rootPassword;
    rootPassword = Next.string(32);

    String mysqldumpPassword;
    mysqldumpPassword = Next.string(32);

    offerExecuteStatementJob(
        LoginPaths.ROOT,

        release.isRelease5_6()
            ? Mysql.sql("SET PASSWORD FOR 'root'@'localhost' = PASSWORD('", rootPassword, "');")
            : Mysql.sql("ALTER USER 'root'@'localhost' IDENTIFIED BY '", rootPassword, "';"),

        Mysql.sql(
            "CREATE USER 'mysqldump'@'localhost' IDENTIFIED BY '",
            mysqldumpPassword,
            "';"
        ),

        Mysql.sql(
            "GRANT ",
            "EVENT, ",
            "SELECT, ",
            "SHOW VIEW, ",
            "PROCESS, ",
            "RELOAD, ",
            "REPLICATION CLIENT, ",
            "SUPER, ",
            "TRIGGER ",
            "ON *.* TO 'mysqldump'@'localhost';"
        )
    );

    assertNotNull(executeStatement);

    offerSetLoginPathJob(
        LoginPaths.ROOT,

        Mysql.user("root"),

        Mysql.password(rootPassword)
    );

    assertNotNull(setLoginPath);

    offerSetLoginPathJob(
        LoginPaths.DUMP,

        Mysql.user("mysqldump"),

        Mysql.password(mysqldumpPassword)
    );

    assertNotNull(setLoginPath);

    offerExecuteStatementJob(
        LoginPaths.ROOT,

        "SHOW DATABASES;"
    );

    assertEquals(
        executeStatement,

        UnmodifiableList.of(
            "information_schema",
            "mysql",
            "performance_schema",
            release.isRelease5_6() ? "test" : "sys"
        )
    );
  }

  public void testCase06Impl(String... dbs) throws Exception {
    offerExecuteStatementJob(
        LoginPaths.ROOT,

        createDatabase("db0"),

        createDatabase("db1"),

        createDatabase("db2"),

        createTable("db0", "a"),

        createTable("db1", "a"),

        createTable("db2", "a"),

        insertRandomData("db0", "a"),

        insertRandomData("db1", "a"),

        insertRandomData("db2", "a"),

        "SHOW DATABASES;"
    );

    assertNotNull(executeStatement);

    assertEquals(
        executeStatement,

        UnmodifiableList.copyOf(dbs)
    );

    Directory backupDirectory;
    backupDirectory = fs.backup;

    offerFullBackupJob(
        LoginPaths.DUMP,

        backupDirectory
    );

    assertNotNull(fullBackup);

    full = fullBackup;

    fullChecksum = computeChecksum("db0", "db1", "db2");
  }

  public final void testCase07Impl() throws Exception {
    offerExecuteStatementJob(
        LoginPaths.ROOT,

        createTable("db0", "b"),

        insertRandomData("db0", "a"),

        insertRandomData("db0", "b"),

        deleteSomeData("db1", "a", 128),

        createTable("db2", "b"),

        insertRandomData("db2", "a"),

        insertRandomData("db2", "b")
    );

    assertNotNull(executeStatement);

    Directory backupDirectory;
    backupDirectory = fs.backup;

    offerIncrementalBackupJob(
        LoginPaths.DUMP,

        backupDirectory
    );

    assertNotNull(incrementalBackup);

    assertEquals(incrementalBackup.size(), 1);

    inc0 = incrementalBackup.get(0);

    inc0Checksum = computeChecksum("db0", "db1", "db2");

    offerExecuteStatementJob(
        LoginPaths.ROOT,

        deleteSomeData("db0", "a", 64),

        createTable("db1", "b"),

        insertRandomData("db1", "b"),

        insertRandomData("db2", "a"),

        deleteSomeData("db2", "b", 256)
    );

    assertNotNull(executeStatement);

    offerIncrementalBackupJob(
        LoginPaths.DUMP,

        backupDirectory
    );

    assertNotNull(incrementalBackup);

    assertEquals(incrementalBackup.size(), 1);

    inc1 = incrementalBackup.get(0);

    inc1Checksum = computeChecksum("db0", "db1", "db2");
  }

  public void testCase08Impl() throws Exception {
    String currentChecksum;
    currentChecksum = computeChecksum("db0", "db1", "db2");

    assertNotEquals(fullChecksum, currentChecksum);

    offerExecuteStatementJob(
        LoginPaths.ROOT,

        "DROP DATABASE db0;",

        "DROP DATABASE db1;",

        "DROP DATABASE db2;"
    );

    assertNotNull(executeStatement);

    offerFullRestoreJob(LoginPaths.ROOT, full);

    assertNotNull(fullRestore);

    currentChecksum = computeChecksum("db0", "db1", "db2");

    assertEquals(fullChecksum, currentChecksum);
  }

  public void testCase09Impl() throws Exception {
    String currentChecksum;
    currentChecksum = computeChecksum("db0", "db1", "db2");

    assertNotEquals(inc0Checksum, currentChecksum);

    assertNotEquals(inc1Checksum, currentChecksum);

    UnmodifiableList<RegularFile> files;
    files = UnmodifiableList.of(inc0, inc1);

    Directory workDirectory;
    workDirectory = fs.root;

    offerIncrementalRestoreJob(
        LoginPaths.ROOT,

        workDirectory,

        files
    );

    assertNotNull(incrementalRestore);

    currentChecksum = computeChecksum("db0", "db1", "db2");

    assertEquals(inc1Checksum, currentChecksum);
  }

  private String checksumTable(String database) {
    return Mysql.sql(
        "USE ", database, ";",

        "CHECKSUM TABLE a, b;"
    );
  }

  private String computeChecksum(String... databases) throws Exception {
    int count;
    count = databases.length;

    String[] statements;
    statements = new String[count];

    for (int i = 0; i < count; i++) {
      String db;
      db = databases[i];

      String statement;
      statement = checksumTable(db);

      statements[i] = statement;
    }

    offerExecuteStatementJob(
        LoginPaths.ROOT,

        statements
    );

    assertNotNull(executeStatement);

    return executeStatement.join(System.lineSeparator());
  }

  private String createDatabase(String database) {
    return Mysql.sql(
        "CREATE DATABASE ", database, ";",

        "USE ", database, ";"
    );
  }

  private String createTable(String database, String table) {
    return Mysql.sql(
        "USE ", database, ";",

        "CREATE TABLE ", table, " (",
        "  ", table, "_id CHAR(10),",
        "  ", table, "_value INT,",
        "  KEY (", table, "_id)",
        ");"
    );
  }

  private String deleteSomeData(String database, String table, int count) {
    return Mysql.sql(
        "USE ", database, ";",

        "DELETE FROM ", table, " LIMIT ", Integer.toString(count), ";"
    );
  }

  private <V> V execute(ClientJob<V> job) throws Exception {
    reset();

    Concurrent.exhaust(job);

    return job.getResult();
  }

  private String insertRandomData(String database, String table) {
    return Mysql.sql(
        "USE ", database, ";",

        insertRandomData0(table)
    );
  }

  private String insertRandomData0(String table) {
    return Mysql.sql(
        "INSERT INTO ", table, " (", table, "_id, ", table, "_value) VALUES ", randomTuples(), ";"
    );
  }

  private void offerExecuteStatementJob(
                                        LoginPath loginPath, String... statements)
                                                                                   throws Exception {
    executeStatement = execute(
        client.executeStatement(loginPath, statements)
    );
  }

  private void offerFullBackupJob(LoginPath loginPath, Directory directory)
                                                                            throws Exception {
    fullBackup = execute(
        client.fullBackup(loginPath, directory)
    );
  }

  private void offerFullRestoreJob(LoginPath loginPath, RegularFile file)
                                                                          throws Exception {
    fullRestore = execute(
        client.fullRestore(loginPath, file)
    );
  }

  private void offerIncrementalBackupJob(LoginPath loginPath, Directory directory)
                                                                                   throws Exception {
    incrementalBackup = execute(
        client.incrementalBackup(loginPath, directory)
    );
  }

  private void offerIncrementalRestoreJob(
                                          LoginPath loginPath, Directory workDirectory, UnmodifiableList<RegularFile> files)
                                                                                                                             throws Exception {
    incrementalRestore = execute(
        client.incrementalRestore(loginPath, workDirectory, files)
    );
  }

  private void offerSetLoginPathJob(ConfigEditorOption... options) throws Exception {
    setLoginPath = execute(
        client.setLoginPath(options)
    );
  }

  private String randomTuple() {
    return Mysql.tuple(
        Next.string(10),

        Integer.toString(Next.intValue())
    );
  }

  private String randomTuples() {
    StringBuilder result;
    result = new StringBuilder();

    for (int i = 0; i < 1024; i++) {
      result.append(randomTuple());

      result.append(',');
    }

    result.append(randomTuple());

    return result.toString();
  }

  private void reset() {
    executeInputStreamSource = null;

    executeStatement = null;

    fullBackup = null;

    fullRestore = null;

    incrementalBackup = null;

    incrementalRestore = null;

    setLoginPath = null;
  }

  private void waitReadyForConnections(long offset) throws IOException, InterruptedException, TimeoutException {
    ResolvedPath errorLogObject;
    errorLogObject = fs.errorLogPath;

    long startTime;
    startTime = System.nanoTime();

    long timeOut;
    timeOut = TimeUnit.SECONDS.toNanos(10);

    while (!server.isReadyForConnections(errorLogObject, offset)) {
      Thread.sleep(10);

      long elapsed;
      elapsed = System.nanoTime() - startTime;

      if (elapsed > timeOut) {
        throw new TimeoutException("elapsed > 10 seconds");
      }
    }

    Thread.sleep(10);
  }

}