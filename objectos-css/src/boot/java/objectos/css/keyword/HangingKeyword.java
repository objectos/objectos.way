package objectos.css.keyword;

import objectos.css.type.TextIndentOptionalValue;

public final class HangingKeyword extends StandardKeyword implements TextIndentOptionalValue {
  static final HangingKeyword INSTANCE = new HangingKeyword();

  private HangingKeyword() {
    super(96, "hanging", "hanging");
  }
}
