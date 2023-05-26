package br.com.objectos.css.keyword;

import br.com.objectos.css.type.DisplayInternalValue;

public final class TableHeaderGroupKeyword extends StandardKeyword implements DisplayInternalValue {
  static final TableHeaderGroupKeyword INSTANCE = new TableHeaderGroupKeyword();

  private TableHeaderGroupKeyword() {
    super(245, "tableHeaderGroup", "table-header-group");
  }
}
