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
import objectos.util.list.UnmodifiableList;
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

      // read commit
      ObjectId commitId;
      commitId = TestCase00.getCommit();

      Commit commit;
      commit = repo.readCommit(commitId);

      assertEquals(
          commit.getMessage(),

          """
          add README.md
          """
      );

      UnmodifiableList<ObjectId> parents;
      parents = commit.getParents();

      assertEquals(parents.size(), 0);
      assertEquals(commit.getTree(), ObjectId.parse("1cd042294d3933032f5fbb9735034dcbce689dc9"));

      // read tree
      ObjectId treeId;
      treeId = TestCase00.getTree();

      Tree tree;
      tree = repo.readTree(treeId);

      assertEquals(tree.objectId, treeId);

      UnmodifiableList<Entry> entries;
      entries = tree.getEntries();

      assertEquals(entries.size(), 1);

      Entry e0;
      e0 = entries.get(0);

      assertEquals(e0.mode, EntryMode.REGULAR_FILE);
      assertEquals(e0.name, "README.md");
      assertEquals(e0.object, ObjectId.parse("6eaf9247b35bbc35676d1698313381be80a4bdc4"));
    } finally {
      TestingGit2.deleteRecursively(root);
    }
  }

}