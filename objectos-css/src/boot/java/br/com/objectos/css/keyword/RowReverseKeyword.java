package br.com.objectos.css.keyword;

import br.com.objectos.css.type.FlexDirectionValue;

public final class RowReverseKeyword extends StandardKeyword implements FlexDirectionValue {
  static final RowReverseKeyword INSTANCE = new RowReverseKeyword();

  private RowReverseKeyword() {
    super(198, "rowReverse", "row-reverse");
  }
}
