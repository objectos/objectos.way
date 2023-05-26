package br.com.objectos.css.keyword;

import br.com.objectos.css.type.FlexDirectionValue;

public final class ColumnReverseKeyword extends StandardKeyword implements FlexDirectionValue {
  static final ColumnReverseKeyword INSTANCE = new ColumnReverseKeyword();

  private ColumnReverseKeyword() {
    super(51, "columnReverse", "column-reverse");
  }
}
