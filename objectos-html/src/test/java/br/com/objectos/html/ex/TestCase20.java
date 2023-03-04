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

import br.com.objectos.html.tmpl.AbstractTemplate;
import br.com.objectos.html.tmpl.Template;

public class TestCase20 extends AbstractTemplate {

  private final Template nav = new AbstractTemplate() {
    @Override
    protected final void definition() {
      nav(
          a("o7html")
      );
    }
  };

  private final Template hero = new AbstractTemplate() {
    @Override
    protected final void definition() {
      section(
          p("is cool")
      );
    }
  };

  @Override
  protected final void definition() {
    body(
        nav,
        hero
    );
  }

}
