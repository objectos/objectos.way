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

import br.com.objectos.css.Color;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("br.com.objectos.css.boot.SpecProcessor")
abstract class GeneratedColor implements BorderColorValue1 {
  GeneratedColor() {
  }

  static class DictionaryHolder {
    static Map<String, Color> keywordMap = initKeywordMap();

    static Map<String, String> toFieldNameMap = initToFieldNameMap();

    private static Map<String, Color> initKeywordMap() {
      Map<String, Color> map = new HashMap<>();
      return map;
    }

    private static Map<String, String> initToFieldNameMap() {
      Map<String, String> map = new HashMap<>();
      return map;
    }
  }
}
