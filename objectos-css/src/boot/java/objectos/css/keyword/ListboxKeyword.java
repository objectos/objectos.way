package objectos.css.keyword;

import objectos.css.type.AppearanceValue;

public final class ListboxKeyword extends StandardKeyword implements AppearanceValue {
  static final ListboxKeyword INSTANCE = new ListboxKeyword();

  private ListboxKeyword() {
    super(136, "listbox", "listbox");
  }
}
