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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HeaderParamNameValidatorTest {

  @Test
  public void valid() {
    final String name;
    name = Rfc.tchar();

    final HeaderParamNameValidator subject;
    subject = new HeaderParamNameValidator(name);

    subject.validate();
  }

  public record Invalid(String name, String msg) {}

  @DataProvider
  public Iterator<Invalid> invalidProvider() {
    final List<Invalid> list;
    list = new ArrayList<>();

    final String tchar;
    tchar = Rfc.tchar();

    final char[] validChars;
    validChars = tchar.toCharArray();

    Arrays.sort(validChars);

    for (char c = 0; c < 256; c++) {
      final int search;
      search = Arrays.binarySearch(validChars, c);

      if (search >= 0) {
        continue;
      }

      final String name;
      name = "foo" + c;

      final String msg;
      msg = "Invalid parameter name: character '%c' at index 3 is not allowed".formatted(c);

      final Invalid invalid;
      invalid = new Invalid(name, msg);

      list.add(invalid);
    }

    return list.iterator();
  }

  @Test(dataProvider = "invalidProvider")
  public void invalid(Invalid invalid) {
    final String name;
    name = invalid.name;

    final HeaderParamNameValidator subject;
    subject = new HeaderParamNameValidator(name);

    try {
      subject.validate();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), invalid.msg);
    }
  }

}
