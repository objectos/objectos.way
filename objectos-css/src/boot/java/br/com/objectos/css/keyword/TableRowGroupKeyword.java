package br.com.objectos.css.keyword;

import br.com.objectos.css.type.DisplayInternalValue;

public final class TableRowGroupKeyword extends StandardKeyword implements DisplayInternalValue {
  static final TableRowGroupKeyword INSTANCE = new TableRowGroupKeyword();

  private TableRowGroupKeyword() {
    super(247, "tableRowGroup", "table-row-group");
  }
}
