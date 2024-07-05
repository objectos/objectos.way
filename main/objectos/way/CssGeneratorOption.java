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

non-sealed abstract class CssGeneratorOption implements Css.Generator.Classes, Css.Generator.Option {

  static final class OverrideKey extends CssGeneratorOption {

    private final CssKey key;

    private final CssProperties properties;

    public OverrideKey(CssKey key, CssProperties properties) {
      this.key = key;
      this.properties = properties;
    }

    @Override
    final void acceptCssGenerator(CssGenerator config) {
      config.override(key, properties);
    }

  }

  CssGeneratorOption() {}

  public static CssGeneratorOption cast(Css.Generator.Classes o) {
    // this cast is safe as Css.Generator.Classes is sealed
    return (CssGeneratorOption) o;
  }

  public static CssGeneratorOption cast(Css.Generator.Option o) {
    // this cast is safe as Css.Generator.Option is sealed
    return (CssGeneratorOption) o;
  }

  abstract void acceptCssGenerator(CssGenerator config);

}