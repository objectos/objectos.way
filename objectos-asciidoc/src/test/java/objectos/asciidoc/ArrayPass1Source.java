/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.asciidoc;

class ArrayPass1Source implements Pass1.Source {

  private final int[] token;

  private final String source;

  ArrayPass1Source(String source, int[] token) {
    this.source = source;

    this.token = token;
  }

  @Override
  public final String substring(int start, int end) { return source.substring(start, end); }

  @Override
  public final int tokenAt(int index) { return token[index]; }

  @Override
  public final int tokens() { return token.length; }

}