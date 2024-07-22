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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import objectos.lang.object.Check;

/**
 * The <strong>Objectos Script</strong> main class. Part of Objectos HTML.
 */
public final class Script {

  /**
   * Represents an action to be executed by the browser in the context of an web
   * application.
   */
  public sealed interface Action permits ScriptAction {
  }

  private Script() {}

  /**
   * Returns a new action that executes all of the specified actions in order.
   *
   * @param actions
   *        the actions to be joined into a single action instance
   *
   * @return a new action that executes all of the specified actions in order.
   */
  public static Action actions(Action... actions) {
    int len = actions.length; // implicit null check

    if (len == 0) {
      return Empty.INSTANCE;
    }

    if (len == 1) {
      return Check.notNull(actions[0], "actions[0] == null");
    }

    Action[] copy;
    copy = new Action[len];

    for (int i = 0; i < len; i++) {
      copy[i] = Check.notNull(actions[i], "actions[", i, "] == null");
    }

    return new Joined(copy);
  }

  public static byte[] getBytes() throws IOException {
    URL resource;
    resource = Script.class.getResource("script.js");

    if (resource == null) {
      throw new FileNotFoundException();
    }

    try (InputStream in = resource.openStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      in.transferTo(out);

      return out.toByteArray();
    }
  }

  public static Action addClass(Html.Id id, Html.ClassName... classes) {
    Check.notNull(id, "id == null");

    for (int idx = 0; idx < classes.length; idx++) {
      Check.notNull(classes[idx], "classes[", idx, "] == null");
    }

    return new ScriptAction() {
      @Override
      final void writeTo(StringBuilder json) {
        objectStart(json);

        property(json, CMD, "add-class");

        comma(json);

        propertyStart(json, "args");

        arrayStart(json);

        stringLiteral(json, id.value());

        for (Html.ClassName className : classes) {
          comma(json);

          stringLiteral(json, className.value());
        }

        arrayEnd(json);

        objectEnd(json);
      }
    };
  }

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

  /**
   * The no-op action.
   *
   * @return the no-op action
   */
  public static Action noop() {
    return Empty.INSTANCE;
  }

  public static Action removeClass(Html.Id id, Html.ClassName... classes) {
    Check.notNull(id, "id == null");

    for (int idx = 0; idx < classes.length; idx++) {
      Check.notNull(classes[idx], "classes[", idx, "] == null");
    }

    return new ScriptAction() {
      @Override
      final void writeTo(StringBuilder json) {
        objectStart(json);

        property(json, CMD, "remove-class");

        comma(json);

        propertyStart(json, "args");

        arrayStart(json);

        stringLiteral(json, id.value());

        for (Html.ClassName className : classes) {
          comma(json);

          stringLiteral(json, className.value());
        }

        arrayEnd(json);

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

  public static Action toggleClass(Html.Id id, String className) {
    Check.notNull(id, "id == null");
    Check.notNull(className, "className == null");

    return new ScriptAction() {
      @Override
      final void writeTo(StringBuilder json) {
        objectStart(json);

        property(json, CMD, "toggle-class");

        comma(json);

        propertyStart(json, "args");

        arrayStart(json);

        stringLiteral(json, id.value());

        comma(json);

        stringLiteral(json, className);

        arrayEnd(json);

        objectEnd(json);
      }
    };
  }

  public static Action toggleClass(Html.Id id, String class1, String class2) {
    Check.notNull(id, "id == null");
    Check.notNull(class1, "class1 == null");
    Check.notNull(class2, "class2 == null");

    return new ScriptAction() {
      @Override
      final void writeTo(StringBuilder json) {
        objectStart(json);

        property(json, CMD, "toggle-class");

        comma(json);

        propertyStart(json, "args");

        arrayStart(json);

        stringLiteral(json, id.value());

        comma(json);

        stringLiteral(json, class1);

        comma(json);

        stringLiteral(json, class2);

        arrayEnd(json);

        objectEnd(json);
      }
    };
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

      default -> {
        boolean valid = false;

        Action[] copy;
        copy = new Action[actions.length];

        for (int idx = 0; idx < actions.length; idx++) {
          Action a;
          a = Check.notNull(actions[idx], "actions[", idx, "] == null");

          if (!valid && a != Empty.INSTANCE) {
            valid = true;
          }

          copy[idx] = a;
        }

        yield new Joined(copy);
      }
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