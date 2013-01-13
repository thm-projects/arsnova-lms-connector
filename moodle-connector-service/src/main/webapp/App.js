define(
		[ 'dojo/_base/declare', 'dojo/request', 'dojo/dom', 'dojo/dom-construct', 'dojo/on' ],
		function(declare, request, dom, domConstruct, on) {
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
						domConstruct.place(
							'<h3>Result</h3>'
							+ '<textarea readonly="readonly">'
							+ result
							+ '</textarea>',
							'result'
						);
					},
					function(error) {
						domConstruct.place(
							'<h3>Result</h3>'
							+ '<pre>ERROR</pre>',
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
						domConstruct.place(
							'<h3>Result</h3>'
							+ '<textarea readonly="readonly">'
							+ result
							+ '</textarea>',
							'result'
						);
					},
					function(error) {
						domConstruct.place(
							'<h3>Result</h3>'
							+ '<pre>ERROR</pre>',
							'result'
						);
					}
				);
			});
		}
	});
});