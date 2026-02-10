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
package objectos.script;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/// Represents an action or a sequence of actions to be executed by the
/// JS runtime.
public sealed abstract class JsAction {

  private static final class One extends JsAction {

    private final Object value;

    One(Object value) {
      this.value = value;
    }

    @Override
    public final String toString() {
      if (value instanceof List<?> list) {
        return list.stream().map(Object::toString).collect(Collectors.joining(",", "[\"W1\",", "]"));
      } else {
        return value.toString();
      }
    }

    @Override
    final void addTo(List<One> list) {
      list.add(this);
    }

    private String seq() {
      if (value instanceof List<?>) {
        return toString();
      } else {
        return value.toString();
      }
    }

  }

  private static final class Seq extends JsAction {

    private final List<One> values;

    Seq(List<One> values) {
      this.values = values;
    }

    @Override
    public final String toString() {
      return "[" + JsString.WS + "," + value() + "]";
    }

    @Override
    final void addTo(List<One> list) {
      list.addAll(values);
    }

    private String value() {
      return values.stream()
          .map(v -> v.seq())
          .collect(Collectors.joining(","));
    }

  }

  static final JsAction FOLLOW = new One(JsOp.of(JsString.FO));

  static final JsAction NOOP = new One(JsOp.of(JsString.NO));

  static final JsAction SUBMIT = new One(JsOp.of(JsString.SU));

  JsAction() {}

  static JsAction follow(Consumer<? super Follow> options) {
    final Follow pojo;
    pojo = new Follow();

    options.accept(pojo);

    return new One(pojo);
  }

  static JsAction one(List<?> list) {
    return new One(list);
  }

  static JsAction seq(JsAction first, JsAction second, JsAction[] more) {
    final List<One> list;
    list = new ArrayList<>();

    first.addTo(list);

    second.addTo(list);

    for (int idx = 0; idx < more.length; idx++) {
      more[idx].addTo(list);
    }

    return new Seq(
        List.copyOf(list)
    );
  }

  static JsAction submit(Consumer<? super Submit> options) {
    final Submit pojo;
    pojo = new Submit();

    options.accept(pojo);

    return new One(pojo);
  }

  static JsAction throwError(JsString msg) {
    Objects.requireNonNull(msg, "msg == null");

    final JsOp op;
    op = JsOp.of(JsString.TE, msg);

    return new One(op);
  }

  static JsAction var(String name, JsObject value) {
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    final JsString $name;
    $name = JsString.raw(name);

    final JsOp op;
    op = JsOp.of(JsString.CW, $name, value);

    return new One(op);
  }

  /// Returns the JSON encoded representation of this JS action.
  ///
  /// @return the JSON representation
  @Override
  public abstract String toString();

  abstract void addTo(List<One> list);

}
