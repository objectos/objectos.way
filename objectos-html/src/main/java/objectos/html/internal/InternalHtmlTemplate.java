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

import objectos.html.HtmlSink;
import objectos.html.HtmlTemplate;
import objectos.html.tmpl.CustomAttributeName;
import objectos.html.tmpl.FragmentAction;
import objectos.html.tmpl.Instruction;
import objectos.html.tmpl.Instruction.AnchorInstruction;
import objectos.html.tmpl.Instruction.ClipPathAttribute;
import objectos.html.tmpl.Instruction.ElementContents;
import objectos.html.tmpl.Instruction.Fragment;
import objectos.html.tmpl.StandardAttributeName;
import objectos.html.tmpl.StandardElementName;
import objectos.lang.Check;

/**
 * @since 0.5.3
 */
public abstract class InternalHtmlTemplate extends GeneratedHtmlTemplate {

  private HtmlTemplateApi api;

  public final void doctype() {
    api().addDoctype();
  }

  @Override
  public final String toString() {
    var sink = new HtmlSink();

    var out = new StringBuilder();

    sink.toStringBuilder((HtmlTemplate) this, out);

    return out.toString();
  }

  protected final void add(HtmlTemplate template) {
    Check.notNull(template, "template == null");

    api().addTemplate(template);
  }

  protected final void addTemplate(HtmlTemplate template) {
    api().addTemplate(template);
  }

  protected final void addText(String text) {
    api().addText(text);
  }

  protected final ClipPathAttribute clipPath(String value) {
    api().addAttribute(StandardAttributeName.CLIPPATH, value);

    return InternalInstruction.INSTANCE;
  }

  protected abstract void definition();

  @Override
  protected final void element(StandardElementName name, Instruction[] contents) {
    api().addElement(name, contents);
  }

  @Deprecated
  protected final ElementContents elementContents() {
    return InternalInstruction.INSTANCE;
  }

  protected final Fragment f(FragmentAction action) {
    api().addFragment(action);

    return InternalFragment.INSTANCE;
  }

  protected final Instruction.NoOp noop() {
    return InternalNoOp.INSTANCE;
  }

  protected final void pathName(String path) {
    Validate.pathName(path.toString()); // path implicit null-check

    api().pathName(path);
  }

  protected AnchorInstruction pathTo(String path) {
    Validate.pathName(path.toString()); // path implicit null-check

    var name = CustomAttributeName.PATH_TO;

    api().addAttribute(name, path);

    return InternalInstruction.INSTANCE;
  }

  protected final ElementContents raw(String text) {
    api().addRaw(text);

    return InternalInstruction.INSTANCE;
  }

  protected final ElementContents t(String text) {
    api().addText(text);

    return InternalInstruction.INSTANCE;
  }

  final void acceptTemplateDsl(HtmlTemplateApi api) {
    this.api = Check.notNull(api, "api == null");

    try {
      definition();
    } finally {
      this.api = null;
    }
  }

  @Override
  final void ambiguous(Ambiguous name, String text) {
    api().addAmbiguous(name, text);
  }

  @Override
  final void attribute(StandardAttributeName name) {
    api().addAttribute(name);
  }

  @Override
  final void attribute(StandardAttributeName name, String value) {
    api().addAttribute(name, value);
  }

  @Override
  final void element(StandardElementName name, String text) {
    api().addElement(name, text);
  }

  private HtmlTemplateApi api() {
    Check.state(api != null, "api not set");

    return api;
  }

}
