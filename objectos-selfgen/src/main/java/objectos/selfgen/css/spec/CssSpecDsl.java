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
package objectos.selfgen.css.spec;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import objectos.lang.Check;
import objectos.util.GrowableSet;

public class CssSpecDsl {

  final Set<String> angleUnits = new TreeSet<>();

  final GrowableSet<String> elementNames = new GrowableSet<>();

  private final Map<String, FunctionName> functions = new TreeMap<>();

  final Map<String, KeywordName> keywords = new TreeMap<>();

  private final Map<Primitive, PrimitiveType> primitives
      = new EnumMap<Primitive, PrimitiveType>(Primitive.class);

  final Map<String, Property> propertyMap = new TreeMap<>();

  final GrowableSet<String> pseudoClasses = new GrowableSet<>();

  final GrowableSet<String> pseudoElements = new GrowableSet<>();

  private final Step step;

  private final Map<String, ValueType> valueTypes = new LinkedHashMap<>();

  public CssSpecDsl(Step step) {
    this.step = step;
  }

  public final void addAngleUnit(String unit) {
    angleUnits.add(unit);

    step.addAngleUnit(unit);
  }

  public void addElementName(String elementName) {
    elementNames.add(elementName);

    step.addElementName(elementName);
  }

  public final FunctionName addFunction(String name, MethodSignature[] signatures) {
    Check.argument(!functions.containsKey(name), "function already defined: ", name);

    FunctionName function;
    function = FunctionName.of(name);

    functions.put(name, function);

    Arrays.sort(signatures);

    function.signatures = signatures;

    for (MethodSignature signature : signatures) {
      step.addMethodSignature(function, signature);
    }

    return function;
  }

  public final void addLengthUnit(String unit) {
    step.addLengthUnit(unit);
  }

  public final void addNamedColor(String name) {
    ColorName colorName;
    colorName = ColorName.of(name);

    step.addColorName(colorName);
  }

  public final void addProperty(Property property, MethodSignature[] signatures) {
    String name;
    name = property.getName();

    Check.argument(!propertyMap.containsKey(name), "property already defined: ", name);

    propertyMap.put(name, property);

    step.addProperty(property);

    Arrays.sort(signatures);

    for (MethodSignature signature : signatures) {
      step.addMethodSignature(property, signature);
    }

    property.signatures = signatures;
  }

  public final void addPseudoClass(String name) {
    step.addPseudoClass(name);
  }

  public final void addPseudoElement(String name) {
    step.addPseudoElement(name);
  }

  public final void execute() {
    for (FunctionName function : functions.values()) {
      step.addFunction(function);
    }

    for (KeywordName keyword : keywords.values()) {
      step.addKeyword(keyword);
    }

    for (PrimitiveType type : primitives.values()) {
      step.addPrimitiveType(type);
    }

    for (ValueType type : valueTypes.values()) {
      step.addValueType(type);
    }

    step.execute();
  }

  public final KeywordName getKeyword(String value) {
    Check.notNull(value, "value == null");

    KeywordName keyword = keywords.get(value);

    if (keyword == null) {
      if (elementNames.contains(value)) {
        keyword = KeywordName.withKwSuffix(value);
      } else {
        keyword = KeywordName.of(value);
      }
      keywords.put(value, keyword);
    }

    return keyword;
  }

  public final PrimitiveType getPrimitive(Primitive kind) {
    Check.notNull(kind, "kind == null");

    PrimitiveType type = primitives.get(kind);

    if (type == null) {
      type = PrimitiveType.of(kind);
      primitives.put(kind, type);
    }

    return type;
  }

  public final ValueType getValueType(String name, Value[] values) {
    Check.notNull(name, "name == null");
    Check.notNull(values, "values == null");

    ValueType type = valueTypes.get(name);

    if (type == null) {
      type = ValueType.of(name);

      valueTypes.put(name, type);

      for (Value value : values) {
        value.acceptValueType(type);
      }
    }

    return type;
  }

  final ValueType valueType(String name) {
    return valueTypes.get(name);
  }

}
