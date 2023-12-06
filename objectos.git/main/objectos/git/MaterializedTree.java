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

import objectos.fs.Directory;

final class MaterializedTree implements MaterializedEntry {

  final Directory directory;

  final String name;

  final ObjectId objectId;

  Tree tree;

  MaterializedTree(Directory directory, String name, ObjectId objectId) {
    this.directory = directory;

    this.name = name;

    this.objectId = objectId;
  }

  @Override
  public final boolean isTree() {
    return true;
  }

}