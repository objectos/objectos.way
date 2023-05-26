package br.com.objectos.css.keyword;

import br.com.objectos.css.type.ContentDistribution;

public final class SpaceEvenlyKeyword extends StandardKeyword implements ContentDistribution {
  static final SpaceEvenlyKeyword INSTANCE = new SpaceEvenlyKeyword();

  private SpaceEvenlyKeyword() {
    super(227, "spaceEvenly", "space-evenly");
  }
}
