package objectos.css.keyword;

import objectos.css.type.AppearanceValue;

public final class SearchfieldKeyword extends StandardKeyword implements AppearanceValue {
  static final SearchfieldKeyword INSTANCE = new SearchfieldKeyword();

  private SearchfieldKeyword() {
    super(211, "searchfield", "searchfield");
  }
}
