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

import java.util.Objects;
import objectos.html.HtmlTemplate;
import objectos.html.tmpl.AttributeOrElement;
import objectos.html.tmpl.CustomAttributeName;
import objectos.html.tmpl.ElementName;
import objectos.html.tmpl.Lambda;
import objectos.html.tmpl.NonVoidElementValue;
import objectos.html.tmpl.StandardAttributeName;
import objectos.html.tmpl.StandardElementName;
import objectos.html.tmpl.Value;
import objectos.lang.Check;

/**
 * @since 0.5.3
 */
public abstract class InternalHtmlTemplate extends GeneratedAbstractTemplate {

  private HtmlTemplateApi api;

  public final void acceptTemplateDsl(HtmlTemplateApi api) {
    this.api = Check.notNull(api, "api == null");

    try {
      definition();
    } finally {
      this.api = null;
    }
  }

  public final void doctype() {
    api().addDoctype();
  }

  protected final AttributeOrElement addAttributeOrElement(AttributeOrElement value, String text) {
    api().addAttributeOrElement(value, text);

    return value;
  }

  @Override
  protected final <N extends StandardAttributeName> N addStandardAttribute(N name) {
    api().addAttribute(name);

    return name;
  }

  @Override
  protected final <N extends StandardAttributeName> N addStandardAttribute(N name, String value) {
    api().addAttribute(name, value);

    return name;
  }

  @Override
  protected final ElementName addStandardElement(StandardElementName element, String text) {
    api().addElement(element, text);

    return element;
  }

  @Override
  protected final ElementName addStandardElement(StandardElementName element, Value[] values) {
    Objects.requireNonNull(element, "element == null");

    api().addElement(element, values);

    return element;
  }

  protected final void addTemplate(HtmlTemplate template) {
    api().addTemplate(template);
  }

  protected final void addText(String text) {
    api().addText(text);
  }

  protected abstract void definition();

  protected final Lambda f(Lambda lambda) {
    api().addLambda(lambda);

    return lambda;
  }

  protected final void pathName(String path) {
    Validate.pathName(path.toString()); // path implicit null-check

    api().pathName(path);
  }

  protected CustomAttributeName.PathTo pathTo(String path) {
    Validate.pathName(path.toString()); // path implicit null-check

    var name = CustomAttributeName.PATH_TO;

    api().addAttribute(name, path);

    return name;
  }

  protected final NonVoidElementValue raw(String text) {
    api().addRaw(text);

    return Raw.INSTANCE;
  }

  private HtmlTemplateApi api() {
    Check.state(api != null, "api not set");

    return api;
  }

}
