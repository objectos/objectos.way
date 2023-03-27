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
   * The {@code abstract} reserved keyword.
   */
  ABSTRACT,

  /**
   * The {@code boolean} reserved keyword.
   */
  BOOLEAN,

  /**
   * The {@code class} reserved keyword.
   */
  CLASS,

  /**
   * The {@code double} reserved keyword.
   */
  DOUBLE,

  /**
   * The {@code else} reserved keyword.
   */
  ELSE,

  /**
   * The {@code enum} reserved keyword.
   */
  ENUM,

  /**
   * The {@code extends} reserved keyword.
   */
  EXTENDS,

  /**
   * The {@code final} reserved keyword.
   */
  FINAL,

  /**
   * The {@code if} reserved keyword.
   *
   * @since 0.4.1
   */
  IF,

  /**
   * The {@code implements} reserved keyword.
   */
  IMPLEMENTS,

  /**
   * The {@code import} reserved keyword.
   */
  IMPORT,

  /**
   * The {@code import} reserved keyword.
   */
  INT,

  /**
   * The {@code interface} reserved keyword.
   */
  INTERFACE,

  /**
   * The {@code new} reserved keyword.
   */
  NEW,

  /**
   * The {@code non-sealed} contextual keyword.
   *
   * @since 0.5.3
   */
  NON_SEALED("non-sealed"),

  /**
   * The {@code null} reserved keyword.
   *
   * @since 0.4.1
   */
  NULL,

  /**
   * The {@code package} reserved keyword.
   */
  PACKAGE,

  /**
   * The {@code private} reserved keyword.
   */
  PRIVATE,

  /**
   * The {@code protected} reserved keyword.
   */
  PROTECTED,

  /**
   * The {@code public} reserved keyword.
   */
  PUBLIC,

  /**
   * The {@code return} reserved keyword.
   */
  RETURN,

  /**
   * The {@code sealed} contextual keyword.
   *
   * @since 0.5.3
   */
  SEALED,

  /**
   * The {@code static} reserved keyword.
   */
  STATIC,

  /**
   * The {@code super} reserved keyword.
   */
  SUPER,

  /**
   * The {@code this} reserved keyword.
   */
  THIS,

  /**
   * The {@code throw} reserved keyword.
   *
   * @since 0.4.1
   */
  THROW,

  /**
   * The {@code var} contextual keyword.
   */
  VAR,

  /**
   * The {@code void} reserved keyword.
   */
  VOID;

  private static final Keyword[] VALUES = values();

  private final String toString;

  Keyword() {
    toString = name().toLowerCase(Locale.US);
  }

  Keyword(String toString) {
    this.toString = toString;
  }

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