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
import java.util.Comparator;
import objectos.lang.object.Equals;
import objectos.lang.object.ToString;

/**
 * An object that can serve as a {@link MutableTree} entry.
 *
 * @since 3
 */
abstract class MutableTreeEntry implements ToString.Formattable {

  static final Comparator<MutableTreeEntry> ORDER = new Comparator<MutableTreeEntry>() {
    @Override
    public final int compare(MutableTreeEntry o1, MutableTreeEntry o2) {
      String name1;
      name1 = getSortName(o1);

      String name2;
      name2 = getSortName(o2);

      return name1.compareTo(name2);
    }

    private String getSortName(MutableTreeEntry o) {
      String result;
      result = o.getName();

      EntryMode mode;
      mode = o.getMode();

      if (mode.isTree()) {
        result = result + '/';
      }

      return result;
    }
  };

  MutableTreeEntry() {}

  /**
   * Returns the file mode of this tree entry.
   *
   * @return the file mode of this tree entry
   */
  public abstract EntryMode getMode();

  /**
   * Returns the name of this tree entry.
   *
   * @return the name of this tree entry
   */
  public abstract String getName();

  /**
   * Returns {@code true} if this tree entry has the specified name,
   * {@code false} otherwise.
   *
   * @param name
   *        the value to test this entry's name against
   *
   * @return {@code true} if this tree entry has the specified name,
   *         {@code false} otherwise
   *
   * @since 3
   */
  public final boolean hasName(String name) {
    return Equals.of(getName(), name);
  }

  @Override
  public final String toString() {
    return ToString.of(this);
  }

  abstract ObjectId computeObjectId(GitInjector injector, Charset charset) throws IOException;

  abstract void executeWriteTree(WriteTree wt);

  abstract void formatToStringImpl(String prefix, StringBuilder sb, int depth);

  void writeTree(GitInjector injector, Repository repository)
                                                              throws GitStubException, IOException {
    // noop
  }

}