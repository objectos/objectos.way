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

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import objectos.concurrent.Concurrent;
import objectos.fs.Directory;
import objectos.util.list.UnmodifiableList;
import org.testng.annotations.Test;

public class ReadCommitTaskTest extends AbstractGitTest {

  @Test(description = TestCase00.DESCRIPTION)
  public void testCase00() throws IOException, ExecutionException {
    Directory directory;
    directory = TestCase00.getDirectory();

    Repository repository;
    repository = openRepository(directory);

    ObjectId oid;
    oid = TestCase00.getCommit();

    GitTask<Commit> task;
    task = engine.readCommit(repository, oid);

    Concurrent.exhaust(task);

    Commit result;
    result = task.getResult();

    assertEquals(
      result.getMessage(),

      String.join(
        System.lineSeparator(),

        "add README.md",
        ""
      )
    );

    UnmodifiableList<ObjectId> parents;
    parents = result.getParents();

    assertEquals(parents.size(), 0);

    assertEquals(result.getTree(), ObjectId.parse("1cd042294d3933032f5fbb9735034dcbce689dc9"));
  }

  @Test(description = ""
      + "1) single parent")
  public void testCase01() throws IOException, ExecutionException {
    Directory directory;
    directory = TestingGit.repo00();

    Repository repository;
    repository = openRepository(directory);

    ObjectId oid;
    oid = ObjectId.parse("8ae78c68a5eb6abf81e7bd006d7d9bf326780589");

    GitTask<Commit> task;
    task = engine.readCommit(repository, oid);

    Concurrent.exhaust(task);

    Commit result;
    result = task.getResult();

    assertEquals(
      result.getMessage(),

      String.join(
        System.lineSeparator(),

        "[bin] add a ci script",
        ""
      )
    );

    UnmodifiableList<ObjectId> parents;
    parents = result.getParents();

    assertEquals(parents.size(), 1);

    ObjectId parent0;
    parent0 = parents.get(0);

    assertEquals(parent0, ObjectId.parse("b9c4f2db7b4fd742990b518ee3c8ae59eb1d6e93"));

    assertEquals(result.getTree(), ObjectId.parse("33fce4e6ef24da6a9c38bfee54c508a2282202d1"));
  }

  @Test(description = TestCase12.DESCRIPTION)
  public void testCase12() throws IOException, ExecutionException {
    Directory directory;
    directory = TestCase12.getDirectory();

    Repository repository;
    repository = openRepository(directory);

    ObjectId oid;
    oid = TestCase12.getCommitNonDeltified();

    GitTask<Commit> task;
    task = engine.readCommit(repository, oid);

    Concurrent.exhaust(task);

    Commit result;
    result = task.getResult();

    assertEquals(
      result.getMessage(),

      String.join(
        System.lineSeparator(),

        "[bin] add a ci script",
        ""
      )
    );

    UnmodifiableList<ObjectId> parents;
    parents = result.getParents();

    assertEquals(parents.size(), 1);

    ObjectId parent0;
    parent0 = parents.get(0);

    assertEquals(parent0, ObjectId.parse("b9c4f2db7b4fd742990b518ee3c8ae59eb1d6e93"));

    assertEquals(result.getTree(), ObjectId.parse("33fce4e6ef24da6a9c38bfee54c508a2282202d1"));
  }

}