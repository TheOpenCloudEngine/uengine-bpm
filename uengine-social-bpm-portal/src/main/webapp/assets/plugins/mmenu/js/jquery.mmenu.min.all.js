/*	
 * jQuery mmenu v4.2.2
 * @requires jQuery 1.7.0 or later
 *
 * mmenu.frebsite.nl
 *
 * Copyright (c) Fred Heusschen
 * www.frebsite.nl
 *
 * Dual licensed under the MIT and GPL licenses.
 * http://en.wikipedia.org/wiki/MIT_License
 * http://en.wikipedia.org/wiki/GNU_General_Public_License
 */
! function (e) {
    function t(t, n, s) {
        if (s) {
            if ("object" != typeof t && (t = {}), "boolean" != typeof t.isMenu) {
                var o = s.children();
                t.isMenu = 1 == o.length && o.is(n.panelNodetype)
            }
            return t
        }
        return t = e.extend(!0, {}, e[a].defaults, t), ("top" == t.position || "bottom" == t.position) && ("back" == t.zposition || "next" == t.zposition) && (e[a].deprecated('Using position "' + t.position + '" in combination with zposition "' + t.zposition + '"', 'zposition "front"'), t.zposition = "front"), t
    }

    function n(t) {
        return t = e.extend(!0, {}, e[a].configuration, t), "string" != typeof t.pageSelector && (t.pageSelector = "> " + t.pageNodetype), t
    }

    function s() {
        r.$wndw = e(window), r.$html = e("html"), r.$body = e("body"), r.$allMenus = e(), e.each([d, c, u], function (e, t) {
            t.add = function (e) {
                e = e.split(" ");
                for (var n in e) t[e[n]] = t.mm(e[n])
            }
        }), d.mm = function (e) {
            return "mm-" + e
        }, d.add("menu ismenu panel list subtitle selected label spacer current highest hidden page blocker modal background opened opening subopened subopen fullsubopen subclose"), d.umm = function (e) {
            return "mm-" == e.slice(0, 3) && (e = e.slice(3)), e
        }, c.mm = function (e) {
            return "mm-" + e
        }, c.add("parent style"), u.mm = function (e) {
            return e + ".mm"
        }, u.add("toggle open opening opened close closing closed update setPage setSelected transitionend webkitTransitionEnd mousedown touchstart mouseup touchend scroll touchmove click keydown keyup resize"), r.$wndw.on(u.keydown, function (e) {
            return r.$html.hasClass(d.opened) && 9 == e.keyCode ? (e.preventDefault(), !1) : void 0
        });
        var t = 0;
        r.$wndw.on(u.resize, function (e, n) {
            if (n || r.$html.hasClass(d.opened)) {
                var s = r.$wndw.height();
                (n || s != t) && (t = s, r.$page.css("minHeight", s))
            }
        }), e[a]._c = d, e[a]._d = c, e[a]._e = u, e[a].glbl = r
    }

    function o(t, n) {
        if (t.hasClass(d.current)) return !1;
        var s = e("." + d.panel, n),
            o = s.filter("." + d.current);
        return s.removeClass(d.highest).removeClass(d.current).not(t).not(o).addClass(d.hidden), t.hasClass(d.opened) ? o.addClass(d.highest).removeClass(d.opened).removeClass(d.subopened) : (t.addClass(d.highest), o.addClass(d.subopened)), t.removeClass(d.hidden).removeClass(d.subopened).addClass(d.current).addClass(d.opened), "open"
    }

    function i(e, t, n) {
        var s = !1,
            o = function () {
                s || t.call(e[0]), s = !0
            };
        e.one(u.transitionend, o), e.one(u.webkitTransitionEnd, o), setTimeout(o, 1.1 * n)
    }
    var a = "mmenu",
        l = "4.2.2";
    if (!e[a]) {
        var r = {
            $wndw: null,
            $html: null,
            $body: null,
            $page: null,
            $blck: null,
            $allMenus: null
        }, d = {}, c = {}, u = {}, p = 0,
            h = 0;
        e[a] = function (e, t, n) {
            return r.$allMenus = r.$allMenus.add(e), this.$menu = e, this.opts = t, this.conf = n, this.serialnr = p++, this._init(), this
        }, e[a].prototype = {
            open: function () {
                var e = this;
                return this._openSetup(), setTimeout(function () {
                    e._openFinish()
                }, 50), "open"
            },
            _openSetup: function () {
                h = r.$wndw.scrollTop(), this.$menu.addClass(d.current), r.$allMenus.not(this.$menu).trigger(u.close), r.$page.data(c.style, r.$page.attr("style") || ""), r.$wndw.trigger(u.resize, [!0]), this.opts.modal && r.$html.addClass(d.modal), this.opts.moveBackground && r.$html.addClass(d.background), "left" != this.opts.position && r.$html.addClass(d.mm(this.opts.position)), "back" != this.opts.zposition && r.$html.addClass(d.mm(this.opts.zposition)), this.opts.classes && r.$html.addClass(this.opts.classes), r.$html.addClass(d.opened), this.$menu.addClass(d.opened)
            },
            _openFinish: function () {
                var e = this;
                i(r.$page, function () {
                    e.$menu.trigger(u.opened)
                }, this.conf.transitionDuration), r.$html.addClass(d.opening), this.$menu.trigger(u.opening)
            },
            close: function () {
                var e = this;
                return i(r.$page, function () {
                    e.$menu.removeClass(d.current).removeClass(d.opened), r.$html.removeClass(d.opened).removeClass(d.modal).removeClass(d.background).removeClass(d.mm(e.opts.position)).removeClass(d.mm(e.opts.zposition)), e.opts.classes && r.$html.removeClass(e.opts.classes), r.$page.attr("style", r.$page.data(c.style)), e.$menu.trigger(u.closed)
                }, this.conf.transitionDuration), r.$html.removeClass(d.opening), this.$menu.trigger(u.closing), "close"
            },
            _init: function () {
                if (this.opts = t(this.opts, this.conf, this.$menu), this.direction = this.opts.slidingSubmenus ? "horizontal" : "vertical", this._initPage(r.$page), this._initMenu(), this._initBlocker(), this._initPanles(), this._initLinks(), this._initOpenClose(), this._bindCustomEvents(), e[a].addons)
                    for (var n = 0; n < e[a].addons.length; n++) "function" == typeof this["_addon_" + e[a].addons[n]] && this["_addon_" + e[a].addons[n]]()
            },
            _bindCustomEvents: function () {
                var t = this;
                this.$menu.off(u.open + " " + u.close + " " + u.setPage + " " + u.update).on(u.open + " " + u.close + " " + u.setPage + " " + u.update, function (e) {
                    e.stopPropagation()
                }), this.$menu.on(u.open, function (n) {
                    return e(this).hasClass(d.current) ? (n.stopImmediatePropagation(), !1) : t.open()
                }).on(u.close, function (n) {
                    return e(this).hasClass(d.current) ? t.close() : (n.stopImmediatePropagation(), !1)
                }).on(u.setPage, function (e, n) {
                    t._initPage(n), t._initOpenClose()
                });
                var n = this.$menu.find(this.opts.isMenu && "horizontal" != this.direction ? "ul, ol" : "." + d.panel);
                n.off(u.toggle + " " + u.open + " " + u.close).on(u.toggle + " " + u.open + " " + u.close, function (e) {
                    e.stopPropagation()
                }), "horizontal" == this.direction ? n.on(u.open, function () {
                    return o(e(this), t.$menu)
                }) : n.on(u.toggle, function () {
                    var t = e(this);
                    return t.triggerHandler(t.parent().hasClass(d.opened) ? u.close : u.open)
                }).on(u.open, function () {
                    return e(this).parent().addClass(d.opened), "open"
                }).on(u.close, function () {
                    return e(this).parent().removeClass(d.opened), "close"
                })
            },
            _initBlocker: function () {
                var t = this;
                r.$blck || (r.$blck = e('<div id="' + d.blocker + '" />').appendTo(r.$body)), r.$blck.off(u.touchstart).on(u.touchstart, function (e) {
                    e.preventDefault(), e.stopPropagation(), r.$blck.trigger(u.mousedown)
                }).on(u.mousedown, function (e) {
                    e.preventDefault(), r.$html.hasClass(d.modal) || t.$menu.trigger(u.close)
                })
            },
            _initPage: function (t) {
                t || (t = e(this.conf.pageSelector, r.$body), t.length > 1 && (e[a].debug("Multiple nodes found for the page-node, all nodes are wrapped in one <" + this.conf.pageNodetype + ">."), t = t.wrapAll("<" + this.conf.pageNodetype + " />").parent())), t.addClass(d.page), r.$page = t
            },
            _initMenu: function () {
                this.conf.clone && (this.$menu = this.$menu.clone(!0), this.$menu.add(this.$menu.find("*")).filter("[id]").each(function () {
                    e(this).attr("id", d.mm(e(this).attr("id")))
                })), this.$menu.contents().each(function () {
                    3 == e(this)[0].nodeType && e(this).remove()
                }), this.$menu.prependTo("body").addClass(d.menu), this.$menu.addClass(d.mm(this.direction)), this.opts.classes && this.$menu.addClass(this.opts.classes), this.opts.isMenu && this.$menu.addClass(d.ismenu), "left" != this.opts.position && this.$menu.addClass(d.mm(this.opts.position)), "back" != this.opts.zposition && this.$menu.addClass(d.mm(this.opts.zposition))
            },
            _initPanles: function () {
                var t = this;
                this.__refactorClass(e("." + this.conf.listClass, this.$menu), "list"), this.opts.isMenu && e("ul, ol", this.$menu).not(".mm-nolist").addClass(d.list);
                var n = e("." + d.list + " > li", this.$menu);
                this.__refactorClass(n.filter("." + this.conf.selectedClass), "selected"), this.__refactorClass(n.filter("." + this.conf.labelClass), "label"), this.__refactorClass(n.filter("." + this.conf.spacerClass), "spacer"), n.off(u.setSelected).on(u.setSelected, function (t, s) {
                    t.stopPropagation(), n.removeClass(d.selected), "boolean" != typeof s && (s = !0), s && e(this).addClass(d.selected)
                }), this.__refactorClass(e("." + this.conf.panelClass, this.$menu), "panel"), this.$menu.children().filter(this.conf.panelNodetype).add(this.$menu.find("." + d.list).children().children().filter(this.conf.panelNodetype)).addClass(d.panel);
                var s = e("." + d.panel, this.$menu);
                s.each(function (n) {
                    var s = e(this),
                        o = s.attr("id") || d.mm("m" + t.serialnr + "-p" + n);
                    s.attr("id", o)
                }), s.find("." + d.panel).each(function () {
                    var n = e(this),
                        s = n.is("ul, ol") ? n : n.find("ul ,ol").first(),
                        o = n.parent(),
                        i = o.find("> a, > span .chat-name"),
                        a = o.closest("." + d.panel);
                    if (n.data(c.parent, o), o.parent().is("." + d.list)) {
                        var l = e('<a class="' + d.subopen + '" href="#' + n.attr("id") + '" />').insertBefore(i);
                        i.is("a") || l.addClass(d.fullsubopen), "horizontal" == t.direction && s.prepend('<li class="' + d.subtitle + '"><a class="' + d.subclose + '" href="#' + a.attr("id") + '">' + i.text() + "</a></li>")
                    }
                });
                var o = "horizontal" == this.direction ? u.open : u.toggle;
                if (s.each(function () {
                    var n = e(this),
                        s = n.attr("id");
                    e('a[href="#' + s + '"]', t.$menu).off(u.click).on(u.click, function (e) {
                        e.preventDefault(), n.trigger(o)
                    })
                }), "horizontal" == this.direction) {
                    var i = e("." + d.list + " > li." + d.selected, this.$menu);
                    i.add(i.parents("li")).parents("li").removeClass(d.selected).end().each(function () {
                        var t = e(this),
                            n = t.find("> ." + d.panel);
                        n.length && (t.parents("." + d.panel).addClass(d.subopened), n.addClass(d.opened))
                    }).closest("." + d.panel).addClass(d.opened).parents("." + d.panel).addClass(d.subopened)
                } else e("li." + d.selected, this.$menu).addClass(d.opened).parents("." + d.selected).removeClass(d.selected);
                var a = s.filter("." + d.opened);
                a.length || (a = s.first()), a.addClass(d.opened).last().addClass(d.current), "horizontal" == this.direction && s.find("." + d.panel).appendTo(this.$menu)
            },
            _initLinks: function () {
                var t = this;
                e("." + d.list + " > li > a", this.$menu).not("." + d.subopen).not("." + d.subclose).not('[rel="external"]').not('[target="_blank"]').off(u.click).on(u.click, function (n) {
                    var s = e(this),
                        o = s.attr("href");
                    t.__valueOrFn(t.opts.onClick.setSelected, s) && s.parent().trigger(u.setSelected);
                    var i = t.__valueOrFn(t.opts.onClick.preventDefault, s, "#" == o.slice(0, 1));
                    i && n.preventDefault(), t.__valueOrFn(t.opts.onClick.blockUI, s, !i) && r.$html.addClass(d.blocking), t.__valueOrFn(t.opts.onClick.close, s, i) && t.$menu.triggerHandler(u.close)
                })
            },
            _initOpenClose: function () {
                var t = this,
                    n = this.$menu.attr("id");
                n && n.length && (this.conf.clone && (n = d.umm(n)), e('a[href="#' + n + '"]').off(u.click).on(u.click, function (e) {
                    e.preventDefault(), t.$menu.trigger(u.open)
                }));
                var n = r.$page.attr("id");
                n && n.length && e('a[href="#' + n + '"]').off(u.click).on(u.click, function (e) {
                    e.preventDefault(), t.$menu.trigger(u.close)
                })
            },
            __valueOrFn: function (e, t, n) {
                return "function" == typeof e ? e.call(t[0]) : "undefined" == typeof e && "undefined" != typeof n ? n : e
            },
            __refactorClass: function (e, t) {
                e.removeClass(this.conf[t + "Class"]).addClass(d[t])
            }
        }, e.fn[a] = function (o, i) {
            return r.$wndw || s(), o = t(o, i), i = n(i), this.each(function () {
                var t = e(this);
                t.data(a) || t.data(a, new e[a](t, o, i))
            })
        }, e[a].version = l, e[a].defaults = {
            position: "left",
            zposition: "back",
            moveBackground: !0,
            slidingSubmenus: !0,
            modal: !1,
            classes: "",
            onClick: {
                setSelected: !0
            }
        }, e[a].configuration = {
            preventTabbing: !0,
            panelClass: "Panel",
            listClass: "List",
            selectedClass: "Selected",
            labelClass: "Label",
            spacerClass: "Spacer",
            pageNodetype: "div",
            panelNodetype: "ul, ol, div",
            transitionDuration: 400
        },
        function () {
            var t = window.document,
                n = window.navigator.userAgent,
                s = (document.createElement("div").style, "ontouchstart" in t),
                o = "WebkitOverflowScrolling" in t.documentElement.style,
                i = function () {
                    return n.indexOf("Android") >= 0 ? 2.4 > parseFloat(n.slice(n.indexOf("Android") + 8)) : !1
                }();
            e[a].support = {
                touch: s,
                oldAndroidBrowser: i,
                overflowscrolling: function () {
                    return s ? o ? !0 : i ? !1 : !0 : !0
                }()
            }
        }(), e[a].debug = function () {}, e[a].deprecated = function (e, t) {
            "undefined" != typeof console && "undefined" != typeof console.warn && console.warn("MMENU: " + e + " is deprecated, use " + t + " instead.")
        }
    }
}(jQuery);
/*	
 * jQuery mmenu counters addon
 * @requires mmenu 4.0.0 or later
 *
 * mmenu.frebsite.nl
 *
 * Copyright (c) Fred Heusschen
 * www.frebsite.nl
 *
 * Dual licensed under the MIT and GPL licenses.
 * http://en.wikipedia.org/wiki/MIT_License
 * http://en.wikipedia.org/wiki/GNU_General_Public_License
 */
! function (t) {
    var e = "mmenu",
        n = "counters";
    t[e].prototype["_addon_" + n] = function () {
        var o = this,
            u = this.opts[n],
            a = t[e]._c,
            r = t[e]._d,
            d = t[e]._e;
        a.add("counter noresults"), d.add("updatecounters"), "boolean" == typeof u && (u = {
            add: u,
            update: u
        }), "object" != typeof u && (u = {}), u = t.extend(!0, {}, t[e].defaults[n], u), u.count && (t[e].deprecated('the option "count" for counters, the option "update"'), u.update = u.count), this.__refactorClass(t("em." + this.conf.counterClass, this.$menu), "counter");
        var s = t("." + a.panel, this.$menu);
        if (u.add && s.each(function () {
            var e = t(this),
                n = e.data(r.parent);
            if (n) {
                var o = t('<em class="' + a.counter + '" />'),
                    u = n.find("> a." + a.subopen);
                u.parent().find("em." + a.counter).length || u.before(o)
            }
        }), u.update) {
            var c = t("em." + a.counter, this.$menu);
            c.off(d.updatecounters).on(d.updatecounters, function (t) {
                t.stopPropagation()
            }).each(function () {
                var e = t(this),
                    n = t(e.next().attr("href"), o.$menu);
                n.is("." + a.list) || (n = n.find("> ." + a.list)), n.length && e.on(d.updatecounters, function () {
                    var t = n.children().not("." + a.label).not("." + a.subtitle).not("." + a.hidden).not("." + a.noresults);
                    e.html(t.length)
                })
            }).trigger(d.updatecounters), this.$menu.on(d.update, function () {
                c.trigger(d.updatecounters)
            })
        }
    }, t[e].defaults[n] = {
        add: !1,
        update: !1
    }, t[e].configuration.counterClass = "Counter", t[e].addons = t[e].addons || [], t[e].addons.push(n)
}(jQuery);
/*	
 * jQuery mmenu dragOpen addon
 * @requires mmenu 4.0.0 or later
 *
 * mmenu.frebsite.nl
 *
 * Copyright (c) Fred Heusschen
 * www.frebsite.nl
 *
 * Dual licensed under the MIT and GPL licenses.
 * http://en.wikipedia.org/wiki/MIT_License
 * http://en.wikipedia.org/wiki/GNU_General_Public_License
 */
! function (e) {
    function t(e, t, a) {
        return t > e && (e = t), e > a && (e = a), e
    }
    var a = "mmenu",
        o = "dragOpen";
    e[a].prototype["_addon_" + o] = function () {
        var n = this,
            r = this.opts[o];
        if (e.fn.hammer) {
            var i = e[a]._c,
                s = (e[a]._d, e[a]._e);
            i.add("dragging"), s.add("dragleft dragright dragup dragdown dragend");
            var d = e[a].glbl;
            if ("boolean" == typeof r && (r = {
                open: r
            }), "object" != typeof r && (r = {}), "number" != typeof r.maxStartPos && (r.maxStartPos = "left" == this.opts.position || "right" == this.opts.position ? 150 : 75), r = e.extend(!0, {}, e[a].defaults[o], r), r.open) {
                var p = 0,
                    g = !1,
                    c = 0,
                    h = 0,
                    l = "width";
                switch (this.opts.position) {
                case "left":
                case "right":
                    l = "width";
                    break;
                default:
                    l = "height"
                }
                switch (this.opts.position) {
                case "left":
                    var f = {
                        events: s.dragleft + " " + s.dragright,
                        open_dir: "right",
                        close_dir: "left",
                        delta: "deltaX",
                        page: "pageX",
                        negative: !1
                    };
                    break;
                case "right":
                    var f = {
                        events: s.dragleft + " " + s.dragright,
                        open_dir: "left",
                        close_dir: "right",
                        delta: "deltaX",
                        page: "pageX",
                        negative: !0
                    };
                    break;
                case "top":
                    var f = {
                        events: s.dragup + " " + s.dragdown,
                        open_dir: "down",
                        close_dir: "up",
                        delta: "deltaY",
                        page: "pageY",
                        negative: !1
                    };
                    break;
                case "bottom":
                    var f = {
                        events: s.dragup + " " + s.dragdown,
                        open_dir: "up",
                        close_dir: "down",
                        delta: "deltaY",
                        page: "pageY",
                        negative: !0
                    }
                }
                var u = this.__valueOrFn(r.pageNode, this.$menu, d.$page);
                "string" == typeof u && (u = e(u));
                var m = d.$page.find("." + i.mm("fixed-top") + ", ." + i.mm("fixed-bottom")),
                    v = d.$page;
                switch (n.opts.zposition) {
                case "back":
                    v = v.add(m);
                    break;
                case "front":
                    v = n.$menu;
                    break;
                case "next":
                    v = v.add(n.$menu).add(m)
                }
                u.hammer().on(s.touchstart + " " + s.mousedown, function (e) {
                    if ("touchstart" == e.type) var t = e.originalEvent.touches[0] || e.originalEvent.changedTouches[0],
                    a = t[f.page];
                    else if ("mousedown" == e.type) var a = e[f.page];
                    switch (n.opts.position) {
                    case "right":
                    case "bottom":
                        a >= d.$wndw[l]() - r.maxStartPos && (p = 1);
                        break;
                    default:
                        a <= r.maxStartPos && (p = 1)
                    }
                }).on(f.events + " " + s.dragend, function (e) {
                    p > 0 && (e.gesture.preventDefault(), e.stopPropagation())
                }).on(f.events, function (e) {
                    var a = f.negative ? -e.gesture[f.delta] : e.gesture[f.delta];
                    if (g = a > c ? f.open_dir : f.close_dir, c = a, c > r.threshold && 1 == p) {
                        if (d.$html.hasClass(i.opened)) return;
                        p = 2, n._openSetup(), d.$html.addClass(i.dragging), h = t(d.$wndw[l]() * n.conf[o][l].perc, n.conf[o][l].min, n.conf[o][l].max)
                    }
                    2 == p && v.css(n.opts.position, t(c, 10, h) - ("front" == n.opts.zposition ? h : 0))
                }).on(s.dragend, function () {
                    2 == p && (d.$html.removeClass(i.dragging), v.css(n.opts.position, ""), g == f.open_dir ? n._openFinish() : n.close()), p = 0
                })
            }
        }
    }, e[a].defaults[o] = {
        open: !1,
        threshold: 50
    }, e[a].configuration[o] = {
        width: {
            perc: .8,
            min: 140,
            max: 440
        },
        height: {
            perc: .8,
            min: 140,
            max: 880
        }
    }, e[a].addons = e[a].addons || [], e[a].addons.push(o)
}(jQuery);
/*	
 * jQuery mmenu header addon
 * @requires mmenu 4.0.0 or later
 *
 * mmenu.frebsite.nl
 *
 * Copyright (c) Fred Heusschen
 * www.frebsite.nl
 *
 * Dual licensed under the MIT and GPL licenses.
 * http://en.wikipedia.org/wiki/MIT_License
 * http://en.wikipedia.org/wiki/GNU_General_Public_License
 */
! function (e) {
    var t = "mmenu",
        a = "header";
    e[t].prototype["_addon_" + a] = function () {
        var n = this,
            r = this.opts[a],
            d = this.conf[a],
            s = e[t]._c,
            i = (e[t]._d, e[t]._e);
        s.add("header hasheader prev next title titletext"), i.add("updateheader");
        var o = e[t].glbl;
        if ("boolean" == typeof r && (r = {
            add: r,
            update: r
        }), "object" != typeof r && (r = {}), r = e.extend(!0, {}, e[t].defaults[a], r), r.add) {
            var h = r.content ? r.content : '<a class="' + s.prev + '" href="#"></a><span class="' + s.title + '"></span><a class="' + s.next + '" href="#"></a>';
            e('<div class="' + s.header + '" />').prependTo(this.$menu).append(h)
        }
        var p = e("div." + s.header, this.$menu);
        if (p.length && this.$menu.addClass(s.hasheader), r.update && p.length) {
            var l = p.find("." + s.title),
                u = p.find("." + s.prev),
                f = p.find("." + s.next),
                c = "#" + o.$page.attr("id");
            u.add(f).on(i.click, function (t) {
                t.preventDefault(), t.stopPropagation();
                var a = e(this).attr("href");
                "#" !== a && (a == c ? n.$menu.trigger(i.close) : e(a, n.$menu).trigger(i.open))
            }), e("." + s.panel, this.$menu).each(function () {
                var t = e(this),
                    a = e("." + d.panelHeaderClass, t).text(),
                    n = e("." + d.panelPrevClass, t).attr("href"),
                    o = e("." + d.panelNextClass, t).attr("href");
                a || (a = e("." + s.subclose, t).text()), a || (a = r.title), n || (n = e("." + s.subclose, t).attr("href")), t.off(i.updateheader).on(i.updateheader, function (e) {
                    e.stopPropagation(), l[a ? "show" : "hide"]().text(a), u[n ? "show" : "hide"]().attr("href", n), f[o ? "show" : "hide"]().attr("href", o)
                }), t.on(i.open, function () {
                    e(this).trigger(i.updateheader)
                })
            }).filter("." + s.current).trigger(i.updateheader)
        }
    }, e[t].defaults[a] = {
        add: !1,
        content: !1,
        update: !1,
        title: "Menu"
    }, e[t].configuration[a] = {
        panelHeaderClass: "Header",
        panelNextClass: "Next",
        panelPrevClass: "Prev"
    }, e[t].addons = e[t].addons || [], e[t].addons.push(a)
}(jQuery);
/*	
 * jQuery mmenu labels addon
 * @requires mmenu 4.1.0 or later
 *
 * mmenu.frebsite.nl
 *
 * Copyright (c) Fred Heusschen
 * www.frebsite.nl
 *
 * Dual licensed under the MIT and GPL licenses.
 * http://en.wikipedia.org/wiki/MIT_License
 * http://en.wikipedia.org/wiki/GNU_General_Public_License
 */
! function (e) {
    var l = "mmenu",
        s = "labels";
    e[l].prototype["_addon_" + s] = function () {
        function a() {
            var e = t.hassearch && o.$menu.hasClass(t.hassearch),
                l = t.hasheader && o.$menu.hasClass(t.hasheader);
            return e ? l ? 100 : 50 : l ? 60 : 0
        }
        var o = this,
            n = this.opts[s],
            t = e[l]._c,
            i = (e[l]._d, e[l]._e);
        if (t.add("collapsed"), t.add("fixedlabels original clone"), i.add("updatelabels position scroll"), e[l].support.touch && (i.scroll += " " + i.mm("touchmove")), "boolean" == typeof n && (n = {
            collapse: n
        }), "object" != typeof n && (n = {}), n = e.extend(!0, {}, e[l].defaults[s], n), n.collapse) {
            this.__refactorClass(e("li." + this.conf.collapsedClass, this.$menu), "collapsed");
            var d = e("." + t.label, this.$menu);
            d.each(function () {
                var l = e(this),
                    s = l.nextUntil("." + t.label, "all" == n.collapse ? null : "." + t.collapsed);
                "all" == n.collapse && (l.addClass(t.opened), s.removeClass(t.collapsed)), s.length && (l.wrapInner("<span />"), e('<a href="#' + n.attr("id") + '" class="' + t.subopen + " " + t.fullsubopen + '" >').prependTo(l).on(i.click, function (e) {
                    e.preventDefault(), l.toggleClass(t.opened), s[l.hasClass(t.opened) ? "removeClass" : "addClass"](t.collapsed)
                }))
            })
        } else if (n.fixed) {
            if ("horizontal" != this.direction) return;
            this.$menu.addClass(t.fixedlabels);
            var r = e("." + t.panel, this.$menu),
                d = e("." + t.label, this.$menu);
            r.add(d).off(i.updatelabels + " " + i.position + " " + i.scroll).on(i.updatelabels + " " + i.position + " " + i.scroll, function (e) {
                e.stopPropagation()
            });
            var p = a();
            r.each(function () {
                var l = e(this),
                    s = l.find("." + t.label);
                if (s.length) {
                    var o = l.scrollTop();
                    s.each(function () {
                        var s = e(this);
                        s.wrapInner("<div />").wrapInner("<div />");
                        var a, n, d, r = s.find("> div"),
                            c = e();
                        s.on(i.updatelabels, function () {
                            o = l.scrollTop(), s.hasClass(t.hidden) || (c = s.nextAll("." + t.label).not("." + t.hidden).first(), a = s.offset().top + o, n = c.length ? c.offset().top + o : !1, d = r.height(), s.trigger(i.position))
                        }), s.on(i.position, function () {
                            var e = 0;
                            n && o + p > n - d ? e = n - a - d : o + p > a && (e = o - a + p), r.css("top", e)
                        })
                    }), l.on(i.updatelabels, function () {
                        o = l.scrollTop(), p = a(), s.trigger(i.position)
                    }).on(i.scroll, function () {
                        s.trigger(i.updatelabels)
                    })
                }
            }), this.$menu.on(i.update, function () {
                r.trigger(i.updatelabels)
            }).on(i.opening, function () {
                r.trigger(i.updatelabels).trigger(i.scroll)
            })
        }
    }, e[l].defaults[s] = {
        fixed: !1,
        collapse: !1
    }, e[l].configuration.collapsedClass = "Collapsed", e[l].addons = e[l].addons || [], e[l].addons.push(s)
}(jQuery);
/*	
 * jQuery mmenu searchfield addon
 * @requires mmenu 4.0.0 or later
 *
 * mmenu.frebsite.nl
 *
 * Copyright (c) Fred Heusschen
 * www.frebsite.nl
 *
 * Dual licensed under the MIT and GPL licenses.
 * http://en.wikipedia.org/wiki/MIT_License
 * http://en.wikipedia.org/wiki/GNU_General_Public_License
 */
! function (e) {
    function s(e) {
        switch (e) {
        case 9:
        case 16:
        case 17:
        case 18:
        case 37:
        case 38:
        case 39:
        case 40:
            return !0
        }
        return !1
    }
    var n = "mmenu",
        t = "searchfield";
    e[n].prototype["_addon_" + t] = function () {
        var a = this,
            r = this.opts[t],
            o = e[n]._c,
            l = e[n]._d,
            d = e[n]._e;
        if (o.add("search hassearch noresults nosubresults counter"), d.add("search reset change"), "boolean" == typeof r && (r = {
            add: r,
            search: r
        }), "object" != typeof r && (r = {}), r = e.extend(!0, {}, e[n].defaults[t], r), r.add && (e('<div class="' + o.search + '"><div class="chat-header">Contacts</div></div>').prependTo(this.$menu).append('<input placeholder="' + r.placeholder + '" type="text" autocomplete="off" />'), r.noResults && e("ul, ol", this.$menu).first().append('<li class="' + o.noresults + '">' + r.noResults + "</li>")), e("div." + o.search, this.$menu).length && this.$menu.addClass(o.hassearch), r.search) {
            var i = e("div." + o.search, this.$menu).find("input");
            if (i.length) {
                var u = e("." + o.panel, this.$menu),
                    h = e("." + o.list + "> li." + o.label, this.$menu),
                    c = e("." + o.list + "> li", this.$menu).not("." + o.subtitle).not("." + o.label).not("." + o.noresults),
                    f = "> a";
                r.showLinksOnly || (f += ", > span .chat-name"), i.off(d.keyup + " " + d.change).on(d.keyup, function (e) {
                    s(e.keyCode) || a.$menu.trigger(d.search)
                }).on(d.change, function () {
                    a.$menu.trigger(d.search)
                }), this.$menu.off(d.reset + " " + d.search).on(d.reset + " " + d.search, function (e) {
                    e.stopPropagation()
                }).on(d.reset, function () {
                    a.$menu.trigger(d.search, [""])
                }).on(d.search, function (s, n) {
                    "string" == typeof n ? i.val(n) : n = i.val(), n = n.toLowerCase(), u.scrollTop(0), c.add(h).addClass(o.hidden), c.each(function () {
                        var s = e(this);
                        e(f, s).text().toLowerCase().indexOf(n) > -1 && s.add(s.prevAll("." + o.label).first()).removeClass(o.hidden)
                    }), e(u.get().reverse()).each(function () {
                        var s = e(this),
                            n = s.data(l.parent);
                        if (n) {
                            var t = s.add(s.find("> ." + o.list)).find("> li").not("." + o.subtitle).not("." + o.label).not("." + o.hidden);
                            t.length ? n.removeClass(o.hidden).removeClass(o.nosubresults).prevAll("." + o.label).first().removeClass(o.hidden) : (s.hasClass(o.current) && n.trigger(d.open), n.addClass(o.nosubresults))
                        }
                    }), a.$menu[c.not("." + o.hidden).length ? "removeClass" : "addClass"](o.noresults), a.$menu.trigger(d.update)
                })
            }
        }
    }, e[n].defaults[t] = {
        add: !1,
        search: !1,
        showLinksOnly: !0,
        placeholder: "Search",
        noResults: "No results found."
    }, e[n].addons = e[n].addons || [], e[n].addons.push(t)
}(jQuery);