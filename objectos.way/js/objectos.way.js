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
		
		const click = dataset.wayClick;
		
		if (!click) {
			return;
		}
		
		const arr = JSON.parse(click);
		
		if (!Array.isArray(arr)) {
			return;
		}
		
		for (const obj of arr) {
			const cmd = obj.cmd;
			const args = obj.args;
			
			if (!cmd || !args) {
				continue;
			}
			
			switch (cmd) {
				case "replace-class":
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
		}
	}
	
	function domLoaded() {
		const body = document.body;
		
		body.addEventListener("click", clickListener);
	} 
	
	window.addEventListener("DOMContentLoaded", domLoaded);
	
})();