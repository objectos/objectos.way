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
import java.io.InputStream;
import java.io.OutputStream;
import objectos.core.io.InputStreamSource;
import objectos.fs.ResolvedPath;
import objectos.util.list.UnmodifiableList;

final class ExecuteInputStreamSource extends AbstractClientJob<UnmodifiableList<String>> {

  private static final byte _CLOSE = 0;

  private static final byte _COPY = 1;

  private static final byte IO_CLOSE = 0;

  private static final byte IO_COPY = 1;

  private static final byte IO_OPEN = 2;

  private final byte[] byteArray;

  private final ConfigurationFile configurationFile;

  private boolean copyMore;

  private InputStream inputStream;

  private final LoginPath loginPath;

  private final LoginPathFile loginPathFile;

  private final ResolvedPath mysql;

  private final UnmodifiableList<ShellOption> options;

  private OutputStream outputStream;

  private final InputStreamSource source;

  ExecuteInputStreamSource(Client worker,
                           LoginPath loginPath,
                           InputStreamSource source,
                           ShellOption... options) {
    super(worker);

    byteArray = worker.getByteArray();

    configurationFile = worker.getConfigurationFile();

    this.loginPath = loginPath;

    loginPathFile = worker.getLoginPathFile();

    mysql = worker.getPath(Program.MYSQL);

    this.options = UnmodifiableList.copyOf(options);

    this.source = source;
  }

  @Override
  final byte execute(byte state) {
    switch (state) {
      case _CLOSE:
        return executeClose();
      case _COPY:
        return executeCopy();
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
      case IO_COPY:
        ioCopy();
        break;
      case IO_OPEN:
        ioOpen();
        break;
      default:
        throw new UnsupportedOperationException("Implement me: task=" + task);
    }
  }

  @Override
  final byte executeStart() {
    addCommand(mysql.getPath());

    addCommand(configurationFile);

    addCommand(loginPathFile);

    addCommand(loginPath);

    for (int i = 0, size = options.size(); i < size; i++) {
      ShellOption option;
      option = options.get(i);

      addCommand(option);
    }

    return toIo(IO_OPEN, _COPY, _CLOSE);
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
    return toIo(IO_OPEN, toFinally());
  }

  private byte executeCopy() {
    if (copyMore) {
      return toIo(IO_COPY, _COPY, _CLOSE);
    }

    else {
      return toIo(IO_CLOSE, toFinally());
    }
  }

  private void ioClose() throws IOException {
    IOException rethrow;
    rethrow = null;

    rethrow = closeOne(rethrow, inputStream);

    inputStream = null;

    rethrow = closeOne(rethrow, outputStream);

    outputStream = null;

    if (rethrow != null) {
      throw rethrow;
    }
  }

  private void ioCopy() throws IOException {
    int count;
    count = inputStream.read(byteArray);

    if (count > 0) {
      outputStream.write(byteArray, 0, count);

      outputStream.flush();

      copyMore = true;
    } else {
      copyMore = false;
    }
  }

  private void ioOpen() throws IOException {
    startProcess();

    startStderrCollector();

    startStdoutCollector();

    outputStream = getProcessOutputStream();

    inputStream = source.openInputStream();

    ioCopy();
  }

}