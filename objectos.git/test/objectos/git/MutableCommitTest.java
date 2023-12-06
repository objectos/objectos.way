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
import static org.testng.Assert.assertNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MutableCommitTest extends AbstractGitTest {

  private final MutableCommit commit = new MutableCommit();

  @BeforeMethod
  public void beforeMethod() {
    commit.clear();
  }

  @Test
  public void testCase03() throws InvalidObjectIdFormatException {
    Identification author;
    author = new Identification("The Author", "author@example.com", 1615551529, "-0300");

    commit.setAuthor(author);

    Identification committer;
    committer = new Identification("The Committer", "committer@example.com", 1615551530, "+0300");

    commit.setCommitter(committer);

    String message;
    message = "[git] test case 03";

    commit.setMessage(message);

    ObjectId parent;
    parent = ObjectId.parse("b9c4f2db7b4fd742990b518ee3c8ae59eb1d6e93");

    commit.addParent(parent);

    ObjectId tree;
    tree = ObjectId.parse("1cd042294d3933032f5fbb9735034dcbce689dc9");

    commit.setTree(tree);

    StringBuilder stringBuilder;
    stringBuilder = engine.getStringBuilder();

    IllegalArgumentException maybe;
    maybe = commit.validate(stringBuilder);

    assertNull(maybe);

    commit.acceptStringBuilder(stringBuilder);

    String s;
    s = stringBuilder.toString();

    engine.putStringBuilder(stringBuilder);

    assertEquals(
        s,
        "tree 1cd042294d3933032f5fbb9735034dcbce689dc9\n"
            + "parent b9c4f2db7b4fd742990b518ee3c8ae59eb1d6e93\n"
            + "author The Author <author@example.com> 1615551529 -0300\n"
            + "committer The Committer <committer@example.com> 1615551530 +0300\n"
            + "\n"
            + "[git] test case 03"
    );
  }

}