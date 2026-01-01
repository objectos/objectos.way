/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
      final Option<String> option = optionString(opts -> {
        opts.name("--test01");
      });
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
      final Option<String> option = optionString(opts -> {
        opts.name("--test02");

        opts.required();
      });

      final Option<String> other = optionString(opts -> {
        opts.name("--other");
      });
    }

    var subject = parseArgs(new Subject(), "--other", "foo");

    assertEquals(subject.messagesSize(), 1);
    assertEquals(subject.message(0), "Missing required option: --test02");
    assertEquals(subject.option.get(), null);
    assertEquals(subject.other.get(), "foo");
  }

  @Test(description = """
  option("--foo", ofString(), defaultValue("some-value"))
  """)
  public void testCase03() {
    class Subject extends Args {
      final Option<String> option = optionString(opts -> {
        opts.name("--test03");

        opts.required();

        opts.value("default");
      });

      final Option<String> other = optionString(opts -> {
        opts.name("--other");
      });
    }

    var subject = parseArgs(new Subject(), "--other", "foo");

    assertEquals(subject.messagesSize(), 0);
    assertEquals(subject.option.get(), "default");
    assertEquals(subject.other.get(), "foo");
  }

  @Test(description = """
  Support custom validator
  """)
  public void testCase04() {
    class Subject extends Args {
      final Option<String> option = optionString(opts -> {
        opts.name("--test04");

        opts.validator(this::validateName, "name must be at least 10 characters long");
      });

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
      final Option<Integer> option = optionInteger(opts -> opts.name("--test05"));
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
      final Option<Path> option = optionPath(opts -> opts.name("--test06"));
    }

    var subject = parseArgs(new Subject(), "--test06", "a/b/c.txt");

    assertEquals(subject.messagesSize(), 0);
    assertEquals(subject.option.get(), Path.of("a", "b", "c.txt"));
  }

  @Test(description = """
  option(Option.ofSet(), "--foo")
  """)
  public void testCase07() {
    class Subject extends Args {
      final Option<Set<Path>> option = optionSet(Path::of, opts -> {
        opts.name("--test07");
      });
    }

    var subject = parseArgs(new Subject(), "--test07", "a.txt", "--test07", "b.txt");

    assertEquals(subject.messagesSize(), 0);

    Set<Path> set = subject.option.get();

    assertEquals(set.size(), 2);
    assertTrue(set.contains(Path.of("a.txt")));
    assertTrue(set.contains(Path.of("b.txt")));
  }

  @Test(description = """
  Disallow unknown options
  """)
  public void testCase08() {
    class Subject extends Args {}

    var subject = parseArgs(new Subject(), "--test08", "foo");

    assertEquals(subject.messagesSize(), 1);
    assertEquals(subject.message(0), "Unrecognized option '--test08'");
  }

  @Test(description = """
  Disallow unknown options
  """)
  public void testCase09() {
    class Subject extends Args {
      @SuppressWarnings("unused")
      Option<String> option = optionString(opts -> opts.name("--test09"));
    }

    var subject = parseArgs(new Subject(), "--test09", "foo", "--unknown", "bar");

    assertEquals(subject.messagesSize(), 1);
    assertEquals(subject.message(0), "Unrecognized option '--unknown'");
  }

  private <T extends Args> T parseArgs(T subject, String... args) {
    subject.parseArgs(args);

    return subject;
  }

}