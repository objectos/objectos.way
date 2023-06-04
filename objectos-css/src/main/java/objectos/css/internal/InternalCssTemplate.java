/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css.internal;

import objectos.css.tmpl.Instruction;
import objectos.css.tmpl.Instruction.ExternalSelector;
import objectos.css.tmpl.TypeSelector;
import objectos.lang.Check;

public abstract class InternalCssTemplate {

  protected static final ExternalSelector A = TypeSelector.A;

  protected static final ExternalSelector BODY = TypeSelector.BODY;

  private CssTemplateApi api;

  protected abstract void definition();

  protected final Instruction.InternalSelector className(String name) {
    var api = api();
    return api.addInternal(ByteProto.CLASS_SELECTOR, api.addObject(name));
  }

  protected final Instruction.InternalSelector id(String id) {
    var api = api();
    return api.addInternal(ByteProto.ID_SELECTOR, api.addObject(id));
  }

  protected final void style(Instruction... elements) {
    api().addRule(elements);
  }

  final void acceptTemplateApi(CssTemplateApi api) {
    this.api = Check.notNull(api, "api == null");

    try {
      definition();
    } finally {
      this.api = null;
    }
  }

  private CssTemplateApi api() {
    Check.state(api != null, "api not set");

    return api;
  }

}