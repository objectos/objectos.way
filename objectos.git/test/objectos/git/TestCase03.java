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
import java.nio.file.Path;
import objectos.fs.Directory;
import objectos.fs.testing.TmpDir;

/**
 * Support reading and parsing loose objects (iter. 4).
 *
 * 0) Copy use-case
 * 1) source=bare, loose objects only
 * 2) target=bare, loose objects only
 * 3) include all, remove none, transform none
 *
 * @since 1
 */
final class TestCase03 extends StageGitCommand<ObjectId> {

  public static final String DESCRIPTION = "Support loose objects (iter 4)";

  private MaybeObjectId parentId;

  private ObjectId result;

  private Commit sourceCommit;

  private Repository sourceRepository;

  private final Directory target;

  private Repository targetRepository;

  private TestCase03(Directory target) {
    this.target = target;
  }

  public static void acceptGitServiceTest(GitServiceTest test) throws IOException {
    Directory source;
    source = getDirectory();

    Directory target;
    target = test.testCase03Target = TmpDir.create();

    source.copyTo(target);

    TestCase03 testCase03;
    testCase03 = new TestCase03(target);

    test.testCase03 = test.submit(testCase03);
  }

  public static ObjectId getCommit() throws InvalidObjectIdFormatException {
    return ObjectId.parse("b9c4f2db7b4fd742990b518ee3c8ae59eb1d6e93");
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

  public static void repositoryTo(Path root) throws IOException {
    Path repo;
    repo = TestingGit2.repo00();

    TestingGit2.copyRecursively(repo, root);
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

        sourceRepository = get();

        openRepository(target);

      case 2:
        if (isComputing()) {
          return;
        }

        targetRepository = get();

        readCommit(sourceRepository, getCommit());

      case 3:
        if (isComputing()) {
          return;
        }

        sourceCommit = get();

        resolve(targetRepository, RefName.MASTER);

      case 4:
        if (isComputing()) {
          return;
        }

        parentId = this.<MaybeObjectId> get();

        final ObjectId tid;
        tid = sourceCommit.getTree();

        copyTree(sourceRepository, tid, targetRepository);

      case 5:
        if (isComputing()) {
          return;
        }

        get();

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

        mutable.setTree(sourceCommit.getTree());

        writeCommit(targetRepository, mutable);

      case 6:
        if (isComputing()) {
          return;
        }

        result = get();

        updateRef(targetRepository, RefName.MASTER, result);

      case 7:
        if (isComputing()) {
          return;
        }

        get();

        setResult(result);

        return;

      default:
        throw new AssertionError("Unexpected step=" + step);
    }
  }

}