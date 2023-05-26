package objectos.css.keyword;

import objectos.css.type.GlobalKeyword;

public final class InitialKeyword extends StandardKeyword implements GlobalKeyword {
  static final InitialKeyword INSTANCE = new InitialKeyword();

  private InitialKeyword() {
    super(105, "initial", "initial");
  }
}
