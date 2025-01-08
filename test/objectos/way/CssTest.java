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

public class CssTest {

  @Test
  public void writeClassName() {
    assertEquals(writeClassName("foo"), ".foo");
    assertEquals(writeClassName("after:block"), ".after\\:block");
    assertEquals(
        writeClassName("focus:shadow-[inset_0_0_0_1px_var(--cds-focus),inset_0_0_0_2px_var(--cds-background)]"),
        ".focus\\:shadow-\\[inset_0_0_0_1px_var\\(--cds-focus\\)\\2c inset_0_0_0_2px_var\\(--cds-background\\)\\]"
    );
    assertEquals(
        writeClassName("md:max-w-[60%]"),
        ".md\\:max-w-\\[60\\%\\]"
    );
    assertEquals(
        writeClassName("grid-template:'a_c'_'b_b'"),
        ".grid-template\\:\\'a_c\\'_\\'b_b\\'"
    );
  }

  private String writeClassName(String className) {
    StringBuilder out;
    out = new StringBuilder();

    Css.writeClassName(out, className);

    return out.toString();
  }

}