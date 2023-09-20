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
package objectos.selfgen.css;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import objectos.code.Code;
import objectos.lang.Check;

public abstract class CssSelfGen {

  protected static final ParameterType DOUBLE = ParameterType.DOUBLE;

  protected static final ParameterType INT = ParameterType.INT;

  protected static final ParameterType STRING = ParameterType.STRING;

  final Code code = Code.of();

  final Map<String, Function> functions = new TreeMap<>();

  final Map<String, KeywordName> keywords = new TreeMap<>();

  final Map<String, Property> properties = new HashMap<>();

  final Map<String, SelectorName> selectors = new HashMap<>();

  final Map<String, ValueType> valueTypes = new TreeMap<>();

  ColorValue colorValue;

  protected DoubleType doubleType;

  protected FilterFunction filterFunction;

  FlexValue flexValue;

  protected IntType intType;

  LengthType lengthType;

  PercentageType percentageType;

  StringType stringType;

  UrlType urlType;

  ZeroType zeroType;

  public final void execute(String[] args) throws IOException {
    String srcDir;
    srcDir = args[0];

    Path directory;
    directory = Path.of(srcDir);

    compile();

    writeTo(new ApiStep(this), directory);

    writeTo(new FunctionStep(this), directory);

    writeTo(new GeneratedColorStep(this), directory);

    writeTo(new GeneratedCssTemplateStep(this), directory);

    writeTo(new LengthUnitStep(this), directory);

    writeTo(new PropertyStep(this), directory);

    writeTo(new StandardNameStep(this), directory);

    writeTo(new StandardSelectorStep(this), directory);
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

  protected final void compile() {
    keywords.clear();

    selectors.clear();

    valueTypes.clear();

    definition();

    for (SelectorName selector : selectors.values()) {
      String name;
      name = selector.selectorName;

      KeywordName clash;
      clash = keywords.get(name);

      if (clash == null) {
        continue;
      }

      selector.disable();

      clash.addSuperType(ThisTemplate.SELECTOR);
    }

    for (KeywordName keyword : keywords.values()) {
      keyword.compile();
    }
  }

  protected final void defineDoubleType() {
    if (doubleType == null) {
      doubleType = new DoubleType();
    }
  }

  protected final void defineFilterFunction() {
    if (filterFunction == null) {
      filterFunction = new FilterFunction();
    }
  }

  protected final void defineIntType() {
    if (intType == null) {
      intType = new IntType();
    }
  }

  protected abstract void definition();

  protected final Function f(String functionName) {
    var function = functions.get(functionName);

    if (function == null) {
      throw new IllegalArgumentException(
        "The %s function was not found".formatted(functionName)
      );
    }

    return function;
  }

  protected final FlexValue flex() {
    if (flexValue == null) {
      flexValue = new FlexValue();
    }

    return flexValue;
  }

  protected final Function function(
      String functionName, Signature... signatures) {
    Function function;
    function = functions.computeIfAbsent(functionName, Function::of);

    for (var signature : signatures) {
      function.addSignature(signature);
    }

    return function;
  }

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

  protected final PercentageType percentage() {
    if (percentageType == null) {
      percentageType = new PercentageType();

      zeroTypeIfNecessary().percentageType();
    }

    return percentageType;
  }

  protected final Property property(
      String propertyName, Signature... signatures) {
    Property property;
    property = properties.computeIfAbsent(propertyName, Property::of);

    for (var signature : signatures) {
      property.addSignature(signature);
    }

    return property;
  }

  protected final void selectors(SelectorKind kind, String... names) {
    for (var name : names) {
      if (selectors.containsKey(name)) {
        throw new IllegalArgumentException(
          "Selector name already registered: name=" + name
        );
      }

      String fieldName;
      fieldName = SelectorName.generateFieldName(name);

      SelectorName selectorName;
      selectorName = new SelectorName(kind, fieldName, name);

      selectors.put(name, selectorName);
    }
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

  protected final Signature sig(
      ParameterType type1, String name1,
      ParameterType type2, String name2,
      ParameterType type3, String name3,
      ParameterType type4, String name4,
      ParameterType type5, String name5) {
    return new Signature5(
      type1.typeName(), name1,
      type2.typeName(), name2,
      type3.typeName(), name3,
      type4.typeName(), name4,
      type5.typeName(), name5);
  }

  protected final Signature sig(
      ParameterType type1, String name1,
      ParameterType type2, String name2,
      ParameterType type3, String name3,
      ParameterType type4, String name4,
      ParameterType type5, String name5,
      ParameterType type6, String name6) {
    return new Signature6(
      type1.typeName(), name1,
      type2.typeName(), name2,
      type3.typeName(), name3,
      type4.typeName(), name4,
      type5.typeName(), name5,
      type6.typeName(), name6);
  }

  protected final Signature sigVar(
      ParameterType type, String name) {
    return new SignatureVarArgs(type.typeName(), "values");
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

  final Collection<Function> functions() {
    return functions.values();
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

  private void writeTo(ThisTemplate template, Path directory) throws IOException {
    template.writeTo(directory);
  }

}
