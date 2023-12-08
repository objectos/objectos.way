/*
 * Copyright (C) 2023 Objectos Software LTDA.
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import objectos.concurrent.Concurrent;
import objectos.concurrent.DirectIoWorker;
import objectos.fs.Directory;
import objectos.fs.LocalFs;
import objectos.fs.ResolvedPath;
import objectos.lang.object.Check;
import objectos.notes.NoteSink;
import objectos.util.set.UnmodifiableSet;

public class GitRepo {

  private final GitEngine engine;

  private final Repository repository;

  private GitRepo(GitEngine engine, Repository repository) {
    this.engine = engine;

    this.repository = repository;
  }

  public static GitRepo open(NoteSink noteSink, Path directory) throws IOException {
    Check.notNull(noteSink, "noteSink == null");
    Check.notNull(directory, "directory == null");

    GitEngine engine;
    engine = GitEngine.create(
        DirectIoWorker.get(),

        GitEngine.bufferSize(4096),

        GitEngine.logger(noteSink)
    );

    File directoryFile;
    directoryFile = directory.toFile();

    Directory legacyDirectory;
    legacyDirectory = LocalFs.getDirectory(directoryFile);

    GitTask<Repository> task;
    task = engine.openRepository(legacyDirectory);

    Repository repository;
    repository = get(task);

    return new GitRepo(engine, repository);
  }

  private static <T> T get(GitTask<T> task) throws IOException {
    try {
      Concurrent.exhaust(task);

      return task.getResult();
    } catch (IllegalStateException | ExecutionException e) {
      throw new IOException(e);
    }
  }

  public final Set<ObjectId> copyObjects(GitRepo target, Set<ObjectId> objects) throws IOException {
    Check.notNull(target, "target == null");
    Check.notNull(objects, "objects == null");

    GitTask<UnmodifiableSet<ObjectId>> task;
    task = engine.copyObjects(repository, objects, target.repository);

    return get(task);
  }

  public final MaybeObjectId resolve(RefName ref) throws IOException {
    Check.notNull(ref, "ref == null");

    GitTask<MaybeObjectId> task;
    task = engine.resolve(repository, ref);

    return get(task);
  }

  public final Blob readBlob(ObjectId id) throws IOException {
    Check.notNull(id, "id == null");

    GitTask<Blob> task;
    task = engine.readBlob(repository, id);

    return get(task);
  }

  public final Commit readCommit(ObjectId id) throws IOException {
    Check.notNull(id, "id == null");

    GitTask<Commit> task;
    task = engine.readCommit(repository, id);

    return get(task);
  }

  public final Tree readTree(ObjectId id) throws IOException {
    Check.notNull(id, "id == null");

    GitTask<Tree> task;
    task = engine.readTree(repository, id);

    return get(task);
  }

  final boolean isBare() {
    return repository.isBare();
  }

  final PackFile getPackFile(int index) {
    return repository.getPackFile(index);
  }

  final int getPackFileCount() {
    return repository.getPackFileCount();
  }

  final ResolvedPath resolveLooseObject(ObjectId objectId) throws IOException {
    return repository.resolveLooseObject(objectId);
  }

}