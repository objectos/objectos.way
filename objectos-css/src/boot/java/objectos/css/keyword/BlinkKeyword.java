package objectos.css.keyword;

import objectos.css.type.TextDecorationLineKind;

public final class BlinkKeyword extends StandardKeyword implements TextDecorationLineKind {
  static final BlinkKeyword INSTANCE = new BlinkKeyword();

  private BlinkKeyword() {
    super(25, "blink", "blink");
  }
}
