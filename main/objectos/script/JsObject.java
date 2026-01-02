/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectos.script;

/// Represents a JS runtime `Object` instance.
public sealed interface JsObject permits JsArray, JsElement, JsString, ScriptJsObject {

  /// Converts this reference to a JS reference of the specified type.
  ///
  /// @param <T> the JS runtime type
  ///
  /// @param type the handle representing the JS runtime type
  ///
  /// @return the converted reference
  <T> T as(JsType<T> type);

  /// Invokes the specified method with the specified arguments, in order, if
  /// the JS object is an instance of the specified type.
  ///
  /// @param type the name of the JS type that defines the method
  /// @param method the method name
  /// @param args the method arguments
  ///
  /// @return an object representing this action
  JsAction invoke(String type, String method, JsObject... args);

  /// Invokes the specified method with the specified arguments, in order, if
  /// the JS object is an instance of the specified type.
  ///
  /// @param returnType the return type of the invoked method
  /// @param type the name of the JS type that defines the method
  /// @param method the method name
  /// @param args the method arguments
  ///
  /// @return the result of the method invocation
  <T> T invoke(JsType<T> returnType, String type, String method, JsObject... args);

  /// Returns the property of the specified name, if the JS object is an
  /// instance of the specified type.
  ///
  /// @param type the name of the JS type that defines the property
  /// @param name the property name
  ///
  /// @return the property
  JsObject prop(String type, String name);

  /// Sets the property of the specified name to the specified value, if the
  /// JS object is an instance of the specified type.
  ///
  /// @param type the name of the JS type that defines the property
  /// @param name the property name
  /// @param value the property value
  ///
  /// @return an object representing this action
  JsAction prop(String type, String name, JsObject value);

}