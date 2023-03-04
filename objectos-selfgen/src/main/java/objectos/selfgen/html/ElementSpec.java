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

import java.util.Set;
import java.util.TreeSet;
import objectos.code.ClassTypeName;
import objectos.selfgen.util.JavaNames;

public final class ElementSpec
    implements
    AttributeDsl<ElementSpec>,
    Child,
    Comparable<ElementSpec>,
    Name {

  private ElementAttributeSpec attribute;

  private ContentModel childSpec = ContentModel.start();

  private final SpecDsl dsl;

  private boolean hasEndTag = true;

  private final String name;

  private final Set<ElementSpec> parentSet = new TreeSet<>();

  public final ClassTypeName className;

  public final String constantName;

  ElementSpec(SpecDsl dsl, String name) {
    this.dsl = dsl;
    this.name = name;

    className = ClassTypeName.of(ThisTemplate.spi_type, valueSimpleName());

    constantName = JavaNames.toIdentifier(name.toUpperCase());
  }

  // DSL methods

  @Override
  public final Name addParent(ElementSpec parent) {
    parentSet.add(parent);
    return this;
  }

  @Override
  public final ElementSpec as(String... alternatives) {
    attribute.as(alternatives);
    return this;
  }

  @Override
  public final ElementSpec attribute(String name) {
    stringKindIfNecessary();
    attribute = dsl.elementAttribute(this, name);
    return this;
  }

  @Override
  public final ElementSpec attributeEnd() {
    stringKindIfNecessary();
    return this;
  }

  public final ElementSpec attributes(Iterable<String> names) {
    for (String name : names) {
      attribute(name);
    }

    return this;
  }

  @Override
  public final ElementSpec booleanType() {
    setKind(AttributeKind.BOOLEAN);
    return this;
  }

  public final ElementSpec category(CategorySpec category) {
    stringKindIfNecessary();
    category.add(this);
    return this;
  }

  @Override
  public final ElementSpec classNameType() {
    throw new UnsupportedOperationException("Global attribute only");
  }

  @Override
  public final int compareTo(ElementSpec o) {
    return name.compareTo(o.name);
  }

  public final ElementSpec contentModel(CategorySpec category) {
    stringKindIfNecessary();
    childSpec = childSpec.category(category);
    return this;
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof ElementSpec)) {
      return false;
    }

    ElementSpec that = (ElementSpec) obj;
    return name.equals(that.name);
  }

  //

  public final ElementSpec except(ElementSpec element) {
    childSpec = childSpec.except(element);
    return this;
  }

  public final boolean hasBuilder() {
    return childSpec.hasBuilder();
  }

  //

  public final boolean hasChildren() {
    return childSpec.hasChildren();
  }

  public final boolean hasEndTag() {
    return hasEndTag;
  }

  @Override
  public final int hashCode() {
    return name.hashCode();
  }

  @Override
  public final ElementSpec idType() {
    throw new UnsupportedOperationException("Global attribute only");
  }

  public final String methodName() {
    return name;
  }

  @Override
  public final String name() {
    return name;
  }

  public final void noEndTag() {
    stringKindIfNecessary();
    hasEndTag = false;
  }

  public final ElementSpec one(Child el) {
    stringKindIfNecessary();
    Name name = el.addParent(this);
    childSpec = childSpec.one(name);
    return this;
  }

  public final Iterable<ElementSpec> parentStream() {
    return parentSet;
  }

  public final void prepare() {
    childSpec.prepare(this);
  }

  public final ElementSpec zeroOrMore(Child el) {
    stringKindIfNecessary();

    Name name = el.addParent(this);

    childSpec = childSpec.zeroOrMore(name);

    return this;
  }

  private void setKind(AttributeKind kind) {
    attribute.addKind(kind);
    attribute = null;
  }

  private String simpleName() {
    return JavaNames.toValidClassName(name);
  }

  private void stringKindIfNecessary() {
    if (attribute != null) {
      setKind(AttributeKind.STRING);
    }
  }

  private String valueSimpleName() {
    return simpleName() + "Value";
  }

}