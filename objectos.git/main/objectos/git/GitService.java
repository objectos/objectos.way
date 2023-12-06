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

import objectos.concurrent.Computation;
import objectos.concurrent.CpuArray;
import objectos.concurrent.CpuWorker;
import objectos.concurrent.IoWorker;
import objectos.fs.Directory;
import objectos.lang.object.Check;
import objectos.notes.NoOpNoteSink;
import objectos.notes.NoteSink;
import objectos.util.set.UnmodifiableSet;

/**
 * A class for supporting the execution of {@link GitCommand} instances.
 *
 * <p>
 * A {@code GitService} allows for the execution of Git commands by offering the
 * required parameters of the
 * {@link GitCommand#submitAsync(GitCommandExecutor, CpuWorker)} method.
 *
 * <p>
 * Additionally, a {@code GitService} allows for configuring a number values
 * that influence, among others, its memory footprint and concurrency level.
 *
 * <p>
 * Applications will typically have one instance of this class per JVM.
 *
 * <h2>Creation example</h2>
 *
 * The following is a code example showing how to obtain a new instance of a
 * {@code GitService}. It creates a new instance using the default values for
 * all of the configuration options:
 *
 * <pre>{@code
 * GitService service;
 * service = GitService.create(cpuArray, ioWorker);
 * }</pre>
 *
 * <p>
 * The next example explicitly sets all configuration options with the default
 * values. Therefore, the previous and the next example are equivalent:
 *
 * <pre>{@code
 * GitService service;
 * service = GitService.create(
 *     cpuArray,
 *
 *     ioWorker,
 *
 *     GitService.bufferSize(4096),
 *
 *     GitService.enginesPerWorker(1),
 *
 *     GitService.logger(NoopLogger.getInstance())
 * );
 * }</pre>
 *
 * @since 3
 */
public final class GitService implements GitExecutor {

  private final CpuArray cpuArray;

  private final GitCommandExecutor gitCommandExecutor = new ThisCommandExecutor();

  private final GitWorker[] workers;

  GitService(CpuArray cpuArray, GitWorker[] workers) {
    this.cpuArray = cpuArray;

    this.workers = workers;
  }

  /**
   * The size in bytes of the internal buffers of each {@link GitEngine} in
   * this service.
   *
   * @param size
   *        the size in bytes
   *
   * @return a new option that sets the buffer size to the specified value
   *
   * @throws IllegalArgumentException
   *         if {@code size < 64}
   *
   * @see GitService#create(CpuArray, IoWorker, Option...)
   * @see GitEngine#bufferSize(int)
   */
  public static Option bufferSize(final int size) {
    Git.checkBufferSize(size);

    return new Option() {
      @Override
      final void acceptBuilder(GitServiceBuilder builder) {
        builder.setBufferSize(size);
      }
    };
  }

  /**
   * Creates a new {@code GitService} instance configured with the provided
   * values.
   *
   * <p>
   * Options are provided by {@code static} methods in this class. The available
   * options are:
   *
   * <table class="plain">
   * <caption>Options provided by the {@code GitService} class</caption>
   * <thead>
   * <tr>
   * <th scope="col">Method name</th>
   * <th scope="col">Description</th>
   * <th scope="col">Default value</th>
   * </tr>
   * </thead>
   * <tbody>
   * <tr>
   * <th scope="row">{@link GitService#bufferSize(int) bufferSize}</th>
   * <td>The size in bytes of the buffers used by each {@link GitEngine} in this
   * service</td>
   * <td>{@code 4096}</td>
   * </tr>
   * <tr>
   * <th scope="row">{@link GitService#enginesPerWorker(int)
   * enginesPerWorker}</th>
   * <td>The number of {@link GitEngine} instances per {@link CpuWorker}. It
   * defines the concurrency level per {@link CpuWorker}.</td>
   * <td>{@code 1}</td>
   * </tr>
   * <tr>
   * <th scope="row">{@link GitService#logger(NoteSink) logger}</th>
   * <td>The logger instance to use</td>
   * <td>{@link NoOpNoteSink#getInstance()}</td>
   * </tr>
   * </tbody>
   * </table>
   *
   * @param cpuArray
   *        the CPU worker array to be used by this Git service
   * @param ioWorker
   *        the I/O worker
   * @param options
   *        the additional configuration options
   *
   * @return a new {@code GitService} instance configured with the provided
   *         values
   */
  public static GitService create(CpuArray cpuArray, IoWorker ioWorker, Option... options) {
    Check.notNull(cpuArray, "cpuArray == null");
    Check.notNull(ioWorker, "ioWorker == null");
    Check.notNull(options, "options == null");

    GitServiceBuilder builder;
    builder = new GitServiceBuilder(cpuArray, ioWorker);

    for (int i = 0; i < options.length; i++) {
      Option option;
      option = options[i];

      Check.notNull(option, "options[", i, "] == null");

      option.acceptBuilder(builder);
    }

    return builder.build();
  }

  /**
   * The number of {@link GitEngine} instances per {@link CpuWorker}.
   *
   * <p>
   * For each {@link CpuWorker} in the {@link CpuArray} used to create this
   * service, the specified value of {@link GitEngine}s are created. For
   * example, if a {@code GitService} is created with:
   *
   * <ul>
   * <li>a cpu array containing {@code 4} cpu workers; and</li>
   * <li>a value of {@code 3} engines per worker.</li>
   * </ul>
   *
   * <p>
   * The service will contain a total of {@code 12} {@link GitEngine} instances,
   * three for each thread (cpu worker). Therefore, in this example, each thread
   * will be able to run, at most, three concurrent Git tasks.
   *
   * @param value
   *        the number of engines per worker to set
   *
   * @return a new option that sets the engines per worker to the
   *         specified value
   *
   * @throws IllegalArgumentException
   *         if {@code value <= 0}
   *
   * @see GitService#create(CpuArray, IoWorker, Option...)
   */
  public static Option enginesPerWorker(final int value) {
    Check.argument(value > 0, "engines/worker minimum value is 1 engine/worker");

    return new Option() {
      @Override
      final void acceptBuilder(GitServiceBuilder builder) {
        builder.setEnginesPerWorker(value);
      }
    };
  }

  /**
   * The logger instance to be used by this service (and by all of the
   * {@link GitEngine} instances).
   *
   * @param logger
   *        the logger instance
   *
   * @return a new option that sets the logger instance to the specified value
   */
  public static Option logger(final NoteSink logger) {
    Check.notNull(logger, "logger == null");

    return new Option() {
      @Override
      final void acceptBuilder(GitServiceBuilder builder) {
        builder.setLogger(logger);
      }
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final <V> Computation<V> submit(GitCommand<V> command) {
    Check.notNull(command, "command == null");

    int index;
    index = Git.randomIndex(cpuArray);

    CpuWorker cpuWorker;
    cpuWorker = cpuArray.get(index);

    return command.submitAsync(gitCommandExecutor, cpuWorker);
  }

  final Computation<MaybeObjectId> updateRef(
                                             Repository repository, RefName ref, ObjectId newValue) {
    GitWorker w;
    w = randomWorker();

    return w.updateRef(repository, ref, newValue);
  }

  final Computation<ObjectId> writeCommit(Repository repository, MutableCommit commit) {
    GitWorker w;
    w = randomWorker();

    return w.writeCommit(repository, commit);
  }

  final Computation<ObjectId> writeTree(
                                        GitCommand<?> command, Repository repository, MutableTree tree) {
    GitWorker w;
    w = randomWorker();

    return w.writeTree(command, repository, tree);
  }

  private GitWorker randomWorker() {
    int index;
    index = Git.randomIndex(workers);

    return workers[index];
  }

  /**
   * A {@code GitService} configuration option.
   */
  public abstract static class Option {

    Option() {}

    abstract void acceptBuilder(GitServiceBuilder builder);

  }

  private class ThisCommandExecutor implements GitCommandExecutor {

    @Override
    public final Computation<UnmodifiableSet<ObjectId>> copyObjects(
                                                                    Repository source, UnmodifiableSet<ObjectId> objectsToCopy, Repository destination) {
      GitWorker w;
      w = randomWorker();

      return w.copyObjects(source, objectsToCopy, destination);
    }

    @Override
    public final Computation<MaterializedEntry> materializeEntry(
                                                                 Repository repository, Entry entry, Directory directory) {
      GitWorker w;
      w = randomWorker();

      return w.materializeEntry(repository, entry, directory);
    }

    @Override
    public final Computation<Repository> openRepository(Directory directory) {
      GitWorker w;
      w = randomWorker();

      return w.openRepository(directory);
    }

    @Override
    public final Computation<Blob> readBlob(Repository repository, ObjectId id) {
      GitWorker w;
      w = randomWorker();

      return w.readBlob(repository, id);
    }

    @Override
    public final Computation<Commit> readCommit(Repository repository, ObjectId id) {
      GitWorker w;
      w = randomWorker();

      return w.readCommit(repository, id);
    }

    @Override
    public final Computation<Tree> readTree(Repository repository, ObjectId id) {
      GitWorker w;
      w = randomWorker();

      return w.readTree(repository, id);
    }

    @Override
    public final Computation<MaybeObjectId> resolve(Repository repository, RefName ref) {
      GitWorker w;
      w = randomWorker();

      return w.resolve(repository, ref);
    }

    @Override
    public final <V> Computation<V> submit(GitCommand<V> command) {
      return GitService.this.submit(command);
    }

    @Override
    public final Computation<MaybeObjectId> updateRef(
                                                      Repository repository, RefName ref, ObjectId newValue) {
      GitWorker w;
      w = randomWorker();

      return w.updateRef(repository, ref, newValue);
    }

    @Override
    public final Computation<ObjectId> writeCommit(Repository repository, MutableCommit commit) {
      GitWorker w;
      w = randomWorker();

      return w.writeCommit(repository, commit);
    }

    @Override
    public final Computation<ObjectId> writeTree(
                                                 GitCommand<?> command, Repository repository, MutableTree tree) {
      GitWorker w;
      w = randomWorker();

      return w.writeTree(command, repository, tree);
    }

  }

}