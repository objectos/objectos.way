package objectos.css.keyword;

import objectos.css.type.ContentDistribution;

public final class SpaceBetweenKeyword extends StandardKeyword implements ContentDistribution {
  static final SpaceBetweenKeyword INSTANCE = new SpaceBetweenKeyword();

  private SpaceBetweenKeyword() {
    super(226, "spaceBetween", "space-between");
  }
}