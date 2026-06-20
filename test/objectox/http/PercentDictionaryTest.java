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
package objectox.http;

import static org.testng.Assert.assertEquals;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PercentDictionaryTest {

  @Test(description = "reject dictionary with non-ascii characters")
  public void generate01() {
    final String input;
    input = "\u00FF";

    try {
      new PercentDictionary(input);

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "Invalid dictionary string: only US-ASCII characters are allowed, but found '\u00FF'");
    }
  }

  @Test(description = "empty string should generate all false")
  public void generate02() {
    final String input;
    input = "";

    final PercentDictionary subject;
    subject = new PercentDictionary(input);

    for (char c = 0; c < subject.length(); c++) {
      assertEquals(subject.test(c), false);
    }
  }

  @Test(description = "all US-ASCII chars should generate all true")
  public void generate03() {
    final StringBuilder sb;
    sb = new StringBuilder();

    for (char c = 0; c < 128; c++) {
      sb.append(c);
    }

    final String input;
    input = sb.toString();

    final PercentDictionary subject;
    subject = new PercentDictionary(input);

    for (char c = 0; c < subject.length(); c++) {
      assertEquals(subject.test(c), true);
    }
  }

  @Test(description = "true indices should match char value")
  public void generate04() {
    final StringBuilder sb;
    sb = new StringBuilder();

    sb.append((char) 30);

    sb.append((char) 60);

    sb.append((char) 90);

    final String input;
    input = sb.toString();

    final PercentDictionary subject;
    subject = new PercentDictionary(input);

    for (char c = 0; c < subject.length(); c++) {
      assertEquals(
          subject.test(c),

          switch (c) {
            case 30, 60, 90 -> true;

            default -> false;
          }
      );
    }
  }

}
