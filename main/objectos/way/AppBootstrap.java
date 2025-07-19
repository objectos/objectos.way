/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

import java.util.List;
import java.util.Map;

abstract class AppBootstrap {

  private List<AppBootstrapOption<?>> options;

  private List<String> messages;

  final void addMessage(String message) {
    if (messages == null) {
      messages = Util.createList();
    }

    messages.add(message);
  }

  final String message(int index) {
    return messages == null ? null : messages.get(index);
  }

  final int messagesSize() {
    return messages == null ? 0 : messages.size();
  }

  final void parseArgs(String[] args) {
    final List<AppBootstrapOption<?>> options;
    options = this.options != null ? this.options : List.of();

    if (messages != null) {
      messages.clear();
    }

    final Map<String, AppBootstrapOption<?>> byName;
    byName = Util.createMap();

    for (AppBootstrapOption<?> option : options) {
      final String name;
      name = option.name;

      final AppBootstrapOption<?> previous;
      previous = byName.put(name, option);

      if (previous != null) {
        throw new IllegalArgumentException("Duplicate option name: " + name);
      }
    }

    int index;
    index = 0;

    final int length;
    length = args.length;

    while (index < length) {
      final String arg;
      arg = args[index++];

      final AppBootstrapOption<?> option;
      option = byName.get(arg);

      if (option == null) {
        final String msg;
        msg = "Unrecognized option '%s'".formatted(arg);

        addMessage(msg);

        break;
      }

      index = option.accept(args, index);
    }

    for (AppBootstrapOption<?> option : options) {
      option.validate(this);
    }
  }

  final <T> AppBootstrapOption<T> register(AppBootstrapOption<T> option) {
    if (options == null) {
      options = Util.createList();
    }

    options.add(option);

    return option;
  }

}