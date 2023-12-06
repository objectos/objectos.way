/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
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
package objectos.git;

import java.io.IOException;
import java.nio.charset.Charset;
import objectos.core.io.Charsets;
import objectos.fs.ResolvedPath;

/**
 * Internal repository pseudo-interface.
 *
 * @since 3
 */
abstract class GitRepository {

  GitRepository() {}

  final Charset getCharset() {
    return Charsets.utf8();
  }

  abstract PackFile getPackFile(int index);

  abstract int getPackFileCount();

  abstract ResolvedPath resolveLooseObject(ObjectId objectId) throws IOException;

}