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

import java.util.Arrays;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Factory;

public class AsciiDocTest extends AbstractAsciiDocTest.Delegate {

  AsciiDoc asciiDoc;

  ThisProcessor processor;

  @Override
  @BeforeClass
  public void _beforeClass() {
    if (asciiDoc == null) {
      asciiDoc = AsciiDoc.create();

      processor = new ThisProcessor();
    }
  }

  @Factory
  public Object[] _factory() {
    return new Object[] {
        new AttributeListTest(this),
        new ConstrainedBoldTest(this),
        new ConstrainedItalicTest(this),
        new DocumentAttributeTest(this),
        new InlineMacroTest(this),
        new LexerTest(this),
        new ListingBlockTest(this),
        new PreambleTest(this),
        new SourceCodeBlockTest(this),
        new UnorderedListTestToRemove(this)
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
    asciiDoc.process(source, processor);

    var result = processor.toString();

    testHtml(result, expectedHtml);

    for (var entry : docAttr.entrySet()) {
      var key = entry.getKey();
      var expected = entry.getValue();

      var actual = processor.attribute(key);

      assertEquals(actual, expected, "key=" + key);
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