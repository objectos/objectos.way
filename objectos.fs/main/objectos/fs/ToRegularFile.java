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
package objectos.fs;

import java.io.IOException;

enum ToRegularFile implements PathNameVisitor<RegularFile, Void> {

  INSTANCE;

  @Override
  public final RegularFile visitDirectory(Directory directory, Void p) throws IOException {
    throw new NotRegularFileException(directory);
  }

  @Override
  public final RegularFile visitNotFound(ResolvedPath notFound, Void p) throws IOException {
    throw new NotFoundException(notFound);
  }

  @Override
  public final RegularFile visitRegularFile(RegularFile file, Void p) throws IOException {
    return file;
  }

}