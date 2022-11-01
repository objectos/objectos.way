/*
 * Copyright 2022 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectox.code;

final class ByteProto {

  static final int NULL = Integer.MIN_VALUE;

  static final int JMP = -1;
  static final int BREAK = -2;

  static final int COMPILATION_UNIT = -3;
  static final int PACKAGE_DECLARATION = -4;
  static final int ANNOTATION = -6;
  static final int MODIFIER = -7;
  static final int CLASS_DECLARATION = -8;
  static final int EXTENDS = -9;
  static final int METHOD_DECLARATION = -10;

  static final int IDENTIFIER = -11;
  static final int CLASS_NAME = -12;
  static final int STRING_LITERAL = -13;

  static final int LOCAL_VARIABLE = -14;
  static final int METHOD_INVOCATION = -15;
  static final int NEW_LINE = -16;
  static final int TYPE_NAME = -17;
  static final int EXPRESSION_NAME = -18;
  static final int PACKAGE_NAME = -19;

  private ByteProto() {}

}