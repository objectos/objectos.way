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
package br.com.objectos.html.writer;

import br.com.objectos.html.element.ElementName;
import br.com.objectos.html.tmpl.CompiledTemplateVisitor;
import br.com.objectos.html.tmpl.Template;
import java.io.IOException;
import java.io.UncheckedIOException;
import objectos.html.tmpl.AttributeName;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;

public class SimpleTemplateWriter implements CompiledTemplateVisitor {

  private final StringBuilder out;

  public SimpleTemplateWriter(StringBuilder out) {
    this.out = Check.notNull(out, "out == null");
  }

  public final void reset() {
    out.setLength(0);
  }

  @Override
  public final String toString() { return out.toString(); }

  @Override
  public final void visitAttribute(AttributeName name) {
    visitAttribute(name.getName());
  }

  @Override
  public final void visitAttribute(AttributeName name, String value) {
    visitAttribute(name.getName(), value);
  }

  @Override
  public final void visitAttribute(AttributeName name, UnmodifiableList<String> values) {
    visitAttribute(name.getName(), values.join(" "));
  }

  @Override
  public final void visitAttribute(String name) {
    append(' ');
    append(name);
  }

  @Override
  public final void visitAttribute(String name, String value) {
    append(' ');
    append(name);
    append('=');
    append('"');
    HtmlEscape.to(value, out);
    append('"');
  }

  @Override
  public void visitEndTag(ElementName element) {
    visitEndTag(element.getName());
  }

  @Override
  public final void visitEndTag(String name) {
    append('<');
    append('/');
    append(name);
    append('>');
  }

  @Override
  public final void visitRaw(String raw) {
    append(raw);
  }

  @Override
  public void visitStartTag(ElementName element) {
    visitStartTag(element.getName());
  }

  @Override
  public final void visitStartTag(String name) {
    append('<');
    append(name);
  }

  @Override
  public final void visitStartTagEnd() {
    append('>');
  }

  @Override
  public final void visitStartTagEndSelfClosing() {
    append('>');
  }

  @Override
  public final void visitText(String text) {
    try {
      visitText0(text);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public final void write(Template template) {
    var compiled = template.compile();

    compiled.acceptTemplateVisitor(this);
  }

  protected final void append(char c) {
    out.append(c);
  }

  protected final void append(String s) {
    out.append(s);
  }

  private void visitText0(String text) throws IOException {
    HtmlEscape.to(text, out);
  }

}
