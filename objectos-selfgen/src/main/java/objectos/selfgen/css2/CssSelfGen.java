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
import objectos.code.PrimitiveTypeName;
import objectos.lang.Check;

public abstract class CssSelfGen extends CompiledSpec {

  protected static final ParameterType DOUBLE = ParameterType.DOUBLE;

  protected static final ParameterType INT = ParameterType.INT;

  protected static final ParameterType STRING = ParameterType.STRING;

  private final Map<String, KeywordName> keywords = new HashMap<>();

  private final Map<String, Property> properties = new HashMap<>();

  private final Map<String, SelectorName> selectors = new HashMap<>();

  private final Map<String, ValueType> valueTypes = new HashMap<>();

  private ColorValue colorValue;

  private LengthType lengthType;

  private PercentageType percentageType;

  private StringType stringType;

  private UrlType urlType;

  private ZeroType zeroType;

  public final void execute(String[] args) throws IOException {
    var srcDir = args[0];

    var directory = Path.of(srcDir);

    var sink = JavaSink.ofDirectory(
      directory,
      JavaSink.overwriteExisting()
    );

    var spec = compile();

    spec.write(sink, new ColorValueStep());

    spec.write(sink, new GeneratedColorStep());

    spec.write(sink, new GeneratedCssTemplateStep());

    spec.write(sink, new KeywordNameStep());

    spec.write(sink, new LengthTypeStep());

    spec.write(sink, new NamedElementStep());

    spec.write(sink, new PercentageTypeStep());

    spec.write(sink, new PropertyStep());

    spec.write(sink, new PropertyValueStep());

    spec.write(sink, new StringTypeStep());

    spec.write(sink, new UrlTypeStep());

    spec.write(sink, new ZeroTypeStep());
  }

  protected final ColorValue color(String... names) {
    if (colorValue == null) {
      colorValue = new ColorValue();
    }

    for (var name : names) {
      colorValue.addName(name);
    }

    return colorValue;
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

  protected abstract void definition();

  protected final KeywordName k(String name) {
    return keywords.computeIfAbsent(name, KeywordName::of);
  }

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

      array[i] = k(name);
    }

    return new KeywordNameSet(array);
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
    var prop = prop(propertyName);

    var typeName = value.typeName();

    prop.addSignature(
      new Signature1(typeName, "all")
    );
    prop.addSignature(
      new Signature2(
        typeName, "vertical",
        typeName, "horizontal"
      )
    );
    prop.addSignature(
      new Signature3(
        typeName, "top",
        typeName, "horizontal",
        typeName, "bottom"
      )
    );
    prop.addSignature(
      new Signature4(
        typeName, "top",
        typeName, "right",
        typeName, "bottom",
        typeName, "left"
      )
    );
  }

  protected final void pdbl(String propertyName) {
    prop(propertyName).addSignature(
      new SignaturePrim(PrimitiveTypeName.DOUBLE, "value")
    );
  }

  protected final PercentageType percentage() {
    if (percentageType == null) {
      percentageType = new PercentageType();

      zeroTypeIfNecessary().percentageType();
    }

    return percentageType;
  }

  protected final void pint(String propertyName) {
    prop(propertyName).addSignature(
      new SignaturePrim(PrimitiveTypeName.INT, "value")
    );
  }

  protected final Property prop(String propertyName) {
    return properties.computeIfAbsent(propertyName, Property::of);
  }

  protected final Property property(
      String propertyName, Signature... signatures) {
    var property = properties.computeIfAbsent(propertyName, Property::of);

    for (var signature : signatures) {
      property.addSignature(signature);
    }

    return property;
  }

  protected final void pva2(String propertyName, ParameterType value) {
    var prop = prop(propertyName);

    var typeName = value.typeName();

    prop.addSignature(
      new Signature2(typeName, "value1", typeName, "value2")
    );
  }

  protected final void pva3(String propertyName, ParameterType value) {
    var prop = prop(propertyName);

    var typeName = value.typeName();

    prop.addSignature(
      new Signature3(typeName, "value1", typeName, "value2", typeName, "value3")
    );
  }

  protected final void pva4(String propertyName, ParameterType value) {
    var prop = prop(propertyName);

    var typeName = value.typeName();

    prop.addSignature(
      new Signature4(
        typeName, "value1",
        typeName, "value2",
        typeName, "value3",
        typeName, "value4"
      )
    );
  }

  protected final void pval(String propertyName, ParameterType value) {
    var prop = prop(propertyName);

    var typeName = value.typeName();

    prop.addSignature(
      new Signature1(typeName, "value")
    );
  }

  protected final void pval(
      String propertyName,
      ParameterType value1, ParameterType value2) {
    var prop = prop(propertyName);

    prop.addSignature(
      new Signature2(
        value1.typeName(), "value1",
        value2.typeName(), "value2"
      )
    );
  }

  protected final void pval(
      String propertyName,
      ParameterType value1, ParameterType value2, ParameterType value3) {
    var prop = prop(propertyName);

    prop.addSignature(
      new Signature3(
        value1.typeName(), "value1",
        value2.typeName(), "value2",
        value3.typeName(), "value3"
      )
    );
  }

  protected final void pvar(String propertyName, ParameterType value) {
    var prop = prop(propertyName);

    prop.addSignature(
      new SignatureVarArgs(value.typeName(), "values")
    );
  }

  protected final void selectors(String... names) {
    for (var name : names) {
      selector(name);
    }
  }

  protected final Signature sig(
      ParameterType type, String name) {
    return type.toSignature(name);
  }

  protected final Signature sig(
      ParameterType type1, String name1,
      ParameterType type2, String name2) {
    return new Signature2(
      type1.typeName(), name1,
      type2.typeName(), name2);
  }

  protected final Signature sig(
      ParameterType type1, String name1,
      ParameterType type2, String name2,
      ParameterType type3, String name3) {
    return new Signature3(
      type1.typeName(), name1,
      type2.typeName(), name2,
      type3.typeName(), name3);
  }

  protected final Signature sig(
      ParameterType type1, String name1,
      ParameterType type2, String name2,
      ParameterType type3, String name3,
      ParameterType type4, String name4) {
    return new Signature4(
      type1.typeName(), name1,
      type2.typeName(), name2,
      type3.typeName(), name3,
      type4.typeName(), name4);
  }

  protected final StringType string() {
    if (stringType == null) {
      stringType = new StringType();
    }

    return stringType;
  }

  protected final ValueType t(String simpleName) {
    var valueType = valueTypes.get(simpleName);

    if (valueType == null) {
      throw new IllegalArgumentException(
        "The %s value type was not found".formatted(simpleName)
      );
    }

    return valueType;
  }

  protected final ValueType t(String simpleName, Value... values) {
    Check.argument(!valueTypes.containsKey(simpleName), "Duplicate ValueType name ", simpleName);

    var valueType = valueTypes.computeIfAbsent(simpleName, ValueType::of);

    for (var value : values) {
      value.addValueType(valueType);
    }

    return valueType;
  }

  protected final UrlType url() {
    if (urlType == null) {
      urlType = new UrlType();
    }

    return urlType;
  }

  @Override
  final ColorValue colorValue() {
    return colorValue;
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
  final UrlType urlType() {
    return urlType;
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
