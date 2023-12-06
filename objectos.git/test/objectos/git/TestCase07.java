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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import objectos.fs.Directory;
import objectos.fs.testing.TmpDir;
import objectos.util.list.UnmodifiableList;
import objectos.util.set.GrowableSet;
import objectos.util.set.UnmodifiableSet;

/**
 * Verify that the copy command can create the first commit of the target
 * repository. That is, the repository exists but has no commits. In particular,
 * ensure that the following is verified:
 *
 * - properly handle the fact that refs/heads/master will not exist
 * - parent of the final commit will not exist
 *
 * @since 1
 */
final class TestCase07 extends StageGitCommand<ObjectId> {

  static final String DESCRIPTION = "Copy operation: create first commit of target repo";

  private final GrowableSet<ObjectId> blobsToCopy = new GrowableSet<>();

  private MaybeObjectId parentId;

  private final Map<ObjectId, MutableTree> parentMap = new HashMap<>();

  private MutableTree rootTree;

  private Commit sourceCommit;

  private Repository sourceRepository;

  private final Directory target;

  private Repository targetRepository;

  private ObjectId written;

  TestCase07(Directory target) {
    this.target = target;
  }

  public static void acceptGitServiceTest(GitServiceTest test) throws IOException {
    Directory target;
    target = test.testCase07Target = TmpDir.create();

    target.createDirectory("objects");

    TestCase07 impl;
    impl = new TestCase07(target);

    test.testCase07 = test.submit(impl);
  }

  public static ObjectId getCommit() throws InvalidObjectIdFormatException {
    return ObjectId.parse("68699720c357a5ce1d4171a65ce801741736ea31");
  }

  public static Directory getDirectory() throws IOException {
    return TestingGit.repo00();
  }

  @Override
  protected final void executeStage(int step) throws Exception {
    switch (step) {
      case 0:
        openRepository(getDirectory());

      case 1:
        if (isComputing()) {
          return;
        }

        sourceRepository = this.<Repository> get();

        openRepository(target);

      case 2:
        if (isComputing()) {
          return;
        }

        targetRepository = this.<Repository> get();

        readCommit(sourceRepository, getCommit());

      case 3:
        if (isComputing()) {
          return;
        }

        sourceCommit = this.<Commit> get();

        resolve(targetRepository, RefName.MASTER);

      case 4:
        if (isComputing()) {
          return;
        }

        parentId = this.<MaybeObjectId> get();

        final ObjectId tid;
        tid = sourceCommit.getTree();

        readTree(sourceRepository, tid);

      case 5:
        if (isComputing()) {
          return;
        }

        rootTree = new MutableTree();

        final Tree sourceRoot;
        sourceRoot = get();

        visitTree(sourceRoot);

        if (!hasComputations()) {
          submitCopyBlobs();

          goTo(7);

          return;
        }

      case 6:
        if (isComputing()) {
          return;
        }

        final List<Tree> trees;
        trees = getAll();

        for (Tree tree : trees) {
          visitTree(tree);
        }

        if (hasComputations()) {
          goTo(6);

          return;
        }

        submitCopyBlobs();

      case 7:
        if (isComputing()) {
          return;
        }

        writeTree(targetRepository, rootTree);

      case 8:
        if (isComputing()) {
          return;
        }

        final ObjectId rootTreeId;
        rootTreeId = get();

        MutableCommit mutable;
        mutable = new MutableCommit();

        if (parentId.isObjectId()) {
          mutable.addParent(
            parentId.getObjectId()
          );
        }

        mutable.setAuthor(sourceCommit.getAuthor());

        mutable.setCommitter(sourceCommit.getCommitter());

        mutable.setMessage(
          String.join(
            System.lineSeparator(),

            sourceCommit.getMessage(),
            "source " + sourceCommit.getObjectId().getHexString()
          )
        );

        mutable.setTree(rootTreeId);

        writeCommit(targetRepository, mutable);

      case 9:
        if (isComputing()) {
          return;
        }

        written = this.<ObjectId> get();

        updateRef(targetRepository, RefName.MASTER, written);

      case 10:
        if (isComputing()) {
          return;
        }

        get();

        setResult(written);

        return;

      default:
        throw new AssertionError("Unexpected step=" + step);
    }
  }

  private void submitCopyBlobs() {
    UnmodifiableSet<ObjectId> ids;
    ids = blobsToCopy.toUnmodifiableSet();

    blobsToCopy.clear();

    copyObjects(sourceRepository, ids, targetRepository);
  }

  private void visitTree(Tree tree) {
    ObjectId treeId = tree.getObjectId();

    MutableTree currentTree;
    currentTree = parentMap.remove(treeId);

    if (currentTree == null) {
      currentTree = rootTree;
    }

    UnmodifiableList<Entry> entries;
    entries = tree.getEntries();

    for (Entry entry : entries) {
      ObjectId id;
      id = entry.getObjectId();

      if (entry.isBlob()) {
        blobsToCopy.add(id);

        currentTree.addEntry(entry);

        continue;
      }

      // entry is tree

      String name;
      name = entry.getName();

      if (currentTree.hasName("src") && name.equals("test")) {
        continue;
      }

      readTree(sourceRepository, id);

      MutableTree mutableTree;
      mutableTree = new MutableTree(name);

      currentTree.addEntry(mutableTree);

      parentMap.put(id, mutableTree);
    }
  }

}