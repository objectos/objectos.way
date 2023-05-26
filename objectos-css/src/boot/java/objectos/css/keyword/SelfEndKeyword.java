package objectos.css.keyword;

import objectos.css.type.SelfPosition;

public final class SelfEndKeyword extends StandardKeyword implements SelfPosition {
  static final SelfEndKeyword INSTANCE = new SelfEndKeyword();

  private SelfEndKeyword() {
    super(212, "selfEnd", "self-end");
  }
}
