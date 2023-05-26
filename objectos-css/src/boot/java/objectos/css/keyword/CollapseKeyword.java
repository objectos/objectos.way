package objectos.css.keyword;

import objectos.css.type.BorderCollapseValue;

public final class CollapseKeyword extends StandardKeyword implements BorderCollapseValue {
  static final CollapseKeyword INSTANCE = new CollapseKeyword();

  private CollapseKeyword() {
    super(49, "collapse", "collapse");
  }
}
