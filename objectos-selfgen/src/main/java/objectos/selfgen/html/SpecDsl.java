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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import objectos.code.JavaSink;

public class SpecDsl {

  private List<AttributeSpec> attributes;

  private final Map<String, AttributeSpec> attributeMap = new TreeMap<>();
  private final Map<String, CategorySpec> categoryMap = new TreeMap<>();
  private final Map<String, ElementSpec> elementMap = new TreeMap<>();

  private final TemplateSpec template;
  private final TextSpec text;

  public SpecDsl() {
    template = new TemplateSpec(this);
    text = new TextSpec(this);
  }

  public final void prepare() {
    for (ElementSpec element : elementMap.values()) {
      element.prepare();
    }
  }

  public final SpecDsl with(Spec spec) {
    spec.acceptSpecDsl(this);
    return this;
  }

  final List<AttributeSpec> attributes() {
    if (attributes == null) {
      attributes = List.copyOf(attributeMap.values());
    }

    return attributes;
  }

  final AttributeSpec attributeSpec(String name) {
    return attributeMap.get(name);
  }

  final CategorySpec category(String name) {
    return categoryMap.computeIfAbsent(name, this::category0);
  }

  final ElementSpec element(String name) {
    return elementMap.computeIfAbsent(name, this::element0);
  }

  final ElementAttributeSpec elementAttribute(ElementSpec parent, String name) {
    AttributeSpec attr = attributeMap.computeIfAbsent(name, this::elementAttribute0);
    return attr.toElementAttributeSpec(parent);
  }

  final Iterable<ElementSpec> elements() {
    return elementMap.values();
  }

  final AttributeSpec globalAttribute(String name) {
    if (attributeMap.containsKey(name)) {
      throw new IllegalArgumentException(name + " global attribute already defined!");
    }
    AttributeSpec attr = AttributeSpec.global(name);
    attributeMap.put(name, attr);
    return attr;
  }

  final RootElementSpec rootElement() {
    return new RootElementSpec(this);
  }

  final TemplateSpec template() {
    return template;
  }

  final TextSpec text() {
    return text;
  }

  private CategorySpec category0(String name) {
    return new CategorySpec(name);
  }

  private ElementSpec element0(String name) {
    return new ElementSpec(this, name);
  }

  private ElementAttributeSpec elementAttribute0(String name) {
    return new ElementAttributeSpec(name);
  }

  public final void write(JavaSink sink, ThisTemplate template) throws IOException {
    template.write(sink, this);
  }

}