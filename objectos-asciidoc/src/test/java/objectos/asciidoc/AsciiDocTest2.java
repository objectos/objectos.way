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
import java.util.Arrays;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class AsciiDocTest2 extends AbstractAsciiDocTest.Delegate {

  AsciiDoc2 asciiDoc;

  ThisDocumentProcessor processor;

  @Override
  @BeforeClass
  public void _beforeClass() {
    if (asciiDoc == null) {
      asciiDoc = new AsciiDoc2();

      processor = new ThisDocumentProcessor();
    }
  }

  @Test(enabled = false)
  public void _enableCodeMinings() {
  }

  @Factory
  public Object[] _factory() {
    return new Object[] {
        new DocumentTitleTest(this)
    };
  }

  final String normalize(String html) {
    Document fragment = Jsoup.parseBodyFragment(html);

    Element body = fragment.body();

    return body.toString();
  }

  @Override
  void test(
      String source,
      int[] p0,
      int[] p1, Map<String, String> docAttr,
      int[][] p2,
      String expectedHtml) {
    try {
      asciiDoc.toProcessor(source, processor);

      var result = processor.toString();

      testHtml(result, expectedHtml);
    } catch (IOException e) {
      throw new AssertionError(
        "StringReader should not throw IOException"
      );
    }
  }

  final void testArrays(int[] result, int[] expected, String header) {
    var msg = """

    %s
    actual  =%s
    expected=%s

    """.formatted(header, Arrays.toString(result), Arrays.toString(expected));

    assertEquals(result, expected, msg);
  }

  final void testHtml(String result, String expected) {
    assertEquals(normalize(result), normalize(expected));
  }

}