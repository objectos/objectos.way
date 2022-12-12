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
package objectos.code;

import java.util.Locale;

public enum Keyword {

  ABSTRACT,

  BOOLEAN,

  CLASS,

  DOUBLE,

  EXTENDS,

  FINAL,

  IMPORT,

  INT,

  NEW,

  PACKAGE,

  PRIVATE,

  PROTECTED,

  PUBLIC,

  RETURN,

  STATIC,

  THIS,

  VAR;

  private static final Keyword[] VALUES = values();

  private final String toString = name().toLowerCase(Locale.US);

  static Keyword get(int index) {
    return VALUES[index];
  }

  @Override
  public final String toString() { return toString; }

}