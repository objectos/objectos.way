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
package objectos.selfgen.css2;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import objectos.code.JavaSink;
import objectos.lang.Check;
import objectos.selfgen.css2.Signature.Style;

public abstract class CssSelfGen extends CompiledSpec {

  private final Map<String, KeywordName> keywords = new HashMap<>();

  private LengthType lengthType;

  private final Map<String, Property> properties = new HashMap<>();

  private final Map<String, SelectorName> selectors = new HashMap<>();

  private final Map<String, ValueType> valueTypes = new HashMap<>();

  private ZeroType zeroType;

  public final void execute(String[] args) throws IOException {
    var srcDir = args[0];

    var directory = Path.of(srcDir);

    var sink = JavaSink.ofDirectory(
      directory,
      JavaSink.overwriteExisting()
    );

    var spec = compile();

    spec.write(sink, new GeneratedCssTemplateStep());

    spec.write(sink, new LengthTypeStep());

    spec.write(sink, new NamedElementStep());

    spec.write(sink, new PropertyStep());

    spec.write(sink, new PropertyValueStep());

    spec.write(sink, new ZeroTypeStep());
  }

  protected final CompiledSpec compile() {
    keywords.clear();

    selectors.clear();

    valueTypes.clear();

    definition();

    return this;
  }

  protected final ValueType def(String simpleName, Value... values) {
    Check.argument(!valueTypes.containsKey(simpleName), "Duplicate ValueType name ", simpleName);

    var valueType = valueTypes.computeIfAbsent(simpleName, ValueType::of);

    for (var value : values) {
      value.addValueType(valueType);
    }

    return valueType;
  }

  protected abstract void definition();

  protected final KeywordName kw(String name) {
    var keyword = KeywordName.of(name);

    var key = keyword.fieldName;

    var existing = keywords.get(key);

    if (existing == null) {
      keywords.put(key, keyword);

      return keyword;
    }

    if (existing.keywordName.equals(name)) {
      return existing;
    }

    throw new IllegalArgumentException(
      """
      Duplicate keyword field name
      keyword=%s
      existing=%s
      """.formatted(name, existing.keywordName)
    );
  }

  protected final LengthType length(String... units) {
    if (lengthType == null) {
      lengthType = new LengthType();

      zeroTypeIfNecessary().lengthType();
    }

    for (var unit : units) {
      lengthType.addUnit(unit);
    }

    return lengthType;
  }

  protected final void pbox(String propertyName, ParameterType value) {
    var property = properties.computeIfAbsent(propertyName, Property::of);

    property.addSignature(Style.BOX, value);
  }

  protected final void pval(String propertyName, ParameterType value) {
    var property = properties.computeIfAbsent(propertyName, Property::of);

    property.addSignature(Style.VALUE, value);
  }

  protected final void selectors(String... names) {
    for (var name : names) {
      selector(name);
    }
  }

  @Override
  final Collection<KeywordName> keywords() {
    return keywords.values();
  }

  @Override
  final LengthType lengthType() {
    return lengthType;
  }

  @Override
  final Collection<Property> properties() {
    return properties.values();
  }

  @Override
  final Collection<SelectorName> selectors() {
    return selectors.values();
  }

  @Override
  final Collection<ValueType> valueTypes() {
    return valueTypes.values();
  }

  @Override
  final ZeroType zeroType() {
    return zeroType;
  }

  private void selector(String name) {
    var selector = SelectorName.of(name);

    var key = selector.fieldName();

    if (selectors.containsKey(key)) {
      throw new IllegalArgumentException(
        """
        Duplicate selector field name
        fieldName=%s
        selectorName=%s
        """.formatted(key, name)
      );
    }

    selectors.put(key, selector);
  }

  private ZeroType zeroTypeIfNecessary() {
    if (zeroType == null) {
      zeroType = new ZeroType();
    }

    return zeroType;
  }

}
