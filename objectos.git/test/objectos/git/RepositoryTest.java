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
import objectos.core.io.Charsets;
import objectos.core.io.Write;
import objectos.fs.Directory;
import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;
import objectos.fs.testing.TmpDir;
import org.testng.annotations.Test;

public class RepositoryTest {

  @Test
  public void resolveLoose() throws IOException, ExecutionException {
    Directory root;
    root = TmpDir.create();

    Directory repoDirectory;
    repoDirectory = TestingGit.repo00();

    repoDirectory.copyTo(root);

    Directory heads;
    heads = root.resolve("refs", "heads").toDirectory();

    RegularFile masterFile;
    masterFile = heads.getRegularFile("master");

    String sha1;
    sha1 = "f85e9a57954552c1b115a61664271040e6daa1c6";

    Write.string(masterFile, Charsets.utf8(), sha1);

    Repository repository;
    repository = TestingGit.bareRepository(root);

    ResolvedPath maybe;
    maybe = repository.resolveLoose(Git.MASTER);

    RegularFile result;
    result = maybe.toRegularFile();

    assertEquals(result, masterFile);
  }

  @Test
  public void testCase07() throws IOException, ExecutionException {
    Repository repository;
    repository = TestingGit.openRepo00();

    MaybeObjectId maybe;
    maybe = TestingGit.resolve(repository, RefName.MASTER);

    assertTrue(maybe.isObjectId());

    ObjectId result;
    result = maybe.getObjectId();

    assertEquals(result.getHexString(), "717271f0f0ee528c0bb094e8b2f84ea6cef7b39d");
  }

  @Test
  public void testCase09() throws IOException, ExecutionException {
    Repository repository;
    repository = TestingGit.openRepo00();

    RegularFile master;
    master = repository.resolveLoose(RefName.MASTER).toRegularFile();

    master.delete();

    MaybeObjectId maybe;
    maybe = TestingGit.resolve(repository, RefName.MASTER);

    assertTrue(maybe.isObjectId());

    ObjectId result;
    result = maybe.getObjectId();

    assertEquals(result.getHexString(), "717271f0f0ee528c0bb094e8b2f84ea6cef7b39d");
  }

}