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
import objectos.fs.Directory;
import objectos.fs.testing.TmpDir;
import objectos.util.list.UnmodifiableList;

/**
 * Support reading and parsing loose objects (iter. 5).
 *
 * 0) Copy use-case;
 * 1) source=bare, loose objects only;
 * 2) target=bare, loose objects only;
 * 3) include sub tree with blobs only
 *
 * @since 1
 */
final class TestCase04 extends StageGitCommand<ObjectId> {

  public static final String DESCRIPTION = "Support loose objects (iter 5)";

  private ObjectId binTreeId;

  private MaybeObjectId parentId;

  private Commit sourceCommit;

  private Repository sourceRepository;

  private final Directory target;

  private Repository targetRepository;

  private ObjectId written;

  TestCase04(Directory target) {
    this.target = target;
  }

  public static void acceptGitServiceTest(GitServiceTest test) throws IOException {
    Directory source;
    source = getDirectory();

    Directory target;
    target = test.testCase04Target = TmpDir.create();

    source.copyTo(target);

    TestCase04 impl;
    impl = new TestCase04(target);

    test.testCase04 = test.submit(impl);
  }

  public static ObjectId getCommit() throws InvalidObjectIdFormatException {
    return ObjectId.parse("68699720c357a5ce1d4171a65ce801741736ea31");
  }

  public static Directory getDirectory() throws IOException {
    return TestingGit.repo00();
  }

  public static RefName getRefName() {
    return RefName.MASTER;
  }

  public static ObjectId getTree() throws InvalidObjectIdFormatException {
    return ObjectId.parse("1cd042294d3933032f5fbb9735034dcbce689dc9");
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

        final Tree sourceRoot;
        sourceRoot = get();

        binTreeId = findBin(sourceRoot);

        copyTree(sourceRepository, binTreeId, targetRepository);

      case 6:
        if (isComputing()) {
          return;
        }

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

        mutable.setTree(binTreeId);

        writeCommit(targetRepository, mutable);

      case 7:
        if (isComputing()) {
          return;
        }

        written = this.<ObjectId> get();

        updateRef(targetRepository, RefName.MASTER, written);

      case 8:
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

  private ObjectId findBin(Tree root) {
    ObjectId result;
    result = null;

    UnmodifiableList<Entry> entries;
    entries = root.getEntries();

    for (int i = 0, size = entries.size(); i < size; i++) {
      Entry entry;
      entry = entries.get(i);

      if (!entry.isTree()) {
        continue;
      }

      String name;
      name = entry.getName();

      if (!name.equals("bin")) {
        continue;
      }

      result = entry.getObjectId();

      break;
    }

    if (result == null) {
      throw new AssertionError("bin tree was not found");
    }

    return result;
  }

}