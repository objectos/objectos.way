/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.constantpool.ClassEntry;
import java.util.HashSet;
import java.util.Set;

final class CssEngine2ClassFiles implements CssEngine2.ClassFiles {

  final Set<String> scan = new HashSet<>();

  final Set<String> scanIfAnnotated = new HashSet<>();

  @Override
  public final void scan(String name, byte[] bytes) {
    scan0(scan, bytes);
  }

  @Override
  public final void scanIfAnnotated(String name, byte[] bytes) {
    scan0(scanIfAnnotated, bytes);
  }

  private void scan0(Set<String> names, byte[] bytes) {
    final ClassFile classFile;
    classFile = ClassFile.of();

    final ClassModel model;
    model = classFile.parse(bytes);

    final ClassEntry thisClass;
    thisClass = model.thisClass();

    final String iname;
    iname = thisClass.asInternalName();

    names.add(iname);
  }

}
