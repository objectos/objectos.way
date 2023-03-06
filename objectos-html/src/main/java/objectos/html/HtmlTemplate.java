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

import br.com.objectos.html.writer.SimpleTemplateWriter;
import java.io.IOException;
import objectos.html.internal.TemplateDslImpl;
import objectos.html.spi.Marker;
import objectos.html.spi.Renderer;
import objectos.html.tmpl.ElementName;
import objectos.lang.Check;

public abstract class HtmlTemplate extends FragmentOrTemplate implements Template {

  public interface Visitor {

    void startTag(ElementName name) throws IOException;

    void startTagEnd(ElementName name) throws IOException;

    void endTag(ElementName name) throws IOException;

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
    if (renderer instanceof TemplateDsl) {
      acceptTemplateDsl((TemplateDsl) renderer);
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
