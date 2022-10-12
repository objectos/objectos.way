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
package br.com.objectos.code.java.io;

import br.com.objectos.code.java.element.CodeElement;
import br.com.objectos.code.java.type.NamedType;
import br.com.objectos.code.java.type.NamedTypeParameter;
import java.util.ArrayDeque;
import java.util.Deque;

public class CodeWriter {

  private static final int INDENTATION_SIZE = 2;

  private final ImportSet importSet;

  private Indentation indentation = Indentation.start();

  private int length;

  private final StringBuilder out = new StringBuilder();

  private final Deque<String> simpleNameStack = new ArrayDeque<String>();

  private CodeWriter(ImportSet importSet) {
    this.importSet = importSet;
  }

  public static CodeWriter forJavaFile(ImportSet importSet) {
    return new CodeWriter(importSet);
  }

  public static CodeWriter forToString() {
    ImportSet importSet = ImportSet.forToString();
    return new CodeWriter(importSet);
  }

  public final CodeWriter beginSection(Section kind) {
    indentation = indentation.push(kind);
    return this;
  }

  public final int charCount() {
    return out.length();
  }

  public final CodeWriter endSection() {
    indentation = indentation.pop();
    return this;
  }

  public final CodeWriter nextLine() {
    indentation.nextLine();
    return write('\n').resetLength();
  }

  public final CodeWriter popSimpleName() {
    simpleNameStack.pop();
    return this;
  }

  public final CodeWriter pushSimpleName(String simpleName) {
    simpleNameStack.push(simpleName);
    return this;
  }

  public final String toJavaFile() {
    StringBuilder out = new StringBuilder();
    out.append(importSet);

    if (!importSet.isEmpty()) {
      out.append('\n');
      out.append('\n');
    }

    out.append(toString());
    return out.toString();
  }

  @Override
  public final String toString() {
    return out.toString();
  }

  public final CodeWriter write(char c) {
    append(c);
    length++;
    return this;
  }

  public final CodeWriter write(String string) {
    append(string);
    length += string.length();
    return this;
  }

  public final CodeWriter writeCodeElement(CodeElement element) {
    return element.acceptCodeWriter(this);
  }

  public final CodeWriter writeCodeElements(Iterable<? extends CodeElement> elements) {
    for (CodeElement element : elements) {
      element.acceptCodeWriter(this);
    }
    return this;
  }

  public final CodeWriter writeNamedType(NamedType name) {
    return writeCanGenerateImportDeclaration(name);
  }

  public final CodeWriter writePreIndentation() {
    if (length == 0) {
      writePreIndentation0();
    }
    return this;
  }

  public final void writePreIndentationOr(char c) {
    if (length == 0) {
      writePreIndentation0();
    } else {
      write(c);
    }
  }

  public final CodeWriter writeSimpleName() {
    return write(peekSimpleName());
  }

  public final CodeWriter writeTypeParameterName(NamedTypeParameter typeName) {
    return writeCanGenerateImportDeclaration(typeName);
  }

  //

  final CodeWriter resetLength() {
    length = 0;
    return this;
  }

  final CodeWriter writeIndentation(int count) {
    for (int i = 0; i < count * INDENTATION_SIZE; i++) {
      write(' ');
    }
    return this;
  }

  private void append(char c) {
    out.append(c);
  }

  private void append(String string) {
    out.append(string);
  }

  private String peekSimpleName() {
    if (simpleNameStack.isEmpty()) {
      throw new IllegalStateException("Stack of simple names is empty.");
    }
    return simpleNameStack.peek();
  }

  private final CodeWriter writeCanGenerateImportDeclaration(CanGenerateImportDeclaration element) {
    String s = importSet.get(element);
    return write(s);
  }

  private void writePreIndentation0() {
    indentation.acceptCodeWriter(this);
  }

}