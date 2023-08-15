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
package objectos.html.internal;

import objectos.html.CompiledHtml;
import objectos.html.tmpl.FragmentAction;
import objectos.html.tmpl.Instruction;
import objectos.html.tmpl.Instruction.ClipPathAttribute;
import objectos.html.tmpl.Instruction.ElementContents;
import objectos.html.tmpl.Instruction.Fragment;
import objectos.html.tmpl.StandardAttributeName;
import objectos.html.tmpl.StandardElementName;
import objectos.lang.Check;

public abstract class InternalHtmlTemplate2 extends GeneratedHtmlTemplate {

  HtmlTemplateApi2 api;

  public final CompiledHtml compile() {
    try {
      api = new HtmlCompiler02();

      api.compilationBegin();

      definition();

      api.compilationEnd();

      api.optimize();

      return api.compile();
    } finally {
      api = null;
    }
  }

  @Override
  public final String toString() {
    CompiledHtml compiled;
    compiled = compile();

    return compiled.toString();
  }

  protected final ClipPathAttribute clipPath(String value) {
    api().attribute(StandardAttributeName.CLIPPATH, value);

    return InternalInstruction.INSTANCE;
  }

  protected abstract void definition();

  protected final void doctype() {
    api().doctype();
  }

  protected final Fragment f(FragmentAction action) {
    api().fragment(action);

    return InternalFragment.INSTANCE;
  }

  protected final ElementContents t(String text) {
    api().text(text);

    return InternalInstruction.INSTANCE;
  }

  @Override
  final void ambiguous(Ambiguous name, String text) {
    api().ambiguous(name, text);
  }

  @Override
  final void attribute(StandardAttributeName name) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  final void attribute(StandardAttributeName name, String value) {
    HtmlTemplateApi2 api;
    api = api();

    api.attribute(name, value);
  }

  @Override
  final void element(StandardElementName name, Instruction[] contents) {
    HtmlTemplateApi2 api;
    api = api();

    api.elementBegin(name);

    for (int i = 0; i < contents.length; i++) {
      Instruction inst;
      inst = Check.notNull(contents[i], "contents[", i, "] == null");

      api.elementValue(inst);
    }

    api.elementEnd();
  }

  @Override
  final void element(StandardElementName name, String text) {
    HtmlTemplateApi2 api;
    api = api();

    api.text(text);

    api.elementBegin(name);
    api.elementValue(InternalInstruction.INSTANCE);
    api.elementEnd();
  }

  private HtmlTemplateApi2 api() {
    Check.state(api != null, "api not set");

    return api;
  }

}