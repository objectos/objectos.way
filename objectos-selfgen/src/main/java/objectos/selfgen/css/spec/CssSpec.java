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

import objectos.lang.Check;

public abstract class CssSpec {

  protected enum FormalDefinition {
    INSTANCE;
  }

  private CssSpecDsl dsl;

  protected CssSpec() {}

  public final void acceptCssSpecDsl(CssSpecDsl dsl) {
    this.dsl = Check.notNull(dsl, "dsl == null");
    try {
      definition();
    } finally {
      this.dsl = null;
    }
  }

  protected final void angleUnits(String... units) {
    for (String unit : units) {
      dsl.addAngleUnit(unit);
    }
  }

  protected abstract void definition();

  protected final void elementName(String name) {
    dsl.addElementName(name);
  }

  protected final FormalDefinition formal(Source source, String... values) {
    return FormalDefinition.INSTANCE;
  }

  protected final FormalDefinition formal(String definition, Source source) {
    return FormalDefinition.INSTANCE;
  }

  protected final FunctionName function(String name, MethodSignature... signatures) {
    return dsl.addFunction(name, signatures);
  }

  protected final void install(CssSpec other) {
    other.acceptCssSpecDsl(dsl);
  }

  protected final KeywordName keyword(String value) {
    return dsl.getKeyword(value);
  }

  protected final void lengthUnits(String... units) {
    for (String unit : units) {
      dsl.addLengthUnit(unit);
    }
  }

  protected final void namedColors(String... names) {
    for (String name : names) {
      dsl.addNamedColor(name);
    }
  }

  protected final String[] names(String... names) {
    return names;
  }

  protected final PrimitiveType primitive(Primitive kind) {
    return dsl.getPrimitive(kind);
  }

  protected final void property(
      String name, FormalDefinition definition, MethodSignature... signatures) {
    property(name, null, definition, signatures);
  }

  protected final void property(
      String name, String identifier,
      FormalDefinition definition, MethodSignature... signatures) {
    property(PropertyKind.STANDARD, name, identifier, definition, signatures);
  }

  protected final void property(
      String[] names, FormalDefinition definition, MethodSignature... signatures) {
    for (String name : names) {
      property(name, definition, signatures);
    }
  }

  protected final void propertyHash(
      String name, FormalDefinition definition, MethodSignature... signatures) {
    property(PropertyKind.HASH, name, null, definition, signatures);
  }

  protected final void pseudoClasses(String... names) {
    for (String name : names) {
      dsl.addPseudoClass(name);
    }
  }

  protected final void pseudoElements(String... names) {
    for (String name : names) {
      dsl.addPseudoElement(name);
    }
  }

  protected final MethodSignature sig(ParameterType type, String name) {
    return MethodSignature.of(type, name);
  }

  protected final MethodSignature sig(
      ParameterType type0, String name0,
      ParameterType type1, String name1) {
    return MethodSignature.of(type0, name0, type1, name1);
  }

  protected final MethodSignature sig(
      ParameterType type0, String name0,
      ParameterType type1, String name1,
      ParameterType type2, String name2) {
    return MethodSignature.of(type0, name0, type1, name1, type2, name2);
  }

  protected final MethodSignature sig(
      ParameterType type0, String name0,
      ParameterType type1, String name1,
      ParameterType type2, String name2,
      ParameterType type3, String name3) {
    return MethodSignature.of(type0, name0, type1, name1, type2, name2, type3, name3);
  }

  protected final MethodSignature sig(
      ParameterType type0, String name0,
      ParameterType type1, String name1,
      ParameterType type2, String name2,
      ParameterType type3, String name3,
      ParameterType type4, String name4) {
    return MethodSignature.of(type0, name0, type1, name1, type2, name2, type3, name3, type4, name4);
  }

  protected final MethodSignature sig(
      ParameterType type0, String name0,
      ParameterType type1, String name1,
      ParameterType type2, String name2,
      ParameterType type3, String name3,
      ParameterType type4, String name4,
      ParameterType type5, String name5) {
    return MethodSignature.of(
      type0, name0, type1, name1, type2, name2, type3, name3, type4, name4, type5, name5
    );
  }

  protected final MethodSignature sigAbstract(
      ParameterType type, String name) {
    return MethodSignature.abstractOf(type, name);
  }

  protected final MethodSignature sigHash() {
    return MethodSignature.sigHash();
  }

  protected final MethodSignature sigZero() {
    return MethodSignature.sigZero();
  }

  protected final ValueType t(String name, Value... values) {
    return dsl.getValueType(name, values);
  }

  private void property(
      PropertyKind kind, String name, String identifier,
      FormalDefinition definition, MethodSignature... signatures) {
    // ignore definition for now
    Property property;
    property = kind.get(name, identifier);

    dsl.addProperty(property, signatures);
  }

}
