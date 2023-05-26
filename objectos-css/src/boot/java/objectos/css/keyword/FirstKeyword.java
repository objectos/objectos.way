package objectos.css.keyword;

import objectos.css.type.BaselinePosition;

public final class FirstKeyword extends StandardKeyword implements BaselinePosition {
  static final FirstKeyword INSTANCE = new FirstKeyword();

  private FirstKeyword() {
    super(78, "first", "first");
  }
}
