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

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import objectos.selfgen.css2.Signature.Style;
import objectos.selfgen.util.JavaNames;
import objectos.util.GrowableList;

final class Property {

  static final Comparator<? super Property> ORDER_BY_CONSTANT_NAME
      = (self, that) -> self.constantName.compareTo(that.constantName);

  static final Comparator<? super Property> ORDER_BY_METHOD_NAME
      = (self, that) -> self.methodName.compareTo(that.methodName);

  public final String propertyName;

  public final String methodName;

  public final String constantName;

  private final List<Signature> signatures = new GrowableList<>();

  private Property(String propertyName, String methodName, String constantName) {
    this.propertyName = propertyName;
    this.methodName = methodName;
    this.constantName = constantName;
  }

  public static Property of(String name) {
    var propertyName = Objects.requireNonNull(name, "name == null");

    var methodName = JavaNames.toValidMethodName(propertyName);

    var constantName = propertyName.replace('-', '_').toUpperCase(Locale.US);

    return new Property(propertyName, methodName, constantName);
  }

  public final void addSignature(Signature signature) {
    signatures.add(signature);
  }

  public final Iterable<Signature> signatures() {
    return signatures;
  }

  public final void addSignature(Style style, ParameterType parameterType) {
    signatures.add(
      new Signature(style, parameterType)
    );
  }

}