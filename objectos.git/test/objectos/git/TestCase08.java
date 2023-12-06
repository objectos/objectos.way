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
 * Verify that the copy command does not create a commit if the resulting tree
 * is empty.
 *
 * @since 1
 */
final class TestCase08 extends StageGitCommand<Directory> {

  static final String DESCRIPTION
      = "Copy use-case: do not create commit if resulting tree is empty";

  private final Directory target;

  private final GitServiceTest test;

  TestCase08(GitServiceTest test, Directory target) {
    this.test = test;

    this.target = target;
  }

  public static void acceptGitServiceTest(GitServiceTest test) throws IOException {
    Directory target;
    target = TmpDir.create();

    Directory repo01;
    repo01 = TestingGit.repo01();

    repo01.copyTo(target);

    TestCase08 impl;
    impl = new TestCase08(test, target);

    test.testCase08 = test.submit(impl);
  }

  @Override
  protected final void executeStage(int step) throws Exception {
    switch (step) {
      case 0:
        submit("68699720c357a5ce1d4171a65ce801741736ea31");

      case 1:
        if (isComputing()) {
          return;
        }

        get();

        test.testCase08A = copyTo();

        submit("9d954d103bebfbd782835e8f632a033b1103152d");

      case 2:
        if (isComputing()) {
          return;
        }

        get();

        test.testCase08B = copyTo();

        submit("a265fca5b5fc9d01302b05e5867b0cf62d6ee0ee");

      case 3:
        if (isComputing()) {
          return;
        }

        get();

        test.testCase08C = copyTo();

        setResult(target);

        return;

      default:
        throw new AssertionError("Unexpected step=" + step);
    }
  }

  private Directory copyTo() throws IOException {
    Directory r;
    r = TmpDir.create();

    target.copyTo(r);

    return r;
  }

  private void submit(String id) throws InvalidObjectIdFormatException {
    ObjectId oid;
    oid = ObjectId.parse(id);

    CopyImpl impl;
    impl = new CopyImpl(oid, target);

    submit(impl);
  }

  private static class CopyImpl extends StageGitCommand<ObjectId> {

    private final GrowableSet<ObjectId> blobsToCopy = new GrowableSet<>();

    private Commit parentCommit;

    private ObjectId parentId;

    private final Map<ObjectId, MutableTree> parentMap = new HashMap<>();

    private MutableTree rootTree;

    private Commit sourceCommit;

    private final ObjectId sourceId;

    private Repository sourceRepository;

    private final Directory target;

    private Repository targetRepository;

    private ObjectId written;

    CopyImpl(ObjectId sourceId, Directory target) {
      this.sourceId = sourceId;

      this.target = target;
    }

    @Override
    protected final void executeStage(int step) throws Exception {
      switch (step) {
        case 0:
          openRepository(
            TestingGit.repo00()
          );

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

          readCommit(sourceRepository, sourceId);

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

          MaybeObjectId maybeParentId;
          maybeParentId = this.<MaybeObjectId> get();

          if (!maybeParentId.isObjectId()) {
            submitReadSourceTree();

            goTo(6);

            return;
          }

          parentId = maybeParentId.getObjectId();

          readCommit(targetRepository, parentId);

        case 5:
          if (isComputing()) {
            return;
          }

          parentCommit = this.<Commit> get();

          submitReadSourceTree();

        case 6:
          if (isComputing()) {
            return;
          }

          rootTree = new MutableTree();

          final Tree sourceRoot;
          sourceRoot = get();

          visitTree(sourceRoot);

          if (!hasComputations()) {
            submitCopyBlobs();

            goTo(8);

            return;
          }

        case 7:
          if (isComputing()) {
            return;
          }

          final List<Tree> trees;
          trees = getAll();

          for (Tree tree : trees) {
            visitTree(tree);
          }

          if (hasComputations()) {
            goTo(7);

            return;
          }

          submitCopyBlobs();

        case 8:
          if (isComputing()) {
            return;
          }

          writeTree(targetRepository, rootTree);

        case 9:
          if (isComputing()) {
            return;
          }

          final ObjectId rootTreeId;
          rootTreeId = get();

          MaybeObjectId parentTree;
          parentTree = MaybeObjectId.empty();

          if (parentCommit != null) {
            parentTree = parentCommit.getTree();
          }

          if (parentTree.equals(rootTreeId)) {
            ObjectId r;
            r = sourceCommit.getObjectId();

            setResult(r);

            return;
          }

          MutableCommit mutable;
          mutable = new MutableCommit();

          if (parentId != null) {
            mutable.addParent(parentId);
          }

          mutable.setAuthor(sourceCommit.getAuthor());

          mutable.setCommitter(sourceCommit.getCommitter());

          mutable.setMessage(
            String.join(
              System.lineSeparator(),

              sourceCommit.getMessage(),
              "source=" + sourceCommit.getObjectId().getHexString()
            )
          );

          mutable.setTree(rootTreeId);

          writeCommit(targetRepository, mutable);

        case 10:
          if (isComputing()) {
            return;
          }

          written = this.<ObjectId> get();

          updateRef(targetRepository, RefName.MASTER, written);

        case 11:
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

    private void submitReadSourceTree() {
      final ObjectId tid;
      tid = sourceCommit.getTree();

      readTree(sourceRepository, tid);
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

        if (currentTree.hasName("") && name.equals("bin")) {
          continue;
        }

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

}
