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
import objectos.concurrent.Computation;
import objectos.concurrent.Concurrent;
import objectos.fs.Directory;
import objectos.util.list.UnmodifiableList;
import org.testng.annotations.Test;

public class CopyTreeCommandTest extends AbstractGitTest {

  @Test(description = TestCase03.DESCRIPTION)
  public void testCase03() throws IOException, ExecutionException {
    Directory directory;
    directory = TestCase03.getDirectory();

    Repository source;
    source = openRepository(directory);

    ObjectId id;
    id = TestCase03.getTree();

    Repository target;
    target = createEmptyRepository();

    CopyTreeCommand command;
    command = new CopyTreeCommand(source, id, target);

    Computation<ObjectId> computation;
    computation = service.submit(command);

    await(computation);

    ObjectId result;
    result = computation.getResult();

    assertEquals(result, id);

    GitTask<Tree> maybeTree;
    maybeTree = engine.readTree(target, result);

    Concurrent.exhaust(maybeTree);

    Tree tree;
    tree = maybeTree.getResult();

    UnmodifiableList<Entry> entries;
    entries = tree.getEntries();

    assertEquals(entries.size(), 1);

    Entry e0;
    e0 = entries.get(0);

    assertEquals(e0.mode, EntryMode.REGULAR_FILE);
    assertEquals(e0.name, "README.md");
    assertEquals(e0.object, ObjectId.parse("6eaf9247b35bbc35676d1698313381be80a4bdc4"));
  }

}