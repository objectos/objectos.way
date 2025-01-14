/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;
import org.testng.annotations.Test;

public class AppBootstrapTest {

  private static abstract class Args extends App.Bootstrap {
    @Override
    protected final void bootstrap() { /*noop*/ }
  }

  @Test(description = """
  option("--foo", ofString())
  """)
  public void testCase01() {
    class Subject extends Args {
      final App.Option<String> option = option("--test01", ofString());
    }

    var subject = parseArgs(new Subject(), "--test01", "foo");

    assertEquals(subject.messagesSize(), 0);
    assertEquals(subject.option.get(), "foo");
  }

  @Test(description = """
  option("--foo", ofString(), required())
  """)
  public void testCase02() {
    class Subject extends Args {
      final App.Option<String> option = option("--test02", ofString(), required());
    }

    var subject = parseArgs(new Subject(), "--test01", "foo");

    assertEquals(subject.messagesSize(), 1);
    assertEquals(subject.message(0), "Missing required option: --test02");
    assertEquals(subject.option.get(), null);
  }

  @Test(description = """
  option("--foo", ofString(), defaultValue("some-value"))
  """)
  public void testCase03() {
    class Subject extends Args {
      final App.Option<String> option = option("--test03", ofString(), required(), withValue("default"));
    }

    var subject = parseArgs(new Subject(), "--test01", "foo");

    assertEquals(subject.messagesSize(), 0);
    assertEquals(subject.option.get(), "default");
  }

  @Test(description = """
  Support custom validator
  """)
  public void testCase04() {
    class Subject extends Args {
      final App.Option<String> option = option("--test04", ofString(), withValidator(this::validateName, "name must be at least 10 characters long"));

      private boolean validateName(String name) {
        return name.length() > 10;
      }
    }

    var subject = parseArgs(new Subject(), "--test04", "foo");

    assertEquals(subject.messagesSize(), 1);
    assertEquals(subject.message(0), "Invalid --test04 value: name must be at least 10 characters long");
    assertEquals(subject.option.get(), null);
  }

  @Test(description = """
  option("--foo", ofInteger())
  """)
  public void testCase05() {
    class Subject extends Args {
      final App.Option<Integer> option = option("--test05", ofInteger());
    }

    var subject = parseArgs(new Subject(), "--test05", "123");

    assertEquals(subject.messagesSize(), 0);
    assertEquals(subject.option.get(), 123);
  }

  @Test(description = """
  option("--foo", ofPath())
  """)
  public void testCase06() {
    class Subject extends Args {
      final App.Option<Path> option = option("--test06", ofPath());
    }

    var subject = parseArgs(new Subject(), "--test06", "a/b/c.txt");

    assertEquals(subject.messagesSize(), 0);
    assertEquals(subject.option.get(), Path.of("a", "b", "c.txt"));
  }

  @Test(description = """
  option("--foo", ofCollection())
  """)
  public void testCase07() {
    class Subject extends Args {
      final App.Option<Set<Path>> option = option("--test07", ofCollection(LinkedHashSet::new, ofPath()));
    }

    var subject = parseArgs(new Subject(), "--test07", "a.txt", "foo", "--test07", "b.txt");

    assertEquals(subject.messagesSize(), 0);

    Set<Path> set = subject.option.get();

    assertEquals(set.size(), 2);
    assertTrue(set.contains(Path.of("a.txt")));
    assertTrue(set.contains(Path.of("b.txt")));
  }

  private <T extends Args> T parseArgs(T subject, String... args) {
    subject.parseArgs(args);

    return subject;
  }

}