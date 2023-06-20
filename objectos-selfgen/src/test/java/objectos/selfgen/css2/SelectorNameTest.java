/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.selfgen.css2;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class SelectorNameTest {

  @Test
  public void ofTypeSelector() {
    var sel = SelectorName.of("a");

    assertEquals(sel.fieldName, "a");
    assertEquals(sel.selectorName, "a");
  }

  @Test
  public void ofPseudoClassSelector() {
    var sel = SelectorName.of(":-moz-focusring");

    assertEquals(sel.fieldName, "_mozFocusring");
    assertEquals(sel.selectorName, ":-moz-focusring");
  }

  @Test
  public void ofPseudoElementSelector() {
    var sel = SelectorName.of("::after");

    assertEquals(sel.fieldName, "__after");
    assertEquals(sel.selectorName, "::after");
  }

  @Test
  public void ofPseudoElementSelectorWebkit() {
    var sel = SelectorName.of("::-webkit-file-upload-button");

    assertEquals(sel.fieldName, "__webkitFileUploadButton");
    assertEquals(sel.selectorName, "::-webkit-file-upload-button");
  }

}