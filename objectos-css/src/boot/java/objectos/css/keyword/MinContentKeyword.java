package objectos.css.keyword;

import objectos.css.type.HeightOrWidthValue;
import objectos.css.type.MaxHeightOrWidthValue;

public final class MinContentKeyword extends StandardKeyword implements HeightOrWidthValue, MaxHeightOrWidthValue {
  static final MinContentKeyword INSTANCE = new MinContentKeyword();

  private MinContentKeyword() {
    super(154, "minContent", "min-content");
  }
}
