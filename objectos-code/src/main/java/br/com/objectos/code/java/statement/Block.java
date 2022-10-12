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
package br.com.objectos.code.java.statement;

import static br.com.objectos.code.java.element.NewLine.nextLine;

import br.com.objectos.code.annotations.Ignore;
import br.com.objectos.code.java.expression.LambdaBody;
import br.com.objectos.code.java.io.CodeWriter;
import br.com.objectos.code.java.io.Section;
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;

public class Block extends AbstractSimpleStatement implements LambdaBody {

  private static final Block EMPTY = new Block(
      UnmodifiableList.<BlockElement> of()
  );

  private final UnmodifiableList<BlockElement> elements;

  private Block(UnmodifiableList<BlockElement> elements) {
    this.elements = elements;
  }

  public static Block block() {
    return EMPTY;
  }

  public static Block block(BlockElement... elements) {
    return new Block(UnmodifiableList.copyOf(elements));
  }

  public static Block block(Iterable<? extends BlockElement> elements) {
    return new Block(UnmodifiableList.copyOf(elements));
  }

  @Ignore
  public static Builder builder() {
    return new Builder();
  }

  @Ignore
  public static Block empty() {
    return EMPTY;
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    w.writeCodeElement(openBrace());
    w.beginSection(Section.BLOCK);

    int count = w.charCount();

    for (BlockElement element : elements) {
      w.writeCodeElement(nextLine());
      w.beginSection(Section.STATEMENT);
      w.writeCodeElement(element);
      w.endSection();
      writeSemicolonIfNecessary(w, element);
    }

    if (count != w.charCount()) {
      w.writeCodeElement(nextLine());
    }

    w.endSection();
    w.writeCodeElement(closeBrace());

    return w;
  }

  @Override
  public final void acceptSemicolon(Semicolon semicolon) {
    // noop
  }

  public final boolean isEmpty() {
    return elements.isEmpty();
  }

  public static class Builder {

    private final GrowableList<BlockElement> elements = new GrowableList<>();

    private Builder() {}

    public final Builder add(BlockElement element) {
      return addWithNullMessage(element, "element == null");
    }

    public final Builder addAll(Iterable<? extends BlockElement> elements) {
      this.elements.addAllIterable(elements);
      return this;
    }

    public final Builder addWithNullMessage(BlockElement element, String message) {
      elements.addWithNullMessage(element, message);
      return this;
    }

    public final Block build() {
      return elements.isEmpty() ? empty() : new Block(elements.toUnmodifiableList());
    }

  }

}
