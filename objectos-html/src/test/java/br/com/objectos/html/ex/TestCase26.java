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

public class TestCase26 extends AbstractTemplate {

  @Override
  protected final void definition() {
    div(
        f(this::f0),

        main(
            article(
                f(this::f1)
            )
        ),

        f(this::f2)
    );
  }

  private void f0() {
    div(id("f0"));
  }

  private void f1() {
    div(id("f1"));
  }

  private void f2() {
    div(id("f2"));
  }

}
