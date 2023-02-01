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
package objectos.code;

/**
 * An instruction that may be used by a {@link JavaSink} instance to increase
 * and decrease indentation level during a code generation.
 *
 * @since 0.4
 */
public enum Indentation {

  /**
   * Indicates that a construct continues in the following line.
   *
   * <p>
   * For example, in the following chained method invocation expressions:
   *
   * <pre>
   * builder()
   *     .name("Foo bar")
   *     .value(123)
   *     .build();</pre>
   *
   * <p>
   * This instruction would appear immediately before each of dot {@code .}
   * character.
   */
  CONTINUATION,

  /**
   * Indicates that the source code generation has entered a body or block
   * construct. In other words, generation will occur inside curly brackets.
   */
  ENTER_BLOCK,

  /**
   * Indicates that the source code generation has exited a body or block
   * construct.
   */
  EXIT_BLOCK,

  /**
   * Indicates that the source code generation has entered a parenthesized
   * construct.
   */
  ENTER_PARENTHESIS,

  /**
   * Indicates that the source code generation has exited a parenthesized
   * construct.
   */
  EXIT_PARENTHESIS;

  private static final Indentation[] VALUES = values();

  static Indentation get(int index) {
    return VALUES[index];
  }

}