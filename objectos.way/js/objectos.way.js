(function() {
	
	"use strict";
	
	function clickListener(event) {
		const target = event.target;
		
		const dataset = target.dataset;
		
		const click = dataset.wayClick;
		
		if (!click) {
			return;
		}
		
		const obj = JSON.parse(click);
		
		for (const [key, value] of Object.entries(obj)) {
  			console.log(`${key} ${value}`); // "a 5", "b 7", "c 9"
		}
	}
	
	function domLoaded() {
		const body = document.body;
		
		body.addEventListener("click", clickListener);
	} 
	
	window.addEventListener("DOMContentLoaded", domLoaded);
	
})();