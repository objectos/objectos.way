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

import java.io.IOException;
import org.jsoup.Jsoup;
import org.testng.annotations.BeforeClass;

public abstract class AsciiDocTest2 {

  AsciiDoc2 asciiDoc;

  ThisDocumentProcessor processor;

  @BeforeClass
  public void _beforeClass() {
    if (asciiDoc == null) {
      asciiDoc = new AsciiDoc2();

      processor = new ThisDocumentProcessor();
    }
  }

  void test(String source, String expectedHtml) {
    try {
      asciiDoc.toProcessor(source, processor);

      var result = processor.toString();

      testHtml(result, expectedHtml);
    } catch (IOException e) {
      throw new AssertionError(
        "ThisDocumentProcessor does not throw IOException"
      );
    }
  }

  final void testHtml(String result, String expected) {
    assertEquals(normalize(result), normalize(expected));
  }

  private String normalize(String html) {
    var fragment = Jsoup.parseBodyFragment(html);

    var body = fragment.body();

    return body.toString();
  }

}