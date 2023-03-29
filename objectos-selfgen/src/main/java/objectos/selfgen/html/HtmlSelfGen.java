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
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import objectos.code.JavaSink;

public abstract class HtmlSelfGen {

  private List<AttributeSpec> attributes;

  private final Map<String, AttributeSpec> attributeMap = new TreeMap<>();

  private final Map<String, CategorySpec> categoryMap = new TreeMap<>();

  private final Map<String, ElementSpec> elementMap = new TreeMap<>();

  private final RootElementSpec rootElement = new RootElementSpec(this);

  private final TemplateSpec template = new TemplateSpec();

  private final TextSpec text = new TextSpec();

  protected HtmlSelfGen() {}

  public final void execute(String[] args) throws IOException {
    var moduleName = args[0];

    var srcDir = args[1];

    var directory = Path.of(srcDir);

    var sink = JavaSink.ofDirectory(
      directory,
      JavaSink.overwriteExisting()
    );

    prepare();

    switch (moduleName) {
      case "html" -> {
        write(sink, new AnyElementValueStep());

        write(sink, new ElementValueIfaceStep());

        write(sink, new GeneratedAbstractTemplateStep());

        write(sink, new GeneratedHtmlTemplateStep());

        write(sink, new NonVoidElementValueStep());

        write(sink, new StandardAttributeNameStep());

        write(sink, new StandardElementNameStep());

        write(sink, new InstructionIfaceStep());

        write(sink, new InternalInstructionStep());
      }

      default -> throw new IllegalArgumentException("Unknown module: " + moduleName);
    }
  }

  protected final CategorySpec category(String name) {
    return categoryMap.computeIfAbsent(name, this::category0);
  }

  protected abstract void definition();

  protected final ElementSpec el(String name) {
    return element(name);
  }

  protected final ElementSpec element(String name) {
    return elementMap.computeIfAbsent(name, this::element0);
  }

  protected final RootElementSpec rootElement() {
    return rootElement;
  }

  protected final TemplateSpec template() {
    return template;
  }

  protected final TextSpec text() {
    return text;
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

  final ElementAttributeSpec elementAttribute(ElementSpec parent, String name) {
    var attr = attributeMap.computeIfAbsent(name, this::elementAttribute0);

    return attr.toElementAttributeSpec(parent);
  }

  final Iterable<ElementSpec> elements() {
    return elementMap.values();
  }

  final AttributeSpec globalAttribute(String name) {
    if (attributeMap.containsKey(name)) {
      throw new IllegalArgumentException(name + " global attribute already defined!");
    }

    var attr = AttributeSpec.global(name);

    attributeMap.put(name, attr);

    return attr;
  }

  final HtmlSelfGen prepare() {
    definition();

    for (ElementSpec element : elementMap.values()) {
      element.prepare();
    }

    return this;
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

  private void write(JavaSink sink, ThisTemplate template) throws IOException {
    template.write(sink, this);
  }

}