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
import objectos.concurrent.StageComputationTask;
import objectos.fs.Directory;
import objectos.lang.object.Check;
import objectos.lang.object.ToString;
import objectos.util.set.UnmodifiableSet;

/**
 * <p>
 * Base class for creating high-level stage-based (porcelain) Git commands. This
 * class is useful for chaining many low-level (plumbing) asynchronous
 * tasks and other high-level asynchronous commands. It does so offering a
 * (mostly) imperative programming style.
 *
 * <p>
 * This class is an instance of {@link GitCommand} and is intended to be
 * executed by a {@link GitService} instance.
 *
 * <h2>Warning</h2>
 *
 * <p>
 * This class extends {@link StageComputationTask} and, therefore, inherits all
 * of its drawbacks. Please understand all of the drawbacks by reading the
 * superclass' javadocs before using this class.
 *
 * <h2>Usage example</h2>
 *
 * The following copies a tree object from one repository to another repository.
 * This the actual implementation for the internal {@code CopyTreeCommand}:
 *
 * <pre>{@code
final class CopyTreeCommand extends StageGitCommand<ObjectId> {
  private final GrowableSet<ObjectId> objectsToCopy = new GrowableSet<>();

  private final Repository source;

  private final ObjectId sourceId;

  private final Repository target;

  CopyTreeCommand(Repository source, ObjectId tree, Repository destination) {
    this.source = Checks.checkNotNull(source, "source == null");

    this.sourceId = Checks.checkNotNull(tree, "tree == null");

    this.target = Checks.checkNotNull(destination, "destination == null");
  }

  protected final void executeStage(int stage) throws Exception {
    switch (stage) {
      case 0:
        readTree(source, sourceId);

      case 1:
        if (isComputing()) {
          return;
        }

        final UnmodifiableList<Tree> trees;
        trees = getAll();

        for (Tree tree : trees) {
          visitTree(tree);
        }

        if (hasComputations()) {
          goTo(1);

          return;
        }

        final UnmodifiableSet<ObjectId> set;
        set = objectsToCopy.toUnmodifiableSet();

        objectsToCopy.clear();

        copyObjects(source, set, target);

      case 2:
        if (isComputing()) {
          return;
        }

        setResult(sourceId);

        return;

      default:
        throw new AssertionError("Unexpected step=" + stage);
    }
  }

  private void visitTree(Tree tree) {
    ObjectId treeId;
    treeId = tree.getObjectId();

    objectsToCopy.add(treeId);

    UnmodifiableList<Entry> entries;
    entries = tree.getEntries();

    for (Entry entry : entries) {
      ObjectId entryId;
      entryId = entry.getObjectId();

      if (entry.isBlob()) {
        objectsToCopy.add(entryId);
      } else {
        readTree(source, entryId);
      }
    }
  }
}
 * }</pre>
 *
 * @param <V>
 *        the type of this command's result
 *
 * @since 3
 */
public abstract class StageGitCommand<V>
    extends StageComputationTask<V>
    implements Computation<V>, GitCommand<V>, ToString.Formattable {

  private GitCommandExecutor git;

  /**
   * Sole constructor.
   */
  protected StageGitCommand() {}

  /**
   * {@inheritDoc}
   */
  @Override
  public void formatToString(StringBuilder toString, int level) {
    ToString.format(
        toString, level, this
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Computation<V> submitAsync(GitCommandExecutor executor, CpuWorker cpuWorker) {
    Check.state(git == null, "The same command must not be submitted more than once");
    Check.notNull(executor, "executor == null");
    Check.notNull(cpuWorker, "cpuWorker == null");

    synchronized (this) {
      Check.state(git == null, "The same command must not be submitted more than once");

      git = executor;
    }

    reset();

    if (cpuWorker.offer(this)) {
      return this;
    } else {
      git = null;

      return new CpuTaskRejected<V>(this);
    }
  }

  @Override
  public final String toString() {
    return ToString.of(this);
  }

  /**
   * Adds a single asynchronous computation by invoking
   * {@link GitCommandExecutor#copyObjects(Repository, UnmodifiableSet, Repository)}.
   *
   * @param source
   *        the source repository containing the objects. Git objects will be
   *        read from this repository.
   * @param objects
   *        the ids (hash values) of the objects to be copied
   * @param destination
   *        the destination repository. Git objects will (possibly) be written
   *        to this repository
   *
   * @see #addComputation(br.com.objectos.concurrent.Computation)
   * @see #openRepository(Directory)
   */
  protected final void copyObjects(Repository source, UnmodifiableSet<ObjectId> objects, Repository destination) {
    GitCommandExecutor git;
    git = checkGit();

    addComputation(
        git.copyObjects(source, objects, destination)
    );
  }

  /**
   * Adds a single asynchronous computation that copies from the source
   * repository the specified tree object to the destination repository (as
   * loose objects).
   *
   * <p>
   * The computation, once completed, returns the hash value of the tree object
   * created in the destination repository.
   *
   * <p>
   * The computation will fail if the source repository does not contain the
   * specified tree object or if an I/O error occurs.
   *
   * @param source
   *        the repository from where to read the tree object
   * @param tree
   *        the hash value of the tree object to copy
   * @param destination
   *        the repository where the tree will be copied to
   *
   * @see #addComputation(br.com.objectos.concurrent.Computation)
   * @see #openRepository(Directory)
   */
  protected final void copyTree(Repository source, ObjectId tree, Repository destination) {
    CopyTreeCommand command;
    command = new CopyTreeCommand(source, tree, destination);

    GitCommandExecutor git;
    git = checkGit();

    addComputation(
        git.submit(command)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void executeFinally() {
    git = null;
  }

  /**
   * Adds a single asynchronous computation by invoking
   * {@link GitCommandExecutor#materializeEntry(Repository, Entry, Directory)}.
   *
   * @param repository
   *        repository to read blob contents from (if necessary)
   * @param entry
   *        the entry to be materialized
   * @param target
   *        the target directory
   *
   * @see #addComputation(br.com.objectos.concurrent.Computation)
   * @see #openRepository(Directory)
   * @see #readTree(Repository, ObjectId)
   */
  protected final void materializeEntry(Repository repository, Entry entry, Directory target) {
    GitCommandExecutor git;
    git = checkGit();

    addComputation(
        git.materializeEntry(repository, entry, target)
    );
  }

  /**
   * Adds a single asynchronous computation by invoking
   * {@link GitCommandExecutor#openRepository(Directory)}.
   *
   * @param directory
   *        the directory where a Git repository is located
   *
   * @see #addComputation(br.com.objectos.concurrent.Computation)
   */
  protected final void openRepository(Directory directory) {
    GitCommandExecutor git;
    git = checkGit();

    addComputation(
        git.openRepository(directory)
    );
  }

  /**
   * Adds a single asynchronous computation by invoking
   * {@link GitCommandExecutor#readBlob(Repository, ObjectId)}.
   *
   * @param repository
   *        the git repository from which to read the commit
   * @param id
   *        the unique hash value of the blob to read
   *
   * @see #addComputation(br.com.objectos.concurrent.Computation)
   * @see #openRepository(Directory)
   */
  protected final void readBlob(Repository repository, ObjectId id) {
    GitCommandExecutor git;
    git = checkGit();

    addComputation(
        git.readBlob(repository, id)
    );
  }

  /**
   * Adds a single asynchronous computation by invoking
   * {@link GitCommandExecutor#readCommit(Repository, ObjectId)}.
   *
   * @param repository
   *        the git repository from which to read the commit
   * @param id
   *        the unique hash value of the commit to read
   *
   * @see #addComputation(br.com.objectos.concurrent.Computation)
   * @see #openRepository(Directory)
   */
  protected final void readCommit(Repository repository, ObjectId id) {
    GitCommandExecutor git;
    git = checkGit();

    addComputation(
        git.readCommit(repository, id)
    );
  }

  /**
   * Adds a single asynchronous computation by invoking
   * {@link GitCommandExecutor#readTree(Repository, ObjectId)}.
   *
   * @param repository
   *        the git repository from which to read the tree
   * @param id
   *        the unique hash value of the tree to read
   *
   * @see #addComputation(br.com.objectos.concurrent.Computation)
   * @see #openRepository(Directory)
   */
  protected final void readTree(Repository repository, ObjectId id) {
    GitCommandExecutor git;
    git = checkGit();

    addComputation(
        git.readTree(repository, id)
    );
  }

  /**
   * Adds a single asynchronous computation by invoking
   * {@link GitCommandExecutor#resolve(Repository, RefName)}.
   *
   * @param repository
   *        the repository for which the reference will be resolved
   * @param ref
   *        the Git reference to be resolved
   *
   * @see #addComputation(br.com.objectos.concurrent.Computation)
   * @see #openRepository(Directory)
   */
  protected final void resolve(Repository repository, RefName ref) {
    GitCommandExecutor git;
    git = checkGit();

    addComputation(
        git.resolve(repository, ref)
    );
  }

  /**
   * Adds a single asynchronous computation by invoking
   * {@link GitCommandExecutor#submit(GitCommand)}.
   *
   * @param command
   *        the command to be submitted
   *
   * @see #addComputation(br.com.objectos.concurrent.Computation)
   */
  protected final void submit(GitCommand<?> command) {
    GitCommandExecutor git;
    git = checkGit();

    addComputation(
        git.submit(command)
    );
  }

  /**
   * Adds a single asynchronous computation by invoking
   * {@link GitCommandExecutor#updateRef(Repository, RefName, ObjectId)}.
   *
   * @param repository
   *        the repository for which the reference will be updated
   * @param ref
   *        the Git reference to be updated
   * @param newValue
   *        the new value for the reference
   *
   * @see #addComputation(br.com.objectos.concurrent.Computation)
   * @see #openRepository(Directory)
   */
  protected final void updateRef(Repository repository, RefName ref, ObjectId newValue) {
    GitCommandExecutor git;
    git = checkGit();

    addComputation(
        git.updateRef(repository, ref, newValue)
    );
  }

  /**
   * Adds a single asynchronous computation by invoking
   * {@link GitCommandExecutor#writeCommit(Repository, MutableCommit)}.
   *
   * @param repository
   *        the git repository where the commit is to be written
   * @param commit
   *        a {@code MutableCommit} instance containing all the information to
   *        be recorded as a commit object
   *
   * @see #addComputation(br.com.objectos.concurrent.Computation)
   * @see #openRepository(Directory)
   */
  protected final void writeCommit(Repository repository, MutableCommit commit) {
    GitCommandExecutor git;
    git = checkGit();

    addComputation(
        git.writeCommit(repository, commit)
    );
  }

  /**
   * Adds a single asynchronous computation by invoking
   * {@link GitCommandExecutor#writeTree(GitCommand, Repository, MutableTree)}.
   *
   * @param repository
   *        the git repository where the tree is to be written
   * @param tree
   *        a {@code MutableTree} to be written
   *
   * @see #addComputation(br.com.objectos.concurrent.Computation)
   * @see #openRepository(Directory)
   */
  protected final void writeTree(Repository repository, MutableTree tree) {
    GitCommandExecutor git;
    git = checkGit();

    addComputation(
        git.writeTree(this, repository, tree)
    );
  }

  private GitCommandExecutor checkGit() {
    Check.state(git != null, "command not submitted or already consumed");

    return git;
  }

}