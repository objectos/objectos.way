package objectos.css.keyword;

import objectos.css.type.WhiteSpaceValue;

public final class PreKeyword extends StandardKeyword implements WhiteSpaceValue {
  static final PreKeyword INSTANCE = new PreKeyword();

  private PreKeyword() {
    super(182, "preKw", "pre");
  }
}
