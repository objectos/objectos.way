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

final class SetLoginPath extends AbstractClientJob<UnmodifiableList<String>> {

  private static final byte IO_EXECUTE_PROCESS = 0;

  private final LoginPathFile loginPathFile;

  private final ResolvedPath mysqlConfigEditor;

  private final ConfigEditorOption[] options;

  private String password;

  public SetLoginPath(Client worker,
                      ConfigEditorOption[] options) {
    super(worker);

    loginPathFile = worker.getLoginPathFile();

    mysqlConfigEditor = worker.getPath(Program.MYSQL_CONFIG_EDITOR);

    this.options = options;
  }

  @Override
  final void executeIoTask(byte task) throws IOException {
    switch (task) {
      case IO_EXECUTE_PROCESS:
        ioExecuteProcess();
        break;
      default:
        throw new UnsupportedOperationException("Implement me: task=" + task);
    }
  }

  @Override
  final byte executeStart() {
    addCommand(loginPathFile);

    addCommand("setsid");

    addCommand(mysqlConfigEditor.getPath());

    addCommand("set");

    addCommand("--skip-warn");

    password = null;

    for (int i = 0; i < options.length; i++) {
      ConfigEditorOption option;
      option = options[i];

      if (option == null) {
        NullPointerException npe;
        npe = new NullPointerException("options[" + i + "] == null");

        return toException(npe);
      }

      addCommand(option);
    }

    return toIo(IO_EXECUTE_PROCESS, toFinally());
  }

  @Override
  final UnmodifiableList<String> getResultImpl(
                                               IOException exception, UnmodifiableList<String> stderr, UnmodifiableList<String> stdout)
                                                                                                                                        throws IOException {
    if (exception == null) {
      return stdout;
    }

    else {
      throw exception;
    }
  }

  @Override
  final void setPassword(String value) {
    super.setPassword(value);

    password = value;
  }

  private void ioExecuteProcess() throws IOException {
    startProcess();

    startStdoutCollector();

    startStderrCollector();

    if (password != null) {
      try (OutputStream outputStream = getProcessOutputStream()) {
        byte[] bytes;
        bytes = password.getBytes();

        outputStream.write(bytes);

        outputStream.flush();
      }
    }
  }

}