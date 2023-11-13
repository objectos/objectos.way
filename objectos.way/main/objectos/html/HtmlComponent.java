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
package objectos.html;

import objectos.html.internal.HtmlTemplateApi;
import objectos.lang.object.Check;

/**
 * Allow for creating <em>components</em>, objects that can render parts of a
 * larger HTML template.
 *
 * <p>
 * A component instance must be bound to a distinct template instance. This
 * template instance is called the <em>parent</em> of the component. Once
 * bound, a component can only be used to render parts for its parent.
 *
 * <p>
 * A component instance may be used to render instructions issued from its
 * parent template.
 */
public non-sealed abstract class HtmlComponent extends BaseTemplateDsl {

  private final BaseTemplateDsl parent;

  /**
   * Creates a new component bound to the specified {@code parent} template.
   *
   * @param parent
   *        the template instance for which this component will be bound to.
   */
  public HtmlComponent(BaseTemplateDsl parent) {
    this.parent = Check.notNull(parent, "parent == null");
  }

  @Override
  final HtmlTemplateApi api() {
    return parent.api();
  }

}
