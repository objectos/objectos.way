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
import objectos.concurrent.CpuWorker;
import objectos.concurrent.IoWorker;
import objectos.fs.Directory;
import objectos.git.GitEngine.Option;
import objectos.util.set.UnmodifiableSet;

final class GitWorker {

  private final CpuWorker cpuWorker;

  private final GitEngine[] engines;

  GitWorker(CpuWorker cpuWorker, GitEngine[] engines) {
    this.cpuWorker = cpuWorker;

    this.engines = engines;
  }

  public static GitWorker create(CpuWorker cpuWorker, IoWorker ioWorker, int enginesPerWorker, Option[] engineOptions) {
    GitEngine[] engines;
    engines = new GitEngine[enginesPerWorker];

    for (int i = 0; i < enginesPerWorker; i++) {
      engines[i] = GitEngine.create(ioWorker, engineOptions);
    }

    return new GitWorker(cpuWorker, engines);
  }

  public final Computation<UnmodifiableSet<ObjectId>> copyObjects(Repository source, UnmodifiableSet<ObjectId> objectsToCopy, Repository destination) {
    GitEngine e;
    e = randomEngine();

    GitTask<UnmodifiableSet<ObjectId>> task;
    task = e.copyObjects(source, objectsToCopy, destination);

    return create(task);
  }

  public final Computation<MaterializedEntry> materializeEntry(Repository repository, Entry entry, Directory directory) {
    GitEngine e;
    e = randomEngine();

    GitTask<MaterializedEntry> task;
    task = e.materializeEntry(repository, entry, directory);

    return create(task);
  }

  public final Computation<Repository> openRepository(Directory directory) {
    GitEngine e;
    e = randomEngine();

    GitTask<Repository> task;
    task = e.openRepository(directory);

    return create(task);
  }

  public final Computation<Blob> readBlob(Repository repository, ObjectId id) {
    GitEngine e;
    e = randomEngine();

    GitTask<Blob> task;
    task = e.readBlob(repository, id);

    return create(task);
  }

  public final Computation<Commit> readCommit(Repository repository, ObjectId id) {
    GitEngine e;
    e = randomEngine();

    GitTask<Commit> task;
    task = e.readCommit(repository, id);

    return create(task);
  }

  public final Computation<Tree> readTree(Repository repository, ObjectId id) {
    GitEngine e;
    e = randomEngine();

    GitTask<Tree> task;
    task = e.readTree(repository, id);

    return create(task);
  }

  public final Computation<MaybeObjectId> resolve(Repository repository, RefName ref) {
    GitEngine e;
    e = randomEngine();

    GitTask<MaybeObjectId> task;
    task = e.resolve(repository, ref);

    return create(task);
  }

  public final Computation<MaybeObjectId> updateRef(Repository repository, RefName ref, ObjectId newValue) {
    GitEngine e;
    e = randomEngine();

    GitTask<MaybeObjectId> task;
    task = e.updateRef(repository, ref, newValue);

    return create(task);
  }

  public final Computation<ObjectId> writeCommit(Repository repository, MutableCommit commit) {
    GitEngine e;
    e = randomEngine();

    GitTask<ObjectId> task;
    task = e.writeCommit(repository, commit);

    return create(task);
  }

  public final Computation<ObjectId> writeTree(GitCommand<?> command, Repository repository, MutableTree tree) {
    GitEngine e;
    e = randomEngine();

    GitTask<ObjectId> task;
    task = e.writeTree(repository, tree);

    return create(command, task);
  }

  private <V> Computation<V> create(GitCommand<?> command, GitTask<V> task) {
    AbstractGitTask<V> o;
    o = (AbstractGitTask<V>) task;

    o.setCommand(command);

    return create(task);
  }

  private <V> Computation<V> create(GitTask<V> task) {
    if (cpuWorker.offer(task)) {
      return task;
    } else {
      return new CpuTaskRejected<V>(task);
    }
  }

  private GitEngine randomEngine() {
    int index;
    index = Git.randomIndex(engines);

    return engines[index];
  }

}