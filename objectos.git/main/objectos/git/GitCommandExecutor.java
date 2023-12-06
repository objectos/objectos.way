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
import objectos.fs.Directory;
import objectos.util.set.UnmodifiableSet;

/**
 * Interface for supporting the execution of {@link GitCommand} instances. It
 * gives {@code GitCommand}s access to low-level (plumbing) git tasks while
 * abstracting away the mechanics of concurrency and multithreading.
 *
 * <p>
 * Users should use {@link StageGitCommand} instead of using this class
 * directly.
 *
 * @since 3
 */
public interface GitCommandExecutor extends GitExecutor {

  /**
   * Returns a new computation for copying a set of objects from one repository
   * to another.
   *
   * <p>
   * This is the asynchronous execution of the
   * {@link GitEngine#copyObjects(Repository, UnmodifiableSet, Repository)}
   * task.
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
   * @return a new computation for copying a set of objects from one repository
   *         to another
   */
  Computation<UnmodifiableSet<ObjectId>> copyObjects(Repository source, UnmodifiableSet<ObjectId> objects, Repository destination);

  /**
   * Returns a new computation for materializing a Git tree entry at the
   * specified target directory.
   *
   * <p>
   * This is the asynchronous execution of the
   * {@link GitEngine#materializeEntry(Repository, Entry, Directory)} task.
   *
   * @param repository
   *        repository to read blob contents from (if necessary)
   * @param entry
   *        the entry to be materialized
   * @param target
   *        the target directory
   *
   * @return a new computation for materializing a Git tree entry at the
   *         specified target directory
   */
  Computation<MaterializedEntry> materializeEntry(Repository repository, Entry entry, Directory target);

  /**
   * Returns a new computation for opening a Git repository located at the
   * specified directory.
   *
   * <p>
   * This is the asynchronous execution of the
   * {@link GitEngine#openRepository(Directory)} task.
   *
   * @param directory
   *        the directory where a Git repository is located
   *
   * @return a new computation for opening a Git repository located at the
   *         specified directory
   */
  Computation<Repository> openRepository(Directory directory);

  /**
   * Returns a new computation for reading a blob object from a repository.
   *
   * <p>
   * This is the asynchronous execution of the
   * {@link GitEngine#readBlob(Repository, ObjectId)} task.
   *
   * @param repository
   *        the git repository from which to read the commit
   * @param id
   *        the unique hash value of the blob to read
   *
   * @return a new computation for reading a blob object from a repository
   */
  Computation<Blob> readBlob(Repository repository, ObjectId id);

  /**
   * Returns a new computation for reading a commit object from a repository.
   *
   * <p>
   * This is the asynchronous execution of the
   * {@link GitEngine#readCommit(Repository, ObjectId)} task.
   *
   * @param repository
   *        the git repository from which to read the commit
   * @param id
   *        the unique hash value of the commit to read
   *
   * @return a new computation for reading a commit object from a repository
   */
  Computation<Commit> readCommit(Repository repository, ObjectId id);

  /**
   * Returns a new computation for reading a tree object from a repository.
   *
   * <p>
   * This is the asynchronous execution of the
   * {@link GitEngine#readTree(Repository, ObjectId)} task.
   *
   * @param repository
   *        the git repository from which to read the tree
   * @param id
   *        the unique hash value of the tree to read
   *
   * @return a new computation for reading a tree object from a repository
   */
  Computation<Tree> readTree(Repository repository, ObjectId id);

  /**
   * Returns a new computation for resolving a Git reference.
   *
   * <p>
   * This is the asynchronous execution of the
   * {@link GitEngine#resolve(Repository, RefName)} task.
   *
   * @param repository
   *        the repository for which the reference will be resolved
   * @param ref
   *        the Git reference to be resolved
   *
   * @return a new computation for resolving a Git reference
   */
  Computation<MaybeObjectId> resolve(Repository repository, RefName ref);

  /**
   * Returns a new computation for updating a Git reference.
   *
   * <p>
   * This is the asynchronous execution of the
   * {@link GitEngine#updateRef(Repository, RefName, ObjectId)} task.
   *
   * @param repository
   *        the repository for which the reference will be updated
   * @param ref
   *        the Git reference to be updated
   * @param newValue
   *        the new value for the reference
   *
   * @return a new computation for updating a Git reference
   */
  Computation<MaybeObjectId> updateRef(Repository repository, RefName ref, ObjectId newValue);

  /**
   * Returns a new computation for writing a commit object (as a loose object)
   * to a repository.
   *
   * <p>
   * This is the asynchronous execution of the
   * {@link GitEngine#writeCommit(Repository, MutableCommit)} task.
   *
   * @param repository
   *        the git repository where the commit is to be written
   * @param commit
   *        a {@code MutableCommit} instance containing all the information to
   *        be recorded as a commit object
   *
   * @return a new computation for writing a commit object (as a loose object)
   *         to a repository
   */
  Computation<ObjectId> writeCommit(Repository repository, MutableCommit commit);

  /**
   * Returns a new computation for recursively writing tree objects to a
   * repository.
   *
   * <p>
   * This is the asynchronous execution of the
   * {@link GitEngine#writeTree(Repository, MutableTree)} task.
   *
   * @param command
   *        the requestor of this computation
   * @param repository
   *        the git repository where the tree is to be written
   * @param tree
   *        a {@code MutableTree} to be written
   *
   * @return a new computation for recursively writing tree objects to a
   *         repository
   */
  Computation<ObjectId> writeTree(GitCommand<?> command, Repository repository, MutableTree tree);

}