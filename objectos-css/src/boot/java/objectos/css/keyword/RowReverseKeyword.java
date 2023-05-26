package objectos.css.keyword;

import objectos.css.type.FlexDirectionValue;

public final class RowReverseKeyword extends StandardKeyword implements FlexDirectionValue {
  static final RowReverseKeyword INSTANCE = new RowReverseKeyword();

  private RowReverseKeyword() {
    super(198, "rowReverse", "row-reverse");
  }
}
