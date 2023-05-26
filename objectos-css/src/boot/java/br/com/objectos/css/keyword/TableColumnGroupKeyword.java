package br.com.objectos.css.keyword;

import br.com.objectos.css.type.DisplayInternalValue;

public final class TableColumnGroupKeyword extends StandardKeyword implements DisplayInternalValue {
  static final TableColumnGroupKeyword INSTANCE = new TableColumnGroupKeyword();

  private TableColumnGroupKeyword() {
    super(243, "tableColumnGroup", "table-column-group");
  }
}
