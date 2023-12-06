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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import objectos.concurrent.Computation;
import objectos.concurrent.Concurrent;
import objectos.concurrent.FixedCpuArray;
import objectos.concurrent.SingleThreadIoWorker;
import objectos.core.service.Services;
import objectos.fs.Directory;
import objectos.fs.ResolvedPath;
import objectos.fs.testing.TmpDir;
import objectos.lang.object.Check;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public abstract class AbstractGitTest implements ResultConsumer {

  static GitService service;

  final GitEngine engine = TestingGit.standardEngineInstance();

  Throwable error;

  Object result;

  @BeforeSuite
  public static void beforeSuiteGitService() throws Exception {
    GitTestingLogger logger;
    logger = new GitTestingLogger();

    FixedCpuArray cpuArray;
    cpuArray = new FixedCpuArray(10, 50, logger);

    SingleThreadIoWorker ioWorker;
    ioWorker = new SingleThreadIoWorker(logger);

    Services.start(
        cpuArray,

        ioWorker
    );

    service = GitService.create(
        cpuArray,

        ioWorker,

        GitService.bufferSize(64),

        GitService.enginesPerWorker(2),

        GitService.logger(logger)
    );
  }

  @BeforeMethod
  public final void beforeMethodClearResult() {
    error = null;

    result = null;
  }

  @Override
  public final void consumeResult(Throwable error, Object result) {
    this.error = error;

    this.result = result;
  }

  final void assertLooseExists(Repository repository, ObjectId objectId) throws IOException {
    ResolvedPath resolvedPath;
    resolvedPath = repository.resolveLooseObject(objectId);

    assertTrue(resolvedPath.exists());
  }

  final void assertLooseNotFound(Repository repository, ObjectId objectId) throws IOException {
    ResolvedPath resolvedPath;
    resolvedPath = repository.resolveLooseObject(objectId);

    assertFalse(resolvedPath.exists());
  }

  final void await(Computation<?> c) {
    while (c.isActive()) {
      // noop
    }
  }

  final Repository createEmptyRepository() throws ExecutionException, IOException {
    Directory directory;
    directory = TmpDir.create();

    directory.createDirectory("objects");

    return openRepository(directory);
  }

  final void executeAndAssertState(AbstractGitEngineTask task, byte... states) {
    for (int i = 0; i < states.length; i++) {
      byte expected;
      expected = states[i];

      assertTrue(task.isActive(), "index=" + i);

      task.executeOne();

      assertEquals(task.state, expected, "index=" + i);
    }
  }

  final void executeWhileAndAssertState(AbstractGitEngineTask task, byte state, byte expected) {
    Check.state(task.isActive(), "task is not active");

    task.executeOne();

    while (task.state == state) {
      Check.state(task.isActive(), "task is not active");

      task.executeOne();
    }

    if (task.state != expected) {
      String message;
      message = "Not the expected state: " + task.state + " != " + state;

      Throwable error;
      error = task.error();

      if (error != null) {
        Assert.fail(message, error);
      } else {
        Assert.fail(message);
      }
    }
  }

  @SuppressWarnings("unchecked")
  final <T> T getResult() {
    return (T) result;
  }

  final Repository openRepository(Directory directory) throws ExecutionException {
    GitTask<Repository> job;
    job = engine.openRepository(directory);

    return getResult(job);
  }

  final MaybeObjectId resolve(Repository repository, RefName ref) throws ExecutionException {
    GitTask<MaybeObjectId> task;
    task = engine.resolve(repository, ref);

    return getResult(task);
  }

  private <T> T getResult(GitTask<T> task) throws ExecutionException {
    Concurrent.exhaust(task);

    return task.getResult();
  }

}