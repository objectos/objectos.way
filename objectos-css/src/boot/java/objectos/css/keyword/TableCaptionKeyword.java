package objectos.css.keyword;

import objectos.css.type.DisplayInternalValue;

public final class TableCaptionKeyword extends StandardKeyword implements DisplayInternalValue {
  static final TableCaptionKeyword INSTANCE = new TableCaptionKeyword();

  private TableCaptionKeyword() {
    super(240, "tableCaption", "table-caption");
  }
}
