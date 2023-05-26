package br.com.objectos.css.keyword;

import br.com.objectos.css.type.TextDecorationLineValue;

public final class GrammarErrorKeyword extends StandardKeyword implements TextDecorationLineValue {
  static final GrammarErrorKeyword INSTANCE = new GrammarErrorKeyword();

  private GrammarErrorKeyword() {
    super(91, "grammarError", "grammar-error");
  }
}
