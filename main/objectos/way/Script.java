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

import module java.base;

/// The **Objectos Script** main interface, part of Objectos HTML.
public sealed interface Script permits ScriptPojo {

  /// Represents an action to be executed by the JS runtime.
  public sealed interface JsAction permits ScriptJsAction {}

  /// Represents a JS runtime `Array` instance.
  public sealed interface JsArray permits ScriptJsArray {

    JsAction forEach(JsAction value);

  }

  /// Represents a JS runtime `Element` instance.
  public sealed interface JsElement extends JsObject permits ScriptJsElement {

    /// Removes this element from its parent node.
    ///
    /// @return an object representing this action
    JsAction remove();

    /// Toggles the specified `class` value on this element.
    ///
    /// @param value the class name
    ///
    /// @return an object representing this action
    JsAction toggleClass(String value);

  }

  /// Represents a JS runtime `Object` instance.
  public sealed interface JsObject permits JsElement, JsString, ScriptJsObject {

    /// Invokes the specified method with the specified arguments, in order, if
    /// the JS object is an instance of the specified type. If the method returns
    /// a value it is ignored.
    ///
    /// @param type the name of the JS type that defines the method
    /// @param method the method name
    /// @param args the method arguments
    ///
    /// @return an object representing this action
    JsAction invoke(String type, String method, Object... args);

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
    JsAction prop(String type, String name, Object value);

  }

  /// Represents a reference to a JS runtime value.
  public sealed interface JsRef permits ScriptJsRef {

    /// Converts this reference to a JS `Element` reference.
    ///
    /// @return the element
    JsElement asElem();

    /// Converts this reference to a JS `String` reference.
    ///
    /// @return the string
    JsString asString();

  }

  public sealed interface JsString extends JsObject permits ScriptJsString {}

  static JsRef args(int index) {
    return ScriptJsRef.args(index);
  }

  static JsArray array(String... values) {
    return ScriptJsArray.of(values);
  }

  static JsElement byId(JsString value) {
    return ScriptJsElement.byId(value);
  }

  /// Returns a handle for the element with the specified ID attribute.
  ///
  /// @param value the ID value
  ///
  /// @return the element
  static JsElement byId(String value) {
    return ScriptJsElement.byId(value);
  }

  /// Creates an action by concatenating all of the specified individual
  /// actions.
  ///
  /// @param first the first action
  /// @param second the second action
  /// @param more additional actions
  ///
  /// @return an object representing the concatenated action
  static JsAction of(JsAction first, JsAction second, JsAction... more) {
    return ScriptJsAction.of(first, second, more);
  }

  /// The element which triggered the Objectos Script.
  ///
  /// @return the element
  static JsElement target() {
    return ScriptJsElement.TARGET;
  }

  /// Retrieves the named property from the event context.
  ///
  /// @param name the property name
  ///
  /// @return an object representing a JS reference
  static JsRef var(String name) {
    return ScriptJsRef.var(name);
  }

  /// Stores the specified value to a named property in the event context.
  ///
  /// @param name the property name
  /// @param value the value to store
  ///
  /// @return an object representing this action
  static JsAction var(String name, Object value) {
    return ScriptJsAction.var(name, value);
  }

  /// Represents the source code of the Objectos Way JS library.
  public sealed interface Library extends Media.Text permits ScriptLibrary {

    /// Returns the sole instance of this interface.
    ///
    /// @return the sole instance of this interface
    static Library of() {
      return ScriptLibrary.INSTANCE;
    }

    /// Returns `text/javascript; charset=utf-8`.
    ///
    /// @return always `text/css; charset=utf-8`
    @Override
    String contentType();

    /// Returns `StandardCharsets.UTF_8`.
    ///
    /// @return always `StandardCharsets.UTF_8`
    @Override
    Charset charset();

    /// Writes the Objectos Way JS library source code to the specified
    /// `Appendable`.
    ///
    /// @param out where to append the source code
    ///
    /// @throws IOException if an I/O error occurs
    @Override
    void writeTo(Appendable out) throws IOException;

  }
  /*

  /// The `GET` method.
  Method GET = Method.GET;

  /// The `POST` method.
  Method POST = Method.POST;

  /// HTTP method of request operations.
  public enum Method {

    /// The `GET` method.
    GET,

    /// The `POST` method.
    POST;

  }
  /// Represents a callback to be executed by the browser's JS engine.
  @FunctionalInterface
  public interface Callback {
  
    /// Executes this callback.
    void execute();
  
  }
  
  /// Represents an element in the browser's DOM.
  public sealed interface Element permits ScriptElement {
  
    /// Returns the value of the attribute with the specified name.
    ///
    /// @param name the attribute name
    ///
    /// @return the attribute value
    StringQuery attr(Html.AttributeName name);
  
    /// Sets the specified attribute to the given value.
    ///
    /// @param name the attribute name
    /// @param value the attribute value
    void attr(Html.AttributeName name, String value);
  
    /// Sets the specified attribute to the given value.
    ///
    /// @param name the attribute name
    /// @param value the attribute value
    void attr(Html.AttributeName name, StringQuery value);
  
    /// Closes this `dialog` element.
    void close();
  
    /// Sets the focus on this element, if it can be focused.
    void focus();
  
    /// Scrolls the element to the specified `x` and `y` coordinates.
    ///
    /// @param x the pixel along the horizontal axis of the element that you want
    ///        displayed in the upper left.
    /// @param y the pixel along the vertical axis of the element that you want
    ///        displayed in the upper left.
    void scroll(int x, int y);
  
    /// Displays this `dialog` element as a modal.
    void showModal();
  
    /// Submits this `form` element.
    void submit();
  
    /// Toggles the specified `class` value on this element.
    ///
    /// @param className the class name
    void toggleClass(String className);
  
  }
  
  /// Represents the source code of the Objectos Way JS library.
  public sealed interface Library extends Media.Text permits ScriptLibrary {
  
    /// Returns the sole instance of this interface.
    ///
    /// @return the sole instance of this interface
    static Library of() {
      return ScriptLibrary.INSTANCE;
    }
  
    /// Returns `text/javascript; charset=utf-8`.
    ///
    /// @return always `text/css; charset=utf-8`
    @Override
    String contentType();
  
    /// Returns `StandardCharsets.UTF_8`.
    ///
    /// @return always `StandardCharsets.UTF_8`
    @Override
    Charset charset();
  
    /// Writes the Objectos Way JS library source code to the specified
    /// `Appendable`.
    ///
    /// @param out where to append the source code
    ///
    /// @throws IOException if an I/O error occurs
    @Override
    void writeTo(Appendable out) throws IOException;
  
  }
  
  /// Represents a reference to a boolean value in the browser's JS engine.
  public sealed interface BooleanQuery permits ScriptBooleanQuery {
  
    /// Executes the specified action if this boolean value is equal to the
    /// specified value.
    ///
    /// @param value the boolean value to compare against the value represented
    ///        by this object
    /// @param action the action to be executed
    void when(boolean value, Callback action);
  
  }
  
  /// Represents a reference to a string value in the browser's JS engine.
  public sealed interface StringQuery permits ScriptStringQuery {
  
    /// Tests this JS string value for strict equality against the specified
    /// value.
    ///
    /// @param value the string value with which to compare
    ///
    /// @return a boolean query representing either `true` or `false`
    BooleanQuery test(String value);
  
  }
  
  /// Returns a reference to the element on which the script is declared.
  ///
  /// @return a reference to the element
  Element element();
  
  /// Returns a reference to the element whose `id` property matches the value
  /// of the specified `Html.Id` instance.
  ///
  /// @param id the id of the element to locate
  ///
  /// @return a reference to the element
  Element elementById(Html.Id id);
  
  /// Returns a reference to the element whose `id` property matches the value
  /// of the specified query.
  ///
  /// @param id the id value
  ///
  /// @return a reference to the element
  Element elementById(StringQuery id);
  
  // actions
  
  /// Executes the specified action after the specified initial delay.
  ///
  /// @param ms the delay in milliseconds.
  /// @param action the action to be executed
  void delay(int ms, Callback action);
  
  /// Swaps the document's `data-frame` contents with the ones from the
  /// specified HTML document.
  ///
  /// @param template the HTML document
  void html(Html.Template template);
  
  /// Performs a soft navigation to the URL specified by the `href` attribute of
  /// this `<a>` HTML element.
  void navigate();
  
  /// Causes the specified `url` to be pushed into the browser's location
  /// history.
  ///
  /// @param url the location history entry
  void pushState(String url);
  
  /// Causes the specified `url` to replace the current entry on the browser's
  /// location history.
  ///
  /// @param url the location history entry
  void replaceState(String url);
  
  /// Configures the [request][#request(Consumer)] action.
  public sealed interface RequestOptions permits ScriptRequestOptions {
  
    /// Sets the HTTP method of this request.
    ///
    /// @param value the HTTP method
    //    void method(Script.Method value);
  
    /// Sets the request URL to the specified value.
    ///
    /// @param value the URL of the resource
    void url(String value);
  
    /// Sets the request URL to the specified value.
    ///
    /// @param value the URL to the resource
    void url(StringQuery value);
  
    /// Sets the action to be executed if the request is successful.
    ///
    /// @param value the action to be executed
    void onSuccess(Callback value);
  
  }
  
  /// Causes the browser to perform a HTTP request.
  ///
  /// @param options allows for configuring the request
  void request(Consumer<? super RequestOptions> options);
  
  /// Causes the event handling to stop at the current HTML element.
  void stopPropagation();
  */

}