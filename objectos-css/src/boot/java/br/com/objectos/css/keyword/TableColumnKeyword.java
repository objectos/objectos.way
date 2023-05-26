package br.com.objectos.css.keyword;

import br.com.objectos.css.type.DisplayInternalValue;

public final class TableColumnKeyword extends StandardKeyword implements DisplayInternalValue {
  static final TableColumnKeyword INSTANCE = new TableColumnKeyword();

  private TableColumnKeyword() {
    super(242, "tableColumn", "table-column");
  }
}
