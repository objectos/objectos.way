/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

import java.nio.charset.Charset;
import java.util.Locale;

record LangMedia(String text, Charset charset) implements Lang.Media {

  @Override
  public final String contentType() {
    final String charsetName;
    charsetName = charset.name();

    return "text/plain; charset=" + charsetName.toLowerCase(Locale.US);
  }

  @Override
  public final byte[] toByteArray() {
    return text.getBytes(charset);
  }

}