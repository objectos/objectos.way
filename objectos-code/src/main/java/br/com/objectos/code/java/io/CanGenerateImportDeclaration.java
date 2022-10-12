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

public interface CanGenerateImportDeclaration {

  /**
   * Returns either a simple string representation or the full (qualified) one.
   * 
   * If this type name or any of its components can be imported then this method
   * should return its simple String representation.
   * 
   * Note: this method, though public, is for internal use only as
   * JavaFileImportSet has package private access level.
   */
  String acceptJavaFileImportSet(JavaFileImportSet set);

}
