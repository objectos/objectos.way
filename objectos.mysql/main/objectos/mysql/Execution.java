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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import objectos.util.list.GrowableList;
import objectos.util.list.UnmodifiableList;

abstract class Execution {

  private final ProcessBuilder builder;

  private final List<String> command;

  private final Map<String, String> environment;

  private final GrowableList<String> error = new GrowableList<>();

  private int exitValue = -1;

  private final GrowableList<String> output = new GrowableList<>();

  Execution() {
    builder = new ProcessBuilder();

    command = builder.command();

    environment = builder.environment();
  }

  public final void addOption(String option) {
    command.add(option);
  }

  public final void putEnvironment(String key, String value) {
    environment.put(key, value);
  }

  public void setPassword(String value) {
    throw new UnsupportedOperationException();
  }

  final void end(Process process) throws IOException {
    InputStream in;
    in = process.getInputStream();

    consume(in, output);

    InputStream err;
    err = process.getErrorStream();

    consume(err, error);

    try {
      exitValue = process.waitFor();
    } catch (InterruptedException e) {
      exitValue = -1;
      Thread.interrupted();
      throw new IOException("Got interrupted while waiting for process to finish", e);
    }
  }

  final Process start() throws IOException {
    return builder.start();
  }

  final UnmodifiableList<String> sysout() throws ExecutionException {
    UnmodifiableList<String> sysout;
    sysout = output.toUnmodifiableList();

    if (exitValue == 0) {
      return sysout;
    }

    UnmodifiableList<String> syserr;
    syserr = error.toUnmodifiableList();

    throw new ExecutionException(exitValue, syserr, sysout);
  }

  final void throwExecutionExceptionIfNecessary() throws ExecutionException {
    if (exitValue != 0) {
      UnmodifiableList<String> sysout;
      sysout = output.toUnmodifiableList();

      UnmodifiableList<String> syserr;
      syserr = error.toUnmodifiableList();

      throw new ExecutionException(exitValue, syserr, sysout);
    }
  }

  private void consume(InputStream in, GrowableList<String> result) throws IOException {
    try (var sr = new InputStreamReader(in); var r = new BufferedReader(sr)) {
      String line;
      line = r.readLine();

      while (line != null) {
        result.add(line);

        line = r.readLine();
      }
    }
  }

}