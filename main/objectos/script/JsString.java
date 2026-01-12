/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectos.script;

/// A JS runtime `String` instance.
public final class JsString extends JsObject {

  /// Represents the `string` JS type.
  public static final JsType<JsString> type = new JsType<>(
      JsString.raw("string"), JsString::new
  );

  // operations
  static final JsString AX = raw("AX"); // argsRead
  static final JsString CR = raw("CR"); // contextRead
  static final JsString CW = raw("CW"); // contextWrite
  static final JsString EI = raw("EI"); // elementById
  static final JsString ET = raw("ET"); // elementTarget
  static final JsString FE = raw("FE"); // forEach
  static final JsString FN = raw("FN"); // functionJs
  static final JsString GR = raw("GR"); // globalRead
  static final JsString IF = raw("IF"); // if
  static final JsString IU = raw("IU"); // invokeUnchecked
  static final JsString IV = raw("IV"); // invokeVirtual
  static final JsString JS = raw("JS"); // jsValue
  static final JsString MO = raw("MO"); // morph
  static final JsString NO = raw("NO"); // no-op
  static final JsString PR = raw("PR"); // propertyRead
  static final JsString pr = raw("pr"); // propertyReadUnchecked
  static final JsString PW = raw("PW"); // propertyWrite
  static final JsString TE = raw("TE"); // throwError
  static final JsString TY = raw("TY"); // typeEnsure
  static final JsString W1 = raw("W1"); // wayOne
  static final JsString WS = raw("WS"); // waySeq

  // types
  static final JsString Array = raw("Array");

  private JsString(Object value) {
    super(value);
  }

  private JsString(JsBase recv, JsOp op) {
    super(recv, op);
  }

  public static JsString of(String s) {
    final String v;
    v = quote(s);

    final JsOp op;
    op = JsOp.of(JS, v);

    return new JsString(op);
  }

  static JsString raw(String s) {
    final String v;
    v = quote(s);

    return new JsString(v);
  }

  private static String quote(String s) {
    if (s == null) {
      throw new NullPointerException("Cannot quote a null string value");
    }

    // TODO escape json string literal
    return '"' + s + '"';
  }

}
