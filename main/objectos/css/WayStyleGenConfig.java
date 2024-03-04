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
package objectos.css;

import java.util.Map;

sealed abstract class WayStyleGenConfig permits WayStyleGen {

  abstract Variant getVariant(String variantName);

  abstract Map<String, String> borderWidth();

  abstract Map<String, String> colors();

  abstract Map<String, String> content();

  abstract Map<String, String> fontSize();

  abstract Map<String, String> fontWeight();

  abstract Map<String, String> height();

  abstract Map<String, String> inset();

  abstract Map<String, String> letterSpacing();

  abstract Map<String, String> lineHeight();

  abstract Map<String, String> margin();

  abstract Map<String, String> outlineOffset();

  abstract Map<String, String> outlineWidth();

  abstract Map<String, String> padding();

  abstract Map<String, String> transitionProperty();

  abstract Map<String, String> utilities();

  abstract Map<String, String> width();

  abstract Map<String, String> zIndex();

}