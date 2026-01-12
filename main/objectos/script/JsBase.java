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

import module java.base;

sealed abstract class JsBase
    permits
    JsObject {

  private final Object value;

  JsBase(Object value) {
    this.value = value;
  }

  JsBase(JsBase recv, JsOp op) {
    value = recv.with(op);
  }

  @Override
  public final String toString() {
    return switch (value) {
      case JsOp single -> single.toString();

      case String s -> s;

      case List<?> list -> list.stream().map(Object::toString).collect(Collectors.joining(",", "[\"W1\",", "]"));

      default -> throw new UnsupportedOperationException(
          "JsObject cannot be used as an argument: " + value.getClass()
      );
    };
  }

  final JsAction action(JsOp op) {
    final List<?> combined;
    combined = with(op);

    return JsAction.one(
        combined
    );
  }

  final List<?> with(JsOp op) {
    return switch (value) {
      case JsOp single -> List.of(single, op);

      case String single -> List.of(single, op);

      case List<?> head -> with0(head, op);

      default -> throw new UnsupportedOperationException(
          "JsObject cannot be used as an argument: " + value.getClass()
      );
    };
  }

  @SuppressWarnings("unchecked")
  private List<?> with0(List<?> head, JsOp op) {
    final List<Object> list;
    list = new ArrayList<>();

    list.addAll(head);

    list.add(op);

    return List.copyOf(list);
  }

}
