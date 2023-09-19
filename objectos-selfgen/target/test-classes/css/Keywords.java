/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package css.test;

import br.com.objectos.css.FontFamilyKeyword;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("br.com.objectos.css.boot.SpecProcessor")
public final class Keywords {
  public static final AutoKeyword auto = new KeywordImpl("auto");

  public static final FixedKeyword fixed = new KeywordImpl("fixed");

  public static final InheritKeyword inherit = new KeywordImpl("inherit");

  public static final MediumKeyword medium = new KeywordImpl("medium");

  public static final FontFamilyKeyword monospace = new KeywordImpl("monospace");

  public static final NoneKeyword none = new KeywordImpl("none");

  public static final FontFamilyKeyword sansSerif = new KeywordImpl("sans-serif");

  public static final ScrollKeyword scroll = new KeywordImpl("scroll");

  public static final ThickKeyword thick = new KeywordImpl("thick");

  public static final ThinKeyword thin = new KeywordImpl("thin");

  public static final TransparentKeyword transparent = new KeywordImpl("transparent");

  private Keywords() {
  }

  public static String toFieldName(String keyword) {
    return DictionaryHolder.toFieldNameMap.getOrDefault(keyword, keyword);
  }

  private static class DictionaryHolder {
    static Map<String, String> toFieldNameMap = initToFieldNameMap();

    private static Map<String, String> initToFieldNameMap() {
      Map<String, String> map = new HashMap<>();
      map.put("sans-serif", "sansSerif");
      return map;
    }
  }
}
