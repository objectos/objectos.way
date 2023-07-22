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
package objectos.selfgen.css2.util;

import objectos.code.ClassTypeName;
import objectos.util.UnmodifiableList;

final class Property {

  public final ClassTypeName className;

  public final UnmodifiableList<NamedArguments> names;

  private Property(ClassTypeName className, UnmodifiableList<NamedArguments> names) {
    this.className = className;
    this.names = names;
  }

  public static Property of(Prefix prefix, SimpleName simpleName, Methods methods, Names names) {
    ClassTypeName enclosing;
    enclosing = prefix.className;

    ClassTypeName className;
    className = ClassTypeName.of(enclosing, simpleName.name());

    return new Property(className, names.values());
  }

}