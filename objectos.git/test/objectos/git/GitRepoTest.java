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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Set;
import objectos.core.io.Charsets;
import objectos.fs.ResolvedPath;
import objectos.util.list.UnmodifiableList;
import objectos.util.set.UnmodifiableSet;
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

  @Test
  public void testCase03() throws IOException {
    Path root;
    root = Files.createTempDirectory("git-test-");

    try {
      TestCase03.repositoryTo(root);

      // open repository
      GitRepo repo;
      repo = GitRepo.open(TestingNoteSink.INSTANCE, root);

      // write commit
      MutableCommit input;
      input = new MutableCommit();

      Identification author;
      author = new Identification("The Author", "author@example.com", 1615551529, "-0300");

      input.setAuthor(author);

      Identification committer;
      committer = new Identification("The Committer", "committer@example.com", 1615551530, "+0300");

      input.setCommitter(committer);

      String message;
      message = "[git] test case 03";

      input.setMessage(message);

      ObjectId parent;
      parent = ObjectId.parse("b9c4f2db7b4fd742990b518ee3c8ae59eb1d6e93");

      input.addParent(parent);

      ObjectId tree;
      tree = ObjectId.parse("1cd042294d3933032f5fbb9735034dcbce689dc9");

      input.setTree(tree);

      ObjectId result;
      result = repo.writeCommit(input);

      Commit commit;
      commit = repo.readCommit(result);

      assertEquals(commit.getAuthor(), author);
      assertEquals(commit.getCommitter(), committer);
      assertEquals(commit.getMessage(), message);

      UnmodifiableList<ObjectId> parents;
      parents = commit.getParents();

      assertEquals(parents.size(), 1);
      assertEquals(parents.get(0), parent);
      assertEquals(commit.getTree(), tree);
    } finally {
      TestingGit2.deleteRecursively(root);
    }
  }

  @Test
  public void testCase05() throws IOException {
    Path root;
    root = Files.createTempDirectory("git-test-");

    try {
      Path src;
      src = root.resolve("src");

      TestCase05.repositoryTo(src);

      Path dest;
      dest = root.resolve("dest");

      TestingGit2.emptyRepo(dest);

      // open repository
      GitRepo repo;
      repo = GitRepo.open(TestingNoteSink.INSTANCE, src);

      GitRepo target;
      target = GitRepo.open(TestingNoteSink.INSTANCE, dest);

      // copy objects
      UnmodifiableSet<ObjectId> set;
      set = TestCase05.getCopyObjectSet();

      Iterator<ObjectId> it;
      it = set.iterator();

      assertLooseNotFound(target, it.next());
      assertLooseNotFound(target, it.next());
      assertLooseNotFound(target, it.next());
      assertLooseNotFound(target, it.next());

      Set<ObjectId> copied;
      copied = repo.copyObjects(target, set);

      assertEquals(copied.size(), 4);

      it = copied.iterator();

      assertLooseExists(target, it.next());
      assertLooseExists(target, it.next());
      assertLooseExists(target, it.next());
      assertLooseExists(target, it.next());

      // write tree
      MutableTree tree;
      tree = TestCase05.getMutableTree();

      ObjectId id;
      id = target.writeTree(tree);

      assertEquals(id, ObjectId.parse("81059b029e0e689eb8cae57cb9f3c27f3061ba9d"));
    } finally {
      TestingGit2.deleteRecursively(root);
    }
  }

  @Test
  public void testCase12() throws IOException {
    Path root;
    root = Files.createTempDirectory("git-test-");

    try {
      TestCase12.repositoryTo(root);

      // open repository
      GitRepo repo;
      repo = GitRepo.open(TestingNoteSink.INSTANCE, root);

      assertEquals(repo.isBare(), true);
      assertEquals(repo.getPackFileCount(), 1);

      PackFile packFile0;
      packFile0 = repo.getPackFile(0);

      assertEquals(packFile0.getObjectId(), TestCase12.getPackName());

      // read blob
      ObjectId blobId;
      blobId = TestCase12.getBlobDeltified();

      Blob blob;
      blob = repo.readBlob(blobId);

      assertEquals(
          blob.toString(Charsets.utf8()),

          """
          # ObjectosRepo

          This is a git repository meant to be used in tests.
          """
      );
    } finally {
      TestingGit2.deleteRecursively(root);
    }
  }

  private void assertLooseNotFound(GitRepo repo, ObjectId objectId) throws IOException {
    ResolvedPath resolvedPath;
    resolvedPath = repo.resolveLooseObject(objectId);

    assertFalse(resolvedPath.exists());
  }

  private void assertLooseExists(GitRepo repo, ObjectId objectId) throws IOException {
    ResolvedPath resolvedPath;
    resolvedPath = repo.resolveLooseObject(objectId);

    assertTrue(resolvedPath.exists());
  }

}