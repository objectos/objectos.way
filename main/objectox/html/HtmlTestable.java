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

public interface HtmlTestable {

  void cell(String value, int width);

  void field(String name, String value);

  void fieldName(String name);

  void heading1(String value);

  void heading2(String value);

  void heading3(String value);

  void heading4(String value);

  void heading5(String value);

  void heading6(String value);

  void newLine();

}
