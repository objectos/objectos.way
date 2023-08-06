/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.carbonated;

import objectos.carbonated.internal.NotificationImpl;
import objectos.carbonated.internal.StyleSheetBuilderImpl;
import objectos.css.StyleSheet;
import objectos.css.util.ClassSelector;
import objectos.html.tmpl.Instruction.ElementContents;

/**
 * @since 0.7.1
 */
public final class Carbon {

  public enum Contrast {
    HIGH,

    LOW;
  }

  public enum Status {
    ERROR,

    SUCCESS,

    WARNING,

    INFO;
  }

  public sealed interface Notification permits NotificationImpl {

    Notification contrast(Contrast value);

    Notification status(Status value);

    Notification title(String value);

    Notification subtitle(String value);

    ElementContents render();

  }

  public sealed interface StyleSheetBuilder permits StyleSheetBuilderImpl {

    StyleSheet build();

    /**
     * Enables the notification CSS generation.
     *
     * <p>
     * Enables:
     *
     * <ul>
     * <li>Reset;
     * <li>Typography.
     * </ul>
     *
     * <p>
     * Requires at least one theme.
     *
     * @return this builder
     */
    StyleSheetBuilder notification();

    StyleSheetBuilder reset();

    StyleSheetBuilder themes(Theme... values);

    StyleSheetBuilder typography();

  }

  public static final ClassSelector WHITE_THEME = ClassSelector.randomClassSelector(5);

  private Carbon() {}

  public static Notification notification() {
    return new NotificationImpl();
  }

  public static StyleSheetBuilder styleSheetBuilder() {
    return new StyleSheetBuilderImpl();
  }

}