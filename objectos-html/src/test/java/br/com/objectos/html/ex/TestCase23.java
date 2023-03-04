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
package br.com.objectos.html.ex;

import br.com.objectos.html.element.ElementName;
import br.com.objectos.html.tmpl.AbstractTemplate;

public class TestCase23 extends AbstractTemplate {

  private final int count;

  public TestCase23(int count) {
    this.count = count;
  }

  @Override
  protected final void definition() {
    ElementName[] children = new ElementName[count];
    for (int i = 0; i < count; i++) {
      children[i] = li(Integer.toString(i));
    }
    ul(children);
  }

}
