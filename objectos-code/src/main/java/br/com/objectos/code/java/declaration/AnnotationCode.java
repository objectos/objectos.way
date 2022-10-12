/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package br.com.objectos.code.java.declaration;

import br.com.objectos.code.annotations.Ignore;
import br.com.objectos.code.java.element.AbstractCodeElement;
import br.com.objectos.code.java.element.AbstractImmutableCodeElement;
import br.com.objectos.code.java.element.CodeElement;
import br.com.objectos.code.java.element.Symbols;
import br.com.objectos.code.java.io.CodeWriter;
import br.com.objectos.code.java.type.NamedClass;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import objectos.lang.Check;
import objectos.util.GrowableList;
import objectos.util.GrowableSet;

public final class AnnotationCode
    extends AbstractImmutableCodeElement
    implements
    ClassCodeElement,
    EnumCodeElement,
    InterfaceCodeElement,
    MethodCodeElement {

  private AnnotationCode(CodeElement... elements) {
    super(elements);
  }

  public static AnnotationCode annotation(AnnotationCodeElement... elements) {
    Check.notNull(elements, "elements == null");
    Builder b = builder();

    for (int i = 0; i < elements.length; i++) {
      AnnotationCodeElement element = elements[i];

      if (element == null) {
        throw new NullPointerException("elements[" + i + "] == null");
      }

      element.acceptAnnotationCodeBuilder(b);
    }

    return b.build();
  }

  public static AnnotationCode annotation(
      AnnotationCodeElement v1,
      AnnotationCodeElement v2) {
    Check.notNull(v1, "v1 == null");
    Check.notNull(v2, "v2 == null");
    Builder b = builder();
    v1.acceptAnnotationCodeBuilder(b);
    v2.acceptAnnotationCodeBuilder(b);
    return b.build();
  }

  public static AnnotationCode annotation(
      AnnotationCodeElement v1,
      AnnotationCodeElement v2,
      AnnotationCodeElement v3) {
    Check.notNull(v1, "v1 == null");
    Check.notNull(v2, "v2 == null");
    Check.notNull(v3, "v3 == null");
    Builder b = builder();
    v1.acceptAnnotationCodeBuilder(b);
    v2.acceptAnnotationCodeBuilder(b);
    v3.acceptAnnotationCodeBuilder(b);
    return b.build();
  }

  public static AnnotationCode annotation(
      AnnotationCodeElement v1,
      AnnotationCodeElement v2,
      AnnotationCodeElement v3,
      AnnotationCodeElement v4) {
    Check.notNull(v1, "v1 == null");
    Check.notNull(v2, "v2 == null");
    Check.notNull(v3, "v3 == null");
    Check.notNull(v4, "v4 == null");
    Builder b = builder();
    v1.acceptAnnotationCodeBuilder(b);
    v2.acceptAnnotationCodeBuilder(b);
    v3.acceptAnnotationCodeBuilder(b);
    v4.acceptAnnotationCodeBuilder(b);
    return b.build();
  }

  public static AnnotationCode annotation(
      AnnotationCodeElement v1,
      AnnotationCodeElement v2,
      AnnotationCodeElement v3,
      AnnotationCodeElement v4,
      AnnotationCodeElement v5) {
    Check.notNull(v1, "v1 == null");
    Check.notNull(v2, "v2 == null");
    Check.notNull(v3, "v3 == null");
    Check.notNull(v4, "v4 == null");
    Check.notNull(v5, "v5 == null");
    Builder b = builder();
    v1.acceptAnnotationCodeBuilder(b);
    v2.acceptAnnotationCodeBuilder(b);
    v3.acceptAnnotationCodeBuilder(b);
    v4.acceptAnnotationCodeBuilder(b);
    v5.acceptAnnotationCodeBuilder(b);
    return b.build();
  }

  public static AnnotationCode annotation(
      AnnotationCodeElement v1,
      AnnotationCodeElement v2,
      AnnotationCodeElement v3,
      AnnotationCodeElement v4,
      AnnotationCodeElement v5,
      AnnotationCodeElement v6) {
    Check.notNull(v1, "v1 == null");
    Check.notNull(v2, "v2 == null");
    Check.notNull(v3, "v3 == null");
    Check.notNull(v4, "v4 == null");
    Check.notNull(v5, "v5 == null");
    Check.notNull(v6, "v6 == null");
    Builder b = builder();
    v1.acceptAnnotationCodeBuilder(b);
    v2.acceptAnnotationCodeBuilder(b);
    v3.acceptAnnotationCodeBuilder(b);
    v4.acceptAnnotationCodeBuilder(b);
    v5.acceptAnnotationCodeBuilder(b);
    v6.acceptAnnotationCodeBuilder(b);
    return b.build();
  }

  public static AnnotationCode annotation(
      AnnotationCodeElement v1,
      AnnotationCodeElement v2,
      AnnotationCodeElement v3,
      AnnotationCodeElement v4,
      AnnotationCodeElement v5,
      AnnotationCodeElement v6,
      AnnotationCodeElement v7) {
    Check.notNull(v1, "v1 == null");
    Check.notNull(v2, "v2 == null");
    Check.notNull(v3, "v3 == null");
    Check.notNull(v4, "v4 == null");
    Check.notNull(v5, "v5 == null");
    Check.notNull(v6, "v6 == null");
    Check.notNull(v7, "v7 == null");
    Builder b = builder();
    v1.acceptAnnotationCodeBuilder(b);
    v2.acceptAnnotationCodeBuilder(b);
    v3.acceptAnnotationCodeBuilder(b);
    v4.acceptAnnotationCodeBuilder(b);
    v5.acceptAnnotationCodeBuilder(b);
    v6.acceptAnnotationCodeBuilder(b);
    v7.acceptAnnotationCodeBuilder(b);
    return b.build();
  }

  public static AnnotationCode annotation(
      AnnotationCodeElement v1,
      AnnotationCodeElement v2,
      AnnotationCodeElement v3,
      AnnotationCodeElement v4,
      AnnotationCodeElement v5,
      AnnotationCodeElement v6,
      AnnotationCodeElement v7,
      AnnotationCodeElement v8) {
    Check.notNull(v1, "v1 == null");
    Check.notNull(v2, "v2 == null");
    Check.notNull(v3, "v3 == null");
    Check.notNull(v5, "v5 == null");
    Check.notNull(v4, "v4 == null");
    Check.notNull(v6, "v6 == null");
    Check.notNull(v7, "v7 == null");
    Check.notNull(v8, "v8 == null");
    Builder b = builder();
    v1.acceptAnnotationCodeBuilder(b);
    v2.acceptAnnotationCodeBuilder(b);
    v3.acceptAnnotationCodeBuilder(b);
    v4.acceptAnnotationCodeBuilder(b);
    v5.acceptAnnotationCodeBuilder(b);
    v6.acceptAnnotationCodeBuilder(b);
    v7.acceptAnnotationCodeBuilder(b);
    v8.acceptAnnotationCodeBuilder(b);
    return b.build();
  }

  public static AnnotationCode annotation(
      AnnotationCodeElement v1,
      AnnotationCodeElement v2,
      AnnotationCodeElement v3,
      AnnotationCodeElement v4,
      AnnotationCodeElement v5,
      AnnotationCodeElement v6,
      AnnotationCodeElement v7,
      AnnotationCodeElement v8,
      AnnotationCodeElement v9) {
    Check.notNull(v1, "v1 == null");
    Check.notNull(v2, "v2 == null");
    Check.notNull(v3, "v3 == null");
    Check.notNull(v5, "v5 == null");
    Check.notNull(v4, "v4 == null");
    Check.notNull(v6, "v6 == null");
    Check.notNull(v7, "v7 == null");
    Check.notNull(v8, "v8 == null");
    Check.notNull(v9, "v9 == null");
    Builder b = builder();
    v1.acceptAnnotationCodeBuilder(b);
    v2.acceptAnnotationCodeBuilder(b);
    v3.acceptAnnotationCodeBuilder(b);
    v4.acceptAnnotationCodeBuilder(b);
    v5.acceptAnnotationCodeBuilder(b);
    v6.acceptAnnotationCodeBuilder(b);
    v7.acceptAnnotationCodeBuilder(b);
    v8.acceptAnnotationCodeBuilder(b);
    v9.acceptAnnotationCodeBuilder(b);
    return b.build();
  }

  public static AnnotationCode annotation(Class<? extends Annotation> annotationType) {
    return annotation(
        NamedClass.ofWithNullMessage(annotationType, "annotationType == null")
    );
  }

  public static AnnotationCode annotation(
      Class<? extends Annotation> annotationType,
      AnnotationCodeValue value) {
    return annotation(
        NamedClass.ofWithNullMessage(annotationType, "annotationType == null"),
        value
    );
  }

  public static AnnotationCode annotation(NamedClass className) {
    Builder b = builder();
    b.setClassName(className);
    return b.build();
  }

  public static AnnotationCode annotation(
      NamedClass className,
      AnnotationCodeValue value) {
    Builder b = builder();
    b.setClassName(className);
    b.setSingleValue(value);
    return b.build();
  }

  @Ignore("AggregatorGenProcessor")
  public static Builder builder() {
    return new Builder();
  }

  @Override
  public final void acceptClassCodeBuilder(ClassCode.Builder builder) {
    builder.addAnnotation(this);
  }

  @Override
  public final void acceptEnumCodeBuilder(EnumCode.Builder builder) {
    builder.addAnnotation(this);
  }

  @Override
  public final void acceptInterfaceCodeBuilder(InterfaceCode.Builder builder) {
    builder.addAnnotation(this);
  }

  @Override
  public final void acceptMethodCodeBuilder(MethodCode.Builder builder) {
    builder.addAnnotation(this);
  }

  public static class Builder {

    NamedClass className;
    CodeElement singleValue;

    private Mode mode = Mode.MARKER;

    private final Set<String> names = new GrowableSet<>();

    private final List<AnnotationCodeValuePair> pairs = new GrowableList<>();

    private Builder() {}

    public final Builder add(AnnotationCodeValuePair value) {
      Check.notNull(value, "value == null");
      addUnchecked(value);
      return this;
    }

    public final AnnotationCode build() {
      return mode.build(this);
    }

    public final Builder setClassName(NamedClass className) {
      this.className = Check.notNull(className, "className == null");
      return this;
    }

    public final Builder setSingleValue(AnnotationCodeValue value) {
      Check.notNull(value, "value == null");
      mode = mode.setValue(this, value);
      return this;
    }

    final void addIfPossible(AnnotationCodeValuePair value) {
      String name;
      name = value.name();

      if (!names.add(name)) {
        throw new IllegalArgumentException("Element with name " + name + " was already defined.");
      }

      pairs.add(value);
    }

    final void addUnchecked(AnnotationCodeValuePair value) {
      mode = mode.add(this, value);
    }

    final AnnotationCode buildMarkerAnnotation() {
      return new AnnotationCode(
          className()
      );
    }

    final AnnotationCode buildNormalAnnotation() {
      return new AnnotationCode(
          className(),
          parenthesized(
              commaSeparated(pairs)
          )
      );
    }

    final AnnotationCode buildSingleElementAnnotation() {
      return new AnnotationCode(
          className(),
          parenthesized(singleValue)
      );
    }

    private AnnotationClassName className() {
      return new AnnotationClassName(className);
    }

  }

  private static class AnnotationClassName extends AbstractCodeElement {

    private final NamedClass className;

    AnnotationClassName(NamedClass className) {
      this.className = className;
    }

    @Override
    public final CodeWriter acceptCodeWriter(CodeWriter w) {
      w.writeCodeElement(Symbols.at());
      w.writeNamedType(className);
      return w;
    }

  }

  private enum Mode {

    MARKER {
      @Override
      final AnnotationCode build(Builder builder) {
        return builder.buildMarkerAnnotation();
      }
    },

    NORMAL {
      @Override
      final AnnotationCode build(Builder builder) {
        return builder.buildNormalAnnotation();
      }

      @Override
      final Mode setValue(Builder builder, AnnotationCodeValue value) {
        throw allValuesMustBeNamed();
      }
    },

    SINGLE {
      @Override
      final Mode add(Builder builder, AnnotationCodeValuePair value) {
        throw allValuesMustBeNamed();
      }

      @Override
      final AnnotationCode build(Builder builder) {
        return builder.buildSingleElementAnnotation();
      }
    };

    Mode add(Builder builder, AnnotationCodeValuePair value) {
      builder.addIfPossible(value);
      return NORMAL;
    }

    final IllegalStateException allValuesMustBeNamed() {
      return new IllegalStateException(
          "All values must be named in a normal annotation. See JLS 9.7.1");
    }

    abstract AnnotationCode build(Builder builder);

    Mode setValue(Builder builder, AnnotationCodeValue value) {
      builder.singleValue = value;
      return SINGLE;
    }

  }

}