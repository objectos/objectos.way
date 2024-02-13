/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.http;

sealed abstract class CookieValue {

  final String source;

  public CookieValue(String source) {
    this.source = source;
  }

  public static CookieValue of(String source, int index) {
    return new CookieValue1(source, index);
  }

  abstract CookieValue beginNew(int index);

  abstract void endIndex(int index);

  abstract String get();

  private static final class CookieValue1 extends CookieValue {

    private final int beginIndex;

    private int endIndex;

    public CookieValue1(String source, int beginIndex) {
      super(source);

      this.beginIndex = beginIndex;
    }

    @Override
    final CookieValue beginNew(int index) {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    final void endIndex(int index) {
      endIndex = index;
    }

    @Override
    final String get() {
      return source.substring(beginIndex, endIndex);
    }

  }

}