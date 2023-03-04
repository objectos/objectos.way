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
package br.com.objectos.html.tmpl;

import objectos.html.tmpl.AttributeName;
import objectos.html.tmpl.ElementName;
import objectos.util.UnmodifiableList;

public class SimpleCompiledTemplateVisitor implements CompiledTemplateVisitor {

  @Override
  public void visitAttribute(AttributeName name) {}

  @Override
  public void visitAttribute(AttributeName name, UnmodifiableList<String> values) {}

  @Override
  public void visitAttribute(AttributeName name, String value) {}

  @Override
  public void visitAttribute(String name) {}

  @Override
  public void visitAttribute(String name, String value) {}

  @Override
  public void visitEndTag(ElementName name) {}

  @Override
  public void visitEndTag(String name) {}

  @Override
  public void visitRaw(String raw) {}

  @Override
  public void visitStartTag(ElementName name) {}

  @Override
  public void visitStartTag(String name) {}

  @Override
  public void visitStartTagEnd() {}

  @Override
  public void visitStartTagEndSelfClosing() {}

  @Override
  public void visitText(String text) {}

}
