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

import java.util.List;
import java.util.Map;
import objectos.lang.object.Check;
import objectos.util.list.GrowableList;
import objectos.util.map.GrowableMap;
import objectos.way.App.Option.Configuration;

abstract class AppBootstrap {

  private static final App.Option.Converter<String> CONVERTER_STRING = s -> s;

  private static final App.OptionConfiguration<Object> REQUIRED = new App.OptionConfiguration<>() {
    @Override
    final void accept(AppOption<Object> option) { option.required(); }
  };

  private List<AppOption<?>> options;

  private List<String> messages;

  protected final App.Option.Converter<String> ofString() {
    return CONVERTER_STRING;
  }

  @SafeVarargs
  protected final <T> App.Option<T> option(String name, App.Option.Converter<T> converter, App.Option.Configuration<T>... configurations) {
    Check.notNull(name, "name == null");
    Check.notNull(converter, "converter == null");

    AppOption<T> option;
    option = new AppOption<>(name, converter);

    for (int i = 0; i < configurations.length; i++) { // configurations implicit null-check
      App.Option.Configuration<T> c;
      c = Check.notNull(configurations[i], "configurations[", i, "] == null");

      App.OptionConfiguration<T> configuration;
      configuration = (App.OptionConfiguration<T>) c;

      configuration.accept(option);
    }

    return register(option);
  }

  protected final <T> App.Option.Configuration<T> defaultValue(T value) {
    Check.notNull(value, "value == null");

    return new App.OptionConfiguration<T>() {
      @Override
      final void accept(AppOption<T> option) {
        option.set(value);
      }
    };
  }

  /**
   * Option config: indicates that an option is required. In other words, the
   * bootstrap fails if a valid value for the option is not supplied.
   *
   * @return an option configuration
   */
  @SuppressWarnings("unchecked")
  protected final <T> App.Option.Configuration<T> required() {
    return (Configuration<T>) REQUIRED;
  }

  final void addMessage(String message) {
    if (messages == null) {
      messages = new GrowableList<>();
    }

    messages.add(message);
  }

  final boolean hasMessages() {
    return messages != null && messages.size() > 0;
  }

  final String message(int index) {
    return messages == null ? null : messages.get(index);
  }

  final void parseArgs(String[] args) {
    if (options == null) {
      return;
    }

    if (messages != null) {
      messages.clear();
    }

    Map<String, AppOption<?>> optionsByName;
    optionsByName = new GrowableMap<>();

    for (AppOption<?> o : options) {
      o.acceptByName(optionsByName);
    }

    int index;
    index = 0;

    int length;
    length = args.length;

    while (index < length) {
      String arg;
      arg = args[index++];

      AppOption<?> option;
      option = optionsByName.get(arg);

      if (option != null) {
        index = option.accept(args, index);
      }
    }

    for (AppOption<?> option : options) {
      option.validate(this);
    }
  }

  private <T> App.Option<T> register(AppOption<T> option) {
    if (options == null) {
      options = new GrowableList<>();
    }

    options.add(option);

    return option;
  }

}