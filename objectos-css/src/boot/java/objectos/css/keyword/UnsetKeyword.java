package objectos.css.keyword;

import objectos.css.type.GlobalKeyword;

public final class UnsetKeyword extends StandardKeyword implements GlobalKeyword {
  static final UnsetKeyword INSTANCE = new UnsetKeyword();

  private UnsetKeyword() {
    super(264, "unset", "unset");
  }
}
