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

import br.com.objectos.code.java.element.NewLine;
import objectos.util.GrowableList;

public class StatementOrBlockBuilder {

  private final GrowableList<BlockElement> blockElements = new GrowableList<>();

  private int blockStatementCount;
  private boolean forceBlock;
  private Statement lastStatement;

  private int statementCount;

  StatementOrBlockBuilder() {}

  public final StatementOrBlockBuilder nl() {
    blockElements.add(NewLine.nl());
    return this;
  }

  public final StatementOrBlockBuilder withStatement(BlockStatement statement) {
    blockElements.add(statement);
    blockStatementCount++;
    return this;
  }

  final Statement build() {
    if (!forceBlock && statementCount == 1 && blockStatementCount == 0) {
      return buildStatement();
    } else {
      return buildBlock();
    }
  }

  final void forceBlock() {
    forceBlock = true;
  }

  final StatementOrBlockBuilder withStatement(Statement statement) {
    blockElements.add(statement);
    lastStatement = statement;
    statementCount++;
    return this;
  }

  private Block buildBlock() {
    return Block.block(blockElements);
  }

  private Statement buildStatement() {
    if (blockElements.size() == 1) {
      return lastStatement;
    } else {
      return FormattedStatement.of(blockElements);
    }
  }

}