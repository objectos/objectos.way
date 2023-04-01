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

import objectos.html.internal.HtmlTemplateApi;
import objectos.html.internal.InternalHtmlTemplate;
import objectos.html.spi.Marker;
import objectos.html.spi.Renderer;
import objectos.html.tmpl.Instruction.ElementContents;
import objectos.html.tmpl.NonVoidElementValue;

/**
 * TODO
 *
 * @since 0.5.0
 */
public non-sealed abstract class HtmlTemplate
    extends InternalHtmlTemplate implements ElementContents, NonVoidElementValue {

  @Override
  public final void mark(Marker dsl) {}

  @Override
  public final void render(Renderer renderer) {
    if (renderer instanceof HtmlTemplateApi dsl) {
      addTemplate(this);
    }
  }

  @Override
  public final String toString() {
    var sink = new HtmlSink();

    var out = new StringBuilder();

    sink.toStringBuilder(this, out);

    return out.toString();
  }

  @Override
  protected abstract void definition();

}
