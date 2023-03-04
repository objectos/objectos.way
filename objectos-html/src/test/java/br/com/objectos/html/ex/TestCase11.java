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

import objectos.html.AbstractTemplate;

/**
 * Nested fragments.
 */
public class TestCase11 extends AbstractTemplate {

  @Override
  protected final void definition() {
    html(
        body(
            f(this::body0)
        )
    );
  }
  
  private void body0() {
    header(
        f(this::hero)
    );
  }
  
  private void hero() {
    nav();
  }

}
