package objectos.css.keyword;

import objectos.css.type.FlexDirectionValue;

public final class ColumnKeyword extends StandardKeyword implements FlexDirectionValue {
  static final ColumnKeyword INSTANCE = new ColumnKeyword();

  private ColumnKeyword() {
    super(50, "column", "column");
  }
}
