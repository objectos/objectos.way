package objectos.css.keyword;

import objectos.css.type.CursorValue;

public final class CopyKeyword extends StandardKeyword implements CursorValue {
  static final CopyKeyword INSTANCE = new CopyKeyword();

  private CopyKeyword() {
    super(57, "copy", "copy");
  }
}
