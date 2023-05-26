package br.com.objectos.css.keyword;

import br.com.objectos.css.type.FlexBasisValue;

public final class ContentKeyword extends StandardKeyword implements FlexBasisValue {
  static final ContentKeyword INSTANCE = new ContentKeyword();

  private ContentKeyword() {
    super(53, "content", "content");
  }
}
