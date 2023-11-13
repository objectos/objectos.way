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

import objectos.html.Html;
import objectos.html.tmpl.Api;

public abstract class HtmlTemplateApi {

  HtmlTemplateApi() {}

  public abstract void ambiguous(Ambiguous name, String value);

  public abstract void attribute(AttributeName name);

  public abstract void attribute(AttributeName name, String value);

  public abstract void compilationBegin();

  public abstract void compilationEnd();

  public Html compile() {
    throw new UnsupportedOperationException();
  }

  public abstract void doctype();

  public abstract void elementBegin(StandardElementName name);

  public abstract void elementEnd();

  public abstract void elementValue(Api.Instruction instruction);

  public abstract void flattenBegin();

  public abstract int fragmentBegin();

  public abstract void fragmentEnd(int index);

  public void optimize() {
    throw new UnsupportedOperationException();
  }

  public abstract void raw(String value);

  public abstract void text(String value);

}