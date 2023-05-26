package br.com.objectos.css.keyword;

import br.com.objectos.css.type.ContentValue;

public final class NoCloseQuoteKeyword extends StandardKeyword implements ContentValue {
  static final NoCloseQuoteKeyword INSTANCE = new NoCloseQuoteKeyword();

  private NoCloseQuoteKeyword() {
    super(162, "noCloseQuote", "no-close-quote");
  }
}
