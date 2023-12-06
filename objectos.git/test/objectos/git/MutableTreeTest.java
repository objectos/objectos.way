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

import static objectos.git.TestingGit.executable;
import static objectos.git.TestingGit.regular;
import static objectos.git.TestingGit.tree;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import objectos.core.io.Charsets;
import objectos.fs.Directory;
import objectos.fs.RegularFile;
import objectos.fs.testing.TmpDir;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class MutableTreeTest {

  private GitEngine adapter;

  @BeforeClass
  public void _setUp() {
    adapter = TestingGit.standardEngineInstance();
  }

  @Test
  public void testCase01() throws Exception {
    Directory directory;
    directory = TmpDir.create();

    MutableTree root;
    root = new MutableTree();

    root.addEntry(
        regular("README.md", "6eaf9247b35bbc35676d1698313381be80a4bdc4")
    );

    root.computeContents(adapter, Charsets.utf8());

    ObjectId rootId;
    rootId = ObjectId.parse("1cd042294d3933032f5fbb9735034dcbce689dc9");

    assertEquals(root.getObjectId(), rootId);

    Repository repository;
    repository = createDatatabse(directory);

    root.writeTree(adapter, repository);

    RegularFile rootFile;
    rootFile = repository.getLooseObjectFile(rootId);

    assertTrue(rootFile.exists());
  }

  @Test
  public void testCase02() throws IOException {
    MutableTree root;
    root = new MutableTree();

    root.addEntry(
        regular("README.md", "6eaf9247b35bbc35676d1698313381be80a4bdc4")
    );

    root.addEntry(
        tree("bin",
            executable("ci", "1584aeadeea6a620b0d91016b7aa1eca2f62af46")
        )
    );

    root.computeContents(adapter, Charsets.utf8());

    assertEquals(
        root.getObjectId(),
        ObjectId.parse("33fce4e6ef24da6a9c38bfee54c508a2282202d1")
    );
  }

  @Test
  public void testCase05() throws IOException {
    MutableTree root;
    root = TestCase05.getMutableTree();

    root.computeContents(adapter, Charsets.utf8());

    assertEquals(
        root.getObjectId(),
        ObjectId.parse("81059b029e0e689eb8cae57cb9f3c27f3061ba9d")
    );
  }

  @Test
  public void testCase11() throws IOException {
    MutableTree root;
    root = new MutableTree();

    root.addEntry(regular(".classpath", "c98ac4554df37ff42905dd8e468157bbc5bf988f"));

    root.addEntry(regular(".project", "dc92fa952fc2a810a47989e2ec1482b24aa4ff7d"));

    root.addEntry(tree(".settings", "3ecb3dd913563ef84d9c9259d84b2249abb8f5f8"));

    root.addEntry(tree("src", "107cf9a080c192f44d1bb6595c41fe6f7c5ee9fb"));

    root.addEntry(regular("pom.xml", "8ebcbf4eb6ed5923b7f093f39d6220b2f55ef465"));

    root.computeContents(adapter, Charsets.utf8());

    assertEquals(
        root.getObjectId(),
        ObjectId.parse("3c3c536c30d7c00f29f67d60fea5c2a152c2b809")
    );
  }

  private Repository createDatatabse(Directory directory) throws IOException, ExecutionException {
    directory.createDirectory("objects");

    return TestingGit.bareRepository(directory);
  }

}