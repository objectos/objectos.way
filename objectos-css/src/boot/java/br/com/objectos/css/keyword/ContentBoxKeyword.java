package br.com.objectos.css.keyword;

import br.com.objectos.css.type.BoxSizingValue;
import br.com.objectos.css.type.BoxValue;

public final class ContentBoxKeyword extends StandardKeyword implements BoxSizingValue, BoxValue {
  static final ContentBoxKeyword INSTANCE = new ContentBoxKeyword();

  private ContentBoxKeyword() {
    super(54, "contentBox", "content-box");
  }
}
