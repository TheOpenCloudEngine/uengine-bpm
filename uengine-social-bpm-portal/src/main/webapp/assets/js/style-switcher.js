/* Okler Themes - Style Switcher - 2.9.0 - 2014-03-19 */
var styleSwitcher = {
    initialized: !1,
    options: {
        color: "#CCC",
        gradient: "true"
    },
    initialize: function () {
        var a = this;
        this.initialized || (jQuery.styleSwitcherCachedScript = function (a, b) {
            return b = $.extend(b || {}, {
                dataType: "script",
                cache: !0,
                url: a
            }), jQuery.ajax(b)
        }, $("head").append($('<link rel="stylesheet">').attr("href", "master/style-switcher/style-switcher.css")), $("head").append($('<link rel="stylesheet/less">').attr("href", "master/less/skin.less")), $("head").append($('<link rel="stylesheet">').attr("href", "master/style-switcher/colorpicker/css/colorpicker.css")), $.styleSwitcherCachedScript("master/style-switcher/colorpicker/js/colorpicker.js").done(function () {
            less = {
                env: "development"
            }, $.styleSwitcherCachedScript("master/less/less.js").done(function () {
                a.build(), a.events(), null != $.cookie("colorGradient") && (a.options.gradient = $.cookie("colorGradient")), null != $.cookie("skin") ? a.setColor($.cookie("skin")) : a.container.find("ul[data-type=colors] li:first a").click(), null != $.cookie("layout") && a.setLayoutStyle($.cookie("layout")), null != $.cookie("pattern") && a.setPattern($.cookie("pattern")), null == $.cookie("initialized") && (a.container.find("h4 a").click(), $.cookie("initialized", !0)), a.initialized = !0
            })
        }), $.styleSwitcherCachedScript("master/style-switcher/cssbeautify/cssbeautify.js").done(function () {}))
    },
    build: function () {
        var a = this,
            b = $("<div />").attr("id", "styleSwitcher").addClass("style-switcher visible-lg").append($("<h4 />").html("Style Switcher").append($("<a />").attr("href", "#").append($("<i />").addClass("icon icon-cogs"))), $("<div />").addClass("style-switcher-mode").append($("<div />").addClass("options-links mode").append($("<a />").attr("href", "#").attr("data-mode", "basic").addClass("active").html("Basic"), $("<a />").attr("href", "#").attr("data-mode", "advanced").html("Advanced"))), $("<div />").addClass("style-switcher-wrap").append($("<h5 />").html("Colors"), $("<ul />").addClass("options colors").attr("data-type", "colors"), $("<h5 />").html("Layout Style"), $("<div />").addClass("options-links layout").append($("<a />").attr("href", "#").attr("data-layout-type", "wide").addClass("active").html("Wide"), $("<a />").attr("href", "#").attr("data-layout-type", "boxed").html("Boxed")), $("<h5 />").html("Website Type"), $("<div />").addClass("options-links website-type").append($("<a />").attr("href", "index.html").attr("data-website-type", "normal").html("Normal"), $("<a />").attr("href", "index-one-page.html").attr("data-website-type", "one-page").html("One Page")), $("<div />").hide().addClass("patterns").append($("<h5 />").html("Background Patterns"), $("<ul />").addClass("options").attr("data-type", "patterns")), $("<hr />"), $("<div />").addClass("options-links").append($("<a />").addClass("reset").attr("href", "#").html("Reset"), $("<a />").addClass("get-css").attr("href", "#getCSSModal").html("Get Skin CSS"))));
        $("body").append(b);
        var c = '<div class="modal fade" id="getCSSModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title" id="cssModalLabel">Skin CSS</h4> </div> <div class="modal-body"> <div class="alert alert-info fade in" id="addBoxedClassInfo">Please add the <strong>&quot;boxed&quot;</strong> class to the &lt;body&gt; element.</div><textarea id="getCSSTextarea" class="get-css" readonly></textarea></div> </div> </div> </div> </div>';
        $("body").append(c), this.container = $("#styleSwitcher"), this.container.find("div.options-links.mode a").click(function (a) {
            a.preventDefault();
            var b = $(this).parents(".mode");
            b.find("a.active").removeClass("active"), $(this).addClass("active"), "advanced" == $(this).attr("data-mode") ? $("#styleSwitcher").addClass("advanced").removeClass("basic") : $("#styleSwitcher").addClass("basic").removeClass("advanced")
        });
        var d = [{
                Hex: "#0088CC",
                colorName: "Blue"
            }, {
                Hex: "#2BAAB1",
                colorName: "Green"
            }, {
                Hex: "#4A5B7D",
                colorName: "Navy"
            }, {
                Hex: "#E36159",
                colorName: "Red"
            }, {
                Hex: "#B8A279",
                colorName: "Beige"
            }, {
                Hex: "#c71c77",
                colorName: "Pink"
            }, {
                Hex: "#734BA9",
                colorName: "Purple"
            }, {
                Hex: "#2BAAB1",
                colorName: "Cyan"
            }],
            e = this.container.find("ul[data-type=colors]");
        if ($.each(d, function (a) {
            var b = $("<li />").append($("<a />").css("background-color", d[a].Hex).attr({
                "data-color-hex": d[a].Hex,
                "data-color-name": d[a].colorName,
                href: "#",
                title: d[a].colorName
            }));
            e.append(b)
        }), null != $.cookie("skin")) var f = $.cookie("skin");
        else var f = d[0].Hex;
        var g = $("<div />").addClass("color-gradient").append($("<input />").attr("id", "colorGradient").attr("checked", a.options.gradient).attr("type", "checkbox"), $("<label />").attr("for", "colorGradient").html("Gradient")),
            h = $("<div />").attr("id", "colorPickerHolder").attr("data-color", f).attr("data-color-format", "hex").addClass("color-picker");
        e.before(g, h), e.find("a").click(function (b) {
            b.preventDefault(), a.setColor($(this).attr("data-color-hex")), $("#colorPickerHolder").ColorPickerSetColor($(this).attr("data-color-hex"))
        }), $("#colorPickerHolder").ColorPicker({
            color: f,
            flat: !0,
            livePreview: !1,
            onChange: function (b, c) {
                a.setColor("#" + c)
            }
        }), $("#colorPickerHolder .colorpicker_color, #colorPickerHolder .colorpicker_hue").on("mousedown", function (b) {
            b.preventDefault(), a.isChanging = !0
        }).on("mouseup", function (b) {
            b.preventDefault(), a.isChanging = !1, setTimeout(function () {
                a.setColor("#" + $("#colorPickerHolder .colorpicker_hex input").val())
            }, 100)
        }), "false" == $.cookie("colorGradient") && $("#colorGradient").removeAttr("checked"), $("#colorGradient").on("change", function () {
            var b = $(this).is(":checked").toString();
            a.options.gradient = b, a.setColor(a.options.color), $.cookie("colorGradient", b)
        }), this.container.find("div.options-links.layout a").click(function (b) {
            b.preventDefault(), a.setLayoutStyle($(this).attr("data-layout-type"), !0)
        }), this.container.find("div.options-links.website-type a").click(function (a) {
            a.preventDefault(), $.cookie("showSwitcher", !0), self.location = $(this).attr("href")
        }), $("body").hasClass("one-page") ? (this.container.find("div.options-links.website-type a:last").addClass("active"), this.container.find("div.options-links.layout").prev().remove(), this.container.find("div.options-links.layout").remove()) : this.container.find("div.options-links.website-type a:first").addClass("active");
        var i = ["gray_jean", "linedpaper", "az_subtle", "blizzard", "denim", "fancy_deboss", "honey_im_subtle", "linen", "pw_maze_white", "skin_side_up", "stitched_wool", "straws", "subtle_grunge", "textured_stripes", "wild_oliva", "worn_dots", "bright_squares", "random_grey_variations"],
            j = this.container.find("ul[data-type=patterns]");
        $.each(i, function (a, b) {
            var c = $("<li />").append($("<a />").addClass("pattern").css("background-image", "url(img/patterns/" + b + ".png)").attr({
                "data-pattern": b,
                href: "#",
                title: b.charAt(0).toUpperCase() + b.slice(1)
            }));
            j.append(c)
        }), j.find("a").click(function (b) {
            b.preventDefault(), a.setPattern($(this).attr("data-pattern"))
        }), a.container.find("a.reset").click(function (b) {
            b.preventDefault(), a.reset()
        }), a.container.find("a.get-css").click(function (b) {
            b.preventDefault(), a.getCss()
        })
    },
    events: function () {
        var a = this;
        a.container.find("h4 a").click(function (b) {
            b.preventDefault(), a.container.hasClass("active") ? a.container.animate({
                left: "-" + a.container.width() + "px"
            }, 300).removeClass("active") : a.container.animate({
                left: "0"
            }, 300).addClass("active")
        }), (null != $.cookie("showSwitcher") || $("body").hasClass("one-page")) && (a.container.find("h4 a").click(), $.removeCookie("showSwitcher"))
    },
    setColor: function (a) {
        var b = this;
        return this.isChanging ? !1 : (b.options.color = a, less.modifyVars({
            gradient: b.options.gradient,
            skinColor: a
        }), $.cookie("skin", a), void(a == this.container.find("ul[data-type=colors] li:first a").attr("data-color-hex") ? $("h1.logo img").attr("src", "img/logo-default.png") : $("h1.logo img").attr("src", "img/logo.png")))
    },
    setLayoutStyle: function (a, b) {
        if ($("body").hasClass("one-page")) return !1;
        if ($.cookie("layout", a), b) return $.cookie("showSwitcher", !0), window.location.reload(), !1;
        var c = this.container.find("div.options-links.layout"),
            d = this.container.find("div.patterns");
        c.find("a.active").removeClass("active"), c.find("a[data-layout-type=" + a + "]").addClass("active"), "wide" == a ? (d.hide(), $("body").removeClass("boxed"), $("link[href*='bootstrap-responsive']").attr("href", "css/bootstrap-responsive.css"), $.removeCookie("pattern")) : (d.show(), $("body").addClass("boxed"), $("link[href*='bootstrap-responsive']").attr("href", "css/bootstrap-responsive-boxed.css"), null == $.cookie("pattern") && this.container.find("ul[data-type=patterns] li:first a").click())
    },
    setPattern: function (a) {
        var b = $("body").hasClass("boxed");
        b && $("body").css("background-image", "url(img/patterns/" + a + ".png)"), $.cookie("pattern", a)
    },
    reset: function () {
        $.removeCookie("skin"), $.removeCookie("layout"), $.removeCookie("pattern"), $.removeCookie("colorGradient"), $.cookie("showSwitcher", !0), window.location.reload()
    },
    getCss: function () {
        raw = "";
        var a = $("body").hasClass("boxed");
        a ? (raw = 'body { background-image: url("img/patterns/' + $.cookie("pattern") + '.png"); }', $("#addBoxedClassInfo").show()) : $("#addBoxedClassInfo").hide(), $("#getCSSTextarea").text(""), $("#getCSSTextarea").text($('style[id^="less:"]').text()), $("#getCSSModal").modal("show"), options = {
            indent: " ",
            autosemicolon: !0
        }, raw += $("#getCSSTextarea").text(), $("#getCSSTextarea").text(cssbeautify(raw, options))
    }
};
styleSwitcher.initialize();