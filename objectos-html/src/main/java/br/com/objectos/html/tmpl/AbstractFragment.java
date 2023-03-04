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

import objectos.html.spi.Marker;
import objectos.html.spi.Renderer;
import objectos.html.tmpl.NonVoidElementValue;
import objectos.lang.Check;

public abstract class AbstractFragment extends FragmentOrTemplate implements NonVoidElementValue {

  @Override
  public final void acceptTemplateDsl(TemplateDsl dsl) {
    this.dsl = Check.notNull(dsl, "dsl == null");

    try {
      definition();
    } finally {
      this.dsl = null;
    }
  }

  @Override
  public final CompiledTemplate compile() {
    var dsl = new TemplateDslImpl();

    acceptTemplateDsl(dsl);

    dsl.markRootElement();

    return dsl.compile();
  }

  @Override
  public final void mark(Marker marker) {
    marker.markLambda();
  }

  @Override
  public final void render(Renderer renderer) {
    // noop
  }

  @Override
  protected abstract void definition();

}