package br.com.objectos.css.keyword;

import br.com.objectos.css.type.ContentValue;

public final class CloseQuoteKeyword extends StandardKeyword implements ContentValue {
  static final CloseQuoteKeyword INSTANCE = new CloseQuoteKeyword();

  private CloseQuoteKeyword() {
    super(47, "closeQuote", "close-quote");
  }
}
