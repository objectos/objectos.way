package objectos.css.keyword;

import objectos.css.type.BackgroundRepeatArity2Value;

public final class NoRepeatKeyword extends StandardKeyword implements BackgroundRepeatArity2Value {
  static final NoRepeatKeyword INSTANCE = new NoRepeatKeyword();

  private NoRepeatKeyword() {
    super(165, "noRepeat", "no-repeat");
  }
}