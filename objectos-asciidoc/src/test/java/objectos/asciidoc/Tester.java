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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.ast.ContentNode;
import org.asciidoctor.extension.InlineMacroProcessor;
import org.asciidoctor.extension.Name;

abstract class Tester {

  private static class Doctor extends Tester {

    @Name("i")
    private static final class IInlineMacro extends InlineMacroProcessor {
      @Override
      public final Object process(
          ContentNode parent, String target, Map<String, Object> attributes) {
        var href = target;

        var text = (String) attributes.get("1");

        var options = new HashMap<String, Object>();
        options.put("type", ":link");
        options.put("target", href);

        return createPhraseNode(parent, "anchor", text, attributes, options);
      }
    }

    public static final Doctor INSTANCE = new Doctor();

    private final Asciidoctor asciidoctor;

    private final Options options;

    private Doctor() {
      asciidoctor = Asciidoctor.Factory.create();

      var registry = asciidoctor.javaExtensionRegistry();

      registry.inlineMacro(new IInlineMacro());

      options = Options.builder()
          .headerFooter(true)
          .build();
    }

    @Override
    public final void test(String source, String expectedHtml) {
      var result = asciidoctor.convert(source, options);

      var lines = result.lines()
          .skip(12) // skip to first body child
          .collect(Collectors.toUnmodifiableList());

      int size = lines.size();

      if (size < 8) {
        throw new AssertionError("size < 8");
      }

      result = lines.subList(0, size - 7)
          .stream()
          .collect(Collectors.joining("\n", "", "\n"));

      testHtml(result, expectedHtml);
    }

  }

  private static class Objectos extends Tester {

    public static final Objectos INSTANCE = new Objectos();

    private final AsciiDoc2 asciiDoc = new AsciiDoc2();

    private final ThisDocumentProcessor processor = new ThisDocumentProcessor();

    @Override
    public final void test(String source, String expectedHtml) {
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

  }

  public static Tester doctor() {
    return Doctor.INSTANCE;
  }

  public static Tester objectos() {
    return Objectos.INSTANCE;
  }

  public abstract void test(String source, String expectedHtml);

  final void testHtml(String result, String expected) {
    assertEquals(normalize(result), normalize(expected));
  }

  private String normalize(String html) {
    return html;
  }

}