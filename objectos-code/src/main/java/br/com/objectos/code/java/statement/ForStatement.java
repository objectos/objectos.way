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
import br.com.objectos.code.java.element.NewLine;
import br.com.objectos.code.java.expression.Expression;
import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.java.type.NamedType;
import objectos.lang.Check;

public abstract class ForStatement extends AbstractSimpleStatement {

  ForStatement() {}

  public static ForStatement _for(
      Class<?> type, Identifier id, Expression expression,
      Statement body) {
    return _for(
        NamedClass.of(type), id, expression,
        body
    );
  }

  public static ForStatement _for(
      ForInitElement init, ForConditionElement condition, ForUpdateElement update,
      ForStatementElement e1) {
    Check.notNull(e1, "e1 == null");
    Builder b = basicBuilder(init, condition, update);
    e1.acceptForStatementBuilder(b);
    return b.build();
  }

  public static ForStatement _for(
      ForInitElement init, ForConditionElement condition, ForUpdateElement update,
      ForStatementElement e1,
      ForStatementElement e2) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Builder b = basicBuilder(init, condition, update);
    e1.acceptForStatementBuilder(b);
    e2.acceptForStatementBuilder(b);
    return b.build();
  }

  public static ForStatement _for(
      NamedType typeName, Identifier id, Expression expression,
      Statement body) {
    Check.notNull(typeName, "typeName == null");
    Check.notNull(id, "id == null");
    Check.notNull(expression, "expression == null");
    Check.notNull(body, "body == null");
    return new EnhancedForStatement(
        typeName, id, expression,
        body
    );
  }

  @Ignore
  public static Builder builder() {
    return new Builder();
  }

  private static Builder basicBuilder(
      ForInitElement init,
      ForConditionElement condition,
      ForUpdateElement update) {
    return new Builder()
        .init(init)
        .condition(condition)
        .update(update);
  }

  public static class Builder {

    private final StatementOrBlockBuilder body = new StatementOrBlockBuilder();
    private ForConditionElement condition;

    private ForInitElement init;
    private Mode mode = Mode.START;
    private ForUpdateElement update;

    private Builder() {}

    public final Builder addNewLine(NewLine newLine) {
      Check.notNull(newLine, "newLine == null");
      newLine.acceptStatementOrBlockBuilder(body);
      return this;
    }

    public final Builder addStatement(BlockStatement statement) {
      Check.notNull(statement, "statement == null");
      statement.acceptStatementOrBlockBuilder(body);
      return this;
    }

    public final ForStatement build() {
      return mode.build(this);
    }

    public final Builder condition(ForConditionElement condition) {
      setModeBasic();
      this.condition = Check.notNull(condition, "condition == null");
      return this;
    }

    public final Builder init(ForInitElement init) {
      setModeBasic();
      this.init = Check.notNull(init, "init == null");
      return this;
    }

    public final Builder update(ForUpdateElement update) {
      setModeBasic();
      this.update = Check.notNull(update, "update == null");
      return this;
    }

    final ForStatement buildBasic() {
      return new BasicForStatement(init, condition, update, body.build());
    }

    private void setModeBasic() {
      mode = mode.toBasic();
    }

  }

  private enum Mode {

    BASIC,

    ENHANCED {
      @Override
      final Mode toBasic() {
        throw new IllegalArgumentException(
            "Already set as an enhanced for statement.");
      }
    },

    START;

    ForStatement build(Builder builder) {
      return builder.buildBasic();
    }

    Mode toBasic() {
      return BASIC;
    }

  }

}