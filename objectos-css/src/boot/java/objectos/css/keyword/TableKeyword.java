package objectos.css.keyword;

import objectos.css.type.DisplayInsideValue;

public final class TableKeyword extends StandardKeyword implements DisplayInsideValue {
  static final TableKeyword INSTANCE = new TableKeyword();

  private TableKeyword() {
    super(239, "tableKw", "table");
  }
}
