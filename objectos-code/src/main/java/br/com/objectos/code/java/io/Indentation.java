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

abstract class Indentation implements CodeElement {

  Indentation() {}

  public static Indentation start() {
    return new Start();
  }

  public abstract Indentation nextLine();

  public abstract Indentation pop();

  public final Indentation push(Section section) {
    return new Rest(this, section);
  }

  abstract int indentation();

  private static class Start extends Indentation {
    @Override
    public final CodeWriter acceptCodeWriter(CodeWriter w) {
      return w;
    }

    @Override
    public final Indentation nextLine() {
      return this;
    }

    @Override
    public final Indentation pop() {
      throw new IllegalStateException("Cannot pop an empty SectionStack.");
    }

    @Override
    final int indentation() {
      return 0;
    }
  }

  private static class Rest extends Indentation {

    private final Indentation parent;
    private final Section section;

    private boolean nextLine;

    Rest(Indentation parent, Section section) {
      this.parent = parent;
      this.section = section;
    }

    @Override
    public CodeWriter acceptCodeWriter(CodeWriter w) {
      return nextLine ? w.writeIndentation(indentation()) : parent.acceptCodeWriter(w);
    }

    @Override
    public final Indentation nextLine() {
      nextLine = true;
      return this;
    }

    @Override
    public final Indentation pop() {
      return parent;
    }

    @Override
    final int indentation() {
      return parent.indentation()
          + (nextLine ? section.indentationOnNextLine : section.indentation);
    }

  }

}