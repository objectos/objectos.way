package objectos.css.keyword;

import objectos.css.type.FontWeightValue;

public final class BoldKeyword extends StandardKeyword implements FontWeightValue {
  static final BoldKeyword INSTANCE = new BoldKeyword();

  private BoldKeyword() {
    super(27, "bold", "bold");
  }
}
