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

import java.util.Objects;
import objectos.css.tmpl.AttributeValueElement;
import objectos.css.tmpl.StyleRuleElement;
import objectos.lang.Check;

public abstract class InternalCssTemplate extends GeneratedCssTemplate {

  private CssTemplateApi api;

  protected abstract void definition();

  protected final StyleRuleElement attr(String name) {
    checkName(name);

    var api = api();
    return api.addInternal(ByteProto.ATTR_NAME_SELECTOR, api.addObject(name));
  }

  protected final StyleRuleElement attr(
      String name, AttributeValueElement element) {
    checkName(name);
    Objects.requireNonNull(element, "element == null");

    var api = api();
    return api.addAttribute(api.addObject(name), element);
  }

  protected final AttributeValueElement eq(String value) {
    return attrValue(AttributeValueOperator.EQUALS, value);
  }

  protected final AttributeValueElement startsWith(String value) {
    return attrValue(AttributeValueOperator.STARTS_WITH, value);
  }

  private AttributeValueElement attrValue(
      AttributeValueOperator operator, String value) {
    checkValue(value);

    var api = api();
    return api.addInternal(
      ByteProto.ATTR_VALUE_ELEMENT, operator.ordinal(), api.addObject(value));
  }

  private void checkName(String name) {
    Objects.requireNonNull(name, "name == null");
    Check.argument(!name.isBlank(), "name must not be blank");
  }

  private void checkValue(String value) {
    Objects.requireNonNull(value, "value == null");
    Check.argument(!value.isBlank(), "value must not be blank");
  }

  protected final StyleRuleElement className(String name) {
    checkName(name);

    var api = api();
    return api.addInternal(ByteProto.CLASS_SELECTOR, api.addObject(name));
  }

  protected final StyleRuleElement id(String id) {
    var api = api();
    return api.addInternal(ByteProto.ID_SELECTOR, api.addObject(id));
  }

  protected final void style(StyleRuleElement... elements) {
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