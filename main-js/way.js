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
const way = (function() {

  "use strict";

  const way = {

    on: on

  };

  // ##################################################################
  // # BEGIN: Actions support
  // ##################################################################

  function on(event, action) {
    const ctx = {
      $document: undefined,

      $el: event.target,

      $recv: undefined,

      document: function() {
        if (!this.$document) {
          const global = globalThis;

          if (!global.document) {
            throw new Error("Illegal state: global scope has no document");
          }

          this.$document = global.document;
        }

        return this.$document;
      }
    };

    execute(ctx, action);

    event.preventDefault();
  }

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
    "cr": contextReadUnchecked,
    "CW": contextWrite,
    "EI": elementById,
    "ET": elementTarget,
    "FE": forEach,
    "FO": follow,
    "FN": functionJs,
    "GR": globalRead,
    "IF": ifElse,
    "IU": invokeUnchecked,
    "IV": invokeVirtual,
    "JS": jsValue,
    "NO": noop,
    "PR": propertyRead,
    "pr": propertyReadUnchecked,
    "PW": propertyWrite,
    "SU": submit,
    "TE": throwError,
    "TY": typeEnsure,
    "UB": updateBody,
    "UH": updateHead,
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

  function contextReadUnchecked(ctx, args) {
    const name = checkString(args.shift(), "name");

    if (name.length === 0) {
      throw new Error("Illegal arg: context variable name must not be empty");
    }

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

  function follow(ctx, args) {
    const el = ctx.$el;

    // validate element

    if (!(el instanceof HTMLAnchorElement)) {
      const actual = el.constructor ? el.constructor.name : "Unknown";

      throw new Error(`Illegal state: follow must be executed on an HTMLAnchorElement but got ${actual}`);
    }

    const href = el.href;

    if (!href) {
      throw new Error(`Illegal state: anchor has no href property`);
    }

    return navigate(ctx, args, href);
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

  async function navigate(ctx, args, url, reqOpts) {
    // TODO validate url
    // - it must be internal

    const opts = navigateOpts(args);

    const actions = navigateActions(opts);

    const req = new Request(url, reqOpts);

    req.headers.set("Way-Request", "true");

    const resp = await globalThis.fetch(req);

    const headers = resp.headers;

    const contentType = headers.get("Content-Type");

    if (!contentType) {
      throw new Error("Invalid response: no content-type");
    }

    if (!contentType.startsWith("text/html")) {
      throw new Error(`Invalid response: unsupported content-type ${contentType}`);
    }

    ctx.$respUrl = resp.url;

    const html = await resp.text();

    const parser = new DOMParser();

    ctx.$htmlDoc = parser.parseFromString(html, "text/html");

    return execute(ctx, actions);
  }

  const $navigateOpts = {
    // scroll to element
    "SE": function(opts, args) {
      opts.scrollElem = args;
    }
  };

  function navigateOpts(args) {
    // initialize with defaults
    const opts = {
      updateHead: true,
      updateBody: true,
      updateElems: undefined,

      scrollElem: undefined,

      pushUrl: true
    };

    for (const arg of args) {
      const opt = checkArray(arg, "opt");

      const id = checkString(opt.shift(), "id");

      const handler = $navigateOpts[id];

      if (handler) {
        handler(opts, arg);
      } else {
        throw new Error(`Illegal arg: no handler found for option=${id}`);
      }
    }

    return opts;
  }

  function navigateActions(opts) {
    const actions = ["WS"];

    if (opts.updateHead) {
      actions.push(["UH"]);
    }

    if (opts.updateBody) {
      actions.push(["UB"]);
    }

    if (opts.updateElems) {
      actions.push(["UE", opts.updateElems]);
    }

    const scroll = ["W1"];

    if (opts.scrollElem) {
      scroll.push(...opts.scrollElem);
    } else {
      scroll.push(["GR"]);
      scroll.push(["PR", "Window", "document"]);
      scroll.push(["PR", "Document", "documentElement"]);
    }

    scroll.push(["IV", "Element", "scrollIntoView", []])

    actions.push(scroll);

    if (opts.pushUrl) {
      const action = ["W1"];

      action.push(["GR"]);
      action.push(["PR", "Window", "history"]);

      const args = [];
      args.push(["JS", { way: true }])
      args.push(["JS", ""]);
      args.push(["cr", "$respUrl"]);
      action.push(["IV", "History", "pushState", args]);

      actions.push(action);
    }

    return actions;
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

  function submit(ctx, args) {
    const el = ctx.$el;

    if (!(el instanceof HTMLFormElement)) {
      const actual = element.constructor ? element.constructor.name : "Unknown";

      throw new Error(`Illegal state: submit must be executed on an HTMLFormElement but got ${actual}`);
    }

    const action = el.action;

    if (!action) {
      throw new Error(`Illegal state: form has no action property`);
    }

    const method = el.method;

    if (!method) {
      throw new Error(`Illegal state: form has no method property`);
    }

    let url = action;

    const req = {
      method: method.toUpperCase()
    };

    const formData = new FormData(el);

    if (req.method === "GET") {
      url = new URL(action);

      for (const [key, value] of formData.entries()) {
        url.searchParams.set(key, value);
      }
    }

    else {
      const enctype = el.getAttribute("enctype");

      if (enctype === "multipart/form-data") {
        throw new Error(`Implement me :: enctype=${enctype}`);
      }

      else {
        req.body = new URLSearchParams(formData);

        req.headers = {
          "Content-Type": "application/x-www-form-urlencoded"
        };
      }
    }

    return navigate(ctx, args, url, req);
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

  function updateBody(ctx) {
    const doc = ctx.document();

    const newDoc = checkDefined(ctx.$htmlDoc, "newDoc");

    doc.body = newDoc.body;
  }

  function updateHead(ctx) {
    const doc = ctx.document();

    const head = doc.head;

    if (!head) {
      return;
    }

    const newDoc = checkDefined(ctx.$htmlDoc, "newDoc");

    const newHead = newDoc.head;

    if (!newHead) {
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

  // ##################################################################
  // # END: private private
  // ##################################################################

  return way;

})();