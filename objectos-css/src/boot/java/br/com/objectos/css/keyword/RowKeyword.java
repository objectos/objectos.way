package br.com.objectos.css.keyword;

import br.com.objectos.css.type.FlexDirectionValue;

public final class RowKeyword extends StandardKeyword implements FlexDirectionValue {
  static final RowKeyword INSTANCE = new RowKeyword();

  private RowKeyword() {
    super(196, "row", "row");
  }
}
