/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.selfgen.css2;

import objectos.code.ClassTypeName;
import objectos.util.UnmodifiableList;

public final class ValueType implements ParameterType {

  public final ClassTypeName className;

  public final UnmodifiableList<Value> values;

  ValueType(ClassTypeName className, UnmodifiableList<Value> values) {
    this.className = className;
    this.values = values;
  }

}