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

import org.testng.annotations.Test;

public class CssModifierTest {

  @Test
  public void writeClassName01() {
    assertEquals(writeClassName("foo"), ".foo");
  }

  @Test
  public void writeClassName02() {
    assertEquals(writeClassName("after:block"), ".after\\:block");
  }

  @Test
  public void writeClassName03() {
    assertEquals(
        writeClassName("focus:shadow-[inset_0_0_0_1px_var(--cds-focus),inset_0_0_0_2px_var(--cds-background)]"),
        ".focus\\:shadow-\\[inset_0_0_0_1px_var\\(--cds-focus\\)\\,inset_0_0_0_2px_var\\(--cds-background\\)\\]"
    );
  }

  @Test
  public void writeClassName04() {
    assertEquals(
        writeClassName("grid-template:'a_c'_'b_b'"),
        ".grid-template\\:\\'a_c\\'_\\'b_b\\'"
    );
  }

  @Test
  public void writeClassName05() {
    assertEquals(
        writeClassName("grid-template:'a_c'_'b_b'"),
        ".grid-template\\:\\'a_c\\'_\\'b_b\\'"
    );
  }

  private String writeClassName(String className) {
    StringBuilder out;
    out = new StringBuilder();

    CssModifier.EMPTY_MODIFIER.writeClassName(out, className);

    return out.toString();
  }

}