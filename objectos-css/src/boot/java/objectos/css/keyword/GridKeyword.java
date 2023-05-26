package objectos.css.keyword;

import objectos.css.type.DisplayInsideValue;

public final class GridKeyword extends StandardKeyword implements DisplayInsideValue {
  static final GridKeyword INSTANCE = new GridKeyword();

  private GridKeyword() {
    super(92, "grid", "grid");
  }
}
