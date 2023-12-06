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
import org.testng.annotations.Test;

public class WriteTreeTaskTest extends AbstractGitTest {

  @Test(description = TestCase05.DESCRIPTION)
  public void testCase05() throws IOException, ExecutionException {
    Repository repository;
    repository = createEmptyRepository();

    MutableTree tree;
    tree = TestCase05.getMutableTree();

    GitTask<ObjectId> task;
    task = engine.writeTree(repository, tree);

    Concurrent.exhaust(task);

    ObjectId id;
    id = task.getResult();

    assertEquals(id, ObjectId.parse("81059b029e0e689eb8cae57cb9f3c27f3061ba9d"));
  }

}