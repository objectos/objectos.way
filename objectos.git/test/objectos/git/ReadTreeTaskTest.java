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

public class ReadTreeTaskTest extends AbstractGitTest {

  @Test(description = TestCase00.DESCRIPTION)
  public void testCase00() throws Exception {
    Directory directory;
    directory = TestCase00.getDirectory();

    Repository repository;
    repository = openRepository(directory);

    ObjectId oid;
    oid = TestCase00.getTree();

    GitTask<Tree> task;
    task = engine.readTree(repository, oid);

    Concurrent.exhaust(task);

    Tree result;
    result = task.getResult();

    assertEquals(result.objectId, oid);

    UnmodifiableList<Entry> entries;
    entries = result.getEntries();

    assertEquals(entries.size(), 1);

    Entry e0;
    e0 = entries.get(0);

    assertEquals(e0.mode, EntryMode.REGULAR_FILE);
    assertEquals(e0.name, "README.md");
    assertEquals(e0.object, ObjectId.parse("6eaf9247b35bbc35676d1698313381be80a4bdc4"));
  }

  @Test(description = ""
      + "1) 644 blob README; "
      + "2) 400 tree bin")
  public void testCase01() throws Exception {
    Directory directory;
    directory = TestingGit.repo00();

    Repository repository;
    repository = openRepository(directory);

    ObjectId oid;
    oid = ObjectId.parse("33fce4e6ef24da6a9c38bfee54c508a2282202d1");

    GitTask<Tree> task;
    task = engine.readTree(repository, oid);

    Concurrent.exhaust(task);

    Tree result;
    result = task.getResult();

    assertEquals(result.objectId, oid);

    UnmodifiableList<Entry> entries;
    entries = result.getEntries();

    assertEquals(entries.size(), 2);

    Entry e0;
    e0 = entries.get(0);

    assertEquals(e0.mode, EntryMode.REGULAR_FILE);
    assertEquals(e0.name, "README.md");
    assertEquals(e0.object, ObjectId.parse("6eaf9247b35bbc35676d1698313381be80a4bdc4"));

    Entry e1;
    e1 = entries.get(1);

    assertEquals(e1.mode, EntryMode.TREE);
    assertEquals(e1.name, "bin");
    assertEquals(e1.object, ObjectId.parse("dcfae91ae42930b3c7aa438404fb96b5c25068e7"));
  }

  @Test(description = TestCase12.DESCRIPTION)
  public void testCase12() throws IOException, ExecutionException {
    Repository repository;
    repository = TestCase12.getRepository();

    ObjectId objectId;
    objectId = TestCase12.getTreeDeltified();

    GitTask<Tree> task;
    task = engine.readTree(repository, objectId);

    Concurrent.exhaust(task);

    Tree result;
    result = task.getResult();

    assertEquals(result.objectId, objectId);

    UnmodifiableList<Entry> entries;
    entries = result.getEntries();

    assertEquals(entries.size(), 2);

    Entry e0;
    e0 = entries.get(0);

    assertEquals(e0.mode, EntryMode.REGULAR_FILE);
    assertEquals(e0.name, "README.md");
    assertEquals(e0.object, ObjectId.parse("6eaf9247b35bbc35676d1698313381be80a4bdc4"));

    Entry e1;
    e1 = entries.get(1);

    assertEquals(e1.mode, EntryMode.TREE);
    assertEquals(e1.name, "bin");
    assertEquals(e1.object, ObjectId.parse("dcfae91ae42930b3c7aa438404fb96b5c25068e7"));
  }

}