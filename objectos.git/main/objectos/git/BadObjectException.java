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

/**
 * Indicates that the contents of a file could not be read or parsed into a git
 * object.
 */
public final class BadObjectException extends GitException {

  private static final long serialVersionUID = 1L;

  /**
   * The object id.
   */
  private final ObjectId objectId;

  BadObjectException(ObjectId objectId) {
    this.objectId = objectId;
  }

  BadObjectException(ObjectId objectId, String message) {
    super(message);

    this.objectId = objectId;
  }

  BadObjectException(ObjectId objectId, String message, Throwable cause) {
    super(message, cause);

    this.objectId = objectId;
  }

  /**
   * Returns the hash value of the object that could not be read.
   *
   * @return the hash value of the object that could not be read
   */
  public final ObjectId getObjectId() {
    return objectId;
  }

}
