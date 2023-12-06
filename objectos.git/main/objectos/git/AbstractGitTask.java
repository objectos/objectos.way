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

import java.util.concurrent.ExecutionException;
import objectos.lang.object.Check;
import objectos.lang.object.ToString;
import objectos.notes.Note1;
import objectos.notes.NoteSink;

abstract class AbstractGitTask<V> implements GitTask<V>, ResultConsumer, ToString.Formattable {

  private static final byte _RESULT = 1;

  private static final byte _SET_INPUT = 2;

  private static final byte _STOP = 0;

  private static final byte _TASK = 3;

  private static final byte _WAIT_TASK_LOCK = 4;

  GitCommand<?> command;

  final GitEngine engine;

  private Throwable error;

  private Object result;

  private byte state = _WAIT_TASK_LOCK;

  private AbstractGitEngineTask task;

  AbstractGitTask(GitEngine engine) {
    this.engine = engine;
  }

  @Override
  public final void consumeResult(Throwable error, Object result) {
    this.error = error;

    this.result = result;
  }

  @Override
  public final void executeOne() {
    state = execute();
  }

  @SuppressWarnings("unchecked")
  @Override
  public final V getResult() throws IllegalStateException, ExecutionException {
    Check.state(state == _STOP, "task is active");

    if (error != null) {
      throw new ExecutionException(error);
    }

    if (result != null) {
      return (V) result;
    }

    throw new AssertionError("No result was produced");
  }

  @Override
  public final boolean isActive() {
    return state != _STOP;
  }

  @Override
  public final String toString() {
    return ToString.of(this);
  }

  void executeFinally() {}

  abstract AbstractGitEngineTask executeSetInputImpl();

  final <V1> void send(Note1<V1> event, V1 v1) {
    NoteSink logger;
    logger = engine.getLogger();

    logger.send(event, v1);
  }

  final void setCommand(GitCommand<?> origin) {
    this.command = origin;
  }

  private byte execute() {
    switch (state) {
      case _RESULT:
        return executeResult();
      case _SET_INPUT:
        return executeSetInput();
      case _TASK:
        return executeTask();
      case _WAIT_TASK_LOCK:
        return executeWaitTaskLock();
      default:
        throw new UnsupportedOperationException("Implement me: state=" + state);
    }
  }

  private byte executeResult() {
    task.acceptResultConsumer(this);

    executeFinally();

    engine.unlock(this);

    return _STOP;
  }

  private byte executeSetInput() {
    task = executeSetInputImpl();

    return _TASK;
  }

  private byte executeTask() {
    if (task.isActive()) {
      task.executeOne();

      return _TASK;
    } else {
      return _RESULT;
    }
  }

  private byte executeWaitTaskLock() {
    if (engine.tryLock(this)) {
      return _SET_INPUT;
    } else {
      return _WAIT_TASK_LOCK;
    }
  }

}