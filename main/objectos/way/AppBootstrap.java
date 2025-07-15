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
    if (options == null) {
      return;
    }

    if (messages != null) {
      messages.clear();
    }

    Map<String, AppBootstrapOption<?>> optionsByName;
    optionsByName = Util.createMap();

    for (AppBootstrapOption<?> o : options) {
      o.acceptByName(optionsByName);
    }

    int index;
    index = 0;

    int length;
    length = args.length;

    while (index < length) {
      String arg;
      arg = args[index++];

      AppBootstrapOption<?> option;
      option = optionsByName.get(arg);

      if (option != null) {
        index = option.accept(args, index);
      }
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