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
 * Defines the operation mode of a {@code ObjectReader}.
 *
 * @since 3
 */
enum ObjectReaderMode {

  /**
   * Checks if an object exists in a repository. Does not read object header nor
   * object body.
   */
  EXISTS,

  /**
   * Reads the object completely.
   */
  READ_OBJECT;

}