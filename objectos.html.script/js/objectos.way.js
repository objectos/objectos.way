/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
(function() {

    "use strict";

    function clickListener(event) {
        const target = event.target;

        const dataset = target.dataset;

        const data = dataset.wayClick;

        if (!data) {
            return;
        }

        const way = JSON.parse(data);

        if (!Array.isArray(way)) {
            return;
        }

        executeActions(way);
    }

    function submitListener(event) {
        const target = event.target;

        const dataset = target.dataset;

        const data = dataset.waySubmit;

        if (!data) {
            return;
        }

        const way = JSON.parse(data);

        if (!Array.isArray(way)) {
            return;
        }

        // verify we have all of the required properties
        const tagName = target.tagName;

        if (tagName !== "FORM") {
            return;
        }

        const action = target.getAttribute("action");

        if (!action) {
            return;
        }

        const method = target.getAttribute("method");

        if (!method) {
            return;
        }


        const body = new FormData(target);

        // this is a way form, we shouldn't submit it
        event.preventDefault();

        const xhr = new XMLHttpRequest();

        xhr.open(method.toUpperCase(), action, true);

        xhr.onload = (_) => {
            executeActions(way, xhr.response);
        }

        xhr.send(body);
    }

    function executeActions(way, resp) {
        for (const obj of way) {
            const cmd = obj.cmd;

            const args = obj.args;

            if (!cmd || !args) {
                continue;
            }

            switch (cmd) {
                case "replace-class": {
                    if (args.length !== 3) {
                        return;
                    }

                    const id = args[0];

                    const el = document.getElementById(id);

                    if (!el) {
                        return;
                    }

                    const classList = el.classList;

                    const classA = args[1];

                    const classB = args[2];

                    classList.replace(classA, classB);

                    break;
                }

                case "swap": {
                    if (args.length !== 2) {
                        return;
                    }

                    const id = args[0];

                    const el = document.getElementById(id);

                    if (!el) {
                        return;
                    }

                    const mode = args[1];

                    switch (mode) {
                        case "innerHTML": { el.innerHTML = resp; }

                        case "outerHTML": { el.outerHTML = resp; }
                    }

                    break;
                }
            }

        }
    }

    function domLoaded() {
        const body = document.body;

        body.addEventListener("click", clickListener);
        body.addEventListener("submit", submitListener);
    }

    window.addEventListener("DOMContentLoaded", domLoaded);

})();