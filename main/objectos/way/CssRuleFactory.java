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

import java.util.List;
import java.util.Map;

@SuppressWarnings("exports")
sealed interface CssRuleFactory {

  record OfProperties(CssKey key, Map<String, String> properties) implements CssRuleFactory {
    @Override
    public final CssRule create(String className, List<CssVariant> variants) {
      return new CssRule.OfProperties(key, className, variants, properties);
    }
  }

  default CssRule create(String className, List<CssVariant> variants) {
    throw new UnsupportedOperationException(
        "Not supported for " + getClass().getSimpleName()
    );
  }

}