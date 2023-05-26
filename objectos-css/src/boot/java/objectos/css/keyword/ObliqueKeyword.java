package objectos.css.keyword;

import objectos.css.type.FontStyleValue;

public final class ObliqueKeyword extends StandardKeyword implements FontStyleValue {
  static final ObliqueKeyword INSTANCE = new ObliqueKeyword();

  private ObliqueKeyword() {
    super(173, "oblique", "oblique");
  }
}
