suite("Action::navigate test", function() {

	setup(function() {
		clearWorkArea();
		this.server = makeServer();
		this.errorStub = sinon.stub(console, 'error');
	});

	teardown(function() {
		this.errorStub.restore();
		this.server.restore();
	});

	test("it should perform a soft navigation", function() {
		make(`
		<div id='before' data-frame='x:before'>
		<a id='link' href='/after' data-on-click='[{"cmd":"navigate"}]'>Before</a>
		</div>`);

		const after = makeElement(`
		<html>
		<div id='after' data-frame='x:after'>After</div>
		</html>`);

		this.server.respondWith("GET", "/after", [200, { "Content-Type": "text/html" }, after.outerHTML]);

		const link = byId("link");

		link.click();

		this.server.respond();

		assert.equal(workArea().innerHTML, after.outerHTML);
	});

	test("it should fail if element is not an anchor", function() {
		make(`
		<div id='before' data-frame='x:before'>
		<button id='link' href='/after' data-on-click='[{"cmd":"navigate"}]'>Before</button>
		</div>`);

		const link = byId("link");

		link.click();

		assert.equal(this.errorStub.calledOnce, true);
		assert.equal(this.errorStub.calledWith("executeNavigate: expected HTMLAnchorElement but got %s", "HTMLButtonElement"), true);
	});

	test("it should fail if anchor has no 'href' attribute", function() {
		make(`
		<div id='before' data-frame='x:before'>
		<a id='link' data-on-click='[{"cmd":"navigate"}]'>Before</button>
		</div>`);
		
		const link = byId("link");

		link.click();

		assert.equal(this.errorStub.calledOnce, true);
		assert.equal(this.errorStub.calledWith("executeNavigate: anchor has no href attribute"), true);
	});

});