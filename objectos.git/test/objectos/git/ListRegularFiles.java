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

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import objectos.fs.Directory;
import objectos.fs.DirectoryContentsVisitor;
import objectos.fs.RegularFile;
import objectos.lang.object.Check;
import objectos.util.list.GrowableList;
import objectos.util.list.UnmodifiableList;

/**
 * A {@link DirectoryContentsVisitor} for recursively listing all of the regular
 * files in a directory.
 */
public final class ListRegularFiles implements DirectoryContentsVisitor {

  private final GrowableList<String> leafs = new GrowableList<>();

  private String prefix = "";

  private ListRegularFiles() {}

  /**
   * Recursively lists the relative pathnames of all (regular) files of the
   * specified directory; the result is both sorted and immutable.
   *
   * @param directory
   *        the directory to list
   *
   * @return sorted immutable list of all files
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  public static UnmodifiableList<String> of(Directory directory) throws IOException {
    Check.notNull(directory, "directory == null");

    ListRegularFiles ls;
    ls = new ListRegularFiles();

    directory.visitContents(ls);

    return ls.build();
  }

  @Override
  public final void visitDirectory(Directory directory) throws IOException {
    String previousPrefix;
    previousPrefix = prefix;

    String directoryName;
    directoryName = directory.getName();

    prefix = prefix + directoryName;

    prefix = prefix + File.separatorChar;

    directory.visitContents(this);

    prefix = previousPrefix;
  }

  @Override
  public final void visitRegularFile(RegularFile file) throws IOException {
    String leaf;
    leaf = prefix + file.getName();

    leafs.add(leaf);
  }

  final UnmodifiableList<String> build() throws IOException {
    leafs.sort(Comparator.naturalOrder());

    return leafs.toUnmodifiableList();
  }

}