/*
 * Copyright (C) 2022 Objectos Software LTDA.
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
import static org.testng.Assert.assertTrue;

import java.util.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ThrowablesTest {

  @Test
  public void getStackTraceAsString() {
    try {
      Thrower.throwNoSuchElementException("does.not.exist");

      Assert.fail();
    } catch (NoSuchElementException expected) {
      String result;
      result = Throwables.printStackTraceToString(expected);

      String[] lines;
      lines = result.split("\n");

      assertEquals(lines[0], "java.util.NoSuchElementException: does.not.exist");

      assertTrue(
        lines[1].contains("objectos.lang.Thrower.throwNoSuchElementException"));

      assertTrue(
        lines[2].contains("objectos.lang.ThrowablesTest.getStackTraceAsString"));

      assertTrue(lines.length > 3);
    }
  }

}