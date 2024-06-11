suite("Action::location test", function() {

	setup(function() {
		clearWorkArea();
		this.server = makeServer();
	});

	teardown(function() {
		this.server.restore();
	});

	test("should respond to 302 FOUND", function() {
		make(`
		<div data-frame='root:x'>
    	<div data-frame='hd:foo'>Foo</div>
		<span>should not remain</span>
    	<form data-frame='x:1' method='post' action='/test'>
    	<button id='b' type='submit'>Before</button>
    	</form>
    	</div>`);
		const page2 = makeElement(`
		<div data-frame='root:y'>
    	<div data-frame='hd:foo'>Bar</div>
    	<div data-frame='x:1'>From form to div</div>
    	</div>`);

		this.server.respondWith("POST", "/test", [302, { "Location": "/home" }, ""]);
		this.server.respondWith("GET", "/home", [200, { "Content-Type": "text/html" }, page2.outerHTML]);

		const btn = byId("b");

		btn.click();

		this.server.respond();

		this.server.respond();

		assert.equal(workArea().innerHTML, page2.outerHTML);
	});

	test("it should work on data-on-click", function() {
		const clickId = "f";
		const onClick = JSON.stringify([
			{ cmd: "replace-class", args: ["a", "a", "x"] },
			{ cmd: "location", value: "/foo" },
			{ cmd: "replace-class", args: ["b", "b", "y"] }
		]);
		
		make(`
		<div>
		<div id='a' class='a'></div>
		<div id='b' class='b'></div>
		<div data-frame='x:foo'><a id='${clickId}' data-on-click='${onClick}'>Foo</a></div>
		</div>
		`);
		
		const page2 = makeElement(`
		<html>
		<div data-frame='x:bar'>Bar</div>
		</html>
		`);
		
		this.server.respondWith("GET", "/foo", [200, { "Content-Type": "text/html" }, page2.outerHTML]);
		
		const expected = makeElement(`
		<div>
		<div id='a' class='x'></div>
		<div id='b' class='y'></div>
		<div data-frame='x:bar'>Bar</div>
		</div>
		`);

		const anchor = byId(clickId);
		
		anchor.click();

		this.server.respond();

		assert.equal(workArea().innerHTML, expected.outerHTML);
	});

});