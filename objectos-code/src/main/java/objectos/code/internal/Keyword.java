/*
 * Copyright (C) 2014-2023 Objectos Software LTDA.
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
package objectos.code.internal;

import java.util.Locale;

/**
 * The reserved and contextual keywords of the Java programming language.
 *
 * @since 0.4
 */
public enum Keyword {

  /**
   * The reserved keyword {@code abstract}.
   */
  ABSTRACT,

  /**
   * The reserved keyword {@code boolean}.
   */
  BOOLEAN,

  /**
   * The reserved keyword {@code class}.
   */
  CLASS,

  /**
   * The reserved keyword {@code double}.
   */
  DOUBLE,

  /**
   * The reserved keyword {@code else}.
   */
  ELSE,

  /**
   * The reserved keyword {@code enum}.
   */
  ENUM,

  /**
   * The reserved keyword {@code extends}.
   */
  EXTENDS,

  /**
   * The reserved keyword {@code final}.
   */
  FINAL,

  /**
   * The reserved keyword {@code if}.
   *
   * @since 0.4.1
   */
  IF,

  /**
   * The reserved keyword {@code implements}.
   */
  IMPLEMENTS,

  /**
   * The reserved keyword {@code import}.
   */
  IMPORT,

  /**
   * The reserved keyword {@code import}.
   */
  INT,

  /**
   * The reserved keyword {@code interface}.
   */
  INTERFACE,

  /**
   * The reserved keyword {@code new}.
   */
  NEW,

  /**
   * The reserved keyword {@code null}.
   *
   * @since 0.4.1
   */
  NULL,

  /**
   * The reserved keyword {@code package}.
   */
  PACKAGE,

  /**
   * The reserved keyword {@code private}.
   */
  PRIVATE,

  /**
   * The reserved keyword {@code protected}.
   */
  PROTECTED,

  /**
   * The reserved keyword {@code public}.
   */
  PUBLIC,

  /**
   * The reserved keyword {@code return}.
   */
  RETURN,

  /**
   * The reserved keyword {@code static}.
   */
  STATIC,

  /**
   * The reserved keyword {@code super}.
   */
  SUPER,

  /**
   * The reserved keyword {@code this}.
   */
  THIS,

  /**
   * The reserved keyword {@code throw}.
   *
   * @since 0.4.1
   */
  THROW,

  /**
   * The contextual keyword {@code var}.
   */
  VAR,

  /**
   * The reserved keyword {@code void}.
   */
  VOID;

  private static final Keyword[] VALUES = values();

  private final String toString = name().toLowerCase(Locale.US);

  static Keyword get(int index) {
    return VALUES[index];
  }

  /**
   * Returns the name of the keyword in the proper case to be used in a Java
   * program.
   *
   * @return the keywords's name
   */
  @Override
  public final String toString() { return toString; }

}