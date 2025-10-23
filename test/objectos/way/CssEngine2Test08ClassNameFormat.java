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

import objectos.way.CssEngine2.ClassNameFormat;
import org.testng.annotations.Test;

public class CssEngine2Test08ClassNameFormat {

  private final ClassNameFormat format = new CssEngine2.ClassNameFormat();

  //
  // https://drafts.csswg.org/cssom/#common-serializing-idioms
  //

  @Test(
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Cannot format an empty string")
  public void serializeIdentifier01() {
    format.format("");
  }

  @Test(description = "If the character is NULL (U+0000), then the REPLACEMENT CHARACTER (U+FFFD).")
  public void serializeIdentifier02() {
    serializeIdentifier("\u0000", ".\uFFFD");
  }

  @Test(description = "If the character is in the range [\1-\1f] (U+0001 to U+001F) or is U+007F, then the character escaped as code point.")
  public void serializeIdentifier03() {
    serializeIdentifier("\u0001", ".\\1 ");
    serializeIdentifier("\u007F", ".\\7f ");
  }

  @Test(description = "If the character is the first character and is in the range [0-9] (U+0030 to U+0039), then the character escaped as code point")
  public void serializeIdentifier04() {
    serializeIdentifier("0", ".\\30 ");
    serializeIdentifier("1", ".\\31 ");
    serializeIdentifier("2", ".\\32 ");
    serializeIdentifier("3", ".\\33 ");
    serializeIdentifier("4", ".\\34 ");
    serializeIdentifier("5", ".\\35 ");
    serializeIdentifier("6", ".\\36 ");
    serializeIdentifier("7", ".\\37 ");
    serializeIdentifier("8", ".\\38 ");
    serializeIdentifier("9", ".\\39 ");
  }

  @Test(description = "If the character is the second character and is in the range [0-9] (U+0030 to U+0039) and the first character is a \"-\" (U+002D), then the character escaped as code point.")
  public void serializeIdentifer05() {
    serializeIdentifier("-0", ".-\\30 ");
    serializeIdentifier("-1", ".-\\31 ");
    serializeIdentifier("-2", ".-\\32 ");
    serializeIdentifier("-3", ".-\\33 ");
    serializeIdentifier("-4", ".-\\34 ");
    serializeIdentifier("-5", ".-\\35 ");
    serializeIdentifier("-6", ".-\\36 ");
    serializeIdentifier("-7", ".-\\37 ");
    serializeIdentifier("-8", ".-\\38 ");
    serializeIdentifier("-9", ".-\\39 ");
  }

  @Test(description = "If the character is the first character and is a \"-\" (U+002D), and there is no second character, then the escaped character.")
  public void serializeIdentifer06() {
    serializeIdentifier("-", ".\\-");
  }

  @Test(description = """
  If the character is not handled by one of the above rules and is greater than or equal to U+0080,
  is "-" (U+002D) or "_" (U+005F), or is in one of the ranges [0-9] (U+0030 to U+0039),
  [A-Z] (U+0041 to U+005A), or [a-z] (U+0061 to U+007A), then the character itself.
  """)
  public void serializeIdentifer07() {
    serializeIdentifier("\u0080-_0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", ".\u0080-_0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
  }

  @Test(description = "Otherwise, the escaped character.")
  public void serializeIdentifier08() {
    serializeIdentifier("!?#$", ".\\!\\?\\#\\$");
  }

  private void serializeIdentifier(String source, String expected) {
    final ClassNameFormat format;
    format = new CssEngine2.ClassNameFormat();

    final String result;
    result = format.format(source);

    assertEquals(result, expected);
  }

}