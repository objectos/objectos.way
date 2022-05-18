/*
 * Copyright (C) 2022-2022 Objectos Software LTDA.
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
package objectos.lang;

import static org.testng.Assert.assertEquals;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LineSeparatorTest implements OperatingSystemVisitor<String, String[]> {

  @Test
  public void join() {
    assertEquals(LineSeparator.join(), "");

    assertEquals(LineSeparator.join("xpto"), "xpto");

    String[] value = {"ab", "cd", "ef"};

    assertEquals(
        LineSeparator.join(value),
        OperatingSystem.get().acceptOperatingSystemVisitor(this, value)
    );

    try {
      value = null;

      LineSeparator.join(value);

      Assert.fail("expected exception was not thrown");
    } catch (NullPointerException expected) {

    }

    try {
      value = new String[] {null};

      LineSeparator.join(value);

      Assert.fail("expected exception was not thrown");
    } catch (NullPointerException expected) {

    }

    try {
      value = new String[] {"a", null, "b"};

      LineSeparator.join(value);

      Assert.fail("expected exception was not thrown");
    } catch (NullPointerException expected) {

    }
  }

  @Override
  public final String visitLinux(Linux os, String[] p) {
    StringBuilder s;
    s = new StringBuilder();

    s.append(p[0]);

    for (int i = 1; i < p.length; i++) {
      s.append('\n');

      s.append(p[i]);
    }

    return s.toString();
  }

  @Override
  public final String visitUnsupportedOs(UnsupportedOperatingSystem os, String[] p) {
    throw new UnsupportedOperationException();
  }

}