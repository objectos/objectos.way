package br.com.objectos.css.keyword;

import br.com.objectos.css.type.DisplayInternalValue;

public final class TableCaptionKeyword extends StandardKeyword implements DisplayInternalValue {
  static final TableCaptionKeyword INSTANCE = new TableCaptionKeyword();

  private TableCaptionKeyword() {
    super(240, "tableCaption", "table-caption");
  }
}
