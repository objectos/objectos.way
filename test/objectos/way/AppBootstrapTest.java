/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

    assertEquals(subject.hasMessages(), false);
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

    assertEquals(subject.hasMessages(), true);
    assertEquals(subject.message(0), "Missing required option: --test02");
    assertEquals(subject.option.get(), null);
  }

  @Test(description = """
  option("--foo", ofString(), defaultValue("some-value"))
  """)
  public void testCase03() {
    class Subject extends Args {
      final App.Option<String> option = option("--test03", ofString(), required(), defaultValue("default"));
    }

    var subject = parseArgs(new Subject(), "--test01", "foo");

    assertEquals(subject.hasMessages(), false);
    assertEquals(subject.option.get(), "default");
  }

  private <T extends Args> T parseArgs(T subject, String... args) {
    subject.parseArgs(args);

    return subject;
  }

}