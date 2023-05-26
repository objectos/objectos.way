package br.com.objectos.css.keyword;

import br.com.objectos.css.type.OutlineColorValue;

public final class InvertKeyword extends StandardKeyword implements OutlineColorValue {
  static final InvertKeyword INSTANCE = new InvertKeyword();

  private InvertKeyword() {
    super(115, "invert", "invert");
  }
}
