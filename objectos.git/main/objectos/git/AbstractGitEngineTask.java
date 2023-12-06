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
package objectos.git;

import java.io.Closeable;
import java.io.IOException;
import objectos.concurrent.CpuTask;
import objectos.concurrent.IoTask;
import objectos.concurrent.IoWorker;
import objectos.lang.object.Check;
import objectos.notes.Note0;
import objectos.notes.Note1;
import objectos.notes.Note2;
import objectos.notes.NoteSink;

abstract class AbstractGitEngineTask implements CpuTask, IoTask {

  static final byte _FINALLY = -4;

  static final byte _START = -3;

  static final byte _STOP = 0;

  static final byte _SUB_TASK = -2;

  static final byte _WAIT_IO = -1;

  GitCommand<?> command;

  final GitInjector injector;

  byte state;

  private Throwable error;

  private byte ioExceptional;

  private byte ioReady;

  private boolean ioRunning;

  private byte ioTask;

  private IoWorker ioWorker;

  private NoteSink logger;

  private Object result;

  private AbstractGitEngineTask subTask;

  private byte subTaskReady;

  private Object subTaskResult;

  AbstractGitEngineTask(GitInjector injector) {
    this.injector = injector;
  }

  public final void catchThrowable(Throwable e) {
    if (error != null) {
      error.addSuppressed(e);
    } else {
      error = e;
    }
  }

  @Override
  public final void executeIo() {
    try {
      executeIo(ioTask);
    } catch (Throwable e) {
      error = e;
    } finally {
      ioRunning = false;
    }
  }

  @Override
  public final void executeOne() {
    try {
      state = execute(state);
    } catch (Throwable e) {
      error = e;

      state = _STOP;

      execute(state);
    }
  }

  @Override
  public final boolean isActive() {
    return state != _STOP;
  }

  public final void setResult(Object newResult) {
    Check.state(result == null, "result was already set");

    result = newResult;
  }

  final <V> void acceptResultConsumer(ResultConsumer consumer) {
    consumer.consumeResult(error, result);

    error = null;

    result = null;
  }

  final void checkSetInput() {
    Check.state(state == _STOP, "task is active");

    Check.state(result == null, "previous result was not consumed");

    state = _START;
  }

  final void close(Closeable resource) {
    error = Git.close(error, resource);
  }

  final Throwable error() {
    return error;
  }

  byte errorState() {
    return _FINALLY;
  }

  byte execute(byte state) {
    switch (state) {
      case _FINALLY:
        return executeFinally();
      case _START:
        return executeStart();
      case _STOP:
        return executeStop();
      case _SUB_TASK:
        return executeSubTask();
      case _WAIT_IO:
        return executeWaitIo();
      default:
        throw new UnsupportedOperationException("Implement me: state=" + state);
    }
  }

  byte executeFinally() {
    ioExceptional = 0;

    ioReady = 0;

    ioRunning = false;

    ioTask = 0;

    ioWorker = null;

    logger = null;

    subTask = null;

    subTaskReady = 0;

    subTaskResult = null;

    state = _STOP;

    return state;
  }

  void executeIo(byte task) throws IOException {
    throw new UnsupportedOperationException("Implement me: task=" + task);
  }

  byte executeStart() {
    ioWorker = injector.getIoWorker();

    logger = injector.getLogger();

    return _STOP;
  }

  byte executeStop() {
    return state;
  }

  @SuppressWarnings("unchecked")
  final <X> X getSubTaskResult() {
    Object r;
    r = subTaskResult;

    subTaskResult = null;

    return (X) r;
  }

  final boolean hasError() {
    return error != null;
  }

  final void ioReady(byte newState) {
    ioReady = newState;
  }

  final void send(Note0 event) {
    logger.send(event);
  }

  final <T1> void send(Note1<T1> event, T1 v1) {
    logger.send(event, v1);
  }

  final <T1, T2> void send(Note2<T1, T2> event, T1 v1, T2 v2) {
    logger.send(event, v1, v2);
  }

  final byte toError(Throwable e) {
    catchThrowable(e);

    return errorState();
  }

  final byte toIo(byte task, byte onReady, byte onExceptional) {
    ioTask = task;

    ioReady = onReady;

    ioExceptional = onExceptional;

    ioRunning = true;

    ioWorker.submit(this);

    return _WAIT_IO;
  }

  final byte toIoException(String message) {
    return toError(new IOException(message));
  }

  final byte toStubException(String message) {
    return toError(new GitStubException(message));
  }

  final byte toSubTask(AbstractGitEngineTask task, byte onReady) {
    subTask = task;

    subTaskReady = onReady;

    return _SUB_TASK;
  }

  private byte acceptSuperTask(AbstractGitEngineTask superTask) {
    if (error != null) {
      Throwable rethrow;
      rethrow = error;

      error = null;

      result = null;

      return superTask.toError(rethrow);
    }

    else if (result == null) {
      Class<? extends AbstractGitEngineTask> t;
      t = getClass();

      String simpleName;
      simpleName = t.getSimpleName();

      GitStubException e;
      e = new GitStubException(simpleName + " did not produce any result");

      return superTask.toError(e);
    }

    else {
      Object r;
      r = result;

      result = null;

      return superTask.toSubTaskResult(r);
    }
  }

  private byte executeSubTask() {
    if (subTask.isActive()) {
      subTask.executeOne();

      return state;
    } else {
      return subTask.acceptSuperTask(this);
    }
  }

  private byte executeWaitIo() {
    if (ioRunning) {
      return _WAIT_IO;
    }

    else if (error != null) {
      return ioExceptional;
    }

    else {
      return ioReady;
    }
  }

  private byte toSubTaskResult(Object r) {
    subTaskResult = r;

    return subTaskReady;
  }

}