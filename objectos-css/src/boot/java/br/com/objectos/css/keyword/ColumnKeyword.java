package br.com.objectos.css.keyword;

import br.com.objectos.css.type.FlexDirectionValue;

public final class ColumnKeyword extends StandardKeyword implements FlexDirectionValue {
  static final ColumnKeyword INSTANCE = new ColumnKeyword();

  private ColumnKeyword() {
    super(50, "column", "column");
  }
}
