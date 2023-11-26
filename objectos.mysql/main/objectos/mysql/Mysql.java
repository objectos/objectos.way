package objectos.mysql;

import java.io.IOException;
import java.nio.charset.Charset;
import objectos.concurrent.IoWorker;
import objectos.fs.Directory;
import objectos.fs.PathName;
import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;
import objectos.notes.NoteSink;

public final class Mysql {

  private Mysql() {}

  public static BinlogOption raw() {
    return BinlogOnlyOption.raw();
  }

  public static BinlogOption readFromRemoteServer() {
    return BinlogOnlyOption.readFromRemoteServer();
  }

  public static Client createClient(Directory directory, ConfigurationFile configurationFile, LoginPathFile loginPathFile, IoWorker ioWorker, NoteSink logger) {
    return Client.createClient(directory, configurationFile, loginPathFile, ioWorker, logger);
  }

  public static String sql(String... parts) {
    return Client.sql(parts);
  }

  public static String tuple(Object... values) {
    return Client.tuple(values);
  }

  public static ClientOrConfigEditorOption user(String user) {
    return ClientOrConfigEditorOptionImpl.user(user);
  }

  public static ConfigurationFile configurationFile(RegularFile file, Charset charset) {
    return ConfigurationFile.configurationFile(file, charset);
  }

  public static ConfigurationFile configurationFile(ResolvedPath path, Charset charset, ConfigurationGroup... groups) throws IOException {
    return ConfigurationFile.configurationFile(path, charset, groups);
  }

  public static ConfigurationGroup client(ClientOption... options) {
    return ConfigurationGroup.client(options);
  }

  public static ConfigurationGroup mysql(ShellOption... options) {
    return ConfigurationGroup.mysql(options);
  }

  public static ConfigurationGroup mysqlbinlog(BinlogOption... options) {
    return ConfigurationGroup.mysqlbinlog(options);
  }

  public static ConfigurationGroup mysqld(ServerOption... options) {
    return ConfigurationGroup.mysqld(options);
  }

  public static ConfigurationGroup mysqldump(DumpOption... options) {
    return ConfigurationGroup.mysqldump(options);
  }

  public static DumpOption allDatabases() {
    return DumpOnlyOption.allDatabases();
  }

  public static DumpOption deleteMasterLogs() {
    return DumpOnlyOption.deleteMasterLogs();
  }

  public static DumpOption events() {
    return DumpOnlyOption.events();
  }

  public static DumpOption flushLogs() {
    return DumpOnlyOption.flushLogs();
  }

  public static DumpOption masterData(int value) {
    return DumpOnlyOption.masterData(value);
  }

  public static DumpOption routines() {
    return DumpOnlyOption.routines();
  }

  public static DumpOption singleTransaction() {
    return DumpOnlyOption.singleTransaction();
  }

  public static GlobalOption bindAddress(String address) {
    return GlobalOption.bindAddress(address);
  }

  public static GlobalOption defaultsFile(RegularFile file) {
    return GlobalOption.defaultsFile(file);
  }

  public static GlobalOption noDefaults() {
    return GlobalOption.noDefaults();
  }

  public static GlobalOption port(int value) {
    return GlobalOption.port(value);
  }

  public static GlobalOption socket(ResolvedPath path) {
    return GlobalOption.socket(path);
  }

  public static LoginPath loginPath(String name) {
    return LoginPath.loginPath(name);
  }

  public static LoginPathFile loginPathFile(PathName path) {
    return LoginPathFile.loginPathFile(path);
  }

  public static NoopOption noop() {
    return NoopOption.noop();
  }

  public static ClientOrConfigEditorOption password(String password) {
    return PasswordOption.password(password);
  }

  public static ShellOption binaryMode() {
    return ShellOnlyOption.binaryMode();
  }

  public static ShellOption skipColumnNames() {
    return ShellOnlyOption.skipColumnNames();
  }

  public static Server createServer(Directory directory, ConfigurationFile configurationFile) throws IOException {
    return Server.createServer(directory, configurationFile);
  }

  public static ServerOption basedir(Directory directory) {
    return ServerOnlyOption.basedir(directory);
  }

  public static ServerOption binlogFormat(BinlogFormat format) {
    return ServerOnlyOption.binlogFormat(format);
  }

  public static ServerOption characterSetServer(MysqlCharset charset) {
    return ServerOnlyOption.characterSetServer(charset);
  }

  public static ServerOption datadir(Directory directory) {
    return ServerOnlyOption.datadir(directory);
  }

  public static ServerOption defaultTimeZone(String value) {
    return ServerOnlyOption.defaultTimeZone(value);
  }

  public static ServerOption disabledStorageEngines(StorageEngine... engines) {
    return ServerOnlyOption.disabledStorageEngines(engines);
  }

  public static ServerOption explicitDefaultsForTimestamp(Toggle value) {
    return ServerOnlyOption.explicitDefaultsForTimestamp(value);
  }

  public static ServerOption generalLog(Toggle value) {
    return ServerOnlyOption.generalLog(value);
  }

  public static ServerOption generalLogFile(ResolvedPath path) {
    return ServerOnlyOption.generalLogFile(path);
  }

  public static ServerOption innodbBufferPoolChunkSize(int value, Unit unit) {
    return ServerOnlyOption.innodbBufferPoolChunkSize(value, unit);
  }

  public static ServerOption innodbBufferPoolSize(int value, Unit unit) {
    return ServerOnlyOption.innodbBufferPoolSize(value, unit);
  }

  public static ServerOption innodbDoublewrite(Toggle value) {
    return ServerOnlyOption.innodbDoublewrite(value);
  }

  public static ServerOption innodbFlushLogAtTrxCommit(int value) {
    return ServerOnlyOption.innodbFlushLogAtTrxCommit(value);
  }

  public static ServerOption innodbFlushMethod(FlushMethod value) {
    return ServerOnlyOption.innodbFlushMethod(value);
  }

  public static ServerOption innodbLogFileSize(int value, Unit unit) {
    return ServerOnlyOption.innodbLogFileSize(value, unit);
  }

  public static ServerOption innodbMaxUndoLogSize(int value, Unit unit) {
    return ServerOnlyOption.innodbMaxUndoLogSize(value, unit);
  }

  public static ServerOption innodbRollbackSegments(int value) {
    return ServerOnlyOption.innodbRollbackSegments(value);
  }

  public static ServerOption keyBufferSize(int value, Unit unit) {
    return ServerOnlyOption.keyBufferSize(value, unit);
  }

  public static ServerOption logBin(Directory directory, String prefix) {
    return ServerOnlyOption.logBin(directory, prefix);
  }

  public static ServerOption logBin(ResolvedPath path) {
    return ServerOnlyOption.logBin(path);
  }

  public static ServerOption logBinTrustFunctionCreators(Toggle value) {
    return ServerOnlyOption.logBinTrustFunctionCreators(value);
  }

  public static ServerOption logError(ResolvedPath path) {
    return ServerOnlyOption.logError(path);
  }

  public static ServerOption maxBinlogSize(int value, Unit unit) {
    return ServerOnlyOption.maxBinlogSize(value, unit);
  }

  public static ServerOption maxConnections(int value) {
    return ServerOnlyOption.maxConnections(value);
  }

  public static ServerOption mysqlx(Toggle value) {
    return ServerOnlyOption.mysqlx(value);
  }

  public static ServerOption performanceSchema(Toggle value) {
    return ServerOnlyOption.performanceSchema(value);
  }

  public static ServerOption pidFile(ResolvedPath path) {
    return ServerOnlyOption.pidFile(path);
  }

  public static ServerOption readBufferSize(int value, Unit unit) {
    return ServerOnlyOption.readBufferSize(value, unit);
  }

  public static ServerOption readRndBufferSize(int value, Unit unit) {
    return ServerOnlyOption.readRndBufferSize(value, unit);
  }

  public static ServerOption secureFilePriv(Directory directory) {
    return ServerOnlyOption.secureFilePriv(directory);
  }

  public static ServerOption serverId(int id) {
    return ServerOnlyOption.serverId(id);
  }

  public static ServerOption skipLogBin() {
    return ServerOnlyOption.skipLogBin();
  }

  public static ServerOption skipNetworking() {
    return ServerOnlyOption.skipNetworking();
  }

  public static ServerOption skipSsl() {
    return ServerOnlyOption.skipSsl();
  }

  public static ServerOption slowQueryLogFile(ResolvedPath path) {
    return ServerOnlyOption.slowQueryLogFile(path);
  }

  public static ServerOption sqlMode(SqlMode... modes) {
    return ServerOnlyOption.sqlMode(modes);
  }

  public static ServerOption syncBinlog(long value) {
    return ServerOnlyOption.syncBinlog(value);
  }

  public static ServerOption tmpdir(Directory directory) {
    return ServerOnlyOption.tmpdir(directory);
  }

  public static ServerOption waitTimeout(int value) {
    return ServerOnlyOption.waitTimeout(value);
  }

}