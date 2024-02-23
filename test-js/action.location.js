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
		<div data-frame='root' data-frame-value='x'>
    	<div data-frame='hd' data-frame-value='foo'>Foo</div>
		<span>should not remain</span>
    	<form data-frame='x' data-frame-value='1' method='post' action='/test'>
    	<button id='b' type='submit'>Before</button>
    	</form>
    	</div>`);
		const page2 = makeElement(`
		<div data-frame='root' data-frame-value='y'>
    	<div data-frame='hd' data-frame-value='foo'>Bar</div>
    	<div data-frame='x' data-frame-value='1'>From form to div</div>
    	</div>`);

		this.server.respondWith("POST", "/test", [302, { "Location": "/home" }, ""]);
		this.server.respondWith("GET", "/home", [200, { "Content-Type": "text/html" }, page2.outerHTML]);

		const btn = byId("b");

		btn.click();

		this.server.respond();

		this.server.respond();

		assert.equal(workArea().innerHTML, page2.outerHTML);
	});

});