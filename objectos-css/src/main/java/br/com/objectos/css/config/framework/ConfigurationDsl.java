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
package br.com.objectos.css.config.framework;

import br.com.objectos.css.property.StandardPropertyName;
import br.com.objectos.css.type.LengthUnit;
import br.com.objectos.css.type.Value;

public interface ConfigurationDsl {

  interface FrameworkAtMedia extends FrameworkObject {}

  interface FrameworkAtMediaDeclaration extends FrameworkAtMediaElement {}

  interface FrameworkAtMediaElement extends FrameworkObject {}

  interface FrameworkAtMediaSet extends FrameworkObject {}

  enum FrameworkGroup {

    BACKGROUND,

    BORDER,

    EFFECTS,

    FLEXBOX,

    INTERACTIVITY,

    LAYOUT,

    SIZING,

    SPACING,

    TYPOGRAPHY;

  }

  interface FrameworkMethodSet extends FrameworkObject {}

  interface FrameworkNamedValue extends FrameworkObject {}

  interface FrameworkNamedValueSet extends FrameworkNamedValue {}

  interface FrameworkObject {
    void acceptFrameworkObjectVisitor(Object visitor);
  }

  enum FrameworkPropertyState {

    FIRST_CHILD("firstChild"),

    HOVER("hover");

    private final String interfaceName;

    private FrameworkPropertyState(String interfaceName) { this.interfaceName = interfaceName; }

    public final String interfaceName() {
      return interfaceName;
    }

  }

  interface FrameworkSimpleName extends FrameworkObject {}

  interface FrameworMultiElement extends FrameworkObject {}

  void defineProperty(
      FrameworkGroup group,
      FrameworkSimpleName simpleName,
      FrameworkMethodSet methods,
      FrameworkNamedValueSet values,
      FrameworkAtMediaSet queries,
      FrameworkPropertyState... states
  );

  FrameworkAtMedia getAtMedia(String name, FrameworkAtMediaElement... elements);

  FrameworkAtMediaSet getAtMediaSet(FrameworkAtMedia... values);

  FrameworkAtMediaDeclaration getDeclaration(StandardPropertyName propertyName, Value value);

  Value getInt(int value);

  Value getLength(LengthUnit unit, double value);

  Value getLength(LengthUnit unit, int value);

  FrameworkMethodSet getMethodSet(String... names);

  FrameworMultiElement getMultiElement(Value... values);

  FrameworkNamedValue getNamedValue(String name, double value);

  FrameworkNamedValue getNamedValue(String name, FrameworkAtMediaSet queries);

  FrameworkNamedValue getNamedValue(String name, FrameworMultiElement... elements);

  FrameworkNamedValue getNamedValue(String name, int value);

  FrameworkNamedValue getNamedValue(String name, Value... values);

  FrameworkNamedValueSet getNamedValueSet(FrameworkNamedValue... values);

  Value getPercentage(double value);

  Value getPercentage(int value);

  Value getRgba(int r, int g, int b, double alpha);

  FrameworkSimpleName getSimpleName(String name);

  void setPackageName(String packageName);

}
