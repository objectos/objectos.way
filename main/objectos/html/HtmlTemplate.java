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
package objectos.html;

import objectos.html.pseudom.DocumentProcessor;
import objectos.html.pseudom.HtmlDocument;
import objectos.lang.object.Check;

/**
 * A template in pure Java for generating HTML.
 *
 * <p>
 * This class provides methods for representing HTML code in a Java class. An
 * instance of the class can then be used to generate the represented HTML code.
 *
 * @see objectos.html
 */
public non-sealed abstract class HtmlTemplate extends TemplateBase {

  Html html;

  /**
   * Sole constructor.
   */
  protected HtmlTemplate() {}

  /**
   * Returns the HTML generated by this template.
   *
   * @return the HTML generated by this template
   */
  @Override
  public final String toString() {
    Html compiler;
    compiler = new Html();

    StringBuilder out;
    out = new StringBuilder();

    PrettyPrintWriter writer;
    writer = new PrettyPrintWriter();

    writer.out = out;

    process(compiler, writer);

    return out.toString();
  }

  /**
   * Defines the HTML code to be generated by this template.
   */
  protected abstract void definition();

  @Override
  final Html $html() {
    Check.state(html != null, "html not set");

    return html;
  }

  final void process(Html templateApi, DocumentProcessor processor) {
    try {
      html = templateApi;

      html.compilationBegin();

      definition();

      html.compilationEnd();

      HtmlDocument document;
      document = html.compile();

      processor.process(document);
    } finally {
      html = null;
    }
  }

}
