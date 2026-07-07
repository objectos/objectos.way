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

public enum HtmlTestableNoop implements HtmlTestable {

  INSTANCE;

  @Override
  public void cell(String value, int width) {}

  @Override
  public void field(String name, String value) {}

  @Override
  public void fieldName(String name) {}

  @Override
  public void heading1(String value) {}

  @Override
  public void heading2(String value) {}

  @Override
  public void heading3(String value) {}

  @Override
  public void heading4(String value) {}

  @Override
  public void heading5(String value) {}

  @Override
  public void heading6(String value) {}

  @Override
  public void newLine() {}

  @Override
  public String toString() {
    return "";
  }

}
