package br.com.objectos.css.keyword;

import br.com.objectos.css.type.BoxValue;

public final class PaddingBoxKeyword extends StandardKeyword implements BoxValue {
  static final PaddingBoxKeyword INSTANCE = new PaddingBoxKeyword();

  private PaddingBoxKeyword() {
    super(179, "paddingBox", "padding-box");
  }
}
