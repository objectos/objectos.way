package objectos.css.keyword;

import objectos.css.type.WhiteSpaceValue;

public final class BreakSpacesKeyword extends StandardKeyword implements WhiteSpaceValue {
  static final BreakSpacesKeyword INSTANCE = new BreakSpacesKeyword();

  private BreakSpacesKeyword() {
    super(32, "breakSpaces", "break-spaces");
  }
}
