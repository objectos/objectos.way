/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
import java.nio.file.Files;
import java.nio.file.Path;
import org.testng.annotations.Test;

public class GitRepoTest {

  @Test
  public void testCase00() throws IOException {
    Path root;
    root = Files.createTempDirectory("git-test-");

    try {
      TestCase00.repositoryTo(root);

      // open repository
      GitRepo repo;
      repo = GitRepo.open(TestingNoteSink.INSTANCE, root);

      assertEquals(repo.isBare(), true);
      assertEquals(repo.getPackFileCount(), 0);

      // resolve
      Path packedRefs;
      packedRefs = root.resolve("packed-refs");

      Files.delete(packedRefs);

      MaybeObjectId maybeId;
      maybeId = repo.resolve(RefName.MASTER);

      assertTrue(maybeId.isObjectId());

      ObjectId objectId;
      objectId = maybeId.getObjectId();

      assertEquals(objectId.getHexString(), "717271f0f0ee528c0bb094e8b2f84ea6cef7b39d");
    } finally {
      TestingGit2.deleteRecursively(root);
    }
  }

}