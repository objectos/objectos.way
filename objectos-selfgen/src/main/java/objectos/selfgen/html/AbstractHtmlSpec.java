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
package objectos.selfgen.html;

public abstract class AbstractHtmlSpec implements Spec {

  private SpecDsl dsl;

  protected AbstractHtmlSpec() {}

  @Override
  public final void acceptSpecDsl(SpecDsl dsl) {
    this.dsl = dsl;

    try {
      definition();

      dsl.prepare();
    } finally {
      this.dsl = null;
    }
  }

  public final SpecDsl toSpecDsl() {
    var dsl = new SpecDsl();

    acceptSpecDsl(dsl);

    return dsl;
  }

  protected final CategorySpec category(String name) {
    return dsl.category(name);
  }

  protected abstract void definition();

  protected final ElementSpec el(String name) {
    return dsl.element(name);
  }

  protected final ElementSpec element(String name) {
    return dsl.element(name);
  }

  protected final RootElementSpec rootElement() {
    return dsl.rootElement();
  }

  protected final TemplateSpec template() {
    return dsl.template();
  }

  protected final TextSpec text() {
    return dsl.text();
  }

}