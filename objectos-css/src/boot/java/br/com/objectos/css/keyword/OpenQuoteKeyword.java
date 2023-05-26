package br.com.objectos.css.keyword;

import br.com.objectos.css.type.ContentValue;

public final class OpenQuoteKeyword extends StandardKeyword implements ContentValue {
  static final OpenQuoteKeyword INSTANCE = new OpenQuoteKeyword();

  private OpenQuoteKeyword() {
    super(174, "openQuote", "open-quote");
  }
}
