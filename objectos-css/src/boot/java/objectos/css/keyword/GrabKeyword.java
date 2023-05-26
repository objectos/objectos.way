package objectos.css.keyword;

import objectos.css.type.CursorValue;

public final class GrabKeyword extends StandardKeyword implements CursorValue {
  static final GrabKeyword INSTANCE = new GrabKeyword();

  private GrabKeyword() {
    super(89, "grab", "grab");
  }
}
