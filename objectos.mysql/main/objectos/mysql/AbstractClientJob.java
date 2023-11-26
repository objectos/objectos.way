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
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import objectos.concurrent.IoTask;
import objectos.concurrent.IoWorker;
import objectos.lang.object.Check;
import objectos.util.list.GrowableList;
import objectos.util.list.UnmodifiableList;

abstract class AbstractClientJob<V> implements ClientJob<V>, IoTask {

  private static final byte _FINALLY = -8;

  private static final byte _FINALLY_REALLY = -7;

  private static final byte _START = -6;

  private static final byte _STOP = -5;

  private static final byte _SUB_TASK = -4;

  private static final byte _WAIT_CLIENT = -3;

  private static final byte _WAIT_IO = -2;

  private static final byte _WAIT_PROCESS = -1;

  final Charset charset;

  final Client client;

  private int exitValue;

  private IOException ioException;

  private byte ioExceptional;

  private final IoWorker ioWorker;

  private byte ioReady;

  private volatile boolean ioRunning;

  private byte ioTask;

  private Process process;

  private final ProcessBuilder processBuilder = new ProcessBuilder();

  private RuntimeException runtimeException;

  private byte state = _WAIT_CLIENT;

  private UnmodifiableList<String> stderr = UnmodifiableList.of();

  private StreamCollector stderrCollector;

  private UnmodifiableList<String> stdout = UnmodifiableList.of();

  private StreamCollector stdoutCollector;

  private AbstractClientJob<?> subTask;

  private byte subTaskReady;

  private Object subTaskResult;

  AbstractClientJob(Client worker) {
    this.client = worker;

    this.charset = worker.getCharset();

    this.ioWorker = worker.getIoExecutor();
  }

  @Override
  public final void executeIo() {
    try {
      executeIoTask(ioTask);
    } catch (IOException e) {
      ioException = e;
    } finally {
      ioRunning = false;
    }
  }

  @Override
  public final void executeOne() {
    state = execute0();
  }

  @Override
  public final V getResult() throws IOException {
    Check.state(!isActive(), "job is active");

    if (runtimeException != null) {
      throw runtimeException;
    }

    IOException e;
    e = ioException;

    ioException = null;

    return getResultImpl(e, stderr, stdout);
  }

  @Override
  public final boolean isActive() {
    return state != _STOP;
  }

  final void addCommand(ConfigurationFile file) {
    file.acceptProcessBuilder(processBuilder);
  }

  final void addCommand(LoginPathFile file) {
    file.acceptProcessBuilder(processBuilder);
  }

  final void addCommand(Option option) {
    option.acceptClientJob(this);
  }

  final void addCommand(String value) {
    List<String> command;
    command = processBuilder.command();

    command.add(value);
  }

  final IOException closeOne(IOException ioe, Closeable c) {
    IOException rethrow = ioe;

    if (c != null) {
      try {
        c.close();
      } catch (IOException e) {
        if (rethrow == null) {
          rethrow = e;
        } else {
          rethrow.addSuppressed(e);
        }
      }
    }

    return rethrow;
  }

  final void closeTwo(Closeable c1, Closeable c2) throws IOException {
    IOException rethrow;
    rethrow = null;

    try {
      c1.close();
    } catch (IOException e) {
      rethrow = e;
    }

    try {
      c2.close();
    } catch (IOException e) {
      if (rethrow == null) {
        rethrow = e;
      } else {
        rethrow.addSuppressed(e);
      }
    }

    if (rethrow != null) {
      throw rethrow;
    }
  }

  final String createBackupPrefix() {
    SimpleDateFormat format;
    format = new SimpleDateFormat("yyyy-MM-dd'T'HHmmss");

    Date now;
    now = new Date();

    return "backup-" + format.format(now);
  }

  byte execute(byte state) {
    throw new UnsupportedOperationException("Implement me: state=" + state);
  }

  abstract void executeIoTask(byte task) throws IOException;

  abstract byte executeStart();

  final String getCommandString() {
    StringBuilder result;
    result = new StringBuilder();

    List<String> command;
    command = processBuilder.command();

    if (!command.isEmpty()) {
      String s;
      s = command.get(0);

      result.append(s);

      for (int i = 1; i < command.size(); i++) {
        result.append(' ');

        s = command.get(i);

        result.append(s);
      }
    }

    return result.toString();
  }

  final InputStream getProcessInputStream() {
    return process.getInputStream();
  }

  final OutputStream getProcessOutputStream() {
    return process.getOutputStream();
  }

  abstract V getResultImpl(
                           IOException exception, UnmodifiableList<String> stderr, UnmodifiableList<String> stdout)
                                                                                                                    throws IOException;

  @SuppressWarnings("unchecked")
  final <X> X getSubTaskResult() {
    Object r;
    r = subTaskResult;

    subTaskResult = null;

    return (X) r;
  }

  void setPassword(String value) {
    addCommand("--password");
  }

  final void startProcess() throws IOException {
    process = processBuilder.start();
  }

  final void startStderrCollector() {
    stderrCollector = new StreamCollector("mysql.stderr", process.getErrorStream());

    stderrCollector.start();
  }

  final void startStdoutCollector() {
    stdoutCollector = new StreamCollector("mysql.stdout", process.getInputStream());

    stdoutCollector.start();
  }

  final byte toException(RuntimeException thrown) {
    runtimeException = thrown;

    return toFinally();
  }

  final byte toFinally() {
    return _FINALLY;
  }

  final byte toIo(byte task, byte onReady) {
    return toIo(task, onReady, _FINALLY);
  }

  final byte toIo(byte task, byte onReady, byte onException) {
    ioTask = task;

    ioReady = onReady;

    ioExceptional = onException;

    // volatile
    ioRunning = true;

    ioWorker.submit(this);

    return _WAIT_IO;
  }

  final byte toSubTask(AbstractClientJob<?> task, byte onReady) {
    subTask = task;

    subTaskReady = onReady;

    subTask.skipClientWait();

    return _SUB_TASK;
  }

  private UnmodifiableList<String> collect(StreamCollector c) {
    c.interrupt();

    try {
      c.join();
    } catch (InterruptedException e) {
      IOException maybeSuppressed;
      maybeSuppressed = new IOException("Interrupted while collecting process stream", e);

      if (ioException != null) {
        ioException.addSuppressed(maybeSuppressed);
      } else {
        ioException = maybeSuppressed;
      }
    }

    return c.get();
  }

  private byte execute0() {
    switch (state) {
      case _FINALLY:
        return executeFinally();
      case _FINALLY_REALLY:
        return executeFinallyReally();
      case _START:
        return executeStart();
      case _SUB_TASK:
        return executeSubTask();
      case _WAIT_CLIENT:
        return executeWaitClient();
      case _WAIT_IO:
        return executeWaitIo();
      case _WAIT_PROCESS:
        return executeWaitProcess();
      default:
        return execute(state);
    }
  }

  private byte executeFinally() {
    if (processIsAlive()) {
      return executeWaitProcess();
    }

    else {
      return executeFinallyReally();
    }
  }

  private byte executeFinallyReally() {
    if (stderrCollector != null) {
      stderr = collect(stderrCollector);

      stderrCollector = null;
    }

    if (stdoutCollector != null) {
      stdout = collect(stdoutCollector);

      stdoutCollector = null;
    }

    subTask = null;

    subTaskResult = null;

    if (process != null) {
      if (exitValue != 0) {
        ExecutionException executionException;
        executionException = new ExecutionException(exitValue, stderr, stdout);

        if (ioException != null) {
          ioException.addSuppressed(executionException);
        } else {
          ioException = executionException;
        }
      }

      process = null;
    }

    List<String> command;
    command = processBuilder.command();

    command.clear();

    Map<String, String> environment;
    environment = processBuilder.environment();

    environment.clear();

    client.unset(this);

    return _STOP;
  }

  private byte executeSubTask() {
    if (subTask.isActive()) {
      subTask.executeOne();

      return _SUB_TASK;
    }

    try {
      subTaskResult = subTask.getResult();

      return subTaskReady;
    } catch (IOException e) {
      ioException = e;

      return _FINALLY;
    }
  }

  private byte executeWaitClient() {
    if (client.isSet()) {
      return _WAIT_CLIENT;
    }

    else {
      client.set(this);

      return _START;
    }
  }

  private byte executeWaitIo() {
    if (ioRunning) {
      return _WAIT_IO;
    }

    else if (ioException != null) {
      return ioExceptional;
    }

    else {
      return ioReady;
    }
  }

  private byte executeWaitProcess() {
    try {
      exitValue = process.exitValue();

      return _FINALLY_REALLY;
    } catch (IllegalThreadStateException e) {
      return _WAIT_PROCESS;
    }
  }

  private boolean processIsAlive() {
    if (process == null) {
      return false;
    }

    int executeProcessWait;
    executeProcessWait = executeWaitProcess();

    return executeProcessWait != _FINALLY_REALLY;
  }

  private void skipClientWait() {
    Check.state(state == _WAIT_CLIENT, "state != _WAIT_CLIENT");

    state = _START;
  }

  private class StreamCollector extends Thread {

    private final InputStream inputStream;

    private final GrowableList<String> lines = new GrowableList<>();

    StreamCollector(String name, InputStream inputStream) {
      super(name);
      this.inputStream = inputStream;

      setDaemon(true);
    }

    public final UnmodifiableList<String> get() {
      return lines.toUnmodifiableList();
    }

    @Override
    public final void run() {
      try (var isr = new InputStreamReader(inputStream, charset); var r = new BufferedReader(isr)) {
        String line;
        line = r.readLine();

        while (line != null) {
          lines.add(line);

          line = r.readLine();
        }
      } catch (IOException e) {
        // log
      }
    }

  }

}