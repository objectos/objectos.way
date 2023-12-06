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
import objectos.core.io.Charsets;
import objectos.fs.Directory;
import org.testng.annotations.Test;

public class ReadBlobTaskTest extends AbstractGitTest {

  @Test
  public void testCase12() throws IOException, ExecutionException {
    Directory directory;
    directory = TestCase12.getDirectory();

    Repository repository;
    repository = openRepository(directory);

    ObjectId objectId;
    objectId = TestCase12.getBlobDeltified();

    GitTask<Blob> task;
    task = engine.readBlob(repository, objectId);

    Concurrent.exhaust(task);

    Blob result;
    result = task.getResult();

    assertEquals(
      result.toString(Charsets.utf8()),

      String.join(
        System.lineSeparator(),

        "# ObjectosRepo",
        "",
        "This is a git repository meant to be used in tests.",
        ""
      )
    );
  }

}