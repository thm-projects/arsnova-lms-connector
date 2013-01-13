define(
		[ 'dojo/_base/declare',
		  'dojo/request',
		  'dojo/query',
		  'dojo/dom',
		  'dojo/dom-construct', 
		  'dojo/on',
		  'dojox/highlight',
		  'dojox/highlight/languages/xml'
		],
		function(declare, request, query, dom, domConstruct, on, highlight) {
	return declare(null, {
		requestHandle: null,
		
		constructor: function() {
			on(dom.byId('membership'), 'click', this.onMembership);
			on(dom.byId('courses'), 'click', this.onCourses);
		},
		
		onMembership: function() {
			if (this.requestHandle != null) {
				this.requestHandle.remove();
			}
			
			var form = dom.byId('form');
			domConstruct.empty(form);
			domConstruct.place(
				'<h3>Request Membership</h3>'
				+ 'Request information for URI '
				+ '<code>/'
				+ '<input id="username" name="username" placeholder="username" />'
				+ '/membership/'
				+ '<input id="courseid" name="courseid" placeholder="courseid" />'
				+ '</code>',
				form
			);
			
			var button = domConstruct.create(
				'input',
				{
					type: 'button',
					value: 'send request'
				}
			);
			domConstruct.place(button, form);
			this.requestHandle = on(button, 'click', function() {
				var username = dom.byId('username').value;
				var courseid = dom.byId('courseid').value;
				domConstruct.empty('result');
				request.get(
					username + '/membership/' + courseid,
					{
						handleAs: 'text'
					}
				).then(
					function(result) {
						var code = highlight.processString(result).result;
						domConstruct.place(
							'<h3>Result</h3>'
							+ '<code class="xml">'
							+ code
							+ '</code>',
							'result'
						);
						query('code').forEach(highlight.init);
					},
					function(status) {
						domConstruct.place(
							'<h3>Result</h3>'
							+ '<pre class="error">'
							+ status 
							+ '</pre>',
							'result'
						);
					}
				);
			});
		},
		
		onCourses: function() {
			if (this.requestHandle != null) {
				this.requestHandle.remove();
			}
			
			var form = dom.byId('form');
			domConstruct.empty(form);
			domConstruct.place(
				'<h3>Request Courses</h3>'
				+ 'Request information for URI '
				+ '<code>/'
				+ '<input id="username" name="username" placeholder="username" />'
				+ '/courses'
				+ '</code>',
				form
			);
			var button = domConstruct.create(
				'input',
				{
					type: 'button',
					value: 'send request'
				}
			);
			domConstruct.place(button, form);
			this.requestHandle = on(button, 'click', function() {
				var username = dom.byId('username').value;
				domConstruct.empty('result');
				request.get(
					username + '/courses',
					{
						handleAs: 'text'
					}
				).then(
					function(result) {
						var code = highlight.processString(result).result;
						domConstruct.place(
							'<h3>Result</h3>'
							+ '<code class="xml">'
							+ code
							+ '</code>',
							'result'
						);
						query('code').forEach(highlight.init);
					},
					function(status) {
						domConstruct.place(
							'<h3>Result</h3>'
							+ '<pre class="error">'
							+ status 
							+ '</pre>',
							'result'
						);
					}
				);
			});
		}
	});
});