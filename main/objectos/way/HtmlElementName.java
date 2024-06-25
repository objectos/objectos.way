/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectos.way;

import java.util.ArrayList;
import java.util.List;

final class HtmlElementName extends HtmlElementNameGenerated implements Html.ElementName {

  static final class Builder {

    static Builder INSTANCE = new Builder();

    private final List<HtmlElementName> standardValues = new ArrayList<>();

    private int index;

    private Builder() {}

    public HtmlElementName[] buildValuesImpl() {
      return standardValues.toArray(HtmlElementName[]::new);
    }

    public final HtmlElementName create(String name, boolean endTag) {
      HtmlElementName result;
      result = new HtmlElementName(index++, name, endTag);

      standardValues.add(result);

      return result;
    }

  }

  private final int index;

  private final String name;

  private final boolean endTag;

  private HtmlElementName(int index, String name, boolean endTag) {
    this.index = index;
    this.name = name;
    this.endTag = endTag;
  }

  public static HtmlElementName createNormal(String name) {
    return Builder.INSTANCE.create(name, true);
  }

  public static HtmlElementName createVoid(String name) {
    return Builder.INSTANCE.create(name, false);
  }

  static HtmlElementName get(int index) {
    return LazyValues.VALUES[index];
  }

  @Override
  public final int index() {
    return index;
  }

  @Override
  public final String name() {
    return name;
  }

  @Override
  public final boolean endTag() {
    return endTag;
  }

  private static class LazyValues {

    private static HtmlElementName[] VALUES = create();

    private static HtmlElementName[] create() {
      HtmlElementName[] result;
      result = Builder.INSTANCE.buildValuesImpl();

      Builder.INSTANCE = null;

      return result;
    }

  }

  static int size() {
    return LazyValues.VALUES.length;
  }

}