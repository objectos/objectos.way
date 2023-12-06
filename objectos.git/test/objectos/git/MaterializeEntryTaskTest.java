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
import objectos.concurrent.Concurrent;
import objectos.core.io.Read;
import objectos.fs.Directory;
import objectos.fs.RegularFile;
import objectos.fs.testing.TmpDir;
import org.testng.annotations.Test;

public class MaterializeEntryTaskTest extends AbstractGitTest {

  @Test
  public void testCase00() throws IOException, ExecutionException {
    Directory directory;
    directory = TestCase00.getDirectory();

    Repository repository;
    repository = openRepository(directory);

    Entry entry;
    entry = TestCase00.getBlobEntry();

    Directory target;
    target = TmpDir.create();

    GitTask<MaterializedEntry> task;
    task = engine.materializeEntry(repository, entry, target);

    assertTrue(target.isEmpty());

    Concurrent.exhaust(task);

    MaterializedEntry result;
    result = task.getResult();

    assertFalse(result.isTree());

    MaterializedBlob blob;
    blob = (MaterializedBlob) result;

    RegularFile file;
    file = blob.getFile();

    assertEquals(file.getParent(), target);

    assertEquals(file.getName(), "README.md");

    assertEquals(Read.byteArray(file), TestCase00.getBlobContents());
  }

}