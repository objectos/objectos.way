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

final class DirectoryContentsVisitorAdapter
    implements PathNameVisitor<Void, DirectoryContentsVisitor> {

  static final DirectoryContentsVisitorAdapter INSTANCE = new DirectoryContentsVisitorAdapter();

  private DirectoryContentsVisitorAdapter() {}

  @Override
  public final Void visitDirectory(
      Directory directory, DirectoryContentsVisitor p) throws IOException {
    p.visitDirectory(directory);

    return null;
  }

  @Override
  public final Void visitNotFound(
      ResolvedPath notFound, DirectoryContentsVisitor p) throws IOException {

    return null;
  }

  @Override
  public final Void visitRegularFile(
      RegularFile file, DirectoryContentsVisitor p) throws IOException {
    p.visitRegularFile(file);

    return null;
  }

}