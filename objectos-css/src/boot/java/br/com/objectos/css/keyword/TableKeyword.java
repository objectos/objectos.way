package br.com.objectos.css.keyword;

import br.com.objectos.css.type.DisplayInsideValue;

public final class TableKeyword extends StandardKeyword implements DisplayInsideValue {
  static final TableKeyword INSTANCE = new TableKeyword();

  private TableKeyword() {
    super(239, "tableKw", "table");
  }
}
