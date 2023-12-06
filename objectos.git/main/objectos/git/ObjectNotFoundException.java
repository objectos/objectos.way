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
 * Indicates a git object denoted by a given hash value could not be found in a
 * repository.
 *
 * @since 1
 */
public final class ObjectNotFoundException extends GitException {

  private static final long serialVersionUID = 1L;

  /**
   * The object id.
   */
  private final ObjectId objectId;

  ObjectNotFoundException(ObjectId objectId) {
    this.objectId = objectId;
  }

  /**
   * Returns the hash value (as hex string) of the object that could be found.
   *
   * @return the hash value (as hex string) of the object that could be found
   */
  @Override
  public final String getMessage() {
    return objectId.getHexString();
  }

}
