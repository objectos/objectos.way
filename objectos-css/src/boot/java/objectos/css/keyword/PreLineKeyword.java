package objectos.css.keyword;

import objectos.css.type.WhiteSpaceValue;

public final class PreLineKeyword extends StandardKeyword implements WhiteSpaceValue {
  static final PreLineKeyword INSTANCE = new PreLineKeyword();

  private PreLineKeyword() {
    super(183, "preLine", "pre-line");
  }
}
