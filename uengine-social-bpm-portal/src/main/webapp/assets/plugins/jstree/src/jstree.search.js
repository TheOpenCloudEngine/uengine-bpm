/**
 * ### Search plugin
 *
 * Adds search functionality to jsTree.
 */
/*globals jQuery, define, exports, require, document */
(function (factory) {
	"use strict";
	if (typeof define === 'function' && define.amd) {
		define('jstree.search', ['jquery','jstree'], factory);
	}
	else if(typeof exports === 'object') {
		factory(require('jquery'), require('jstree'));
	}
	else {
		factory(jQuery, jQuery.jstree);
	}
}(function ($, jstree, undefined) {
	"use strict";

	if($.jstree.plugins.search) { return; }

	/**
	 * stores all defaults for the search plugin
	 * @name $.jstree.defaults.search
	 * @plugin search
	 */
	$.jstree.defaults.search = {
		/**
		 * a jQuery-like AJAX config, which jstree uses if a server should be queried for results. 
		 * 
		 * A `str` (which is the search string) parameter will be added with the request. The expected result is a JSON array with nodes that need to be opened so that matching nodes will be revealed.
		 * Leave this setting as `false` to not query the server.
		 * @name $.jstree.defaults.search.ajax
		 * @plugin search
		 */
		ajax : false,
		/**
		 * Indicates if the search should be fuzzy or not (should `chnd3` match `child node 3`). Default is `true`.
		 * @name $.jstree.defaults.search.fuzzy
		 * @plugin search
		 */
		fuzzy : true,
		/**
		 * Indicates if the search should be case sensitive. Default is `false`.
		 * @name $.jstree.defaults.search.case_sensitive
		 * @plugin search
		 */
		case_sensitive : false,
		/**
		 * Indicates if the tree should be filtered to show only matching nodes (keep in mind this can be a heavy on large trees in old browsers). Default is `false`.
		 * @name $.jstree.defaults.search.show_only_matches
		 * @plugin search
		 */
		show_only_matches : false,
		/**
		 * Indicates if all nodes opened to reveal the search result, should be closed when the search is cleared or a new search is performed. Default is `true`.
		 * @name $.jstree.defaults.search.close_opened_onclear
		 * @plugin search
		 */
		close_opened_onclear : true
	};

	$.jstree.plugins.search = function (options, parent) {
		this.bind = function () {
			parent.bind.call(this);

			this._data.search.str = "";
			this._data.search.dom = $();
			this._data.search.res = [];
			this._data.search.opn = [];
			this._data.search.sln = null;

			if(this.settings.search.show_only_matches) {
				this.element
					.on("search.jstree", function (e, data) {
						if(data.nodes.length) {
							$(this).find("li").hide().filter('.jstree-last').filter(function() { return this.nextSibling; }).removeClass('jstree-last');
							data.nodes.parentsUntil(".jstree").addBack().show()
								.filter("ul").each(function () { $(this).children("li:visible").eq(-1).addClass("jstree-last"); });
						}
					})
					.on("clear_search.jstree", function (e, data) {
						if(data.nodes.length) {
							$(this).find("li").css("display","").filter('.jstree-last').filter(function() { return this.nextSibling; }).removeClass('jstree-last');
						}
					});
			}
		};
		/**
		 * used to search the tree nodes for a given string
		 * @name search(str [, skip_async])
		 * @param {String} str the search string
		 * @param {Boolean} skip_async if set to true server will not be queried even if configured
		 * @plugin search
		 * @trigger search.jstree
		 */
		this.search = function (str, skip_async) {
			if(str === false || $.trim(str) === "") {
				return this.clear_search();
			}
			var s = this.settings.search,
				a = s.ajax ? $.extend({}, s.ajax) : false,
				f = null,
				r = [],
				p = [], i, j;
			if(this._data.search.res.length) {
				this.clear_search();
			}
			if(!skip_async && a !== false) {
				if(!a.data) { a.data = {}; }
				a.data.str = str;
				return $.ajax(a)
					.fail($.proxy(function () {
						this._data.core.last_error = { 'error' : 'ajax', 'plugin' : 'search', 'id' : 'search_01', 'reason' : 'Could not load search parents', 'data' : JSON.stringify(a) };
						this.settings.core.error.call(this, this._data.core.last_error);
					}, this))
					.done($.proxy(function (d) {
						if(d && d.d) { d = d.d; }
						this._data.search.sln = !$.isArray(d) ? [] : d;
						this._search_load(str);
					}, this));
			}
			this._data.search.str = str;
			this._data.search.dom = $();
			this._data.search.res = [];
			this._data.search.opn = [];

			f = new $.vakata.search(str, true, { caseSensitive : s.case_sensitive, fuzzy : s.fuzzy });

			$.each(this._model.data, function (i, v) {
				if(v.text && f.search(v.text).isMatch) {
					r.push(i);
					p = p.concat(v.parents);
				}
			});
			if(r.length) {
				p = $.vakata.array_unique(p);
				this._search_open(p);
				for(i = 0, j = r.length; i < j; i++) {
					f = this.get_node(r[i], true);
					if(f) {
						this._data.search.dom = this._data.search.dom.add(f);
					}
				}
				this._data.search.res = r;
				this._data.search.dom.children(".jstree-anchor").addClass('jstree-search');
			}
			/**
			 * triggered after search is complete
			 * @event
			 * @name search.jstree
			 * @param {jQuery} nodes a jQuery collection of matching nodes
			 * @param {String} str the search string
			 * @param {Array} res a collection of objects represeing the matching nodes
			 * @plugin search
			 */
			this.trigger('search', { nodes : this._data.search.dom, str : str, res : this._data.search.res });
		};
		/**
		 * used to clear the last search (removes classes and shows all nodes if filtering is on)
		 * @name clear_search()
		 * @plugin search
		 * @trigger clear_search.jstree
		 */
		this.clear_search = function () {
			this._data.search.dom.children(".jstree-anchor").removeClass("jstree-search");
			if(this.settings.search.close_opened_onclear) {
				this.close_node(this._data.search.opn, 0);
			}
			/**
			 * triggered after search is complete
			 * @event
			 * @name clear_search.jstree
			 * @param {jQuery} nodes a jQuery collection of matching nodes (the result from the last search)
			 * @param {String} str the search string (the last search string)
			 * @param {Array} res a collection of objects represeing the matching nodes (the result from the last search)
			 * @plugin search
			 */
			this.trigger('clear_search', { 'nodes' : this._data.search.dom, str : this._data.search.str, res : this._data.search.res });
			this._data.search.str = "";
			this._data.search.res = [];
			this._data.search.opn = [];
			this._data.search.dom = $();
		};
		/**
		 * opens nodes that need to be opened to reveal the search results. Used only internally.
		 * @private
		 * @name _search_open(d)
		 * @param {Array} d an array of node IDs
		 * @plugin search
		 */
		this._search_open = function (d) {
			var t = this;
			$.each(d.concat([]), function (i, v) {
				v = document.getElementById(v);
				if(v) {
					if(t.is_closed(v)) {
						t._data.search.opn.push(v.id);
						t.open_node(v, function () { t._search_open(d); }, 0);
					}
				}
			});
		};
		/**
		 * loads nodes that need to be opened to reveal the search results. Used only internally.
		 * @private
		 * @name _search_load(d, str)
		 * @param {String} str the search string
		 * @plugin search
		 */
		this._search_load = function (str) {
			var res = true,
				t = this,
				m = t._model.data;
			if($.isArray(this._data.search.sln)) {
				if(!this._data.search.sln.length) {
					this._data.search.sln = null;
					this.search(str, true);
				}
				else {
					$.each(this._data.search.sln, function (i, v) {
						if(m[v]) {
							$.vakata.array_remove_item(t._data.search.sln, v);
							if(!m[v].state.loaded) {
								t.load_node(v, function (o, s) { if(s) { t._search_load(str); } });
								res = false;
							}
						}
					});
					if(res) {
						this._data.search.sln = [];
						this._search_load(str);
					}
				}
			}
		};
	};

	// helpers
	(function ($) {
		// from http://kiro.me/projects/fuse.html
		$.vakata.search = function(pattern, txt, options) {
			options = options || {};
			if(options.fuzzy !== false) {
				options.fuzzy = true;
			}
			pattern = options.caseSensitive ? pattern : pattern.toLowerCase();
			var MATCH_LOCATION	= options.location || 0,
				MATCH_DISTANCE	= options.distance || 100,
				MATCH_THRESHOLD	= options.threshold || 0.6,
				patternLen = pattern.length,
				matchmask, pattern_alphabet, match_bitapScore, search;
			if(patternLen > 32) {
				options.fuzzy = false;
			}
			if(options.fuzzy) {
				matchmask = 1 << (patternLen - 1);
				pattern_alphabet = (function () {
					var mask = {},
						i = 0;
					for (i = 0; i < patternLen; i++) {
						mask[pattern.charAt(i)] = 0;
					}
					for (i = 0; i < patternLen; i++) {
						mask[pattern.charAt(i)] |= 1 << (patternLen - i - 1);
					}
					return mask;
				}());
				match_bitapScore = function (e, x) {
					var accuracy = e / patternLen,
						proximity = Math.abs(MATCH_LOCATION - x);
					if(!MATCH_DISTANCE) {
						return proximity ? 1.0 : accuracy;
					}
					return accuracy + (proximity / MATCH_DISTANCE);
				};
			}
			search = function (text) {
				text = options.caseSensitive ? text : text.toLowerCase();
				if(pattern === text || text.indexOf(pattern) !== -1) {
					return {
						isMatch: true,
						score: 0
					};
				}
				if(!options.fuzzy) {
					return {
						isMatch: false,
						score: 1
					};
				}
				var i, j,
					textLen = text.length,
					scoreThreshold = MATCH_THRESHOLD,
					bestLoc = text.indexOf(pattern, MATCH_LOCATION),
					binMin, binMid,
					binMax = patternLen + textLen,
					lastRd, start, finish, rd, charMatch,
					score = 1,
					locations = [];
				if (bestLoc !== -1) {
					scoreThreshold = Math.min(match_bitapScore(0, bestLoc), scoreThreshold);
					bestLoc = text.lastIndexOf(pattern, MATCH_LOCATION + patternLen);
					if (bestLoc !== -1) {
						scoreThreshold = Math.min(match_bitapScore(0, bestLoc), scoreThreshold);
					}
				}
				bestLoc = -1;
				for (i = 0; i < patternLen; i++) {
					binMin = 0;
					binMid = binMax;
					while (binMin < binMid) {
						if (match_bitapScore(i, MATCH_LOCATION + binMid) <= scoreThreshold) {
							binMin = binMid;
						} else {
							binMax = binMid;
						}
						binMid = Math.floor((binMax - binMin) / 2 + binMin);
					}
					binMax = binMid;
					start = Math.max(1, MATCH_LOCATION - binMid + 1);
					finish = Math.min(MATCH_LOCATION + binMid, textLen) + patternLen;
					rd = new Array(finish + 2);
					rd[finish + 1] = (1 << i) - 1;
					for (j = finish; j >= start; j--) {
						charMatch = pattern_alphabet[text.charAt(j - 1)];
						if (i === 0) {
							rd[j] = ((rd[j + 1] << 1) | 1) & charMatch;
						} else {
							rd[j] = ((rd[j + 1] << 1) | 1) & charMatch | (((lastRd[j + 1] | lastRd[j]) << 1) | 1) | lastRd[j + 1];
						}
						if (rd[j] & matchmask) {
							score = match_bitapScore(i, j - 1);
							if (score <= scoreThreshold) {
								scoreThreshold = score;
								bestLoc = j - 1;
								locations.push(bestLoc);
								if (bestLoc > MATCH_LOCATION) {
									start = Math.max(1, 2 * MATCH_LOCATION - bestLoc);
								} else {
									break;
								}
							}
						}
					}
					if (match_bitapScore(i + 1, MATCH_LOCATION) > scoreThreshold) {
						break;
					}
					lastRd = rd;
				}
				return {
					isMatch: bestLoc >= 0,
					score: score
				};
			};
			return txt === true ? { 'search' : search } : search(txt);
		};
	}(jQuery));

	// include the search plugin by default
	// $.jstree.defaults.plugins.push("search");
}));