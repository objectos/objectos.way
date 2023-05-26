package objectos.css.keyword;

import objectos.css.type.HeightOrWidthValue;
import objectos.css.type.MaxHeightOrWidthValue;

public final class MaxContentKeyword extends StandardKeyword implements HeightOrWidthValue, MaxHeightOrWidthValue {
  static final MaxContentKeyword INSTANCE = new MaxContentKeyword();

  private MaxContentKeyword() {
    super(146, "maxContent", "max-content");
  }
}
