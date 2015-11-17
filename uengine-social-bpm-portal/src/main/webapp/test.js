/*! bpmn-io-demo - 2015-10-23 - https://github.com/bpmn-io */
require = function e(t, n, i) {
    function r(a, s) {
        if (!n[a]) {
            if (!t[a]) {
                var c = "function" == typeof require && require;
                if (!s && c)return c(a, !0);
                if (o)return o(a, !0);
                var l = new Error("Cannot find module '" + a + "'");
                throw l.code = "MODULE_NOT_FOUND", l
            }
            var u = n[a] = {exports: {}};
            t[a][0].call(u.exports, function (e) {
                var n = t[a][1][e];
                return r(n ? n : e)
            }, u, u.exports, e, t, n, i)
        }
        return n[a].exports
    }

    for (var o = "function" == typeof require && require, a = 0; a < i.length; a++)r(i[a]);
    return r
}({
    1: [function (e, t, n) {
        (function (t) {
            "use strict";
            function n(e) {
                T = e
            }

            function i() {
                return T ? "The changes you performed on the diagram will be lost upon navigation." : void 0
            }

            function r() {
                var e = window.localStorage || {};
                this.get = function (t) {
                    return e[t]
                }, this.set = function (t, n) {
                    e[t] = n
                }
            }

            function o(e) {
                _(document.body).removeClass(k.join(" ")).addClass(e)
            }

            function a(e) {
                o("error"), C.find(".error .error-log").val(e.message), console.error(e)
            }

            function s(e) {
                var t = e && e.length;
                if (c(D["import-warnings-alert"], t), t) {
                    console.warn("imported with warnings");
                    var n = "";
                    e.forEach(function (e) {
                        console.log(e), n += e.message + "\n\n"
                    });
                    var i = D["import-warnings-dialog"];
                    i.find(".error-log").val(n)
                }
            }

            function c(e, t) {
                e[t ? "addClass" : "removeClass"]("open")
            }

            function l(e) {
                function t(e) {
                    e.stopPropagation()
                }

                function n(r) {
                    c(e, !1), e.off("click", n), i.off("click", t)
                }

                var i = e.find(".content");
                c(e, !0), i.on("click", t), e.on("click", n)
            }

            function u(e) {
                N.importXML(e, function (e, t) {
                    e ? (a(e), track("diagram", "open", "error")) : (setTimeout(function () {
                        N.get("canvas").zoom("fit-viewport"), o("shown")
                    }, 0), track("diagram", "open", "success"), o("loaded")), s(t)
                })
            }

            function p() {
                N.createDiagram(function (e, t) {
                    e ? (a(e), track("diagram", "create", "error")) : (N.get("selection").select(N.get("elementRegistry").get("StartEvent_1")), setTimeout(function () {
                        N.get("canvas").zoom("fit-viewport"), o("shown")
                    }, 0), track("diagram", "create", "success"), o("loaded")), t && t.length && console.warn("[import]", t);
                    var n = N.get("keyboard");
                    n.bind(document)
                })
            }

            function d(e) {
                N.saveSVG(e)
            }

            function h(e) {
                N.saveXML({format: !0}, function (t, n) {
                    e(t, n)
                })
            }

            function f(e, t) {
                if (!window.FileReader)return window.alert("Looks like you use an older browser that does not support drag and drop. Try using a modern browser such as Chrome, Firefox or Internet Explorer > 10.");
                if (e) {
                    o("loading");
                    var n = new FileReader;
                    n.onload = function (e) {
                        var n = e.target.result;
                        t(n)
                    }, n.readAsText(e)
                }
            }

            function m(e) {
                N.diagram && (c(D["undo-redo-alert"], !1), j.set("hide-alert", "yes"))
            }

            function g() {
                if (!j.get("hide-alert")) {
                    var e = N.get("commandStack"), t = e._stackIdx;
                    c(D["undo-redo-alert"], t >= 0)
                }
            }

            function v() {
                D["editing-tools"].show()
            }

            function y(e) {
                document.fullscreenElement || document.mozFullScreenElement || document.webkitFullscreenElement || document.msFullscreenElement ? document.exitFullscreen ? document.exitFullscreen() : document.msExitFullscreen ? document.msExitFullscreen() : document.mozCancelFullScreen ? document.mozCancelFullScreen() : document.webkitExitFullscreen && document.webkitExitFullscreen() : e.requestFullscreen ? e.requestFullscreen() : e.msRequestFullscreen ? e.msRequestFullscreen() : e.mozRequestFullScreen ? e.mozRequestFullScreen() : document.documentElement.webkitRequestFullscreen && e.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT)
            }

            function b(e) {
                N.diagram && N.diagram.get("commandStack").undo()
            }

            t.track = function () {
            };
            var x = e("bpmn-js/lib/Modeler"), E = e("diagram-js-origin"), w = e("bpmn-js-cli"), _ = e("jquery"), S = (e("lodash/function/defer"), e("lodash/function/debounce")), C = _("#js-drop-zone"), A = _("#js-canvas"), T = !1;
            window.onbeforeunload = i;
            var j = new r, N = new x({
                container: A,
                keyboard: {bindTo: document},
                additionalModules: [E, w]
            }), k = ["error", "loading", "loaded", "shown", "intro"];
            !function (e, t) {
                function n(e) {
                    e.stopPropagation(), e.preventDefault(), track("diagram", "open-drop");
                    var n = e.dataTransfer.files;
                    f(n[0], t)
                }

                function i(e) {
                    e.stopPropagation(), e.preventDefault(), e.dataTransfer.dropEffect = "copy"
                }

                e.get(0).addEventListener("dragover", i, !1), e.get(0).addEventListener("drop", n, !1)
            }(C, u);
            var R, M = _('<input type="file" />').appendTo(document.body).css({
                width: 1,
                height: 1,
                display: "none",
                overflow: "hidden"
            }).on("change", function (e) {
                track("diagram", "open-dialog"), f(e.target.files[0], u)
            }), D = {}, P = {
                "bio.toggleFullscreen": function () {
                    var e = document.querySelector("html");
                    y(e)
                }, "bio.createNew": p, "bio.openLocal": function () {
                    _(M).trigger("click")
                }, "bio.zoomReset": function () {
                    N.diagram && N.diagram.get("zoomScroll").reset()
                }, "bio.zoomIn": function (e) {
                    N.diagram && N.diagram.get("zoomScroll").stepZoom(1)
                }, "bio.zoomOut": function (e) {
                    N.diagram && N.diagram.get("zoomScroll").stepZoom(-1)
                }, "bio.showKeyboard": function (e) {
                    var t = D["keybindings-dialog"], n = navigator.platform;
                    /Mac/.test(n) ? t.find(".bindings-default").remove() : t.find(".bindings-mac").remove(), l(t)
                }, "bio.showAbout": function (e) {
                    l(D["about-dialog"])
                }, "bio.undo": b, "bio.hideUndoAlert": m, "bio.clearImportDetails": function (e) {
                    s(null)
                }, "bio.showImportDetails": function (e) {
                    l(D["import-warnings-dialog"])
                }
            };
            _(document).on("ready", function () {
                function e(e) {
                    var t = _(e).attr("jsaction").split(/:(.+$)/, 2);
                    return {event: t[0], name: t[1]}
                }

                function t(t) {
                    var n = e(_(this)), i = n.name, r = P[i];
                    if (!r)throw new Error("no action <" + i + "> defined");
                    t.preventDefault(), r(t)
                }

                function i(e, t, n) {
                    var i = encodeURIComponent(n);
                    n ? e.attr({
                        href: "data:application/bpmn20-xml;charset=UTF-8," + i,
                        download: t
                    }).removeClass("inactive") : e.addClass("inactive")
                }

                function r() {
                    n(!0), g(), R()
                }

                function o() {
                    n(!1), g(), v(), R()
                }

                _("[jswidget]").each(function () {
                    var e = _(this), t = e.attr("jswidget");
                    D[t] = e
                });
                var a = {};
                _("[jsaction]").each(function () {
                    var n = e(_(this)), i = n.event;
                    a[i] || (_(document.body).on(i, "[jsaction]", t), a[i] = !0);
                    var r = n.name, o = P[r];
                    if (!o)throw new Error("no action <" + r + "> defined")
                }), D.downloadBPMN.click(function (e) {
                    n(!1)
                }), R = S(function () {
                    d(function (e, t) {
                        i(D.downloadSVG, "diagram.svg", e ? null : t)
                    }), h(function (e, t) {
                        i(D.downloadBPMN, "diagram.bpmn", e ? null : t), j.get("save") && j.set("save.diagramXML", t)
                    })
                }, 500), N.on("commandStack.changed", r), N.on("import.success", o)
            }), t.bpmnio = {renderer: N, openDiagram: u}, function () {
                var e = window.location.href, t = null, n = /\/s\/(.*)/.exec(e);
                if (n)t = function () {
                    var e = _.ajax("/bpmn/diagrams/" + n[1] + ".bpmn", {dataType: "text"});
                    e.success(function (e) {
                        u(e)
                    }).error(function (e) {
                        var t;
                        t = 404 === e.status ? new Error("The diagram does not exist (code=404)") : new Error("Failed to load diagram (code=" + e.code + ")"), a(t)
                    })
                }; else if (/\/new(\?.*)?/.test(e))t = p; else if (j.get("save")) {
                    var i = j.get("save.diagramXML");
                    i && (t = function () {
                        u(i)
                    })
                }
                t && (o("loading"), setTimeout(t, 100))
            }()
        }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
    }, {
        "bpmn-js-cli": 2,
        "bpmn-js/lib/Modeler": 22,
        "diagram-js-origin": 119,
        jquery: "jquery",
        "lodash/function/debounce": 284,
        "lodash/function/defer": 285
    }],
    2: [function (e, t, n) {
        t.exports = e("./lib")
    }, {"./lib": 16}],
    3: [function (e, t, n) {
        "use strict";
        function i(e) {
            return Array.prototype.slice.call(e)
        }

        function r(e) {
            return function (t, n) {
                return {
                    name: t, parse: function (t) {
                        return e(t, n || {})
                    }
                }
            }
        }

        function o() {
            return function (e, t) {
                if (l(e) && (e = e.join(" ")), "" === e || e)return e;
                if (t.defaultValue)return t.defaultValue;
                throw new Error("no value given")
            }
        }

        function a() {
            return function (e, t) {
                if (e)return e && "false" !== e;
                if (t.defaultValue)return t.defaultValue;
                if (t.optional)return void 0;
                throw new Error("no value given")
            }
        }

        function s() {
            return function (e, t) {
                if (0 === e || e)return u(e) ? e : parseFloat(e, 10);
                if (t.defaultValue)return t.defaultValue;
                throw new Error("no value given")
            }
        }

        function c(e, t) {
            this._commands = {}, this._params = {}, this._injector = t, this._registerParsers(), this._registerCommands(), this._bind(e)
        }

        var l = e("lodash/lang/isArray"), u = e("lodash/lang/isNumber"), p = e("lodash/lang/isString"), d = e("lodash/lang/isFunction"), h = e("lodash/collection/forEach");
        c.$inject = ["config", "injector"], t.exports = c, c.prototype = {}, c.prototype._bind = function (e) {
            e.cli && e.cli.bindTo && (console.info("bpmn-js-cli is available via window." + e.cli.bindTo), window[e.cli.bindTo] = this)
        }, c.prototype._registerParser = function (e, t) {
            var n = this._injector.invoke(t);
            if (!d(n))throw new Error("parser must be a Function<String, Object> -> Object");
            this._params[e] = r(n)
        }, c.prototype._registerCommand = function (e, t) {
            var n = d(t) ? this._injector.invoke(t) : t;
            n.args = n.args || [], this._commands[e] = n;
            var r = this;
            this[e] = function () {
                var t = i(arguments);
                return t.unshift(e), r.exec.apply(r, t)
            }
        }, c.prototype._registerParsers = function () {
            this._registerParser("string", o), this._registerParser("number", s), this._registerParser("bool", a)
        }, c.prototype._registerCommands = function () {
            var e = this;
            this._registerCommand("help", {
                exec: function () {
                    var t = "available commands:\n";
                    return h(e._commands, function (e, n) {
                        t += "\n	" + n
                    }), t
                }
            })
        }, c.prototype.parseArguments = function (e, t) {
            var n = [], i = t.args.length - 1;
            return h(t.args, function (r, o) {
                var a;
                a = o === i && e.length > t.args.length ? e.slice(o) : e[o];
                try {
                    n.push(r.parse(a))
                } catch (s) {
                    throw new Error("could not parse <" + r.name + ">: " + s.message)
                }
            }), n
        }, c.prototype.exec = function () {
            var e = [];
            i(arguments).forEach(function (t) {
                p(t) ? e = e.concat(t.split(/\s+/)) : e.push(t)
            });
            var t = e.shift(), n = this._commands[t];
            if (!n)throw new Error("no command <" + t + ">, execute <commands> to get a list of available commands");
            var r, o;
            try {
                r = this.parseArguments(e, n), o = n.exec.apply(this, r)
            } catch (a) {
                throw new Error("failed to execute <" + t + "> with args <[" + e.join(", ") + "]> : " + a.message)
            }
            return o
        }
    }, {
        "lodash/collection/forEach": 274,
        "lodash/lang/isArray": 387,
        "lodash/lang/isFunction": 388,
        "lodash/lang/isNumber": 390,
        "lodash/lang/isString": 393
    }],
    4: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            return {
                args: [e.shape("source"), e.string("type"), e.point("delta", {defaultValue: {x: 200, y: 0}})],
                exec: function (e, n, i) {
                    var r = {x: e.x + e.width / 2 + i.x, y: e.y + e.height / 2 + i.y};
                    return t.appendShape(e, {type: n}, r).id
                }
            }
        }

        i.$inject = ["cli._params", "modeling"], t.exports = i
    }, {}],
    5: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            return {
                args: [e.shape("source"), e.shape("target"), e.string("type"), e.shape("parent", {optional: !0})],
                exec: function (e, n, i, r) {
                    return t.createConnection(e, n, {type: i}, r || e.parent).id
                }
            }
        }

        i.$inject = ["cli._params", "modeling"], t.exports = i
    }, {}],
    6: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            return {
                args: [e.string("type"), e.point("position"), e.shape("parent"), e.bool("isAttach", {optional: !0})],
                exec: function (e, n, i, r) {
                    return t.createShape({type: e}, n, i, r).id
                }
            }
        }

        i.$inject = ["cli._params", "modeling"], t.exports = i
    }, {}],
    7: [function (e, t, n) {
        "use strict";
        function i(e) {
            return {
                args: [e.element("element")], exec: function (e) {
                    return e
                }
            }
        }

        i.$inject = ["cli._params"], t.exports = i
    }, {}],
    8: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            return {
                exec: function () {
                    function e() {
                        return !0
                    }

                    function n(e) {
                        return e.id
                    }

                    return t.filter(e).map(n)
                }
            }
        }

        i.$inject = ["cli._params", "elementRegistry"], t.exports = i
    }, {}],
    9: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            return {
                args: [e.shapes("shapes"), e.point("delta"), e.shape("newParent", {optional: !0}), e.bool("isAttach", {optional: !0})],
                exec: function (e, n, i, r) {
                    return t.moveElements(e, n, i, r), e
                }
            }
        }

        i.$inject = ["cli._params", "modeling"], t.exports = i
    }, {}],
    10: [function (e, t, n) {
        "use strict";
        function i(e) {
            return {
                exec: function () {
                    e.redo()
                }
            }
        }

        i.$inject = ["commandStack"], t.exports = i
    }, {}],
    11: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            return {
                args: [e.element("connection")], exec: function (e) {
                    return t.removeConnection(e)
                }
            }
        }

        i.$inject = ["cli._params", "modeling"], t.exports = i
    }, {}],
    12: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            return {
                args: [e.shape("shape")], exec: function (e) {
                    return t.removeShape(e)
                }
            }
        }

        i.$inject = ["cli._params", "modeling"], t.exports = i
    }, {}],
    13: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            return {
                args: [e.string("format")], exec: function (e) {
                    if ("svg" !== e) {
                        if ("bpmn" === e)return t.saveXML(function (e, t) {
                            e ? console.error(e) : console.info(t)
                        });
                        throw new Error("unknown format, <svg> and <bpmn> are available")
                    }
                    t.saveSVG(function (e, t) {
                        e ? console.error(e) : console.info(t)
                    })
                }
            }
        }

        i.$inject = ["cli._params", "bpmnjs"], t.exports = i
    }, {}],
    14: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            return {
                args: [e.element("element"), e.string("newLabel")], exec: function (e, n) {
                    return t.updateLabel(e, n), e
                }
            }
        }

        i.$inject = ["cli._params", "modeling"], t.exports = i
    }, {}],
    15: [function (e, t, n) {
        "use strict";
        function i(e) {
            return {
                exec: function () {
                    e.undo()
                }
            }
        }

        i.$inject = ["commandStack"], t.exports = i
    }, {}],
    16: [function (e, t, n) {
        t.exports = {
            __init__: ["cliInitializer"],
            cli: ["type", e("./cli")],
            cliInitializer: ["type", e("./initializer")]
        }
    }, {"./cli": 3, "./initializer": 17}],
    17: [function (e, t, n) {
        "use strict";
        function i(t) {
            t._registerParser("point", e("./parsers/point")), t._registerParser("element", e("./parsers/element")), t._registerParser("shape", e("./parsers/shape")), t._registerParser("shapes", e("./parsers/shapes")), t._registerCommand("append", e("./commands/append")), t._registerCommand("connect", e("./commands/connect")), t._registerCommand("create", e("./commands/create")), t._registerCommand("element", e("./commands/element")), t._registerCommand("elements", e("./commands/elements")), t._registerCommand("move", e("./commands/move")), t._registerCommand("redo", e("./commands/redo")), t._registerCommand("save", e("./commands/save")), t._registerCommand("setLabel", e("./commands/set-label")), t._registerCommand("undo", e("./commands/undo")), t._registerCommand("removeShape", e("./commands/removeShape")), t._registerCommand("removeConnection", e("./commands/removeConnection"))
        }

        i.$inject = ["cli"], t.exports = i
    }, {
        "./commands/append": 4,
        "./commands/connect": 5,
        "./commands/create": 6,
        "./commands/element": 7,
        "./commands/elements": 8,
        "./commands/move": 9,
        "./commands/redo": 10,
        "./commands/removeConnection": 11,
        "./commands/removeShape": 12,
        "./commands/save": 13,
        "./commands/set-label": 14,
        "./commands/undo": 15,
        "./parsers/element": 18,
        "./parsers/point": 19,
        "./parsers/shape": 20,
        "./parsers/shapes": 21
    }],
    18: [function (e, t, n) {
        "use strict";
        function i(e) {
            return function (t, n) {
                if (r(t))return t;
                var i = e.get(t);
                if (!i) {
                    if (n.optional)return null;
                    throw t ? new Error("element with id <" + t + "> does not exist") : new Error("argument required")
                }
                return i
            }
        }

        var r = e("lodash/lang/isObject");
        i.$inject = ["elementRegistry"], t.exports = i
    }, {"lodash/lang/isObject": 391}],
    19: [function (e, t, n) {
        "use strict";
        function i() {
            return function (e, t) {
                if (r(e))return e;
                if (!e && t.defaultValue)return t.defaultValue;
                var n = e.split(/,/);
                if (2 !== n.length)throw new Error("expected delta to match (\\d*,\\d*)");
                return {x: parseInt(n[0], 10) || 0, y: parseInt(n[1], 10) || 0}
            }
        }

        var r = e("lodash/lang/isObject");
        t.exports = i
    }, {"lodash/lang/isObject": 391}],
    20: [function (e, t, n) {
        "use strict";
        function i(e) {
            return function (t, n) {
                if (r(t))return t;
                var i = e.get(t);
                if (!i) {
                    if (n.optional)return null;
                    throw t ? new Error("element with id <" + t + "> does not exist") : new Error("argument required")
                }
                if (i.waypoints)throw new Error("element <" + t + "> is a connection");
                return i
            }
        }

        var r = e("lodash/lang/isObject");
        i.$inject = ["elementRegistry"], t.exports = i
    }, {"lodash/lang/isObject": 391}],
    21: [function (e, t, n) {
        "use strict";
        function i(e) {
            return function (t, n) {
                return a(t) ? t = t.split(",") : r(t) || (t = [t]), t.map(function (t) {
                    if (o(t))return t;
                    var i = e.get(t);
                    if (!i) {
                        if (n.optional)return null;
                        throw t ? new Error("element with id <" + t + "> does not exist") : new Error("argument required")
                    }
                    if (i.waypoints)throw new Error("element <" + t + "> is a connection");
                    return i
                }).filter(function (e) {
                    return e
                })
            }
        }

        var r = e("lodash/lang/isArray"), o = e("lodash/lang/isObject"), a = e("lodash/lang/isString");
        i.$inject = ["elementRegistry"], t.exports = i
    }, {"lodash/lang/isArray": 387, "lodash/lang/isObject": 391, "lodash/lang/isString": 393}],
    22: [function (e, t, n) {
        "use strict";
        function i(e) {
            s.call(this, e)
        }

        var r = e("inherits"), o = e("bpmn-moddle/lib/id-support"), a = e("ids"), s = e("./Viewer"), c = '<?xml version="1.0" encoding="UTF-8"?><bpmn:definitions xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:dc="http://www.omg.org/spec/DD/20100524/DC" targetNamespace="http://bpmn.io/schema/bpmn" id="Definitions_1"><bpmn:process id="Process_1" isExecutable="false"><bpmn:startEvent id="StartEvent_1"/></bpmn:process><bpmndi:BPMNDiagram id="BPMNDiagram_1"><bpmndi:BPMNPlane id="BPMNPlane_1" bpmnElement="Process_1"><bpmndi:BPMNShape id="_BPMNShape_StartEvent_2" bpmnElement="StartEvent_1"><dc:Bounds height="36.0" width="36.0" x="173.0" y="102.0"/></bpmndi:BPMNShape></bpmndi:BPMNPlane></bpmndi:BPMNDiagram></bpmn:definitions>';
        r(i, s), i.prototype.createDiagram = function (e) {
            return this.importXML(c, e)
        }, i.prototype.createModdle = function () {
            var e = s.prototype.createModdle.call(this);
            return o.extend(e, new a([32, 36, 1])), e
        }, i.prototype._interactionModules = [e("./features/label-editing"), e("./features/auto-resize"), e("diagram-js/lib/navigation/zoomscroll"), e("diagram-js/lib/navigation/movecanvas"), e("diagram-js/lib/navigation/touch")], i.prototype._modelingModules = [e("diagram-js/lib/features/move"), e("diagram-js/lib/features/bendpoints"), e("diagram-js/lib/features/resize"), e("diagram-js/lib/features/space-tool"), e("diagram-js/lib/features/lasso-tool"), e("./features/keyboard"), e("./features/snapping"), e("./features/modeling"), e("./features/context-pad"), e("./features/palette"), e("./features/replace-preview")], i.prototype._modules = [].concat(i.prototype._modules, i.prototype._interactionModules, i.prototype._modelingModules), t.exports = i
    }, {
        "./Viewer": 23,
        "./features/auto-resize": 29,
        "./features/context-pad": 31,
        "./features/keyboard": 33,
        "./features/label-editing": 37,
        "./features/modeling": 63,
        "./features/palette": 69,
        "./features/replace-preview": 71,
        "./features/snapping": 79,
        "bpmn-moddle/lib/id-support": 90,
        "diagram-js/lib/features/bendpoints": 143,
        "diagram-js/lib/features/lasso-tool": 163,
        "diagram-js/lib/features/move": 188,
        "diagram-js/lib/features/resize": 204,
        "diagram-js/lib/features/space-tool": 218,
        "diagram-js/lib/navigation/movecanvas": 230,
        "diagram-js/lib/navigation/touch": 231,
        "diagram-js/lib/navigation/zoomscroll": 234,
        ids: 113,
        inherits: 115
    }],
    23: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            var n = e.get("eventBus");
            t.forEach(function (e) {
                n.on(e.event, e.priority, e.handler, e.that)
            })
        }

        function r(e) {
            var t = /unparsable content <([^>]+)> detected([\s\S]*)$/, n = t.exec(e.message);
            return n && (e.message = "unparsable content <" + n[1] + "> detected; this may indicate an invalid BPMN 2.0 diagram file" + n[2]), e
        }

        function o(e) {
            return e + (u(e) ? "px" : "")
        }

        function a(e) {
            this.options = e = s({}, v, e || {});
            var t = e.container;
            t.get && (t = t.get(0)), l(t) && (t = d(t));
            var n = this.container = p('<div class="bjs-container"></div>');
            t.appendChild(n), s(n.style, {width: o(e.width), height: o(e.height), position: e.position});
            var i = "iVBORw0KGgoAAAANSUhEUgAAADQAAAA0CAMAAADypuvZAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAADBQTFRFiMte9PrwldFwfcZPqtqN0+zEyOe1XLgjvuKncsJAZ70y6fXh3vDT////UrQV////G2zN+AAAABB0Uk5T////////////////////AOAjXRkAAAHDSURBVHjavJZJkoUgDEBJmAX8979tM8u3E6x20VlYJfFFMoL4vBDxATxZcakIOJTWSmxvKWVIkJ8jHvlRv1F2LFrVISCZI+tCtQx+XfewgVTfyY3plPiQEAzI3zWy+kR6NBhFBYeBuscJLOUuA2WVLpCjVIaFzrNQZArxAZKUQm6gsj37L9Cb7dnIBUKxENaaMJQqMpDXvSL+ktxdGRm2IsKgJGGPg7atwUG5CcFUEuSv+CwQqizTrvDTNXdMU2bMiDWZd8d7QIySWVRsb2vBBioxOFt4OinPBapL+neAb5KL5IJ8szOza2/DYoipUCx+CjO0Bpsv0V6mktNZ+k8rlABlWG0FrOpKYVo8DT3dBeLEjUBAj7moDogVii7nSS9QzZnFcOVBp1g2PyBQ3Vr5aIapN91VJy33HTJLC1iX2FY6F8gRdaAeIEfVONgtFCzZTmoLEdOjBDfsIOA6128gw3eu1shAajdZNAORxuQDJN5A5PbEG6gNIu24QJD5iNyRMZIr6bsHbCtCU/OaOaSvgkUyDMdDa1BXGf5HJ1To+/Ym6mCKT02Y+/Sa126ZKyd3jxhzpc1r8zVL6YM1Qy/kR4ABAFJ6iQUnivhAAAAAAElFTkSuQmCC", r = '<a href="http://bpmn.io" target="_blank" class="bjs-powered-by" title="Powered by bpmn.io" style="position: absolute; bottom: 15px; right: 15px; z-index: 100"><img src="data:image/png;base64,' + i + '"></a>';
            n.appendChild(p(r))
        }

        var s = e("lodash/object/assign"), c = e("lodash/object/omit"), l = e("lodash/lang/isString"), u = e("lodash/lang/isNumber"), p = e("min-dom/lib/domify"), d = e("min-dom/lib/query"), h = e("min-dom/lib/remove"), f = e("diagram-js"), m = e("bpmn-moddle"), g = e("./import/Importer"), v = {
            width: "100%",
            height: "100%",
            position: "relative",
            container: "body"
        };
        a.prototype.importXML = function (e, t) {
            var n = this;
            this.moddle = this.createModdle(), this.moddle.fromXML(e, "bpmn:Definitions", function (e, i, o) {
                if (e)return e = r(e), t(e);
                var a = o.warnings;
                n.importDefinitions(i, function (e, n) {
                    return e ? t(e) : void t(null, a.concat(n || []))
                })
            })
        }, a.prototype.saveXML = function (e, t) {
            t || (t = e, e = {});
            var n = this.definitions;
            return n ? void this.moddle.toXML(n, e, t) : t(new Error("no definitions loaded"))
        }, a.prototype.createModdle = function () {
            return new m(this.options.moddleExtensions)
        }, a.prototype.saveSVG = function (e, t) {
            t || (t = e, e = {});
            var n = this.get("canvas"), i = n.getDefaultLayer(), r = n._svg.select("defs"), o = i.innerSVG(), a = r && r.outerSVG() || "", s = i.getBBox(), c = '<?xml version="1.0" encoding="utf-8"?>\n<!-- created with bpmn-js / http://bpmn.io -->\n<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">\n<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="' + s.width + '" height="' + s.height + '" viewBox="' + s.x + " " + s.y + " " + s.width + " " + s.height + '" version="1.1">' + a + o + "</svg>";
            t(null, c)
        }, a.prototype.get = function (e) {
            if (!this.diagram)throw new Error("no diagram loaded");
            return this.diagram.get(e)
        }, a.prototype.invoke = function (e) {
            if (!this.diagram)throw new Error("no diagram loaded");
            return this.diagram.invoke(e)
        }, a.prototype.importDefinitions = function (e, t) {
            try {
                this.diagram && this.clear(), this.definitions = e;
                var n = this.diagram = this._createDiagram(this.options);
                this._init(n), g.importBpmnDiagram(n, e, t)
            } catch (i) {
                t(i)
            }
        }, a.prototype._init = function (e) {
            i(e, this.__listeners || [])
        }, a.prototype._createDiagram = function (e) {
            var t = [].concat(e.modules || this.getModules(), e.additionalModules || []);
            return t.unshift({
                bpmnjs: ["value", this],
                moddle: ["value", this.moddle]
            }), e = c(e, "additionalModules"), e = s(e, {canvas: {container: this.container}, modules: t}), new f(e)
        }, a.prototype.getModules = function () {
            return this._modules
        }, a.prototype.clear = function () {
            var e = this.diagram;
            e && e.destroy()
        }, a.prototype.destroy = function () {
            this.clear(), h(this.container)
        }, a.prototype.on = function (e, t, n, i) {
            var r = this.diagram, o = this.__listeners = this.__listeners || [];
            o.push({event: e, priority: t, handler: n, that: i}), r && r.get("eventBus").on(e, t, n, i)
        }, a.prototype._modules = [e("./core"), e("diagram-js/lib/features/selection"), e("diagram-js/lib/features/overlays")], t.exports = a
    }, {
        "./core": 24,
        "./import/Importer": 82,
        "bpmn-moddle": 88,
        "diagram-js": 121,
        "diagram-js/lib/features/overlays": 193,
        "diagram-js/lib/features/selection": 211,
        "lodash/lang/isNumber": 390,
        "lodash/lang/isString": 393,
        "lodash/object/assign": 396,
        "lodash/object/omit": 400,
        "min-dom/lib/domify": 413,
        "min-dom/lib/query": 416,
        "min-dom/lib/remove": 417
    }],
    24: [function (e, t, n) {
        t.exports = {__depends__: [e("../draw"), e("../import")]}
    }, {"../draw": 27, "../import": 84}],
    25: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i) {
            function l(e, t) {
                F[e] = t
            }

            function u(e) {
                return F[e]
            }

            function p(e) {
                function t(e, t) {
                    var n = m({
                        fill: "black",
                        strokeWidth: 1,
                        strokeLinecap: "round",
                        strokeDasharray: "none"
                    }, t.attrs), i = t.ref || {x: 0, y: 0}, r = t.scale || 1;
                    "none" === n.strokeDasharray && (n.strokeDasharray = [1e4, 1]);
                    var o = t.element.attr(n).marker(0, 0, 20, 20, i.x, i.y).attr({
                        markerWidth: 20 * r,
                        markerHeight: 20 * r
                    });
                    return l(e, o)
                }

                t("sequenceflow-end", {
                    element: e.path("M 1 5 L 11 10 L 1 15 Z"),
                    ref: {x: 11, y: 10},
                    scale: .5
                }), t("messageflow-start", {
                    element: e.circle(6, 6, 3.5),
                    attrs: {fill: "white", stroke: "black"},
                    ref: {x: 6, y: 6}
                }), t("messageflow-end", {
                    element: e.path("m 1 5 l 0 -3 l 7 3 l -7 3 z"),
                    attrs: {fill: "white", stroke: "black", strokeLinecap: "butt"},
                    ref: {x: 8.5, y: 5}
                }), t("data-association-end", {
                    element: e.path("M 1 5 L 11 10 L 1 15"),
                    attrs: {fill: "white", stroke: "black"},
                    ref: {x: 11, y: 10},
                    scale: .5
                }), t("conditional-flow-marker", {
                    element: e.path("M 0 10 L 8 6 L 16 10 L 8 14 Z"),
                    attrs: {fill: "white", stroke: "black"},
                    ref: {x: -1, y: 10},
                    scale: .5
                }), t("conditional-default-flow-marker", {
                    element: e.path("M 1 4 L 5 16"),
                    attrs: {stroke: "black"},
                    ref: {x: -5, y: 10},
                    scale: .5
                })
            }

            function d(e, t, n, i, r) {
                f(i) && (r = i, i = 0), i = i || 0, r = U(r, {stroke: "black", strokeWidth: 2, fill: "white"});
                var o = t / 2, a = n / 2;
                return e.circle(o, a, Math.round((t + n) / 4 - i)).attr(r)
            }

            function h(e, t, n, i, r, o) {
                return f(r) && (o = r, r = 0), r = r || 0, o = U(o, {
                    stroke: "black",
                    strokeWidth: 2,
                    fill: "white"
                }), e.rect(r, r, t - 2 * r, n - 2 * r, i).attr(o)
            }

            function v(e, t, n, i) {
                var r = t / 2, o = n / 2, a = [r, 0, t, o, r, n, 0, o];
                return i = U(i, {stroke: "black", strokeWidth: 2, fill: "white"}), e.polygon(a).attr(i)
            }

            function b(e, t, n) {
                return n = U(n, ["no-fill"], {stroke: "black", strokeWidth: 2, fill: "none"}), A(t, n).appendTo(e)
            }

            function _(e, t, n) {
                return n = U(n, ["no-fill"], {strokeWidth: 2, stroke: "black"}), e.path(t).attr(n)
            }

            function S(e) {
                return function (t, n) {
                    return z[e](t, n)
                }
            }

            function C(e) {
                return z[e]
            }

            function k(e, t) {
                var n = c(e), i = o(n);
                return r(n, "bpmn:MessageEventDefinition") ? C("bpmn:MessageEventDefinition")(t, e, i) : r(n, "bpmn:TimerEventDefinition") ? C("bpmn:TimerEventDefinition")(t, e, i) : r(n, "bpmn:ConditionalEventDefinition") ? C("bpmn:ConditionalEventDefinition")(t, e) : r(n, "bpmn:SignalEventDefinition") ? C("bpmn:SignalEventDefinition")(t, e, i) : r(n, "bpmn:CancelEventDefinition") && r(n, "bpmn:TerminateEventDefinition", {parallelMultiple: !1}) ? C("bpmn:MultipleEventDefinition")(t, e, i) : r(n, "bpmn:CancelEventDefinition") && r(n, "bpmn:TerminateEventDefinition", {parallelMultiple: !0}) ? C("bpmn:ParallelMultipleEventDefinition")(t, e, i) : r(n, "bpmn:EscalationEventDefinition") ? C("bpmn:EscalationEventDefinition")(t, e, i) : r(n, "bpmn:LinkEventDefinition") ? C("bpmn:LinkEventDefinition")(t, e, i) : r(n, "bpmn:ErrorEventDefinition") ? C("bpmn:ErrorEventDefinition")(t, e, i) : r(n, "bpmn:CancelEventDefinition") ? C("bpmn:CancelEventDefinition")(t, e, i) : r(n, "bpmn:CompensateEventDefinition") ? C("bpmn:CompensateEventDefinition")(t, e, i) : r(n, "bpmn:TerminateEventDefinition") ? C("bpmn:TerminateEventDefinition")(t, e, i) : null
            }

            function R(e, t, n) {
                return I.createText(e, t || "", n).addClass("djs-label")
            }

            function M(e, t, n) {
                var i = c(t);
                return R(e, i.name, {box: t, align: n, padding: 5})
            }

            function D(e, t, n) {
                var i = c(t);
                return R(e, i.name, {box: t, align: n, style: {fontSize: "11px"}})
            }

            function P(e, t, n) {
                var i = R(e, t, {box: {height: 30, width: n.height}, align: "center-middle"}), r = -1 * n.height;
                i.transform("rotate(270) translate(" + r + ",0)")
            }

            function B(e) {
                for (var t = e.waypoints, n = "m  " + t[0].x + "," + t[0].y, i = 1; i < t.length; i++)n += "L" + t[i].x + "," + t[i].y + " ";
                return n
            }

            function L(e, t, n) {
                var i, r = c(t), o = y(n, "SubProcessMarker");
                return i = o ? {seq: -21, parallel: -22, compensation: -42, loop: -18, adhoc: 10} : {
                    seq: -3,
                    parallel: -6,
                    compensation: -27,
                    loop: 0,
                    adhoc: 10
                }, g(n, function (n) {
                    C(n)(e, t, i)
                }), "bpmn:AdHocSubProcess" === r.$type && C("AdhocMarker")(e, t, i), r.loopCharacteristics && void 0 === r.loopCharacteristics.isSequential ? void C("LoopMarker")(e, t, i) : (r.loopCharacteristics && void 0 !== r.loopCharacteristics.isSequential && !r.loopCharacteristics.isSequential && C("ParallelMarker")(e, t, i), r.loopCharacteristics && r.loopCharacteristics.isSequential && C("SequentialMarker")(e, t, i), void(r.isForCompensation && C("CompensationMarker")(e, t, i)))
            }

            function O(e, t) {
                var i = (t.height - 16) / t.height, r = n.getScaledPath("DATA_OBJECT_COLLECTION_PATH", {
                    xScaleFactor: 1,
                    yScaleFactor: 1,
                    containerWidth: t.width,
                    containerHeight: t.height,
                    position: {mx: .451, my: i}
                });
                _(e, r, {strokeWidth: 2})
            }

            x.call(this, e, i);
            var I = new E({style: N, size: {width: 100}}), F = {}, U = t.computeStyle, z = this.handlers = {
                "bpmn:Event": function (e, t, n) {
                    return d(e, t.width, t.height, n)
                },
                "bpmn:StartEvent": function (e, t) {
                    var n = {}, i = c(t);
                    i.isInterrupting || (n = {strokeDasharray: "6", strokeLinecap: "round"});
                    var r = C("bpmn:Event")(e, t, n);
                    return k(t, e), r
                },
                "bpmn:MessageEventDefinition": function (e, t, i) {
                    var r = n.getScaledPath("EVENT_MESSAGE", {
                        xScaleFactor: .9,
                        yScaleFactor: .9,
                        containerWidth: t.width,
                        containerHeight: t.height,
                        position: {mx: .235, my: .315}
                    }), o = i ? "black" : "white", a = i ? "white" : "black", s = _(e, r, {
                        strokeWidth: 1,
                        fill: o,
                        stroke: a
                    });
                    return s
                },
                "bpmn:TimerEventDefinition": function (e, t) {
                    var i = d(e, t.width, t.height, .2 * t.height, {strokeWidth: 2}), r = n.getScaledPath("EVENT_TIMER_WH", {
                        xScaleFactor: .75,
                        yScaleFactor: .75,
                        containerWidth: t.width,
                        containerHeight: t.height,
                        position: {mx: .5, my: .5}
                    });
                    _(e, r, {strokeWidth: 2, strokeLinecap: "square"});
                    for (var o = 0; 12 > o; o++) {
                        var a = n.getScaledPath("EVENT_TIMER_LINE", {
                            xScaleFactor: .75,
                            yScaleFactor: .75,
                            containerWidth: t.width,
                            containerHeight: t.height,
                            position: {mx: .5, my: .5}
                        }), s = t.width / 2, c = t.height / 2;
                        _(e, a, {
                            strokeWidth: 1,
                            strokeLinecap: "square",
                            transform: "rotate(" + 30 * o + "," + c + "," + s + ")"
                        })
                    }
                    return i
                },
                "bpmn:EscalationEventDefinition": function (e, t, i) {
                    var r = n.getScaledPath("EVENT_ESCALATION", {
                        xScaleFactor: 1,
                        yScaleFactor: 1,
                        containerWidth: t.width,
                        containerHeight: t.height,
                        position: {mx: .5, my: .555}
                    }), o = i ? "black" : "none";
                    return _(e, r, {strokeWidth: 1, fill: o})
                },
                "bpmn:ConditionalEventDefinition": function (e, t) {
                    var i = n.getScaledPath("EVENT_CONDITIONAL", {
                        xScaleFactor: 1,
                        yScaleFactor: 1,
                        containerWidth: t.width,
                        containerHeight: t.height,
                        position: {mx: .5, my: .222}
                    });
                    return _(e, i, {strokeWidth: 1})
                },
                "bpmn:LinkEventDefinition": function (e, t, i) {
                    var r = n.getScaledPath("EVENT_LINK", {
                        xScaleFactor: 1,
                        yScaleFactor: 1,
                        containerWidth: t.width,
                        containerHeight: t.height,
                        position: {mx: .57, my: .263}
                    }), o = i ? "black" : "none";
                    return _(e, r, {strokeWidth: 1, fill: o})
                },
                "bpmn:ErrorEventDefinition": function (e, t, i) {
                    var r = n.getScaledPath("EVENT_ERROR", {
                        xScaleFactor: 1.1,
                        yScaleFactor: 1.1,
                        containerWidth: t.width,
                        containerHeight: t.height,
                        position: {mx: .2, my: .722}
                    }), o = i ? "black" : "none";
                    return _(e, r, {strokeWidth: 1, fill: o})
                },
                "bpmn:CancelEventDefinition": function (e, t, i) {
                    var r = n.getScaledPath("EVENT_CANCEL_45", {
                        xScaleFactor: 1,
                        yScaleFactor: 1,
                        containerWidth: t.width,
                        containerHeight: t.height,
                        position: {mx: .638, my: -.055}
                    }), o = i ? "black" : "none";
                    return _(e, r, {strokeWidth: 1, fill: o}).transform("rotate(45)")
                },
                "bpmn:CompensateEventDefinition": function (e, t, i) {
                    var r = n.getScaledPath("EVENT_COMPENSATION", {
                        xScaleFactor: 1,
                        yScaleFactor: 1,
                        containerWidth: t.width,
                        containerHeight: t.height,
                        position: {mx: .201, my: .472}
                    }), o = i ? "black" : "none";
                    return _(e, r, {strokeWidth: 1, fill: o})
                },
                "bpmn:SignalEventDefinition": function (e, t, i) {
                    var r = n.getScaledPath("EVENT_SIGNAL", {
                        xScaleFactor: .9,
                        yScaleFactor: .9,
                        containerWidth: t.width,
                        containerHeight: t.height,
                        position: {mx: .5, my: .2}
                    }), o = i ? "black" : "none";
                    return _(e, r, {strokeWidth: 1, fill: o})
                },
                "bpmn:MultipleEventDefinition": function (e, t, i) {
                    var r = n.getScaledPath("EVENT_MULTIPLE", {
                        xScaleFactor: 1.1,
                        yScaleFactor: 1.1,
                        containerWidth: t.width,
                        containerHeight: t.height,
                        position: {mx: .222, my: .36}
                    }), o = i ? "black" : "none";
                    return _(e, r, {strokeWidth: 1, fill: o})
                },
                "bpmn:ParallelMultipleEventDefinition": function (e, t) {
                    var i = n.getScaledPath("EVENT_PARALLEL_MULTIPLE", {
                        xScaleFactor: 1.2,
                        yScaleFactor: 1.2,
                        containerWidth: t.width,
                        containerHeight: t.height,
                        position: {mx: .458, my: .194}
                    });
                    return _(e, i, {strokeWidth: 1})
                },
                "bpmn:EndEvent": function (e, t) {
                    var n = C("bpmn:Event")(e, t, {strokeWidth: 4});
                    return k(t, e, !0), n
                },
                "bpmn:TerminateEventDefinition": function (e, t) {
                    var n = d(e, t.width, t.height, 8, {strokeWidth: 4, fill: "black"});
                    return n
                },
                "bpmn:IntermediateEvent": function (e, t) {
                    var n = C("bpmn:Event")(e, t, {strokeWidth: 1});
                    return d(e, t.width, t.height, j, {strokeWidth: 1, fill: "none"}), k(t, e), n
                },
                "bpmn:IntermediateCatchEvent": S("bpmn:IntermediateEvent"),
                "bpmn:IntermediateThrowEvent": S("bpmn:IntermediateEvent"),
                "bpmn:Activity": function (e, t, n) {
                    return h(e, t.width, t.height, T, n)
                },
                "bpmn:Task": function (e, t, n) {
                    var i = C("bpmn:Activity")(e, t, n);
                    return M(e, t, "center-middle"), L(e, t), i
                },
                "bpmn:ServiceTask": function (e, t) {
                    var i = C("bpmn:Task")(e, t), r = n.getScaledPath("TASK_TYPE_SERVICE", {abspos: {x: 12, y: 18}});
                    _(e, r, {strokeWidth: 1, fill: "none"});
                    var o = n.getScaledPath("TASK_TYPE_SERVICE_FILL", {abspos: {x: 17.2, y: 18}});
                    _(e, o, {strokeWidth: 0, stroke: "none", fill: "white"});
                    var a = n.getScaledPath("TASK_TYPE_SERVICE", {abspos: {x: 17, y: 22}});
                    return _(e, a, {strokeWidth: 1, fill: "white"}), i
                },
                "bpmn:UserTask": function (e, t) {
                    var i = C("bpmn:Task")(e, t), r = 15, o = 12, a = n.getScaledPath("TASK_TYPE_USER_1", {
                        abspos: {
                            x: r,
                            y: o
                        }
                    });
                    _(e, a, {strokeWidth: .5, fill: "none"});
                    var s = n.getScaledPath("TASK_TYPE_USER_2", {abspos: {x: r, y: o}});
                    _(e, s, {strokeWidth: .5, fill: "none"});
                    var c = n.getScaledPath("TASK_TYPE_USER_3", {abspos: {x: r, y: o}});
                    return _(e, c, {strokeWidth: .5, fill: "black"}), i
                },
                "bpmn:ManualTask": function (e, t) {
                    var i = C("bpmn:Task")(e, t), r = n.getScaledPath("TASK_TYPE_MANUAL", {abspos: {x: 17, y: 15}});
                    return _(e, r, {strokeWidth: .25, fill: "white", stroke: "black"}), i
                },
                "bpmn:SendTask": function (e, t) {
                    var i = C("bpmn:Task")(e, t), r = n.getScaledPath("TASK_TYPE_SEND", {
                        xScaleFactor: 1,
                        yScaleFactor: 1,
                        containerWidth: 21,
                        containerHeight: 14,
                        position: {mx: .285, my: .357}
                    });
                    return _(e, r, {strokeWidth: 1, fill: "black", stroke: "white"}), i
                },
                "bpmn:ReceiveTask": function (e, t) {
                    var i, r = c(t), o = C("bpmn:Task")(e, t);
                    return r.instantiate ? (d(e, 28, 28, 4.4, {
                        strokeWidth: 1
                    }), i = n.getScaledPath("TASK_TYPE_INSTANTIATING_SEND", {
                        abspos: {
                            x: 7.77,
                            y: 9.52
                        }
                    })) : i = n.getScaledPath("TASK_TYPE_SEND", {
                        xScaleFactor: .9,
                        yScaleFactor: .9,
                        containerWidth: 21,
                        containerHeight: 14,
                        position: {mx: .3, my: .4}
                    }), _(e, i, {strokeWidth: 1}), o
                },
                "bpmn:ScriptTask": function (e, t) {
                    var i = C("bpmn:Task")(e, t), r = n.getScaledPath("TASK_TYPE_SCRIPT", {abspos: {x: 15, y: 20}});
                    return _(e, r, {strokeWidth: 1}), i
                },
                "bpmn:BusinessRuleTask": function (e, t) {
                    var i = C("bpmn:Task")(e, t), r = n.getScaledPath("TASK_TYPE_BUSINESS_RULE_HEADER", {
                        abspos: {
                            x: 8,
                            y: 8
                        }
                    }), o = _(e, r);
                    o.attr({strokeWidth: 1, fill: "AAA"});
                    var a = n.getScaledPath("TASK_TYPE_BUSINESS_RULE_MAIN", {abspos: {x: 8, y: 8}}), s = _(e, a);
                    return s.attr({strokeWidth: 1}), i
                },
                "bpmn:SubProcess": function (e, t, n) {
                    var i = C("bpmn:Activity")(e, t, n), r = w.isExpanded(t), o = w.isEventSubProcess(t);
                    return o && i.attr({strokeDasharray: "1,2"}), M(e, t, r ? "center-top" : "center-middle"), r ? L(e, t) : L(e, t, ["SubProcessMarker"]), i
                },
                "bpmn:AdHocSubProcess": function (e, t) {
                    return C("bpmn:SubProcess")(e, t)
                },
                "bpmn:Transaction": function (e, n) {
                    var i = C("bpmn:SubProcess")(e, n), r = t.style(["no-fill", "no-events"]);
                    return h(e, n.width, n.height, T - 2, j, r), i
                },
                "bpmn:CallActivity": function (e, t) {
                    return C("bpmn:Task")(e, t, {strokeWidth: 5})
                },
                "bpmn:Participant": function (e, t) {
                    var n = C("bpmn:Lane")(e, t, {fill: "White"}), i = w.isExpanded(t);
                    if (i) {
                        b(e, [{x: 30, y: 0}, {x: 30, y: t.height}]);
                        var r = c(t).name;
                        P(e, r, t)
                    } else {
                        var o = c(t).name;
                        R(e, o, {box: t, align: "center-middle"})
                    }
                    var a = !!c(t).participantMultiplicity;
                    return a && C("ParticipantMultiplicityMarker")(e, t), n
                },
                "bpmn:Lane": function (e, t, n) {
                    var i = h(e, t.width, t.height, 0, n || {fill: "none"}), r = c(t);
                    if ("bpmn:Lane" === r.$type) {
                        var o = r.name;
                        P(e, o, t)
                    }
                    return i
                },
                "bpmn:InclusiveGateway": function (e, t) {
                    var n = v(e, t.width, t.height);
                    return d(e, t.width, t.height, .24 * t.height, {strokeWidth: 2.5, fill: "none"}), n
                },
                "bpmn:ExclusiveGateway": function (e, t) {
                    var i = v(e, t.width, t.height), r = n.getScaledPath("GATEWAY_EXCLUSIVE", {
                        xScaleFactor: .4,
                        yScaleFactor: .4,
                        containerWidth: t.width,
                        containerHeight: t.height,
                        position: {mx: .32, my: .3}
                    });
                    return s(t).isMarkerVisible && _(e, r, {strokeWidth: 1, fill: "black"}), i
                },
                "bpmn:ComplexGateway": function (e, t) {
                    var i = v(e, t.width, t.height), r = n.getScaledPath("GATEWAY_COMPLEX", {
                        xScaleFactor: .5,
                        yScaleFactor: .5,
                        containerWidth: t.width,
                        containerHeight: t.height,
                        position: {mx: .46, my: .26}
                    });
                    return _(e, r, {strokeWidth: 1, fill: "black"}), i
                },
                "bpmn:ParallelGateway": function (e, t) {
                    var i = v(e, t.width, t.height), r = n.getScaledPath("GATEWAY_PARALLEL", {
                        xScaleFactor: .6,
                        yScaleFactor: .6,
                        containerWidth: t.width,
                        containerHeight: t.height,
                        position: {mx: .46, my: .2}
                    });
                    return _(e, r, {strokeWidth: 1, fill: "black"}), i
                },
                "bpmn:EventBasedGateway": function (e, t) {
                    function i() {
                        var i = n.getScaledPath("GATEWAY_EVENT_BASED", {
                            xScaleFactor: .18,
                            yScaleFactor: .18,
                            containerWidth: t.width,
                            containerHeight: t.height,
                            position: {mx: .36, my: .44}
                        });
                        _(e, i, {strokeWidth: 2, fill: "none"})
                    }

                    var r = c(t), o = v(e, t.width, t.height);
                    d(e, t.width, t.height, .2 * t.height, {strokeWidth: 1, fill: "none"});
                    var a = r.eventGatewayType, s = !!r.instantiate;
                    if ("Parallel" === a) {
                        var l = n.getScaledPath("GATEWAY_PARALLEL", {
                            xScaleFactor: .4,
                            yScaleFactor: .4,
                            containerWidth: t.width,
                            containerHeight: t.height,
                            position: {mx: .474, my: .296}
                        }), u = _(e, l);
                        u.attr({strokeWidth: 1, fill: "none"})
                    } else if ("Exclusive" === a) {
                        if (!s) {
                            var p = d(e, t.width, t.height, .26 * t.height);
                            p.attr({strokeWidth: 1, fill: "none"})
                        }
                        i()
                    }
                    return o
                },
                "bpmn:Gateway": function (e, t) {
                    return v(e, t.width, t.height)
                },
                "bpmn:SequenceFlow": function (e, t) {
                    var n = B(t), i = _(e, n, {
                        strokeLinejoin: "round",
                        markerEnd: u("sequenceflow-end")
                    }), r = c(t), o = t.source.businessObject;
                    return r.conditionExpression && o.$instanceOf("bpmn:Activity") && i.attr({markerStart: u("conditional-flow-marker")}), o["default"] && o.$instanceOf("bpmn:Gateway") && o["default"] === r && i.attr({markerStart: u("conditional-default-flow-marker")}), i
                },
                "bpmn:Association": function (e, t, n) {
                    return n = m({
                        strokeDasharray: "1,6",
                        strokeLinecap: "round",
                        strokeLinejoin: "round"
                    }, n || {}), b(e, t.waypoints, n)
                },
                "bpmn:DataInputAssociation": function (e, t) {
                    return C("bpmn:Association")(e, t, {markerEnd: u("data-association-end")})
                },
                "bpmn:DataOutputAssociation": function (e, t) {
                    return C("bpmn:Association")(e, t, {markerEnd: u("data-association-end")})
                },
                "bpmn:MessageFlow": function (e, t) {
                    var i = c(t), r = s(t), o = B(t), a = _(e, o, {
                        markerEnd: u("messageflow-end"),
                        markerStart: u("messageflow-start"),
                        strokeDasharray: "10, 12",
                        strokeLinecap: "round",
                        strokeLinejoin: "round",
                        strokeWidth: "1.5px"
                    });
                    if (i.messageRef) {
                        var l = a.getPointAtLength(a.getTotalLength() / 2), p = n.getScaledPath("MESSAGE_FLOW_MARKER", {
                            abspos: {
                                x: l.x,
                                y: l.y
                            }
                        }), d = {strokeWidth: 1};
                        "initiating" === r.messageVisibleKind ? (d.fill = "white", d.stroke = "black") : (d.fill = "#888", d.stroke = "white"), _(e, p, d)
                    }
                    return a
                },
                "bpmn:DataObject": function (e, t) {
                    var i = n.getScaledPath("DATA_OBJECT_PATH", {
                        xScaleFactor: 1,
                        yScaleFactor: 1,
                        containerWidth: t.width,
                        containerHeight: t.height,
                        position: {mx: .474, my: .296}
                    }), r = _(e, i, {fill: "white"}), o = c(t);
                    return a(o) && O(e, t), r
                },
                "bpmn:DataObjectReference": S("bpmn:DataObject"),
                "bpmn:DataInput": function (e, t) {
                    var i = n.getRawPath("DATA_ARROW"), r = C("bpmn:DataObject")(e, t);
                    return _(e, i, {strokeWidth: 1}), r
                },
                "bpmn:DataOutput": function (e, t) {
                    var i = n.getRawPath("DATA_ARROW"), r = C("bpmn:DataObject")(e, t);
                    return _(e, i, {strokeWidth: 1, fill: "black"}), r
                },
                "bpmn:DataStoreReference": function (e, t) {
                    var i = n.getScaledPath("DATA_STORE", {
                        xScaleFactor: 1,
                        yScaleFactor: 1,
                        containerWidth: t.width,
                        containerHeight: t.height,
                        position: {mx: 0, my: .133}
                    }), r = _(e, i, {strokeWidth: 2, fill: "white"});
                    return r
                },
                "bpmn:BoundaryEvent": function (e, t) {
                    var n = c(t), i = n.cancelActivity, r = {strokeWidth: 1};
                    i || (r.strokeDasharray = "6", r.strokeLinecap = "round");
                    var o = C("bpmn:Event")(e, t, r);
                    return d(e, t.width, t.height, j, m(r, {fill: "none"})), k(t, e), o
                },
                "bpmn:Group": function (e, t) {
                    return h(e, t.width, t.height, T, {
                        strokeWidth: 1,
                        strokeDasharray: "8,3,1,3",
                        fill: "none",
                        pointerEvents: "none"
                    })
                },
                label: function (e, t) {
                    return D(e, t, "")
                },
                "bpmn:TextAnnotation": function (e, t) {
                    var i = {
                        fill: "none",
                        stroke: "none"
                    }, r = h(e, t.width, t.height, 0, 0, i), o = n.getScaledPath("TEXT_ANNOTATION", {
                        xScaleFactor: 1,
                        yScaleFactor: 1,
                        containerWidth: t.width,
                        containerHeight: t.height,
                        position: {mx: 0, my: 0}
                    });
                    _(e, o);
                    var a = c(t).text || "";
                    return R(e, a, {box: t, align: "left-middle", padding: 5}), r
                },
                ParticipantMultiplicityMarker: function (e, t) {
                    var i = n.getScaledPath("MARKER_PARALLEL", {
                        xScaleFactor: 1,
                        yScaleFactor: 1,
                        containerWidth: t.width,
                        containerHeight: t.height,
                        position: {mx: t.width / 2 / t.width, my: (t.height - 15) / t.height}
                    });
                    _(e, i)
                },
                SubProcessMarker: function (e, t) {
                    var i = h(e, 14, 14, 0, {strokeWidth: 1});
                    i.transform("translate(" + (t.width / 2 - 7.5) + "," + (t.height - 20) + ")");
                    var r = n.getScaledPath("MARKER_SUB_PROCESS", {
                        xScaleFactor: 1.5,
                        yScaleFactor: 1.5,
                        containerWidth: t.width,
                        containerHeight: t.height,
                        position: {mx: (t.width / 2 - 7.5) / t.width, my: (t.height - 20) / t.height}
                    });
                    _(e, r)
                },
                ParallelMarker: function (e, t, i) {
                    var r = n.getScaledPath("MARKER_PARALLEL", {
                        xScaleFactor: 1,
                        yScaleFactor: 1,
                        containerWidth: t.width,
                        containerHeight: t.height,
                        position: {mx: (t.width / 2 + i.parallel) / t.width, my: (t.height - 20) / t.height}
                    });
                    _(e, r)
                },
                SequentialMarker: function (e, t, i) {
                    var r = n.getScaledPath("MARKER_SEQUENTIAL", {
                        xScaleFactor: 1,
                        yScaleFactor: 1,
                        containerWidth: t.width,
                        containerHeight: t.height,
                        position: {mx: (t.width / 2 + i.seq) / t.width, my: (t.height - 19) / t.height}
                    });
                    _(e, r)
                },
                CompensationMarker: function (e, t, i) {
                    var r = n.getScaledPath("MARKER_COMPENSATION", {
                        xScaleFactor: 1,
                        yScaleFactor: 1,
                        containerWidth: t.width,
                        containerHeight: t.height,
                        position: {mx: (t.width / 2 + i.compensation) / t.width, my: (t.height - 13) / t.height}
                    });
                    _(e, r, {strokeWidth: 1})
                },
                LoopMarker: function (e, t, i) {
                    var r = n.getScaledPath("MARKER_LOOP", {
                        xScaleFactor: 1,
                        yScaleFactor: 1,
                        containerWidth: t.width,
                        containerHeight: t.height,
                        position: {mx: (t.width / 2 + i.loop) / t.width, my: (t.height - 7) / t.height}
                    });
                    _(e, r, {strokeWidth: 1, fill: "none", strokeLinecap: "round", strokeMiterlimit: .5})
                },
                AdhocMarker: function (e, t, i) {
                    var r = n.getScaledPath("MARKER_ADHOC", {
                        xScaleFactor: 1,
                        yScaleFactor: 1,
                        containerWidth: t.width,
                        containerHeight: t.height,
                        position: {mx: (t.width / 2 + i.adhoc) / t.width, my: (t.height - 15) / t.height}
                    });
                    _(e, r, {strokeWidth: 1, fill: "black"})
                }
            };
            e.on("canvas.init", function (e) {
                p(e.svg)
            })
        }

        function r(e, t, n) {
            function i(e, t) {
                return v(t, function (t, n) {
                    return e[n] == t
                })
            }

            return b(e.eventDefinitions, function (r) {
                return r.$type === t && i(e, n)
            })
        }

        function o(e) {
            return "bpmn:IntermediateThrowEvent" === e.$type || "bpmn:EndEvent" === e.$type
        }

        function a(e) {
            return e.isCollection || e.elementObjectRef && e.elementObjectRef.isCollection
        }

        function s(e) {
            return e.businessObject.di
        }

        function c(e) {
            return e.businessObject
        }

        function l(e) {
            var t = e.x + e.width / 2, n = e.y + e.height / 2, i = e.width / 2, r = [["M", t, n], ["m", 0, -i], ["a", i, i, 0, 1, 1, 0, 2 * i], ["a", i, i, 0, 1, 1, 0, -2 * i], ["z"]];
            return C(r)
        }

        function u(e, t) {
            var n = e.x, i = e.y, r = e.width, o = e.height, a = [["M", n + t, i], ["l", r - 2 * t, 0], ["a", t, t, 0, 0, 1, t, t], ["l", 0, o - 2 * t], ["a", t, t, 0, 0, 1, -t, t], ["l", 2 * t - r, 0], ["a", t, t, 0, 0, 1, -t, -t], ["l", 0, 2 * t - o], ["a", t, t, 0, 0, 1, t, -t], ["z"]];
            return C(a)
        }

        function p(e) {
            var t = e.width, n = e.height, i = e.x, r = e.y, o = t / 2, a = n / 2, s = [["M", i + o, r], ["l", o, a], ["l", -o, a], ["l", -o, -a], ["z"]];
            return C(s)
        }

        function d(e) {
            var t = e.x, n = e.y, i = e.width, r = e.height, o = [["M", t, n], ["l", i, 0], ["l", 0, r], ["l", -i, 0], ["z"]];
            return C(o)
        }

        var h = e("inherits"), f = e("lodash/lang/isObject"), m = e("lodash/object/assign"), g = e("lodash/collection/forEach"), v = e("lodash/collection/every"), y = e("lodash/collection/includes"), b = e("lodash/collection/some"), x = e("diagram-js/lib/draw/BaseRenderer"), E = e("diagram-js/lib/util/Text"), w = e("../util/DiUtil"), _ = e("../util/ModelUtil").is, S = e("diagram-js/lib/util/RenderUtil"), C = S.componentsToPath, A = S.createLine, T = 10, j = 3, N = {
            fontFamily: "Arial, sans-serif",
            fontSize: "12px"
        };
        h(i, x), i.$inject = ["eventBus", "styles", "pathMap"], t.exports = i, i.prototype.canRender = function (e) {
            return _(e, "bpmn:BaseElement")
        }, i.prototype.drawShape = function (e, t) {
            var n = t.type, i = this.handlers[n];
            return i(e, t)
        }, i.prototype.drawConnection = function (e, t) {
            var n = t.type, i = this.handlers[n];
            return i(e, t)
        }, i.prototype.getShapePath = function (e) {
            return _(e, "bpmn:Event") ? l(e) : _(e, "bpmn:Activity") ? u(e, T) : _(e, "bpmn:Gateway") ? p(e) : d(e)
        }
    }, {
        "../util/DiUtil": 85,
        "../util/ModelUtil": 87,
        "diagram-js/lib/draw/BaseRenderer": 132,
        "diagram-js/lib/util/RenderUtil": 249,
        "diagram-js/lib/util/Text": 250,
        inherits: 115,
        "lodash/collection/every": 271,
        "lodash/collection/forEach": 274,
        "lodash/collection/includes": 276,
        "lodash/collection/some": 280,
        "lodash/lang/isObject": 391,
        "lodash/object/assign": 396
    }],
    26: [function (e, t, n) {
        "use strict";
        function i() {
            this.pathMap = {
                EVENT_MESSAGE: {
                    d: "m {mx},{my} l 0,{e.y1} l {e.x1},0 l 0,-{e.y1} z l {e.x0},{e.y0} l {e.x0},-{e.y0}",
                    height: 36,
                    width: 36,
                    heightElements: [6, 14],
                    widthElements: [10.5, 21]
                },
                EVENT_SIGNAL: {
                    d: "M {mx},{my} l {e.x0},{e.y0} l -{e.x1},0 Z",
                    height: 36,
                    width: 36,
                    heightElements: [18],
                    widthElements: [10, 20]
                },
                EVENT_ESCALATION: {
                    d: "m {mx},{my} c -{e.x1},{e.y0} -{e.x3},{e.y1} -{e.x5},{e.y4} {e.x1},-{e.y3} {e.x3},-{e.y5} {e.x5},-{e.y6} {e.x0},{e.y3} {e.x2},{e.y5} {e.x4},{e.y6} -{e.x0},-{e.y0} -{e.x2},-{e.y1} -{e.x4},-{e.y4} z",
                    height: 36,
                    width: 36,
                    heightElements: [2.382, 4.764, 4.926, 6.589333, 7.146, 13.178667, 19.768],
                    widthElements: [2.463, 2.808, 4.926, 5.616, 7.389, 8.424]
                },
                EVENT_CONDITIONAL: {
                    d: "M {e.x0},{e.y0} l {e.x1},0 l 0,{e.y2} l -{e.x1},0 Z M {e.x2},{e.y3} l {e.x0},0 M {e.x2},{e.y4} l {e.x0},0 M {e.x2},{e.y5} l {e.x0},0 M {e.x2},{e.y6} l {e.x0},0 M {e.x2},{e.y7} l {e.x0},0 M {e.x2},{e.y8} l {e.x0},0 ",
                    height: 36,
                    width: 36,
                    heightElements: [8.5, 14.5, 18, 11.5, 14.5, 17.5, 20.5, 23.5, 26.5],
                    widthElements: [10.5, 14.5, 12.5]
                },
                EVENT_LINK: {
                    d: "m {mx},{my} 0,{e.y0} -{e.x1},0 0,{e.y1} {e.x1},0 0,{e.y0} {e.x0},-{e.y2} -{e.x0},-{e.y2} z",
                    height: 36,
                    width: 36,
                    heightElements: [4.4375, 6.75, 7.8125],
                    widthElements: [9.84375, 13.5]
                },
                EVENT_ERROR: {
                    d: "m {mx},{my} {e.x0},-{e.y0} {e.x1},-{e.y1} {e.x2},{e.y2} {e.x3},-{e.y3} -{e.x4},{e.y4} -{e.x5},-{e.y5} z",
                    height: 36,
                    width: 36,
                    heightElements: [.023, 8.737, 8.151, 16.564, 10.591, 8.714],
                    widthElements: [.085, 6.672, 6.97, 4.273, 5.337, 6.636]
                },
                EVENT_CANCEL_45: {
                    d: "m {mx},{my} -{e.x1},0 0,{e.x0} {e.x1},0 0,{e.y1} {e.x0},0 0,-{e.y1} {e.x1},0 0,-{e.y0} -{e.x1},0 0,-{e.y1} -{e.x0},0 z",
                    height: 36,
                    width: 36,
                    heightElements: [4.75, 8.5],
                    widthElements: [4.75, 8.5]
                },
                EVENT_COMPENSATION: {
                    d: "m {mx},{my} {e.x0},-{e.y0} 0,{e.y1} z m {e.x0},0 {e.x0},-{e.y0} 0,{e.y1} z",
                    height: 36,
                    width: 36,
                    heightElements: [5, 10],
                    widthElements: [10]
                },
                EVENT_TIMER_WH: {
                    d: "M {mx},{my} l {e.x0},-{e.y0} m -{e.x0},{e.y0} l {e.x1},{e.y1} ",
                    height: 36,
                    width: 36,
                    heightElements: [10, 2],
                    widthElements: [3, 7]
                },
                EVENT_TIMER_LINE: {
                    d: "M {mx},{my} m {e.x0},{e.y0} l -{e.x1},{e.y1} ",
                    height: 36,
                    width: 36,
                    heightElements: [10, 3],
                    widthElements: [0, 0]
                },
                EVENT_MULTIPLE: {
                    d: "m {mx},{my} {e.x1},-{e.y0} {e.x1},{e.y0} -{e.x0},{e.y1} -{e.x2},0 z",
                    height: 36,
                    width: 36,
                    heightElements: [6.28099, 12.56199],
                    widthElements: [3.1405, 9.42149, 12.56198]
                },
                EVENT_PARALLEL_MULTIPLE: {
                    d: "m {mx},{my} {e.x0},0 0,{e.y1} {e.x1},0 0,{e.y0} -{e.x1},0 0,{e.y1} -{e.x0},0 0,-{e.y1} -{e.x1},0 0,-{e.y0} {e.x1},0 z",
                    height: 36,
                    width: 36,
                    heightElements: [2.56228, 7.68683],
                    widthElements: [2.56228, 7.68683]
                },
                GATEWAY_EXCLUSIVE: {
                    d: "m {mx},{my} {e.x0},{e.y0} {e.x1},{e.y0} {e.x2},0 {e.x4},{e.y2} {e.x4},{e.y1} {e.x2},0 {e.x1},{e.y3} {e.x0},{e.y3} {e.x3},0 {e.x5},{e.y1} {e.x5},{e.y2} {e.x3},0 z",
                    height: 17.5,
                    width: 17.5,
                    heightElements: [8.5, 6.5312, -6.5312, -8.5],
                    widthElements: [6.5, -6.5, 3, -3, 5, -5]
                },
                GATEWAY_PARALLEL: {
                    d: "m {mx},{my} 0,{e.y1} -{e.x1},0 0,{e.y0} {e.x1},0 0,{e.y1} {e.x0},0 0,-{e.y1} {e.x1},0 0,-{e.y0} -{e.x1},0 0,-{e.y1} -{e.x0},0 z",
                    height: 30,
                    width: 30,
                    heightElements: [5, 12.5],
                    widthElements: [5, 12.5]
                },
                GATEWAY_EVENT_BASED: {
                    d: "m {mx},{my} {e.x0},{e.y0} {e.x0},{e.y1} {e.x1},{e.y2} {e.x2},0 z",
                    height: 11,
                    width: 11,
                    heightElements: [-6, 6, 12, -12],
                    widthElements: [9, -3, -12]
                },
                GATEWAY_COMPLEX: {
                    d: "m {mx},{my} 0,{e.y0} -{e.x0},-{e.y1} -{e.x1},{e.y2} {e.x0},{e.y1} -{e.x2},0 0,{e.y3} {e.x2},0  -{e.x0},{e.y1} l {e.x1},{e.y2} {e.x0},-{e.y1} 0,{e.y0} {e.x3},0 0,-{e.y0} {e.x0},{e.y1} {e.x1},-{e.y2} -{e.x0},-{e.y1} {e.x2},0 0,-{e.y3} -{e.x2},0 {e.x0},-{e.y1} -{e.x1},-{e.y2} -{e.x0},{e.y1} 0,-{e.y0} -{e.x3},0 z",
                    height: 17.125,
                    width: 17.125,
                    heightElements: [4.875, 3.4375, 2.125, 3],
                    widthElements: [3.4375, 2.125, 4.875, 3]
                },
                DATA_OBJECT_PATH: {
                    d: "m 0,0 {e.x1},0 {e.x0},{e.y0} 0,{e.y1} -{e.x2},0 0,-{e.y2} {e.x1},0 0,{e.y0} {e.x0},0",
                    height: 61,
                    width: 51,
                    heightElements: [10, 50, 60],
                    widthElements: [10, 40, 50, 60]
                },
                DATA_OBJECT_COLLECTION_PATH: {
                    d: "m {mx}, {my} m  0 15  l 0 -15 m  4 15  l 0 -15 m  4 15  l 0 -15 ",
                    height: 61,
                    width: 51,
                    heightElements: [12],
                    widthElements: [1, 6, 12, 15]
                },
                DATA_ARROW: {
                    d: "m 5,9 9,0 0,-3 5,5 -5,5 0,-3 -9,0 z",
                    height: 61,
                    width: 51,
                    heightElements: [],
                    widthElements: []
                },
                DATA_STORE: {
                    d: "m  {mx},{my} l  0,{e.y2} c  {e.x0},{e.y1} {e.x1},{e.y1}  {e.x2},0 l  0,-{e.y2} c -{e.x0},-{e.y1} -{e.x1},-{e.y1} -{e.x2},0c  {e.x0},{e.y1} {e.x1},{e.y1}  {e.x2},0 m  -{e.x2},{e.y0}c  {e.x0},{e.y1} {e.x1},{e.y1} {e.x2},0m  -{e.x2},{e.y0}c  {e.x0},{e.y1} {e.x1},{e.y1}  {e.x2},0",
                    height: 61,
                    width: 61,
                    heightElements: [7, 10, 45],
                    widthElements: [2, 58, 60]
                },
                TEXT_ANNOTATION: {
                    d: "m {mx}, {my} m 10,0 l -10,0 l 0,{e.y0} l 10,0",
                    height: 30,
                    width: 10,
                    heightElements: [30],
                    widthElements: [10]
                },
                MARKER_SUB_PROCESS: {
                    d: "m{mx},{my} m 7,2 l 0,10 m -5,-5 l 10,0",
                    height: 10,
                    width: 10,
                    heightElements: [],
                    widthElements: []
                },
                MARKER_PARALLEL: {
                    d: "m{mx},{my} m 3,2 l 0,10 m 3,-10 l 0,10 m 3,-10 l 0,10",
                    height: 10,
                    width: 10,
                    heightElements: [],
                    widthElements: []
                },
                MARKER_SEQUENTIAL: {
                    d: "m{mx},{my} m 0,3 l 10,0 m -10,3 l 10,0 m -10,3 l 10,0",
                    height: 10,
                    width: 10,
                    heightElements: [],
                    widthElements: []
                },
                MARKER_COMPENSATION: {
                    d: "m {mx},{my} 8,-5 0,10 z m 9,0 8,-5 0,10 z",
                    height: 10,
                    width: 21,
                    heightElements: [],
                    widthElements: []
                },
                MARKER_LOOP: {
                    d: "m {mx},{my} c 3.526979,0 6.386161,-2.829858 6.386161,-6.320661 0,-3.490806 -2.859182,-6.320661 -6.386161,-6.320661 -3.526978,0 -6.38616,2.829855 -6.38616,6.320661 0,1.745402 0.714797,3.325567 1.870463,4.469381 0.577834,0.571908 1.265885,1.034728 2.029916,1.35457 l -0.718163,-3.909793 m 0.718163,3.909793 -3.885211,0.802902",
                    height: 13.9,
                    width: 13.7,
                    heightElements: [],
                    widthElements: []
                },
                MARKER_ADHOC: {
                    d: "m {mx},{my} m 0.84461,2.64411 c 1.05533,-1.23780996 2.64337,-2.07882 4.29653,-1.97997996 2.05163,0.0805 3.85579,1.15803 5.76082,1.79107 1.06385,0.34139996 2.24454,0.1438 3.18759,-0.43767 0.61743,-0.33642 1.2775,-0.64078 1.7542,-1.17511 0,0.56023 0,1.12046 0,1.6807 -0.98706,0.96237996 -2.29792,1.62393996 -3.6918,1.66181996 -1.24459,0.0927 -2.46671,-0.2491 -3.59505,-0.74812 -1.35789,-0.55965 -2.75133,-1.33436996 -4.27027,-1.18121996 -1.37741,0.14601 -2.41842,1.13685996 -3.44288,1.96782996 z",
                    height: 4,
                    width: 15,
                    heightElements: [],
                    widthElements: []
                },
                TASK_TYPE_SEND: {
                    d: "m {mx},{my} l 0,{e.y1} l {e.x1},0 l 0,-{e.y1} z l {e.x0},{e.y0} l {e.x0},-{e.y0}",
                    height: 14,
                    width: 21,
                    heightElements: [6, 14],
                    widthElements: [10.5, 21]
                },
                TASK_TYPE_SCRIPT: {
                    d: "m {mx},{my} c 9.966553,-6.27276 -8.000926,-7.91932 2.968968,-14.938 l -8.802728,0 c -10.969894,7.01868 6.997585,8.66524 -2.968967,14.938 z m -7,-12 l 5,0 m -4.5,3 l 4.5,0 m -3,3 l 5,0m -4,3 l 5,0",
                    height: 15,
                    width: 12.6,
                    heightElements: [6, 14],
                    widthElements: [10.5, 21]
                },
                TASK_TYPE_USER_1: {d: "m {mx},{my} c 0.909,-0.845 1.594,-2.049 1.594,-3.385 0,-2.554 -1.805,-4.62199999 -4.357,-4.62199999 -2.55199998,0 -4.28799998,2.06799999 -4.28799998,4.62199999 0,1.348 0.974,2.562 1.89599998,3.405 -0.52899998,0.187 -5.669,2.097 -5.794,4.7560005 v 6.718 h 17 v -6.718 c 0,-2.2980005 -5.5279996,-4.5950005 -6.0509996,-4.7760005 zm -8,6 l 0,5.5 m 11,0 l 0,-5"},
                TASK_TYPE_USER_2: {d: "m {mx},{my} m 2.162,1.009 c 0,2.4470005 -2.158,4.4310005 -4.821,4.4310005 -2.66499998,0 -4.822,-1.981 -4.822,-4.4310005 "},
                TASK_TYPE_USER_3: {d: "m {mx},{my} m -6.9,-3.80 c 0,0 2.25099998,-2.358 4.27399998,-1.177 2.024,1.181 4.221,1.537 4.124,0.965 -0.098,-0.57 -0.117,-3.79099999 -4.191,-4.13599999 -3.57499998,0.001 -4.20799998,3.36699999 -4.20699998,4.34799999 z"},
                TASK_TYPE_MANUAL: {d: "m {mx},{my} c 0.234,-0.01 5.604,0.008 8.029,0.004 0.808,0 1.271,-0.172 1.417,-0.752 0.227,-0.898 -0.334,-1.314 -1.338,-1.316 -2.467,-0.01 -7.886,-0.004 -8.108,-0.004 -0.014,-0.079 0.016,-0.533 0,-0.61 0.195,-0.042 8.507,0.006 9.616,0.002 0.877,-0.007 1.35,-0.438 1.353,-1.208 0.003,-0.768 -0.479,-1.09 -1.35,-1.091 -2.968,-0.002 -9.619,-0.013 -9.619,-0.013 v -0.591 c 0,0 5.052,-0.016 7.225,-0.016 0.888,-0.002 1.354,-0.416 1.351,-1.193 -0.006,-0.761 -0.492,-1.196 -1.361,-1.196 -3.473,-0.005 -10.86,-0.003 -11.0829995,-0.003 -0.022,-0.047 -0.045,-0.094 -0.069,-0.139 0.3939995,-0.319 2.0409995,-1.626 2.4149995,-2.017 0.469,-0.4870005 0.519,-1.1650005 0.162,-1.6040005 -0.414,-0.511 -0.973,-0.5 -1.48,-0.236 -1.4609995,0.764 -6.5999995,3.6430005 -7.7329995,4.2710005 -0.9,0.499 -1.516,1.253 -1.882,2.19 -0.37000002,0.95 -0.17,2.01 -0.166,2.979 0.004,0.718 -0.27300002,1.345 -0.055,2.063 0.629,2.087 2.425,3.312 4.859,3.318 4.6179995,0.014 9.2379995,-0.139 13.8569995,-0.158 0.755,-0.004 1.171,-0.301 1.182,-1.033 0.012,-0.754 -0.423,-0.969 -1.183,-0.973 -1.778,-0.01 -5.824,-0.004 -6.04,-0.004 10e-4,-0.084 0.003,-0.586 10e-4,-0.67 z"},
                TASK_TYPE_INSTANTIATING_SEND: {d: "m {mx},{my} l 0,8.4 l 12.6,0 l 0,-8.4 z l 6.3,3.6 l 6.3,-3.6"},
                TASK_TYPE_SERVICE: {d: "m {mx},{my} v -1.71335 c 0.352326,-0.0705 0.703932,-0.17838 1.047628,-0.32133 0.344416,-0.14465 0.665822,-0.32133 0.966377,-0.52145 l 1.19431,1.18005 1.567487,-1.57688 -1.195028,-1.18014 c 0.403376,-0.61394 0.683079,-1.29908 0.825447,-2.01824 l 1.622133,-0.01 v -2.2196 l -1.636514,0.01 c -0.07333,-0.35153 -0.178319,-0.70024 -0.323564,-1.04372 -0.145244,-0.34406 -0.321407,-0.6644 -0.522735,-0.96217 l 1.131035,-1.13631 -1.583305,-1.56293 -1.129598,1.13589 c -0.614052,-0.40108 -1.302883,-0.68093 -2.022633,-0.82247 l 0.0093,-1.61852 h -2.241173 l 0.0042,1.63124 c -0.353763,0.0736 -0.705369,0.17977 -1.049785,0.32371 -0.344415,0.14437 -0.665102,0.32092 -0.9635006,0.52046 l -1.1698628,-1.15823 -1.5667691,1.5792 1.1684265,1.15669 c -0.4026573,0.61283 -0.68308,1.29797 -0.8247287,2.01713 l -1.6588041,0.003 v 2.22174 l 1.6724648,-0.006 c 0.073327,0.35077 0.1797598,0.70243 0.3242851,1.04472 0.1452428,0.34448 0.3214064,0.6644 0.5227339,0.96066 l -1.1993431,1.19723 1.5840256,1.56011 1.1964668,-1.19348 c 0.6140517,0.40346 1.3028827,0.68232 2.0233517,0.82331 l 7.19e-4,1.69892 h 2.226848 z m 0.221462,-3.9957 c -1.788948,0.7502 -3.8576,-0.0928 -4.6097055,-1.87438 -0.7521065,-1.78321 0.090598,-3.84627 1.8802645,-4.59604 1.78823,-0.74936 3.856881,0.0929 4.608987,1.87437 0.752106,1.78165 -0.0906,3.84612 -1.879546,4.59605 z"},
                TASK_TYPE_SERVICE_FILL: {d: "m {mx},{my} c -1.788948,0.7502 -3.8576,-0.0928 -4.6097055,-1.87438 -0.7521065,-1.78321 0.090598,-3.84627 1.8802645,-4.59604 1.78823,-0.74936 3.856881,0.0929 4.608987,1.87437 0.752106,1.78165 -0.0906,3.84612 -1.879546,4.59605 z"},
                TASK_TYPE_BUSINESS_RULE_HEADER: {d: "m {mx},{my} 0,4 20,0 0,-4 z"},
                TASK_TYPE_BUSINESS_RULE_MAIN: {d: "m {mx},{my} 0,12 20,0 0,-12 zm 0,8 l 20,0 m -13,-4 l 0,8"},
                MESSAGE_FLOW_MARKER: {d: "m {mx},{my} m -10.5 ,-7 l 0,14 l 21,0 l 0,-14 z l 10.5,6 l 10.5,-6"}
            }, this.getRawPath = function (e) {
                return this.pathMap[e].d
            }, this.getScaledPath = function (e, t) {
                var n, i, o = this.pathMap[e];
                t.abspos ? (n = t.abspos.x, i = t.abspos.y) : (n = t.containerWidth * t.position.mx, i = t.containerHeight * t.position.my);
                var a = {};
                if (t.position) {
                    for (var s = t.containerHeight / o.height * t.yScaleFactor, c = t.containerWidth / o.width * t.xScaleFactor, l = 0; l < o.heightElements.length; l++)a["y" + l] = o.heightElements[l] * s;
                    for (var u = 0; u < o.widthElements.length; u++)a["x" + u] = o.widthElements[u] * c
                }
                var p = r.format(o.d, {mx: n, my: i, e: a});
                return p
            }
        }

        var r = e("diagram-js/vendor/snapsvg");
        t.exports = i
    }, {"diagram-js/vendor/snapsvg": 258}],
    27: [function (e, t, n) {
        t.exports = {
            __init__: ["bpmnRenderer"],
            bpmnRenderer: ["type", e("./BpmnRenderer")],
            pathMap: ["type", e("./PathMap")]
        }
    }, {"./BpmnRenderer": 25, "./PathMap": 26}],
    28: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i) {
            function r(e, t, n) {
                return {
                    top: e.y < t.y + n.top,
                    bottom: e.y + e.height > t.y + t.height - n.bottom,
                    left: e.x < t.x + n.left,
                    right: e.x + e.width > t.x + t.width - n.right
                }
            }

            function g(e, t) {
                "string" == typeof t && (t = i.get(t));
                var u = a(e), p = !0;
                if ((o(t, "bpmn:Participant") || o(t, "bpmn:Lane") || o(t, "bpmn:SubProcess")) && (l(e, function (e) {
                        return o(e, "bpmn:Lane") || e.labelTarget ? void(p = !1) : void 0
                    }), p)) {
                    var d = r(u, t, m), h = s(t, ["x", "y", "width", "height"]);
                    if (d.top) {
                        var v = u.y - f.top;
                        c(h, {y: v, height: h.height + h.y - v})
                    }
                    if (d.bottom && c(h, {height: u.y + u.height + f.bottom - h.y}), d.left) {
                        var y = u.x - f.left;
                        c(h, {x: y, width: h.width + h.x - y})
                    }
                    d.right && c(h, {width: u.x + u.width + f.right - h.x}), o(t, "bpmn:Participant") ? n.resizeLane(t, h) : n.resizeShape(t, h);
                    var b = t.parent;
                    b && g([t], b)
                }
            }

            h.call(this, e), this.postExecuted(["shape.create"], function (e) {
                var t = e.context, n = t.shape, i = t.parent || t.newParent;
                g([n], i)
            }), this.postExecuted(["elements.move"], function (e) {
                var t = e.context, n = p(u(t.closure.topLevel)), i = d(n, function (e) {
                    return e.parent.id
                });
                l(i, function (e, t) {
                    g(e, t)
                })
            })
        }

        var r = e("inherits"), o = e("../../util/ModelUtil").is, a = e("diagram-js/lib/util/Elements").getBBox, s = e("lodash/object/pick"), c = e("lodash/object/assign"), l = e("lodash/collection/forEach"), u = e("lodash/object/values"), p = e("lodash/array/flatten"), d = e("lodash/collection/groupBy"), h = e("diagram-js/lib/command/CommandInterceptor"), f = {
            top: 60,
            bottom: 60,
            left: 100,
            right: 100
        }, m = {top: 2, bottom: 2, left: 15, right: 15};
        i.$inject = ["eventBus", "canvas", "modeling", "elementRegistry"], r(i, h), t.exports = i
    }, {
        "../../util/ModelUtil": 87,
        "diagram-js/lib/command/CommandInterceptor": 123,
        "diagram-js/lib/util/Elements": 239,
        inherits: 115,
        "lodash/array/flatten": 264,
        "lodash/collection/forEach": 274,
        "lodash/collection/groupBy": 275,
        "lodash/object/assign": 396,
        "lodash/object/pick": 402,
        "lodash/object/values": 404
    }],
    29: [function (e, t, n) {
        t.exports = {__init__: ["autoResize"], autoResize: ["type", e("./AutoResize")]}
    }, {"./AutoResize": 28}],
    30: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i, r, o, a) {
            e.registerProvider(this), this._contextPad = e, this._modeling = t, this._elementFactory = n, this._connect = i, this._create = r, this._bpmnReplace = o, this._canvas = a
        }

        function r(e, t, n) {
            var i = e.$instanceOf(t), r = !1, o = e.eventDefinitions || [];
            return a(o, function (e) {
                e.$type === n && (r = !0)
            }), i && r
        }

        var o = e("lodash/object/assign"), a = e("lodash/collection/forEach"), s = e("../../util/ModelUtil").is, c = e("../modeling/util/ModelingUtil").isAny, l = e("../modeling/util/LaneUtil").getChildLanes, u = e("../../util/DiUtil").isEventSubProcess;
        i.$inject = ["contextPad", "modeling", "elementFactory", "connect", "create", "bpmnReplace", "canvas"], t.exports = i, i.prototype.getContextPadEntries = function (e) {
            function t(e, t, n) {
                m.start(e, t, n)
            }

            function n(t) {
                h.removeElements([e])
            }

            function i(e) {
                var t = 5, n = y.getContainer(), i = d.getPad(e).html, r = n.getBoundingClientRect(), o = i.getBoundingClientRect(), a = o.top - r.top, s = o.left - r.left, c = {
                    x: s,
                    y: a + o.height + t
                };
                return c
            }

            function a(e, t, n) {
                function i(t, i) {
                    var r = f.createShape(o({type: e}, n));
                    g.start(t, r, i)
                }

                var r = e.replace(/^bpmn\:/, "");
                return {group: "model", className: t, title: "Append " + r, action: {dragstart: i, click: i}}
            }

            function p(e) {
                return function (t, n) {
                    h.splitLane(n, e), d.open(n, !0)
                }
            }

            var d = this._contextPad, h = this._modeling, f = this._elementFactory, m = this._connect, g = this._create, v = this._bpmnReplace, y = this._canvas, b = {};
            if ("label" === e.type)return b;
            var x = e.businessObject;
            if (c(x, ["bpmn:Lane", "bpmn:Participant"])) {
                var E = l(e);
                o(b, {
                    "lane-insert-above": {
                        group: "lane-insert-above",
                        className: "icon-lane-insert-above",
                        title: "Add Lane above",
                        action: {
                            click: function (e, t) {
                                h.addLane(t, "top")
                            }
                        }
                    }
                }), E.length < 2 && (e.height >= 120 && o(b, {
                    "lane-divide-two": {
                        group: "lane-divide",
                        className: "icon-lane-divide-two",
                        title: "Divide into two Lanes",
                        action: {click: p(2)}
                    }
                }), e.height >= 180 && o(b, {
                    "lane-divide-three": {
                        group: "lane-divide",
                        className: "icon-lane-divide-three",
                        title: "Divide into three Lanes",
                        action: {click: p(3)}
                    }
                })), o(b, {
                    "lane-insert-below": {
                        group: "lane-insert-below",
                        className: "icon-lane-insert-below",
                        title: "Add Lane below",
                        action: {
                            click: function (e, t) {
                                h.addLane(t, "bottom")
                            }
                        }
                    }
                })
            }
            s(x, "bpmn:FlowNode") && (s(x, "bpmn:EndEvent") || s(x, "bpmn:EventBasedGateway") || r(x, "bpmn:IntermediateThrowEvent", "bpmn:LinkEventDefinition") || u(x) || o(b, {
                "append.end-event": a("bpmn:EndEvent", "icon-end-event-none"),
                "append.gateway": a("bpmn:ExclusiveGateway", "icon-gateway-xor"),
                "append.append-task": a("bpmn:Task", "icon-task"),
                "append.intermediate-event": a("bpmn:IntermediateThrowEvent", "icon-intermediate-event-none")
            }), s(x, "bpmn:EventBasedGateway") && o(b, {
                "append.receive-task": a("bpmn:ReceiveTask", "icon-receive-task"),
                "append.message-intermediate-event": a("bpmn:IntermediateCatchEvent", "icon-intermediate-event-catch-message", {_eventDefinitionType: "bpmn:MessageEventDefinition"}),
                "append.timer-intermediate-event": a("bpmn:IntermediateCatchEvent", "icon-intermediate-event-catch-timer", {_eventDefinitionType: "bpmn:TimerEventDefinition"}),
                "append.condtion-intermediate-event": a("bpmn:IntermediateCatchEvent", "icon-intermediate-event-catch-condition", {_eventDefinitionType: "bpmn:ConditionalEventDefinition"}),
                "append.signal-intermediate-event": a("bpmn:IntermediateCatchEvent", "icon-intermediate-event-catch-signal", {_eventDefinitionType: "bpmn:SignalEventDefinition"})
            }));
            var w = v.getReplaceOptions(e);
            return w.length && o(b, {
                replace: {
                    group: "edit",
                    className: "icon-screw-wrench",
                    title: "Change type",
                    action: {
                        click: function (e, t) {
                            v.openChooser(i(t), t)
                        }
                    }
                }
            }), c(x, ["bpmn:FlowNode", "bpmn:InteractionNode"]) && o(b, {
                "append.text-annotation": a("bpmn:TextAnnotation", "icon-text-annotation"),
                connect: {
                    group: "connect",
                    className: "icon-connection-multi",
                    title: "Connect using Sequence/MessageFlow",
                    action: {click: t, dragstart: t}
                }
            }), s(x, "bpmn:DataObjectReference") && o(b, {
                connect: {
                    group: "connect",
                    className: "icon-connection-multi",
                    title: "Connect using DataInputAssociation",
                    action: {click: t, dragstart: t}
                }
            }), o(b, {
                "delete": {
                    group: "edit",
                    className: "icon-trash",
                    title: "Remove",
                    action: {click: n, dragstart: n}
                }
            }), b
        }
    }, {
        "../../util/DiUtil": 85,
        "../../util/ModelUtil": 87,
        "../modeling/util/LaneUtil": 64,
        "../modeling/util/ModelingUtil": 65,
        "lodash/collection/forEach": 274,
        "lodash/object/assign": 396
    }],
    31: [function (e, t, n) {
        t.exports = {
            __depends__: [e("diagram-js-direct-editing"), e("diagram-js/lib/features/context-pad"), e("diagram-js/lib/features/selection"), e("diagram-js/lib/features/connect"), e("diagram-js/lib/features/create"), e("../replace")],
            __init__: ["contextPadProvider"],
            contextPadProvider: ["type", e("./ContextPadProvider")]
        }
    }, {
        "../replace": 74,
        "./ContextPadProvider": 30,
        "diagram-js-direct-editing": 110,
        "diagram-js/lib/features/connect": 147,
        "diagram-js/lib/features/context-pad": 149,
        "diagram-js/lib/features/create": 151,
        "diagram-js/lib/features/selection": 211
    }],
    32: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i, r, o, a, s) {
            var c = {
                selectElements: function () {
                    var e = o.getRootElement(), t = a.filter(function (t) {
                        return t != e
                    });
                    r.select(t)
                }, spaceTool: function () {
                    t.activateSelection()
                }, lassoTool: function () {
                    n.activateSelection()
                }, directEditing: function () {
                    var e = r.get();
                    e.length && i.activate(e[0])
                }
            };
            s.register(c), e.addListener(function (t, n) {
                if (65 === t && e.isCmd(n))return s.trigger("selectElements"), !0;
                if (!e.hasModifier(n))return 83 === t ? (s.trigger("spaceTool"), !0) : 76 === t ? (s.trigger("lassoTool"), !0) : 69 === t ? (s.trigger("directEditing"), !0) : void 0
            })
        }

        i.$inject = ["keyboard", "spaceTool", "lassoTool", "directEditing", "selection", "canvas", "elementRegistry", "editorActions"], t.exports = i
    }, {}],
    33: [function (e, t, n) {
        t.exports = {
            __depends__: [e("diagram-js/lib/features/keyboard")],
            __init__: ["bpmnKeyBindings"],
            bpmnKeyBindings: ["type", e("./BpmnKeyBindings")]
        }
    }, {"./BpmnKeyBindings": 32, "diagram-js/lib/features/keyboard": 159}],
    34: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i) {
            n.registerProvider(this), i.registerHandler("element.updateLabel", r), e.on("element.dblclick", function (e) {
                n.activate(e.element)
            }), e.on(["element.mousedown", "drag.activate", "canvas.viewbox.changed"], function (e) {
                n.complete()
            }), e.on(["commandStack.changed"], function () {
                n.cancel()
            }), "ontouchstart"in document.documentElement || e.on("create.end", 500, function (e) {
                var t = e.shape, i = e.context.canExecute;
                i && (a(t, "bpmn:Task") || a(t, "bpmn:TextAnnotation") || a(t, "bpmn:SubProcess") && !s(t)) && n.activate(t)
            }), this._canvas = t, this._commandStack = i
        }

        var r = e("./cmd/UpdateLabelHandler"), o = e("./LabelUtil"), a = e("../../util/ModelUtil").is, s = e("../../util/DiUtil").isExpanded, c = {
            width: 150,
            height: 50
        };
        i.$inject = ["eventBus", "canvas", "directEditing", "commandStack"], t.exports = i, i.prototype.activate = function (e) {
            var t = o.getLabel(e);
            if (void 0 !== t) {
                var n = this.getEditingBBox(e);
                return (a(e, "bpmn:Participant") && s(e) || a(e, "bpmn:Lane")) && (n.width = c.width, n.height = c.height, n.x = n.x + 10 - n.width / 2, n.y = n.mid.y - n.height / 2), a(e, "bpmn:SubProcess") && s(e) && (n.height = c.height, n.x = n.mid.x - n.width / 2, n.y = n.y + 10 - n.height / 2), {
                    bounds: n,
                    text: t
                }
            }
        }, i.prototype.getEditingBBox = function (e, t) {
            var n = e.label || e, i = this._canvas.getAbsoluteBBox(n), r = {
                x: i.x + i.width / 2,
                y: i.y + i.height / 2
            };
            return n.labelTarget && (i.width = Math.max(i.width, c.width), i.height = Math.max(i.height, c.height), i.x = r.x - i.width / 2), i.mid = r, i
        }, i.prototype.update = function (e, t) {
            this._commandStack.execute("element.updateLabel", {element: e, newLabel: t})
        }
    }, {"../../util/DiUtil": 85, "../../util/ModelUtil": 87, "./LabelUtil": 35, "./cmd/UpdateLabelHandler": 36}],
    35: [function (e, t, n) {
        "use strict";
        function i(e) {
            return r(e, "bpmn:FlowElement") || r(e, "bpmn:Participant") || r(e, "bpmn:Lane") || r(e, "bpmn:SequenceFlow") || r(e, "bpmn:MessageFlow") ? "name" : r(e, "bpmn:TextAnnotation") ? "text" : void 0
        }

        var r = e("../../util/ModelUtil").is;
        t.exports.getLabel = function (e) {
            var t = e.businessObject, n = i(t);
            return n ? t[n] || "" : void 0
        }, t.exports.setLabel = function (e, t) {
            var n = e.businessObject, r = i(n);
            r && (n[r] = t);
            var o = e.label || e;
            return o.hidden = !1, o
        }
    }, {"../../util/ModelUtil": 87}],
    36: [function (e, t, n) {
        "use strict";
        function i(e) {
            function t(t, n) {
                var i = r.setLabel(t, n);
                e.fire("element.changed", {element: i})
            }

            function n(e) {
                return e.oldLabel = r.getLabel(e.element), t(e.element, e.newLabel)
            }

            function i(e) {
                return t(e.element, e.oldLabel)
            }

            function o(e) {
                return !0
            }

            this.execute = n, this.revert = i, this.canExecute = o
        }

        var r = e("../LabelUtil");
        i.$inject = ["eventBus"], t.exports = i
    }, {"../LabelUtil": 35}],
    37: [function (e, t, n) {
        t.exports = {
            __depends__: [e("diagram-js/lib/command"), e("diagram-js/lib/features/change-support"), e("diagram-js-direct-editing")],
            __init__: ["labelEditingProvider"],
            labelEditingProvider: ["type", e("./LabelEditingProvider")]
        }
    }, {
        "./LabelEditingProvider": 34,
        "diagram-js-direct-editing": 110,
        "diagram-js/lib/command": 125,
        "diagram-js/lib/features/change-support": 145
    }],
    38: [function (e, t, n) {
        "use strict";
        function i(e) {
            this._model = e
        }

        var r = e("lodash/collection/map"), o = e("lodash/object/assign"), a = e("lodash/object/pick");
        i.$inject = ["moddle"],
            i.prototype._needsId = function (e) {
                return e.$instanceOf("bpmn:RootElement") || e.$instanceOf("bpmn:FlowElement") || e.$instanceOf("bpmn:MessageFlow") || e.$instanceOf("bpmn:DataAssociation") || e.$instanceOf("bpmn:Artifact") || e.$instanceOf("bpmn:Participant") || e.$instanceOf("bpmn:Lane") || e.$instanceOf("bpmn:Process") || e.$instanceOf("bpmn:Collaboration") || e.$instanceOf("bpmndi:BPMNShape") || e.$instanceOf("bpmndi:BPMNEdge") || e.$instanceOf("bpmndi:BPMNDiagram") || e.$instanceOf("bpmndi:BPMNPlane")
            }, i.prototype._ensureId = function (e) {
            var t = (e.$type || "").replace(/^[^:]*:/g, "") + "_";
            !e.id && this._needsId(e) && (e.id = this._model.ids.nextPrefixed(t, e))
        }, i.prototype.create = function (e, t) {
            var n = this._model.create(e, t || {});
            return this._ensureId(n), n
        }, i.prototype.createDiLabel = function () {
            return this.create("bpmndi:BPMNLabel", {bounds: this.createDiBounds()})
        }, i.prototype.createDiShape = function (e, t, n) {
            return this.create("bpmndi:BPMNShape", o({bpmnElement: e, bounds: this.createDiBounds(t)}, n))
        }, i.prototype.createDiBounds = function (e) {
            return this.create("dc:Bounds", e)
        }, i.prototype.createDiWaypoints = function (e) {
            return r(e, function (e) {
                return this.createDiWaypoint(e)
            }, this)
        }, i.prototype.createDiWaypoint = function (e) {
            return this.create("dc:Point", a(e, ["x", "y"]))
        }, i.prototype.createDiEdge = function (e, t, n) {
            return this.create("bpmndi:BPMNEdge", o({bpmnElement: e}, n))
        }, i.prototype.createDiPlane = function (e) {
            return this.create("bpmndi:BPMNPlane", {bpmnElement: e})
        }, t.exports = i
    }, {"lodash/collection/map": 277, "lodash/object/assign": 396, "lodash/object/pick": 402}],
    39: [function (e, t, n) {
        "use strict";
        function i(e, t, n) {
            u.call(this, e), this.postExecute(["shape.create", "connection.create"], function (e) {
                var n, i = e.context, r = i.shape || i.connection, o = r.businessObject;
                c(o) && (n = l(r), t.createLabel(r, n, {id: o.id + "_label", hidden: !0, businessObject: o}))
            }), this.executed(["label.create", "shape.moved"], function (e) {
                var t, i, o = e.context.shape;
                o.labelTarget && s(o.labelTarget || o, "bpmn:BaseElement") && (t = o.businessObject, i = t.di, i.label || (i.label = n.create("bpmndi:BPMNLabel", {bounds: n.create("dc:Bounds")})), r(i.label.bounds, {
                    x: o.x,
                    y: o.y,
                    width: o.width,
                    height: o.height
                }))
            })
        }

        var r = e("lodash/object/assign"), o = e("inherits"), a = e("../../util/LabelUtil"), s = e("../../util/ModelUtil").is, c = a.hasExternalLabel, l = a.getExternalLabelMid, u = e("diagram-js/lib/command/CommandInterceptor");
        o(i, u), i.$inject = ["eventBus", "modeling", "bpmnFactory"], t.exports = i
    }, {
        "../../util/LabelUtil": 86,
        "../../util/ModelUtil": 87,
        "diagram-js/lib/command/CommandInterceptor": 123,
        inherits: 115,
        "lodash/object/assign": 396
    }],
    40: [function (e, t, n) {
        "use strict";
        function i() {
        }

        function r(e) {
            var t = e.host, n = -10;
            return d(p(e), t, n)
        }

        function o(e, t, n) {
            var i = e && e[t];
            return i ? i.original || i : p(n)
        }

        var a = e("inherits"), s = e("lodash/object/assign"), c = e("diagram-js/lib/layout/BaseLayouter"), l = e("diagram-js/lib/layout/ManhattanLayout"), u = e("diagram-js/lib/layout/LayoutUtil"), p = u.getMid, d = u.getOrientation, h = e("../../util/ModelUtil").is;
        a(i, c), t.exports = i, i.prototype.layoutConnection = function (e, t) {
            var n, i, a, c, u = e.source, p = e.target, d = e.waypoints;
            if (n = o(d, 0, u), i = o(d, d && d.length - 1, p), (h(e, "bpmn:Association") || h(e, "bpmn:DataAssociation")) && d)return d;
            if (h(e, "bpmn:MessageFlow"))a = {preferredLayouts: ["straight", "v:v"]}, h(p, "bpmn:Event") && (a = {preferredLayouts: ["v:v"]}); else if (h(e, "bpmn:SequenceFlow"))if (h(u, "bpmn:BoundaryEvent")) {
                var f = r(u);
                /left|right/.test(f) ? a = {preferredLayouts: ["h:v"]} : /top|bottom/.test(f) && (a = {preferredLayouts: ["v:h"]})
            } else a = h(u, "bpmn:Gateway") ? {preferredLayouts: ["v:h"]} : h(p, "bpmn:Gateway") ? {preferredLayouts: ["h:v"]} : {preferredLayouts: ["h:h"]};
            return a && (a = s(a, t), c = l.repairConnection(u, p, n, i, d, a)), c || [n, i]
        }
    }, {
        "../../util/ModelUtil": 87,
        "diagram-js/lib/layout/BaseLayouter": 224,
        "diagram-js/lib/layout/LayoutUtil": 226,
        "diagram-js/lib/layout/ManhattanLayout": 227,
        inherits: 115,
        "lodash/object/assign": 396
    }],
    41: [function (e, t, n) {
        "use strict";
        function i(e, t, n) {
            function i(e) {
                var t, i = e.context;
                i.cropped || (t = i.connection, t.waypoints = n.getCroppedWaypoints(t), i.cropped = !0)
            }

            function r(e) {
                var t = e.context;
                g.updateParent(t.shape || t.connection, t.oldParent)
            }

            function a(e) {
                var t = e.context, n = t.shape || t.connection, i = t.parent || t.newParent;
                g.updateParent(n, i)
            }

            function c(e) {
                var t = e.context, n = t.oldRoot, i = n.children;
                s(i, function (e) {
                    g.updateParent(e)
                })
            }

            function l(e) {
                var t = e.context.shape;
                d(t, "bpmn:BaseElement") && g.updateBounds(t)
            }

            function u(e) {
                g.updateConnection(e.context)
            }

            function f(e) {
                g.updateConnectionWaypoints(e.context.connection)
            }

            function m(e) {
                g.updateAttachment(e.context)
            }

            h.call(this, e), this._bpmnFactory = t;
            var g = this;
            this.executed(["connection.layout", "connection.create", "connection.reconnectEnd", "connection.reconnectStart"], i), this.reverted(["connection.layout"], function (e) {
                delete e.context.cropped
            }), this.executed(["shape.move", "shape.create", "shape.delete", "connection.create", "connection.move", "connection.delete"], o(r)), this.reverted(["shape.move", "shape.create", "shape.delete", "connection.create", "connection.move", "connection.delete"], o(a)), this.executed(["canvas.updateRoot"], c), this.reverted(["canvas.updateRoot"], c), this.executed(["shape.move", "shape.create", "shape.resize"], o(l)), this.reverted(["shape.move", "shape.create", "shape.resize"], o(l)), this.executed(["connection.create", "connection.move", "connection.delete", "connection.reconnectEnd", "connection.reconnectStart"], o(u)), this.reverted(["connection.create", "connection.move", "connection.delete", "connection.reconnectEnd", "connection.reconnectStart"], o(u)), this.executed(["connection.layout", "connection.move", "connection.updateWaypoints", "connection.reconnectEnd", "connection.reconnectStart"], o(f)), this.reverted(["connection.layout", "connection.move", "connection.updateWaypoints", "connection.reconnectEnd", "connection.reconnectStart"], o(f)), this.executed(["connection.reconnectEnd", "connection.reconnectStart"], o(function (e) {
                var t = e.context, n = t.connection, i = p(n), r = p(t.oldSource), o = p(t.oldTarget), a = p(n.source), s = p(n.target);
                r !== a && o !== s && (r && r["default"] && (t["default"] = r["default"], r["default"] = void 0), i.sourceRef && i.sourceRef["default"] && !d(s, "bpmn:Activity") && (t["default"] = i.sourceRef["default"], i.sourceRef["default"] = void 0), i.conditionExpression && d(r, "bpmn:Activity") && (t.conditionExpression = i.conditionExpression, i.conditionExpression = void 0), i.conditionExpression && !d(s, "bpmn:Activity") && (t.conditionExpression = i.conditionExpression, i.conditionExpression = void 0))
            })), this.reverted(["connection.reconnectEnd", "connection.reconnectStart"], o(function (e) {
                var t = e.context, n = t.connection, i = p(n), r = p(n.source);
                t["default"] && (d(r, "bpmn:ExclusiveGateway") || d(r, "bpmn:InclusiveGateway")) && (r["default"] = t["default"]), t.conditionExpression && d(r, "bpmn:Activity") && (i.conditionExpression = t.conditionExpression)
            })), this.executed(["element.updateAttachment"], o(m)), this.reverted(["element.updateAttachment"], o(m))
        }

        function r(e) {
            for (; e && !d(e, "bpmn:Definitions");)e = e.$parent;
            return e
        }

        function o(e) {
            return function (t) {
                var n = t.context, i = n.shape || n.connection;
                d(i, "bpmn:BaseElement") && e(t)
            }
        }

        var a = e("lodash/object/assign"), s = e("lodash/collection/forEach"), c = e("inherits"), l = e("diagram-js/lib/util/Collections"), u = e("diagram-js/lib/model"), p = e("../../util/ModelUtil").getBusinessObject, d = e("../../util/ModelUtil").is, h = e("diagram-js/lib/command/CommandInterceptor");
        c(i, h), t.exports = i, i.$inject = ["eventBus", "bpmnFactory", "connectionDocking"], i.prototype.updateAttachment = function (e) {
            var t = e.shape, n = t.businessObject, i = t.host;
            n.attachedToRef = i && i.businessObject
        }, i.prototype.updateParent = function (e, t) {
            if (!(e instanceof u.Label)) {
                var n = e.parent, i = e.businessObject, r = n && n.businessObject, o = r && r.di;
                d(e, "bpmn:FlowNode") && this.updateFlowNodeRefs(i, r, t && t.businessObject), d(e, "bpmn:DataOutputAssociation") && (r = e.source ? e.source.businessObject : null), d(e, "bpmn:DataInputAssociation") && (r = e.target ? e.target.businessObject : null), this.updateSemanticParent(i, r), d(e, "bpmn:DataObjectReference") && i.dataObjectRef && this.updateSemanticParent(i.dataObjectRef, r), this.updateDiParent(i.di, o)
            }
        }, i.prototype.updateBounds = function (e) {
            var t = e.businessObject.di, n = e instanceof u.Label ? this._getLabel(t).bounds : t.bounds;
            a(n, {x: e.x, y: e.y, width: e.width, height: e.height})
        }, i.prototype.updateFlowNodeRefs = function (e, t, n) {
            if (n !== t) {
                var i, r;
                d(n, "bpmn:Lane") && (i = n.get("flowNodeRef"), l.remove(i, e)), d(t, "bpmn:Lane") && (r = t.get("flowNodeRef"), l.add(r, e))
            }
        }, i.prototype.updateDiParent = function (e, t) {
            if (t && !d(t, "bpmndi:BPMNPlane") && (t = t.$parent), e.$parent !== t) {
                var n = (t || e.$parent).get("planeElement");
                t ? (n.push(e), e.$parent = t) : (l.remove(n, e), e.$parent = null)
            }
        }, i.prototype.getLaneSet = function (e) {
            var t, n;
            return d(e, "bpmn:Lane") ? (t = e.childLaneSet, t || (t = this._bpmnFactory.create("bpmn:LaneSet"), e.childLaneSet = t, t.$parent = e), t) : (d(e, "bpmn:Participant") && (e = e.processRef), n = e.get("laneSets"), t = n[0], t || (t = this._bpmnFactory.create("bpmn:LaneSet"), t.$parent = e, n.push(t)), t)
        }, i.prototype.updateSemanticParent = function (e, t) {
            var n;
            if (e.$parent !== t) {
                if (d(e, "bpmn:Lane"))t && (t = this.getLaneSet(t)), n = "lanes"; else if (d(e, "bpmn:FlowElement")) {
                    if (t)if (d(t, "bpmn:Participant"))t = t.processRef; else if (d(t, "bpmn:Lane"))do t = t.$parent.$parent; while (d(t, "bpmn:Lane"));
                    n = "flowElements"
                } else if (d(e, "bpmn:Artifact")) {
                    for (; t && !d(t, "bpmn:Process") && !d(t, "bpmn:SubProcess") && !d(t, "bpmn:Collaboration");) {
                        if (d(t, "bpmn:Participant")) {
                            t = t.processRef;
                            break
                        }
                        t = t.$parent
                    }
                    n = "artifacts"
                } else if (d(e, "bpmn:MessageFlow"))n = "messageFlows"; else if (d(e, "bpmn:Participant")) {
                    n = "participants";
                    var i, o = e.processRef;
                    o && (i = r(e.$parent || t), e.$parent && (l.remove(i.get("rootElements"), o), o.$parent = null), t && (l.add(i.get("rootElements"), o), o.$parent = i))
                } else d(e, "bpmn:DataOutputAssociation") ? n = "dataOutputAssociations" : d(e, "bpmn:DataInputAssociation") && (n = "dataInputAssociations");
                if (!n)throw new Error("no parent for ", e, t);
                var a;
                e.$parent && (a = e.$parent.get(n), l.remove(a, e)), t ? (a = t.get(n), a.push(e), e.$parent = t) : e.$parent = null
            }
        }, i.prototype.updateConnectionWaypoints = function (e) {
            e.businessObject.di.set("waypoint", this._bpmnFactory.createDiWaypoints(e.waypoints))
        }, i.prototype.updateConnection = function (e) {
            var t = e.connection, n = p(t), i = p(t.source), r = p(t.target);
            if (d(n, "bpmn:DataAssociation"))d(n, "bpmn:DataInputAssociation") ? (n.get("sourceRef")[0] = i, this.updateSemanticParent(n, r)) : d(n, "bpmn:DataOutputAssociation") && (this.updateSemanticParent(n, i), n.targetRef = r); else {
                var o = d(n, "bpmn:SequenceFlow");
                n.sourceRef !== i && (o && (l.remove(n.sourceRef && n.sourceRef.get("outgoing"), n), i && i.get("outgoing") && i.get("outgoing").push(n)), n.sourceRef = i), n.targetRef !== r && (o && (l.remove(n.targetRef && n.targetRef.get("incoming"), n), r && r.get("incoming") && r.get("incoming").push(n)), n.targetRef = r)
            }
            this.updateConnectionWaypoints(t)
        }, i.prototype._getLabel = function (e) {
            return e.label || (e.label = this._bpmnFactory.createDiLabel()), e.label
        }
    }, {
        "../../util/ModelUtil": 87,
        "diagram-js/lib/command/CommandInterceptor": 123,
        "diagram-js/lib/model": 228,
        "diagram-js/lib/util/Collections": 237,
        inherits: 115,
        "lodash/collection/forEach": 274,
        "lodash/object/assign": 396
    }],
    42: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            a.call(this), this._bpmnFactory = e, this._moddle = t
        }

        var r = e("lodash/object/assign"), o = e("inherits"), a = e("diagram-js/lib/core/ElementFactory"), s = e("../../util/LabelUtil");
        o(i, a), i.$inject = ["bpmnFactory", "moddle"], t.exports = i, i.prototype.baseCreate = a.prototype.create, i.prototype.create = function (e, t) {
            return "label" === e ? this.baseCreate(e, r({type: "label"}, s.DEFAULT_LABEL_SIZE, t)) : this.createBpmnElement(e, t)
        }, i.prototype.createBpmnElement = function (e, t) {
            var n;
            t = t || {};
            var i = t.businessObject;
            if (!i) {
                if (!t.type)throw new Error("no shape type specified");
                i = this._bpmnFactory.create(t.type)
            }
            if (i.di || ("root" === e ? i.di = this._bpmnFactory.createDiPlane(i, [], {id: i.id + "_di"}) : "connection" === e ? i.di = this._bpmnFactory.createDiEdge(i, [], {id: i.id + "_di"}) : i.di = this._bpmnFactory.createDiShape(i, {}, {id: i.id + "_di"})), t.isExpanded && (i.di.isExpanded = t.isExpanded), i.$instanceOf("bpmn:ExclusiveGateway") && (i.di.isMarkerVisible = !0), t.isInterrupting === !1 && (i.isInterrupting = !1), t._eventDefinitionType) {
                var o = i.get("eventDefinitions") || [], a = this._moddle.create(t._eventDefinitionType);
                o.push(a), i.eventDefinitions = o
            }
            return n = this._getDefaultSize(i), t = r({businessObject: i, id: i.id}, n, t), this.baseCreate(e, t)
        }, i.prototype._getDefaultSize = function (e) {
            if (e.$instanceOf("bpmn:SubProcess")) {
                var t = e.di.isExpanded === !0;
                return t ? {width: 350, height: 200} : {width: 100, height: 80}
            }
            return e.$instanceOf("bpmn:Task") ? {width: 100, height: 80} : e.$instanceOf("bpmn:Gateway") ? {
                width: 50,
                height: 50
            } : e.$instanceOf("bpmn:Event") ? {width: 36, height: 36} : e.$instanceOf("bpmn:Participant") ? {
                width: 600,
                height: 250
            } : e.$instanceOf("bpmn:Lane") ? {
                width: 400,
                height: 100
            } : e.$instanceOf("bpmn:DataObjectReference") ? {width: 36, height: 50} : {width: 100, height: 80}
        }, i.prototype.createParticipantShape = function (e) {
            var t = this.createShape({type: "bpmn:Participant"});
            return e || (t.businessObject.processRef = this._bpmnFactory.create("bpmn:Process")), t
        }
    }, {
        "../../util/LabelUtil": 86,
        "diagram-js/lib/core/ElementFactory": 127,
        inherits: 115,
        "lodash/object/assign": 396
    }],
    43: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i) {
            o.call(this, e, t, n), this._bpmnRules = i
        }

        var r = e("inherits"), o = e("diagram-js/lib/features/modeling/Modeling"), a = e("./cmd/UpdatePropertiesHandler"), s = e("./cmd/UpdateCanvasRootHandler"), c = e("./cmd/AddLaneHandler"), l = e("./cmd/SplitLaneHandler"), u = e("./cmd/ResizeLaneHandler"), p = e("./cmd/UpdateFlowNodeRefsHandler");
        r(i, o), i.$inject = ["eventBus", "elementFactory", "commandStack", "bpmnRules"], t.exports = i, i.prototype.getHandlers = function () {
            var e = o.prototype.getHandlers.call(this);
            return e["element.updateProperties"] = a, e["canvas.updateRoot"] = s, e["lane.add"] = c, e["lane.resize"] = u, e["lane.split"] = l, e["lane.updateRefs"] = p, e
        }, i.prototype.updateLabel = function (e, t) {
            this._commandStack.execute("element.updateLabel", {element: e, newLabel: t})
        }, i.prototype.connect = function (e, t, n) {
            var i = this._bpmnRules;
            return n || (n = i.canConnect(e, t) || {type: "bpmn:Association"}), this.createConnection(e, t, n, e.parent)
        }, i.prototype.updateProperties = function (e, t) {
            this._commandStack.execute("element.updateProperties", {element: e, properties: t})
        }, i.prototype.resizeLane = function (e, t, n) {
            this._commandStack.execute("lane.resize", {shape: e, newBounds: t, balanced: n})
        }, i.prototype.addLane = function (e, t) {
            var n = {shape: e, location: t};
            return this._commandStack.execute("lane.add", n), n.newLane
        }, i.prototype.splitLane = function (e, t) {
            this._commandStack.execute("lane.split", {shape: e, count: t})
        }, i.prototype.makeCollaboration = function () {
            var e = this._create("root", {type: "bpmn:Collaboration"}), t = {newRoot: e};
            return this._commandStack.execute("canvas.updateRoot", t), e
        }, i.prototype.updateLaneRefs = function (e, t) {
            this._commandStack.execute("lane.updateRefs", {flowNodeShapes: e, laneShapes: t})
        }, i.prototype.makeProcess = function () {
            var e = this._create("root", {type: "bpmn:Process"}), t = {newRoot: e};
            this._commandStack.execute("canvas.updateRoot", t)
        }
    }, {
        "./cmd/AddLaneHandler": 57,
        "./cmd/ResizeLaneHandler": 58,
        "./cmd/SplitLaneHandler": 59,
        "./cmd/UpdateCanvasRootHandler": 60,
        "./cmd/UpdateFlowNodeRefsHandler": 61,
        "./cmd/UpdatePropertiesHandler": 62,
        "diagram-js/lib/features/modeling/Modeling": 164,
        inherits: 115
    }],
    44: [function (e, t, n) {
        "use strict";
        function i(e, t, n) {
            a.call(this, e), this.preExecute("shape.append", function (e) {
                var t = e.source, n = e.shape;
                e.position || (o(n, "bpmn:TextAnnotation") ? e.position = {
                    x: t.x + t.width / 2 + 75,
                    y: t.y - 50 - n.height / 2
                } : e.position = {x: t.x + t.width + 80 + n.width / 2, y: t.y + t.height / 2})
            }, !0)
        }

        var r = e("inherits"), o = e("../../../util/ModelUtil").is, a = e("diagram-js/lib/command/CommandInterceptor");
        i.$inject = ["eventBus", "elementFactory", "bpmnRules"], r(i, a), t.exports = i
    }, {"../../../util/ModelUtil": 87, "diagram-js/lib/command/CommandInterceptor": 123, inherits: 115}],
    45: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i) {
            o.call(this, e), this.preExecute("shape.create", function (e) {
                var t, r, o = e.shape, s = e.host, c = {cancelActivity: !0};
                s && a(o, "bpmn:IntermediateThrowEvent") && (c.attachedToRef = s.businessObject, t = i.create("bpmn:BoundaryEvent", c), r = {
                    type: "bpmn:BoundaryEvent",
                    businessObject: t
                }, e.shape = n.createShape(r))
            }, !0)
        }

        var r = e("inherits"), o = e("diagram-js/lib/command/CommandInterceptor"), a = e("../../../util/ModelUtil").is;
        i.$inject = ["eventBus", "modeling", "elementFactory", "bpmnFactory"], r(i, o), t.exports = i
    }, {"../../../util/ModelUtil": 87, "diagram-js/lib/command/CommandInterceptor": 123, inherits: 115}],
    46: [function (e, t, n) {
        "use strict";
        function i(e, t, n) {
            o.call(this, e), this.preExecute("shape.create", function (e) {
                var n = e.context, i = n.shape;
                if (a(i, "bpmn:DataObjectReference") && "label" !== i.type) {
                    var r = t.create("bpmn:DataObject");
                    i.businessObject.dataObjectRef = r
                }
            })
        }

        var r = e("inherits"), o = e("diagram-js/lib/command/CommandInterceptor"), a = e("../../../util/ModelUtil").is;
        i.$inject = ["eventBus", "bpmnFactory", "moddle"], r(i, o), t.exports = i
    }, {"../../../util/ModelUtil": 87, "diagram-js/lib/command/CommandInterceptor": 123, inherits: 115}],
    47: [function (e, t, n) {
        "use strict";
        function i(e) {
            return a({}, e)
        }

        function r(e, t, n) {
            s.call(this, e), this.preExecute("shape.create", function (e) {
                var n = e.parent, i = e.shape;
                t.canInsert(i, n) && (e.targetFlow = n, e.parent = n.parent)
            }, !0), this.postExecute("shape.create", function (e) {
                var r, o, a, s, l, u, p, d, h = e.shape, f = e.targetFlow, m = e.position;
                f && (l = f.waypoints, s = c(l, m), s && (u = l.slice(0, s.index), p = l.slice(s.index + (s.bendpoint ? 1 : 0)), d = s.bendpoint ? l[s.index] : m, u.push(i(d)), p.unshift(i(d))), r = f.source, o = f.target, t.canConnect(r, h, f) && (n.reconnectEnd(f, h, u || i(m)), a = !0), t.canConnect(h, o, f) && (a ? n.connect(h, o, {
                    type: f.type,
                    waypoints: p
                }) : n.reconnectStart(f, h, p || i(m))))
            }, !0)
        }

        var o = e("inherits"), a = e("lodash/object/assign"), s = e("diagram-js/lib/command/CommandInterceptor"), c = e("diagram-js/lib/util/LineIntersection").getApproxIntersection;
        o(r, s), r.$inject = ["eventBus", "bpmnRules", "modeling"], t.exports = r
    }, {
        "diagram-js/lib/command/CommandInterceptor": 123,
        "diagram-js/lib/util/LineIntersection": 244,
        inherits: 115,
        "lodash/object/assign": 396
    }],
    48: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i) {
            o.call(this, e), this.preExecute("shape.create", function (e) {
                var n = e.parent, i = e.shape, r = e.position;
                if (a(n, "bpmn:Process") && a(i, "bpmn:Participant")) {
                    var o = t.makeCollaboration();
                    e.position = r, e.parent = o, e.processRoot = n
                }
            }, !0), this.execute("shape.create", function (e) {
                var t = e.processRoot, n = e.shape;
                t && (e.oldProcessRef = n.businessObject.processRef, n.businessObject.processRef = t.businessObject)
            }, !0), this.revert("shape.create", function (e) {
                var t = e.processRoot, n = e.shape;
                t && (n.businessObject.processRef = e.oldProcessRef)
            }, !0), this.postExecute("shape.create", function (e) {
                var n = e.processRoot, i = e.shape;
                if (n) {
                    var r = n.children.slice();
                    t.moveElements(r, {x: 0, y: 0}, i)
                }
            }, !0)
        }

        var r = e("inherits"), o = e("diagram-js/lib/command/CommandInterceptor"), a = e("../../../util/ModelUtil").is;
        i.$inject = ["eventBus", "modeling", "elementFactory", "bpmnFactory"], r(i, o), t.exports = i
    }, {"../../../util/ModelUtil": 87, "diagram-js/lib/command/CommandInterceptor": 123, inherits: 115}],
    49: [function (e, t, n) {
        "use strict";
        function i(e, t, n) {
            function i(e, t) {
                var i = s(t), r = [], o = [];
                if (c(i, function (t) {
                        return t.y > e.y ? o.push(t) : r.push(t), t.children
                    }), i.length) {
                    var a;
                    a = o.length && r.length ? e.height / 2 : e.height;
                    var l, u;
                    r.length && (l = n.calculateAdjustments(r, "y", a, e.y - 10), n.makeSpace(l.movingShapes, l.resizingShapes, {
                        x: 0,
                        y: a
                    }, "s")), o.length && (u = n.calculateAdjustments(o, "y", -a, e.y + e.height + 10), n.makeSpace(u.movingShapes, u.resizingShapes, {
                        x: 0,
                        y: -a
                    }, "n"))
                }
            }

            o.call(this, e), this.postExecuted("shape.delete", l, function (e) {
                var t = e.context, n = t.hints, r = t.shape, o = t.oldParent;
                a(r, "bpmn:Lane") && (n && n.nested || i(r, o))
            })
        }

        var r = e("inherits"), o = e("diagram-js/lib/command/CommandInterceptor"), a = e("../../../util/ModelUtil").is, s = e("../util/LaneUtil").getChildLanes, c = e("diagram-js/lib/util/Elements").eachElement, l = 500;
        i.$inject = ["eventBus", "modeling", "spaceTool"], r(i, o), t.exports = i
    }, {
        "../../../util/ModelUtil": 87,
        "../util/LaneUtil": 64,
        "diagram-js/lib/command/CommandInterceptor": 123,
        "diagram-js/lib/util/Elements": 239,
        inherits: 115
    }],
    50: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            function n(e, n) {
                t.add({position: {x: e.x + 5, y: e.y + 5}, type: "error", timeout: 2e3, html: "<div>" + n + "</div>"})
            }

            e.on(["shape.move.rejected", "create.rejected"], function (e) {
                var t = e.context, i = t.shape, o = t.target;
                r(o, "bpmn:Collaboration") && r(i, "bpmn:FlowNode") && n(e, "flow elements must be children of pools/participants")
            })
        }

        var r = e("../../../util/ModelUtil").is;
        i.$inject = ["eventBus", "tooltips"], t.exports = i
    }, {"../../../util/ModelUtil": 87}],
    51: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            o.call(this, e), this.preExecute("shape.delete", function (e) {
                var t = e.shape, n = t.parent;
                a(t, "bpmn:Participant") && (e.collaborationRoot = n)
            }, !0), this.postExecute("shape.delete", function (e) {
                var n = e.collaborationRoot;
                n && !n.businessObject.participants.length && t.makeProcess()
            }, !0)
        }

        var r = e("inherits"), o = e("diagram-js/lib/command/CommandInterceptor"), a = e("../../../util/ModelUtil").is;
        i.$inject = ["eventBus", "modeling"], r(i, o), t.exports = i
    }, {"../../../util/ModelUtil": 87, "diagram-js/lib/command/CommandInterceptor": 123, inherits: 115}],
    52: [function (e, t, n) {
        "use strict";
        function i(e, t, n) {
            function i(e) {
                var i = e.source, r = e.target, o = e.parent;
                if (o) {
                    var a, c;
                    s(e, "bpmn:SequenceFlow") && (n.canConnectSequenceFlow(i, r) || (c = !0), n.canConnectMessageFlow(i, r) && (a = "bpmn:MessageFlow")), s(e, "bpmn:MessageFlow") && (n.canConnectMessageFlow(i, r) || (c = !0), n.canConnectSequenceFlow(i, r) && (a = "bpmn:SequenceFlow")), s(e, "bpmn:Association") && !n.canConnectAssociation(i, r) && (c = !0), c && t.removeConnection(e), a && t.connect(i, r, {
                        type: a,
                        waypoints: e.waypoints.slice()
                    })
                }
            }

            a.call(this, e), this.postExecuted("elements.move", function (e) {
                var t = e.closure, n = t.allConnections;
                r(n, i)
            }, !0), this.postExecuted(["connection.reconnectStart", "connection.reconnectEnd"], function (e) {
                var t = e.context.connection;
                i(t)
            })
        }

        var r = e("lodash/collection/forEach"), o = e("inherits"), a = e("diagram-js/lib/command/CommandInterceptor"), s = e("../../../util/ModelUtil").is;
        o(i, a), i.$inject = ["eventBus", "modeling", "bpmnRules"], t.exports = i
    }, {
        "../../../util/ModelUtil": 87,
        "diagram-js/lib/command/CommandInterceptor": 123,
        inherits: 115,
        "lodash/collection/forEach": 274
    }],
    53: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i, r, c) {
            o.call(this, e), this._bpmnReplace = t, this._elementRegistry = i, this._selection = r, this._modeling = c, this.postExecuted(["elements.move"], 500, function (e) {
                var t = e.context, i = t.newParent, r = t.newHost, o = [];
                a(t.closure.topLevel, function (e) {
                    o = s(e) ? o.concat(e.children) : o.concat(e)
                }), 1 === o.length && r && (i = r);
                var c = n.canReplace(o, i);
                c && this.replaceElements(o, c.replacements, r)
            }, this), this.postExecute(["shape.replace"], 1500, function (e) {
                var t, i = e.context, r = i.oldShape, o = i.newShape, a = r.attachers;
                a && a.length && (t = n.canReplace(a, o), this.replaceElements(a, t.replacements))
            }, this)
        }

        var r = e("inherits"), o = e("diagram-js/lib/command/CommandInterceptor"), a = e("lodash/collection/forEach"), s = e("../../../util/DiUtil").isEventSubProcess, c = e("../../../util/ModelUtil").is;
        r(i, o), i.prototype.replaceElements = function (e, t, n) {
            var i = this._elementRegistry, r = this._bpmnReplace, o = this._selection, s = this._modeling;
            a(t, function (t) {
                var o = {type: t.newElementType}, a = i.get(t.oldElementId);
                n && c(a, "bpmn:BoundaryEvent") && s.updateAttachment(a, null);
                var l = e.indexOf(a);
                e[l] = r.replaceElement(a, o, {select: !1}), n && c(e[l], "bpmn:BoundaryEvent") && s.updateAttachment(e[l], n)
            }), t && o.select(e)
        }, i.$inject = ["eventBus", "bpmnReplace", "bpmnRules", "elementRegistry", "selection", "modeling"], t.exports = i
    }, {
        "../../../util/DiUtil": 85,
        "../../../util/ModelUtil": 87,
        "diagram-js/lib/command/CommandInterceptor": 123,
        inherits: 115,
        "lodash/collection/forEach": 274
    }],
    54: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            e.on("resize.start", s + 500, function (e) {
                var t = e.context, n = t.shape;
                (r(n, "bpmn:Lane") || r(n, "bpmn:Participant")) && (t.balanced = !a(e))
            }), e.on("resize.end", s, function (e) {
                var n = e.context, i = n.shape, a = n.canExecute, s = n.newBounds;
                return r(i, "bpmn:Lane") || r(i, "bpmn:Participant") ? (a && (s = o(s), t.resizeLane(i, s, n.balanced)), !1) : void 0
            })
        }

        var r = e("../../../util/ModelUtil").is, o = e("diagram-js/lib/layout/LayoutUtil").roundBounds, a = e("diagram-js/lib/util/Mouse").hasPrimaryModifier, s = 1001;
        i.$inject = ["eventBus", "modeling"], t.exports = i
    }, {"../../../util/ModelUtil": 87, "diagram-js/lib/layout/LayoutUtil": 226, "diagram-js/lib/util/Mouse": 246}],
    55: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            function n() {
                return u = u || new r, u.enter(), u
            }

            function i() {
                if (!u)throw new Error("out of bounds release");
                return u
            }

            function o() {
                if (!u)throw new Error("out of bounds release");
                var e = u.leave();
                return e && (t.updateLaneRefs(u.flowNodes, u.lanes), u = null), e
            }

            a.call(this, e);
            var u, p = ["spaceTool", "lane.add", "lane.resize", "lane.split", "elements.move", "elements.delete", "shape.create", "shape.delete", "shape.move", "shape.resize"];
            this.preExecute(p, l, function (e) {
                n()
            }), this.postExecuted(p, c, function (e) {
                o()
            }), this.preExecute(["shape.create", "shape.move", "shape.delete", "shape.resize"], function (e) {
                var t = e.context, n = t.shape, r = i();
                n.labelTarget || (s(n, "bpmn:Lane") && r.addLane(n), s(n, "bpmn:FlowNode") && r.addFlowNode(n))
            })
        }

        function r() {
            this.flowNodes = [], this.lanes = [], this.counter = 0, this.addLane = function (e) {
                this.lanes.push(e)
            }, this.addFlowNode = function (e) {
                this.flowNodes.push(e)
            }, this.enter = function () {
                this.counter++
            }, this.leave = function () {
                return this.counter--, !this.counter
            }
        }

        var o = e("inherits"), a = e("diagram-js/lib/command/CommandInterceptor"), s = e("../../../util/ModelUtil").is, c = 500, l = 5e3;
        i.$inject = ["eventBus", "modeling"], o(i, a), t.exports = i
    }, {"../../../util/ModelUtil": 87, "diagram-js/lib/command/CommandInterceptor": 123, inherits: 115}],
    56: [function (e, t, n) {
        t.exports = {
            __init__: ["appendBehavior", "createBoundaryEventBehavior", "createDataObjectBehavior", "deleteLaneBehavior", "createOnFlowBehavior", "createParticipantBehavior", "modelingFeedback", "removeParticipantBehavior", "replaceConnectionBehavior", "replaceElementBehaviour", "resizeLaneBehavior", "updateFlowNodeRefsBehavior"],
            appendBehavior: ["type", e("./AppendBehavior")],
            createBoundaryEventBehavior: ["type", e("./CreateBoundaryEventBehavior")],
            createDataObjectBehavior: ["type", e("./CreateDataObjectBehavior")],
            deleteLaneBehavior: ["type", e("./DeleteLaneBehavior")],
            createOnFlowBehavior: ["type", e("./CreateOnFlowBehavior")],
            createParticipantBehavior: ["type", e("./CreateParticipantBehavior")],
            modelingFeedback: ["type", e("./ModelingFeedback")],
            removeParticipantBehavior: ["type", e("./RemoveParticipantBehavior")],
            replaceConnectionBehavior: ["type", e("./ReplaceConnectionBehavior")],
            replaceElementBehaviour: ["type", e("./ReplaceElementBehaviour")],
            resizeLaneBehavior: ["type", e("./ResizeLaneBehavior")],
            updateFlowNodeRefsBehavior: ["type", e("./UpdateFlowNodeRefsBehavior")]
        }
    }, {
        "./AppendBehavior": 44,
        "./CreateBoundaryEventBehavior": 45,
        "./CreateDataObjectBehavior": 46,
        "./CreateOnFlowBehavior": 47,
        "./CreateParticipantBehavior": 48,
        "./DeleteLaneBehavior": 49,
        "./ModelingFeedback": 50,
        "./RemoveParticipantBehavior": 51,
        "./ReplaceConnectionBehavior": 52,
        "./ReplaceElementBehaviour": 53,
        "./ResizeLaneBehavior": 54,
        "./UpdateFlowNodeRefsBehavior": 55
    }],
    57: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            this._modeling = e, this._spaceTool = t
        }

        var r = e("lodash/collection/filter"), o = e("diagram-js/lib/util/Elements"), a = e("../util/LaneUtil").getLanesRoot, s = e("../util/LaneUtil").getChildLanes, c = e("../util/LaneUtil").LANE_INDENTATION;
        i.$inject = ["modeling", "spaceTool"], t.exports = i, i.prototype.preExecute = function (e) {
            var t = this._spaceTool, n = this._modeling, i = e.shape, l = e.location, u = a(i), p = u === i, d = p ? i : i.parent, h = s(d);
            h.length || n.createShape({type: "bpmn:Lane"}, {
                x: i.x + c,
                y: i.y,
                width: i.width - c,
                height: i.height
            }, d);
            var f = [];
            o.eachElement(u, function (e) {
                return f.push(e), e === i ? [] : r(e.children, function (e) {
                    return e !== i
                })
            });
            var m = "top" === l ? -120 : 120, g = "top" === l ? i.y : i.y + i.height, v = g + ("top" === l ? 10 : -10), y = "top" === l ? "n" : "s", b = t.calculateAdjustments(f, "y", m, v);
            t.makeSpace(b.movingShapes, b.resizingShapes, {
                x: 0,
                y: m
            }, y), e.newLane = n.createShape({type: "bpmn:Lane"}, {
                x: i.x + (p ? c : 0),
                y: g - ("top" === l ? 120 : 0),
                width: i.width - (p ? c : 0),
                height: 120
            }, d)
        }
    }, {"../util/LaneUtil": 64, "diagram-js/lib/util/Elements": 239, "lodash/collection/filter": 272}],
    58: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            this._modeling = e, this._spaceTool = t
        }

        var r = e("../../../util/ModelUtil").is, o = e("../util/LaneUtil").getLanesRoot, a = e("../util/LaneUtil").computeLanesResize, s = e("diagram-js/lib/util/Elements").eachElement, c = e("diagram-js/lib/layout/LayoutUtil").asTRBL, l = e("diagram-js/lib/features/resize/ResizeUtil").substractTRBL;
        i.$inject = ["modeling", "spaceTool"], t.exports = i, i.prototype.preExecute = function (e) {
            var t = e.shape, n = e.newBounds, i = e.balanced;
            i !== !1 ? this.resizeBalanced(t, n) : this.resizeSpace(t, n)
        }, i.prototype.resizeBalanced = function (e, t) {
            var n = this._modeling, i = a(e, t);
            n.resizeShape(e, t), i.forEach(function (e) {
                n.resizeShape(e.shape, e.newBounds)
            })
        }, i.prototype.resizeSpace = function (e, t) {
            var n = this._spaceTool, i = c(e), a = c(t), u = l(a, i), p = o(e), d = [], h = [];
            s(p, function (e) {
                return d.push(e), (r(e, "bpmn:Lane") || r(e, "bpmn:Participant")) && h.push(e), e.children
            });
            var f, m, g, v, y;
            (u.bottom || u.top) && (f = u.bottom || u.top, m = e.y + (u.bottom ? e.height : 0) + (u.bottom ? -10 : 10), g = u.bottom ? "s" : "n", v = u.top > 0 || u.bottom < 0 ? -f : f, y = n.calculateAdjustments(d, "y", v, m), n.makeSpace(y.movingShapes, y.resizingShapes, {
                x: 0,
                y: f
            }, g)), (u.left || u.right) && (f = u.right || u.left, m = e.x + (u.right ? e.width : 0) + (u.right ? -10 : 100), g = u.right ? "e" : "w", v = u.left > 0 || u.right < 0 ? -f : f, y = n.calculateAdjustments(h, "x", v, m), n.makeSpace(y.movingShapes, y.resizingShapes, {
                x: f,
                y: 0
            }, g))
        }
    }, {
        "../../../util/ModelUtil": 87,
        "../util/LaneUtil": 64,
        "diagram-js/lib/features/resize/ResizeUtil": 202,
        "diagram-js/lib/layout/LayoutUtil": 226,
        "diagram-js/lib/util/Elements": 239
    }],
    59: [function (e, t, n) {
        "use strict";
        function i(e) {
            this._modeling = e
        }

        var r = e("../util/LaneUtil").getChildLanes, o = e("../util/LaneUtil").LANE_INDENTATION;
        i.$inject = ["modeling"], t.exports = i, i.prototype.preExecute = function (e) {
            var t = this._modeling, n = e.shape, i = e.count, a = r(n), s = a.length;
            if (s > i)throw new Error("more than " + i + " child lanes");
            var c, l, u, p, d, h = Math.round(n.height / i);
            for (d = 0; i > d; d++)c = n.y + d * h, l = d === i - 1 ? n.height - h * d : h, u = {
                x: n.x + o,
                y: c,
                width: n.width - o,
                height: l
            }, s > d ? t.resizeShape(a[d], u) : (p = {type: "bpmn:Lane"}, t.createShape(p, u, n))
        }
    }, {"../util/LaneUtil": 64}],
    60: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            this._canvas = e, this._modeling = t
        }

        var r = e("diagram-js/lib/util/Collections");
        i.$inject = ["canvas", "modeling"], t.exports = i, i.prototype.execute = function (e) {
            var t = this._canvas, n = e.newRoot, i = n.businessObject, o = t.getRootElement(), a = o.businessObject, s = a.$parent, c = a.di;
            t.setRootElement(n, !0), r.add(s.rootElements, i), i.$parent = s, r.remove(s.rootElements, a), a.$parent = null, a.di = null, c.bpmnElement = i, i.di = c, e.oldRoot = o
        }, i.prototype.revert = function (e) {
            var t = this._canvas, n = e.newRoot, i = n.businessObject, o = e.oldRoot, a = o.businessObject, s = i.$parent, c = i.di;
            t.setRootElement(o, !0), r.remove(s.rootElements, i), i.$parent = null, r.add(s.rootElements, a), a.$parent = s, i.di = null, c.bpmnElement = a, a.di = c
        }
    }, {"diagram-js/lib/util/Collections": 237}],
    61: [function (e, t, n) {
        "use strict";
        function i(e) {
            this._elementRegistry = e
        }

        var r = e("../util/LaneUtil").collectLanes, o = e("../util/LaneUtil").getLanesRoot, a = e("../../../util/ModelUtil").is, s = e("diagram-js/lib/util/Collections"), c = e("diagram-js/lib/layout/LayoutUtil").asTRBL, l = "flowNodeRef", u = "lanes";
        i.$inject = ["elementRegistry"], t.exports = i, i.prototype.computeUpdates = function (e, t) {
            function n(e, t) {
                var n = c(t), i = {x: e.x + e.width / 2, y: e.y + e.height / 2};
                return i.x > n.left && i.x < n.right && i.y > n.top && i.y < n.bottom
            }

            function i(e) {
                d[e.id] || (m.push(e), d[e.id] = e)
            }

            function s(e) {
                var t = o(e);
                return f[t.id] || (f[t.id] = r(t)), f[t.id]
            }

            function p(e) {
                if (!e.parent)return [];
                var t = s(e);
                return t.filter(function (t) {
                    return n(e, t)
                }).map(function (e) {
                    return e.businessObject
                })
            }

            var d = {}, h = [], f = {}, m = [];
            return t.forEach(function (e) {
                var t = o(e);
                if (t && !d[t.id]) {
                    var n = t.children.filter(function (e) {
                        return a(e, "bpmn:FlowNode")
                    });
                    n.forEach(i), d[t.id] = t
                }
            }), e.forEach(i), m.forEach(function (e) {
                var t = e.businessObject, n = t.get(u), i = n.slice(), r = p(e);
                h.push({flowNode: t, remove: i, add: r})
            }), t.forEach(function (e) {
                var t = e.businessObject;
                e.parent || t.get(l).forEach(function (e) {
                    h.push({flowNode: e, remove: [t], add: []})
                })
            }), h
        }, i.prototype.execute = function (e) {
            var t = e.updates;
            t || (t = e.updates = this.computeUpdates(e.flowNodeShapes, e.laneShapes)), t.forEach(function (e) {
                var t = e.flowNode, n = t.get(u);
                e.remove.forEach(function (e) {
                    s.remove(n, e), s.remove(e.get(l), t)
                }), e.add.forEach(function (e) {
                    s.add(n, e),
                        s.add(e.get(l), t)
                })
            })
        }, i.prototype.revert = function (e) {
            var t = e.updates;
            t.forEach(function (e) {
                var t = e.flowNode, n = t.get(u);
                e.add.forEach(function (e) {
                    s.remove(n, e), s.remove(e.get(l), t)
                }), e.remove.forEach(function (e) {
                    s.add(n, e), s.add(e.get(l), t)
                })
            })
        }
    }, {
        "../../../util/ModelUtil": 87,
        "../util/LaneUtil": 64,
        "diagram-js/lib/layout/LayoutUtil": 226,
        "diagram-js/lib/util/Collections": 237
    }],
    62: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            this._elementRegistry = e, this._moddle = t
        }

        function r(e, t) {
            return a(t, function (t, n) {
                return t[n] = e.get(n), t
            }, {})
        }

        function o(e, t) {
            c(t, function (t, n) {
                e.set(n, t)
            })
        }

        var a = e("lodash/object/transform"), s = e("lodash/object/keys"), c = e("lodash/collection/forEach"), l = "default", u = "name", p = "id";
        i.$inject = ["elementRegistry", "moddle"], t.exports = i, i.prototype.execute = function (e) {
            var t = e.element, n = [t];
            if (!t)throw new Error("element required");
            var i = this._elementRegistry, a = this._moddle.ids, c = t.businessObject, d = e.properties, h = e.oldProperties || r(c, s(d));
            return p in d && (a.unclaim(c[p]), i.updateId(t, d[p])), l in d && (d[l] && n.push(i.get(d[l].id)), c[l] && n.push(i.get(c[l].id))), u in d && t.label && (n.push(t.label), t.label.hidden = !d[u]), o(c, d), e.oldProperties = h, e.changed = n, n
        }, i.prototype.revert = function (e) {
            var t = e.element, n = e.properties, i = e.oldProperties, r = t.businessObject, a = this._elementRegistry, s = this._moddle.ids;
            return o(r, i), p in n && (s.unclaim(n[p]), a.updateId(t, i[p])), e.changed
        }
    }, {"lodash/collection/forEach": 274, "lodash/object/keys": 397, "lodash/object/transform": 403}],
    63: [function (e, t, n) {
        t.exports = {
            __init__: ["modeling", "bpmnUpdater", "bpmnLabelSupport"],
            __depends__: [e("./behavior"), e("../label-editing"), e("../rules"), e("../ordering"), e("../replace"), e("diagram-js/lib/command"), e("diagram-js/lib/features/tooltips"), e("diagram-js/lib/features/label-support"), e("diagram-js/lib/features/attach-support"), e("diagram-js/lib/features/selection"), e("diagram-js/lib/features/change-support"), e("diagram-js/lib/features/space-tool")],
            bpmnFactory: ["type", e("./BpmnFactory")],
            bpmnUpdater: ["type", e("./BpmnUpdater")],
            elementFactory: ["type", e("./ElementFactory")],
            modeling: ["type", e("./Modeling")],
            bpmnLabelSupport: ["type", e("./BpmnLabelSupport")],
            layouter: ["type", e("./BpmnLayouter")],
            connectionDocking: ["type", e("diagram-js/lib/layout/CroppingConnectionDocking")]
        }
    }, {
        "../label-editing": 37,
        "../ordering": 67,
        "../replace": 74,
        "../rules": 76,
        "./BpmnFactory": 38,
        "./BpmnLabelSupport": 39,
        "./BpmnLayouter": 40,
        "./BpmnUpdater": 41,
        "./ElementFactory": 42,
        "./Modeling": 43,
        "./behavior": 56,
        "diagram-js/lib/command": 125,
        "diagram-js/lib/features/attach-support": 137,
        "diagram-js/lib/features/change-support": 145,
        "diagram-js/lib/features/label-support": 161,
        "diagram-js/lib/features/selection": 211,
        "diagram-js/lib/features/space-tool": 218,
        "diagram-js/lib/features/tooltips": 220,
        "diagram-js/lib/layout/CroppingConnectionDocking": 225
    }],
    64: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            return p(u(t), u(e))
        }

        function r(e, t) {
            return t = t || [], e.children.filter(function (e) {
                c(e, "bpmn:Lane") && (r(e, t), t.push(e))
            }), t
        }

        function o(e) {
            return e.children.filter(function (e) {
                return c(e, "bpmn:Lane")
            })
        }

        function a(e) {
            return l(e, f) || e
        }

        function s(e, t) {
            var n = a(e), o = c(n, "bpmn:Process") ? [] : [n], s = r(n, o), l = u(e), p = u(t), f = i(e, t), m = [];
            return s.forEach(function (t) {
                if (t !== e) {
                    var n = 0, i = f.right, r = 0, o = f.left, a = u(t);
                    f.top && (h(a.bottom - l.top) < 10 && (r = p.top - a.bottom), h(a.top - l.top) < 5 && (n = p.top - a.top)), f.bottom && (h(a.top - l.bottom) < 10 && (n = p.bottom - a.top), h(a.bottom - l.bottom) < 5 && (r = p.bottom - a.bottom)), (n || i || r || o) && m.push({
                        shape: t,
                        newBounds: d(t, {top: n, right: i, bottom: r, left: o})
                    })
                }
            }), m
        }

        var c = e("../../../util/ModelUtil").is, l = e("./ModelingUtil").getParent, u = e("diagram-js/lib/layout/LayoutUtil").asTRBL, p = e("diagram-js/lib/features/resize/ResizeUtil").substractTRBL, d = e("diagram-js/lib/features/resize/ResizeUtil").resizeTRBL, h = Math.abs, f = ["bpmn:Participant", "bpmn:Process", "bpmn:SubProcess"], m = 30;
        t.exports.LANE_INDENTATION = m, t.exports.collectLanes = r, t.exports.getChildLanes = o, t.exports.getLanesRoot = a, t.exports.computeLanesResize = s
    }, {
        "../../../util/ModelUtil": 87,
        "./ModelingUtil": 65,
        "diagram-js/lib/features/resize/ResizeUtil": 202,
        "diagram-js/lib/layout/LayoutUtil": 226
    }],
    65: [function (e, t, n) {
        "use strict";
        function i(e) {
            for (var t = []; e;)e = e.parent, e && t.push(e);
            return t
        }

        function r(e, t) {
            return a(t, function (t) {
                return s(e, t)
            })
        }

        function o(e, t) {
            for ("string" == typeof t && (t = [t]); e = e.parent;)if (r(e, t))return e;
            return null
        }

        var a = e("lodash/collection/any"), s = e("../../../util/ModelUtil").is;
        t.exports.getParents = i, t.exports.isAny = r, t.exports.getParent = o
    }, {"../../../util/ModelUtil": 87, "lodash/collection/any": 270}],
    66: [function (e, t, n) {
        "use strict";
        function i(e) {
            function t(e) {
                var t = c(r, function (t) {
                    return e.type === t.type
                });
                return t && t.order || {level: 1}
            }

            function n(e) {
                var n = e.order;
                return n || (e.order = n = t(e)), n
            }

            function i(e, t, n) {
                for (var i = t; i && !a(i, n);)i = i.parent;
                if (!i)throw new Error("no parent for " + e.id + " in " + t.id);
                return i
            }

            o.call(this, e);
            var r = [{type: "label", order: {level: 10}}, {
                type: "bpmn:SubProcess",
                order: {level: 6}
            }, {
                type: "bpmn:SequenceFlow",
                order: {level: 5, containers: ["bpmn:Participant", "bpmn:FlowElementsContainer"]}
            }, {
                type: "bpmn:Association",
                order: {level: 6, containers: ["bpmn:Participant", "bpmn:FlowElementsContainer", "bpmn:Collaboration"]}
            }, {
                type: "bpmn:MessageFlow",
                order: {level: 9, containers: ["bpmn:Collaboration"]}
            }, {type: "bpmn:BoundaryEvent", order: {level: 8}}, {
                type: "bpmn:Participant",
                order: {level: -2}
            }, {type: "bpmn:Lane", order: {level: -1}}];
            this.getOrdering = function (e, t) {
                var r = n(e);
                r.containers && (t = i(e, t, r.containers));
                var o = t.children.indexOf(e), a = s(t.children, function (e) {
                    return r.level < n(e).level
                });
                return -1 !== a && -1 !== o && a > o && (a -= 1), {index: a, parent: t}
            }
        }

        var r = e("inherits"), o = e("diagram-js/lib/features/ordering/OrderingProvider"), a = e("../modeling/util/ModelingUtil").isAny, s = e("lodash/array/findIndex"), c = e("lodash/collection/find");
        i.$inject = ["eventBus"], r(i, o), t.exports = i
    }, {
        "../modeling/util/ModelingUtil": 65,
        "diagram-js/lib/features/ordering/OrderingProvider": 189,
        inherits: 115,
        "lodash/array/findIndex": 263,
        "lodash/collection/find": 273
    }],
    67: [function (e, t, n) {
        t.exports = {__init__: ["bpmnOrderingProvider"], bpmnOrderingProvider: ["type", e("./BpmnOrderingProvider")]}
    }, {"./BpmnOrderingProvider": 66}],
    68: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i, r) {
            this._create = t, this._elementFactory = n, this._spaceTool = i, this._lassoTool = r, e.registerProvider(this)
        }

        var r = e("lodash/object/assign");
        t.exports = i, i.$inject = ["palette", "create", "elementFactory", "spaceTool", "lassoTool"], i.prototype.getPaletteEntries = function (e) {
            function t(e, t, n, i, s) {
                function c(t) {
                    var n = a.createShape(r({type: e}, s));
                    s && (n.businessObject.di.isExpanded = s.isExpanded), o.start(t, n)
                }

                var l = e.replace(/^bpmn\:/, "");
                return {group: t, className: n, title: i || "Create " + l, action: {dragstart: c, click: c}}
            }

            function n(e, t) {
                o.start(e, a.createParticipantShape(t))
            }

            var i = {}, o = this._create, a = this._elementFactory, s = this._spaceTool, c = this._lassoTool;
            return r(i, {
                "lasso-tool": {
                    group: "tools",
                    className: "icon-lasso-tool",
                    title: "Activate the lasso tool",
                    action: {
                        click: function (e) {
                            c.activateSelection(e)
                        }
                    }
                },
                "space-tool": {
                    group: "tools",
                    className: "icon-space-tool",
                    title: "Activate the create/remove space tool",
                    action: {
                        click: function (e) {
                            s.activateSelection(e)
                        }
                    }
                },
                "tool-separator": {group: "tools", separator: !0},
                "create.start-event": t("bpmn:StartEvent", "event", "icon-start-event-none"),
                "create.intermediate-event": t("bpmn:IntermediateThrowEvent", "event", "icon-intermediate-event-none"),
                "create.end-event": t("bpmn:EndEvent", "event", "icon-end-event-none"),
                "create.exclusive-gateway": t("bpmn:ExclusiveGateway", "gateway", "icon-gateway-xor"),
                "create.task": t("bpmn:Task", "activity", "icon-task"),
                "create.data-object": t("bpmn:DataObjectReference", "data-object", "icon-data-object"),
                "create.subprocess-expanded": t("bpmn:SubProcess", "activity", "icon-subprocess-expanded", "Create expanded SubProcess", {isExpanded: !0}),
                "create.participant-expanded": {
                    group: "collaboration",
                    className: "icon-participant",
                    title: "Create Pool/Participant",
                    action: {dragstart: n, click: n}
                }
            }), i
        }
    }, {"lodash/object/assign": 396}],
    69: [function (e, t, n) {
        t.exports = {
            __depends__: [e("diagram-js/lib/features/palette"), e("diagram-js/lib/features/create"), e("diagram-js/lib/features/space-tool"), e("diagram-js/lib/features/lasso-tool")],
            __init__: ["paletteProvider"],
            paletteProvider: ["type", e("./PaletteProvider")]
        }
    }, {
        "./PaletteProvider": 68,
        "diagram-js/lib/features/create": 151,
        "diagram-js/lib/features/lasso-tool": 163,
        "diagram-js/lib/features/palette": 195,
        "diagram-js/lib/features/space-tool": 218
    }],
    70: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i, o) {
            function l(e) {
                var r = e.canExecute.replacements;
                s(r, function (r) {
                    var s = r.oldElementId, c = {type: r.newElementType};
                    if (!e.visualReplacements[s]) {
                        var l = t.get(s);
                        a(c, {x: l.x, y: l.y});
                        var u = n.createShape(c);
                        i.addShape(u, l.parent);
                        var p = e.dragGroup.select("[data-element-id=" + l.id + "]");
                        p && p.attr({display: "none"});
                        var d = o.addDragger(e, u);
                        e.visualReplacements[s] = d, i.removeShape(u)
                    }
                })
            }

            function u(e) {
                var t = e.visualReplacements;
                s(t, function (n, i) {
                    var r = e.dragGroup.select("[data-element-id=" + i + "]");
                    r && r.attr({display: "inline"}), n.remove(), t[i] && delete t[i]
                })
            }

            r.call(this, e), e.on("shape.move.move", c, function (e) {
                var t = e.context, n = t.canExecute;
                t.visualReplacements || (t.visualReplacements = {}), n.replacements ? l(t) : u(t)
            })
        }

        var r = e("diagram-js/lib/command/CommandInterceptor"), o = e("inherits"), a = e("lodash/object/assign"), s = e("lodash/collection/forEach"), c = 250;
        i.$inject = ["eventBus", "elementRegistry", "elementFactory", "canvas", "moveVisuals"], o(i, r), t.exports = i
    }, {
        "diagram-js/lib/command/CommandInterceptor": 123,
        inherits: 115,
        "lodash/collection/forEach": 274,
        "lodash/object/assign": 396
    }],
    71: [function (e, t, n) {
        t.exports = {
            __depends__: [e("diagram-js/lib/features/move")],
            __init__: ["bpmnReplacePreview"],
            bpmnReplacePreview: ["type", e("./BpmnReplacePreview")]
        }
    }, {"./BpmnReplacePreview": 70, "diagram-js/lib/features/move": 188}],
    72: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i, c, C, A) {
            function T(n, r, o) {
                o = o || {};
                var l = r.type, u = n.businessObject, p = e.create(l), d = {type: l, businessObject: p};
                if (r.eventDefinition) {
                    var h = p.get("eventDefinitions"), f = t.create(r.eventDefinition);
                    h.push(f)
                }
                return s(p, a(r, S)), x(u, "bpmn:Activity") && (d.width = n.width, d.height = n.height), x(u, "bpmn:SubProcess") && (d.isExpanded = w(u)), p.name = u.name, _(p) || (p.loopCharacteristics = u.loopCharacteristics), (x(u, "bpmn:ExclusiveGateway") || x(u, "bpmn:InclusiveGateway")) && (x(p, "bpmn:ExclusiveGateway") || x(p, "bpmn:InclusiveGateway")) && (p["default"] = u["default"]), d = i.replaceElement(n, d), o.select !== !1 && c.select(d), d
            }

            function j(e, n) {
                var i, o = M.getLoopEntries(R);
                n.active ? i = void 0 : r(o, function (e) {
                    var r = e.options;
                    n.id === e.id && (i = t.create(r.loopCharacteristics), r.isSequential && (i.isSequential = r.isSequential))
                }), C.updateProperties(R, {loopCharacteristics: i})
            }

            function N(e) {
                R = e;
                var t, n, i, r = E(e), o = r.loopCharacteristics;
                o && (t = o.isSequential, n = void 0 === o.isSequential, i = void 0 !== o.isSequential && !o.isSequential);
                var a = [{
                    id: "toggle-parallel-mi",
                    className: "icon-parallel-mi-marker",
                    title: "Parallel Multi Instance",
                    active: i,
                    action: j,
                    options: {loopCharacteristics: "bpmn:MultiInstanceLoopCharacteristics", isSequential: !1}
                }, {
                    id: "toggle-sequential-mi",
                    className: "icon-sequential-mi-marker",
                    title: "Sequential Multi Instance",
                    active: t,
                    action: j,
                    options: {loopCharacteristics: "bpmn:MultiInstanceLoopCharacteristics", isSequential: !0}
                }, {
                    id: "toggle-loop",
                    className: "icon-loop-marker",
                    title: "Loop",
                    active: n,
                    action: j,
                    options: {loopCharacteristics: "bpmn:StandardLoopCharacteristics"}
                }];
                return a
            }

            function k(e) {
                var t = E(e), n = x(t, "bpmn:AdHocSubProcess"), i = {
                    id: "toggle-adhoc",
                    className: "icon-ad-hoc-marker",
                    title: "Ad-hoc",
                    active: n,
                    action: function (t, i) {
                        return n ? T(e, {type: "bpmn:SubProcess"}) : T(e, {type: "bpmn:AdHocSubProcess"})
                    }
                };
                return i
            }

            var R, M = this;
            this.getReplaceOptions = function (e) {
                function n(n) {
                    r(n, function (n) {
                        switch (n.actionName) {
                            case"replace-with-default-flow":
                                (E.sourceRef["default"] !== E && x(E.sourceRef, "bpmn:ExclusiveGateway") || x(E.sourceRef, "bpmn:InclusiveGateway")) && c.push(s(n, function () {
                                    C.updateProperties(e.source, {"default": E})
                                }));
                                break;
                            case"replace-with-conditional-flow":
                                !E.conditionExpression && x(E.sourceRef, "bpmn:Activity") && c.push(s(n, function () {
                                    var n = t.create("bpmn:FormalExpression", {body: ""});
                                    C.updateProperties(e, {conditionExpression: n})
                                }));
                                break;
                            default:
                                if (x(E.sourceRef, "bpmn:Activity") && E.conditionExpression)return c.push(s(n, function () {
                                    C.updateProperties(e, {conditionExpression: void 0})
                                }));
                                if ((x(E.sourceRef, "bpmn:ExclusiveGateway") || x(E.sourceRef, "bpmn:InclusiveGateway")) && E.sourceRef["default"] === E)return c.push(s(n, function () {
                                    C.updateProperties(e.source, {"default": void 0})
                                }))
                        }
                    })
                }

                function i(e) {
                    var t = e.target, n = E.eventDefinitions && E.eventDefinitions[0].$type, i = t.eventDefinition == n, r = E.$type == t.type;
                    if (x(E, "bpmn:BoundaryEvent")) {
                        if ("bpmn:CancelEventDefinition" == t.eventDefinition && !x(E.attachedToRef, "bpmn:Transaction"))return !1;
                        var o = t.cancelActivity !== !1, a = E.cancelActivity == o;
                        return !(i && r && a)
                    }
                    if (x(E, "bpmn:StartEvent") && _(E.$parent)) {
                        var s = t.isInterrupting !== !1, c = E.isInterrupting == s;
                        return !(i && i && c)
                    }
                    return x(E, "bpmn:EndEvent") && "bpmn:CancelEventDefinition" == t.eventDefinition && !x(E.$parent, "bpmn:Transaction") ? !1 : !i && r || !r
                }

                function a(e, t) {
                    var n = o(e, t);
                    r(n, function (e) {
                        var t = s(e);
                        c.push(t)
                    })
                }

                function s(t, n) {
                    var i = {label: t.label, className: t.className, id: t.actionName, action: n};
                    return i.action || (i.action = function () {
                        return T(e, t.target)
                    }), i
                }

                var c = [], E = e.businessObject;
                return x(E, "bpmn:StartEvent") && !_(E.$parent) ? a(l, i) : x(E, "bpmn:StartEvent") && _(E.$parent) ? a(y, i) : x(E, "bpmn:IntermediateCatchEvent") || x(E, "bpmn:IntermediateThrowEvent") ? a(u, i) : x(E, "bpmn:EndEvent") ? a(p, i) : x(E, "bpmn:Gateway") ? a(d, function (e) {
                    return e.target.type !== E.$type
                }) : x(E, "bpmn:Transaction") ? a(m) : _(E) && w(E) ? a(g) : x(E, "bpmn:SubProcess") && w(E) ? a(f) : x(E, "bpmn:AdHocSubProcess") && !w(E) ? a(h, function (e) {
                    return "bpmn:SubProcess" !== e.target.type
                }) : x(E, "bpmn:BoundaryEvent") ? a(v, i) : x(E, "bpmn:SequenceFlow") ? n(b) : x(E, "bpmn:FlowNode") && a(h, function (e) {
                    return e.target.type !== E.$type
                }), c
            }, this.openChooser = function (e, t) {
                var i = this.getReplaceOptions(t), r = [];
                x(t, "bpmn:Activity") && !_(t) && (r = r.concat(this.getLoopEntries(t))), !x(t, "bpmn:SubProcess") || x(t, "bpmn:Transaction") || _(t) || r.push(this.getAdHocEntry(t)), n.open({
                    className: "replace-menu",
                    element: t,
                    position: e,
                    headerEntries: r,
                    entries: i
                })
            }, this.getLoopEntries = N, this.getAdHocEntry = k, this.replaceElement = T
        }

        var r = e("lodash/collection/forEach"), o = e("lodash/collection/filter"), a = e("lodash/object/pick"), s = e("lodash/object/assign"), c = e("./ReplaceOptions"), l = c.START_EVENT, u = c.INTERMEDIATE_EVENT, p = c.END_EVENT, d = c.GATEWAY, h = c.TASK, f = c.SUBPROCESS_EXPANDED, m = c.TRANSACTION, g = c.EVENT_SUB_PROCESS, v = c.BOUNDARY_EVENT, y = c.EVENT_SUB_PROCESS_START_EVENT, b = c.SEQUENCE_FLOW, x = e("../../util/ModelUtil").is, E = e("../../util/ModelUtil").getBusinessObject, w = e("../../util/DiUtil").isExpanded, _ = e("../../util/DiUtil").isEventSubProcess, S = ["cancelActivity", "instantiate", "eventGatewayType", "triggeredByEvent", "isInterrupting"];
        i.$inject = ["bpmnFactory", "moddle", "popupMenu", "replace", "selection", "modeling", "eventBus"], t.exports = i
    }, {
        "../../util/DiUtil": 85,
        "../../util/ModelUtil": 87,
        "./ReplaceOptions": 73,
        "lodash/collection/filter": 272,
        "lodash/collection/forEach": 274,
        "lodash/object/assign": 396,
        "lodash/object/pick": 402
    }],
    73: [function (e, t, n) {
        "use strict";
        t.exports.START_EVENT = [{
            label: "Start Event",
            actionName: "replace-with-none-start",
            className: "icon-start-event-none",
            target: {type: "bpmn:StartEvent"}
        }, {
            label: "Intermediate Throw Event",
            actionName: "replace-with-none-intermediate-throwing",
            className: "icon-intermediate-event-none",
            target: {type: "bpmn:IntermediateThrowEvent"}
        }, {
            label: "End Event",
            actionName: "replace-with-none-end",
            className: "icon-end-event-none",
            target: {type: "bpmn:EndEvent"}
        }, {
            label: "Message Start Event",
            actionName: "replace-with-message-start",
            className: "icon-start-event-message",
            target: {type: "bpmn:StartEvent", eventDefinition: "bpmn:MessageEventDefinition"}
        }, {
            label: "Timer Start Event",
            actionName: "replace-with-timer-start",
            className: "icon-start-event-timer",
            target: {type: "bpmn:StartEvent", eventDefinition: "bpmn:TimerEventDefinition"}
        }, {
            label: "Conditional Start Event",
            actionName: "replace-with-conditional-start",
            className: "icon-start-event-condition",
            target: {type: "bpmn:StartEvent", eventDefinition: "bpmn:ConditionalEventDefinition"}
        }, {
            label: "Signal Start Event",
            actionName: "replace-with-signal-start",
            className: "icon-start-event-signal",
            target: {type: "bpmn:StartEvent", eventDefinition: "bpmn:SignalEventDefinition"}
        }], t.exports.INTERMEDIATE_EVENT = [{
            label: "Start Event",
            actionName: "replace-with-none-start",
            className: "icon-start-event-none",
            target: {type: "bpmn:StartEvent"}
        }, {
            label: "Intermediate Throw Event",
            actionName: "replace-with-none-intermediate-throw",
            className: "icon-intermediate-event-none",
            target: {type: "bpmn:IntermediateThrowEvent"}
        }, {
            label: "End Event",
            actionName: "replace-with-none-end",
            className: "icon-end-event-none",
            target: {type: "bpmn:EndEvent"}
        }, {
            label: "Message Intermediate Catch Event",
            actionName: "replace-with-message-intermediate-catch",
            className: "icon-intermediate-event-catch-message",
            target: {type: "bpmn:IntermediateCatchEvent", eventDefinition: "bpmn:MessageEventDefinition"}
        }, {
            label: "Message Intermediate Throw Event",
            actionName: "replace-with-message-intermediate-throw",
            className: "icon-intermediate-event-throw-message",
            target: {type: "bpmn:IntermediateThrowEvent", eventDefinition: "bpmn:MessageEventDefinition"}
        }, {
            label: "Timer Intermediate Catch Event",
            actionName: "replace-with-timer-intermediate-catch",
            className: "icon-intermediate-event-catch-timer",
            target: {type: "bpmn:IntermediateCatchEvent", eventDefinition: "bpmn:TimerEventDefinition"}
        }, {
            label: "Escalation Intermediate Catch Event",
            actionName: "replace-with-escalation-intermediate-catch",
            className: "icon-intermediate-event-catch-escalation",
            target: {type: "bpmn:IntermediateCatchEvent", eventDefinition: "bpmn:EscalationEventDefinition"}
        }, {
            label: "Conditional Intermediate Catch Event",
            actionName: "replace-with-conditional-intermediate-catch",
            className: "icon-intermediate-event-catch-condition",
            target: {type: "bpmn:IntermediateCatchEvent", eventDefinition: "bpmn:ConditionalEventDefinition"}
        }, {
            label: "Link Intermediate Catch Event",
            actionName: "replace-with-link-intermediate-catch",
            className: "icon-intermediate-event-catch-link",
            target: {type: "bpmn:IntermediateCatchEvent", eventDefinition: "bpmn:LinkEventDefinition"}
        }, {
            label: "Link Intermediate Throw Event",
            actionName: "replace-with-link-intermediate-throw",
            className: "icon-intermediate-event-throw-link",
            target: {type: "bpmn:IntermediateThrowEvent", eventDefinition: "bpmn:LinkEventDefinition"}
        }, {
            label: "Compensation Intermediate Throw Event",
            actionName: "replace-with-compensation-intermediate-throw",
            className: "icon-intermediate-event-throw-compensation",
            target: {type: "bpmn:IntermediateThrowEvent", eventDefinition: "bpmn:CompensateEventDefinition"}
        }, {
            label: "Signal Intermediate Catch Event",
            actionName: "replace-with-signal-intermediate-catch",
            className: "icon-intermediate-event-catch-signal",
            target: {type: "bpmn:IntermediateCatchEvent", eventDefinition: "bpmn:SignalEventDefinition"}
        }, {
            label: "Signal Intermediate Throw Event",
            actionName: "replace-with-signal-intermediate-throw",
            className: "icon-intermediate-event-throw-signal",
            target: {type: "bpmn:IntermediateThrowEvent", eventDefinition: "bpmn:SignalEventDefinition"}
        }], t.exports.END_EVENT = [{
            label: "Start Event",
            actionName: "replace-with-none-start",
            className: "icon-start-event-none",
            target: {type: "bpmn:StartEvent"}
        }, {
            label: "Intermediate Throw Event",
            actionName: "replace-with-none-intermediate-throw",
            className: "icon-intermediate-event-none",
            target: {type: "bpmn:IntermediateThrowEvent"}
        }, {
            label: "End Event",
            actionName: "replace-with-none-end",
            className: "icon-end-event-none",
            target: {type: "bpmn:EndEvent"}
        }, {
            label: "Message End Event",
            actionName: "replace-with-message-end",
            className: "icon-end-event-message",
            target: {type: "bpmn:EndEvent", eventDefinition: "bpmn:MessageEventDefinition"}
        }, {
            label: "Escalation End Event",
            actionName: "replace-with-escalation-end",
            className: "icon-end-event-escalation",
            target: {type: "bpmn:EndEvent", eventDefinition: "bpmn:EscalationEventDefinition"}
        }, {
            label: "Error End Event",
            actionName: "replace-with-error-end",
            className: "icon-end-event-error",
            target: {type: "bpmn:EndEvent", eventDefinition: "bpmn:ErrorEventDefinition"}
        }, {
            label: "Cancel End Event",
            actionName: "replace-with-cancel-end",
            className: "icon-end-event-cancel",
            target: {type: "bpmn:EndEvent", eventDefinition: "bpmn:CancelEventDefinition"}
        }, {
            label: "Compensation End Event",
            actionName: "replace-with-compensation-end",
            className: "icon-end-event-compensation",
            target: {type: "bpmn:EndEvent", eventDefinition: "bpmn:CompensateEventDefinition"}
        }, {
            label: "Signal End Event",
            actionName: "replace-with-signal-end",
            className: "icon-end-event-signal",
            target: {type: "bpmn:EndEvent", eventDefinition: "bpmn:SignalEventDefinition"}
        }, {
            label: "Terminate End Event",
            actionName: "replace-with-terminate-end",
            className: "icon-end-event-terminate",
            target: {type: "bpmn:EndEvent", eventDefinition: "bpmn:TerminateEventDefinition"}
        }], t.exports.GATEWAY = [{
            label: "Exclusive Gateway",
            actionName: "replace-with-exclusive-gateway",
            className: "icon-gateway-xor",
            target: {type: "bpmn:ExclusiveGateway"}
        }, {
            label: "Parallel Gateway",
            actionName: "replace-with-parallel-gateway",
            className: "icon-gateway-parallel",
            target: {type: "bpmn:ParallelGateway"}
        }, {
            label: "Inclusive Gateway",
            actionName: "replace-with-inclusive-gateway",
            className: "icon-gateway-or",
            target: {type: "bpmn:InclusiveGateway"}
        }, {
            label: "Complex Gateway",
            actionName: "replace-with-complex-gateway",
            className: "icon-gateway-complex",
            target: {type: "bpmn:ComplexGateway"}
        }, {
            label: "Event based Gateway",
            actionName: "replace-with-event-based-gateway",
            className: "icon-gateway-eventbased",
            target: {type: "bpmn:EventBasedGateway", instantiate: !1, eventGatewayType: "Exclusive"}
        }], t.exports.SUBPROCESS_EXPANDED = [{
            label: "Transaction",
            actionName: "replace-with-transaction",
            className: "icon-transaction",
            target: {type: "bpmn:Transaction", isExpanded: !0}
        }, {
            label: "Event Sub Process",
            actionName: "replace-with-event-subprocess",
            className: "icon-event-subprocess-expanded",
            target: {type: "bpmn:SubProcess", triggeredByEvent: !0, isExpanded: !0}
        }], t.exports.TRANSACTION = [{
            label: "Sub Process",
            actionName: "replace-with-subprocess",
            className: "icon-subprocess-expanded",
            target: {type: "bpmn:SubProcess", isExpanded: !0}
        }, {
            label: "Event Sub Process",
            actionName: "replace-with-event-subprocess",
            className: "icon-event-subprocess-expanded",
            target: {type: "bpmn:SubProcess", triggeredByEvent: !0, isExpanded: !0}
        }], t.exports.EVENT_SUB_PROCESS = [{
            label: "Sub Process",
            actionName: "replace-with-subprocess",
            className: "icon-subprocess-expanded",
            target: {type: "bpmn:SubProcess", isExpanded: !0}
        }, {
            label: "Transaction",
            actionName: "replace-with-transaction",
            className: "icon-transaction",
            target: {type: "bpmn:Transaction", isExpanded: !0}
        }], t.exports.TASK = [{
            label: "Task",
            actionName: "replace-with-task",
            className: "icon-task",
            target: {type: "bpmn:Task"}
        }, {
            label: "Send Task",
            actionName: "replace-with-send-task",
            className: "icon-send",
            target: {type: "bpmn:SendTask"}
        }, {
            label: "Receive Task",
            actionName: "replace-with-receive-task",
            className: "icon-receive",
            target: {type: "bpmn:ReceiveTask"}
        }, {
            label: "User Task",
            actionName: "replace-with-user-task",
            className: "icon-user",
            target: {type: "bpmn:UserTask"}
        }, {
            label: "Manual Task",
            actionName: "replace-with-manual-task",
            className: "icon-manual",
            target: {type: "bpmn:ManualTask"}
        }, {
            label: "Business Rule Task",
            actionName: "replace-with-rule-task",
            className: "icon-business-rule",
            target: {type: "bpmn:BusinessRuleTask"}
        }, {
            label: "Service Task",
            actionName: "replace-with-service-task",
            className: "icon-service",
            target: {type: "bpmn:ServiceTask"}
        }, {
            label: "Script Task",
            actionName: "replace-with-script-task",
            className: "icon-script",
            target: {type: "bpmn:ScriptTask"}
        }, {
            label: "Call Activity",
            actionName: "replace-with-call-activity",
            className: "icon-call-activity",
            target: {type: "bpmn:CallActivity"}
        }, {
            label: "Sub Process (collapsed)",
            actionName: "replace-with-collapsed-subprocess",
            className: "icon-subprocess-collapsed",
            target: {type: "bpmn:SubProcess", isExpanded: !1}
        }], t.exports.BOUNDARY_EVENT = [{
            label: "Message Boundary Event",
            actionName: "replace-with-message-boundary",
            className: "icon-intermediate-event-catch-message",
            target: {type: "bpmn:BoundaryEvent", eventDefinition: "bpmn:MessageEventDefinition"}
        }, {
            label: "Timer Boundary Event",
            actionName: "replace-with-timer-boundary",
            className: "icon-intermediate-event-catch-timer",
            target: {type: "bpmn:BoundaryEvent", eventDefinition: "bpmn:TimerEventDefinition"}
        }, {
            label: "Escalation Boundary Event",
            actionName: "replace-with-escalation-boundary",
            className: "icon-intermediate-event-catch-escalation",
            target: {type: "bpmn:BoundaryEvent", eventDefinition: "bpmn:EscalationEventDefinition"}
        }, {
            label: "Conditional Boundary Event",
            actionName: "replace-with-conditional-boundary",
            className: "icon-intermediate-event-catch-condition",
            target: {type: "bpmn:BoundaryEvent", eventDefinition: "bpmn:ConditionalEventDefinition"}
        }, {
            label: "Error Boundary Event",
            actionName: "replace-with-error-boundary",
            className: "icon-intermediate-event-catch-error",
            target: {type: "bpmn:BoundaryEvent", eventDefinition: "bpmn:ErrorEventDefinition"}
        }, {
            label: "Cancel Boundary Event",
            actionName: "replace-with-cancel-boundary",
            className: "icon-intermediate-event-catch-cancel",
            target: {type: "bpmn:BoundaryEvent", eventDefinition: "bpmn:CancelEventDefinition"}
        }, {
            label: "Signal Boundary Event",
            actionName: "replace-with-signal-boundary",
            className: "icon-intermediate-event-catch-signal",
            target: {type: "bpmn:BoundaryEvent", eventDefinition: "bpmn:SignalEventDefinition"}
        }, {
            label: "Message Boundary Event (non-interrupting)",
            actionName: "replace-with-non-interrupting-message-boundary",
            className: "icon-intermediate-event-catch-non-interrupting-message",
            target: {type: "bpmn:BoundaryEvent", eventDefinition: "bpmn:MessageEventDefinition", cancelActivity: !1}
        }, {
            label: "Timer Boundary Event (non-interrupting)",
            actionName: "replace-with-non-interrupting-timer-boundary",
            className: "icon-intermediate-event-catch-non-interrupting-timer",
            target: {type: "bpmn:BoundaryEvent", eventDefinition: "bpmn:TimerEventDefinition", cancelActivity: !1}
        }, {
            label: "Escalation Boundary Event (non-interrupting)",
            actionName: "replace-with-non-interrupting-escalation-boundary",
            className: "icon-intermediate-event-catch-non-interrupting-escalation",
            target: {type: "bpmn:BoundaryEvent", eventDefinition: "bpmn:EscalationEventDefinition", cancelActivity: !1}
        }, {
            label: "Conditional Boundary Event (non-interrupting)",
            actionName: "replace-with-non-interrupting-conditional-boundary",
            className: "icon-intermediate-event-catch-non-interrupting-condition",
            target: {type: "bpmn:BoundaryEvent", eventDefinition: "bpmn:ConditionalEventDefinition", cancelActivity: !1}
        }, {
            label: "Signal Boundary Event (non-interrupting)",
            actionName: "replace-with-non-interrupting-signal-boundary",
            className: "icon-intermediate-event-catch-non-interrupting-signal",
            target: {type: "bpmn:BoundaryEvent", eventDefinition: "bpmn:SignalEventDefinition", cancelActivity: !1}
        }], t.exports.EVENT_SUB_PROCESS_START_EVENT = [{
            label: "Message Start Event",
            actionName: "replace-with-message-start",
            className: "icon-start-event-message",
            target: {type: "bpmn:StartEvent", eventDefinition: "bpmn:MessageEventDefinition"}
        }, {
            label: "Timer Start Event",
            actionName: "replace-with-timer-start",
            className: "icon-start-event-timer",
            target: {type: "bpmn:StartEvent", eventDefinition: "bpmn:TimerEventDefinition"}
        }, {
            label: "Conditional Start Event",
            actionName: "replace-with-conditional-start",
            className: "icon-start-event-condition",
            target: {type: "bpmn:StartEvent", eventDefinition: "bpmn:ConditionalEventDefinition"}
        }, {
            label: "Signal Start Event",
            actionName: "replace-with-signal-start",
            className: "icon-start-event-signal",
            target: {type: "bpmn:StartEvent", eventDefinition: "bpmn:SignalEventDefinition"}
        }, {
            label: "Error Start Event",
            actionName: "replace-with-error-start",
            className: "icon-start-event-error",
            target: {type: "bpmn:StartEvent", eventDefinition: "bpmn:ErrorEventDefinition"}
        }, {
            label: "Escalation Start Event",
            actionName: "replace-with-escalation-start",
            className: "icon-start-event-escalation",
            target: {type: "bpmn:StartEvent", eventDefinition: "bpmn:EscalationEventDefinition"}
        }, {
            label: "Compensation Start Event",
            actionName: "replace-with-compensation-start",
            className: "icon-start-event-compensation",
            target: {type: "bpmn:StartEvent", eventDefinition: "bpmn:CompensateEventDefinition"}
        }, {
            label: "Message Start Event (non-interrupting)",
            actionName: "replace-with-non-interrupting-message-start",
            className: "icon-start-event-non-interrupting-message",
            target: {type: "bpmn:StartEvent", eventDefinition: "bpmn:MessageEventDefinition", isInterrupting: !1}
        }, {
            label: "Timer Start Event (non-interrupting)",
            actionName: "replace-with-non-interrupting-timer-start",
            className: "icon-start-event-non-interrupting-timer",
            target: {type: "bpmn:StartEvent", eventDefinition: "bpmn:TimerEventDefinition", isInterrupting: !1}
        }, {
            label: "Conditional Start Event (non-interrupting)",
            actionName: "replace-with-non-interrupting-conditional-start",
            className: "icon-start-event-non-interrupting-condition",
            target: {type: "bpmn:StartEvent", eventDefinition: "bpmn:ConditionalEventDefinition", isInterrupting: !1}
        }, {
            label: "Signal Start Event (non-interrupting)",
            actionName: "replace-with-non-interrupting-signal-start",
            className: "icon-start-event-non-interrupting-signal",
            target: {type: "bpmn:StartEvent", eventDefinition: "bpmn:SignalEventDefinition", isInterrupting: !1}
        }, {
            label: "Escalation Start Event (non-interrupting)",
            actionName: "replace-with-non-interrupting-escalation-start",
            className: "icon-start-event-non-interrupting-escalation",
            target: {type: "bpmn:StartEvent", eventDefinition: "bpmn:EscalationEventDefinition", isInterrupting: !1}
        }], t.exports.SEQUENCE_FLOW = [{
            label: "Sequence Flow",
            actionName: "replace-with-sequence-flow",
            className: "icon-connection"
        }, {
            label: "Default Flow",
            actionName: "replace-with-default-flow",
            className: "icon-default-flow"
        }, {label: "Conditional Flow", actionName: "replace-with-conditional-flow", className: "icon-conditional-flow"}]
    }, {}],
    74: [function (e, t, n) {
        t.exports = {
            __depends__: [e("diagram-js/lib/features/popup-menu"), e("diagram-js/lib/features/replace"), e("diagram-js/lib/features/selection")],
            bpmnReplace: ["type", e("./BpmnReplace")]
        }
    }, {
        "./BpmnReplace": 72,
        "diagram-js/lib/features/popup-menu": 197,
        "diagram-js/lib/features/replace": 199,
        "diagram-js/lib/features/selection": 211
    }],
    75: [function (e, t, n) {
        "use strict";
        function i(e) {
            W.call(this, e)
        }

        function r(e) {
            return !e || v(e)
        }

        function o(e, t) {
            return e === t
        }

        function a(e) {
            for (var t = $(e); t && !U(t, "bpmn:Process");) {
                if (U(t, "bpmn:Participant"))return t.processRef || t;
                t = t.$parent
            }
            return t
        }

        function s(e, t) {
            var n = a(e), i = a(t);
            return n === i
        }

        function c(e) {
            return U(e, "bpmn:InteractionNode") && (!U(e, "bpmn:Event") || U(e, "bpmn:ThrowEvent") && h(e, "bpmn:MessageEventDefinition"))
        }

        function l(e) {
            return U(e, "bpmn:InteractionNode") && (!U(e, "bpmn:Event") || U(e, "bpmn:CatchEvent") && h(e, "bpmn:MessageEventDefinition"))
        }

        function u(e) {
            var t = $(e);
            if (U(t, "bpmn:Participant"))return null;
            for (; t;)if (t = t.$parent, U(t, "bpmn:FlowElementsContainer"))return t;
            return t
        }

        function p(e, t) {
            var n = u(e), i = u(t);
            return n && n === i
        }

        function d(e, t) {
            var n = $(e);
            return !!B(n.eventDefinitions || [], function (e) {
                return U(e, t)
            })
        }

        function h(e, t) {
            var n = $(e);
            return (n.eventDefinitions || []).every(function (e) {
                return U(e, t)
            })
        }

        function f(e) {
            return U(e, "bpmn:FlowNode") && !U(e, "bpmn:EndEvent") && !q(e) && !(U(e, "bpmn:IntermediateThrowEvent") && d(e, "bpmn:LinkEventDefinition"))
        }

        function m(e) {
            return U(e, "bpmn:FlowNode") && !U(e, "bpmn:StartEvent") && !U(e, "bpmn:BoundaryEvent") && !q(e) && !(U(e, "bpmn:IntermediateCatchEvent") && d(e, "bpmn:LinkEventDefinition"))
        }

        function g(e) {
            return U(e, "bpmn:ReceiveTask") || U(e, "bpmn:IntermediateCatchEvent") && (d(e, "bpmn:MessageEventDefinition") || d(e, "bpmn:TimerEventDefinition") || d(e, "bpmn:ConditionalEventDefinition") || d(e, "bpmn:SignalEventDefinition"))
        }

        function v(e) {
            return e.labelTarget
        }

        function y(e) {
            return e.waypoints
        }

        function b(e, t) {
            var n = F(t);
            return -1 !== n.indexOf(e)
        }

        function x(e, t, n) {
            if (r(e) || r(t))return null;
            if (o(e, t))return !1;
            if (R(e, t) && !U(n, "bpmn:DataAssociation"))return {type: "bpmn:MessageFlow"};
            if (M(e, t) && !U(n, "bpmn:DataAssociation"))return {type: "bpmn:SequenceFlow"};
            var i = D(e, t);
            return !i || n && !U(n, "bpmn:DataAssociation") ? U(n, "bpmn:Association") && !U(n, "bpmn:DataAssociation") && k(e, t) ? {type: "bpmn:Association"} : !1 : i
        }

        function E(e, t, n) {
            return v(e) && !y(t) ? !0 : U(e, "bpmn:Participant") ? U(t, "bpmn:Process") || U(t, "bpmn:Collaboration") : U(e, "bpmn:Lane") ? U(t, "bpmn:Participant") || U(t, "bpmn:Lane") : U(e, "bpmn:BoundaryEvent") ? !1 : U(e, "bpmn:FlowElement") ? U(t, "bpmn:FlowElementsContainer") ? H(t) !== !1 : z(t, ["bpmn:Participant", "bpmn:Lane"]) : U(e, "bpmn:Artifact") ? z(t, ["bpmn:Collaboration", "bpmn:Lane", "bpmn:Participant", "bpmn:Process"]) : U(e, "bpmn:MessageFlow") ? U(t, "bpmn:Collaboration") : !1;
        }

        function w(e) {
            return !v(e) && U(e, "bpmn:BoundaryEvent")
        }

        function _(e) {
            return U(e, "bpmn:Lane")
        }

        function S(e) {
            return w(e) || U(e, "bpmn:IntermediateThrowEvent") && !e.parent
        }

        function C(e, t, n, i) {
            if (Array.isArray(e) || (e = [e]), n)return !1;
            if (1 !== e.length)return !1;
            var r = e[0];
            return v(r) ? !1 : S(r) ? t ? q(t) ? !1 : U(t, "bpmn:Activity") ? i && !V(i, t) ? !1 : "attach" : !1 : !0 : !1
        }

        function A(e, t, n) {
            if (!t)return !1;
            var i = {replacements: []};
            return O(e, function (e) {
                q(t) || U(e, "bpmn:StartEvent") && !G(e) && "label" !== e.type && E(e, t) && i.replacements.push({
                    oldElementId: e.id,
                    newElementType: "bpmn:StartEvent"
                }), U(t, "bpmn:Transaction") || d(e, "bpmn:CancelEventDefinition") && "label" !== e.type && (U(e, "bpmn:EndEvent") && E(e, t) && i.replacements.push({
                    oldElementId: e.id,
                    newElementType: "bpmn:EndEvent"
                }), U(e, "bpmn:BoundaryEvent") && C(e, t, null, n) && i.replacements.push({
                    oldElementId: e.id,
                    newElementType: "bpmn:BoundaryEvent"
                }))
            }), i.replacements.length ? i : !1
        }

        function T(e, t) {
            return L(e, w) ? !1 : L(e, _) ? !1 : t ? e.every(function (e) {
                return E(e, t)
            }) : !0
        }

        function j(e, t, n, i) {
            return t ? v(t) ? null : o(n, t) ? !1 : n && b(n, t) ? !1 : E(e, t, i) || P(e, t, i) : !1
        }

        function N(e, t) {
            return U(e, "bpmn:SubProcess") ? !!H(e) && (!t || t.width >= 100 && t.height >= 80) : U(e, "bpmn:Lane") ? !t || t.width >= 130 && t.height >= 60 : U(e, "bpmn:Participant") ? !t || t.width >= 250 && t.height >= 50 : U(e, "bpmn:TextAnnotation") ? !0 : !1
        }

        function k(e, t) {
            return y(e) || y(t) ? !1 : !b(t, e) && !b(e, t)
        }

        function R(e, t) {
            return c(e) && l(t) && !s(e, t)
        }

        function M(e, t) {
            return f(e) && m(t) && p(e, t) && !(U(e, "bpmn:EventBasedGateway") && !g(t))
        }

        function D(e, t) {
            return U(e, "bpmn:DataObjectReference") && z(t, ["bpmn:Activity", "bpmn:ThrowEvent"]) ? {type: "bpmn:DataInputAssociation"} : U(t, "bpmn:DataObjectReference") && z(e, ["bpmn:Activity", "bpmn:CatchEvent"]) ? {type: "bpmn:DataOutputAssociation"} : !1
        }

        function P(e, t, n) {
            return z(t, ["bpmn:SequenceFlow", "bpmn:MessageFlow"]) && U(e, "bpmn:FlowNode") && !U(e, "bpmn:BoundaryEvent") && E(e, t.parent, n)
        }

        var B = e("lodash/collection/find"), L = e("lodash/collection/any"), O = e("lodash/collection/forEach"), I = e("inherits"), F = e("../modeling/util/ModelingUtil").getParents, U = e("../../util/ModelUtil").is, z = e("../modeling/util/ModelingUtil").isAny, $ = e("../../util/ModelUtil").getBusinessObject, H = e("../../util/DiUtil").isExpanded, q = e("../../util/DiUtil").isEventSubProcess, G = e("../../util/DiUtil").isInterrupting, W = e("diagram-js/lib/features/rules/RuleProvider"), V = e("../snapping/BpmnSnappingUtil").getBoundaryAttachment;
        I(i, W), i.$inject = ["eventBus"], t.exports = i, i.prototype.init = function () {
            this.addRule("connection.create", function (e) {
                var t = e.source, n = e.target;
                return x(t, n)
            }), this.addRule("connection.reconnectStart", function (e) {
                var t = e.connection, n = e.hover || e.source, i = t.target;
                return x(n, i, t)
            }), this.addRule("connection.reconnectEnd", function (e) {
                var t = e.connection, n = t.source, i = e.hover || e.target;
                return x(n, i, t)
            }), this.addRule("connection.updateWaypoints", function (e) {
                return null
            }), this.addRule("shape.resize", function (e) {
                var t = e.shape, n = e.newBounds;
                return N(t, n)
            }), this.addRule("elements.move", function (e) {
                var t = e.target, n = e.shapes, i = e.position;
                return C(n, t, null, i) || A(n, t, i) || T(n, t, i)
            }), this.addRule(["shape.create", "shape.append"], function (e) {
                var t = e.target, n = e.shape, i = e.source, r = e.position;
                return C([n], t, i, r) || j(n, t, i, r)
            })
        }, i.prototype.canConnectMessageFlow = R, i.prototype.canConnectSequenceFlow = M, i.prototype.canConnectDataAssociation = D, i.prototype.canConnectAssociation = k, i.prototype.canMove = T, i.prototype.canAttach = C, i.prototype.canReplace = A, i.prototype.canDrop = E, i.prototype.canInsert = P, i.prototype.canCreate = j, i.prototype.canConnect = x, i.prototype.canResize = N
    }, {
        "../../util/DiUtil": 85,
        "../../util/ModelUtil": 87,
        "../modeling/util/ModelingUtil": 65,
        "../snapping/BpmnSnappingUtil": 78,
        "diagram-js/lib/features/rules/RuleProvider": 205,
        inherits: 115,
        "lodash/collection/any": 270,
        "lodash/collection/find": 273,
        "lodash/collection/forEach": 274
    }],
    76: [function (e, t, n) {
        t.exports = {
            __depends__: [e("diagram-js/lib/features/rules")],
            __init__: ["bpmnRules"],
            bpmnRules: ["type", e("./BpmnRules")]
        }
    }, {"./BpmnRules": 75, "diagram-js/lib/features/rules": 207}],
    77: [function (e, t, n) {
        "use strict";
        function i(t, n, i, s) {
            function c(e, t, n) {
                return "attach" === i.canAttach([e], t, null, n)
            }

            h.call(this, t, n), t.on("create.start", function (e) {
                var t = e.context, i = t.shape, o = n.getRootElement();
                u(i, "bpmn:Participant") && u(o, "bpmn:Process") && r(t, i, o.children)
            }), t.on(["create.move", "create.end"], 1500, function (e) {
                var t = e.context, n = t.shape, i = t.participantSnapBox;
                !x(e) && i && o(i, n, e)
            }), t.on("shape.move.start", function (e) {
                var t = e.context, i = t.shape, o = n.getRootElement();
                u(i, "bpmn:Participant") && u(o, "bpmn:Process") && r(t, i, o.children)
            }), t.on(["create.move", "create.end", "shape.move.move", "shape.move.end"], 1500, function (e) {
                var t = e.context, n = t.target, i = t.shape;
                n && !x(e) && c(i, n, e) && a(e, i, n)
            }), t.on(["shape.move.hover", "shape.move.move", "shape.move.end", "create.hover", "create.move", "create.end"], 1500, function (e) {
                var t = e.context, n = t.shape, i = e.hover;
                u(i, "bpmn:Lane") && !p(n, ["bpmn:Lane", "bpmn:Participant"]) && (e.hover = S(i), e.hoverGfx = s.getGraphics(e.hover))
            });
            var l = Math.abs, f = e("lodash/collection/filter"), g = e("lodash/object/assign");
            t.on(["create.move", "shape.move.move"], function (e) {
                var t = e.context, n = t.shape, i = t.target, r = 30;
                if (u(n, "bpmn:Lane") && p(i, ["bpmn:Lane", "bpmn:Participant"])) {
                    var o, a = f(i.children, function (e) {
                        return u(e, "bpmn:Lane")
                    }), s = e.y, c = a.reduce(function (e, t) {
                        var n = m(t);
                        return l(n.top - s) < r ? e = g(e || {}, {
                            before: {
                                element: t,
                                y: n.top
                            }
                        }) : l(n.bottom - s) < r ? e = g(e || {}, {
                            after: {
                                element: t,
                                y: n.bottom
                            }
                        }) : n.top < s && n.bottom > s && (e = l(n.top - s) > l(n.bottom - s) ? g(e || {}, {
                            after: {
                                element: t,
                                y: n.bottom
                            }
                        }) : g(e || {}, {before: {element: t, y: n.top}})), e
                    }, !1);
                    c || (o = m(i), c = l(o.top - s) < r ? {
                        before: {
                            element: i,
                            y: o.top
                        }
                    } : l(o.bottom - s) < r ? {after: {element: i, y: o.bottom}} : {
                        into: {
                            element: i,
                            y: (o.top + o.bottom) / 2
                        }
                    }), c.before && c.after ? (console.log("insert between", c.before.element.id, "and", c.after.element.id), E(e, "x", c.before.element.x + c.before.element.width / 2), E(e, "y", c.before.y)) : c.after ? (console.log("insert after", c.after.element.id), E(e, "x", c.after.element.x + c.after.element.width / 2), E(e, "y", c.after.y)) : c.before ? (console.log("insert before", c.before.element.id), E(e, "x", c.before.element.x + c.before.element.width / 2), E(e, "y", c.before.y)) : c.into && (console.log("insert into", c.into.element.id), E(e, "x", c.into.element.x + c.into.element.width / 2), E(e, "y", c.into.y))
                }
            }), t.on("resize.start", 1500, function (e) {
                var t = e.context, n = t.shape;
                u(n, "bpmn:SubProcess") && d(n) && (t.minDimensions = {
                    width: 140,
                    height: 120
                }), u(n, "bpmn:Participant") && (t.minDimensions = {
                    width: 300,
                    height: 150
                }), (u(n, "bpmn:Lane") || u(n, "bpmn:Participant")) && (t.resizeConstraints = _(n, t.direction, t.balanced)), u(n, "bpmn:TextAnnotation") && (t.minDimensions = {
                    width: 50,
                    height: 50
                })
            })
        }

        function r(e, t, n) {
            if (n.length) {
                var i = l(n.filter(function (e) {
                    return !e.labelTarget && !e.waypoints
                }));
                i.x -= 50, i.y -= 20, i.width += 70, i.height += 40, t.width = Math.max(t.width, i.width), t.height = Math.max(t.height, i.height), e.participantSnapBox = i
            }
        }

        function o(e, t, n, i) {
            i = i || 0;
            var r = t.width / 2 - i, o = t.height / 2, a = {x: n.x - r - i, y: n.y - o}, s = {
                x: n.x + r + i,
                y: n.y + o
            }, c = e, l = b(e);
            a.x >= c.x ? E(n, "x", c.x + i + r) : s.x <= l.x && E(n, "x", l.x - i - r), a.y >= c.y ? E(n, "y", c.y + o) : s.y <= l.y && E(n, "y", l.y - o)
        }

        function a(e, t, n) {
            var i = m(n), r = w(e, n);
            /top/.test(r) ? E(e, "y", i.top) : /bottom/.test(r) && E(e, "y", i.bottom), /left/.test(r) ? E(e, "x", i.left) : /right/.test(r) && E(e, "x", i.right)
        }

        var s = e("inherits"), c = e("lodash/collection/forEach"), l = e("diagram-js/lib/util/Elements").getBBox, u = e("../../util/ModelUtil").is, p = e("../modeling/util/ModelingUtil").isAny, d = e("../../util/DiUtil").isExpanded, h = e("diagram-js/lib/features/snapping/Snapping"), f = e("diagram-js/lib/features/snapping/SnapUtil"), m = e("diagram-js/lib/layout/LayoutUtil").asTRBL, g = Math.round, v = f.mid, y = f.topLeft, b = f.bottomRight, x = f.isSnapped, E = f.setSnapped, w = e("./BpmnSnappingUtil").getBoundaryAttachment, _ = e("./BpmnSnappingUtil").getParticipantSizeConstraints, S = e("../modeling/util/LaneUtil").getLanesRoot;
        s(i, h), i.$inject = ["eventBus", "canvas", "bpmnRules", "elementRegistry"], t.exports = i, i.prototype.initSnap = function (e) {
            var t, n, i, r, o, a = e.context, s = e.shape;
            o = h.prototype.initSnap.call(this, e), u(s, "bpmn:Participant") && o.setSnapLocations(["top-left", "bottom-right", "mid"]), s && (t = v(s, e), n = {
                width: s.width,
                height: s.height,
                x: isNaN(s.x) ? g(t.x - s.width / 2) : s.x,
                y: isNaN(s.y) ? g(t.y - s.height / 2) : s.y
            }, i = y(n), r = b(n), o.setSnapOrigin("top-left", {
                x: i.x - e.x,
                y: i.y - e.y
            }), o.setSnapOrigin("bottom-right", {x: r.x - e.x, y: r.y - e.y}), c(s.outgoing, function (t) {
                var n = t.waypoints[0];
                n = n.original || n, o.setSnapOrigin(t.id + "-docking", {x: n.x - e.x, y: n.y - e.y})
            }), c(s.incoming, function (t) {
                var n = t.waypoints[t.waypoints.length - 1];
                n = n.original || n, o.setSnapOrigin(t.id + "-docking", {x: n.x - e.x, y: n.y - e.y})
            }));
            var l = a.source;
            l && o.addDefaultSnap("mid", v(l))
        }, i.prototype.addTargetSnaps = function (e, t, n) {
            u(t, "bpmn:BoundaryEvent") && "label" !== t.type && (n = n.parent), u(n, "bpmn:SequenceFlow") && this.addTargetSnaps(e, t, n.parent);
            var i = this.getSiblings(t, n) || [];
            c(i, function (t) {
                u(t, "bpmn:Lane") || (e.add("mid", v(t)), u(t, "bpmn:Participant") && (e.add("top-left", y(t)), e.add("bottom-right", b(t))))
            }), c(t.incoming, function (t) {
                -1 === i.indexOf(t.source) && e.add("mid", v(t.source));
                var n = t.waypoints[0];
                e.add(t.id + "-docking", n.original || n)
            }), c(t.outgoing, function (t) {
                -1 === i.indexOf(t.target) && e.add("mid", v(t.target));
                var n = t.waypoints[t.waypoints.length - 1];
                e.add(t.id + "-docking", n.original || n)
            })
        }
    }, {
        "../../util/DiUtil": 85,
        "../../util/ModelUtil": 87,
        "../modeling/util/LaneUtil": 64,
        "../modeling/util/ModelingUtil": 65,
        "./BpmnSnappingUtil": 78,
        "diagram-js/lib/features/snapping/SnapUtil": 213,
        "diagram-js/lib/features/snapping/Snapping": 214,
        "diagram-js/lib/layout/LayoutUtil": 226,
        "diagram-js/lib/util/Elements": 239,
        inherits: 115,
        "lodash/collection/filter": 272,
        "lodash/collection/forEach": 274,
        "lodash/object/assign": 396
    }],
    78: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            var n = c(e, t, -15);
            return "intersect" !== n ? n : null
        }

        function r(e, t, n, i) {
            var r = e[t];
            e[t] = void 0 === r ? n : i(n, r)
        }

        function o(e, t, n) {
            return r(e, t, n, f)
        }

        function a(e, t, n) {
            return r(e, t, n, m)
        }

        function s(e, t, n) {
            var i = d(e), r = !0, s = !0, c = p(i, [i]), f = u(e), m = {}, w = {};
            /e/.test(t) ? w.right = f.left + v : /w/.test(t) && (w.left = f.right - v), c.forEach(function (e) {
                var i = u(e);
                /n/.test(t) && (i.top < f.top - 10 && (r = !1), n && h(f.top - i.bottom) < 10 && a(m, "top", i.top + g), h(f.top - i.top) < 5 && o(w, "top", i.bottom - g)), /s/.test(t) && (i.bottom > f.bottom + 10 && (s = !1), n && h(f.bottom - i.top) < 10 && o(m, "bottom", i.bottom - g), h(f.bottom - i.bottom) < 5 && a(w, "bottom", i.top + g))
            });
            var _ = i.children.filter(function (e) {
                return !e.hidden && !e.waypoints && (l(e, "bpmn:FlowElement") || l(e, "bpmn:Artifact"))
            });
            return _.forEach(function (e) {
                var n = u(e);
                r && /n/.test(t) && o(w, "top", n.top - x), /e/.test(t) && a(w, "right", n.right + y), s && /s/.test(t) && a(w, "bottom", n.bottom + E), /w/.test(t) && o(w, "left", n.left - b)
            }), {min: w, max: m}
        }

        var c = e("diagram-js/lib/layout/LayoutUtil").getOrientation;
        t.exports.getBoundaryAttachment = i;
        var l = e("../../util/ModelUtil").is, u = e("diagram-js/lib/layout/LayoutUtil").asTRBL, p = e("../modeling/util/LaneUtil").collectLanes, d = e("../modeling/util/LaneUtil").getLanesRoot, h = Math.abs, f = Math.min, m = Math.max, g = 60, v = 300, y = 20, b = 50, x = 20, E = 20;
        t.exports.getParticipantSizeConstraints = s
    }, {"../../util/ModelUtil": 87, "../modeling/util/LaneUtil": 64, "diagram-js/lib/layout/LayoutUtil": 226}],
    79: [function (e, t, n) {
        t.exports = {__init__: ["snapping"], snapping: ["type", e("./BpmnSnapping")]}
    }, {"./BpmnSnapping": 77}],
    80: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            return s({id: e.id, type: e.$type, businessObject: e}, t)
        }

        function r(e) {
            return c(e, function (e) {
                return {x: e.x, y: e.y}
            })
        }

        function o(e, t, n) {
            return new Error("element " + f(t) + " referenced by " + f(e) + "#" + n + " not yet drawn")
        }

        function a(e, t, n, i) {
            this._eventBus = e, this._canvas = t, this._elementFactory = n, this._elementRegistry = i
        }

        var s = e("lodash/object/assign"), c = e("lodash/collection/map"), l = e("../util/LabelUtil"), u = e("../util/ModelUtil").is, p = l.hasExternalLabel, d = l.getExternalLabelBounds, h = e("../util/DiUtil").isExpanded, f = e("./Util").elementToString;
        a.$inject = ["eventBus", "canvas", "elementFactory", "elementRegistry"], t.exports = a, a.prototype.add = function (e, t) {
            var n, o = e.di;
            if (o.$instanceOf("bpmndi:BPMNPlane"))n = this._elementFactory.createRoot(i(e)), this._canvas.setRootElement(n); else if (o.$instanceOf("bpmndi:BPMNShape")) {
                var a = !h(e), s = t && (t.hidden || t.collapsed), c = e.di.bounds;
                n = this._elementFactory.createShape(i(e, {
                    collapsed: a,
                    hidden: s,
                    x: Math.round(c.x),
                    y: Math.round(c.y),
                    width: Math.round(c.width),
                    height: Math.round(c.height)
                })), u(e, "bpmn:BoundaryEvent") && this._attachBoundary(e, n), this._canvas.addShape(n, t)
            } else {
                if (!o.$instanceOf("bpmndi:BPMNEdge"))throw new Error("unknown di " + f(o) + " for element " + f(e));
                var l = this._getSource(e), d = this._getTarget(e);
                n = this._elementFactory.createConnection(i(e, {
                    source: l,
                    target: d,
                    waypoints: r(e.di.waypoint)
                })), this._canvas.addConnection(n, t)
            }
            return p(e) && this.addLabel(e, n), this._eventBus.fire("bpmnElement.added", {element: n}), n
        }, a.prototype._attachBoundary = function (e, t) {
            var n = e.attachedToRef;
            if (!n)throw new Error("missing " + f(e) + "#attachedToRef");
            var i = this._elementRegistry.get(n.id), r = i && i.attachers;
            if (!i)throw o(e, n, "attachedToRef");
            t.host = i, r || (i.attachers = r = []), -1 === r.indexOf(t) && r.push(t)
        }, a.prototype.addLabel = function (e, t) {
            var n = d(e, t), r = this._elementFactory.createLabel(i(e, {
                id: e.id + "_label",
                labelTarget: t,
                type: "label",
                hidden: t.hidden || !e.name,
                x: Math.round(n.x),
                y: Math.round(n.y),
                width: Math.round(n.width),
                height: Math.round(n.height)
            }));
            return this._canvas.addShape(r, t.parent)
        }, a.prototype._getEnd = function (e, t) {
            var n, i, r = e.$type;
            if (i = e[t + "Ref"], "source" === t && "bpmn:DataInputAssociation" === r && (i = i && i[0]), ("source" === t && "bpmn:DataOutputAssociation" === r || "target" === t && "bpmn:DataInputAssociation" === r) && (i = e.$parent), n = i && this._getElement(i))return n;
            throw i ? o(e, i, t + "Ref") : new Error(f(e) + "#" + t + "Ref not specified")
        }, a.prototype._getSource = function (e) {
            return this._getEnd(e, "source")
        }, a.prototype._getTarget = function (e) {
            return this._getEnd(e, "target")
        }, a.prototype._getElement = function (e) {
            return this._elementRegistry.get(e.id)
        }
    }, {
        "../util/DiUtil": 85,
        "../util/LabelUtil": 86,
        "../util/ModelUtil": 87,
        "./Util": 83,
        "lodash/collection/map": 277,
        "lodash/object/assign": 396
    }],
    81: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            return e.$instanceOf(t)
        }

        function r(e) {
            return s(e.rootElements, function (e) {
                return i(e, "bpmn:Process") || i(e, "bpmn:Collaboration")
            })
        }

        function o(e) {
            function t(e, t) {
                return function (n) {
                    e(n, t)
                }
            }

            function n(e) {
                H[e.id] = e
            }

            function o(e) {
                return H[e.id]
            }

            function s(t, n) {
                var i = t.gfx;
                if (i)throw new Error("already rendered " + u(t));
                return e.element(t, n)
            }

            function l(t, n) {
                return e.root(t, n)
            }

            function d(e, t) {
                try {
                    var i = e.di && s(e, t);
                    return n(e), i
                } catch (r) {
                    h(r.message, {element: e, error: r}), console.error("failed to import " + u(e)), console.error(r)
                }
            }

            function h(t, n) {
                e.error(t, n)
            }

            function f(e) {
                var t = e.bpmnElement;
                t ? t.di ? h("multiple DI elements defined for " + u(t), {element: t}) : (p.bind(t, "di"), t.di = e) : h("no bpmnElement referenced in " + u(e), {element: e})
            }

            function m(e) {
                g(e.plane)
            }

            function g(e) {
                f(e), c(e.planeElement, v)
            }

            function v(e) {
                f(e)
            }

            function y(e, t) {
                var n = e.diagrams;
                if (t && -1 === n.indexOf(t))throw new Error("diagram not part of bpmn:Definitions");
                if (!t && n && n.length && (t = n[0]), t) {
                    m(t);
                    var o = t.plane;
                    if (!o)throw new Error("no plane for " + u(t));
                    var a = o.bpmnElement;
                    if (!a) {
                        if (a = r(e), !a)return h("no process or collaboration present to display");
                        h("correcting missing bpmnElement on " + u(o) + " to " + u(a)), o.bpmnElement = a, f(o)
                    }
                    var s = l(a, o);
                    if (i(a, "bpmn:Process"))x(a, s); else {
                        if (!i(a, "bpmn:Collaboration"))throw new Error("unsupported bpmnElement for " + u(o) + " : " + u(a));
                        z(a, s), E(e.rootElements, s)
                    }
                    b(q)
                }
            }

            function b(e) {
                c(e, function (e) {
                    e()
                })
            }

            function x(e, t) {
                I(e, t), N(e.ioSpecification, t), j(e.artifacts, t), n(e)
            }

            function E(e) {
                var n = a(e, function (e) {
                    return !o(e) && i(e, "bpmn:Process") && e.laneSets
                });
                n.forEach(t(x))
            }

            function w(e, t) {
                d(e, t)
            }

            function _(e, n) {
                c(e, t(w, n))
            }

            function S(e, t) {
                d(e, t)
            }

            function C(e, t) {
                d(e, t)
            }

            function A(e, t) {
                d(e, t)
            }

            function T(e, t) {
                d(e, t)
            }

            function j(e, t) {
                c(e, function (e) {
                    i(e, "bpmn:Association") ? q.push(function () {
                        T(e, t)
                    }) : T(e, t)
                })
            }

            function N(e, n) {
                e && (c(e.dataInputs, t(C, n)), c(e.dataOutputs, t(A, n)))
            }

            function k(e, t) {
                I(e, t), j(e.artifacts, t)
            }

            function R(e, n) {
                var r = d(e, n);
                i(e, "bpmn:SubProcess") && k(e, r || n), i(e, "bpmn:Activity") && (N(e.ioSpecification, n), q.push(function () {
                    c(e.dataInputAssociations, t(S, n)), c(e.dataOutputAssociations, t(S, n))
                }))
            }

            function M(e, t) {
                d(e, t)
            }

            function D(e, t) {
                d(e, t)
            }

            function P(e, t) {
                d(e, t)
            }

            function B(e, t) {
                var n = d(e, t);
                e.childLaneSet && L(e.childLaneSet, n || t), $(e)
            }

            function L(e, n) {
                c(e.lanes, t(B, n))
            }

            function O(e, n) {
                c(e, t(L, n))
            }

            function I(e, t) {
                e.laneSets && O(e.laneSets, t), F(e.flowElements, t)
            }

            function F(e, t) {
                c(e, function (e) {
                    i(e, "bpmn:SequenceFlow") ? q.push(function () {
                        M(e, t)
                    }) : i(e, "bpmn:BoundaryEvent") ? q.unshift(function () {
                        P(e, t)
                    }) : i(e, "bpmn:FlowNode") ? R(e, t) : i(e, "bpmn:DataObject") || (i(e, "bpmn:DataStoreReference") ? D(e, t) : i(e, "bpmn:DataObjectReference") ? D(e, t) : h("unrecognized flowElement " + u(e) + " in context " + (t ? u(t.businessObject) : null), {
                        element: e,
                        context: t
                    }))
                })
            }

            function U(e, t) {
                var n = d(e, t), i = e.processRef;
                i && x(i, n || t)
            }

            function z(e) {
                c(e.participants, t(U)), j(e.artifacts), q.push(function () {
                    _(e.messageFlows)
                })
            }

            function $(e) {
                c(e.flowNodeRef, function (t) {
                    var n = t.get("lanes");
                    n && n.push(e)
                })
            }

            var H = {}, q = [];
            return {handleDefinitions: y}
        }

        var a = e("lodash/collection/filter"), s = e("lodash/collection/find"), c = e("lodash/collection/forEach"), l = e("object-refs"), u = e("./Util").elementToString, p = new l({
            name: "bpmnElement",
            enumerable: !0
        }, {name: "di"});
        t.exports = o
    }, {
        "./Util": 83,
        "lodash/collection/filter": 272,
        "lodash/collection/find": 273,
        "lodash/collection/forEach": 274,
        "object-refs": 116
    }],
    82: [function (e, t, n) {
        "use strict";
        function i(e, t, n) {
            function i(e) {
                var t = {
                    root: function (e) {
                        return a.add(e)
                    }, element: function (e, t) {
                        return a.add(e, t)
                    }, error: function (e, t) {
                        c.push({message: e, context: t})
                    }
                }, n = new r(t);
                n.handleDefinitions(e)
            }

            var o, a = e.get("bpmnImporter"), s = e.get("eventBus"), c = [];
            s.fire("import.start");
            try {
                i(t)
            } catch (l) {
                o = l
            }
            s.fire(o ? "import.error" : "import.success", {error: o, warnings: c}), n(o, c)
        }

        var r = e("./BpmnTreeWalker");
        t.exports.importBpmnDiagram = i
    }, {"./BpmnTreeWalker": 81}],
    83: [function (e, t, n) {
        "use strict";
        t.exports.elementToString = function (e) {
            return e ? "<" + e.$type + (e.id ? ' id="' + e.id : "") + '" />' : "<null>"
        }
    }, {}],
    84: [function (e, t, n) {
        t.exports = {bpmnImporter: ["type", e("./BpmnImporter")]}
    }, {"./BpmnImporter": 80}],
    85: [function (e, t, n) {
        "use strict";
        var i = e("./ModelUtil").is, r = e("./ModelUtil").getBusinessObject;
        t.exports.isExpanded = function (e) {
            return i(e, "bpmn:CallActivity") ? !1 : i(e, "bpmn:SubProcess") ? r(e).di.isExpanded : i(e, "bpmn:Participant") ? !!r(e).processRef : !0
        }, t.exports.isInterrupting = function (e) {
            return e && r(e).isInterrupting !== !1
        }, t.exports.isEventSubProcess = function (e) {
            return e && !!r(e).triggeredByEvent
        }
    }, {"./ModelUtil": 87}],
    86: [function (e, t, n) {
        "use strict";
        function i(e) {
            var t = e.length / 2 - 1, n = e[Math.floor(t)], i = e[Math.ceil(t + .01)];
            return {x: n.x + (i.x - n.x) / 2, y: n.y + (i.y - n.y) / 2}
        }

        function r(e) {
            return e.waypoints ? i(e.waypoints) : {x: e.x + e.width / 2, y: e.y + e.height + s.height / 2}
        }

        var o = e("lodash/object/assign"), a = e("./ModelUtil").is, s = t.exports.DEFAULT_LABEL_SIZE = {
            width: 90,
            height: 20
        };
        t.exports.hasExternalLabel = function (e) {
            return a(e, "bpmn:Event") || a(e, "bpmn:Gateway") || a(e, "bpmn:DataStoreReference") || a(e, "bpmn:DataObjectReference") || a(e, "bpmn:SequenceFlow") || a(e, "bpmn:MessageFlow")
        }, t.exports.getWaypointsMid = i, t.exports.getExternalLabelMid = r, t.exports.getExternalLabelBounds = function (e, t) {
            var n, i, a, c = e.di, l = c.label;
            return l && l.bounds ? (a = l.bounds, i = {
                width: Math.max(s.width, a.width),
                height: a.height
            }, n = {x: a.x + a.width / 2, y: a.y + a.height / 2}) : (n = r(t), i = s), o({
                x: n.x - i.width / 2,
                y: n.y - i.height / 2
            }, i)
        }
    }, {"./ModelUtil": 87, "lodash/object/assign": 396}],
    87: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            var n = r(e);
            return n && "function" == typeof n.$instanceOf && n.$instanceOf(t)
        }

        function r(e) {
            return e && e.businessObject || e
        }

        t.exports.is = i, t.exports.getBusinessObject = r
    }, {}],
    88: [function (e, t, n) {
        t.exports = e("./lib/simple")
    }, {"./lib/simple": 91}],
    89: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            s.call(this, e, t)
        }

        var r = e("lodash/lang/isString"), o = e("lodash/lang/isFunction"), a = e("lodash/object/assign"), s = e("moddle"), c = e("moddle-xml/lib/reader"), l = e("moddle-xml/lib/writer");
        i.prototype = Object.create(s.prototype), t.exports = i, i.prototype.fromXML = function (e, t, n, i) {
            r(t) || (i = n, n = t, t = "bpmn:Definitions"), o(n) && (i = n, n = {});
            var s = new c(a({model: this, lax: !0}, n)), l = s.handler(t);
            s.fromXML(e, l, i)
        }, i.prototype.toXML = function (e, t, n) {
            o(t) && (n = t, t = {});
            var i = new l(t);
            try {
                var r = i.toXML(e);
                n(null, r)
            } catch (a) {
                n(a)
            }
        }
    }, {
        "lodash/lang/isFunction": 388,
        "lodash/lang/isString": 393,
        "lodash/object/assign": 396,
        moddle: 97,
        "moddle-xml/lib/reader": 93,
        "moddle-xml/lib/writer": 94
    }],
    90: [function (e, t, n) {
        "use strict";
        var i = /^(.*:)?id$/;
        t.exports.extend = function (e, t) {
            var n = e.properties.set;
            return e.ids || (e.properties.set = function (t, r, o) {
                if (i.test(r)) {
                    var a = e.ids.assigned(o);
                    if (a && a !== t)throw new Error("id <" + o + "> already used");
                    e.ids.claim(o, t)
                }
                n.call(this, t, r, o)
            }), e.ids = t, e
        }
    }, {}],
    91: [function (e, t, n) {
        "use strict";
        var i = e("lodash/object/assign"), r = e("./bpmn-moddle"), o = {
            bpmn: e("../resources/bpmn/json/bpmn.json"),
            bpmndi: e("../resources/bpmn/json/bpmndi.json"),
            dc: e("../resources/bpmn/json/dc.json"),
            di: e("../resources/bpmn/json/di.json")
        };
        t.exports = function (e, t) {
            return new r(i({}, o, e), t)
        }
    }, {
        "../resources/bpmn/json/bpmn.json": 106,
        "../resources/bpmn/json/bpmndi.json": 107,
        "../resources/bpmn/json/dc.json": 108,
        "../resources/bpmn/json/di.json": 109,
        "./bpmn-moddle": 89,
        "lodash/object/assign": 396
    }],
    92: [function (e, t, n) {
        "use strict";
        function i(e) {
            return e.charAt(0).toUpperCase() + e.slice(1)
        }

        function r(e) {
            return e.charAt(0).toLowerCase() + e.slice(1)
        }

        function o(e) {
            return e.xml && "lowerCase" === e.xml.tagAlias
        }

        function a(e) {
            return e.xml && e.xml.serialize
        }

        t.exports.aliasToName = function (e, t) {
            return o(t) ? i(e) : e
        }, t.exports.nameToAlias = function (e, t) {
            return o(t) ? r(e) : e
        }, t.exports.DEFAULT_NS_MAP = {xsi: "http://www.w3.org/2001/XMLSchema-instance"};
        var s = t.exports.XSI_TYPE = "xsi:type";
        t.exports.serializeAsType = function (e) {
            return a(e) === s
        }, t.exports.serializeAsProperty = function (e) {
            return "property" === a(e)
        }
    }, {}],
    93: [function (e, t, n) {
        "use strict";
        function i(e) {
            var t = e.attributes;
            return g(t, function (e, t, n) {
                var i, r;
                return t.local ? (r = S(t.name, t.prefix), i = r.name) : i = t.prefix, e[i] = t.value, e
            }, {})
        }

        function r(e, t, n) {
            var i, r = S(t.value), o = e.ns[r.prefix || ""], a = r.localName, s = o && n.getPackage(o);
            s && (i = s.xml && s.xml.typePrefix, i && 0 === a.indexOf(i) && (a = a.slice(i.length)), t.value = s.prefix + ":" + a)
        }

        function o(e, t, n) {
            var i, a;
            if (i = e.uri || n) {
                var s = t.getPackage(i);
                a = s ? s.prefix : e.prefix, e.prefix = a, e.uri = i
            }
            v(e.attributes, function (n) {
                n.uri === k && "type" === n.local && r(e, n, t), o(n, t, null)
            })
        }

        function a(e) {
            b(this, e);
            var t = this.elementsById = {}, n = this.references = [], i = this.warnings = [];
            this.addReference = function (e) {
                n.push(e)
            }, this.addElement = function (e, n) {
                if (!e || !n)throw new Error("[xml-reader] id or ctx must not be null");
                t[e] = n
            }, this.addWarning = function (e) {
                i.push(e)
            }
        }

        function s() {
        }

        function c() {
        }

        function l() {
        }

        function u(e, t) {
            this.property = e, this.context = t
        }

        function p(e, t) {
            this.element = t, this.propertyDesc = e
        }

        function d() {
        }

        function h(e, t, n) {
            this.model = e, this.type = e.getType(t), this.context = n
        }

        function f(e, t, n) {
            this.model = e, this.context = n
        }

        function m(e) {
            e instanceof _ && (e = {model: e}), b(this, {lax: !1}, e)
        }

        var g = e("lodash/collection/reduce"), v = e("lodash/collection/forEach"), y = e("lodash/collection/find"), b = e("lodash/object/assign"), x = e("lodash/function/defer"), E = e("tiny-stack"), w = e("sax").parser, _ = e("moddle"), S = e("moddle/lib/ns").parseName, C = e("moddle/lib/types"), A = C.coerceType, T = C.isSimple, j = e("./common"), N = j.XSI_TYPE, k = j.DEFAULT_NS_MAP.xsi, R = j.serializeAsType, M = j.aliasToName;
        s.prototype.handleEnd = function () {
        }, s.prototype.handleText = function () {
        }, s.prototype.handleNode = function () {
        }, c.prototype = new s, c.prototype.handleNode = function () {
            return this
        }, l.prototype = new s, l.prototype.handleText = function (e) {
            this.body = (this.body || "") + e
        }, u.prototype = new l, u.prototype.handleNode = function (e) {
            if (this.element)throw new Error("expected no sub nodes");
            return this.element = this.createReference(e), this
        }, u.prototype.handleEnd = function () {
            this.element.id = this.body
        }, u.prototype.createReference = function (e) {
            return {property: this.property.ns.name, id: ""}
        }, p.prototype = new l, p.prototype.handleEnd = function () {
            var e = this.body, t = this.element, n = this.propertyDesc;
            e = A(n.type, e), n.isMany ? t.get(n.name).push(e) : t.set(n.name, e)
        }, d.prototype = Object.create(l.prototype), d.prototype.handleNode = function (e) {
            var t, n = this, i = this.element;
            return i ? n = this.handleChild(e) : (i = this.element = this.createElement(e), t = i.id, t && this.context.addElement(t, i)), n
        }, h.prototype = new d, h.prototype.addReference = function (e) {
            this.context.addReference(e)
        }, h.prototype.handleEnd = function () {
            var e = this.body, t = this.element, n = t.$descriptor, i = n.bodyProperty;
            i && void 0 !== e && (e = A(i.type, e), t.set(i.name, e))
        }, h.prototype.createElement = function (e) {
            var t = i(e), n = this.type, r = n.$descriptor, o = this.context, a = new n({});
            return v(t, function (e, t) {
                var n, i = r.propertiesByName[t];
                i && i.isReference ? i.isMany ? (n = e.split(" "), v(n, function (e) {
                    o.addReference({element: a, property: i.ns.name, id: e})
                })) : o.addReference({element: a, property: i.ns.name, id: e}) : (i && (e = A(i.type, e)), a.set(t, e))
            }), a
        }, h.prototype.getPropertyForNode = function (e) {
            var t, n, i, r = S(e.local, e.prefix), o = this.type, a = this.model, s = o.$descriptor, c = r.name, l = s.propertiesByName[c];
            if (l)return R(l) && (i = e.attributes[N]) ? (t = i.value, n = a.getType(t), b({}, l, {effectiveType: n.$descriptor.name})) : l;
            var u = a.getPackage(r.prefix);
            if (u) {
                if (t = r.prefix + ":" + M(r.localName, s.$pkg), n = a.getType(t), l = y(s.properties, function (e) {
                        return !e.isVirtual && !e.isReference && !e.isAttribute && n.hasType(e.type)
                    }))return b({}, l, {effectiveType: n.$descriptor.name})
            } else if (l = y(s.properties, function (e) {
                    return !e.isReference && !e.isAttribute && "Element" === e.type
                }))return l;
            throw new Error("unrecognized element <" + r.name + ">")
        }, h.prototype.toString = function () {
            return "ElementDescriptor[" + this.type.$descriptor.name + "]"
        }, h.prototype.valueHandler = function (e, t) {
            return new p(e, t)
        }, h.prototype.referenceHandler = function (e) {
            return new u(e, this.context)
        }, h.prototype.handler = function (e) {
            return "Element" === e ? new f(this.model, e, this.context) : new h(this.model, e, this.context)
        }, h.prototype.handleChild = function (e) {
            var t, n, i, r;
            if (t = this.getPropertyForNode(e), i = this.element, n = t.effectiveType || t.type, T(n))return this.valueHandler(t, i);
            r = t.isReference ? this.referenceHandler(t).handleNode(e) : this.handler(n).handleNode(e);
            var o = r.element;
            return void 0 !== o && (t.isMany ? i.get(t.name).push(o) : i.set(t.name, o), t.isReference ? (b(o, {element: i}), this.context.addReference(o)) : o.$parent = i), r
        }, f.prototype = Object.create(d.prototype), f.prototype.createElement = function (e) {
            var t = e.name, n = e.prefix, i = e.ns[n], r = e.attributes;
            return this.model.createAny(t, i, r)
        }, f.prototype.handleChild = function (e) {
            var t, n = new f(this.model, "Element", this.context).handleNode(e), i = this.element, r = n.element;
            return void 0 !== r && (t = i.$children = i.$children || [], t.push(r), r.$parent = i), n
        }, f.prototype.handleText = function (e) {
            this.body = this.body || "" + e
        }, f.prototype.handleEnd = function () {
            this.body && (this.element.$body = this.body)
        }, m.prototype.fromXML = function (e, t, n) {
            function i() {
                var e, t, n = d.elementsById, i = d.references;
                for (e = 0; t = i[e]; e++) {
                    var r = t.element, o = n[t.id], a = r.$descriptor.propertiesByName[t.property];
                    if (o || d.addWarning({
                            message: "unresolved reference <" + t.id + ">",
                            element: t.element,
                            property: t.property,
                            value: t.id
                        }), a.isMany) {
                        var s = r.get(a.name), c = s.indexOf(t);
                        -1 === c && (c = s.length), o ? s[c] = o : s.splice(c, 1)
                    } else r.set(a.name, o)
                }
            }

            function r(e) {
                f.pop().handleEnd()
            }

            function s(e) {
                var t = f.peek();
                o(e, u);
                try {
                    f.push(t.handleNode(e))
                } catch (n) {
                    var i = this.line, r = this.column, a = "unparsable content <" + e.name + "> detected\n	line: " + i + "\n	column: " + r + "\n	nested error: " + n.message;
                    if (!p)throw console.error("could not parse document"), console.error(n), new Error(a);
                    d.addWarning({
                        message: a,
                        error: n
                    }), console.warn("could not parse node"), console.warn(n), f.push(new c)
                }
            }

            function l(e) {
                f.peek().handleText(e)
            }

            var u = this.model, p = this.lax, d = new a({parseRoot: t}), h = new w(!0, {
                xmlns: !0,
                trim: !0
            }), f = new E;
            t.context = d, f.push(t), h.onopentag = s, h.oncdata = h.ontext = l, h.onclosetag = r, h.onend = i, x(function () {
                var i;
                try {
                    h.write(e).close()
                } catch (r) {
                    i = r
                }
                n(i, i ? void 0 : t.element, d)
            })
        }, m.prototype.handler = function (e) {
            return new h(this.model, e)
        }, t.exports = m, t.exports.ElementHandler = h
    }, {
        "./common": 92,
        "lodash/collection/find": 273,
        "lodash/collection/forEach": 274,
        "lodash/collection/reduce": 278,
        "lodash/function/defer": 285,
        "lodash/object/assign": 396,
        moddle: 97,
        "moddle/lib/ns": 102,
        "moddle/lib/types": 105,
        sax: 95,
        "tiny-stack": 96
    }],
    94: [function (e, t, n) {
        "use strict";
        function i(e) {
            return x(e) ? e : (e.prefix ? e.prefix + ":" : "") + e.localName
        }

        function r(e, t) {
            return t.isGeneric ? t.name : w({localName: A(t.ns.localName, t.$pkg)}, e)
        }

        function o(e, t) {
            return w({localName: t.ns.localName}, e)
        }

        function a(e) {
            var t = e.$descriptor;
            return E(t.properties, function (t) {
                var n = t.name;
                if (t.isVirtual)return !1;
                if (!e.hasOwnProperty(n))return !1;
                var i = e[n];
                return i === t["default"] ? !1 : t.isMany ? i.length : !0
            })
        }

        function s(e) {
            return e = x(e) ? e : "" + e, e.replace(k, function (e) {
                return "&#" + D[e] + ";"
            })
        }

        function c(e) {
            return E(e, function (e) {
                return e.isAttr
            })
        }

        function l(e) {
            return E(e, function (e) {
                return !e.isAttr
            })
        }

        function u(e, t) {
            this.ns = t
        }

        function p() {
        }

        function d(e) {
            this.ns = e
        }

        function h(e, t) {
            this.body = [], this.attrs = [], this.parent = e, this.ns = t
        }

        function f(e, t) {
            h.call(this, e, t)
        }

        function m() {
            this.value = "", this.write = function (e) {
                this.value += e
            }
        }

        function g(e, t) {
            var n = [""];
            this.append = function (t) {
                return e.write(t), this
            }, this.appendNewLine = function () {
                return t && e.write("\n"), this
            }, this.appendIndent = function () {
                return t && e.write(n.join("  ")), this
            }, this.indent = function () {
                return n.push(""), this
            }, this.unindent = function () {
                return n.pop(), this
            }
        }

        function v(e) {
            function t(t, n) {
                var i = n || new m, r = new g(i, e.format);
                return e.preamble && r.append(N), (new h).build(t).serializeTo(r), n ? void 0 : i.value
            }

            return e = w({format: !1, preamble: !0}, e || {}), {toXML: t}
        }

        var y = e("lodash/collection/map"), b = e("lodash/collection/forEach"), x = e("lodash/lang/isString"), E = e("lodash/collection/filter"), w = e("lodash/object/assign"), _ = e("moddle/lib/types"), S = e("moddle/lib/ns").parseName, C = e("./common"), A = C.nameToAlias, T = C.serializeAsType, j = C.serializeAsProperty, N = '<?xml version="1.0" encoding="UTF-8"?>\n', k = /(<|>|'|"|&|\n\r|\n)/g, R = C.DEFAULT_NS_MAP, M = C.XSI_TYPE, D = {
            "\n": "10",
            "\n\r": "10",
            '"': "34",
            "'": "39",
            "<": "60",
            ">": "62",
            "&": "38"
        };
        u.prototype.build = function (e) {
            return this.element = e, this
        }, u.prototype.serializeTo = function (e) {
            e.appendIndent().append("<" + i(this.ns) + ">" + this.element.id + "</" + i(this.ns) + ">").appendNewLine()
        }, p.prototype.serializeValue = p.prototype.serializeTo = function (e) {
            var t = this.escape;
            t && e.append("<![CDATA["), e.append(this.value), t && e.append("]]>")
        }, p.prototype.build = function (e, t) {
            return this.value = t, "String" === e.type && k.test(t) && (this.escape = !0), this
        }, d.prototype = new p, d.prototype.serializeTo = function (e) {
            e.appendIndent().append("<" + i(this.ns) + ">"), this.serializeValue(e), e.append("</" + i(this.ns) + ">").appendNewLine()
        }, h.prototype.build = function (e) {
            this.element = e;
            var t = this.parseNsAttributes(e);
            if (this.ns || (this.ns = this.nsTagName(e.$descriptor)), e.$descriptor.isGeneric)this.parseGeneric(e); else {
                var n = a(e);
                this.parseAttributes(c(n)), this.parseContainments(l(n)), this.parseGenericAttributes(e, t)
            }
            return this
        }, h.prototype.nsTagName = function (e) {
            var t = this.logNamespaceUsed(e.ns);
            return r(t, e)
        }, h.prototype.nsPropertyTagName = function (e) {
            var t = this.logNamespaceUsed(e.ns);
            return o(t, e)
        }, h.prototype.isLocalNs = function (e) {
            return e.uri === this.ns.uri
        }, h.prototype.nsAttributeName = function (e) {
            var t;
            x(e) ? t = S(e) : e.ns && (t = e.ns);
            var n = this.logNamespaceUsed(t);
            return this.isLocalNs(n) ? {localName: t.localName} : w({localName: t.localName}, n)
        }, h.prototype.parseGeneric = function (e) {
            var t = this, n = this.body, i = this.attrs;
            b(e, function (e, r) {
                "$body" === r ? n.push((new p).build({type: "String"}, e)) : "$children" === r ? b(e, function (e) {
                    n.push(new h(t).build(e))
                }) : 0 !== r.indexOf("$") && i.push({name: r, value: s(e)})
            })
        }, h.prototype.parseNsAttributes = function (e) {
            var t = this, n = e.$attrs, i = [];
            return b(n, function (e, n) {
                var r = S(n);
                "xmlns" === r.prefix ? t.logNamespace({
                    prefix: r.localName,
                    uri: e
                }) : r.prefix || "xmlns" !== r.localName ? i.push({name: n, value: e}) : t.logNamespace({uri: e})
            }), i
        }, h.prototype.parseGenericAttributes = function (e, t) {
            var n = this;
            b(t, function (t) {
                if (t.name !== M)try {
                    n.addAttribute(n.nsAttributeName(t.name), t.value);
                } catch (i) {
                    console.warn("[writer] missing namespace information for ", t.name, "=", t.value, "on", e, i)
                }
            })
        }, h.prototype.parseContainments = function (e) {
            var t = this, n = this.body, i = this.element;
            b(e, function (e) {
                var r = i.get(e.name), o = e.isReference, a = e.isMany, s = t.nsPropertyTagName(e);
                if (a || (r = [r]), e.isBody)n.push((new p).build(e, r[0])); else if (_.isSimple(e.type))b(r, function (t) {
                    n.push(new d(s).build(e, t))
                }); else if (o)b(r, function (e) {
                    n.push(new u(t, s).build(e))
                }); else {
                    var c = T(e), l = j(e);
                    b(r, function (e) {
                        var i;
                        i = c ? new f(t, s) : l ? new h(t, s) : new h(t), n.push(i.build(e))
                    })
                }
            })
        }, h.prototype.getNamespaces = function () {
            return this.parent ? this.namespaces = this.parent.getNamespaces() : this.namespaces || (this.namespaces = {
                prefixMap: {},
                uriMap: {},
                used: {}
            }), this.namespaces
        }, h.prototype.logNamespace = function (e) {
            var t = this.getNamespaces(), n = t.uriMap[e.uri];
            return n || (t.uriMap[e.uri] = e), t.prefixMap[e.prefix] = e.uri, e
        }, h.prototype.logNamespaceUsed = function (e) {
            var t = this.element, n = t.$model, i = this.getNamespaces(), r = e.prefix, o = e.uri || R[r] || i.prefixMap[r] || (n ? (n.getPackage(r) || {}).uri : null);
            if (!o)throw new Error("no namespace uri given for prefix <" + e.prefix + ">");
            return e = i.uriMap[o], e || (e = this.logNamespace({
                prefix: r,
                uri: o
            })), i.used[e.uri] || (i.used[e.uri] = e), e
        }, h.prototype.parseAttributes = function (e) {
            var t = this, n = this.element;
            b(e, function (e) {
                t.logNamespaceUsed(e.ns);
                var i = n.get(e.name);
                if (e.isReference)if (e.isMany) {
                    var r = [];
                    b(i, function (e) {
                        r.push(e.id)
                    }), i = r.join(" ")
                } else i = i.id;
                t.addAttribute(t.nsAttributeName(e), i)
            })
        }, h.prototype.addAttribute = function (e, t) {
            var n = this.attrs;
            x(t) && (t = s(t)), n.push({name: e, value: t})
        }, h.prototype.serializeAttributes = function (e) {
            function t() {
                return y(o.used, function (e) {
                    var t = "xmlns" + (e.prefix ? ":" + e.prefix : "");
                    return {name: t, value: e.uri}
                })
            }

            var n = this.attrs, r = !this.parent, o = this.namespaces;
            r && (n = t().concat(n)), b(n, function (t) {
                e.append(" ").append(i(t.name)).append('="').append(t.value).append('"')
            })
        }, h.prototype.serializeTo = function (e) {
            var t = this.body.length, n = !(1 === this.body.length && this.body[0]instanceof p);
            e.appendIndent().append("<" + i(this.ns)), this.serializeAttributes(e), e.append(t ? ">" : " />"), t && (n && e.appendNewLine().indent(), b(this.body, function (t) {
                t.serializeTo(e)
            }), n && e.unindent().appendIndent(), e.append("</" + i(this.ns) + ">")), e.appendNewLine()
        }, f.prototype = new h, f.prototype.build = function (e) {
            var t = e.$descriptor;
            this.element = e, this.typeNs = this.nsTagName(t);
            var n = this.typeNs, i = e.$model.getPackage(n.uri), r = i.xml && i.xml.typePrefix || "";
            return this.addAttribute(this.nsAttributeName(M), (n.prefix ? n.prefix + ":" : "") + r + t.ns.localName), h.prototype.build.call(this, e)
        }, f.prototype.isLocalNs = function (e) {
            return e.uri === this.typeNs.uri
        }, t.exports = v
    }, {
        "./common": 92,
        "lodash/collection/filter": 272,
        "lodash/collection/forEach": 274,
        "lodash/collection/map": 277,
        "lodash/lang/isString": 393,
        "lodash/object/assign": 396,
        "moddle/lib/ns": 102,
        "moddle/lib/types": 105
    }],
    95: [function (e, t, n) {
        (function (t) {
            !function (n) {
                function i(e, t) {
                    if (!(this instanceof i))return new i(e, t);
                    var r = this;
                    o(r), r.q = r.c = "", r.bufferCheckPosition = n.MAX_BUFFER_LENGTH, r.opt = t || {}, r.opt.lowercase = r.opt.lowercase || r.opt.lowercasetags, r.looseCase = r.opt.lowercase ? "toLowerCase" : "toUpperCase", r.tags = [], r.closed = r.closedRoot = r.sawRoot = !1, r.tag = r.error = null, r.strict = !!e, r.noscript = !(!e && !r.opt.noscript), r.state = q.BEGIN, r.ENTITIES = Object.create(n.ENTITIES), r.attribList = [], r.opt.xmlns && (r.ns = Object.create(z)), r.trackPosition = r.opt.position !== !1, r.trackPosition && (r.position = r.line = r.column = 0), h(r, "onready")
                }

                function r(e) {
                    for (var t = Math.max(n.MAX_BUFFER_LENGTH, 10), i = 0, r = 0, o = T.length; o > r; r++) {
                        var a = e[T[r]].length;
                        if (a > t)switch (T[r]) {
                            case"textNode":
                                m(e);
                                break;
                            case"cdata":
                                f(e, "oncdata", e.cdata), e.cdata = "";
                                break;
                            case"script":
                                f(e, "onscript", e.script), e.script = "";
                                break;
                            default:
                                v(e, "Max buffer length exceeded: " + T[r])
                        }
                        i = Math.max(i, a)
                    }
                    e.bufferCheckPosition = n.MAX_BUFFER_LENGTH - i + e.position
                }

                function o(e) {
                    for (var t = 0, n = T.length; n > t; t++)e[T[t]] = ""
                }

                function a(e) {
                    m(e), "" !== e.cdata && (f(e, "oncdata", e.cdata), e.cdata = ""), "" !== e.script && (f(e, "onscript", e.script), e.script = "")
                }

                function s(e, t) {
                    return new c(e, t)
                }

                function c(e, t) {
                    if (!(this instanceof c))return new c(e, t);
                    j.apply(this), this._parser = new i(e, t), this.writable = !0, this.readable = !0;
                    var n = this;
                    this._parser.onend = function () {
                        n.emit("end")
                    }, this._parser.onerror = function (e) {
                        n.emit("error", e), n._parser.error = null
                    }, this._decoder = null, k.forEach(function (e) {
                        Object.defineProperty(n, "on" + e, {
                            get: function () {
                                return n._parser["on" + e]
                            }, set: function (t) {
                                return t ? void n.on(e, t) : (n.removeAllListeners(e), n._parser["on" + e] = t)
                            }, enumerable: !0, configurable: !1
                        })
                    })
                }

                function l(e) {
                    return e.split("").reduce(function (e, t) {
                        return e[t] = !0, e
                    }, {})
                }

                function u(e) {
                    return "[object RegExp]" === Object.prototype.toString.call(e)
                }

                function p(e, t) {
                    return u(e) ? !!t.match(e) : e[t]
                }

                function d(e, t) {
                    return !p(e, t)
                }

                function h(e, t, n) {
                    e[t] && e[t](n)
                }

                function f(e, t, n) {
                    e.textNode && m(e), h(e, t, n)
                }

                function m(e) {
                    e.textNode = g(e.opt, e.textNode), e.textNode && h(e, "ontext", e.textNode), e.textNode = ""
                }

                function g(e, t) {
                    return e.trim && (t = t.trim()), e.normalize && (t = t.replace(/\s+/g, " ")), t
                }

                function v(e, t) {
                    return m(e), e.trackPosition && (t += "\nLine: " + e.line + "\nColumn: " + e.column + "\nChar: " + e.c), t = new Error(t), e.error = t, h(e, "onerror", t), e
                }

                function y(e) {
                    return e.closedRoot || b(e, "Unclosed root tag"), e.state !== q.BEGIN && e.state !== q.TEXT && v(e, "Unexpected end"), m(e), e.c = "", e.closed = !0, h(e, "onend"), i.call(e, e.strict, e.opt), e
                }

                function b(e, t) {
                    if ("object" != typeof e || !(e instanceof i))throw new Error("bad call to strictFail");
                    e.strict && v(e, t)
                }

                function x(e) {
                    e.strict || (e.tagName = e.tagName[e.looseCase]());
                    var t = e.tags[e.tags.length - 1] || e, n = e.tag = {name: e.tagName, attributes: {}};
                    e.opt.xmlns && (n.ns = t.ns), e.attribList.length = 0
                }

                function E(e, t) {
                    var n = e.indexOf(":"), i = 0 > n ? ["", e] : e.split(":"), r = i[0], o = i[1];
                    return t && "xmlns" === e && (r = "xmlns", o = ""), {prefix: r, local: o}
                }

                function w(e) {
                    if (e.strict || (e.attribName = e.attribName[e.looseCase]()), -1 !== e.attribList.indexOf(e.attribName) || e.tag.attributes.hasOwnProperty(e.attribName))return e.attribName = e.attribValue = "";
                    if (e.opt.xmlns) {
                        var t = E(e.attribName, !0), n = t.prefix, i = t.local;
                        if ("xmlns" === n)if ("xml" === i && e.attribValue !== F)b(e, "xml: prefix must be bound to " + F + "\nActual: " + e.attribValue); else if ("xmlns" === i && e.attribValue !== U)b(e, "xmlns: prefix must be bound to " + U + "\nActual: " + e.attribValue); else {
                            var r = e.tag, o = e.tags[e.tags.length - 1] || e;
                            r.ns === o.ns && (r.ns = Object.create(o.ns)), r.ns[i] = e.attribValue
                        }
                        e.attribList.push([e.attribName, e.attribValue])
                    } else e.tag.attributes[e.attribName] = e.attribValue, f(e, "onattribute", {
                        name: e.attribName,
                        value: e.attribValue
                    });
                    e.attribName = e.attribValue = ""
                }

                function _(e, t) {
                    if (e.opt.xmlns) {
                        var n = e.tag, i = E(e.tagName);
                        n.prefix = i.prefix, n.local = i.local, n.uri = n.ns[i.prefix] || "", n.prefix && !n.uri && (b(e, "Unbound namespace prefix: " + JSON.stringify(e.tagName)), n.uri = i.prefix);
                        var r = e.tags[e.tags.length - 1] || e;
                        n.ns && r.ns !== n.ns && Object.keys(n.ns).forEach(function (t) {
                            f(e, "onopennamespace", {prefix: t, uri: n.ns[t]})
                        });
                        for (var o = 0, a = e.attribList.length; a > o; o++) {
                            var s = e.attribList[o], c = s[0], l = s[1], u = E(c, !0), p = u.prefix, d = u.local, h = "" == p ? "" : n.ns[p] || "", m = {
                                name: c,
                                value: l,
                                prefix: p,
                                local: d,
                                uri: h
                            };
                            p && "xmlns" != p && !h && (b(e, "Unbound namespace prefix: " + JSON.stringify(p)), m.uri = p), e.tag.attributes[c] = m, f(e, "onattribute", m)
                        }
                        e.attribList.length = 0
                    }
                    e.tag.isSelfClosing = !!t, e.sawRoot = !0, e.tags.push(e.tag), f(e, "onopentag", e.tag), t || (e.noscript || "script" !== e.tagName.toLowerCase() ? e.state = q.TEXT : e.state = q.SCRIPT, e.tag = null, e.tagName = ""), e.attribName = e.attribValue = "", e.attribList.length = 0
                }

                function S(e) {
                    if (!e.tagName)return b(e, "Weird empty close tag."), e.textNode += "</>", void(e.state = q.TEXT);
                    if (e.script) {
                        if ("script" !== e.tagName)return e.script += "</" + e.tagName + ">", e.tagName = "", void(e.state = q.SCRIPT);
                        f(e, "onscript", e.script), e.script = ""
                    }
                    var t = e.tags.length, n = e.tagName;
                    e.strict || (n = n[e.looseCase]());
                    for (var i = n; t--;) {
                        var r = e.tags[t];
                        if (r.name === i)break;
                        b(e, "Unexpected close tag")
                    }
                    if (0 > t)return b(e, "Unmatched closing tag: " + e.tagName), e.textNode += "</" + e.tagName + ">", void(e.state = q.TEXT);
                    e.tagName = n;
                    for (var o = e.tags.length; o-- > t;) {
                        var a = e.tag = e.tags.pop();
                        e.tagName = e.tag.name, f(e, "onclosetag", e.tagName);
                        var s = {};
                        for (var c in a.ns)s[c] = a.ns[c];
                        var l = e.tags[e.tags.length - 1] || e;
                        e.opt.xmlns && a.ns !== l.ns && Object.keys(a.ns).forEach(function (t) {
                            var n = a.ns[t];
                            f(e, "onclosenamespace", {prefix: t, uri: n})
                        })
                    }
                    0 === t && (e.closedRoot = !0), e.tagName = e.attribValue = e.attribName = "", e.attribList.length = 0, e.state = q.TEXT
                }

                function C(e) {
                    var t, n = e.entity, i = n.toLowerCase(), r = "";
                    return e.ENTITIES[n] ? e.ENTITIES[n] : e.ENTITIES[i] ? e.ENTITIES[i] : (n = i, "#" === n.charAt(0) && ("x" === n.charAt(1) ? (n = n.slice(2), t = parseInt(n, 16), r = t.toString(16)) : (n = n.slice(1), t = parseInt(n, 10), r = t.toString(10))), n = n.replace(/^0+/, ""), r.toLowerCase() !== n ? (b(e, "Invalid character entity"), "&" + e.entity + ";") : String.fromCodePoint(t))
                }

                function A(e) {
                    var t = this;
                    if (this.error)throw this.error;
                    if (t.closed)return v(t, "Cannot write after close. Assign an onready handler.");
                    if (null === e)return y(t);
                    for (var n = 0, i = ""; t.c = i = e.charAt(n++);)switch (t.trackPosition && (t.position++, "\n" === i ? (t.line++, t.column = 0) : t.column++), t.state) {
                        case q.BEGIN:
                            "<" === i ? (t.state = q.OPEN_WAKA, t.startTagPosition = t.position) : d(R, i) && (b(t, "Non-whitespace before first tag."), t.textNode = i, t.state = q.TEXT);
                            continue;
                        case q.TEXT:
                            if (t.sawRoot && !t.closedRoot) {
                                for (var o = n - 1; i && "<" !== i && "&" !== i;)i = e.charAt(n++), i && t.trackPosition && (t.position++, "\n" === i ? (t.line++, t.column = 0) : t.column++);
                                t.textNode += e.substring(o, n - 1)
                            }
                            "<" === i ? (t.state = q.OPEN_WAKA, t.startTagPosition = t.position) : (!d(R, i) || t.sawRoot && !t.closedRoot || b(t, "Text data outside of root node."), "&" === i ? t.state = q.TEXT_ENTITY : t.textNode += i);
                            continue;
                        case q.SCRIPT:
                            "<" === i ? t.state = q.SCRIPT_ENDING : t.script += i;
                            continue;
                        case q.SCRIPT_ENDING:
                            "/" === i ? t.state = q.CLOSE_TAG : (t.script += "<" + i, t.state = q.SCRIPT);
                            continue;
                        case q.OPEN_WAKA:
                            if ("!" === i)t.state = q.SGML_DECL, t.sgmlDecl = ""; else if (p(R, i)); else if (p($, i))t.state = q.OPEN_TAG, t.tagName = i; else if ("/" === i)t.state = q.CLOSE_TAG, t.tagName = ""; else if ("?" === i)t.state = q.PROC_INST, t.procInstName = t.procInstBody = ""; else {
                                if (b(t, "Unencoded <"), t.startTagPosition + 1 < t.position) {
                                    var a = t.position - t.startTagPosition;
                                    i = new Array(a).join(" ") + i
                                }
                                t.textNode += "<" + i, t.state = q.TEXT
                            }
                            continue;
                        case q.SGML_DECL:
                            (t.sgmlDecl + i).toUpperCase() === O ? (f(t, "onopencdata"), t.state = q.CDATA, t.sgmlDecl = "", t.cdata = "") : t.sgmlDecl + i === "--" ? (t.state = q.COMMENT, t.comment = "", t.sgmlDecl = "") : (t.sgmlDecl + i).toUpperCase() === I ? (t.state = q.DOCTYPE, (t.doctype || t.sawRoot) && b(t, "Inappropriately located doctype declaration"), t.doctype = "", t.sgmlDecl = "") : ">" === i ? (f(t, "onsgmldeclaration", t.sgmlDecl), t.sgmlDecl = "", t.state = q.TEXT) : p(P, i) ? (t.state = q.SGML_DECL_QUOTED, t.sgmlDecl += i) : t.sgmlDecl += i;
                            continue;
                        case q.SGML_DECL_QUOTED:
                            i === t.q && (t.state = q.SGML_DECL, t.q = ""), t.sgmlDecl += i;
                            continue;
                        case q.DOCTYPE:
                            ">" === i ? (t.state = q.TEXT, f(t, "ondoctype", t.doctype), t.doctype = !0) : (t.doctype += i, "[" === i ? t.state = q.DOCTYPE_DTD : p(P, i) && (t.state = q.DOCTYPE_QUOTED, t.q = i));
                            continue;
                        case q.DOCTYPE_QUOTED:
                            t.doctype += i, i === t.q && (t.q = "", t.state = q.DOCTYPE);
                            continue;
                        case q.DOCTYPE_DTD:
                            t.doctype += i, "]" === i ? t.state = q.DOCTYPE : p(P, i) && (t.state = q.DOCTYPE_DTD_QUOTED, t.q = i);
                            continue;
                        case q.DOCTYPE_DTD_QUOTED:
                            t.doctype += i, i === t.q && (t.state = q.DOCTYPE_DTD, t.q = "");
                            continue;
                        case q.COMMENT:
                            "-" === i ? t.state = q.COMMENT_ENDING : t.comment += i;
                            continue;
                        case q.COMMENT_ENDING:
                            "-" === i ? (t.state = q.COMMENT_ENDED, t.comment = g(t.opt, t.comment), t.comment && f(t, "oncomment", t.comment), t.comment = "") : (t.comment += "-" + i, t.state = q.COMMENT);
                            continue;
                        case q.COMMENT_ENDED:
                            ">" !== i ? (b(t, "Malformed comment"), t.comment += "--" + i, t.state = q.COMMENT) : t.state = q.TEXT;
                            continue;
                        case q.CDATA:
                            "]" === i ? t.state = q.CDATA_ENDING : t.cdata += i;
                            continue;
                        case q.CDATA_ENDING:
                            "]" === i ? t.state = q.CDATA_ENDING_2 : (t.cdata += "]" + i, t.state = q.CDATA);
                            continue;
                        case q.CDATA_ENDING_2:
                            ">" === i ? (t.cdata && f(t, "oncdata", t.cdata), f(t, "onclosecdata"), t.cdata = "", t.state = q.TEXT) : "]" === i ? t.cdata += "]" : (t.cdata += "]]" + i, t.state = q.CDATA);
                            continue;
                        case q.PROC_INST:
                            "?" === i ? t.state = q.PROC_INST_ENDING : p(R, i) ? t.state = q.PROC_INST_BODY : t.procInstName += i;
                            continue;
                        case q.PROC_INST_BODY:
                            if (!t.procInstBody && p(R, i))continue;
                            "?" === i ? t.state = q.PROC_INST_ENDING : t.procInstBody += i;
                            continue;
                        case q.PROC_INST_ENDING:
                            ">" === i ? (f(t, "onprocessinginstruction", {
                                name: t.procInstName,
                                body: t.procInstBody
                            }), t.procInstName = t.procInstBody = "", t.state = q.TEXT) : (t.procInstBody += "?" + i, t.state = q.PROC_INST_BODY);
                            continue;
                        case q.OPEN_TAG:
                            p(H, i) ? t.tagName += i : (x(t), ">" === i ? _(t) : "/" === i ? t.state = q.OPEN_TAG_SLASH : (d(R, i) && b(t, "Invalid character in tag name"), t.state = q.ATTRIB));
                            continue;
                        case q.OPEN_TAG_SLASH:
                            ">" === i ? (_(t, !0), S(t)) : (b(t, "Forward-slash in opening tag not followed by >"), t.state = q.ATTRIB);
                            continue;
                        case q.ATTRIB:
                            if (p(R, i))continue;
                            ">" === i ? _(t) : "/" === i ? t.state = q.OPEN_TAG_SLASH : p($, i) ? (t.attribName = i, t.attribValue = "", t.state = q.ATTRIB_NAME) : b(t, "Invalid attribute name");
                            continue;
                        case q.ATTRIB_NAME:
                            "=" === i ? t.state = q.ATTRIB_VALUE : ">" === i ? (b(t, "Attribute without value"), t.attribValue = t.attribName, w(t), _(t)) : p(R, i) ? t.state = q.ATTRIB_NAME_SAW_WHITE : p(H, i) ? t.attribName += i : b(t, "Invalid attribute name");
                            continue;
                        case q.ATTRIB_NAME_SAW_WHITE:
                            if ("=" === i)t.state = q.ATTRIB_VALUE; else {
                                if (p(R, i))continue;
                                b(t, "Attribute without value"), t.tag.attributes[t.attribName] = "", t.attribValue = "", f(t, "onattribute", {
                                    name: t.attribName,
                                    value: ""
                                }), t.attribName = "", ">" === i ? _(t) : p($, i) ? (t.attribName = i, t.state = q.ATTRIB_NAME) : (b(t, "Invalid attribute name"), t.state = q.ATTRIB)
                            }
                            continue;
                        case q.ATTRIB_VALUE:
                            if (p(R, i))continue;
                            p(P, i) ? (t.q = i, t.state = q.ATTRIB_VALUE_QUOTED) : (b(t, "Unquoted attribute value"), t.state = q.ATTRIB_VALUE_UNQUOTED, t.attribValue = i);
                            continue;
                        case q.ATTRIB_VALUE_QUOTED:
                            if (i !== t.q) {
                                "&" === i ? t.state = q.ATTRIB_VALUE_ENTITY_Q : t.attribValue += i;
                                continue
                            }
                            w(t), t.q = "", t.state = q.ATTRIB_VALUE_CLOSED;
                            continue;
                        case q.ATTRIB_VALUE_CLOSED:
                            p(R, i) ? t.state = q.ATTRIB : ">" === i ? _(t) : "/" === i ? t.state = q.OPEN_TAG_SLASH : p($, i) ? (b(t, "No whitespace between attributes"), t.attribName = i, t.attribValue = "", t.state = q.ATTRIB_NAME) : b(t, "Invalid attribute name");
                            continue;
                        case q.ATTRIB_VALUE_UNQUOTED:
                            if (d(L, i)) {
                                "&" === i ? t.state = q.ATTRIB_VALUE_ENTITY_U : t.attribValue += i;
                                continue
                            }
                            w(t), ">" === i ? _(t) : t.state = q.ATTRIB;
                            continue;
                        case q.CLOSE_TAG:
                            if (t.tagName)">" === i ? S(t) : p(H, i) ? t.tagName += i : t.script ? (t.script += "</" + t.tagName, t.tagName = "", t.state = q.SCRIPT) : (d(R, i) && b(t, "Invalid tagname in closing tag"), t.state = q.CLOSE_TAG_SAW_WHITE); else {
                                if (p(R, i))continue;
                                d($, i) ? t.script ? (t.script += "</" + i, t.state = q.SCRIPT) : b(t, "Invalid tagname in closing tag.") : t.tagName = i
                            }
                            continue;
                        case q.CLOSE_TAG_SAW_WHITE:
                            if (p(R, i))continue;
                            ">" === i ? S(t) : b(t, "Invalid characters in closing tag");
                            continue;
                        case q.TEXT_ENTITY:
                        case q.ATTRIB_VALUE_ENTITY_Q:
                        case q.ATTRIB_VALUE_ENTITY_U:
                            switch (t.state) {
                                case q.TEXT_ENTITY:
                                    var s = q.TEXT, c = "textNode";
                                    break;
                                case q.ATTRIB_VALUE_ENTITY_Q:
                                    var s = q.ATTRIB_VALUE_QUOTED, c = "attribValue";
                                    break;
                                case q.ATTRIB_VALUE_ENTITY_U:
                                    var s = q.ATTRIB_VALUE_UNQUOTED, c = "attribValue"
                            }
                            ";" === i ? (t[c] += C(t), t.entity = "", t.state = s) : p(B, i) ? t.entity += i : (b(t, "Invalid character entity"), t[c] += "&" + t.entity + i, t.entity = "", t.state = s);
                            continue;
                        default:
                            throw new Error(t, "Unknown state: " + t.state)
                    }
                    return t.position >= t.bufferCheckPosition && r(t), t
                }

                n.parser = function (e, t) {
                    return new i(e, t)
                }, n.SAXParser = i, n.SAXStream = c, n.createStream = s, n.MAX_BUFFER_LENGTH = 65536;
                var T = ["comment", "sgmlDecl", "textNode", "tagName", "doctype", "procInstName", "procInstBody", "entity", "attribName", "attribValue", "cdata", "script"];
                n.EVENTS = ["text", "processinginstruction", "sgmldeclaration", "doctype", "comment", "attribute", "opentag", "closetag", "opencdata", "cdata", "closecdata", "error", "end", "ready", "script", "opennamespace", "closenamespace"], Object.create || (Object.create = function (e) {
                    function t() {
                        this.__proto__ = e
                    }

                    return t.prototype = e, new t
                }), Object.getPrototypeOf || (Object.getPrototypeOf = function (e) {
                    return e.__proto__
                }), Object.keys || (Object.keys = function (e) {
                    var t = [];
                    for (var n in e)e.hasOwnProperty(n) && t.push(n);
                    return t
                }), i.prototype = {
                    end: function () {
                        y(this)
                    }, write: A, resume: function () {
                        return this.error = null, this
                    }, close: function () {
                        return this.write(null)
                    }, flush: function () {
                        a(this)
                    }
                };
                try {
                    var j = e("stream").Stream
                } catch (N) {
                    var j = function () {
                    }
                }
                var k = n.EVENTS.filter(function (e) {
                    return "error" !== e && "end" !== e
                });
                c.prototype = Object.create(j.prototype, {constructor: {value: c}}), c.prototype.write = function (n) {
                    if ("function" == typeof t && "function" == typeof t.isBuffer && t.isBuffer(n)) {
                        if (!this._decoder) {
                            var i = e("string_decoder").StringDecoder;
                            this._decoder = new i("utf8")
                        }
                        n = this._decoder.write(n)
                    }
                    return this._parser.write(n.toString()), this.emit("data", n), !0
                }, c.prototype.end = function (e) {
                    return e && e.length && this.write(e), this._parser.end(), !0
                }, c.prototype.on = function (e, t) {
                    var n = this;
                    return n._parser["on" + e] || -1 === k.indexOf(e) || (n._parser["on" + e] = function () {
                        var t = 1 === arguments.length ? [arguments[0]] : Array.apply(null, arguments);
                        t.splice(0, 0, e), n.emit.apply(n, t)
                    }), j.prototype.on.call(n, e, t)
                };
                var R = "\r\n	 ", M = "0124356789", D = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", P = "'\"", B = M + D + "#", L = R + ">", O = "[CDATA[", I = "DOCTYPE", F = "http://www.w3.org/XML/1998/namespace", U = "http://www.w3.org/2000/xmlns/", z = {
                    xml: F,
                    xmlns: U
                };
                R = l(R), M = l(M), D = l(D);
                var $ = /[:_A-Za-z\u00C0-\u00D6\u00D8-\u00F6\u00F8-\u02FF\u0370-\u037D\u037F-\u1FFF\u200C-\u200D\u2070-\u218F\u2C00-\u2FEF\u3001-\uD7FF\uF900-\uFDCF\uFDF0-\uFFFD]/, H = /[:_A-Za-z\u00C0-\u00D6\u00D8-\u00F6\u00F8-\u02FF\u0370-\u037D\u037F-\u1FFF\u200C-\u200D\u2070-\u218F\u2C00-\u2FEF\u3001-\uD7FF\uF900-\uFDCF\uFDF0-\uFFFD\u00B7\u0300-\u036F\u203F-\u2040\.\d-]/;
                P = l(P), B = l(B), L = l(L);
                var q = 0;
                n.STATE = {
                    BEGIN: q++,
                    TEXT: q++,
                    TEXT_ENTITY: q++,
                    OPEN_WAKA: q++,
                    SGML_DECL: q++,
                    SGML_DECL_QUOTED: q++,
                    DOCTYPE: q++,
                    DOCTYPE_QUOTED: q++,
                    DOCTYPE_DTD: q++,
                    DOCTYPE_DTD_QUOTED: q++,
                    COMMENT_STARTING: q++,
                    COMMENT: q++,
                    COMMENT_ENDING: q++,
                    COMMENT_ENDED: q++,
                    CDATA: q++,
                    CDATA_ENDING: q++,
                    CDATA_ENDING_2: q++,
                    PROC_INST: q++,
                    PROC_INST_BODY: q++,
                    PROC_INST_ENDING: q++,
                    OPEN_TAG: q++,
                    OPEN_TAG_SLASH: q++,
                    ATTRIB: q++,
                    ATTRIB_NAME: q++,
                    ATTRIB_NAME_SAW_WHITE: q++,
                    ATTRIB_VALUE: q++,
                    ATTRIB_VALUE_QUOTED: q++,
                    ATTRIB_VALUE_CLOSED: q++,
                    ATTRIB_VALUE_UNQUOTED: q++,
                    ATTRIB_VALUE_ENTITY_Q: q++,
                    ATTRIB_VALUE_ENTITY_U: q++,
                    CLOSE_TAG: q++,
                    CLOSE_TAG_SAW_WHITE: q++,
                    SCRIPT: q++,
                    SCRIPT_ENDING: q++
                }, n.ENTITIES = {
                    amp: "&",
                    gt: ">",
                    lt: "<",
                    quot: '"',
                    apos: "'",
                    AElig: 198,
                    Aacute: 193,
                    Acirc: 194,
                    Agrave: 192,
                    Aring: 197,
                    Atilde: 195,
                    Auml: 196,
                    Ccedil: 199,
                    ETH: 208,
                    Eacute: 201,
                    Ecirc: 202,
                    Egrave: 200,
                    Euml: 203,
                    Iacute: 205,
                    Icirc: 206,
                    Igrave: 204,
                    Iuml: 207,
                    Ntilde: 209,
                    Oacute: 211,
                    Ocirc: 212,
                    Ograve: 210,
                    Oslash: 216,
                    Otilde: 213,
                    Ouml: 214,
                    THORN: 222,
                    Uacute: 218,
                    Ucirc: 219,
                    Ugrave: 217,
                    Uuml: 220,
                    Yacute: 221,
                    aacute: 225,
                    acirc: 226,
                    aelig: 230,
                    agrave: 224,
                    aring: 229,
                    atilde: 227,
                    auml: 228,
                    ccedil: 231,
                    eacute: 233,
                    ecirc: 234,
                    egrave: 232,
                    eth: 240,
                    euml: 235,
                    iacute: 237,
                    icirc: 238,
                    igrave: 236,
                    iuml: 239,
                    ntilde: 241,
                    oacute: 243,
                    ocirc: 244,
                    ograve: 242,
                    oslash: 248,
                    otilde: 245,
                    ouml: 246,
                    szlig: 223,
                    thorn: 254,
                    uacute: 250,
                    ucirc: 251,
                    ugrave: 249,
                    uuml: 252,
                    yacute: 253,
                    yuml: 255,
                    copy: 169,
                    reg: 174,
                    nbsp: 160,
                    iexcl: 161,
                    cent: 162,
                    pound: 163,
                    curren: 164,
                    yen: 165,
                    brvbar: 166,
                    sect: 167,
                    uml: 168,
                    ordf: 170,
                    laquo: 171,
                    not: 172,
                    shy: 173,
                    macr: 175,
                    deg: 176,
                    plusmn: 177,
                    sup1: 185,
                    sup2: 178,
                    sup3: 179,
                    acute: 180,
                    micro: 181,
                    para: 182,
                    middot: 183,
                    cedil: 184,
                    ordm: 186,
                    raquo: 187,
                    frac14: 188,
                    frac12: 189,
                    frac34: 190,
                    iquest: 191,
                    times: 215,
                    divide: 247,
                    OElig: 338,
                    oelig: 339,
                    Scaron: 352,
                    scaron: 353,
                    Yuml: 376,
                    fnof: 402,
                    circ: 710,
                    tilde: 732,
                    Alpha: 913,
                    Beta: 914,
                    Gamma: 915,
                    Delta: 916,
                    Epsilon: 917,
                    Zeta: 918,
                    Eta: 919,
                    Theta: 920,
                    Iota: 921,
                    Kappa: 922,
                    Lambda: 923,
                    Mu: 924,
                    Nu: 925,
                    Xi: 926,
                    Omicron: 927,
                    Pi: 928,
                    Rho: 929,
                    Sigma: 931,
                    Tau: 932,
                    Upsilon: 933,
                    Phi: 934,
                    Chi: 935,
                    Psi: 936,
                    Omega: 937,
                    alpha: 945,
                    beta: 946,
                    gamma: 947,
                    delta: 948,
                    epsilon: 949,
                    zeta: 950,
                    eta: 951,
                    theta: 952,
                    iota: 953,
                    kappa: 954,
                    lambda: 955,
                    mu: 956,
                    nu: 957,
                    xi: 958,
                    omicron: 959,
                    pi: 960,
                    rho: 961,
                    sigmaf: 962,
                    sigma: 963,
                    tau: 964,
                    upsilon: 965,
                    phi: 966,
                    chi: 967,
                    psi: 968,
                    omega: 969,
                    thetasym: 977,
                    upsih: 978,
                    piv: 982,
                    ensp: 8194,
                    emsp: 8195,
                    thinsp: 8201,
                    zwnj: 8204,
                    zwj: 8205,
                    lrm: 8206,
                    rlm: 8207,
                    ndash: 8211,
                    mdash: 8212,
                    lsquo: 8216,
                    rsquo: 8217,
                    sbquo: 8218,
                    ldquo: 8220,
                    rdquo: 8221,
                    bdquo: 8222,
                    dagger: 8224,
                    Dagger: 8225,
                    bull: 8226,
                    hellip: 8230,
                    permil: 8240,
                    prime: 8242,
                    Prime: 8243,
                    lsaquo: 8249,
                    rsaquo: 8250,
                    oline: 8254,
                    frasl: 8260,
                    euro: 8364,
                    image: 8465,
                    weierp: 8472,
                    real: 8476,
                    trade: 8482,
                    alefsym: 8501,
                    larr: 8592,
                    uarr: 8593,
                    rarr: 8594,
                    darr: 8595,
                    harr: 8596,
                    crarr: 8629,
                    lArr: 8656,
                    uArr: 8657,
                    rArr: 8658,
                    dArr: 8659,
                    hArr: 8660,
                    forall: 8704,
                    part: 8706,
                    exist: 8707,
                    empty: 8709,
                    nabla: 8711,
                    isin: 8712,
                    notin: 8713,
                    ni: 8715,
                    prod: 8719,
                    sum: 8721,
                    minus: 8722,
                    lowast: 8727,
                    radic: 8730,
                    prop: 8733,
                    infin: 8734,
                    ang: 8736,
                    and: 8743,
                    or: 8744,
                    cap: 8745,
                    cup: 8746,
                    "int": 8747,
                    there4: 8756,
                    sim: 8764,
                    cong: 8773,
                    asymp: 8776,
                    ne: 8800,
                    equiv: 8801,
                    le: 8804,
                    ge: 8805,
                    sub: 8834,
                    sup: 8835,
                    nsub: 8836,
                    sube: 8838,
                    supe: 8839,
                    oplus: 8853,
                    otimes: 8855,
                    perp: 8869,
                    sdot: 8901,
                    lceil: 8968,
                    rceil: 8969,
                    lfloor: 8970,
                    rfloor: 8971,
                    lang: 9001,
                    rang: 9002,
                    loz: 9674,
                    spades: 9824,
                    clubs: 9827,
                    hearts: 9829,
                    diams: 9830
                }, Object.keys(n.ENTITIES).forEach(function (e) {
                    var t = n.ENTITIES[e], i = "number" == typeof t ? String.fromCharCode(t) : t;
                    n.ENTITIES[e] = i
                });
                for (var q in n.STATE)n.STATE[n.STATE[q]] = q;
                q = n.STATE, String.fromCodePoint || !function () {
                    var e = String.fromCharCode, t = Math.floor, n = function () {
                        var n, i, r = 16384, o = [], a = -1, s = arguments.length;
                        if (!s)return "";
                        for (var c = ""; ++a < s;) {
                            var l = Number(arguments[a]);
                            if (!isFinite(l) || 0 > l || l > 1114111 || t(l) != l)throw RangeError("Invalid code point: " + l);
                            65535 >= l ? o.push(l) : (l -= 65536, n = (l >> 10) + 55296, i = l % 1024 + 56320, o.push(n, i)), (a + 1 == s || o.length > r) && (c += e.apply(null, o), o.length = 0)
                        }
                        return c
                    };
                    Object.defineProperty ? Object.defineProperty(String, "fromCodePoint", {
                        value: n,
                        configurable: !0,
                        writable: !0
                    }) : String.fromCodePoint = n
                }()
            }("undefined" == typeof n ? sax = {} : n)
        }).call(this, void 0)
    }, {stream: void 0, string_decoder: void 0}],
    96: [function (e, t, n) {
        !function (e) {
            "use strict";
            function i() {
                this.data = [null], this.top = 0
            }

            function r() {
                return new i
            }

            i.prototype.clear = function () {
                return this.data = [null], this.top = 0, this
            }, i.prototype.length = function () {
                return this.top
            }, i.prototype.peek = function () {
                return this.data[this.top]
            }, i.prototype.pop = function () {
                return this.top > 0 ? (this.top--, this.data.pop()) : void 0
            }, i.prototype.push = function (e) {
                return this.data[++this.top] = e, this
            }, "undefined" != typeof n ? t.exports = r : "function" == typeof define ? define(function () {
                return r
            }) : e.stack = r
        }(this)
    }, {}],
    97: [function (e, t, n) {
        t.exports = e("./lib/moddle")
    }, {"./lib/moddle": 101}],
    98: [function (e, t, n) {
        "use strict";
        function i() {
        }

        i.prototype.get = function (e) {
            return this.$model.properties.get(this, e)
        }, i.prototype.set = function (e, t) {
            this.$model.properties.set(this, e, t)
        }, t.exports = i
    }, {}],
    99: [function (e, t, n) {
        "use strict";
        function i(e) {
            this.ns = e, this.name = e.name, this.allTypes = [], this.properties = [], this.propertiesByName = {}
        }

        var r = e("lodash/object/pick"), o = e("lodash/object/assign"), a = e("lodash/collection/forEach"), s = e("./ns").parseName;
        t.exports = i, i.prototype.build = function () {
            return r(this, ["ns", "name", "allTypes", "properties", "propertiesByName", "bodyProperty"])
        }, i.prototype.addProperty = function (e, t) {
            this.addNamedProperty(e, !0);
            var n = this.properties;
            void 0 !== t ? n.splice(t, 0, e) : n.push(e)
        }, i.prototype.replaceProperty = function (e, t) {
            var n = e.ns, i = this.properties, r = this.propertiesByName, o = e.name !== t.name;
            if (e.isBody) {
                if (!t.isBody)throw new Error("property <" + t.ns.name + "> must be body property to refine <" + e.ns.name + ">");
                this.setBodyProperty(t, !1)
            }
            this.addNamedProperty(t, o);
            var a = i.indexOf(e);
            if (-1 === a)throw new Error("property <" + n.name + "> not found in property list");
            i[a] = t, r[n.name] = r[n.localName] = t
        }, i.prototype.redefineProperty = function (e) {
            var t = e.ns.prefix, n = e.redefines.split("#"), i = s(n[0], t), r = s(n[1], i.prefix).name, o = this.propertiesByName[r];
            if (!o)throw new Error("refined property <" + r + "> not found");
            this.replaceProperty(o, e), delete e.redefines
        }, i.prototype.addNamedProperty = function (e, t) {
            var n = e.ns, i = this.propertiesByName;
            t && (this.assertNotDefined(e, n.name), this.assertNotDefined(e, n.localName)), i[n.name] = i[n.localName] = e
        }, i.prototype.removeNamedProperty = function (e) {
            var t = e.ns, n = this.propertiesByName;
            delete n[t.name], delete n[t.localName]
        }, i.prototype.setBodyProperty = function (e, t) {
            if (t && this.bodyProperty)throw new Error("body property defined multiple times (<" + this.bodyProperty.ns.name + ">, <" + e.ns.name + ">)");
            this.bodyProperty = e
        }, i.prototype.addIdProperty = function (e) {
            var t = s(e, this.ns.prefix), n = {name: t.localName, type: "String", isAttr: !0, ns: t};
            this.addProperty(n, 0)
        }, i.prototype.assertNotDefined = function (e, t) {
            var n = e.name, i = this.propertiesByName[n];
            if (i)throw new Error("property <" + n + "> already defined; override of <" + i.definedBy.ns.name + "#" + i.ns.name + "> by <" + e.definedBy.ns.name + "#" + e.ns.name + "> not allowed without redefines")
        }, i.prototype.hasProperty = function (e) {
            return this.propertiesByName[e]
        }, i.prototype.addTrait = function (e) {
            var t = this.allTypes;
            -1 === t.indexOf(e) && (a(e.properties, function (t) {
                t = o({}, t, {name: t.ns.localName}), Object.defineProperty(t, "definedBy", {value: e}), t.redefines ? this.redefineProperty(t) : (t.isBody && this.setBodyProperty(t), this.addProperty(t))
            }, this), t.push(e))
        }
    }, {"./ns": 102, "lodash/collection/forEach": 274, "lodash/object/assign": 396, "lodash/object/pick": 402}],
    100: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            this.model = e, this.properties = t
        }

        var r = e("lodash/collection/forEach"), o = e("./base");
        t.exports = i, i.prototype.createType = function (e) {
            function t(e) {
                i.define(this, "$type", {
                    value: s,
                    enumerable: !0
                }), i.define(this, "$attrs", {value: {}}), i.define(this, "$parent", {writable: !0}), r(e, function (e, t) {
                    this.set(t, e)
                }, this)
            }

            var n = this.model, i = this.properties, a = Object.create(o.prototype);
            r(e.properties, function (e) {
                e.isMany || void 0 === e["default"] || (a[e.name] = e["default"])
            }), i.defineModel(a, n), i.defineDescriptor(a, e);
            var s = e.ns.name;
            return t.prototype = a, t.hasType = a.$instanceOf = this.model.hasType, i.defineModel(t, n), i.defineDescriptor(t, e), t
        }
    }, {"./base": 98, "lodash/collection/forEach": 274}],
    101: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            t = t || {}, this.properties = new u(this), this.factory = new c(this, this.properties), this.registry = new l(e, this.properties, t), this.typeCache = {}
        }

        var r = e("lodash/lang/isString"), o = e("lodash/lang/isObject"), a = e("lodash/collection/forEach"), s = e("lodash/collection/find"), c = e("./factory"), l = e("./registry"), u = e("./properties"), p = e("./ns").parseName;
        t.exports = i, i.prototype.create = function (e, t) {
            var n = this.getType(e);
            if (!n)throw new Error("unknown type <" + e + ">");
            return new n(t)
        }, i.prototype.getType = function (e) {
            var t = this.typeCache, n = r(e) ? e : e.ns.name, i = t[n];
            return i || (e = this.registry.getEffectiveDescriptor(n), i = t[n] = this.factory.createType(e)), i
        }, i.prototype.createAny = function (e, t, n) {
            var i = p(e), r = {$type: e}, s = {
                name: e,
                isGeneric: !0,
                ns: {prefix: i.prefix, localName: i.localName, uri: t}
            };
            return this.properties.defineDescriptor(r, s), this.properties.defineModel(r, this), this.properties.define(r, "$parent", {
                enumerable: !1,
                writable: !0
            }), a(n, function (e, t) {
                o(e) && void 0 !== e.value ? r[e.name] = e.value : r[t] = e
            }), r
        }, i.prototype.getPackage = function (e) {
            return this.registry.getPackage(e)
        }, i.prototype.getPackages = function () {
            return this.registry.getPackages()
        }, i.prototype.getElementDescriptor = function (e) {
            return e.$descriptor
        }, i.prototype.hasType = function (e, t) {
            void 0 === t && (t = e, e = this);
            var n = e.$model.getElementDescriptor(e);
            return !!s(n.allTypes, function (e) {
                return e.name === t
            })
        }, i.prototype.getPropertyDescriptor = function (e, t) {
            return this.getElementDescriptor(e).propertiesByName[t]
        }
    }, {
        "./factory": 100,
        "./ns": 102,
        "./properties": 103,
        "./registry": 104,
        "lodash/collection/find": 273,
        "lodash/collection/forEach": 274,
        "lodash/lang/isObject": 391,
        "lodash/lang/isString": 393
    }],
    102: [function (e, t, n) {
        "use strict";
        t.exports.parseName = function (e, t) {
            var n, i, r = e.split(/:/);
            if (1 === r.length)n = e, i = t; else {
                if (2 !== r.length)throw new Error("expected <prefix:localName> or <localName>, got " + e);
                n = r[1], i = r[0]
            }
            return e = (i ? i + ":" : "") + n, {name: e, prefix: i, localName: n}
        }
    }, {}],
    103: [function (e, t, n) {
        "use strict";
        function i(e) {
            this.model = e
        }

        t.exports = i, i.prototype.set = function (e, t, n) {
            var i = this.model.getPropertyDescriptor(e, t);
            i ? Object.defineProperty(e, i.name, {enumerable: !i.isReference, writable: !0, value: n}) : e.$attrs[t] = n
        }, i.prototype.get = function (e, t) {
            var n = this.model.getPropertyDescriptor(e, t);
            if (!n)return e.$attrs[t];
            var i = n.name;
            return !e[i] && n.isMany && Object.defineProperty(e, i, {
                enumerable: !n.isReference,
                writable: !0,
                value: []
            }), e[i]
        }, i.prototype.define = function (e, t, n) {
            Object.defineProperty(e, t, n)
        }, i.prototype.defineDescriptor = function (e, t) {
            this.define(e, "$descriptor", {value: t})
        }, i.prototype.defineModel = function (e, t) {
            this.define(e, "$model", {value: t})
        }
    }, {}],
    104: [function (e, t, n) {
        "use strict";
        function i(e, t, n) {
            this.options = r({generateId: "id"}, n || {}), this.packageMap = {}, this.typeMap = {}, this.packages = [], this.properties = t, o(e, this.registerPackage, this)
        }

        var r = e("lodash/object/assign"), o = e("lodash/collection/forEach"), a = e("./types"), s = e("./descriptor-builder"), c = e("./ns").parseName, l = a.isBuiltIn;
        t.exports = i, i.prototype.getPackage = function (e) {
            return this.packageMap[e]
        }, i.prototype.getPackages = function () {
            return this.packages
        }, i.prototype.registerPackage = function (e) {
            e = r({}, e), o(e.types, function (t) {
                this.registerType(t, e)
            }, this), this.packageMap[e.uri] = this.packageMap[e.prefix] = e, this.packages.push(e)
        }, i.prototype.registerType = function (e, t) {
            e = r({}, e, {
                superClass: (e.superClass || []).slice(),
                "extends": (e["extends"] || []).slice(),
                properties: (e.properties || []).slice()
            });
            var n = c(e.name, t.prefix), i = n.name, a = {};
            o(e.properties, function (e) {
                var t = c(e.name, n.prefix), i = t.name;
                l(e.type) || (e.type = c(e.type, t.prefix).name), r(e, {ns: t, name: i}), a[i] = e
            }), r(e, {ns: n, name: i, propertiesByName: a}), o(e["extends"], function (e) {
                var t = this.typeMap[e];
                t.traits = t.traits || [], t.traits.push(i)
            }, this), this.definePackage(e, t), this.typeMap[i] = e
        }, i.prototype.mapTypes = function (e, t) {
            function n(n) {
                var i = c(n, l(n) ? "" : e.prefix);
                r.mapTypes(i, t)
            }

            var i = l(e.name) ? {name: e.name} : this.typeMap[e.name], r = this;
            if (!i)throw new Error("unknown type <" + e.name + ">");
            o(i.superClass, n), t(i), o(i.traits, n)
        }, i.prototype.getEffectiveDescriptor = function (e) {
            var t = c(e), n = new s(t);
            this.mapTypes(t, function (e) {
                n.addTrait(e)
            });
            var i = this.options.generateId;
            i && !n.hasProperty(i) && n.addIdProperty(i);
            var r = n.build();
            return this.definePackage(r, r.allTypes[r.allTypes.length - 1].$pkg), r
        }, i.prototype.definePackage = function (e, t) {
            this.properties.define(e, "$pkg", {value: t})
        }
    }, {
        "./descriptor-builder": 99,
        "./ns": 102,
        "./types": 105,
        "lodash/collection/forEach": 274,
        "lodash/object/assign": 396
    }],
    105: [function (e, t, n) {
        "use strict";
        var i = {String: !0, Boolean: !0, Integer: !0, Real: !0, Element: !0}, r = {
            String: function (e) {
                return e
            }, Boolean: function (e) {
                return "true" === e
            }, Integer: function (e) {
                return parseInt(e, 10)
            }, Real: function (e) {
                return parseFloat(e, 10)
            }
        };
        t.exports.coerceType = function (e, t) {
            var n = r[e];
            return n ? n(t) : t
        }, t.exports.isBuiltIn = function (e) {
            return !!i[e]
        }, t.exports.isSimple = function (e) {
            return !!r[e]
        }
    }, {}],
    106: [function (e, t, n) {
        t.exports = {
            name: "BPMN20",
            uri: "http://www.omg.org/spec/BPMN/20100524/MODEL",
            associations: [],
            types: [{
                name: "Interface",
                superClass: ["RootElement"],
                properties: [{name: "name", isAttr: !0, type: "String"}, {
                    name: "operations",
                    type: "Operation",
                    isMany: !0
                }, {name: "implementationRef", type: "String", isAttr: !0}]
            }, {
                name: "Operation",
                superClass: ["BaseElement"],
                properties: [{name: "name", isAttr: !0, type: "String"}, {
                    name: "inMessageRef",
                    type: "Message",
                    isReference: !0
                }, {name: "outMessageRef", type: "Message", isReference: !0}, {
                    name: "errorRefs",
                    type: "Error",
                    isMany: !0,
                    isReference: !0
                }, {name: "implementationRef", type: "String", isAttr: !0}]
            }, {name: "EndPoint", superClass: ["RootElement"]}, {
                name: "Auditing",
                superClass: ["BaseElement"]
            }, {
                name: "GlobalTask",
                superClass: ["CallableElement"],
                properties: [{name: "resources", type: "ResourceRole", isMany: !0}]
            }, {name: "Monitoring", superClass: ["BaseElement"]}, {name: "Performer", superClass: ["ResourceRole"]}, {
                name: "Process",
                superClass: ["FlowElementsContainer", "CallableElement"],
                properties: [{name: "processType", type: "ProcessType", isAttr: !0}, {
                    name: "isClosed",
                    isAttr: !0,
                    type: "Boolean"
                }, {name: "auditing", type: "Auditing"}, {name: "monitoring", type: "Monitoring"}, {
                    name: "properties",
                    type: "Property",
                    isMany: !0
                }, {name: "artifacts", type: "Artifact", isMany: !0}, {
                    name: "resources",
                    type: "ResourceRole",
                    isMany: !0
                }, {name: "correlationSubscriptions", type: "CorrelationSubscription", isMany: !0}, {
                    name: "supports",
                    type: "Process",
                    isMany: !0,
                    isReference: !0
                }, {name: "definitionalCollaborationRef", type: "Collaboration", isAttr: !0, isReference: !0}, {
                    name: "isExecutable",
                    isAttr: !0, type: "Boolean"
                }]
            }, {
                name: "LaneSet",
                superClass: ["BaseElement"],
                properties: [{name: "lanes", type: "Lane", isMany: !0}, {name: "name", isAttr: !0, type: "String"}]
            }, {
                name: "Lane",
                superClass: ["BaseElement"],
                properties: [{name: "name", isAttr: !0, type: "String"}, {
                    name: "childLaneSet",
                    type: "LaneSet",
                    xml: {serialize: "xsi:type"}
                }, {
                    name: "partitionElementRef",
                    type: "BaseElement",
                    isAttr: !0,
                    isReference: !0
                }, {name: "flowNodeRef", type: "FlowNode", isMany: !0, isReference: !0}, {
                    name: "partitionElement",
                    type: "BaseElement"
                }]
            }, {name: "GlobalManualTask", superClass: ["GlobalTask"]}, {
                name: "ManualTask",
                superClass: ["Task"]
            }, {
                name: "UserTask",
                superClass: ["Task"],
                properties: [{name: "renderings", type: "Rendering", isMany: !0}, {
                    name: "implementation",
                    isAttr: !0,
                    type: "String"
                }]
            }, {name: "Rendering", superClass: ["BaseElement"]}, {
                name: "HumanPerformer",
                superClass: ["Performer"]
            }, {name: "PotentialOwner", superClass: ["HumanPerformer"]}, {
                name: "GlobalUserTask",
                superClass: ["GlobalTask"],
                properties: [{name: "implementation", isAttr: !0, type: "String"}, {
                    name: "renderings",
                    type: "Rendering",
                    isMany: !0
                }]
            }, {
                name: "Gateway",
                isAbstract: !0,
                superClass: ["FlowNode"],
                properties: [{name: "gatewayDirection", type: "GatewayDirection", "default": "Unspecified", isAttr: !0}]
            }, {
                name: "EventBasedGateway",
                superClass: ["Gateway"],
                properties: [{
                    name: "instantiate",
                    "default": !1,
                    isAttr: !0,
                    type: "Boolean"
                }, {name: "eventGatewayType", type: "EventBasedGatewayType", isAttr: !0, "default": "Exclusive"}]
            }, {
                name: "ComplexGateway",
                superClass: ["Gateway"],
                properties: [{
                    name: "activationCondition",
                    type: "Expression",
                    xml: {serialize: "xsi:type"}
                }, {name: "default", type: "SequenceFlow", isAttr: !0, isReference: !0}]
            }, {
                name: "ExclusiveGateway",
                superClass: ["Gateway"],
                properties: [{name: "default", type: "SequenceFlow", isAttr: !0, isReference: !0}]
            }, {
                name: "InclusiveGateway",
                superClass: ["Gateway"],
                properties: [{name: "default", type: "SequenceFlow", isAttr: !0, isReference: !0}]
            }, {name: "ParallelGateway", superClass: ["Gateway"]}, {
                name: "RootElement",
                isAbstract: !0,
                superClass: ["BaseElement"]
            }, {
                name: "Relationship",
                superClass: ["BaseElement"],
                properties: [{name: "type", isAttr: !0, type: "String"}, {
                    name: "direction",
                    type: "RelationshipDirection",
                    isAttr: !0
                }, {name: "source", isMany: !0, isReference: !0, type: "Element"}, {
                    name: "target",
                    isMany: !0,
                    isReference: !0,
                    type: "Element"
                }]
            }, {
                name: "BaseElement",
                isAbstract: !0,
                properties: [{name: "id", isAttr: !0, type: "String"}, {
                    name: "documentation",
                    type: "Documentation",
                    isMany: !0
                }, {
                    name: "extensionDefinitions",
                    type: "ExtensionDefinition",
                    isMany: !0,
                    isReference: !0
                }, {name: "extensionElements", type: "ExtensionElements"}]
            }, {
                name: "Extension",
                properties: [{name: "mustUnderstand", "default": !1, isAttr: !0, type: "Boolean"}, {
                    name: "definition",
                    type: "ExtensionDefinition"
                }]
            }, {
                name: "ExtensionDefinition",
                properties: [{name: "name", isAttr: !0, type: "String"}, {
                    name: "extensionAttributeDefinitions",
                    type: "ExtensionAttributeDefinition",
                    isMany: !0
                }]
            }, {
                name: "ExtensionAttributeDefinition",
                properties: [{name: "name", isAttr: !0, type: "String"}, {
                    name: "type",
                    isAttr: !0,
                    type: "String"
                }, {name: "isReference", "default": !1, isAttr: !0, type: "Boolean"}, {
                    name: "extensionDefinition",
                    type: "ExtensionDefinition",
                    isAttr: !0,
                    isReference: !0
                }]
            }, {
                name: "ExtensionElements",
                properties: [{name: "valueRef", isAttr: !0, isReference: !0, type: "Element"}, {
                    name: "values",
                    type: "Element",
                    isMany: !0
                }, {
                    name: "extensionAttributeDefinition",
                    type: "ExtensionAttributeDefinition",
                    isAttr: !0,
                    isReference: !0
                }]
            }, {
                name: "Documentation",
                superClass: ["BaseElement"],
                properties: [{name: "text", type: "String", isBody: !0}, {
                    name: "textFormat",
                    "default": "text/plain",
                    isAttr: !0,
                    type: "String"
                }]
            }, {
                name: "Event",
                isAbstract: !0,
                superClass: ["FlowNode", "InteractionNode"],
                properties: [{name: "properties", type: "Property", isMany: !0}]
            }, {name: "IntermediateCatchEvent", superClass: ["CatchEvent"]}, {
                name: "IntermediateThrowEvent",
                superClass: ["ThrowEvent"]
            }, {name: "EndEvent", superClass: ["ThrowEvent"]}, {
                name: "StartEvent",
                superClass: ["CatchEvent"],
                properties: [{name: "isInterrupting", "default": !0, isAttr: !0, type: "Boolean"}]
            }, {
                name: "ThrowEvent",
                isAbstract: !0,
                superClass: ["Event"],
                properties: [{name: "inputSet", type: "InputSet"}, {
                    name: "eventDefinitionRefs",
                    type: "EventDefinition",
                    isMany: !0,
                    isReference: !0
                }, {name: "dataInputAssociations", type: "DataInputAssociation", isMany: !0}, {
                    name: "dataInputs",
                    type: "DataInput",
                    isMany: !0
                }, {name: "eventDefinitions", type: "EventDefinition", isMany: !0}]
            }, {
                name: "CatchEvent",
                isAbstract: !0,
                superClass: ["Event"],
                properties: [{name: "parallelMultiple", isAttr: !0, type: "Boolean", "default": !1}, {
                    name: "outputSet",
                    type: "OutputSet"
                }, {
                    name: "eventDefinitionRefs",
                    type: "EventDefinition",
                    isMany: !0,
                    isReference: !0
                }, {name: "dataOutputAssociations", type: "DataOutputAssociation", isMany: !0}, {
                    name: "dataOutputs",
                    type: "DataOutput",
                    isMany: !0
                }, {name: "eventDefinitions", type: "EventDefinition", isMany: !0}]
            }, {
                name: "BoundaryEvent",
                superClass: ["CatchEvent"],
                properties: [{
                    name: "cancelActivity",
                    "default": !0,
                    isAttr: !0,
                    type: "Boolean"
                }, {name: "attachedToRef", type: "Activity", isAttr: !0, isReference: !0}]
            }, {name: "EventDefinition", isAbstract: !0, superClass: ["RootElement"]}, {
                name: "CancelEventDefinition",
                superClass: ["EventDefinition"]
            }, {
                name: "ErrorEventDefinition",
                superClass: ["EventDefinition"],
                properties: [{name: "errorRef", type: "Error", isAttr: !0, isReference: !0}]
            }, {name: "TerminateEventDefinition", superClass: ["EventDefinition"]}, {
                name: "EscalationEventDefinition",
                superClass: ["EventDefinition"],
                properties: [{name: "escalationRef", type: "Escalation", isAttr: !0, isReference: !0}]
            }, {
                name: "Escalation",
                properties: [{name: "structureRef", type: "ItemDefinition", isAttr: !0, isReference: !0}, {
                    name: "name",
                    isAttr: !0,
                    type: "String"
                }, {name: "escalationCode", isAttr: !0, type: "String"}],
                superClass: ["RootElement"]
            }, {
                name: "CompensateEventDefinition",
                superClass: ["EventDefinition"],
                properties: [{name: "waitForCompletion", isAttr: !0, type: "Boolean"}, {
                    name: "activityRef",
                    type: "Activity",
                    isAttr: !0,
                    isReference: !0
                }]
            }, {
                name: "TimerEventDefinition",
                superClass: ["EventDefinition"],
                properties: [{name: "timeDate", type: "Expression", xml: {serialize: "xsi:type"}}, {
                    name: "timeCycle",
                    type: "Expression",
                    xml: {serialize: "xsi:type"}
                }, {name: "timeDuration", type: "Expression", xml: {serialize: "xsi:type"}}]
            }, {
                name: "LinkEventDefinition",
                superClass: ["EventDefinition"],
                properties: [{name: "name", isAttr: !0, type: "String"}, {
                    name: "target",
                    type: "LinkEventDefinition",
                    isAttr: !0,
                    isReference: !0
                }, {name: "source", type: "LinkEventDefinition", isMany: !0, isReference: !0}]
            }, {
                name: "MessageEventDefinition",
                superClass: ["EventDefinition"],
                properties: [{name: "messageRef", type: "Message", isAttr: !0, isReference: !0}, {
                    name: "operationRef",
                    type: "Operation",
                    isAttr: !0,
                    isReference: !0
                }]
            }, {
                name: "ConditionalEventDefinition",
                superClass: ["EventDefinition"],
                properties: [{name: "condition", type: "Expression", xml: {serialize: "xsi:type"}}]
            }, {
                name: "SignalEventDefinition",
                superClass: ["EventDefinition"],
                properties: [{name: "signalRef", type: "Signal", isAttr: !0, isReference: !0}]
            }, {
                name: "Signal",
                superClass: ["RootElement"],
                properties: [{name: "structureRef", type: "ItemDefinition", isAttr: !0, isReference: !0}, {
                    name: "name",
                    isAttr: !0,
                    type: "String"
                }]
            }, {name: "ImplicitThrowEvent", superClass: ["ThrowEvent"]}, {
                name: "DataState",
                superClass: ["BaseElement"],
                properties: [{name: "name", isAttr: !0, type: "String"}]
            }, {
                name: "ItemAwareElement",
                superClass: ["BaseElement"],
                properties: [{
                    name: "itemSubjectRef",
                    type: "ItemDefinition",
                    isAttr: !0,
                    isReference: !0
                }, {name: "dataState", type: "DataState"}]
            }, {
                name: "DataAssociation",
                superClass: ["BaseElement"],
                properties: [{name: "transformation", type: "FormalExpression"}, {
                    name: "assignment",
                    type: "Assignment",
                    isMany: !0
                }, {name: "sourceRef", type: "ItemAwareElement", isMany: !0, isReference: !0}, {
                    name: "targetRef",
                    type: "ItemAwareElement",
                    isReference: !0
                }]
            }, {
                name: "DataInput",
                superClass: ["ItemAwareElement"],
                properties: [{name: "name", isAttr: !0, type: "String"}, {
                    name: "isCollection",
                    "default": !1,
                    isAttr: !0,
                    type: "Boolean"
                }, {
                    name: "inputSetRefs",
                    type: "InputSet",
                    isVirtual: !0,
                    isMany: !0,
                    isReference: !0
                }, {
                    name: "inputSetWithOptional",
                    type: "InputSet",
                    isVirtual: !0,
                    isMany: !0,
                    isReference: !0
                }, {name: "inputSetWithWhileExecuting", type: "InputSet", isVirtual: !0, isMany: !0, isReference: !0}]
            }, {
                name: "DataOutput",
                superClass: ["ItemAwareElement"],
                properties: [{name: "name", isAttr: !0, type: "String"}, {
                    name: "isCollection",
                    "default": !1,
                    isAttr: !0,
                    type: "Boolean"
                }, {
                    name: "outputSetRefs",
                    type: "OutputSet",
                    isVirtual: !0,
                    isMany: !0,
                    isReference: !0
                }, {
                    name: "outputSetWithOptional",
                    type: "OutputSet",
                    isVirtual: !0,
                    isMany: !0,
                    isReference: !0
                }, {name: "outputSetWithWhileExecuting", type: "OutputSet", isVirtual: !0, isMany: !0, isReference: !0}]
            }, {
                name: "InputSet",
                superClass: ["BaseElement"],
                properties: [{name: "name", isAttr: !0, type: "String"}, {
                    name: "dataInputRefs",
                    type: "DataInput",
                    isMany: !0,
                    isReference: !0
                }, {
                    name: "optionalInputRefs",
                    type: "DataInput",
                    isMany: !0,
                    isReference: !0
                }, {
                    name: "whileExecutingInputRefs",
                    type: "DataInput",
                    isMany: !0,
                    isReference: !0
                }, {name: "outputSetRefs", type: "OutputSet", isMany: !0, isReference: !0}]
            }, {
                name: "OutputSet",
                superClass: ["BaseElement"],
                properties: [{name: "dataOutputRefs", type: "DataOutput", isMany: !0, isReference: !0}, {
                    name: "name",
                    isAttr: !0,
                    type: "String"
                }, {name: "inputSetRefs", type: "InputSet", isMany: !0, isReference: !0}, {
                    name: "optionalOutputRefs",
                    type: "DataOutput",
                    isMany: !0,
                    isReference: !0
                }, {name: "whileExecutingOutputRefs", type: "DataOutput", isMany: !0, isReference: !0}]
            }, {
                name: "Property",
                superClass: ["ItemAwareElement"],
                properties: [{name: "name", isAttr: !0, type: "String"}]
            }, {name: "DataInputAssociation", superClass: ["DataAssociation"]}, {
                name: "DataOutputAssociation",
                superClass: ["DataAssociation"]
            }, {
                name: "InputOutputSpecification",
                superClass: ["BaseElement"],
                properties: [{name: "dataInputs", type: "DataInput", isMany: !0}, {
                    name: "dataOutputs",
                    type: "DataOutput",
                    isMany: !0
                }, {name: "inputSets", type: "InputSet", isMany: !0}, {
                    name: "outputSets",
                    type: "OutputSet",
                    isMany: !0
                }]
            }, {
                name: "DataObject",
                superClass: ["FlowElement", "ItemAwareElement"],
                properties: [{name: "isCollection", "default": !1, isAttr: !0, type: "Boolean"}]
            }, {
                name: "InputOutputBinding",
                properties: [{
                    name: "inputDataRef",
                    type: "InputSet",
                    isAttr: !0,
                    isReference: !0
                }, {name: "outputDataRef", type: "OutputSet", isAttr: !0, isReference: !0}, {
                    name: "operationRef",
                    type: "Operation",
                    isAttr: !0,
                    isReference: !0
                }]
            }, {
                name: "Assignment",
                superClass: ["BaseElement"],
                properties: [{name: "from", type: "Expression", xml: {serialize: "xsi:type"}}, {
                    name: "to",
                    type: "Expression",
                    xml: {serialize: "xsi:type"}
                }]
            }, {
                name: "DataStore",
                superClass: ["RootElement", "ItemAwareElement"],
                properties: [{name: "name", isAttr: !0, type: "String"}, {
                    name: "capacity",
                    isAttr: !0,
                    type: "Integer"
                }, {name: "isUnlimited", "default": !0, isAttr: !0, type: "Boolean"}]
            }, {
                name: "DataStoreReference",
                superClass: ["ItemAwareElement", "FlowElement"],
                properties: [{name: "dataStoreRef", type: "DataStore", isAttr: !0, isReference: !0}]
            }, {
                name: "DataObjectReference",
                superClass: ["ItemAwareElement", "FlowElement"],
                properties: [{name: "dataObjectRef", type: "DataObject", isAttr: !0, isReference: !0}]
            }, {
                name: "ConversationLink",
                superClass: ["BaseElement"],
                properties: [{
                    name: "sourceRef",
                    type: "InteractionNode",
                    isAttr: !0,
                    isReference: !0
                }, {name: "targetRef", type: "InteractionNode", isAttr: !0, isReference: !0}, {
                    name: "name",
                    isAttr: !0,
                    type: "String"
                }]
            }, {
                name: "ConversationAssociation",
                superClass: ["BaseElement"],
                properties: [{
                    name: "innerConversationNodeRef",
                    type: "ConversationNode",
                    isAttr: !0,
                    isReference: !0
                }, {name: "outerConversationNodeRef", type: "ConversationNode", isAttr: !0, isReference: !0}]
            }, {
                name: "CallConversation",
                superClass: ["ConversationNode"],
                properties: [{
                    name: "calledCollaborationRef",
                    type: "Collaboration",
                    isAttr: !0,
                    isReference: !0
                }, {name: "participantAssociations", type: "ParticipantAssociation", isMany: !0}]
            }, {name: "Conversation", superClass: ["ConversationNode"]}, {
                name: "SubConversation",
                superClass: ["ConversationNode"],
                properties: [{name: "conversationNodes", type: "ConversationNode", isMany: !0}]
            }, {
                name: "ConversationNode",
                isAbstract: !0,
                superClass: ["InteractionNode", "BaseElement"],
                properties: [{name: "name", isAttr: !0, type: "String"}, {
                    name: "participantRefs",
                    type: "Participant",
                    isMany: !0,
                    isReference: !0
                }, {
                    name: "messageFlowRefs",
                    type: "MessageFlow",
                    isMany: !0,
                    isReference: !0
                }, {name: "correlationKeys", type: "CorrelationKey", isMany: !0}]
            }, {name: "GlobalConversation", superClass: ["Collaboration"]}, {
                name: "PartnerEntity",
                superClass: ["RootElement"],
                properties: [{name: "name", isAttr: !0, type: "String"}, {
                    name: "participantRef",
                    type: "Participant",
                    isMany: !0,
                    isReference: !0
                }]
            }, {
                name: "PartnerRole",
                superClass: ["RootElement"],
                properties: [{name: "name", isAttr: !0, type: "String"}, {
                    name: "participantRef",
                    type: "Participant",
                    isMany: !0,
                    isReference: !0
                }]
            }, {
                name: "CorrelationProperty",
                superClass: ["RootElement"],
                properties: [{
                    name: "correlationPropertyRetrievalExpression",
                    type: "CorrelationPropertyRetrievalExpression",
                    isMany: !0
                }, {name: "name", isAttr: !0, type: "String"}, {
                    name: "type",
                    type: "ItemDefinition",
                    isAttr: !0,
                    isReference: !0
                }]
            }, {
                name: "Error",
                superClass: ["RootElement"],
                properties: [{name: "structureRef", type: "ItemDefinition", isAttr: !0, isReference: !0}, {
                    name: "name",
                    isAttr: !0,
                    type: "String"
                }, {name: "errorCode", isAttr: !0, type: "String"}]
            }, {
                name: "CorrelationKey",
                superClass: ["BaseElement"],
                properties: [{
                    name: "correlationPropertyRef",
                    type: "CorrelationProperty",
                    isMany: !0,
                    isReference: !0
                }, {name: "name", isAttr: !0, type: "String"}]
            }, {name: "Expression", superClass: ["BaseElement"], isAbstract: !0}, {
                name: "FormalExpression",
                superClass: ["Expression"],
                properties: [{name: "language", isAttr: !0, type: "String"}, {
                    name: "body",
                    type: "String",
                    isBody: !0
                }, {name: "evaluatesToTypeRef", type: "ItemDefinition", isAttr: !0, isReference: !0}]
            }, {
                name: "Message",
                superClass: ["RootElement"],
                properties: [{name: "name", isAttr: !0, type: "String"}, {
                    name: "itemRef",
                    type: "ItemDefinition",
                    isAttr: !0,
                    isReference: !0
                }]
            }, {
                name: "ItemDefinition",
                superClass: ["RootElement"],
                properties: [{name: "itemKind", type: "ItemKind", isAttr: !0}, {
                    name: "structureRef",
                    type: "String",
                    isAttr: !0
                }, {name: "isCollection", "default": !1, isAttr: !0, type: "Boolean"}, {
                    name: "import",
                    type: "Import",
                    isAttr: !0,
                    isReference: !0
                }]
            }, {
                name: "FlowElement",
                isAbstract: !0,
                superClass: ["BaseElement"],
                properties: [{name: "name", isAttr: !0, type: "String"}, {
                    name: "auditing",
                    type: "Auditing"
                }, {name: "monitoring", type: "Monitoring"}, {
                    name: "categoryValueRef",
                    type: "CategoryValue",
                    isMany: !0,
                    isReference: !0
                }]
            }, {
                name: "SequenceFlow",
                superClass: ["FlowElement"],
                properties: [{name: "isImmediate", isAttr: !0, type: "Boolean"}, {
                    name: "conditionExpression",
                    type: "Expression",
                    xml: {serialize: "xsi:type"}
                }, {name: "sourceRef", type: "FlowNode", isAttr: !0, isReference: !0}, {
                    name: "targetRef",
                    type: "FlowNode",
                    isAttr: !0,
                    isReference: !0
                }]
            }, {
                name: "FlowElementsContainer",
                isAbstract: !0,
                superClass: ["BaseElement"],
                properties: [{name: "laneSets", type: "LaneSet", isMany: !0}, {
                    name: "flowElements",
                    type: "FlowElement",
                    isMany: !0
                }]
            }, {
                name: "CallableElement",
                isAbstract: !0,
                superClass: ["RootElement"],
                properties: [{name: "name", isAttr: !0, type: "String"}, {
                    name: "ioSpecification",
                    type: "InputOutputSpecification",
                    xml: {serialize: "property"}
                }, {name: "supportedInterfaceRefs", type: "Interface", isMany: !0, isReference: !0}, {
                    name: "ioBinding",
                    type: "InputOutputBinding",
                    isMany: !0,
                    xml: {serialize: "property"}
                }]
            }, {
                name: "FlowNode",
                isAbstract: !0,
                superClass: ["FlowElement"],
                properties: [{name: "incoming", type: "SequenceFlow", isMany: !0, isReference: !0}, {
                    name: "outgoing",
                    type: "SequenceFlow",
                    isMany: !0,
                    isReference: !0
                }, {name: "lanes", type: "Lane", isVirtual: !0, isMany: !0, isReference: !0}]
            }, {
                name: "CorrelationPropertyRetrievalExpression",
                superClass: ["BaseElement"],
                properties: [{name: "messagePath", type: "FormalExpression"}, {
                    name: "messageRef",
                    type: "Message",
                    isAttr: !0,
                    isReference: !0
                }]
            }, {
                name: "CorrelationPropertyBinding",
                superClass: ["BaseElement"],
                properties: [{name: "dataPath", type: "FormalExpression"}, {
                    name: "correlationPropertyRef",
                    type: "CorrelationProperty",
                    isAttr: !0,
                    isReference: !0
                }]
            }, {
                name: "Resource",
                superClass: ["RootElement"],
                properties: [{name: "name", isAttr: !0, type: "String"}, {
                    name: "resourceParameters",
                    type: "ResourceParameter",
                    isMany: !0
                }]
            }, {
                name: "ResourceParameter",
                superClass: ["BaseElement"],
                properties: [{name: "name", isAttr: !0, type: "String"}, {
                    name: "isRequired",
                    isAttr: !0,
                    type: "Boolean"
                }, {name: "type", type: "ItemDefinition", isAttr: !0, isReference: !0}]
            }, {
                name: "CorrelationSubscription",
                superClass: ["BaseElement"],
                properties: [{
                    name: "correlationKeyRef",
                    type: "CorrelationKey",
                    isAttr: !0,
                    isReference: !0
                }, {name: "correlationPropertyBinding", type: "CorrelationPropertyBinding", isMany: !0}]
            }, {
                name: "MessageFlow",
                superClass: ["BaseElement"],
                properties: [{name: "name", isAttr: !0, type: "String"}, {
                    name: "sourceRef",
                    type: "InteractionNode",
                    isAttr: !0,
                    isReference: !0
                }, {name: "targetRef", type: "InteractionNode", isAttr: !0, isReference: !0}, {
                    name: "messageRef",
                    type: "Message",
                    isAttr: !0,
                    isReference: !0
                }]
            }, {
                name: "MessageFlowAssociation",
                superClass: ["BaseElement"],
                properties: [{
                    name: "innerMessageFlowRef",
                    type: "MessageFlow",
                    isAttr: !0,
                    isReference: !0
                }, {name: "outerMessageFlowRef", type: "MessageFlow", isAttr: !0, isReference: !0}]
            }, {
                name: "InteractionNode",
                isAbstract: !0,
                properties: [{
                    name: "incomingConversationLinks",
                    type: "ConversationLink",
                    isVirtual: !0,
                    isMany: !0,
                    isReference: !0
                }, {
                    name: "outgoingConversationLinks",
                    type: "ConversationLink",
                    isVirtual: !0,
                    isMany: !0,
                    isReference: !0
                }]
            }, {
                name: "Participant",
                superClass: ["InteractionNode", "BaseElement"],
                properties: [{name: "name", isAttr: !0, type: "String"}, {
                    name: "interfaceRefs",
                    type: "Interface",
                    isMany: !0,
                    isReference: !0
                }, {name: "participantMultiplicity", type: "ParticipantMultiplicity"}, {
                    name: "endPointRefs",
                    type: "EndPoint",
                    isMany: !0,
                    isReference: !0
                }, {name: "processRef", type: "Process", isAttr: !0, isReference: !0}]
            }, {
                name: "ParticipantAssociation",
                superClass: ["BaseElement"],
                properties: [{
                    name: "innerParticipantRef",
                    type: "Participant",
                    isAttr: !0,
                    isReference: !0
                }, {name: "outerParticipantRef", type: "Participant", isAttr: !0, isReference: !0}]
            }, {
                name: "ParticipantMultiplicity",
                properties: [{name: "minimum", "default": 0, isAttr: !0, type: "Integer"}, {
                    name: "maximum",
                    "default": 1,
                    isAttr: !0,
                    type: "Integer"
                }]
            }, {
                name: "Collaboration",
                superClass: ["RootElement"],
                properties: [{name: "name", isAttr: !0, type: "String"}, {
                    name: "isClosed",
                    isAttr: !0,
                    type: "Boolean"
                }, {name: "choreographyRef", type: "Choreography", isMany: !0, isReference: !0}, {
                    name: "artifacts",
                    type: "Artifact",
                    isMany: !0
                }, {
                    name: "participantAssociations",
                    type: "ParticipantAssociation",
                    isMany: !0
                }, {
                    name: "messageFlowAssociations",
                    type: "MessageFlowAssociation",
                    isMany: !0
                }, {name: "conversationAssociations", type: "ConversationAssociation"}, {
                    name: "participants",
                    type: "Participant",
                    isMany: !0
                }, {name: "messageFlows", type: "MessageFlow", isMany: !0}, {
                    name: "correlationKeys",
                    type: "CorrelationKey",
                    isMany: !0
                }, {name: "conversations", type: "ConversationNode", isMany: !0}, {
                    name: "conversationLinks",
                    type: "ConversationLink",
                    isMany: !0
                }]
            }, {
                name: "ChoreographyActivity",
                isAbstract: !0,
                superClass: ["FlowNode"],
                properties: [{
                    name: "participantRefs",
                    type: "Participant",
                    isMany: !0,
                    isReference: !0
                }, {
                    name: "initiatingParticipantRef",
                    type: "Participant",
                    isAttr: !0,
                    isReference: !0
                }, {name: "correlationKeys", type: "CorrelationKey", isMany: !0}, {
                    name: "loopType",
                    type: "ChoreographyLoopType",
                    "default": "None",
                    isAttr: !0
                }]
            }, {
                name: "CallChoreography",
                superClass: ["ChoreographyActivity"],
                properties: [{
                    name: "calledChoreographyRef",
                    type: "Choreography",
                    isAttr: !0,
                    isReference: !0
                }, {name: "participantAssociations", type: "ParticipantAssociation", isMany: !0}]
            }, {
                name: "SubChoreography",
                superClass: ["ChoreographyActivity", "FlowElementsContainer"],
                properties: [{name: "artifacts", type: "Artifact", isMany: !0}]
            }, {
                name: "ChoreographyTask",
                superClass: ["ChoreographyActivity"],
                properties: [{name: "messageFlowRef", type: "MessageFlow", isMany: !0, isReference: !0}]
            }, {
                name: "Choreography",
                superClass: ["FlowElementsContainer", "Collaboration"]
            }, {
                name: "GlobalChoreographyTask",
                superClass: ["Choreography"],
                properties: [{name: "initiatingParticipantRef", type: "Participant", isAttr: !0, isReference: !0}]
            }, {
                name: "TextAnnotation",
                superClass: ["Artifact"],
                properties: [{name: "text", type: "String"}, {
                    name: "textFormat",
                    "default": "text/plain",
                    isAttr: !0,
                    type: "String"
                }]
            }, {
                name: "Group",
                superClass: ["Artifact"],
                properties: [{name: "categoryValueRef", type: "CategoryValue", isAttr: !0, isReference: !0}]
            }, {
                name: "Association",
                superClass: ["Artifact"],
                properties: [{
                    name: "associationDirection",
                    type: "AssociationDirection",
                    isAttr: !0
                }, {name: "sourceRef", type: "BaseElement", isAttr: !0, isReference: !0}, {
                    name: "targetRef",
                    type: "BaseElement",
                    isAttr: !0,
                    isReference: !0
                }]
            }, {
                name: "Category",
                superClass: ["RootElement"],
                properties: [{name: "categoryValue", type: "CategoryValue", isMany: !0}, {
                    name: "name",
                    isAttr: !0,
                    type: "String"
                }]
            }, {name: "Artifact", isAbstract: !0, superClass: ["BaseElement"]}, {
                name: "CategoryValue",
                superClass: ["BaseElement"],
                properties: [{
                    name: "categorizedFlowElements",
                    type: "FlowElement",
                    isVirtual: !0,
                    isMany: !0,
                    isReference: !0
                }, {name: "value", isAttr: !0, type: "String"}]
            }, {
                name: "Activity",
                isAbstract: !0,
                superClass: ["FlowNode"],
                properties: [{name: "isForCompensation", "default": !1, isAttr: !0, type: "Boolean"}, {
                    name: "default",
                    type: "SequenceFlow",
                    isAttr: !0,
                    isReference: !0
                }, {
                    name: "ioSpecification",
                    type: "InputOutputSpecification",
                    xml: {serialize: "property"}
                }, {name: "boundaryEventRefs", type: "BoundaryEvent", isMany: !0, isReference: !0}, {
                    name: "properties",
                    type: "Property",
                    isMany: !0
                }, {
                    name: "dataInputAssociations",
                    type: "DataInputAssociation",
                    isMany: !0
                }, {name: "dataOutputAssociations", type: "DataOutputAssociation", isMany: !0}, {
                    name: "startQuantity",
                    "default": 1,
                    isAttr: !0,
                    type: "Integer"
                }, {name: "resources", type: "ResourceRole", isMany: !0}, {
                    name: "completionQuantity",
                    "default": 1,
                    isAttr: !0,
                    type: "Integer"
                }, {name: "loopCharacteristics", type: "LoopCharacteristics"}]
            }, {
                name: "ServiceTask",
                superClass: ["Task"],
                properties: [{name: "implementation", isAttr: !0, type: "String"}, {
                    name: "operationRef",
                    type: "Operation",
                    isAttr: !0,
                    isReference: !0
                }]
            }, {
                name: "SubProcess",
                superClass: ["Activity", "FlowElementsContainer", "InteractionNode"],
                properties: [{name: "triggeredByEvent", "default": !1, isAttr: !0, type: "Boolean"}, {
                    name: "artifacts",
                    type: "Artifact",
                    isMany: !0
                }]
            }, {
                name: "LoopCharacteristics",
                isAbstract: !0,
                superClass: ["BaseElement"]
            }, {
                name: "MultiInstanceLoopCharacteristics",
                superClass: ["LoopCharacteristics"],
                properties: [{name: "isSequential", "default": !1, isAttr: !0, type: "Boolean"}, {
                    name: "behavior",
                    type: "MultiInstanceBehavior",
                    "default": "All",
                    isAttr: !0
                }, {
                    name: "loopCardinality",
                    type: "Expression",
                    xml: {serialize: "xsi:type"}
                }, {
                    name: "loopDataInputRef",
                    type: "ItemAwareElement",
                    isAttr: !0,
                    isReference: !0
                }, {
                    name: "loopDataOutputRef",
                    type: "ItemAwareElement",
                    isAttr: !0,
                    isReference: !0
                }, {name: "inputDataItem", type: "DataInput"}, {
                    name: "outputDataItem",
                    type: "DataOutput"
                }, {
                    name: "completionCondition",
                    type: "Expression",
                    xml: {serialize: "xsi:type"}
                }, {
                    name: "complexBehaviorDefinition",
                    type: "ComplexBehaviorDefinition",
                    isMany: !0
                }, {
                    name: "oneBehaviorEventRef",
                    type: "EventDefinition",
                    isAttr: !0,
                    isReference: !0
                }, {name: "noneBehaviorEventRef", type: "EventDefinition", isAttr: !0, isReference: !0}]
            }, {
                name: "StandardLoopCharacteristics",
                superClass: ["LoopCharacteristics"],
                properties: [{name: "testBefore", "default": !1, isAttr: !0, type: "Boolean"}, {
                    name: "loopCondition",
                    type: "Expression",
                    xml: {serialize: "xsi:type"}
                }, {name: "loopMaximum", type: "Expression", xml: {serialize: "xsi:type"}}]
            }, {
                name: "CallActivity",
                superClass: ["Activity"],
                properties: [{name: "calledElement", type: "String", isAttr: !0}]
            }, {name: "Task", superClass: ["Activity", "InteractionNode"]}, {
                name: "SendTask",
                superClass: ["Task"],
                properties: [{name: "implementation", isAttr: !0, type: "String"}, {
                    name: "operationRef",
                    type: "Operation",
                    isAttr: !0,
                    isReference: !0
                }, {name: "messageRef", type: "Message", isAttr: !0, isReference: !0}]
            }, {
                name: "ReceiveTask",
                superClass: ["Task"],
                properties: [{name: "implementation", isAttr: !0, type: "String"}, {
                    name: "instantiate",
                    "default": !1,
                    isAttr: !0,
                    type: "Boolean"
                }, {name: "operationRef", type: "Operation", isAttr: !0, isReference: !0}, {
                    name: "messageRef",
                    type: "Message",
                    isAttr: !0,
                    isReference: !0
                }]
            }, {
                name: "ScriptTask",
                superClass: ["Task"],
                properties: [{name: "scriptFormat", isAttr: !0, type: "String"}, {name: "script", type: "String"}]
            }, {
                name: "BusinessRuleTask",
                superClass: ["Task"],
                properties: [{name: "implementation", isAttr: !0, type: "String"}]
            }, {
                name: "AdHocSubProcess",
                superClass: ["SubProcess"],
                properties: [{
                    name: "completionCondition",
                    type: "Expression",
                    xml: {serialize: "xsi:type"}
                }, {name: "ordering", type: "AdHocOrdering", isAttr: !0}, {
                    name: "cancelRemainingInstances",
                    "default": !0,
                    isAttr: !0,
                    type: "Boolean"
                }]
            }, {
                name: "Transaction",
                superClass: ["SubProcess"],
                properties: [{name: "protocol", isAttr: !0, type: "String"}, {
                    name: "method",
                    isAttr: !0,
                    type: "String"
                }]
            }, {
                name: "GlobalScriptTask",
                superClass: ["GlobalTask"],
                properties: [{name: "scriptLanguage", isAttr: !0, type: "String"}, {
                    name: "script",
                    isAttr: !0,
                    type: "String"
                }]
            }, {
                name: "GlobalBusinessRuleTask",
                superClass: ["GlobalTask"],
                properties: [{name: "implementation", isAttr: !0, type: "String"}]
            }, {
                name: "ComplexBehaviorDefinition",
                superClass: ["BaseElement"],
                properties: [{name: "condition", type: "FormalExpression"}, {name: "event", type: "ImplicitThrowEvent"}]
            }, {
                name: "ResourceRole",
                superClass: ["BaseElement"],
                properties: [{
                    name: "resourceRef",
                    type: "Resource",
                    isReference: !0
                }, {
                    name: "resourceParameterBindings",
                    type: "ResourceParameterBinding",
                    isMany: !0
                }, {name: "resourceAssignmentExpression", type: "ResourceAssignmentExpression"}, {
                    name: "name",
                    isAttr: !0,
                    type: "String"
                }]
            }, {
                name: "ResourceParameterBinding",
                properties: [{
                    name: "expression",
                    type: "Expression",
                    xml: {serialize: "xsi:type"}
                }, {name: "parameterRef", type: "ResourceParameter", isAttr: !0, isReference: !0}]
            }, {
                name: "ResourceAssignmentExpression",
                properties: [{name: "expression", type: "Expression", xml: {serialize: "xsi:type"}}]
            }, {
                name: "Import",
                properties: [{name: "importType", isAttr: !0, type: "String"}, {
                    name: "location",
                    isAttr: !0,
                    type: "String"
                }, {name: "namespace", isAttr: !0, type: "String"}]
            }, {
                name: "Definitions",
                superClass: ["BaseElement"],
                properties: [{name: "name", isAttr: !0, type: "String"}, {
                    name: "targetNamespace",
                    isAttr: !0,
                    type: "String"
                }, {
                    name: "expressionLanguage",
                    "default": "http://www.w3.org/1999/XPath",
                    isAttr: !0,
                    type: "String"
                }, {
                    name: "typeLanguage",
                    "default": "http://www.w3.org/2001/XMLSchema",
                    isAttr: !0,
                    type: "String"
                }, {name: "imports", type: "Import", isMany: !0}, {
                    name: "extensions",
                    type: "Extension",
                    isMany: !0
                }, {name: "rootElements", type: "RootElement", isMany: !0}, {
                    name: "diagrams",
                    isMany: !0,
                    type: "bpmndi:BPMNDiagram"
                }, {name: "exporter", isAttr: !0, type: "String"}, {
                    name: "relationships",
                    type: "Relationship",
                    isMany: !0
                }, {name: "exporterVersion", isAttr: !0, type: "String"}]
            }],
            emumerations: [{
                name: "ProcessType",
                literalValues: [{name: "None"}, {name: "Public"}, {name: "Private"}]
            }, {
                name: "GatewayDirection",
                literalValues: [{name: "Unspecified"}, {name: "Converging"}, {name: "Diverging"}, {name: "Mixed"}]
            }, {
                name: "EventBasedGatewayType",
                literalValues: [{name: "Parallel"}, {name: "Exclusive"}]
            }, {
                name: "RelationshipDirection",
                literalValues: [{name: "None"}, {name: "Forward"}, {name: "Backward"}, {name: "Both"}]
            }, {
                name: "ItemKind",
                literalValues: [{name: "Physical"}, {name: "Information"}]
            }, {
                name: "ChoreographyLoopType",
                literalValues: [{name: "None"}, {name: "Standard"}, {name: "MultiInstanceSequential"}, {name: "MultiInstanceParallel"}]
            }, {
                name: "AssociationDirection",
                literalValues: [{name: "None"}, {name: "One"}, {name: "Both"}]
            }, {
                name: "MultiInstanceBehavior",
                literalValues: [{name: "None"}, {name: "One"}, {name: "All"}, {name: "Complex"}]
            }, {name: "AdHocOrdering", literalValues: [{name: "Parallel"}, {name: "Sequential"}]}],
            prefix: "bpmn",
            xml: {tagAlias: "lowerCase", typePrefix: "t"}
        }
    }, {}],
    107: [function (e, t, n) {
        t.exports = {
            name: "BPMNDI",
            uri: "http://www.omg.org/spec/BPMN/20100524/DI",
            types: [{
                name: "BPMNDiagram",
                properties: [{
                    name: "plane",
                    type: "BPMNPlane",
                    redefines: "di:Diagram#rootElement"
                }, {name: "labelStyle", type: "BPMNLabelStyle", isMany: !0}],
                superClass: ["di:Diagram"]
            }, {
                name: "BPMNPlane",
                properties: [{
                    name: "bpmnElement",
                    isAttr: !0,
                    isReference: !0,
                    type: "bpmn:BaseElement",
                    redefines: "di:DiagramElement#modelElement"
                }],
                superClass: ["di:Plane"]
            }, {
                name: "BPMNShape",
                properties: [{
                    name: "bpmnElement",
                    isAttr: !0,
                    isReference: !0,
                    type: "bpmn:BaseElement",
                    redefines: "di:DiagramElement#modelElement"
                }, {name: "isHorizontal", isAttr: !0, type: "Boolean"}, {
                    name: "isExpanded",
                    isAttr: !0,
                    type: "Boolean"
                }, {name: "isMarkerVisible", isAttr: !0, type: "Boolean"}, {
                    name: "label",
                    type: "BPMNLabel"
                }, {name: "isMessageVisible", isAttr: !0, type: "Boolean"}, {
                    name: "participantBandKind",
                    type: "ParticipantBandKind",
                    isAttr: !0
                }, {name: "choreographyActivityShape", type: "BPMNShape", isAttr: !0, isReference: !0}],
                superClass: ["di:LabeledShape"]
            }, {
                name: "BPMNEdge",
                properties: [{name: "label", type: "BPMNLabel"}, {
                    name: "bpmnElement",
                    isAttr: !0,
                    isReference: !0,
                    type: "bpmn:BaseElement",
                    redefines: "di:DiagramElement#modelElement"
                }, {
                    name: "sourceElement",
                    isAttr: !0,
                    isReference: !0,
                    type: "di:DiagramElement",
                    redefines: "di:Edge#source"
                }, {
                    name: "targetElement",
                    isAttr: !0,
                    isReference: !0,
                    type: "di:DiagramElement",
                    redefines: "di:Edge#target"
                }, {name: "messageVisibleKind", type: "MessageVisibleKind", isAttr: !0, "default": "initiating"}],
                superClass: ["di:LabeledEdge"]
            }, {
                name: "BPMNLabel",
                properties: [{
                    name: "labelStyle",
                    type: "BPMNLabelStyle",
                    isAttr: !0,
                    isReference: !0,
                    redefines: "di:DiagramElement#style"
                }],
                superClass: ["di:Label"]
            }, {name: "BPMNLabelStyle", properties: [{name: "font", type: "dc:Font"}], superClass: ["di:Style"]}],
            emumerations: [{
                name: "ParticipantBandKind",
                literalValues: [{name: "top_initiating"}, {name: "middle_initiating"}, {name: "bottom_initiating"}, {name: "top_non_initiating"}, {name: "middle_non_initiating"}, {name: "bottom_non_initiating"}]
            }, {name: "MessageVisibleKind", literalValues: [{name: "initiating"}, {name: "non_initiating"}]}],
            associations: [],
            prefix: "bpmndi"
        }
    }, {}],
    108: [function (e, t, n) {
        t.exports = {
            name: "DC",
            uri: "http://www.omg.org/spec/DD/20100524/DC",
            types: [{name: "Boolean"}, {name: "Integer"}, {name: "Real"}, {name: "String"}, {
                name: "Font",
                properties: [{name: "name", type: "String", isAttr: !0}, {
                    name: "size",
                    type: "Real",
                    isAttr: !0
                }, {name: "isBold", type: "Boolean", isAttr: !0}, {
                    name: "isItalic",
                    type: "Boolean",
                    isAttr: !0
                }, {name: "isUnderline", type: "Boolean", isAttr: !0}, {
                    name: "isStrikeThrough",
                    type: "Boolean",
                    isAttr: !0
                }]
            }, {
                name: "Point",
                properties: [{name: "x", type: "Real", "default": "0", isAttr: !0}, {
                    name: "y",
                    type: "Real",
                    "default": "0",
                    isAttr: !0
                }]
            }, {
                name: "Bounds",
                properties: [{name: "x", type: "Real", "default": "0", isAttr: !0}, {
                    name: "y",
                    type: "Real",
                    "default": "0",
                    isAttr: !0
                }, {name: "width", type: "Real", isAttr: !0}, {name: "height", type: "Real", isAttr: !0}]
            }],
            prefix: "dc",
            associations: []
        }
    }, {}],
    109: [function (e, t, n) {
        t.exports = {
            name: "DI",
            uri: "http://www.omg.org/spec/DD/20100524/DI",
            types: [{
                name: "DiagramElement",
                isAbstract: !0,
                properties: [{name: "extension", type: "Extension"}, {
                    name: "owningDiagram",
                    type: "Diagram",
                    isReadOnly: !0,
                    isVirtual: !0,
                    isReference: !0
                }, {
                    name: "owningElement",
                    type: "DiagramElement",
                    isReadOnly: !0,
                    isVirtual: !0,
                    isReference: !0
                }, {
                    name: "modelElement",
                    isReadOnly: !0,
                    isVirtual: !0,
                    isReference: !0,
                    type: "Element"
                }, {
                    name: "style",
                    type: "Style",
                    isReadOnly: !0,
                    isVirtual: !0,
                    isReference: !0
                }, {name: "ownedElement", type: "DiagramElement", isReadOnly: !0, isVirtual: !0, isMany: !0}]
            }, {name: "Node", isAbstract: !0, superClass: ["DiagramElement"]}, {
                name: "Edge",
                isAbstract: !0,
                superClass: ["DiagramElement"],
                properties: [{
                    name: "source",
                    type: "DiagramElement",
                    isReadOnly: !0,
                    isVirtual: !0,
                    isReference: !0
                }, {
                    name: "target",
                    type: "DiagramElement",
                    isReadOnly: !0,
                    isVirtual: !0,
                    isReference: !0
                }, {name: "waypoint", isUnique: !1, isMany: !0, type: "dc:Point", xml: {serialize: "xsi:type"}}]
            }, {
                name: "Diagram",
                isAbstract: !0,
                properties: [{
                    name: "rootElement",
                    type: "DiagramElement",
                    isReadOnly: !0,
                    isVirtual: !0
                }, {name: "name", isAttr: !0, type: "String"}, {
                    name: "documentation",
                    isAttr: !0,
                    type: "String"
                }, {name: "resolution", isAttr: !0, type: "Real"}, {
                    name: "ownedStyle",
                    type: "Style",
                    isReadOnly: !0,
                    isVirtual: !0,
                    isMany: !0
                }]
            }, {
                name: "Shape",
                isAbstract: !0,
                superClass: ["Node"],
                properties: [{name: "bounds", type: "dc:Bounds"}]
            }, {
                name: "Plane",
                isAbstract: !0,
                superClass: ["Node"],
                properties: [{
                    name: "planeElement",
                    type: "DiagramElement",
                    subsettedProperty: "DiagramElement-ownedElement",
                    isMany: !0
                }]
            }, {
                name: "LabeledEdge",
                isAbstract: !0,
                superClass: ["Edge"],
                properties: [{
                    name: "ownedLabel",
                    type: "Label",
                    isReadOnly: !0,
                    subsettedProperty: "DiagramElement-ownedElement",
                    isVirtual: !0,
                    isMany: !0
                }]
            }, {
                name: "LabeledShape",
                isAbstract: !0,
                superClass: ["Shape"],
                properties: [{
                    name: "ownedLabel",
                    type: "Label",
                    isReadOnly: !0,
                    subsettedProperty: "DiagramElement-ownedElement",
                    isVirtual: !0,
                    isMany: !0
                }]
            }, {
                name: "Label",
                isAbstract: !0,
                superClass: ["Node"],
                properties: [{name: "bounds", type: "dc:Bounds"}]
            }, {name: "Style", isAbstract: !0}, {
                name: "Extension",
                properties: [{name: "values", type: "Element", isMany: !0}]
            }],
            associations: [],
            prefix: "di",
            xml: {tagAlias: "lowerCase"}
        }
    }, {}],
    110: [function (e, t, n) {
        t.exports = {
            __depends__: [e("diagram-js/lib/features/interaction-events")],
            __init__: ["directEditing"],
            directEditing: ["type", e("./lib/DirectEditing")]
        }
    }, {"./lib/DirectEditing": 111, "diagram-js/lib/features/interaction-events": 157}],
    111: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            this._eventBus = e, this._providers = [], this._textbox = new a({
                container: t.getContainer(),
                keyHandler: r(this._handleKey, this)
            })
        }

        var r = e("lodash/function/bind"), o = e("lodash/collection/find"), a = e("./TextBox");
        i.$inject = ["eventBus", "canvas"], i.prototype.registerProvider = function (e) {
            this._providers.push(e)
        }, i.prototype.isActive = function () {
            return !!this._active
        }, i.prototype.cancel = function () {
            this._active && (this._fire("cancel"), this.close())
        }, i.prototype._fire = function (e) {
            this._eventBus.fire("directEditing." + e, {active: this._active})
        }, i.prototype.close = function () {
            this._textbox.destroy(), this._fire("deactivate"), this._active = null
        }, i.prototype.complete = function () {
            var e = this._active;
            if (e) {
                var t = this.getValue();
                t !== e.context.text && e.provider.update(e.element, t, e.context.text), this._fire("complete"), this.close()
            }
        }, i.prototype.getValue = function () {
            return this._textbox.getValue()
        }, i.prototype._handleKey = function (e) {
            e.stopPropagation();
            var t = e.keyCode || e.charCode;
            return 27 === t ? (e.preventDefault(), this.cancel()) : 13 !== t || e.shiftKey ? void 0 : (e.preventDefault(), this.complete())
        }, i.prototype.activate = function (e) {
            this.isActive() && this.cancel();
            var t, n = o(this._providers, function (n) {
                return (t = n.activate(e)) ? n : null
            });
            return t && (this._textbox.create(t.bounds, t.style, t.text), this._active = {
                element: e,
                context: t,
                provider: n
            }, this._fire("activate")), !!t
        }, t.exports = i
    }, {"./TextBox": 112, "lodash/collection/find": 273, "lodash/function/bind": 283}],
    112: [function (e, t, n) {
        "use strict";
        function i(e) {
            e.stopPropagation()
        }

        function r(e) {
            this.container = e.container, this.textarea = document.createElement("textarea"), this.keyHandler = e.keyHandler || function () {
            }
        }

        var o = e("lodash/object/assign"), a = e("min-dom/lib/event"), s = e("min-dom/lib/remove");
        t.exports = r, r.prototype.create = function (e, t, n) {
            var r = this.textarea, s = this.container;
            o(r.style, {
                width: e.width + "px",
                height: e.height + "px",
                left: e.x + "px",
                top: e.y + "px",
                position: "absolute",
                textAlign: "center",
                boxSizing: "border-box"
            }, t || {}), r.value = n, r.title = "Press SHIFT+Enter for line feed", a.bind(r, "keydown", this.keyHandler), a.bind(r, "mousedown", i), s.appendChild(r), setTimeout(function () {
                r.parent && r.select(), r.focus()
            }, 100)
        }, r.prototype.destroy = function () {
            var e = this.textarea;
            e.value = "", a.unbind(e, "keydown", this.keyHandler), a.unbind(e, "mousedown", i), s(e)
        }, r.prototype.getValue = function () {
            return this.textarea.value
        }
    }, {"lodash/object/assign": 396, "min-dom/lib/event": 414, "min-dom/lib/remove": 417}],
    113: [function (e, t, n) {
        "use strict";
        function i(e) {
            e = e || [128, 36, 1], this._seed = e.length ? r.rack(e[0], e[1], e[2]) : e
        }

        var r = e("hat");
        t.exports = i, i.prototype.next = function (e) {
            return this._seed(e || !0)
        }, i.prototype.nextPrefixed = function (e, t) {
            var n;
            do n = e + this.next(!0); while (this.assigned(n));
            return this.claim(n, t), n
        }, i.prototype.claim = function (e, t) {
            this._seed.set(e, t || !0)
        }, i.prototype.assigned = function (e) {
            return this._seed.get(e) || !1
        }, i.prototype.unclaim = function (e) {
            delete this._seed.hats[e]
        }
    }, {hat: 114}],
    114: [function (e, t, n) {
        var i = t.exports = function (e, t) {
            if (t || (t = 16), void 0 === e && (e = 128), 0 >= e)return "0";
            for (var n = Math.log(Math.pow(2, e)) / Math.log(t), r = 2; n === 1 / 0; r *= 2)n = Math.log(Math.pow(2, e / r)) / Math.log(t) * r;
            for (var o = n - Math.floor(n), a = "", r = 0; r < Math.floor(n); r++) {
                var s = Math.floor(Math.random() * t).toString(t);
                a = s + a
            }
            if (o) {
                var c = Math.pow(t, o), s = Math.floor(Math.random() * c).toString(t);
                a = s + a
            }
            var l = parseInt(a, t);
            return l !== 1 / 0 && l >= Math.pow(2, e) ? i(e, t) : a
        };
        i.rack = function (e, t, n) {
            var r = function (r) {
                var a = 0;
                do {
                    if (a++ > 10) {
                        if (!n)throw new Error("too many ID collisions, use more bits");
                        e += n
                    }
                    var s = i(e, t)
                } while (Object.hasOwnProperty.call(o, s));
                return o[s] = r, s
            }, o = r.hats = {};
            return r.get = function (e) {
                return r.hats[e]
            }, r.set = function (e, t) {
                return r.hats[e] = t, r
            }, r.bits = e || 128, r.base = t || 16, r
        }
    }, {}],
    115: [function (e, t, n) {
        "function" == typeof Object.create ? t.exports = function (e, t) {
            e.super_ = t, e.prototype = Object.create(t.prototype, {
                constructor: {
                    value: e,
                    enumerable: !1,
                    writable: !0,
                    configurable: !0
                }
            })
        } : t.exports = function (e, t) {
            e.super_ = t;
            var n = function () {
            };
            n.prototype = t.prototype, e.prototype = new n, e.prototype.constructor = e
        }
    }, {}],
    116: [function (e, t, n) {
        t.exports = e("./lib/refs"), t.exports.Collection = e("./lib/collection")
    }, {"./lib/collection": 117, "./lib/refs": 118}],
    117: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i) {
            var r = n.inverse;
            return Object.defineProperty(e, "remove", {
                value: function (e) {
                    var n = this.indexOf(e);
                    return -1 !== n && (this.splice(n, 1), t.unset(e, r, i)), e
                }
            }), Object.defineProperty(e, "contains", {
                value: function (e) {
                    return -1 !== this.indexOf(e)
                }
            }), Object.defineProperty(e, "add", {
                value: function (e) {
                    this.contains(e) || (this.push(e), t.set(e, r, i))
                }
            }), Object.defineProperty(e, "__refs_collection", {value: !0}), e
        }

        function r(e) {
            return e.__refs_collection === !0
        }

        t.exports.extend = i, t.exports.isExtended = r
    }, {}],
    118: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            return Object.prototype.hasOwnProperty.call(e, t.name || t)
        }

        function r(e, t, n) {
            Object.defineProperty(n, t.name, {enumerable: t.enumerable, value: s.extend(n[t.name] || [], e, t, n)})
        }

        function o(e, t, n) {
            var i = t.inverse, r = n[t.name];
            Object.defineProperty(n, t.name, {
                enumerable: t.enumerable, get: function () {
                    return r
                }, set: function (t) {
                    if (t !== r) {
                        var o = r;
                        r = null, o && e.unset(o, i, n), r = t, e.set(r, i, n)
                    }
                }
            })
        }

        function a(e, t) {
            return this instanceof a ? (e.inverse = t, t.inverse = e, this.props = {}, this.props[e.name] = e, void(this.props[t.name] = t)) : new a(e, t)
        }

        var s = e("./collection");
        a.prototype.bind = function (e, t) {
            if ("string" == typeof t) {
                if (!this.props[t])throw new Error("no property <" + t + "> in ref");
                t = this.props[t]
            }
            t.collection ? r(this, t, e) : o(this, t, e)
        }, a.prototype.ensureRefsCollection = function (e, t) {
            var n = e[t.name];
            return s.isExtended(n) || r(this, t, e), n
        }, a.prototype.ensureBound = function (e, t) {
            i(e, t) || this.bind(e, t)
        }, a.prototype.unset = function (e, t, n) {
            e && (this.ensureBound(e, t), t.collection ? this.ensureRefsCollection(e, t).remove(n) : e[t.name] = void 0)
        }, a.prototype.set = function (e, t, n) {
            e && (this.ensureBound(e, t), t.collection ? this.ensureRefsCollection(e, t).add(n) : e[t.name] = n)
        }, t.exports = a
    }, {"./collection": 117}],
    119: [function (e, t, n) {
        t.exports = {__init__: [e("./lib/configure-origin")]}
    }, {"./lib/configure-origin": 120}],
    120: [function (e, t, n) {
        function i(e) {
            var t = e.getLayer("bg"), n = t.group(), i = 30, r = 2, o = {fill: "#CCC", "pointer-events": "none"};
            n.rect(i / -2 - 1, r / -2 - 1, i, r, r / 2).attr(o), n.rect(r / -2 - 1, i / -2 - 1, r, i, r / 2).attr(o), n.text(-40, -10, "(0, 0)").attr(o)
        }

        i.$inject = ["canvas"], t.exports = i
    }, {}],
    121: [function (e, t, n) {
        t.exports = e("./lib/Diagram")
    }, {"./lib/Diagram": 122}],
    122: [function (e, t, n) {
        "use strict";
        function i(e) {
            function t(e) {
                return r.indexOf(e) >= 0
            }

            function n(e) {
                r.push(e)
            }

            function i(e) {
                t(e) || ((e.__depends__ || []).forEach(i), t(e) || (n(e), (e.__init__ || []).forEach(function (e) {
                    o.push(e)
                })))
            }

            var r = [], o = [];
            e.forEach(i);
            var s = new a.Injector(r);
            return o.forEach(function (e) {
                try {
                    s["string" == typeof e ? "get" : "invoke"](e)
                } catch (t) {
                    throw console.error("Failed to instantiate component"), console.error(t.stack), t
                }
            }), s
        }

        function r(t) {
            t = t || {};
            var n = {config: ["value", t]}, r = e("./core"), o = [n, r].concat(t.modules || []);
            return i(o)
        }

        function o(e, t) {
            this.injector = t = t || r(e), this.get = t.get, this.invoke = t.invoke, this.get("eventBus").fire("diagram.init")
        }

        var a = e("didi");
        t.exports = o, o.prototype.destroy = function () {
            this.get("eventBus").fire("diagram.destroy")
        }
    }, {"./core": 131, didi: 260}],
    123: [function (e, t, n) {
        "use strict";
        function i(e) {
            return "object" == typeof e
        }

        function r(e) {
            this._eventBus = e
        }

        function o(e, t) {
            return function (n) {
                return e.call(t || null, n.context, n.command, n)
            }
        }

        var a = e("lodash/collection/forEach"), s = e("lodash/lang/isFunction"), c = e("lodash/lang/isArray"), l = e("lodash/lang/isNumber"), u = 1e3;
        r.$inject = ["eventBus"], t.exports = r, r.prototype.on = function (e, t, n, r, p, d) {
            if ((s(t) || l(t)) && (d = p, p = r, r = n, n = t, t = null), s(n) && (d = p, p = r, r = n, n = u), i(p) && (d = p, p = !1), !s(r))throw new Error("handlerFn must be a function");
            c(e) || (e = [e]);
            var h = this._eventBus;
            a(e, function (e) {
                var i = ["commandStack", e, t].filter(function (e) {
                    return e
                }).join(".");
                h.on(i, n, p ? o(r, d) : r, d)
            })
        };
        var p = ["canExecute", "preExecute", "preExecuted", "execute", "executed", "postExecute", "postExecuted", "revert", "reverted"];
        a(p, function (e) {
            r.prototype[e] = function (t, n, i, r, o) {
                (s(t) || l(t)) && (o = r, r = i, i = n, n = t, t = null), this.on(t, e, n, i, r, o)
            }
        })
    }, {
        "lodash/collection/forEach": 274,
        "lodash/lang/isArray": 387,
        "lodash/lang/isFunction": 388,
        "lodash/lang/isNumber": 390
    }],
    124: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            this._handlerMap = {}, this._stack = [], this._stackIdx = -1, this._currentExecution = {
                actions: [],
                dirty: []
            }, this._injector = t, this._eventBus = e, this._uid = 1
        }

        var r = e("lodash/array/unique"), o = e("lodash/lang/isArray"), a = e("lodash/object/assign"), s = e("../core/EventBus").Event;
        i.$inject = ["eventBus", "injector"], t.exports = i, i.prototype.execute = function (e, t) {
            if (!e)throw new Error("command required");
            var n = {command: e, context: t};
            this._pushAction(n), this._internalExecute(n), this._popAction(n)
        }, i.prototype.canExecute = function (e, t) {
            var n = {command: e, context: t}, i = this._getHandler(e);
            if (!i)return !1;
            var r = this._fire(e, "canExecute", n);
            return void 0 === r && i.canExecute && (r = i.canExecute(t)), r
        }, i.prototype.clear = function () {
            this._stack.length = 0, this._stackIdx = -1, this._fire("changed")
        }, i.prototype.undo = function () {
            var e, t = this._getUndoAction();
            if (t) {
                for (this._pushAction(t); t && (this._internalUndo(t), e = this._getUndoAction(), e && e.id === t.id);)t = e;
                this._popAction()
            }
        }, i.prototype.redo = function () {
            var e, t = this._getRedoAction();
            if (t) {
                for (this._pushAction(t); t && (this._internalExecute(t, !0), e = this._getRedoAction(), e && e.id === t.id);)t = e;
                this._popAction()
            }
        }, i.prototype.register = function (e, t) {
            this._setHandler(e, t)
        }, i.prototype.registerHandler = function (e, t) {
            if (!e || !t)throw new Error("command and handlerCls must be defined");
            var n = this._injector.instantiate(t);
            this.register(e, n)
        }, i.prototype.canUndo = function () {
            return !!this._getUndoAction()
        }, i.prototype.canRedo = function () {
            return !!this._getRedoAction()
        }, i.prototype._getRedoAction = function () {
            return this._stack[this._stackIdx + 1]
        }, i.prototype._getUndoAction = function () {
            return this._stack[this._stackIdx]
        }, i.prototype._internalUndo = function (e) {
            var t = e.command, n = e.context, i = this._getHandler(t);
            this._fire(t, "revert", e), i.revert && this._markDirty(i.revert(n)), this._revertedAction(e), this._fire(t, "reverted", e)
        }, i.prototype._fire = function (e, t, n) {
            arguments.length < 3 && (n = t, t = null);
            var i, r, o, c = t ? [e + "." + t, t] : [e];
            for (n = a(new s, n), i = 0; (r = c[i]) && (o = this._eventBus.fire("commandStack." + r, n), !n.cancelBubble); i++);
            return o
        }, i.prototype._createId = function () {
            return this._uid++
        }, i.prototype._internalExecute = function (e, t) {
            var n = e.command, i = e.context, r = this._getHandler(n);
            if (!r)throw new Error("no command handler registered for <" + n + ">");
            this._pushAction(e), t || (this._fire(n, "preExecute", e), r.preExecute && r.preExecute(i), this._fire(n, "preExecuted", e)), this._fire(n, "execute", e), r.execute && this._markDirty(r.execute(i)), this._executedAction(e, t), this._fire(n, "executed", e), t || (this._fire(n, "postExecute", e), r.postExecute && r.postExecute(i), this._fire(n, "postExecuted", e)), this._popAction(e)
        }, i.prototype._pushAction = function (e) {
            var t = this._currentExecution, n = t.actions, i = n[0];
            e.id || (e.id = i && i.id || this._createId()), n.push(e)
        }, i.prototype._popAction = function () {
            var e = this._currentExecution, t = e.actions, n = e.dirty;
            t.pop(), t.length || (this._eventBus.fire("elements.changed", {elements: r(n)}), n.length = 0, this._fire("changed"))
        }, i.prototype._markDirty = function (e) {
            var t = this._currentExecution;
            e && (e = o(e) ? e : [e], t.dirty = t.dirty.concat(e))
        }, i.prototype._executedAction = function (e, t) {
            var n = ++this._stackIdx;
            t || this._stack.splice(n, this._stack.length, e)
        }, i.prototype._revertedAction = function (e) {
            this._stackIdx--
        }, i.prototype._getHandler = function (e) {
            return this._handlerMap[e]
        }, i.prototype._setHandler = function (e, t) {
            if (!e || !t)throw new Error("command and handler required");
            if (this._handlerMap[e])throw new Error("overriding handler for command <" + e + ">");
            this._handlerMap[e] = t
        }
    }, {"../core/EventBus": 129, "lodash/array/unique": 267, "lodash/lang/isArray": 387, "lodash/object/assign": 396}],
    125: [function (e, t, n) {
        t.exports = {commandStack: ["type", e("./CommandStack")]}
    }, {"./CommandStack": 124}],
    126: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            return Math.round(e * t) / t
        }

        function r(e) {
            return l(e) ? e + "px" : e
        }

        function o(e) {
            e = u({}, {width: "100%", height: "100%"}, e);
            var t = e.container || document.body, n = document.createElement("div");
            return n.setAttribute("class", "djs-container"), u(n.style, {
                position: "relative",
                overflow: "hidden",
                width: r(e.width),
                height: r(e.height)
            }), t.appendChild(n), n
        }

        function a(e, t) {
            return e.group().attr({"class": t})
        }

        function s(e, t, n, i) {
            this._eventBus = t, this._elementRegistry = i, this._graphicsFactory = n, this._init(e || {})
        }

        function c(e, t) {
            var n = "matrix(" + t.a + "," + t.b + "," + t.c + "," + t.d + "," + t.e + "," + t.f + ")";
            e.setAttribute("transform", n)
        }

        var l = e("lodash/lang/isNumber"), u = e("lodash/object/assign"), p = e("lodash/collection/forEach"), d = e("lodash/collection/every"), h = e("lodash/function/debounce"), f = e("../util/Collections"), m = e("../../vendor/snapsvg"), g = "base", v = {
            shape: ["x", "y", "width", "height"],
            connection: ["waypoints"]
        };
        s.$inject = ["config.canvas", "eventBus", "graphicsFactory", "elementRegistry"], t.exports = s, s.prototype._init = function (e) {
            var t = this._eventBus, n = o(e), i = m.createSnapAt("100%", "100%", n), r = a(i, "viewport"), s = this;
            this._container = n, this._svg = i, this._viewport = r, this._layers = {}, t.on("diagram.init", function (e) {
                t.fire("canvas.init", {svg: i, viewport: r})
            }), t.on("diagram.destroy", function () {
                var e = s._container.parentNode;
                e && e.removeChild(n), t.fire("canvas.destroy", {
                    svg: s._svg,
                    viewport: s._viewport
                }), s._svg.remove(), s._svg = s._container = s._layers = s._viewport = null
            }), e.deferUpdate !== !1 && (this._viewboxChanged = h(this._viewboxChanged, 300))
        }, s.prototype.getDefaultLayer = function () {
            return this.getLayer(g)
        }, s.prototype.getLayer = function (e) {
            if (!e)throw new Error("must specify a name");
            var t = this._layers[e];
            return t || (t = this._layers[e] = a(this._viewport, "layer-" + e)), t
        }, s.prototype.getContainer = function () {
            return this._container
        }, s.prototype._updateMarker = function (e, t, n) {
            var i;
            e.id || (e = this._elementRegistry.get(e)), i = this._elementRegistry._elements[e.id], i && (p([i.gfx, i.secondaryGfx], function (e) {
                e && e[n ? "addClass" : "removeClass"](t)
            }), this._eventBus.fire("element.marker.update", {element: e, gfx: i.gfx, marker: t, add: !!n}))
        }, s.prototype.addMarker = function (e, t) {
            this._updateMarker(e, t, !0)
        }, s.prototype.removeMarker = function (e, t) {
            this._updateMarker(e, t, !1)
        }, s.prototype.hasMarker = function (e, t) {
            e.id || (e = this._elementRegistry.get(e));
            var n = this.getGraphics(e);
            return n && n.hasClass(t)
        }, s.prototype.toggleMarker = function (e, t) {
            this.hasMarker(e, t) ? this.removeMarker(e, t) : this.addMarker(e, t)
        }, s.prototype.getRootElement = function () {
            return this._rootElement || this.setRootElement({id: "__implicitroot", children: []}), this._rootElement
        }, s.prototype.setRootElement = function (e, t) {
            this._ensureValid("root", e);
            var n = this._rootElement, i = this._elementRegistry, r = this._eventBus;
            if (n) {
                if (!t)throw new Error("rootElement already set, need to specify override");
                r.fire("root.remove", {element: n}), r.fire("root.removed", {element: n}), i.remove(n)
            }
            var o = this.getDefaultLayer();
            return r.fire("root.add", {element: e}), i.add(e, o, this._svg), r.fire("root.added", {
                element: e,
                gfx: o
            }), this._rootElement = e, e
        }, s.prototype._ensureValid = function (e, t) {
            if (!t.id)throw new Error("element must have an id");
            if (this._elementRegistry.get(t.id))throw new Error("element with id " + t.id + " already exists");
            var n = v[e], i = d(n, function (e) {
                return "undefined" != typeof t[e]
            });
            if (!i)throw new Error("must supply { " + n.join(", ") + " } with " + e)
        }, s.prototype._setParent = function (e, t, n) {
            f.add(t.children, e, n), e.parent = t
        }, s.prototype._addElement = function (e, t, n, i) {
            n = n || this.getRootElement();
            var r = this._eventBus, o = this._graphicsFactory;
            this._ensureValid(e, t), r.fire(e + ".add", {element: t, parent: n}), this._setParent(t, n, i);
            var a = o.create(e, t);
            return this._elementRegistry.add(t, a), o.update(e, t, a), r.fire(e + ".added", {element: t, gfx: a}), t
        }, s.prototype.addShape = function (e, t, n) {
            return this._addElement("shape", e, t, n)
        }, s.prototype.addConnection = function (e, t, n) {
            return this._addElement("connection", e, t, n)
        }, s.prototype._removeElement = function (e, t) {
            var n = this._elementRegistry, i = this._graphicsFactory, r = this._eventBus;
            return (e = n.get(e.id || e)) ? (r.fire(t + ".remove", {element: e}), i.remove(e), f.remove(e.parent && e.parent.children, e), e.parent = null, r.fire(t + ".removed", {element: e}), n.remove(e), e) : void 0
        }, s.prototype.removeShape = function (e) {
            return this._removeElement(e, "shape")
        }, s.prototype.removeConnection = function (e) {
            return this._removeElement(e, "connection")
        }, s.prototype.sendToFront = function (e, t) {
            t !== !1 && (t = !0), t && e.parent && this.sendToFront(e.parent), p(e.children, function (e) {
                this.sendToFront(e, !1)
            }, this);
            var n = this.getGraphics(e), i = n.parent();
            n.remove().appendTo(i)
        }, s.prototype.getGraphics = function (e, t) {
            return this._elementRegistry.getGraphics(e, t)
        }, s.prototype._viewboxChanging = function () {
            this._eventBus.fire("canvas.viewbox.changing")
        }, s.prototype._viewboxChanged = function () {
            this._eventBus.fire("canvas.viewbox.changed", {viewbox: this.viewbox(!1)})
        }, s.prototype.viewbox = function (e) {
            if (void 0 === e && this._cachedViewbox)return this._cachedViewbox;
            var t, n, r, o, a, s = this._viewport, c = this.getSize();
            return e ? (this._viewboxChanging(), r = Math.min(c.width / e.width, c.height / e.height), n = (new m.Matrix).scale(r).translate(-e.x, -e.y), s.transform(n), this._viewboxChanged(), e) : (t = this.getDefaultLayer().getBBox(!0), n = s.transform().localMatrix, r = i(n.a, 1e3), o = i(-n.e || 0, 1e3), a = i(-n.f || 0, 1e3), e = this._cachedViewbox = {
                x: o ? o / r : 0,
                y: a ? a / r : 0,
                width: c.width / r,
                height: c.height / r,
                scale: r,
                inner: {width: t.width, height: t.height, x: t.x, y: t.y},
                outer: c
            })
        }, s.prototype.scroll = function (e) {
            var t = this._viewport.node, n = t.getCTM();
            return e && (this._viewboxChanging(), e = u({
                dx: 0,
                dy: 0
            }, e || {}), n = this._svg.node.createSVGMatrix().translate(e.dx, e.dy).multiply(n), c(t, n), this._viewboxChanged()), {
                x: n.e,
                y: n.f
            }
        }, s.prototype.zoom = function (e, t) {
            if (!e)return this.viewbox(e).scale;
            if ("fit-viewport" === e)return this._fitViewport(t);
            var n, r;
            return this._viewboxChanging(), "object" != typeof t && (n = this.viewbox().outer, t = {
                x: n.width / 2,
                y: n.height / 2
            }), r = this._setZoom(e, t), this._viewboxChanged(), i(r.a, 1e3)
        }, s.prototype._fitViewport = function (e) {
            var t, n, i = this.viewbox(), r = i.outer, o = i.inner;
            return o.x >= 0 && o.y >= 0 && o.x + o.width <= r.width && o.y + o.height <= r.height && !e ? n = {
                x: 0,
                y: 0,
                width: Math.max(o.width + o.x, r.width),
                height: Math.max(o.height + o.y, r.height)
            } : (t = Math.min(1, r.width / o.width, r.height / o.height), n = {
                x: o.x + (e ? o.width / 2 - r.width / t / 2 : 0),
                y: o.y + (e ? o.height / 2 - r.height / t / 2 : 0),
                width: r.width / t,
                height: r.height / t
            }), this.viewbox(n), this.viewbox(!1).scale
        }, s.prototype._setZoom = function (e, t) {
            var n, i, r, o, a, s = this._svg.node, l = this._viewport.node, p = s.createSVGMatrix(), d = s.createSVGPoint();
            r = l.getCTM();
            var h = r.a;
            return t ? (n = u(d, t), i = n.matrixTransform(r.inverse()), o = p.translate(i.x, i.y).scale(1 / h * e).translate(-i.x, -i.y), a = r.multiply(o)) : a = p.scale(e), c(this._viewport.node, a), a
        }, s.prototype.getSize = function () {
            return {width: this._container.clientWidth, height: this._container.clientHeight}
        }, s.prototype.getAbsoluteBBox = function (e) {
            var t, n = this.viewbox();
            if (e.waypoints) {
                var i = this.getGraphics(e), r = i.getBBox(!0);
                t = i.getBBox(), t.x -= r.x, t.y -= r.y, t.width += 2 * r.x, t.height += 2 * r.y
            } else t = e;
            var o = t.x * n.scale - n.x * n.scale, a = t.y * n.scale - n.y * n.scale, s = t.width * n.scale, c = t.height * n.scale;
            return {x: o, y: a, width: s, height: c}
        }
    }, {
        "../../vendor/snapsvg": 258,
        "../util/Collections": 237,
        "lodash/collection/every": 271,
        "lodash/collection/forEach": 274,
        "lodash/function/debounce": 284,
        "lodash/lang/isNumber": 390,
        "lodash/object/assign": 396
    }],
    127: [function (e, t, n) {
        "use strict";
        function i() {
            this._uid = 12
        }

        var r = e("../model");
        t.exports = i, i.prototype.createRoot = function (e) {
            return this.create("root", e)
        }, i.prototype.createLabel = function (e) {
            return this.create("label", e)
        }, i.prototype.createShape = function (e) {
            return this.create("shape", e)
        }, i.prototype.createConnection = function (e) {
            return this.create("connection", e)
        }, i.prototype.create = function (e, t) {
            return t = t || {}, t.id || (t.id = e + "_" + this._uid++), r.create(e, t)
        }
    }, {"../model": 228}],
    128: [function (e, t, n) {
        "use strict";
        function i() {
            this._elements = {}
        }

        var r = "data-element-id";
        t.exports = i, i.prototype.add = function (e, t, n) {
            var i = e.id;
            this._validateId(i), t.attr(r, i), n && n.attr(r, i), this._elements[i] = {
                element: e,
                gfx: t,
                secondaryGfx: n
            }
        }, i.prototype.remove = function (e) {
            var t = this._elements, n = e.id || e, i = n && t[n];
            i && (i.gfx.attr(r, null), i.secondaryGfx && i.secondaryGfx.attr(r, null), delete t[n])
        }, i.prototype.updateId = function (e, t) {
            this._validateId(t), "string" == typeof e && (e = this.get(e));
            var n = this.getGraphics(e), i = this.getGraphics(e, !0);
            this.remove(e), e.id = t, this.add(e, n, i)
        }, i.prototype.get = function (e) {
            var t;
            t = "string" == typeof e ? e : e && e.attr(r);
            var n = this._elements[t];
            return n && n.element
        }, i.prototype.filter = function (e) {
            var t = [];
            return this.forEach(function (n, i) {
                e(n, i) && t.push(n)
            }), t
        }, i.prototype.getAll = function () {
            return this.filter(function (e) {
                return e
            })
        }, i.prototype.forEach = function (e) {
            var t = this._elements;
            Object.keys(t).forEach(function (n) {
                var i = t[n], r = i.element, o = i.gfx;
                return e(r, o)
            })
        }, i.prototype.getGraphics = function (e, t) {
            var n = e.id || e, i = this._elements[n];
            return i && (t ? i.secondaryGfx : i.gfx)
        }, i.prototype._validateId = function (e) {
            if (!e)throw new Error("element must have an id");
            if (this._elements[e])throw new Error("element with id " + e + " already added")
        }
    }, {}],
    129: [function (e, t, n) {
        "use strict";
        function i() {
            this._listeners = {};
            var e = this;
            this.on("diagram.destroy", 1, function () {
                e._listeners = null
            })
        }

        function r() {
        }

        function o(e, t) {
            return e.apply(null, t)
        }

        var a = e("lodash/lang/isFunction"), s = e("lodash/lang/isArray"), c = e("lodash/lang/isNumber"), l = e("lodash/function/bind"), u = e("lodash/object/assign"), p = 1e3, d = Array.prototype.slice;
        t.exports = i, i.prototype.on = function (e, t, n, i) {
            if (e = s(e) ? e : [e], a(t) && (i = n, n = t, t = p), !c(t))throw new Error("priority must be a number");
            i && (n = l(n, i));
            var r = this, o = {priority: t, callback: n};
            e.forEach(function (e) {
                r._addListener(e, o)
            })
        }, i.prototype.once = function (e, t, n) {
            function i() {
                t.apply(n || r, arguments), r.off(e, i)
            }

            var r = this;
            this.on(e, i)
        }, i.prototype.off = function (e, t) {
            var n, i, r = this._getListeners(e);
            if (t)for (i = r.length - 1; n = r[i]; i--)n.callback === t && r.splice(i, 1); else r.length = 0
        }, i.prototype.fire = function (e, t) {
            var n, i, o, a;
            if (a = d.call(arguments), "object" == typeof e && (n = e, e = n.type), !e)throw new Error("no event type specified");
            if (i = this._listeners[e]) {
                t instanceof r ? n = t : (n = new r, n.init(t)), a[0] = n;
                var s = n.type;
                e !== s && (n.type = e);
                try {
                    o = this._invokeListeners(n, a, i)
                } finally {
                    e !== s && (n.type = s)
                }
                return void 0 === o && n.defaultPrevented && (o = !1), o
            }
        }, i.prototype.handleError = function (e) {
            return this.fire("error", {error: e}) === !1
        }, i.prototype._invokeListeners = function (e, t, n) {
            var i, r, o;
            for (i = 0; (r = n[i]) && !e.cancelBubble; i++)o = this._invokeListener(e, t, r);
            return o
        }, i.prototype._invokeListener = function (e, t, n) {
            var i;
            try {
                i = e.returnValue = o(n.callback, t), void 0 !== i && e.stopPropagation(), i === !1 && e.preventDefault()
            } catch (r) {
                if (!this.handleError(r))throw console.error("unhandled error in event listener"), console.error(r.stack), r
            }
            return i
        }, i.prototype._addListener = function (e, t) {
            var n, i, r = this._getListeners(e);
            for (i = 0; n = r[i]; i++)if (n.priority < t.priority)return void r.splice(i, 0, t);
            r.push(t)
        }, i.prototype._getListeners = function (e) {
            var t = this._listeners[e];
            return t || (this._listeners[e] = t = []), t
        }, t.exports.Event = r, r.prototype.stopPropagation = function () {
            this.cancelBubble = !0
        }, r.prototype.preventDefault = function () {
            this.defaultPrevented = !0
        }, r.prototype.init = function (e) {
            u(this, e || {})
        }
    }, {
        "lodash/function/bind": 283,
        "lodash/lang/isArray": 387,
        "lodash/lang/isFunction": 388,
        "lodash/lang/isNumber": 390,
        "lodash/object/assign": 396
    }],
    130: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            this._eventBus = e, this._elementRegistry = t
        }

        var r = e("lodash/collection/forEach"), o = e("lodash/collection/reduce"), a = e("../util/GraphicsUtil"), s = e("min-dom/lib/clear");
        i.$inject = ["eventBus", "elementRegistry"], t.exports = i, i.prototype._getChildren = function (e) {
            var t, n = this._elementRegistry.getGraphics(e);
            return e.parent ? (t = a.getChildren(n), t || (t = n.parent().group().attr("class", "djs-children"))) : t = n, t
        }, i.prototype._clear = function (e) {
            var t = a.getVisual(e);
            return s(t.node), t
        }, i.prototype._createContainer = function (e, t) {
            var n = t.group().attr("class", "djs-group"), i = n.group().attr("class", "djs-element djs-" + e);
            return i.group().attr("class", "djs-visual"), i
        }, i.prototype.create = function (e, t) {
            var n = this._getChildren(t.parent);
            return this._createContainer(e, n)
        }, i.prototype.updateContainments = function (e) {
            var t, n = this, i = this._elementRegistry;
            t = o(e, function (e, t) {
                return t.parent && (e[t.parent.id] = t.parent), e
            }, {}), r(t, function (e) {
                var t = n._getChildren(e), o = e.children;
                o && r(o.slice().reverse(), function (e) {
                    var n = i.getGraphics(e);
                    n.parent().prependTo(t)
                })
            })
        }, i.prototype.drawShape = function (e, t) {
            var n = this._eventBus;
            return n.fire("render.shape", {gfx: e, element: t})
        }, i.prototype.getShapePath = function (e) {
            var t = this._eventBus;
            return t.fire("render.getShapePath", e)
        }, i.prototype.drawConnection = function (e, t) {
            var n = this._eventBus;
            return n.fire("render.connection", {gfx: e, element: t})
        }, i.prototype.getConnectionPath = function (e) {
            var t = this._eventBus;
            return t.fire("render.getConnectionPath", e)
        }, i.prototype.update = function (e, t, n) {
            if (t.parent) {
                var i = this._clear(n);
                if ("shape" === e)this.drawShape(i, t), n.translate(t.x, t.y); else {
                    if ("connection" !== e)throw new Error("unknown type: " + e);
                    this.drawConnection(i, t)
                }
                n.attr("display", t.hidden ? "none" : "block")
            }
        }, i.prototype.remove = function (e) {
            var t = this._elementRegistry.getGraphics(e);
            t.parent().remove()
        }
    }, {
        "../util/GraphicsUtil": 242,
        "lodash/collection/forEach": 274,
        "lodash/collection/reduce": 278,
        "min-dom/lib/clear": 410
    }],
    131: [function (e, t, n) {
        t.exports = {
            __depends__: [e("../draw")],
            __init__: ["canvas"],
            canvas: ["type", e("./Canvas")],
            elementRegistry: ["type", e("./ElementRegistry")],
            elementFactory: ["type", e("./ElementFactory")],
            eventBus: ["type", e("./EventBus")],
            graphicsFactory: ["type", e("./GraphicsFactory")]
        }
    }, {
        "../draw": 135,
        "./Canvas": 126,
        "./ElementFactory": 127,
        "./ElementRegistry": 128,
        "./EventBus": 129,
        "./GraphicsFactory": 130
    }],
    132: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            var n = this;
            t = t || r, e.on(["render.shape", "render.connection"], t, function (e, t) {
                var i = e.type, r = t.element, o = t.gfx;
                return n.canRender(r) ? "render.shape" === i ? n.drawShape(o, r) : n.drawConnection(o, r) : void 0
            }), e.on(["render.getShapePath", "render.getConnectionPath"], t, function (e, t) {
                return n.canRender(t) ? "render.getShapePath" === e.type ? n.getShapePath(t) : n.getConnectionPath(t) : void 0
            })
        }

        var r = 1e3;
        i.prototype.canRender = function () {
        }, i.prototype.drawShape = function () {
        }, i.prototype.drawConnection = function () {
        }, i.prototype.getShapePath = function () {
        }, i.prototype.getConnectionPath = function () {
        }, t.exports = i
    }, {}],
    133: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            o.call(this, e, l), this.CONNECTION_STYLE = t.style(["no-fill"], {
                strokeWidth: 5,
                stroke: "fuchsia"
            }), this.SHAPE_STYLE = t.style({fill: "white", stroke: "fuchsia", strokeWidth: 2})
        }

        var r = e("inherits"), o = e("./BaseRenderer"), a = e("../util/RenderUtil"), s = a.componentsToPath, c = a.createLine, l = 1;
        r(i, o), i.prototype.canRender = function () {
            return !0
        }, i.prototype.drawShape = function (e, t) {
            return e.rect(0, 0, t.width || 0, t.height || 0).attr(this.SHAPE_STYLE)
        }, i.prototype.drawConnection = function (e, t) {
            return c(t.waypoints, this.CONNECTION_STYLE).appendTo(e)
        }, i.prototype.getShapePath = function (e) {
            var t = e.x, n = e.y, i = e.width, r = e.height, o = [["M", t, n], ["l", i, 0], ["l", 0, r], ["l", -i, 0], ["z"]];
            return s(o)
        }, i.prototype.getConnectionPath = function (e) {
            var t, n, i = e.waypoints, r = [];
            for (t = 0; n = i[t]; t++)n = n.original || n, r.push([0 === t ? "M" : "L", n.x, n.y]);
            return s(r)
        }, i.$inject = ["eventBus", "styles"], t.exports = i
    }, {"../util/RenderUtil": 249, "./BaseRenderer": 132, inherits: 253}],
    134: [function (e, t, n) {
        "use strict";
        function i() {
            var e = {
                "no-fill": {fill: "none"},
                "no-border": {strokeOpacity: 0},
                "no-events": {pointerEvents: "none"}
            }, t = this;
            this.cls = function (e, t, n) {
                var i = this.style(t, n);
                return o(i, {"class": e})
            }, this.style = function (t, n) {
                r(t) || n || (n = t, t = []);
                var i = a(t, function (t, n) {
                    return o(t, e[n] || {})
                }, {});
                return n ? o(i, n) : i
            }, this.computeStyle = function (e, n, i) {
                return r(n) || (i = n, n = []), t.style(n || [], o(i, e || {}))
            }
        }

        var r = e("lodash/lang/isArray"), o = e("lodash/object/assign"), a = e("lodash/collection/reduce");
        t.exports = i
    }, {"lodash/collection/reduce": 278, "lodash/lang/isArray": 387, "lodash/object/assign": 396}],
    135: [function (e, t, n) {
        t.exports = {
            __init__: ["defaultRenderer"],
            defaultRenderer: ["type", e("./DefaultRenderer")],
            styles: ["type", e("./Styles")]
        }
    }, {"./DefaultRenderer": 133, "./Styles": 134}],
    136: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i) {
            g.call(this, e), e.on("shape.move.start", m, function (e) {
                var t = e.context, n = t.shapes;
                t.shapes = o(n)
            }), e.on("shape.move.start", f, function (e) {
                var t = e.context, i = t.shapes, o = t.movedAttachers = r(i);
                a(o, function (e) {
                    n.makeDraggable(t, e, !0), e.label && n.makeDraggable(t, e.label, !0)
                })
            }), this.postExecuted(["elements.move"], function (e) {
                var n = e.context, i = n.delta, o = n.newParent, s = n.closure, c = s.enclosedElements, l = r(c);
                a(l, function (e) {
                    c[e.id] || t.moveShape(e, i, o), e.label && !c[e.label.id] && t.moveShape(e.label, i, o)
                })
            }), this.postExecuted(["elements.move"], function (e) {
                var n, i = e.context, r = i.shapes, o = i.newHost;
                r.length > 1 || (n = o ? r : c(r, function (e) {
                    return !!e.host
                }), a(n, function (e) {
                    t.updateAttachment(e, o)
                }))
            }), this.postExecuted(["elements.move"], function (e) {
                var n = e.context.shapes;
                a(n, function (e) {
                    a(e.attachers, function (e) {
                        a(e.outgoing, function (e) {
                            var n = i.allowed("connection.reconnectStart", {
                                connection: e,
                                source: e.source,
                                target: e.target
                            });
                            n || t.removeConnection(e)
                        }), a(e.incoming, function (e) {
                            var n = i.allowed("connection.reconnectEnd", {
                                connection: e,
                                source: e.source,
                                target: e.target
                            });
                            n || t.removeConnection(e)
                        })
                    })
                })
            }), this.postExecute(["shape.create"], function (e) {
                var n = e.context, i = n.shape, r = n.host;
                r && t.updateAttachment(i, r)
            }), this.postExecute(["shape.replace"], function (e) {
                var n = e.context, r = n.oldShape, o = n.newShape;
                p(r.attachers, function (e) {
                    var n = i.allowed("elements.move", {target: o, shapes: [e]});
                    "attach" === n ? t.updateAttachment(e, o) : t.removeShape(e)
                })
            }), this.postExecute(["shape.resize"], function (e) {
                var n = e.context, i = n.shape, r = n.oldBounds, o = n.newBounds, s = i.attachers;
                s.length && a(s, function (e) {
                    var n = d(e, r, o);
                    t.moveShape(e, n, e.parent)
                })
            }), this.preExecute(["shape.delete"], function (e) {
                var n = e.context.shape;
                p(n.attachers, function (e) {
                    t.removeShape(e)
                }), n.host && t.updateAttachment(n, null)
            })
        }

        function r(e) {
            return s(u(e, function (e) {
                return e.attachers || []
            }))
        }

        function o(e) {
            var t = l(e, "id");
            return c(e, function (e) {
                for (; e;) {
                    if (e.host && t[e.host.id])return !1;
                    e = e.parent
                }
                return !0
            })
        }

        var a = e("lodash/collection/forEach"), s = e("lodash/array/flatten"), c = e("lodash/collection/filter"), l = e("lodash/collection/groupBy"), u = e("lodash/collection/map"), p = e("../../util/Removal").saveClear, d = e("../../util/AttachUtil").getNewAttachShapeDelta, h = e("inherits"), f = 250, m = 1500, g = e("../../command/CommandInterceptor");
        h(i, g), i.$inject = ["eventBus", "modeling", "moveVisuals", "rules"], t.exports = i
    }, {
        "../../command/CommandInterceptor": 123,
        "../../util/AttachUtil": 235,
        "../../util/Removal": 248,
        inherits: 253,
        "lodash/array/flatten": 264,
        "lodash/collection/filter": 272,
        "lodash/collection/forEach": 274,
        "lodash/collection/groupBy": 275,
        "lodash/collection/map": 277
    }],
    137: [function (e, t, n) {
        t.exports = {
            __depends__: [e("../move"), e("../label-support")],
            __init__: ["attachSupport"],
            attachSupport: ["type", e("./AttachSupport")]
        }
    }, {"../label-support": 161, "../move": 188, "./AttachSupport": 136}],
    138: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i, f, m, g) {
            function v(e) {
                f.update("connection", e.connection, e.connectionGfx)
            }

            function y(e) {
                return e.filter(function (t, n) {
                    return !r.pointsOnLine(e[n - 1], e[n + 1], t)
                })
            }

            var b = e.get("connectionDocking", !1);
            this.start = function (e, t, r, o) {
                var a, s, c = t.waypoints, l = n.getGraphics(t);
                a = o || 0 !== r ? o || r !== c.length - 1 ? u : d : p, s = {
                    connection: t,
                    bendpointIndex: r,
                    insert: o,
                    type: a
                }, i.activate(e, "bendpoint.move", {data: {connection: t, connectionGfx: l, context: s}})
            }, t.on("bendpoint.move.start", function (e) {
                var t = e.context, i = t.connection, r = i.waypoints, a = r.slice(), s = t.insert, c = t.bendpointIndex;
                t.originalWaypoints = r, s && a.splice(c, 0, null), i.waypoints = a, t.draggerGfx = o.addBendpoint(n.getLayer("overlays")), t.draggerGfx.addClass("djs-dragging"), n.addMarker(i, l)
            }), t.on("bendpoint.move.hover", function (e) {
                e.context.hover = e.hover, n.addMarker(e.hover, c)
            }), t.on(["bendpoint.move.out", "bendpoint.move.cleanup"], function (e) {
                var t = e.context.hover;
                t && (n.removeMarker(t, c), n.removeMarker(t, e.context.target ? a : s))
            }), t.on("bendpoint.move.move", function (e) {
                var t, i, r = e.context, o = r.type, c = e.connection;
                c.waypoints[r.bendpointIndex] = {
                    x: e.x,
                    y: e.y
                }, b && (r.hover && (o === p && (t = r.hover), o === d && (i = r.hover)), c.waypoints = b.getCroppedWaypoints(c, t, i));
                var l = r.allowed = m.allowed(r.type, r);
                l ? r.hover && (n.removeMarker(r.hover, s), n.addMarker(r.hover, a), r.target = r.hover) : l === !1 && r.hover && (n.removeMarker(r.hover, a), n.addMarker(r.hover, s), r.target = null), r.draggerGfx.translate(e.x, e.y), v(e)
            }), t.on(["bendpoint.move.end", "bendpoint.move.cancel"], function (e) {
                var t = e.context, i = t.connection;
                t.draggerGfx.remove(), t.newWaypoints = i.waypoints.slice(), i.waypoints = t.originalWaypoints, n.removeMarker(i, l), n.removeMarker(t.hover, a), n.removeMarker(t.hover, s)
            }), t.on("bendpoint.move.end", function (e) {
                var t = e.context, n = t.newWaypoints, i = t.bendpointIndex, r = n[i], o = t.allowed;
                if (r.x = h(r.x), r.y = h(r.y), o && t.type === p)g.reconnectStart(t.connection, t.target, r); else if (o && t.type === d)g.reconnectEnd(t.connection, t.target, r); else {
                    if (o === !1 || t.type !== u)return v(e), !1;
                    g.updateWaypoints(t.connection, y(n))
                }
            }), t.on("bendpoint.move.cancel", function (e) {
                v(e)
            })
        }

        var r = e("../../util/Geometry"), o = e("./BendpointUtil"), a = "connect-ok", s = "connect-not-ok", c = "connect-hover", l = "djs-updating", u = "connection.updateWaypoints", p = "connection.reconnectStart", d = "connection.reconnectEnd", h = Math.round;
        i.$inject = ["injector", "eventBus", "canvas", "dragging", "graphicsFactory", "rules", "modeling"], t.exports = i
    }, {"../../util/Geometry": 241, "./BendpointUtil": 140}],
    139: [function (e, t, n) {
        "use strict";
        function i(e) {
            function t(e, t) {
                return c.snapTo(e, t)
            }

            function n(e) {
                return o(e, ["x", "y"])
            }

            function i(e) {
                return e.width ? {x: l(e.width / 2 + e.x), y: l(e.height / 2 + e.y)} : void 0
            }

            function u(e) {
                var t = e.snapPoints, n = e.connection, r = n.waypoints, o = e.segmentStart, s = e.segmentStartIndex, c = e.segmentEnd, l = e.segmentEndIndex, u = e.axis;
                if (t)return t;
                var p = [r[s - 1], o, c, r[l + 1]];
                return 2 > s && p.unshift(i(n.source)), l > r.length - 3 && p.unshift(i(n.target)), e.snapPoints = t = {
                    horizontal: [],
                    vertical: []
                }, a(p, function (e) {
                    e && (e = e.original || e, "y" === u && t.horizontal.push(e.y), "x" === u && t.vertical.push(e.x))
                }), t
            }

            function p(e) {
                var t = e.snapPoints, n = e.connection.waypoints, i = e.bendpointIndex;
                if (t)return t;
                var r = [n[i - 1], n[i + 1]];
                return e.snapPoints = t = {horizontal: [], vertical: []}, a(r, function (e) {
                    e && (e = e.original || e, t.horizontal.push(e.y), t.vertical.push(e.x))
                }), t
            }

            e.on("connectionSegment.move.start", function (e) {
                var t = e.context, i = t.segmentStart, r = t.segmentEnd, o = s(i, r);
                t.snapStart = n(o)
            }), e.on("connectionSegment.move.move", 1500, function (e) {
                var n, i, o = e.context, a = u(o), s = o.snapStart, c = s.x + e.dx, l = s.y + e.dy;
                if (a) {
                    n = t(a.vertical, c), i = t(a.horizontal, l);
                    var p = c - n, d = l - i;
                    r(e, {dx: e.dx - p, dy: e.dy - d, x: e.x - p, y: e.y - d})
                }
            }), e.on("bendpoint.move.start", function (e) {
                var t = e.context;
                t.snapStart = n(e)
            }), e.on("bendpoint.move.move", 1500, function (e) {
                var n, o, a = e.context, s = p(a), c = a.snapStart, l = a.target, u = l && i(l), d = c.x + e.dx, h = c.y + e.dy;
                if (s) {
                    n = t(u ? s.vertical.concat([u.x]) : s.vertical, d), o = t(u ? s.horizontal.concat([u.y]) : s.horizontal, h);
                    var f = d - n, m = h - o;
                    r(e, {dx: e.dx - f, dy: e.dy - m, x: e.x - f, y: e.y - m})
                }
            })
        }

        var r = e("lodash/object/assign"), o = e("lodash/object/pick"), a = e("lodash/collection/forEach"), s = e("../../util/Geometry").getMidPoint, c = e("../../../vendor/snapsvg"), l = Math.round;
        i.$inject = ["eventBus"], t.exports = i
    }, {
        "../../../vendor/snapsvg": 258,
        "../../util/Geometry": 241,
        "lodash/collection/forEach": 274,
        "lodash/object/assign": 396,
        "lodash/object/pick": 402
    }],
    140: [function (e, t, n) {
        "use strict";
        function i(e, t, n) {
            var i = e.group(), r = 22, o = 4, s = 6, c = r + s, l = o + s;
            i.rect(-r / 2, -o / 2, r, o).addClass("djs-visual"), i.rect(-c / 2, -l / 2, c, l).addClass("djs-hit");
            var u = (new a.Matrix).rotate("h" === n ? 90 : 0, 0, 0);
            return i.transform(u), i
        }

        var r = e("../../util/Event"), o = e("../../util/Geometry"), a = e("../../../vendor/snapsvg"), s = t.exports.BENDPOINT_CLS = "djs-bendpoint", c = t.exports.SEGMENT_DRAGGER_CLS = "djs-segment-dragger";
        t.exports.toCanvasCoordinates = function (e, t) {
            var n, i = r.toPoint(t), o = e._container.getBoundingClientRect();
            n = {x: o.left, y: o.top};
            var a = e.viewbox();
            return {x: a.x + (i.x - n.x) / a.scale, y: a.y + (i.y - n.y) / a.scale}
        }, t.exports.addBendpoint = function (e, t) {
            var n = e.group().addClass(s);
            return n.circle(0, 0, 4).addClass("djs-visual"), n.circle(0, 0, 10).addClass("djs-hit"), t && n.addClass(t), n
        }, t.exports.addSegmentDragger = function (e, t, n) {
            var r = e.group(), a = o.getMidPoint(t, n), s = o.pointsAligned(t, n);
            return i(r, a, s), r.addClass(c), r.addClass("h" === s ? "vertical" : "horizontal"), r.translate(a.x, a.y), r
        }
    }, {"../../../vendor/snapsvg": 258, "../../util/Event": 240, "../../util/Geometry": 241}],
    141: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i, p) {
            function d(e, n) {
                var i = o.toCanvasCoordinates(t, n), r = u(e, i);
                return r
            }

            function h(e, t, n) {
                var i, r, o, c, l, u, p = e.index, d = e.point;
                return 0 >= p || e.bendpoint ? !1 : (i = t[p - 1], r = t[p], o = s(i, r), c = a(i, r), l = Math.abs(d.x - o.x), u = Math.abs(d.y - o.y), c && n >= l && n >= u)
            }

            function f(e, t) {
                var n = t.waypoints, r = d(n, e);
                r && (h(r, n, 10) ? p.start(e, t, r.index) : i.start(e, t, r.index, !r.bendpoint))
            }

            function m(e, n) {
                var i = t.getLayer("overlays"), o = i.select(".djs-bendpoints[data-element-id=" + e.id + "]");
                return !o && n && (o = i.group().addClass("djs-bendpoints").attr("data-element-id", e.id), r.bind(o.node, "mousedown", function (t) {
                    f(t, e)
                })), o
            }

            function g(e, t) {
                t.waypoints.forEach(function (t, n) {
                    o.addBendpoint(e).translate(t.x, t.y)
                }), o.addBendpoint(e, "floating")
            }

            function v(e, t) {
                for (var n, i, r = t.waypoints, s = 1; s < r.length; s++)n = r[s - 1], i = r[s], a(n, i) && o.addSegmentDragger(e, n, i)
            }

            function y(e) {
                e.selectAll("." + c).forEach(function (e) {
                    e.remove()
                })
            }

            function b(e) {
                e.selectAll("." + l).forEach(function (e) {
                    e.remove()
                })
            }

            function x(e) {
                var t = m(e);
                return t || (t = m(e, !0), g(t, e), v(t, e)), t
            }

            function E(e) {
                var t = m(e);
                t && (b(t), y(t), v(t, e), g(t, e))
            }

            e.on("connection.changed", function (e) {
                E(e.element)
            }), e.on("connection.remove", function (e) {
                var t = m(e.element);
                t && t.remove()
            }), e.on("element.marker.update", function (e) {
                var t, n = e.element;
                n.waypoints && (t = x(n), t[e.add ? "addClass" : "removeClass"](e.marker))
            }), e.on("element.mousemove", function (e) {
                var t, n, i, r = e.element, o = r.waypoints;
                if (o) {
                    if (t = m(r, !0), n = t.select(".floating"), !n)return;
                    i = d(o, e.originalEvent), i && n.translate(i.point.x, i.point.y)
                }
            }), e.on("element.mousedown", function (e) {
                var t = e.originalEvent, n = e.element, i = n.waypoints;
                i && f(t, n, i)
            }), e.on("selection.changed", function (e) {
                var t = e.newSelection, n = t[0];
                n && n.waypoints && x(n)
            }), e.on("element.hover", function (e) {
                var t = e.element;
                t.waypoints && (x(t), n.registerEvent(e.gfx.node, "mousemove", "element.mousemove"))
            }), e.on("element.out", function (e) {
                n.unregisterEvent(e.gfx.node, "mousemove", "element.mousemove")
            })
        }

        var r = e("min-dom/lib/event"), o = e("./BendpointUtil"), a = e("../../util/Geometry").pointsAligned, s = e("../../util/Geometry").getMidPoint, c = o.BENDPOINT_CLS, l = o.SEGMENT_DRAGGER_CLS, u = e("../../util/LineIntersection").getApproxIntersection;
        i.$inject = ["eventBus", "canvas", "interactionEvents", "bendpointMove", "connectionSegmentMove"], t.exports = i
    }, {
        "../../util/Geometry": 241,
        "../../util/LineIntersection": 244,
        "./BendpointUtil": 140,
        "min-dom/lib/event": 414
    }],
    142: [function (e, t, n) {
        "use strict";
        function i(e, t, n) {
            return r(e, t, e[t] + n)
        }

        function r(e, t, n) {
            return {x: "x" === t ? n : e.x, y: "y" === t ? n : e.y}
        }

        function o(e, t, n, i) {
            var o = Math.max(t[i], n[i]), a = Math.min(t[i], n[i]), s = 25, c = Math.min(Math.max(a + s, e[i]), o - s);
            return r(t, i, c)
        }

        function a(e) {
            return "x" === e ? "y" : "x"
        }

        function s(e, t, n) {
            var i, o;
            return e.original ? e.original : (i = p.getMid(t), o = a(n), r(e, o, i[o]))
        }

        function c(e, t, n, r, c, f, m) {
            function g(e) {
                c.update("connection", e.connection, e.connectionGfx)
            }

            function v(e, t, n) {
                var i = e.newWaypoints, r = e.segmentStartIndex + t, s = i[r], c = e.segmentEndIndex + t, l = i[c], u = a(e.axis), p = o(n, s, l, u);
                e.draggerGfx.translate(p.x, p.y)
            }

            function y(e) {
                return e.filter(function (t, n) {
                    return !l.pointsOnLine(e[n - 1], e[n + 1], t)
                })
            }

            var b = e.get("connectionDocking", !1);
            this.start = function (e, t, i) {
                var o, a, c, u = n.getGraphics(t), p = i - 1, d = i, h = t.waypoints, f = h[p], m = h[d];
                a = l.pointsAligned(f, m), a && (c = "v" === a ? "y" : "x", 0 === p && (f = s(f, t.source, c)), d === h.length - 1 && (m = s(m, t.target, c)), o = {
                    connection: t,
                    segmentStartIndex: p,
                    segmentEndIndex: d,
                    segmentStart: f,
                    segmentEnd: m,
                    axis: c
                }, r.activate(e, "connectionSegment.move", {
                    cursor: "x" === c ? "resize-ew" : "resize-ns",
                    data: {connection: t, connectionGfx: u, context: o}
                }))
            }, t.on("connectionSegment.move.start", function (e) {
                var t = e.context, i = e.connection, r = n.getLayer("overlays");
                t.originalWaypoints = i.waypoints.slice(), t.draggerGfx = u.addSegmentDragger(r, t.segmentStart, t.segmentEnd), t.draggerGfx.addClass("djs-dragging"), n.addMarker(i, h)
            }), t.on("connectionSegment.move.move", function (e) {
                var t = e.context, n = t.connection, r = t.segmentStartIndex, o = t.segmentEndIndex, a = t.segmentStart, s = t.segmentEnd, c = t.axis, l = t.originalWaypoints.slice(), u = i(a, c, e["d" + c]), d = i(s, c, e["d" + c]), h = l.length, f = 0;
                l[r] = u, l[o] = d;
                var m, y;
                2 > r && (m = p.getOrientation(n.source, u), 1 === r ? "intersect" === m && (l.shift(), l[0] = u, f--) : "intersect" !== m && (l.unshift(a), f++)), o > h - 3 && (y = p.getOrientation(n.target, d), o === h - 2 ? "intersect" === y && (l.pop(), l[l.length - 1] = d) : "intersect" !== y && l.push(s)), n.waypoints = l, b && (n.waypoints = l = b.getCroppedWaypoints(n)), t.newWaypoints = l, v(t, f, e), g(e)
            }), t.on("connectionSegment.move.hover", function (e) {
                e.context.hover = e.hover, n.addMarker(e.hover, d)
            }), t.on(["connectionSegment.move.out", "connectionSegment.move.cleanup"], function (e) {
                var t = e.context.hover;
                t && n.removeMarker(t, d)
            }), t.on("connectionSegment.move.cleanup", function (e) {
                var t = e.context, i = t.connection;
                t.draggerGfx && t.draggerGfx.remove(), n.removeMarker(i, h)
            }), t.on(["connectionSegment.move.cancel", "connectionSegment.move.end"], function (e) {
                var t = e.context, n = t.connection;
                n.waypoints = t.originalWaypoints, g(e)
            }), t.on("connectionSegment.move.end", function (e) {
                var t = e.context, n = t.newWaypoints;
                n.forEach(function (e) {
                    e.x = Math.round(e.x), e.y = Math.round(e.y)
                }), m.updateWaypoints(t.connection, y(n))
            })
        }

        var l = e("../../util/Geometry"), u = e("./BendpointUtil"), p = e("../../layout/LayoutUtil"), d = "connect-hover", h = "djs-updating";
        c.$inject = ["injector", "eventBus", "canvas", "dragging", "graphicsFactory", "rules", "modeling"], t.exports = c
    }, {"../../layout/LayoutUtil": 226, "../../util/Geometry": 241, "./BendpointUtil": 140}],
    143: [function (e, t, n) {
        t.exports = {
            __depends__: [e("../dragging"), e("../rules")],
            __init__: ["bendpoints", "bendpointSnapping"],
            bendpoints: ["type", e("./Bendpoints")],
            bendpointMove: ["type", e("./BendpointMove")],
            connectionSegmentMove: ["type", e("./ConnectionSegmentMove")],
            bendpointSnapping: ["type", e("./BendpointSnapping")]
        }
    }, {
        "../dragging": 153,
        "../rules": 207,
        "./BendpointMove": 138,
        "./BendpointSnapping": 139,
        "./Bendpoints": 141,
        "./ConnectionSegmentMove": 142
    }],
    144: [function (e, t, n) {
        "use strict";
        function i(e, t, n) {
            e.on("element.changed", function (n) {
                var i = n.element;
                n.gfx || (n.gfx = t.getGraphics(i)), n.gfx && (i.waypoints ? e.fire("connection.changed", n) : e.fire("shape.changed", n))
            }), e.on("elements.changed", function (t) {
                var i = t.elements;
                i.forEach(function (t) {
                    e.fire("element.changed", {element: t})
                }), n.updateContainments(i)
            }), e.on("shape.changed", function (e) {
                n.update("shape", e.element, e.gfx)
            }), e.on("connection.changed", function (e) {
                n.update("connection", e.element, e.gfx)
            })
        }

        i.$inject = ["eventBus", "elementRegistry", "graphicsFactory"], t.exports = i
    }, {}],
    145: [function (e, t, n) {
        t.exports = {__init__: ["changeSupport"], changeSupport: ["type", e("./ChangeSupport")]}
    }, {"./ChangeSupport": 144}],
    146: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i, s, c) {
            function l(e, t) {
                return i.allowed("connection.create", {source: e, target: t})
            }

            function u(e, t, n, i) {
                var o = c.getShapePath(n), a = i && c.getShapePath(i), s = c.getConnectionPath({waypoints: [e, t]});
                return e = r.getElementLineIntersection(o, s, !0) || e, t = i && r.getElementLineIntersection(a, s, !1) || t, [e, t]
            }

            e.on("connect.move", function (e) {
                var t, n, i, o = e.context, a = o.source, s = o.target, c = o.visual;
                t = r.getMid(a), n = {
                    x: e.x,
                    y: e.y
                }, i = u(t, n, a, s), c.attr("points", [i[0].x, i[0].y, i[1].x, i[1].y])
            }), e.on("connect.hover", function (e) {
                var t, n = e.context, i = n.source, r = e.hover;
                t = n.canExecute = l(i, r), null !== t && (n.target = r, s.addMarker(r, t ? o : a))
            }), e.on(["connect.out", "connect.cleanup"], function (e) {
                var t = e.context;
                t.target && s.removeMarker(t.target, t.canExecute ? o : a), t.target = null
            }), e.on("connect.cleanup", function (e) {
                var t = e.context;
                t.visual && t.visual.remove()
            }), e.on("connect.start", function (e) {
                var t, n = e.context;
                t = s.getDefaultLayer().polyline().attr({
                    stroke: "#333",
                    strokeDasharray: [1],
                    strokeWidth: 2,
                    "pointer-events": "none"
                }), n.visual = t
            }), e.on("connect.end", function (e) {
                var t = e.context, i = t.source, r = t.target, o = t.canExecute || l(i, r);
                return o ? void n.connect(i, r) : !1
            }), this.start = function (e, n, i) {
                t.activate(e, "connect", {autoActivate: i, data: {shape: n, context: {source: n}}})
            }
        }

        var r = e("../../layout/LayoutUtil"), o = "connect-ok", a = "connect-not-ok";
        i.$inject = ["eventBus", "dragging", "modeling", "rules", "canvas", "graphicsFactory"], t.exports = i
    }, {"../../layout/LayoutUtil": 226}],
    147: [function (e, t, n) {
        t.exports = {
            __depends__: [e("../selection"), e("../rules"), e("../dragging")],
            connect: ["type", e("./Connect")]
        }
    }, {"../dragging": 153, "../rules": 207, "../selection": 211, "./Connect": 146}],
    148: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            this._providers = [], this._eventBus = e, this._overlays = t, this._current = null, this._init()
        }

        var r = e("lodash/lang/isFunction"), o = e("lodash/collection/forEach"), a = e("min-dom/lib/delegate"), s = e("min-dom/lib/clear"), c = e("min-dom/lib/event"), l = e("min-dom/lib/attr"), u = e("min-dom/lib/query"), p = e("min-dom/lib/classes"), d = e("min-dom/lib/domify"), h = ".entry";
        i.$inject = ["eventBus", "overlays"], i.prototype._init = function () {
            var e = this._eventBus, t = this;
            e.on("selection.changed", function (e) {
                var n = e.newSelection;
                1 === n.length ? t.open(n[0]) : t.close()
            })
        }, i.prototype.registerProvider = function (e) {
            this._providers.push(e)
        }, i.prototype.getEntries = function (e) {
            var t = {};
            return o(this._providers, function (n) {
                var i = n.getContextPadEntries(e);
                o(i, function (e, n) {
                    t[n] = e
                })
            }), t
        }, i.prototype.trigger = function (e, t, n) {
            var i, o, a, s = this._current, c = s.element, u = s.entries, p = t.delegateTarget || t.target;
            if (!p)return t.preventDefault();
            if (i = u[l(p, "data-action")], o = i.action, a = t.originalEvent || t, r(o)) {
                if ("click" === e)return o(a, c, n)
            } else if (o[e])return o[e](a, c, n);
            t.preventDefault()
        }, i.prototype.open = function (e, t) {
            if (this._current && this._current.open) {
                if (t !== !0 && this._current.element === e)return;
                this.close()
            }
            this._updateAndOpen(e)
        }, i.prototype._updateAndOpen = function (e) {
            var t = this.getEntries(e), n = this.getPad(e), i = n.html;
            s(i), o(t, function (e, t) {
                var n, r = e.group || "default", o = d(e.html || '<div class="entry" draggable="true"></div>');
                l(o, "data-action", t), n = u("[data-group=" + r + "]", i), n || (n = d('<div class="group" data-group="' + r + '"></div>'), i.appendChild(n)), n.appendChild(o), e.className && p(o).add(e.className), e.title && l(o, "title", e.title), e.imageUrl && o.appendChild(d('<img src="' + e.imageUrl + '">'))
            }), p(i).add("open"), this._current = {
                element: e,
                pad: n,
                entries: t,
                open: !0
            }, this._eventBus.fire("contextPad.open", {current: this._current})
        }, i.prototype.getPad = function (e) {
            var t = this, n = this._overlays, i = n.get({element: e, type: "context-pad"});
            if (!i.length) {
                var r = d('<div class="djs-context-pad"></div>');
                a.bind(r, h, "click", function (e) {
                    t.trigger("click", e)
                }), a.bind(r, h, "dragstart", function (e) {
                    t.trigger("dragstart", e)
                }), c.bind(r, "mousedown", function (e) {
                    e.stopPropagation()
                }), n.add(e, "context-pad", {position: {right: -9, top: -6}, html: r}), i = n.get({
                    element: e,
                    type: "context-pad"
                }), this._eventBus.fire("contextPad.create", {element: e, pad: i[0]})
            }
            return i[0]
        }, i.prototype.close = function () {
            var e;
            this._current && (this._current.open && (e = this._current.pad.html, p(e).remove("open")), this._current.open = !1, this._eventBus.fire("contextPad.close", {current: this._current}))
        }, i.prototype.isOpen = function () {
            return this._current && this._current.open
        }, t.exports = i
    }, {
        "lodash/collection/forEach": 274,
        "lodash/lang/isFunction": 388,
        "min-dom/lib/attr": 408,
        "min-dom/lib/classes": 409,
        "min-dom/lib/clear": 410,
        "min-dom/lib/delegate": 412,
        "min-dom/lib/domify": 413,
        "min-dom/lib/event": 414,
        "min-dom/lib/query": 416
    }],
    149: [function (e, t, n) {
        t.exports = {
            __depends__: [e("../interaction-events"), e("../overlays")],
            contextPad: ["type", e("./ContextPad")]
        }
    }, {"../interaction-events": 157, "../overlays": 193, "./ContextPad": 148}],
    150: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i, c, l, u) {
            function p(e, t, i, r) {
                return i ? n.allowed("shape.append", {
                    source: i,
                    shape: e,
                    target: t,
                    position: r
                }) : n.allowed("shape.create", {shape: e, target: t, position: r})
            }

            function d(e, t) {
                [a, r, o, s].forEach(function (n) {
                    n === t ? c.addMarker(e, n) : c.removeMarker(e, n)
                })
            }

            function h(e) {
                var t, n, i;
                return t = c.getDefaultLayer().group().attr(l.cls("djs-drag-group", ["no-events"])), n = t.group().addClass("djs-dragger"), n.translate(e.width / -2, e.height / -2), i = n.group().addClass("djs-visual"), u.drawShape(i, e), t
            }

            e.on("create.move", function (e) {
                var t = e.context, n = t.shape, i = t.visual;
                i || (i = t.visual = h(n)), i.translate(e.x, e.y);
                var r, c = e.hover, l = {x: e.x, y: e.y};
                r = t.canExecute = c && p(t.shape, c, t.source, l), c && null !== r && (t.target = c, "attach" === r ? d(c, a) : d(c, t.canExecute ? s : o))
            }), e.on(["create.end", "create.out", "create.cleanup"], function (e) {
                var t = e.context, n = t.target;
                n && d(n, null)
            }), e.on("create.end", function (e) {
                var t, n = e.context, r = n.source, o = n.shape, a = n.target, s = n.canExecute, c = {x: e.x, y: e.y};
                return s ? (r ? o = i.appendShape(r, o, c, a) : (t = "attach" === s, o = i.createShape(o, c, a, t)), void(n.shape = o)) : !1
            }), e.on("create.cleanup", function (e) {
                var t = e.context;
                t.visual && t.visual.remove()
            }), this.start = function (e, n, i) {
                t.activate(e, "create", {
                    cursor: "grabbing",
                    autoActivate: !0,
                    data: {shape: n, context: {shape: n, source: i}}
                })
            }
        }

        var r = "drop-ok", o = "drop-not-ok", a = "attach-ok", s = "new-parent";
        i.$inject = ["eventBus", "dragging", "rules", "modeling", "canvas", "styles", "graphicsFactory"], t.exports = i
    }, {}],
    151: [function (e, t, n) {
        t.exports = {__depends__: [e("../dragging"), e("../selection"), e("../rules")], create: ["type", e("./Create")]}
    }, {"../dragging": 153, "../rules": 207, "../selection": 211, "./Create": 150}],
    152: [function (e, t, n) {
        "use strict";
        function i(e) {
            e instanceof MouseEvent ? p.stopEvent(e, !0) : p.preventDefault(e)
        }

        function r(e) {
            return Math.sqrt(Math.pow(e.x, 2) + Math.pow(e.y, 2))
        }

        function o(e, t) {
            return {x: e.x - t.x, y: e.y - t.y}
        }

        function a(e, t) {
            return {x: e.x + t.x, y: e.y + t.y}
        }

        function s(e, t, n) {
            function s(e) {
                var n = t.viewbox(), i = t._container.getBoundingClientRect();
                return {x: n.x + c((e.x - i.left) / n.scale), y: n.y + c((e.y - i.top) / n.scale)}
            }

            function m(e) {
                var n = t.viewbox();
                return {x: c(e.x / n.scale), y: c(e.y / n.scale)}
            }

            function g(t) {
                var n = l(new f, T.payload, T.data);
                return e.fire("drag." + t, n) === !1 ? !1 : e.fire(T.prefix + "." + t, n)
            }

            function v(e, t) {
                var s = T.payload, c = T.globalStart, u = p.toPoint(e), d = o(u, c), f = T.localStart, v = m(d), y = a(f, v);
                if (!T.active && (t || r(d) > T.threshold)) {
                    if (l(s, {x: f.x, y: f.y, dx: 0, dy: 0}, {originalEvent: e}), !1 === g("start"))return S();
                    T.active = !0, T.keepSelection || (s.previousSelection = n.get(), n.select(null)), T.cursor && h.set(T.cursor)
                }
                i(e), T.active && (l(s, {x: y.x, y: y.y, dx: v.x, dy: v.y}, {originalEvent: e}), g("move"))
            }

            function y(e) {
                var t = !0;
                T.active && (e && (T.payload.originalEvent = e, i(e)), t = g("end")), t === !1 && g("rejected"), C(t !== !0)
            }

            function b(e) {
                27 === e.which && (e.preventDefault(), S())
            }

            function x(e) {
                var t;
                T.active && (t = d.install(), setTimeout(t, 400)), y(e)
            }

            function E(e) {
                v(e)
            }

            function w(e) {
                var t = T.payload;
                t.hoverGfx = e.gfx, t.hover = e.element, g("hover")
            }

            function _(e) {
                g("out");
                var t = T.payload;
                t.hoverGfx = null, t.hover = null
            }

            function S(e) {
                T && (T.active && g("cancel"), C(e))
            }

            function C(t) {
                g("cleanup"), h.unset(), u.unbind(document, "mousemove", v), u.unbind(document, "mousedown", x, !0), u.unbind(document, "mouseup", x, !0), u.unbind(document, "keyup", b), u.unbind(document, "touchstart", E, !0), u.unbind(document, "touchcancel", S, !0), u.unbind(document, "touchmove", v, !0), u.unbind(document, "touchend", y, !0), e.off("element.hover", w), e.off("element.out", _);
                var i = T.payload.previousSelection;
                t !== !1 && i && !n.get().length && n.select(i), T = null
            }

            function A(t, n, r, o) {
                T && S(!1), "string" == typeof n && (o = r, r = n, n = null), o = l({}, j, o || {});
                var a, c, d = o.data || {};
                t ? (a = p.getOriginal(t) || t, c = p.toPoint(t), i(t)) : (a = null, c = {
                    x: 0,
                    y: 0
                }), n || (n = s(c)), T = l({
                    prefix: r,
                    data: d,
                    payload: {},
                    globalStart: c,
                    localStart: n
                }, o), o.manual || ("undefined" != typeof TouchEvent && a instanceof TouchEvent ? (u.bind(document, "touchstart", E, !0), u.bind(document, "touchcancel", S, !0), u.bind(document, "touchmove", v, !0), u.bind(document, "touchend", y, !0)) : (u.bind(document, "mousemove", v), u.bind(document, "mousedown", x, !0), u.bind(document, "mouseup", x, !0)), u.bind(document, "keyup", b), e.on("element.hover", w), e.on("element.out", _)), g("activate"), o.autoActivate && v(t, !0)
            }

            var T, j = {threshold: 5};
            e.on("diagram.destroy", S), this.activate = A, this.move = v, this.hover = w, this.out = _, this.end = y, this.cancel = S, this.active = function () {
                return T
            }, this.setOptions = function (e) {
                l(j, e)
            }
        }

        var c = Math.round, l = e("lodash/object/assign"), u = e("min-dom/lib/event"), p = e("../../util/Event"), d = e("../../util/ClickTrap"), h = e("../../util/Cursor"), f = e("../../core/EventBus").Event;
        s.$inject = ["eventBus", "canvas", "selection"], t.exports = s
    }, {
        "../../core/EventBus": 129,
        "../../util/ClickTrap": 236,
        "../../util/Cursor": 238,
        "../../util/Event": 240,
        "lodash/object/assign": 396,
        "min-dom/lib/event": 414
    }],
    153: [function (e, t, n) {
        t.exports = {__depends__: [e("../selection")], dragging: ["type", e("./Dragging")]}
    }, {"../selection": 211, "./Dragging": 152}],
    154: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i, r, o) {
            this._actions = {
                undo: function () {
                    t.undo()
                }, redo: function () {
                    t.redo()
                }, stepZoom: function (e) {
                    r.stepZoom(e.value)
                }, zoom: function (e) {
                    o.zoom(e.value)
                }, removeSelection: function () {
                    var e = i.get();
                    e.length && n.removeElements(e.slice())
                }, moveCanvas: function (e) {
                    var t = 0, n = 0, i = e.invertY, r = e.speed, a = r / Math.min(Math.sqrt(o.viewbox().scale), 1);
                    switch (e.direction) {
                        case"left":
                            t = a;
                            break;
                        case"up":
                            n = a;
                            break;
                        case"right":
                            t = -a;
                            break;
                        case"down":
                            n = -a
                    }
                    n && i && (n = -n), o.scroll({dx: t, dy: n})
                }
            }
        }

        function r(e, t) {
            return new Error(e + " " + t)
        }

        var o = e("lodash/collection/forEach"), a = "is not a registered action", s = "is already registered";
        i.$inject = ["eventBus", "commandStack", "modeling", "selection", "zoomScroll", "canvas"], t.exports = i, i.prototype.trigger = function (e, t) {
            if (!this._actions[e])throw r(e, a);
            return this._actions[e](t)
        }, i.prototype.register = function (e, t) {
            return "string" == typeof e ? this._registerAction(e, t) : void o(e, function (e, t) {
                this._registerAction(t, e)
            }, this)
        }, i.prototype._registerAction = function (e, t) {
            if (this.isRegistered(e))throw r(e, s);
            this._actions[e] = t
        }, i.prototype.unregister = function (e) {
            if (!this.isRegistered(e))throw r(e, a);
            this._actions[e] = void 0
        }, i.prototype.length = function () {
            return Object.keys(this._actions).length
        }, i.prototype.isRegistered = function (e) {
            return !!this._actions[e]
        }
    }, {"lodash/collection/forEach": 274}],
    155: [function (e, t, n) {
        t.exports = {
            __depends__: [e("../selection"), e("../../navigation/zoomscroll")],
            __init__: ["editorActions"],
            editorActions: ["type", e("./EditorActions")]
        }
    }, {"../../navigation/zoomscroll": 234, "../selection": 211, "./EditorActions": 154}],
    156: [function (e, t, n) {
        "use strict";
        function i(e, t, n) {
            function i(n, i) {
                var r, o = i.delegateTarget || i.target, a = o && new s(o), c = t.get(a);
                a && c && (r = e.fire(n, {
                    element: c,
                    gfx: a,
                    originalEvent: i
                }), r === !1 && (i.stopPropagation(), i.preventDefault()))
            }

            function c(e) {
                var t = g[e];
                return t || (t = g[e] = function (t) {
                    a(t) && i(e, t)
                }), t
            }

            function p(e, t, n) {
                var i = c(n);
                i.$delegate = o.bind(e, y, t, i)
            }

            function d(e, t, n) {
                o.unbind(e, t, c(n).$delegate)
            }

            function h(e) {
                r(v, function (t, n) {
                    p(e.node, n, t)
                })
            }

            function f(e) {
                r(v, function (t, n) {
                    d(e.node, n, t)
                })
            }

            var m = n.cls("djs-hit", ["no-fill", "no-border"], {
                stroke: "white",
                strokeWidth: 15
            }), g = {}, v = {
                mouseover: "element.hover",
                mouseout: "element.out",
                click: "element.click",
                dblclick: "element.dblclick",
                mousedown: "element.mousedown",
                mouseup: "element.mouseup"
            }, y = "svg, .djs-element";
            e.on("canvas.destroy", function (e) {
                f(e.svg)
            }), e.on("canvas.init", function (e) {
                h(e.svg)
            }), e.on(["shape.added", "connection.added"], function (e) {
                var t, n, i = e.element, r = e.gfx;
                i.waypoints ? (t = l(i.waypoints), n = "connection") : (t = s.create("rect", {
                    x: 0,
                    y: 0,
                    width: i.width,
                    height: i.height
                }), n = "shape"), t.attr(m).appendTo(r.node)
            }), e.on("shape.changed", function (e) {
                var t = e.element, n = e.gfx, i = n.select(".djs-hit");
                i.attr({width: t.width, height: t.height})
            }), e.on("connection.changed", function (e) {
                var t = e.element, n = e.gfx, i = n.select(".djs-hit");
                u(i, t.waypoints)
            }), this.fire = i, this.mouseHandler = c, this.registerEvent = p, this.unregisterEvent = d
        }

        var r = e("lodash/collection/forEach"), o = e("min-dom/lib/delegate"), a = e("../../util/Mouse").isPrimaryButton, s = e("../../../vendor/snapsvg"), c = e("../../util/RenderUtil"), l = c.createLine, u = c.updateLine;
        i.$inject = ["eventBus", "elementRegistry", "styles"], t.exports = i
    }, {
        "../../../vendor/snapsvg": 258,
        "../../util/Mouse": 246,
        "../../util/RenderUtil": 249,
        "lodash/collection/forEach": 274,
        "min-dom/lib/delegate": 412
    }],
    157: [function (e, t, n) {
        t.exports = {__init__: ["interactionEvents"], interactionEvents: ["type", e("./InteractionEvents")]}
    }, {"./InteractionEvents": 156}],
    158: [function (e, t, n) {
        "use strict";
        function i(e, t, n) {
            var i = this;
            this._config = e || {}, this._eventBus = t, this._editorActions = n, this._listeners = [], this._keyHandler = function (e) {
                var t, n, r = e.target, o = i._listeners, a = e.keyCode || e.charCode || -1;
                if (!c(r, "input, textarea"))for (t = 0; n = o[t]; t++)n(a, e) && (e.preventDefault(), e.stopPropagation())
            }, t.on("diagram.destroy", function () {
                i._fire("destroy"), i.unbind(), i._listeners = null
            }), t.on("diagram.init", function () {
                i._fire("init"), e && e.bindTo && i.bind(e.bindTo)
            }), this._init()
        }

        function r(e) {
            return e.ctrlKey || e.metaKey || e.shiftKey || e.altKey
        }

        function o(e) {
            return e.ctrlKey || e.metaKey
        }

        function a(e) {
            return e.shiftKey
        }

        var s = e("min-dom/lib/event"), c = e("min-dom/lib/matches");
        i.$inject = ["config.keyboard", "eventBus", "editorActions"], t.exports = i, i.prototype.bind = function (e) {
            this._node = e, s.bind(e, "keydown", this._keyHandler, !0), this._fire("bind")
        }, i.prototype.getBinding = function () {
            return this._node
        }, i.prototype.unbind = function () {
            var e = this._node;
            e && (this._fire("unbind"), s.unbind(e, "keydown", this._keyHandler, !0)), this._node = null
        }, i.prototype._fire = function (e) {
            this._eventBus.fire("keyboard." + e, {node: this._node, listeners: this._listeners})
        }, i.prototype._init = function () {
            function e(e, t) {
                return o(t) && !a(t) && 90 === e ? (u.trigger("undo"), !0) : void 0
            }

            function t(e, t) {
                return o(t) && (89 === e || 90 === e && a(t)) ? (u.trigger("redo"), !0) : void 0
            }

            function n(e, t) {
                return 107 !== e && 187 !== e && 171 !== e && 61 !== e || !o(t) ? void 0 : (u.trigger("stepZoom", {value: 1}), !0)
            }

            function i(e, t) {
                return 109 !== e && 189 !== e && 173 !== e || !o(t) ? void 0 : (u.trigger("stepZoom", {value: -1}), !0)
            }

            function r(e, t) {
                return 96 !== e && 48 !== e || !o(t) ? void 0 : (u.trigger("zoom", {value: 1}), !0)
            }

            function s(e, t) {
                return 46 === e ? (u.trigger("removeSelection"), !0) : void 0
            }

            function c(e, t) {
                if ([37, 38, 39, 40].indexOf(e) >= 0) {
                    var n = {invertY: p.invertY, speed: p.speed || 50};
                    switch (e) {
                        case 37:
                            n.direction = "left";
                            break;
                        case 38:
                            n.direction = "up";
                            break;
                        case 39:
                            n.direction = "right";
                            break;
                        case 40:
                            n.direction = "down"
                    }
                    return u.trigger("moveCanvas", n), !0
                }
            }

            var l = this._listeners, u = this._editorActions, p = this._config;
            l.push(e), l.push(t), l.push(s), l.push(n), l.push(i), l.push(r), l.push(c)
        }, i.prototype.addListener = function (e) {
            this._listeners.push(e)
        }, i.prototype.hasModifier = r, i.prototype.isCmd = o, i.prototype.isShift = a
    }, {"min-dom/lib/event": 414, "min-dom/lib/matches": 415}],
    159: [function (e, t, n) {
        t.exports = {__depends__: [e("../editor-actions")], __init__: ["keyboard"], keyboard: ["type", e("./Keyboard")]}
    }, {"../editor-actions": 155, "./Keyboard": 158}],
    160: [function (e, t, n) {
        "use strict";
        function i(e, t, n) {
            u.call(this, e), e.on("shape.move.start", l, function (e) {
                var t = e.context, n = t.shapes;
                t.shapes = r(n)
            }), e.on("shape.move.start", c, function (e) {
                var t = e.context, i = t.shapes, r = [];
                o(i, function (e) {
                    var n = e.label;
                    n && !n.hidden && -1 === t.shapes.indexOf(n) && r.push(n), e.labelTarget && r.push(e)
                }), o(r, function (e) {
                    n.makeDraggable(t, e, !0)
                })
            }), this.postExecuted(["elements.move"], function (e) {
                var n = e.context, i = n.closure, r = i.enclosedElements;
                o(r, function (e) {
                    e.label && !r[e.label.id] && t.moveShape(e.label, n.delta, e.parent)
                })
            })
        }

        function r(e) {
            return a(e, function (t) {
                return -1 === e.indexOf(t.labelTarget)
            })
        }

        var o = e("lodash/collection/forEach"), a = e("lodash/collection/filter"), s = e("inherits"), c = 250, l = 1500, u = e("../../command/CommandInterceptor");
        s(i, u), i.$inject = ["eventBus", "modeling", "moveVisuals"], t.exports = i
    }, {
        "../../command/CommandInterceptor": 123,
        inherits: 253,
        "lodash/collection/filter": 272,
        "lodash/collection/forEach": 274
    }],
    161: [function (e, t, n) {
        t.exports = {
            __depends__: [e("../move")],
            __init__: ["labelSupport"],
            labelSupport: ["type", e("./LabelSupport")]
        }
    }, {"../move": 188, "./LabelSupport": 160}],
    162: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i, o) {
            this._selection = o, this._dragging = n;
            var a = this, l = {
                create: function (e) {
                    var n, i = t.getDefaultLayer();
                    n = e.frame = c.create("rect", {
                        "class": "djs-lasso-overlay",
                        width: 1,
                        height: 1,
                        x: 0,
                        y: 0
                    }), n.appendTo(i)
                }, update: function (e) {
                    var t = e.frame, n = e.bbox;
                    t.attr({x: n.x, y: n.y, width: n.width, height: n.height})
                }, remove: function (e) {
                    e.frame && e.frame.remove()
                }
            };
            e.on("lasso.selection.end", function (e) {
                setTimeout(function () {
                    a.activateLasso(e.originalEvent, !0)
                })
            }), e.on("lasso.end", function (e) {
                var t = r(e), n = i.filter(function (e) {
                    return e
                });
                a.select(n, t)
            }), e.on("lasso.start", function (e) {
                var t = e.context;
                t.bbox = r(e), l.create(t)
            }), e.on("lasso.move", function (e) {
                var t = e.context;
                t.bbox = r(e), l.update(t)
            }), e.on("lasso.end", function (e) {
                var t = e.context;
                l.remove(t)
            }), e.on("lasso.cleanup", function (e) {
                var t = e.context;
                l.remove(t)
            }), e.on("element.mousedown", 1500, function (e) {
                s(e) && (a.activateLasso(e.originalEvent), e.stopPropagation())
            })
        }

        function r(e) {
            var t, n = {x: e.x - e.dx, y: e.y - e.dy}, i = {x: e.x, y: e.y};
            return t = n.x <= i.x && n.y < i.y || n.x < i.x && n.y <= i.y ? {
                x: n.x,
                y: n.y,
                width: i.x - n.x,
                height: i.y - n.y
            } : n.x >= i.x && n.y < i.y || n.x > i.x && n.y <= i.y ? {
                x: i.x,
                y: n.y,
                width: n.x - i.x,
                height: i.y - n.y
            } : n.x <= i.x && n.y > i.y || n.x < i.x && n.y >= i.y ? {
                x: n.x,
                y: i.y,
                width: i.x - n.x,
                height: n.y - i.y
            } : n.x >= i.x && n.y > i.y || n.x > i.x && n.y >= i.y ? {
                x: i.x,
                y: i.y,
                width: n.x - i.x,
                height: n.y - i.y
            } : {x: i.x, y: i.y, width: 0, height: 0}
        }

        var o = e("lodash/object/values"), a = e("../../util/Elements").getEnclosedElements, s = e("../../util/Mouse").hasSecondaryModifier, c = e("../../../vendor/snapsvg");
        i.$inject = ["eventBus", "canvas", "dragging", "elementRegistry", "selection"], t.exports = i, i.prototype.activateLasso = function (e, t) {
            this._dragging.activate(e, "lasso", {autoActivate: t, cursor: "crosshair", data: {context: {}}})
        }, i.prototype.activateSelection = function (e) {
            this._dragging.activate(e, "lasso.selection", {cursor: "crosshair"})
        }, i.prototype.select = function (e, t) {
            var n = a(e, t);
            this._selection.select(o(n))
        }
    }, {
        "../../../vendor/snapsvg": 258,
        "../../util/Elements": 239,
        "../../util/Mouse": 246,
        "lodash/object/values": 404
    }],
    163: [function (e, t, n) {
        "use strict";
        t.exports = {__init__: ["lassoTool"], lassoTool: ["type", e("./LassoTool")]}
    }, {"./LassoTool": 162}],
    164: [function (e, t, n) {
        "use strict";
        function i(e, t, n) {
            this._eventBus = e, this._elementFactory = t, this._commandStack = n;
            var i = this;
            e.on("diagram.init", function () {
                i.registerHandlers(n)
            })
        }

        var r = e("lodash/collection/forEach"), o = e("../../model");
        i.$inject = ["eventBus", "elementFactory", "commandStack"], t.exports = i, i.prototype.getHandlers = function () {
            return {
                "shape.append": e("./cmd/AppendShapeHandler"),
                "shape.create": e("./cmd/CreateShapeHandler"),
                "shape.delete": e("./cmd/DeleteShapeHandler"),
                "shape.move": e("./cmd/MoveShapeHandler"),
                "shape.resize": e("./cmd/ResizeShapeHandler"),
                "shape.replace": e("./cmd/ReplaceShapeHandler"),
                spaceTool: e("./cmd/SpaceToolHandler"),
                "label.create": e("./cmd/CreateLabelHandler"),
                "connection.create": e("./cmd/CreateConnectionHandler"),
                "connection.delete": e("./cmd/DeleteConnectionHandler"),
                "connection.move": e("./cmd/MoveConnectionHandler"),
                "connection.layout": e("./cmd/LayoutConnectionHandler"),
                "connection.updateWaypoints": e("./cmd/UpdateWaypointsHandler"),
                "connection.reconnectStart": e("./cmd/ReconnectConnectionHandler"),
                "connection.reconnectEnd": e("./cmd/ReconnectConnectionHandler"),
                "elements.move": e("./cmd/MoveElementsHandler"),
                "elements.delete": e("./cmd/DeleteElementsHandler"),
                "element.updateAttachment": e("./cmd/UpdateAttachmentHandler"),
                "element.updateAnchors": e("./cmd/UpdateAnchorsHandler")
            }
        }, i.prototype.registerHandlers = function (e) {
            r(this.getHandlers(), function (t, n) {
                e.registerHandler(n, t)
            })
        }, i.prototype.moveShape = function (e, t, n, i, r) {
            "object" == typeof i && (r = i, i = null);
            var o = {shape: e, delta: t, newParent: n, newParentIndex: i, hints: r || {}};
            this._commandStack.execute("shape.move", o)
        }, i.prototype.updateAttachment = function (e, t) {
            var n = {shape: e, newHost: t};
            this._commandStack.execute("element.updateAttachment", n)
        }, i.prototype.moveElements = function (e, t, n, i, r) {
            "object" == typeof i && (r = i, i = void 0);
            var o, a = n;
            i === !0 && (o = n, a = n.parent), i === !1 && (o = null);
            var s = {shapes: e, delta: t, newParent: a, newHost: o, hints: r || {}};
            this._commandStack.execute("elements.move", s)
        }, i.prototype.updateAnchors = function (e, t) {
            var n = {
                element: e, oldBounds: t
            };
            this._commandStack.execute("element.updateAnchors", n)
        }, i.prototype.moveConnection = function (e, t, n, i, r) {
            "object" == typeof i && (r = i, i = void 0);
            var o = {connection: e, delta: t, newParent: n, newParentIndex: i, hints: r || {}};
            this._commandStack.execute("connection.move", o)
        }, i.prototype.layoutConnection = function (e, t) {
            var n = {connection: e, hints: t || {}};
            this._commandStack.execute("connection.layout", n)
        }, i.prototype.createConnection = function (e, t, n, i, r) {
            "object" == typeof n && (r = i, i = n, n = void 0), i = this._create("connection", i);
            var o = {source: e, target: t, parent: r, parentIndex: n, connection: i};
            return this._commandStack.execute("connection.create", o), o.connection
        }, i.prototype.createShape = function (e, t, n, i, r) {
            "boolean" == typeof i && (r = i, i = void 0), e = this._create("shape", e);
            var o = {position: t, shape: e, parent: n, parentIndex: i, host: e.host};
            return r && (o.parent = n.parent, o.host = n), this._commandStack.execute("shape.create", o), o.shape
        }, i.prototype.createLabel = function (e, t, n, i) {
            n = this._create("label", n);
            var r = {labelTarget: e, position: t, parent: i || e.parent, shape: n};
            return this._commandStack.execute("label.create", r), r.shape
        }, i.prototype.appendShape = function (e, t, n, i, r, o) {
            t = this._create("shape", t);
            var a = {source: e, position: n, parent: i, shape: t, connection: r, connectionParent: o};
            return this._commandStack.execute("shape.append", a), a.shape
        }, i.prototype.removeElements = function (e) {
            var t = {elements: e};
            this._commandStack.execute("elements.delete", t)
        }, i.prototype.removeShape = function (e, t) {
            var n = {shape: e, hints: t || {}};
            this._commandStack.execute("shape.delete", n)
        }, i.prototype.removeConnection = function (e, t) {
            var n = {connection: e, hints: t || {}};
            this._commandStack.execute("connection.delete", n)
        }, i.prototype.replaceShape = function (e, t, n) {
            var i = {oldShape: e, newData: t, options: n};
            return this._commandStack.execute("shape.replace", i), i.newShape
        }, i.prototype.resizeShape = function (e, t) {
            var n = {shape: e, newBounds: t};
            this._commandStack.execute("shape.resize", n)
        }, i.prototype.createSpace = function (e, t, n, i) {
            var r = {movingShapes: e, resizingShapes: t, delta: n, direction: i};
            this._commandStack.execute("spaceTool", r)
        }, i.prototype.updateWaypoints = function (e, t) {
            var n = {connection: e, newWaypoints: t};
            this._commandStack.execute("connection.updateWaypoints", n)
        }, i.prototype.reconnectStart = function (e, t, n) {
            var i = {connection: e, newSource: t, dockingOrPoints: n};
            this._commandStack.execute("connection.reconnectStart", i)
        }, i.prototype.reconnectEnd = function (e, t, n) {
            var i = {connection: e, newTarget: t, dockingOrPoints: n};
            this._commandStack.execute("connection.reconnectEnd", i)
        }, i.prototype.connect = function (e, t, n) {
            return this.createConnection(e, t, n || {}, e.parent)
        }, i.prototype._create = function (e, t) {
            return t instanceof o.Base ? t : this._elementFactory.create(e, t)
        }
    }, {
        "../../model": 228,
        "./cmd/AppendShapeHandler": 165,
        "./cmd/CreateConnectionHandler": 166,
        "./cmd/CreateLabelHandler": 167,
        "./cmd/CreateShapeHandler": 168,
        "./cmd/DeleteConnectionHandler": 169,
        "./cmd/DeleteElementsHandler": 170,
        "./cmd/DeleteShapeHandler": 171,
        "./cmd/LayoutConnectionHandler": 172,
        "./cmd/MoveConnectionHandler": 173,
        "./cmd/MoveElementsHandler": 174,
        "./cmd/MoveShapeHandler": 175,
        "./cmd/ReconnectConnectionHandler": 177,
        "./cmd/ReplaceShapeHandler": 178,
        "./cmd/ResizeShapeHandler": 179,
        "./cmd/SpaceToolHandler": 180,
        "./cmd/UpdateAnchorsHandler": 181,
        "./cmd/UpdateAttachmentHandler": 182,
        "./cmd/UpdateWaypointsHandler": 183,
        "lodash/collection/forEach": 274
    }],
    165: [function (e, t, n) {
        "use strict";
        function i(e) {
            this._modeling = e
        }

        function r(e, t) {
            return o(e.outgoing, function (e) {
                return e.target === t
            })
        }

        var o = e("lodash/collection/any"), a = e("inherits");
        a(i, e("./NoopHandler")), i.$inject = ["modeling"], t.exports = i, i.prototype.preExecute = function (e) {
            if (!e.source)throw new Error("source required");
            var t = e.parent || e.source.parent, n = this._modeling.createShape(e.shape, e.position, t);
            e.shape = n
        }, i.prototype.postExecute = function (e) {
            var t = e.connectionParent || e.shape.parent;
            r(e.source, e.shape) || this._modeling.connect(e.source, e.shape, e.connection, t)
        }
    }, {"./NoopHandler": 176, inherits: 253, "lodash/collection/any": 270}],
    166: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            this._canvas = e, this._layouter = t
        }

        i.$inject = ["canvas", "layouter"], t.exports = i, i.prototype.execute = function (e) {
            var t = e.source, n = e.target, i = e.parent;
            if (!t || !n)throw new Error("source and target required");
            if (!i)throw new Error("parent required");
            var r = e.connection;
            return r.source = t, r.target = n, r.waypoints || (r.waypoints = this._layouter.layoutConnection(r)), this._canvas.addConnection(r, i), r
        }, i.prototype.revert = function (e) {
            var t = e.connection;
            this._canvas.removeConnection(t), t.source = null, t.target = null
        }
    }, {}],
    167: [function (e, t, n) {
        "use strict";
        function i(e) {
            a.call(this, e)
        }

        function r(e) {
            ["width", "height"].forEach(function (t) {
                "undefined" == typeof e[t] && (e[t] = 0)
            })
        }

        var o = e("inherits"), a = e("./CreateShapeHandler");
        o(i, a), i.$inject = ["canvas"], t.exports = i;
        var s = a.prototype.execute;
        i.prototype.execute = function (e) {
            var t = e.shape;
            return r(t), t.labelTarget = e.labelTarget, s.call(this, e)
        };
        var c = a.prototype.revert;
        i.prototype.revert = function (e) {
            return e.shape.labelTarget = null, c.call(this, e)
        }
    }, {"./CreateShapeHandler": 168, inherits: 253}],
    168: [function (e, t, n) {
        "use strict";
        function i(e) {
            this._canvas = e
        }

        var r = e("lodash/object/assign"), o = Math.round;
        i.$inject = ["canvas"], t.exports = i, i.prototype.execute = function (e) {
            var t = e.shape, n = e.position, i = e.parent, a = e.parentIndex;
            if (!i)throw new Error("parent required");
            if (!n)throw new Error("position required");
            return void 0 !== n.width ? r(t, n) : r(t, {
                x: n.x - o(t.width / 2),
                y: n.y - o(t.height / 2)
            }), this._canvas.addShape(t, i, a), t
        }, i.prototype.revert = function (e) {
            this._canvas.removeShape(e.shape)
        }
    }, {"lodash/object/assign": 396}],
    169: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            this._canvas = e, this._modeling = t
        }

        var r = e("../../../util/Collections");
        i.$inject = ["canvas", "modeling"], t.exports = i, i.prototype.preExecute = function (e) {
            var t = e.connection;
            t.label && this._modeling.removeShape(t.label)
        }, i.prototype.execute = function (e) {
            var t = e.connection, n = t.parent;
            e.parent = n, e.parentIndex = r.indexOf(n.children, t), e.source = t.source, e.target = t.target, this._canvas.removeConnection(t), t.source = null, t.target = null, t.label = null
        }, i.prototype.revert = function (e) {
            var t = e.connection, n = e.parent, i = e.parentIndex;
            t.source = e.source, t.target = e.target, r.add(n.children, t, i), this._canvas.addConnection(t, n)
        }
    }, {"../../../util/Collections": 237}],
    170: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            this._modeling = e, this._elementRegistry = t
        }

        var r = e("lodash/collection/forEach"), o = e("inherits");
        o(i, e("./NoopHandler")), i.$inject = ["modeling", "elementRegistry"], t.exports = i, i.prototype.postExecute = function (e) {
            var t = this._modeling, n = this._elementRegistry, i = e.elements;
            r(i, function (e) {
                n.get(e.id) && (e.waypoints ? t.removeConnection(e) : t.removeShape(e))
            })
        }
    }, {"./NoopHandler": 176, inherits: 253, "lodash/collection/forEach": 274}],
    171: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            this._canvas = e, this._modeling = t
        }

        var r = e("../../../util/Collections"), o = e("../../../util/Removal").saveClear;
        i.$inject = ["canvas", "modeling"], t.exports = i, i.prototype.preExecute = function (e) {
            var t = this._modeling, n = e.shape, i = n.label;
            n.labelTarget && (e.labelTarget = n.labelTarget, n.labelTarget = null), i && this._modeling.removeShape(i, {nested: !0}), o(n.incoming, function (e) {
                t.removeConnection(e, {nested: !0})
            }), o(n.outgoing, function (e) {
                t.removeConnection(e, {nested: !0})
            }), o(n.children, function (e) {
                t.removeShape(e, {nested: !0})
            })
        }, i.prototype.execute = function (e) {
            var t = this._canvas, n = e.shape, i = n.parent;
            e.oldParent = i, e.oldParentIndex = r.indexOf(i.children, n), n.label = null, t.removeShape(n)
        }, i.prototype.revert = function (e) {
            var t = this._canvas, n = e.shape, i = e.oldParent, o = e.oldParentIndex, a = e.labelTarget;
            r.add(i.children, n, o), a && (a.label = n), t.addShape(n, i)
        }
    }, {"../../../util/Collections": 237, "../../../util/Removal": 248}],
    172: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            this._layouter = e, this._canvas = t
        }

        function r(e) {
            return e[e.length - 1]
        }

        function o(e) {
            function t(e, n) {
                var i = e.indexOf(n);
                if (0 > i && n) {
                    var r = n.parent;
                    i = t(e, r)
                }
                return i
            }

            var n = e.parent.children, i = n.indexOf(e), r = t(n, e.source), o = t(n, e.target), a = Math.max(r + 1, o + 1, i);
            return a > i && (n.splice(a, 0, e), n.splice(i, 1)), a
        }

        var a = e("lodash/object/assign");
        i.$inject = ["layouter", "canvas"], t.exports = i, i.prototype.execute = function (e) {
            var t = e.connection, n = t.parent, i = n.children, s = i.indexOf(t), c = t.waypoints;
            return a(e, {
                oldWaypoints: c,
                oldIndex: s
            }), o(t), t.waypoints = this._layouter.layoutConnection(t, e.hints), c.length ? (c[0].original && !t.waypoints[0].original && (t.waypoints[0].original = {
                x: c[0].original.x,
                y: c[0].original.y
            }), r(c).original && !r(t.waypoints).original && (r(t.waypoints).original = {
                x: r(c).original.x,
                y: r(c).original.y
            }), t) : t
        }, i.prototype.revert = function (e) {
            var t = e.connection, n = t.parent, i = n.children, r = i.indexOf(t), o = e.oldIndex;
            return t.waypoints = e.oldWaypoints, o !== r && (i.splice(r, 1), i.splice(o, 0, t)), t
        }
    }, {"lodash/object/assign": 396}],
    173: [function (e, t, n) {
        "use strict";
        function i() {
        }

        var r = e("lodash/collection/forEach"), o = e("../../../util/Collections");
        t.exports = i, i.prototype.execute = function (e) {
            var t = e.connection, n = e.delta, i = e.newParent || t.parent, a = e.newParentIndex, s = t.parent;
            e.oldParent = s, e.oldParentIndex = o.remove(s.children, t), o.add(i.children, t, a), t.parent = i;
            var c = e.hints.updateAnchors !== !1;
            return r(t.waypoints, function (e) {
                e.x += n.x, e.y += n.y, c && e.original && (e.original.x += n.x, e.original.y += n.y)
            }), t
        }, i.prototype.revert = function (e) {
            var t = e.connection, n = t.parent, i = e.oldParent, a = e.oldParentIndex, s = e.delta;
            o.remove(n.children, t), o.add(i.children, t, a), t.parent = i;
            var c = e.hints.updateAnchors !== !1;
            return r(t.waypoints, function (e) {
                e.x -= s.x, e.y -= s.y, c && e.original && (e.original.x -= s.x, e.original.y -= s.y)
            }), t
        }
    }, {"../../../util/Collections": 237, "lodash/collection/forEach": 274}],
    174: [function (e, t, n) {
        "use strict";
        function i(e) {
            this._helper = new r(e)
        }

        var r = e("./helper/MoveHelper");
        i.$inject = ["modeling"], t.exports = i, i.prototype.preExecute = function (e) {
            e.closure = this._helper.getClosure(e.shapes)
        }, i.prototype.postExecute = function (e) {
            var t, n = e.hints;
            n && n.primaryShape && (t = n.primaryShape, n.oldParent = t.parent), this._helper.moveClosure(e.closure, e.delta, e.newParent, e.newHost, t)
        }, i.prototype.execute = function (e) {
        }, i.prototype.revert = function (e) {
        }
    }, {"./helper/MoveHelper": 184}],
    175: [function (e, t, n) {
        "use strict";
        function i(e) {
            this._modeling = e, this._helper = new s(e)
        }

        var r = e("lodash/object/assign"), o = e("lodash/collection/forEach"), a = e("lodash/object/pick"), s = e("./helper/MoveHelper"), c = e("../../../util/Collections");
        i.$inject = ["modeling"], t.exports = i, i.prototype.execute = function (e) {
            var t = e.shape, n = e.delta, i = e.newParent || t.parent, o = e.newParentIndex, s = t.parent;
            return e.oldBounds = a(t, ["x", "y", "width", "height"]), e.oldParent = s, e.oldParentIndex = c.remove(s.children, t), c.add(i.children, t, o), r(t, {
                parent: i,
                x: t.x + n.x,
                y: t.y + n.y
            }), t
        }, i.prototype.postExecute = function (e) {
            var t = e.shape, n = e.oldBounds, i = this._modeling;
            e.hints.updateAnchors !== !1 && i.updateAnchors(t, n), e.hints.layout !== !1 && (o(t.incoming, function (e) {
                i.layoutConnection(e, {endChanged: !0})
            }), o(t.outgoing, function (e) {
                i.layoutConnection(e, {startChanged: !0})
            })), e.hints.recurse !== !1 && this.moveChildren(e)
        }, i.prototype.revert = function (e) {
            var t = e.shape, n = e.oldParent, i = e.oldParentIndex, o = e.delta;
            return c.add(n.children, t, i), r(t, {parent: n, x: t.x - o.x, y: t.y - o.y}), t
        }, i.prototype.moveChildren = function (e) {
            var t = e.delta, n = e.shape;
            this._helper.moveRecursive(n.children, t, null)
        }, i.prototype.getNewParent = function (e) {
            return e.newParent || e.shape.parent
        }
    }, {
        "../../../util/Collections": 237,
        "./helper/MoveHelper": 184,
        "lodash/collection/forEach": 274,
        "lodash/object/assign": 396,
        "lodash/object/pick": 402
    }],
    176: [function (e, t, n) {
        "use strict";
        function i() {
        }

        t.exports = i, i.prototype.execute = function () {
        }, i.prototype.revert = function () {
        }
    }, {}],
    177: [function (e, t, n) {
        "use strict";
        function i() {
        }

        var r = e("lodash/lang/isArray");
        i.$inject = [], t.exports = i, i.prototype.execute = function (e) {
            var t, n = e.newSource, i = e.newTarget, o = e.connection, a = e.dockingOrPoints, s = o.waypoints;
            if (!n && !i)throw new Error("newSource or newTarget are required");
            if (n && i)throw new Error("must specify either newSource or newTarget");
            return e.oldWaypoints = s, r(a) ? t = a : (t = s.slice(), t.splice(n ? 0 : -1, 1, a)), n && (e.oldSource = o.source, o.source = n), i && (e.oldTarget = o.target, o.target = i), o.waypoints = t, o
        }, i.prototype.revert = function (e) {
            var t = e.newSource, n = e.newTarget, i = e.connection;
            return t && (i.source = e.oldSource), n && (i.target = e.oldTarget), i.waypoints = e.oldWaypoints, i
        }
    }, {"lodash/lang/isArray": 387}],
    178: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            this._modeling = e, this._rules = t
        }

        var r = e("lodash/collection/forEach");
        i.$inject = ["modeling", "rules"], t.exports = i, i.prototype.preExecute = function (e) {
            var t, n = this._modeling, i = this._rules, o = e.oldShape, a = e.newData, s = {x: a.x, y: a.y};
            t = e.newShape = e.newShape || n.createShape(a, s, o.parent), o.host && n.updateAttachment(t, o.host), n.moveElements(o.children, {
                x: 0,
                y: 0
            }, t);
            var c = o.incoming.slice(), l = o.outgoing.slice();
            r(c, function (e) {
                var r = e.waypoints, o = r[r.length - 1], a = i.allowed("connection.reconnectEnd", {
                    source: e.source,
                    target: t,
                    connection: e
                });
                a && n.reconnectEnd(e, t, o)
            }), r(l, function (e) {
                var r = e.waypoints, o = r[0], a = i.allowed("connection.reconnectStart", {
                    source: t,
                    target: e.target,
                    connection: e
                });
                a && n.reconnectStart(e, t, o)
            })
        }, i.prototype.postExecute = function (e) {
            var t = this._modeling, n = e.oldShape;
            t.removeShape(n)
        }, i.prototype.execute = function (e) {
        }, i.prototype.revert = function (e) {
        }
    }, {"lodash/collection/forEach": 274}],
    179: [function (e, t, n) {
        "use strict";
        function i(e) {
            this._modeling = e
        }

        var r = e("lodash/object/assign"), o = e("lodash/collection/forEach");
        i.$inject = ["modeling"], t.exports = i, i.prototype.execute = function (e) {
            var t = e.shape, n = e.newBounds;
            if (void 0 === n.x || void 0 === n.y || void 0 === n.width || void 0 === n.height)throw new Error("newBounds must have {x, y, width, height} properties");
            if (n.width < 10 || n.height < 10)throw new Error("width and height cannot be less than 10px");
            return e.oldBounds = {width: t.width, height: t.height, x: t.x, y: t.y}, r(t, {
                width: n.width,
                height: n.height,
                x: n.x,
                y: n.y
            }), t
        }, i.prototype.postExecute = function (e) {
            var t = e.shape, n = e.oldBounds, i = this._modeling;
            i.updateAnchors(t, n), o(t.incoming, function (e) {
                i.layoutConnection(e, {endChanged: !0})
            }), o(t.outgoing, function (e) {
                i.layoutConnection(e, {startChanged: !0})
            })
        }, i.prototype.revert = function (e) {
            var t = e.shape, n = e.oldBounds;
            return r(t, {width: n.width, height: n.height, x: n.x, y: n.y}), t
        }
    }, {"lodash/collection/forEach": 274, "lodash/object/assign": 396}],
    180: [function (e, t, n) {
        "use strict";
        function i(e) {
            this._modeling = e
        }

        var r = e("lodash/collection/forEach"), o = e("../../space-tool/SpaceUtil");
        i.$inject = ["modeling"], t.exports = i, i.prototype.preExecute = function (e) {
            var t = this._modeling, n = e.resizingShapes, i = e.delta, a = e.direction;
            r(n, function (e) {
                var n = o.resizeBounds(e, a, i);
                t.resizeShape(e, n)
            })
        }, i.prototype.postExecute = function (e) {
            var t = this._modeling, n = e.movingShapes, i = e.delta;
            t.moveElements(n, i)
        }, i.prototype.execute = function (e) {
        }, i.prototype.revert = function (e) {
        }
    }, {"../../space-tool/SpaceUtil": 217, "lodash/collection/forEach": 274}],
    181: [function (e, t, n) {
        "use strict";
        function i() {
        }

        var r = e("lodash/collection/forEach"), o = e("../../../util/AttachUtil").getNewAttachPoint;
        t.exports = i, i.prototype.execute = function (e) {
            function t(e, t) {
                r(e, function (e) {
                    var r = e.waypoints, c = "end" === t ? r.length - 1 : 0, l = r[c];
                    s.push({point: l, oldOriginal: l.original}), l.original = o(l.original || l, i, n), a.push(e)
                })
            }

            var n = e.element, i = e.oldBounds, a = e.changedConnections = [], s = e.oldAnchors = [];
            return t(n.incoming, "end"), t(n.outgoing, "start"), a
        }, i.prototype.revert = function (e) {
            var t = e.oldAnchors, n = e.changedConnections;
            return r(t, function (e) {
                e.point.original = e.oldOriginal
            }), n
        }
    }, {"../../../util/AttachUtil": 235, "lodash/collection/forEach": 274}],
    182: [function (e, t, n) {
        "use strict";
        function i(e) {
            this._modeling = e
        }

        function r(e, t) {
            return a.remove(e && e.attachers, t)
        }

        function o(e, t, n) {
            if (e) {
                var i = e.attachers;
                i || (e.attachers = i = []), a.add(i, t, n)
            }
        }

        var a = e("../../../util/Collections");
        t.exports = i, i.$inject = ["modeling"], i.prototype.execute = function (e) {
            var t = e.shape, n = e.newHost, i = t.host;
            return e.oldHost = i, e.attacherIdx = r(i, t), o(n, t), t.host = n, t
        }, i.prototype.revert = function (e) {
            var t = e.shape, n = e.newHost, i = e.oldHost, a = e.attacherIdx;
            return t.host = i, r(n, t), o(i, t, a), t
        }
    }, {"../../../util/Collections": 237}],
    183: [function (e, t, n) {
        "use strict";
        function i() {
        }

        t.exports = i, i.prototype.execute = function (e) {
            var t = e.connection, n = e.newWaypoints;
            return e.oldWaypoints = t.waypoints, t.waypoints = n, t
        }, i.prototype.revert = function (e) {
            var t = e.connection, n = e.oldWaypoints;
            return t.waypoints = n, t
        }
    }, {}],
    184: [function (e, t, n) {
        "use strict";
        function i(e) {
            this._modeling = e
        }

        var r = e("lodash/collection/forEach"), o = e("../../../../util/Elements");
        t.exports = i, i.prototype.moveRecursive = function (e, t, n) {
            return e ? this.moveClosure(this.getClosure(e), t, n) : []
        }, i.prototype.moveClosure = function (e, t, n, i, o) {
            var a = this._modeling, s = e.allShapes, c = e.allConnections, l = e.enclosedConnections, u = e.topLevel, p = !1;
            o && o.parent === n && (p = !0), r(s, function (e) {
                a.moveShape(e, t, u[e.id] && !p && n, {recurse: !1, layout: !1})
            }), r(c, function (e) {
                var i = !!s[e.source.id], r = !!s[e.target.id];
                l[e.id] && i && r ? a.moveConnection(e, t, u[e.id] && n, {updateAnchors: !1}) : a.layoutConnection(e, {
                    startChanged: i,
                    endChanged: r
                })
            })
        }, i.prototype.getClosure = function (e) {
            return o.getClosure(e)
        }
    }, {"../../../../util/Elements": 239, "lodash/collection/forEach": 274}],
    185: [function (e, t, n) {
        t.exports = {
            __depends__: [e("../../command"), e("../change-support"), e("../rules")],
            __init__: ["modeling"],
            modeling: ["type", e("./Modeling")],
            layouter: ["type", e("../../layout/BaseLayouter")]
        }
    }, {
        "../../command": 125,
        "../../layout/BaseLayouter": 224,
        "../change-support": 145,
        "../rules": 207,
        "./Modeling": 164
    }],
    186: [function (e, t, n) {
        "use strict";
        function i(e) {
            return {x: e.x + h(e.width / 2), y: e.y + h(e.height / 2)}
        }

        function r(e, t, n, r, s) {
            function c(e, t, n, i) {
                return s.allowed("elements.move", {shapes: e, delta: t, position: n, target: i})
            }

            function f(e, n, r) {
                if (!n.waypoints && n.parent) {
                    var o = i(n);
                    t.activate(e, o, "shape.move", {cursor: "grabbing", autoActivate: r, data: {shape: n, context: {}}})
                }
            }

            e.on("shape.move.start", p, function (e) {
                var t = e.context, n = e.shape, i = r.get().slice();
                -1 === i.indexOf(n) && (i = [n]), i = o(i), a(t, {shapes: i, shape: n})
            }), e.on("shape.move.start", u, function (e) {
                var t, n = e.context, i = n.shapes;
                return t = n.canExecute = c(i), t ? void 0 : (e.stopPropagation(), !1)
            }), e.on("shape.move.move", l, function (e) {
                var t, n = e.context, i = n.shapes, r = e.hover, o = {x: e.dx, y: e.dy}, a = {x: e.x, y: e.y};
                return t = c(i, o, a, r), n.delta = o, n.canExecute = t, null === t ? void(n.target = null) : void(n.target = r)
            }), e.on("shape.move.end", function (e) {
                var t = e.context, i = t.delta, r = t.canExecute, o = "attach" === r;
                return r ? (i.x = h(i.x), i.y = h(i.y), void n.moveElements(t.shapes, i, t.target, o, {primaryShape: t.shape})) : !1
            }), e.on("element.mousedown", function (e) {
                var t = d(e);
                if (!t)throw new Error("must supply DOM mousedown event");
                f(t, e.element)
            }), this.start = f
        }

        function o(e) {
            var t = c(e, "id");
            return s(e, function (e) {
                for (; e = e.parent;)if (t[e.id])return !1;
                return !0
            })
        }

        var a = e("lodash/object/assign"), s = e("lodash/collection/filter"), c = e("lodash/collection/groupBy"), l = 500, u = 1250, p = 1500, d = e("../../util/Event").getOriginal, h = Math.round;
        r.$inject = ["eventBus", "dragging", "modeling", "selection", "rules"], t.exports = r
    }, {
        "../../util/Event": 240,
        "lodash/collection/filter": 272,
        "lodash/collection/groupBy": 275,
        "lodash/object/assign": 396
    }],
    187: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i) {
            function c(e) {
                return t.getGraphics(e)
            }

            function l(e) {
                var t = h.selfAndDirectChildren(e, !0), n = r(t);
                return n
            }

            function u(e) {
                var t = h.selfAndAllChildren(e, !0), n = d(t, function (e) {
                    return (e.incoming || []).concat(e.outgoing || [])
                });
                return a(t.concat(n), !0)
            }

            function p(e, t) {
                [b, g, v, y].forEach(function (i) {
                    i === t ? n.addMarker(e, i) : n.removeMarker(e, i)
                })
            }

            function x(e, t) {
                var r = e.dragGroup;
                r || (r = e.dragGroup = n.getDefaultLayer().group().attr(i.cls("djs-drag-group", ["no-events"])));
                var o = c(t), a = o.clone(), s = o.getBBox();
                return a.attr(i.cls("djs-dragger", [], {x: s.x, y: s.y})), r.add(a), a
            }

            function E(e, t, i) {
                x(e, t), i && n.addMarker(t, m), e.allDraggedElements ? e.allDraggedElements.push(t) : e.allDraggedElements = [t]
            }

            this.addDragger = x, this.makeDraggable = E, e.on("shape.move.start", f, function (e) {
                var t = e.context, i = t.shapes, r = t.allDraggedElements, c = l(i);
                c.forEach(function (e) {
                    x(t, e)
                }), r = r ? a(r, u(i)) : u(i), s(r, function (e) {
                    n.addMarker(e, m)
                }), t.allDraggedElements = r, t.differentParents = o(i)
            }), e.on("shape.move.move", f, function (e) {
                var t = e.context, n = t.dragGroup, i = t.target, r = t.shape.parent, o = t.canExecute;
                i && ("attach" === o ? p(i, b) : t.canExecute && i && i.id !== r.id ? p(i, y) : p(i, t.canExecute ? g : v)), n.translate(e.dx, e.dy)
            }), e.on(["shape.move.out", "shape.move.cleanup"], function (e) {
                var t = e.context, n = t.target;
                n && p(n, null)
            }), e.on("shape.move.cleanup", function (e) {
                var t = e.context, i = t.allDraggedElements, r = t.dragGroup;
                s(i, function (e) {
                    n.removeMarker(e, m)
                }), r && r.remove()
            })
        }

        function r(e) {
            var t = c(e, function (t) {
                if (t.waypoints) {
                    var n = l(e, t.source), i = l(e, t.target);
                    return n && i
                }
                return !0
            });
            return t
        }

        function o(e) {
            return 1 !== u(p(e, function (e) {
                    return e.parent && e.parent.id
                }))
        }

        var a = e("lodash/array/flatten"), s = e("lodash/collection/forEach"), c = e("lodash/collection/filter"), l = e("lodash/collection/find"), u = e("lodash/collection/size"), p = e("lodash/collection/groupBy"), d = e("lodash/collection/map"), h = e("../../util/Elements"), f = 500, m = "djs-dragging", g = "drop-ok", v = "drop-not-ok", y = "new-parent", b = "attach-ok";
        i.$inject = ["eventBus", "elementRegistry", "canvas", "styles"], t.exports = i
    }, {
        "../../util/Elements": 239,
        "lodash/array/flatten": 264,
        "lodash/collection/filter": 272,
        "lodash/collection/find": 273,
        "lodash/collection/forEach": 274,
        "lodash/collection/groupBy": 275,
        "lodash/collection/map": 277,
        "lodash/collection/size": 279
    }],
    188: [function (e, t, n) {
        t.exports = {
            __depends__: [e("../interaction-events"), e("../selection"), e("../outline"), e("../rules"), e("../dragging")],
            __init__: ["move", "moveVisuals"],
            move: ["type", e("./Move")],
            moveVisuals: ["type", e("./MoveVisuals")]
        }
    }, {
        "../dragging": 153,
        "../interaction-events": 157,
        "../outline": 191,
        "../rules": 207,
        "../selection": 211,
        "./Move": 186,
        "./MoveVisuals": 187
    }],
    189: [function (e, t, n) {
        "use strict";
        function i(e) {
            o.call(this, e);
            var t = this;
            this.preExecute(["shape.create", "connection.create"], function (e) {
                var n = e.context, i = n.shape || n.connection, r = n.parent, o = t.getOrdering(i, r);
                o && (void 0 !== o.parent && (n.parent = o.parent), n.parentIndex = o.index)
            }), this.preExecute(["shape.move", "connection.move"], function (e) {
                var n = e.context, i = n.shape || n.connection, r = n.newParent || i.parent, o = t.getOrdering(i, r);
                o && (void 0 !== o.parent && (n.newParent = o.parent), n.newParentIndex = o.index)
            })
        }

        var r = e("inherits"), o = e("../../command/CommandInterceptor");
        i.prototype.getOrdering = function (e, t) {
            return null
        }, r(i, o), t.exports = i
    }, {"../../command/CommandInterceptor": 123, inherits: 253}],
    190: [function (e, t, n) {
        "use strict";
        function i(e, t, n) {
            function i(e, t) {
                return e.rect(10, 10, 0, 0).attr(c)
            }

            function o(e, t) {
                e.attr({x: -s, y: -s, width: t.width + 2 * s, height: t.height + 2 * s})
            }

            function a(e, t) {
                var n = r(t);
                e.attr({x: n.x - s, y: n.y - s, width: n.width + 2 * s, height: n.height + 2 * s})
            }

            var s = 6, c = t.cls("djs-outline", ["no-fill"]);
            e.on(["shape.added", "shape.changed"], function (e) {
                var t = e.element, n = e.gfx, r = n.select(".djs-outline");
                r || (r = i(n, t)), o(r, t)
            }), e.on(["connection.added", "connection.changed"], function (e) {
                var t = e.element, n = e.gfx, r = n.select(".djs-outline");
                r || (r = i(n, t)), a(r, t)
            })
        }

        var r = e("../../util/Elements").getBBox;
        i.$inject = ["eventBus", "styles", "elementRegistry"], t.exports = i
    }, {"../../util/Elements": 239}],
    191: [function (e, t, n) {
        "use strict";
        t.exports = {__init__: ["outline"], outline: ["type", e("./Outline")]}
    }, {"./Outline": 190}],
    192: [function (e, t, n) {
        "use strict";
        function i(e) {
            var t = h('<div class="djs-overlay-container" style="position: absolute; width: 0; height: 0;" />');
            return e.insertBefore(t, e.firstChild), t
        }

        function r(e, t, n) {
            u(e.style, {left: t + "px", top: n + "px"})
        }

        function o(e, t) {
            e.style.display = t === !1 ? "none" : ""
        }

        function a(e, t, n) {
            this._eventBus = e, this._canvas = t, this._elementRegistry = n, this._ids = v, this._overlayDefaults = {
                show: {
                    minZoom: .7,
                    maxZoom: 5
                }
            }, this._overlays = {}, this._overlayContainers = {}, this._overlayRoot = i(t.getContainer()), this._init()
        }

        var s = e("lodash/lang/isArray"), c = e("lodash/lang/isString"), l = e("lodash/lang/isObject"), u = e("lodash/object/assign"), p = e("lodash/collection/forEach"), d = e("lodash/collection/filter"), h = e("min-dom/lib/domify"), f = e("min-dom/lib/classes"), m = e("min-dom/lib/remove"), g = e("../../util/Elements").getBBox, v = new (e("../../util/IdGenerator"))("ov");
        a.$inject = ["eventBus", "canvas", "elementRegistry"], t.exports = a, a.prototype.get = function (e) {
            if (c(e) && (e = {id: e}), e.element) {
                var t = this._getOverlayContainer(e.element, !0);
                return t ? e.type ? d(t.overlays, {type: e.type}) : t.overlays.slice() : []
            }
            return e.type ? d(this._overlays, {type: e.type}) : e.id ? this._overlays[e.id] : null
        }, a.prototype.add = function (e, t, n) {
            if (l(t) && (n = t, t = null), e.id || (e = this._elementRegistry.get(e)), !n.position)throw new Error("must specifiy overlay position");
            if (!n.html)throw new Error("must specifiy overlay html");
            if (!e)throw new Error("invalid element specified");
            var i = this._ids.next();
            return n = u({}, this._overlayDefaults, n, {
                id: i,
                type: t,
                element: e,
                html: n.html
            }), this._addOverlay(n), i
        }, a.prototype.remove = function (e) {
            var t = this.get(e) || [];
            s(t) || (t = [t]);
            var n = this;
            p(t, function (e) {
                var t = n._getOverlayContainer(e.element, !0);
                if (e && (m(e.html), m(e.htmlContainer), delete e.htmlContainer, delete e.element, delete n._overlays[e.id]), t) {
                    var i = t.overlays.indexOf(e);
                    -1 !== i && t.overlays.splice(i, 1)
                }
            })
        }, a.prototype.show = function () {
            o(this._overlayRoot)
        }, a.prototype.hide = function () {
            o(this._overlayRoot, !1)
        }, a.prototype._updateOverlayContainer = function (e) {
            var t = e.element, n = e.html, i = t.x, o = t.y;
            if (t.waypoints) {
                var a = g(t);
                i = a.x, o = a.y
            }
            r(n, i, o)
        }, a.prototype._updateOverlay = function (e) {
            var t = e.position, n = e.htmlContainer, i = e.element, o = t.left, a = t.top;
            if (void 0 !== t.right) {
                var s;
                s = i.waypoints ? g(i).width : i.width, o = -1 * t.right + s
            }
            if (void 0 !== t.bottom) {
                var c;
                c = i.waypoints ? g(i).height : i.height, a = -1 * t.bottom + c
            }
            r(n, o || 0, a || 0)
        }, a.prototype._createOverlayContainer = function (e) {
            var t = h('<div class="djs-overlays djs-overlays-' + e.id + '" style="position: absolute" />');
            this._overlayRoot.appendChild(t);
            var n = {html: t, element: e, overlays: []};
            return this._updateOverlayContainer(n), n
        }, a.prototype._updateRoot = function (e) {
            var t = e.scale || 1, n = e.scale || 1, i = "matrix(" + t + ",0,0," + n + "," + -1 * e.x * t + "," + -1 * e.y * n + ")";
            this._overlayRoot.style.transform = i, this._overlayRoot.style["-ms-transform"] = i, this._overlayRoot.style["-webkit-transform"] = i
        }, a.prototype._getOverlayContainer = function (e, t) {
            var n = e && e.id || e, i = this._overlayContainers[n];
            return i || t || (i = this._overlayContainers[n] = this._createOverlayContainer(e)), i
        }, a.prototype._addOverlay = function (e) {
            var t, n, i = e.id, r = e.element, o = e.html;
            o.get && (o = o.get(0)), c(o) && (o = h(o)), n = this._getOverlayContainer(r), t = h('<div class="djs-overlay" data-overlay-id="' + i + '" style="position: absolute">'), t.appendChild(o), e.type && f(t).add("djs-overlay-" + e.type), e.htmlContainer = t, n.overlays.push(e), n.html.appendChild(t), this._overlays[i] = e, this._updateOverlay(e)
        }, a.prototype._updateOverlayVisibilty = function (e) {
            p(this._overlays, function (t) {
                var n = t.show, i = t.htmlContainer, r = !0;
                n && ((n.minZoom > e.scale || n.maxZoom < e.scale) && (r = !1), o(i, r))
            })
        }, a.prototype._init = function () {
            function e(e) {
                n._updateRoot(e), n._updateOverlayVisibilty(e), n.show()
            }

            var t = this._eventBus, n = this;
            t.on("canvas.viewbox.changing", function (e) {
                n.hide()
            }), t.on("canvas.viewbox.changed", function (t) {
                e(t.viewbox)
            }), t.on(["shape.remove", "connection.remove"], function (e) {
                var t = n.get({element: e.element});
                p(t, function (e) {
                    n.remove(e.id)
                })
            }), t.on(["element.changed"], function (e) {
                var t = e.element, i = n._getOverlayContainer(t, !0);
                i && (p(i.overlays, function (e) {
                    n._updateOverlay(e)
                }), n._updateOverlayContainer(i))
            }), t.on("element.marker.update", function (e) {
                var t = n._getOverlayContainer(e.element, !0);
                t && f(t.html)[e.add ? "add" : "remove"](e.marker)
            })
        }
    }, {
        "../../util/Elements": 239,
        "../../util/IdGenerator": 243,
        "lodash/collection/filter": 272,
        "lodash/collection/forEach": 274,
        "lodash/lang/isArray": 387,
        "lodash/lang/isObject": 391,
        "lodash/lang/isString": 393,
        "lodash/object/assign": 396,
        "min-dom/lib/classes": 409,
        "min-dom/lib/domify": 413,
        "min-dom/lib/remove": 417
    }],
    193: [function (e, t, n) {
        t.exports = {__init__: ["overlays"], overlays: ["type", e("./Overlays")]}
    }, {"./Overlays": 192}],
    194: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            this._eventBus = e, this._canvas = t, this._providers = []
        }

        var r = e("lodash/lang/isFunction"), o = e("lodash/collection/forEach"), a = e("min-dom/lib/domify"), s = e("min-dom/lib/query"), c = e("min-dom/lib/attr"), l = e("min-dom/lib/clear"), u = e("min-dom/lib/classes"), p = e("min-dom/lib/matches"), d = e("min-dom/lib/delegate"), h = e("min-dom/lib/event"), f = ".djs-palette-toggle", m = ".entry", g = f + ", " + m;
        i.$inject = ["eventBus", "canvas"], t.exports = i, i.prototype.registerProvider = function (e) {
            this._providers.push(e), this._container || this._init(), this._update()
        }, i.prototype.getEntries = function () {
            var e = {};
            return o(this._providers, function (t) {
                var n = t.getPaletteEntries();
                o(n, function (t, n) {
                    e[n] = t
                })
            }), e
        }, i.prototype._init = function () {
            var e = this._canvas.getContainer(), t = this._container = a(i.HTML_MARKUP), n = this;
            e.appendChild(t), d.bind(t, g, "click", function (e) {
                var t = e.delegateTarget;
                return p(t, f) ? n.toggle() : void n.trigger("click", e)
            }), h.bind(t, "mousedown", function (e) {
                e.stopPropagation()
            }), d.bind(t, m, "dragstart", function (e) {
                n.trigger("dragstart", e)
            }), this._eventBus.fire("palette.create", {html: t})
        }, i.prototype._update = function () {
            var e = s(".djs-palette-entries", this._container), t = this._entries = this.getEntries();
            l(e), o(t, function (t, n) {
                var i = t.group || "default", r = s("[data-group=" + i + "]", e);
                r || (r = a('<div class="group" data-group="' + i + '"></div>'), e.appendChild(r));
                var o = t.html || (t.separator ? '<hr class="separator" />' : '<div class="entry" draggable="true"></div>'), l = a(o);
                r.appendChild(l), t.separator || (c(l, "data-action", n), t.title && c(l, "title", t.title), t.className && u(l).add(t.className), t.imageUrl && l.appendChild(a('<img src="' + t.imageUrl + '">')))
            }), this.open(!0)
        }, i.prototype.trigger = function (e, t, n) {
            var i, o, a, s = this._entries, l = t.delegateTarget || t.target;
            if (!l)return t.preventDefault();
            if (i = s[c(l, "data-action")], o = i.action, a = t.originalEvent || t, r(o)) {
                if ("click" === e)return o(a, n)
            } else if (o[e])return o[e](a, n);
            t.preventDefault()
        }, i.prototype.close = function () {
            u(this._container).remove("open")
        }, i.prototype.open = function () {
            u(this._container).add("open")
        }, i.prototype.toggle = function (e) {
            this.isOpen() ? this.close() : this.open()
        }, i.prototype.isOpen = function () {
            return this._container && u(this._container).has("open")
        }, i.HTML_MARKUP = '<div class="djs-palette"><div class="djs-palette-entries"></div><div class="djs-palette-toggle"></div></div>'
    }, {
        "lodash/collection/forEach": 274,
        "lodash/lang/isFunction": 388,
        "min-dom/lib/attr": 408,
        "min-dom/lib/classes": 409,
        "min-dom/lib/clear": 410,
        "min-dom/lib/delegate": 412,
        "min-dom/lib/domify": 413,
        "min-dom/lib/event": 414,
        "min-dom/lib/matches": 415,
        "min-dom/lib/query": 416
    }],
    195: [function (e, t, n) {
        t.exports = {__init__: ["palette"], palette: ["type", e("./Palette")]}
    }, {"./Palette": 194}],
    196: [function (e, t, n) {
        "use strict";
        function i(e, t, n) {
            this._eventBus = e, this._canvas = t, this._modeling = n
        }

        var r = e("lodash/collection/forEach"), o = e("lodash/object/assign"), a = e("lodash/collection/find"), s = e("min-dom/lib/delegate"), c = e("min-dom/lib/domify"), l = e("min-dom/lib/classes"), u = e("min-dom/lib/attr"), p = e("min-dom/lib/remove"), d = "data-id";
        i.$inject = ["eventBus", "canvas", "modeling"], i.prototype.open = function (e) {
            var t = e.className || "popup-menu", n = e.position, i = e.entries, r = e.headerEntries;
            if (!n)throw new Error("the position argument is missing");
            if (!i)throw new Error("the entries argument is missing");
            this.isOpen() && this.close();
            var o = this._canvas, a = o.getContainer(), s = this._createContainer(t, n);
            if (r) {
                var c = this._createEntries(r, "djs-popup-header");
                s.appendChild(c)
            }
            var l = this._createEntries(i, "djs-popup-body");
            return s.appendChild(l), this._attachContainer(s, a), this._current = {container: s, menu: e}, this
        }, i.prototype.close = function () {
            this.isOpen() && (this._unbindHandlers(), p(this._current.container), this._current = null)
        }, i.prototype.isOpen = function () {
            return !!this._current
        }, i.prototype.trigger = function (e) {
            e.preventDefault();
            var t = e.delegateTarget || e.target, n = u(t, d), i = this._getEntry(n);
            if (i.action) {
                var r = i.action.call(null, e, i);
                return this.close(), r
            }
        }, i.prototype._getEntry = function (e) {
            var t = this._current.menu, n = {id: e}, i = a(t.entries, n) || a(t.headerEntries, n);
            if (!i)throw new Error("entry not found");
            return i
        }, i.prototype._createContainer = function (e, t) {
            var n = c('<div class="djs-popup">');
            return o(n.style, {position: "absolute", left: t.x + "px", top: t.y + "px"}), l(n).add(e), n
        }, i.prototype._attachContainer = function (e, t) {
            var n = this;
            s.bind(e, ".entry", "click", function (e) {
                n.trigger(e)
            });
            var i = this._canvas.zoom();
            e.style.transformOrigin = "top left", e.style.transform = "scale(" + i + ")", t.appendChild(e), this._bindHandlers()
        }, i.prototype._createEntries = function (e, t) {
            var n = c("<div>"), i = this;
            return l(n).add(t), r(e, function (e) {
                var t = i._createEntry(e, n);
                n.appendChild(t)
            }), n
        }, i.prototype._createEntry = function (e) {
            if (!e.id)throw new Error("every entry must have the id property set");
            var t = c("<div>"), n = l(t);
            if (n.add("entry"), e.className && n.add(e.className), u(t, d, e.id), e.label) {
                var i = c("<span>");
                i.textContent = e.label, t.appendChild(i)
            }
            return e.imageUrl && t.appendChild(c('<img src="' + e.imageUrl + '" />')), e.active === !0 && n.add("active"), e.disabled === !0 && n.add("disabled"), e.title && (t.title = e.title), t
        }, i.prototype._bindHandlers = function () {
            function e() {
                n.close()
            }

            var t = this._eventBus, n = this;
            t.once("contextPad.close", e), t.once("canvas.viewbox.changing", e), t.once("commandStack.changed", e)
        }, i.prototype._unbindHandlers = function () {
            function e() {
                n.close()
            }

            var t = this._eventBus, n = this;
            t.off("contextPad.close", e), t.off("canvas.viewbox.changed", e), t.off("commandStack.changed", e)
        }, t.exports = i
    }, {
        "lodash/collection/find": 273,
        "lodash/collection/forEach": 274,
        "lodash/object/assign": 396,
        "min-dom/lib/attr": 408,
        "min-dom/lib/classes": 409,
        "min-dom/lib/delegate": 412,
        "min-dom/lib/domify": 413,
        "min-dom/lib/remove": 417
    }],
    197: [function (e, t, n) {
        "use strict";
        t.exports = {__init__: ["popupMenu"], popupMenu: ["type", e("./PopupMenu")]}
    }, {"./PopupMenu": 196}],
    198: [function (e, t, n) {
        "use strict";
        function i(e) {
            this._modeling = e
        }

        t.exports = i, i.$inject = ["modeling"], i.prototype.replaceElement = function (e, t, n) {
            var i = this._modeling, r = null;
            return e.waypoints || (t.x = e.x + (t.width || e.width) / 2, t.y = e.y + (t.height || e.height) / 2, r = i.replaceShape(e, t, n)), r
        }
    }, {}],
    199: [function (e, t, n) {
        "use strict";
        t.exports = {__init__: ["replace"], replace: ["type", e("./Replace")]}
    }, {"./Replace": 198}],
    200: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i) {
            this._dragging = i, this._rules = t;
            var r = this;
            e.on("resize.start", function (e) {
                var t = e.context, n = t.resizeConstraints, i = t.minBounds;
                void 0 === n && (void 0 === i && (i = r.computeMinResizeBox(t)), t.resizeConstraints = {min: s(i)})
            }), e.on("resize.move", function (e) {
                var t, n, i = e.context, o = i.shape, s = i.direction, c = i.resizeConstraints;
                t = {
                    x: e.dx,
                    y: e.dy
                }, i.delta = t, n = a.resizeBounds(o, s, t), i.newBounds = a.ensureConstraints(n, c), i.canExecute = r.canResize(i)
            }), e.on("resize.end", function (e) {
                var t = e.context, i = t.shape, r = t.canExecute, o = t.newBounds;
                r && (o = c(o), n.resizeShape(i, o))
            })
        }

        var r = e("lodash/object/pick"), o = e("lodash/object/assign"), a = e("./ResizeUtil"), s = e("../../layout/LayoutUtil").asTRBL, c = e("../../layout/LayoutUtil").roundBounds, l = 10;
        i.prototype.canResize = function (e) {
            var t = this._rules, n = r(e, ["newBounds", "shape", "delta", "direction"]);
            return t.allowed("shape.resize", n)
        }, i.prototype.activate = function (e, t, n) {
            var i, r, a = this._dragging;
            if ("string" == typeof n && (n = {direction: n}), i = o({shape: t}, n), r = i.direction, !r)throw new Error("must provide a direction (nw|se|ne|sw)");
            a.activate(e, "resize", {
                autoActivate: !0,
                cursor: "resize-" + (/nw|se/.test(r) ? "nwse" : "nesw"),
                data: {shape: t, context: i}
            })
        }, i.prototype.computeMinResizeBox = function (e) {
            var t, n, i = e.shape, r = e.direction;
            return t = e.minDimensions || {
                width: l,
                height: l
            }, n = a.computeChildrenBBox(i, e.childrenBoxPadding), a.getMinResizeBounds(r, i, t, n)
        }, i.$inject = ["eventBus", "rules", "modeling", "dragging"], t.exports = i
    }, {"../../layout/LayoutUtil": 226, "./ResizeUtil": 202, "lodash/object/assign": 396, "lodash/object/pick": 402}],
    201: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i) {
            this._resize = i, this._canvas = t;
            var o = this;
            e.on("selection.changed", function (e) {
                var t = e.oldSelection, n = e.newSelection;
                r(t, o.removeResizer, o), 1 === n.length && r(n, o.addResizer, o)
            }), e.on("shape.changed", function (e) {
                var t = e.element;
                o.removeResizer(t), n.isSelected(t) && o.addResizer(t)
            })
        }

        var r = e("lodash/collection/forEach"), o = e("../../../vendor/snapsvg"), a = -2, s = 5, c = 20, l = "djs-resizer", u = e("min-dom/lib/event"), p = e("../../util/Mouse").isPrimaryButton, d = e("../../layout/LayoutUtil").asTRBL;
        i.prototype.makeDraggable = function (e, t, n) {
            function i(t) {
                p(t) && r.activate(t, e, n)
            }

            var r = this._resize;
            u.bind(t.node, "mousedown", i), u.bind(t.node, "touchstart", i)
        }, i.prototype._createResizer = function (e, t, n, i, r) {
            var u = this._getResizersParent(), p = u.group().addClass(l).addClass(l + "-" + e.id).addClass(l + "-" + r), d = -s + a;
            p.rect(d, d, s, s).addClass(l + "-visual"), p.rect(d, d, c, c).addClass(l + "-hit");
            var h = (new o.Matrix).translate(t, n).rotate(i, 0, 0);
            return p.transform(h), p
        }, i.prototype.createResizer = function (e, t) {
            var n, i = d(e);
            n = "nw" === t ? this._createResizer(e, i.left, i.top, 0, t) : "ne" === t ? this._createResizer(e, i.right, i.top, 90, t) : "se" === t ? this._createResizer(e, i.right, i.bottom, 180, t) : this._createResizer(e, i.left, i.bottom, 270, t), this.makeDraggable(e, n, t)
        }, i.prototype.addResizer = function (e) {
            var t = this._resize;
            t.canResize({shape: e}) && (this.createResizer(e, "nw"), this.createResizer(e, "ne"), this.createResizer(e, "se"), this.createResizer(e, "sw"))
        }, i.prototype.removeResizer = function (e) {
            var t = this._getResizersParent(), n = t.selectAll("." + l + "-" + e.id);
            r(n, function (e) {
                e.remove()
            })
        }, i.prototype._getResizersParent = function () {
            return this._canvas.getLayer("resizers")
        }, i.$inject = ["eventBus", "canvas", "selection", "resize"], t.exports = i
    }, {
        "../../../vendor/snapsvg": 258,
        "../../layout/LayoutUtil": 226,
        "../../util/Mouse": 246,
        "lodash/collection/forEach": 274,
        "min-dom/lib/event": 414
    }],
    202: [function (e, t, n) {
        "use strict";
        function i(e) {
            return "number" == typeof e
        }

        function r(e, t, n) {
            var r = t[e], o = n.min && n.min[e], a = n.max && n.max[e];
            return i(o) && (r = (/top|left/.test(e) ? p : u)(r, o)), i(a) && (r = (/top|left/.test(e) ? u : p)(r, a)), r
        }

        function o(e, t) {
            return "undefined" != typeof e ? e : d
        }

        function a(e, t) {
            var n, i, r, a;
            return "object" == typeof t ? (n = o(t.left), i = o(t.right), r = o(t.top), a = o(t.bottom)) : n = i = r = a = o(t), {
                x: e.x - n,
                y: e.y - r,
                width: e.width + n + i,
                height: e.height + r + a
            }
        }

        function s(e) {
            return e.waypoints ? !1 : "label" === e.type ? !1 : !0
        }

        function c(e, t) {
            var n;
            return n = void 0 === e.length ? l(e.children, s) : e, n.length ? a(h(n), t) : void 0
        }

        var l = e("lodash/collection/filter"), u = Math.max, p = Math.min, d = 20, h = e("../../util/Elements").getBBox, f = e("../../layout/LayoutUtil").asTRBL, m = e("../../layout/LayoutUtil").asBounds;
        t.exports.substractTRBL = function (e, t) {
            return {top: e.top - t.top, right: e.right - t.right, bottom: e.bottom - t.bottom, left: e.left - t.left}
        }, t.exports.resizeBounds = function (e, t, n) {
            var i = n.x, r = n.y;
            switch (t) {
                case"nw":
                    return {x: e.x + i, y: e.y + r, width: e.width - i, height: e.height - r};
                case"sw":
                    return {x: e.x + i, y: e.y, width: e.width - i, height: e.height + r};
                case"ne":
                    return {x: e.x, y: e.y + r, width: e.width + i, height: e.height - r};
                case"se":
                    return {x: e.x, y: e.y, width: e.width + i, height: e.height + r};
                default:
                    throw new Error("unrecognized direction: " + t)
            }
        }, t.exports.resizeTRBL = function (e, t) {
            return {
                x: e.x + (t.left || 0),
                y: e.y + (t.top || 0),
                width: e.width - (t.left || 0) + (t.right || 0),
                height: e.height - (t.top || 0) + (t.bottom || 0)
            }
        }, t.exports.reattachPoint = function (e, t, n) {
            var i = e.width / t.width, r = e.height / t.height;
            return {
                x: Math.round(t.x + t.width / 2) - Math.floor((e.x + e.width / 2 - n.x) / i),
                y: Math.round(t.y + t.height / 2) - Math.floor((e.y + e.height / 2 - n.y) / r)
            }
        }, t.exports.ensureConstraints = function (e, t) {
            if (!t)return e;
            var n = f(e);
            return m({top: r("top", n, t), right: r("right", n, t), bottom: r("bottom", n, t), left: r("left", n, t)})
        }, t.exports.getMinResizeBounds = function (e, t, n, i) {
            var r = f(t), o = {
                top: /n/.test(e) ? r.bottom - n.height : r.top,
                left: /w/.test(e) ? r.right - n.width : r.left,
                bottom: /s/.test(e) ? r.top + n.height : r.bottom,
                right: /e/.test(e) ? r.left + n.width : r.right
            }, a = i ? f(i) : o, s = {
                top: p(o.top, a.top),
                left: p(o.left, a.left),
                bottom: u(o.bottom, a.bottom),
                right: u(o.right, a.right)
            };
            return m(s)
        }, t.exports.addPadding = a, t.exports.computeChildrenBBox = c
    }, {"../../layout/LayoutUtil": 226, "../../util/Elements": 239, "lodash/collection/filter": 272}],
    203: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            this._canvas = t;
            var n = this;
            e.on("resize.start", s, function (e) {
                var i = e.context, r = i.shape;
                t.addMarker(r, o), n.create(i)
            }), e.on("resize.move", s, function (e) {
                var t = e.context;
                n.update(t)
            }), e.on("resize.cleanup", function (e) {
                var i = e.context, r = i.shape;
                t.removeMarker(r, o), n.remove(i)
            })
        }

        var r = e("../../../vendor/snapsvg"), o = "djs-resizing", a = "resize-not-ok", s = 500;
        i.prototype.create = function (e) {
            var t, n = this._canvas.getDefaultLayer(), i = e.shape;
            t = e.frame = r.create("rect", {
                "class": "djs-resize-overlay",
                width: i.width + 10,
                height: i.height + 10,
                x: i.x - 5,
                y: i.y - 5
            }), t.appendTo(n)
        }, i.prototype.update = function (e) {
            var t = e.frame, n = e.newBounds;
            n.width > 5 && t.attr({x: n.x, width: n.width}), n.height > 5 && t.attr({
                y: n.y,
                height: n.height
            }), t[e.canExecute ? "removeClass" : "addClass"](a)
        }, i.prototype.remove = function (e) {
            e.frame && e.frame.remove()
        }, i.$inject = ["eventBus", "canvas"], t.exports = i
    }, {"../../../vendor/snapsvg": 258}],
    204: [function (e, t, n) {
        t.exports = {
            __depends__: [e("../modeling"), e("../rules"), e("../dragging")],
            __init__: ["resize", "resizeVisuals", "resizeHandles"],
            resize: ["type", e("./Resize")],
            resizeVisuals: ["type", e("./ResizeVisuals")],
            resizeHandles: ["type", e("./ResizeHandles")]
        }
    }, {
        "../dragging": 153,
        "../modeling": 185,
        "../rules": 207,
        "./Resize": 200,
        "./ResizeHandles": 201,
        "./ResizeVisuals": 203
    }],
    205: [function (e, t, n) {
        "use strict";
        function i(e) {
            o.call(this, e), this.init()
        }

        var r = e("inherits"), o = e("../../command/CommandInterceptor");
        i.$inject = ["eventBus"], r(i, o), t.exports = i, i.prototype.addRule = function (e, t, n) {
            var i = this;
            "string" == typeof e && (e = [e]), e.forEach(function (e) {
                i.canExecute(e, t, function (e, t, i) {
                    return n(e)
                }, !0)
            })
        }
    }, {"../../command/CommandInterceptor": 123, inherits: 253}],
    206: [function (e, t, n) {
        "use strict";
        function i(e) {
            this._commandStack = e
        }

        i.$inject = ["commandStack"], t.exports = i, i.prototype.allowed = function (e, t) {
            var n = this._commandStack.canExecute(e, t);
            return void 0 === n ? !0 : n
        }
    }, {}],
    207: [function (e, t, n) {
        t.exports = {__depends__: [e("../../command")], __init__: ["rules"], rules: ["type", e("./Rules")]}
    }, {"../../command": 125, "./Rules": 206}],
    208: [function (e, t, n) {
        "use strict";
        function i(e) {
            this._eventBus = e, this._selectedElements = [];
            var t = this;
            e.on(["shape.remove", "connection.remove"], function (e) {
                var n = e.element;
                t.deselect(n)
            })
        }

        var r = e("lodash/lang/isArray"), o = e("lodash/collection/forEach");
        i.$inject = ["eventBus"], t.exports = i, i.prototype.deselect = function (e) {
            var t = this._selectedElements, n = t.indexOf(e);
            if (-1 !== n) {
                var i = t.slice();
                t.splice(n, 1), this._eventBus.fire("selection.changed", {oldSelection: i, newSelection: t})
            }
        }, i.prototype.get = function () {
            return this._selectedElements
        }, i.prototype.isSelected = function (e) {
            return -1 !== this._selectedElements.indexOf(e)
        }, i.prototype.select = function (e, t) {
            var n = this._selectedElements, i = n.slice();
            r(e) || (e = e ? [e] : []), t ? o(e, function (e) {
                -1 === n.indexOf(e) && n.push(e)
            }) : this._selectedElements = n = e.slice(), this._eventBus.fire("selection.changed", {
                oldSelection: i,
                newSelection: n
            })
        }
    }, {"lodash/collection/forEach": 274, "lodash/lang/isArray": 387}],
    209: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i) {
            e.on("create.end", 500, function (e) {
                e.context.canExecute && t.select(e.context.shape)
            }), e.on("connect.end", 500, function (e) {
                e.context.canExecute && e.context.target && t.select(e.context.target)
            }), e.on("shape.move.end", 500, function (e) {
                var n = e.previousSelection || [], r = i.get(e.context.shape.id);
                r && -1 === n.indexOf(r) && t.select(r)
            }), e.on("element.click", function (e) {
                var i = e.element;
                i === n.getRootElement() && (i = null);
                var o = t.isSelected(i), a = t.get().length > 1, s = r(e);
                return o && a ? s ? t.deselect(i) : t.select(i) : void(o ? t.deselect(i) : t.select(i, s))
            })
        }

        var r = e("../../util/Mouse").hasPrimaryModifier;
        i.$inject = ["eventBus", "selection", "canvas", "elementRegistry"], t.exports = i
    }, {"../../util/Mouse": 246}],
    210: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i, s) {
            function c(e, n) {
                t.addMarker(e, n)
            }

            function l(e, n) {
                t.removeMarker(e, n)
            }

            this._multiSelectionBox = null, e.on("element.hover", function (e) {
                c(e.element, o)
            }), e.on("element.out", function (e) {
                l(e.element, o)
            }), e.on("selection.changed", function (e) {
                function t(e) {
                    l(e, a)
                }

                function n(e) {
                    c(e, a)
                }

                var i = e.oldSelection, o = e.newSelection;
                r(i, function (e) {
                    -1 === o.indexOf(e) && t(e)
                }), r(o, function (e) {
                    -1 === i.indexOf(e) && n(e)
                })
            })
        }

        var r = e("lodash/collection/forEach"), o = "hover", a = "selected";
        i.$inject = ["eventBus", "canvas", "selection", "graphicsFactory", "styles"], t.exports = i
    }, {"lodash/collection/forEach": 274}],
    211: [function (e, t, n) {
        t.exports = {
            __init__: ["selectionVisuals", "selectionBehavior"],
            __depends__: [e("../interaction-events"), e("../outline")],
            selection: ["type", e("./Selection")],
            selectionVisuals: ["type", e("./SelectionVisuals")],
            selectionBehavior: ["type", e("./SelectionBehavior")]
        }
    }, {
        "../interaction-events": 157,
        "../outline": 191,
        "./Selection": 208,
        "./SelectionBehavior": 209,
        "./SelectionVisuals": 210
    }],
    212: [function (e, t, n) {
        "use strict";
        function i() {
            this._targets = {}, this._snapOrigins = {}, this._snapLocations = [], this._defaultSnaps = {}
        }

        function r(e) {
            this._snapValues = {}
        }

        var o = e("lodash/collection/forEach"), a = e("./SnapUtil").snapTo;
        i.prototype.getSnapOrigin = function (e) {
            return this._snapOrigins[e]
        }, i.prototype.setSnapOrigin = function (e, t) {
            this._snapOrigins[e] = t, -1 === this._snapLocations.indexOf(e) && this._snapLocations.push(e)
        }, i.prototype.addDefaultSnap = function (e, t) {
            var n = this._defaultSnaps[e];
            n || (n = this._defaultSnaps[e] = []), n.push(t)
        }, i.prototype.getSnapLocations = function () {
            return this._snapLocations
        }, i.prototype.setSnapLocations = function (e) {
            this._snapLocations = e
        }, i.prototype.pointsForTarget = function (e) {
            var t = e.id || e, n = this._targets[t];
            return n || (n = this._targets[t] = new r, n.initDefaults(this._defaultSnaps)), n
        }, t.exports = i, r.prototype.add = function (e, t) {
            var n = this._snapValues[e];
            n || (n = this._snapValues[e] = {
                x: [],
                y: []
            }), -1 === n.x.indexOf(t.x) && n.x.push(t.x), -1 === n.y.indexOf(t.y) && n.y.push(t.y)
        }, r.prototype.snap = function (e, t, n, i) {
            var r = this._snapValues[t];
            return r && a(e[n], r[n], i)
        }, r.prototype.initDefaults = function (e) {
            var t = this;
            o(e || {}, function (e, n) {
                o(e, function (e) {
                    t.add(n, e)
                })
            })
        }
    }, {"./SnapUtil": 213, "lodash/collection/forEach": 274}],
    213: [function (e, t, n) {
        "use strict";
        function i(e, t, n) {
            n = void 0 === n ? 10 : n;
            var i, r;
            for (i = 0; i < t.length; i++)if (r = t[i], s(r - e) <= n)return r
        }

        function r(e) {
            return {x: e.x, y: e.y}
        }

        function o(e, t) {
            return !e || isNaN(e.x) || isNaN(e.y) ? t : {x: c(e.x + e.width / 2), y: c(e.y + e.height / 2)}
        }

        function a(e) {
            return {x: e.x + e.width, y: e.y + e.height}
        }

        var s = Math.abs, c = Math.round;
        t.exports.snapTo = i, t.exports.topLeft = r, t.exports.mid = o, t.exports.bottomRight = a, t.exports.isSnapped = function (e, t) {
            var n = e.snapped;
            return n ? "string" == typeof t ? n[t] : n.x && n.y : !1
        }, t.exports.setSnapped = function (e, t, n) {
            if ("string" != typeof t)throw new Error("axis must be in [x, y]");
            if ("number" != typeof n && n !== !1)throw new Error("value must be Number or false");
            var i, r = e[t], o = e.snapped = e.snapped || {};
            return n === !1 ? o[t] = !1 : (o[t] = !0, i = n - r, e[t] += i, e["d" + t] += i), r
        }
    }, {}],
    214: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            this._canvas = t;
            var n = this;
            e.on(["shape.move.start", "create.start"], function (e) {
                n.initSnap(e)
            }), e.on(["shape.move.move", "shape.move.end", "create.move", "create.end"], function (e) {
                e.originalEvent && e.originalEvent.ctrlKey || u(e) || n.snap(e)
            }), e.on(["shape.move.cleanup", "create.cleanup"], function (e) {
                n.hide()
            }), this._asyncHide = a(this.hide, 1e3)
        }

        var r = e("lodash/collection/filter"), o = e("lodash/collection/forEach"), a = e("lodash/function/debounce"), s = e("./SnapUtil").mid, c = e("./SnapContext"), l = e("./SnapUtil"), u = l.isSnapped, p = l.setSnapped;
        i.$inject = ["eventBus", "canvas"], t.exports = i, i.prototype.initSnap = function (e) {
            var t = e.context, n = t.shape, i = t.snapContext;
            i || (i = t.snapContext = new c);
            var r = s(n, e);
            return i.setSnapOrigin("mid", {x: r.x - e.x, y: r.y - e.y}), i
        }, i.prototype.snap = function (e) {
            var t = e.context, n = t.snapContext, i = t.shape, r = t.target, a = n.getSnapLocations();
            if (r) {
                var s = n.pointsForTarget(r);
                s.initialized || (this.addTargetSnaps(s, i, r), s.initialized = !0);
                var c = {x: u(e, "x"), y: u(e, "y")};
                o(a, function (t) {
                    var i = n.getSnapOrigin(t), r = {x: e.x + i.x, y: e.y + i.y};
                    return o(["x", "y"], function (e) {
                        var n;
                        c[e] || (n = s.snap(r, t, e, 7), void 0 !== n && (c[e] = {value: n, originValue: n - i[e]}))
                    }), c.x && c.y ? !1 : void 0
                }), this.showSnapLine("vertical", c.x && c.x.value), this.showSnapLine("horizontal", c.y && c.y.value), o(["x", "y"], function (t) {
                    var n = c[t];
                    "object" == typeof n && p(e, t, n.originValue)
                })
            }
        }, i.prototype._createLine = function (e) {
            var t = this._canvas.getLayer("snap"), n = t.path("M0,0 L0,0").addClass("djs-snap-line");
            return {
                update: function (t) {
                    "number" != typeof t ? n.attr({display: "none"}) : "horizontal" === e ? n.attr({
                        path: "M-100000," + t + " L+100000," + t,
                        display: ""
                    }) : n.attr({path: "M " + t + ",-100000 L " + t + ", +100000", display: ""})
                }
            }
        }, i.prototype._createSnapLines = function () {
            this._snapLines = {horizontal: this._createLine("horizontal"), vertical: this._createLine("vertical")}
        }, i.prototype.showSnapLine = function (e, t) {
            var n = this.getSnapLine(e);
            n && n.update(t), this._asyncHide()
        }, i.prototype.getSnapLine = function (e) {
            return this._snapLines || this._createSnapLines(), this._snapLines[e]
        }, i.prototype.hide = function () {
            o(this._snapLines, function (e) {
                e.update()
            })
        }, i.prototype.addTargetSnaps = function (e, t, n) {
            var i = this.getSiblings(t, n);
            o(i, function (t) {
                e.add("mid", s(t))
            })
        }, i.prototype.getSiblings = function (e, t) {
            return t && r(t.children, function (t) {
                    return !t.hidden && !t.labelTarget && !t.waypoints && t.host !== e && t !== e
                })
        }
    }, {
        "./SnapContext": 212,
        "./SnapUtil": 213,
        "lodash/collection/filter": 272,
        "lodash/collection/forEach": 274,
        "lodash/function/debounce": 284
    }],
    215: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i, r) {
            this._canvas = n, this._dragging = t, this._modeling = i, this._rules = r;
            var o = this;
            e.on("spaceTool.selection.end", function (e) {
                setTimeout(function () {
                    o.activateMakeSpace(e.originalEvent)
                })
            }), e.on("spaceTool.move", l, function (e) {
                var t = e.context;
                t.initialized || (t.initialized = o.initializeMakeSpace(e, t))
            }), e.on("spaceTool.end", function (e) {
                var t = e.context, n = t.axis, i = t.direction, r = t.movingShapes, a = t.resizingShapes;
                if (t.initialized) {
                    var s = {x: c(e.dx), y: c(e.dy)};
                    s[p[n]] = 0, o.makeSpace(r, a, s, i)
                }
            })
        }

        var r = e("./SpaceUtil"), o = e("../../util/Cursor"), a = e("../../util/Mouse").hasPrimaryModifier, s = Math.abs, c = Math.round, l = 1500, u = {
            x: "width",
            y: "height"
        }, p = {x: "y", y: "x"}, d = e("../../util/Elements").selfAndAllChildren, h = e("lodash/object/assign");
        i.$inject = ["eventBus", "dragging", "canvas", "modeling", "rules"], t.exports = i, i.prototype.activateSelection = function (e, t) {
            this._dragging.activate(e, "spaceTool.selection", {
                cursor: "crosshair",
                autoActivate: t,
                data: {context: {}}
            })
        }, i.prototype.activateMakeSpace = function (e) {
            this._dragging.activate(e, "spaceTool", {autoActivate: !0, cursor: "crosshair", data: {context: {}}})
        }, i.prototype.makeSpace = function (e, t, n, i) {
            return this._modeling.createSpace(e, t, n, i)
        }, i.prototype.initializeMakeSpace = function (e, t) {
            var n = s(e.dx) > s(e.dy) ? "x" : "y", i = e["d" + n], c = e[n] - i;
            if (s(i) < 5)return !1;
            a(e) && (i *= -1);
            var l = this._canvas.getRootElement(), u = d(l, !0), p = this.calculateAdjustments(u, n, i, c);
            return h(t, p, {axis: n, direction: r.getDirection(n, i)}), o.set("resize-" + ("x" === n ? "ew" : "ns")), !0
        }, i.prototype.calculateAdjustments = function (e, t, n, i) {
            var r = [], o = [], a = this._rules;
            return e.forEach(function (e) {
                var s = e[t], c = s + e[u[t]];
                if (e.parent && !e.waypoints)return n > 0 && s > i ? r.push(e) : 0 > n && i > c ? r.push(e) : i > s && c > i && a.allowed("shape.resize", {shape: e}) ? o.push(e) : void 0
            }), {movingShapes: r, resizingShapes: o}
        }, t.exports = i
    }, {
        "../../util/Cursor": 238,
        "../../util/Elements": 239,
        "../../util/Mouse": 246,
        "./SpaceUtil": 217,
        "lodash/object/assign": 396
    }],
    216: [function (e, t, n) {
        "use strict";
        function i(e, t, n, i) {
            function a(e) {
                return t.getGraphics(e)
            }

            function s(e, t) {
                var n = a(e), r = n.clone(), o = n.getBBox();
                r.attr(i.cls("djs-dragger", [], {x: o.x, y: o.y})), t.add(r)
            }

            e.on("spaceTool.selection.start", function (e) {
                var t = n.getLayer("space"), r = e.context, o = {
                    x: "M 0,-10000 L 0,10000",
                    y: "M -10000,0 L 10000,0"
                }, a = t.group().attr(i.cls("djs-crosshair-group", ["no-events"]));
                a.path(o.x).addClass("djs-crosshair"), a.path(o.y).addClass("djs-crosshair"), r.crosshairGroup = a
            }), e.on("spaceTool.selection.move", function (e) {
                var t = e.context.crosshairGroup;
                t.translate(e.x, e.y)
            }), e.on("spaceTool.selection.cleanup", function (e) {
                var t = e.context, n = t.crosshairGroup;
                n && n.remove()
            }), e.on("spaceTool.move", function (e) {
                var t = e.context, a = t.line, c = t.axis, l = t.movingShapes;
                if (t.initialized) {
                    if (!t.dragGroup) {
                        var u = n.getLayer("space");
                        a = u.path("M0,0 L0,0").addClass("djs-crosshair"), t.line = a;
                        var p = n.getDefaultLayer().group().attr(i.cls("djs-drag-group", ["no-events"]));
                        r(l, function (e) {
                            s(e, p), n.addMarker(e, o)
                        }), t.dragGroup = p
                    }
                    var d = {x: "M" + e.x + ", -10000 L" + e.x + ", 10000", y: "M -10000, " + e.y + " L 10000, " + e.y};
                    a.attr({path: d[c], display: ""});
                    var h = {x: "y", y: "x"}, f = {x: e.dx, y: e.dy};
                    f[h[t.axis]] = 0, t.dragGroup.translate(f.x, f.y)
                }
            }), e.on("spaceTool.cleanup", function (e) {
                var t = e.context, i = t.movingShapes, a = t.line, s = t.dragGroup;
                r(i, function (e) {
                    n.removeMarker(e, o)
                }), s && (a.remove(), s.remove())
            })
        }

        var r = e("lodash/collection/forEach"), o = "djs-dragging";
        i.$inject = ["eventBus", "elementRegistry", "canvas", "styles"], t.exports = i
    }, {"lodash/collection/forEach": 274}],
    217: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            if ("x" === e) {
                if (t > 0)return "e";
                if (0 > t)return "w"
            }
            if ("y" === e) {
                if (t > 0)return "s";
                if (0 > t)return "n"
            }
            return null
        }

        t.exports.getDirection = i, t.exports.resizeBounds = function (e, t, n) {
            var i = n.x, r = n.y;
            switch (t) {
                case"n":
                    return {x: e.x, y: e.y + r, width: e.width, height: e.height - r};
                case"s":
                    return {x: e.x, y: e.y, width: e.width, height: e.height + r};
                case"w":
                    return {x: e.x + i, y: e.y, width: e.width - i, height: e.height};
                case"e":
                    return {x: e.x, y: e.y, width: e.width + i, height: e.height};
                default:
                    throw new Error("unrecognized direction: " + t)
            }
        }
    }, {}],
    218: [function (e, t, n) {
        t.exports = {
            __init__: ["spaceToolVisuals"],
            __depends__: [e("../dragging"), e("../modeling"), e("../rules")],
            spaceTool: ["type", e("./SpaceTool")],
            spaceToolVisuals: ["type", e("./SpaceToolVisuals")]
        }
    }, {"../dragging": 153, "../modeling": 185, "../rules": 207, "./SpaceTool": 215, "./SpaceToolVisuals": 216}],
    219: [function (e, t, n) {
        "use strict";
        function i(e) {
            var t = u('<div class="djs-tooltip-container" style="position: absolute; width: 0; height: 0;" />');
            return e.insertBefore(t, e.firstChild), t
        }

        function r(e, t, n) {
            c(e.style, {left: t + "px", top: n + "px"})
        }

        function o(e, t) {
            e.style.display = t === !1 ? "none" : ""
        }

        function a(e, t) {
            this._eventBus = e, this._canvas = t, this._ids = m, this._tooltipDefaults = {
                show: {
                    minZoom: .7,
                    maxZoom: 5
                }
            }, this._tooltips = {}, this._tooltipRoot = i(t.getContainer());
            var n = this;
            f.bind(this._tooltipRoot, v, "mousedown", function (e) {
                e.stopPropagation()
            }), f.bind(this._tooltipRoot, v, "mouseover", function (e) {
                n.trigger("mouseover", e)
            }), f.bind(this._tooltipRoot, v, "mouseout", function (e) {
                n.trigger("mouseout", e)
            }), this._init()
        }

        var s = e("lodash/lang/isString"), c = e("lodash/object/assign"), l = e("lodash/collection/forEach"), u = e("min-dom/lib/domify"), p = e("min-dom/lib/attr"), d = e("min-dom/lib/classes"), h = e("min-dom/lib/remove"), f = e("min-dom/lib/delegate"), m = new (e("../../util/IdGenerator"))("tt"), g = "djs-tooltip", v = "." + g;
        a.$inject = ["eventBus", "canvas"], t.exports = a, a.prototype.add = function (e) {
            if (!e.position)throw new Error("must specifiy tooltip position");
            if (!e.html)throw new Error("must specifiy tooltip html");
            var t = this._ids.next();
            return e = c({}, this._tooltipDefaults, e, {id: t}), this._addTooltip(e), e.timeout && this.setTimeout(e), t
        }, a.prototype.trigger = function (e, t) {
            var n = t.delegateTarget || t.target, i = this.get(p(n, "data-tooltip-id"));
            i && ("mouseover" === e && i.timeout && this.clearTimeout(i), "mouseout" === e && i.timeout && (i.timeout = 1e3, this.setTimeout(i)))
        }, a.prototype.get = function (e) {
            return "string" != typeof e && (e = e.id), this._tooltips[e]
        }, a.prototype.clearTimeout = function (e) {
            if (e = this.get(e)) {
                var t = e.removeTimer;
                t && (clearTimeout(t), e.removeTimer = null)
            }
        }, a.prototype.setTimeout = function (e) {
            if (e = this.get(e)) {
                this.clearTimeout(e);
                var t = this;
                e.removeTimer = setTimeout(function () {
                    t.remove(e)
                }, e.timeout)
            }
        }, a.prototype.remove = function (e) {
            var t = this.get(e);
            t && (h(t.html), h(t.htmlContainer), delete t.htmlContainer, delete this._tooltips[t.id])
        }, a.prototype.show = function () {
            o(this._tooltipRoot)
        }, a.prototype.hide = function () {
            o(this._tooltipRoot, !1)
        }, a.prototype._updateRoot = function (e) {
            var t = e.scale || 1, n = e.scale || 1, i = "matrix(" + t + ",0,0," + n + "," + -1 * e.x * t + "," + -1 * e.y * n + ")";
            this._tooltipRoot.style.transform = i, this._tooltipRoot.style["-ms-transform"] = i
        }, a.prototype._addTooltip = function (e) {
            var t, n = e.id, i = e.html, r = this._tooltipRoot;
            i.get && (i = i.get(0)), s(i) && (i = u(i)), t = u('<div data-tooltip-id="' + n + '" class="' + g + '" style="position: absolute">'), t.appendChild(i), e.type && d(t).add("djs-tooltip-" + e.type), e.className && d(t).add(e.className), e.htmlContainer = t, r.appendChild(t), this._tooltips[n] = e, this._updateTooltip(e)
        }, a.prototype._updateTooltip = function (e) {
            var t = e.position, n = e.htmlContainer;
            r(n, t.x, t.y)
        }, a.prototype._updateTooltipVisibilty = function (e) {
            l(this._tooltips, function (t) {
                var n = t.show, i = t.htmlContainer, r = !0;
                n && ((n.minZoom > e.scale || n.maxZoom < e.scale) && (r = !1), o(i, r))
            })
        }, a.prototype._init = function () {
            function e(e) {
                t._updateRoot(e), t._updateTooltipVisibilty(e), t.show()
            }

            var t = this;
            this._eventBus.on("canvas.viewbox.changing", function (e) {
                t.hide()
            }), this._eventBus.on("canvas.viewbox.changed", function (t) {
                e(t.viewbox)
            })
        }
    }, {
        "../../util/IdGenerator": 243,
        "lodash/collection/forEach": 274,
        "lodash/lang/isString": 393,
        "lodash/object/assign": 396,
        "min-dom/lib/attr": 408,
        "min-dom/lib/classes": 409,
        "min-dom/lib/delegate": 412,
        "min-dom/lib/domify": 413,
        "min-dom/lib/remove": 417
    }],
    220: [function (e, t, n) {
        t.exports = {__init__: ["tooltips"], tooltips: ["type", e("./Tooltips")]}
    }, {"./Tooltips": 219}],
    221: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            var n = this;
            t.on("canvas.init", function (e) {
                n.addBBoxMarker(e.svg)
            })
        }

        i.$inject = ["canvas", "eventBus"], t.exports = i, i.prototype.addBBoxMarker = function (e) {
            var t = {fill: "none", "class": "outer-bound-marker"};
            e.rect(-1e4, -1e4, 10, 10).attr(t), e.rect(1e4, 1e4, 10, 10).attr(t)
        }
    }, {}],
    222: [function (e, t, n) {
        "use strict";
        function i() {
        }

        function r(e, t) {
            return t.get(e, !1)
        }

        function o(e) {
            function t(e) {
                d.stopEvent(e, !0)
            }

            function n(n) {
                s(m, function (n) {
                    c.bind(e, n, t, !0)
                })
            }

            function r(n) {
                setTimeout(function () {
                    s(m, function (n) {
                        c.unbind(e, n, t, !0)
                    })
                }, 500)
            }

            c.bind(e, "touchstart", n, !0), c.bind(e, "touchend", r, !0), c.bind(e, "touchcancel", r, !0);
            var o = new u.Manager(e, {
                inputClass: u.TouchInput,
                recognizers: []
            }), a = new u.Tap, l = new u.Pan({threshold: 10}), p = new u.Press, h = new u.Pinch, f = new u.Tap({
                event: "doubletap",
                taps: 2
            });
            return h.requireFailure(l), h.requireFailure(p), o.add([l, p, h, f, a]), o.reset = function (e) {
                var t = this.recognizers, n = this.session;
                n.stopped || (i("recognizer", "stop"), o.stop(e), setTimeout(function () {
                    var e, r;
                    for (i("recognizer", "reset"), e = 0; r = t[e]; e++)r.reset(), r.state = 8;
                    n.curRecognizer = null
                }, 0))
            }, o.on("hammer.input", function (e) {
                e.srcEvent.defaultPrevented && o.reset(!0)
            }), o
        }

        function a(e, t, n, a, s, c) {
            function u(e) {
                return function (t) {
                    i("element", e, t), s.fire(e, t)
                }
            }

            function m(e) {
                var t = l(e, "svg, .djs-element", !0);
                return t && new p(t)
            }

            function g(e) {
                function n(e) {
                    function n(e) {
                        var n = e.deltaX - o, i = e.deltaY - a;
                        t.scroll({dx: n, dy: i}), o = e.deltaX, a = e.deltaY
                    }

                    function r(e) {
                        v.off("panmove", n), v.off("panend", r), v.off("pancancel", r), i("canvas", "grab end")
                    }

                    i("canvas", "grab start");
                    var o = 0, a = 0;
                    v.on("panmove", n), v.on("panend", r), v.on("pancancel", r)
                }

                function r(e) {
                    var r = m(e.target), o = r && a.get(r);
                    return b && t.getRootElement() !== o ? (i("element", "move start", o, e, !0), b.start(e, o, !0)) : void n(e)
                }

                function s(e) {
                    function n(e) {
                        var n = 1 - (1 - e.scale) / 1.5, i = Math.max(h, Math.min(f, n * o));
                        t.zoom(i, a), d.stopEvent(e, !0)
                    }

                    function r(e) {
                        v.off("pinchmove", n), v.off("pinchend", r), v.off("pinchcancel", r), v.reset(!0), i("canvas", "zoom end")
                    }

                    i("canvas", "zoom start");
                    var o = t.zoom(), a = e.center;
                    v.on("pinchmove", n), v.on("pinchend", r), v.on("pinchcancel", r)
                }

                v = o(e), v.on("doubletap", u("element.dblclick")), v.on("tap", u("element.click")), v.on("panstart", r), v.on("press", r), v.on("pinchstart", s)
            }

            var v, y = r("dragging", e), b = r("move", e), x = r("contextPad", e), E = r("palette", e);
            y && n.on("drag.move", function (e) {
                var t = e.originalEvent;
                if (t && !(t instanceof MouseEvent)) {
                    var n = d.toPoint(t), i = document.elementFromPoint(n.x, n.y), r = m(i), o = r && a.get(r);
                    o !== e.hover && (e.hover && y.out(e), o && (y.hover({
                        element: o,
                        gfx: r
                    }), e.hover = o, e.hoverGfx = r))
                }
            }), x && n.on("contextPad.create", function (e) {
                var t = e.pad.html, n = o(t);
                n.on("panstart", function (e) {
                    i("context-pad", "panstart", e), x.trigger("dragstart", e, !0)
                }), n.on("press", function (e) {
                    i("context-pad", "press", e), x.trigger("dragstart", e, !0)
                }), n.on("tap", function (e) {
                    i("context-pad", "tap", e), x.trigger("click", e)
                })
            }), E && n.on("palette.create", function (e) {
                var t = e.html, n = o(t);
                n.on("panstart", function (e) {
                    i("palette", "panstart", e), E.trigger("dragstart", e, !0)
                }), n.on("press", function (e) {
                    i("palette", "press", e), E.trigger("dragstart", e, !0)
                }), n.on("tap", function (e) {
                    i("palette", "tap", e), E.trigger("click", e)
                })
            }), n.on("canvas.init", function (e) {
                g(e.svg.node)
            })
        }

        var s = e("lodash/collection/forEach"), c = e("min-dom/lib/event"), l = e("min-dom/lib/closest"), u = e("hammerjs"), p = e("../../../vendor/snapsvg"), d = e("../../util/Event"), h = .2, f = 4, m = ["mousedown", "mouseup", "mouseover", "mouseout", "click", "dblclick"];
        a.$inject = ["injector", "canvas", "eventBus", "elementRegistry", "interactionEvents", "touchFix"], t.exports = a
    }, {
        "../../../vendor/snapsvg": 258,
        "../../util/Event": 240,
        hammerjs: 252,
        "lodash/collection/forEach": 274,
        "min-dom/lib/closest": 411,
        "min-dom/lib/event": 414
    }],
    223: [function (e, t, n) {
        t.exports = {
            __depends__: [e("../interaction-events")],
            __init__: ["touchInteractionEvents"],
            touchInteractionEvents: ["type", e("./TouchInteractionEvents")],
            touchFix: ["type", e("./TouchFix")]
        }
    }, {"../interaction-events": 157, "./TouchFix": 221, "./TouchInteractionEvents": 222}],
    224: [function (e, t, n) {
        "use strict";
        function i() {
        }

        var r = e("./LayoutUtil").getMid;
        t.exports = i, i.prototype.layoutConnection = function (e, t) {
            return [r(e.source), r(e.target)]
        }
    }, {"./LayoutUtil": 226}],
    225: [function (e, t, n) {
        "use strict";
        function i(e) {
            return o({original: e.point.original || e.point}, e.actual)
        }

        function r(e, t) {
            this._elementRegistry = e, this._graphicsFactory = t
        }

        var o = e("lodash/object/assign"), a = e("./LayoutUtil");
        r.$inject = ["elementRegistry", "graphicsFactory"], t.exports = r, r.prototype.getCroppedWaypoints = function (e, t, n) {
            t = t || e.source, n = n || e.target;
            var r = this.getDockingPoint(e, t, !0), o = this.getDockingPoint(e, n), a = e.waypoints.slice(r.idx + 1, o.idx);
            return a.unshift(i(r)), a.push(i(o)), a
        }, r.prototype.getDockingPoint = function (e, t, n) {
            var i, r, o, a = e.waypoints;
            return i = n ? 0 : a.length - 1, r = a[i], o = this._getIntersection(t, e, n), {
                point: r,
                actual: o || r,
                idx: i
            }
        }, r.prototype._getIntersection = function (e, t, n) {
            var i = this._getShapePath(e), r = this._getConnectionPath(t);
            return a.getElementLineIntersection(i, r, n)
        }, r.prototype._getConnectionPath = function (e) {
            return this._graphicsFactory.getConnectionPath(e)
        }, r.prototype._getShapePath = function (e) {
            return this._graphicsFactory.getShapePath(e)
        }, r.prototype._getGfx = function (e) {
            return this._elementRegistry.getGraphics(e)
        }
    }, {"./LayoutUtil": 226, "lodash/object/assign": 396}],
    226: [function (e, t, n) {
        "use strict";
        function i(e) {
            return {x: Math.round(e.x), y: Math.round(e.y), width: Math.round(e.width), height: Math.round(e.height)}
        }

        function r(e) {
            return {x: Math.round(e.x), y: Math.round(e.y)}
        }

        function o(e) {
            return {top: e.y, right: e.x + (e.width || 0), bottom: e.y + (e.height || 0), left: e.x}
        }

        function a(e) {
            return {x: e.left, y: e.top, width: e.right - e.left, height: e.bottom - e.top}
        }

        function s(e) {
            return r({x: e.x + (e.width || 0) / 2, y: e.y + (e.height || 0) / 2})
        }

        function c(e, t, n) {
            n = n || 0, p(n) || (n = {x: n, y: n});
            var i = o(e), r = o(t), a = i.bottom + n.y <= r.top, s = i.left - n.x >= r.right, c = i.top - n.y >= r.bottom, l = i.right + n.x <= r.left, u = a ? "top" : c ? "bottom" : null, d = l ? "left" : s ? "right" : null;
            return d && u ? u + "-" + d : d || u || "intersect"
        }

        function l(e, t, n) {
            var i = u(e, t);
            return 1 === i.length ? r(i[0]) : 2 === i.length && h(i[0], i[1]) < 1 ? r(i[0]) : i.length > 1 ? (i = d(i, function (e) {
                var t = Math.floor(100 * e.t2) || 1;
                return t = 100 - t, t = (10 > t ? "0" : "") + t, e.segment2 + "#" + t
            }), r(i[n ? 0 : i.length - 1])) : null
        }

        function u(e, t) {
            return f.path.intersection(e, t)
        }

        var p = e("lodash/lang/isObject"), d = e("lodash/collection/sortBy"), h = e("../util/Geometry").pointDistance, f = e("../../vendor/snapsvg");
        t.exports.roundBounds = i, t.exports.roundPoint = r, t.exports.asTRBL = o, t.exports.asBounds = a, t.exports.getMid = s, t.exports.getOrientation = c, t.exports.getElementLineIntersection = l, t.exports.getIntersections = u
    }, {
        "../../vendor/snapsvg": 258,
        "../util/Geometry": 241,
        "lodash/collection/sortBy": 281,
        "lodash/lang/isObject": 391
    }],
    227: [function (e, t, n) {
        "use strict";
        function i(e, t, n) {
            return e >= t && n >= e
        }

        function r(e, t, n) {
            var r = {x: "width", y: "height"};
            return i(t[e], n[e], n[e] + n[r[e]])
        }

        function o(e, t) {
            switch (e) {
                case"intersect":
                    return null;
                case"top":
                case"bottom":
                    return "v:v";
                case"left":
                case"right":
                    return "h:h";
                default:
                    return t
            }
        }

        var a = e("lodash/lang/isArray"), s = e("lodash/collection/find"), c = e("lodash/array/without"), l = e("lodash/object/assign"), u = e("./LayoutUtil"), p = e("../util/Geometry"), d = u.getOrientation, h = u.getMid, f = p.pointsAligned, m = p.pointInRect, g = p.pointDistance, v = 20, y = {
            "h:h": 20,
            "v:v": 20,
            "h:v": -10,
            "v:h": -10
        };
        t.exports.getBendpoints = function (e, t, n) {
            n = n || "h:h";
            var i, r;
            if ("h:v" === n)return [{x: t.x, y: e.y}];
            if ("v:h" === n)return [{x: e.x, y: t.y}];
            if ("h:h" === n)return i = Math.round((t.x - e.x) / 2 + e.x), [{x: i, y: e.y}, {x: i, y: t.y}];
            if ("v:v" === n)return r = Math.round((t.y - e.y) / 2 + e.y), [{x: e.x, y: r}, {x: t.x, y: r}];
            throw new Error("unknown directions: <" + n + ">: directions must be specified as {a direction}:{b direction} (direction in h|v)")
        }, t.exports.connectPoints = function (e, t, n) {
            var i = [];
            return f(e, t) || (i = this.getBendpoints(e, t, n)), i.unshift(e), i.push(t), i
        }, t.exports.connectRectangles = function (e, t, n, i, r) {
            var a = r && r.preferredLayouts || [], s = c(a, "straight")[0] || "h:h", l = y[s] || 0, u = d(e, t, l), p = o(u, s);
            if (n = n || h(e), i = i || h(t), p) {
                if ("h:h" === p)switch (u) {
                    case"top-right":
                    case"right":
                    case"bottom-right":
                        n = {original: n, x: e.x, y: n.y}, i = {original: i, x: t.x + t.width, y: i.y};
                        break;
                    case"top-left":
                    case"left":
                    case"bottom-left":
                        n = {original: n, x: e.x + e.width, y: n.y}, i = {original: i, x: t.x, y: i.y}
                }
                if ("v:v" === p)switch (u) {
                    case"top-left":
                    case"top":
                    case"top-right":
                        n = {original: n, x: n.x, y: e.y + e.height}, i = {original: i, x: i.x, y: t.y};
                        break;
                    case"bottom-left":
                    case"bottom":
                    case"bottom-right":
                        n = {original: n, x: n.x, y: e.y}, i = {original: i, x: i.x, y: t.y + t.height}
                }
                return this.connectPoints(n, i, p)
            }
        }, t.exports.repairConnection = function (e, t, n, i, r, o) {
            a(n) && (r = n, o = i, n = h(e), i = h(t)), o = l({preferredLayouts: []}, o);
            var s, c = o.preferredLayouts, u = -1 !== c.indexOf("straight");
            return u && (s = this.layoutStraight(e, t, n, i, o)), s || (o.endChanged ? (s = this._repairConnectionSide(t, e, i, r.slice().reverse()), s = s && s.reverse()) : o.startChanged ? s = this._repairConnectionSide(e, t, n, r) : r && r.length && (s = r)), s || (s = this.connectRectangles(e, t, n, i, o)), s
        }, t.exports.layoutStraight = function (e, t, n, i, o) {
            var a, s, c, l = {};
            return c = d(e, t), /^(top|bottom|left|right)$/.test(c) ? (/top|bottom/.test(c) && (a = "x", s = "y"), /left|right/.test(c) && (a = "y", s = "x"), r(a, n, t) ? (l[a] = n[a], [{
                x: n.x,
                y: n.y
            }, {
                x: void 0 !== l.x ? l.x : i.x,
                y: void 0 !== l.y ? l.y : i.y,
                original: {x: void 0 !== l.x ? l.x : i.x, y: void 0 !== l.y ? l.y : i.y}
            }]) : null) : null
        }, t.exports._repairConnectionSide = function (e, t, n, i) {
            function r(e, t, n) {
                return n.length < 3 ? !0 : n.length > 4 ? !1 : !!s(n, function (e, t) {
                    var i = n[t - 1];
                    return i && g(e, i) < 3
                })
            }

            function o(e, t, n) {
                var i = f(t, e);
                switch (i) {
                    case"v":
                        return {x: e.x, y: n.y};
                    case"h":
                        return {x: n.x, y: e.y}
                }
                return {x: e.x, y: e.y}
            }

            function a(e, t, n) {
                var i;
                for (i = e.length - 2; 0 !== i; i--)if (m(e[i], t, v) || m(e[i], n, v))return e.slice(i);
                return e
            }

            if (r(e, t, i))return null;
            var c, l = i[0], u = i.slice();
            return u[0] = n, u[1] = o(u[1], l, n), c = a(u, e, t), c !== u ? this._repairConnectionSide(e, t, n, c) : u
        }
    }, {
        "../util/Geometry": 241,
        "./LayoutUtil": 226,
        "lodash/array/without": 268,
        "lodash/collection/find": 273,
        "lodash/lang/isArray": 387,
        "lodash/object/assign": 396
    }],
    228: [function (e, t, n) {
        "use strict";
        function i() {
            Object.defineProperty(this, "businessObject", {writable: !0}), p.bind(this, "parent"), d.bind(this, "label"), f.bind(this, "outgoing"), m.bind(this, "incoming")
        }

        function r() {
            i.call(this), p.bind(this, "children"), h.bind(this, "host"), h.bind(this, "attachers")
        }

        function o() {
            r.call(this)
        }

        function a() {
            r.call(this), d.bind(this, "labelTarget")
        }

        function s() {
            i.call(this), f.bind(this, "source"), m.bind(this, "target")
        }

        var c = e("lodash/object/assign"), l = e("inherits"), u = e("object-refs"), p = new u({
            name: "children",
            enumerable: !0,
            collection: !0
        }, {name: "parent"}), d = new u({
            name: "label",
            enumerable: !0
        }, {name: "labelTarget"}), h = new u({
            name: "attachers",
            collection: !0
        }, {name: "host"}), f = new u({
            name: "outgoing",
            collection: !0
        }, {name: "source"}), m = new u({name: "incoming", collection: !0}, {name: "target"});
        l(r, i), l(o, r), l(a, r), l(s, i);
        var g = {connection: s, shape: r, label: a, root: o};
        t.exports.create = function (e, t) {
            var n = g[e];
            if (!n)throw new Error("unknown type: <" + e + ">");
            return c(new n, t)
        }, t.exports.Base = i, t.exports.Root = o, t.exports.Shape = r, t.exports.Connection = s, t.exports.Label = a
    }, {inherits: 253, "lodash/object/assign": 396, "object-refs": 254}],
    229: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            return {x: e.x - t.x, y: e.y - t.y}
        }

        function r(e) {
            return Math.sqrt(Math.pow(e.x, 2) + Math.pow(e.y, 2))
        }

        function o(e, t) {
            function n(e) {
                var n = d.start, o = l.toPoint(e), c = i(o, n);
                if (!d.dragging && r(c) > u && (d.dragging = !0, s.install(), a.set("move")), d.dragging) {
                    var p = d.last || d.start;
                    c = i(o, p), t.scroll({dx: c.x, dy: c.y}), d.last = o
                }
                e.preventDefault()
            }

            function o(e) {
                c.unbind(document, "mousemove", n), c.unbind(document, "mouseup", o), d = null, a.unset(), l.stopEvent(e)
            }

            function p(e) {
                e.button || e.ctrlKey || e.shiftKey || e.altKey || (d = {start: l.toPoint(e)}, c.bind(document, "mousemove", n), c.bind(document, "mouseup", o), l.stopEvent(e))
            }

            var d, h = t._container;
            c.bind(h, "mousedown", p)
        }

        var a = e("../../util/Cursor"), s = e("../../util/ClickTrap"), c = e("min-dom/lib/event"), l = e("../../util/Event"), u = 15;
        o.$inject = ["eventBus", "canvas"], t.exports = o
    }, {"../../util/ClickTrap": 236, "../../util/Cursor": 238, "../../util/Event": 240, "min-dom/lib/event": 414}],
    230: [function (e, t, n) {
        t.exports = {__init__: ["moveCanvas"], moveCanvas: ["type", e("./MoveCanvas")]}
    }, {"./MoveCanvas": 229}],
    231: [function (e, t, n) {
        t.exports = {__depends__: [e("../../features/touch")]}
    }, {"../../features/touch": 223}],
    232: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            this._canvas = t;
            var n = this;
            e.on("canvas.init", function (e) {
                n._init(t._container)
            })
        }

        var r = e("min-dom/lib/event"), o = e("../../util/Mouse").hasPrimaryModifier, a = e("../../util/Mouse").hasSecondaryModifier, s = e("../../util/Platform").isMac, c = e("./ZoomUtil").getStepRange, l = e("./ZoomUtil").cap, u = e("../../util/Math").log10, p = {
            min: .2,
            max: 4
        }, d = 10;
        i.prototype.scroll = function (e) {
            this._canvas.scroll(e)
        }, i.prototype.reset = function () {
            this._canvas.zoom("fit-viewport")
        }, i.prototype.zoom = function (e, t) {
            var n = this._canvas, i = n.zoom(!1), r = Math.pow(1 + Math.abs(e), e > 0 ? 1 : -1);
            n.zoom(l(p, i * r), t)
        }, i.prototype.stepZoom = function (e, t) {
            var n = this._canvas, i = c(p, d);
            e = e > 0 ? 1 : -1;
            var r = u(n.zoom()), o = Math.round(r / i) * i;
            o += i * e;
            var a = Math.pow(10, o);
            n.zoom(l(p, a), t)
        }, i.prototype._init = function (e) {
            var t = this;
            r.bind(e, "wheel", function (n) {
                n.preventDefault();
                var i, r = o(n), c = a(n);
                if (r || c) {
                    i = s ? 0 === n.deltaMode ? 1.25 : 50 : 0 === n.deltaMode ? .025 : .5;
                    var l = {};
                    c ? l.dx = i * (n.deltaX || n.deltaY) : l.dy = i * n.deltaY, t.scroll(l)
                } else {
                    i = 0 === n.deltaMode ? .025 : .5;
                    var u = e.getBoundingClientRect(), p = {x: n.clientX - u.left, y: n.clientY - u.top};
                    t.zoom(n.deltaY * i / -5, p)
                }
            })
        }, i.$inject = ["eventBus", "canvas"], t.exports = i
    }, {
        "../../util/Math": 245,
        "../../util/Mouse": 246,
        "../../util/Platform": 247,
        "./ZoomUtil": 233,
        "min-dom/lib/event": 414
    }],
    233: [function (e, t, n) {
        "use strict";
        var i = e("../../util/Math").log10;
        t.exports.getStepRange = function (e, t) {
            var n = i(e.min), r = i(e.max), o = Math.abs(n) + Math.abs(r);
            return o / t
        }, t.exports.cap = function (e, t) {
            return Math.max(e.min, Math.min(e.max, t))
        }
    }, {"../../util/Math": 245}],
    234: [function (e, t, n) {
        t.exports = {__init__: ["zoomScroll"], zoomScroll: ["type", e("./ZoomScroll")]}
    }, {"./ZoomScroll": 232}],
    235: [function (e, t, n) {
        "use strict";
        function i(e) {
            return {x: e.x + e.width / 2, y: e.y + e.height / 2}
        }

        function r(e, t) {
            return {x: e.x - t.x, y: e.y - t.y}
        }

        function o(e, t, n) {
            var o = i(t), a = i(n), c = r(e, o), l = {x: c.x * (n.width / t.width), y: c.y * (n.height / t.height)};
            return s({x: a.x + l.x, y: a.y + l.y})
        }

        function a(e, t, n) {
            var o = i(e), a = i(t), c = i(n), l = r(e, o), u = r(o, a), p = {
                x: u.x * (n.width / t.width),
                y: u.y * (n.height / t.height)
            }, d = {x: c.x + p.x, y: c.y + p.y};
            return s({x: d.x + l.x - e.x, y: d.y + l.y - e.y})
        }

        var s = e("../layout/LayoutUtil").roundPoint;
        t.exports.getNewAttachPoint = o, t.exports.getNewAttachShapeDelta = a
    }, {"../layout/LayoutUtil": 226}],
    236: [function (e, t, n) {
        "use strict";
        function i(e) {
            s(e), r(!1)
        }

        function r(e) {
            a[e ? "bind" : "unbind"](document.body, "click", i, !0)
        }

        function o() {
            return r(!0), function () {
                r(!1)
            }
        }

        var a = e("min-dom/lib/event"), s = e("./Event").stopEvent;
        t.exports.install = o
    }, {"./Event": 240, "min-dom/lib/event": 414}],
    237: [function (e, t, n) {
        "use strict";
        t.exports.remove = function (e, t) {
            if (!e || !t)return -1;
            var n = e.indexOf(t);
            return -1 !== n && e.splice(n, 1), n
        }, t.exports.add = function (e, t, n) {
            if (e && t) {
                "number" != typeof n && (n = -1);
                var i = e.indexOf(t);
                if (-1 !== i) {
                    if (i === n)return;
                    if (-1 === n)return;
                    e.splice(i, 1)
                }
                -1 !== n ? e.splice(n, 0, t) : e.push(t)
            }
        }, t.exports.indexOf = function (e, t) {
            return e && t ? e.indexOf(t) : -1
        }
    }, {}],
    238: [function (e, t, n) {
        "use strict";
        var i = e("min-dom/lib/classes"), r = /^djs-cursor-.*$/;
        t.exports.set = function (e) {
            var t = i(document.body);
            t.removeMatching(r), e && t.add("djs-cursor-" + e)
        }, t.exports.unset = function () {
            this.set(null)
        }
    }, {"min-dom/lib/classes": 409}],
    239: [function (e, t, n) {
        "use strict";
        function i(e, t, n) {
            var i = !n || -1 === e.indexOf(t);
            return i && e.push(t), i
        }

        function r(e, t, n) {
            n = n || 0, p(e) || (e = [e]), f(e, function (e, i) {
                var o = t(e, i, n);
                p(o) && o.length && r(o, t, n + 1)
            })
        }

        function o(e, t, n) {
            var o = [], a = [];
            return r(e, function (e, r, s) {
                i(o, e, t);
                var c = e.children;
                return (-1 === n || n > s) && c && i(a, c, t) ? c : void 0
            }), o
        }

        function a(e, t) {
            return o(e, !t, 1)
        }

        function s(e, t) {
            return o(e, !t, -1)
        }

        function c(e) {
            function t(e) {
                i[e.source.id] && i[e.target.id] && (i[e.id] = e), o[e.source.id] && o[e.target.id] && (c[e.id] = s[e.id] = e), a[e.id] = e
            }

            function n(e) {
                return s[e.id] = e, e.waypoints ? void(c[e.id] = a[e.id] = e) : (o[e.id] = e, f(e.incoming, t), f(e.outgoing, t), e.children)
            }

            var i = h(e, function (e) {
                return e.id
            }), o = {}, a = {}, s = {}, c = {};
            return r(e, n), {allShapes: o, allConnections: a, topLevel: i, enclosedConnections: c, enclosedElements: s}
        }

        function l(e, t) {
            t = !!t, p(e) || (e = [e]);
            var n, i, r, o;
            return f(e, function (e) {
                var a = e;
                e.waypoints && !t && (a = l(e.waypoints, !0));
                var s = a.x, c = a.y, u = a.height || 0, p = a.width || 0;
                (n > s || void 0 === n) && (n = s), (i > c || void 0 === i) && (i = c), (s + p > r || void 0 === r) && (r = s + p), (c + u > o || void 0 === o) && (o = c + u)
            }), {x: n, y: i, height: o - i, width: r - n}
        }

        function u(e, t) {
            var n = {};
            return f(e, function (e) {
                var i = e;
                i.waypoints && (i = l(i)), !d(t.y) && i.x > t.x && (n[e.id] = e), !d(t.x) && i.y > t.y && (n[e.id] = e), i.x > t.x && i.y > t.y && (d(t.width) && d(t.height) && i.width + i.x < t.width + t.x && i.height + i.y < t.height + t.y ? n[e.id] = e : d(t.width) && d(t.height) || (n[e.id] = e))
            }), n
        }

        var p = e("lodash/lang/isArray"), d = e("lodash/lang/isNumber"), h = e("lodash/collection/groupBy"), f = e("lodash/collection/forEach");
        t.exports.eachElement = r, t.exports.selfAndDirectChildren = a, t.exports.selfAndAllChildren = s, t.exports.getBBox = l, t.exports.getEnclosedElements = u, t.exports.getClosure = c
    }, {
        "lodash/collection/forEach": 274,
        "lodash/collection/groupBy": 275,
        "lodash/lang/isArray": 387,
        "lodash/lang/isNumber": 390
    }],
    240: [function (e, t, n) {
        "use strict";
        function i(e) {
            return e && e.preventDefault()
        }

        function r(e, t) {
            e && (e.stopPropagation && e.stopPropagation(), t && e.stopImmediatePropagation && e.stopImmediatePropagation())
        }

        function o(e) {
            return e.originalEvent || e.srcEvent
        }

        function a(e, t) {
            c(e, t), s(e)
        }

        function s(e) {
            i(e), i(o(e))
        }

        function c(e, t) {
            r(e, t), r(o(e), t)
        }

        function l(e) {
            return e.pointers && e.pointers.length && (e = e.pointers[0]), e.touches && e.touches.length && (e = e.touches[0]), e ? {
                x: e.clientX,
                y: e.clientY
            } : null
        }

        t.exports.getOriginal = o, t.exports.stopEvent = a, t.exports.preventDefault = s, t.exports.stopPropagation = c, t.exports.toPoint = l
    }, {}],
    241: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            return Math.sqrt(Math.pow(e.x - t.x, 2) + Math.pow(e.y - t.y, 2))
        }

        function r(e, t) {
            return Math.abs(e.x - t.x) <= o ? "h" : Math.abs(e.y - t.y) <= o ? "v" : !1
        }

        t.exports.pointDistance = i, t.exports.pointsOnLine = function (e, t, n) {
            if (!e || !t || !n)return !1;
            var r = (t.x - e.x) * (n.y - e.y) - (t.y - e.y) * (n.x - e.x), o = i(e, t);
            return Math.abs(r / o) < 5
        };
        var o = 2;
        t.exports.pointsAligned = r, t.exports.pointInRect = function (e, t, n) {
            return n = n || 0, e.x > t.x - n && e.y > t.y - n && e.x < t.x + t.width + n && e.y < t.y + t.height + n
        }, t.exports.getMidPoint = function (e, t) {
            return {x: Math.round(e.x + (t.x - e.x) / 2), y: Math.round(e.y + (t.y - e.y) / 2)}
        }
    }, {}],
    242: [function (e, t, n) {
        "use strict";
        function i(e) {
            return e.select(".djs-visual")
        }

        function r(e) {
            return e.parent().children()[1]
        }

        function o(e) {
            return i(e).select("*").getBBox()
        }

        t.exports.getVisual = i, t.exports.getChildren = r, t.exports.getBBox = o
    }, {}],
    243: [function (e, t, n) {
        "use strict";
        function i(e) {
            this._counter = 0, this._prefix = (e ? e + "-" : "") + Math.floor(1e9 * Math.random()) + "-"
        }

        t.exports = i, i.prototype.next = function () {
            return this._prefix + ++this._counter
        }
    }, {}],
    244: [function (e, t, n) {
        "use strict";
        function i(e, t) {
            var n = e.x, i = e.y;
            return [["M", n, i], ["m", 0, -t], ["a", t, t, 0, 1, 1, 0, 2 * t], ["a", t, t, 0, 1, 1, 0, -2 * t], ["z"]]
        }

        function r(e) {
            var t = [];
            return e.forEach(function (e, n) {
                t.push([0 === n ? "M" : "L", e.x, e.y])
            }), t
        }

        function o(e, t) {
            var n, i;
            for (n = 0; i = e[n]; n++)if (s(i, t) <= p)return {point: e[n], bendpoint: !0, index: n};
            return null
        }

        function a(e, t) {
            var n, o = c.path.intersection(i(t, p), r(e)), a = o[0], s = o[o.length - 1];
            return a ? a !== s ? a.segment2 !== s.segment2 ? (n = u(a.segment2, s.segment2) - 1, {
                point: e[n],
                bendpoint: !0,
                index: n
            }) : {point: {x: l(a.x + s.x) / 2, y: l(a.y + s.y) / 2}, index: a.segment2} : {
                point: {
                    x: l(a.x),
                    y: l(a.y)
                }, index: a.segment2
            } : null
        }

        var s = e("./Geometry").pointDistance, c = e("../../vendor/snapsvg"), l = Math.round, u = Math.max, p = 10;
        t.exports.getApproxIntersection = function (e, t) {
            return o(e, t) || a(e, t)
        }
    }, {"../../vendor/snapsvg": 258, "./Geometry": 241}],
    245: [function (e, t, n) {
        "use strict";
        function i(e) {
            return Math.log(e) / Math.log(10)
        }

        t.exports.log10 = i
    }, {}],
    246: [function (e, t, n) {
        "use strict";
        function i(e) {
            return !(r(e) || e).button
        }

        var r = e("./Event").getOriginal, o = e("./Platform").isMac;
        t.exports.isPrimaryButton = i, t.exports.isMac = o, t.exports.hasPrimaryModifier = function (e) {
            var t = r(e) || e;
            return i(e) ? o() ? t.altKey : t.ctrlKey : !1
        }, t.exports.hasSecondaryModifier = function (e) {
            var t = r(e) || e;
            return i(e) && t.shiftKey
        }
    }, {"./Event": 240, "./Platform": 247}],
    247: [function (e, t, n) {
        "use strict";
        t.exports.isMac = function () {
            return /mac/i.test(navigator.platform)
        }
    }, {}],
    248: [function (e, t, n) {
        "use strict";
        t.exports.saveClear = function (e, t) {
            if ("function" != typeof t)throw new Error("removeFn iterator must be a function");
            if (e) {
                for (var n; n = e[0];)t(n);
                return e
            }
        }
    }, {}],
    249: [function (e, t, n) {
        "use strict";
        function i(e) {
            for (var t, n = "", i = 0; t = e[i]; i++)n += t.x + "," + t.y + " ";
            return n
        }

        var r = e("../../vendor/snapsvg");
        t.exports.componentsToPath = function (e) {
            return e.join(",").replace(/,?([A-z]),?/g, "$1")
        }, t.exports.toSVGPoints = i, t.exports.createLine = function (e, t) {
            return r.create("polyline", {points: i(e)}).attr(t || {})
        }, t.exports.updateLine = function (e, t) {
            return e.attr({points: i(t)})
        }
    }, {"../../vendor/snapsvg": 258}],
    250: [function (e, t, n) {
        "use strict";
        function i(e) {
            var t = e.split("-");
            return {horizontal: t[0] || "center", vertical: t[1] || "top"}
        }

        function r(e) {
            return p(e) ? d({top: 0, left: 0, right: 0, bottom: 0}, e) : {top: e, left: e, right: e, bottom: e}
        }

        function o(e, t) {
            return t.textContent = e, h(t.getBBox(), ["width", "height"])
        }

        function a(e, t, n) {
            for (var i, r = e.shift(), a = r; ;) {
                if (i = o(a, n), i.width = a ? i.width : 0, " " === a || "" === a || i.width < Math.round(t) || a.length < 4)return s(e, a, r, i);
                a = l(a, i.width, t)
            }
        }

        function s(e, t, n, i) {
            if (t.length < n.length) {
                var r = e[0] || "", o = n.slice(t.length).trim();
                r = /-\s*$/.test(o) ? o.replace(/-\s*$/, "") + r.replace(/^\s+/, "") : o + " " + r, e[0] = r
            }
            return {width: i.width, height: i.height, text: t}
        }

        function c(e, t) {
            var n, i = e.split(/(\s|-)/g), r = [], o = 0;
            if (i.length > 1)for (; n = i.shift();) {
                if (!(n.length + o < t)) {
                    "-" === n && r.pop();
                    break
                }
                r.push(n), o += n.length
            }
            return r.join("")
        }

        function l(e, t, n) {
            var i = Math.max(e.length * (n / t), 1), r = c(e, i);
            return r || (r = e.slice(0, Math.max(Math.round(i - 1), 1))), r
        }

        function u(e) {
            this._config = d({}, {size: b, padding: y, style: {}, align: "center-top"}, e || {})
        }

        var p = e("lodash/lang/isObject"), d = e("lodash/object/assign"), h = e("lodash/object/pick"), f = e("lodash/collection/forEach"), m = e("lodash/collection/reduce"), g = e("lodash/object/merge"), v = e("../../vendor/snapsvg"), y = 0, b = {
            width: 150,
            height: 50
        };
        u.prototype.createText = function (e, t, n) {
            for (var o = g({}, this._config.size, n.box || {}), s = g({}, this._config.style, n.style || {}), c = i(n.align || this._config.align), l = r(void 0 !== n.padding ? n.padding : this._config.padding), u = t.split(/\r?\n/g), p = [], d = o.width - l.left - l.right, h = e.paper.text(0, 0, "").attr(s).node; u.length;)p.push(a(u, d, h));
            var y, b, x = m(p, function (e, t, n) {
                return e + t.height
            }, 0);
            switch (c.vertical) {
                case"middle":
                    y = (o.height - x) / 2 - p[0].height / 4;
                    break;
                default:
                    y = l.top
            }
            var E = e.text().attr(s);
            return f(p, function (e) {
                switch (y += e.height, c.horizontal) {
                    case"left":
                        b = l.left;
                        break;
                    case"right":
                        b = d - l.right - e.width;
                        break;
                    default:
                        b = Math.max((d - e.width) / 2 + l.left, 0)
                }
                var t = v.create("tspan", {x: b, y: y}).node;
                t.textContent = e.text, E.append(t)
            }), h.parentNode.removeChild(h), E
        }, t.exports = u
    }, {
        "../../vendor/snapsvg": 258,
        "lodash/collection/forEach": 274,
        "lodash/collection/reduce": 278,
        "lodash/lang/isObject": 391,
        "lodash/object/assign": 396,
        "lodash/object/merge": 399,
        "lodash/object/pick": 402
    }],
    251: [function (e, t, n) {
        !function (e) {
            var n, i, r = "0.4.2", o = "hasOwnProperty", a = /[\.\/]/, s = /\s*,\s*/, c = "*", l = function (e, t) {
                return e - t
            }, u = {n: {}}, p = function () {
                for (var e = 0, t = this.length; t > e; e++)if ("undefined" != typeof this[e])return this[e]
            }, d = function () {
                for (var e = this.length; --e;)if ("undefined" != typeof this[e])return this[e]
            }, h = function (e, t) {
                e = String(e);
                var r, o = i, a = Array.prototype.slice.call(arguments, 2), s = h.listeners(e), c = 0, u = [], f = {}, m = [], g = n;
                m.firstDefined = p, m.lastDefined = d, n = e, i = 0;
                for (var v = 0, y = s.length; y > v; v++)"zIndex"in s[v] && (u.push(s[v].zIndex), s[v].zIndex < 0 && (f[s[v].zIndex] = s[v]));
                for (u.sort(l); u[c] < 0;)if (r = f[u[c++]], m.push(r.apply(t, a)), i)return i = o, m;
                for (v = 0; y > v; v++)if (r = s[v], "zIndex"in r)if (r.zIndex == u[c]) {
                    if (m.push(r.apply(t, a)), i)break;
                    do if (c++, r = f[u[c]], r && m.push(r.apply(t, a)), i)break; while (r)
                } else f[r.zIndex] = r; else if (m.push(r.apply(t, a)), i)break;
                return i = o, n = g, m
            };
            h._events = u, h.listeners = function (e) {
                var t, n, i, r, o, s, l, p, d = e.split(a), h = u, f = [h], m = [];
                for (r = 0, o = d.length; o > r; r++) {
                    for (p = [], s = 0, l = f.length; l > s; s++)for (h = f[s].n, n = [h[d[r]], h[c]], i = 2; i--;)t = n[i], t && (p.push(t), m = m.concat(t.f || []));
                    f = p
                }
                return m
            }, h.on = function (e, t) {
                if (e = String(e), "function" != typeof t)return function () {
                };
                for (var n = e.split(s), i = 0, r = n.length; r > i; i++)!function (e) {
                    for (var n, i = e.split(a), r = u, o = 0, s = i.length; s > o; o++)r = r.n, r = r.hasOwnProperty(i[o]) && r[i[o]] || (r[i[o]] = {n: {}});
                    for (r.f = r.f || [], o = 0, s = r.f.length; s > o; o++)if (r.f[o] == t) {
                        n = !0;
                        break
                    }
                    !n && r.f.push(t)
                }(n[i]);
                return function (e) {
                    +e == +e && (t.zIndex = +e)
                }
            }, h.f = function (e) {
                var t = [].slice.call(arguments, 1);
                return function () {
                    h.apply(null, [e, null].concat(t).concat([].slice.call(arguments, 0)))
                }
            }, h.stop = function () {
                i = 1
            }, h.nt = function (e) {
                return e ? new RegExp("(?:\\.|\\/|^)" + e + "(?:\\.|\\/|$)").test(n) : n
            }, h.nts = function () {
                return n.split(a)
            }, h.off = h.unbind = function (e, t) {
                if (!e)return void(h._events = u = {n: {}});
                var n = e.split(s);
                if (n.length > 1)for (var i = 0, r = n.length; r > i; i++)h.off(n[i], t); else {
                    n = e.split(a);
                    var l, p, d, i, r, f, m, g = [u];
                    for (i = 0, r = n.length; r > i; i++)for (f = 0; f < g.length; f += d.length - 2) {
                        if (d = [f, 1], l = g[f].n, n[i] != c)l[n[i]] && d.push(l[n[i]]); else for (p in l)l[o](p) && d.push(l[p]);
                        g.splice.apply(g, d)
                    }
                    for (i = 0, r = g.length; r > i; i++)for (l = g[i]; l.n;) {
                        if (t) {
                            if (l.f) {
                                for (f = 0, m = l.f.length; m > f; f++)if (l.f[f] == t) {
                                    l.f.splice(f, 1);
                                    break
                                }
                                !l.f.length && delete l.f
                            }
                            for (p in l.n)if (l.n[o](p) && l.n[p].f) {
                                var v = l.n[p].f;
                                for (f = 0, m = v.length; m > f; f++)if (v[f] == t) {
                                    v.splice(f, 1);
                                    break
                                }
                                !v.length && delete l.n[p].f
                            }
                        } else {
                            delete l.f;
                            for (p in l.n)l.n[o](p) && l.n[p].f && delete l.n[p].f
                        }
                        l = l.n
                    }
                }
            }, h.once = function (e, t) {
                var n = function () {
                    return h.unbind(e, n), t.apply(this, arguments)
                };
                return h.on(e, n)
            }, h.version = r, h.toString = function () {
                return "You are running Eve " + r
            }, "undefined" != typeof t && t.exports ? t.exports = h : "function" == typeof define && define.amd ? define("eve", [], function () {
                return h
            }) : e.eve = h
        }(this)
    }, {}],
    252: [function (e, t, n) {
        !function (e, n, i, r) {
            "use strict";
            function o(e, t, n) {
                return setTimeout(p(e, n), t)
            }

            function a(e, t, n) {
                return Array.isArray(e) ? (s(e, n[t], n), !0) : !1
            }

            function s(e, t, n) {
                var i;
                if (e)if (e.forEach)e.forEach(t, n); else if (e.length !== r)for (i = 0; i < e.length;)t.call(n, e[i], i, e), i++; else for (i in e)e.hasOwnProperty(i) && t.call(n, e[i], i, e)
            }

            function c(e, t, n) {
                for (var i = Object.keys(t), o = 0; o < i.length;)(!n || n && e[i[o]] === r) && (e[i[o]] = t[i[o]]), o++;
                return e
            }

            function l(e, t) {
                return c(e, t, !0)
            }

            function u(e, t, n) {
                var i, r = t.prototype;
                i = e.prototype = Object.create(r), i.constructor = e, i._super = r, n && c(i, n)
            }

            function p(e, t) {
                return function () {
                    return e.apply(t, arguments)
                }
            }

            function d(e, t) {
                return typeof e == pe ? e.apply(t ? t[0] || r : r, t) : e
            }

            function h(e, t) {
                return e === r ? t : e
            }

            function f(e, t, n) {
                s(y(t), function (t) {
                    e.addEventListener(t, n, !1)
                })
            }

            function m(e, t, n) {
                s(y(t), function (t) {
                    e.removeEventListener(t, n, !1)
                })
            }

            function g(e, t) {
                for (; e;) {
                    if (e == t)return !0;
                    e = e.parentNode
                }
                return !1
            }

            function v(e, t) {
                return e.indexOf(t) > -1
            }

            function y(e) {
                return e.trim().split(/\s+/g)
            }

            function b(e, t, n) {
                if (e.indexOf && !n)return e.indexOf(t);
                for (var i = 0; i < e.length;) {
                    if (n && e[i][n] == t || !n && e[i] === t)return i;
                    i++
                }
                return -1
            }

            function x(e) {
                return Array.prototype.slice.call(e, 0)
            }

            function E(e, t, n) {
                for (var i = [], r = [], o = 0; o < e.length;) {
                    var a = t ? e[o][t] : e[o];
                    b(r, a) < 0 && i.push(e[o]), r[o] = a, o++
                }
                return n && (i = t ? i.sort(function (e, n) {
                    return e[t] > n[t]
                }) : i.sort()), i
            }

            function w(e, t) {
                for (var n, i, o = t[0].toUpperCase() + t.slice(1), a = 0; a < le.length;) {
                    if (n = le[a], i = n ? n + o : t, i in e)return i;
                    a++
                }
                return r
            }

            function _() {
                return me++
            }

            function S(e) {
                var t = e.ownerDocument;
                return t.defaultView || t.parentWindow
            }

            function C(e, t) {
                var n = this;
                this.manager = e, this.callback = t, this.element = e.element, this.target = e.options.inputTarget, this.domHandler = function (t) {
                    d(e.options.enable, [e]) && n.handler(t)
                }, this.init()
            }

            function A(e) {
                var t, n = e.options.inputClass;
                return new (t = n ? n : ye ? U : be ? H : ve ? G : F)(e, T)
            }

            function T(e, t, n) {
                var i = n.pointers.length, r = n.changedPointers.length, o = t & Ce && i - r === 0, a = t & (Te | je) && i - r === 0;
                n.isFirst = !!o, n.isFinal = !!a, o && (e.session = {}), n.eventType = t, j(e, n), e.emit("hammer.input", n), e.recognize(n), e.session.prevInput = n
            }

            function j(e, t) {
                var n = e.session, i = t.pointers, r = i.length;
                n.firstInput || (n.firstInput = R(t)), r > 1 && !n.firstMultiple ? n.firstMultiple = R(t) : 1 === r && (n.firstMultiple = !1);
                var o = n.firstInput, a = n.firstMultiple, s = a ? a.center : o.center, c = t.center = M(i);
                t.timeStamp = fe(), t.deltaTime = t.timeStamp - o.timeStamp, t.angle = L(s, c), t.distance = B(s, c), N(n, t), t.offsetDirection = P(t.deltaX, t.deltaY), t.scale = a ? I(a.pointers, i) : 1, t.rotation = a ? O(a.pointers, i) : 0, k(n, t);
                var l = e.element;
                g(t.srcEvent.target, l) && (l = t.srcEvent.target), t.target = l
            }

            function N(e, t) {
                var n = t.center, i = e.offsetDelta || {}, r = e.prevDelta || {}, o = e.prevInput || {};
                (t.eventType === Ce || o.eventType === Te) && (r = e.prevDelta = {
                    x: o.deltaX || 0,
                    y: o.deltaY || 0
                }, i = e.offsetDelta = {x: n.x, y: n.y}), t.deltaX = r.x + (n.x - i.x), t.deltaY = r.y + (n.y - i.y)
            }

            function k(e, t) {
                var n, i, o, a, s = e.lastInterval || t, c = t.timeStamp - s.timeStamp;
                if (t.eventType != je && (c > Se || s.velocity === r)) {
                    var l = s.deltaX - t.deltaX, u = s.deltaY - t.deltaY, p = D(c, l, u);
                    i = p.x, o = p.y, n = he(p.x) > he(p.y) ? p.x : p.y, a = P(l, u), e.lastInterval = t
                } else n = s.velocity, i = s.velocityX, o = s.velocityY, a = s.direction;
                t.velocity = n, t.velocityX = i, t.velocityY = o, t.direction = a
            }

            function R(e) {
                for (var t = [], n = 0; n < e.pointers.length;)t[n] = {
                    clientX: de(e.pointers[n].clientX),
                    clientY: de(e.pointers[n].clientY)
                }, n++;
                return {timeStamp: fe(), pointers: t, center: M(t), deltaX: e.deltaX, deltaY: e.deltaY}
            }

            function M(e) {
                var t = e.length;
                if (1 === t)return {x: de(e[0].clientX), y: de(e[0].clientY)};
                for (var n = 0, i = 0, r = 0; t > r;)n += e[r].clientX, i += e[r].clientY, r++;
                return {x: de(n / t), y: de(i / t)}
            }

            function D(e, t, n) {
                return {x: t / e || 0, y: n / e || 0}
            }

            function P(e, t) {
                return e === t ? Ne : he(e) >= he(t) ? e > 0 ? ke : Re : t > 0 ? Me : De
            }

            function B(e, t, n) {
                n || (n = Oe);
                var i = t[n[0]] - e[n[0]], r = t[n[1]] - e[n[1]];
                return Math.sqrt(i * i + r * r)
            }

            function L(e, t, n) {
                n || (n = Oe);
                var i = t[n[0]] - e[n[0]], r = t[n[1]] - e[n[1]];
                return 180 * Math.atan2(r, i) / Math.PI
            }

            function O(e, t) {
                return L(t[1], t[0], Ie) - L(e[1], e[0], Ie)
            }

            function I(e, t) {
                return B(t[0], t[1], Ie) / B(e[0], e[1], Ie)
            }

            function F() {
                this.evEl = Ue, this.evWin = ze, this.allow = !0, this.pressed = !1, C.apply(this, arguments)
            }

            function U() {
                this.evEl = qe, this.evWin = Ge, C.apply(this, arguments), this.store = this.manager.session.pointerEvents = []
            }

            function z() {
                this.evTarget = Ve, this.evWin = Ye, this.started = !1, C.apply(this, arguments)
            }

            function $(e, t) {
                var n = x(e.touches), i = x(e.changedTouches);
                return t & (Te | je) && (n = E(n.concat(i), "identifier", !0)), [n, i]
            }

            function H() {
                this.evTarget = Ke, this.targetIds = {}, C.apply(this, arguments)
            }

            function q(e, t) {
                var n = x(e.touches), i = this.targetIds;
                if (t & (Ce | Ae) && 1 === n.length)return i[n[0].identifier] = !0, [n, n];
                var r, o, a = x(e.changedTouches), s = [], c = this.target;
                if (o = n.filter(function (e) {
                        return g(e.target, c)
                    }), t === Ce)for (r = 0; r < o.length;)i[o[r].identifier] = !0, r++;
                for (r = 0; r < a.length;)i[a[r].identifier] && s.push(a[r]), t & (Te | je) && delete i[a[r].identifier], r++;
                return s.length ? [E(o.concat(s), "identifier", !0), s] : void 0
            }

            function G() {
                C.apply(this, arguments);
                var e = p(this.handler, this);
                this.touch = new H(this.manager, e), this.mouse = new F(this.manager, e)
            }

            function W(e, t) {
                this.manager = e, this.set(t)
            }

            function V(e) {
                if (v(e, nt))return nt;
                var t = v(e, it), n = v(e, rt);
                return t && n ? it + " " + rt : t || n ? t ? it : rt : v(e, tt) ? tt : et
            }

            function Y(e) {
                this.id = _(), this.manager = null, this.options = l(e || {}, this.defaults), this.options.enable = h(this.options.enable, !0), this.state = ot, this.simultaneous = {}, this.requireFail = []
            }

            function X(e) {
                return e & ut ? "cancel" : e & ct ? "end" : e & st ? "move" : e & at ? "start" : ""
            }

            function K(e) {
                return e == De ? "down" : e == Me ? "up" : e == ke ? "left" : e == Re ? "right" : ""
            }

            function Z(e, t) {
                var n = t.manager;
                return n ? n.get(e) : e
            }

            function Q() {
                Y.apply(this, arguments)
            }

            function J() {
                Q.apply(this, arguments), this.pX = null, this.pY = null
            }

            function ee() {
                Q.apply(this, arguments)
            }

            function te() {
                Y.apply(this, arguments), this._timer = null, this._input = null
            }

            function ne() {
                Q.apply(this, arguments)
            }

            function ie() {
                Q.apply(this, arguments)
            }

            function re() {
                Y.apply(this, arguments), this.pTime = !1, this.pCenter = !1, this._timer = null, this._input = null, this.count = 0
            }

            function oe(e, t) {
                return t = t || {}, t.recognizers = h(t.recognizers, oe.defaults.preset), new ae(e, t)
            }

            function ae(e, t) {
                t = t || {}, this.options = l(t, oe.defaults), this.options.inputTarget = this.options.inputTarget || e, this.handlers = {}, this.session = {}, this.recognizers = [], this.element = e, this.input = A(this), this.touchAction = new W(this, this.options.touchAction), se(this, !0), s(t.recognizers, function (e) {
                    var t = this.add(new e[0](e[1]));
                    e[2] && t.recognizeWith(e[2]), e[3] && t.requireFailure(e[3])
                }, this)
            }

            function se(e, t) {
                var n = e.element;
                s(e.options.cssProps, function (e, i) {
                    n.style[w(n.style, i)] = t ? e : ""
                })
            }

            function ce(e, t) {
                var i = n.createEvent("Event");
                i.initEvent(e, !0, !0), i.gesture = t, t.target.dispatchEvent(i)
            }

            var le = ["", "webkit", "moz", "MS", "ms", "o"], ue = n.createElement("div"), pe = "function", de = Math.round, he = Math.abs, fe = Date.now, me = 1, ge = /mobile|tablet|ip(ad|hone|od)|android/i, ve = "ontouchstart"in e, ye = w(e, "PointerEvent") !== r, be = ve && ge.test(navigator.userAgent), xe = "touch", Ee = "pen", we = "mouse", _e = "kinect", Se = 25, Ce = 1, Ae = 2, Te = 4, je = 8, Ne = 1, ke = 2, Re = 4, Me = 8, De = 16, Pe = ke | Re, Be = Me | De, Le = Pe | Be, Oe = ["x", "y"], Ie = ["clientX", "clientY"];
            C.prototype = {
                handler: function () {
                }, init: function () {
                    this.evEl && f(this.element, this.evEl, this.domHandler), this.evTarget && f(this.target, this.evTarget, this.domHandler), this.evWin && f(S(this.element), this.evWin, this.domHandler)
                }, destroy: function () {
                    this.evEl && m(this.element, this.evEl, this.domHandler), this.evTarget && m(this.target, this.evTarget, this.domHandler), this.evWin && m(S(this.element), this.evWin, this.domHandler)
                }
            };
            var Fe = {mousedown: Ce, mousemove: Ae, mouseup: Te}, Ue = "mousedown", ze = "mousemove mouseup";
            u(F, C, {
                handler: function (e) {
                    var t = Fe[e.type];
                    t & Ce && 0 === e.button && (this.pressed = !0), t & Ae && 1 !== e.which && (t = Te), this.pressed && this.allow && (t & Te && (this.pressed = !1), this.callback(this.manager, t, {
                        pointers: [e],
                        changedPointers: [e],
                        pointerType: we,
                        srcEvent: e
                    }))
                }
            });
            var $e = {pointerdown: Ce, pointermove: Ae, pointerup: Te, pointercancel: je, pointerout: je}, He = {
                2: xe,
                3: Ee,
                4: we,
                5: _e
            }, qe = "pointerdown", Ge = "pointermove pointerup pointercancel";
            e.MSPointerEvent && (qe = "MSPointerDown", Ge = "MSPointerMove MSPointerUp MSPointerCancel"), u(U, C, {
                handler: function (e) {
                    var t = this.store, n = !1, i = e.type.toLowerCase().replace("ms", ""), r = $e[i], o = He[e.pointerType] || e.pointerType, a = o == xe, s = b(t, e.pointerId, "pointerId");
                    r & Ce && (0 === e.button || a) ? 0 > s && (t.push(e), s = t.length - 1) : r & (Te | je) && (n = !0), 0 > s || (t[s] = e, this.callback(this.manager, r, {
                        pointers: t,
                        changedPointers: [e],
                        pointerType: o,
                        srcEvent: e
                    }), n && t.splice(s, 1))
                }
            });
            var We = {
                touchstart: Ce,
                touchmove: Ae,
                touchend: Te,
                touchcancel: je
            }, Ve = "touchstart", Ye = "touchstart touchmove touchend touchcancel";
            u(z, C, {
                handler: function (e) {
                    var t = We[e.type];
                    if (t === Ce && (this.started = !0), this.started) {
                        var n = $.call(this, e, t);
                        t & (Te | je) && n[0].length - n[1].length === 0 && (this.started = !1), this.callback(this.manager, t, {
                            pointers: n[0],
                            changedPointers: n[1],
                            pointerType: xe,
                            srcEvent: e
                        })
                    }
                }
            });
            var Xe = {
                touchstart: Ce,
                touchmove: Ae,
                touchend: Te,
                touchcancel: je
            }, Ke = "touchstart touchmove touchend touchcancel";
            u(H, C, {
                handler: function (e) {
                    var t = Xe[e.type], n = q.call(this, e, t);
                    n && this.callback(this.manager, t, {
                        pointers: n[0],
                        changedPointers: n[1],
                        pointerType: xe,
                        srcEvent: e
                    })
                }
            }), u(G, C, {
                handler: function (e, t, n) {
                    var i = n.pointerType == xe, r = n.pointerType == we;
                    if (i)this.mouse.allow = !1; else if (r && !this.mouse.allow)return;
                    t & (Te | je) && (this.mouse.allow = !0), this.callback(e, t, n)
                }, destroy: function () {
                    this.touch.destroy(), this.mouse.destroy()
                }
            });
            var Ze = w(ue.style, "touchAction"), Qe = Ze !== r, Je = "compute", et = "auto", tt = "manipulation", nt = "none", it = "pan-x", rt = "pan-y";
            W.prototype = {
                set: function (e) {
                    e == Je && (e = this.compute()), Qe && (this.manager.element.style[Ze] = e), this.actions = e.toLowerCase().trim()
                }, update: function () {
                    this.set(this.manager.options.touchAction)
                }, compute: function () {
                    var e = [];
                    return s(this.manager.recognizers, function (t) {
                        d(t.options.enable, [t]) && (e = e.concat(t.getTouchAction()))
                    }), V(e.join(" "))
                }, preventDefaults: function (e) {
                    if (!Qe) {
                        var t = e.srcEvent, n = e.offsetDirection;
                        if (this.manager.session.prevented)return void t.preventDefault();
                        var i = this.actions, r = v(i, nt), o = v(i, rt), a = v(i, it);
                        return r || o && n & Pe || a && n & Be ? this.preventSrc(t) : void 0
                    }
                }, preventSrc: function (e) {
                    this.manager.session.prevented = !0, e.preventDefault()
                }
            };
            var ot = 1, at = 2, st = 4, ct = 8, lt = ct, ut = 16, pt = 32;
            Y.prototype = {
                defaults: {}, set: function (e) {
                    return c(this.options, e), this.manager && this.manager.touchAction.update(), this
                }, recognizeWith: function (e) {
                    if (a(e, "recognizeWith", this))return this;
                    var t = this.simultaneous;
                    return e = Z(e, this), t[e.id] || (t[e.id] = e, e.recognizeWith(this)), this
                }, dropRecognizeWith: function (e) {
                    return a(e, "dropRecognizeWith", this) ? this : (e = Z(e, this), delete this.simultaneous[e.id], this)
                }, requireFailure: function (e) {
                    if (a(e, "requireFailure", this))return this;
                    var t = this.requireFail;
                    return e = Z(e, this), -1 === b(t, e) && (t.push(e), e.requireFailure(this)), this
                }, dropRequireFailure: function (e) {
                    if (a(e, "dropRequireFailure", this))return this;
                    e = Z(e, this);
                    var t = b(this.requireFail, e);
                    return t > -1 && this.requireFail.splice(t, 1), this
                }, hasRequireFailures: function () {
                    return this.requireFail.length > 0
                }, canRecognizeWith: function (e) {
                    return !!this.simultaneous[e.id]
                }, emit: function (e) {
                    function t(t) {
                        n.manager.emit(n.options.event + (t ? X(i) : ""), e)
                    }

                    var n = this, i = this.state;
                    ct > i && t(!0), t(), i >= ct && t(!0)
                }, tryEmit: function (e) {
                    return this.canEmit() ? this.emit(e) : void(this.state = pt)
                }, canEmit: function () {
                    for (var e = 0; e < this.requireFail.length;) {
                        if (!(this.requireFail[e].state & (pt | ot)))return !1;
                        e++
                    }
                    return !0
                }, recognize: function (e) {
                    var t = c({}, e);
                    return d(this.options.enable, [this, t]) ? (this.state & (lt | ut | pt) && (this.state = ot), this.state = this.process(t), void(this.state & (at | st | ct | ut) && this.tryEmit(t))) : (this.reset(), void(this.state = pt))
                }, process: function (e) {
                }, getTouchAction: function () {
                }, reset: function () {
                }
            }, u(Q, Y, {
                defaults: {pointers: 1}, attrTest: function (e) {
                    var t = this.options.pointers;
                    return 0 === t || e.pointers.length === t
                }, process: function (e) {
                    var t = this.state, n = e.eventType, i = t & (at | st), r = this.attrTest(e);
                    return i && (n & je || !r) ? t | ut : i || r ? n & Te ? t | ct : t & at ? t | st : at : pt
                }
            }), u(J, Q, {
                defaults: {event: "pan", threshold: 10, pointers: 1, direction: Le},
                getTouchAction: function () {
                    var e = this.options.direction, t = [];
                    return e & Pe && t.push(rt), e & Be && t.push(it), t
                },
                directionTest: function (e) {
                    var t = this.options, n = !0, i = e.distance, r = e.direction, o = e.deltaX, a = e.deltaY;
                    return r & t.direction || (t.direction & Pe ? (r = 0 === o ? Ne : 0 > o ? ke : Re, n = o != this.pX, i = Math.abs(e.deltaX)) : (r = 0 === a ? Ne : 0 > a ? Me : De, n = a != this.pY, i = Math.abs(e.deltaY))), e.direction = r, n && i > t.threshold && r & t.direction
                },
                attrTest: function (e) {
                    return Q.prototype.attrTest.call(this, e) && (this.state & at || !(this.state & at) && this.directionTest(e))
                },
                emit: function (e) {
                    this.pX = e.deltaX, this.pY = e.deltaY;
                    var t = K(e.direction);
                    t && this.manager.emit(this.options.event + t, e), this._super.emit.call(this, e)
                }
            }), u(ee, Q, {
                defaults: {event: "pinch", threshold: 0, pointers: 2}, getTouchAction: function () {
                    return [nt]
                }, attrTest: function (e) {
                    return this._super.attrTest.call(this, e) && (Math.abs(e.scale - 1) > this.options.threshold || this.state & at)
                }, emit: function (e) {
                    if (this._super.emit.call(this, e), 1 !== e.scale) {
                        var t = e.scale < 1 ? "in" : "out";
                        this.manager.emit(this.options.event + t, e);
                    }
                }
            }), u(te, Y, {
                defaults: {event: "press", pointers: 1, time: 500, threshold: 5},
                getTouchAction: function () {
                    return [et]
                },
                process: function (e) {
                    var t = this.options, n = e.pointers.length === t.pointers, i = e.distance < t.threshold, r = e.deltaTime > t.time;
                    if (this._input = e, !i || !n || e.eventType & (Te | je) && !r)this.reset(); else if (e.eventType & Ce)this.reset(), this._timer = o(function () {
                        this.state = lt, this.tryEmit()
                    }, t.time, this); else if (e.eventType & Te)return lt;
                    return pt
                },
                reset: function () {
                    clearTimeout(this._timer)
                },
                emit: function (e) {
                    this.state === lt && (e && e.eventType & Te ? this.manager.emit(this.options.event + "up", e) : (this._input.timeStamp = fe(), this.manager.emit(this.options.event, this._input)))
                }
            }), u(ne, Q, {
                defaults: {event: "rotate", threshold: 0, pointers: 2}, getTouchAction: function () {
                    return [nt]
                }, attrTest: function (e) {
                    return this._super.attrTest.call(this, e) && (Math.abs(e.rotation) > this.options.threshold || this.state & at)
                }
            }), u(ie, Q, {
                defaults: {event: "swipe", threshold: 10, velocity: .65, direction: Pe | Be, pointers: 1},
                getTouchAction: function () {
                    return J.prototype.getTouchAction.call(this)
                },
                attrTest: function (e) {
                    var t, n = this.options.direction;
                    return n & (Pe | Be) ? t = e.velocity : n & Pe ? t = e.velocityX : n & Be && (t = e.velocityY), this._super.attrTest.call(this, e) && n & e.direction && e.distance > this.options.threshold && he(t) > this.options.velocity && e.eventType & Te
                },
                emit: function (e) {
                    var t = K(e.direction);
                    t && this.manager.emit(this.options.event + t, e), this.manager.emit(this.options.event, e)
                }
            }), u(re, Y, {
                defaults: {
                    event: "tap",
                    pointers: 1,
                    taps: 1,
                    interval: 300,
                    time: 250,
                    threshold: 2,
                    posThreshold: 10
                }, getTouchAction: function () {
                    return [tt]
                }, process: function (e) {
                    var t = this.options, n = e.pointers.length === t.pointers, i = e.distance < t.threshold, r = e.deltaTime < t.time;
                    if (this.reset(), e.eventType & Ce && 0 === this.count)return this.failTimeout();
                    if (i && r && n) {
                        if (e.eventType != Te)return this.failTimeout();
                        var a = this.pTime ? e.timeStamp - this.pTime < t.interval : !0, s = !this.pCenter || B(this.pCenter, e.center) < t.posThreshold;
                        this.pTime = e.timeStamp, this.pCenter = e.center, s && a ? this.count += 1 : this.count = 1, this._input = e;
                        var c = this.count % t.taps;
                        if (0 === c)return this.hasRequireFailures() ? (this._timer = o(function () {
                            this.state = lt, this.tryEmit()
                        }, t.interval, this), at) : lt
                    }
                    return pt
                }, failTimeout: function () {
                    return this._timer = o(function () {
                        this.state = pt
                    }, this.options.interval, this), pt
                }, reset: function () {
                    clearTimeout(this._timer)
                }, emit: function () {
                    this.state == lt && (this._input.tapCount = this.count, this.manager.emit(this.options.event, this._input))
                }
            }), oe.VERSION = "2.0.4", oe.defaults = {
                domEvents: !1,
                touchAction: Je,
                enable: !0,
                inputTarget: null,
                inputClass: null,
                preset: [[ne, {enable: !1}], [ee, {enable: !1}, ["rotate"]], [ie, {direction: Pe}], [J, {direction: Pe}, ["swipe"]], [re], [re, {
                    event: "doubletap",
                    taps: 2
                }, ["tap"]], [te]],
                cssProps: {
                    userSelect: "none",
                    touchSelect: "none",
                    touchCallout: "none",
                    contentZooming: "none",
                    userDrag: "none",
                    tapHighlightColor: "rgba(0,0,0,0)"
                }
            };
            var dt = 1, ht = 2;
            ae.prototype = {
                set: function (e) {
                    return c(this.options, e), e.touchAction && this.touchAction.update(), e.inputTarget && (this.input.destroy(), this.input.target = e.inputTarget, this.input.init()), this
                }, stop: function (e) {
                    this.session.stopped = e ? ht : dt
                }, recognize: function (e) {
                    var t = this.session;
                    if (!t.stopped) {
                        this.touchAction.preventDefaults(e);
                        var n, i = this.recognizers, r = t.curRecognizer;
                        (!r || r && r.state & lt) && (r = t.curRecognizer = null);
                        for (var o = 0; o < i.length;)n = i[o], t.stopped === ht || r && n != r && !n.canRecognizeWith(r) ? n.reset() : n.recognize(e), !r && n.state & (at | st | ct) && (r = t.curRecognizer = n), o++
                    }
                }, get: function (e) {
                    if (e instanceof Y)return e;
                    for (var t = this.recognizers, n = 0; n < t.length; n++)if (t[n].options.event == e)return t[n];
                    return null
                }, add: function (e) {
                    if (a(e, "add", this))return this;
                    var t = this.get(e.options.event);
                    return t && this.remove(t), this.recognizers.push(e), e.manager = this, this.touchAction.update(), e
                }, remove: function (e) {
                    if (a(e, "remove", this))return this;
                    var t = this.recognizers;
                    return e = this.get(e), t.splice(b(t, e), 1), this.touchAction.update(), this
                }, on: function (e, t) {
                    var n = this.handlers;
                    return s(y(e), function (e) {
                        n[e] = n[e] || [], n[e].push(t)
                    }), this
                }, off: function (e, t) {
                    var n = this.handlers;
                    return s(y(e), function (e) {
                        t ? n[e].splice(b(n[e], t), 1) : delete n[e]
                    }), this
                }, emit: function (e, t) {
                    this.options.domEvents && ce(e, t);
                    var n = this.handlers[e] && this.handlers[e].slice();
                    if (n && n.length) {
                        t.type = e, t.preventDefault = function () {
                            t.srcEvent.preventDefault()
                        };
                        for (var i = 0; i < n.length;)n[i](t), i++
                    }
                }, destroy: function () {
                    this.element && se(this, !1), this.handlers = {}, this.session = {}, this.input.destroy(), this.element = null
                }
            }, c(oe, {
                INPUT_START: Ce,
                INPUT_MOVE: Ae,
                INPUT_END: Te,
                INPUT_CANCEL: je,
                STATE_POSSIBLE: ot,
                STATE_BEGAN: at,
                STATE_CHANGED: st,
                STATE_ENDED: ct,
                STATE_RECOGNIZED: lt,
                STATE_CANCELLED: ut,
                STATE_FAILED: pt,
                DIRECTION_NONE: Ne,
                DIRECTION_LEFT: ke,
                DIRECTION_RIGHT: Re,
                DIRECTION_UP: Me,
                DIRECTION_DOWN: De,
                DIRECTION_HORIZONTAL: Pe,
                DIRECTION_VERTICAL: Be,
                DIRECTION_ALL: Le,
                Manager: ae,
                Input: C,
                TouchAction: W,
                TouchInput: H,
                MouseInput: F,
                PointerEventInput: U,
                TouchMouseInput: G,
                SingleTouchInput: z,
                Recognizer: Y,
                AttrRecognizer: Q,
                Tap: re,
                Pan: J,
                Swipe: ie,
                Pinch: ee,
                Rotate: ne,
                Press: te,
                on: f,
                off: m,
                each: s,
                merge: l,
                extend: c,
                inherit: u,
                bindFn: p,
                prefixed: w
            }), typeof define == pe && define.amd ? define(function () {
                return oe
            }) : "undefined" != typeof t && t.exports ? t.exports = oe : e[i] = oe
        }(window, document, "Hammer")
    }, {}],
    253: [function (e, t, n) {
        arguments[4][115][0].apply(n, arguments)
    }, {dup: 115}],
    254: [function (e, t, n) {
        arguments[4][116][0].apply(n, arguments)
    }, {"./lib/collection": 255, "./lib/refs": 256, dup: 116}],
    255: [function (e, t, n) {
        arguments[4][117][0].apply(n, arguments)
    }, {dup: 117}],
    256: [function (e, t, n) {
        arguments[4][118][0].apply(n, arguments)
    }, {"./collection": 255, dup: 118}],
    257: [function (e, t, n) {
        !function (i, r) {
            if ("function" == typeof define && define.amd)define(["eve"], function (e) {
                return r(i, e)
            }); else if ("undefined" != typeof n) {
                var o = e("eve");
                t.exports = r(i, o)
            } else r(i, i.eve)
        }(window || this, function (e, t) {
            var n = function (t) {
                var n = {}, i = e.requestAnimationFrame || e.webkitRequestAnimationFrame || e.mozRequestAnimationFrame || e.oRequestAnimationFrame || e.msRequestAnimationFrame || function (e) {
                        setTimeout(e, 16)
                    }, r = Array.isArray || function (e) {
                        return e instanceof Array || "[object Array]" == Object.prototype.toString.call(e)
                    }, o = 0, a = "M" + (+new Date).toString(36), s = function () {
                    return a + (o++).toString(36)
                }, c = Date.now || function () {
                        return +new Date
                    }, l = function (e) {
                    var t = this;
                    if (null == e)return t.s;
                    var n = t.s - e;
                    t.b += t.dur * n, t.B += t.dur * n, t.s = e
                }, u = function (e) {
                    var t = this;
                    return null == e ? t.spd : void(t.spd = e)
                }, p = function (e) {
                    var t = this;
                    return null == e ? t.dur : (t.s = t.s * e / t.dur, void(t.dur = e))
                }, d = function () {
                    var e = this;
                    delete n[e.id], e.update(), t("mina.stop." + e.id, e)
                }, h = function () {
                    var e = this;
                    e.pdif || (delete n[e.id], e.update(), e.pdif = e.get() - e.b)
                }, f = function () {
                    var e = this;
                    e.pdif && (e.b = e.get() - e.pdif, delete e.pdif, n[e.id] = e)
                }, m = function () {
                    var e, t = this;
                    if (r(t.start)) {
                        e = [];
                        for (var n = 0, i = t.start.length; i > n; n++)e[n] = +t.start[n] + (t.end[n] - t.start[n]) * t.easing(t.s)
                    } else e = +t.start + (t.end - t.start) * t.easing(t.s);
                    t.set(e)
                }, g = function () {
                    var e = 0;
                    for (var r in n)if (n.hasOwnProperty(r)) {
                        var o = n[r], a = o.get();
                        e++, o.s = (a - o.b) / (o.dur / o.spd), o.s >= 1 && (delete n[r], o.s = 1, e--, function (e) {
                            setTimeout(function () {
                                t("mina.finish." + e.id, e)
                            })
                        }(o)), o.update()
                    }
                    e && i(g)
                }, v = function (e, t, r, o, a, c, y) {
                    var b = {
                        id: s(),
                        start: e,
                        end: t,
                        b: r,
                        s: 0,
                        dur: o - r,
                        spd: 1,
                        get: a,
                        set: c,
                        easing: y || v.linear,
                        status: l,
                        speed: u,
                        duration: p,
                        stop: d,
                        pause: h,
                        resume: f,
                        update: m
                    };
                    n[b.id] = b;
                    var x, E = 0;
                    for (x in n)if (n.hasOwnProperty(x) && (E++, 2 == E))break;
                    return 1 == E && i(g), b
                };
                return v.time = c, v.getById = function (e) {
                    return n[e] || null
                }, v.linear = function (e) {
                    return e
                }, v.easeout = function (e) {
                    return Math.pow(e, 1.7)
                }, v.easein = function (e) {
                    return Math.pow(e, .48)
                }, v.easeinout = function (e) {
                    if (1 == e)return 1;
                    if (0 == e)return 0;
                    var t = .48 - e / 1.04, n = Math.sqrt(.1734 + t * t), i = n - t, r = Math.pow(Math.abs(i), 1 / 3) * (0 > i ? -1 : 1), o = -n - t, a = Math.pow(Math.abs(o), 1 / 3) * (0 > o ? -1 : 1), s = r + a + .5;
                    return 3 * (1 - s) * s * s + s * s * s
                }, v.backin = function (e) {
                    if (1 == e)return 1;
                    var t = 1.70158;
                    return e * e * ((t + 1) * e - t)
                }, v.backout = function (e) {
                    if (0 == e)return 0;
                    e -= 1;
                    var t = 1.70158;
                    return e * e * ((t + 1) * e + t) + 1
                }, v.elastic = function (e) {
                    return e == !!e ? e : Math.pow(2, -10 * e) * Math.sin((e - .075) * (2 * Math.PI) / .3) + 1
                }, v.bounce = function (e) {
                    var t, n = 7.5625, i = 2.75;
                    return 1 / i > e ? t = n * e * e : 2 / i > e ? (e -= 1.5 / i, t = n * e * e + .75) : 2.5 / i > e ? (e -= 2.25 / i, t = n * e * e + .9375) : (e -= 2.625 / i, t = n * e * e + .984375), t
                }, e.mina = v, v
            }("undefined" == typeof t ? function () {
            } : t), i = function (e) {
                function n(e, t) {
                    if (e) {
                        if (e.tagName)return w(e);
                        if (r(e, "array") && n.set)return n.set.apply(n, e);
                        if (e instanceof y)return e;
                        if (null == t)return e = _.doc.querySelector(e), w(e)
                    }
                    return e = null == e ? "100%" : e, t = null == t ? "100%" : t, new E(e, t)
                }

                function i(e, t) {
                    if (t) {
                        if ("#text" == e && (e = _.doc.createTextNode(t.text || "")), "string" == typeof e && (e = i(e)), "string" == typeof t)return "xlink:" == t.substring(0, 6) ? e.getAttributeNS(q, t.substring(6)) : "xml:" == t.substring(0, 4) ? e.getAttributeNS(G, t.substring(4)) : e.getAttribute(t);
                        for (var n in t)if (t[S](n)) {
                            var r = C(t[n]);
                            r ? "xlink:" == n.substring(0, 6) ? e.setAttributeNS(q, n.substring(6), r) : "xml:" == n.substring(0, 4) ? e.setAttributeNS(G, n.substring(4), r) : e.setAttribute(n, r) : e.removeAttribute(n)
                        }
                    } else e = _.doc.createElementNS(G, e);
                    return e
                }

                function r(e, t) {
                    return t = C.prototype.toLowerCase.call(t), "finite" == t ? isFinite(e) : "array" == t && (e instanceof Array || Array.isArray && Array.isArray(e)) ? !0 : "null" == t && null === e || t == typeof e && null !== e || "object" == t && e === Object(e) || P.call(e).slice(8, -1).toLowerCase() == t
                }

                function o(e) {
                    if ("function" == typeof e || Object(e) !== e)return e;
                    var t = new e.constructor;
                    for (var n in e)e[S](n) && (t[n] = o(e[n]));
                    return t
                }

                function a(e, t) {
                    for (var n = 0, i = e.length; i > n; n++)if (e[n] === t)return e.push(e.splice(n, 1)[0])
                }

                function s(e, t, n) {
                    function i() {
                        var r = Array.prototype.slice.call(arguments, 0), o = r.join("␀"), s = i.cache = i.cache || {}, c = i.count = i.count || [];
                        return s[S](o) ? (a(c, o), n ? n(s[o]) : s[o]) : (c.length >= 1e3 && delete s[c.shift()], c.push(o), s[o] = e.apply(t, r), n ? n(s[o]) : s[o])
                    }

                    return i
                }

                function c(e, t, n, i, r, o) {
                    if (null == r) {
                        var a = e - n, s = t - i;
                        return a || s ? (180 + 180 * j.atan2(-s, -a) / M + 360) % 360 : 0
                    }
                    return c(e, t, r, o) - c(n, i, r, o)
                }

                function l(e) {
                    return e % 360 * M / 180
                }

                function u(e) {
                    return 180 * e / M % 360
                }

                function p(e) {
                    var t = [];
                    return e = e.replace(/(?:^|\s)(\w+)\(([^)]+)\)/g, function (e, n, i) {
                        return i = i.split(/\s*,\s*|\s+/), "rotate" == n && 1 == i.length && i.push(0, 0), "scale" == n && (i.length > 2 ? i = i.slice(0, 2) : 2 == i.length && i.push(0, 0), 1 == i.length && i.push(i[0], 0, 0)), "skewX" == n ? t.push(["m", 1, 0, j.tan(l(i[0])), 1, 0, 0]) : "skewY" == n ? t.push(["m", 1, j.tan(l(i[0])), 0, 1, 0, 0]) : t.push([n.charAt(0)].concat(i)), e
                    }), t
                }

                function d(e, t) {
                    var i = J(e), r = new n.Matrix;
                    if (i)for (var o = 0, a = i.length; a > o; o++) {
                        var s, c, l, u, p, d = i[o], h = d.length, f = C(d[0]).toLowerCase(), m = d[0] != f, g = m ? r.invert() : 0;
                        "t" == f && 2 == h ? r.translate(d[1], 0) : "t" == f && 3 == h ? m ? (s = g.x(0, 0), c = g.y(0, 0), l = g.x(d[1], d[2]), u = g.y(d[1], d[2]), r.translate(l - s, u - c)) : r.translate(d[1], d[2]) : "r" == f ? 2 == h ? (p = p || t, r.rotate(d[1], p.x + p.width / 2, p.y + p.height / 2)) : 4 == h && (m ? (l = g.x(d[2], d[3]), u = g.y(d[2], d[3]), r.rotate(d[1], l, u)) : r.rotate(d[1], d[2], d[3])) : "s" == f ? 2 == h || 3 == h ? (p = p || t, r.scale(d[1], d[h - 1], p.x + p.width / 2, p.y + p.height / 2)) : 4 == h ? m ? (l = g.x(d[2], d[3]), u = g.y(d[2], d[3]), r.scale(d[1], d[1], l, u)) : r.scale(d[1], d[1], d[2], d[3]) : 5 == h && (m ? (l = g.x(d[3], d[4]), u = g.y(d[3], d[4]), r.scale(d[1], d[2], l, u)) : r.scale(d[1], d[2], d[3], d[4])) : "m" == f && 7 == h && r.add(d[1], d[2], d[3], d[4], d[5], d[6])
                    }
                    return r
                }

                function h(e) {
                    var t = e.node.ownerSVGElement && w(e.node.ownerSVGElement) || e.node.parentNode && w(e.node.parentNode) || n.select("svg") || n(0, 0), i = t.select("defs"), r = null == i ? !1 : i.node;
                    return r || (r = x("defs", t.node).node), r
                }

                function f(e) {
                    return e.node.ownerSVGElement && w(e.node.ownerSVGElement) || n.select("svg")
                }

                function m(e, t, n) {
                    function r(e) {
                        if (null == e)return D;
                        if (e == +e)return e;
                        i(l, {width: e});
                        try {
                            return l.getBBox().width
                        } catch (t) {
                            return 0
                        }
                    }

                    function o(e) {
                        if (null == e)return D;
                        if (e == +e)return e;
                        i(l, {height: e});
                        try {
                            return l.getBBox().height
                        } catch (t) {
                            return 0
                        }
                    }

                    function a(i, r) {
                        null == t ? c[i] = r(e.attr(i) || 0) : i == t && (c = r(null == n ? e.attr(i) || 0 : n))
                    }

                    var s = f(e).node, c = {}, l = s.querySelector(".svg---mgr");
                    switch (l || (l = i("rect"), i(l, {
                        x: -9e9,
                        y: -9e9,
                        width: 10,
                        height: 10,
                        "class": "svg---mgr",
                        fill: "none"
                    }), s.appendChild(l)), e.type) {
                        case"rect":
                            a("rx", r), a("ry", o);
                        case"image":
                            a("width", r), a("height", o);
                        case"text":
                            a("x", r), a("y", o);
                            break;
                        case"circle":
                            a("cx", r), a("cy", o), a("r", r);
                            break;
                        case"ellipse":
                            a("cx", r), a("cy", o), a("rx", r), a("ry", o);
                            break;
                        case"line":
                            a("x1", r), a("x2", r), a("y1", o), a("y2", o);
                            break;
                        case"marker":
                            a("refX", r), a("markerWidth", r), a("refY", o), a("markerHeight", o);
                            break;
                        case"radialGradient":
                            a("fx", r), a("fy", o);
                            break;
                        case"tspan":
                            a("dx", r), a("dy", o);
                            break;
                        default:
                            a(t, r)
                    }
                    return s.removeChild(l), c
                }

                function v(e) {
                    r(e, "array") || (e = Array.prototype.slice.call(arguments, 0));
                    for (var t = 0, n = 0, i = this.node; this[t];)delete this[t++];
                    for (t = 0; t < e.length; t++)"set" == e[t].type ? e[t].forEach(function (e) {
                        i.appendChild(e.node)
                    }) : i.appendChild(e[t].node);
                    var o = i.childNodes;
                    for (t = 0; t < o.length; t++)this[n++] = w(o[t]);
                    return this
                }

                function y(e) {
                    if (e.snap in W)return W[e.snap];
                    var t;
                    try {
                        t = e.ownerSVGElement
                    } catch (n) {
                    }
                    this.node = e, t && (this.paper = new E(t)), this.type = e.tagName;
                    var i = this.id = H(this);
                    if (this.anims = {}, this._ = {transform: []}, e.snap = i, W[i] = this, "g" == this.type && (this.add = v), this.type in{
                            g: 1,
                            mask: 1,
                            pattern: 1,
                            symbol: 1
                        })for (var r in E.prototype)E.prototype[S](r) && (this[r] = E.prototype[r])
                }

                function b(e) {
                    this.node = e
                }

                function x(e, t) {
                    var n = i(e);
                    t.appendChild(n);
                    var r = w(n);
                    return r
                }

                function E(e, t) {
                    var n, r, o, a = E.prototype;
                    if (e && "svg" == e.tagName) {
                        if (e.snap in W)return W[e.snap];
                        var s = e.ownerDocument;
                        n = new y(e), r = e.getElementsByTagName("desc")[0], o = e.getElementsByTagName("defs")[0], r || (r = i("desc"), r.appendChild(s.createTextNode("Created with Snap")), n.node.appendChild(r)), o || (o = i("defs"), n.node.appendChild(o)), n.defs = o;
                        for (var c in a)a[S](c) && (n[c] = a[c]);
                        n.paper = n.root = n
                    } else n = x("svg", _.doc.body), i(n.node, {height: t, version: 1.1, width: e, xmlns: G});
                    return n
                }

                function w(e) {
                    return e ? e instanceof y || e instanceof b ? e : e.tagName && "svg" == e.tagName.toLowerCase() ? new E(e) : e.tagName && "object" == e.tagName.toLowerCase() && "image/svg+xml" == e.type ? new E(e.contentDocument.getElementsByTagName("svg")[0]) : new y(e) : e
                }

                n.version = "0.3.0", n.toString = function () {
                    return "Snap v" + this.version
                }, n._ = {};
                var _ = {win: e.window, doc: e.window.document};
                n._.glob = _;
                var S = "hasOwnProperty", C = String, A = parseFloat, T = parseInt, j = Math, N = j.max, k = j.min, R = j.abs, M = (j.pow, j.PI), D = (j.round, ""), P = Object.prototype.toString, B = /^\s*((#[a-f\d]{6})|(#[a-f\d]{3})|rgba?\(\s*([\d\.]+%?\s*,\s*[\d\.]+%?\s*,\s*[\d\.]+%?(?:\s*,\s*[\d\.]+%?)?)\s*\)|hsba?\(\s*([\d\.]+(?:deg|\xb0|%)?\s*,\s*[\d\.]+%?\s*,\s*[\d\.]+(?:%?\s*,\s*[\d\.]+)?%?)\s*\)|hsla?\(\s*([\d\.]+(?:deg|\xb0|%)?\s*,\s*[\d\.]+%?\s*,\s*[\d\.]+(?:%?\s*,\s*[\d\.]+)?%?)\s*\))\s*$/i, L = (n._.separator = /[,\s]+/, /[\s]*,[\s]*/), O = {
                    hs: 1,
                    rg: 1
                }, I = /([a-z])[\s,]*((-?\d*\.?\d*(?:e[\-+]?\d+)?[\s]*,?[\s]*)+)/gi, F = /([rstm])[\s,]*((-?\d*\.?\d*(?:e[\-+]?\d+)?[\s]*,?[\s]*)+)/gi, U = /(-?\d*\.?\d*(?:e[\-+]?\\d+)?)[\s]*,?[\s]*/gi, z = 0, $ = "S" + (+new Date).toString(36), H = function (e) {
                    return (e && e.type ? e.type : D) + $ + (z++).toString(36)
                }, q = "http://www.w3.org/1999/xlink", G = "http://www.w3.org/2000/svg", W = {};
                n.url = function (e) {
                    return "url('#" + e + "')"
                };
                n._.$ = i, n._.id = H, n.format = function () {
                    var e = /\{([^\}]+)\}/g, t = /(?:(?:^|\.)(.+?)(?=\[|\.|$|\()|\[('|")(.+?)\2\])(\(\))?/g, n = function (e, n, i) {
                        var r = i;
                        return n.replace(t, function (e, t, n, i, o) {
                            t = t || i, r && (t in r && (r = r[t]), "function" == typeof r && o && (r = r()))
                        }), r = (null == r || r == i ? e : r) + ""
                    };
                    return function (t, i) {
                        return C(t).replace(e, function (e, t) {
                            return n(e, t, i)
                        })
                    }
                }(), n._.clone = o, n._.cacher = s, n.rad = l, n.deg = u, n.angle = c, n.is = r, n.snapTo = function (e, t, n) {
                    if (n = r(n, "finite") ? n : 10, r(e, "array")) {
                        for (var i = e.length; i--;)if (R(e[i] - t) <= n)return e[i]
                    } else {
                        e = +e;
                        var o = t % e;
                        if (n > o)return t - o;
                        if (o > e - n)return t - o + e
                    }
                    return t
                }, n.getRGB = s(function (e) {
                    if (!e || (e = C(e)).indexOf("-") + 1)return {
                        r: -1,
                        g: -1,
                        b: -1,
                        hex: "none",
                        error: 1,
                        toString: K
                    };
                    if ("none" == e)return {r: -1, g: -1, b: -1, hex: "none", toString: K};
                    if (!(O[S](e.toLowerCase().substring(0, 2)) || "#" == e.charAt()) && (e = V(e)), !e)return {
                        r: -1,
                        g: -1,
                        b: -1,
                        hex: "none",
                        error: 1,
                        toString: K
                    };
                    var t, i, o, a, s, c, l = e.match(B);
                    return l ? (l[2] && (o = T(l[2].substring(5), 16), i = T(l[2].substring(3, 5), 16), t = T(l[2].substring(1, 3), 16)), l[3] && (o = T((s = l[3].charAt(3)) + s, 16), i = T((s = l[3].charAt(2)) + s, 16), t = T((s = l[3].charAt(1)) + s, 16)), l[4] && (c = l[4].split(L), t = A(c[0]), "%" == c[0].slice(-1) && (t *= 2.55), i = A(c[1]), "%" == c[1].slice(-1) && (i *= 2.55), o = A(c[2]), "%" == c[2].slice(-1) && (o *= 2.55), "rgba" == l[1].toLowerCase().slice(0, 4) && (a = A(c[3])), c[3] && "%" == c[3].slice(-1) && (a /= 100)), l[5] ? (c = l[5].split(L), t = A(c[0]), "%" == c[0].slice(-1) && (t /= 100), i = A(c[1]), "%" == c[1].slice(-1) && (i /= 100), o = A(c[2]), "%" == c[2].slice(-1) && (o /= 100), ("deg" == c[0].slice(-3) || "°" == c[0].slice(-1)) && (t /= 360), "hsba" == l[1].toLowerCase().slice(0, 4) && (a = A(c[3])), c[3] && "%" == c[3].slice(-1) && (a /= 100), n.hsb2rgb(t, i, o, a)) : l[6] ? (c = l[6].split(L), t = A(c[0]), "%" == c[0].slice(-1) && (t /= 100), i = A(c[1]), "%" == c[1].slice(-1) && (i /= 100), o = A(c[2]), "%" == c[2].slice(-1) && (o /= 100), ("deg" == c[0].slice(-3) || "°" == c[0].slice(-1)) && (t /= 360), "hsla" == l[1].toLowerCase().slice(0, 4) && (a = A(c[3])), c[3] && "%" == c[3].slice(-1) && (a /= 100), n.hsl2rgb(t, i, o, a)) : (t = k(j.round(t), 255), i = k(j.round(i), 255), o = k(j.round(o), 255), a = k(N(a, 0), 1), l = {
                        r: t,
                        g: i,
                        b: o,
                        toString: K
                    }, l.hex = "#" + (16777216 | o | i << 8 | t << 16).toString(16).slice(1), l.opacity = r(a, "finite") ? a : 1, l)) : {
                        r: -1,
                        g: -1,
                        b: -1,
                        hex: "none",
                        error: 1,
                        toString: K
                    }
                }, n), n.hsb = s(function (e, t, i) {
                    return n.hsb2rgb(e, t, i).hex
                }), n.hsl = s(function (e, t, i) {
                    return n.hsl2rgb(e, t, i).hex
                }), n.rgb = s(function (e, t, n, i) {
                    if (r(i, "finite")) {
                        var o = j.round;
                        return "rgba(" + [o(e), o(t), o(n), +i.toFixed(2)] + ")"
                    }
                    return "#" + (16777216 | n | t << 8 | e << 16).toString(16).slice(1)
                });
                var V = function (e) {
                    var t = _.doc.getElementsByTagName("head")[0] || _.doc.getElementsByTagName("svg")[0], n = "rgb(255, 0, 0)";
                    return (V = s(function (e) {
                        if ("red" == e.toLowerCase())return n;
                        t.style.color = n, t.style.color = e;
                        var i = _.doc.defaultView.getComputedStyle(t, D).getPropertyValue("color");
                        return i == n ? null : i
                    }))(e)
                }, Y = function () {
                    return "hsb(" + [this.h, this.s, this.b] + ")"
                }, X = function () {
                    return "hsl(" + [this.h, this.s, this.l] + ")"
                }, K = function () {
                    return 1 == this.opacity || null == this.opacity ? this.hex : "rgba(" + [this.r, this.g, this.b, this.opacity] + ")"
                }, Z = function (e, t, i) {
                    if (null == t && r(e, "object") && "r"in e && "g"in e && "b"in e && (i = e.b, t = e.g, e = e.r), null == t && r(e, string)) {
                        var o = n.getRGB(e);
                        e = o.r, t = o.g, i = o.b
                    }
                    return (e > 1 || t > 1 || i > 1) && (e /= 255, t /= 255, i /= 255), [e, t, i]
                }, Q = function (e, t, i, o) {
                    e = j.round(255 * e), t = j.round(255 * t), i = j.round(255 * i);
                    var a = {r: e, g: t, b: i, opacity: r(o, "finite") ? o : 1, hex: n.rgb(e, t, i), toString: K};
                    return r(o, "finite") && (a.opacity = o), a
                };
                n.color = function (e) {
                    var t;
                    return r(e, "object") && "h"in e && "s"in e && "b"in e ? (t = n.hsb2rgb(e), e.r = t.r, e.g = t.g, e.b = t.b, e.opacity = 1, e.hex = t.hex) : r(e, "object") && "h"in e && "s"in e && "l"in e ? (t = n.hsl2rgb(e), e.r = t.r, e.g = t.g, e.b = t.b, e.opacity = 1, e.hex = t.hex) : (r(e, "string") && (e = n.getRGB(e)), r(e, "object") && "r"in e && "g"in e && "b"in e && !("error"in e) ? (t = n.rgb2hsl(e), e.h = t.h, e.s = t.s, e.l = t.l, t = n.rgb2hsb(e), e.v = t.b) : (e = {hex: "none"}, e.r = e.g = e.b = e.h = e.s = e.v = e.l = -1, e.error = 1)), e.toString = K, e
                }, n.hsb2rgb = function (e, t, n, i) {
                    r(e, "object") && "h"in e && "s"in e && "b"in e && (n = e.b, t = e.s, e = e.h, i = e.o), e *= 360;
                    var o, a, s, c, l;
                    return e = e % 360 / 60, l = n * t, c = l * (1 - R(e % 2 - 1)), o = a = s = n - l, e = ~~e, o += [l, c, 0, 0, c, l][e], a += [c, l, l, c, 0, 0][e], s += [0, 0, c, l, l, c][e], Q(o, a, s, i)
                }, n.hsl2rgb = function (e, t, n, i) {
                    r(e, "object") && "h"in e && "s"in e && "l"in e && (n = e.l, t = e.s, e = e.h), (e > 1 || t > 1 || n > 1) && (e /= 360, t /= 100, n /= 100), e *= 360;
                    var o, a, s, c, l;
                    return e = e % 360 / 60, l = 2 * t * (.5 > n ? n : 1 - n), c = l * (1 - R(e % 2 - 1)), o = a = s = n - l / 2, e = ~~e, o += [l, c, 0, 0, c, l][e], a += [c, l, l, c, 0, 0][e], s += [0, 0, c, l, l, c][e], Q(o, a, s, i)
                }, n.rgb2hsb = function (e, t, n) {
                    n = Z(e, t, n), e = n[0], t = n[1], n = n[2];
                    var i, r, o, a;
                    return o = N(e, t, n), a = o - k(e, t, n), i = 0 == a ? null : o == e ? (t - n) / a : o == t ? (n - e) / a + 2 : (e - t) / a + 4, i = (i + 360) % 6 * 60 / 360, r = 0 == a ? 0 : a / o, {
                        h: i,
                        s: r,
                        b: o,
                        toString: Y
                    }
                }, n.rgb2hsl = function (e, t, n) {
                    n = Z(e, t, n), e = n[0], t = n[1], n = n[2];
                    var i, r, o, a, s, c;
                    return a = N(e, t, n), s = k(e, t, n), c = a - s, i = 0 == c ? null : a == e ? (t - n) / c : a == t ? (n - e) / c + 2 : (e - t) / c + 4, i = (i + 360) % 6 * 60 / 360, o = (a + s) / 2, r = 0 == c ? 0 : .5 > o ? c / (2 * o) : c / (2 - 2 * o), {
                        h: i,
                        s: r,
                        l: o,
                        toString: X
                    }
                }, n.parsePathString = function (e) {
                    if (!e)return null;
                    var t = n.path(e);
                    if (t.arr)return n.path.clone(t.arr);
                    var i = {a: 7, c: 6, o: 2, h: 1, l: 2, m: 2, r: 4, q: 4, s: 4, t: 2, v: 1, u: 3, z: 0}, o = [];
                    return r(e, "array") && r(e[0], "array") && (o = n.path.clone(e)), o.length || C(e).replace(I, function (e, t, n) {
                        var r = [], a = t.toLowerCase();
                        if (n.replace(U, function (e, t) {
                                t && r.push(+t)
                            }), "m" == a && r.length > 2 && (o.push([t].concat(r.splice(0, 2))), a = "l", t = "m" == t ? "l" : "L"), "o" == a && 1 == r.length && o.push([t, r[0]]), "r" == a)o.push([t].concat(r)); else for (; r.length >= i[a] && (o.push([t].concat(r.splice(0, i[a]))), i[a]););
                    }), o.toString = n.path.toString, t.arr = n.path.clone(o), o
                };
                var J = n.parseTransformString = function (e) {
                    if (!e)return null;
                    var t = [];
                    return r(e, "array") && r(e[0], "array") && (t = n.path.clone(e)), t.length || C(e).replace(F, function (e, n, i) {
                        var r = [];
                        n.toLowerCase();
                        i.replace(U, function (e, t) {
                            t && r.push(+t)
                        }), t.push([n].concat(r))
                    }), t.toString = n.path.toString, t
                };
                n._.svgTransform2string = p, n._.rgTransform = /^[a-z][\s]*-?\.?\d/i, n._.transform2matrix = d, n._unit2px = m;
                _.doc.contains || _.doc.compareDocumentPosition ? function (e, t) {
                    var n = 9 == e.nodeType ? e.documentElement : e, i = t && t.parentNode;
                    return e == i || !(!i || 1 != i.nodeType || !(n.contains ? n.contains(i) : e.compareDocumentPosition && 16 & e.compareDocumentPosition(i)))
                } : function (e, t) {
                    if (t)for (; t;)if (t = t.parentNode, t == e)return !0;
                    return !1
                };
                n._.getSomeDefs = h, n._.getSomeSVG = f, n.select = function (e) {
                    return e = C(e).replace(/([^\\]):/g, "$1\\:"), w(_.doc.querySelector(e))
                }, n.selectAll = function (e) {
                    for (var t = _.doc.querySelectorAll(e), i = (n.set || Array)(), r = 0; r < t.length; r++)i.push(w(t[r]));
                    return i
                }, setInterval(function () {
                    for (var e in W)if (W[S](e)) {
                        var t = W[e], n = t.node;
                        ("svg" != t.type && !n.ownerSVGElement || "svg" == t.type && (!n.parentNode || "ownerSVGElement"in n.parentNode && !n.ownerSVGElement)) && delete W[e]
                    }
                }, 1e4), y.prototype.attr = function (e, n) {
                    var i = this;
                    i.node;
                    if (!e)return i;
                    if (r(e, "string")) {
                        if (!(arguments.length > 1))return t("snap.util.getattr." + e, i).firstDefined();
                        var o = {};
                        o[e] = n, e = o
                    }
                    for (var a in e)e[S](a) && t("snap.util.attr." + a, i, e[a]);
                    return i
                }, n.parse = function (e) {
                    var t = _.doc.createDocumentFragment(), n = !0, i = _.doc.createElement("div");
                    if (e = C(e), e.match(/^\s*<\s*svg(?:\s|>)/) || (e = "<svg>" + e + "</svg>", n = !1), i.innerHTML = e, e = i.getElementsByTagName("svg")[0])if (n)t = e; else {
                        for (; e.firstChild;)t.appendChild(e.firstChild);
                        i.innerHTML = D
                    }
                    return new b(t)
                }, n.fragment = function () {
                    for (var e = Array.prototype.slice.call(arguments, 0), t = _.doc.createDocumentFragment(), i = 0, r = e.length; r > i; i++) {
                        var o = e[i];
                        o.node && o.node.nodeType && t.appendChild(o.node), o.nodeType && t.appendChild(o), "string" == typeof o && t.appendChild(n.parse(o).node)
                    }
                    return new b(t)
                }, n._.make = x, n._.wrap = w, E.prototype.el = function (e, t) {
                    var n = x(e, this.node);
                    return t && n.attr(t), n
                }, t.on("snap.util.getattr", function () {
                    var e = t.nt();
                    e = e.substring(e.lastIndexOf(".") + 1);
                    var n = e.replace(/[A-Z]/g, function (e) {
                        return "-" + e.toLowerCase()
                    });
                    return ee[S](n) ? this.node.ownerDocument.defaultView.getComputedStyle(this.node, null).getPropertyValue(n) : i(this.node, e)
                });
                var ee = {
                    "alignment-baseline": 0,
                    "baseline-shift": 0,
                    clip: 0,
                    "clip-path": 0,
                    "clip-rule": 0,
                    color: 0,
                    "color-interpolation": 0,
                    "color-interpolation-filters": 0,
                    "color-profile": 0,
                    "color-rendering": 0,
                    cursor: 0,
                    direction: 0,
                    display: 0,
                    "dominant-baseline": 0,
                    "enable-background": 0,
                    fill: 0,
                    "fill-opacity": 0,
                    "fill-rule": 0,
                    filter: 0,
                    "flood-color": 0,
                    "flood-opacity": 0,
                    font: 0,
                    "font-family": 0,
                    "font-size": 0,
                    "font-size-adjust": 0,
                    "font-stretch": 0,
                    "font-style": 0,
                    "font-variant": 0,
                    "font-weight": 0,
                    "glyph-orientation-horizontal": 0,
                    "glyph-orientation-vertical": 0,
                    "image-rendering": 0,
                    kerning: 0,
                    "letter-spacing": 0,
                    "lighting-color": 0,
                    marker: 0,
                    "marker-end": 0,
                    "marker-mid": 0,
                    "marker-start": 0,
                    mask: 0,
                    opacity: 0,
                    overflow: 0,
                    "pointer-events": 0,
                    "shape-rendering": 0,
                    "stop-color": 0,
                    "stop-opacity": 0,
                    stroke: 0,
                    "stroke-dasharray": 0,
                    "stroke-dashoffset": 0,
                    "stroke-linecap": 0,
                    "stroke-linejoin": 0,
                    "stroke-miterlimit": 0,
                    "stroke-opacity": 0,
                    "stroke-width": 0,
                    "text-anchor": 0,
                    "text-decoration": 0,
                    "text-rendering": 0,
                    "unicode-bidi": 0,
                    visibility: 0,
                    "word-spacing": 0,
                    "writing-mode": 0
                };
                t.on("snap.util.attr", function (e) {
                    var n = t.nt(), r = {};
                    n = n.substring(n.lastIndexOf(".") + 1), r[n] = e;
                    var o = n.replace(/-(\w)/gi, function (e, t) {
                        return t.toUpperCase()
                    }), a = n.replace(/[A-Z]/g, function (e) {
                        return "-" + e.toLowerCase()
                    });
                    ee[S](a) ? this.node.style[o] = null == e ? D : e : i(this.node, r)
                }), function (e) {
                }(E.prototype), n.ajax = function (e, n, i, o) {
                    var a = new XMLHttpRequest, s = H();
                    if (a) {
                        if (r(n, "function"))o = i, i = n, n = null; else if (r(n, "object")) {
                            var c = [];
                            for (var l in n)n.hasOwnProperty(l) && c.push(encodeURIComponent(l) + "=" + encodeURIComponent(n[l]));
                            n = c.join("&")
                        }
                        return a.open(n ? "POST" : "GET", e, !0), n && (a.setRequestHeader("X-Requested-With", "XMLHttpRequest"), a.setRequestHeader("Content-type", "application/x-www-form-urlencoded")), i && (t.once("snap.ajax." + s + ".0", i), t.once("snap.ajax." + s + ".200", i), t.once("snap.ajax." + s + ".304", i)), a.onreadystatechange = function () {
                            4 == a.readyState && t("snap.ajax." + s + "." + a.status, o, a)
                        }, 4 == a.readyState ? a : (a.send(n), a)
                    }
                }, n.load = function (e, t, i) {
                    n.ajax(e, function (e) {
                        var r = n.parse(e.responseText);
                        i ? t.call(i, r) : t(r)
                    })
                };
                var te = function (e) {
                    var t = e.getBoundingClientRect(), n = e.ownerDocument, i = n.body, r = n.documentElement, o = r.clientTop || i.clientTop || 0, a = r.clientLeft || i.clientLeft || 0, s = t.top + (g.win.pageYOffset || r.scrollTop || i.scrollTop) - o, c = t.left + (g.win.pageXOffset || r.scrollLeft || i.scrollLeft) - a;
                    return {y: s, x: c}
                };
                return n.getElementByPoint = function (e, t) {
                    var n = this, i = (n.canvas, _.doc.elementFromPoint(e, t));
                    if (_.win.opera && "svg" == i.tagName) {
                        var r = te(i), o = i.createSVGRect();
                        o.x = e - r.x, o.y = t - r.y, o.width = o.height = 1;
                        var a = i.getIntersectionList(o, null);
                        a.length && (i = a[a.length - 1])
                    }
                    return i ? w(i) : null
                }, n.plugin = function (e) {
                    e(n, y, E, _, b)
                }, _.win.Snap = n, n
            }(e || this);
            return i.plugin(function (i, r, o, a, s) {
                function c(e, t) {
                    if (null == t) {
                        var n = !0;
                        if (t = "linearGradient" == e.type || "radialGradient" == e.type ? e.node.getAttribute("gradientTransform") : "pattern" == e.type ? e.node.getAttribute("patternTransform") : e.node.getAttribute("transform"), !t)return new i.Matrix;
                        t = i._.svgTransform2string(t)
                    } else t = i._.rgTransform.test(t) ? f(t).replace(/\.{3}|\u2026/g, e._.transform || E) : i._.svgTransform2string(t), h(t, "array") && (t = i.path ? i.path.toString.call(t) : f(t)), e._.transform = t;
                    var r = i._.transform2matrix(t, e.getBBox(1));
                    return n ? r : void(e.matrix = r)
                }

                function l(e) {
                    function t(e, t) {
                        var n = g(e.node, t);
                        n = n && n.match(o), n = n && n[2], n && "#" == n.charAt() && (n = n.substring(1), n && (s[n] = (s[n] || []).concat(function (n) {
                            var i = {};
                            i[t] = URL(n), g(e.node, i)
                        })))
                    }

                    function n(e) {
                        var t = g(e.node, "xlink:href");
                        t && "#" == t.charAt() && (t = t.substring(1), t && (s[t] = (s[t] || []).concat(function (t) {
                            e.attr("xlink:href", "#" + t)
                        })))
                    }

                    for (var i, r = e.selectAll("*"), o = /^\s*url\(("|'|)(.*)\1\)\s*$/, a = [], s = {}, c = 0, l = r.length; l > c; c++) {
                        i = r[c], t(i, "fill"), t(i, "stroke"), t(i, "filter"), t(i, "mask"), t(i, "clip-path"), n(i);
                        var u = g(i.node, "id");
                        u && (g(i.node, {id: i.id}), a.push({old: u, id: i.id}))
                    }
                    for (c = 0, l = a.length; l > c; c++) {
                        var p = s[a[c].old];
                        if (p)for (var d = 0, h = p.length; h > d; d++)p[d](a[c].id)
                    }
                }

                function u(e, t, n) {
                    return function (i) {
                        var r = i.slice(e, t);
                        return 1 == r.length && (r = r[0]), n ? n(r) : r
                    }
                }

                function p(e) {
                    return function () {
                        var t = e ? "<" + this.type : "", n = this.node.attributes, i = this.node.childNodes;
                        if (e)for (var r = 0, o = n.length; o > r; r++)t += " " + n[r].name + '="' + n[r].value.replace(/"/g, '\\"') + '"';
                        if (i.length) {
                            for (e && (t += ">"), r = 0, o = i.length; o > r; r++)3 == i[r].nodeType ? t += i[r].nodeValue : 1 == i[r].nodeType && (t += x(i[r]).toString());
                            e && (t += "</" + this.type + ">")
                        } else e && (t += "/>");
                        return t
                    }
                }

                var d = r.prototype, h = i.is, f = String, m = i._unit2px, g = i._.$, v = i._.make, y = i._.getSomeDefs, b = "hasOwnProperty", x = i._.wrap;
                d.getBBox = function (e) {
                    if (!i.Matrix || !i.path)return this.node.getBBox();
                    var t = this, n = new i.Matrix;
                    if (t.removed)return i._.box();
                    for (; "use" == t.type;)if (e || (n = n.add(t.transform().localMatrix.translate(t.attr("x") || 0, t.attr("y") || 0))), t.original)t = t.original; else {
                        var r = t.attr("xlink:href");
                        t = t.original = t.node.ownerDocument.getElementById(r.substring(r.indexOf("#") + 1))
                    }
                    var o = t._, a = i.path.get[t.type] || i.path.get.deflt;
                    try {
                        return e ? (o.bboxwt = a ? i.path.getBBox(t.realPath = a(t)) : i._.box(t.node.getBBox()), i._.box(o.bboxwt)) : (t.realPath = a(t), t.matrix = t.transform().localMatrix, o.bbox = i.path.getBBox(i.path.map(t.realPath, n.add(t.matrix))), i._.box(o.bbox))
                    } catch (s) {
                        return i._.box()
                    }
                };
                var w = function () {
                    return this.string
                };
                d.transform = function (e) {
                    var t = this._;
                    if (null == e) {
                        for (var n, r = this, o = new i.Matrix(this.node.getCTM()), a = c(this), s = [a], l = new i.Matrix, u = a.toTransformString(), p = f(a) == f(this.matrix) ? f(t.transform) : u; "svg" != r.type && (r = r.parent());)s.push(c(r));
                        for (n = s.length; n--;)l.add(s[n]);
                        return {
                            string: p,
                            globalMatrix: o,
                            totalMatrix: l,
                            localMatrix: a,
                            diffMatrix: o.clone().add(a.invert()),
                            global: o.toTransformString(),
                            total: l.toTransformString(),
                            local: u,
                            toString: w
                        }
                    }
                    return e instanceof i.Matrix ? (this.matrix = e, this._.transform = e.toTransformString()) : c(this, e), this.node && ("linearGradient" == this.type || "radialGradient" == this.type ? g(this.node, {gradientTransform: this.matrix}) : "pattern" == this.type ? g(this.node, {patternTransform: this.matrix}) : g(this.node, {transform: this.matrix})), this
                }, d.parent = function () {
                    return x(this.node.parentNode)
                }, d.append = d.add = function (e) {
                    if (e) {
                        if ("set" == e.type) {
                            var t = this;
                            return e.forEach(function (e) {
                                t.add(e)
                            }), this
                        }
                        e = x(e), this.node.appendChild(e.node), e.paper = this.paper
                    }
                    return this
                }, d.appendTo = function (e) {
                    return e && (e = x(e), e.append(this)), this
                }, d.prepend = function (e) {
                    if (e) {
                        if ("set" == e.type) {
                            var t, n = this;
                            return e.forEach(function (e) {
                                t ? t.after(e) : n.prepend(e), t = e
                            }), this
                        }
                        e = x(e);
                        var i = e.parent();
                        this.node.insertBefore(e.node, this.node.firstChild), this.add && this.add(), e.paper = this.paper, this.parent() && this.parent().add(), i && i.add()
                    }
                    return this
                }, d.prependTo = function (e) {
                    return e = x(e), e.prepend(this), this
                }, d.before = function (e) {
                    if ("set" == e.type) {
                        var t = this;
                        return e.forEach(function (e) {
                            var n = e.parent();
                            t.node.parentNode.insertBefore(e.node, t.node), n && n.add()
                        }), this.parent().add(), this
                    }
                    e = x(e);
                    var n = e.parent();
                    return this.node.parentNode.insertBefore(e.node, this.node), this.parent() && this.parent().add(), n && n.add(), e.paper = this.paper, this
                }, d.after = function (e) {
                    e = x(e);
                    var t = e.parent();
                    return this.node.nextSibling ? this.node.parentNode.insertBefore(e.node, this.node.nextSibling) : this.node.parentNode.appendChild(e.node), this.parent() && this.parent().add(), t && t.add(), e.paper = this.paper, this
                }, d.insertBefore = function (e) {
                    e = x(e);
                    var t = this.parent();
                    return e.node.parentNode.insertBefore(this.node, e.node), this.paper = e.paper, t && t.add(), e.parent() && e.parent().add(), this
                }, d.insertAfter = function (e) {
                    e = x(e);
                    var t = this.parent();
                    return e.node.parentNode.insertBefore(this.node, e.node.nextSibling), this.paper = e.paper, t && t.add(), e.parent() && e.parent().add(), this
                }, d.remove = function () {
                    var e = this.parent();
                    return this.node.parentNode && this.node.parentNode.removeChild(this.node), delete this.paper, this.removed = !0, e && e.add(), this
                }, d.select = function (e) {
                    return e = f(e).replace(/([^\\]):/g, "$1\\:"), x(this.node.querySelector(e))
                }, d.selectAll = function (e) {
                    for (var t = this.node.querySelectorAll(e), n = (i.set || Array)(), r = 0; r < t.length; r++)n.push(x(t[r]));
                    return n
                }, d.asPX = function (e, t) {
                    return null == t && (t = this.attr(e)), +m(this, e, t)
                }, d.use = function () {
                    var e, t = this.node.id;
                    return t || (t = this.id, g(this.node, {id: t})), e = "linearGradient" == this.type || "radialGradient" == this.type || "pattern" == this.type ? v(this.type, this.node.parentNode) : v("use", this.node.parentNode), g(e.node, {"xlink:href": "#" + t}), e.original = this, e
                }, d.clone = function () {
                    var e = x(this.node.cloneNode(!0));
                    return g(e.node, "id") && g(e.node, {id: e.id}), l(e), e.insertAfter(this), e
                }, d.toDefs = function () {
                    var e = y(this);
                    return e.appendChild(this.node), this
                }, d.pattern = d.toPattern = function (e, t, n, i) {
                    var r = v("pattern", y(this));
                    return null == e && (e = this.getBBox()), h(e, "object") && "x"in e && (t = e.y, n = e.width, i = e.height, e = e.x), g(r.node, {
                        x: e,
                        y: t,
                        width: n,
                        height: i,
                        patternUnits: "userSpaceOnUse",
                        id: r.id,
                        viewBox: [e, t, n, i].join(" ")
                    }), r.node.appendChild(this.node), r
                }, d.marker = function (e, t, n, i, r, o) {
                    var a = v("marker", y(this));
                    return null == e && (e = this.getBBox()), h(e, "object") && "x"in e && (t = e.y, n = e.width, i = e.height, r = e.refX || e.cx, o = e.refY || e.cy, e = e.x), g(a.node, {
                        viewBox: [e, t, n, i].join(" "),
                        markerWidth: n,
                        markerHeight: i,
                        orient: "auto",
                        refX: r || 0,
                        refY: o || 0,
                        id: a.id
                    }), a.node.appendChild(this.node), a
                };
                var _ = function (e, t, i, r) {
                    "function" != typeof i || i.length || (r = i, i = n.linear), this.attr = e, this.dur = t, i && (this.easing = i), r && (this.callback = r)
                };
                i._.Animation = _, i.animation = function (e, t, n, i) {
                    return new _(e, t, n, i);
                }, d.inAnim = function () {
                    var e = this, t = [];
                    for (var n in e.anims)e.anims[b](n) && !function (e) {
                        t.push({
                            anim: new _(e._attrs, e.dur, e.easing, e._callback),
                            mina: e,
                            curStatus: e.status(),
                            status: function (t) {
                                return e.status(t)
                            },
                            stop: function () {
                                e.stop()
                            }
                        })
                    }(e.anims[n]);
                    return t
                }, i.animate = function (e, i, r, o, a, s) {
                    "function" != typeof a || a.length || (s = a, a = n.linear);
                    var c = n.time(), l = n(e, i, c, c + o, n.time, r, a);
                    return s && t.once("mina.finish." + l.id, s), l
                }, d.stop = function () {
                    for (var e = this.inAnim(), t = 0, n = e.length; n > t; t++)e[t].stop();
                    return this
                }, d.animate = function (e, i, r, o) {
                    "function" != typeof r || r.length || (o = r, r = n.linear), e instanceof _ && (o = e.callback, r = e.easing, i = r.dur, e = e.attr);
                    var a, s, c, l, p = [], d = [], m = {}, g = this;
                    for (var v in e)if (e[b](v)) {
                        g.equal ? (l = g.equal(v, f(e[v])), a = l.from, s = l.to, c = l.f) : (a = +g.attr(v), s = +e[v]);
                        var y = h(a, "array") ? a.length : 1;
                        m[v] = u(p.length, p.length + y, c), p = p.concat(a), d = d.concat(s)
                    }
                    var x = n.time(), E = n(p, d, x, x + i, n.time, function (e) {
                        var t = {};
                        for (var n in m)m[b](n) && (t[n] = m[n](e));
                        g.attr(t)
                    }, r);
                    return g.anims[E.id] = E, E._attrs = e, E._callback = o, t("snap.animcreated." + g.id, E), t.once("mina.finish." + E.id, function () {
                        delete g.anims[E.id], o && o.call(g)
                    }), t.once("mina.stop." + E.id, function () {
                        delete g.anims[E.id]
                    }), g
                };
                var S = {};
                d.data = function (e, n) {
                    var r = S[this.id] = S[this.id] || {};
                    if (0 == arguments.length)return t("snap.data.get." + this.id, this, r, null), r;
                    if (1 == arguments.length) {
                        if (i.is(e, "object")) {
                            for (var o in e)e[b](o) && this.data(o, e[o]);
                            return this
                        }
                        return t("snap.data.get." + this.id, this, r[e], e), r[e]
                    }
                    return r[e] = n, t("snap.data.set." + this.id, this, n, e), this
                }, d.removeData = function (e) {
                    return null == e ? S[this.id] = {} : S[this.id] && delete S[this.id][e], this
                }, d.outerSVG = d.toString = p(1), d.innerSVG = p(), d.toDataURL = function () {
                    if (e && e.btoa) {
                        var t = this.getBBox(), n = i.format('<svg version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="{width}" height="{height}" viewBox="{x} {y} {width} {height}">{contents}</svg>', {
                            x: +t.x.toFixed(3),
                            y: +t.y.toFixed(3),
                            width: +t.width.toFixed(3),
                            height: +t.height.toFixed(3),
                            contents: this.outerSVG()
                        });
                        return "data:image/svg+xml;base64," + btoa(unescape(encodeURIComponent(n)))
                    }
                }, s.prototype.select = d.select, s.prototype.selectAll = d.selectAll
            }), i.plugin(function (e, t, n, i, r) {
                function o(e, t, n, i, r, o) {
                    return null == t && "[object SVGMatrix]" == a.call(e) ? (this.a = e.a, this.b = e.b, this.c = e.c, this.d = e.d, this.e = e.e, void(this.f = e.f)) : void(null != e ? (this.a = +e, this.b = +t, this.c = +n, this.d = +i, this.e = +r, this.f = +o) : (this.a = 1, this.b = 0, this.c = 0, this.d = 1, this.e = 0, this.f = 0))
                }

                var a = Object.prototype.toString, s = String, c = Math, l = "";
                !function (t) {
                    function n(e) {
                        return e[0] * e[0] + e[1] * e[1]
                    }

                    function i(e) {
                        var t = c.sqrt(n(e));
                        e[0] && (e[0] /= t), e[1] && (e[1] /= t)
                    }

                    t.add = function (e, t, n, i, r, a) {
                        var s, c, l, u, p = [[], [], []], d = [[this.a, this.c, this.e], [this.b, this.d, this.f], [0, 0, 1]], h = [[e, n, r], [t, i, a], [0, 0, 1]];
                        for (e && e instanceof o && (h = [[e.a, e.c, e.e], [e.b, e.d, e.f], [0, 0, 1]]), s = 0; 3 > s; s++)for (c = 0; 3 > c; c++) {
                            for (u = 0, l = 0; 3 > l; l++)u += d[s][l] * h[l][c];
                            p[s][c] = u
                        }
                        return this.a = p[0][0], this.b = p[1][0], this.c = p[0][1], this.d = p[1][1], this.e = p[0][2], this.f = p[1][2], this
                    }, t.invert = function () {
                        var e = this, t = e.a * e.d - e.b * e.c;
                        return new o(e.d / t, -e.b / t, -e.c / t, e.a / t, (e.c * e.f - e.d * e.e) / t, (e.b * e.e - e.a * e.f) / t)
                    }, t.clone = function () {
                        return new o(this.a, this.b, this.c, this.d, this.e, this.f)
                    }, t.translate = function (e, t) {
                        return this.add(1, 0, 0, 1, e, t)
                    }, t.scale = function (e, t, n, i) {
                        return null == t && (t = e), (n || i) && this.add(1, 0, 0, 1, n, i), this.add(e, 0, 0, t, 0, 0), (n || i) && this.add(1, 0, 0, 1, -n, -i), this
                    }, t.rotate = function (t, n, i) {
                        t = e.rad(t), n = n || 0, i = i || 0;
                        var r = +c.cos(t).toFixed(9), o = +c.sin(t).toFixed(9);
                        return this.add(r, o, -o, r, n, i), this.add(1, 0, 0, 1, -n, -i)
                    }, t.x = function (e, t) {
                        return e * this.a + t * this.c + this.e
                    }, t.y = function (e, t) {
                        return e * this.b + t * this.d + this.f
                    }, t.get = function (e) {
                        return +this[s.fromCharCode(97 + e)].toFixed(4)
                    }, t.toString = function () {
                        return "matrix(" + [this.get(0), this.get(1), this.get(2), this.get(3), this.get(4), this.get(5)].join() + ")"
                    }, t.offset = function () {
                        return [this.e.toFixed(4), this.f.toFixed(4)]
                    }, t.determinant = function () {
                        return this.a * this.d - this.b * this.c
                    }, t.split = function () {
                        var t = {};
                        t.dx = this.e, t.dy = this.f;
                        var r = [[this.a, this.c], [this.b, this.d]];
                        t.scalex = c.sqrt(n(r[0])), i(r[0]), t.shear = r[0][0] * r[1][0] + r[0][1] * r[1][1], r[1] = [r[1][0] - r[0][0] * t.shear, r[1][1] - r[0][1] * t.shear], t.scaley = c.sqrt(n(r[1])), i(r[1]), t.shear /= t.scaley, this.determinant() < 0 && (t.scalex = -t.scalex);
                        var o = -r[0][1], a = r[1][1];
                        return 0 > a ? (t.rotate = e.deg(c.acos(a)), 0 > o && (t.rotate = 360 - t.rotate)) : t.rotate = e.deg(c.asin(o)), t.isSimple = !(+t.shear.toFixed(9) || t.scalex.toFixed(9) != t.scaley.toFixed(9) && t.rotate), t.isSuperSimple = !+t.shear.toFixed(9) && t.scalex.toFixed(9) == t.scaley.toFixed(9) && !t.rotate, t.noRotation = !+t.shear.toFixed(9) && !t.rotate, t
                    }, t.toTransformString = function (e) {
                        var t = e || this.split();
                        return +t.shear.toFixed(9) ? "m" + [this.get(0), this.get(1), this.get(2), this.get(3), this.get(4), this.get(5)] : (t.scalex = +t.scalex.toFixed(4), t.scaley = +t.scaley.toFixed(4), t.rotate = +t.rotate.toFixed(4), (t.dx || t.dy ? "t" + [+t.dx.toFixed(4), +t.dy.toFixed(4)] : l) + (1 != t.scalex || 1 != t.scaley ? "s" + [t.scalex, t.scaley, 0, 0] : l) + (t.rotate ? "r" + [+t.rotate.toFixed(4), 0, 0] : l))
                    }
                }(o.prototype), e.Matrix = o, e.matrix = function (e, t, n, i, r, a) {
                    return new o(e, t, n, i, r, a)
                }
            }), i.plugin(function (e, n, i, r, o) {
                function a(i) {
                    return function (r) {
                        if (t.stop(), r instanceof o && 1 == r.node.childNodes.length && ("radialGradient" == r.node.firstChild.tagName || "linearGradient" == r.node.firstChild.tagName || "pattern" == r.node.firstChild.tagName) && (r = r.node.firstChild, h(this).appendChild(r), r = p(r)), r instanceof n)if ("radialGradient" == r.type || "linearGradient" == r.type || "pattern" == r.type) {
                            r.node.id || m(r.node, {id: r.id});
                            var a = g(r.node.id)
                        } else a = r.attr(i); else if (a = e.color(r), a.error) {
                            var s = e(h(this).ownerSVGElement).gradient(r);
                            s ? (s.node.id || m(s.node, {id: s.id}), a = g(s.node.id)) : a = r
                        } else a = v(a);
                        var c = {};
                        c[i] = a, m(this.node, c), this.node.style[i] = b
                    }
                }

                function s(e) {
                    t.stop(), e == +e && (e += "px"), this.node.style.fontSize = e
                }

                function c(e) {
                    for (var t = [], n = e.childNodes, i = 0, r = n.length; r > i; i++) {
                        var o = n[i];
                        3 == o.nodeType && t.push(o.nodeValue), "tspan" == o.tagName && (1 == o.childNodes.length && 3 == o.firstChild.nodeType ? t.push(o.firstChild.nodeValue) : t.push(c(o)))
                    }
                    return t
                }

                function l() {
                    return t.stop(), this.node.style.fontSize
                }

                var u = e._.make, p = e._.wrap, d = e.is, h = e._.getSomeDefs, f = /^url\(#?([^)]+)\)$/, m = e._.$, g = e.url, v = String, y = e._.separator, b = "";
                t.on("snap.util.attr.mask", function (e) {
                    if (e instanceof n || e instanceof o) {
                        if (t.stop(), e instanceof o && 1 == e.node.childNodes.length && (e = e.node.firstChild, h(this).appendChild(e), e = p(e)), "mask" == e.type)var i = e; else i = u("mask", h(this)), i.node.appendChild(e.node);
                        !i.node.id && m(i.node, {id: i.id}), m(this.node, {mask: g(i.id)})
                    }
                }), function (e) {
                    t.on("snap.util.attr.clip", e), t.on("snap.util.attr.clip-path", e), t.on("snap.util.attr.clipPath", e)
                }(function (e) {
                    if (e instanceof n || e instanceof o) {
                        if (t.stop(), "clipPath" == e.type)var i = e; else i = u("clipPath", h(this)), i.node.appendChild(e.node), !i.node.id && m(i.node, {id: i.id});
                        m(this.node, {"clip-path": g(i.node.id || i.id)})
                    }
                }), t.on("snap.util.attr.fill", a("fill")), t.on("snap.util.attr.stroke", a("stroke"));
                var x = /^([lr])(?:\(([^)]*)\))?(.*)$/i;
                t.on("snap.util.grad.parse", function (e) {
                    e = v(e);
                    var t = e.match(x);
                    if (!t)return null;
                    var n = t[1], i = t[2], r = t[3];
                    return i = i.split(/\s*,\s*/).map(function (e) {
                        return +e == e ? +e : e
                    }), 1 == i.length && 0 == i[0] && (i = []), r = r.split("-"), r = r.map(function (e) {
                        e = e.split(":");
                        var t = {color: e[0]};
                        return e[1] && (t.offset = parseFloat(e[1])), t
                    }), {type: n, params: i, stops: r}
                }), t.on("snap.util.attr.d", function (n) {
                    t.stop(), d(n, "array") && d(n[0], "array") && (n = e.path.toString.call(n)), n = v(n), n.match(/[ruo]/i) && (n = e.path.toAbsolute(n)), m(this.node, {d: n})
                })(-1), t.on("snap.util.attr.#text", function (e) {
                    t.stop(), e = v(e);
                    for (var n = r.doc.createTextNode(e); this.node.firstChild;)this.node.removeChild(this.node.firstChild);
                    this.node.appendChild(n)
                })(-1), t.on("snap.util.attr.path", function (e) {
                    t.stop(), this.attr({d: e})
                })(-1), t.on("snap.util.attr.class", function (e) {
                    t.stop(), this.node.className.baseVal = e
                })(-1), t.on("snap.util.attr.viewBox", function (e) {
                    var n;
                    n = d(e, "object") && "x"in e ? [e.x, e.y, e.width, e.height].join(" ") : d(e, "array") ? e.join(" ") : e, m(this.node, {viewBox: n}), t.stop()
                })(-1), t.on("snap.util.attr.transform", function (e) {
                    this.transform(e), t.stop()
                })(-1), t.on("snap.util.attr.r", function (e) {
                    "rect" == this.type && (t.stop(), m(this.node, {rx: e, ry: e}))
                })(-1), t.on("snap.util.attr.textpath", function (e) {
                    if (t.stop(), "text" == this.type) {
                        var i, r, o;
                        if (!e && this.textPath) {
                            for (r = this.textPath; r.node.firstChild;)this.node.appendChild(r.node.firstChild);
                            return r.remove(), void delete this.textPath
                        }
                        if (d(e, "string")) {
                            var a = h(this), s = p(a.parentNode).path(e);
                            a.appendChild(s.node), i = s.id, s.attr({id: i})
                        } else e = p(e), e instanceof n && (i = e.attr("id"), i || (i = e.id, e.attr({id: i})));
                        if (i)if (r = this.textPath, o = this.node, r)r.attr({"xlink:href": "#" + i}); else {
                            for (r = m("textPath", {"xlink:href": "#" + i}); o.firstChild;)r.appendChild(o.firstChild);
                            o.appendChild(r), this.textPath = p(r)
                        }
                    }
                })(-1), t.on("snap.util.attr.text", function (e) {
                    if ("text" == this.type) {
                        for (var n = this.node, i = function (e) {
                            var t = m("tspan");
                            if (d(e, "array"))for (var n = 0; n < e.length; n++)t.appendChild(i(e[n])); else t.appendChild(r.doc.createTextNode(e));
                            return t.normalize && t.normalize(), t
                        }; n.firstChild;)n.removeChild(n.firstChild);
                        for (var o = i(e); o.firstChild;)n.appendChild(o.firstChild)
                    }
                    t.stop()
                })(-1), t.on("snap.util.attr.fontSize", s)(-1), t.on("snap.util.attr.font-size", s)(-1), t.on("snap.util.getattr.transform", function () {
                    return t.stop(), this.transform()
                })(-1), t.on("snap.util.getattr.textpath", function () {
                    return t.stop(), this.textPath
                })(-1), function () {
                    function n(n) {
                        return function () {
                            t.stop();
                            var i = r.doc.defaultView.getComputedStyle(this.node, null).getPropertyValue("marker-" + n);
                            return "none" == i ? i : e(r.doc.getElementById(i.match(f)[1]))
                        }
                    }

                    function i(e) {
                        return function (n) {
                            t.stop();
                            var i = "marker" + e.charAt(0).toUpperCase() + e.substring(1);
                            if ("" == n || !n)return void(this.node.style[i] = "none");
                            if ("marker" == n.type) {
                                var r = n.node.id;
                                return r || m(n.node, {id: n.id}), void(this.node.style[i] = g(r))
                            }
                        }
                    }

                    t.on("snap.util.getattr.marker-end", n("end"))(-1), t.on("snap.util.getattr.markerEnd", n("end"))(-1), t.on("snap.util.getattr.marker-start", n("start"))(-1), t.on("snap.util.getattr.markerStart", n("start"))(-1), t.on("snap.util.getattr.marker-mid", n("mid"))(-1), t.on("snap.util.getattr.markerMid", n("mid"))(-1), t.on("snap.util.attr.marker-end", i("end"))(-1), t.on("snap.util.attr.markerEnd", i("end"))(-1), t.on("snap.util.attr.marker-start", i("start"))(-1), t.on("snap.util.attr.markerStart", i("start"))(-1), t.on("snap.util.attr.marker-mid", i("mid"))(-1), t.on("snap.util.attr.markerMid", i("mid"))(-1)
                }(), t.on("snap.util.getattr.r", function () {
                    return "rect" == this.type && m(this.node, "rx") == m(this.node, "ry") ? (t.stop(), m(this.node, "rx")) : void 0
                })(-1), t.on("snap.util.getattr.text", function () {
                    if ("text" == this.type || "tspan" == this.type) {
                        t.stop();
                        var e = c(this.node);
                        return 1 == e.length ? e[0] : e
                    }
                })(-1), t.on("snap.util.getattr.#text", function () {
                    return this.node.textContent
                })(-1), t.on("snap.util.getattr.viewBox", function () {
                    t.stop();
                    var n = m(this.node, "viewBox");
                    return n ? (n = n.split(y), e._.box(+n[0], +n[1], +n[2], +n[3])) : void 0
                })(-1), t.on("snap.util.getattr.points", function () {
                    var e = m(this.node, "points");
                    return t.stop(), e ? e.split(y) : void 0
                })(-1), t.on("snap.util.getattr.path", function () {
                    var e = m(this.node, "d");
                    return t.stop(), e
                })(-1), t.on("snap.util.getattr.class", function () {
                    return this.node.className.baseVal
                })(-1), t.on("snap.util.getattr.fontSize", l)(-1), t.on("snap.util.getattr.font-size", l)(-1)
            }), i.plugin(function (n, i, r, o, a) {
                var s = r.prototype, c = n.is;
                s.rect = function (e, t, n, i, r, o) {
                    var a;
                    return null == o && (o = r), c(e, "object") && "[object Object]" == e ? a = e : null != e && (a = {
                        x: e,
                        y: t,
                        width: n,
                        height: i
                    }, null != r && (a.rx = r, a.ry = o)), this.el("rect", a)
                }, s.circle = function (e, t, n) {
                    var i;
                    return c(e, "object") && "[object Object]" == e ? i = e : null != e && (i = {
                        cx: e,
                        cy: t,
                        r: n
                    }), this.el("circle", i)
                };
                var l = function () {
                    function e() {
                        this.parentNode.removeChild(this)
                    }

                    return function (t, n) {
                        var i = o.doc.createElement("img"), r = o.doc.body;
                        i.style.cssText = "position:absolute;left:-9999em;top:-9999em", i.onload = function () {
                            n.call(i), i.onload = i.onerror = null, r.removeChild(i)
                        }, i.onerror = e, r.appendChild(i), i.src = t
                    }
                }();
                s.image = function (e, t, i, r, o) {
                    var a = this.el("image");
                    if (c(e, "object") && "src"in e)a.attr(e); else if (null != e) {
                        var s = {"xlink:href": e, preserveAspectRatio: "none"};
                        null != t && null != i && (s.x = t, s.y = i), null != r && null != o ? (s.width = r, s.height = o) : l(e, function () {
                            n._.$(a.node, {width: this.offsetWidth, height: this.offsetHeight})
                        }), n._.$(a.node, s)
                    }
                    return a
                }, s.ellipse = function (e, t, n, i) {
                    var r;
                    return c(e, "object") && "[object Object]" == e ? r = e : null != e && (r = {
                        cx: e,
                        cy: t,
                        rx: n,
                        ry: i
                    }), this.el("ellipse", r)
                }, s.path = function (e) {
                    var t;
                    return c(e, "object") && !c(e, "array") ? t = e : e && (t = {d: e}), this.el("path", t)
                }, s.group = s.g = function (e) {
                    var t = this.el("g");
                    return 1 == arguments.length && e && !e.type ? t.attr(e) : arguments.length && t.add(Array.prototype.slice.call(arguments, 0)), t
                }, s.svg = function (e, t, n, i, r, o, a, s) {
                    var l = {};
                    return c(e, "object") && null == t ? l = e : (null != e && (l.x = e), null != t && (l.y = t), null != n && (l.width = n), null != i && (l.height = i), null != r && null != o && null != a && null != s && (l.viewBox = [r, o, a, s])), this.el("svg", l)
                }, s.mask = function (e) {
                    var t = this.el("mask");
                    return 1 == arguments.length && e && !e.type ? t.attr(e) : arguments.length && t.add(Array.prototype.slice.call(arguments, 0)), t
                }, s.ptrn = function (e, t, n, i, r, o, a, s) {
                    if (c(e, "object"))var l = e; else l = {patternUnits: "userSpaceOnUse"}, e && (l.x = e), t && (l.y = t), null != n && (l.width = n), null != i && (l.height = i), null != r && null != o && null != a && null != s && (l.viewBox = [r, o, a, s]);
                    return this.el("pattern", l)
                }, s.use = function (e) {
                    return null != e ? (e instanceof i && (e.attr("id") || e.attr({id: n._.id(e)}), e = e.attr("id")), "#" == String(e).charAt() && (e = e.substring(1)), this.el("use", {"xlink:href": "#" + e})) : i.prototype.use.call(this)
                }, s.symbol = function (e, t, n, i) {
                    var r = {};
                    return null != e && null != t && null != n && null != i && (r.viewBox = [e, t, n, i]), this.el("symbol", r)
                }, s.text = function (e, t, n) {
                    var i = {};
                    return c(e, "object") ? i = e : null != e && (i = {x: e, y: t, text: n || ""}), this.el("text", i)
                }, s.line = function (e, t, n, i) {
                    var r = {};
                    return c(e, "object") ? r = e : null != e && (r = {x1: e, x2: n, y1: t, y2: i}), this.el("line", r)
                }, s.polyline = function (e) {
                    arguments.length > 1 && (e = Array.prototype.slice.call(arguments, 0));
                    var t = {};
                    return c(e, "object") && !c(e, "array") ? t = e : null != e && (t = {points: e}), this.el("polyline", t)
                }, s.polygon = function (e) {
                    arguments.length > 1 && (e = Array.prototype.slice.call(arguments, 0));
                    var t = {};
                    return c(e, "object") && !c(e, "array") ? t = e : null != e && (t = {points: e}), this.el("polygon", t)
                }, function () {
                    function i() {
                        return this.selectAll("stop")
                    }

                    function r(e, t) {
                        var i = u("stop"), r = {offset: +t + "%"};
                        return e = n.color(e), r["stop-color"] = e.hex, e.opacity < 1 && (r["stop-opacity"] = e.opacity), u(i, r), this.node.appendChild(i), this
                    }

                    function o() {
                        if ("linearGradient" == this.type) {
                            var e = u(this.node, "x1") || 0, t = u(this.node, "x2") || 1, i = u(this.node, "y1") || 0, r = u(this.node, "y2") || 0;
                            return n._.box(e, i, math.abs(t - e), math.abs(r - i))
                        }
                        var o = this.node.cx || .5, a = this.node.cy || .5, s = this.node.r || 0;
                        return n._.box(o - s, a - s, 2 * s, 2 * s)
                    }

                    function a(e, n) {
                        function i(e, t) {
                            for (var n = (t - p) / (e - d), i = d; e > i; i++)a[i].offset = +(+p + n * (i - d)).toFixed(2);
                            d = e, p = t
                        }

                        var r, o = t("snap.util.grad.parse", null, n).firstDefined();
                        if (!o)return null;
                        o.params.unshift(e), r = "l" == o.type.toLowerCase() ? c.apply(0, o.params) : l.apply(0, o.params), o.type != o.type.toLowerCase() && u(r.node, {gradientUnits: "userSpaceOnUse"});
                        var a = o.stops, s = a.length, p = 0, d = 0;
                        s--;
                        for (var h = 0; s > h; h++)"offset"in a[h] && i(h, a[h].offset);
                        for (a[s].offset = a[s].offset || 100, i(s, a[s].offset), h = 0; s >= h; h++) {
                            var f = a[h];
                            r.addStop(f.color, f.offset)
                        }
                        return r
                    }

                    function c(e, t, a, s, c) {
                        var l = n._.make("linearGradient", e);
                        return l.stops = i, l.addStop = r, l.getBBox = o, null != t && u(l.node, {
                            x1: t,
                            y1: a,
                            x2: s,
                            y2: c
                        }), l
                    }

                    function l(e, t, a, s, c, l) {
                        var p = n._.make("radialGradient", e);
                        return p.stops = i, p.addStop = r, p.getBBox = o, null != t && u(p.node, {
                            cx: t,
                            cy: a,
                            r: s
                        }), null != c && null != l && u(p.node, {fx: c, fy: l}), p
                    }

                    var u = n._.$;
                    s.gradient = function (e) {
                        return a(this.defs, e)
                    }, s.gradientLinear = function (e, t, n, i) {
                        return c(this.defs, e, t, n, i)
                    }, s.gradientRadial = function (e, t, n, i, r) {
                        return l(this.defs, e, t, n, i, r)
                    }, s.toString = function () {
                        var e, t = this.node.ownerDocument, i = t.createDocumentFragment(), r = t.createElement("div"), o = this.node.cloneNode(!0);
                        return i.appendChild(r), r.appendChild(o), n._.$(o, {xmlns: "http://www.w3.org/2000/svg"}), e = r.innerHTML, i.removeChild(i.firstChild), e
                    }, s.toDataURL = function () {
                        return e && e.btoa ? "data:image/svg+xml;base64," + btoa(unescape(encodeURIComponent(this))) : void 0
                    }, s.clear = function () {
                        for (var e, t = this.node.firstChild; t;)e = t.nextSibling, "defs" != t.tagName ? t.parentNode.removeChild(t) : s.clear.call({node: t}), t = e
                    }
                }()
            }), i.plugin(function (e, t, n, i) {
                function r(e) {
                    var t = r.ps = r.ps || {};
                    return t[e] ? t[e].sleep = 100 : t[e] = {sleep: 100}, setTimeout(function () {
                        for (var n in t)t[I](n) && n != e && (t[n].sleep--, !t[n].sleep && delete t[n])
                    }), t[e]
                }

                function o(e, t, n, i) {
                    return null == e && (e = t = n = i = 0), null == t && (t = e.y, n = e.width, i = e.height, e = e.x), {
                        x: e,
                        y: t,
                        width: n,
                        w: n,
                        height: i,
                        h: i,
                        x2: e + n,
                        y2: t + i,
                        cx: e + n / 2,
                        cy: t + i / 2,
                        r1: z.min(n, i) / 2,
                        r2: z.max(n, i) / 2,
                        r0: z.sqrt(n * n + i * i) / 2,
                        path: S(e, t, n, i),
                        vb: [e, t, n, i].join(" ")
                    }
                }

                function a() {
                    return this.join(",").replace(F, "$1")
                }

                function s(e) {
                    var t = O(e);
                    return t.toString = a, t
                }

                function c(e, t, n, i, r, o, a, s, c) {
                    return null == c ? m(e, t, n, i, r, o, a, s) : u(e, t, n, i, r, o, a, s, g(e, t, n, i, r, o, a, s, c))
                }

                function l(n, i) {
                    function r(e) {
                        return +(+e).toFixed(3)
                    }

                    return e._.cacher(function (e, o, a) {
                        e instanceof t && (e = e.attr("d")), e = M(e);
                        for (var s, l, p, d, h, f = "", m = {}, g = 0, v = 0, y = e.length; y > v; v++) {
                            if (p = e[v], "M" == p[0])s = +p[1], l = +p[2]; else {
                                if (d = c(s, l, p[1], p[2], p[3], p[4], p[5], p[6]), g + d > o) {
                                    if (i && !m.start) {
                                        if (h = c(s, l, p[1], p[2], p[3], p[4], p[5], p[6], o - g), f += ["C" + r(h.start.x), r(h.start.y), r(h.m.x), r(h.m.y), r(h.x), r(h.y)], a)return f;
                                        m.start = f, f = ["M" + r(h.x), r(h.y) + "C" + r(h.n.x), r(h.n.y), r(h.end.x), r(h.end.y), r(p[5]), r(p[6])].join(), g += d, s = +p[5], l = +p[6];
                                        continue
                                    }
                                    if (!n && !i)return h = c(s, l, p[1], p[2], p[3], p[4], p[5], p[6], o - g)
                                }
                                g += d, s = +p[5], l = +p[6]
                            }
                            f += p.shift() + p
                        }
                        return m.end = f, h = n ? g : i ? m : u(s, l, p[0], p[1], p[2], p[3], p[4], p[5], 1)
                    }, null, e._.clone)
                }

                function u(e, t, n, i, r, o, a, s, c) {
                    var l = 1 - c, u = G(l, 3), p = G(l, 2), d = c * c, h = d * c, f = u * e + 3 * p * c * n + 3 * l * c * c * r + h * a, m = u * t + 3 * p * c * i + 3 * l * c * c * o + h * s, g = e + 2 * c * (n - e) + d * (r - 2 * n + e), v = t + 2 * c * (i - t) + d * (o - 2 * i + t), y = n + 2 * c * (r - n) + d * (a - 2 * r + n), b = i + 2 * c * (o - i) + d * (s - 2 * o + i), x = l * e + c * n, E = l * t + c * i, w = l * r + c * a, _ = l * o + c * s, S = 90 - 180 * z.atan2(g - y, v - b) / $;
                    return {
                        x: f,
                        y: m,
                        m: {x: g, y: v},
                        n: {x: y, y: b},
                        start: {x: x, y: E},
                        end: {x: w, y: _},
                        alpha: S
                    }
                }

                function p(t, n, i, r, a, s, c, l) {
                    e.is(t, "array") || (t = [t, n, i, r, a, s, c, l]);
                    var u = R.apply(null, t);
                    return o(u.min.x, u.min.y, u.max.x - u.min.x, u.max.y - u.min.y)
                }

                function d(e, t, n) {
                    return t >= e.x && t <= e.x + e.width && n >= e.y && n <= e.y + e.height
                }

                function h(e, t) {
                    return e = o(e), t = o(t), d(t, e.x, e.y) || d(t, e.x2, e.y) || d(t, e.x, e.y2) || d(t, e.x2, e.y2) || d(e, t.x, t.y) || d(e, t.x2, t.y) || d(e, t.x, t.y2) || d(e, t.x2, t.y2) || (e.x < t.x2 && e.x > t.x || t.x < e.x2 && t.x > e.x) && (e.y < t.y2 && e.y > t.y || t.y < e.y2 && t.y > e.y)
                }

                function f(e, t, n, i, r) {
                    var o = -3 * t + 9 * n - 9 * i + 3 * r, a = e * o + 6 * t - 12 * n + 6 * i;
                    return e * a - 3 * t + 3 * n
                }

                function m(e, t, n, i, r, o, a, s, c) {
                    null == c && (c = 1), c = c > 1 ? 1 : 0 > c ? 0 : c;
                    for (var l = c / 2, u = 12, p = [-.1252, .1252, -.3678, .3678, -.5873, .5873, -.7699, .7699, -.9041, .9041, -.9816, .9816], d = [.2491, .2491, .2335, .2335, .2032, .2032, .1601, .1601, .1069, .1069, .0472, .0472], h = 0, m = 0; u > m; m++) {
                        var g = l * p[m] + l, v = f(g, e, n, r, a), y = f(g, t, i, o, s), b = v * v + y * y;
                        h += d[m] * z.sqrt(b)
                    }
                    return l * h
                }

                function g(e, t, n, i, r, o, a, s, c) {
                    if (!(0 > c || m(e, t, n, i, r, o, a, s) < c)) {
                        var l, u = 1, p = u / 2, d = u - p, h = .01;
                        for (l = m(e, t, n, i, r, o, a, s, d); W(l - c) > h;)p /= 2, d += (c > l ? 1 : -1) * p, l = m(e, t, n, i, r, o, a, s, d);
                        return d
                    }
                }

                function v(e, t, n, i, r, o, a, s) {
                    if (!(q(e, n) < H(r, a) || H(e, n) > q(r, a) || q(t, i) < H(o, s) || H(t, i) > q(o, s))) {
                        var c = (e * i - t * n) * (r - a) - (e - n) * (r * s - o * a), l = (e * i - t * n) * (o - s) - (t - i) * (r * s - o * a), u = (e - n) * (o - s) - (t - i) * (r - a);
                        if (u) {
                            var p = c / u, d = l / u, h = +p.toFixed(2), f = +d.toFixed(2);
                            if (!(h < +H(e, n).toFixed(2) || h > +q(e, n).toFixed(2) || h < +H(r, a).toFixed(2) || h > +q(r, a).toFixed(2) || f < +H(t, i).toFixed(2) || f > +q(t, i).toFixed(2) || f < +H(o, s).toFixed(2) || f > +q(o, s).toFixed(2)))return {
                                x: p,
                                y: d
                            }
                        }
                    }
                }

                function y(e, t, n) {
                    var i = p(e), r = p(t);
                    if (!h(i, r))return n ? 0 : [];
                    for (var o = m.apply(0, e), a = m.apply(0, t), s = ~~(o / 8), c = ~~(a / 8), l = [], d = [], f = {}, g = n ? 0 : [], y = 0; s + 1 > y; y++) {
                        var b = u.apply(0, e.concat(y / s));
                        l.push({x: b.x, y: b.y, t: y / s})
                    }
                    for (y = 0; c + 1 > y; y++)b = u.apply(0, t.concat(y / c)), d.push({x: b.x, y: b.y, t: y / c});
                    for (y = 0; s > y; y++)for (var x = 0; c > x; x++) {
                        var E = l[y], w = l[y + 1], _ = d[x], S = d[x + 1], C = W(w.x - E.x) < .001 ? "y" : "x", A = W(S.x - _.x) < .001 ? "y" : "x", T = v(E.x, E.y, w.x, w.y, _.x, _.y, S.x, S.y);
                        if (T) {
                            if (f[T.x.toFixed(4)] == T.y.toFixed(4))continue;
                            f[T.x.toFixed(4)] = T.y.toFixed(4);
                            var j = E.t + W((T[C] - E[C]) / (w[C] - E[C])) * (w.t - E.t), N = _.t + W((T[A] - _[A]) / (S[A] - _[A])) * (S.t - _.t);
                            j >= 0 && 1 >= j && N >= 0 && 1 >= N && (n ? g++ : g.push({x: T.x, y: T.y, t1: j, t2: N}))
                        }
                    }
                    return g
                }

                function b(e, t) {
                    return E(e, t)
                }

                function x(e, t) {
                    return E(e, t, 1)
                }

                function E(e, t, n) {
                    e = M(e), t = M(t);
                    for (var i, r, o, a, s, c, l, u, p, d, h = n ? 0 : [], f = 0, m = e.length; m > f; f++) {
                        var g = e[f];
                        if ("M" == g[0])i = s = g[1], r = c = g[2]; else {
                            "C" == g[0] ? (p = [i, r].concat(g.slice(1)), i = p[6], r = p[7]) : (p = [i, r, i, r, s, c, s, c], i = s, r = c);
                            for (var v = 0, b = t.length; b > v; v++) {
                                var x = t[v];
                                if ("M" == x[0])o = l = x[1], a = u = x[2]; else {
                                    "C" == x[0] ? (d = [o, a].concat(x.slice(1)), o = d[6], a = d[7]) : (d = [o, a, o, a, l, u, l, u], o = l, a = u);
                                    var E = y(p, d, n);
                                    if (n)h += E; else {
                                        for (var w = 0, _ = E.length; _ > w; w++)E[w].segment1 = f, E[w].segment2 = v, E[w].bez1 = p, E[w].bez2 = d;
                                        h = h.concat(E)
                                    }
                                }
                            }
                        }
                    }
                    return h
                }

                function w(e, t, n) {
                    var i = _(e);
                    return d(i, t, n) && E(e, [["M", t, n], ["H", i.x2 + 10]], 1) % 2 == 1
                }

                function _(e) {
                    var t = r(e);
                    if (t.bbox)return O(t.bbox);
                    if (!e)return o();
                    e = M(e);
                    for (var n, i = 0, a = 0, s = [], c = [], l = 0, u = e.length; u > l; l++)if (n = e[l], "M" == n[0])i = n[1], a = n[2], s.push(i), c.push(a); else {
                        var p = R(i, a, n[1], n[2], n[3], n[4], n[5], n[6]);
                        s = s.concat(p.min.x, p.max.x), c = c.concat(p.min.y, p.max.y), i = n[5], a = n[6]
                    }
                    var d = H.apply(0, s), h = H.apply(0, c), f = q.apply(0, s), m = q.apply(0, c), g = o(d, h, f - d, m - h);
                    return t.bbox = O(g), g
                }

                function S(e, t, n, i, r) {
                    if (r)return [["M", +e + +r, t], ["l", n - 2 * r, 0], ["a", r, r, 0, 0, 1, r, r], ["l", 0, i - 2 * r], ["a", r, r, 0, 0, 1, -r, r], ["l", 2 * r - n, 0], ["a", r, r, 0, 0, 1, -r, -r], ["l", 0, 2 * r - i], ["a", r, r, 0, 0, 1, r, -r], ["z"]];
                    var o = [["M", e, t], ["l", n, 0], ["l", 0, i], ["l", -n, 0], ["z"]];
                    return o.toString = a, o
                }

                function C(e, t, n, i, r) {
                    if (null == r && null == i && (i = n), e = +e, t = +t, n = +n, i = +i, null != r)var o = Math.PI / 180, s = e + n * Math.cos(-i * o), c = e + n * Math.cos(-r * o), l = t + n * Math.sin(-i * o), u = t + n * Math.sin(-r * o), p = [["M", s, l], ["A", n, n, 0, +(r - i > 180), 0, c, u]]; else p = [["M", e, t], ["m", 0, -i], ["a", n, i, 0, 1, 1, 0, 2 * i], ["a", n, i, 0, 1, 1, 0, -2 * i], ["z"]];
                    return p.toString = a, p
                }

                function A(t) {
                    var n = r(t), i = String.prototype.toLowerCase;
                    if (n.rel)return s(n.rel);
                    e.is(t, "array") && e.is(t && t[0], "array") || (t = e.parsePathString(t));
                    var o = [], c = 0, l = 0, u = 0, p = 0, d = 0;
                    "M" == t[0][0] && (c = t[0][1], l = t[0][2], u = c, p = l, d++, o.push(["M", c, l]));
                    for (var h = d, f = t.length; f > h; h++) {
                        var m = o[h] = [], g = t[h];
                        if (g[0] != i.call(g[0]))switch (m[0] = i.call(g[0]), m[0]) {
                            case"a":
                                m[1] = g[1], m[2] = g[2], m[3] = g[3], m[4] = g[4], m[5] = g[5], m[6] = +(g[6] - c).toFixed(3), m[7] = +(g[7] - l).toFixed(3);
                                break;
                            case"v":
                                m[1] = +(g[1] - l).toFixed(3);
                                break;
                            case"m":
                                u = g[1], p = g[2];
                            default:
                                for (var v = 1, y = g.length; y > v; v++)m[v] = +(g[v] - (v % 2 ? c : l)).toFixed(3)
                        } else {
                            m = o[h] = [], "m" == g[0] && (u = g[1] + c, p = g[2] + l);
                            for (var b = 0, x = g.length; x > b; b++)o[h][b] = g[b]
                        }
                        var E = o[h].length;
                        switch (o[h][0]) {
                            case"z":
                                c = u, l = p;
                                break;
                            case"h":
                                c += +o[h][E - 1];
                                break;
                            case"v":
                                l += +o[h][E - 1];
                                break;
                            default:
                                c += +o[h][E - 2], l += +o[h][E - 1]
                        }
                    }
                    return o.toString = a, n.rel = s(o), o
                }

                function T(t) {
                    var n = r(t);
                    if (n.abs)return s(n.abs);
                    if (L(t, "array") && L(t && t[0], "array") || (t = e.parsePathString(t)), !t || !t.length)return [["M", 0, 0]];
                    var i, o = [], c = 0, l = 0, u = 0, p = 0, d = 0;
                    "M" == t[0][0] && (c = +t[0][1], l = +t[0][2], u = c, p = l, d++, o[0] = ["M", c, l]);
                    for (var h, f, m = 3 == t.length && "M" == t[0][0] && "R" == t[1][0].toUpperCase() && "Z" == t[2][0].toUpperCase(), g = d, v = t.length; v > g; g++) {
                        if (o.push(h = []), f = t[g], i = f[0], i != i.toUpperCase())switch (h[0] = i.toUpperCase(), h[0]) {
                            case"A":
                                h[1] = f[1], h[2] = f[2], h[3] = f[3], h[4] = f[4], h[5] = f[5], h[6] = +f[6] + c, h[7] = +f[7] + l;
                                break;
                            case"V":
                                h[1] = +f[1] + l;
                                break;
                            case"H":
                                h[1] = +f[1] + c;
                                break;
                            case"R":
                                for (var y = [c, l].concat(f.slice(1)), b = 2, x = y.length; x > b; b++)y[b] = +y[b] + c, y[++b] = +y[b] + l;
                                o.pop(), o = o.concat(P(y, m));
                                break;
                            case"O":
                                o.pop(), y = C(c, l, f[1], f[2]), y.push(y[0]), o = o.concat(y);
                                break;
                            case"U":
                                o.pop(), o = o.concat(C(c, l, f[1], f[2], f[3])), h = ["U"].concat(o[o.length - 1].slice(-2));
                                break;
                            case"M":
                                u = +f[1] + c, p = +f[2] + l;
                            default:
                                for (b = 1, x = f.length; x > b; b++)h[b] = +f[b] + (b % 2 ? c : l)
                        } else if ("R" == i)y = [c, l].concat(f.slice(1)), o.pop(), o = o.concat(P(y, m)), h = ["R"].concat(f.slice(-2)); else if ("O" == i)o.pop(), y = C(c, l, f[1], f[2]), y.push(y[0]), o = o.concat(y); else if ("U" == i)o.pop(), o = o.concat(C(c, l, f[1], f[2], f[3])), h = ["U"].concat(o[o.length - 1].slice(-2)); else for (var E = 0, w = f.length; w > E; E++)h[E] = f[E];
                        if (i = i.toUpperCase(), "O" != i)switch (h[0]) {
                            case"Z":
                                c = +u, l = +p;
                                break;
                            case"H":
                                c = h[1];
                                break;
                            case"V":
                                l = h[1];
                                break;
                            case"M":
                                u = h[h.length - 2], p = h[h.length - 1];
                            default:
                                c = h[h.length - 2], l = h[h.length - 1]
                        }
                    }
                    return o.toString = a, n.abs = s(o), o
                }

                function j(e, t, n, i) {
                    return [e, t, n, i, n, i]
                }

                function N(e, t, n, i, r, o) {
                    var a = 1 / 3, s = 2 / 3;
                    return [a * e + s * n, a * t + s * i, a * r + s * n, a * o + s * i, r, o]
                }

                function k(t, n, i, r, o, a, s, c, l, u) {
                    var p, d = 120 * $ / 180, h = $ / 180 * (+o || 0), f = [], m = e._.cacher(function (e, t, n) {
                        var i = e * z.cos(n) - t * z.sin(n), r = e * z.sin(n) + t * z.cos(n);
                        return {x: i, y: r}
                    });
                    if (u)S = u[0], C = u[1], w = u[2], _ = u[3]; else {
                        p = m(t, n, -h), t = p.x, n = p.y, p = m(c, l, -h), c = p.x, l = p.y;
                        var g = (z.cos($ / 180 * o), z.sin($ / 180 * o), (t - c) / 2), v = (n - l) / 2, y = g * g / (i * i) + v * v / (r * r);
                        y > 1 && (y = z.sqrt(y), i = y * i, r = y * r);
                        var b = i * i, x = r * r, E = (a == s ? -1 : 1) * z.sqrt(W((b * x - b * v * v - x * g * g) / (b * v * v + x * g * g))), w = E * i * v / r + (t + c) / 2, _ = E * -r * g / i + (n + l) / 2, S = z.asin(((n - _) / r).toFixed(9)), C = z.asin(((l - _) / r).toFixed(9));
                        S = w > t ? $ - S : S, C = w > c ? $ - C : C, 0 > S && (S = 2 * $ + S), 0 > C && (C = 2 * $ + C), s && S > C && (S -= 2 * $), !s && C > S && (C -= 2 * $)
                    }
                    var A = C - S;
                    if (W(A) > d) {
                        var T = C, j = c, N = l;
                        C = S + d * (s && C > S ? 1 : -1), c = w + i * z.cos(C), l = _ + r * z.sin(C), f = k(c, l, i, r, o, 0, s, j, N, [C, T, w, _])
                    }
                    A = C - S;
                    var R = z.cos(S), M = z.sin(S), D = z.cos(C), P = z.sin(C), B = z.tan(A / 4), L = 4 / 3 * i * B, O = 4 / 3 * r * B, I = [t, n], F = [t + L * M, n - O * R], U = [c + L * P, l - O * D], H = [c, l];
                    if (F[0] = 2 * I[0] - F[0], F[1] = 2 * I[1] - F[1], u)return [F, U, H].concat(f);
                    f = [F, U, H].concat(f).join().split(",");
                    for (var q = [], G = 0, V = f.length; V > G; G++)q[G] = G % 2 ? m(f[G - 1], f[G], h).y : m(f[G], f[G + 1], h).x;
                    return q
                }

                function R(e, t, n, i, r, o, a, s) {
                    for (var c, l, u, p, d, h, f, m, g = [], v = [[], []], y = 0; 2 > y; ++y)if (0 == y ? (l = 6 * e - 12 * n + 6 * r, c = -3 * e + 9 * n - 9 * r + 3 * a, u = 3 * n - 3 * e) : (l = 6 * t - 12 * i + 6 * o, c = -3 * t + 9 * i - 9 * o + 3 * s, u = 3 * i - 3 * t), W(c) < 1e-12) {
                        if (W(l) < 1e-12)continue;
                        p = -u / l, p > 0 && 1 > p && g.push(p)
                    } else f = l * l - 4 * u * c, m = z.sqrt(f), 0 > f || (d = (-l + m) / (2 * c), d > 0 && 1 > d && g.push(d), h = (-l - m) / (2 * c), h > 0 && 1 > h && g.push(h));
                    for (var b, x = g.length, E = x; x--;)p = g[x], b = 1 - p, v[0][x] = b * b * b * e + 3 * b * b * p * n + 3 * b * p * p * r + p * p * p * a, v[1][x] = b * b * b * t + 3 * b * b * p * i + 3 * b * p * p * o + p * p * p * s;
                    return v[0][E] = e, v[1][E] = t, v[0][E + 1] = a, v[1][E + 1] = s, v[0].length = v[1].length = E + 2, {
                        min: {
                            x: H.apply(0, v[0]),
                            y: H.apply(0, v[1])
                        }, max: {x: q.apply(0, v[0]), y: q.apply(0, v[1])}
                    }
                }

                function M(e, t) {
                    var n = !t && r(e);
                    if (!t && n.curve)return s(n.curve);
                    for (var i = T(e), o = t && T(t), a = {
                        x: 0,
                        y: 0,
                        bx: 0,
                        by: 0,
                        X: 0,
                        Y: 0,
                        qx: null,
                        qy: null
                    }, c = {x: 0, y: 0, bx: 0, by: 0, X: 0, Y: 0, qx: null, qy: null}, l = (function (e, t, n) {
                        var i, r;
                        if (!e)return ["C", t.x, t.y, t.x, t.y, t.x, t.y];
                        switch (!(e[0]in{T: 1, Q: 1}) && (t.qx = t.qy = null), e[0]) {
                            case"M":
                                t.X = e[1], t.Y = e[2];
                                break;
                            case"A":
                                e = ["C"].concat(k.apply(0, [t.x, t.y].concat(e.slice(1))));
                                break;
                            case"S":
                                "C" == n || "S" == n ? (i = 2 * t.x - t.bx, r = 2 * t.y - t.by) : (i = t.x, r = t.y), e = ["C", i, r].concat(e.slice(1));
                                break;
                            case"T":
                                "Q" == n || "T" == n ? (t.qx = 2 * t.x - t.qx, t.qy = 2 * t.y - t.qy) : (t.qx = t.x, t.qy = t.y), e = ["C"].concat(N(t.x, t.y, t.qx, t.qy, e[1], e[2]));
                                break;
                            case"Q":
                                t.qx = e[1], t.qy = e[2], e = ["C"].concat(N(t.x, t.y, e[1], e[2], e[3], e[4]));
                                break;
                            case"L":
                                e = ["C"].concat(j(t.x, t.y, e[1], e[2]));
                                break;
                            case"H":
                                e = ["C"].concat(j(t.x, t.y, e[1], t.y));
                                break;
                            case"V":
                                e = ["C"].concat(j(t.x, t.y, t.x, e[1]));
                                break;
                            case"Z":
                                e = ["C"].concat(j(t.x, t.y, t.X, t.Y))
                        }
                        return e
                    }), u = function (e, t) {
                        if (e[t].length > 7) {
                            e[t].shift();
                            for (var n = e[t]; n.length;)d[t] = "A", o && (h[t] = "A"), e.splice(t++, 0, ["C"].concat(n.splice(0, 6)));
                            e.splice(t, 1), v = q(i.length, o && o.length || 0)
                        }
                    }, p = function (e, t, n, r, a) {
                        e && t && "M" == e[a][0] && "M" != t[a][0] && (t.splice(a, 0, ["M", r.x, r.y]), n.bx = 0, n.by = 0, n.x = e[a][1], n.y = e[a][2], v = q(i.length, o && o.length || 0))
                    }, d = [], h = [], f = "", m = "", g = 0, v = q(i.length, o && o.length || 0); v > g; g++) {
                        i[g] && (f = i[g][0]), "C" != f && (d[g] = f, g && (m = d[g - 1])), i[g] = l(i[g], a, m), "A" != d[g] && "C" == f && (d[g] = "C"), u(i, g), o && (o[g] && (f = o[g][0]), "C" != f && (h[g] = f, g && (m = h[g - 1])), o[g] = l(o[g], c, m), "A" != h[g] && "C" == f && (h[g] = "C"), u(o, g)), p(i, o, a, c, g), p(o, i, c, a, g);
                        var y = i[g], b = o && o[g], x = y.length, E = o && b.length;
                        a.x = y[x - 2], a.y = y[x - 1], a.bx = U(y[x - 4]) || a.x, a.by = U(y[x - 3]) || a.y, c.bx = o && (U(b[E - 4]) || c.x), c.by = o && (U(b[E - 3]) || c.y), c.x = o && b[E - 2], c.y = o && b[E - 1]
                    }
                    return o || (n.curve = s(i)), o ? [i, o] : i
                }

                function D(e, t) {
                    if (!t)return e;
                    var n, i, r, o, a, s, c;
                    for (e = M(e), r = 0, a = e.length; a > r; r++)for (c = e[r], o = 1, s = c.length; s > o; o += 2)n = t.x(c[o], c[o + 1]), i = t.y(c[o], c[o + 1]), c[o] = n, c[o + 1] = i;
                    return e
                }

                function P(e, t) {
                    for (var n = [], i = 0, r = e.length; r - 2 * !t > i; i += 2) {
                        var o = [{x: +e[i - 2], y: +e[i - 1]}, {x: +e[i], y: +e[i + 1]}, {
                            x: +e[i + 2],
                            y: +e[i + 3]
                        }, {x: +e[i + 4], y: +e[i + 5]}];
                        t ? i ? r - 4 == i ? o[3] = {x: +e[0], y: +e[1]} : r - 2 == i && (o[2] = {
                            x: +e[0],
                            y: +e[1]
                        }, o[3] = {x: +e[2], y: +e[3]}) : o[0] = {
                            x: +e[r - 2],
                            y: +e[r - 1]
                        } : r - 4 == i ? o[3] = o[2] : i || (o[0] = {
                            x: +e[i],
                            y: +e[i + 1]
                        }), n.push(["C", (-o[0].x + 6 * o[1].x + o[2].x) / 6, (-o[0].y + 6 * o[1].y + o[2].y) / 6, (o[1].x + 6 * o[2].x - o[3].x) / 6, (o[1].y + 6 * o[2].y - o[3].y) / 6, o[2].x, o[2].y])
                    }
                    return n
                }

                var B = t.prototype, L = e.is, O = e._.clone, I = "hasOwnProperty", F = /,?([a-z]),?/gi, U = parseFloat, z = Math, $ = z.PI, H = z.min, q = z.max, G = z.pow, W = z.abs, V = l(1), Y = l(), X = l(0, 1), K = e._unit2px, Z = {
                    path: function (e) {
                        return e.attr("path")
                    }, circle: function (e) {
                        var t = K(e);
                        return C(t.cx, t.cy, t.r)
                    }, ellipse: function (e) {
                        var t = K(e);
                        return C(t.cx || 0, t.cy || 0, t.rx, t.ry)
                    }, rect: function (e) {
                        var t = K(e);
                        return S(t.x || 0, t.y || 0, t.width, t.height, t.rx, t.ry)
                    }, image: function (e) {
                        var t = K(e);
                        return S(t.x || 0, t.y || 0, t.width, t.height)
                    }, line: function (e) {
                        return "M" + [e.attr("x1") || 0, e.attr("y1") || 0, e.attr("x2"), e.attr("y2")]
                    }, polyline: function (e) {
                        return "M" + e.attr("points")
                    }, polygon: function (e) {
                        return "M" + e.attr("points") + "z"
                    }, deflt: function (e) {
                        var t = e.node.getBBox();
                        return S(t.x, t.y, t.width, t.height)
                    }
                };
                e.path = r, e.path.getTotalLength = V, e.path.getPointAtLength = Y, e.path.getSubpath = function (e, t, n) {
                    if (this.getTotalLength(e) - n < 1e-6)return X(e, t).end;
                    var i = X(e, n, 1);
                    return t ? X(i, t).end : i
                }, B.getTotalLength = function () {
                    return this.node.getTotalLength ? this.node.getTotalLength() : void 0
                }, B.getPointAtLength = function (e) {
                    return Y(this.attr("d"), e)
                }, B.getSubpath = function (t, n) {
                    return e.path.getSubpath(this.attr("d"), t, n)
                }, e._.box = o, e.path.findDotsAtSegment = u, e.path.bezierBBox = p, e.path.isPointInsideBBox = d, e.path.isBBoxIntersect = h, e.path.intersection = b, e.path.intersectionNumber = x, e.path.isPointInside = w, e.path.getBBox = _, e.path.get = Z, e.path.toRelative = A, e.path.toAbsolute = T, e.path.toCubic = M, e.path.map = D, e.path.toString = a, e.path.clone = s
            }), i.plugin(function (e, n, i, r) {
                for (var o = n.prototype, a = "hasOwnProperty", s = ("createTouch"in r.doc), c = ["click", "dblclick", "mousedown", "mousemove", "mouseout", "mouseover", "mouseup", "touchstart", "touchmove", "touchend", "touchcancel"], l = {
                    mousedown: "touchstart",
                    mousemove: "touchmove",
                    mouseup: "touchend"
                }, u = (function (e, t) {
                    var n = "y" == e ? "scrollTop" : "scrollLeft", i = t && t.node ? t.node.ownerDocument : r.doc;
                    return i[n in i.documentElement ? "documentElement" : "body"][n]
                }), p = function () {
                    this.returnValue = !1
                }, d = function () {
                    return this.originalEvent.preventDefault()
                }, h = function () {
                    this.cancelBubble = !0
                }, f = function () {
                    return this.originalEvent.stopPropagation()
                }, m = function () {
                    return r.doc.addEventListener ? function (e, t, n, i) {
                        var r = s && l[t] ? l[t] : t, o = function (r) {
                            var o = u("y", i), c = u("x", i);
                            if (s && l[a](t))for (var p = 0, h = r.targetTouches && r.targetTouches.length; h > p; p++)if (r.targetTouches[p].target == e || e.contains(r.targetTouches[p].target)) {
                                var m = r;
                                r = r.targetTouches[p], r.originalEvent = m, r.preventDefault = d, r.stopPropagation = f;
                                break
                            }
                            var g = r.clientX + c, v = r.clientY + o;
                            return n.call(i, r, g, v)
                        };
                        return t !== r && e.addEventListener(t, o, !1), e.addEventListener(r, o, !1), function () {
                            return t !== r && e.removeEventListener(t, o, !1), e.removeEventListener(r, o, !1), !0
                        }
                    } : r.doc.attachEvent ? function (e, t, n, i) {
                        var r = function (e) {
                            e = e || i.node.ownerDocument.window.event;
                            var t = u("y", i), r = u("x", i), o = e.clientX + r, a = e.clientY + t;
                            return e.preventDefault = e.preventDefault || p, e.stopPropagation = e.stopPropagation || h, n.call(i, e, o, a)
                        };
                        e.attachEvent("on" + t, r);
                        var o = function () {
                            return e.detachEvent("on" + t, r), !0
                        };
                        return o
                    } : void 0
                }(), g = [], v = function (e) {
                    for (var n, i = e.clientX, r = e.clientY, o = u("y"), a = u("x"), c = g.length; c--;) {
                        if (n = g[c], s) {
                            for (var l, p = e.touches && e.touches.length; p--;)if (l = e.touches[p], l.identifier == n.el._drag.id || n.el.node.contains(l.target)) {
                                i = l.clientX,
                                    r = l.clientY, (e.originalEvent ? e.originalEvent : e).preventDefault();
                                break
                            }
                        } else e.preventDefault();
                        var d = n.el.node;
                        d.nextSibling, d.parentNode, d.style.display;
                        i += a, r += o, t("snap.drag.move." + n.el.id, n.move_scope || n.el, i - n.el._drag.x, r - n.el._drag.y, i, r, e)
                    }
                }, y = function (n) {
                    e.unmousemove(v).unmouseup(y);
                    for (var i, r = g.length; r--;)i = g[r], i.el._drag = {}, t("snap.drag.end." + i.el.id, i.end_scope || i.start_scope || i.move_scope || i.el, n);
                    g = []
                }, b = c.length; b--;)!function (t) {
                    e[t] = o[t] = function (n, i) {
                        return e.is(n, "function") && (this.events = this.events || [], this.events.push({
                            name: t,
                            f: n,
                            unbind: m(this.node || document, t, n, i || this)
                        })), this
                    }, e["un" + t] = o["un" + t] = function (e) {
                        for (var n = this.events || [], i = n.length; i--;)if (n[i].name == t && (n[i].f == e || !e))return n[i].unbind(), n.splice(i, 1), !n.length && delete this.events, this;
                        return this
                    }
                }(c[b]);
                o.hover = function (e, t, n, i) {
                    return this.mouseover(e, n).mouseout(t, i || n)
                }, o.unhover = function (e, t) {
                    return this.unmouseover(e).unmouseout(t)
                };
                var x = [];
                o.drag = function (n, i, r, o, a, s) {
                    function c(c, l, u) {
                        (c.originalEvent || c).preventDefault(), this._drag.x = l, this._drag.y = u, this._drag.id = c.identifier, !g.length && e.mousemove(v).mouseup(y), g.push({
                            el: this,
                            move_scope: o,
                            start_scope: a,
                            end_scope: s
                        }), i && t.on("snap.drag.start." + this.id, i), n && t.on("snap.drag.move." + this.id, n), r && t.on("snap.drag.end." + this.id, r), t("snap.drag.start." + this.id, a || o || this, l, u, c)
                    }

                    if (!arguments.length) {
                        var l;
                        return this.drag(function (e, t) {
                            this.attr({transform: l + (l ? "T" : "t") + [e, t]})
                        }, function () {
                            l = this.transform().local
                        })
                    }
                    return this._drag = {}, x.push({el: this, start: c}), this.mousedown(c), this
                }, o.undrag = function () {
                    for (var n = x.length; n--;)x[n].el == this && (this.unmousedown(x[n].start), x.splice(n, 1), t.unbind("snap.drag.*." + this.id));
                    return !x.length && e.unmousemove(v).unmouseup(y), this
                }
            }), i.plugin(function (e, n, i, r) {
                var o = (n.prototype, i.prototype), a = /^\s*url\((.+)\)/, s = String, c = e._.$;
                e.filter = {}, o.filter = function (t) {
                    var i = this;
                    "svg" != i.type && (i = i.paper);
                    var r = e.parse(s(t)), o = e._.id(), a = (i.node.offsetWidth, i.node.offsetHeight, c("filter"));
                    return c(a, {
                        id: o,
                        filterUnits: "userSpaceOnUse"
                    }), a.appendChild(r.node), i.defs.appendChild(a), new n(a)
                }, t.on("snap.util.getattr.filter", function () {
                    t.stop();
                    var n = c(this.node, "filter");
                    if (n) {
                        var i = s(n).match(a);
                        return i && e.select(i[1])
                    }
                }), t.on("snap.util.attr.filter", function (i) {
                    if (i instanceof n && "filter" == i.type) {
                        t.stop();
                        var r = i.node.id;
                        r || (c(i.node, {id: i.id}), r = i.id), c(this.node, {filter: e.url(r)})
                    }
                    i && "none" != i || (t.stop(), this.node.removeAttribute("filter"))
                }), e.filter.blur = function (t, n) {
                    null == t && (t = 2);
                    var i = null == n ? t : [t, n];
                    return e.format('<feGaussianBlur stdDeviation="{def}"/>', {def: i})
                }, e.filter.blur.toString = function () {
                    return this()
                }, e.filter.shadow = function (t, n, i, r, o) {
                    return "string" == typeof i && (r = i, o = r, i = 4), "string" != typeof r && (o = r, r = "#000"), r = r || "#000", null == i && (i = 4), null == o && (o = 1), null == t && (t = 0, n = 2), null == n && (n = t), r = e.color(r), e.format('<feGaussianBlur in="SourceAlpha" stdDeviation="{blur}"/><feOffset dx="{dx}" dy="{dy}" result="offsetblur"/><feFlood flood-color="{color}"/><feComposite in2="offsetblur" operator="in"/><feComponentTransfer><feFuncA type="linear" slope="{opacity}"/></feComponentTransfer><feMerge><feMergeNode/><feMergeNode in="SourceGraphic"/></feMerge>', {
                        color: r,
                        dx: t,
                        dy: n,
                        blur: i,
                        opacity: o
                    })
                }, e.filter.shadow.toString = function () {
                    return this()
                }, e.filter.grayscale = function (t) {
                    return null == t && (t = 1), e.format('<feColorMatrix type="matrix" values="{a} {b} {c} 0 0 {d} {e} {f} 0 0 {g} {b} {h} 0 0 0 0 0 1 0"/>', {
                        a: .2126 + .7874 * (1 - t),
                        b: .7152 - .7152 * (1 - t),
                        c: .0722 - .0722 * (1 - t),
                        d: .2126 - .2126 * (1 - t),
                        e: .7152 + .2848 * (1 - t),
                        f: .0722 - .0722 * (1 - t),
                        g: .2126 - .2126 * (1 - t),
                        h: .0722 + .9278 * (1 - t)
                    })
                }, e.filter.grayscale.toString = function () {
                    return this()
                }, e.filter.sepia = function (t) {
                    return null == t && (t = 1), e.format('<feColorMatrix type="matrix" values="{a} {b} {c} 0 0 {d} {e} {f} 0 0 {g} {h} {i} 0 0 0 0 0 1 0"/>', {
                        a: .393 + .607 * (1 - t),
                        b: .769 - .769 * (1 - t),
                        c: .189 - .189 * (1 - t),
                        d: .349 - .349 * (1 - t),
                        e: .686 + .314 * (1 - t),
                        f: .168 - .168 * (1 - t),
                        g: .272 - .272 * (1 - t),
                        h: .534 - .534 * (1 - t),
                        i: .131 + .869 * (1 - t)
                    })
                }, e.filter.sepia.toString = function () {
                    return this()
                }, e.filter.saturate = function (t) {
                    return null == t && (t = 1), e.format('<feColorMatrix type="saturate" values="{amount}"/>', {amount: 1 - t})
                }, e.filter.saturate.toString = function () {
                    return this()
                }, e.filter.hueRotate = function (t) {
                    return t = t || 0, e.format('<feColorMatrix type="hueRotate" values="{angle}"/>', {angle: t})
                }, e.filter.hueRotate.toString = function () {
                    return this()
                }, e.filter.invert = function (t) {
                    return null == t && (t = 1), e.format('<feComponentTransfer><feFuncR type="table" tableValues="{amount} {amount2}"/><feFuncG type="table" tableValues="{amount} {amount2}"/><feFuncB type="table" tableValues="{amount} {amount2}"/></feComponentTransfer>', {
                        amount: t,
                        amount2: 1 - t
                    })
                }, e.filter.invert.toString = function () {
                    return this()
                }, e.filter.brightness = function (t) {
                    return null == t && (t = 1), e.format('<feComponentTransfer><feFuncR type="linear" slope="{amount}"/><feFuncG type="linear" slope="{amount}"/><feFuncB type="linear" slope="{amount}"/></feComponentTransfer>', {amount: t})
                }, e.filter.brightness.toString = function () {
                    return this()
                }, e.filter.contrast = function (t) {
                    return null == t && (t = 1), e.format('<feComponentTransfer><feFuncR type="linear" slope="{amount}" intercept="{amount2}"/><feFuncG type="linear" slope="{amount}" intercept="{amount2}"/><feFuncB type="linear" slope="{amount}" intercept="{amount2}"/></feComponentTransfer>', {
                        amount: t,
                        amount2: .5 - t / 2
                    })
                }, e.filter.contrast.toString = function () {
                    return this()
                }
            }), i
        })
    }, {eve: 251}],
    258: [function (e, t, n) {
        "use strict";
        var i = t.exports = e("snapsvg");
        i.plugin(function (e, t) {
            t.prototype.children = function () {
                for (var t = [], n = this.node.childNodes, i = 0, r = n.length; r > i; i++)t[i] = new e(n[i]);
                return t
            }
        }), i.plugin(function (e, t, n, i) {
            function r(e) {
                return e.split(/\s+/)
            }

            function o(e) {
                return e.join(" ")
            }

            function a(e) {
                return r(e.attr("class") || "")
            }

            function s(e, t) {
                e.attr("class", o(t))
            }

            t.prototype.addClass = function (e) {
                var t, n, i = a(this), o = r(e);
                for (t = 0, n; n = o[t]; t++)-1 === i.indexOf(n) && i.push(n);
                return s(this, i), this
            }, t.prototype.hasClass = function (e) {
                if (!e)throw new Error("[snapsvg] syntax: hasClass(clsStr)");
                return -1 !== a(this).indexOf(e)
            }, t.prototype.removeClass = function (e) {
                var t, n, i, o = a(this), c = r(e);
                for (t = 0, n; n = c[t]; t++)i = o.indexOf(n), -1 !== i && o.splice(i, 1);
                return s(this, o), this
            }
        }), i.plugin(function (e, t, n, i) {
            t.prototype.translate = function (t, n) {
                var i = new e.Matrix;
                return i.translate(t, n), this.transform(i)
            }
        }), i.plugin(function (e) {
            e.create = function (t, n) {
                return e._.wrap(e._.$(t, n))
            }
        }), i.plugin(function (e, t, n, i) {
            e.createSnapAt = function (t, n, i) {
                var r = document.createElementNS("http://www.w3.org/2000/svg", "svg");
                return r.setAttribute("width", t), r.setAttribute("height", n), i || (i = document.body), i.appendChild(r), new e(r)
            }
        })
    }, {snapsvg: 257}],
    259: [function (e, t, n) {
        var i = function (e) {
            return "[object Array]" === Object.prototype.toString.call(e)
        }, r = function () {
            var e = Array.prototype.slice.call(arguments);
            1 === e.length && i(e[0]) && (e = e[0]);
            var t = e.pop();
            return t.$inject = e, t
        }, o = /^function\s*[^\(]*\(\s*([^\)]*)\)/m, a = /\/\*([^\*]*)\*\//m, s = function (e) {
            if ("function" != typeof e)throw new Error('Cannot annotate "' + e + '". Expected a function!');
            var t = e.toString().match(o);
            return t[1] && t[1].split(",").map(function (e) {
                    return t = e.match(a), t ? t[1].trim() : e.trim()
                }) || []
        };
        n.annotate = r, n.parse = s, n.isArray = i
    }, {}],
    260: [function (e, t, n) {
        t.exports = {annotate: e("./annotation").annotate, Module: e("./module"), Injector: e("./injector")}
    }, {"./annotation": 259, "./injector": 261, "./module": 262}],
    261: [function (e, t, n) {
        var i = e("./module"), r = e("./annotation").parse, o = e("./annotation").annotate, a = e("./annotation").isArray, s = function (e, t) {
            t = t || {
                get: function (e, t) {
                    if (n.push(e), t === !1)return null;
                    throw p('No provider for "' + e + '"!')
                }
            };
            var n = [], c = this._providers = Object.create(t._providers || null), l = this._instances = Object.create(null), u = l.injector = this, p = function (e) {
                var t = n.join(" -> ");
                return n.length = 0, new Error(t ? e + " (Resolving: " + t + ")" : e)
            }, d = function (e, i) {
                if (!c[e] && -1 !== e.indexOf(".")) {
                    for (var r = e.split("."), o = d(r.shift()); r.length;)o = o[r.shift()];
                    return o
                }
                if (Object.hasOwnProperty.call(l, e))return l[e];
                if (Object.hasOwnProperty.call(c, e)) {
                    if (-1 !== n.indexOf(e))throw n.push(e), p("Cannot resolve circular dependency!");
                    return n.push(e), l[e] = c[e][0](c[e][1]), n.pop(), l[e]
                }
                return t.get(e, i)
            }, h = function (e) {
                var t = Object.create(e.prototype), n = f(e, t);
                return "object" == typeof n ? n : t
            }, f = function (e, t) {
                if ("function" != typeof e) {
                    if (!a(e))throw new Error('Cannot invoke "' + e + '". Expected a function!');
                    e = o(e.slice())
                }
                var n = e.$inject && e.$inject || r(e), i = n.map(function (e) {
                    return d(e)
                });
                return e.apply(t, i)
            }, m = function (e) {
                return o(function (t) {
                    return e.get(t)
                })
            }, g = function (e, t) {
                if (t && t.length) {
                    var n, i, r, o, a = Object.create(null), l = Object.create(null), p = [], d = [], h = [];
                    for (var f in c)n = c[f], -1 !== t.indexOf(f) && ("private" === n[2] ? (i = p.indexOf(n[3]), -1 === i ? (r = n[3].createChild([], t), o = m(r), p.push(n[3]), d.push(r), h.push(o), a[f] = [o, f, "private", r]) : a[f] = [h[i], f, "private", d[i]]) : a[f] = [n[2], n[1]], l[f] = !0), "factory" !== n[2] && "type" !== n[2] || !n[1].$scope || t.forEach(function (e) {
                        -1 !== n[1].$scope.indexOf(e) && (a[f] = [n[2], n[1]], l[e] = !0)
                    });
                    t.forEach(function (e) {
                        if (!l[e])throw new Error('No provider for "' + e + '". Cannot use provider from the parent!')
                    }), e.unshift(a)
                }
                return new s(e, u)
            }, v = {
                factory: f, type: h, value: function (e) {
                    return e
                }
            };
            e.forEach(function (e) {
                function t(e, t) {
                    return "value" !== e && a(t) && (t = o(t.slice())), t
                }

                if (e instanceof i)e.forEach(function (e) {
                    var n = e[0], i = e[1], r = e[2];
                    c[n] = [v[i], t(i, r), i]
                }); else if ("object" == typeof e)if (e.__exports__) {
                    var n = Object.keys(e).reduce(function (t, n) {
                        return "__" !== n.substring(0, 2) && (t[n] = e[n]), t
                    }, Object.create(null)), r = new s((e.__modules__ || []).concat([n]), u), l = o(function (e) {
                        return r.get(e)
                    });
                    e.__exports__.forEach(function (e) {
                        c[e] = [l, e, "private", r]
                    })
                } else Object.keys(e).forEach(function (n) {
                    if ("private" === e[n][2])return void(c[n] = e[n]);
                    var i = e[n][0], r = e[n][1];
                    c[n] = [v[i], t(i, r), i]
                })
            }), this.get = d, this.invoke = f, this.instantiate = h, this.createChild = g
        };
        t.exports = s
    }, {"./annotation": 259, "./module": 262}],
    262: [function (e, t, n) {
        var i = function () {
            var e = [];
            this.factory = function (t, n) {
                return e.push([t, "factory", n]), this
            }, this.value = function (t, n) {
                return e.push([t, "value", n]), this
            }, this.type = function (t, n) {
                return e.push([t, "type", n]), this
            }, this.forEach = function (t) {
                e.forEach(t)
            }
        };
        t.exports = i
    }, {}],
    263: [function (e, t, n) {
        var i = e("../internal/createFindIndex"), r = i();
        t.exports = r
    }, {"../internal/createFindIndex": 350}],
    264: [function (e, t, n) {
        function i(e, t, n) {
            var i = e ? e.length : 0;
            return n && o(e, t, n) && (t = !1), i ? r(e, t) : []
        }

        var r = e("../internal/baseFlatten"), o = e("../internal/isIterateeCall");
        t.exports = i
    }, {"../internal/baseFlatten": 311, "../internal/isIterateeCall": 367}],
    265: [function (e, t, n) {
        function i(e) {
            var t = e ? e.length : 0;
            return t ? e[t - 1] : void 0
        }

        t.exports = i
    }, {}],
    266: [function (e, t, n) {
        function i(e, t, n, i) {
            var c = e ? e.length : 0;
            return c ? (null != t && "boolean" != typeof t && (i = n, n = a(e, t, i) ? void 0 : t, t = !1), n = null == n ? n : r(n, i, 3), t ? s(e, n) : o(e, n)) : []
        }

        var r = e("../internal/baseCallback"), o = e("../internal/baseUniq"), a = e("../internal/isIterateeCall"), s = e("../internal/sortedUniq");
        t.exports = i
    }, {
        "../internal/baseCallback": 300,
        "../internal/baseUniq": 334,
        "../internal/isIterateeCall": 367,
        "../internal/sortedUniq": 382
    }],
    267: [function (e, t, n) {
        t.exports = e("./uniq")
    }, {"./uniq": 266}],
    268: [function (e, t, n) {
        var i = e("../internal/baseDifference"), r = e("../internal/isArrayLike"), o = e("../function/restParam"), a = o(function (e, t) {
            return r(e) ? i(e, t) : []
        });
        t.exports = a
    }, {"../function/restParam": 286, "../internal/baseDifference": 305, "../internal/isArrayLike": 365}],
    269: [function (e, t, n) {
        function i(e) {
            if (c(e) && !s(e) && !(e instanceof r)) {
                if (e instanceof o)return e;
                if (p.call(e, "__chain__") && p.call(e, "__wrapped__"))return l(e)
            }
            return new o(e)
        }

        var r = e("../internal/LazyWrapper"), o = e("../internal/LodashWrapper"), a = e("../internal/baseLodash"), s = e("../lang/isArray"), c = e("../internal/isObjectLike"), l = e("../internal/wrapperClone"), u = Object.prototype, p = u.hasOwnProperty;
        i.prototype = a.prototype, t.exports = i
    }, {
        "../internal/LazyWrapper": 287,
        "../internal/LodashWrapper": 288,
        "../internal/baseLodash": 320,
        "../internal/isObjectLike": 371,
        "../internal/wrapperClone": 385,
        "../lang/isArray": 387
    }],
    270: [function (e, t, n) {
        t.exports = e("./some")
    }, {"./some": 280}],
    271: [function (e, t, n) {
        function i(e, t, n) {
            var i = s(e) ? r : a;
            return n && c(e, t, n) && (t = void 0), ("function" != typeof t || void 0 !== n) && (t = o(t, n, 3)), i(e, t)
        }

        var r = e("../internal/arrayEvery"), o = e("../internal/baseCallback"), a = e("../internal/baseEvery"), s = e("../lang/isArray"), c = e("../internal/isIterateeCall");
        t.exports = i
    }, {
        "../internal/arrayEvery": 292,
        "../internal/baseCallback": 300,
        "../internal/baseEvery": 307,
        "../internal/isIterateeCall": 367,
        "../lang/isArray": 387
    }],
    272: [function (e, t, n) {
        function i(e, t, n) {
            var i = s(e) ? r : a;
            return t = o(t, n, 3), i(e, t)
        }

        var r = e("../internal/arrayFilter"), o = e("../internal/baseCallback"), a = e("../internal/baseFilter"), s = e("../lang/isArray");
        t.exports = i
    }, {
        "../internal/arrayFilter": 293,
        "../internal/baseCallback": 300,
        "../internal/baseFilter": 308,
        "../lang/isArray": 387
    }],
    273: [function (e, t, n) {
        var i = e("../internal/baseEach"), r = e("../internal/createFind"), o = r(i);
        t.exports = o
    }, {"../internal/baseEach": 306, "../internal/createFind": 349}],
    274: [function (e, t, n) {
        var i = e("../internal/arrayEach"), r = e("../internal/baseEach"), o = e("../internal/createForEach"), a = o(i, r);
        t.exports = a
    }, {"../internal/arrayEach": 291, "../internal/baseEach": 306, "../internal/createForEach": 351}],
    275: [function (e, t, n) {
        var i = e("../internal/createAggregator"), r = Object.prototype, o = r.hasOwnProperty, a = i(function (e, t, n) {
            o.call(e, n) ? e[n].push(t) : e[n] = [t]
        });
        t.exports = a
    }, {"../internal/createAggregator": 342}],
    276: [function (e, t, n) {
        function i(e, t, n, i) {
            var d = e ? o(e) : 0;
            return c(d) || (e = u(e), d = e.length), n = "number" != typeof n || i && s(t, n, i) ? 0 : 0 > n ? p(d + n, 0) : n || 0, "string" == typeof e || !a(e) && l(e) ? d >= n && e.indexOf(t, n) > -1 : !!d && r(e, t, n) > -1
        }

        var r = e("../internal/baseIndexOf"), o = e("../internal/getLength"), a = e("../lang/isArray"), s = e("../internal/isIterateeCall"), c = e("../internal/isLength"), l = e("../lang/isString"), u = e("../object/values"), p = Math.max;
        t.exports = i
    }, {
        "../internal/baseIndexOf": 316,
        "../internal/getLength": 361,
        "../internal/isIterateeCall": 367,
        "../internal/isLength": 370,
        "../lang/isArray": 387,
        "../lang/isString": 393,
        "../object/values": 404
    }],
    277: [function (e, t, n) {
        function i(e, t, n) {
            var i = s(e) ? r : a;
            return t = o(t, n, 3), i(e, t)
        }

        var r = e("../internal/arrayMap"), o = e("../internal/baseCallback"), a = e("../internal/baseMap"), s = e("../lang/isArray");
        t.exports = i
    }, {
        "../internal/arrayMap": 294,
        "../internal/baseCallback": 300,
        "../internal/baseMap": 321,
        "../lang/isArray": 387
    }],
    278: [function (e, t, n) {
        var i = e("../internal/arrayReduce"), r = e("../internal/baseEach"), o = e("../internal/createReduce"), a = o(i, r);
        t.exports = a
    }, {"../internal/arrayReduce": 296, "../internal/baseEach": 306, "../internal/createReduce": 354}],
    279: [function (e, t, n) {
        function i(e) {
            var t = e ? r(e) : 0;
            return o(t) ? t : a(e).length
        }

        var r = e("../internal/getLength"), o = e("../internal/isLength"), a = e("../object/keys");
        t.exports = i
    }, {"../internal/getLength": 361, "../internal/isLength": 370, "../object/keys": 397}],
    280: [function (e, t, n) {
        function i(e, t, n) {
            var i = s(e) ? r : a;
            return n && c(e, t, n) && (t = void 0), ("function" != typeof t || void 0 !== n) && (t = o(t, n, 3)), i(e, t)
        }

        var r = e("../internal/arraySome"), o = e("../internal/baseCallback"), a = e("../internal/baseSome"), s = e("../lang/isArray"), c = e("../internal/isIterateeCall");
        t.exports = i
    }, {
        "../internal/arraySome": 297,
        "../internal/baseCallback": 300,
        "../internal/baseSome": 331,
        "../internal/isIterateeCall": 367,
        "../lang/isArray": 387
    }],
    281: [function (e, t, n) {
        function i(e, t, n) {
            if (null == e)return [];
            n && c(e, t, n) && (t = void 0);
            var i = -1;
            t = r(t, n, 3);
            var l = o(e, function (e, n, r) {
                return {criteria: t(e, n, r), index: ++i, value: e}
            });
            return a(l, s)
        }

        var r = e("../internal/baseCallback"), o = e("../internal/baseMap"), a = e("../internal/baseSortBy"), s = e("../internal/compareAscending"), c = e("../internal/isIterateeCall");
        t.exports = i
    }, {
        "../internal/baseCallback": 300,
        "../internal/baseMap": 321,
        "../internal/baseSortBy": 332,
        "../internal/compareAscending": 339,
        "../internal/isIterateeCall": 367
    }],
    282: [function (e, t, n) {
        var i = e("../internal/getNative"), r = i(Date, "now"), o = r || function () {
                return (new Date).getTime()
            };
        t.exports = o
    }, {"../internal/getNative": 363}],
    283: [function (e, t, n) {
        var i = e("../internal/createWrapper"), r = e("../internal/replaceHolders"), o = e("./restParam"), a = 1, s = 32, c = o(function (e, t, n) {
            var o = a;
            if (n.length) {
                var l = r(n, c.placeholder);
                o |= s
            }
            return i(e, o, t, n, l)
        });
        c.placeholder = {}, t.exports = c
    }, {"../internal/createWrapper": 355, "../internal/replaceHolders": 379, "./restParam": 286}],
    284: [function (e, t, n) {
        function i(e, t, n) {
            function i() {
                v && clearTimeout(v), h && clearTimeout(h), b = 0, h = v = y = void 0
            }

            function c(t, n) {
                n && clearTimeout(n), h = v = y = void 0, t && (b = o(), f = e.apply(g, d), v || h || (d = g = void 0))
            }

            function l() {
                var e = t - (o() - m);
                0 >= e || e > t ? c(y, h) : v = setTimeout(l, e)
            }

            function u() {
                c(E, v)
            }

            function p() {
                if (d = arguments, m = o(), g = this, y = E && (v || !w), x === !1)var n = w && !v; else {
                    h || w || (b = m);
                    var i = x - (m - b), r = 0 >= i || i > x;
                    r ? (h && (h = clearTimeout(h)), b = m, f = e.apply(g, d)) : h || (h = setTimeout(u, i))
                }
                return r && v ? v = clearTimeout(v) : v || t === x || (v = setTimeout(l, t)), n && (r = !0, f = e.apply(g, d)), !r || v || h || (d = g = void 0), f
            }

            var d, h, f, m, g, v, y, b = 0, x = !1, E = !0;
            if ("function" != typeof e)throw new TypeError(a);
            if (t = 0 > t ? 0 : +t || 0, n === !0) {
                var w = !0;
                E = !1
            } else r(n) && (w = !!n.leading, x = "maxWait"in n && s(+n.maxWait || 0, t), E = "trailing"in n ? !!n.trailing : E);
            return p.cancel = i, p
        }

        var r = e("../lang/isObject"), o = e("../date/now"), a = "Expected a function", s = Math.max;
        t.exports = i
    }, {"../date/now": 282, "../lang/isObject": 391}],
    285: [function (e, t, n) {
        var i = e("../internal/baseDelay"), r = e("./restParam"), o = r(function (e, t) {
            return i(e, 1, t)
        });
        t.exports = o
    }, {"../internal/baseDelay": 304, "./restParam": 286}],
    286: [function (e, t, n) {
        function i(e, t) {
            if ("function" != typeof e)throw new TypeError(r);
            return t = o(void 0 === t ? e.length - 1 : +t || 0, 0), function () {
                for (var n = arguments, i = -1, r = o(n.length - t, 0), a = Array(r); ++i < r;)a[i] = n[t + i];
                switch (t) {
                    case 0:
                        return e.call(this, a);
                    case 1:
                        return e.call(this, n[0], a);
                    case 2:
                        return e.call(this, n[0], n[1], a)
                }
                var s = Array(t + 1);
                for (i = -1; ++i < t;)s[i] = n[i];
                return s[t] = a, e.apply(this, s)
            }
        }

        var r = "Expected a function", o = Math.max;
        t.exports = i
    }, {}],
    287: [function (e, t, n) {
        function i(e) {
            this.__wrapped__ = e, this.__actions__ = [], this.__dir__ = 1, this.__filtered__ = !1, this.__iteratees__ = [], this.__takeCount__ = a, this.__views__ = []
        }

        var r = e("./baseCreate"), o = e("./baseLodash"), a = Number.POSITIVE_INFINITY;
        i.prototype = r(o.prototype), i.prototype.constructor = i, t.exports = i
    }, {"./baseCreate": 303, "./baseLodash": 320}],
    288: [function (e, t, n) {
        function i(e, t, n) {
            this.__wrapped__ = e, this.__actions__ = n || [], this.__chain__ = !!t
        }

        var r = e("./baseCreate"), o = e("./baseLodash");
        i.prototype = r(o.prototype), i.prototype.constructor = i, t.exports = i
    }, {"./baseCreate": 303, "./baseLodash": 320}],
    289: [function (e, t, n) {
        (function (n) {
            function i(e) {
                var t = e ? e.length : 0;
                for (this.data = {hash: s(null), set: new a}; t--;)this.push(e[t])
            }

            var r = e("./cachePush"), o = e("./getNative"), a = o(n, "Set"), s = o(Object, "create");
            i.prototype.push = r, t.exports = i
        }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
    }, {"./cachePush": 338, "./getNative": 363}],
    290: [function (e, t, n) {
        function i(e, t) {
            var n = -1, i = e.length;
            for (t || (t = Array(i)); ++n < i;)t[n] = e[n];
            return t
        }

        t.exports = i
    }, {}],
    291: [function (e, t, n) {
        function i(e, t) {
            for (var n = -1, i = e.length; ++n < i && t(e[n], n, e) !== !1;);
            return e
        }

        t.exports = i
    }, {}],
    292: [function (e, t, n) {
        function i(e, t) {
            for (var n = -1, i = e.length; ++n < i;)if (!t(e[n], n, e))return !1;
            return !0
        }

        t.exports = i
    }, {}],
    293: [function (e, t, n) {
        function i(e, t) {
            for (var n = -1, i = e.length, r = -1, o = []; ++n < i;) {
                var a = e[n];
                t(a, n, e) && (o[++r] = a)
            }
            return o
        }

        t.exports = i
    }, {}],
    294: [function (e, t, n) {
        function i(e, t) {
            for (var n = -1, i = e.length, r = Array(i); ++n < i;)r[n] = t(e[n], n, e);
            return r
        }

        t.exports = i
    }, {}],
    295: [function (e, t, n) {
        function i(e, t) {
            for (var n = -1, i = t.length, r = e.length; ++n < i;)e[r + n] = t[n];
            return e
        }

        t.exports = i
    }, {}],
    296: [function (e, t, n) {
        function i(e, t, n, i) {
            var r = -1, o = e.length;
            for (i && o && (n = e[++r]); ++r < o;)n = t(n, e[r], r, e);
            return n
        }

        t.exports = i
    }, {}],
    297: [function (e, t, n) {
        function i(e, t) {
            for (var n = -1, i = e.length; ++n < i;)if (t(e[n], n, e))return !0;
            return !1
        }

        t.exports = i
    }, {}],
    298: [function (e, t, n) {
        function i(e, t, n) {
            for (var i = -1, o = r(t), a = o.length; ++i < a;) {
                var s = o[i], c = e[s], l = n(c, t[s], s, e, t);
                (l === l ? l === c : c !== c) && (void 0 !== c || s in e) || (e[s] = l)
            }
            return e
        }

        var r = e("../object/keys");
        t.exports = i
    }, {"../object/keys": 397}],
    299: [function (e, t, n) {
        function i(e, t) {
            return null == t ? e : r(t, o(t), e)
        }

        var r = e("./baseCopy"), o = e("../object/keys");
        t.exports = i
    }, {"../object/keys": 397, "./baseCopy": 302}],
    300: [function (e, t, n) {
        function i(e, t, n) {
            var i = typeof e;
            return "function" == i ? void 0 === t ? e : a(e, t, n) : null == e ? s : "object" == i ? r(e) : void 0 === t ? c(e) : o(e, t)
        }

        var r = e("./baseMatches"), o = e("./baseMatchesProperty"), a = e("./bindCallback"), s = e("../utility/identity"), c = e("../utility/property");
        t.exports = i
    }, {
        "../utility/identity": 405,
        "../utility/property": 407,
        "./baseMatches": 322,
        "./baseMatchesProperty": 323,
        "./bindCallback": 336
    }],
    301: [function (e, t, n) {
        function i(e, t) {
            if (e !== t) {
                var n = null === e, i = void 0 === e, r = e === e, o = null === t, a = void 0 === t, s = t === t;
                if (e > t && !o || !r || n && !a && s || i && s)return 1;
                if (t > e && !n || !s || o && !i && r || a && r)return -1
            }
            return 0
        }

        t.exports = i
    }, {}],
    302: [function (e, t, n) {
        function i(e, t, n) {
            n || (n = {});
            for (var i = -1, r = t.length; ++i < r;) {
                var o = t[i];
                n[o] = e[o]
            }
            return n
        }

        t.exports = i
    }, {}],
    303: [function (e, t, n) {
        var i = e("../lang/isObject"), r = function () {
            function e() {
            }

            return function (t) {
                if (i(t)) {
                    e.prototype = t;
                    var n = new e;
                    e.prototype = void 0
                }
                return n || {}
            }
        }();
        t.exports = r
    }, {"../lang/isObject": 391}],
    304: [function (e, t, n) {
        function i(e, t, n) {
            if ("function" != typeof e)throw new TypeError(r);
            return setTimeout(function () {
                e.apply(void 0, n)
            }, t)
        }

        var r = "Expected a function";
        t.exports = i
    }, {}],
    305: [function (e, t, n) {
        function i(e, t) {
            var n = e ? e.length : 0, i = [];
            if (!n)return i;
            var c = -1, l = r, u = !0, p = u && t.length >= s ? a(t) : null, d = t.length;
            p && (l = o, u = !1, t = p);
            e:for (; ++c < n;) {
                var h = e[c];
                if (u && h === h) {
                    for (var f = d; f--;)if (t[f] === h)continue e;
                    i.push(h)
                } else l(t, h, 0) < 0 && i.push(h)
            }
            return i
        }

        var r = e("./baseIndexOf"), o = e("./cacheIndexOf"), a = e("./createCache"), s = 200;
        t.exports = i
    }, {"./baseIndexOf": 316, "./cacheIndexOf": 337, "./createCache": 347}],
    306: [function (e, t, n) {
        var i = e("./baseForOwn"), r = e("./createBaseEach"), o = r(i);
        t.exports = o
    }, {"./baseForOwn": 314, "./createBaseEach": 344}],
    307: [function (e, t, n) {
        function i(e, t) {
            var n = !0;
            return r(e, function (e, i, r) {
                return n = !!t(e, i, r)
            }), n
        }

        var r = e("./baseEach");
        t.exports = i
    }, {"./baseEach": 306}],
    308: [function (e, t, n) {
        function i(e, t) {
            var n = [];
            return r(e, function (e, i, r) {
                t(e, i, r) && n.push(e)
            }), n
        }

        var r = e("./baseEach");
        t.exports = i
    }, {"./baseEach": 306}],
    309: [function (e, t, n) {
        function i(e, t, n, i) {
            var r;
            return n(e, function (e, n, o) {
                return t(e, n, o) ? (r = i ? n : e, !1) : void 0
            }), r
        }

        t.exports = i
    }, {}],
    310: [function (e, t, n) {
        function i(e, t, n) {
            for (var i = e.length, r = n ? i : -1; n ? r-- : ++r < i;)if (t(e[r], r, e))return r;
            return -1
        }

        t.exports = i
    }, {}],
    311: [function (e, t, n) {
        function i(e, t, n, l) {
            l || (l = []);
            for (var u = -1, p = e.length; ++u < p;) {
                var d = e[u];
                c(d) && s(d) && (n || a(d) || o(d)) ? t ? i(d, t, n, l) : r(l, d) : n || (l[l.length] = d)
            }
            return l
        }

        var r = e("./arrayPush"), o = e("../lang/isArguments"), a = e("../lang/isArray"), s = e("./isArrayLike"), c = e("./isObjectLike");
        t.exports = i
    }, {
        "../lang/isArguments": 386,
        "../lang/isArray": 387,
        "./arrayPush": 295,
        "./isArrayLike": 365,
        "./isObjectLike": 371
    }],
    312: [function (e, t, n) {
        var i = e("./createBaseFor"), r = i();
        t.exports = r
    }, {"./createBaseFor": 345}],
    313: [function (e, t, n) {
        function i(e, t) {
            return r(e, t, o)
        }

        var r = e("./baseFor"), o = e("../object/keysIn");
        t.exports = i
    }, {"../object/keysIn": 398, "./baseFor": 312}],
    314: [function (e, t, n) {
        function i(e, t) {
            return r(e, t, o)
        }

        var r = e("./baseFor"), o = e("../object/keys");
        t.exports = i
    }, {"../object/keys": 397, "./baseFor": 312}],
    315: [function (e, t, n) {
        function i(e, t, n) {
            if (null != e) {
                void 0 !== n && n in r(e) && (t = [n]);
                for (var i = 0, o = t.length; null != e && o > i;)e = e[t[i++]];
                return i && i == o ? e : void 0
            }
        }

        var r = e("./toObject");
        t.exports = i
    }, {"./toObject": 383}],
    316: [function (e, t, n) {
        function i(e, t, n) {
            if (t !== t)return r(e, n);
            for (var i = n - 1, o = e.length; ++i < o;)if (e[i] === t)return i;
            return -1
        }

        var r = e("./indexOfNaN");
        t.exports = i
    }, {"./indexOfNaN": 364}],
    317: [function (e, t, n) {
        function i(e, t, n, s, c, l) {
            return e === t ? !0 : null == e || null == t || !o(e) && !a(t) ? e !== e && t !== t : r(e, t, i, n, s, c, l)
        }

        var r = e("./baseIsEqualDeep"), o = e("../lang/isObject"), a = e("./isObjectLike");
        t.exports = i
    }, {"../lang/isObject": 391, "./baseIsEqualDeep": 318, "./isObjectLike": 371}],
    318: [function (e, t, n) {
        function i(e, t, n, i, d, m, g) {
            var v = s(e), y = s(t), b = u, x = u;
            v || (b = f.call(e), b == l ? b = p : b != p && (v = c(e))), y || (x = f.call(t), x == l ? x = p : x != p && (y = c(t)));
            var E = b == p, w = x == p, _ = b == x;
            if (_ && !v && !E)return o(e, t, b);
            if (!d) {
                var S = E && h.call(e, "__wrapped__"), C = w && h.call(t, "__wrapped__");
                if (S || C)return n(S ? e.value() : e, C ? t.value() : t, i, d, m, g)
            }
            if (!_)return !1;
            m || (m = []), g || (g = []);
            for (var A = m.length; A--;)if (m[A] == e)return g[A] == t;
            m.push(e), g.push(t);
            var T = (v ? r : a)(e, t, n, i, d, m, g);
            return m.pop(), g.pop(), T
        }

        var r = e("./equalArrays"), o = e("./equalByTag"), a = e("./equalObjects"), s = e("../lang/isArray"), c = e("../lang/isTypedArray"), l = "[object Arguments]", u = "[object Array]", p = "[object Object]", d = Object.prototype, h = d.hasOwnProperty, f = d.toString;
        t.exports = i
    }, {
        "../lang/isArray": 387,
        "../lang/isTypedArray": 394,
        "./equalArrays": 356,
        "./equalByTag": 357,
        "./equalObjects": 358
    }],
    319: [function (e, t, n) {
        function i(e, t, n) {
            var i = t.length, a = i, s = !n;
            if (null == e)return !a;
            for (e = o(e); i--;) {
                var c = t[i];
                if (s && c[2] ? c[1] !== e[c[0]] : !(c[0]in e))return !1
            }
            for (; ++i < a;) {
                c = t[i];
                var l = c[0], u = e[l], p = c[1];
                if (s && c[2]) {
                    if (void 0 === u && !(l in e))return !1
                } else {
                    var d = n ? n(u, p, l) : void 0;
                    if (!(void 0 === d ? r(p, u, n, !0) : d))return !1
                }
            }
            return !0
        }

        var r = e("./baseIsEqual"), o = e("./toObject");
        t.exports = i
    }, {"./baseIsEqual": 317, "./toObject": 383}],
    320: [function (e, t, n) {
        function i() {
        }

        t.exports = i
    }, {}],
    321: [function (e, t, n) {
        function i(e, t) {
            var n = -1, i = o(e) ? Array(e.length) : [];
            return r(e, function (e, r, o) {
                i[++n] = t(e, r, o)
            }), i
        }

        var r = e("./baseEach"), o = e("./isArrayLike");
        t.exports = i
    }, {"./baseEach": 306, "./isArrayLike": 365}],
    322: [function (e, t, n) {
        function i(e) {
            var t = o(e);
            if (1 == t.length && t[0][2]) {
                var n = t[0][0], i = t[0][1];
                return function (e) {
                    return null == e ? !1 : e[n] === i && (void 0 !== i || n in a(e))
                }
            }
            return function (e) {
                return r(e, t)
            }
        }

        var r = e("./baseIsMatch"), o = e("./getMatchData"), a = e("./toObject");
        t.exports = i
    }, {"./baseIsMatch": 319, "./getMatchData": 362, "./toObject": 383}],
    323: [function (e, t, n) {
        function i(e, t) {
            var n = s(e), i = c(e) && l(t), h = e + "";
            return e = d(e), function (s) {
                if (null == s)return !1;
                var c = h;
                if (s = p(s), (n || !i) && !(c in s)) {
                    if (s = 1 == e.length ? s : r(s, a(e, 0, -1)), null == s)return !1;
                    c = u(e), s = p(s)
                }
                return s[c] === t ? void 0 !== t || c in s : o(t, s[c], void 0, !0)
            }
        }

        var r = e("./baseGet"), o = e("./baseIsEqual"), a = e("./baseSlice"), s = e("../lang/isArray"), c = e("./isKey"), l = e("./isStrictComparable"), u = e("../array/last"), p = e("./toObject"), d = e("./toPath");
        t.exports = i
    }, {
        "../array/last": 265,
        "../lang/isArray": 387,
        "./baseGet": 315,
        "./baseIsEqual": 317,
        "./baseSlice": 330,
        "./isKey": 368,
        "./isStrictComparable": 372,
        "./toObject": 383,
        "./toPath": 384
    }],
    324: [function (e, t, n) {
        function i(e, t, n, d, h) {
            if (!c(e))return e;
            var f = s(t) && (a(t) || u(t)), m = f ? void 0 : p(t);
            return r(m || t, function (r, a) {
                if (m && (a = r, r = t[a]), l(r))d || (d = []), h || (h = []), o(e, t, a, i, n, d, h); else {
                    var s = e[a], c = n ? n(s, r, a, e, t) : void 0, u = void 0 === c;
                    u && (c = r), void 0 === c && (!f || a in e) || !u && (c === c ? c === s : s !== s) || (e[a] = c)
                }
            }), e
        }

        var r = e("./arrayEach"), o = e("./baseMergeDeep"), a = e("../lang/isArray"), s = e("./isArrayLike"), c = e("../lang/isObject"), l = e("./isObjectLike"), u = e("../lang/isTypedArray"), p = e("../object/keys");
        t.exports = i
    }, {
        "../lang/isArray": 387,
        "../lang/isObject": 391,
        "../lang/isTypedArray": 394,
        "../object/keys": 397,
        "./arrayEach": 291,
        "./baseMergeDeep": 325,
        "./isArrayLike": 365,
        "./isObjectLike": 371
    }],
    325: [function (e, t, n) {
        function i(e, t, n, i, p, d, h) {
            for (var f = d.length, m = t[n]; f--;)if (d[f] == m)return void(e[n] = h[f]);
            var g = e[n], v = p ? p(g, m, n, e, t) : void 0, y = void 0 === v;
            y && (v = m, s(m) && (a(m) || l(m)) ? v = a(g) ? g : s(g) ? r(g) : [] : c(m) || o(m) ? v = o(g) ? u(g) : c(g) ? g : {} : y = !1), d.push(m), h.push(v), y ? e[n] = i(v, m, p, d, h) : (v === v ? v !== g : g === g) && (e[n] = v)
        }

        var r = e("./arrayCopy"), o = e("../lang/isArguments"), a = e("../lang/isArray"), s = e("./isArrayLike"), c = e("../lang/isPlainObject"), l = e("../lang/isTypedArray"), u = e("../lang/toPlainObject");
        t.exports = i
    }, {
        "../lang/isArguments": 386,
        "../lang/isArray": 387,
        "../lang/isPlainObject": 392,
        "../lang/isTypedArray": 394,
        "../lang/toPlainObject": 395,
        "./arrayCopy": 290,
        "./isArrayLike": 365
    }],
    326: [function (e, t, n) {
        function i(e) {
            return function (t) {
                return null == t ? void 0 : t[e]
            }
        }

        t.exports = i
    }, {}],
    327: [function (e, t, n) {
        function i(e) {
            var t = e + "";
            return e = o(e), function (n) {
                return r(n, e, t)
            }
        }

        var r = e("./baseGet"), o = e("./toPath");
        t.exports = i
    }, {"./baseGet": 315, "./toPath": 384}],
    328: [function (e, t, n) {
        function i(e, t, n, i, r) {
            return r(e, function (e, r, o) {
                n = i ? (i = !1, e) : t(n, e, r, o)
            }), n
        }

        t.exports = i
    }, {}],
    329: [function (e, t, n) {
        var i = e("../utility/identity"), r = e("./metaMap"), o = r ? function (e, t) {
            return r.set(e, t), e
        } : i;
        t.exports = o
    }, {"../utility/identity": 405, "./metaMap": 374}],
    330: [function (e, t, n) {
        function i(e, t, n) {
            var i = -1, r = e.length;
            t = null == t ? 0 : +t || 0, 0 > t && (t = -t > r ? 0 : r + t), n = void 0 === n || n > r ? r : +n || 0, 0 > n && (n += r), r = t > n ? 0 : n - t >>> 0, t >>>= 0;
            for (var o = Array(r); ++i < r;)o[i] = e[i + t];
            return o
        }

        t.exports = i
    }, {}],
    331: [function (e, t, n) {
        function i(e, t) {
            var n;
            return r(e, function (e, i, r) {
                return n = t(e, i, r), !n
            }), !!n
        }

        var r = e("./baseEach");
        t.exports = i
    }, {"./baseEach": 306}],
    332: [function (e, t, n) {
        function i(e, t) {
            var n = e.length;
            for (e.sort(t); n--;)e[n] = e[n].value;
            return e
        }

        t.exports = i
    }, {}],
    333: [function (e, t, n) {
        function i(e) {
            return null == e ? "" : e + ""
        }

        t.exports = i
    }, {}],
    334: [function (e, t, n) {
        function i(e, t) {
            var n = -1, i = r, c = e.length, l = !0, u = l && c >= s, p = u ? a() : null, d = [];
            p ? (i = o, l = !1) : (u = !1, p = t ? [] : d);
            e:for (; ++n < c;) {
                var h = e[n], f = t ? t(h, n, e) : h;
                if (l && h === h) {
                    for (var m = p.length; m--;)if (p[m] === f)continue e;
                    t && p.push(f), d.push(h)
                } else i(p, f, 0) < 0 && ((t || u) && p.push(f), d.push(h))
            }
            return d
        }

        var r = e("./baseIndexOf"), o = e("./cacheIndexOf"), a = e("./createCache"), s = 200;
        t.exports = i
    }, {"./baseIndexOf": 316, "./cacheIndexOf": 337, "./createCache": 347}],
    335: [function (e, t, n) {
        function i(e, t) {
            for (var n = -1, i = t.length, r = Array(i); ++n < i;)r[n] = e[t[n]];
            return r
        }

        t.exports = i
    }, {}],
    336: [function (e, t, n) {
        function i(e, t, n) {
            if ("function" != typeof e)return r;
            if (void 0 === t)return e;
            switch (n) {
                case 1:
                    return function (n) {
                        return e.call(t, n)
                    };
                case 3:
                    return function (n, i, r) {
                        return e.call(t, n, i, r)
                    };
                case 4:
                    return function (n, i, r, o) {
                        return e.call(t, n, i, r, o)
                    };
                case 5:
                    return function (n, i, r, o, a) {
                        return e.call(t, n, i, r, o, a)
                    }
            }
            return function () {
                return e.apply(t, arguments)
            }
        }

        var r = e("../utility/identity");
        t.exports = i
    }, {"../utility/identity": 405}],
    337: [function (e, t, n) {
        function i(e, t) {
            var n = e.data, i = "string" == typeof t || r(t) ? n.set.has(t) : n.hash[t];
            return i ? 0 : -1
        }

        var r = e("../lang/isObject");
        t.exports = i
    }, {"../lang/isObject": 391}],
    338: [function (e, t, n) {
        function i(e) {
            var t = this.data;
            "string" == typeof e || r(e) ? t.set.add(e) : t.hash[e] = !0
        }

        var r = e("../lang/isObject");
        t.exports = i
    }, {"../lang/isObject": 391}],
    339: [function (e, t, n) {
        function i(e, t) {
            return r(e.criteria, t.criteria) || e.index - t.index
        }

        var r = e("./baseCompareAscending");
        t.exports = i
    }, {"./baseCompareAscending": 301}],
    340: [function (e, t, n) {
        function i(e, t, n) {
            for (var i = n.length, o = -1, a = r(e.length - i, 0), s = -1, c = t.length, l = Array(c + a); ++s < c;)l[s] = t[s];
            for (; ++o < i;)l[n[o]] = e[o];
            for (; a--;)l[s++] = e[o++];
            return l
        }

        var r = Math.max;
        t.exports = i
    }, {}],
    341: [function (e, t, n) {
        function i(e, t, n) {
            for (var i = -1, o = n.length, a = -1, s = r(e.length - o, 0), c = -1, l = t.length, u = Array(s + l); ++a < s;)u[a] = e[a];
            for (var p = a; ++c < l;)u[p + c] = t[c];
            for (; ++i < o;)u[p + n[i]] = e[a++];
            return u
        }

        var r = Math.max;
        t.exports = i
    }, {}],
    342: [function (e, t, n) {
        function i(e, t) {
            return function (n, i, s) {
                var c = t ? t() : {};
                if (i = r(i, s, 3), a(n))for (var l = -1, u = n.length; ++l < u;) {
                    var p = n[l];
                    e(c, p, i(p, l, n), n)
                } else o(n, function (t, n, r) {
                    e(c, t, i(t, n, r), r)
                });
                return c
            }
        }

        var r = e("./baseCallback"), o = e("./baseEach"), a = e("../lang/isArray");
        t.exports = i
    }, {"../lang/isArray": 387, "./baseCallback": 300, "./baseEach": 306}],
    343: [function (e, t, n) {
        function i(e) {
            return a(function (t, n) {
                var i = -1, a = null == t ? 0 : n.length, s = a > 2 ? n[a - 2] : void 0, c = a > 2 ? n[2] : void 0, l = a > 1 ? n[a - 1] : void 0;
                for ("function" == typeof s ? (s = r(s, l, 5), a -= 2) : (s = "function" == typeof l ? l : void 0, a -= s ? 1 : 0), c && o(n[0], n[1], c) && (s = 3 > a ? void 0 : s, a = 1); ++i < a;) {
                    var u = n[i];
                    u && e(t, u, s)
                }
                return t
            })
        }

        var r = e("./bindCallback"), o = e("./isIterateeCall"), a = e("../function/restParam");
        t.exports = i
    }, {"../function/restParam": 286, "./bindCallback": 336, "./isIterateeCall": 367}],
    344: [function (e, t, n) {
        function i(e, t) {
            return function (n, i) {
                var s = n ? r(n) : 0;
                if (!o(s))return e(n, i);
                for (var c = t ? s : -1, l = a(n); (t ? c-- : ++c < s) && i(l[c], c, l) !== !1;);
                return n
            }
        }

        var r = e("./getLength"), o = e("./isLength"), a = e("./toObject");
        t.exports = i
    }, {"./getLength": 361, "./isLength": 370, "./toObject": 383}],
    345: [function (e, t, n) {
        function i(e) {
            return function (t, n, i) {
                for (var o = r(t), a = i(t), s = a.length, c = e ? s : -1; e ? c-- : ++c < s;) {
                    var l = a[c];
                    if (n(o[l], l, o) === !1)break
                }
                return t
            }
        }

        var r = e("./toObject");
        t.exports = i
    }, {"./toObject": 383}],
    346: [function (e, t, n) {
        (function (n) {
            function i(e, t) {
                function i() {
                    var r = this && this !== n && this instanceof i ? o : e;
                    return r.apply(t, arguments)
                }

                var o = r(e);
                return i
            }

            var r = e("./createCtorWrapper");
            t.exports = i
        }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
    }, {"./createCtorWrapper": 348}],
    347: [function (e, t, n) {
        (function (n) {
            function i(e) {
                return s && a ? new r(e) : null
            }

            var r = e("./SetCache"), o = e("./getNative"), a = o(n, "Set"), s = o(Object, "create");
            t.exports = i
        }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
    }, {"./SetCache": 289, "./getNative": 363}],
    348: [function (e, t, n) {
        function i(e) {
            return function () {
                var t = arguments;
                switch (t.length) {
                    case 0:
                        return new e;
                    case 1:
                        return new e(t[0]);
                    case 2:
                        return new e(t[0], t[1]);
                    case 3:
                        return new e(t[0], t[1], t[2]);
                    case 4:
                        return new e(t[0], t[1], t[2], t[3]);
                    case 5:
                        return new e(t[0], t[1], t[2], t[3], t[4]);
                    case 6:
                        return new e(t[0], t[1], t[2], t[3], t[4], t[5]);
                    case 7:
                        return new e(t[0], t[1], t[2], t[3], t[4], t[5], t[6])
                }
                var n = r(e.prototype), i = e.apply(n, t);
                return o(i) ? i : n
            }
        }

        var r = e("./baseCreate"), o = e("../lang/isObject");
        t.exports = i
    }, {"../lang/isObject": 391, "./baseCreate": 303}],
    349: [function (e, t, n) {
        function i(e, t) {
            return function (n, i, c) {
                if (i = r(i, c, 3), s(n)) {
                    var l = a(n, i, t);
                    return l > -1 ? n[l] : void 0
                }
                return o(n, i, e)
            }
        }

        var r = e("./baseCallback"), o = e("./baseFind"), a = e("./baseFindIndex"), s = e("../lang/isArray");
        t.exports = i
    }, {"../lang/isArray": 387, "./baseCallback": 300, "./baseFind": 309, "./baseFindIndex": 310}],
    350: [function (e, t, n) {
        function i(e) {
            return function (t, n, i) {
                return t && t.length ? (n = r(n, i, 3), o(t, n, e)) : -1
            }
        }

        var r = e("./baseCallback"), o = e("./baseFindIndex");
        t.exports = i
    }, {"./baseCallback": 300, "./baseFindIndex": 310}],
    351: [function (e, t, n) {
        function i(e, t) {
            return function (n, i, a) {
                return "function" == typeof i && void 0 === a && o(n) ? e(n, i) : t(n, r(i, a, 3))
            }
        }

        var r = e("./bindCallback"), o = e("../lang/isArray");
        t.exports = i
    }, {"../lang/isArray": 387, "./bindCallback": 336}],
    352: [function (e, t, n) {
        (function (n) {
            function i(e, t, E, w, _, S, C, A, T, j) {
                function N() {
                    for (var f = arguments.length, m = f, g = Array(f); m--;)g[m] = arguments[m];
                    if (w && (g = o(g, w, _)), S && (g = a(g, S, C)), D || B) {
                        var b = N.placeholder, O = u(g, b);
                        if (f -= O.length, j > f) {
                            var I = A ? r(A) : void 0, F = x(j - f, 0), U = D ? O : void 0, z = D ? void 0 : O, $ = D ? g : void 0, H = D ? void 0 : g;
                            t |= D ? v : y, t &= ~(D ? y : v), P || (t &= ~(d | h));
                            var q = [e, t, E, $, U, H, z, I, T, F], G = i.apply(void 0, q);
                            return c(e) && p(G, q), G.placeholder = b, G
                        }
                    }
                    var W = R ? E : this, V = M ? W[e] : e;
                    return A && (g = l(g, A)), k && T < g.length && (g.length = T), this && this !== n && this instanceof N && (V = L || s(e)), V.apply(W, g)
                }

                var k = t & b, R = t & d, M = t & h, D = t & m, P = t & f, B = t & g, L = M ? void 0 : s(e);
                return N
            }

            var r = e("./arrayCopy"), o = e("./composeArgs"), a = e("./composeArgsRight"), s = e("./createCtorWrapper"), c = e("./isLaziable"), l = e("./reorder"), u = e("./replaceHolders"), p = e("./setData"), d = 1, h = 2, f = 4, m = 8, g = 16, v = 32, y = 64, b = 128, x = Math.max;
            t.exports = i
        }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
    }, {
        "./arrayCopy": 290,
        "./composeArgs": 340,
        "./composeArgsRight": 341,
        "./createCtorWrapper": 348,
        "./isLaziable": 369,
        "./reorder": 378,
        "./replaceHolders": 379,
        "./setData": 380
    }],
    353: [function (e, t, n) {
        (function (n) {
            function i(e, t, i, a) {
                function s() {
                    for (var t = -1, r = arguments.length, o = -1, u = a.length, p = Array(u + r); ++o < u;)p[o] = a[o];
                    for (; r--;)p[o++] = arguments[++t];
                    var d = this && this !== n && this instanceof s ? l : e;
                    return d.apply(c ? i : this, p)
                }

                var c = t & o, l = r(e);
                return s
            }

            var r = e("./createCtorWrapper"), o = 1;
            t.exports = i
        }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
    }, {"./createCtorWrapper": 348}],
    354: [function (e, t, n) {
        function i(e, t) {
            return function (n, i, s, c) {
                var l = arguments.length < 3;
                return "function" == typeof i && void 0 === c && a(n) ? e(n, i, s, l) : o(n, r(i, c, 4), s, l, t)
            }
        }

        var r = e("./baseCallback"), o = e("./baseReduce"), a = e("../lang/isArray");
        t.exports = i
    }, {"../lang/isArray": 387, "./baseCallback": 300, "./baseReduce": 328}],
    355: [function (e, t, n) {
        function i(e, t, n, i, v, y, b, x) {
            var E = t & d;
            if (!E && "function" != typeof e)throw new TypeError(m);
            var w = i ? i.length : 0;
            if (w || (t &= ~(h | f), i = v = void 0), w -= v ? v.length : 0, t & f) {
                var _ = i, S = v;
                i = v = void 0
            }
            var C = E ? void 0 : c(e), A = [e, t, n, i, v, _, S, y, b, x];
            if (C && (l(A, C), t = A[1], x = A[9]), A[9] = null == x ? E ? 0 : e.length : g(x - w, 0) || 0, t == p)var T = o(A[0], A[2]); else T = t != h && t != (p | h) || A[4].length ? a.apply(void 0, A) : s.apply(void 0, A);
            var j = C ? r : u;
            return j(T, A)
        }

        var r = e("./baseSetData"), o = e("./createBindWrapper"), a = e("./createHybridWrapper"), s = e("./createPartialWrapper"), c = e("./getData"), l = e("./mergeData"), u = e("./setData"), p = 1, d = 2, h = 32, f = 64, m = "Expected a function", g = Math.max;
        t.exports = i
    }, {
        "./baseSetData": 329,
        "./createBindWrapper": 346,
        "./createHybridWrapper": 352,
        "./createPartialWrapper": 353,
        "./getData": 359,
        "./mergeData": 373,
        "./setData": 380
    }],
    356: [function (e, t, n) {
        function i(e, t, n, i, o, a, s) {
            var c = -1, l = e.length, u = t.length;
            if (l != u && !(o && u > l))return !1;
            for (; ++c < l;) {
                var p = e[c], d = t[c], h = i ? i(o ? d : p, o ? p : d, c) : void 0;
                if (void 0 !== h) {
                    if (h)continue;
                    return !1
                }
                if (o) {
                    if (!r(t, function (e) {
                            return p === e || n(p, e, i, o, a, s)
                        }))return !1
                } else if (p !== d && !n(p, d, i, o, a, s))return !1
            }
            return !0
        }

        var r = e("./arraySome");
        t.exports = i
    }, {"./arraySome": 297}],
    357: [function (e, t, n) {
        function i(e, t, n) {
            switch (n) {
                case r:
                case o:
                    return +e == +t;
                case a:
                    return e.name == t.name && e.message == t.message;
                case s:
                    return e != +e ? t != +t : e == +t;
                case c:
                case l:
                    return e == t + ""
            }
            return !1
        }

        var r = "[object Boolean]", o = "[object Date]", a = "[object Error]", s = "[object Number]", c = "[object RegExp]", l = "[object String]";
        t.exports = i
    }, {}],
    358: [function (e, t, n) {
        function i(e, t, n, i, o, s, c) {
            var l = r(e), u = l.length, p = r(t), d = p.length;
            if (u != d && !o)return !1;
            for (var h = u; h--;) {
                var f = l[h];
                if (!(o ? f in t : a.call(t, f)))return !1
            }
            for (var m = o; ++h < u;) {
                f = l[h];
                var g = e[f], v = t[f], y = i ? i(o ? v : g, o ? g : v, f) : void 0;
                if (!(void 0 === y ? n(g, v, i, o, s, c) : y))return !1;
                m || (m = "constructor" == f)
            }
            if (!m) {
                var b = e.constructor, x = t.constructor;
                if (b != x && "constructor"in e && "constructor"in t && !("function" == typeof b && b instanceof b && "function" == typeof x && x instanceof x))return !1
            }
            return !0
        }

        var r = e("../object/keys"), o = Object.prototype, a = o.hasOwnProperty;
        t.exports = i
    }, {"../object/keys": 397}],
    359: [function (e, t, n) {
        var i = e("./metaMap"), r = e("../utility/noop"), o = i ? function (e) {
            return i.get(e)
        } : r;
        t.exports = o
    }, {"../utility/noop": 406, "./metaMap": 374}],
    360: [function (e, t, n) {
        function i(e) {
            for (var t = e.name + "", n = r[t], i = n ? n.length : 0; i--;) {
                var o = n[i], a = o.func;
                if (null == a || a == e)return o.name
            }
            return t
        }

        var r = e("./realNames");
        t.exports = i
    }, {"./realNames": 377}],
    361: [function (e, t, n) {
        var i = e("./baseProperty"), r = i("length");
        t.exports = r
    }, {"./baseProperty": 326}],
    362: [function (e, t, n) {
        function i(e) {
            for (var t = o(e), n = t.length; n--;)t[n][2] = r(t[n][1]);
            return t
        }

        var r = e("./isStrictComparable"), o = e("../object/pairs");
        t.exports = i
    }, {"../object/pairs": 401, "./isStrictComparable": 372}],
    363: [function (e, t, n) {
        function i(e, t) {
            var n = null == e ? void 0 : e[t];
            return r(n) ? n : void 0
        }

        var r = e("../lang/isNative");
        t.exports = i
    }, {"../lang/isNative": 389}],
    364: [function (e, t, n) {
        function i(e, t, n) {
            for (var i = e.length, r = t + (n ? 0 : -1); n ? r-- : ++r < i;) {
                var o = e[r];
                if (o !== o)return r
            }
            return -1
        }

        t.exports = i
    }, {}],
    365: [function (e, t, n) {
        function i(e) {
            return null != e && o(r(e))
        }

        var r = e("./getLength"), o = e("./isLength");
        t.exports = i
    }, {"./getLength": 361, "./isLength": 370}],
    366: [function (e, t, n) {
        function i(e, t) {
            return e = "number" == typeof e || r.test(e) ? +e : -1, t = null == t ? o : t, e > -1 && e % 1 == 0 && t > e
        }

        var r = /^\d+$/, o = 9007199254740991;
        t.exports = i
    }, {}],
    367: [function (e, t, n) {
        function i(e, t, n) {
            if (!a(n))return !1;
            var i = typeof t;
            if ("number" == i ? r(n) && o(t, n.length) : "string" == i && t in n) {
                var s = n[t];
                return e === e ? e === s : s !== s
            }
            return !1
        }

        var r = e("./isArrayLike"), o = e("./isIndex"), a = e("../lang/isObject");
        t.exports = i
    }, {"../lang/isObject": 391, "./isArrayLike": 365, "./isIndex": 366}],
    368: [function (e, t, n) {
        function i(e, t) {
            var n = typeof e;
            if ("string" == n && s.test(e) || "number" == n)return !0;
            if (r(e))return !1;
            var i = !a.test(e);
            return i || null != t && e in o(t)
        }

        var r = e("../lang/isArray"), o = e("./toObject"), a = /\.|\[(?:[^[\]]*|(["'])(?:(?!\1)[^\n\\]|\\.)*?\1)\]/, s = /^\w*$/;
        t.exports = i
    }, {"../lang/isArray": 387, "./toObject": 383}],
    369: [function (e, t, n) {
        function i(e) {
            var t = a(e), n = s[t];
            if ("function" != typeof n || !(t in r.prototype))return !1;
            if (e === n)return !0;
            var i = o(n);
            return !!i && e === i[0]
        }

        var r = e("./LazyWrapper"), o = e("./getData"), a = e("./getFuncName"), s = e("../chain/lodash");
        t.exports = i
    }, {"../chain/lodash": 269, "./LazyWrapper": 287, "./getData": 359, "./getFuncName": 360}],
    370: [function (e, t, n) {
        function i(e) {
            return "number" == typeof e && e > -1 && e % 1 == 0 && r >= e
        }

        var r = 9007199254740991;
        t.exports = i
    }, {}],
    371: [function (e, t, n) {
        function i(e) {
            return !!e && "object" == typeof e
        }

        t.exports = i
    }, {}],
    372: [function (e, t, n) {
        function i(e) {
            return e === e && !r(e)
        }

        var r = e("../lang/isObject");
        t.exports = i
    }, {"../lang/isObject": 391}],
    373: [function (e, t, n) {
        function i(e, t) {
            var n = e[1], i = t[1], m = n | i, g = p > m, v = i == p && n == u || i == p && n == d && e[7].length <= t[8] || i == (p | d) && n == u;
            if (!g && !v)return e;
            i & c && (e[2] = t[2], m |= n & c ? 0 : l);
            var y = t[3];
            if (y) {
                var b = e[3];
                e[3] = b ? o(b, y, t[4]) : r(y), e[4] = b ? s(e[3], h) : r(t[4])
            }
            return y = t[5], y && (b = e[5], e[5] = b ? a(b, y, t[6]) : r(y), e[6] = b ? s(e[5], h) : r(t[6])), y = t[7], y && (e[7] = r(y)), i & p && (e[8] = null == e[8] ? t[8] : f(e[8], t[8])), null == e[9] && (e[9] = t[9]), e[0] = t[0], e[1] = m, e
        }

        var r = e("./arrayCopy"), o = e("./composeArgs"), a = e("./composeArgsRight"), s = e("./replaceHolders"), c = 1, l = 4, u = 8, p = 128, d = 256, h = "__lodash_placeholder__", f = Math.min;
        t.exports = i
    }, {"./arrayCopy": 290, "./composeArgs": 340, "./composeArgsRight": 341, "./replaceHolders": 379}],
    374: [function (e, t, n) {
        (function (n) {
            var i = e("./getNative"), r = i(n, "WeakMap"), o = r && new r;
            t.exports = o
        }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
    }, {"./getNative": 363}],
    375: [function (e, t, n) {
        function i(e, t) {
            e = r(e);
            for (var n = -1, i = t.length, o = {}; ++n < i;) {
                var a = t[n];
                a in e && (o[a] = e[a])
            }
            return o
        }

        var r = e("./toObject");
        t.exports = i
    }, {"./toObject": 383}],
    376: [function (e, t, n) {
        function i(e, t) {
            var n = {};
            return r(e, function (e, i, r) {
                t(e, i, r) && (n[i] = e)
            }), n
        }

        var r = e("./baseForIn");
        t.exports = i
    }, {"./baseForIn": 313}],
    377: [function (e, t, n) {
        var i = {};
        t.exports = i
    }, {}],
    378: [function (e, t, n) {
        function i(e, t) {
            for (var n = e.length, i = a(t.length, n), s = r(e); i--;) {
                var c = t[i];
                e[i] = o(c, n) ? s[c] : void 0
            }
            return e
        }

        var r = e("./arrayCopy"), o = e("./isIndex"), a = Math.min;
        t.exports = i
    }, {"./arrayCopy": 290, "./isIndex": 366}],
    379: [function (e, t, n) {
        function i(e, t) {
            for (var n = -1, i = e.length, o = -1, a = []; ++n < i;)e[n] === t && (e[n] = r, a[++o] = n);
            return a
        }

        var r = "__lodash_placeholder__";
        t.exports = i
    }, {}],
    380: [function (e, t, n) {
        var i = e("./baseSetData"), r = e("../date/now"), o = 150, a = 16, s = function () {
            var e = 0, t = 0;
            return function (n, s) {
                var c = r(), l = a - (c - t);
                if (t = c, l > 0) {
                    if (++e >= o)return n
                } else e = 0;
                return i(n, s)
            }
        }();
        t.exports = s
    }, {"../date/now": 282, "./baseSetData": 329}],
    381: [function (e, t, n) {
        function i(e) {
            for (var t = c(e), n = t.length, i = n && e.length, l = !!i && s(i) && (o(e) || r(e)), p = -1, d = []; ++p < n;) {
                var h = t[p];
                (l && a(h, i) || u.call(e, h)) && d.push(h)
            }
            return d
        }

        var r = e("../lang/isArguments"), o = e("../lang/isArray"), a = e("./isIndex"), s = e("./isLength"), c = e("../object/keysIn"), l = Object.prototype, u = l.hasOwnProperty;
        t.exports = i
    }, {
        "../lang/isArguments": 386,
        "../lang/isArray": 387,
        "../object/keysIn": 398,
        "./isIndex": 366,
        "./isLength": 370
    }],
    382: [function (e, t, n) {
        function i(e, t) {
            for (var n, i = -1, r = e.length, o = -1, a = []; ++i < r;) {
                var s = e[i], c = t ? t(s, i, e) : s;
                i && n === c || (n = c, a[++o] = s)
            }
            return a
        }

        t.exports = i
    }, {}],
    383: [function (e, t, n) {
        function i(e) {
            return r(e) ? e : Object(e)
        }

        var r = e("../lang/isObject");
        t.exports = i
    }, {"../lang/isObject": 391}],
    384: [function (e, t, n) {
        function i(e) {
            if (o(e))return e;
            var t = [];
            return r(e).replace(a, function (e, n, i, r) {
                t.push(i ? r.replace(s, "$1") : n || e)
            }), t
        }

        var r = e("./baseToString"), o = e("../lang/isArray"), a = /[^.[\]]+|\[(?:(-?\d+(?:\.\d+)?)|(["'])((?:(?!\2)[^\n\\]|\\.)*?)\2)\]/g, s = /\\(\\)?/g;
        t.exports = i
    }, {"../lang/isArray": 387, "./baseToString": 333}],
    385: [function (e, t, n) {
        function i(e) {
            return e instanceof r ? e.clone() : new o(e.__wrapped__, e.__chain__, a(e.__actions__))
        }

        var r = e("./LazyWrapper"), o = e("./LodashWrapper"), a = e("./arrayCopy");
        t.exports = i
    }, {"./LazyWrapper": 287, "./LodashWrapper": 288, "./arrayCopy": 290}],
    386: [function (e, t, n) {
        function i(e) {
            return o(e) && r(e) && s.call(e, "callee") && !c.call(e, "callee")
        }

        var r = e("../internal/isArrayLike"), o = e("../internal/isObjectLike"), a = Object.prototype, s = a.hasOwnProperty, c = a.propertyIsEnumerable;
        t.exports = i
    }, {"../internal/isArrayLike": 365, "../internal/isObjectLike": 371}],
    387: [function (e, t, n) {
        var i = e("../internal/getNative"), r = e("../internal/isLength"), o = e("../internal/isObjectLike"), a = "[object Array]", s = Object.prototype, c = s.toString, l = i(Array, "isArray"), u = l || function (e) {
                return o(e) && r(e.length) && c.call(e) == a
            };
        t.exports = u
    }, {"../internal/getNative": 363, "../internal/isLength": 370, "../internal/isObjectLike": 371}],
    388: [function (e, t, n) {
        function i(e) {
            return r(e) && s.call(e) == o
        }

        var r = e("./isObject"), o = "[object Function]", a = Object.prototype, s = a.toString;
        t.exports = i
    }, {"./isObject": 391}],
    389: [function (e, t, n) {
        function i(e) {
            return null == e ? !1 : r(e) ? u.test(c.call(e)) : o(e) && a.test(e)
        }

        var r = e("./isFunction"), o = e("../internal/isObjectLike"), a = /^\[object .+?Constructor\]$/, s = Object.prototype, c = Function.prototype.toString, l = s.hasOwnProperty, u = RegExp("^" + c.call(l).replace(/[\\^$.*+?()[\]{}|]/g, "\\$&").replace(/hasOwnProperty|(function).*?(?=\\\()| for .+?(?=\\\])/g, "$1.*?") + "$");
        t.exports = i
    }, {"../internal/isObjectLike": 371, "./isFunction": 388}],
    390: [function (e, t, n) {
        function i(e) {
            return "number" == typeof e || r(e) && s.call(e) == o
        }

        var r = e("../internal/isObjectLike"), o = "[object Number]", a = Object.prototype, s = a.toString;
        t.exports = i
    }, {"../internal/isObjectLike": 371}],
    391: [function (e, t, n) {
        function i(e) {
            var t = typeof e;
            return !!e && ("object" == t || "function" == t)
        }

        t.exports = i
    }, {}],
    392: [function (e, t, n) {
        function i(e) {
            var t;
            if (!a(e) || u.call(e) != s || o(e) || !l.call(e, "constructor") && (t = e.constructor, "function" == typeof t && !(t instanceof t)))return !1;
            var n;
            return r(e, function (e, t) {
                n = t
            }), void 0 === n || l.call(e, n)
        }

        var r = e("../internal/baseForIn"), o = e("./isArguments"), a = e("../internal/isObjectLike"), s = "[object Object]", c = Object.prototype, l = c.hasOwnProperty, u = c.toString;
        t.exports = i
    }, {"../internal/baseForIn": 313, "../internal/isObjectLike": 371, "./isArguments": 386}],
    393: [function (e, t, n) {
        function i(e) {
            return "string" == typeof e || r(e) && s.call(e) == o
        }

        var r = e("../internal/isObjectLike"), o = "[object String]", a = Object.prototype, s = a.toString;
        t.exports = i
    }, {"../internal/isObjectLike": 371}],
    394: [function (e, t, n) {
        function i(e) {
            return o(e) && r(e.length) && !!N[R.call(e)]
        }

        var r = e("../internal/isLength"), o = e("../internal/isObjectLike"), a = "[object Arguments]", s = "[object Array]", c = "[object Boolean]", l = "[object Date]", u = "[object Error]", p = "[object Function]", d = "[object Map]", h = "[object Number]", f = "[object Object]", m = "[object RegExp]", g = "[object Set]", v = "[object String]", y = "[object WeakMap]", b = "[object ArrayBuffer]", x = "[object Float32Array]", E = "[object Float64Array]", w = "[object Int8Array]", _ = "[object Int16Array]", S = "[object Int32Array]", C = "[object Uint8Array]", A = "[object Uint8ClampedArray]", T = "[object Uint16Array]", j = "[object Uint32Array]", N = {};
        N[x] = N[E] = N[w] = N[_] = N[S] = N[C] = N[A] = N[T] = N[j] = !0, N[a] = N[s] = N[b] = N[c] = N[l] = N[u] = N[p] = N[d] = N[h] = N[f] = N[m] = N[g] = N[v] = N[y] = !1;
        var k = Object.prototype, R = k.toString;
        t.exports = i
    }, {"../internal/isLength": 370, "../internal/isObjectLike": 371}],
    395: [function (e, t, n) {
        function i(e) {
            return r(e, o(e))
        }

        var r = e("../internal/baseCopy"), o = e("../object/keysIn");
        t.exports = i
    }, {"../internal/baseCopy": 302, "../object/keysIn": 398}],
    396: [function (e, t, n) {
        var i = e("../internal/assignWith"), r = e("../internal/baseAssign"), o = e("../internal/createAssigner"), a = o(function (e, t, n) {
            return n ? i(e, t, n) : r(e, t)
        });
        t.exports = a
    }, {"../internal/assignWith": 298, "../internal/baseAssign": 299, "../internal/createAssigner": 343}],
    397: [function (e, t, n) {
        var i = e("../internal/getNative"), r = e("../internal/isArrayLike"), o = e("../lang/isObject"), a = e("../internal/shimKeys"), s = i(Object, "keys"), c = s ? function (e) {
            var t = null == e ? void 0 : e.constructor;
            return "function" == typeof t && t.prototype === e || "function" != typeof e && r(e) ? a(e) : o(e) ? s(e) : []
        } : a;
        t.exports = c
    }, {
        "../internal/getNative": 363,
        "../internal/isArrayLike": 365,
        "../internal/shimKeys": 381,
        "../lang/isObject": 391
    }],
    398: [function (e, t, n) {
        function i(e) {
            if (null == e)return [];
            c(e) || (e = Object(e));
            var t = e.length;
            t = t && s(t) && (o(e) || r(e)) && t || 0;
            for (var n = e.constructor, i = -1, l = "function" == typeof n && n.prototype === e, p = Array(t), d = t > 0; ++i < t;)p[i] = i + "";
            for (var h in e)d && a(h, t) || "constructor" == h && (l || !u.call(e, h)) || p.push(h);
            return p
        }

        var r = e("../lang/isArguments"), o = e("../lang/isArray"), a = e("../internal/isIndex"), s = e("../internal/isLength"), c = e("../lang/isObject"), l = Object.prototype, u = l.hasOwnProperty;
        t.exports = i
    }, {
        "../internal/isIndex": 366,
        "../internal/isLength": 370,
        "../lang/isArguments": 386,
        "../lang/isArray": 387,
        "../lang/isObject": 391
    }],
    399: [function (e, t, n) {
        var i = e("../internal/baseMerge"), r = e("../internal/createAssigner"), o = r(i);
        t.exports = o
    }, {"../internal/baseMerge": 324, "../internal/createAssigner": 343}],
    400: [function (e, t, n) {
        var i = e("../internal/arrayMap"), r = e("../internal/baseDifference"), o = e("../internal/baseFlatten"), a = e("../internal/bindCallback"), s = e("./keysIn"), c = e("../internal/pickByArray"), l = e("../internal/pickByCallback"), u = e("../function/restParam"), p = u(function (e, t) {
            if (null == e)return {};
            if ("function" != typeof t[0]) {
                var t = i(o(t), String);
                return c(e, r(s(e), t))
            }
            var n = a(t[0], t[1], 3);
            return l(e, function (e, t, i) {
                return !n(e, t, i)
            })
        });
        t.exports = p
    }, {
        "../function/restParam": 286,
        "../internal/arrayMap": 294,
        "../internal/baseDifference": 305,
        "../internal/baseFlatten": 311,
        "../internal/bindCallback": 336,
        "../internal/pickByArray": 375,
        "../internal/pickByCallback": 376,
        "./keysIn": 398
    }],
    401: [function (e, t, n) {
        function i(e) {
            e = o(e);
            for (var t = -1, n = r(e), i = n.length, a = Array(i); ++t < i;) {
                var s = n[t];
                a[t] = [s, e[s]]
            }
            return a
        }

        var r = e("./keys"), o = e("../internal/toObject");
        t.exports = i
    }, {"../internal/toObject": 383, "./keys": 397}],
    402: [function (e, t, n) {
        var i = e("../internal/baseFlatten"), r = e("../internal/bindCallback"), o = e("../internal/pickByArray"), a = e("../internal/pickByCallback"), s = e("../function/restParam"), c = s(function (e, t) {
            return null == e ? {} : "function" == typeof t[0] ? a(e, r(t[0], t[1], 3)) : o(e, i(t))
        });
        t.exports = c
    }, {
        "../function/restParam": 286,
        "../internal/baseFlatten": 311,
        "../internal/bindCallback": 336,
        "../internal/pickByArray": 375,
        "../internal/pickByCallback": 376
    }],
    403: [function (e, t, n) {
        function i(e, t, n, i) {
            var d = c(e) || p(e);
            if (t = o(t, i, 4), null == n)if (d || u(e)) {
                var h = e.constructor;
                n = d ? c(e) ? new h : [] : a(l(h) ? h.prototype : void 0)
            } else n = {};
            return (d ? r : s)(e, function (e, i, r) {
                return t(n, e, i, r)
            }), n
        }

        var r = e("../internal/arrayEach"), o = e("../internal/baseCallback"), a = e("../internal/baseCreate"), s = e("../internal/baseForOwn"), c = e("../lang/isArray"), l = e("../lang/isFunction"), u = e("../lang/isObject"), p = e("../lang/isTypedArray");
        t.exports = i
    }, {
        "../internal/arrayEach": 291,
        "../internal/baseCallback": 300,
        "../internal/baseCreate": 303,
        "../internal/baseForOwn": 314,
        "../lang/isArray": 387,
        "../lang/isFunction": 388,
        "../lang/isObject": 391,
        "../lang/isTypedArray": 394
    }],
    404: [function (e, t, n) {
        function i(e) {
            return r(e, o(e))
        }

        var r = e("../internal/baseValues"), o = e("./keys");
        t.exports = i
    }, {"../internal/baseValues": 335, "./keys": 397}],
    405: [function (e, t, n) {
        function i(e) {
            return e
        }

        t.exports = i
    }, {}],
    406: [function (e, t, n) {
        function i() {
        }

        t.exports = i
    }, {}],
    407: [function (e, t, n) {
        function i(e) {
            return a(e) ? r(e) : o(e)
        }

        var r = e("../internal/baseProperty"), o = e("../internal/basePropertyDeep"), a = e("../internal/isKey");
        t.exports = i
    }, {"../internal/baseProperty": 326, "../internal/basePropertyDeep": 327, "../internal/isKey": 368}],
    408: [function (e, t, n) {
        t.exports = function (e, t, n) {
            return 2 == arguments.length ? e.getAttribute(t) : null === n ? e.removeAttribute(t) : (e.setAttribute(t, n), e)
        }
    }, {}],
    409: [function (e, t, n) {
        t.exports = e("component-classes")
    }, {"component-classes": 418}],
    410: [function (e, t, n) {
        t.exports = function (e) {
            for (var t; e.childNodes.length;)t = e.childNodes[0], e.removeChild(t);
            return e
        }
    }, {}],
    411: [function (e, t, n) {
        t.exports = e("component-closest")
    }, {"component-closest": 420}],
    412: [function (e, t, n) {
        t.exports = e("component-delegate")
    }, {"component-delegate": 421}],
    413: [function (e, t, n) {
        t.exports = e("domify")
    }, {domify: 425}],
    414: [function (e, t, n) {
        t.exports = e("component-event")
    }, {"component-event": 422}],
    415: [function (e, t, n) {
        t.exports = e("component-matches-selector")
    }, {"component-matches-selector": 423}],
    416: [function (e, t, n) {
        t.exports = e("component-query")
    }, {"component-query": 424}],
    417: [function (e, t, n) {
        t.exports = function (e) {
            e.parentNode && e.parentNode.removeChild(e)
        }
    }, {}],
    418: [function (e, t, n) {
        function i(e) {
            if (!e || !e.nodeType)throw new Error("A DOM element reference is required");
            this.el = e, this.list = e.classList
        }

        var r = e("indexof"), o = /\s+/, a = Object.prototype.toString;
        t.exports = function (e) {
            return new i(e)
        }, i.prototype.add = function (e) {
            if (this.list)return this.list.add(e), this;
            var t = this.array(), n = r(t, e);
            return ~n || t.push(e), this.el.className = t.join(" "), this
        }, i.prototype.remove = function (e) {
            if ("[object RegExp]" == a.call(e))return this.removeMatching(e);
            if (this.list)return this.list.remove(e), this;
            var t = this.array(), n = r(t, e);
            return ~n && t.splice(n, 1), this.el.className = t.join(" "), this
        }, i.prototype.removeMatching = function (e) {
            for (var t = this.array(), n = 0; n < t.length; n++)e.test(t[n]) && this.remove(t[n]);
            return this
        }, i.prototype.toggle = function (e, t) {
            return this.list ? ("undefined" != typeof t ? t !== this.list.toggle(e, t) && this.list.toggle(e) : this.list.toggle(e), this) : ("undefined" != typeof t ? t ? this.add(e) : this.remove(e) : this.has(e) ? this.remove(e) : this.add(e), this)
        }, i.prototype.array = function () {
            var e = this.el.getAttribute("class") || "", t = e.replace(/^\s+|\s+$/g, ""), n = t.split(o);
            return "" === n[0] && n.shift(), n
        }, i.prototype.has = i.prototype.contains = function (e) {
            return this.list ? this.list.contains(e) : !!~r(this.array(), e)
        }
    }, {indexof: 419}],
    419: [function (e, t, n) {
        t.exports = function (e, t) {
            if (e.indexOf)return e.indexOf(t);
            for (var n = 0; n < e.length; ++n)if (e[n] === t)return n;
            return -1
        }
    }, {}],
    420: [function (e, t, n) {
        var i = e("matches-selector");
        t.exports = function (e, t, n, r) {
            for (e = n ? {parentNode: e} : e, r = r || document; (e = e.parentNode) && e !== document;) {
                if (i(e, t))return e;
                if (e === r)return
            }
        }
    }, {"matches-selector": 423}],
    421: [function (e, t, n) {
        var i = e("closest"), r = e("event");
        n.bind = function (e, t, n, o, a) {
            return r.bind(e, n, function (n) {
                var r = n.target || n.srcElement;
                n.delegateTarget = i(r, t, !0, e), n.delegateTarget && o.call(e, n)
            }, a)
        }, n.unbind = function (e, t, n, i) {
            r.unbind(e, t, n, i)
        }
    }, {closest: 420, event: 422}],
    422: [function (e, t, n) {
        var i = window.addEventListener ? "addEventListener" : "attachEvent", r = window.removeEventListener ? "removeEventListener" : "detachEvent", o = "addEventListener" !== i ? "on" : "";
        n.bind = function (e, t, n, r) {
            return e[i](o + t, n, r || !1), n
        }, n.unbind = function (e, t, n, i) {
            return e[r](o + t, n, i || !1), n
        }
    }, {}],
    423: [function (e, t, n) {
        function i(e, t) {
            if (!e || 1 !== e.nodeType)return !1;
            if (a)return a.call(e, t);
            for (var n = r.all(t, e.parentNode), i = 0; i < n.length; ++i)if (n[i] == e)return !0;
            return !1
        }

        var r = e("query"), o = Element.prototype, a = o.matches || o.webkitMatchesSelector || o.mozMatchesSelector || o.msMatchesSelector || o.oMatchesSelector;
        t.exports = i
    }, {query: 424}],
    424: [function (e, t, n) {
        function i(e, t) {
            return t.querySelector(e)
        }

        n = t.exports = function (e, t) {
            return t = t || document, i(e, t)
        }, n.all = function (e, t) {
            return t = t || document, t.querySelectorAll(e)
        }, n.engine = function (e) {
            if (!e.one)throw new Error(".one callback required");
            if (!e.all)throw new Error(".all callback required");
            return i = e.one, n.all = e.all, n
        }
    }, {}],
    425: [function (e, t, n) {
        function i(e, t) {
            if ("string" != typeof e)throw new TypeError("String expected");
            t || (t = document);
            var n = /<([\w:]+)/.exec(e);
            if (!n)return t.createTextNode(e);
            e = e.replace(/^\s+|\s+$/g, "");
            var i = n[1];
            if ("body" == i) {
                var r = t.createElement("html");
                return r.innerHTML = e, r.removeChild(r.lastChild)
            }
            var o = a[i] || a._default, s = o[0], c = o[1], l = o[2], r = t.createElement("div");
            for (r.innerHTML = c + e + l; s--;)r = r.lastChild;
            if (r.firstChild == r.lastChild)return r.removeChild(r.firstChild);
            for (var u = t.createDocumentFragment(); r.firstChild;)u.appendChild(r.removeChild(r.firstChild));
            return u
        }

        t.exports = i;
        var r, o = !1;
        "undefined" != typeof document && (r = document.createElement("div"), r.innerHTML = '  <link/><table></table><a href="/a">a</a><input type="checkbox"/>', o = !r.getElementsByTagName("link").length, r = void 0);
        var a = {
            legend: [1, "<fieldset>", "</fieldset>"],
            tr: [2, "<table><tbody>", "</tbody></table>"],
            col: [2, "<table><tbody></tbody><colgroup>", "</colgroup></table>"],
            _default: o ? [1, "X<div>", "</div>"] : [0, "", ""]
        };
        a.td = a.th = [3, "<table><tbody><tr>", "</tr></tbody></table>"], a.option = a.optgroup = [1, '<select multiple="multiple">', "</select>"], a.thead = a.tbody = a.colgroup = a.caption = a.tfoot = [1, "<table>", "</table>"], a.polyline = a.ellipse = a.polygon = a.circle = a.text = a.line = a.path = a.rect = a.g = [1, '<svg xmlns="http://www.w3.org/2000/svg" version="1.1">', "</svg>"]
    }, {}],
    jquery: [function (e, t, n) {
        !function (e, n) {
            "object" == typeof t && "object" == typeof t.exports ? t.exports = e.document ? n(e, !0) : function (e) {
                if (!e.document)throw new Error("jQuery requires a window with a document");
                return n(e)
            } : n(e)
        }("undefined" != typeof window ? window : this, function (e, t) {
            function n(e) {
                var t = "length"in e && e.length, n = J.type(e);
                return "function" === n || J.isWindow(e) ? !1 : 1 === e.nodeType && t ? !0 : "array" === n || 0 === t || "number" == typeof t && t > 0 && t - 1 in e
            }

            function i(e, t, n) {
                if (J.isFunction(t))return J.grep(e, function (e, i) {
                    return !!t.call(e, i, e) !== n
                });
                if (t.nodeType)return J.grep(e, function (e) {
                    return e === t !== n
                });
                if ("string" == typeof t) {
                    if (se.test(t))return J.filter(t, e, n);
                    t = J.filter(t, e)
                }
                return J.grep(e, function (e) {
                    return W.call(t, e) >= 0 !== n
                })
            }

            function r(e, t) {
                for (; (e = e[t]) && 1 !== e.nodeType;);
                return e
            }

            function o(e) {
                var t = fe[e] = {};
                return J.each(e.match(he) || [], function (e, n) {
                    t[n] = !0
                }), t
            }

            function a() {
                Z.removeEventListener("DOMContentLoaded", a, !1), e.removeEventListener("load", a, !1), J.ready()
            }

            function s() {
                Object.defineProperty(this.cache = {}, 0, {
                    get: function () {
                        return {}
                    }
                }), this.expando = J.expando + s.uid++
            }

            function c(e, t, n) {
                var i;
                if (void 0 === n && 1 === e.nodeType)if (i = "data-" + t.replace(xe, "-$1").toLowerCase(), n = e.getAttribute(i), "string" == typeof n) {
                    try {
                        n = "true" === n ? !0 : "false" === n ? !1 : "null" === n ? null : +n + "" === n ? +n : be.test(n) ? J.parseJSON(n) : n
                    } catch (r) {
                    }
                    ye.set(e, t, n)
                } else n = void 0;
                return n
            }

            function l() {
                return !0
            }

            function u() {
                return !1
            }

            function p() {
                try {
                    return Z.activeElement
                } catch (e) {
                }
            }

            function d(e, t) {
                return J.nodeName(e, "table") && J.nodeName(11 !== t.nodeType ? t : t.firstChild, "tr") ? e.getElementsByTagName("tbody")[0] || e.appendChild(e.ownerDocument.createElement("tbody")) : e
            }

            function h(e) {
                return e.type = (null !== e.getAttribute("type")) + "/" + e.type, e
            }

            function f(e) {
                var t = Le.exec(e.type);
                return t ? e.type = t[1] : e.removeAttribute("type"), e
            }

            function m(e, t) {
                for (var n = 0, i = e.length; i > n; n++)ve.set(e[n], "globalEval", !t || ve.get(t[n], "globalEval"))
            }

            function g(e, t) {
                var n, i, r, o, a, s, c, l;
                if (1 === t.nodeType) {
                    if (ve.hasData(e) && (o = ve.access(e), a = ve.set(t, o), l = o.events)) {
                        delete a.handle, a.events = {};
                        for (r in l)for (n = 0, i = l[r].length; i > n; n++)J.event.add(t, r, l[r][n])
                    }
                    ye.hasData(e) && (s = ye.access(e), c = J.extend({}, s), ye.set(t, c))
                }
            }

            function v(e, t) {
                var n = e.getElementsByTagName ? e.getElementsByTagName(t || "*") : e.querySelectorAll ? e.querySelectorAll(t || "*") : [];
                return void 0 === t || t && J.nodeName(e, t) ? J.merge([e], n) : n
            }

            function y(e, t) {
                var n = t.nodeName.toLowerCase();
                "input" === n && Se.test(e.type) ? t.checked = e.checked : ("input" === n || "textarea" === n) && (t.defaultValue = e.defaultValue)
            }

            function b(t, n) {
                var i, r = J(n.createElement(t)).appendTo(n.body), o = e.getDefaultComputedStyle && (i = e.getDefaultComputedStyle(r[0])) ? i.display : J.css(r[0], "display");
                return r.detach(), o
            }

            function x(e) {
                var t = Z, n = Ue[e];
                return n || (n = b(e, t), "none" !== n && n || (Fe = (Fe || J("<iframe frameborder='0' width='0' height='0'/>")).appendTo(t.documentElement), t = Fe[0].contentDocument, t.write(), t.close(), n = b(e, t), Fe.detach()), Ue[e] = n), n
            }

            function E(e, t, n) {
                var i, r, o, a, s = e.style;
                return n = n || He(e), n && (a = n.getPropertyValue(t) || n[t]), n && ("" !== a || J.contains(e.ownerDocument, e) || (a = J.style(e, t)), $e.test(a) && ze.test(t) && (i = s.width, r = s.minWidth, o = s.maxWidth, s.minWidth = s.maxWidth = s.width = a, a = n.width, s.width = i, s.minWidth = r, s.maxWidth = o)), void 0 !== a ? a + "" : a
            }

            function w(e, t) {
                return {
                    get: function () {
                        return e() ? void delete this.get : (this.get = t).apply(this, arguments)
                    }
                }
            }

            function _(e, t) {
                if (t in e)return t;
                for (var n = t[0].toUpperCase() + t.slice(1), i = t, r = Xe.length; r--;)if (t = Xe[r] + n, t in e)return t;
                return i
            }

            function S(e, t, n) {
                var i = Ge.exec(t);
                return i ? Math.max(0, i[1] - (n || 0)) + (i[2] || "px") : t
            }

            function C(e, t, n, i, r) {
                for (var o = n === (i ? "border" : "content") ? 4 : "width" === t ? 1 : 0, a = 0; 4 > o; o += 2)"margin" === n && (a += J.css(e, n + we[o], !0, r)), i ? ("content" === n && (a -= J.css(e, "padding" + we[o], !0, r)), "margin" !== n && (a -= J.css(e, "border" + we[o] + "Width", !0, r))) : (a += J.css(e, "padding" + we[o], !0, r), "padding" !== n && (a += J.css(e, "border" + we[o] + "Width", !0, r)));
                return a
            }

            function A(e, t, n) {
                var i = !0, r = "width" === t ? e.offsetWidth : e.offsetHeight, o = He(e), a = "border-box" === J.css(e, "boxSizing", !1, o);
                if (0 >= r || null == r) {
                    if (r = E(e, t, o), (0 > r || null == r) && (r = e.style[t]), $e.test(r))return r;
                    i = a && (K.boxSizingReliable() || r === e.style[t]), r = parseFloat(r) || 0
                }
                return r + C(e, t, n || (a ? "border" : "content"), i, o) + "px"
            }

            function T(e, t) {
                for (var n, i, r, o = [], a = 0, s = e.length; s > a; a++)i = e[a], i.style && (o[a] = ve.get(i, "olddisplay"), n = i.style.display, t ? (o[a] || "none" !== n || (i.style.display = ""), "" === i.style.display && _e(i) && (o[a] = ve.access(i, "olddisplay", x(i.nodeName)))) : (r = _e(i), "none" === n && r || ve.set(i, "olddisplay", r ? n : J.css(i, "display"))));
                for (a = 0; s > a; a++)i = e[a], i.style && (t && "none" !== i.style.display && "" !== i.style.display || (i.style.display = t ? o[a] || "" : "none"));
                return e
            }

            function j(e, t, n, i, r) {
                return new j.prototype.init(e, t, n, i, r)
            }

            function N() {
                return setTimeout(function () {
                    Ke = void 0
                }), Ke = J.now()
            }

            function k(e, t) {
                var n, i = 0, r = {height: e};
                for (t = t ? 1 : 0; 4 > i; i += 2 - t)n = we[i], r["margin" + n] = r["padding" + n] = e;
                return t && (r.opacity = r.width = e), r
            }

            function R(e, t, n) {
                for (var i, r = (nt[t] || []).concat(nt["*"]), o = 0, a = r.length; a > o; o++)if (i = r[o].call(n, t, e))return i
            }

            function M(e, t, n) {
                var i, r, o, a, s, c, l, u, p = this, d = {}, h = e.style, f = e.nodeType && _e(e), m = ve.get(e, "fxshow");
                n.queue || (s = J._queueHooks(e, "fx"), null == s.unqueued && (s.unqueued = 0, c = s.empty.fire, s.empty.fire = function () {
                    s.unqueued || c()
                }), s.unqueued++, p.always(function () {
                    p.always(function () {
                        s.unqueued--, J.queue(e, "fx").length || s.empty.fire()
                    })
                })), 1 === e.nodeType && ("height"in t || "width"in t) && (n.overflow = [h.overflow, h.overflowX, h.overflowY], l = J.css(e, "display"), u = "none" === l ? ve.get(e, "olddisplay") || x(e.nodeName) : l, "inline" === u && "none" === J.css(e, "float") && (h.display = "inline-block")), n.overflow && (h.overflow = "hidden", p.always(function () {
                    h.overflow = n.overflow[0], h.overflowX = n.overflow[1], h.overflowY = n.overflow[2]
                }));
                for (i in t)if (r = t[i], Qe.exec(r)) {
                    if (delete t[i], o = o || "toggle" === r, r === (f ? "hide" : "show")) {
                        if ("show" !== r || !m || void 0 === m[i])continue;
                        f = !0
                    }
                    d[i] = m && m[i] || J.style(e, i)
                } else l = void 0;
                if (J.isEmptyObject(d))"inline" === ("none" === l ? x(e.nodeName) : l) && (h.display = l); else {
                    m ? "hidden"in m && (f = m.hidden) : m = ve.access(e, "fxshow", {}), o && (m.hidden = !f), f ? J(e).show() : p.done(function () {
                        J(e).hide()
                    }), p.done(function () {
                        var t;
                        ve.remove(e, "fxshow");
                        for (t in d)J.style(e, t, d[t])
                    });
                    for (i in d)a = R(f ? m[i] : 0, i, p), i in m || (m[i] = a.start, f && (a.end = a.start, a.start = "width" === i || "height" === i ? 1 : 0))
                }
            }

            function D(e, t) {
                var n, i, r, o, a;
                for (n in e)if (i = J.camelCase(n), r = t[i], o = e[n], J.isArray(o) && (r = o[1], o = e[n] = o[0]), n !== i && (e[i] = o, delete e[n]), a = J.cssHooks[i], a && "expand"in a) {
                    o = a.expand(o), delete e[i];
                    for (n in o)n in e || (e[n] = o[n], t[n] = r)
                } else t[i] = r
            }

            function P(e, t, n) {
                var i, r, o = 0, a = tt.length, s = J.Deferred().always(function () {
                    delete c.elem
                }), c = function () {
                    if (r)return !1;
                    for (var t = Ke || N(), n = Math.max(0, l.startTime + l.duration - t), i = n / l.duration || 0, o = 1 - i, a = 0, c = l.tweens.length; c > a; a++)l.tweens[a].run(o);
                    return s.notifyWith(e, [l, o, n]), 1 > o && c ? n : (s.resolveWith(e, [l]), !1)
                }, l = s.promise({
                    elem: e,
                    props: J.extend({}, t),
                    opts: J.extend(!0, {specialEasing: {}}, n),
                    originalProperties: t,
                    originalOptions: n,
                    startTime: Ke || N(),
                    duration: n.duration,
                    tweens: [],
                    createTween: function (t, n) {
                        var i = J.Tween(e, l.opts, t, n, l.opts.specialEasing[t] || l.opts.easing);
                        return l.tweens.push(i), i
                    },
                    stop: function (t) {
                        var n = 0, i = t ? l.tweens.length : 0;
                        if (r)return this;
                        for (r = !0; i > n; n++)l.tweens[n].run(1);
                        return t ? s.resolveWith(e, [l, t]) : s.rejectWith(e, [l, t]), this
                    }
                }), u = l.props;
                for (D(u, l.opts.specialEasing); a > o; o++)if (i = tt[o].call(l, e, u, l.opts))return i;
                return J.map(u, R, l), J.isFunction(l.opts.start) && l.opts.start.call(e, l), J.fx.timer(J.extend(c, {
                    elem: e,
                    anim: l,
                    queue: l.opts.queue
                })), l.progress(l.opts.progress).done(l.opts.done, l.opts.complete).fail(l.opts.fail).always(l.opts.always)
            }

            function B(e) {
                return function (t, n) {
                    "string" != typeof t && (n = t, t = "*");
                    var i, r = 0, o = t.toLowerCase().match(he) || [];
                    if (J.isFunction(n))for (; i = o[r++];)"+" === i[0] ? (i = i.slice(1) || "*", (e[i] = e[i] || []).unshift(n)) : (e[i] = e[i] || []).push(n)
                }
            }

            function L(e, t, n, i) {
                function r(s) {
                    var c;
                    return o[s] = !0, J.each(e[s] || [], function (e, s) {
                        var l = s(t, n, i);
                        return "string" != typeof l || a || o[l] ? a ? !(c = l) : void 0 : (t.dataTypes.unshift(l), r(l), !1)
                    }), c
                }

                var o = {}, a = e === bt;
                return r(t.dataTypes[0]) || !o["*"] && r("*")
            }

            function O(e, t) {
                var n, i, r = J.ajaxSettings.flatOptions || {};
                for (n in t)void 0 !== t[n] && ((r[n] ? e : i || (i = {}))[n] = t[n]);
                return i && J.extend(!0, e, i), e
            }

            function I(e, t, n) {
                for (var i, r, o, a, s = e.contents, c = e.dataTypes; "*" === c[0];)c.shift(), void 0 === i && (i = e.mimeType || t.getResponseHeader("Content-Type"));
                if (i)for (r in s)if (s[r] && s[r].test(i)) {
                    c.unshift(r);
                    break
                }
                if (c[0]in n)o = c[0]; else {
                    for (r in n) {
                        if (!c[0] || e.converters[r + " " + c[0]]) {
                            o = r;
                            break
                        }
                        a || (a = r)
                    }
                    o = o || a
                }
                return o ? (o !== c[0] && c.unshift(o), n[o]) : void 0
            }

            function F(e, t, n, i) {
                var r, o, a, s, c, l = {}, u = e.dataTypes.slice();
                if (u[1])for (a in e.converters)l[a.toLowerCase()] = e.converters[a];
                for (o = u.shift(); o;)if (e.responseFields[o] && (n[e.responseFields[o]] = t), !c && i && e.dataFilter && (t = e.dataFilter(t, e.dataType)), c = o, o = u.shift())if ("*" === o)o = c; else if ("*" !== c && c !== o) {
                    if (a = l[c + " " + o] || l["* " + o], !a)for (r in l)if (s = r.split(" "), s[1] === o && (a = l[c + " " + s[0]] || l["* " + s[0]])) {
                        a === !0 ? a = l[r] : l[r] !== !0 && (o = s[0], u.unshift(s[1]));
                        break
                    }
                    if (a !== !0)if (a && e["throws"])t = a(t); else try {
                        t = a(t)
                    } catch (p) {
                        return {state: "parsererror", error: a ? p : "No conversion from " + c + " to " + o}
                    }
                }
                return {state: "success", data: t}
            }

            function U(e, t, n, i) {
                var r;
                if (J.isArray(t))J.each(t, function (t, r) {
                    n || St.test(e) ? i(e, r) : U(e + "[" + ("object" == typeof r ? t : "") + "]", r, n, i)
                }); else if (n || "object" !== J.type(t))i(e, t); else for (r in t)U(e + "[" + r + "]", t[r], n, i)
            }

            function z(e) {
                return J.isWindow(e) ? e : 9 === e.nodeType && e.defaultView
            }

            var $ = [], H = $.slice, q = $.concat, G = $.push, W = $.indexOf, V = {}, Y = V.toString, X = V.hasOwnProperty, K = {}, Z = e.document, Q = "2.1.4", J = function (e, t) {
                return new J.fn.init(e, t)
            }, ee = /^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, te = /^-ms-/, ne = /-([\da-z])/gi, ie = function (e, t) {
                return t.toUpperCase()
            };
            J.fn = J.prototype = {
                jquery: Q, constructor: J, selector: "", length: 0, toArray: function () {
                    return H.call(this)
                }, get: function (e) {
                    return null != e ? 0 > e ? this[e + this.length] : this[e] : H.call(this)
                }, pushStack: function (e) {
                    var t = J.merge(this.constructor(), e);
                    return t.prevObject = this, t.context = this.context, t
                }, each: function (e, t) {
                    return J.each(this, e, t)
                }, map: function (e) {
                    return this.pushStack(J.map(this, function (t, n) {
                        return e.call(t, n, t)
                    }))
                }, slice: function () {
                    return this.pushStack(H.apply(this, arguments))
                }, first: function () {
                    return this.eq(0)
                }, last: function () {
                    return this.eq(-1)
                }, eq: function (e) {
                    var t = this.length, n = +e + (0 > e ? t : 0);
                    return this.pushStack(n >= 0 && t > n ? [this[n]] : [])
                }, end: function () {
                    return this.prevObject || this.constructor(null)
                }, push: G, sort: $.sort, splice: $.splice
            }, J.extend = J.fn.extend = function () {
                var e, t, n, i, r, o, a = arguments[0] || {}, s = 1, c = arguments.length, l = !1;
                for ("boolean" == typeof a && (l = a, a = arguments[s] || {}, s++), "object" == typeof a || J.isFunction(a) || (a = {}), s === c && (a = this, s--); c > s; s++)if (null != (e = arguments[s]))for (t in e)n = a[t], i = e[t], a !== i && (l && i && (J.isPlainObject(i) || (r = J.isArray(i))) ? (r ? (r = !1, o = n && J.isArray(n) ? n : []) : o = n && J.isPlainObject(n) ? n : {}, a[t] = J.extend(l, o, i)) : void 0 !== i && (a[t] = i));
                return a
            }, J.extend({
                expando: "jQuery" + (Q + Math.random()).replace(/\D/g, ""), isReady: !0, error: function (e) {
                    throw new Error(e)
                }, noop: function () {
                }, isFunction: function (e) {
                    return "function" === J.type(e)
                }, isArray: Array.isArray, isWindow: function (e) {
                    return null != e && e === e.window
                }, isNumeric: function (e) {
                    return !J.isArray(e) && e - parseFloat(e) + 1 >= 0
                }, isPlainObject: function (e) {
                    return "object" !== J.type(e) || e.nodeType || J.isWindow(e) ? !1 : e.constructor && !X.call(e.constructor.prototype, "isPrototypeOf") ? !1 : !0
                }, isEmptyObject: function (e) {
                    var t;
                    for (t in e)return !1;
                    return !0
                }, type: function (e) {
                    return null == e ? e + "" : "object" == typeof e || "function" == typeof e ? V[Y.call(e)] || "object" : typeof e
                }, globalEval: function (e) {
                    var t, n = eval;
                    e = J.trim(e), e && (1 === e.indexOf("use strict") ? (t = Z.createElement("script"), t.text = e, Z.head.appendChild(t).parentNode.removeChild(t)) : n(e))
                }, camelCase: function (e) {
                    return e.replace(te, "ms-").replace(ne, ie)
                }, nodeName: function (e, t) {
                    return e.nodeName && e.nodeName.toLowerCase() === t.toLowerCase()
                }, each: function (e, t, i) {
                    var r, o = 0, a = e.length, s = n(e);
                    if (i) {
                        if (s)for (; a > o && (r = t.apply(e[o], i), r !== !1); o++); else for (o in e)if (r = t.apply(e[o], i), r === !1)break
                    } else if (s)for (; a > o && (r = t.call(e[o], o, e[o]), r !== !1); o++); else for (o in e)if (r = t.call(e[o], o, e[o]), r === !1)break;
                    return e
                }, trim: function (e) {
                    return null == e ? "" : (e + "").replace(ee, "")
                }, makeArray: function (e, t) {
                    var i = t || [];
                    return null != e && (n(Object(e)) ? J.merge(i, "string" == typeof e ? [e] : e) : G.call(i, e)), i
                }, inArray: function (e, t, n) {
                    return null == t ? -1 : W.call(t, e, n)
                }, merge: function (e, t) {
                    for (var n = +t.length, i = 0, r = e.length; n > i; i++)e[r++] = t[i];
                    return e.length = r, e
                }, grep: function (e, t, n) {
                    for (var i, r = [], o = 0, a = e.length, s = !n; a > o; o++)i = !t(e[o], o), i !== s && r.push(e[o]);
                    return r
                }, map: function (e, t, i) {
                    var r, o = 0, a = e.length, s = n(e), c = [];
                    if (s)for (; a > o; o++)r = t(e[o], o, i), null != r && c.push(r); else for (o in e)r = t(e[o], o, i), null != r && c.push(r);
                    return q.apply([], c)
                }, guid: 1, proxy: function (e, t) {
                    var n, i, r;
                    return "string" == typeof t && (n = e[t], t = e, e = n), J.isFunction(e) ? (i = H.call(arguments, 2), r = function () {
                        return e.apply(t || this, i.concat(H.call(arguments)))
                    }, r.guid = e.guid = e.guid || J.guid++, r) : void 0
                }, now: Date.now, support: K
            }), J.each("Boolean Number String Function Array Date RegExp Object Error".split(" "), function (e, t) {
                V["[object " + t + "]"] = t.toLowerCase()
            });
            var re = function (e) {
                function t(e, t, n, i) {
                    var r, o, a, s, c, l, p, h, f, m;
                    if ((t ? t.ownerDocument || t : U) !== M && R(t), t = t || M, n = n || [], s = t.nodeType, "string" != typeof e || !e || 1 !== s && 9 !== s && 11 !== s)return n;
                    if (!i && P) {
                        if (11 !== s && (r = ye.exec(e)))if (a = r[1]) {
                            if (9 === s) {
                                if (o = t.getElementById(a), !o || !o.parentNode)return n;
                                if (o.id === a)return n.push(o), n
                            } else if (t.ownerDocument && (o = t.ownerDocument.getElementById(a)) && I(t, o) && o.id === a)return n.push(o), n
                        } else {
                            if (r[2])return Q.apply(n, t.getElementsByTagName(e)), n;
                            if ((a = r[3]) && E.getElementsByClassName)return Q.apply(n, t.getElementsByClassName(a)), n
                        }
                        if (E.qsa && (!B || !B.test(e))) {
                            if (h = p = F, f = t, m = 1 !== s && e, 1 === s && "object" !== t.nodeName.toLowerCase()) {
                                for (l = C(e), (p = t.getAttribute("id")) ? h = p.replace(xe, "\\$&") : t.setAttribute("id", h), h = "[id='" + h + "'] ", c = l.length; c--;)l[c] = h + d(l[c]);
                                f = be.test(e) && u(t.parentNode) || t, m = l.join(",")
                            }
                            if (m)try {
                                return Q.apply(n, f.querySelectorAll(m)), n
                            } catch (g) {
                            } finally {
                                p || t.removeAttribute("id")
                            }
                        }
                    }
                    return T(e.replace(ce, "$1"), t, n, i)
                }

                function n() {
                    function e(n, i) {
                        return t.push(n + " ") > w.cacheLength && delete e[t.shift()], e[n + " "] = i
                    }

                    var t = [];
                    return e
                }

                function i(e) {
                    return e[F] = !0, e
                }

                function r(e) {
                    var t = M.createElement("div");
                    try {
                        return !!e(t)
                    } catch (n) {
                        return !1
                    } finally {
                        t.parentNode && t.parentNode.removeChild(t), t = null
                    }
                }

                function o(e, t) {
                    for (var n = e.split("|"), i = e.length; i--;)w.attrHandle[n[i]] = t
                }

                function a(e, t) {
                    var n = t && e, i = n && 1 === e.nodeType && 1 === t.nodeType && (~t.sourceIndex || V) - (~e.sourceIndex || V);
                    if (i)return i;
                    if (n)for (; n = n.nextSibling;)if (n === t)return -1;
                    return e ? 1 : -1
                }

                function s(e) {
                    return function (t) {
                        var n = t.nodeName.toLowerCase();
                        return "input" === n && t.type === e
                    }
                }

                function c(e) {
                    return function (t) {
                        var n = t.nodeName.toLowerCase();
                        return ("input" === n || "button" === n) && t.type === e
                    }
                }

                function l(e) {
                    return i(function (t) {
                        return t = +t, i(function (n, i) {
                            for (var r, o = e([], n.length, t), a = o.length; a--;)n[r = o[a]] && (n[r] = !(i[r] = n[r]))
                        })
                    })
                }

                function u(e) {
                    return e && "undefined" != typeof e.getElementsByTagName && e
                }

                function p() {
                }

                function d(e) {
                    for (var t = 0, n = e.length, i = ""; n > t; t++)i += e[t].value;
                    return i
                }

                function h(e, t, n) {
                    var i = t.dir, r = n && "parentNode" === i, o = $++;
                    return t.first ? function (t, n, o) {
                        for (; t = t[i];)if (1 === t.nodeType || r)return e(t, n, o)
                    } : function (t, n, a) {
                        var s, c, l = [z, o];
                        if (a) {
                            for (; t = t[i];)if ((1 === t.nodeType || r) && e(t, n, a))return !0
                        } else for (; t = t[i];)if (1 === t.nodeType || r) {
                            if (c = t[F] || (t[F] = {}), (s = c[i]) && s[0] === z && s[1] === o)return l[2] = s[2];
                            if (c[i] = l, l[2] = e(t, n, a))return !0
                        }
                    }
                }

                function f(e) {
                    return e.length > 1 ? function (t, n, i) {
                        for (var r = e.length; r--;)if (!e[r](t, n, i))return !1;
                        return !0
                    } : e[0]
                }

                function m(e, n, i) {
                    for (var r = 0, o = n.length; o > r; r++)t(e, n[r], i);
                    return i
                }

                function g(e, t, n, i, r) {
                    for (var o, a = [], s = 0, c = e.length, l = null != t; c > s; s++)(o = e[s]) && (!n || n(o, i, r)) && (a.push(o), l && t.push(s));
                    return a
                }

                function v(e, t, n, r, o, a) {
                    return r && !r[F] && (r = v(r)), o && !o[F] && (o = v(o, a)), i(function (i, a, s, c) {
                        var l, u, p, d = [], h = [], f = a.length, v = i || m(t || "*", s.nodeType ? [s] : s, []), y = !e || !i && t ? v : g(v, d, e, s, c), b = n ? o || (i ? e : f || r) ? [] : a : y;
                        if (n && n(y, b, s, c), r)for (l = g(b, h), r(l, [], s, c), u = l.length; u--;)(p = l[u]) && (b[h[u]] = !(y[h[u]] = p));
                        if (i) {
                            if (o || e) {
                                if (o) {
                                    for (l = [], u = b.length; u--;)(p = b[u]) && l.push(y[u] = p);
                                    o(null, b = [], l, c)
                                }
                                for (u = b.length; u--;)(p = b[u]) && (l = o ? ee(i, p) : d[u]) > -1 && (i[l] = !(a[l] = p))
                            }
                        } else b = g(b === a ? b.splice(f, b.length) : b), o ? o(null, a, b, c) : Q.apply(a, b)
                    })
                }

                function y(e) {
                    for (var t, n, i, r = e.length, o = w.relative[e[0].type], a = o || w.relative[" "], s = o ? 1 : 0, c = h(function (e) {
                        return e === t
                    }, a, !0), l = h(function (e) {
                        return ee(t, e) > -1
                    }, a, !0), u = [function (e, n, i) {
                        var r = !o && (i || n !== j) || ((t = n).nodeType ? c(e, n, i) : l(e, n, i));
                        return t = null, r
                    }]; r > s; s++)if (n = w.relative[e[s].type])u = [h(f(u), n)]; else {
                        if (n = w.filter[e[s].type].apply(null, e[s].matches), n[F]) {
                            for (i = ++s; r > i && !w.relative[e[i].type]; i++);
                            return v(s > 1 && f(u), s > 1 && d(e.slice(0, s - 1).concat({value: " " === e[s - 2].type ? "*" : ""})).replace(ce, "$1"), n, i > s && y(e.slice(s, i)), r > i && y(e = e.slice(i)), r > i && d(e))
                        }
                        u.push(n)
                    }
                    return f(u)
                }

                function b(e, n) {
                    var r = n.length > 0, o = e.length > 0, a = function (i, a, s, c, l) {
                        var u, p, d, h = 0, f = "0", m = i && [], v = [], y = j, b = i || o && w.find.TAG("*", l), x = z += null == y ? 1 : Math.random() || .1, E = b.length;
                        for (l && (j = a !== M && a); f !== E && null != (u = b[f]); f++) {
                            if (o && u) {
                                for (p = 0; d = e[p++];)if (d(u, a, s)) {
                                    c.push(u);
                                    break
                                }
                                l && (z = x)
                            }
                            r && ((u = !d && u) && h--, i && m.push(u))
                        }
                        if (h += f, r && f !== h) {
                            for (p = 0; d = n[p++];)d(m, v, a, s);
                            if (i) {
                                if (h > 0)for (; f--;)m[f] || v[f] || (v[f] = K.call(c));
                                v = g(v)
                            }
                            Q.apply(c, v), l && !i && v.length > 0 && h + n.length > 1 && t.uniqueSort(c)
                        }
                        return l && (z = x, j = y), m
                    };
                    return r ? i(a) : a
                }

                var x, E, w, _, S, C, A, T, j, N, k, R, M, D, P, B, L, O, I, F = "sizzle" + 1 * new Date, U = e.document, z = 0, $ = 0, H = n(), q = n(), G = n(), W = function (e, t) {
                    return e === t && (k = !0), 0
                }, V = 1 << 31, Y = {}.hasOwnProperty, X = [], K = X.pop, Z = X.push, Q = X.push, J = X.slice, ee = function (e, t) {
                    for (var n = 0, i = e.length; i > n; n++)if (e[n] === t)return n;
                    return -1
                }, te = "checked|selected|async|autofocus|autoplay|controls|defer|disabled|hidden|ismap|loop|multiple|open|readonly|required|scoped", ne = "[\\x20\\t\\r\\n\\f]", ie = "(?:\\\\.|[\\w-]|[^\\x00-\\xa0])+", re = ie.replace("w", "w#"), oe = "\\[" + ne + "*(" + ie + ")(?:" + ne + "*([*^$|!~]?=)" + ne + "*(?:'((?:\\\\.|[^\\\\'])*)'|\"((?:\\\\.|[^\\\\\"])*)\"|(" + re + "))|)" + ne + "*\\]", ae = ":(" + ie + ")(?:\\((('((?:\\\\.|[^\\\\'])*)'|\"((?:\\\\.|[^\\\\\"])*)\")|((?:\\\\.|[^\\\\()[\\]]|" + oe + ")*)|.*)\\)|)", se = new RegExp(ne + "+", "g"), ce = new RegExp("^" + ne + "+|((?:^|[^\\\\])(?:\\\\.)*)" + ne + "+$", "g"), le = new RegExp("^" + ne + "*," + ne + "*"), ue = new RegExp("^" + ne + "*([>+~]|" + ne + ")" + ne + "*"), pe = new RegExp("=" + ne + "*([^\\]'\"]*?)" + ne + "*\\]", "g"), de = new RegExp(ae), he = new RegExp("^" + re + "$"), fe = {
                    ID: new RegExp("^#(" + ie + ")"),
                    CLASS: new RegExp("^\\.(" + ie + ")"),
                    TAG: new RegExp("^(" + ie.replace("w", "w*") + ")"),
                    ATTR: new RegExp("^" + oe),
                    PSEUDO: new RegExp("^" + ae),
                    CHILD: new RegExp("^:(only|first|last|nth|nth-last)-(child|of-type)(?:\\(" + ne + "*(even|odd|(([+-]|)(\\d*)n|)" + ne + "*(?:([+-]|)" + ne + "*(\\d+)|))" + ne + "*\\)|)", "i"),
                    bool: new RegExp("^(?:" + te + ")$", "i"),
                    needsContext: new RegExp("^" + ne + "*[>+~]|:(even|odd|eq|gt|lt|nth|first|last)(?:\\(" + ne + "*((?:-\\d)?\\d*)" + ne + "*\\)|)(?=[^-]|$)", "i")
                }, me = /^(?:input|select|textarea|button)$/i, ge = /^h\d$/i, ve = /^[^{]+\{\s*\[native \w/, ye = /^(?:#([\w-]+)|(\w+)|\.([\w-]+))$/, be = /[+~]/, xe = /'|\\/g, Ee = new RegExp("\\\\([\\da-f]{1,6}" + ne + "?|(" + ne + ")|.)", "ig"), we = function (e, t, n) {
                    var i = "0x" + t - 65536;
                    return i !== i || n ? t : 0 > i ? String.fromCharCode(i + 65536) : String.fromCharCode(i >> 10 | 55296, 1023 & i | 56320)
                }, _e = function () {
                    R()
                };
                try {
                    Q.apply(X = J.call(U.childNodes), U.childNodes), X[U.childNodes.length].nodeType
                } catch (Se) {
                    Q = {
                        apply: X.length ? function (e, t) {
                            Z.apply(e, J.call(t))
                        } : function (e, t) {
                            for (var n = e.length, i = 0; e[n++] = t[i++];);
                            e.length = n - 1
                        }
                    }
                }
                E = t.support = {}, S = t.isXML = function (e) {
                    var t = e && (e.ownerDocument || e).documentElement;
                    return t ? "HTML" !== t.nodeName : !1
                }, R = t.setDocument = function (e) {
                    var t, n, i = e ? e.ownerDocument || e : U;
                    return i !== M && 9 === i.nodeType && i.documentElement ? (M = i, D = i.documentElement, n = i.defaultView, n && n !== n.top && (n.addEventListener ? n.addEventListener("unload", _e, !1) : n.attachEvent && n.attachEvent("onunload", _e)), P = !S(i), E.attributes = r(function (e) {
                        return e.className = "i", !e.getAttribute("className")
                    }), E.getElementsByTagName = r(function (e) {
                        return e.appendChild(i.createComment("")), !e.getElementsByTagName("*").length
                    }), E.getElementsByClassName = ve.test(i.getElementsByClassName), E.getById = r(function (e) {
                        return D.appendChild(e).id = F, !i.getElementsByName || !i.getElementsByName(F).length
                    }), E.getById ? (w.find.ID = function (e, t) {
                        if ("undefined" != typeof t.getElementById && P) {
                            var n = t.getElementById(e);
                            return n && n.parentNode ? [n] : []
                        }
                    }, w.filter.ID = function (e) {
                        var t = e.replace(Ee, we);
                        return function (e) {
                            return e.getAttribute("id") === t
                        }
                    }) : (delete w.find.ID, w.filter.ID = function (e) {
                        var t = e.replace(Ee, we);
                        return function (e) {
                            var n = "undefined" != typeof e.getAttributeNode && e.getAttributeNode("id");
                            return n && n.value === t
                        }
                    }), w.find.TAG = E.getElementsByTagName ? function (e, t) {
                        return "undefined" != typeof t.getElementsByTagName ? t.getElementsByTagName(e) : E.qsa ? t.querySelectorAll(e) : void 0
                    } : function (e, t) {
                        var n, i = [], r = 0, o = t.getElementsByTagName(e);
                        if ("*" === e) {
                            for (; n = o[r++];)1 === n.nodeType && i.push(n);
                            return i
                        }
                        return o
                    }, w.find.CLASS = E.getElementsByClassName && function (e, t) {
                        return P ? t.getElementsByClassName(e) : void 0
                    }, L = [], B = [], (E.qsa = ve.test(i.querySelectorAll)) && (r(function (e) {
                        D.appendChild(e).innerHTML = "<a id='" + F + "'></a><select id='" + F + "-\f]' msallowcapture=''><option selected=''></option></select>", e.querySelectorAll("[msallowcapture^='']").length && B.push("[*^$]=" + ne + "*(?:''|\"\")"), e.querySelectorAll("[selected]").length || B.push("\\[" + ne + "*(?:value|" + te + ")"), e.querySelectorAll("[id~=" + F + "-]").length || B.push("~="), e.querySelectorAll(":checked").length || B.push(":checked"), e.querySelectorAll("a#" + F + "+*").length || B.push(".#.+[+~]")
                    }), r(function (e) {
                        var t = i.createElement("input");
                        t.setAttribute("type", "hidden"), e.appendChild(t).setAttribute("name", "D"), e.querySelectorAll("[name=d]").length && B.push("name" + ne + "*[*^$|!~]?="), e.querySelectorAll(":enabled").length || B.push(":enabled", ":disabled"), e.querySelectorAll("*,:x"), B.push(",.*:")
                    })), (E.matchesSelector = ve.test(O = D.matches || D.webkitMatchesSelector || D.mozMatchesSelector || D.oMatchesSelector || D.msMatchesSelector)) && r(function (e) {
                        E.disconnectedMatch = O.call(e, "div"), O.call(e, "[s!='']:x"), L.push("!=", ae)
                    }), B = B.length && new RegExp(B.join("|")), L = L.length && new RegExp(L.join("|")), t = ve.test(D.compareDocumentPosition), I = t || ve.test(D.contains) ? function (e, t) {
                        var n = 9 === e.nodeType ? e.documentElement : e, i = t && t.parentNode;
                        return e === i || !(!i || 1 !== i.nodeType || !(n.contains ? n.contains(i) : e.compareDocumentPosition && 16 & e.compareDocumentPosition(i)))
                    } : function (e, t) {
                        if (t)for (; t = t.parentNode;)if (t === e)return !0;
                        return !1
                    }, W = t ? function (e, t) {
                        if (e === t)return k = !0, 0;
                        var n = !e.compareDocumentPosition - !t.compareDocumentPosition;
                        return n ? n : (n = (e.ownerDocument || e) === (t.ownerDocument || t) ? e.compareDocumentPosition(t) : 1, 1 & n || !E.sortDetached && t.compareDocumentPosition(e) === n ? e === i || e.ownerDocument === U && I(U, e) ? -1 : t === i || t.ownerDocument === U && I(U, t) ? 1 : N ? ee(N, e) - ee(N, t) : 0 : 4 & n ? -1 : 1)
                    } : function (e, t) {
                        if (e === t)return k = !0, 0;
                        var n, r = 0, o = e.parentNode, s = t.parentNode, c = [e], l = [t];
                        if (!o || !s)return e === i ? -1 : t === i ? 1 : o ? -1 : s ? 1 : N ? ee(N, e) - ee(N, t) : 0;
                        if (o === s)return a(e, t);
                        for (n = e; n = n.parentNode;)c.unshift(n);
                        for (n = t; n = n.parentNode;)l.unshift(n);
                        for (; c[r] === l[r];)r++;
                        return r ? a(c[r], l[r]) : c[r] === U ? -1 : l[r] === U ? 1 : 0
                    }, i) : M
                }, t.matches = function (e, n) {
                    return t(e, null, null, n)
                }, t.matchesSelector = function (e, n) {
                    if ((e.ownerDocument || e) !== M && R(e), n = n.replace(pe, "='$1']"), E.matchesSelector && P && (!L || !L.test(n)) && (!B || !B.test(n)))try {
                        var i = O.call(e, n);
                        if (i || E.disconnectedMatch || e.document && 11 !== e.document.nodeType)return i
                    } catch (r) {
                    }
                    return t(n, M, null, [e]).length > 0
                }, t.contains = function (e, t) {
                    return (e.ownerDocument || e) !== M && R(e), I(e, t)
                }, t.attr = function (e, t) {
                    (e.ownerDocument || e) !== M && R(e);
                    var n = w.attrHandle[t.toLowerCase()], i = n && Y.call(w.attrHandle, t.toLowerCase()) ? n(e, t, !P) : void 0;
                    return void 0 !== i ? i : E.attributes || !P ? e.getAttribute(t) : (i = e.getAttributeNode(t)) && i.specified ? i.value : null
                }, t.error = function (e) {
                    throw new Error("Syntax error, unrecognized expression: " + e)
                }, t.uniqueSort = function (e) {
                    var t, n = [], i = 0, r = 0;
                    if (k = !E.detectDuplicates, N = !E.sortStable && e.slice(0), e.sort(W), k) {
                        for (; t = e[r++];)t === e[r] && (i = n.push(r));
                        for (; i--;)e.splice(n[i], 1)
                    }
                    return N = null, e
                }, _ = t.getText = function (e) {
                    var t, n = "", i = 0, r = e.nodeType;
                    if (r) {
                        if (1 === r || 9 === r || 11 === r) {
                            if ("string" == typeof e.textContent)return e.textContent;
                            for (e = e.firstChild; e; e = e.nextSibling)n += _(e)
                        } else if (3 === r || 4 === r)return e.nodeValue
                    } else for (; t = e[i++];)n += _(t);
                    return n
                }, w = t.selectors = {
                    cacheLength: 50,
                    createPseudo: i,
                    match: fe,
                    attrHandle: {},
                    find: {},
                    relative: {
                        ">": {dir: "parentNode", first: !0},
                        " ": {dir: "parentNode"},
                        "+": {dir: "previousSibling", first: !0},
                        "~": {dir: "previousSibling"}
                    },
                    preFilter: {
                        ATTR: function (e) {
                            return e[1] = e[1].replace(Ee, we), e[3] = (e[3] || e[4] || e[5] || "").replace(Ee, we), "~=" === e[2] && (e[3] = " " + e[3] + " "), e.slice(0, 4)
                        }, CHILD: function (e) {
                            return e[1] = e[1].toLowerCase(), "nth" === e[1].slice(0, 3) ? (e[3] || t.error(e[0]), e[4] = +(e[4] ? e[5] + (e[6] || 1) : 2 * ("even" === e[3] || "odd" === e[3])), e[5] = +(e[7] + e[8] || "odd" === e[3])) : e[3] && t.error(e[0]), e
                        }, PSEUDO: function (e) {
                            var t, n = !e[6] && e[2];
                            return fe.CHILD.test(e[0]) ? null : (e[3] ? e[2] = e[4] || e[5] || "" : n && de.test(n) && (t = C(n, !0)) && (t = n.indexOf(")", n.length - t) - n.length) && (e[0] = e[0].slice(0, t), e[2] = n.slice(0, t)), e.slice(0, 3))
                        }
                    },
                    filter: {
                        TAG: function (e) {
                            var t = e.replace(Ee, we).toLowerCase();
                            return "*" === e ? function () {
                                return !0
                            } : function (e) {
                                return e.nodeName && e.nodeName.toLowerCase() === t
                            }
                        }, CLASS: function (e) {
                            var t = H[e + " "];
                            return t || (t = new RegExp("(^|" + ne + ")" + e + "(" + ne + "|$)")) && H(e, function (e) {
                                    return t.test("string" == typeof e.className && e.className || "undefined" != typeof e.getAttribute && e.getAttribute("class") || "")
                                })
                        }, ATTR: function (e, n, i) {
                            return function (r) {
                                var o = t.attr(r, e);
                                return null == o ? "!=" === n : n ? (o += "", "=" === n ? o === i : "!=" === n ? o !== i : "^=" === n ? i && 0 === o.indexOf(i) : "*=" === n ? i && o.indexOf(i) > -1 : "$=" === n ? i && o.slice(-i.length) === i : "~=" === n ? (" " + o.replace(se, " ") + " ").indexOf(i) > -1 : "|=" === n ? o === i || o.slice(0, i.length + 1) === i + "-" : !1) : !0
                            }
                        }, CHILD: function (e, t, n, i, r) {
                            var o = "nth" !== e.slice(0, 3), a = "last" !== e.slice(-4), s = "of-type" === t;
                            return 1 === i && 0 === r ? function (e) {
                                return !!e.parentNode
                            } : function (t, n, c) {
                                var l, u, p, d, h, f, m = o !== a ? "nextSibling" : "previousSibling", g = t.parentNode, v = s && t.nodeName.toLowerCase(), y = !c && !s;
                                if (g) {
                                    if (o) {
                                        for (; m;) {
                                            for (p = t; p = p[m];)if (s ? p.nodeName.toLowerCase() === v : 1 === p.nodeType)return !1;
                                            f = m = "only" === e && !f && "nextSibling"
                                        }
                                        return !0
                                    }
                                    if (f = [a ? g.firstChild : g.lastChild], a && y) {
                                        for (u = g[F] || (g[F] = {}), l = u[e] || [], h = l[0] === z && l[1], d = l[0] === z && l[2], p = h && g.childNodes[h]; p = ++h && p && p[m] || (d = h = 0) || f.pop();)if (1 === p.nodeType && ++d && p === t) {
                                            u[e] = [z, h, d];
                                            break
                                        }
                                    } else if (y && (l = (t[F] || (t[F] = {}))[e]) && l[0] === z)d = l[1]; else for (; (p = ++h && p && p[m] || (d = h = 0) || f.pop()) && ((s ? p.nodeName.toLowerCase() !== v : 1 !== p.nodeType) || !++d || (y && ((p[F] || (p[F] = {}))[e] = [z, d]), p !== t)););
                                    return d -= r, d === i || d % i === 0 && d / i >= 0
                                }
                            }
                        }, PSEUDO: function (e, n) {
                            var r, o = w.pseudos[e] || w.setFilters[e.toLowerCase()] || t.error("unsupported pseudo: " + e);
                            return o[F] ? o(n) : o.length > 1 ? (r = [e, e, "", n], w.setFilters.hasOwnProperty(e.toLowerCase()) ? i(function (e, t) {
                                for (var i, r = o(e, n), a = r.length; a--;)i = ee(e, r[a]), e[i] = !(t[i] = r[a])
                            }) : function (e) {
                                return o(e, 0, r)
                            }) : o
                        }
                    },
                    pseudos: {
                        not: i(function (e) {
                            var t = [], n = [], r = A(e.replace(ce, "$1"));
                            return r[F] ? i(function (e, t, n, i) {
                                for (var o, a = r(e, null, i, []), s = e.length; s--;)(o = a[s]) && (e[s] = !(t[s] = o))
                            }) : function (e, i, o) {
                                return t[0] = e, r(t, null, o, n), t[0] = null, !n.pop()
                            }
                        }), has: i(function (e) {
                            return function (n) {
                                return t(e, n).length > 0
                            }
                        }), contains: i(function (e) {
                            return e = e.replace(Ee, we), function (t) {
                                return (t.textContent || t.innerText || _(t)).indexOf(e) > -1
                            }
                        }), lang: i(function (e) {
                            return he.test(e || "") || t.error("unsupported lang: " + e), e = e.replace(Ee, we).toLowerCase(), function (t) {
                                var n;
                                do if (n = P ? t.lang : t.getAttribute("xml:lang") || t.getAttribute("lang"))return n = n.toLowerCase(), n === e || 0 === n.indexOf(e + "-"); while ((t = t.parentNode) && 1 === t.nodeType);
                                return !1
                            }
                        }), target: function (t) {
                            var n = e.location && e.location.hash;
                            return n && n.slice(1) === t.id
                        }, root: function (e) {
                            return e === D
                        }, focus: function (e) {
                            return e === M.activeElement && (!M.hasFocus || M.hasFocus()) && !!(e.type || e.href || ~e.tabIndex)
                        }, enabled: function (e) {
                            return e.disabled === !1
                        }, disabled: function (e) {
                            return e.disabled === !0
                        }, checked: function (e) {
                            var t = e.nodeName.toLowerCase();
                            return "input" === t && !!e.checked || "option" === t && !!e.selected
                        }, selected: function (e) {
                            return e.parentNode && e.parentNode.selectedIndex, e.selected === !0
                        }, empty: function (e) {
                            for (e = e.firstChild; e; e = e.nextSibling)if (e.nodeType < 6)return !1;
                            return !0
                        }, parent: function (e) {
                            return !w.pseudos.empty(e)
                        }, header: function (e) {
                            return ge.test(e.nodeName)
                        }, input: function (e) {
                            return me.test(e.nodeName)
                        }, button: function (e) {
                            var t = e.nodeName.toLowerCase();
                            return "input" === t && "button" === e.type || "button" === t
                        }, text: function (e) {
                            var t;
                            return "input" === e.nodeName.toLowerCase() && "text" === e.type && (null == (t = e.getAttribute("type")) || "text" === t.toLowerCase())
                        }, first: l(function () {
                            return [0]
                        }), last: l(function (e, t) {
                            return [t - 1]
                        }), eq: l(function (e, t, n) {
                            return [0 > n ? n + t : n]
                        }), even: l(function (e, t) {
                            for (var n = 0; t > n; n += 2)e.push(n);
                            return e
                        }), odd: l(function (e, t) {
                            for (var n = 1; t > n; n += 2)e.push(n);
                            return e
                        }), lt: l(function (e, t, n) {
                            for (var i = 0 > n ? n + t : n; --i >= 0;)e.push(i);
                            return e
                        }), gt: l(function (e, t, n) {
                            for (var i = 0 > n ? n + t : n; ++i < t;)e.push(i);
                            return e
                        })
                    }
                }, w.pseudos.nth = w.pseudos.eq;
                for (x in{radio: !0, checkbox: !0, file: !0, password: !0, image: !0})w.pseudos[x] = s(x);
                for (x in{submit: !0, reset: !0})w.pseudos[x] = c(x);
                return p.prototype = w.filters = w.pseudos, w.setFilters = new p, C = t.tokenize = function (e, n) {
                    var i, r, o, a, s, c, l, u = q[e + " "];
                    if (u)return n ? 0 : u.slice(0);
                    for (s = e, c = [], l = w.preFilter; s;) {
                        (!i || (r = le.exec(s))) && (r && (s = s.slice(r[0].length) || s), c.push(o = [])), i = !1, (r = ue.exec(s)) && (i = r.shift(), o.push({
                            value: i,
                            type: r[0].replace(ce, " ")
                        }), s = s.slice(i.length));
                        for (a in w.filter)!(r = fe[a].exec(s)) || l[a] && !(r = l[a](r)) || (i = r.shift(), o.push({
                            value: i,
                            type: a,
                            matches: r
                        }), s = s.slice(i.length));
                        if (!i)break
                    }
                    return n ? s.length : s ? t.error(e) : q(e, c).slice(0)
                }, A = t.compile = function (e, t) {
                    var n, i = [], r = [], o = G[e + " "];
                    if (!o) {
                        for (t || (t = C(e)), n = t.length; n--;)o = y(t[n]), o[F] ? i.push(o) : r.push(o);
                        o = G(e, b(r, i)), o.selector = e
                    }
                    return o
                }, T = t.select = function (e, t, n, i) {
                    var r, o, a, s, c, l = "function" == typeof e && e, p = !i && C(e = l.selector || e);
                    if (n = n || [], 1 === p.length) {
                        if (o = p[0] = p[0].slice(0), o.length > 2 && "ID" === (a = o[0]).type && E.getById && 9 === t.nodeType && P && w.relative[o[1].type]) {
                            if (t = (w.find.ID(a.matches[0].replace(Ee, we), t) || [])[0], !t)return n;
                            l && (t = t.parentNode), e = e.slice(o.shift().value.length)
                        }
                        for (r = fe.needsContext.test(e) ? 0 : o.length; r-- && (a = o[r], !w.relative[s = a.type]);)if ((c = w.find[s]) && (i = c(a.matches[0].replace(Ee, we), be.test(o[0].type) && u(t.parentNode) || t))) {
                            if (o.splice(r, 1), e = i.length && d(o), !e)return Q.apply(n, i), n;
                            break
                        }
                    }
                    return (l || A(e, p))(i, t, !P, n, be.test(e) && u(t.parentNode) || t), n
                }, E.sortStable = F.split("").sort(W).join("") === F, E.detectDuplicates = !!k, R(), E.sortDetached = r(function (e) {
                    return 1 & e.compareDocumentPosition(M.createElement("div"))
                }), r(function (e) {
                    return e.innerHTML = "<a href='#'></a>", "#" === e.firstChild.getAttribute("href")
                }) || o("type|href|height|width", function (e, t, n) {
                    return n ? void 0 : e.getAttribute(t, "type" === t.toLowerCase() ? 1 : 2)
                }), E.attributes && r(function (e) {
                    return e.innerHTML = "<input/>", e.firstChild.setAttribute("value", ""), "" === e.firstChild.getAttribute("value")
                }) || o("value", function (e, t, n) {
                    return n || "input" !== e.nodeName.toLowerCase() ? void 0 : e.defaultValue
                }), r(function (e) {
                    return null == e.getAttribute("disabled")
                }) || o(te, function (e, t, n) {
                    var i;
                    return n ? void 0 : e[t] === !0 ? t.toLowerCase() : (i = e.getAttributeNode(t)) && i.specified ? i.value : null
                }), t
            }(e);
            J.find = re, J.expr = re.selectors, J.expr[":"] = J.expr.pseudos, J.unique = re.uniqueSort, J.text = re.getText, J.isXMLDoc = re.isXML, J.contains = re.contains;
            var oe = J.expr.match.needsContext, ae = /^<(\w+)\s*\/?>(?:<\/\1>|)$/, se = /^.[^:#\[\.,]*$/;
            J.filter = function (e, t, n) {
                var i = t[0];
                return n && (e = ":not(" + e + ")"), 1 === t.length && 1 === i.nodeType ? J.find.matchesSelector(i, e) ? [i] : [] : J.find.matches(e, J.grep(t, function (e) {
                    return 1 === e.nodeType
                }))
            }, J.fn.extend({
                find: function (e) {
                    var t, n = this.length, i = [], r = this;
                    if ("string" != typeof e)return this.pushStack(J(e).filter(function () {
                        for (t = 0; n > t; t++)if (J.contains(r[t], this))return !0
                    }));
                    for (t = 0; n > t; t++)J.find(e, r[t], i);
                    return i = this.pushStack(n > 1 ? J.unique(i) : i), i.selector = this.selector ? this.selector + " " + e : e, i
                }, filter: function (e) {
                    return this.pushStack(i(this, e || [], !1))
                }, not: function (e) {
                    return this.pushStack(i(this, e || [], !0))
                }, is: function (e) {
                    return !!i(this, "string" == typeof e && oe.test(e) ? J(e) : e || [], !1).length
                }
            });
            var ce, le = /^(?:\s*(<[\w\W]+>)[^>]*|#([\w-]*))$/, ue = J.fn.init = function (e, t) {
                var n, i;
                if (!e)return this;
                if ("string" == typeof e) {
                    if (n = "<" === e[0] && ">" === e[e.length - 1] && e.length >= 3 ? [null, e, null] : le.exec(e), !n || !n[1] && t)return !t || t.jquery ? (t || ce).find(e) : this.constructor(t).find(e);
                    if (n[1]) {
                        if (t = t instanceof J ? t[0] : t, J.merge(this, J.parseHTML(n[1], t && t.nodeType ? t.ownerDocument || t : Z, !0)), ae.test(n[1]) && J.isPlainObject(t))for (n in t)J.isFunction(this[n]) ? this[n](t[n]) : this.attr(n, t[n]);
                        return this
                    }
                    return i = Z.getElementById(n[2]), i && i.parentNode && (this.length = 1, this[0] = i), this.context = Z, this.selector = e, this
                }
                return e.nodeType ? (this.context = this[0] = e, this.length = 1, this) : J.isFunction(e) ? "undefined" != typeof ce.ready ? ce.ready(e) : e(J) : (void 0 !== e.selector && (this.selector = e.selector, this.context = e.context), J.makeArray(e, this))
            };
            ue.prototype = J.fn, ce = J(Z);
            var pe = /^(?:parents|prev(?:Until|All))/, de = {children: !0, contents: !0, next: !0, prev: !0};
            J.extend({
                dir: function (e, t, n) {
                    for (var i = [], r = void 0 !== n; (e = e[t]) && 9 !== e.nodeType;)if (1 === e.nodeType) {
                        if (r && J(e).is(n))break;
                        i.push(e)
                    }
                    return i
                }, sibling: function (e, t) {
                    for (var n = []; e; e = e.nextSibling)1 === e.nodeType && e !== t && n.push(e);
                    return n
                }
            }), J.fn.extend({
                has: function (e) {
                    var t = J(e, this), n = t.length;
                    return this.filter(function () {
                        for (var e = 0; n > e; e++)if (J.contains(this, t[e]))return !0
                    })
                }, closest: function (e, t) {
                    for (var n, i = 0, r = this.length, o = [], a = oe.test(e) || "string" != typeof e ? J(e, t || this.context) : 0; r > i; i++)for (n = this[i]; n && n !== t; n = n.parentNode)if (n.nodeType < 11 && (a ? a.index(n) > -1 : 1 === n.nodeType && J.find.matchesSelector(n, e))) {
                        o.push(n);
                        break
                    }
                    return this.pushStack(o.length > 1 ? J.unique(o) : o)
                }, index: function (e) {
                    return e ? "string" == typeof e ? W.call(J(e), this[0]) : W.call(this, e.jquery ? e[0] : e) : this[0] && this[0].parentNode ? this.first().prevAll().length : -1
                }, add: function (e, t) {
                    return this.pushStack(J.unique(J.merge(this.get(), J(e, t))))
                }, addBack: function (e) {
                    return this.add(null == e ? this.prevObject : this.prevObject.filter(e))
                }
            }), J.each({
                parent: function (e) {
                    var t = e.parentNode;
                    return t && 11 !== t.nodeType ? t : null
                }, parents: function (e) {
                    return J.dir(e, "parentNode")
                }, parentsUntil: function (e, t, n) {
                    return J.dir(e, "parentNode", n)
                }, next: function (e) {
                    return r(e, "nextSibling")
                }, prev: function (e) {
                    return r(e, "previousSibling")
                }, nextAll: function (e) {
                    return J.dir(e, "nextSibling")
                }, prevAll: function (e) {
                    return J.dir(e, "previousSibling")
                }, nextUntil: function (e, t, n) {
                    return J.dir(e, "nextSibling", n)
                }, prevUntil: function (e, t, n) {
                    return J.dir(e, "previousSibling", n)
                }, siblings: function (e) {
                    return J.sibling((e.parentNode || {}).firstChild, e)
                }, children: function (e) {
                    return J.sibling(e.firstChild)
                }, contents: function (e) {
                    return e.contentDocument || J.merge([], e.childNodes)
                }
            }, function (e, t) {
                J.fn[e] = function (n, i) {
                    var r = J.map(this, t, n);
                    return "Until" !== e.slice(-5) && (i = n), i && "string" == typeof i && (r = J.filter(i, r)), this.length > 1 && (de[e] || J.unique(r), pe.test(e) && r.reverse()), this.pushStack(r)
                }
            });
            var he = /\S+/g, fe = {};
            J.Callbacks = function (e) {
                e = "string" == typeof e ? fe[e] || o(e) : J.extend({}, e);
                var t, n, i, r, a, s, c = [], l = !e.once && [], u = function (o) {
                    for (t = e.memory && o, n = !0, s = r || 0, r = 0, a = c.length, i = !0; c && a > s; s++)if (c[s].apply(o[0], o[1]) === !1 && e.stopOnFalse) {
                        t = !1;
                        break
                    }
                    i = !1, c && (l ? l.length && u(l.shift()) : t ? c = [] : p.disable())
                }, p = {
                    add: function () {
                        if (c) {
                            var n = c.length;
                            !function o(t) {
                                J.each(t, function (t, n) {
                                    var i = J.type(n);
                                    "function" === i ? e.unique && p.has(n) || c.push(n) : n && n.length && "string" !== i && o(n)
                                })
                            }(arguments), i ? a = c.length : t && (r = n, u(t))
                        }
                        return this
                    }, remove: function () {
                        return c && J.each(arguments, function (e, t) {
                            for (var n; (n = J.inArray(t, c, n)) > -1;)c.splice(n, 1), i && (a >= n && a--, s >= n && s--)
                        }), this
                    }, has: function (e) {
                        return e ? J.inArray(e, c) > -1 : !(!c || !c.length)
                    }, empty: function () {
                        return c = [], a = 0, this
                    }, disable: function () {
                        return c = l = t = void 0, this
                    }, disabled: function () {
                        return !c
                    }, lock: function () {
                        return l = void 0, t || p.disable(), this
                    }, locked: function () {
                        return !l
                    }, fireWith: function (e, t) {
                        return !c || n && !l || (t = t || [], t = [e, t.slice ? t.slice() : t], i ? l.push(t) : u(t)), this
                    }, fire: function () {
                        return p.fireWith(this, arguments), this
                    }, fired: function () {
                        return !!n
                    }
                };
                return p
            }, J.extend({
                Deferred: function (e) {
                    var t = [["resolve", "done", J.Callbacks("once memory"), "resolved"], ["reject", "fail", J.Callbacks("once memory"), "rejected"], ["notify", "progress", J.Callbacks("memory")]], n = "pending", i = {
                        state: function () {
                            return n
                        }, always: function () {
                            return r.done(arguments).fail(arguments), this
                        }, then: function () {
                            var e = arguments;
                            return J.Deferred(function (n) {
                                J.each(t, function (t, o) {
                                    var a = J.isFunction(e[t]) && e[t];
                                    r[o[1]](function () {
                                        var e = a && a.apply(this, arguments);
                                        e && J.isFunction(e.promise) ? e.promise().done(n.resolve).fail(n.reject).progress(n.notify) : n[o[0] + "With"](this === i ? n.promise() : this, a ? [e] : arguments)
                                    })
                                }), e = null
                            }).promise()
                        }, promise: function (e) {
                            return null != e ? J.extend(e, i) : i
                        }
                    }, r = {};
                    return i.pipe = i.then, J.each(t, function (e, o) {
                        var a = o[2], s = o[3];
                        i[o[1]] = a.add, s && a.add(function () {
                            n = s
                        }, t[1 ^ e][2].disable, t[2][2].lock), r[o[0]] = function () {
                            return r[o[0] + "With"](this === r ? i : this, arguments), this
                        }, r[o[0] + "With"] = a.fireWith
                    }), i.promise(r), e && e.call(r, r), r
                }, when: function (e) {
                    var t, n, i, r = 0, o = H.call(arguments), a = o.length, s = 1 !== a || e && J.isFunction(e.promise) ? a : 0, c = 1 === s ? e : J.Deferred(), l = function (e, n, i) {
                        return function (r) {
                            n[e] = this, i[e] = arguments.length > 1 ? H.call(arguments) : r, i === t ? c.notifyWith(n, i) : --s || c.resolveWith(n, i)
                        }
                    };
                    if (a > 1)for (t = new Array(a), n = new Array(a), i = new Array(a); a > r; r++)o[r] && J.isFunction(o[r].promise) ? o[r].promise().done(l(r, i, o)).fail(c.reject).progress(l(r, n, t)) : --s;
                    return s || c.resolveWith(i, o), c.promise()
                }
            });
            var me;
            J.fn.ready = function (e) {
                return J.ready.promise().done(e), this
            }, J.extend({
                isReady: !1, readyWait: 1, holdReady: function (e) {
                    e ? J.readyWait++ : J.ready(!0)
                }, ready: function (e) {
                    (e === !0 ? --J.readyWait : J.isReady) || (J.isReady = !0, e !== !0 && --J.readyWait > 0 || (me.resolveWith(Z, [J]), J.fn.triggerHandler && (J(Z).triggerHandler("ready"), J(Z).off("ready"))))
                }
            }), J.ready.promise = function (t) {
                return me || (me = J.Deferred(), "complete" === Z.readyState ? setTimeout(J.ready) : (Z.addEventListener("DOMContentLoaded", a, !1), e.addEventListener("load", a, !1))), me.promise(t)
            }, J.ready.promise();
            var ge = J.access = function (e, t, n, i, r, o, a) {
                var s = 0, c = e.length, l = null == n;
                if ("object" === J.type(n)) {
                    r = !0;
                    for (s in n)J.access(e, t, s, n[s], !0, o, a)
                } else if (void 0 !== i && (r = !0, J.isFunction(i) || (a = !0), l && (a ? (t.call(e, i), t = null) : (l = t, t = function (e, t, n) {
                        return l.call(J(e), n)
                    })), t))for (; c > s; s++)t(e[s], n, a ? i : i.call(e[s], s, t(e[s], n)));
                return r ? e : l ? t.call(e) : c ? t(e[0], n) : o
            };
            J.acceptData = function (e) {
                return 1 === e.nodeType || 9 === e.nodeType || !+e.nodeType
            }, s.uid = 1, s.accepts = J.acceptData, s.prototype = {
                key: function (e) {
                    if (!s.accepts(e))return 0;
                    var t = {}, n = e[this.expando];
                    if (!n) {
                        n = s.uid++;
                        try {
                            t[this.expando] = {value: n}, Object.defineProperties(e, t)
                        } catch (i) {
                            t[this.expando] = n, J.extend(e, t)
                        }
                    }
                    return this.cache[n] || (this.cache[n] = {}), n
                }, set: function (e, t, n) {
                    var i, r = this.key(e), o = this.cache[r];
                    if ("string" == typeof t)o[t] = n; else if (J.isEmptyObject(o))J.extend(this.cache[r], t); else for (i in t)o[i] = t[i];
                    return o
                }, get: function (e, t) {
                    var n = this.cache[this.key(e)];
                    return void 0 === t ? n : n[t]
                }, access: function (e, t, n) {
                    var i;
                    return void 0 === t || t && "string" == typeof t && void 0 === n ? (i = this.get(e, t), void 0 !== i ? i : this.get(e, J.camelCase(t))) : (this.set(e, t, n), void 0 !== n ? n : t)
                }, remove: function (e, t) {
                    var n, i, r, o = this.key(e), a = this.cache[o];
                    if (void 0 === t)this.cache[o] = {}; else {
                        J.isArray(t) ? i = t.concat(t.map(J.camelCase)) : (r = J.camelCase(t), t in a ? i = [t, r] : (i = r, i = i in a ? [i] : i.match(he) || [])), n = i.length;
                        for (; n--;)delete a[i[n]]
                    }
                }, hasData: function (e) {
                    return !J.isEmptyObject(this.cache[e[this.expando]] || {})
                }, discard: function (e) {
                    e[this.expando] && delete this.cache[e[this.expando]]
                }
            };
            var ve = new s, ye = new s, be = /^(?:\{[\w\W]*\}|\[[\w\W]*\])$/, xe = /([A-Z])/g;
            J.extend({
                hasData: function (e) {
                    return ye.hasData(e) || ve.hasData(e)
                }, data: function (e, t, n) {
                    return ye.access(e, t, n)
                }, removeData: function (e, t) {
                    ye.remove(e, t)
                }, _data: function (e, t, n) {
                    return ve.access(e, t, n)
                }, _removeData: function (e, t) {
                    ve.remove(e, t)
                }
            }), J.fn.extend({
                data: function (e, t) {
                    var n, i, r, o = this[0], a = o && o.attributes;
                    if (void 0 === e) {
                        if (this.length && (r = ye.get(o), 1 === o.nodeType && !ve.get(o, "hasDataAttrs"))) {
                            for (n = a.length; n--;)a[n] && (i = a[n].name, 0 === i.indexOf("data-") && (i = J.camelCase(i.slice(5)), c(o, i, r[i])));
                            ve.set(o, "hasDataAttrs", !0)
                        }
                        return r
                    }
                    return "object" == typeof e ? this.each(function () {
                        ye.set(this, e)
                    }) : ge(this, function (t) {
                        var n, i = J.camelCase(e);
                        if (o && void 0 === t) {
                            if (n = ye.get(o, e), void 0 !== n)return n;
                            if (n = ye.get(o, i), void 0 !== n)return n;
                            if (n = c(o, i, void 0), void 0 !== n)return n
                        } else this.each(function () {
                            var n = ye.get(this, i);
                            ye.set(this, i, t), -1 !== e.indexOf("-") && void 0 !== n && ye.set(this, e, t)
                        })
                    }, null, t, arguments.length > 1, null, !0)
                }, removeData: function (e) {
                    return this.each(function () {
                        ye.remove(this, e)
                    })
                }
            }), J.extend({
                queue: function (e, t, n) {
                    var i;
                    return e ? (t = (t || "fx") + "queue", i = ve.get(e, t), n && (!i || J.isArray(n) ? i = ve.access(e, t, J.makeArray(n)) : i.push(n)), i || []) : void 0
                }, dequeue: function (e, t) {
                    t = t || "fx";
                    var n = J.queue(e, t), i = n.length, r = n.shift(), o = J._queueHooks(e, t), a = function () {
                        J.dequeue(e, t)
                    };
                    "inprogress" === r && (r = n.shift(), i--), r && ("fx" === t && n.unshift("inprogress"), delete o.stop, r.call(e, a, o)), !i && o && o.empty.fire()
                }, _queueHooks: function (e, t) {
                    var n = t + "queueHooks";
                    return ve.get(e, n) || ve.access(e, n, {
                            empty: J.Callbacks("once memory").add(function () {
                                ve.remove(e, [t + "queue", n])
                            })
                        })
                }
            }), J.fn.extend({
                queue: function (e, t) {
                    var n = 2;
                    return "string" != typeof e && (t = e, e = "fx", n--), arguments.length < n ? J.queue(this[0], e) : void 0 === t ? this : this.each(function () {
                        var n = J.queue(this, e, t);
                        J._queueHooks(this, e), "fx" === e && "inprogress" !== n[0] && J.dequeue(this, e)
                    })
                }, dequeue: function (e) {
                    return this.each(function () {
                        J.dequeue(this, e)
                    })
                }, clearQueue: function (e) {
                    return this.queue(e || "fx", [])
                }, promise: function (e, t) {
                    var n, i = 1, r = J.Deferred(), o = this, a = this.length, s = function () {
                        --i || r.resolveWith(o, [o])
                    };
                    for ("string" != typeof e && (t = e, e = void 0), e = e || "fx"; a--;)n = ve.get(o[a], e + "queueHooks"), n && n.empty && (i++, n.empty.add(s));
                    return s(), r.promise(t)
                }
            });
            var Ee = /[+-]?(?:\d*\.|)\d+(?:[eE][+-]?\d+|)/.source, we = ["Top", "Right", "Bottom", "Left"], _e = function (e, t) {
                return e = t || e, "none" === J.css(e, "display") || !J.contains(e.ownerDocument, e)
            }, Se = /^(?:checkbox|radio)$/i;
            !function () {
                var e = Z.createDocumentFragment(), t = e.appendChild(Z.createElement("div")), n = Z.createElement("input");
                n.setAttribute("type", "radio"), n.setAttribute("checked", "checked"), n.setAttribute("name", "t"), t.appendChild(n), K.checkClone = t.cloneNode(!0).cloneNode(!0).lastChild.checked, t.innerHTML = "<textarea>x</textarea>", K.noCloneChecked = !!t.cloneNode(!0).lastChild.defaultValue
            }();
            var Ce = "undefined";
            K.focusinBubbles = "onfocusin"in e;
            var Ae = /^key/, Te = /^(?:mouse|pointer|contextmenu)|click/, je = /^(?:focusinfocus|focusoutblur)$/, Ne = /^([^.]*)(?:\.(.+)|)$/;
            J.event = {
                global: {},
                add: function (e, t, n, i, r) {
                    var o, a, s, c, l, u, p, d, h, f, m, g = ve.get(e);
                    if (g)for (n.handler && (o = n, n = o.handler, r = o.selector), n.guid || (n.guid = J.guid++), (c = g.events) || (c = g.events = {}), (a = g.handle) || (a = g.handle = function (t) {
                        return typeof J !== Ce && J.event.triggered !== t.type ? J.event.dispatch.apply(e, arguments) : void 0
                    }), t = (t || "").match(he) || [""], l = t.length; l--;)s = Ne.exec(t[l]) || [], h = m = s[1], f = (s[2] || "").split(".").sort(), h && (p = J.event.special[h] || {}, h = (r ? p.delegateType : p.bindType) || h, p = J.event.special[h] || {}, u = J.extend({
                        type: h,
                        origType: m,
                        data: i,
                        handler: n,
                        guid: n.guid,
                        selector: r,
                        needsContext: r && J.expr.match.needsContext.test(r),
                        namespace: f.join(".")
                    }, o), (d = c[h]) || (d = c[h] = [], d.delegateCount = 0, p.setup && p.setup.call(e, i, f, a) !== !1 || e.addEventListener && e.addEventListener(h, a, !1)), p.add && (p.add.call(e, u), u.handler.guid || (u.handler.guid = n.guid)), r ? d.splice(d.delegateCount++, 0, u) : d.push(u), J.event.global[h] = !0)
                },
                remove: function (e, t, n, i, r) {
                    var o, a, s, c, l, u, p, d, h, f, m, g = ve.hasData(e) && ve.get(e);
                    if (g && (c = g.events)) {
                        for (t = (t || "").match(he) || [""], l = t.length; l--;)if (s = Ne.exec(t[l]) || [], h = m = s[1], f = (s[2] || "").split(".").sort(), h) {
                            for (p = J.event.special[h] || {}, h = (i ? p.delegateType : p.bindType) || h, d = c[h] || [], s = s[2] && new RegExp("(^|\\.)" + f.join("\\.(?:.*\\.|)") + "(\\.|$)"), a = o = d.length; o--;)u = d[o], !r && m !== u.origType || n && n.guid !== u.guid || s && !s.test(u.namespace) || i && i !== u.selector && ("**" !== i || !u.selector) || (d.splice(o, 1), u.selector && d.delegateCount--, p.remove && p.remove.call(e, u));
                            a && !d.length && (p.teardown && p.teardown.call(e, f, g.handle) !== !1 || J.removeEvent(e, h, g.handle), delete c[h])
                        } else for (h in c)J.event.remove(e, h + t[l], n, i, !0);
                        J.isEmptyObject(c) && (delete g.handle, ve.remove(e, "events"))
                    }
                },
                trigger: function (t, n, i, r) {
                    var o, a, s, c, l, u, p, d = [i || Z], h = X.call(t, "type") ? t.type : t, f = X.call(t, "namespace") ? t.namespace.split(".") : [];
                    if (a = s = i = i || Z, 3 !== i.nodeType && 8 !== i.nodeType && !je.test(h + J.event.triggered) && (h.indexOf(".") >= 0 && (f = h.split("."), h = f.shift(), f.sort()), l = h.indexOf(":") < 0 && "on" + h, t = t[J.expando] ? t : new J.Event(h, "object" == typeof t && t), t.isTrigger = r ? 2 : 3, t.namespace = f.join("."), t.namespace_re = t.namespace ? new RegExp("(^|\\.)" + f.join("\\.(?:.*\\.|)") + "(\\.|$)") : null, t.result = void 0, t.target || (t.target = i), n = null == n ? [t] : J.makeArray(n, [t]), p = J.event.special[h] || {}, r || !p.trigger || p.trigger.apply(i, n) !== !1)) {
                        if (!r && !p.noBubble && !J.isWindow(i)) {
                            for (c = p.delegateType || h, je.test(c + h) || (a = a.parentNode); a; a = a.parentNode)d.push(a), s = a;
                            s === (i.ownerDocument || Z) && d.push(s.defaultView || s.parentWindow || e)
                        }
                        for (o = 0; (a = d[o++]) && !t.isPropagationStopped();)t.type = o > 1 ? c : p.bindType || h, u = (ve.get(a, "events") || {})[t.type] && ve.get(a, "handle"), u && u.apply(a, n), u = l && a[l], u && u.apply && J.acceptData(a) && (t.result = u.apply(a, n), t.result === !1 && t.preventDefault());
                        return t.type = h, r || t.isDefaultPrevented() || p._default && p._default.apply(d.pop(), n) !== !1 || !J.acceptData(i) || l && J.isFunction(i[h]) && !J.isWindow(i) && (s = i[l], s && (i[l] = null), J.event.triggered = h, i[h](), J.event.triggered = void 0, s && (i[l] = s)), t.result
                    }
                },
                dispatch: function (e) {
                    e = J.event.fix(e);
                    var t, n, i, r, o, a = [], s = H.call(arguments), c = (ve.get(this, "events") || {})[e.type] || [], l = J.event.special[e.type] || {};
                    if (s[0] = e, e.delegateTarget = this, !l.preDispatch || l.preDispatch.call(this, e) !== !1) {
                        for (a = J.event.handlers.call(this, e, c), t = 0; (r = a[t++]) && !e.isPropagationStopped();)for (e.currentTarget = r.elem, n = 0; (o = r.handlers[n++]) && !e.isImmediatePropagationStopped();)(!e.namespace_re || e.namespace_re.test(o.namespace)) && (e.handleObj = o, e.data = o.data, i = ((J.event.special[o.origType] || {}).handle || o.handler).apply(r.elem, s), void 0 !== i && (e.result = i) === !1 && (e.preventDefault(), e.stopPropagation()));
                        return l.postDispatch && l.postDispatch.call(this, e), e.result
                    }
                },
                handlers: function (e, t) {
                    var n, i, r, o, a = [], s = t.delegateCount, c = e.target;
                    if (s && c.nodeType && (!e.button || "click" !== e.type))for (; c !== this; c = c.parentNode || this)if (c.disabled !== !0 || "click" !== e.type) {
                        for (i = [], n = 0; s > n; n++)o = t[n], r = o.selector + " ", void 0 === i[r] && (i[r] = o.needsContext ? J(r, this).index(c) >= 0 : J.find(r, this, null, [c]).length), i[r] && i.push(o);
                        i.length && a.push({elem: c, handlers: i})
                    }
                    return s < t.length && a.push({elem: this, handlers: t.slice(s)}), a
                },
                props: "altKey bubbles cancelable ctrlKey currentTarget eventPhase metaKey relatedTarget shiftKey target timeStamp view which".split(" "),
                fixHooks: {},
                keyHooks: {
                    props: "char charCode key keyCode".split(" "), filter: function (e, t) {
                        return null == e.which && (e.which = null != t.charCode ? t.charCode : t.keyCode), e
                    }
                },
                mouseHooks: {
                    props: "button buttons clientX clientY offsetX offsetY pageX pageY screenX screenY toElement".split(" "),
                    filter: function (e, t) {
                        var n, i, r, o = t.button;
                        return null == e.pageX && null != t.clientX && (n = e.target.ownerDocument || Z, i = n.documentElement, r = n.body, e.pageX = t.clientX + (i && i.scrollLeft || r && r.scrollLeft || 0) - (i && i.clientLeft || r && r.clientLeft || 0), e.pageY = t.clientY + (i && i.scrollTop || r && r.scrollTop || 0) - (i && i.clientTop || r && r.clientTop || 0)), e.which || void 0 === o || (e.which = 1 & o ? 1 : 2 & o ? 3 : 4 & o ? 2 : 0), e
                    }
                },
                fix: function (e) {
                    if (e[J.expando])return e;
                    var t, n, i, r = e.type, o = e, a = this.fixHooks[r];
                    for (a || (this.fixHooks[r] = a = Te.test(r) ? this.mouseHooks : Ae.test(r) ? this.keyHooks : {}), i = a.props ? this.props.concat(a.props) : this.props, e = new J.Event(o), t = i.length; t--;)n = i[t], e[n] = o[n];
                    return e.target || (e.target = Z), 3 === e.target.nodeType && (e.target = e.target.parentNode), a.filter ? a.filter(e, o) : e
                },
                special: {
                    load: {noBubble: !0}, focus: {
                        trigger: function () {
                            return this !== p() && this.focus ? (this.focus(), !1) : void 0
                        }, delegateType: "focusin"
                    }, blur: {
                        trigger: function () {
                            return this === p() && this.blur ? (this.blur(), !1) : void 0
                        }, delegateType: "focusout"
                    }, click: {
                        trigger: function () {
                            return "checkbox" === this.type && this.click && J.nodeName(this, "input") ? (this.click(), !1) : void 0
                        }, _default: function (e) {
                            return J.nodeName(e.target, "a")
                        }
                    }, beforeunload: {
                        postDispatch: function (e) {
                            void 0 !== e.result && e.originalEvent && (e.originalEvent.returnValue = e.result)
                        }
                    }
                },
                simulate: function (e, t, n, i) {
                    var r = J.extend(new J.Event, n, {type: e, isSimulated: !0, originalEvent: {}});
                    i ? J.event.trigger(r, null, t) : J.event.dispatch.call(t, r), r.isDefaultPrevented() && n.preventDefault()
                }
            }, J.removeEvent = function (e, t, n) {
                e.removeEventListener && e.removeEventListener(t, n, !1)
            }, J.Event = function (e, t) {
                return this instanceof J.Event ? (e && e.type ? (this.originalEvent = e, this.type = e.type, this.isDefaultPrevented = e.defaultPrevented || void 0 === e.defaultPrevented && e.returnValue === !1 ? l : u) : this.type = e, t && J.extend(this, t), this.timeStamp = e && e.timeStamp || J.now(), void(this[J.expando] = !0)) : new J.Event(e, t)
            }, J.Event.prototype = {
                isDefaultPrevented: u,
                isPropagationStopped: u,
                isImmediatePropagationStopped: u,
                preventDefault: function () {
                    var e = this.originalEvent;
                    this.isDefaultPrevented = l, e && e.preventDefault && e.preventDefault()
                },
                stopPropagation: function () {
                    var e = this.originalEvent;
                    this.isPropagationStopped = l, e && e.stopPropagation && e.stopPropagation()
                },
                stopImmediatePropagation: function () {
                    var e = this.originalEvent;
                    this.isImmediatePropagationStopped = l, e && e.stopImmediatePropagation && e.stopImmediatePropagation(), this.stopPropagation()
                }
            }, J.each({
                mouseenter: "mouseover",
                mouseleave: "mouseout",
                pointerenter: "pointerover",
                pointerleave: "pointerout"
            }, function (e, t) {
                J.event.special[e] = {
                    delegateType: t, bindType: t, handle: function (e) {
                        var n, i = this, r = e.relatedTarget, o = e.handleObj;
                        return (!r || r !== i && !J.contains(i, r)) && (e.type = o.origType, n = o.handler.apply(this, arguments), e.type = t), n
                    }
                }
            }), K.focusinBubbles || J.each({focus: "focusin", blur: "focusout"}, function (e, t) {
                var n = function (e) {
                    J.event.simulate(t, e.target, J.event.fix(e), !0)
                };
                J.event.special[t] = {
                    setup: function () {
                        var i = this.ownerDocument || this, r = ve.access(i, t);
                        r || i.addEventListener(e, n, !0), ve.access(i, t, (r || 0) + 1)
                    }, teardown: function () {
                        var i = this.ownerDocument || this, r = ve.access(i, t) - 1;
                        r ? ve.access(i, t, r) : (i.removeEventListener(e, n, !0), ve.remove(i, t))
                    }
                }
            }), J.fn.extend({
                on: function (e, t, n, i, r) {
                    var o, a;
                    if ("object" == typeof e) {
                        "string" != typeof t && (n = n || t, t = void 0);
                        for (a in e)this.on(a, t, n, e[a], r);
                        return this
                    }
                    if (null == n && null == i ? (i = t, n = t = void 0) : null == i && ("string" == typeof t ? (i = n, n = void 0) : (i = n, n = t, t = void 0)), i === !1)i = u; else if (!i)return this;
                    return 1 === r && (o = i, i = function (e) {
                        return J().off(e), o.apply(this, arguments)
                    }, i.guid = o.guid || (o.guid = J.guid++)), this.each(function () {
                        J.event.add(this, e, i, n, t)
                    })
                }, one: function (e, t, n, i) {
                    return this.on(e, t, n, i, 1)
                }, off: function (e, t, n) {
                    var i, r;
                    if (e && e.preventDefault && e.handleObj)return i = e.handleObj, J(e.delegateTarget).off(i.namespace ? i.origType + "." + i.namespace : i.origType, i.selector, i.handler), this;
                    if ("object" == typeof e) {
                        for (r in e)this.off(r, t, e[r]);
                        return this
                    }
                    return (t === !1 || "function" == typeof t) && (n = t, t = void 0), n === !1 && (n = u), this.each(function () {
                        J.event.remove(this, e, n, t)
                    })
                }, trigger: function (e, t) {
                    return this.each(function () {
                        J.event.trigger(e, t, this)
                    })
                }, triggerHandler: function (e, t) {
                    var n = this[0];
                    return n ? J.event.trigger(e, t, n, !0) : void 0
                }
            });
            var ke = /<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:]+)[^>]*)\/>/gi, Re = /<([\w:]+)/, Me = /<|&#?\w+;/, De = /<(?:script|style|link)/i, Pe = /checked\s*(?:[^=]|=\s*.checked.)/i, Be = /^$|\/(?:java|ecma)script/i, Le = /^true\/(.*)/, Oe = /^\s*<!(?:\[CDATA\[|--)|(?:\]\]|--)>\s*$/g, Ie = {
                option: [1, "<select multiple='multiple'>", "</select>"],
                thead: [1, "<table>", "</table>"],
                col: [2, "<table><colgroup>", "</colgroup></table>"],
                tr: [2, "<table><tbody>", "</tbody></table>"],
                td: [3, "<table><tbody><tr>", "</tr></tbody></table>"],
                _default: [0, "", ""]
            };
            Ie.optgroup = Ie.option, Ie.tbody = Ie.tfoot = Ie.colgroup = Ie.caption = Ie.thead, Ie.th = Ie.td, J.extend({
                clone: function (e, t, n) {
                    var i, r, o, a, s = e.cloneNode(!0), c = J.contains(e.ownerDocument, e);
                    if (!(K.noCloneChecked || 1 !== e.nodeType && 11 !== e.nodeType || J.isXMLDoc(e)))for (a = v(s), o = v(e), i = 0, r = o.length; r > i; i++)y(o[i], a[i]);
                    if (t)if (n)for (o = o || v(e), a = a || v(s), i = 0, r = o.length; r > i; i++)g(o[i], a[i]); else g(e, s);
                    return a = v(s, "script"), a.length > 0 && m(a, !c && v(e, "script")), s
                }, buildFragment: function (e, t, n, i) {
                    for (var r, o, a, s, c, l, u = t.createDocumentFragment(), p = [], d = 0, h = e.length; h > d; d++)if (r = e[d], r || 0 === r)if ("object" === J.type(r))J.merge(p, r.nodeType ? [r] : r); else if (Me.test(r)) {
                        for (o = o || u.appendChild(t.createElement("div")), a = (Re.exec(r) || ["", ""])[1].toLowerCase(), s = Ie[a] || Ie._default, o.innerHTML = s[1] + r.replace(ke, "<$1></$2>") + s[2], l = s[0]; l--;)o = o.lastChild;
                        J.merge(p, o.childNodes), o = u.firstChild, o.textContent = ""
                    } else p.push(t.createTextNode(r));
                    for (u.textContent = "", d = 0; r = p[d++];)if ((!i || -1 === J.inArray(r, i)) && (c = J.contains(r.ownerDocument, r), o = v(u.appendChild(r), "script"), c && m(o), n))for (l = 0; r = o[l++];)Be.test(r.type || "") && n.push(r);
                    return u
                }, cleanData: function (e) {
                    for (var t, n, i, r, o = J.event.special, a = 0; void 0 !== (n = e[a]); a++) {
                        if (J.acceptData(n) && (r = n[ve.expando], r && (t = ve.cache[r]))) {
                            if (t.events)for (i in t.events)o[i] ? J.event.remove(n, i) : J.removeEvent(n, i, t.handle);
                            ve.cache[r] && delete ve.cache[r]
                        }
                        delete ye.cache[n[ye.expando]]
                    }
                }
            }), J.fn.extend({
                text: function (e) {
                    return ge(this, function (e) {
                        return void 0 === e ? J.text(this) : this.empty().each(function () {
                            (1 === this.nodeType || 11 === this.nodeType || 9 === this.nodeType) && (this.textContent = e)
                        })
                    }, null, e, arguments.length)
                }, append: function () {
                    return this.domManip(arguments, function (e) {
                        if (1 === this.nodeType || 11 === this.nodeType || 9 === this.nodeType) {
                            var t = d(this, e);
                            t.appendChild(e)
                        }
                    })
                }, prepend: function () {
                    return this.domManip(arguments, function (e) {
                        if (1 === this.nodeType || 11 === this.nodeType || 9 === this.nodeType) {
                            var t = d(this, e);
                            t.insertBefore(e, t.firstChild)
                        }
                    })
                }, before: function () {
                    return this.domManip(arguments, function (e) {
                        this.parentNode && this.parentNode.insertBefore(e, this)
                    })
                }, after: function () {
                    return this.domManip(arguments, function (e) {
                        this.parentNode && this.parentNode.insertBefore(e, this.nextSibling)
                    })
                }, remove: function (e, t) {
                    for (var n, i = e ? J.filter(e, this) : this, r = 0; null != (n = i[r]); r++)t || 1 !== n.nodeType || J.cleanData(v(n)), n.parentNode && (t && J.contains(n.ownerDocument, n) && m(v(n, "script")), n.parentNode.removeChild(n));
                    return this
                }, empty: function () {
                    for (var e, t = 0; null != (e = this[t]); t++)1 === e.nodeType && (J.cleanData(v(e, !1)), e.textContent = "");
                    return this
                }, clone: function (e, t) {
                    return e = null == e ? !1 : e, t = null == t ? e : t, this.map(function () {
                        return J.clone(this, e, t)
                    })
                }, html: function (e) {
                    return ge(this, function (e) {
                        var t = this[0] || {}, n = 0, i = this.length;
                        if (void 0 === e && 1 === t.nodeType)return t.innerHTML;
                        if ("string" == typeof e && !De.test(e) && !Ie[(Re.exec(e) || ["", ""])[1].toLowerCase()]) {
                            e = e.replace(ke, "<$1></$2>");
                            try {
                                for (; i > n; n++)t = this[n] || {}, 1 === t.nodeType && (J.cleanData(v(t, !1)), t.innerHTML = e);
                                t = 0
                            } catch (r) {
                            }
                        }
                        t && this.empty().append(e)
                    }, null, e, arguments.length)
                }, replaceWith: function () {
                    var e = arguments[0];
                    return this.domManip(arguments, function (t) {
                        e = this.parentNode, J.cleanData(v(this)), e && e.replaceChild(t, this)
                    }), e && (e.length || e.nodeType) ? this : this.remove()
                }, detach: function (e) {
                    return this.remove(e, !0)
                }, domManip: function (e, t) {
                    e = q.apply([], e);
                    var n, i, r, o, a, s, c = 0, l = this.length, u = this, p = l - 1, d = e[0], m = J.isFunction(d);
                    if (m || l > 1 && "string" == typeof d && !K.checkClone && Pe.test(d))return this.each(function (n) {
                        var i = u.eq(n);
                        m && (e[0] = d.call(this, n, i.html())), i.domManip(e, t)
                    });
                    if (l && (n = J.buildFragment(e, this[0].ownerDocument, !1, this), i = n.firstChild, 1 === n.childNodes.length && (n = i), i)) {
                        for (r = J.map(v(n, "script"), h), o = r.length; l > c; c++)a = n, c !== p && (a = J.clone(a, !0, !0), o && J.merge(r, v(a, "script"))), t.call(this[c], a, c);
                        if (o)for (s = r[r.length - 1].ownerDocument, J.map(r, f), c = 0; o > c; c++)a = r[c], Be.test(a.type || "") && !ve.access(a, "globalEval") && J.contains(s, a) && (a.src ? J._evalUrl && J._evalUrl(a.src) : J.globalEval(a.textContent.replace(Oe, "")))
                    }
                    return this
                }
            }), J.each({
                appendTo: "append",
                prependTo: "prepend",
                insertBefore: "before",
                insertAfter: "after",
                replaceAll: "replaceWith"
            }, function (e, t) {
                J.fn[e] = function (e) {
                    for (var n, i = [], r = J(e), o = r.length - 1, a = 0; o >= a; a++)n = a === o ? this : this.clone(!0), J(r[a])[t](n), G.apply(i, n.get());
                    return this.pushStack(i)
                }
            });
            var Fe, Ue = {}, ze = /^margin/, $e = new RegExp("^(" + Ee + ")(?!px)[a-z%]+$", "i"), He = function (t) {
                return t.ownerDocument.defaultView.opener ? t.ownerDocument.defaultView.getComputedStyle(t, null) : e.getComputedStyle(t, null)
            };
            !function () {
                function t() {
                    a.style.cssText = "-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;display:block;margin-top:1%;top:1%;border:1px;padding:1px;width:4px;position:absolute", a.innerHTML = "", r.appendChild(o);
                    var t = e.getComputedStyle(a, null);
                    n = "1%" !== t.top, i = "4px" === t.width, r.removeChild(o)
                }

                var n, i, r = Z.documentElement, o = Z.createElement("div"), a = Z.createElement("div");
                a.style && (a.style.backgroundClip = "content-box", a.cloneNode(!0).style.backgroundClip = "", K.clearCloneStyle = "content-box" === a.style.backgroundClip, o.style.cssText = "border:0;width:0;height:0;top:0;left:-9999px;margin-top:1px;position:absolute", o.appendChild(a), e.getComputedStyle && J.extend(K, {
                    pixelPosition: function () {
                        return t(), n
                    }, boxSizingReliable: function () {
                        return null == i && t(), i
                    }, reliableMarginRight: function () {
                        var t, n = a.appendChild(Z.createElement("div"));
                        return n.style.cssText = a.style.cssText = "-webkit-box-sizing:content-box;-moz-box-sizing:content-box;box-sizing:content-box;display:block;margin:0;border:0;padding:0", n.style.marginRight = n.style.width = "0", a.style.width = "1px", r.appendChild(o), t = !parseFloat(e.getComputedStyle(n, null).marginRight), r.removeChild(o), a.removeChild(n), t
                    }
                }))
            }(), J.swap = function (e, t, n, i) {
                var r, o, a = {};
                for (o in t)a[o] = e.style[o], e.style[o] = t[o];
                r = n.apply(e, i || []);
                for (o in t)e.style[o] = a[o];
                return r
            };
            var qe = /^(none|table(?!-c[ea]).+)/, Ge = new RegExp("^(" + Ee + ")(.*)$", "i"), We = new RegExp("^([+-])=(" + Ee + ")", "i"), Ve = {
                position: "absolute",
                visibility: "hidden",
                display: "block"
            }, Ye = {letterSpacing: "0", fontWeight: "400"}, Xe = ["Webkit", "O", "Moz", "ms"];
            J.extend({
                cssHooks: {
                    opacity: {
                        get: function (e, t) {
                            if (t) {
                                var n = E(e, "opacity");
                                return "" === n ? "1" : n
                            }
                        }
                    }
                },
                cssNumber: {
                    columnCount: !0,
                    fillOpacity: !0,
                    flexGrow: !0,
                    flexShrink: !0,
                    fontWeight: !0,
                    lineHeight: !0,
                    opacity: !0,
                    order: !0,
                    orphans: !0,
                    widows: !0,
                    zIndex: !0,
                    zoom: !0
                },
                cssProps: {"float": "cssFloat"},
                style: function (e, t, n, i) {
                    if (e && 3 !== e.nodeType && 8 !== e.nodeType && e.style) {
                        var r, o, a, s = J.camelCase(t), c = e.style;
                        return t = J.cssProps[s] || (J.cssProps[s] = _(c, s)), a = J.cssHooks[t] || J.cssHooks[s], void 0 === n ? a && "get"in a && void 0 !== (r = a.get(e, !1, i)) ? r : c[t] : (o = typeof n, "string" === o && (r = We.exec(n)) && (n = (r[1] + 1) * r[2] + parseFloat(J.css(e, t)), o = "number"), null != n && n === n && ("number" !== o || J.cssNumber[s] || (n += "px"), K.clearCloneStyle || "" !== n || 0 !== t.indexOf("background") || (c[t] = "inherit"), a && "set"in a && void 0 === (n = a.set(e, n, i)) || (c[t] = n)), void 0)
                    }
                },
                css: function (e, t, n, i) {
                    var r, o, a, s = J.camelCase(t);
                    return t = J.cssProps[s] || (J.cssProps[s] = _(e.style, s)), a = J.cssHooks[t] || J.cssHooks[s], a && "get"in a && (r = a.get(e, !0, n)), void 0 === r && (r = E(e, t, i)), "normal" === r && t in Ye && (r = Ye[t]), "" === n || n ? (o = parseFloat(r), n === !0 || J.isNumeric(o) ? o || 0 : r) : r
                }
            }), J.each(["height", "width"], function (e, t) {
                J.cssHooks[t] = {
                    get: function (e, n, i) {
                        return n ? qe.test(J.css(e, "display")) && 0 === e.offsetWidth ? J.swap(e, Ve, function () {
                            return A(e, t, i)
                        }) : A(e, t, i) : void 0
                    }, set: function (e, n, i) {
                        var r = i && He(e);
                        return S(e, n, i ? C(e, t, i, "border-box" === J.css(e, "boxSizing", !1, r), r) : 0)
                    }
                }
            }), J.cssHooks.marginRight = w(K.reliableMarginRight, function (e, t) {
                return t ? J.swap(e, {display: "inline-block"}, E, [e, "marginRight"]) : void 0
            }), J.each({margin: "", padding: "", border: "Width"}, function (e, t) {
                J.cssHooks[e + t] = {
                    expand: function (n) {
                        for (var i = 0, r = {}, o = "string" == typeof n ? n.split(" ") : [n]; 4 > i; i++)r[e + we[i] + t] = o[i] || o[i - 2] || o[0];
                        return r
                    }
                }, ze.test(e) || (J.cssHooks[e + t].set = S)
            }), J.fn.extend({
                css: function (e, t) {
                    return ge(this, function (e, t, n) {
                        var i, r, o = {}, a = 0;
                        if (J.isArray(t)) {
                            for (i = He(e), r = t.length; r > a; a++)o[t[a]] = J.css(e, t[a], !1, i);
                            return o
                        }
                        return void 0 !== n ? J.style(e, t, n) : J.css(e, t)
                    }, e, t, arguments.length > 1)
                }, show: function () {
                    return T(this, !0)
                }, hide: function () {
                    return T(this)
                }, toggle: function (e) {
                    return "boolean" == typeof e ? e ? this.show() : this.hide() : this.each(function () {
                        _e(this) ? J(this).show() : J(this).hide()
                    })
                }
            }), J.Tween = j, j.prototype = {
                constructor: j, init: function (e, t, n, i, r, o) {
                    this.elem = e, this.prop = n, this.easing = r || "swing", this.options = t, this.start = this.now = this.cur(), this.end = i, this.unit = o || (J.cssNumber[n] ? "" : "px")
                }, cur: function () {
                    var e = j.propHooks[this.prop];
                    return e && e.get ? e.get(this) : j.propHooks._default.get(this)
                }, run: function (e) {
                    var t, n = j.propHooks[this.prop];
                    return this.options.duration ? this.pos = t = J.easing[this.easing](e, this.options.duration * e, 0, 1, this.options.duration) : this.pos = t = e, this.now = (this.end - this.start) * t + this.start, this.options.step && this.options.step.call(this.elem, this.now, this), n && n.set ? n.set(this) : j.propHooks._default.set(this), this
                }
            }, j.prototype.init.prototype = j.prototype, j.propHooks = {
                _default: {
                    get: function (e) {
                        var t;
                        return null == e.elem[e.prop] || e.elem.style && null != e.elem.style[e.prop] ? (t = J.css(e.elem, e.prop, ""), t && "auto" !== t ? t : 0) : e.elem[e.prop]
                    }, set: function (e) {
                        J.fx.step[e.prop] ? J.fx.step[e.prop](e) : e.elem.style && (null != e.elem.style[J.cssProps[e.prop]] || J.cssHooks[e.prop]) ? J.style(e.elem, e.prop, e.now + e.unit) : e.elem[e.prop] = e.now
                    }
                }
            }, j.propHooks.scrollTop = j.propHooks.scrollLeft = {
                set: function (e) {
                    e.elem.nodeType && e.elem.parentNode && (e.elem[e.prop] = e.now)
                }
            }, J.easing = {
                linear: function (e) {
                    return e
                }, swing: function (e) {
                    return .5 - Math.cos(e * Math.PI) / 2
                }
            }, J.fx = j.prototype.init, J.fx.step = {};
            var Ke, Ze, Qe = /^(?:toggle|show|hide)$/, Je = new RegExp("^(?:([+-])=|)(" + Ee + ")([a-z%]*)$", "i"), et = /queueHooks$/, tt = [M], nt = {
                "*": [function (e, t) {
                    var n = this.createTween(e, t), i = n.cur(), r = Je.exec(t), o = r && r[3] || (J.cssNumber[e] ? "" : "px"), a = (J.cssNumber[e] || "px" !== o && +i) && Je.exec(J.css(n.elem, e)), s = 1, c = 20;
                    if (a && a[3] !== o) {
                        o = o || a[3], r = r || [], a = +i || 1;
                        do s = s || ".5", a /= s, J.style(n.elem, e, a + o); while (s !== (s = n.cur() / i) && 1 !== s && --c)
                    }
                    return r && (a = n.start = +a || +i || 0, n.unit = o, n.end = r[1] ? a + (r[1] + 1) * r[2] : +r[2]), n
                }]
            };
            J.Animation = J.extend(P, {
                tweener: function (e, t) {
                    J.isFunction(e) ? (t = e, e = ["*"]) : e = e.split(" ");
                    for (var n, i = 0, r = e.length; r > i; i++)n = e[i], nt[n] = nt[n] || [], nt[n].unshift(t)
                }, prefilter: function (e, t) {
                    t ? tt.unshift(e) : tt.push(e)
                }
            }), J.speed = function (e, t, n) {
                var i = e && "object" == typeof e ? J.extend({}, e) : {
                    complete: n || !n && t || J.isFunction(e) && e,
                    duration: e,
                    easing: n && t || t && !J.isFunction(t) && t
                };
                return i.duration = J.fx.off ? 0 : "number" == typeof i.duration ? i.duration : i.duration in J.fx.speeds ? J.fx.speeds[i.duration] : J.fx.speeds._default, (null == i.queue || i.queue === !0) && (i.queue = "fx"), i.old = i.complete, i.complete = function () {
                    J.isFunction(i.old) && i.old.call(this), i.queue && J.dequeue(this, i.queue)
                }, i
            }, J.fn.extend({
                fadeTo: function (e, t, n, i) {
                    return this.filter(_e).css("opacity", 0).show().end().animate({opacity: t}, e, n, i)
                }, animate: function (e, t, n, i) {
                    var r = J.isEmptyObject(e), o = J.speed(t, n, i), a = function () {
                        var t = P(this, J.extend({}, e), o);
                        (r || ve.get(this, "finish")) && t.stop(!0)
                    };
                    return a.finish = a, r || o.queue === !1 ? this.each(a) : this.queue(o.queue, a)
                }, stop: function (e, t, n) {
                    var i = function (e) {
                        var t = e.stop;
                        delete e.stop, t(n)
                    };
                    return "string" != typeof e && (n = t, t = e, e = void 0), t && e !== !1 && this.queue(e || "fx", []), this.each(function () {
                        var t = !0, r = null != e && e + "queueHooks", o = J.timers, a = ve.get(this);
                        if (r)a[r] && a[r].stop && i(a[r]); else for (r in a)a[r] && a[r].stop && et.test(r) && i(a[r]);
                        for (r = o.length; r--;)o[r].elem !== this || null != e && o[r].queue !== e || (o[r].anim.stop(n), t = !1, o.splice(r, 1));
                        (t || !n) && J.dequeue(this, e)
                    })
                }, finish: function (e) {
                    return e !== !1 && (e = e || "fx"), this.each(function () {
                        var t, n = ve.get(this), i = n[e + "queue"], r = n[e + "queueHooks"], o = J.timers, a = i ? i.length : 0;
                        for (n.finish = !0, J.queue(this, e, []), r && r.stop && r.stop.call(this, !0), t = o.length; t--;)o[t].elem === this && o[t].queue === e && (o[t].anim.stop(!0), o.splice(t, 1));
                        for (t = 0; a > t; t++)i[t] && i[t].finish && i[t].finish.call(this);
                        delete n.finish
                    })
                }
            }), J.each(["toggle", "show", "hide"], function (e, t) {
                var n = J.fn[t];
                J.fn[t] = function (e, i, r) {
                    return null == e || "boolean" == typeof e ? n.apply(this, arguments) : this.animate(k(t, !0), e, i, r)
                }
            }), J.each({
                slideDown: k("show"),
                slideUp: k("hide"),
                slideToggle: k("toggle"),
                fadeIn: {opacity: "show"},
                fadeOut: {opacity: "hide"},
                fadeToggle: {opacity: "toggle"}
            }, function (e, t) {
                J.fn[e] = function (e, n, i) {
                    return this.animate(t, e, n, i)
                }
            }), J.timers = [], J.fx.tick = function () {
                var e, t = 0, n = J.timers;
                for (Ke = J.now(); t < n.length; t++)e = n[t], e() || n[t] !== e || n.splice(t--, 1);
                n.length || J.fx.stop(), Ke = void 0
            }, J.fx.timer = function (e) {
                J.timers.push(e), e() ? J.fx.start() : J.timers.pop()
            }, J.fx.interval = 13, J.fx.start = function () {
                Ze || (Ze = setInterval(J.fx.tick, J.fx.interval))
            }, J.fx.stop = function () {
                clearInterval(Ze), Ze = null
            }, J.fx.speeds = {slow: 600, fast: 200, _default: 400}, J.fn.delay = function (e, t) {
                return e = J.fx ? J.fx.speeds[e] || e : e, t = t || "fx", this.queue(t, function (t, n) {
                    var i = setTimeout(t, e);
                    n.stop = function () {
                        clearTimeout(i)
                    }
                })
            }, function () {
                var e = Z.createElement("input"), t = Z.createElement("select"), n = t.appendChild(Z.createElement("option"));
                e.type = "checkbox", K.checkOn = "" !== e.value, K.optSelected = n.selected, t.disabled = !0, K.optDisabled = !n.disabled, e = Z.createElement("input"), e.value = "t", e.type = "radio", K.radioValue = "t" === e.value
            }();
            var it, rt, ot = J.expr.attrHandle;
            J.fn.extend({
                attr: function (e, t) {
                    return ge(this, J.attr, e, t, arguments.length > 1)
                }, removeAttr: function (e) {
                    return this.each(function () {
                        J.removeAttr(this, e)
                    })
                }
            }), J.extend({
                attr: function (e, t, n) {
                    var i, r, o = e.nodeType;
                    if (e && 3 !== o && 8 !== o && 2 !== o)return typeof e.getAttribute === Ce ? J.prop(e, t, n) : (1 === o && J.isXMLDoc(e) || (t = t.toLowerCase(), i = J.attrHooks[t] || (J.expr.match.bool.test(t) ? rt : it)), void 0 === n ? i && "get"in i && null !== (r = i.get(e, t)) ? r : (r = J.find.attr(e, t), null == r ? void 0 : r) : null !== n ? i && "set"in i && void 0 !== (r = i.set(e, n, t)) ? r : (e.setAttribute(t, n + ""), n) : void J.removeAttr(e, t))
                }, removeAttr: function (e, t) {
                    var n, i, r = 0, o = t && t.match(he);
                    if (o && 1 === e.nodeType)for (; n = o[r++];)i = J.propFix[n] || n, J.expr.match.bool.test(n) && (e[i] = !1), e.removeAttribute(n)
                }, attrHooks: {
                    type: {
                        set: function (e, t) {
                            if (!K.radioValue && "radio" === t && J.nodeName(e, "input")) {
                                var n = e.value;
                                return e.setAttribute("type", t), n && (e.value = n), t
                            }
                        }
                    }
                }
            }), rt = {
                set: function (e, t, n) {
                    return t === !1 ? J.removeAttr(e, n) : e.setAttribute(n, n), n
                }
            }, J.each(J.expr.match.bool.source.match(/\w+/g), function (e, t) {
                var n = ot[t] || J.find.attr;
                ot[t] = function (e, t, i) {
                    var r, o;
                    return i || (o = ot[t], ot[t] = r, r = null != n(e, t, i) ? t.toLowerCase() : null, ot[t] = o), r
                }
            });
            var at = /^(?:input|select|textarea|button)$/i;
            J.fn.extend({
                prop: function (e, t) {
                    return ge(this, J.prop, e, t, arguments.length > 1)
                }, removeProp: function (e) {
                    return this.each(function () {
                        delete this[J.propFix[e] || e]
                    })
                }
            }), J.extend({
                propFix: {"for": "htmlFor", "class": "className"}, prop: function (e, t, n) {
                    var i, r, o, a = e.nodeType;
                    if (e && 3 !== a && 8 !== a && 2 !== a)return o = 1 !== a || !J.isXMLDoc(e), o && (t = J.propFix[t] || t, r = J.propHooks[t]), void 0 !== n ? r && "set"in r && void 0 !== (i = r.set(e, n, t)) ? i : e[t] = n : r && "get"in r && null !== (i = r.get(e, t)) ? i : e[t]
                }, propHooks: {
                    tabIndex: {
                        get: function (e) {
                            return e.hasAttribute("tabindex") || at.test(e.nodeName) || e.href ? e.tabIndex : -1
                        }
                    }
                }
            }), K.optSelected || (J.propHooks.selected = {
                get: function (e) {
                    var t = e.parentNode;
                    return t && t.parentNode && t.parentNode.selectedIndex, null
                }
            }), J.each(["tabIndex", "readOnly", "maxLength", "cellSpacing", "cellPadding", "rowSpan", "colSpan", "useMap", "frameBorder", "contentEditable"], function () {
                J.propFix[this.toLowerCase()] = this
            });
            var st = /[\t\r\n\f]/g;
            J.fn.extend({
                addClass: function (e) {
                    var t, n, i, r, o, a, s = "string" == typeof e && e, c = 0, l = this.length;
                    if (J.isFunction(e))return this.each(function (t) {
                        J(this).addClass(e.call(this, t, this.className))
                    });
                    if (s)for (t = (e || "").match(he) || []; l > c; c++)if (n = this[c], i = 1 === n.nodeType && (n.className ? (" " + n.className + " ").replace(st, " ") : " ")) {
                        for (o = 0; r = t[o++];)i.indexOf(" " + r + " ") < 0 && (i += r + " ");
                        a = J.trim(i), n.className !== a && (n.className = a)
                    }
                    return this
                }, removeClass: function (e) {
                    var t, n, i, r, o, a, s = 0 === arguments.length || "string" == typeof e && e, c = 0, l = this.length;
                    if (J.isFunction(e))return this.each(function (t) {
                        J(this).removeClass(e.call(this, t, this.className))
                    });
                    if (s)for (t = (e || "").match(he) || []; l > c; c++)if (n = this[c], i = 1 === n.nodeType && (n.className ? (" " + n.className + " ").replace(st, " ") : "")) {
                        for (o = 0; r = t[o++];)for (; i.indexOf(" " + r + " ") >= 0;)i = i.replace(" " + r + " ", " ");
                        a = e ? J.trim(i) : "", n.className !== a && (n.className = a)
                    }
                    return this
                }, toggleClass: function (e, t) {
                    var n = typeof e;
                    return "boolean" == typeof t && "string" === n ? t ? this.addClass(e) : this.removeClass(e) : J.isFunction(e) ? this.each(function (n) {
                        J(this).toggleClass(e.call(this, n, this.className, t), t)
                    }) : this.each(function () {
                        if ("string" === n)for (var t, i = 0, r = J(this), o = e.match(he) || []; t = o[i++];)r.hasClass(t) ? r.removeClass(t) : r.addClass(t); else(n === Ce || "boolean" === n) && (this.className && ve.set(this, "__className__", this.className), this.className = this.className || e === !1 ? "" : ve.get(this, "__className__") || "")
                    })
                }, hasClass: function (e) {
                    for (var t = " " + e + " ", n = 0, i = this.length; i > n; n++)if (1 === this[n].nodeType && (" " + this[n].className + " ").replace(st, " ").indexOf(t) >= 0)return !0;
                    return !1
                }
            });
            var ct = /\r/g;
            J.fn.extend({
                val: function (e) {
                    var t, n, i, r = this[0];
                    {
                        if (arguments.length)return i = J.isFunction(e), this.each(function (n) {
                            var r;
                            1 === this.nodeType && (r = i ? e.call(this, n, J(this).val()) : e, null == r ? r = "" : "number" == typeof r ? r += "" : J.isArray(r) && (r = J.map(r, function (e) {
                                return null == e ? "" : e + ""
                            })), t = J.valHooks[this.type] || J.valHooks[this.nodeName.toLowerCase()], t && "set"in t && void 0 !== t.set(this, r, "value") || (this.value = r))
                        });
                        if (r)return t = J.valHooks[r.type] || J.valHooks[r.nodeName.toLowerCase()], t && "get"in t && void 0 !== (n = t.get(r, "value")) ? n : (n = r.value, "string" == typeof n ? n.replace(ct, "") : null == n ? "" : n)
                    }
                }
            }), J.extend({
                valHooks: {
                    option: {
                        get: function (e) {
                            var t = J.find.attr(e, "value");
                            return null != t ? t : J.trim(J.text(e))
                        }
                    }, select: {
                        get: function (e) {
                            for (var t, n, i = e.options, r = e.selectedIndex, o = "select-one" === e.type || 0 > r, a = o ? null : [], s = o ? r + 1 : i.length, c = 0 > r ? s : o ? r : 0; s > c; c++)if (n = i[c], (n.selected || c === r) && (K.optDisabled ? !n.disabled : null === n.getAttribute("disabled")) && (!n.parentNode.disabled || !J.nodeName(n.parentNode, "optgroup"))) {
                                if (t = J(n).val(), o)return t;
                                a.push(t)
                            }
                            return a
                        }, set: function (e, t) {
                            for (var n, i, r = e.options, o = J.makeArray(t), a = r.length; a--;)i = r[a], (i.selected = J.inArray(i.value, o) >= 0) && (n = !0);
                            return n || (e.selectedIndex = -1), o
                        }
                    }
                }
            }), J.each(["radio", "checkbox"], function () {
                J.valHooks[this] = {
                    set: function (e, t) {
                        return J.isArray(t) ? e.checked = J.inArray(J(e).val(), t) >= 0 : void 0
                    }
                }, K.checkOn || (J.valHooks[this].get = function (e) {
                    return null === e.getAttribute("value") ? "on" : e.value
                })
            }), J.each("blur focus focusin focusout load resize scroll unload click dblclick mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave change select submit keydown keypress keyup error contextmenu".split(" "), function (e, t) {
                J.fn[t] = function (e, n) {
                    return arguments.length > 0 ? this.on(t, null, e, n) : this.trigger(t)
                }
            }), J.fn.extend({
                hover: function (e, t) {
                    return this.mouseenter(e).mouseleave(t || e)
                }, bind: function (e, t, n) {
                    return this.on(e, null, t, n)
                }, unbind: function (e, t) {
                    return this.off(e, null, t)
                }, delegate: function (e, t, n, i) {
                    return this.on(t, e, n, i)
                }, undelegate: function (e, t, n) {
                    return 1 === arguments.length ? this.off(e, "**") : this.off(t, e || "**", n)
                }
            });
            var lt = J.now(), ut = /\?/;
            J.parseJSON = function (e) {
                return JSON.parse(e + "")
            }, J.parseXML = function (e) {
                var t, n;
                if (!e || "string" != typeof e)return null;
                try {
                    n = new DOMParser, t = n.parseFromString(e, "text/xml")
                } catch (i) {
                    t = void 0
                }
                return (!t || t.getElementsByTagName("parsererror").length) && J.error("Invalid XML: " + e), t
            };
            var pt = /#.*$/, dt = /([?&])_=[^&]*/, ht = /^(.*?):[ \t]*([^\r\n]*)$/gm, ft = /^(?:about|app|app-storage|.+-extension|file|res|widget):$/, mt = /^(?:GET|HEAD)$/, gt = /^\/\//, vt = /^([\w.+-]+:)(?:\/\/(?:[^\/?#]*@|)([^\/?#:]*)(?::(\d+)|)|)/, yt = {}, bt = {}, xt = "*/".concat("*"), Et = e.location.href, wt = vt.exec(Et.toLowerCase()) || [];
            J.extend({
                active: 0,
                lastModified: {},
                etag: {},
                ajaxSettings: {
                    url: Et,
                    type: "GET",
                    isLocal: ft.test(wt[1]),
                    global: !0,
                    processData: !0,
                    async: !0,
                    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                    accepts: {
                        "*": xt,
                        text: "text/plain",
                        html: "text/html",
                        xml: "application/xml, text/xml",
                        json: "application/json, text/javascript"
                    },
                    contents: {xml: /xml/, html: /html/, json: /json/},
                    responseFields: {xml: "responseXML", text: "responseText", json: "responseJSON"},
                    converters: {"* text": String, "text html": !0, "text json": J.parseJSON, "text xml": J.parseXML},
                    flatOptions: {url: !0, context: !0}
                },
                ajaxSetup: function (e, t) {
                    return t ? O(O(e, J.ajaxSettings), t) : O(J.ajaxSettings, e)
                },
                ajaxPrefilter: B(yt),
                ajaxTransport: B(bt),
                ajax: function (e, t) {
                    function n(e, t, n, a) {
                        var c, u, v, y, x, w = t;
                        2 !== b && (b = 2, s && clearTimeout(s), i = void 0, o = a || "", E.readyState = e > 0 ? 4 : 0, c = e >= 200 && 300 > e || 304 === e, n && (y = I(p, E, n)), y = F(p, y, E, c), c ? (p.ifModified && (x = E.getResponseHeader("Last-Modified"), x && (J.lastModified[r] = x), x = E.getResponseHeader("etag"), x && (J.etag[r] = x)), 204 === e || "HEAD" === p.type ? w = "nocontent" : 304 === e ? w = "notmodified" : (w = y.state, u = y.data, v = y.error, c = !v)) : (v = w, (e || !w) && (w = "error", 0 > e && (e = 0))), E.status = e, E.statusText = (t || w) + "", c ? f.resolveWith(d, [u, w, E]) : f.rejectWith(d, [E, w, v]), E.statusCode(g), g = void 0, l && h.trigger(c ? "ajaxSuccess" : "ajaxError", [E, p, c ? u : v]), m.fireWith(d, [E, w]), l && (h.trigger("ajaxComplete", [E, p]), --J.active || J.event.trigger("ajaxStop")))
                    }

                    "object" == typeof e && (t = e, e = void 0), t = t || {};
                    var i, r, o, a, s, c, l, u, p = J.ajaxSetup({}, t), d = p.context || p, h = p.context && (d.nodeType || d.jquery) ? J(d) : J.event, f = J.Deferred(), m = J.Callbacks("once memory"), g = p.statusCode || {}, v = {}, y = {}, b = 0, x = "canceled", E = {
                        readyState: 0,
                        getResponseHeader: function (e) {
                            var t;
                            if (2 === b) {
                                if (!a)for (a = {}; t = ht.exec(o);)a[t[1].toLowerCase()] = t[2];
                                t = a[e.toLowerCase()]
                            }
                            return null == t ? null : t
                        },
                        getAllResponseHeaders: function () {
                            return 2 === b ? o : null
                        },
                        setRequestHeader: function (e, t) {
                            var n = e.toLowerCase();
                            return b || (e = y[n] = y[n] || e, v[e] = t), this
                        },
                        overrideMimeType: function (e) {
                            return b || (p.mimeType = e), this
                        },
                        statusCode: function (e) {
                            var t;
                            if (e)if (2 > b)for (t in e)g[t] = [g[t], e[t]]; else E.always(e[E.status]);
                            return this
                        },
                        abort: function (e) {
                            var t = e || x;
                            return i && i.abort(t), n(0, t), this
                        }
                    };
                    if (f.promise(E).complete = m.add, E.success = E.done, E.error = E.fail, p.url = ((e || p.url || Et) + "").replace(pt, "").replace(gt, wt[1] + "//"), p.type = t.method || t.type || p.method || p.type, p.dataTypes = J.trim(p.dataType || "*").toLowerCase().match(he) || [""], null == p.crossDomain && (c = vt.exec(p.url.toLowerCase()), p.crossDomain = !(!c || c[1] === wt[1] && c[2] === wt[2] && (c[3] || ("http:" === c[1] ? "80" : "443")) === (wt[3] || ("http:" === wt[1] ? "80" : "443")))), p.data && p.processData && "string" != typeof p.data && (p.data = J.param(p.data, p.traditional)), L(yt, p, t, E), 2 === b)return E;
                    l = J.event && p.global, l && 0 === J.active++ && J.event.trigger("ajaxStart"), p.type = p.type.toUpperCase(), p.hasContent = !mt.test(p.type), r = p.url, p.hasContent || (p.data && (r = p.url += (ut.test(r) ? "&" : "?") + p.data, delete p.data), p.cache === !1 && (p.url = dt.test(r) ? r.replace(dt, "$1_=" + lt++) : r + (ut.test(r) ? "&" : "?") + "_=" + lt++)), p.ifModified && (J.lastModified[r] && E.setRequestHeader("If-Modified-Since", J.lastModified[r]), J.etag[r] && E.setRequestHeader("If-None-Match", J.etag[r])), (p.data && p.hasContent && p.contentType !== !1 || t.contentType) && E.setRequestHeader("Content-Type", p.contentType), E.setRequestHeader("Accept", p.dataTypes[0] && p.accepts[p.dataTypes[0]] ? p.accepts[p.dataTypes[0]] + ("*" !== p.dataTypes[0] ? ", " + xt + "; q=0.01" : "") : p.accepts["*"]);
                    for (u in p.headers)E.setRequestHeader(u, p.headers[u]);
                    if (p.beforeSend && (p.beforeSend.call(d, E, p) === !1 || 2 === b))return E.abort();
                    x = "abort";
                    for (u in{success: 1, error: 1, complete: 1})E[u](p[u]);
                    if (i = L(bt, p, t, E)) {
                        E.readyState = 1, l && h.trigger("ajaxSend", [E, p]), p.async && p.timeout > 0 && (s = setTimeout(function () {
                            E.abort("timeout")
                        }, p.timeout));
                        try {
                            b = 1, i.send(v, n)
                        } catch (w) {
                            if (!(2 > b))throw w;
                            n(-1, w)
                        }
                    } else n(-1, "No Transport");
                    return E
                },
                getJSON: function (e, t, n) {
                    return J.get(e, t, n, "json")
                },
                getScript: function (e, t) {
                    return J.get(e, void 0, t, "script")
                }
            }), J.each(["get", "post"], function (e, t) {
                J[t] = function (e, n, i, r) {
                    return J.isFunction(n) && (r = r || i, i = n, n = void 0), J.ajax({
                        url: e,
                        type: t,
                        dataType: r,
                        data: n,
                        success: i
                    })
                }
            }), J._evalUrl = function (e) {
                return J.ajax({url: e, type: "GET", dataType: "script", async: !1, global: !1, "throws": !0})
            }, J.fn.extend({
                wrapAll: function (e) {
                    var t;
                    return J.isFunction(e) ? this.each(function (t) {
                        J(this).wrapAll(e.call(this, t))
                    }) : (this[0] && (t = J(e, this[0].ownerDocument).eq(0).clone(!0), this[0].parentNode && t.insertBefore(this[0]), t.map(function () {
                        for (var e = this; e.firstElementChild;)e = e.firstElementChild;
                        return e
                    }).append(this)), this)
                }, wrapInner: function (e) {
                    return J.isFunction(e) ? this.each(function (t) {
                        J(this).wrapInner(e.call(this, t))
                    }) : this.each(function () {
                        var t = J(this), n = t.contents();
                        n.length ? n.wrapAll(e) : t.append(e)
                    })
                }, wrap: function (e) {
                    var t = J.isFunction(e);
                    return this.each(function (n) {
                        J(this).wrapAll(t ? e.call(this, n) : e)
                    })
                }, unwrap: function () {
                    return this.parent().each(function () {
                        J.nodeName(this, "body") || J(this).replaceWith(this.childNodes)
                    }).end()
                }
            }), J.expr.filters.hidden = function (e) {
                return e.offsetWidth <= 0 && e.offsetHeight <= 0
            }, J.expr.filters.visible = function (e) {
                return !J.expr.filters.hidden(e)
            };
            var _t = /%20/g, St = /\[\]$/, Ct = /\r?\n/g, At = /^(?:submit|button|image|reset|file)$/i, Tt = /^(?:input|select|textarea|keygen)/i;
            J.param = function (e, t) {
                var n, i = [], r = function (e, t) {
                    t = J.isFunction(t) ? t() : null == t ? "" : t, i[i.length] = encodeURIComponent(e) + "=" + encodeURIComponent(t)
                };
                if (void 0 === t && (t = J.ajaxSettings && J.ajaxSettings.traditional), J.isArray(e) || e.jquery && !J.isPlainObject(e))J.each(e, function () {
                    r(this.name, this.value)
                }); else for (n in e)U(n, e[n], t, r);
                return i.join("&").replace(_t, "+")
            }, J.fn.extend({
                serialize: function () {
                    return J.param(this.serializeArray())
                }, serializeArray: function () {
                    return this.map(function () {
                        var e = J.prop(this, "elements");
                        return e ? J.makeArray(e) : this
                    }).filter(function () {
                        var e = this.type;
                        return this.name && !J(this).is(":disabled") && Tt.test(this.nodeName) && !At.test(e) && (this.checked || !Se.test(e))
                    }).map(function (e, t) {
                        var n = J(this).val();
                        return null == n ? null : J.isArray(n) ? J.map(n, function (e) {
                            return {name: t.name, value: e.replace(Ct, "\r\n")}
                        }) : {name: t.name, value: n.replace(Ct, "\r\n")}
                    }).get()
                }
            }), J.ajaxSettings.xhr = function () {
                try {
                    return new XMLHttpRequest
                } catch (e) {
                }
            };
            var jt = 0, Nt = {}, kt = {0: 200, 1223: 204}, Rt = J.ajaxSettings.xhr();
            e.attachEvent && e.attachEvent("onunload", function () {
                for (var e in Nt)Nt[e]()
            }), K.cors = !!Rt && "withCredentials"in Rt, K.ajax = Rt = !!Rt, J.ajaxTransport(function (e) {
                var t;
                return K.cors || Rt && !e.crossDomain ? {
                    send: function (n, i) {
                        var r, o = e.xhr(), a = ++jt;
                        if (o.open(e.type, e.url, e.async, e.username, e.password), e.xhrFields)for (r in e.xhrFields)o[r] = e.xhrFields[r];
                        e.mimeType && o.overrideMimeType && o.overrideMimeType(e.mimeType), e.crossDomain || n["X-Requested-With"] || (n["X-Requested-With"] = "XMLHttpRequest");
                        for (r in n)o.setRequestHeader(r, n[r]);
                        t = function (e) {
                            return function () {
                                t && (delete Nt[a], t = o.onload = o.onerror = null, "abort" === e ? o.abort() : "error" === e ? i(o.status, o.statusText) : i(kt[o.status] || o.status, o.statusText, "string" == typeof o.responseText ? {text: o.responseText} : void 0, o.getAllResponseHeaders()))
                            }
                        }, o.onload = t(), o.onerror = t("error"), t = Nt[a] = t("abort");
                        try {
                            o.send(e.hasContent && e.data || null)
                        } catch (s) {
                            if (t)throw s
                        }
                    }, abort: function () {
                        t && t()
                    }
                } : void 0
            }), J.ajaxSetup({
                accepts: {script: "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript"},
                contents: {script: /(?:java|ecma)script/},
                converters: {
                    "text script": function (e) {
                        return J.globalEval(e), e
                    }
                }
            }), J.ajaxPrefilter("script", function (e) {
                void 0 === e.cache && (e.cache = !1), e.crossDomain && (e.type = "GET")
            }), J.ajaxTransport("script", function (e) {
                if (e.crossDomain) {
                    var t, n;
                    return {
                        send: function (i, r) {
                            t = J("<script>").prop({
                                async: !0,
                                charset: e.scriptCharset,
                                src: e.url
                            }).on("load error", n = function (e) {
                                t.remove(), n = null, e && r("error" === e.type ? 404 : 200, e.type)
                            }), Z.head.appendChild(t[0])
                        }, abort: function () {
                            n && n()
                        }
                    }
                }
            });
            var Mt = [], Dt = /(=)\?(?=&|$)|\?\?/;
            J.ajaxSetup({
                jsonp: "callback", jsonpCallback: function () {
                    var e = Mt.pop() || J.expando + "_" + lt++;
                    return this[e] = !0, e
                }
            }), J.ajaxPrefilter("json jsonp", function (t, n, i) {
                var r, o, a, s = t.jsonp !== !1 && (Dt.test(t.url) ? "url" : "string" == typeof t.data && !(t.contentType || "").indexOf("application/x-www-form-urlencoded") && Dt.test(t.data) && "data");
                return s || "jsonp" === t.dataTypes[0] ? (r = t.jsonpCallback = J.isFunction(t.jsonpCallback) ? t.jsonpCallback() : t.jsonpCallback, s ? t[s] = t[s].replace(Dt, "$1" + r) : t.jsonp !== !1 && (t.url += (ut.test(t.url) ? "&" : "?") + t.jsonp + "=" + r), t.converters["script json"] = function () {
                    return a || J.error(r + " was not called"), a[0]
                }, t.dataTypes[0] = "json", o = e[r], e[r] = function () {
                    a = arguments
                }, i.always(function () {
                    e[r] = o, t[r] && (t.jsonpCallback = n.jsonpCallback, Mt.push(r)), a && J.isFunction(o) && o(a[0]), a = o = void 0
                }), "script") : void 0
            }), J.parseHTML = function (e, t, n) {
                if (!e || "string" != typeof e)return null;
                "boolean" == typeof t && (n = t, t = !1), t = t || Z;
                var i = ae.exec(e), r = !n && [];
                return i ? [t.createElement(i[1])] : (i = J.buildFragment([e], t, r), r && r.length && J(r).remove(), J.merge([], i.childNodes))
            };
            var Pt = J.fn.load;
            J.fn.load = function (e, t, n) {
                if ("string" != typeof e && Pt)return Pt.apply(this, arguments);
                var i, r, o, a = this, s = e.indexOf(" ");
                return s >= 0 && (i = J.trim(e.slice(s)), e = e.slice(0, s)), J.isFunction(t) ? (n = t, t = void 0) : t && "object" == typeof t && (r = "POST"), a.length > 0 && J.ajax({
                    url: e,
                    type: r,
                    dataType: "html",
                    data: t
                }).done(function (e) {
                    o = arguments, a.html(i ? J("<div>").append(J.parseHTML(e)).find(i) : e)
                }).complete(n && function (e, t) {
                    a.each(n, o || [e.responseText, t, e])
                }), this
            }, J.each(["ajaxStart", "ajaxStop", "ajaxComplete", "ajaxError", "ajaxSuccess", "ajaxSend"], function (e, t) {
                J.fn[t] = function (e) {
                    return this.on(t, e)
                }
            }), J.expr.filters.animated = function (e) {
                return J.grep(J.timers, function (t) {
                    return e === t.elem
                }).length
            };
            var Bt = e.document.documentElement;
            J.offset = {
                setOffset: function (e, t, n) {
                    var i, r, o, a, s, c, l, u = J.css(e, "position"), p = J(e), d = {};
                    "static" === u && (e.style.position = "relative"), s = p.offset(), o = J.css(e, "top"), c = J.css(e, "left"), l = ("absolute" === u || "fixed" === u) && (o + c).indexOf("auto") > -1, l ? (i = p.position(), a = i.top, r = i.left) : (a = parseFloat(o) || 0, r = parseFloat(c) || 0), J.isFunction(t) && (t = t.call(e, n, s)), null != t.top && (d.top = t.top - s.top + a), null != t.left && (d.left = t.left - s.left + r), "using"in t ? t.using.call(e, d) : p.css(d)
                }
            }, J.fn.extend({
                offset: function (e) {
                    if (arguments.length)return void 0 === e ? this : this.each(function (t) {
                        J.offset.setOffset(this, e, t)
                    });
                    var t, n, i = this[0], r = {top: 0, left: 0}, o = i && i.ownerDocument;
                    if (o)return t = o.documentElement, J.contains(t, i) ? (typeof i.getBoundingClientRect !== Ce && (r = i.getBoundingClientRect()), n = z(o), {
                        top: r.top + n.pageYOffset - t.clientTop,
                        left: r.left + n.pageXOffset - t.clientLeft
                    }) : r
                }, position: function () {
                    if (this[0]) {
                        var e, t, n = this[0], i = {top: 0, left: 0};
                        return "fixed" === J.css(n, "position") ? t = n.getBoundingClientRect() : (e = this.offsetParent(), t = this.offset(), J.nodeName(e[0], "html") || (i = e.offset()), i.top += J.css(e[0], "borderTopWidth", !0), i.left += J.css(e[0], "borderLeftWidth", !0)), {
                            top: t.top - i.top - J.css(n, "marginTop", !0),
                            left: t.left - i.left - J.css(n, "marginLeft", !0)
                        }
                    }
                }, offsetParent: function () {
                    return this.map(function () {
                        for (var e = this.offsetParent || Bt; e && !J.nodeName(e, "html") && "static" === J.css(e, "position");)e = e.offsetParent;
                        return e || Bt
                    })
                }
            }), J.each({scrollLeft: "pageXOffset", scrollTop: "pageYOffset"}, function (t, n) {
                var i = "pageYOffset" === n;
                J.fn[t] = function (r) {
                    return ge(this, function (t, r, o) {
                        var a = z(t);
                        return void 0 === o ? a ? a[n] : t[r] : void(a ? a.scrollTo(i ? e.pageXOffset : o, i ? o : e.pageYOffset) : t[r] = o)
                    }, t, r, arguments.length, null)
                }
            }), J.each(["top", "left"], function (e, t) {
                J.cssHooks[t] = w(K.pixelPosition, function (e, n) {
                    return n ? (n = E(e, t), $e.test(n) ? J(e).position()[t] + "px" : n) : void 0
                })
            }), J.each({Height: "height", Width: "width"}, function (e, t) {
                J.each({padding: "inner" + e, content: t, "": "outer" + e}, function (n, i) {
                    J.fn[i] = function (i, r) {
                        var o = arguments.length && (n || "boolean" != typeof i), a = n || (i === !0 || r === !0 ? "margin" : "border");
                        return ge(this, function (t, n, i) {
                            var r;
                            return J.isWindow(t) ? t.document.documentElement["client" + e] : 9 === t.nodeType ? (r = t.documentElement, Math.max(t.body["scroll" + e], r["scroll" + e], t.body["offset" + e], r["offset" + e], r["client" + e])) : void 0 === i ? J.css(t, n, a) : J.style(t, n, i, a)
                        }, t, o ? i : void 0, o, null)
                    }
                })
            }), J.fn.size = function () {
                return this.length
            }, J.fn.andSelf = J.fn.addBack, "function" == typeof define && define.amd && define("jquery", [], function () {
                return J
            });
            var Lt = e.jQuery, Ot = e.$;
            return J.noConflict = function (t) {
                return e.$ === J && (e.$ = Ot), t && e.jQuery === J && (e.jQuery = Lt), J
            }, typeof t === Ce && (e.jQuery = e.$ = J), J
        })
    }, {}]
}, {}, [1]);