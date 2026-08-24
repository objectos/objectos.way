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
package objectox.html.stream;

sealed abstract class Frame
    permits
    JumpFrame,
    ScanDocumentNodeFrame,
    ScanRegularElementAttributeFrame,
    ScanElementNodeFrame {

  int index;

  Frame(int index) {
    this.index = index;
  }

  public final <T extends Frame> T as(Class<T> type) {
    return type.cast(this);
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof Frame that
        && index == that.index
        && equalsImpl(that);
  }

  abstract boolean equalsImpl(Frame that);

  public final IllegalStateException expected(Class<?> expectedType) {
    final String msg;
    msg = "Expected frame %s but got %s".formatted(expectedType.getSimpleName(), getClass().getSimpleName());

    return new IllegalStateException(msg);
  }

  public abstract State newState(Ctx ctx);

  @Override
  public final String toString() {
    final Class<? extends Frame> c;
    c = getClass();

    final String simpleName;
    simpleName = c.getSimpleName();

    return simpleName + "(" + index + toStringImpl() + ")";
  }

  String toStringImpl() {
    return "";
  }

  public void updateIfNecessary(ScanElementNodeFrame previous) {
    // noop
  }

}
