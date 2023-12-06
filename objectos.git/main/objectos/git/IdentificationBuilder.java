/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
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
package objectos.git;

import objectos.lang.object.Check;

final class IdentificationBuilder {

  String email;

  String gitTimeZone;

  String name;

  long seconds;

  private boolean valid;

  IdentificationBuilder() {}

  public final void setEmail(String email) {
    this.email = Check.notNull(email, "email == null");
  }

  public final void setName(String name) {
    this.name = Check.notNull(name, "name == null");
  }

  final Identification build() {
    return new Identification(this);
  }

  final boolean isValid() {
    return valid;
  }

  final void reset() {
    email = null;

    gitTimeZone = null;

    name = null;

    seconds = 0;

    valid = false;
  }

  final void setGitTimeZone(String value) {
    this.gitTimeZone = Check.notNull(value, "value == null");
  }

  final void setSeconds(long seconds) {
    this.seconds = seconds;
  }

  final void setValid() {
    valid = true;
  }

}