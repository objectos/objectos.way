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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HeaderValueValidatorTest {

  public record Valid(String value, String description) {}

  @DataProvider
  public Iterator<Valid> validProvider() {
    final List<Valid> list;
    list = new ArrayList<>();

    list.add(new Valid("", "Empty string is valid"));

    final String validChars;
    validChars = Rfc.vchar();

    for (int idx = 0, len = validChars.length(); idx < len; idx++) {
      final char c;
      c = validChars.charAt(idx);

      final String s;
      s = Character.toString(c);

      list.add(new Valid(s, "Character: " + c));
    }

    list.add(new Valid("x y", "SPACE allowed if not at the beginning/end"));
    list.add(new Valid("x\ty", "TAB allowed if not at the beginning/end"));

    return list.iterator();
  }

  @Test(dataProvider = "validProvider")
  public void valid(Valid valid) {
    final String value;
    value = valid.value;

    final HeaderValueValidator subject;
    subject = new HeaderValueValidator(value);

    subject.validate();
  }

  public record Invalid(String value, String expected, String description) {}

  @DataProvider
  public Iterator<Invalid> invalidProvider() {
    final List<Invalid> list;
    list = new ArrayList<>();

    final boolean[] valid;
    valid = new boolean[256];

    final String validChars;
    validChars = Rfc.vchar();

    for (int idx = 0, len = validChars.length(); idx < len; idx++) {
      final char validChar;
      validChar = validChars.charAt(idx);

      valid[validChar] = true;
    }

    // we'll handle SPACE and HTAB later
    valid[' '] = true;
    valid['\t'] = true;

    for (int c = 0; c < 0xFF; c++) {
      if (!valid[c]) {
        final String value;
        value = Character.toString(c);

        final String expected;
        expected = "Invalid header value: character '%s' at index 0 is not allowed".formatted(value);

        final String description;
        description = "Invalid character " + value;

        final Invalid data;
        data = new Invalid(value, expected, description);

        list.add(data);
      }
    }

    list.add(new Invalid(" abc", "Invalid header value: leading SPACE or HTAB characters are not allowed", ""));
    list.add(new Invalid("\tabc", "Invalid header value: leading SPACE or HTAB characters are not allowed", ""));
    list.add(new Invalid("abc ", "Invalid header value: trailing SPACE or HTAB characters are not allowed", ""));
    list.add(new Invalid("abc\t", "Invalid header value: trailing SPACE or HTAB characters are not allowed", ""));

    return list.iterator();
  }

  @Test(dataProvider = "invalidProvider")
  public void invalid(Invalid invalid) {
    final String value;
    value = invalid.value;

    final HeaderValueValidator subject;
    subject = new HeaderValueValidator(value);

    try {
      subject.validate();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(invalid.expected, msg);
    }
  }

}
