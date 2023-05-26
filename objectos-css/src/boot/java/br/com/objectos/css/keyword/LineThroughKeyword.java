package br.com.objectos.css.keyword;

import br.com.objectos.css.type.TextDecorationLineKind;

public final class LineThroughKeyword extends StandardKeyword implements TextDecorationLineKind {
  static final LineThroughKeyword INSTANCE = new LineThroughKeyword();

  private LineThroughKeyword() {
    super(134, "lineThrough", "line-through");
  }
}
