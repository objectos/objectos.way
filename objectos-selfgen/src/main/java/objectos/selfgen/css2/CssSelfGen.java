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

public abstract class CssSelfGen extends CompiledSpec {

  private final Map<String, KeywordName> keywords = new HashMap<>();

  private final Map<String, Property> properties = new HashMap<>();

  private final Map<String, SelectorName> selectors = new HashMap<>();

  private final Map<String, ValueType> valueTypes = new HashMap<>();

  private LengthType lengthType;

  private PercentageType percentageType;

  private StringType stringType;

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

    spec.write(sink, new KeywordNameStep());

    spec.write(sink, new LengthTypeStep());

    spec.write(sink, new NamedElementStep());

    spec.write(sink, new PercentageTypeStep());

    spec.write(sink, new PropertyStep());

    spec.write(sink, new PropertyValueStep());

    spec.write(sink, new StringTypeStep());

    spec.write(sink, new ZeroTypeStep());
  }

  protected final CompiledSpec compile() {
    keywords.clear();

    selectors.clear();

    valueTypes.clear();

    definition();

    for (var selector : selectors.values()) {
      var name = selector.selectorName;

      var clash = keywords.get(name);

      if (clash == null) {
        continue;
      }

      selector.disable();

      clash.addSuperType(ThisTemplate.SELECTOR);
    }

    for (var keyword : keywords.values()) {
      keyword.compile();
    }

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

  protected final void keywordFieldName(String keywordName, String fieldName) {
    if (keywords.containsKey(keywordName)) {
      throw new IllegalArgumentException(
        """
        Keyword %s was already defined.
        """.formatted(keywordName)
      );
    }

    var value = new KeywordName(fieldName, keywordName);

    keywords.put(keywordName, value);
  }

  protected final KeywordNameSet keywords(String... names) {
    var array = new KeywordName[names.length];

    for (int i = 0; i < names.length; i++) {
      var name = names[i];

      array[i] = kw(name);
    }

    return new KeywordNameSet(array);
  }

  protected final KeywordName kw(String name) {
    return keywords.computeIfAbsent(name, KeywordName::of);
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
    pimpl(propertyName, value, Style.BOX);
  }

  protected final void pdbl(String propertyName) {
    pimpl(propertyName, ParameterType.DOUBLE, Style.DOUBLE);
  }

  protected final PercentageType percentage() {
    if (percentageType == null) {
      percentageType = new PercentageType();

      zeroTypeIfNecessary().percentageType();
    }

    return percentageType;
  }

  protected final void pint(String propertyName) {
    pimpl(propertyName, ParameterType.INT, Style.INT);
  }

  protected final void pval(String propertyName, ParameterType value) {
    pimpl(propertyName, value, Style.VALUE);
  }

  protected final void pval(
      String propertyName,
      ParameterType value1, ParameterType value2) {
    var property = properties.computeIfAbsent(propertyName, Property::of);

    property.addSignature(
      new Signature.Custom2(value1, value2)
    );
  }

  protected final void pval(
      String propertyName,
      ParameterType value1, ParameterType value2, ParameterType value3) {
    var property = properties.computeIfAbsent(propertyName, Property::of);

    property.addSignature(
      new Signature.Custom3(value1, value2, value3)
    );
  }

  protected final void pva2(String propertyName, ParameterType value) {
    pimpl(propertyName, value, Style.VALUE2);
  }

  protected final void pva3(String propertyName, ParameterType value) {
    pimpl(propertyName, value, Style.VALUE3);
  }

  protected final void pva4(String propertyName, ParameterType value) {
    pimpl(propertyName, value, Style.VALUE4);
  }

  protected final void pvar(String propertyName, ParameterType value) {
    pimpl(propertyName, value, Style.VARARGS);
  }

  private void pimpl(String propertyName, ParameterType value, Style style) {
    var property = properties.computeIfAbsent(propertyName, Property::of);

    property.addSignature(style, value);
  }

  protected final void selectors(String... names) {
    for (var name : names) {
      selector(name);
    }
  }

  protected final StringType string() {
    if (stringType == null) {
      stringType = new StringType();
    }

    return stringType;
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
  final PercentageType percentageType() {
    return percentageType;
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
  final StringType stringType() {
    return stringType;
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
    selectors.computeIfAbsent(name, SelectorName::of);
  }

  private ZeroType zeroTypeIfNecessary() {
    if (zeroType == null) {
      zeroType = new ZeroType();
    }

    return zeroType;
  }

}
