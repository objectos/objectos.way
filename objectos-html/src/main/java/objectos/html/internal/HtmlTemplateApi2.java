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
import objectos.html.tmpl.StandardAttributeName;
import objectos.html.tmpl.StandardElementName;

/**
 * TODO
 *
 * @since 0.5.3
 */
abstract class HtmlTemplateApi2 {

  HtmlTemplateApi2() {}

  public abstract void ambiguous(Ambiguous name, String value);

  public abstract void attribute(StandardAttributeName name, String value);

  public abstract void compilationBegin();

  public abstract void compilationEnd();

  public CompiledHtml compile() {
    throw new UnsupportedOperationException();
  }

  public abstract void doctype();

  public abstract void elementBegin(StandardElementName name);

  public abstract void elementEnd();

  public abstract void elementValue(Instruction instruction);

  public abstract void fragment(FragmentAction action);

  public void optimize() {
    throw new UnsupportedOperationException();
  }

  public abstract void text(String value);

}