package br.com.objectos.css.keyword;

import br.com.objectos.css.type.TextAlignValue;

public final class JustifyKeyword extends StandardKeyword implements TextAlignValue {
  static final JustifyKeyword INSTANCE = new JustifyKeyword();

  private JustifyKeyword() {
    super(119, "justify", "justify");
  }
}
