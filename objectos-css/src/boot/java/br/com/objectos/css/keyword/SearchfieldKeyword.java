package br.com.objectos.css.keyword;

import br.com.objectos.css.type.AppearanceValue;

public final class SearchfieldKeyword extends StandardKeyword implements AppearanceValue {
  static final SearchfieldKeyword INSTANCE = new SearchfieldKeyword();

  private SearchfieldKeyword() {
    super(211, "searchfield", "searchfield");
  }
}
