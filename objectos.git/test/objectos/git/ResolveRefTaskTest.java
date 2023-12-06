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
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import objectos.concurrent.Concurrent;
import objectos.fs.Directory;
import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;
import objectos.fs.testing.TmpDir;
import org.testng.annotations.Test;

public class ResolveRefTaskTest extends AbstractGitTest {

  @Test(description = TestCase00.DESCRIPTION)
  public void testCase00() throws GitStubException, IOException, ExecutionException {
    Directory root;
    root = TmpDir.create();

    Directory directory;
    directory = TestCase00.getDirectory();

    directory.copyTo(root);

    RegularFile packedRefs;
    packedRefs = root.getRegularFile("packed-refs");

    packedRefs.delete();

    Repository repository;
    repository = openRepository(root);

    GitTask<MaybeObjectId> task;
    task = engine.resolve(repository, RefName.MASTER);

    Concurrent.exhaust(task);

    MaybeObjectId result;
    result = task.getResult();

    assertTrue(result.isObjectId());

    ObjectId objectId;
    objectId = result.getObjectId();

    assertEquals(objectId.getHexString(), "717271f0f0ee528c0bb094e8b2f84ea6cef7b39d");
  }

  @Test
  public void testCase09() throws GitStubException, IOException, ExecutionException {
    Directory root;
    root = TmpDir.create();

    Directory directory;
    directory = TestCase00.getDirectory();

    directory.copyTo(root);

    ResolvedPath maybeMaster;
    maybeMaster = root.resolve("refs", "heads", "master");

    RegularFile master;
    master = maybeMaster.toRegularFile();

    master.delete();

    Repository repository;
    repository = openRepository(root);

    GitTask<MaybeObjectId> task;
    task = engine.resolve(repository, RefName.MASTER);

    Concurrent.exhaust(task);

    MaybeObjectId result;
    result = task.getResult();

    assertTrue(result.isObjectId());

    ObjectId objectId;
    objectId = result.getObjectId();

    assertEquals(objectId.getHexString(), "717271f0f0ee528c0bb094e8b2f84ea6cef7b39d");
  }

}