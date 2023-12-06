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

import objectos.notes.NoOpNoteSink;
import objectos.notes.Note;
import objectos.notes.Note2;

final class GitTestingLogger extends NoOpNoteSink {

  @SuppressWarnings("unused")
  private MutableTree testCase11Tree;

  @Override
  public final boolean isEnabled(Note event) {
    return super.isEnabled(event);
  }

  @Override
  public final <T1, T2> void send(Note2<T1, T2> event, T1 v1, T2 v2) {
    if (event == GitEvents.writeTreeStart()) {
      writeTreeStart(v1, v2);
    }
  }

  private boolean hasSimpleName(Object o, String test) {
    if (o == null) {
      return false;
    }

    Class<? extends Object> type;
    type = o.getClass();

    String simpleName;
    simpleName = type.getSimpleName();

    return simpleName.equals(test);
  }

  private void writeTreeStart(Object v1, Object v2) {
    if (hasSimpleName(v1, "TestCase11")) {
      testCase11Tree = (MutableTree) v2;
    }
  }

}