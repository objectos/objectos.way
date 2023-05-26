package objectos.css.keyword;

import objectos.css.type.DisplayListItemValue;

public final class ListItemKeyword extends StandardKeyword implements DisplayListItemValue {
  static final ListItemKeyword INSTANCE = new ListItemKeyword();

  private ListItemKeyword() {
    super(135, "listItem", "list-item");
  }
}
