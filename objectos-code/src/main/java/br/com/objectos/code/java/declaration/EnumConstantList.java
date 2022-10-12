/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package br.com.objectos.code.java.declaration;

import br.com.objectos.code.annotations.Ignore;
import br.com.objectos.code.java.element.AbstractCodeElement;
import br.com.objectos.code.java.element.CodeElement;
import br.com.objectos.code.java.io.CodeWriter;
import java.util.Iterator;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;

public class EnumConstantList extends AbstractCodeElement
    implements
    EnumCodeElement {

  private static final EnumConstantList EMPTY = new EnumConstantList(
      UnmodifiableList.<EnumConstantCode> of()
  );

  private final UnmodifiableList<EnumConstantCode> list;

  private EnumConstantList(UnmodifiableList<EnumConstantCode> list) {
    this.list = list;
  }

  @Ignore("o7.code.Java: do not include me")
  public static Builder builder() {
    return new Builder();
  }

  @Ignore("o7.code.Java: do not include me")
  public static EnumConstantList empty() {
    return EMPTY;
  }

  public static EnumConstantList enumConstants(Iterable<? extends EnumConstantCode> constants) {
    Builder b = builder();
    b.addEnumConstants(constants);
    return b.build();
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    Iterator<? extends CodeElement> iter = list.iterator();
    if (iter.hasNext()) {
      w.nextLine();
      w.nextLine();
      w.writeCodeElement(iter.next());
      while (iter.hasNext()) {
        w.writeCodeElement(comma());
        w.nextLine();
        w.nextLine();
        w.writeCodeElement(iter.next());
      }
    }

    if (!list.isEmpty()) {
      w.writeCodeElement(semicolon());
    }

    return w;
  }

  @Override
  public final void acceptEnumCodeBuilder(EnumCode.Builder builder) {
    builder.uncheckedAddEnumConstants(list);
  }

  public final boolean isEmpty() {
    return list.isEmpty();
  }

  public static class Builder {

    private final GrowableList<EnumConstantCode> list = new GrowableList<>();

    private Builder() {}

    public final Builder addEnumConstant(EnumConstantCode constant) {
      list.addWithNullMessage(constant, "constant == null");
      return this;
    }

    public final Builder addEnumConstants(Iterable<? extends EnumConstantCode> constants) {
      Check.notNull(constants, "constants == null");

      for (EnumConstantCode constant : constants) {
        list.addWithNullMessage(constant, "constants[n] == null");
      }

      return this;
    }

    public final EnumConstantList build() {
      return new EnumConstantList(list.toUnmodifiableList());
    }

    final void uncheckedAddEnumConstants(Iterable<EnumConstantCode> values) {
      list.addAllIterable(values);
    }

  }

}
