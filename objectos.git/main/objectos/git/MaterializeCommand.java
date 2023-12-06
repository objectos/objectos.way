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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import objectos.fs.Directory;
import objectos.lang.object.Check;
import objectos.lang.object.ToString;
import objectos.util.list.UnmodifiableList;
import objectos.util.map.GrowableMap;

/**
 * A command for <em>materializing</em> a Git repository.
 *
 * <p>
 * To <em>materialize</em> a specific {@code commit} or a specific {@code ref}
 * from a {@code source} repository into a {@code target} directory means to
 * recursively create, at the {@code target} directory, the tree associated with
 * the {@code commit}.
 *
 * <p>
 * In other words, this command behaves like a {@code git clone} except it does
 * not copy the whole history of the repository; in fact it will not create the
 * {@code .git} directory at the {@code target} directory at all.
 *
 * @since 3
 */
public final class MaterializeCommand extends StageGitCommand<Directory> {

  private ObjectId objectId;

  private RefName ref;

  private Repository repository;

  private final Directory source;

  private final Directory target;

  private final Map<ObjectId, Directory> treeMap = new GrowableMap<>();

  /**
   * <em>Materializes</em> from the {@code source} repository the specified
   * {@code commit} to the {@code target} directory.
   *
   * @param source
   *        the source directory containing the Git repository
   * @param commit
   *        the hash value of the containing pointing to the tree to be
   *        materialized
   * @param target
   *        the target directory
   */
  public MaterializeCommand(Directory source,
                            ObjectId commit,
                            Directory target) {
    this.source = Check.notNull(source, "source == null");

    this.objectId = Check.notNull(commit, "commit == null");

    this.target = Check.notNull(target, "target == null");
  }

  /**
   * <em>Materializes</em> from the {@code source} repository the specified
   * {@code ref} to the {@code target} directory.
   *
   * @param source
   *        the source directory containing the Git repository
   * @param ref
   *        the Git reference to be materialized
   * @param target
   *        the target directory
   */
  public MaterializeCommand(Directory source,
                            RefName ref,
                            Directory target) {
    this.source = Check.notNull(source, "source == null");

    this.ref = Check.notNull(ref, "ref == null");

    this.target = Check.notNull(target, "target == null");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void formatToString(StringBuilder toString, int level) {
    ToString.format(
        toString, level, this,
        "repository", source,
        "commit", objectId != null ? objectId : ref,
        "target", target
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final void executeStage(int stage) throws Exception {
    switch (stage) {
      case 0:
        openRepository(source);

      case 1:
        if (isComputing()) {
          return;
        }

        repository = get();

        if (objectId != null) {
          readCommit(repository, objectId);

          goTo(3);

          return;
        }

        resolve(repository, ref);

      case 2:
        if (isComputing()) {
          return;
        }

        final MaybeObjectId resolved;
        resolved = get();

        if (!resolved.isObjectId()) {
          throw new IOException("Failed to resolve ref: " + ref);
        }

        objectId = resolved.getObjectId();

        readCommit(repository, objectId);

      case 3:
        if (isComputing()) {
          return;
        }

        final Commit commit;
        commit = get();

        final ObjectId id;
        id = commit.getTree();

        readTree(repository, id);

      case 4:
        if (isComputing()) {
          return;
        }

        final Tree t;
        t = get();

        materializeTree(t, target);

      case 5:
        if (isComputing()) {
          return;
        }

        final List<MaterializedEntry> entries;
        entries = getAll();

        for (MaterializedEntry entry : entries) {
          if (entry.isTree()) {
            MaterializedTree mt;
            mt = (MaterializedTree) entry;

            treeMap.put(mt.objectId, mt.directory);

            readTree(repository, mt.objectId);
          }
        }

      case 6:
        if (isComputing()) {
          return;
        }

        final List<Tree> trees;
        trees = getAll();

        if (trees.isEmpty()) {
          setResult(target);

          return;
        }

        for (Tree tree : trees) {
          ObjectId treeId;
          treeId = tree.getObjectId();

          Directory directory;
          directory = treeMap.get(treeId);

          materializeTree(tree, directory);
        }

        treeMap.clear();

        goTo(5);

        return;

      default:
        throw new AssertionError("Unexpected step=" + stage);
    }
  }

  private void materializeTree(Tree tree, Directory directory) {
    UnmodifiableList<Entry> entries;
    entries = tree.getEntries();

    for (int i = 0, size = entries.size(); i < size; i++) {
      Entry entry;
      entry = entries.get(i);

      materializeEntry(repository, entry, directory);
    }
  }

}