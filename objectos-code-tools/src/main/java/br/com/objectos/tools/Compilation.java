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

import java.util.Map;
import objectos.lang.Check;

public final class Compilation {

  private final Map<String, ? extends GeneratedClassFile> generatedClassFiles;

  private final Map<String, ? extends GeneratedJavaFile> generatedJavaFiles;

  private final Map<String, ? extends GeneratedResource> generatedResources;

  private final String message;

  private final boolean successful;

  private Compilation(Builder builder) {
    successful = builder.successful();

    message = builder.message();

    generatedClassFiles = builder.generatedClassFiles();

    generatedJavaFiles = builder.generatedJavaFiles();

    generatedResources = builder.generatedResources();
  }

  public final void assertWasSuccessful() {
    if (!successful) {
      throw new AssertionError(message);
    }
  }

  public final boolean containsClassFile(String qualifiedName) {
    return generatedClassFiles.containsKey(qualifiedName);
  }

  public final boolean containsJavaFile(String qualifiedName) {
    return generatedJavaFiles.containsKey(qualifiedName);
  }

  public final boolean containsResource(String resourceName) {
    return generatedResources.containsKey(resourceName);
  }

  public final GeneratedClassFile getClassFile(String qualifiedName) {
    Check.notNull(qualifiedName, "qualifiedName == null");
    checkKey(generatedClassFiles, qualifiedName);

    return generatedClassFiles.get(qualifiedName);
  }

  public final GeneratedJavaFile getJavaFile(String qualifiedName) {
    Check.notNull(qualifiedName, "qualifiedName == null");
    checkKey(generatedJavaFiles, qualifiedName);

    return generatedJavaFiles.get(qualifiedName);
  }

  public final String getMessage() {
    return message;
  }

  public final GeneratedResource getResource(String resourceName) {
    Check.notNull(resourceName, "resourceName == null");
    checkKey(generatedResources, resourceName);
    return generatedResources.get(resourceName);
  }

  public final boolean wasSuccessful() {
    return successful;
  }

  private void checkKey(Map<String, ?> map, String key) {
    if (!map.containsKey(key)) {
      throw new AssertionError("Compilation did not generate [" + key + "]");
    }
  }

  abstract static class Builder {

    public final Compilation build() {
      return new Compilation(this);
    }

    abstract Map<String, ? extends GeneratedClassFile> generatedClassFiles();

    abstract Map<String, ? extends GeneratedJavaFile> generatedJavaFiles();

    abstract Map<String, ? extends GeneratedResource> generatedResources();

    abstract String message();

    abstract boolean successful();

  }

}