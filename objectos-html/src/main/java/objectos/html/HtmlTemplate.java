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

import objectos.html.internal.TemplateDslImpl;
import objectos.html.internal.Validate;
import objectos.html.io.SimpleTemplateWriter;
import objectos.html.spi.Marker;
import objectos.html.spi.Renderer;
import objectos.html.tmpl.StandardAttributeName;
import objectos.html.tmpl.StandardElementName;
import objectos.lang.Check;

public abstract class HtmlTemplate extends FragmentOrTemplate implements Template {

  public interface Visitor {

    void attributeEnd();

    void attributeStart(StandardAttributeName name);

    void attributeValue(String value);

    void doctype();

    /**
     * Invoked after the visiting ends.
     *
     * @since 0.5.1
     */
    void documentEnd();

    /**
     * Invoked before the visiting starts.
     *
     * @since 0.5.1
     */
    void documentStart();

    void endTag(StandardElementName name);

    void raw(String value);

    void selfClosingEnd();

    void startTag(StandardElementName name);

    void startTagEnd();

    void text(String value);

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
    var sink = new HtmlSink();

    sink.minified();

    var out = new StringBuilder();

    sink.toStringBuilder(this, out);

    return out.toString();
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

  protected final void pathName(String path) {
    Validate.pathName(path.toString()); // path implicit null-check

    dsl().pathName(path);
  }

}
