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
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardLocation;

class CompilationJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

  final Map<String, WritableByteArrayJavaFileObject> generatedClassFiles = new LinkedHashMap<>();
  final Map<String, WritableStringJavaFileObject> generatedJavaFiles = new LinkedHashMap<>();
  final Map<String, WritableByteArrayJavaFileObject> generatedResources = new LinkedHashMap<>();

  CompilationJavaFileManager(JavaCompiler compiler) {
    super(compiler.getStandardFileManager(null, Locale.getDefault(), Charset.defaultCharset()));
  }

  @Override
  public final FileObject getFileForOutput(
      Location location, String packageName, String relativeName, FileObject sibling)
      throws IOException {
    if (StandardLocation.CLASS_OUTPUT.equals(location)) {
      return getFileForOutput0(packageName, relativeName);
    }

    else {
      return super.getFileForOutput(location, packageName, relativeName, sibling);
    }
  }

  @Override
  public final JavaFileObject getJavaFileForOutput(
      Location location, String className, Kind kind, FileObject sibling)
      throws IOException {
    if (StandardLocation.SOURCE_OUTPUT.equals(location)) {
      WritableStringJavaFileObject file;
      file = generatedJavaFiles.get(className);

      if (file == null) {
        file = new WritableStringJavaFileObject(className, kind);

        generatedJavaFiles.put(className, file);
      }

      return file;
    }

    else if (StandardLocation.CLASS_OUTPUT.equals(location)) {
      WritableByteArrayJavaFileObject file;
      file = generatedClassFiles.get(className);

      if (file == null) {
        file = new WritableByteArrayJavaFileObject(className, kind);

        generatedClassFiles.put(className, file);
      }

      return file;
    }

    else {
      return super.getJavaFileForOutput(location, className, kind, sibling);
    }
  }

  @Override
  public final boolean isSameFile(FileObject a, FileObject b) {
    return a.toUri().equals(b.toUri());
  }

  private FileObject getFileForOutput0(String packageName, String relativeName) {
    String resourceName;
    resourceName = getResourceName(packageName, relativeName);

    WritableByteArrayJavaFileObject file;
    file = generatedResources.get(resourceName);

    if (file == null) {
      file = new WritableByteArrayJavaFileObject(resourceName, Kind.OTHER);

      generatedResources.put(resourceName, file);
    }

    return file;
  }

  private String getResourceName(String packageName, String relativeName) {
    if (packageName.equals("")) {
      return relativeName;
    }

    StringBuilder resourceName;
    resourceName = new StringBuilder();

    resourceName.append(packageName.replace('.', '/'));

    resourceName.append('/');

    resourceName.append(relativeName);

    return resourceName.toString();
  }

}