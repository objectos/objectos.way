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

import objectos.carbonated.internal.CompNotification;
import objectos.html.tmpl.Instruction.ElementContents;

public sealed interface Notification permits CompNotification {

  enum Contrast {
    HIGH,

    LOW;
  }

  enum Status {
    ERROR,

    SUCCESS,

    WARNING,

    INFO;
  }

  static Notification of() {
    return new CompNotification();
  }

  Notification contrast(Notification.Contrast value);

  Notification status(Notification.Status value);

  Notification title(String value);

  Notification subtitle(String value);

  ElementContents render();

}