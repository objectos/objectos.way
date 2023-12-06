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

import objectos.lang.object.ToString;
import objectos.util.list.UnmodifiableList;

/**
 * A Git tree object resulting from a <em>read tree</em> operation.
 *
 * @since 1
 */
public final class Tree extends GitObject {

  final UnmodifiableList<Entry> entries;

  private int index;

  Tree(UnmodifiableList<Entry> entries, ObjectId objectId) {
    super(objectId);

    this.entries = entries;
  }

  @Override
  public final void formatToString(StringBuilder sb, int depth) {
    ToString.formatStart(sb, this);

    for (int i = 0, size = entries.size(); i < size; i++) {
      sb.append('\n');

      ToString.appendIndentation(sb, depth);

      Entry e;
      e = entries.get(i);

      sb.append(e.print());
    }

    sb.append('\n');

    ToString.formatEnd(sb, depth);
  }

  /**
   * Returns the entries of this tree object.
   *
   * @return the entries of this tree object
   */
  public final UnmodifiableList<Entry> getEntries() {
    return entries;
  }

  final boolean hasNextEntry() {
    return index < entries.size();
  }

  final Entry nextEntry() {
    return entries.get(index++);
  }

}