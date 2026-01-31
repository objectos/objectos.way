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

  // ##################################################################
  // # BEGIN: DOM Event Handlers
  // ##################################################################

  function clickListener(event) {
    let el = event.target;

    while (el instanceof Node) {
      const result = executeEvent(el, "onClick");

      if (result) {
        event.preventDefault();

        break;
      }

      // there was no data-on-click action...
      // but we explicitly stop at these elements

      if (el instanceof HTMLAnchorElement) {
        break;
      }

      if (el instanceof HTMLButtonElement) {
        break;
      }

      // continue with this element's parent

      el = el.parentNode;
    }
  }

  function submitListener(event) {
    const el = event.target;

    if (!(el instanceof HTMLFormElement)) {
      return;
    }

    const action = el.getAttribute("action");

    if (!action) {
      return;
    }

    const method = el.getAttribute("method");

    if (!method) {
      return;
    }

    // this is a way form,
    // we'll submit it via fetch API
    event.preventDefault();

    const formData = new FormData(el);

    const ctx = {
      // ctx
      $el: el,
      $recv: undefined,

      // requestInit
      headers: {
        "Way-Request": true
      },

      method: method.toUpperCase()
    };

    if (ctx.method === "GET") {
      throw new Error(`Implement me :: method=${ctx.method}`);
    }

    else {
      const enctype = el.getAttribute("enctype");

      if (enctype === "multipart/form-data") {
        throw new Error(`Implement me :: enctype=${enctype}`);
      }

      else {
        ctx.body = new URLSearchParams(formData);

        ctx.headers["Content-Type"] = "application/x-www-form-urlencoded";
      }

      fetch0(ctx, action);
    }
  }

  function executeEvent(el, name) {
    const dataset = el.dataset;

    if (!dataset) {
      return false;
    }

    const data = dataset[name];

    if (!data) {
      return false;
    }

    const ctx = {
      $el: el,
      $recv: undefined
    };

    const action = JSON.parse(data);

    execute(ctx, action);

    return true;
  }

  // ##################################################################
  // # END: DOM Event Handlers
  // ##################################################################

  // ##################################################################
  // # BEGIN: Fetch support
  // ##################################################################

  function fetch0(ctx, resource) {
    globalThis.fetch(resource, ctx).then(resp => {

      const headers = resp.headers;

      const contentType = headers.get("Content-Type");

      if (!contentType) {
        throw new Error("Invalid response: no content-type");
      }

      else if (contentType.startsWith("text/html")) {

        resp.text().then(html => {
          const global = globalThis;

          // update browser location
          const history = global.history;

          if (history) {
            const state = { way: true };

            const unused = "";

            const url = resp.url;

            history.pushState(state, unused, url);
          }

          // morph
          const recv = ctx.$recv !== undefined ? ctx.$recv : documentElement();

          morph0(recv, html);
        });

      }

      else {
        throw new Error(`Invalid response: unsupported content-type ${contentType}`);
      }

    });
  }


  // ##################################################################
  // # END: Fetch support
  // ##################################################################

  // ##################################################################
  // # BEGIN: Actions support
  // ##################################################################

  function execute(ctx, action) {
    checkArray(action, "action");

    const id = checkString(action.shift(), "id");

    const handler = operations[id];

    if (handler) {
      return handler(ctx, action);
    } else {
      throw new Error(`Illegal arg: no handler found for operation=${id}`);
    }
  }

  function nest(ctx, action) {
    checkArray(action, "action");

    return (...args) => {
      const nested = { ...ctx };

      nested.$args = args;

      const nestedAction = structuredClone(action);

      return execute(nested, nestedAction);
    };
  }

  // ##################################################################
  // # END: Actions support
  // ##################################################################

  // ##################################################################
  // # BEGIN: Objectos Way Actions
  // ##################################################################

  const operations = {
    "AX": argsRead,
    "CR": contextRead,
    "CW": contextWrite,
    "EI": elementById,
    "ET": elementTarget,
    "FE": forEach,
    "FN": functionJs,
    "GR": globalRead,
    "IF": ifElse,
    "IU": invokeUnchecked,
    "IV": invokeVirtual,
    "JS": jsValue,
    "MO": morph,
    "NA": navigate,
    "NO": noop,
    "PR": propertyRead,
    "pr": propertyReadUnchecked,
    "PW": propertyWrite,
    "TE": throwError,
    "TY": typeEnsure,
    "W1": wayOne,
    "WS": waySeq
  };

  function argsRead(ctx, args) {
    const index = checkInteger(args.shift(), "index");

    if (index < 0) {
      throw new Error(`Illegal arg: index must not be negative. index=${index}`);
    }

    const values = ctx.$args;

    if (!values) {
      throw new Error(`Illegal arg: context does not define $args`);
    }

    return values[index];
  }

  function contextRead(ctx, args) {
    const name = contextName(args.shift());

    return ctx[name];
  }

  function contextWrite(ctx, args) {
    const name = contextName(args.shift());

    const val = checkDefined(args.shift(), "value");

    ctx[name] = execute(ctx, val);
  }

  function contextName(maybe) {
    const name = checkString(maybe, "name");

    if (name.length === 0) {
      throw new Error("Illegal arg: context variable name must not be empty");
    }

    if (name.startsWith("$")) {
      throw new Error("Illegal arg: context variable name must not begin with the '$' character");
    }

    return name;
  }

  function elementById(ctx, args) {
    const action = checkDefined(args.shift(), "action");

    const id = execute(ctx, action);

    checkString(id, "id");

    const element = document.getElementById(id);

    if (!element) {
      throw new Error(`Illegal arg: element not found with ID ${id}`);
    }

    return element;
  }

  function elementTarget(ctx, _) {
    return ctx.$el;
  }

  function forEach(ctx, args) {
    const recv = checkRecv(ctx, args.shift());

    const action = checkDefined(args.shift(), "action");

    const fn = nest(ctx, action);

    recv.forEach(fn);
  }

  function functionJs(ctx, args) {
    const action = checkDefined(args.shift(), "action");

    return nest(ctx, action);
  }

  function globalRead() {
    return globalThis;
  }

  function ifElse(ctx, args) {
    const recv = checkRecv(ctx, "boolean");

    const onTrue = checkDefined(args.shift(), "onTrue");

    const onFalse = checkDefined(args.shift(), "onFalse");

    const action = recv ? onTrue : onFalse;

    return execute(ctx, action);
  }

  function invokeUnchecked(ctx, args) {
    const recv = ctx.$recv;

    return invoke0(ctx, args, recv);
  }

  function invokeVirtual(ctx, args) {
    const recv = checkRecv(ctx, args.shift());

    return invoke0(ctx, args, recv);
  }

  function invoke0(ctx, args, recv) {
    const methodName = checkString(args.shift(), "methodName");

    const method = recv[methodName];

    if (!method) {
      throw new Error(`Illegal arg: ${recv} does not declare the ${methodName} method`);
    }

    const encodedArgs = checkArray(args.shift(), "encodedArgs");

    const methodArgs = encodedArgs.map(x => execute(ctx, x));

    return method.apply(recv, methodArgs);
  }

  function jsValue(_, args) {
    return checkDefined(args.shift(), "value");
  }

  function morph(ctx, args) {
    const recv = ctx.$recv;

    const $src = checkDefined(args.shift(), "src");

    const src = checkString(execute(ctx, $src), "src");

    morph0(recv, src);
  }

  function morph0(recv, src) {
    const valid = typeof recv.querySelectorAll === 'function';

    if (!valid) {
      throw new Error(`Illegal arg: ${recv} does not declare the 'querySelectorAll' method`);
    }

    const parser = new DOMParser();

    const newContent = parser.parseFromString(src, "text/html");

    morph1Head(recv, newContent);

    // handle frames
    const newFrames = newContent.querySelectorAll("[data-frame]");

    const newNameMap = new Map();

    for (const el of newFrames) {
      const data = frame(el);

      if (data) {
        newNameMap.set(data.name, el);
      }
    }

    const frames = recv.querySelectorAll("[data-frame]");

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

        //loadHandler(newElem);
      }
    }
  }

  function morph1Head(content, newContent) {
    const newHead = newContent.querySelector("head");

    if (!newHead) {
      return;
    }

    const head = content.querySelector("head");

    if (!head) {
      return;
    }

    const newEls = new Map();

    for (const newChild of newHead.children) {
      const newHtml = newChild.outerHTML;

      newEls.set(newHtml, newChild);
    }

    const remEls = [];

    for (const child of head.children) {
      const html = child.outerHTML;

      const newChild = newEls.get(html);

      if (newChild) {
        // current elem exists in new head
        // => let's keep it
        newEls.delete(html);
      } else {
        // current elem does not exist in new head
        // => let's remove it
        remEls.push(child);
      }
    }

    for (const newChild of newEls.values()) {
      head.appendChild(newChild);
    }

    for (const remChild of remEls) {
      head.removeChild(remChild);
    }
  }

  function navigate(ctx, args) {
    const el = ctx.$el;

    if (!(el instanceof HTMLAnchorElement)) {
      const actual = element.constructor ? element.constructor.name : "Unknown";

      throw new Error(`Illegal arg: navigate must be executed on an HTMLAnchorElement but got ${actual}`);
    }

    const href = el.href;

    if (!href) {
      throw new Error(`Illegal arg: anchor has no href property`);
    }

    const opts = checkDefined(args.shift(), "opts");

    globalThis.fetch(href).then(resp => navigateFetchSuccess(resp, opts));
  }

  function navigateFetchSuccess(resp, opts) {
    const headers = resp.headers;

    const contentType = headers.get("Content-Type");

    if (!contentType) {
      throw new Error("Invalid response: no content-type");
    }

    if (!contentType.startsWith("text/html")) {
      throw new Error(`Invalid response: unsupported content-type ${contentType}`);
    }

    const global = globalThis;

    const scroll = opts.scroll !== undefined ? opts.scroll : "auto";

    if (scroll === "auto") {
      // scroll to 0,0 
      if (!(global instanceof Window)) {
        const actual = global.constructor ? global.constructor.name : "Unknown";

        throw new Error(`Illegal arg: navigate must be executed on a Window but got ${actual}`);
      }

      global.scrollTo({
        top: 0, left: 0, behavior: "instant"
      });
    }

    // update browser location
    const history = global.history;

    if (history) {
      const state = { way: true };

      const unused = "";

      const url = resp.url;

      history.pushState(state, unused, url);
    }

    resp.text().then(navigateFetchSuccessHtml);
  }

  function navigateFetchSuccessHtml(text) {
    const global = globalThis;

    const doc = global.document;

    if (!doc) {
      throw new Error("Illegal state: global scope has no document");
    }

    const element = doc.documentElement;

    morph0(element, text);
  }

  function noop() {}

  function propertyRead(ctx, args) {
    const recv = checkRecv(ctx, args.shift());

    const propName = checkString(args.shift(), "propName");

    return propertyRead0(recv, propName);
  }

  function propertyReadUnchecked(ctx, args) {
    const recv = ctx.$recv;

    const propName = checkString(args.shift(), "propName");

    return propertyRead0(recv, propName);
  }

  function propertyRead0(recv, propName) {
    const prop = recv[propName];

    if (!prop) {
      throw new Error(`Illegal arg: ${recv} does not declare the ${propName} property`);
    }

    return prop;
  }

  function propertyWrite(ctx, args) {
    const recv = checkRecv(ctx, args.shift());

    const propName = checkString(args.shift(), "propName");

    const val = checkDefined(args.shift(), "value");

    recv[propName] = execute(ctx, val);
  }

  function throwError(ctx, args) {
    const $msg = checkDefined(args.shift(), "msg");

    const msg = execute(ctx, $msg);

    checkString(msg, "msg");

    throw new Error(msg);
  }

  function typeEnsure(ctx, args) {
    const typeName = checkString(args.shift(), "typeName");

    return checkType(ctx.$recv, "recv", typeName);
  }

  function wayOne(ctx, action) {
    checkArray(action, "action");

    const old = ctx.$recv;

    for (const op of action) {
      ctx.$recv = execute(ctx, op);
    }

    const res = ctx.$recv;

    ctx.$recv = old;

    return res;
  }

  function waySeq(ctx, actions) {
    checkArray(actions, "actions");

    const old = ctx.$recv;

    for (const action of actions) {
      execute(ctx, action);
    }

    ctx.$recv = old;
  }

  // ##################################################################
  // # END: Objectos Way Actions
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

  function checkBoolean(maybe, name) {
    const t = typeof maybe;

    if (t !== "boolean") {
      throw new Error(`Illegal arg: ${name} must be a Boolean value but got ${t}`);
    }

    return maybe;
  }

  function checkDefined(maybe, name) {
    if (maybe === undefined) {
      throw new Error(`Illegal arg: ${name} must be a defined value`);
    }

    return maybe;
  }

  function checkInteger(maybe, name) {
    if (!Number.isInteger(maybe)) {
      throw new Error(`Illegal arg: ${name} must be an integer value but got ${maybe}`);
    }

    return maybe;
  }

  function checkRecv(ctx, tn) {
    const typeName = checkString(tn, "typeName");

    const recv = ctx.$recv;

    checkType(recv, "recv", typeName);

    return recv;
  }

  function checkType(maybe, name, typeName) {
    if (typeName === "boolean") {
      return checkBoolean(maybe, name);
    } else if (typeName === "string") {
      return checkString(maybe, name);
    } else {
      const t = typeof maybe;

      if (t !== "object") {
        throw new Error(`Illegal arg: ${name} must be an Object value but got ${t}`);
      }

      let prototype = Object.getPrototypeOf(maybe);

      while (prototype) {
        const constructor = prototype.constructor;

        if (!constructor) {
          throw new Error(`Illegal arg: expected ${name} to have a constructor, but found ${constructor}`);
        }

        const actualName = constructor.name;

        if (actualName === typeName) {
          return maybe;
        }

        prototype = Object.getPrototypeOf(prototype);
      }

      throw new Error(`Illegal arg: ${name} does not have ${typeName} in its prototype chain`);
    }
  }

  function checkString(maybe, name) {
    const t = typeof maybe;

    if (t !== "string") {
      throw new Error(`Illegal arg: ${name} must be a String value but got ${t}`);
    }

    return maybe;
  }

  function documentElement() {
    const global = globalThis;

    const doc = global.document;

    if (!doc) {
      throw new Error("Illegal state: global scope has no document");
    }

    return doc.documentElement;
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
    document.addEventListener("click", listener(clickListener));
    document.addEventListener("submit", listener(submitListener));
  }

  window.addEventListener("DOMContentLoaded", domLoaded);

  // ##################################################################
  // # END: Objectos Way Bootstrap
  // ##################################################################

  return way;

})();