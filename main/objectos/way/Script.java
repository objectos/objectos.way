/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.way;

import objectos.lang.object.Check;

/**
 * The Objectos Script main class.
 */
public final class Script {

  /**
   * Represents an action to be executed by the browser in the context of an web
   * application.
   */
  public sealed interface Action permits ScriptAction {
  }

  private Script() {}

  public static Action delay(int ms, Action... actions) {
    Check.argument(ms >= 0, "ms must not be negative");

    Action[] copy;
    copy = actions.clone(); // implicit null check

    return new ScriptAction() {
      @Override
      final void writeTo(StringBuilder json) {
        objectStart(json);

        property(json, CMD, "delay");

        comma(json);

        property(json, "ms", ms);

        comma(json);

        property(json, "actions", copy);

        objectEnd(json);
      }
    };
  }

  public static Action location(String url) {
    Check.notNull(url, "url == null");

    return new ScriptAction() {
      @Override
      final void writeTo(StringBuilder json) {
        objectStart(json);

        property(json, CMD, "location");

        comma(json);

        property(json, "value", url);

        objectEnd(json);
      }
    };
  }

  public static Action replaceClass(Html.Id id,
                                    Html.ClassName from,
                                    Html.ClassName to) {
    Check.notNull(id, "id == null");
    Check.notNull(from, "from == null");
    Check.notNull(to, "to == null");

    return replaceClass0(id, from.value(), to.value());
  }

  public static Action replaceClass(Html.Id id,
                                    String from,
                                    String to) {
    Check.notNull(id, "id == null");
    Check.notNull(from, "from == null");
    Check.notNull(to, "to == null");

    return replaceClass0(id, from, to);
  }

  private static Action replaceClass0(Html.Id id, String from, String to) {
    return new ScriptAction() {
      @Override
      final void writeTo(StringBuilder json) {
        objectStart(json);

        property(json, CMD, "replace-class");

        comma(json);

        propertyStart(json, "args");

        arrayStart(json);

        stringLiteral(json, id.value());

        comma(json);

        stringLiteral(json, from);

        comma(json);

        stringLiteral(json, to);

        arrayEnd(json);

        objectEnd(json);
      }
    };
  }

  public static Action submit(Html.Id id) {
    Check.notNull(id, "id == null");

    final String value;
    value = id.value();

    return new ScriptAction() {
      @Override
      final void writeTo(StringBuilder json) {
        objectStart(json);

        property(json, CMD, "submit");

        comma(json);

        property(json, "id", value);

        objectEnd(json);
      }
    };
  }

  /**
   * Casts the specified action interface into its implementation class.
   *
   * <p>
   * The cast is safe as the {@code Action} interface is sealed.
   */
  static ScriptAction cast(Action action) {
    return (ScriptAction) action;
  }

  static Action join(Action... actions) {
    return switch (actions.length) {
      case 0 -> Empty.INSTANCE;

      case 1 -> actions[0];

      default -> new Joined(actions);
    };
  }

  private static final class Empty extends ScriptAction {

    static final Script.Action INSTANCE = new Empty();

    private Empty() {}

    @Override
    final void writeTo(StringBuilder json) {}

  }

  private static final class Joined extends ScriptAction {

    private final Script.Action[] actions;

    public Joined(Script.Action[] actions) {
      this.actions = actions.clone();
    }

    @Override
    final void writeTo(StringBuilder json) {
      actions(json, actions);
    }

  }

}