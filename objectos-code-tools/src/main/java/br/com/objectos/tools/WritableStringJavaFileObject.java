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
package br.com.objectos.tools;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import javax.tools.SimpleJavaFileObject;

class WritableStringJavaFileObject extends SimpleJavaFileObject implements GeneratedJavaFile {

  private final String qualifiedName;
  private String source = "";

  WritableStringJavaFileObject(String qualifiedName, Kind kind) {
    super(URI.create(new StringBuilder()
        .append("writable:///")
        .append(qualifiedName.replace('.', '/'))
        .append(kind.extension)
        .toString()), kind);
    this.qualifiedName = qualifiedName;
  }

  @Override
  public final String contents() {
    return source;
  }

  @Override
  public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
    return source;
  }

  @Override
  public Writer openWriter() throws IOException {
    return new StringWriter() {
      @Override
      public void close() throws IOException {
        super.close();
        source = toString();
      }
    };
  }

  @Override
  public String qualifiedName() {
    return qualifiedName;
  }

  @Override
  public final String toString() {
    return source;
  }

}