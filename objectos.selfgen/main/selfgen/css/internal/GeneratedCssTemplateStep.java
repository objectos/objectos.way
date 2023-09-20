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
package selfgen.css.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import objectos.code.ClassName;
import objectos.code.PrimitiveTypeName;

final class GeneratedCssTemplateStep extends ThisTemplate {

  private boolean declSig1;
  private boolean declSig2;
  private boolean declSig3;
  private boolean declSig4;
  private boolean declSig5;
  private boolean declSig6;
  private boolean declSigPrim;
  private boolean declSigString;

  private boolean funcSig2;

  public GeneratedCssTemplateStep(CssSelfGen spec) {
    super(spec);
  }

  @Override
  final String contents() {
    className(ClassName.of(CSS_INTERNAL, "GeneratedCssTemplate"));

    return code."""
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
    package \{packageName};
    \{importList}
    \{GENERATED_MSG}
    abstract class \{simpleName} {
    \{body()}
    }
    """;
  }

  private String body() {
    List<String> result;
    result = new ArrayList<>();

    selectors(result);

    colors(result);

    keywords(result);

    lengthUnits(result);

    properties(result);

    declarationMethods(result);

    functions(result);

    return result.stream().collect(Collectors.joining("\n", "\n", ""));
  }

  private void selectors(List<String> result) {
    Collection<SelectorName> unsorted;
    unsorted = spec.selectors.values();

    List<SelectorName> sorted;
    sorted = new ArrayList<>();

    for (var selector : unsorted) {
      if (selector.disabled) {
        continue;
      }

      sorted.add(selector);
    }

    sorted.sort(SelectorName.ORDER_BY_FIELD_NAME);

    for (int i = 0, size = sorted.size(); i < size; i++) {
      SelectorName selector;
      selector = sorted.get(i);

      String fieldName;
      fieldName = selector.fieldName;

      String selectorName;
      selectorName = selector.selectorName;

      ClassName impl;
      impl = switch (selector.kind) {
        case TYPE -> STANDARD_TYPE_SELECTOR;

        case PSEUDO_CLASS -> STANDARD_PSEUDO_CLASS_SELECTOR;

        case PSEUDO_ELEMENT -> STANDARD_PSEUDO_ELEMENT_SELECTOR;

        default -> STANDARD_NAME;
      };

      result.add(
        code."""
          protected static final \{SELECTOR} \{fieldName} = \{impl}.\{fieldName};
        """
      );
    }

    result.add(
      code."""
        protected static final \{SELECTOR} any = \{STANDARD_NAME}.any;
      """
    );
  }

  private void colors(List<String> result) {
    ColorValue colorValue;
    colorValue = spec.colorValue;

    if (colorValue == null) {
      return;
    }

    Collection<ColorName> unsorted;
    unsorted = colorValue.names;

    ArrayList<ColorName> sorted;
    sorted = new ArrayList<>(unsorted);

    Comparator<ColorName> byFieldName;
    byFieldName = (self, that) -> (self.fieldName().compareTo(that.fieldName()));

    sorted.sort(byFieldName);

    for (int i = 0, size = sorted.size(); i < size; i++) {
      ColorName color;
      color = sorted.get(i);

      String fieldName;
      fieldName = color.fieldName();

      result.add(
        code."""
          protected static final \{COLOR_VALUE} \{fieldName} = \{STANDARD_NAME}.\{fieldName};
        """
      );
    }

    for (ColorHex colorHex : colorValue.palette) {
      String constantName;
      constantName = colorHex.constantName();

      String hexValue;
      hexValue = colorHex.hexValue();

      result.add(
        code."""
          protected static final \{COLOR_VALUE} \{constantName} = \{COLOR}.ofHex("\{hexValue}");
        """
      );
    }
  }

  private void keywords(List<String> result) {
    Collection<KeywordName> unsorted;
    unsorted = spec.keywords.values();

    List<KeywordName> sorted;
    sorted = new ArrayList<>(unsorted);

    sorted.sort(KeywordName.ORDER_BY_FIELD_NAME);

    for (int i = 0, size = sorted.size(); i < size; i++) {
      KeywordName kw;
      kw = sorted.get(i);

      ClassName type;
      type = kw.className();

      String fieldName;
      fieldName = kw.fieldName;

      result.add(
        code."""
          protected static final \{type} \{fieldName} = \{STANDARD_NAME}.\{fieldName};
        """
      );
    }
  }

  private void lengthUnits(List<String> result) {
    LengthType lengthType;
    lengthType = spec.lengthType;

    if (lengthType == null) {
      return;
    }

    List<PrimitiveTypeName> primitives;
    primitives = List.of(PrimitiveTypeName.DOUBLE, PrimitiveTypeName.INT);

    List<String> unsorted;
    unsorted = lengthType.units;

    List<String> sorted;
    sorted = new ArrayList<>(unsorted);

    sorted.sort(null);

    for (int i = 0, size = sorted.size(); i < size; i++) {
      String unit;
      unit = sorted.get(i);

      String enumName;
      enumName = unit.toUpperCase(Locale.US);

      for (var primitive : primitives) {
        result.add(
          code."""
            protected final \{LENGTH_VALUE} \{unit}(\{primitive} value) {
              return length(value, \{LENGTH_UNIT}.\{enumName});
            }
          """
        );
      }
    }

    for (var primitive : primitives) {
      result.add(
        code."""
          abstract \{LENGTH_VALUE} length(\{primitive} value, \{LENGTH_UNIT} unit);
        """
      );
    }
  }

  private void properties(List<String> result) {
    Collection<Property> unsorted;
    unsorted = spec.properties.values();

    List<Property> sorted;
    sorted = new ArrayList<>(unsorted);

    sorted.sort(Property.ORDER_BY_METHOD_NAME);

    for (int i = 0, size = sorted.size(); i < size; i++) {
      Property property;
      property = sorted.get(i);

      for (Signature signature : property.signatures()) {
        ClassName returnName;
        returnName = property.declarationClassName;

        String constantName;
        constantName = property.constantName;

        String methodName;
        methodName = property.methodName;

        String m;
        m = switch (signature) {
          case Signature1 sig -> {
            declSig1 = true;

            yield code."""
              protected final \{returnName} \{methodName}(\{sig.type()} \{sig.name()}) {
                \{CHECK}.notNull(\{sig.name()}, "\{sig.name()} == null");
                declaration(\{PROPERTY}.\{constantName}, \{sig.name()});
                return \{INTERNAL_INSTRUCTION}.INSTANCE;
              }
            """;
          }

          case Signature2 sig -> {
            declSig2 = true;

            yield code."""
              protected final \{returnName} \{methodName}(\{sig.type1()} \{sig.name1()}, \{sig.type2()} \{sig.name2()}) {
                \{CHECK}.notNull(\{sig.name1()}, "\{sig.name1()} == null");
                \{CHECK}.notNull(\{sig.name2()}, "\{sig.name2()} == null");
                declaration(\{PROPERTY}.\{constantName}, \{sig.name1()}, \{sig.name2()});
                return \{INTERNAL_INSTRUCTION}.INSTANCE;
              }
            """;
          }

          case Signature3 sig -> {
            declSig3 = true;

            yield code."""
              protected final \{returnName} \{methodName}(\{sig.type1()} \{sig.name1()}, \{sig.type2()} \{sig.name2()}, \{sig.type3()} \{sig.name3()}) {
                \{CHECK}.notNull(\{sig.name1()}, "\{sig.name1()} == null");
                \{CHECK}.notNull(\{sig.name2()}, "\{sig.name2()} == null");
                \{CHECK}.notNull(\{sig.name3()}, "\{sig.name3()} == null");
                declaration(\{PROPERTY}.\{constantName}, \{sig.name1()}, \{sig.name2()}, \{sig.name3()});
                return \{INTERNAL_INSTRUCTION}.INSTANCE;
              }
            """;
          }

          case Signature4 sig -> {
            declSig4 = true;

            yield code."""
              protected final \{returnName} \{methodName}(\{sig.type1()} \{sig.name1()}, \{sig.type2()} \{sig.name2()}, \{sig.type3()} \{sig.name3()}, \{sig.type4()} \{sig.name4()}) {
                \{CHECK}.notNull(\{sig.name1()}, "\{sig.name1()} == null");
                \{CHECK}.notNull(\{sig.name2()}, "\{sig.name2()} == null");
                \{CHECK}.notNull(\{sig.name3()}, "\{sig.name3()} == null");
                \{CHECK}.notNull(\{sig.name4()}, "\{sig.name4()} == null");
                declaration(\{PROPERTY}.\{constantName}, \{sig.name1()}, \{sig.name2()}, \{sig.name3()}, \{sig.name4()});
                return \{INTERNAL_INSTRUCTION}.INSTANCE;
              }
            """;
          }

          case Signature5 sig -> {
            declSig5 = true;

            yield code."""
              protected final \{returnName} \{methodName}(\{sig.type1()} \{sig.name1()}, \{sig.type2()} \{sig.name2()}, \{sig.type3()} \{sig.name3()}, \{sig.type4()} \{sig.name4()}, \{sig.type5()} \{sig.name5()}) {
                \{CHECK}.notNull(\{sig.name1()}, "\{sig.name1()} == null");
                \{CHECK}.notNull(\{sig.name2()}, "\{sig.name2()} == null");
                \{CHECK}.notNull(\{sig.name3()}, "\{sig.name3()} == null");
                \{CHECK}.notNull(\{sig.name4()}, "\{sig.name4()} == null");
                \{CHECK}.notNull(\{sig.name5()}, "\{sig.name5()} == null");
                declaration(\{PROPERTY}.\{constantName}, \{sig.name1()}, \{sig.name2()}, \{sig.name3()}, \{sig.name4()}, \{sig.name5()});
                return \{INTERNAL_INSTRUCTION}.INSTANCE;
              }
            """;
          }

          case Signature6 sig -> {
            declSig6 = true;

            yield code."""
              protected final \{returnName} \{methodName}(\{sig.type1()} \{sig.name1()}, \{sig.type2()} \{sig.name2()}, \{sig.type3()} \{sig.name3()}, \{sig.type4()} \{sig.name4()}, \{sig.type5()} \{sig.name5()}, \{sig.type6()} \{sig.name6()}) {
                \{CHECK}.notNull(\{sig.name1()}, "\{sig.name1()} == null");
                \{CHECK}.notNull(\{sig.name2()}, "\{sig.name2()} == null");
                \{CHECK}.notNull(\{sig.name3()}, "\{sig.name3()} == null");
                \{CHECK}.notNull(\{sig.name4()}, "\{sig.name4()} == null");
                \{CHECK}.notNull(\{sig.name5()}, "\{sig.name5()} == null");
                \{CHECK}.notNull(\{sig.name6()}, "\{sig.name6()} == null");
                declaration(\{PROPERTY}.\{constantName}, \{sig.name1()}, \{sig.name2()}, \{sig.name3()}, \{sig.name4()}, \{sig.name5()}, \{sig.name6()});
                return \{INTERNAL_INSTRUCTION}.INSTANCE;
              }
            """;
          }

          case SignaturePrim sig -> {
            declSigPrim = true;

            yield code."""
              protected final \{returnName} \{methodName}(\{sig.type()} \{sig.name()}) {
                declaration(\{PROPERTY}.\{constantName}, \{sig.name()});
                return \{INTERNAL_INSTRUCTION}.INSTANCE;
              }
            """;
          }

          case SignatureString sig -> {
            declSigString = true;

            yield code."""
              protected final \{returnName} \{methodName}(\{sig.type()} \{sig.name()}) {
                \{CHECK}.notNull(\{sig.name()}, "\{sig.name()} == null");
                declaration(\{PROPERTY}.\{constantName}, \{sig.name()});
                return \{INTERNAL_INSTRUCTION}.INSTANCE;
              }
            """;
          }

          case SignatureVarArgs sig -> {
            declSigString = true;

            yield code."""
              protected abstract \{returnName} \{methodName}(\{sig.typeName()}... \{sig.name()});
            """;
          }
        };

        result.add(m);
      }
    }
  }

  private void declarationMethods(List<String> result) {
    if (declSig1) {
      result.add(
        code."""
          abstract void declaration(\{PROPERTY} name, \{PROPERTY_VALUE} value);
        """
      );
    }

    if (declSig2) {
      result.add(
        code."""
          abstract void declaration(\{PROPERTY} name, \{PROPERTY_VALUE} value1, \{PROPERTY_VALUE} value2);
        """
      );
    }

    if (declSig3) {
      result.add(
        code."""
          abstract void declaration(\{PROPERTY} name, \{PROPERTY_VALUE} value1, \{PROPERTY_VALUE} value2, \{PROPERTY_VALUE} value3);
        """
      );
    }

    if (declSig4) {
      result.add(
        code."""
          abstract void declaration(\{PROPERTY} name, \{PROPERTY_VALUE} value1, \{PROPERTY_VALUE} value2, \{PROPERTY_VALUE} value3, \{PROPERTY_VALUE} value4);
        """
      );
    }

    if (declSig5) {
      result.add(
        code."""
          abstract void declaration(\{PROPERTY} name, \{PROPERTY_VALUE} value1, \{PROPERTY_VALUE} value2, \{PROPERTY_VALUE} value3, \{PROPERTY_VALUE} value4, \{PROPERTY_VALUE} value5);
        """
      );
    }

    if (declSig6) {
      result.add(
        code."""
          abstract void declaration(\{PROPERTY} name, \{PROPERTY_VALUE} value1, \{PROPERTY_VALUE} value2, \{PROPERTY_VALUE} value3, \{PROPERTY_VALUE} value4, \{PROPERTY_VALUE} value5, \{PROPERTY_VALUE} value6);
        """
      );
    }

    if (declSigPrim) {
      result.add(
        code."""
          abstract void declaration(\{PROPERTY} name, int value);
        """
      );

      result.add(
        code."""
          abstract void declaration(\{PROPERTY} name, double value);
        """
      );
    }

    if (declSigString) {
      result.add(
        code."""
          abstract void declaration(\{PROPERTY} name, \{STRING} value);
        """
      );
    }
  }

  private void functions(List<String> result) {
    Collection<Function> functions;
    functions = spec.functions.values();

    for (var function : functions) {
      ClassName returnName;
      returnName = function.className;

      String methodName;
      methodName = function.methodName;

      String constantName;
      constantName = function.constantName;

      for (var signature : function.signatures()) {
        String m;

        if (signature instanceof Signature2 sig) {
          funcSig2 = true;

          m = code."""
            protected final \{returnName} \{methodName}(\{sig.type1()} \{sig.name1()}, \{sig.type2()} \{sig.name2()}) {
              \{CHECK}.notNull(\{sig.name1()}, "\{sig.name1()} == null");
              \{CHECK}.notNull(\{sig.name2()}, "\{sig.name2()} == null");
              function(\{FUNCTION}.\{constantName}, \{sig.name1()}, \{sig.name2()});
              return \{INTERNAL_INSTRUCTION}.INSTANCE;
            }
          """;
        }

        else {
          throw new UnsupportedOperationException(
            "Implement me :: sig.type=" + signature.getClass().getSimpleName()
          );
        }

        result.add(m);
      }
    }

    if (funcSig2) {
      result.add(
        code."""
          abstract void function(\{FUNCTION} name, \{PROPERTY_VALUE} value1, \{PROPERTY_VALUE} value2);
        """
      );
    }
  }

}
