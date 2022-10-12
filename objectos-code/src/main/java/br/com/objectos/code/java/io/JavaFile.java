/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package br.com.objectos.code.java.io;

import br.com.objectos.code.annotations.Ignore;
import br.com.objectos.code.java.declaration.PackageName;
import br.com.objectos.code.java.declaration.TypeCode;
import br.com.objectos.code.java.type.NamedClass;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;
import objectos.lang.Check;
import objectos.util.GrowableList;

public final class JavaFile {

  public static class Builder {

    private PackageName packageName = PackageName.unnamed();

    private final GrowableList<TypeCode> types = new GrowableList<>();

    private Builder() {}

    public final Builder addType(TypeCode type) {
      types.addWithNullMessage(type, "type == null");

      return this;
    }

    public final JavaFile build() {
      return new JavaFile(this);
    }

    public final Builder setPackage(PackageName packageName) {
      this.packageName = Check.notNull(packageName, "packageName == null");

      return this;
    }

    final TypeCode getOnly() {
      Check.state(types.size() == 1, "Expected only one type");

      return types.get(0);
    }

  }

  private final PackageName packageName;

  private final TypeCode typeCode;

  public JavaFile(PackageName packageName, TypeCode typeCode) {
    this.packageName = packageName;
    this.typeCode = typeCode;
  }

  private JavaFile(Builder builder) {
    packageName = builder.packageName;

    typeCode = builder.getOnly();
  }

  @Ignore("AggregatorGenProcessor")
  public static Builder builder() {
    return new Builder();
  }

  public static JavaFile javaFile(PackageName packageName, TypeCode type) {
    Check.notNull(packageName, "packageName == null");
    Check.notNull(type, "type == null");
    Builder b = builder();
    b.setPackage(packageName);
    b.addType(type);
    return b.build();
  }

  public final NamedClass className() {
    return typeCode.className(packageName);
  }

  public final Path resolvePath(Path basedir) {
    var packageDir = packageName.resolve(basedir);

    var simpleName = typeCode.simpleName();

    var fileName = simpleName + ".java";

    return packageDir.resolve(fileName);
  }

  /**
   * Returns this file simple name. It is the file name without the .java
   * extension.
   *
   * @return this file simple name
   */
  public final String simpleName() {
    return typeCode.simpleName();
  }

  @Override
  public final String toString() {
    ImportSet importSet = ImportSet.forPackage(packageName);
    CodeWriter writer = CodeWriter.forJavaFile(importSet);
    typeCode.acceptCodeWriter(writer);
    return writer.toJavaFile();
  }

  public final void writeTo(Filer filer) throws IOException {
    NamedClass className = className();
    JavaFileObject object = filer.createSourceFile(className.toString());
    Writer writer = object.openWriter();
    try {
      writer.write(toString());
    } finally {
      writer.close();
    }
  }

  public final Path writeTo(Path basedir) throws IOException {
    var path = resolvePath(basedir);

    var parent = path.getParent();

    Files.createDirectories(parent);

    Files.writeString(path, toString());

    return path;
  }

}