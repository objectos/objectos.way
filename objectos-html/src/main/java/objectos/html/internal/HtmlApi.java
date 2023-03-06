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

import java.io.IOException;
import objectos.html.AttributeOrElement;
import objectos.html.HtmlFragment;
import objectos.html.HtmlSink;
import objectos.html.HtmlTemplate;
import objectos.html.Lambda;
import objectos.html.TemplateDsl;
import objectos.html.tmpl.AttributeName;
import objectos.html.tmpl.ElementName;
import objectos.html.tmpl.Value;

public abstract sealed class HtmlApi implements TemplateDsl permits HtmlSink {

  protected HtmlTemplate.Visitor visitor;

  @Override
  public void addAttribute(AttributeName name) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void addAttribute(AttributeName name, String value) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void addAttribute(String name, String value) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void addAttributeOrElement(AttributeOrElement value, String text) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void addDoctype() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void addElement(ElementName name, String text) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final void addElement(ElementName name, Value... values) {
    try {
      visitor.startTag(name);
      visitor.startTagEnd(name);
      visitor.endTag(name);
    } catch (IOException e) {
      throw new InternalIOException(e);
    }
  }

  @Override
  public void addFragment(HtmlFragment fragment) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void addLambda(Lambda lambda) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void addRaw(String text) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void addText(String text) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void markAttribute() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void markAttributeOrElement() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void markElement() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void markLambda() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void markRaw() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final void markRootElement() {

  }

  @Override
  public void markTemplate() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void markText() {
    throw new UnsupportedOperationException("Implement me");
  }

}