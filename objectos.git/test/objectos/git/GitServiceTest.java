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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import objectos.concurrent.Computation;
import objectos.concurrent.Concurrent;
import objectos.core.io.Charsets;
import objectos.core.io.Read;
import objectos.fs.Directory;
import objectos.fs.Posix;
import objectos.fs.RegularFile;
import objectos.fs.testing.TmpDir;
import objectos.util.list.GrowableList;
import objectos.util.list.UnmodifiableList;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class GitServiceTest extends AbstractGitTest {

  Computation<ObjectId> testCase03;

  Directory testCase03Target;

  Computation<ObjectId> testCase04;

  Directory testCase04Target;

  Computation<ObjectId> testCase05;

  Directory testCase05Target;

  Computation<ObjectId> testCase06;

  Directory testCase06Target;

  Computation<ObjectId> testCase07;

  Directory testCase07Target;

  Computation<Directory> testCase08;

  Directory testCase08A;

  Directory testCase08B;

  Directory testCase08C;

  Computation<ObjectId> testCase10;

  Directory testCase10Target;

  Computation<ObjectId> testCase11;

  Directory testCase11Target;

  private Computation<Directory> testCase00;

  private Computation<Directory> testCase01;

  private Computation<Directory> testCase02;

  @BeforeClass
  public void beforeClass() throws Exception {
    testCase00 = TestCase00.acceptGitServiceTest(service);

    testCase01 = TestCase01.acceptGitServiceTest(service);

    testCase02 = TestCase02.acceptGitServiceTest(service);

    TestCase03.acceptGitServiceTest(this);

    TestCase04.acceptGitServiceTest(this);

    TestCase05.acceptGitServiceTest(this);

    TestCase06.acceptGitServiceTest(this);

    TestCase07.acceptGitServiceTest(this);

    TestCase08.acceptGitServiceTest(this);

    TestCase10.acceptGitServiceTest(this);

    TestCase11.acceptGitServiceTest(this);
  }

  @Test(description = TestCase00.DESCRIPTION)
  public void testCase00() throws ExecutionException, IOException {
    await(testCase00);

    Directory result;
    result = testCase00.getResult();

    testTree(
        result,

        "README.md"
    );

    testRegularFile(
        result, "README.md",

        "# ObjectosRepo",
        "",
        "This is a git repository meant to be used in tests."
    );
  }

  @Test(description = TestCase01.DESCRIPTION)
  public void testCase01() throws ExecutionException, IOException {
    await(testCase01);

    Directory result;
    result = testCase01.getResult();

    testTree(
        result,

        "README.md",
        "bin/ci"
    );

    testRegularFile(
        result, "README.md",

        "# ObjectosRepo",
        "",
        "This is a git repository meant to be used in tests."
    );

    testExecutableFile(
        result, "bin/ci",

        "#!/bin/bash",
        "#For testing purposes only."
    );
  }

  @Test(description = TestCase02.DESCRIPTION)
  public void testCase02() throws ExecutionException, IOException {
    await(testCase02);

    Directory result;
    result = testCase02.getResult();

    testTree(
        result,

        "README.md",
        "bin/ci",
        "src/main/java/br/com/objectos/Main.java"
    );

    testRegularFile(
        result, "README.md",

        "# ObjectosRepo",
        "",
        "This is a git repository meant to be used in tests."
    );

    testExecutableFile(
        result, "bin/ci",

        "#!/bin/bash",
        "#For testing purposes only."
    );

    testRegularFile(
        result, "src/main/java/br/com/objectos/Main.java",

        "package br.com.objectos;",
        "",
        "public class Main {",
        "}"
    );
  }

  @Test(description = TestCase03.DESCRIPTION)
  public void testCase03() throws ExecutionException, IOException {
    await(testCase03);

    ObjectId commitId;
    commitId = testCase03.getResult();

    testCommit(
        testCase03Target, commitId,

        "tree 1cd042294d3933032f5fbb9735034dcbce689dc9",
        "parent 717271f0f0ee528c0bb094e8b2f84ea6cef7b39d",
        "author The Author <author@example.com> 1600083193 -0300",
        "committer The Committer <committer@example.com> 1600083193 -0300",
        "",
        "add README.md",
        "",
        "source b9c4f2db7b4fd742990b518ee3c8ae59eb1d6e93"
    );

    Directory result;
    result = materialize(testCase03Target);

    testTree(
        result,

        "README.md"
    );

    testRegularFile(
        result, "README.md",

        "# ObjectosRepo",
        "",
        "This is a git repository meant to be used in tests."
    );
  }

  @Test(description = TestCase04.DESCRIPTION)
  public void testCase04() throws ExecutionException, IOException {
    await(testCase04);

    ObjectId commitId;
    commitId = testCase04.getResult();

    testCommit(
        testCase04Target, commitId,

        "tree 1eda06f88aa783287b37817f287fbcbf318b8770",
        "parent 717271f0f0ee528c0bb094e8b2f84ea6cef7b39d",
        "author The Author <author@example.com> 1616414442 -0300",
        "committer The Committer <committer@example.com> 1616414442 -0300",
        "",
        "[test] add some tests",
        "",
        "source 68699720c357a5ce1d4171a65ce801741736ea31"
    );

    Directory result;
    result = materialize(testCase04Target);

    testTree(
        result,

        "ci",
        "test/include-me"
    );

    testExecutableFile(
        result, "ci",

        "#!/bin/bash",
        "#For testing purposes only."
    );

    testRegularFile(
        result, "test/include-me",

        "should have been included"
    );
  }

  @Test(description = TestCase05.DESCRIPTION)
  public void testCase05() throws ExecutionException, IOException {
    await(testCase05);

    ObjectId commitId;
    commitId = testCase05.getResult();

    testCommit(
        testCase05Target, commitId,

        "tree 81059b029e0e689eb8cae57cb9f3c27f3061ba9d",
        "parent 717271f0f0ee528c0bb094e8b2f84ea6cef7b39d",
        "author The Author <author@example.com> 1616414442 -0300",
        "committer The Committer <committer@example.com> 1616414442 -0300",
        "",
        "[test] add some tests",
        "",
        "source 68699720c357a5ce1d4171a65ce801741736ea31"
    );

    Directory result;
    result = materialize(testCase05Target);

    testTree(
        result,

        "README.md",
        "bin/ci",
        "bin/test/include-me",
        "src/main/java/br/com/objectos/Main.java"
    );

    testRegularFile(
        result, "README.md",

        "# ObjectosRepo",
        "",
        "This is a git repository meant to be used in tests."
    );

    testExecutableFile(
        result, "bin/ci",

        "#!/bin/bash",
        "#For testing purposes only."
    );

    testRegularFile(
        result, "src/main/java/br/com/objectos/Main.java",

        "package br.com.objectos;",
        "",
        "public class Main {",
        "}"
    );
  }

  @Test(description = TestCase06.DESCRIPTION)
  public void testCase06() throws ExecutionException, IOException {
    await(testCase06);

    ObjectId commitId;
    commitId = testCase06.getResult();

    testCommit(
        testCase06Target, commitId,

        "tree 81059b029e0e689eb8cae57cb9f3c27f3061ba9d",
        "parent 717271f0f0ee528c0bb094e8b2f84ea6cef7b39d",
        "author Changed Author <changed.author@example.com> 1616414442 -0300",
        "committer Changed Committer <changed.committer@example.com> 1616414442 -0300",
        "",
        "[test] add some tests",
        "",
        "source 68699720c357a5ce1d4171a65ce801741736ea31"
    );
  }

  @Test(description = TestCase07.DESCRIPTION)
  public void testCase07() throws ExecutionException, IOException {
    await(testCase07);

    ObjectId commitId;
    commitId = testCase07.getResult();

    testCommit(
        testCase07Target, commitId,

        "tree 81059b029e0e689eb8cae57cb9f3c27f3061ba9d",
        "author The Author <author@example.com> 1616414442 -0300",
        "committer The Committer <committer@example.com> 1616414442 -0300",
        "",
        "[test] add some tests",
        "",
        "source 68699720c357a5ce1d4171a65ce801741736ea31"
    );

    Directory result;
    result = materialize(testCase07Target);

    testTree(
        result,

        "README.md",
        "bin/ci",
        "bin/test/include-me",
        "src/main/java/br/com/objectos/Main.java"
    );

    testRegularFile(
        result, "README.md",

        "# ObjectosRepo",
        "",
        "This is a git repository meant to be used in tests."
    );

    testExecutableFile(
        result, "bin/ci",

        "#!/bin/bash",
        "#For testing purposes only."
    );

    testRegularFile(
        result, "src/main/java/br/com/objectos/Main.java",

        "package br.com.objectos;",
        "",
        "public class Main {",
        "}"
    );
  }

  @Test(description = TestCase08.DESCRIPTION)
  public void testCase08() throws ExecutionException, IOException {
    await(testCase08);

    testCase08.getResult();

    testLog(
        testCase08A,

        "68699720c357a5ce1d4171a65ce801741736ea31"
    );

    testTree(
        materialize(testCase08A),

        "README.md",
        "src/main/java/br/com/objectos/Main.java"
    );

    testLog(
        testCase08B,

        "68699720c357a5ce1d4171a65ce801741736ea31"
    );

    testTree(
        materialize(testCase08B),

        "README.md",
        "src/main/java/br/com/objectos/Main.java"
    );

    testLog(
        testCase08C,

        "a265fca5b5fc9d01302b05e5867b0cf62d6ee0ee",
        "68699720c357a5ce1d4171a65ce801741736ea31"
    );

    testTree(
        materialize(testCase08C),

        "README.md",
        "src/main/java/br/com/objectos/Git.java",
        "src/main/java/br/com/objectos/Main.java"
    );
  }

  @Test(description = TestCase10.DESCRIPTION)
  public void testCase10() throws ExecutionException, IOException {
    await(testCase10);

    ObjectId commitId;
    commitId = testCase10.getResult();

    testCommit(
        testCase10Target, commitId,

        "tree 455ac98d24c80566d39583e3dc84f1d187cd7e69",
        "author The Author <author@example.com> 1620139035 -0300",
        "committer The Committer <committer@example.com> 1620139035 -0300",
        "",
        "src only commit",
        "",
        "source=717271f0f0ee528c0bb094e8b2f84ea6cef7b39d",
        ""
    );

    Directory result;
    result = materialize(testCase10Target);

    testTree(
        result,

        "README.md",
        "src/main/java/br/com/objectos/Git.java",
        "src/main/java/br/com/objectos/Main.java"
    );

    testRegularFile(
        result, "README.md",

        "# ObjectosRepo",
        "",
        "This is a git repository meant to be used in tests."
    );

    testRegularFile(
        result, "src/main/java/br/com/objectos/Git.java",

        "package br.com.objectos;",
        "",
        "// edited by Copy10",
        "",
        "public final class Git {}"
    );

    testRegularFile(
        result, "src/main/java/br/com/objectos/Main.java",

        "package br.com.objectos;",
        "",
        "public class Main {",
        "}"
    );
  }

  @Test(description = TestCase11.DESCRIPTION)
  public void testCase11() throws ExecutionException, IOException {
    await(testCase11);

    ObjectId commitId;
    commitId = testCase11.getResult();

    testCommit(
        testCase11Target, commitId,

        "tree a15379c93a131bad60b1bf54081ae5f371870427",
        "author The Author <author@example.com> 1620139035 -0300",
        "committer The Committer <committer@example.com> 1620139035 -0300",
        "",
        "src only commit",
        "",
        "source=717271f0f0ee528c0bb094e8b2f84ea6cef7b39d",
        ""
    );

    Directory result;
    result = materialize(testCase11Target);

    testTree(
        result,

        "README.md",
        "src/main/java/br/com/objectos/Git.java",
        "src/main/java/br/com/objectos/Main.java"
    );

    testRegularFile(
        result, "README.md",

        "# ObjectosRepo",
        "",
        "test case 11"
    );
  }

  final <V> Computation<V> submit(GitCommand<V> command) {
    return service.submit(command);
  }

  private Directory materialize(Directory repository) throws IOException {
    Directory result;
    result = TmpDir.create();

    MaterializeCommand materializeCommand;
    materializeCommand = new MaterializeCommand(repository, RefName.MASTER, result);

    Computation<Directory> handle;
    handle = service.submit(materializeCommand);

    await(handle);

    try {
      return handle.getResult();
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  private void testCommit(Directory repository, ObjectId id, String... lines)
                                                                              throws IOException, ExecutionException {
    Repository repo;
    repo = openRepository(repository);

    GitTask<Commit> task;
    task = engine.readCommit(repo, id);

    Concurrent.exhaust(task);

    Commit commit;
    commit = task.getResult();

    assertEquals(
        commit.print(),

        String.join(System.lineSeparator(), lines)
    );
  }

  private void testExecutableFile(
                                  Directory dir, String path, String... expected) throws IOException {
    RegularFile file;
    file = dir.resolve(path).toRegularFile();

    assertEquals(
        Read.lines(file, Charsets.utf8()),

        UnmodifiableList.copyOf(expected)
    );

    assertTrue(file.is(Posix.ownerExecutable()));
  }

  private void testLog(Directory dir, String... expected) throws IOException, ExecutionException {
    Repository repository;
    repository = openRepository(dir);

    MaybeObjectId maybe;
    maybe = resolve(repository, Git.MASTER);

    if (!maybe.isObjectId()) {
      throw new IOException("master branch was not found");
    }

    ObjectId id;
    id = maybe.getObjectId();

    GrowableList<String> list;
    list = new GrowableList<>();

    while (id != null) {
      GitTask<Commit> computable;
      computable = engine.readCommit(repository, id);

      id = null;

      Concurrent.exhaust(computable);

      Commit commit;
      commit = computable.getResult();

      String message;
      message = commit.getMessage();

      int indexOf;
      indexOf = message.indexOf("source=");

      String source;
      source = message.substring(indexOf + 7, indexOf + 40 + 7);

      list.add(source);

      UnmodifiableList<ObjectId> parents;
      parents = commit.getParents();

      if (!parents.isEmpty()) {
        id = parents.get(0);
      }
    }

    UnmodifiableList<String> log;
    log = list.toUnmodifiableList();

    assertEquals(log, UnmodifiableList.copyOf(expected));
  }

  private void testRegularFile(
                               Directory dir, String path, String... expected) throws IOException {
    RegularFile file;
    file = dir.resolve(path).toRegularFile();

    assertEquals(
        Read.lines(file, Charsets.utf8()),

        UnmodifiableList.copyOf(expected)
    );

    assertFalse(file.is(Posix.ownerExecutable()));
  }

  private void testTree(Directory dir, String... expected) throws IOException {
    UnmodifiableList<String> result;
    result = ListRegularFiles.of(dir);

    assertEquals(
        result,

        UnmodifiableList.copyOf(expected),

        result.toString()
    );
  }

}