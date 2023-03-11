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

import java.io.IOException;
import objectos.html.internal.TemplateDslImpl;
import objectos.html.internal.Validate;
import objectos.html.io.SimpleTemplateWriter;
import objectos.html.spi.Marker;
import objectos.html.spi.Renderer;
import objectos.lang.Check;

public abstract class HtmlTemplate extends FragmentOrTemplate implements Template {

  public interface Visitor {

    void attributeEnd() throws IOException;

    void attributeStart(String name) throws IOException;

    void attributeValue(String value) throws IOException;

    void doctype() throws IOException;

    void endTag(String name) throws IOException;

    void raw(String value) throws IOException;

    void selfClosingEnd() throws IOException;

    void startTag(String name) throws IOException;

    void startTagEnd(String name) throws IOException;

    void text(String value) throws IOException;

  }

  @Override
  public final void acceptTemplateDsl(TemplateDsl dsl) {
    this.dsl = Check.notNull(dsl, "dsl == null");

    try {
      definition();

      this.dsl.markRootElement();
    } finally {
      this.dsl = null;
    }
  }

  protected final void pathName(String path) {
    Validate.pathName(path.toString()); // path implicit null-check

    dsl().pathName(path);
  }

  @Override
  public final CompiledTemplate compile() {
    var dsl = new TemplateDslImpl();

    acceptTemplateDsl(dsl);

    return dsl.compile();
  }

  @Override
  public final void mark(Marker dsl) {
    dsl.markTemplate();
  }

  public final String minified() {
    try {
      var sink = new HtmlSink();

      var out = new StringBuilder();

      sink.appendTo(this, out);

      return out.toString();
    } catch (IOException e) {
      throw new AssertionError("java.lang.StringBuilder does not throw IOException", e);
    }
  }

  @Override
  public final String printMinified() {
    CompiledTemplate compiled;
    compiled = compile();

    StringBuilder out;
    out = new StringBuilder();

    SimpleTemplateWriter writer;
    writer = new SimpleTemplateWriter(out);

    compiled.acceptTemplateVisitor(writer);

    return out.toString();
  }

  @Override
  public final void render(Renderer renderer) {
    if (renderer instanceof TemplateDsl dsl) {
      dsl.addTemplate(this);
    }
  }

  public final void runFragment(TemplateDsl dsl) {
    this.dsl = dsl;
    try {
      definition();
    } finally {
      this.dsl = null;
    }
  }

}
