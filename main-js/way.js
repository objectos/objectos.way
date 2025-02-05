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
const way = (function() {

  "use strict";

  const way = {

    config: {

      errorHandler: function(error) {
        throw error;
      },

      history: true

    }

  };

  const actionHandlers = {
    "delay-0": executeDelay0,
    "html-0": executeHtml0,
    "id-1": executeId1,
    "id-2": executeId2,
    "navigate-0": executeNavigate0,
    "push-state-0": executePushState0,
    "replace-state-0": executeReplaceState0,
    "request-0": executeRequest0,
    "scroll-0": executeScroll0,
    "stop-propagation-0": executeStopPropagation0
  };

  const defaultScroll = [['scroll-0', 0, 0, "instant"]];

  // ##################################################################
  // # BEGIN: DOM Event Handlers
  // ##################################################################

  function clickListener(event) {
    let target = event.target;

    while (target instanceof Node) {
      const element = target;

      target = target.parentNode;

      const result = executeEvent(element, "onClick");

      if (result) {
        event.preventDefault();

        break;
      }

      // there was no data-on-click action...
      // but we explicitly stop at these elements

      if (element instanceof HTMLAnchorElement ||
        element instanceof HTMLButtonElement) {
        break;
      }
    }
  }

  function inputListener(event) {
    const target = event.target;

    executeEvent(target, "onInput");
  }

  function submitListener(event) {
    const target = event.target;

    // verify we have all of the required properties
    const tagName = target.tagName;

    if (tagName !== "FORM") {
      return;
    }

    let action = target.getAttribute("action");

    if (!action) {
      return;
    }

    let method = target.getAttribute("method");

    if (!method) {
      return;
    }

    method = method.toUpperCase();

    let onSuccess = [];

    const dataset = target.dataset;

    if (dataset) {
      const data = dataset["onSuccess"];

      if (data) {
        onSuccess = JSON.parse(data);
      }
    }

    // this is possibly a way form, we shouldn't submit it
    event.preventDefault();

    const formData = new FormData(target);

    if (method === "GET") {
      const params = new URLSearchParams(formData);

      action = action + "?" + params;

      const xhr = createXhr({ method: method, url: action, element: target, onSuccess: onSuccess });

      xhr.send();
    }

    else {
      const xhr = createXhr({ method: method, url: action, element: target, onSuccess: onSuccess });

      const enctype = target.getAttribute("enctype");

      if (enctype && enctype === "multipart/form-data") {
        xhr.send(formData);
      } else {
        const params = new URLSearchParams(formData);

        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

        xhr.send(params.toString());
      }
    }
  }

  function popstateListener() {
    const url = window.location.href;

    const xhr = createXhr({ url: url, history: false, onSuccess: [defaultScroll] });

    xhr.send();
  }

  function executeEvent(element, name) {
    const dataset = element.dataset;

    if (!dataset) {
      return false;
    }

    const data = dataset[name];

    if (!data) {
      return false;
    }

    const way = JSON.parse(data);

    return executeActions(way, element);
  }

  // ##################################################################
  // # END: DOM Event Handlers
  // ##################################################################

  // ##################################################################
  // # BEGIN: Objectos Way Actions
  // ##################################################################

  function executeActions(actions, element) {
    checkArray(actions);

    if (actions.length === 0) {
      return false;
    }

    let count = 0;

    for (const action of actions) {
      checkArray(action);

      if (action.length === 0) {
        continue;
      }

      const key = action.shift();

      const handler = actionHandlers[key];

      if (handler) {
        count++;

        handler(action, element);
      } else {
        console.error("Unknown action %s", key);
      }
    }

    return count > 0;
  }

  function executeDelay0(args, el) {
    if (args.length !== 2) {
      console.error("delay-0: action invoked with the wrong number of args. Expected 2 but got %d", args.length);

      return;
    }

    const ms = args[0];

    if (!ms) {
      console.error("delay-0: missing 'ms' property");

      return;
    }

    if (!Number.isInteger(ms)) {
      console.error("delay-0: invalid 'ms' property. Expected integer but got %s", ms);

      return;
    }

    const actions = args[1];

    if (!actions) {
      console.error("delay-0: missing 'actions' array value");

      return;
    }

    if (!Array.isArray(actions)) {
      console.error("delay: invalid 'actions' property. Expected Array but got %s", actions);

      return;
    }

    if (el) {

      const dataset = el.dataset;

      let timer = dataset.timer;

      if (!timer) {
        timer = 0;
      }

      const delay = function() {
        clearTimeout(timer);

        timer = setTimeout(() => executeActions(actions, el), ms);

        dataset.timer = timer;
      }

      delay();

      return;

    }

    setTimeout(() => executeActions(actions), ms);
  }

  function executeHtml(value) {
    if (!value) {
      return;
    }

    const parser = new DOMParser();

    const newContent = parser.parseFromString(value, "text/html");

    // handle title

    const newTitle = newContent.querySelector("title");

    if (newTitle) {
      document.title = newTitle.innerText;
    }

    // handle frames
    const newFrames = newContent.querySelectorAll("[data-frame]");

    const newNameMap = new Map();

    for (const el of newFrames) {
      const data = frame(el);

      if (data) {
        newNameMap.set(data.name, el);
      }
    }

    const frames = document.querySelectorAll("[data-frame]");

    const replaced = new Set();

    outer: for (const elem of frames) {
      const data = frame(elem);

      if (!data) {
        continue;
      }

      for (const parent of replaced) {
        if (parent.contains(elem)) {
          continue outer;
        }
      }

      const name = data.name;

      const maybe = newNameMap.get(name);

      if (!maybe) {
        // this frame does not exist in the new data
        elem.remove();

        continue;
      }

      const newElem = maybe;

      const oldValue = data.value;

      const newData = frame(newElem);

      const newValue = newData.value;

      if (oldValue !== newValue || oldValue === null && newValue === null) {
        replaced.add(elem);

        elem.replaceWith(newElem);
      }
    }
  }

  function executeHtml0(args) {
    if (args.length !== 1) {
      logError("Illegal number of args", { action: "html-0", expected: 1, actual: args });

      return;
    }

    const value = args[0];

    executeHtml(value);
  }

  function executeId1(args) {
    queryId1(args);
  }

  function executeId2(args) {
    queryId2(args);
  }

  function executeNavigate0(_, element) {
    if (!(element instanceof HTMLAnchorElement)) {
      const actual = element.constructor ? element.constructor.name : "Unknown";

      throw new Error(`Illegal element: navigate-0 must be executed on an HTMLAnchorElement but got ${actual}`);
    }

    const actions = [

      ["request-0", "GET", ["element-1", "getAttribute", "href"], [defaultScroll]]

    ];

    executeActions(actions, element);
  }

  function executePushState0(args) {
    checkArgsLength(args, 1, "push-state-0");

    const url = args[0];

    history.pushState({ way: true }, "", url);
  }

  function executeReplaceState0(args) {
    checkArgsLength(args, 1, "replace-state-0");

    const url = args[0];

    history.replaceState({ way: true }, "", url);
  }

  function executeRequest0(args, element) {
    checkArgsLength(args, 3, "request-0");

    // delegate validation to createXhr
    const method = args[0];

    const url = stringQuery(args[1], element);

    // delegate validation to createXhr
    const onSuccess = args[2];

    const xhr = createXhr({ method: method, url: url, element: element, onSuccess: onSuccess });

    xhr.send();
  }

  function executeScroll0(args) {
    if (args.length !== 3) {
      logError("Illegal number of args", { action: "scroll-0", expected: 3, actual: args });

      return;
    }

    const value = {
      top: args[0],

      left: args[1],

      behavior: args[2]
    }

    window.scroll(value);
  }

  function executeStopPropagation0() {
    // noop
  }

  function checkArgsLength(args, expected, action) {
    if (args.length !== expected) {
      throw new Error(`Illegal number of args: ${action} action expected ${expected} args but got ${args.length}`);
    }
  }

  function frame(el) {
    const dataset = el.dataset;

    if (!dataset) {
      return null;
    }

    const frame = dataset.frame;

    const colon = frame.indexOf(":");

    if (colon === -1) {
      return { name: frame, value: null };
    }

    const name = frame.substring(0, colon);

    const value = frame.substring(colon + 1);

    return { name: name, value: value };
  }

  // ##################################################################
  // # END: Objectos Way Actions
  // ##################################################################

  // ##################################################################
  // # BEGIN: Objectos Way Query
  // ##################################################################

  const queryHandlers = {
    "element-1": queryElement1,
    "id-1": queryId1,
    "id-2": queryId2
  };

  function stringQuery(value, element) {
    if (typeof value === "string") {
      return value;
    }

    const maybe = query(value, element);

    if (typeof maybe !== "string") {
      throw new Error(`Invalid type: expected String value but query [${value}] on ${element.cloneNode(false).outerHTML} returned ${maybe}`);
    }

    return maybe;
  }

  function query(query, element) {
    checkArray(query, "query");
    checkArrayLengthMin(query, 2, "query");

    const key = query.shift();

    const handler = queryHandlers[key];

    if (!handler) {
      throw new Error(`Illegal arg: unknown query for ${key}`);
    }

    return handler(query, element);
  }

  function queryElement1(args, element) {
    return elementMethod(args, element);
  }

  function elementMethod(args, element) {
    checkArrayLengthMin(args, 1, "args");

    const methodName = checkString(args.shift(), "methodName");

    const method = element[methodName];

    if (!method) {
      throw new Error(`Illegal arg: method ${methodName} not found on ${element}`);
    }

    return method.apply(element, args);
  }

  function queryId(args) {
    checkArrayLengthMin(args, 1, "args");

    const id = checkString(args.shift(), "id");

    const element = document.getElementById(id);

    if (!element) {
      throw new Error(`Illegal arg: element not found with ID ${id}`);
    }

    return element;
  }

  function queryId1(args) {
    const element = queryId(args);

    return elementMethod(args, element);
  }

  function queryId2(args) {
    const element = queryId(args);

    elementAction(args, element);
  }

  const elementActions = {
    "attr-0": elementAttr0,
    "submit-0": elementSubmit0,
    "toggle-class-0": elementToggleClass0
  };

  function elementAction(args, element) {
    checkArrayLengthMin(args, 1, "args");

    const key = args.shift();

    const action = elementActions[key];

    if (!action) {
      throw new Error(`Illegal arg: unknown element action for ${key}`);
    }

    action(args, element);
  }

  function elementAttr0(args, element) {
    checkArgsLength(args, 2, "arg");

    const name = args.shift();

    const value = stringQuery(args.shift());

    element.setAttribute(name, value);
  }

  function elementSubmit0(_, element) {
    if (!(element instanceof HTMLFormElement)) {
      const actual = element.constructor ? element.constructor.name : "Unknown";

      throw new Error(`Illegal element: submit-0 must be executed on an HTMLFormElement but got ${actual}`);
    }

    const event = new Event("submit", { bubbles: true });

    element.dispatchEvent(event);
  }

  function elementToggleClass0(args, element) {
    checkArrayLengthMin(args, 1, "args");

    const classList = element.classList;

    for (const className of args) {
      classList.toggle(className);
    }
  }

  // ##################################################################
  // # END: Objectos Way Query
  // ##################################################################

  // ##################################################################
  // # BEGIN: private private
  // ##################################################################

  function checkArray(maybe, name) {
    if (!Array.isArray(maybe)) {
      throw new Error(`Illegal arg: ${name} must be an Array value but got ${maybe}`);
    }

    return maybe;
  }

  function checkArrayLengthMin(array, minLength, name) {
    if (array.length < minLength) {
      throw new Error(`Illegal arg: ${name} array length must be >= ${minLength} but got ${array.length}`);
    }
  }

  function checkBoolean(maybe, name) {
    if (typeof maybe !== "boolean") {
      throw new Error(`Illegal arg: ${name} must be a Boolean value but got ${maybe}`);
    }

    return maybe;
  }

  function checkElement(maybe) {
    if (!(maybe instanceof Element)) {
      throw new Error(`Illegal arg: expected an Element value but got ${maybe}`);
    }

    return maybe;
  }

  function checkString(maybe, name) {
    if (typeof maybe !== "string") {
      throw new Error(`Illegal arg: ${name} must be a String value but got ${maybe}`);
    }

    return maybe;
  }

  function createXhr({ method = "GET", url, element, history = true, onSuccess = [] } = {}) {
    checkString(method, "method");
    checkBoolean(history, "history");
    checkArray(onSuccess, "onSuccess");

    const xhr = new XMLHttpRequest();

    xhr.open(method, url, true);

    xhr.setRequestHeader("Way-Request", "true");

    xhr.onload = (_) => {
      if (xhr.status === 200) {

        const contentType = xhr.getResponseHeader("content-type");

        if (!contentType) {
          return;
        }

        let data;

        if (contentType.startsWith("application/json")) {
          data = JSON.parse(xhr.response);
        }

        else if (contentType.startsWith("text/html")) {
          data = [];

          data.push(['html-0', xhr.response]);

          if (history && way.config.history) {
            const pushUrl = xhr.responseURL || url;

            data.push(['push-state-0', pushUrl]);
          }
        }

        else {
          return;
        }

        const actions = data.concat(onSuccess);

        executeActions(actions, element);

      }
    }

    return xhr;
  }

  function logError(...args) {
    console.error(...args);
  }

  // ##################################################################
  // # END: private private
  // ##################################################################

  // ##################################################################
  // # BEGIN: Objectos Way Bootstrap
  // ##################################################################

  function listener(actual) {
    return function(event) {
      try {
        actual.call(this, event)
      } catch (error) {
        way.config.errorHandler(error);
      }
    };
  }

  function domLoaded() {
    const body = document.body;

    body.addEventListener("click", listener(clickListener));
    body.addEventListener("input", listener(inputListener));
    body.addEventListener("submit", listener(submitListener));
  }

  window.addEventListener("DOMContentLoaded", domLoaded);
  window.addEventListener("popstate", popstateListener);

  // ##################################################################
  // # END: Objectos Way Bootstrap
  // ##################################################################

  return way;

})();