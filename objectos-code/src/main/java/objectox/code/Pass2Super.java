/*
 * Copyright 2022 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectox.code;

import objectos.code.JavaTemplate.Renderer;
import objectos.util.IntArrays;

abstract class Pass2Super {

  int[] codes;

  int cursor;

  Object[] objects;

  ImportSet importSet;

  Renderer processor;

  int[] stack = new int[16];

  int stackCursor;

  int code;

  final void codeadv() {
    code = codes[cursor++];
  }

  final void codeass(int value) {
    assert code == value : value;
  }

  final void codejmp() {
    codeadv();

    codeass(Pass1.JMP);

    codeadv();

    codepsh();
  }

  final boolean codenop() {
    return code == Pass1.NOP;
  }

  final boolean codenxt() {
    codeadv();

    return code != Pass1.NOP;
  }

  final Object codeobj() {
    return objects[code];
  }

  final int codepek() {
    return codes[code];
  }

  final void codepop() {
    cursor = stack[--stackCursor];
  }

  final void codepsh() {
    stack = IntArrays.growIfNecessary(stack, stackCursor);

    stack[stackCursor++] = cursor;

    cursor = code;

    codeadv();
  }

  final UnsupportedOperationException codeuoe() {
    return new UnsupportedOperationException("Implement me :: code=" + code);
  }

}
