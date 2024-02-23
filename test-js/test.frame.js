suite("Frame test", function() {

	setup(function() {
		clearWorkArea();
		this.server = makeServer();
	});

	teardown(function() {
		this.server.restore();
	});

	test("single data-frame: same name different value", function() {
		make("<form data-frame='x' data-frame-value='1' method='post' action='/test'><button id='b' type='submit'>Before</button></form>");
		const frame2 = makeElement("<form data-frame='x' data-frame-value='2' method='post' action='/test'><button type='submit'>After</button></form>");

		this.server.respondWith(
			"POST",
			"/test", [
			200,
			{ "Content-Type": "application/json" },
			JSON.stringify([
				{
					cmd: "html",
					value: frame2.outerHTML
				}
			])
		]);

		const btn = byId("b");

		btn.click();

		this.server.respond();

		assert.equal(workArea().innerHTML, frame2.outerHTML);
	});

	test("single data-frame: same name same value", function() {
		const frame1 = make("<form data-frame='x' data-frame-value='1' method='post' action='/test'><button id='b' type='submit'>Before</button></form>");
		const frame2 = makeElement("<form data-frame='x' data-frame-value='1' method='post' action='/test'><button type='submit'>Ignored</button></form>");

		this.server.respondWith(
			"POST",
			"/test", [
			200,
			{ "Content-Type": "application/json" },
			JSON.stringify([
				{
					cmd: "html",
					value: frame2.outerHTML
				}
			])
		]);

		const btn = byId("b");

		btn.click();

		this.server.respond();

		assert.equal(workArea().innerHTML, frame1.outerHTML);
	});

	test("nested data-frames (1 child): morph inner only", function() {
		make("<div data-frame='root' data-frame-value='x'>A<form data-frame='x' data-frame-value='1' method='post' action='/test'><button id='b' type='submit'>Before</button></form></div>");
		const frame2 = makeElement("<div data-frame='root' data-frame-value='x'>B<form data-frame='x' data-frame-value='2' method='post' action='/test'><button type='submit'>After</button></form></div>");
		const expect = makeElement("<div data-frame='root' data-frame-value='x'>A<form data-frame='x' data-frame-value='2' method='post' action='/test'><button type='submit'>After</button></form></div>");

		this.server.respondWith(
			"POST",
			"/test", [
			200,
			{ "Content-Type": "application/json" },
			JSON.stringify([
				{
					cmd: "html",
					value: frame2.outerHTML
				}
			])
		]);

		const btn = byId("b");

		btn.click();

		this.server.respond();

		assert.equal(workArea().innerHTML, expect.outerHTML);
	});

	test("nested data-frames (1 child): morph outer only", function() {
		make("<div data-frame='root' data-frame-value='x'>A<form data-frame='x' data-frame-value='1' method='post' action='/test'><button id='b' type='submit'>Before</button></form></div>");
		const frame2 = makeElement("<div data-frame='root' data-frame-value='y'>B</div>");

		this.server.respondWith(
			"POST",
			"/test", [
			200,
			{ "Content-Type": "application/json" },
			JSON.stringify([
				{
					cmd: "html",
					value: frame2.outerHTML
				}
			])
		]);

		const btn = byId("b");

		btn.click();

		this.server.respond();

		assert.equal(workArea().innerHTML, frame2.outerHTML);
	});

	test("nested data-frames (2 children): morph one child only", function() {
		make(`
		<div data-frame='root' data-frame-value='x'>
    	<div data-frame='hd' data-frame-value='foo'>Foo</div>
		<span>should remain</span>
    	<form data-frame='x' data-frame-value='1' method='post' action='/test'>
    	<button id='b' type='submit'>Before</button>
    	</form>
    	</div>`);
		const page2 = makeElement(`
		<div data-frame='root' data-frame-value='x'>
    	<div data-frame='hd' data-frame-value='bar'>Bar</div>
    	<form data-frame='x' data-frame-value='1' method='post' action='/test'>
    	<button id='b' type='submit'>After</button>
    	</form>
    	</div>`);
		const expect = makeElement(`
		<div data-frame='root' data-frame-value='x'>
    	<div data-frame='hd' data-frame-value='bar'>Bar</div>
		<span>should remain</span>
    	<form data-frame='x' data-frame-value='1' method='post' action='/test'>
    	<button id='b' type='submit'>Before</button>
    	</form>
    	</div>`);

		this.server.respondWith(
			"POST",
			"/test", [
			200,
			{ "Content-Type": "application/json" },
			JSON.stringify([
				{
					cmd: "html",
					value: page2.outerHTML
				}
			])
		]);

		const btn = byId("b");

		btn.click();

		this.server.respond();

		assert.equal(workArea().innerHTML, expect.outerHTML);
	});

	test("nested data-frames (2 children): morph both children", function() {
		make(`
		<div data-frame='root' data-frame-value='x'>
    	<div data-frame='hd' data-frame-value='foo'>Foo</div>
		<span>should remain</span>
    	<form data-frame='x' data-frame-value='1' method='post' action='/test'>
    	<button id='b' type='submit'>Before</button>
    	</form>
    	</div>`);
		const page2 = makeElement(`
		<div data-frame='root' data-frame-value='x'>
    	<div data-frame='hd' data-frame-value='bar'>Bar</div>
    	<div data-frame='x' data-frame-value='2'>From form to div</div>
    	</div>`);
		const expect = makeElement(`
		<div data-frame='root' data-frame-value='x'>
    	<div data-frame='hd' data-frame-value='bar'>Bar</div>
		<span>should remain</span>
    	<div data-frame='x' data-frame-value='2'>From form to div</div>
    	</div>`);

		this.server.respondWith(
			"POST",
			"/test", [
			200,
			{ "Content-Type": "application/json" },
			JSON.stringify([
				{
					cmd: "html",
					value: page2.outerHTML
				}
			])
		]);

		const btn = byId("b");

		btn.click();

		this.server.respond();

		assert.equal(workArea().innerHTML, expect.outerHTML);
	});

	test("nested data-frames (2 children): morph parent only", function() {
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

		this.server.respondWith(
			"POST",
			"/test", [
			200,
			{ "Content-Type": "application/json" },
			JSON.stringify([
				{
					cmd: "html",
					value: page2.outerHTML
				}
			])
		]);

		const btn = byId("b");

		btn.click();

		this.server.respond();

		assert.equal(workArea().innerHTML, page2.outerHTML);
	});

	test("accept text/html response", function() {
		make("<form data-frame='x' data-frame-value='1' method='post' action='/test'><button id='b' type='submit'>Before</button></form>");
		const frame2 = makeElement("<form data-frame='x' data-frame-value='2' method='post' action='/test'><button type='submit'>After</button></form>");

		this.server.respondWith(
			"POST",
			"/test", [
			200,
			{ "Content-Type": "text/html" },
			frame2.outerHTML
		]);

		const btn = byId("b");

		btn.click();

		this.server.respond();

		assert.equal(workArea().innerHTML, frame2.outerHTML);
	});

});