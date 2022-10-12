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
package br.com.objectos.code.java.element;

import br.com.objectos.code.annotations.Ignore;
import br.com.objectos.code.java.declaration.Modifiers;

public class Keywords {

  private static final Keyword ASSERT = KeywordImpl.named("assert");
  private static final Keyword CASE = KeywordImpl.named("case");
  private static final Keyword CATCH = KeywordImpl.named("catch");
  private static final Keyword CLASS = KeywordImpl.named("class");
  private static final Keyword DEFAULT = KeywordImpl.named("default");
  private static final Keyword DO = KeywordImpl.named("do");
  private static final Keyword ENUM = KeywordImpl.named("enum");
  private static final Keyword EXTENDS = KeywordImpl.named("extends");
  private static final Keyword FINAL = KeywordImpl.named("final");
  private static final Keyword FOR = KeywordImpl.named("for");
  private static final Keyword IF = KeywordImpl.named("if");
  private static final Keyword IMPLEMENTS = KeywordImpl.named("implements");
  private static final Keyword INTERFACE = KeywordImpl.named("interface");
  private static final Keyword NEW = KeywordImpl.named("new");
  private static final Keyword PRIVATE = KeywordImpl.named("private");
  private static final Keyword PROTECTED = KeywordImpl.named("protected");
  private static final Keyword PUBLIC = KeywordImpl.named("public");
  private static final Keyword STATIC = KeywordImpl.named("static");
  private static final Keyword SWITCH = KeywordImpl.named("switch");
  private static final Keyword THROW = KeywordImpl.named("throw");
  private static final Keyword THROWS = KeywordImpl.named("throws");
  private static final Keyword TRANSIENT = KeywordImpl.named("transient");
  private static final Keyword TRY = KeywordImpl.named("try");
  private static final Keyword VOLATILE = KeywordImpl.named("volatile");
  private static final Keyword WHILE = KeywordImpl.named("while");

  private Keywords() {}

  public static Keyword _assert() {
    return ASSERT;
  }

  public static BreakKeyword _break() {
    return BreakKeyword.INSTANCE;
  }

  public static Keyword _case() {
    return CASE;
  }

  public static Keyword _catch() {
    return CATCH;
  }

  public static Keyword _class() {
    return CLASS;
  }

  public static ContinueKeyword _continue() {
    return ContinueKeyword.INSTANCE;
  }

  @Ignore("Use the on in Declarations")
  public static Keyword _default() {
    return DEFAULT;
  }

  public static Keyword _do() {
    return DO;
  }

  public static ElseKeyword _else() {
    return ElseKeyword.INSTANCE;
  }

  public static Keyword _enum() {
    return ENUM;
  }

  public static Keyword _extends() {
    return EXTENDS;
  }

  @Ignore("Use the on in Declarations")
  public static Keyword _final() {
    return FINAL;
  }

  public static FinallyKeyword _finally() {
    return FinallyKeyword.INSTANCE;
  }

  public static Keyword _for() {
    return FOR;
  }

  public static Keyword _if() {
    return IF;
  }

  public static Keyword _implements() {
    return IMPLEMENTS;
  }

  public static Keyword _interface() {
    return INTERFACE;
  }

  public static Keyword _new() {
    return NEW;
  }

  @Ignore("Use the on in Declarations")
  public static Keyword _private() {
    return PRIVATE;
  }

  @Ignore("Use the on in Declarations")
  public static Keyword _protected() {
    return PROTECTED;
  }

  @Ignore("Use the on in Declarations")
  public static Keyword _public() {
    return PUBLIC;
  }

  public static ReturnKeyword _return() {
    return ReturnKeyword.INSTANCE;
  }

  @Ignore("Use the on in Declarations")
  public static Keyword _static() {
    return STATIC;
  }

  public static SuperKeyword _super() {
    return SuperKeyword._super();
  }

  public static Keyword _switch() {
    return SWITCH;
  }

  @Ignore("Use the on in Declarations")
  public static Keyword _synchronized() {
    return Modifiers._synchronized();
  }

  public static ThisKeyword _this() {
    return ThisKeyword._this();
  }

  public static Keyword _throw() {
    return THROW;
  }

  public static Keyword _throws() {
    return THROWS;
  }

  @Ignore("Use the on in Declarations")
  public static Keyword _transient() {
    return TRANSIENT;
  }

  public static Keyword _try() {
    return TRY;
  }

  @Ignore("Use the on in Declarations")
  public static Keyword _volatile() {
    return VOLATILE;
  }

  public static Keyword _while() {
    return WHILE;
  }

}
