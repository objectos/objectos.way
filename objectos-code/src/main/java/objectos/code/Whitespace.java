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
 * Represents instructions to emit whitespace in the generated Java source code.
 *
 * @since 0.4
 */
public enum Whitespace {

  /**
   * Indicates that a mandatory whitespace must be emitted.
   *
   * <p>
   * For example, in the following field declaration:
   *
   * <pre>
   * private final String name;</pre>
   *
   * <p>
   * Every keyword and identifier must be separated by whitespace.
   */
  MANDATORY,

  /**
   * Indicates that a whitespace is typically emitted though it is not strictly
   * required.
   *
   * <p>
   * For example, in the following assignment operator expression:
   *
   * <pre>
   * a = b</pre>
   *
   * <p>
   * The spaces around the {@code =} operator are optional.
   */
  OPTIONAL,

  /**
   * Indicates that an user-provided line separator must be emitted.
   */
  NEW_LINE,

  /**
   * Indicates that the next language construct comes after a declaration
   * annotation.
   *
   * <p>
   * In the following method declaration:
   *
   * <pre>
   * &#064;A
   * &#064;B
   * public &#064;Nullable String method() {
   *   ...
   * }</pre>
   *
   * <p>
   * This instruction would appear after annotations {@code A} and {@code B}.
   * On the other hand, the instruction would <em>not</em> appear after the
   * annotation {@code Nullable}.
   */
  AFTER_ANNOTATION,

  /**
   * Indicates that the first construct of a line will be written in the next
   * instruction.
   *
   * <p>
   * It is typically used to indicate that indentation, if any, should be
   * emitted.
   */
  BEFORE_FIRST_LINE_CONTENT,

  /**
   * Indicates that the first member of a declaration will be written in the
   * next instructions.
   *
   * <p>
   * For example, in the following enum declaration:
   *
   * <pre>
   * enum Example {
   *   FOO;
   * }</pre>
   *
   * <p>
   * This instruction would appear immediately after the opening right curly
   * bracket of the enum's body.
   */
  BEFORE_FIRST_MEMBER,

  /**
   * Indicates that a member other than the first one will be written in the
   * next instructions.
   *
   * <p>
   * For example, in the following enum declaration:
   *
   * <pre>
   * enum Example {
   *   ONE,
   *
   *   TWO,
   *
   *   THREE;
   * }</pre>
   *
   * <p>
   * This instruction would appear immediately after the two commas.
   */
  BEFORE_NEXT_MEMBER,

  /**
   * Indicates that a Java statement is about to be written.
   *
   * <p>
   * For example, in the following method declaration:
   *
   * <pre>
   * void method() {
   *   a();
   *   b();
   *   c();
   * }</pre>
   *
   * <p>
   * This instruction would appear before each of the method invocations.
   */
  BEFORE_NEXT_STATEMENT,

  /**
   * Indicates that the next construct comes after a comma of a comma
   * separated list.
   *
   * <p>
   * For example, in the following method invocation expression:
   *
   * <pre>
   * foo(1, 2, 3)</pre>
   *
   * <p>
   * This instruction would appear immediately before the values {@code 2} and
   * {@code 3}.
   */
  BEFORE_NEXT_COMMA_SEPARATED_ITEM,

  /**
   * Indicates that closing right curly bracket of a non-empty body or block is
   * about to be emitted.
   */
  BEFORE_NON_EMPTY_BLOCK_END,

  /**
   * Indicates that closing right curly bracket of an empty body or block is
   * about to be emitted.
   */
  BEFORE_EMPTY_BLOCK_END;

  private static final Whitespace[] VALUES = values();

  static Whitespace get(int index) {
    return VALUES[index];
  }

}