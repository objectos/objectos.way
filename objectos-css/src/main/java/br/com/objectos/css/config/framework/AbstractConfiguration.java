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

import br.com.objectos.css.config.framework.ConfigurationDsl.FrameworMultiElement;
import br.com.objectos.css.config.framework.ConfigurationDsl.FrameworkAtMedia;
import br.com.objectos.css.config.framework.ConfigurationDsl.FrameworkAtMediaDeclaration;
import br.com.objectos.css.config.framework.ConfigurationDsl.FrameworkAtMediaElement;
import br.com.objectos.css.config.framework.ConfigurationDsl.FrameworkAtMediaSet;
import br.com.objectos.css.config.framework.ConfigurationDsl.FrameworkGroup;
import br.com.objectos.css.config.framework.ConfigurationDsl.FrameworkMethodSet;
import br.com.objectos.css.config.framework.ConfigurationDsl.FrameworkNamedValue;
import br.com.objectos.css.config.framework.ConfigurationDsl.FrameworkNamedValueSet;
import br.com.objectos.css.config.framework.ConfigurationDsl.FrameworkPropertyState;
import br.com.objectos.css.config.framework.ConfigurationDsl.FrameworkSimpleName;
import br.com.objectos.css.property.StandardPropertyName;
import br.com.objectos.css.type.Color;
import br.com.objectos.css.type.LengthUnit;
import br.com.objectos.css.type.Value;
import br.com.objectos.css.type.Zero;
import objectos.lang.Check;

public abstract class AbstractConfiguration implements Configuration {

  protected static final Zero _0 = Zero.INSTANCE;

  private ConfigurationDsl dsl;

  protected AbstractConfiguration() {}

  @Override
  public final void acceptConfigurationDsl(ConfigurationDsl dsl) {
    this.dsl = Check.notNull(dsl, "dsl == null");
    try {
      configure();
    } finally {
      this.dsl = null;
    }
  }

  protected abstract void configure();

  protected final FrameworkAtMediaDeclaration declaration(
      StandardPropertyName propertyName, Value value) {
    return dsl.getDeclaration(propertyName, value);
  }

  protected final Value em(double value) {
    return dsl.getLength(LengthUnit.EM, value);
  }

  protected final void install(Configuration configuration) {
    configuration.acceptConfigurationDsl(dsl);
  }

  protected final Value l(int value) {
    return dsl.getInt(value);
  }

  protected final FrameworkAtMedia media(
      String name, FrameworkAtMediaElement... elements) {
    return dsl.getAtMedia(name, elements);
  }

  protected final FrameworkAtMediaSet mediaSet(FrameworkAtMedia... queries) {
    return dsl.getAtMediaSet(queries);
  }

  protected final FrameworkMethodSet methods(String... names) {
    return dsl.getMethodSet(names);
  }

  protected final FrameworMultiElement multi(Value... values) {
    return dsl.getMultiElement(values);
  }

  protected final void packageName(String packageName) {
    dsl.setPackageName(packageName);
  }

  protected final Value pct(double value) {
    return dsl.getPercentage(value);
  }

  protected final Value pct(int value) {
    return dsl.getPercentage(value);
  }

  protected final void property(
      FrameworkGroup group,
      FrameworkSimpleName simpleName,
      FrameworkMethodSet methods,
      FrameworkNamedValueSet values) {
    property(group, simpleName, methods, values, mediaSet());
  }

  protected final void property(
      FrameworkGroup group,
      FrameworkSimpleName simpleName,
      FrameworkMethodSet methods,
      FrameworkNamedValueSet values,
      FrameworkAtMediaSet queries,
      FrameworkPropertyState... states) {
    dsl.defineProperty(group, simpleName, methods, values, queries, states);
  }

  protected final Value px(int value) {
    return dsl.getLength(LengthUnit.PX, value);
  }

  protected final Value rem(double value) {
    return dsl.getLength(LengthUnit.REM, value);
  }

  protected final Value rem(int value) {
    return dsl.getLength(LengthUnit.REM, value);
  }

  protected final Value rgb(int hex) {
    return Color.rgb(hex);
  }

  protected final Value rgba(int r, int g, int b, double alpha) {
    return dsl.getRgba(r, g, b, alpha);
  }

  protected final FrameworkSimpleName simpleName(String name) {
    return dsl.getSimpleName(name);
  }

  protected final FrameworkNamedValue v(String name, double value) {
    return dsl.getNamedValue(name, value);
  }

  protected final FrameworkNamedValue v(String name, FrameworkAtMediaSet value) {
    return dsl.getNamedValue(name, value);
  }

  protected final FrameworkNamedValue v(String name, FrameworMultiElement... arguments) {
    return dsl.getNamedValue(name, arguments);
  }

  protected final FrameworkNamedValue v(String name, int value) {
    return dsl.getNamedValue(name, value);
  }

  protected final FrameworkNamedValue v(String name, Value... values) {
    return dsl.getNamedValue(name, values);
  }

  protected final FrameworkNamedValueSet valueSet(FrameworkNamedValue... values) {
    return dsl.getNamedValueSet(values);
  }

  protected final Value vh(int value) {
    return dsl.getLength(LengthUnit.VH, value);
  }

  protected final Value vw(int value) {
    return dsl.getLength(LengthUnit.VW, value);
  }

}
