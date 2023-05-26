package objectos.css.keyword;

import objectos.css.type.ContentValue;

public final class NoOpenQuoteKeyword extends StandardKeyword implements ContentValue {
  static final NoOpenQuoteKeyword INSTANCE = new NoOpenQuoteKeyword();

  private NoOpenQuoteKeyword() {
    super(164, "noOpenQuote", "no-open-quote");
  }
}
