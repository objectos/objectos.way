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

import java.util.HashMap;
import java.util.Map;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.ast.ContentNode;
import org.asciidoctor.extension.InlineMacroProcessor;
import org.asciidoctor.extension.Name;
import org.jsoup.Jsoup;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AsciidoctorTest extends AsciiDocTest {

  @Name("i")
  static final class IInlineMacro extends InlineMacroProcessor {
    @Override
    public final Object process(ContentNode parent, String target, Map<String, Object> attributes) {
      var href = target;

      var text = (String) attributes.get("1");

      var options = new HashMap<String, Object>();
      options.put("type", ":link");
      options.put("target", href);

      return createPhraseNode(parent, "anchor", text, attributes, options);
    }
  }

  private Asciidoctor asciidoctor;

  private Options options;

  @BeforeClass
  @Override
  public void _beforeClass() {
    asciidoctor = Asciidoctor.Factory.create();

    var registry = asciidoctor.javaExtensionRegistry();

    registry.inlineMacro(new IInlineMacro());

    options = Options.builder()
        .headerFooter(true)
        .build();
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
    var html = asciidoctor.convert(source, options);

    var document = Jsoup.parse(html);

    var body = document.body();

    body.removeAttr("class");

    var footer = body.getElementById("footer");

    footer.remove();

    var result = body.toString();

    testHtml(result, expectedHtml);
  }

}