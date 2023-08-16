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

import objectos.html.HtmlTemplate;
import objectos.html.tmpl.FragmentAction;
import objectos.html.tmpl.Instruction;
import objectos.html.tmpl.Instruction.ClipPathAttribute;
import objectos.html.tmpl.Instruction.ElementContents;
import objectos.html.tmpl.Instruction.Fragment;
import objectos.html.tmpl.StandardAttributeName;
import objectos.html.tmpl.StandardElementName;
import objectos.lang.Check;

abstract class CommonHtml extends GeneratedHtmlTemplate {

  protected final Fragment add(HtmlTemplate template) {
    Check.notNull(template, "template == null");

    InternalHtmlTemplate internal;
    internal = template;

    try {
      internal.api = api();

      internal.definition();
    } finally {
      internal.api = null;
    }

    return InternalFragment.INSTANCE;
  }

  protected final ClipPathAttribute clipPath(String value) {
    api().attribute(StandardAttributeName.CLIPPATH, value);

    return InternalInstruction.INSTANCE;
  }

  protected final void doctype() {
    api().doctype();
  }

  protected final Fragment f(FragmentAction action) {
    Check.notNull(action, "action == null");

    api().fragment(action);

    return InternalFragment.INSTANCE;
  }

  protected final Instruction.NoOp noop() {
    return InternalNoOp.INSTANCE;
  }

  protected final ElementContents raw(String text) {
    Check.notNull(text, "text == null");

    api().raw(text);

    return InternalInstruction.INSTANCE;
  }

  protected final ElementContents t(String text) {
    Check.notNull(text, "text == null");

    api().text(text);

    return InternalInstruction.INSTANCE;
  }

  @Override
  final void ambiguous(Ambiguous name, String text) {
    api().ambiguous(name, text);
  }

  @Override
  final void attribute(StandardAttributeName name) {
    api().attribute(name);
  }

  @Override
  final void attribute(StandardAttributeName name, String value) {
    HtmlTemplateApi api;
    api = api();

    api.attribute(name, value);
  }

  @Override
  final void element(StandardElementName name, Instruction[] contents) {
    HtmlTemplateApi api;
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
    HtmlTemplateApi api;
    api = api();

    api.text(text);

    api.elementBegin(name);
    api.elementValue(InternalInstruction.INSTANCE);
    api.elementEnd();
  }

  abstract HtmlTemplateApi api();

}