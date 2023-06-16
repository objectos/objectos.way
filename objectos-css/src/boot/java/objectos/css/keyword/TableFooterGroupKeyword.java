package objectos.css.keyword;

import objectos.css.type.DisplayInternalValue;

public final class TableFooterGroupKeyword extends StandardKeyword implements DisplayInternalValue {
  static final TableFooterGroupKeyword INSTANCE = new TableFooterGroupKeyword();

  private TableFooterGroupKeyword() {
    super(244, "tableFooterGroup", "table-footer-group");
  }
}