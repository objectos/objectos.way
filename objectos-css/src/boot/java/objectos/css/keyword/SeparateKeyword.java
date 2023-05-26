package objectos.css.keyword;

import objectos.css.type.BorderCollapseValue;

public final class SeparateKeyword extends StandardKeyword implements BorderCollapseValue {
  static final SeparateKeyword INSTANCE = new SeparateKeyword();

  private SeparateKeyword() {
    super(214, "separate", "separate");
  }
}
