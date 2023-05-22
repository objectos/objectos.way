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
package objectos.asciidoc.pseudom;

import java.io.IOException;
import objectos.asciidoc.internal.PseudoDocument;

public sealed interface Document extends AutoCloseable permits PseudoDocument {

  @Override
  void close() throws IOException;

  String getNamed(String name);

  String getNamed(String name, String defaultValue);

  IterableOnce<Node> nodes();

}