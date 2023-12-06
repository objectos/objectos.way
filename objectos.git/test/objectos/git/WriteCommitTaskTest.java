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
import objectos.fs.testing.TmpDir;
import objectos.util.list.UnmodifiableList;
import org.testng.annotations.Test;

public class WriteCommitTaskTest extends AbstractGitTest {

  @Test
  public void testCase03() throws IOException, ExecutionException {
    MutableCommit input;
    input = new MutableCommit();

    Identification author;
    author = new Identification("The Author", "author@example.com", 1615551529, "-0300");

    input.setAuthor(author);

    Identification committer;
    committer = new Identification("The Committer", "committer@example.com", 1615551530, "+0300");

    input.setCommitter(committer);

    String message;
    message = "[git] test case 03";

    input.setMessage(message);

    ObjectId parent;
    parent = ObjectId.parse("b9c4f2db7b4fd742990b518ee3c8ae59eb1d6e93");

    input.addParent(parent);

    ObjectId tree;
    tree = ObjectId.parse("1cd042294d3933032f5fbb9735034dcbce689dc9");

    input.setTree(tree);

    Directory target;
    target = TmpDir.create();

    Directory repo00;
    repo00 = TestingGit.repo00();

    repo00.copyTo(target);

    Repository repository;
    repository = TestingGit.bareRepository(target);

    GitTask<ObjectId> task;
    task = engine.writeCommit(repository, input);

    Concurrent.exhaust(task);

    ObjectId result;
    result = task.getResult();

    GitTask<Commit> fetchCommitTask;
    fetchCommitTask = engine.readCommit(repository, result);

    Concurrent.exhaust(fetchCommitTask);

    Commit commit;
    commit = fetchCommitTask.getResult();

    assertEquals(commit.getAuthor(), author);

    assertEquals(commit.getCommitter(), committer);

    assertEquals(commit.getMessage(), message);

    UnmodifiableList<ObjectId> parents;
    parents = commit.getParents();

    assertEquals(parents.size(), 1);

    assertEquals(parents.get(0), parent);

    assertEquals(commit.getTree(), tree);
  }

}