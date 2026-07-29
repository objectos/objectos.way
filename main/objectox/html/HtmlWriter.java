/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.html;

public class HtmlWriter implements Appendable {

  public static HtmlWriter wrap(char[] cbuf, int off, int len) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final Appendable append(char c) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final Appendable append(CharSequence csq) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final Appendable append(CharSequence csq, int start, int end) {
    throw new UnsupportedOperationException("Implement me");
  }

  public final boolean exhausted() {
    throw new UnsupportedOperationException("Implement me");
  }

  public final String overflow() {
    throw new UnsupportedOperationException("Implement me");
  }

  public final int written() {
    throw new UnsupportedOperationException("Implement me");
  }

}
