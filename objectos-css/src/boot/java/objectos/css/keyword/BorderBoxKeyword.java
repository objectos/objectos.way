package objectos.css.keyword;

import objectos.css.type.BoxSizingValue;
import objectos.css.type.BoxValue;

public final class BorderBoxKeyword extends StandardKeyword implements BoxSizingValue, BoxValue {
  static final BorderBoxKeyword INSTANCE = new BorderBoxKeyword();

  private BorderBoxKeyword() {
    super(29, "borderBox", "border-box");
  }
}
