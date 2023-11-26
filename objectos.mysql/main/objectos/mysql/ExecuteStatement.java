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
import java.io.OutputStream;
import objectos.fs.ResolvedPath;
import objectos.util.list.UnmodifiableList;

final class ExecuteStatement extends AbstractClientJob<UnmodifiableList<String>> {

  private static final byte _CLOSE = 0;

  private static final byte _WRITE_STATEMENT = 1;

  private static final byte IO_CLOSE = 0;

  private static final byte IO_START_PROCESS = 1;

  private static final byte IO_WRITE_STATEMENT = 2;

  private volatile byte[] bytes;

  private final ConfigurationFile configurationFile;

  private final LoginPath loginPath;

  private final LoginPathFile loginPathFile;

  private final ResolvedPath mysql;

  private final UnmodifiableList<ShellOption> options;

  private volatile OutputStream outputStream;

  private int statementIndex;

  private final UnmodifiableList<String> statements;

  ExecuteStatement(Client worker,
                   LoginPath loginPath,
                   String[] statements,
                   ShellOption... options) {
    super(worker);

    configurationFile = worker.getConfigurationFile();

    this.loginPath = loginPath;

    loginPathFile = worker.getLoginPathFile();

    mysql = worker.getPath(Program.MYSQL);

    this.options = UnmodifiableList.copyOf(options);

    this.statements = UnmodifiableList.copyOf(statements);
  }

  @Override
  final byte execute(byte state) {
    switch (state) {
      case _CLOSE:
        return executeClose();
      case _WRITE_STATEMENT:
        return executeWriteStatement();
      default:
        throw new UnsupportedOperationException("Implement me: state=" + state);
    }
  }

  @Override
  final void executeIoTask(byte task) throws IOException {
    switch (task) {
      case IO_CLOSE:
        ioClose();
        break;
      case IO_START_PROCESS:
        ioStartProcess();
        break;
      case IO_WRITE_STATEMENT:
        ioWriteStatement();
        break;
      default:
        throw new UnsupportedOperationException("Implement me: task=" + task);
    }
  }

  @Override
  final byte executeStart() {
    if (statements.isEmpty()) {
      return toFinally();
    }

    statementIndex = 0;

    addCommand(mysql.getPath());

    addCommand(configurationFile);

    addCommand(loginPathFile);

    addCommand(loginPath);

    for (int i = 0, size = options.size(); i < size; i++) {
      ShellOption option;
      option = options.get(i);

      addCommand(option);
    }

    return toIo(IO_START_PROCESS, _WRITE_STATEMENT, _CLOSE);
  }

  @Override
  final UnmodifiableList<String> getResultImpl(
                                               IOException exception, UnmodifiableList<String> stderr, UnmodifiableList<String> stdout)
                                                                                                                                        throws IOException {
    if (exception == null) {
      return stdout;
    } else {
      throw exception;
    }
  }

  private byte executeClose() {
    return toIo(IO_CLOSE, toFinally());
  }

  private byte executeWriteStatement() {
    if (statementIndex < statements.size()) {
      String statement;
      statement = statements.get(statementIndex);

      bytes = statement.getBytes(charset);

      statementIndex++;

      return toIo(IO_WRITE_STATEMENT, _WRITE_STATEMENT, _CLOSE);
    }

    else {
      return executeClose();
    }
  }

  private void ioClose() throws IOException {
    if (outputStream != null) {
      outputStream.close();

      outputStream = null;
    }
  }

  private void ioStartProcess() throws IOException {
    startProcess();

    startStderrCollector();

    startStdoutCollector();

    outputStream = getProcessOutputStream();
  }

  private void ioWriteStatement() throws IOException {
    outputStream.write(bytes);
  }

}