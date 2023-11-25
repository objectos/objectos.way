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
 * Indicates that an operation meant for a regular file failed because the
 * file is not a regular file (it is a directory for example).
 *
 * @since 2
 */
public final class NotRegularFileException extends IOException {

  private static final long serialVersionUID = 1L;

  /**
   * Creates a new instance of this exception with the specified
   * {@code PathName} as cause.
   *
   * @since 3
   */
  public NotRegularFileException(PathName cause) {
    super(cause.getPath());
  }

  NotRegularFileException(String message) {
    super(message);
  }

  NotRegularFileException(Throwable cause) {
    super(cause);
  }

}
