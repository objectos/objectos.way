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
import org.testng.annotations.Test;

public class OpenRepositoryTaskTest extends AbstractGitTest {

  @Test(description = TestCase00.DESCRIPTION)
  public void testCase00() throws IOException, ExecutionException {
    Directory directory;
    directory = TestCase00.getDirectory();

    GitTask<Repository> task;
    task = engine.openRepository(directory);

    Concurrent.exhaust(task);

    Repository result;
    result = task.getResult();

    assertEquals(result.getPackFileCount(), 0);
  }

  @Test(description = TestCase12.DESCRIPTION)
  public void testCase12() throws IOException, ExecutionException {
    Directory directory;
    directory = TestCase12.getDirectory();

    GitTask<Repository> task;
    task = engine.openRepository(directory);

    Concurrent.exhaust(task);

    Repository result;
    result = task.getResult();

    assertEquals(result.getPackFileCount(), 1);

    PackFile file0;
    file0 = result.getPackFile(0);

    assertEquals(file0.getObjectId(), TestCase12.getPackName());
  }

}