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

import java.nio.charset.Charset;
import objectos.concurrent.IoWorker;
import objectos.core.io.InputStreamSource;
import objectos.fs.Directory;
import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;
import objectos.lang.object.Check;
import objectos.notes.NoteSink;
import objectos.util.list.UnmodifiableList;

public final class Client {

  private byte[] byteArray;

  private final ConfigurationFile configurationFile;

  private final Directory directory;

  private final IoWorker ioWorker;

  private AbstractClientJob<?> job;

  private final NoteSink logger;

  private final LoginPathFile loginPathFile;

  private Client(Directory directory,
                 ConfigurationFile configurationFile,
                 LoginPathFile loginPathFile,
                 IoWorker ioWorker,
                 NoteSink logger) {
    this.directory = directory;
    this.configurationFile = configurationFile;
    this.loginPathFile = loginPathFile;
    this.ioWorker = ioWorker;
    this.logger = logger;
  }

  public static Client createClient(Directory directory,
                                    ConfigurationFile configurationFile, LoginPathFile loginPathFile,
                                    IoWorker ioWorker, NoteSink logger) {
    Check.notNull(directory, "directory == null");
    Check.notNull(configurationFile, "configurationFile == null");
    Check.notNull(loginPathFile, "loginPathFile == null");
    Check.notNull(ioWorker, "ioWorker == null");
    Check.notNull(logger, "logger == null");

    return new Client(
        directory,
        configurationFile, loginPathFile,
        ioWorker, logger
    );
  }

  public static String sql(String... parts) {
    Check.notNull(parts, "parts == null");

    StringBuilder sql;
    sql = new StringBuilder();

    for (int i = 0; i < parts.length; i++) {
      String part;
      part = parts[i];

      if (part == null) {
        throw new NullPointerException("parts[" + i + "] == null");
      }

      sql.append(part);
    }

    return sql.toString();
  }

  public static String tuple(Object... values) {
    Check.notNull(values, "values == null");

    StringBuilder result;
    result = new StringBuilder();

    result.append('(');

    if (values.length > 0) {
      tuple0(result, values, 0);
    }

    for (int i = 1; i < values.length; i++) {
      result.append(',');

      tuple0(result, values, i);
    }

    result.append(')');

    return result.toString();
  }

  private static void tuple0(StringBuilder out, Object[] values, int index) {
    Object value;
    value = values[index];

    if (value == null) {
      throw new NullPointerException("values[" + index + "] == null");
    }

    String s;
    s = value.toString();

    if (s == null) {
      throw new NullPointerException("values[" + index + "].toString() == null");
    }

    out.append('\'');

    out.append(s);

    out.append('\'');
  }

  public final ClientJob<UnmodifiableList<String>> executeInputStreamSource(LoginPath loginPath,
                                                                            InputStreamSource source) {
    Check.notNull(loginPath, "loginPath == null");
    Check.notNull(source, "source == null");

    return new ExecuteInputStreamSource(this, loginPath, source);
  }

  public final ClientJob<UnmodifiableList<String>> executeStatement(LoginPath loginPath,
                                                                    String... statements) {
    Check.notNull(loginPath, "loginPath == null");
    Check.notNull(statements, "statements == null");

    return new ExecuteStatement(this, loginPath, statements);
  }

  public final ClientJob<RegularFile> fullBackup(LoginPath loginPath,
                                                 Directory targetDirectory) {
    Check.notNull(loginPath, "loginPath == null");
    Check.notNull(targetDirectory, "targetDirectory == null");

    return new FullBackup(this, loginPath, targetDirectory);
  }

  public final ClientJob<UnmodifiableList<String>> fullRestore(LoginPath loginPath,
                                                               RegularFile file) {
    Check.notNull(loginPath, "loginPath == null");
    Check.notNull(file, "file == null");

    return new FullRestore(this, loginPath, file);
  }

  public final ClientJob<UnmodifiableList<RegularFile>> incrementalBackup(LoginPath loginPath,
                                                                          Directory targetDirectory) {
    Check.notNull(loginPath, "loginPath == null");
    Check.notNull(targetDirectory, "targetDirectory == null");

    return new IncrementalBackup(this, loginPath, targetDirectory);
  }

  public final ClientJob<UnmodifiableList<String>> incrementalRestore(LoginPath loginPath,
                                                                      Directory workDirectory,
                                                                      UnmodifiableList<RegularFile> files) {
    Check.notNull(loginPath, "loginPath == null");
    Check.notNull(workDirectory, "workDirectory == null");
    Check.notNull(files, "files == null");

    return new IncrementalRestore(this, loginPath, workDirectory, files);
  }

  public final ClientJob<UnmodifiableList<String>> setLoginPath(ConfigEditorOption... options) {
    Check.notNull(options, "options == null");

    return new SetLoginPath(this, options);
  }

  final byte[] getByteArray() {
    if (byteArray == null) {
      byteArray = new byte[8192];
    }

    return byteArray;
  }

  final Charset getCharset() {
    return configurationFile.charset;
  }

  final ConfigurationFile getConfigurationFile() {
    return configurationFile;
  }

  final IoWorker getIoExecutor() {
    return ioWorker;
  }

  final NoteSink getLogger() {
    return logger;
  }

  final LoginPathFile getLoginPathFile() {
    return loginPathFile;
  }

  final ResolvedPath getPath(Program program) {
    return program.resolve(directory);
  }

  final boolean isSet() {
    return job != null;
  }

  final void set(AbstractClientJob<?> newJob) {
    Check.state(job == null, "A previous job is already running");

    job = newJob;
  }

  final void unset(AbstractClientJob<?> clientJob) {
    if (job == clientJob) {
      job = null;
    }
  }

}
