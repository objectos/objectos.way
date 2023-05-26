package objectos.css.keyword;

import objectos.css.type.FlexDirectionValue;

public final class ColumnReverseKeyword extends StandardKeyword implements FlexDirectionValue {
  static final ColumnReverseKeyword INSTANCE = new ColumnReverseKeyword();

  private ColumnReverseKeyword() {
    super(51, "columnReverse", "column-reverse");
  }
}
