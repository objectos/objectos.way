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

import objectos.util.list.GrowableList;
import objectos.util.list.UnmodifiableList;
import org.testng.annotations.Test;

public class EntryTest {

  @Test
  public void compareTo() {
    GrowableList<MutableTreeEntry> entries;
    entries = new GrowableList<>();

    entries.add(TestingGit.tree("foo", "74a9c52015b363408e231ddea72ca57746615e77"));
    entries.add(TestingGit.tree("foo-bar", "74a9c52015b363408e231ddea72ca57746615e77"));

    entries.sort(MutableTreeEntry.ORDER);

    assertEquals(
        toNames(entries),

        UnmodifiableList.of(
            "foo-bar",
            "foo"
        )
    );
  }

  @Test
  public void print() throws InvalidObjectIdFormatException {
    assertEquals(
        new Entry(
            EntryMode.TREE,

            "objectos",

            ObjectId.parse("74a9c52015b363408e231ddea72ca57746615e77")
        ).print(),

        "040000 tree 74a9c52015b363408e231ddea72ca57746615e77    objectos"
    );

    assertEquals(
        new Entry(
            EntryMode.REGULAR_FILE,

            "pom.xml",

            ObjectId.parse("d72e03e0e09d1151dfd769e90bfe8f83f9fc5dfe")
        ).print(),

        "100644 blob d72e03e0e09d1151dfd769e90bfe8f83f9fc5dfe    pom.xml"
    );
  }

  private UnmodifiableList<String> toNames(GrowableList<MutableTreeEntry> entries) {
    GrowableList<String> result;
    result = new GrowableList<>();

    for (int i = 0, size = entries.size(); i < size; i++) {
      MutableTreeEntry e;
      e = entries.get(i);

      result.add(e.getName());
    }

    return result.toUnmodifiableList();
  }

}