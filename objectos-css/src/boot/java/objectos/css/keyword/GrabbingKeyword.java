package objectos.css.keyword;

import objectos.css.type.CursorValue;

public final class GrabbingKeyword extends StandardKeyword implements CursorValue {
  static final GrabbingKeyword INSTANCE = new GrabbingKeyword();

  private GrabbingKeyword() {
    super(90, "grabbing", "grabbing");
  }
}
