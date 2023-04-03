/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.asciidoc;

import static org.testng.Assert.assertEquals;

import java.util.Map;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DocumentTest extends AsciiDocTest {

  @BeforeClass
  @Override
  public void _beforeClass() {
    super._beforeClass();
  }

  @Test(enabled = false)
  public void _enableCodeMinings() {
  }

  @Override
  final void test(
      String source,
      int[] p0,
      int[] p1, Map<String, String> docAttr,
      int[][] p2,
      String expectedHtml) {
    var document = asciiDoc.parse(source);

    for (var entry : docAttr.entrySet()) {
      var key = entry.getKey();
      var expected = entry.getValue();

      var actual = document.getAttribute(key, "");

      assertEquals(actual, expected, "key=" + key);
    }

    document.process(processor);

    var result = processor.toString();

    testHtml(result, expectedHtml);
  }

}