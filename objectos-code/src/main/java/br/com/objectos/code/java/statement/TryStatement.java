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

import br.com.objectos.code.annotations.Ignore;
import br.com.objectos.code.java.element.Keywords;
import br.com.objectos.code.java.io.CodeWriter;
import java.util.Iterator;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;

public class TryStatement extends AbstractSimpleStatement {

  private final UnmodifiableList<CatchBlock> catchBlocks;
  private final Block finallyBlock;
  private final UnmodifiableList<ResourceElement> resources;
  private final Block tryBlock;

  private TryStatement(UnmodifiableList<ResourceElement> resources,
                       Block tryBlock,
                       UnmodifiableList<CatchBlock> catchBlocks,
                       Block finallyBlock) {
    this.resources = resources;
    this.tryBlock = tryBlock;
    this.catchBlocks = catchBlocks;
    this.finallyBlock = finallyBlock;
  }

  public static TryStatement _try(
      TryStatementElement e1,
      TryStatementElement e2) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Builder b = builder();
    e1.acceptTryStatementBuilder(b);
    e2.acceptTryStatementBuilder(b);
    return b.build();
  }

  public static TryStatement _try(
      TryStatementElement e1,
      TryStatementElement e2,
      TryStatementElement e3) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Builder b = builder();
    e1.acceptTryStatementBuilder(b);
    e2.acceptTryStatementBuilder(b);
    e3.acceptTryStatementBuilder(b);
    return b.build();
  }

  public static TryStatement _try(
      TryStatementElement e1,
      TryStatementElement e2,
      TryStatementElement e3,
      TryStatementElement e4) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Builder b = builder();
    e1.acceptTryStatementBuilder(b);
    e2.acceptTryStatementBuilder(b);
    e3.acceptTryStatementBuilder(b);
    e4.acceptTryStatementBuilder(b);
    return b.build();
  }

  public static TryStatement _try(
      TryStatementElement e1,
      TryStatementElement e2,
      TryStatementElement e3,
      TryStatementElement e4,
      TryStatementElement e5) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Builder b = builder();
    e1.acceptTryStatementBuilder(b);
    e2.acceptTryStatementBuilder(b);
    e3.acceptTryStatementBuilder(b);
    e4.acceptTryStatementBuilder(b);
    e5.acceptTryStatementBuilder(b);
    return b.build();
  }

  @Ignore
  public static Builder builder() {
    return new Builder();
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    w.writeCodeElement(Keywords._try());
    w.writeCodeElement(space());

    if (!resources.isEmpty()) {
      w.writeCodeElement(openParens());

      Iterator<ResourceElement> it = resources.iterator();
      if (it.hasNext()) {
        w.writeCodeElement(it.next());
        while (it.hasNext()) {
          w.writeCodeElement(semicolon());
          w.writeCodeElement(space());
          w.writeCodeElement(it.next());
        }
      }

      w.writeCodeElement(closeParens());
      w.writeCodeElement(space());
    }

    w.writeCodeElement(tryBlock);

    for (CatchBlock catchBlock : catchBlocks) {
      w.writeCodeElement(space());
      w.writeCodeElement(catchBlock);
    }

    if (finallyBlock != null) {
      w.writeCodeElement(space());
      w.writeCodeElement(Keywords._finally());
      w.writeCodeElement(space());
      w.writeCodeElement(finallyBlock);
    }

    return w;
  }

  public static class Builder {

    final GrowableList<Block.Builder> catchBlocks = new GrowableList<>();

    final GrowableList<CatchElement> catchElements = new GrowableList<>();

    Block.Builder currentBlock;

    Block.Builder finallyBlock;

    final GrowableList<ResourceElement> resources = new GrowableList<>();

    final Block.Builder tryBlock = Block.builder();

    private Builder() {
      currentBlock = tryBlock;
    }

    public final Builder addCatchElement(CatchElement catchElement) {
      catchElements.addWithNullMessage(catchElement, "catchElement == null");
      currentBlock = Block.builder();
      catchBlocks.add(currentBlock);
      return this;
    }

    public final Builder addFinally() {
      if (finallyBlock == null) {
        finallyBlock = Block.builder();
      }
      currentBlock = finallyBlock;
      return this;
    }

    public final Builder addResource(ResourceElement resource) {
      resources.addWithNullMessage(resource, "resource == null");
      return this;
    }

    public final Builder addStatement(BlockStatement statement) {
      currentBlock.addWithNullMessage(statement, "statement == null");
      return this;
    }

    public final TryStatement build() {
      return new TryStatement(
          resources.toUnmodifiableList(),
          tryBlock.build(),
          buildCatchBlocks(),
          buildFinallyBlock()
      );
    }

    private UnmodifiableList<CatchBlock> buildCatchBlocks() {
      GrowableList<CatchBlock> blocks = new GrowableList<>();

      for (int i = 0; i < catchElements.size(); i++) {
        blocks.add(
            new CatchBlock(
                catchElements.get(i),
                catchBlocks.get(i).build()
            )
        );
      }

      return blocks.toUnmodifiableList();
    }

    private Block buildFinallyBlock() {
      return finallyBlock != null ? finallyBlock.build() : null;
    }

  }

}
