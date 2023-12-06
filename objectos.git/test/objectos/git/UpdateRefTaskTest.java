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
import objectos.core.io.Read;
import objectos.fs.Directory;
import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;
import objectos.fs.testing.TmpDir;
import org.testng.annotations.Test;

public class UpdateRefTaskTest extends AbstractGitTest {

  @Test(description = TestCase03.DESCRIPTION)
  public void testCase03() throws IOException, ExecutionException {
    Directory directory;
    directory = TestCase03.getDirectory();

    Directory target;
    target = TmpDir.create();

    directory.copyTo(target);

    Repository repository;
    repository = openRepository(target);

    ResolvedPath maybeMaster;
    maybeMaster = repository.resolveLoose(RefName.MASTER);

    RegularFile master;
    master = maybeMaster.toRegularFile();

    String before;
    before = Read.string(master, Charsets.utf8());

    assertEquals(before, "717271f0f0ee528c0bb094e8b2f84ea6cef7b39d\n");

    String id;
    id = "68699720c357a5ce1d4171a65ce801741736ea31";

    GitTask<MaybeObjectId> task;
    task = engine.updateRef(repository, RefName.MASTER, ObjectId.parse(id));

    Concurrent.exhaust(task);

    MaybeObjectId result;
    result = task.getResult();

    assertEquals(result, ObjectId.parse("717271f0f0ee528c0bb094e8b2f84ea6cef7b39d"));

    String after;
    after = Read.string(master, Charsets.utf8());

    assertEquals(after, "68699720c357a5ce1d4171a65ce801741736ea31\n");
  }

}
