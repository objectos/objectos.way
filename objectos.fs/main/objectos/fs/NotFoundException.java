/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.fs;

import java.io.IOException;

/**
 * Indicates that an operation meant for an existent file failed because the
 * file does not actually exist.
 *
 * @since 2
 */
public final class NotFoundException extends IOException {

  private static final long serialVersionUID = 1L;

  NotFoundException(PathName cause) {
    super(cause.getPath());
  }

  NotFoundException(String message) {
    super(message);
  }

}