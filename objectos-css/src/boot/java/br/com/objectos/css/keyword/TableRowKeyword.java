package br.com.objectos.css.keyword;

import br.com.objectos.css.type.DisplayInternalValue;

public final class TableRowKeyword extends StandardKeyword implements DisplayInternalValue {
  static final TableRowKeyword INSTANCE = new TableRowKeyword();

  private TableRowKeyword() {
    super(246, "tableRow", "table-row");
  }
}
