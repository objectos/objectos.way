package objectos.css.keyword;

import objectos.css.type.ContentValue;

public final class OpenQuoteKeyword extends StandardKeyword implements ContentValue {
  static final OpenQuoteKeyword INSTANCE = new OpenQuoteKeyword();

  private OpenQuoteKeyword() {
    super(174, "openQuote", "open-quote");
  }
}
