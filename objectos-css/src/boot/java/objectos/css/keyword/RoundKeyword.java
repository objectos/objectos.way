package objectos.css.keyword;

import objectos.css.type.BackgroundRepeatArity2Value;

public final class RoundKeyword extends StandardKeyword implements BackgroundRepeatArity2Value {
  static final RoundKeyword INSTANCE = new RoundKeyword();

  private RoundKeyword() {
    super(195, "round", "round");
  }
}