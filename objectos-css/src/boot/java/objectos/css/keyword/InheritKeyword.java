package objectos.css.keyword;

import objectos.css.type.GlobalKeyword;

public final class InheritKeyword extends StandardKeyword implements GlobalKeyword {
  static final InheritKeyword INSTANCE = new InheritKeyword();

  private InheritKeyword() {
    super(104, "inherit", "inherit");
  }
}
