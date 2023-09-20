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

public final class RootElementSpec {

  private final HtmlSelfGen dsl;

  private AttributeSpec attribute;

  RootElementSpec(HtmlSelfGen dsl) {
    this.dsl = dsl;
  }

  public final RootElementSpec as(String... alternatives) {
    attribute.as(alternatives);
    return this;
  }

  public final RootElementSpec attribute(String name) {
    stringKindIfNecessary();
    attribute = dsl.globalAttribute(name);
    return this;
  }

  public final RootElementSpec attributeEnd() {
    stringKindIfNecessary();
    return this;
  }

  public final RootElementSpec booleanType() {
    setKind(AttributeKind.BOOLEAN);
    return this;
  }

  public final RootElementSpec classNameType() {
    attribute.addKind(AttributeKind.STRING);
    setKind(AttributeKind.CLASS_NAME);
    return this;
  }

  public final RootElementSpec idType() {
    attribute.addKind(AttributeKind.STRING);
    setKind(AttributeKind.ID);
    return this;
  }

  private void stringKindIfNecessary() {
    if (attribute != null) {
      setKind(AttributeKind.STRING);
    }
  }

  private void setKind(AttributeKind kind) {
    attribute.addKind(kind);
    attribute = null;
  }

}