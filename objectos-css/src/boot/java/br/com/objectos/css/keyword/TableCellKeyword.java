package br.com.objectos.css.keyword;

import br.com.objectos.css.type.DisplayInternalValue;

public final class TableCellKeyword extends StandardKeyword implements DisplayInternalValue {
  static final TableCellKeyword INSTANCE = new TableCellKeyword();

  private TableCellKeyword() {
    super(241, "tableCell", "table-cell");
  }
}
