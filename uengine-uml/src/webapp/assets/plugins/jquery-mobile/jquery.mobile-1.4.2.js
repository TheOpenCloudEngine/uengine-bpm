/* jQuery Mobile v1.4.2 */

if($('body').data('page') != 'image_croping' && $('body').data('page') != 'widgets' && $('body').data('page') != 'charts'){

  (function ( root, doc, factory ) {
    if ( typeof define === "function" && define.amd ) {
      // AMD. Register as an anonymous module.
      define( [ "jquery" ], function ( $ ) {
        factory( $, root, doc );
        return $.mobile;
      });
    } else {
      // Browser globals
      factory( root.jQuery, root, doc );
    }
  }( this, document, function ( jQuery, window, document, undefined ) {


  (function($,window,undefined){
    '$:nomunge'; // Used by YUI compressor.
    
    // Reused string.
    var str_hashchange = 'hashchange',
      
      // Method / object references.
      doc = document,
      fake_onhashchange,
      special = $.event.special,
      
      // Does the browser support window.onhashchange? Note that IE8 running in
      // IE7 compatibility mode reports true for 'onhashchange' in window, even
      // though the event isn't supported, so also test document.documentMode.
      doc_mode = doc.documentMode,
      supports_onhashchange = 'on' + str_hashchange in window && ( doc_mode === undefined || doc_mode > 7 );
    
    // Get location.hash (or what you'd expect location.hash to be) sans any
    // leading #. Thanks for making this necessary, Firefox!
    function get_fragment( url ) {
      url = url || location.href;
      return '#' + url.replace( /^[^#]*#?(.*)$/, '$1' );
    };
    
   
    $.fn[ str_hashchange ] = function( fn ) {
      return fn ? this.bind( str_hashchange, fn ) : this.trigger( str_hashchange );
    };
   
    
    $.fn[ str_hashchange ].delay = 50;
    
    special[ str_hashchange ] = $.extend( special[ str_hashchange ], {
      
      // Called only when the first 'hashchange' event is bound to window.
      setup: function() {
        // If window.onhashchange is supported natively, there's nothing to do..
        if ( supports_onhashchange ) { return false; }
        
        // Otherwise, we need to create our own. And we don't want to call this
        // until the user binds to the event, just in case they never do, since it
        // will create a polling loop and possibly even a hidden Iframe.
        $( fake_onhashchange.start );
      },
      
      // Called only when the last 'hashchange' event is unbound from window.
      teardown: function() {
        // If window.onhashchange is supported natively, there's nothing to do..
        if ( supports_onhashchange ) { return false; }
        
        // Otherwise, we need to stop ours (if possible).
        $( fake_onhashchange.stop );
      }
      
    });
    
    // fake_onhashchange does all the work of triggering the window.onhashchange
    // event for browsers that don't natively support it, including creating a
    // polling loop to watch for hash changes and in IE 6/7 creating a hidden
    // Iframe to enable back and forward.
    fake_onhashchange = (function(){
      var self = {},
        timeout_id,
        
        // Remember the initial hash so it doesn't get triggered immediately.
        last_hash = get_fragment(),
        
        fn_retval = function(val){ return val; },
        history_set = fn_retval,
        history_get = fn_retval;
      
      // Start the polling loop.
      self.start = function() {
        timeout_id || poll();
      };
      
      // Stop the polling loop.
      self.stop = function() {
        timeout_id && clearTimeout( timeout_id );
        timeout_id = undefined;
      };
      
      // This polling loop checks every $.fn.hashchange.delay milliseconds to see
      // if location.hash has changed, and triggers the 'hashchange' event on
      // window when necessary.
      function poll() {
        var hash = get_fragment(),
          history_hash = history_get( last_hash );
        
        if ( hash !== last_hash ) {
          history_set( last_hash = hash, history_hash );
          
          $(window).trigger( str_hashchange );
          
        } else if ( history_hash !== last_hash ) {
          location.href = location.href.replace( /#.*/, '' ) + history_hash;
        }
        
        timeout_id = setTimeout( poll, $.fn[ str_hashchange ].delay );
      };
      
      // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
      // vvvvvvvvvvvvvvvvvvv REMOVE IF NOT SUPPORTING IE6/7/8 vvvvvvvvvvvvvvvvvvv
      // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
      window.attachEvent && !window.addEventListener && !supports_onhashchange && (function(){
        // Not only do IE6/7 need the "magical" Iframe treatment, but so does IE8
        // when running in "IE7 compatibility" mode.
        
        var iframe,
          iframe_src;
        
        // When the event is bound and polling starts in IE 6/7, create a hidden
        // Iframe for history handling.
        self.start = function(){
          if ( !iframe ) {
            iframe_src = $.fn[ str_hashchange ].src;
            iframe_src = iframe_src && iframe_src + get_fragment();
            
            // Create hidden Iframe. Attempt to make Iframe as hidden as possible
            // by using techniques from http://www.paciellogroup.com/blog/?p=604.
            iframe = $('<iframe tabindex="-1" title="empty"/>').hide()
              
              // When Iframe has completely loaded, initialize the history and
              // start polling.
              .one( 'load', function(){
                iframe_src || history_set( get_fragment() );
                poll();
              })
              
              // Load Iframe src if specified, otherwise nothing.
              .attr( 'src', iframe_src || 'javascript:0' )
              
              // Append Iframe after the end of the body to prevent unnecessary
              // initial page scrolling (yes, this works).
              .insertAfter( 'body' )[0].contentWindow;
            
            // Whenever `document.title` changes, update the Iframe's title to
            // prettify the back/next history menu entries. Since IE sometimes
            // errors with "Unspecified error" the very first time this is set
            // (yes, very useful) wrap this with a try/catch block.
            doc.onpropertychange = function(){
              try {
                if ( event.propertyName === 'title' ) {
                  iframe.document.title = doc.title;
                }
              } catch(e) {}
            };
            
          }
        };
        
        // Override the "stop" method since an IE6/7 Iframe was created. Even
        // if there are no longer any bound event handlers, the polling loop
        // is still necessary for back/next to work at all!
        self.stop = fn_retval;
        
        // Get history by looking at the hidden Iframe's location.hash.
        history_get = function() {
          return get_fragment( iframe.location.href );
        };
        
        // Set a new history item by opening and then closing the Iframe
        // document, *then* setting its location.hash. If document.domain has
        // been set, update that as well.
        history_set = function( hash, history_hash ) {
          var iframe_doc = iframe.document,
            domain = $.fn[ str_hashchange ].domain;
          
          if ( hash !== history_hash ) {
            // Update Iframe with any initial `document.title` that might be set.
            iframe_doc.title = doc.title;
            
            // Opening the Iframe's document after it has been closed is what
            // actually adds a history entry.
            iframe_doc.open();
            
            // Set document.domain for the Iframe document as well, if necessary.
            domain && iframe_doc.write( '<script>document.domain="' + domain + '"</script>' );
            
            iframe_doc.close();
            
            // Update the Iframe's hash, for great justice.
            iframe.location.hash = hash;
          }
        };
        
      })();
      // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
      // ^^^^^^^^^^^^^^^^^^^ REMOVE IF NOT SUPPORTING IE6/7/8 ^^^^^^^^^^^^^^^^^^^
      // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
      
      return self;
    })();
    
  })(jQuery,this);

  (function( $ ) {
    $.mobile = {};
  }( jQuery ));

  (function( $, window, undefined ) {
    $.extend( $.mobile, {

      // Version of the jQuery Mobile Framework
      version: "1.4.2",

      // Deprecated and no longer used in 1.4 remove in 1.5
      // Define the url parameter used for referencing widget-generated sub-pages.
      // Translates to example.html&ui-page=subpageIdentifier
      // hash segment before &ui-page= is used to make Ajax request
      subPageUrlKey: "",

      hideUrlBar: true,

      // Keepnative Selector
      keepNative: ":jqmData(role='none'), :jqmData(role='nojs')",

      // Deprecated in 1.4 remove in 1.5
      // Class assigned to page currently in view, and during transitions
      activePageClass: "page-active",

      // Deprecated in 1.4 remove in 1.5
      // Class used for "active" button state, from CSS framework
      activeBtnClass: "ui-btn-active",

      // Deprecated in 1.4 remove in 1.5
      // Class used for "focus" form element state, from CSS framework
      focusClass: "ui-focus",

      // Automatically handle clicks and form submissions through Ajax, when same-domain
      ajaxEnabled: false,

      // Automatically load and show pages based on location.hash
      hashListeningEnabled: true,

      // disable to prevent jquery from bothering with links
      linkBindingEnabled: false,

      // Set default page transition - 'none' for no transitions
      defaultPageTransition: "fade",

      // Set maximum window width for transitions to apply - 'false' for no limit
      maxTransitionWidth: false,

      // Minimum scroll distance that will be remembered when returning to a page
      // Deprecated remove in 1.5
      minScrollBack: 0,

      // Set default dialog transition - 'none' for no transitions
      defaultDialogTransition: "pop",

      // Error response message - appears when an Ajax page request fails
      pageLoadErrorMessage: "Error Loading Page",

      // For error messages, which theme does the box uses?
      pageLoadErrorMessageTheme: "a",

      // replace calls to window.history.back with phonegaps navigation helper
      // where it is provided on the window object
      phonegapNavigationEnabled: false,

      //automatically initialize the DOM when it's ready
      autoInitializePage: true,

      pushStateEnabled: true,

      // allows users to opt in to ignoring content by marking a parent element as
      // data-ignored
      ignoreContentEnabled: false,

      buttonMarkup: {
        hoverDelay: 200
      },

      // disable the alteration of the dynamic base tag or links in the case
      // that a dynamic base tag isn't supported
      dynamicBaseEnabled: true,

      // default the property to remove dependency on assignment in init module
      pageContainer: $(),

      //enable cross-domain page support
      allowCrossDomainPages: false,

      dialogHashKey: "&ui-state=dialog"
    });
  })( jQuery, this );

  (function( $, window, undefined ) {
    var nsNormalizeDict = {},
      oldFind = $.find,
      rbrace = /(?:\{[\s\S]*\}|\[[\s\S]*\])$/,
      jqmDataRE = /:jqmData\(([^)]*)\)/g;

    $.extend( $.mobile, {

      // Namespace used framework-wide for data-attrs. Default is no namespace

      ns: "",

      // Retrieve an attribute from an element and perform some massaging of the value

      getAttribute: function( element, key ) {
        var data;

        element = element.jquery ? element[0] : element;

        if ( element && element.getAttribute ) {
          data = element.getAttribute( "data-" + $.mobile.ns + key );
        }

        // Copied from core's src/data.js:dataAttr()
        // Convert from a string to a proper data type
        try {
          data = data === "true" ? true :
            data === "false" ? false :
            data === "null" ? null :
            // Only convert to a number if it doesn't change the string
            +data + "" === data ? +data :
            rbrace.test( data ) ? JSON.parse( data ) :
            data;
        } catch( err ) {}

        return data;
      },

      // Expose our cache for testing purposes.
      nsNormalizeDict: nsNormalizeDict,

      // Take a data attribute property, prepend the namespace
      // and then camel case the attribute string. Add the result
      // to our nsNormalizeDict so we don't have to do this again.
      nsNormalize: function( prop ) {
        return nsNormalizeDict[ prop ] ||
          ( nsNormalizeDict[ prop ] = $.camelCase( $.mobile.ns + prop ) );
      },

      // Find the closest javascript page element to gather settings data jsperf test
      // http://jsperf.com/single-complex-selector-vs-many-complex-selectors/edit
      // possibly naive, but it shows that the parsing overhead for *just* the page selector vs
      // the page and dialog selector is negligable. This could probably be speed up by
      // doing a similar parent node traversal to the one found in the inherited theme code above
      closestPageData: function( $target ) {
        return $target
          .closest( ":jqmData(role='page'), :jqmData(role='dialog')" )
          .data( "mobile-page" );
      }

    });

    // Mobile version of data and removeData and hasData methods
    // ensures all data is set and retrieved using jQuery Mobile's data namespace
    $.fn.jqmData = function( prop, value ) {
      var result;
      if ( typeof prop !== "undefined" ) {
        if ( prop ) {
          prop = $.mobile.nsNormalize( prop );
        }

        // undefined is permitted as an explicit input for the second param
        // in this case it returns the value and does not set it to undefined
        if ( arguments.length < 2 || value === undefined ) {
          result = this.data( prop );
        } else {
          result = this.data( prop, value );
        }
      }
      return result;
    };

    $.jqmData = function( elem, prop, value ) {
      var result;
      if ( typeof prop !== "undefined" ) {
        result = $.data( elem, prop ? $.mobile.nsNormalize( prop ) : prop, value );
      }
      return result;
    };

    $.fn.jqmRemoveData = function( prop ) {
      return this.removeData( $.mobile.nsNormalize( prop ) );
    };

    $.jqmRemoveData = function( elem, prop ) {
      return $.removeData( elem, $.mobile.nsNormalize( prop ) );
    };

    $.find = function( selector, context, ret, extra ) {
      if ( selector.indexOf( ":jqmData" ) > -1 ) {
        selector = selector.replace( jqmDataRE, "[data-" + ( $.mobile.ns || "" ) + "$1]" );
      }

      return oldFind.call( this, selector, context, ret, extra );
    };

    $.extend( $.find, oldFind );

  })( jQuery, this );

  /*!
   * jQuery UI Core c0ab71056b936627e8a7821f03c044aec6280a40
   * http://jqueryui.com
   *
   * Copyright 2013 jQuery Foundation and other contributors
   * Released under the MIT license.
   * http://jquery.org/license
   *
   * http://api.jqueryui.com/category/ui-core/
   */
  (function( $, undefined ) {

  var uuid = 0,
    runiqueId = /^ui-id-\d+$/;

  // $.ui might exist from components with no dependencies, e.g., $.ui.position
  $.ui = $.ui || {};

  $.extend( $.ui, {
    version: "c0ab71056b936627e8a7821f03c044aec6280a40",

    keyCode: {
      BACKSPACE: 8,
      COMMA: 188,
      DELETE: 46,
      DOWN: 40,
      END: 35,
      ENTER: 13,
      ESCAPE: 27,
      HOME: 36,
      LEFT: 37,
      PAGE_DOWN: 34,
      PAGE_UP: 33,
      PERIOD: 190,
      RIGHT: 39,
      SPACE: 32,
      TAB: 9,
      UP: 38
    }
  });

  // plugins
  $.fn.extend({
    focus: (function( orig ) {
      return function( delay, fn ) {
        return typeof delay === "number" ?
          this.each(function() {
            var elem = this;
            setTimeout(function() {
              $( elem ).focus();
              if ( fn ) {
                fn.call( elem );
              }
            }, delay );
          }) :
          orig.apply( this, arguments );
      };
    })( $.fn.focus ),

    scrollParent: function() {
      var scrollParent;
      if (($.ui.ie && (/(static|relative)/).test(this.css("position"))) || (/absolute/).test(this.css("position"))) {
        scrollParent = this.parents().filter(function() {
          return (/(relative|absolute|fixed)/).test($.css(this,"position")) && (/(auto|scroll)/).test($.css(this,"overflow")+$.css(this,"overflow-y")+$.css(this,"overflow-x"));
        }).eq(0);
      } else {
        scrollParent = this.parents().filter(function() {
          return (/(auto|scroll)/).test($.css(this,"overflow")+$.css(this,"overflow-y")+$.css(this,"overflow-x"));
        }).eq(0);
      }

      return ( /fixed/ ).test( this.css( "position") ) || !scrollParent.length ? $( this[ 0 ].ownerDocument || document ) : scrollParent;
    },

    uniqueId: function() {
      return this.each(function() {
        if ( !this.id ) {
          this.id = "ui-id-" + (++uuid);
        }
      });
    },

    removeUniqueId: function() {
      return this.each(function() {
        if ( runiqueId.test( this.id ) ) {
          $( this ).removeAttr( "id" );
        }
      });
    }
  });

  // selectors
  function focusable( element, isTabIndexNotNaN ) {
    var map, mapName, img,
      nodeName = element.nodeName.toLowerCase();
    if ( "area" === nodeName ) {
      map = element.parentNode;
      mapName = map.name;
      if ( !element.href || !mapName || map.nodeName.toLowerCase() !== "map" ) {
        return false;
      }
      img = $( "img[usemap=#" + mapName + "]" )[0];
      return !!img && visible( img );
    }
    return ( /input|select|textarea|button|object/.test( nodeName ) ?
      !element.disabled :
      "a" === nodeName ?
        element.href || isTabIndexNotNaN :
        isTabIndexNotNaN) &&
      // the element and all of its ancestors must be visible
      visible( element );
  }

  function visible( element ) {
    return $.expr.filters.visible( element ) &&
      !$( element ).parents().addBack().filter(function() {
        return $.css( this, "visibility" ) === "hidden";
      }).length;
  }

  $.extend( $.expr[ ":" ], {
    data: $.expr.createPseudo ?
      $.expr.createPseudo(function( dataName ) {
        return function( elem ) {
          return !!$.data( elem, dataName );
        };
      }) :
      // support: jQuery <1.8
      function( elem, i, match ) {
        return !!$.data( elem, match[ 3 ] );
      },

    focusable: function( element ) {
      return focusable( element, !isNaN( $.attr( element, "tabindex" ) ) );
    },

    tabbable: function( element ) {
      var tabIndex = $.attr( element, "tabindex" ),
        isTabIndexNaN = isNaN( tabIndex );
      return ( isTabIndexNaN || tabIndex >= 0 ) && focusable( element, !isTabIndexNaN );
    }
  });

  // support: jQuery <1.8
  if ( !$( "<a>" ).outerWidth( 1 ).jquery ) {
    $.each( [ "Width", "Height" ], function( i, name ) {
      var side = name === "Width" ? [ "Left", "Right" ] : [ "Top", "Bottom" ],
        type = name.toLowerCase(),
        orig = {
          innerWidth: $.fn.innerWidth,
          innerHeight: $.fn.innerHeight,
          outerWidth: $.fn.outerWidth,
          outerHeight: $.fn.outerHeight
        };

      function reduce( elem, size, border, margin ) {
        $.each( side, function() {
          size -= parseFloat( $.css( elem, "padding" + this ) ) || 0;
          if ( border ) {
            size -= parseFloat( $.css( elem, "border" + this + "Width" ) ) || 0;
          }
          if ( margin ) {
            size -= parseFloat( $.css( elem, "margin" + this ) ) || 0;
          }
        });
        return size;
      }

      $.fn[ "inner" + name ] = function( size ) {
        if ( size === undefined ) {
          return orig[ "inner" + name ].call( this );
        }

        return this.each(function() {
          $( this ).css( type, reduce( this, size ) + "px" );
        });
      };

      $.fn[ "outer" + name] = function( size, margin ) {
        if ( typeof size !== "number" ) {
          return orig[ "outer" + name ].call( this, size );
        }

        return this.each(function() {
          $( this).css( type, reduce( this, size, true, margin ) + "px" );
        });
      };
    });
  }

  // support: jQuery <1.8
  if ( !$.fn.addBack ) {
    $.fn.addBack = function( selector ) {
      return this.add( selector == null ?
        this.prevObject : this.prevObject.filter( selector )
      );
    };
  }

  // support: jQuery 1.6.1, 1.6.2 (http://bugs.jquery.com/ticket/9413)
  if ( $( "<a>" ).data( "a-b", "a" ).removeData( "a-b" ).data( "a-b" ) ) {
    $.fn.removeData = (function( removeData ) {
      return function( key ) {
        if ( arguments.length ) {
          return removeData.call( this, $.camelCase( key ) );
        } else {
          return removeData.call( this );
        }
      };
    })( $.fn.removeData );
  }





  // deprecated
  $.ui.ie = !!/msie [\w.]+/.exec( navigator.userAgent.toLowerCase() );

  $.support.selectstart = "onselectstart" in document.createElement( "div" );
  $.fn.extend({
    disableSelection: function() {
      return this.bind( ( $.support.selectstart ? "selectstart" : "mousedown" ) +
        ".ui-disableSelection", function( event ) {
          event.preventDefault();
        });
    },

    enableSelection: function() {
      return this.unbind( ".ui-disableSelection" );
    },

    zIndex: function( zIndex ) {
      if ( zIndex !== undefined ) {
        return this.css( "zIndex", zIndex );
      }

      if ( this.length ) {
        var elem = $( this[ 0 ] ), position, value;
        while ( elem.length && elem[ 0 ] !== document ) {
          // Ignore z-index if position is set to a value where z-index is ignored by the browser
          // This makes behavior of this function consistent across browsers
          // WebKit always returns auto if the element is positioned
          position = elem.css( "position" );
          if ( position === "absolute" || position === "relative" || position === "fixed" ) {
            // IE returns 0 when zIndex is not specified
            // other browsers return a string
            // we ignore the case of nested elements with an explicit value of 0
            // <div style="z-index: -10;"><div style="z-index: 0;"></div></div>
            value = parseInt( elem.css( "zIndex" ), 10 );
            if ( !isNaN( value ) && value !== 0 ) {
              return value;
            }
          }
          elem = elem.parent();
        }
      }

      return 0;
    }
  });

  // $.ui.plugin is deprecated. Use $.widget() extensions instead.
  $.ui.plugin = {
    add: function( module, option, set ) {
      var i,
        proto = $.ui[ module ].prototype;
      for ( i in set ) {
        proto.plugins[ i ] = proto.plugins[ i ] || [];
        proto.plugins[ i ].push( [ option, set[ i ] ] );
      }
    },
    call: function( instance, name, args, allowDisconnected ) {
      var i,
        set = instance.plugins[ name ];

      if ( !set ) {
        return;
      }

      if ( !allowDisconnected && ( !instance.element[ 0 ].parentNode || instance.element[ 0 ].parentNode.nodeType === 11 ) ) {
        return;
      }

      for ( i = 0; i < set.length; i++ ) {
        if ( instance.options[ set[ i ][ 0 ] ] ) {
          set[ i ][ 1 ].apply( instance.element, args );
        }
      }
    }
  };

  })( jQuery );

  (function( $, window, undefined ) {

    // Subtract the height of external toolbars from the page height, if the page does not have
    // internal toolbars of the same type
    var compensateToolbars = function( page, desiredHeight ) {
      var pageParent = page.parent(),
        toolbarsAffectingHeight = [],
        externalHeaders = pageParent.children( ":jqmData(role='header')" ),
        internalHeaders = page.children( ":jqmData(role='header')" ),
        externalFooters = pageParent.children( ":jqmData(role='footer')" ),
        internalFooters = page.children( ":jqmData(role='footer')" );

      // If we have no internal headers, but we do have external headers, then their height
      // reduces the page height
      if ( internalHeaders.length === 0 && externalHeaders.length > 0 ) {
        toolbarsAffectingHeight = toolbarsAffectingHeight.concat( externalHeaders.toArray() );
      }

      // If we have no internal footers, but we do have external footers, then their height
      // reduces the page height
      if ( internalFooters.length === 0 && externalFooters.length > 0 ) {
        toolbarsAffectingHeight = toolbarsAffectingHeight.concat( externalFooters.toArray() );
      }

      $.each( toolbarsAffectingHeight, function( index, value ) {
        desiredHeight -= $( value ).outerHeight();
      });

      // Height must be at least zero
      return Math.max( 0, desiredHeight );
    };

    $.extend( $.mobile, {
      // define the window and the document objects
      window: $( window ),
      document: $( document ),

      // TODO: Remove and use $.ui.keyCode directly
      keyCode: $.ui.keyCode,

      // Place to store various widget extensions
      behaviors: {},

      // Scroll page vertically: scroll to 0 to hide iOS address bar, or pass a Y value
      silentScroll: function( ypos ) {
        if ( $.type( ypos ) !== "number" ) {
          ypos = $.mobile.defaultHomeScroll;
        }

        // prevent scrollstart and scrollstop events
        $.event.special.scrollstart.enabled = false;

        setTimeout(function() {
          window.scrollTo( 0, ypos );
          $.mobile.document.trigger( "silentscroll", { x: 0, y: ypos });
        }, 20 );

        setTimeout(function() {
          $.event.special.scrollstart.enabled = true;
        }, 150 );
      },

      getClosestBaseUrl: function( ele )  {
        // Find the closest page and extract out its url.
        var url = $( ele ).closest( ".ui-page" ).jqmData( "url" ),
          base = $.mobile.path.documentBase.hrefNoHash;

        if ( !$.mobile.dynamicBaseEnabled || !url || !$.mobile.path.isPath( url ) ) {
          url = base;
        }

        return $.mobile.path.makeUrlAbsolute( url, base );
      },
      removeActiveLinkClass: function( forceRemoval ) {
        if ( !!$.mobile.activeClickedLink &&
          ( !$.mobile.activeClickedLink.closest( "." + $.mobile.activePageClass ).length ||
            forceRemoval ) ) {

          $.mobile.activeClickedLink.removeClass( $.mobile.activeBtnClass );
        }
        $.mobile.activeClickedLink = null;
      },

      // DEPRECATED in 1.4
      // Find the closest parent with a theme class on it. Note that
      // we are not using $.fn.closest() on purpose here because this
      // method gets called quite a bit and we need it to be as fast
      // as possible.
      getInheritedTheme: function( el, defaultTheme ) {
        var e = el[ 0 ],
          ltr = "",
          re = /ui-(bar|body|overlay)-([a-z])\b/,
          c, m;
        while ( e ) {
          c = e.className || "";
          if ( c && ( m = re.exec( c ) ) && ( ltr = m[ 2 ] ) ) {
            // We found a parent with a theme class
            // on it so bail from this loop.
            break;
          }

          e = e.parentNode;
        }
        // Return the theme letter we found, if none, return the
        // specified default.
        return ltr || defaultTheme || "a";
      },

      enhanceable: function( elements ) {
        return this.haveParents( elements, "enhance" );
      },

      hijackable: function( elements ) {
        return this.haveParents( elements, "ajax" );
      },

      haveParents: function( elements, attr ) {
        if ( !$.mobile.ignoreContentEnabled ) {
          return elements;
        }

        var count = elements.length,
          $newSet = $(),
          e, $element, excluded,
          i, c;

        for ( i = 0; i < count; i++ ) {
          $element = elements.eq( i );
          excluded = false;
          e = elements[ i ];

          while ( e ) {
            c = e.getAttribute ? e.getAttribute( "data-" + $.mobile.ns + attr ) : "";

            if ( c === "false" ) {
              excluded = true;
              break;
            }

            e = e.parentNode;
          }

          if ( !excluded ) {
            $newSet = $newSet.add( $element );
          }
        }

        return $newSet;
      },

      getScreenHeight: function() {
        // Native innerHeight returns more accurate value for this across platforms,
        // jQuery version is here as a normalized fallback for platforms like Symbian
        return window.innerHeight || $.mobile.window.height();
      },

      //simply set the active page's minimum height to screen height, depending on orientation
      resetActivePageHeight: function( height ) {
        var page = $( "." + $.mobile.activePageClass ),
          pageHeight = page.height(),
          pageOuterHeight = page.outerHeight( true );

        height = compensateToolbars( page,
          ( typeof height === "number" ) ? height : $.mobile.getScreenHeight() );

        page.css( "min-height", height - ( pageOuterHeight - pageHeight ) );
      },

      loading: function() {
        // If this is the first call to this function, instantiate a loader widget
        var loader = this.loading._widget || $( $.mobile.loader.prototype.defaultHtml ).loader(),

          // Call the appropriate method on the loader
          returnValue = loader.loader.apply( loader, arguments );

        // Make sure the loader is retained for future calls to this function.
        this.loading._widget = loader;

        return returnValue;
      }
    });

    $.addDependents = function( elem, newDependents ) {
      var $elem = $( elem ),
        dependents = $elem.jqmData( "dependents" ) || $();

      $elem.jqmData( "dependents", $( dependents ).add( newDependents ) );
    };

    // plugins
    $.fn.extend({
      removeWithDependents: function() {
        $.removeWithDependents( this );
      },

      // Enhance child elements
      enhanceWithin: function() {
        var index,
          widgetElements = {},
          keepNative = $.mobile.page.prototype.keepNativeSelector(),
          that = this;

        // Add no js class to elements
        if ( $.mobile.nojs ) {
          $.mobile.nojs( this );
        }

        // Bind links for ajax nav
        if ( $.mobile.links ) {
          $.mobile.links( this );
        }

        // Degrade inputs for styleing
        if ( $.mobile.degradeInputsWithin ) {
          $.mobile.degradeInputsWithin( this );
        }

        // Run buttonmarkup
        if ( $.fn.buttonMarkup ) {
          this.find( $.fn.buttonMarkup.initSelector ).not( keepNative )
          .jqmEnhanceable().buttonMarkup();
        }

        // Add classes for fieldContain
        if ( $.fn.fieldcontain ) {
          this.find( ":jqmData(role='fieldcontain')" ).not( keepNative )
          .jqmEnhanceable().fieldcontain();
        }

        // Enhance widgets
        $.each( $.mobile.widgets, function( name, constructor ) {

          // If initSelector not false find elements
          if ( constructor.initSelector ) {

            // Filter elements that should not be enhanced based on parents
            var elements = $.mobile.enhanceable( that.find( constructor.initSelector ) );

            // If any matching elements remain filter ones with keepNativeSelector
            if ( elements.length > 0 ) {

              // $.mobile.page.prototype.keepNativeSelector is deprecated this is just for backcompat
              // Switch to $.mobile.keepNative in 1.5 which is just a value not a function
              elements = elements.not( keepNative );
            }

            // Enhance whatever is left
            if ( elements.length > 0 ) {
              widgetElements[ constructor.prototype.widgetName ] = elements;
            }
          }
        });

        for ( index in widgetElements ) {
          widgetElements[ index ][ index ]();
        }

        return this;
      },

      addDependents: function( newDependents ) {
        $.addDependents( this, newDependents );
      },

      // note that this helper doesn't attempt to handle the callback
      // or setting of an html element's text, its only purpose is
      // to return the html encoded version of the text in all cases. (thus the name)
      getEncodedText: function() {
        return $( "<a>" ).text( this.text() ).html();
      },

      // fluent helper function for the mobile namespaced equivalent
      jqmEnhanceable: function() {
        return $.mobile.enhanceable( this );
      },

      jqmHijackable: function() {
        return $.mobile.hijackable( this );
      }
    });

    $.removeWithDependents = function( nativeElement ) {
      var element = $( nativeElement );

      ( element.jqmData( "dependents" ) || $() ).remove();
      element.remove();
    };
    $.addDependents = function( nativeElement, newDependents ) {
      var element = $( nativeElement ),
        dependents = element.jqmData( "dependents" ) || $();

      element.jqmData( "dependents", $( dependents ).add( newDependents ) );
    };

    $.find.matches = function( expr, set ) {
      return $.find( expr, null, null, set );
    };

    $.find.matchesSelector = function( node, expr ) {
      return $.find( expr, null, null, [ node ] ).length > 0;
    };

  })( jQuery, this );


  (function( $, undefined ) {

    /*! matchMedia() polyfill - Test a CSS media type/query in JS. Authors & copyright (c) 2012: Scott Jehl, Paul Irish, Nicholas Zakas. Dual MIT/BSD license */
    window.matchMedia = window.matchMedia || (function( doc, undefined ) {

      var bool,
        docElem = doc.documentElement,
        refNode = docElem.firstElementChild || docElem.firstChild,
        // fakeBody required for <FF4 when executed in <head>
        fakeBody = doc.createElement( "body" ),
        div = doc.createElement( "div" );

      div.id = "mq-test-1";
      div.style.cssText = "position:absolute;top:-100em";
      fakeBody.style.background = "none";
      fakeBody.appendChild(div);

      return function(q){

        div.innerHTML = "&shy;<style media=\"" + q + "\"> #mq-test-1 { width: 42px; }</style>";

        docElem.insertBefore( fakeBody, refNode );
        bool = div.offsetWidth === 42;
        docElem.removeChild( fakeBody );

        return {
          matches: bool,
          media: q
        };

      };

    }( document ));

    // $.mobile.media uses matchMedia to return a boolean.
    $.mobile.media = function( q ) {
      return window.matchMedia( q ).matches;
    };

  })(jQuery);

    (function( $, undefined ) {
      var support = {
        touch: "ontouchend" in document
      };

      $.mobile.support = $.mobile.support || {};
      $.extend( $.support, support );
      $.extend( $.mobile.support, support );
    }( jQuery ));

    (function( $, undefined ) {
      $.extend( $.support, {
        orientation: "orientation" in window && "onorientationchange" in window
      });
    }( jQuery ));

  (function( $, undefined ) {

  // thx Modernizr
  function propExists( prop ) {
    var uc_prop = prop.charAt( 0 ).toUpperCase() + prop.substr( 1 ),
      props = ( prop + " " + vendors.join( uc_prop + " " ) + uc_prop ).split( " " ),
      v;

    for ( v in props ) {
      if ( fbCSS[ props[ v ] ] !== undefined ) {
        return true;
      }
    }
  }

  var fakeBody = $( "<body>" ).prependTo( "html" ),
    fbCSS = fakeBody[ 0 ].style,
    vendors = [ "Webkit", "Moz", "O" ],
    webos = "palmGetResource" in window, //only used to rule out scrollTop
    operamini = window.operamini && ({}).toString.call( window.operamini ) === "[object OperaMini]",
    bb = window.blackberry && !propExists( "-webkit-transform" ), //only used to rule out box shadow, as it's filled opaque on BB 5 and lower
    nokiaLTE7_3;

  // inline SVG support test
  function inlineSVG() {
    // Thanks Modernizr & Erik Dahlstrom
    var w = window,
      svg = !!w.document.createElementNS && !!w.document.createElementNS( "http://www.w3.org/2000/svg", "svg" ).createSVGRect && !( w.opera && navigator.userAgent.indexOf( "Chrome" ) === -1 ),
      support = function( data ) {
        if ( !( data && svg ) ) {
          $( "html" ).addClass( "ui-nosvg" );
        }
      },
      img = new w.Image();

    img.onerror = function() {
      support( false );
    };
    img.onload = function() {
      support( img.width === 1 && img.height === 1 );
    };
    img.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
  }

  function transform3dTest() {
    var mqProp = "transform-3d",
      // Because the `translate3d` test below throws false positives in Android:
      ret = $.mobile.media( "(-" + vendors.join( "-" + mqProp + "),(-" ) + "-" + mqProp + "),(" + mqProp + ")" ),
      el, transforms, t;

    if ( ret ) {
      return !!ret;
    }

    el = document.createElement( "div" );
    transforms = {
      // We’re omitting Opera for the time being; MS uses unprefixed.
      "MozTransform": "-moz-transform",
      "transform": "transform"
    };

    fakeBody.append( el );

    for ( t in transforms ) {
      if ( el.style[ t ] !== undefined ) {
        el.style[ t ] = "translate3d( 100px, 1px, 1px )";
        ret = window.getComputedStyle( el ).getPropertyValue( transforms[ t ] );
      }
    }
    return ( !!ret && ret !== "none" );
  }

  // Test for dynamic-updating base tag support ( allows us to avoid href,src attr rewriting )
  function baseTagTest() {
    var fauxBase = location.protocol + "//" + location.host + location.pathname + "ui-dir/",
      base = $( "head base" ),
      fauxEle = null,
      href = "",
      link, rebase;

    if ( !base.length ) {
      base = fauxEle = $( "<base>", { "href": fauxBase }).appendTo( "head" );
    } else {
      href = base.attr( "href" );
    }

    link = $( "<a href='testurl' />" ).prependTo( fakeBody );
    rebase = link[ 0 ].href;
    base[ 0 ].href = href || location.pathname;

    if ( fauxEle ) {
      fauxEle.remove();
    }
    return rebase.indexOf( fauxBase ) === 0;
  }

  // Thanks Modernizr
  function cssPointerEventsTest() {
    var element = document.createElement( "x" ),
      documentElement = document.documentElement,
      getComputedStyle = window.getComputedStyle,
      supports;

    if ( !( "pointerEvents" in element.style ) ) {
      return false;
    }

    element.style.pointerEvents = "auto";
    element.style.pointerEvents = "x";
    documentElement.appendChild( element );
    supports = getComputedStyle &&
    getComputedStyle( element, "" ).pointerEvents === "auto";
    documentElement.removeChild( element );
    return !!supports;
  }

  function boundingRect() {
    var div = document.createElement( "div" );
    return typeof div.getBoundingClientRect !== "undefined";
  }

  // non-UA-based IE version check by James Padolsey, modified by jdalton - from http://gist.github.com/527683
  // allows for inclusion of IE 6+, including Windows Mobile 7
  $.extend( $.mobile, { browser: {} } );
  $.mobile.browser.oldIE = (function() {
    var v = 3,
      div = document.createElement( "div" ),
      a = div.all || [];

    do {
      div.innerHTML = "<!--[if gt IE " + ( ++v ) + "]><br><![endif]-->";
    } while( a[0] );

    return v > 4 ? v : !v;
  })();

  function fixedPosition() {
    var w = window,
      ua = navigator.userAgent,
      platform = navigator.platform,
      // Rendering engine is Webkit, and capture major version
      wkmatch = ua.match( /AppleWebKit\/([0-9]+)/ ),
      wkversion = !!wkmatch && wkmatch[ 1 ],
      ffmatch = ua.match( /Fennec\/([0-9]+)/ ),
      ffversion = !!ffmatch && ffmatch[ 1 ],
      operammobilematch = ua.match( /Opera Mobi\/([0-9]+)/ ),
      omversion = !!operammobilematch && operammobilematch[ 1 ];

    if (
      // iOS 4.3 and older : Platform is iPhone/Pad/Touch and Webkit version is less than 534 (ios5)
      ( ( platform.indexOf( "iPhone" ) > -1 || platform.indexOf( "iPad" ) > -1  || platform.indexOf( "iPod" ) > -1 ) && wkversion && wkversion < 534 ) ||
      // Opera Mini
      ( w.operamini && ({}).toString.call( w.operamini ) === "[object OperaMini]" ) ||
      ( operammobilematch && omversion < 7458 ) ||
      //Android lte 2.1: Platform is Android and Webkit version is less than 533 (Android 2.2)
      ( ua.indexOf( "Android" ) > -1 && wkversion && wkversion < 533 ) ||
      // Firefox Mobile before 6.0 -
      ( ffversion && ffversion < 6 ) ||
      // WebOS less than 3
      ( "palmGetResource" in window && wkversion && wkversion < 534 ) ||
      // MeeGo
      ( ua.indexOf( "MeeGo" ) > -1 && ua.indexOf( "NokiaBrowser/8.5.0" ) > -1 ) ) {
      return false;
    }

    return true;
  }

  $.extend( $.support, {
    // Note, Chrome for iOS has an extremely quirky implementation of popstate.
    // We've chosen to take the shortest path to a bug fix here for issue #5426
    // See the following link for information about the regex chosen
    // https://developers.google.com/chrome/mobile/docs/user-agent#chrome_for_ios_user-agent
    pushState: "pushState" in history &&
      "replaceState" in history &&
      // When running inside a FF iframe, calling replaceState causes an error
      !( window.navigator.userAgent.indexOf( "Firefox" ) >= 0 && window.top !== window ) &&
      ( window.navigator.userAgent.search(/CriOS/) === -1 ),

    mediaquery: $.mobile.media( "only all" ),
    cssPseudoElement: !!propExists( "content" ),
    touchOverflow: !!propExists( "overflowScrolling" ),
    cssTransform3d: transform3dTest(),
    boxShadow: !!propExists( "boxShadow" ) && !bb,
    fixedPosition: fixedPosition(),
    scrollTop: ("pageXOffset" in window ||
      "scrollTop" in document.documentElement ||
      "scrollTop" in fakeBody[ 0 ]) && !webos && !operamini,

    dynamicBaseTag: baseTagTest(),
    cssPointerEvents: cssPointerEventsTest(),
    boundingRect: boundingRect(),
    inlineSVG: inlineSVG
  });

  fakeBody.remove();

  // $.mobile.ajaxBlacklist is used to override ajaxEnabled on platforms that have known conflicts with hash history updates (BB5, Symbian)
  // or that generally work better browsing in regular http for full page refreshes (Opera Mini)
  // Note: This detection below is used as a last resort.
  // We recommend only using these detection methods when all other more reliable/forward-looking approaches are not possible
  nokiaLTE7_3 = (function() {

    var ua = window.navigator.userAgent;

    //The following is an attempt to match Nokia browsers that are running Symbian/s60, with webkit, version 7.3 or older
    return ua.indexOf( "Nokia" ) > -1 &&
        ( ua.indexOf( "Symbian/3" ) > -1 || ua.indexOf( "Series60/5" ) > -1 ) &&
        ua.indexOf( "AppleWebKit" ) > -1 &&
        ua.match( /(BrowserNG|NokiaBrowser)\/7\.[0-3]/ );
  })();

  // Support conditions that must be met in order to proceed
  // default enhanced qualifications are media query support OR IE 7+

  $.mobile.gradeA = function() {
    return ( ( $.support.mediaquery && $.support.cssPseudoElement ) || $.mobile.browser.oldIE && $.mobile.browser.oldIE >= 8 ) && ( $.support.boundingRect || $.fn.jquery.match(/1\.[0-7+]\.[0-9+]?/) !== null );
  };

  $.mobile.ajaxBlacklist =
        // BlackBerry browsers, pre-webkit
        window.blackberry && !window.WebKitPoint ||
        // Opera Mini
        operamini ||
        // Symbian webkits pre 7.3
        nokiaLTE7_3;

  // Lastly, this workaround is the only way we've found so far to get pre 7.3 Symbian webkit devices
  // to render the stylesheets when they're referenced before this script, as we'd recommend doing.
  // This simply reappends the CSS in place, which for some reason makes it apply
  if ( nokiaLTE7_3 ) {
    $(function() {
      $( "head link[rel='stylesheet']" ).attr( "rel", "alternate stylesheet" ).attr( "rel", "stylesheet" );
    });
  }

  // For ruling out shadows via css
  if ( !$.support.boxShadow ) {
    $( "html" ).addClass( "ui-noboxshadow" );
  }

  })( jQuery );


  (function( $, undefined ) {
    var $win = $.mobile.window, self,
      dummyFnToInitNavigate = function() {
      };

    $.event.special.beforenavigate = {
      setup: function() {
        $win.on( "navigate", dummyFnToInitNavigate );
      },

      teardown: function() {
        $win.off( "navigate", dummyFnToInitNavigate );
      }
    };

    $.event.special.navigate = self = {
      bound: false,

      pushStateEnabled: true,

      originalEventName: undefined,

      // If pushstate support is present and push state support is defined to
      // be true on the mobile namespace.
      isPushStateEnabled: function() {
        return $.support.pushState &&
          $.mobile.pushStateEnabled === true &&
          this.isHashChangeEnabled();
      },

      // !! assumes mobile namespace is present
      isHashChangeEnabled: function() {
        return $.mobile.hashListeningEnabled === true;
      },

      // TODO a lot of duplication between popstate and hashchange
      popstate: function( event ) {
        var newEvent = new $.Event( "navigate" ),
          beforeNavigate = new $.Event( "beforenavigate" ),
          state = event.originalEvent.state || {};

        beforeNavigate.originalEvent = event;
        $win.trigger( beforeNavigate );

        if ( beforeNavigate.isDefaultPrevented() ) {
          return;
        }

        if ( event.historyState ) {
          $.extend(state, event.historyState);
        }

        // Make sure the original event is tracked for the end
        // user to inspect incase they want to do something special
        newEvent.originalEvent = event;

        // NOTE we let the current stack unwind because any assignment to
        //      location.hash will stop the world and run this event handler. By
        //      doing this we create a similar behavior to hashchange on hash
        //      assignment
        setTimeout(function() {
          $win.trigger( newEvent, {
            state: state
          });
        }, 0);
      },

      hashchange: function( event /*, data */ ) {
        var newEvent = new $.Event( "navigate" ),
          beforeNavigate = new $.Event( "beforenavigate" );

        beforeNavigate.originalEvent = event;
        $win.trigger( beforeNavigate );

        if ( beforeNavigate.isDefaultPrevented() ) {
          return;
        }

        // Make sure the original event is tracked for the end
        // user to inspect incase they want to do something special
        newEvent.originalEvent = event;

        // Trigger the hashchange with state provided by the user
        // that altered the hash
        $win.trigger( newEvent, {
          // Users that want to fully normalize the two events
          // will need to do history management down the stack and
          // add the state to the event before this binding is fired
          // TODO consider allowing for the explicit addition of callbacks
          //      to be fired before this value is set to avoid event timing issues
          state: event.hashchangeState || {}
        });
      },

      // TODO We really only want to set this up once
      //      but I'm not clear if there's a beter way to achieve
      //      this with the jQuery special event structure
      setup: function( /* data, namespaces */ ) {
        if ( self.bound ) {
          return;
        }

        self.bound = true;

        if ( self.isPushStateEnabled() ) {
          self.originalEventName = "popstate";
          $win.bind( "popstate.navigate", self.popstate );
        } else if ( self.isHashChangeEnabled() ) {
          self.originalEventName = "hashchange";
          $win.bind( "hashchange.navigate", self.hashchange );
        }
      }
    };
  })( jQuery );



    // throttled resize event
    (function( $ ) {
      $.event.special.throttledresize = {
        setup: function() {
          $( this ).bind( "resize", handler );
        },
        teardown: function() {
          $( this ).unbind( "resize", handler );
        }
      };

      var throttle = 250,
        handler = function() {
          curr = ( new Date() ).getTime();
          diff = curr - lastCall;

          if ( diff >= throttle ) {

            lastCall = curr;
            $( this ).trigger( "throttledresize" );

          } else {

            if ( heldCall ) {
              clearTimeout( heldCall );
            }

            // Promise a held call will still execute
            heldCall = setTimeout( handler, throttle - diff );
          }
        },
        lastCall = 0,
        heldCall,
        curr,
        diff;
    })( jQuery );


  (function( $, window ) {
    var win = $( window ),
      event_name = "orientationchange",
      get_orientation,
      last_orientation,
      initial_orientation_is_landscape,
      initial_orientation_is_default,
      portrait_map = { "0": true, "180": true },
      ww, wh, landscape_threshold;

    // It seems that some device/browser vendors use window.orientation values 0 and 180 to
    // denote the "default" orientation. For iOS devices, and most other smart-phones tested,
    // the default orientation is always "portrait", but in some Android and RIM based tablets,
    // the default orientation is "landscape". The following code attempts to use the window
    // dimensions to figure out what the current orientation is, and then makes adjustments
    // to the to the portrait_map if necessary, so that we can properly decode the
    // window.orientation value whenever get_orientation() is called.
    //
    // Note that we used to use a media query to figure out what the orientation the browser
    // thinks it is in:
    //
    //     initial_orientation_is_landscape = $.mobile.media("all and (orientation: landscape)");
    //
    // but there was an iPhone/iPod Touch bug beginning with iOS 4.2, up through iOS 5.1,
    // where the browser *ALWAYS* applied the landscape media query. This bug does not
    // happen on iPad.

    if ( $.support.orientation ) {

      // Check the window width and height to figure out what the current orientation
      // of the device is at this moment. Note that we've initialized the portrait map
      // values to 0 and 180, *AND* we purposely check for landscape so that if we guess
      // wrong, , we default to the assumption that portrait is the default orientation.
      // We use a threshold check below because on some platforms like iOS, the iPhone
      // form-factor can report a larger width than height if the user turns on the
      // developer console. The actual threshold value is somewhat arbitrary, we just
      // need to make sure it is large enough to exclude the developer console case.

      ww = window.innerWidth || win.width();
      wh = window.innerHeight || win.height();
      landscape_threshold = 50;

      initial_orientation_is_landscape = ww > wh && ( ww - wh ) > landscape_threshold;

      // Now check to see if the current window.orientation is 0 or 180.
      initial_orientation_is_default = portrait_map[ window.orientation ];

      // If the initial orientation is landscape, but window.orientation reports 0 or 180, *OR*
      // if the initial orientation is portrait, but window.orientation reports 90 or -90, we
      // need to flip our portrait_map values because landscape is the default orientation for
      // this device/browser.
      if ( ( initial_orientation_is_landscape && initial_orientation_is_default ) || ( !initial_orientation_is_landscape && !initial_orientation_is_default ) ) {
        portrait_map = { "-90": true, "90": true };
      }
    }

    $.event.special.orientationchange = $.extend( {}, $.event.special.orientationchange, {
      setup: function() {
        // If the event is supported natively, return false so that jQuery
        // will bind to the event using DOM methods.
        if ( $.support.orientation && !$.event.special.orientationchange.disabled ) {
          return false;
        }

        // Get the current orientation to avoid initial double-triggering.
        last_orientation = get_orientation();

        // Because the orientationchange event doesn't exist, simulate the
        // event by testing window dimensions on resize.
        win.bind( "throttledresize", handler );
      },
      teardown: function() {
        // If the event is not supported natively, return false so that
        // jQuery will unbind the event using DOM methods.
        if ( $.support.orientation && !$.event.special.orientationchange.disabled ) {
          return false;
        }

        // Because the orientationchange event doesn't exist, unbind the
        // resize event handler.
        win.unbind( "throttledresize", handler );
      },
      add: function( handleObj ) {
        // Save a reference to the bound event handler.
        var old_handler = handleObj.handler;

        handleObj.handler = function( event ) {
          // Modify event object, adding the .orientation property.
          event.orientation = get_orientation();

          // Call the originally-bound event handler and return its result.
          return old_handler.apply( this, arguments );
        };
      }
    });

    // If the event is not supported natively, this handler will be bound to
    // the window resize event to simulate the orientationchange event.
    function handler() {
      // Get the current orientation.
      var orientation = get_orientation();

      if ( orientation !== last_orientation ) {
        // The orientation has changed, so trigger the orientationchange event.
        last_orientation = orientation;
        win.trigger( event_name );
      }
    }

    // Get the current page orientation. This method is exposed publicly, should it
    // be needed, as jQuery.event.special.orientationchange.orientation()
    $.event.special.orientationchange.orientation = get_orientation = function() {
      var isPortrait = true, elem = document.documentElement;

      // prefer window orientation to the calculation based on screensize as
      // the actual screen resize takes place before or after the orientation change event
      // has been fired depending on implementation (eg android 2.3 is before, iphone after).
      // More testing is required to determine if a more reliable method of determining the new screensize
      // is possible when orientationchange is fired. (eg, use media queries + element + opacity)
      if ( $.support.orientation ) {
        // if the window orientation registers as 0 or 180 degrees report
        // portrait, otherwise landscape
        isPortrait = portrait_map[ window.orientation ];
      } else {
        isPortrait = elem && elem.clientWidth / elem.clientHeight < 1.1;
      }

      return isPortrait ? "portrait" : "landscape";
    };

    $.fn[ event_name ] = function( fn ) {
      return fn ? this.bind( event_name, fn ) : this.trigger( event_name );
    };

    // jQuery < 1.8
    if ( $.attrFn ) {
      $.attrFn[ event_name ] = true;
    }

  }( jQuery, this ));


  // This plugin is an experiment for abstracting away the touch and mouse
  // events so that developers don't have to worry about which method of input
  // the device their document is loaded on supports.
  //
  // The idea here is to allow the developer to register listeners for the
  // basic mouse events, such as mousedown, mousemove, mouseup, and click,
  // and the plugin will take care of registering the correct listeners
  // behind the scenes to invoke the listener at the fastest possible time
  // for that device, while still retaining the order of event firing in
  // the traditional mouse environment, should multiple handlers be registered
  // on the same element for different events.
  //
  // The current version exposes the following virtual events to jQuery bind methods:
  // "vmouseover vmousedown vmousemove vmouseup vclick vmouseout vmousecancel"

  (function( $, window, document, undefined ) {

  var dataPropertyName = "virtualMouseBindings",
    touchTargetPropertyName = "virtualTouchID",
    virtualEventNames = "vmouseover vmousedown vmousemove vmouseup vclick vmouseout vmousecancel".split( " " ),
    touchEventProps = "clientX clientY pageX pageY screenX screenY".split( " " ),
    mouseHookProps = $.event.mouseHooks ? $.event.mouseHooks.props : [],
    mouseEventProps = $.event.props.concat( mouseHookProps ),
    activeDocHandlers = {},
    resetTimerID = 0,
    startX = 0,
    startY = 0,
    didScroll = false,
    clickBlockList = [],
    blockMouseTriggers = false,
    blockTouchTriggers = false,
    eventCaptureSupported = "addEventListener" in document,
    $document = $( document ),
    nextTouchID = 1,
    lastTouchID = 0, threshold,
    i;

  $.vmouse = {
    moveDistanceThreshold: 10,
    clickDistanceThreshold: 10,
    resetTimerDuration: 1500
  };

  function getNativeEvent( event ) {

    while ( event && typeof event.originalEvent !== "undefined" ) {
      event = event.originalEvent;
    }
    return event;
  }

  function createVirtualEvent( event, eventType ) {

    var t = event.type,
      oe, props, ne, prop, ct, touch, i, j, len;

    event = $.Event( event );
    event.type = eventType;

    oe = event.originalEvent;
    props = $.event.props;

    // addresses separation of $.event.props in to $.event.mouseHook.props and Issue 3280
    // https://github.com/jquery/jquery-mobile/issues/3280
    if ( t.search( /^(mouse|click)/ ) > -1 ) {
      props = mouseEventProps;
    }

    // copy original event properties over to the new event
    // this would happen if we could call $.event.fix instead of $.Event
    // but we don't have a way to force an event to be fixed multiple times
    if ( oe ) {
      for ( i = props.length, prop; i; ) {
        prop = props[ --i ];
        event[ prop ] = oe[ prop ];
      }
    }

    // make sure that if the mouse and click virtual events are generated
    // without a .which one is defined
    if ( t.search(/mouse(down|up)|click/) > -1 && !event.which ) {
      event.which = 1;
    }

    if ( t.search(/^touch/) !== -1 ) {
      ne = getNativeEvent( oe );
      t = ne.touches;
      ct = ne.changedTouches;
      touch = ( t && t.length ) ? t[0] : ( ( ct && ct.length ) ? ct[ 0 ] : undefined );

      if ( touch ) {
        for ( j = 0, len = touchEventProps.length; j < len; j++) {
          prop = touchEventProps[ j ];
          event[ prop ] = touch[ prop ];
        }
      }
    }

    return event;
  }

  function getVirtualBindingFlags( element ) {

    var flags = {},
      b, k;

    while ( element ) {

      b = $.data( element, dataPropertyName );

      for (  k in b ) {
        if ( b[ k ] ) {
          flags[ k ] = flags.hasVirtualBinding = true;
        }
      }
      element = element.parentNode;
    }
    return flags;
  }

  function getClosestElementWithVirtualBinding( element, eventType ) {
    var b;
    while ( element ) {

      b = $.data( element, dataPropertyName );

      if ( b && ( !eventType || b[ eventType ] ) ) {
        return element;
      }
      element = element.parentNode;
    }
    return null;
  }

  function enableTouchBindings() {
    blockTouchTriggers = false;
  }

  function disableTouchBindings() {
    blockTouchTriggers = true;
  }

  function enableMouseBindings() {
    lastTouchID = 0;
    clickBlockList.length = 0;
    blockMouseTriggers = false;

    // When mouse bindings are enabled, our
    // touch bindings are disabled.
    disableTouchBindings();
  }

  function disableMouseBindings() {
    // When mouse bindings are disabled, our
    // touch bindings are enabled.
    enableTouchBindings();
  }

  function startResetTimer() {
    clearResetTimer();
    resetTimerID = setTimeout( function() {
      resetTimerID = 0;
      enableMouseBindings();
    }, $.vmouse.resetTimerDuration );
  }

  function clearResetTimer() {
    if ( resetTimerID ) {
      clearTimeout( resetTimerID );
      resetTimerID = 0;
    }
  }

  function triggerVirtualEvent( eventType, event, flags ) {
    var ve;

    if ( ( flags && flags[ eventType ] ) ||
          ( !flags && getClosestElementWithVirtualBinding( event.target, eventType ) ) ) {

      ve = createVirtualEvent( event, eventType );

      $( event.target).trigger( ve );
    }

    return ve;
  }

  function mouseEventCallback( event ) {
    var touchID = $.data( event.target, touchTargetPropertyName ),
      ve;

    if ( !blockMouseTriggers && ( !lastTouchID || lastTouchID !== touchID ) ) {
      ve = triggerVirtualEvent( "v" + event.type, event );
      if ( ve ) {
        if ( ve.isDefaultPrevented() ) {
     
        }
        if ( ve.isPropagationStopped() ) {
          event.stopPropagation();
        }
        if ( ve.isImmediatePropagationStopped() ) {
          event.stopImmediatePropagation();
        }
      }
    }
  }

  function handleTouchStart( event ) {

    var touches = getNativeEvent( event ).touches,
      target, flags, t;

    if ( touches && touches.length === 1 ) {

      target = event.target;
      flags = getVirtualBindingFlags( target );

      if ( flags.hasVirtualBinding ) {

        lastTouchID = nextTouchID++;
        $.data( target, touchTargetPropertyName, lastTouchID );

        clearResetTimer();

        disableMouseBindings();
        didScroll = false;

        t = getNativeEvent( event ).touches[ 0 ];
        startX = t.pageX;
        startY = t.pageY;

        triggerVirtualEvent( "vmouseover", event, flags );
        triggerVirtualEvent( "vmousedown", event, flags );
      }
    }
  }

  function handleScroll( event ) {
    if ( blockTouchTriggers ) {
      return;
    }

    if ( !didScroll ) {
      triggerVirtualEvent( "vmousecancel", event, getVirtualBindingFlags( event.target ) );
    }

    didScroll = true;
    startResetTimer();
  }

  function handleTouchMove( event ) {
    if ( blockTouchTriggers ) {
      return;
    }

    var t = getNativeEvent( event ).touches[ 0 ],
      didCancel = didScroll,
      moveThreshold = $.vmouse.moveDistanceThreshold,
      flags = getVirtualBindingFlags( event.target );

      didScroll = didScroll ||
        ( Math.abs( t.pageX - startX ) > moveThreshold ||
          Math.abs( t.pageY - startY ) > moveThreshold );

    if ( didScroll && !didCancel ) {
      triggerVirtualEvent( "vmousecancel", event, flags );
    }

    triggerVirtualEvent( "vmousemove", event, flags );
    startResetTimer();
  }

  function handleTouchEnd( event ) {
    if ( blockTouchTriggers ) {
      return;
    }

    disableTouchBindings();

    var flags = getVirtualBindingFlags( event.target ),
      ve, t;
    triggerVirtualEvent( "vmouseup", event, flags );

    if ( !didScroll ) {
      ve = triggerVirtualEvent( "vclick", event, flags );
      if ( ve && ve.isDefaultPrevented() ) {
        // The target of the mouse events that follow the touchend
        // event don't necessarily match the target used during the
        // touch. This means we need to rely on coordinates for blocking
        // any click that is generated.
        t = getNativeEvent( event ).changedTouches[ 0 ];
        clickBlockList.push({
          touchID: lastTouchID,
          x: t.clientX,
          y: t.clientY
        });

        // Prevent any mouse events that follow from triggering
        // virtual event notifications.
        blockMouseTriggers = true;
      }
    }
    triggerVirtualEvent( "vmouseout", event, flags);
    didScroll = false;

    startResetTimer();
  }

  function hasVirtualBindings( ele ) {
    var bindings = $.data( ele, dataPropertyName ),
      k;

    if ( bindings ) {
      for ( k in bindings ) {
        if ( bindings[ k ] ) {
          return true;
        }
      }
    }
    return false;
  }

  function dummyMouseHandler() {}

  function getSpecialEventObject( eventType ) {
    var realType = eventType.substr( 1 );

    return {
      setup: function(/* data, namespace */) {
        // If this is the first virtual mouse binding for this element,
        // add a bindings object to its data.

        if ( !hasVirtualBindings( this ) ) {
          $.data( this, dataPropertyName, {} );
        }

        // If setup is called, we know it is the first binding for this
        // eventType, so initialize the count for the eventType to zero.
        var bindings = $.data( this, dataPropertyName );
        bindings[ eventType ] = true;

        // If this is the first virtual mouse event for this type,
        // register a global handler on the document.

        activeDocHandlers[ eventType ] = ( activeDocHandlers[ eventType ] || 0 ) + 1;

        if ( activeDocHandlers[ eventType ] === 1 ) {
          $document.bind( realType, mouseEventCallback );
        }

        // Some browsers, like Opera Mini, won't dispatch mouse/click events
        // for elements unless they actually have handlers registered on them.
        // To get around this, we register dummy handlers on the elements.

        $( this ).bind( realType, dummyMouseHandler );

        // For now, if event capture is not supported, we rely on mouse handlers.
        if ( eventCaptureSupported ) {
          // If this is the first virtual mouse binding for the document,
          // register our touchstart handler on the document.

          activeDocHandlers[ "touchstart" ] = ( activeDocHandlers[ "touchstart" ] || 0) + 1;

          if ( activeDocHandlers[ "touchstart" ] === 1 ) {
            $document.bind( "touchstart", handleTouchStart )
              .bind( "touchend", handleTouchEnd )

              // On touch platforms, touching the screen and then dragging your finger
              // causes the window content to scroll after some distance threshold is
              // exceeded. On these platforms, a scroll prevents a click event from being
              // dispatched, and on some platforms, even the touchend is suppressed. To
              // mimic the suppression of the click event, we need to watch for a scroll
              // event. Unfortunately, some platforms like iOS don't dispatch scroll
              // events until *AFTER* the user lifts their finger (touchend). This means
              // we need to watch both scroll and touchmove events to figure out whether
              // or not a scroll happenens before the touchend event is fired.

              .bind( "touchmove", handleTouchMove )
              .bind( "scroll", handleScroll );
          }
        }
      },

      teardown: function(/* data, namespace */) {
        // If this is the last virtual binding for this eventType,
        // remove its global handler from the document.

        --activeDocHandlers[ eventType ];

        if ( !activeDocHandlers[ eventType ] ) {
          $document.unbind( realType, mouseEventCallback );
        }

        if ( eventCaptureSupported ) {
          // If this is the last virtual mouse binding in existence,
          // remove our document touchstart listener.

          --activeDocHandlers[ "touchstart" ];

          if ( !activeDocHandlers[ "touchstart" ] ) {
            $document.unbind( "touchstart", handleTouchStart )
              .unbind( "touchmove", handleTouchMove )
              .unbind( "touchend", handleTouchEnd )
              .unbind( "scroll", handleScroll );
          }
        }

        var $this = $( this ),
          bindings = $.data( this, dataPropertyName );

        // teardown may be called when an element was
        // removed from the DOM. If this is the case,
        // jQuery core may have already stripped the element
        // of any data bindings so we need to check it before
        // using it.
        if ( bindings ) {
          bindings[ eventType ] = false;
        }

        // Unregister the dummy event handler.

        $this.unbind( realType, dummyMouseHandler );

        // If this is the last virtual mouse binding on the
        // element, remove the binding data from the element.

        if ( !hasVirtualBindings( this ) ) {
          $this.removeData( dataPropertyName );
        }
      }
    };
  }

  // Expose our custom events to the jQuery bind/unbind mechanism.

  for ( i = 0; i < virtualEventNames.length; i++ ) {
    $.event.special[ virtualEventNames[ i ] ] = getSpecialEventObject( virtualEventNames[ i ] );
  }

  // Add a capture click handler to block clicks.
  // Note that we require event capture support for this so if the device
  // doesn't support it, we punt for now and rely solely on mouse events.
  if ( eventCaptureSupported ) {
    document.addEventListener( "click", function( e ) {
      var cnt = clickBlockList.length,
        target = e.target,
        x, y, ele, i, o, touchID;

      if ( cnt ) {
        x = e.clientX;
        y = e.clientY;
        threshold = $.vmouse.clickDistanceThreshold;

        // The idea here is to run through the clickBlockList to see if
        // the current click event is in the proximity of one of our
        // vclick events that had preventDefault() called on it. If we find
        // one, then we block the click.
        //
        // Why do we have to rely on proximity?
        //
        // Because the target of the touch event that triggered the vclick
        // can be different from the target of the click event synthesized
        // by the browser. The target of a mouse/click event that is synthesized
        // from a touch event seems to be implementation specific. For example,
        // some browsers will fire mouse/click events for a link that is near
        // a touch event, even though the target of the touchstart/touchend event
        // says the user touched outside the link. Also, it seems that with most
        // browsers, the target of the mouse/click event is not calculated until the
        // time it is dispatched, so if you replace an element that you touched
        // with another element, the target of the mouse/click will be the new
        // element underneath that point.
        //
        // Aside from proximity, we also check to see if the target and any
        // of its ancestors were the ones that blocked a click. This is necessary
        // because of the strange mouse/click target calculation done in the
        // Android 2.1 browser, where if you click on an element, and there is a
        // mouse/click handler on one of its ancestors, the target will be the
        // innermost child of the touched element, even if that child is no where
        // near the point of touch.

        ele = target;

        while ( ele ) {
          for ( i = 0; i < cnt; i++ ) {
            o = clickBlockList[ i ];
            touchID = 0;

            if ( ( ele === target && Math.abs( o.x - x ) < threshold && Math.abs( o.y - y ) < threshold ) ||
                  $.data( ele, touchTargetPropertyName ) === o.touchID ) {
              // XXX: We may want to consider removing matches from the block list
              //      instead of waiting for the reset timer to fire.
              e.preventDefault();
              e.stopPropagation();
              return;
            }
          }
          ele = ele.parentNode;
        }
      }
    }, true);
  }
  })( jQuery, window, document );


  (function( $, window, undefined ) {
    var $document = $( document ),
      supportTouch = $.mobile.support.touch,
      scrollEvent = "touchmove scroll",
      touchStartEvent = supportTouch ? "touchstart" : "mousedown",
      touchStopEvent = supportTouch ? "touchend" : "mouseup",
      touchMoveEvent = supportTouch ? "touchmove" : "mousemove";

    // setup new event shortcuts
    $.each( ( "touchstart touchmove touchend " +
      "tap taphold " +
      "swipe swipeleft swiperight " +
      "scrollstart scrollstop" ).split( " " ), function( i, name ) {

      $.fn[ name ] = function( fn ) {
        return fn ? this.bind( name, fn ) : this.trigger( name );
      };

      // jQuery < 1.8
      if ( $.attrFn ) {
        $.attrFn[ name ] = true;
      }
    });

    function triggerCustomEvent( obj, eventType, event, bubble ) {
      var originalType = event.type;
      event.type = eventType;
      if ( bubble ) {
        $.event.trigger( event, undefined, obj );
      } else {
        $.event.dispatch.call( obj, event );
      }
      event.type = originalType;
    }

    // also handles scrollstop
    $.event.special.scrollstart = {

      enabled: true,
      setup: function() {

        var thisObject = this,
          $this = $( thisObject ),
          scrolling,
          timer;

        function trigger( event, state ) {
          scrolling = state;
          triggerCustomEvent( thisObject, scrolling ? "scrollstart" : "scrollstop", event );
        }

        // iPhone triggers scroll after a small delay; use touchmove instead
        $this.bind( scrollEvent, function( event ) {

          if ( !$.event.special.scrollstart.enabled ) {
            return;
          }

          if ( !scrolling ) {
            trigger( event, true );
          }

          clearTimeout( timer );
          timer = setTimeout( function() {
            trigger( event, false );
          }, 50 );
        });
      },
      teardown: function() {
        $( this ).unbind( scrollEvent );
      }
    };

    // also handles taphold
    $.event.special.tap = {
      tapholdThreshold: 750,
      emitTapOnTaphold: true,
      setup: function() {
        var thisObject = this,
          $this = $( thisObject ),
          isTaphold = false;

        $this.bind( "vmousedown", function( event ) {
          isTaphold = false;
          if ( event.which && event.which !== 1 ) {
            return false;
          }

          var origTarget = event.target,
            timer;

          function clearTapTimer() {
            clearTimeout( timer );
          }

          function clearTapHandlers() {
            clearTapTimer();

            $this.unbind( "vclick", clickHandler )
              .unbind( "vmouseup", clearTapTimer );
            $document.unbind( "vmousecancel", clearTapHandlers );
          }

          function clickHandler( event ) {
            clearTapHandlers();

            // ONLY trigger a 'tap' event if the start target is
            // the same as the stop target.
            if ( !isTaphold && origTarget === event.target ) {
              triggerCustomEvent( thisObject, "tap", event );
            } else if ( isTaphold ) {
              event.stopPropagation();
            }
          }

          $this.bind( "vmouseup", clearTapTimer )
            .bind( "vclick", clickHandler );
          $document.bind( "vmousecancel", clearTapHandlers );

          timer = setTimeout( function() {
            if ( !$.event.special.tap.emitTapOnTaphold ) {
              isTaphold = true;
            }
            triggerCustomEvent( thisObject, "taphold", $.Event( "taphold", { target: origTarget } ) );
          }, $.event.special.tap.tapholdThreshold );
        });
      },
      teardown: function() {
        $( this ).unbind( "vmousedown" ).unbind( "vclick" ).unbind( "vmouseup" );
        $document.unbind( "vmousecancel" );
      }
    };

    // Also handles swipeleft, swiperight
    $.event.special.swipe = {

      // More than this horizontal displacement, and we will suppress scrolling.
      scrollSupressionThreshold: 30,

      // More time than this, and it isn't a swipe.
      durationThreshold: 1000,

      // Swipe horizontal displacement must be more than this.
      horizontalDistanceThreshold: 30,

      // Swipe vertical displacement must be less than this.
      verticalDistanceThreshold: 30,

      getLocation: function ( event ) {
        var winPageX = window.pageXOffset,
          winPageY = window.pageYOffset,
          x = event.clientX,
          y = event.clientY;

        if ( event.pageY === 0 && Math.floor( y ) > Math.floor( event.pageY ) ||
          event.pageX === 0 && Math.floor( x ) > Math.floor( event.pageX ) ) {

          // iOS4 clientX/clientY have the value that should have been
          // in pageX/pageY. While pageX/page/ have the value 0
          x = x - winPageX;
          y = y - winPageY;
        } else if ( y < ( event.pageY - winPageY) || x < ( event.pageX - winPageX ) ) {

          // Some Android browsers have totally bogus values for clientX/Y
          // when scrolling/zooming a page. Detectable since clientX/clientY
          // should never be smaller than pageX/pageY minus page scroll
          x = event.pageX - winPageX;
          y = event.pageY - winPageY;
        }

        return {
          x: x,
          y: y
        };
      },

      start: function( event ) {
        var data = event.originalEvent.touches ?
            event.originalEvent.touches[ 0 ] : event,
          location = $.event.special.swipe.getLocation( data );
        return {
              time: ( new Date() ).getTime(),
              coords: [ location.x, location.y ],
              origin: $( event.target )
            };
      },

      stop: function( event ) {
        var data = event.originalEvent.touches ?
            event.originalEvent.touches[ 0 ] : event,
          location = $.event.special.swipe.getLocation( data );
        return {
              time: ( new Date() ).getTime(),
              coords: [ location.x, location.y ]
            };
      },

      handleSwipe: function( start, stop, thisObject, origTarget ) {
        if ( stop.time - start.time < $.event.special.swipe.durationThreshold &&
          Math.abs( start.coords[ 0 ] - stop.coords[ 0 ] ) > $.event.special.swipe.horizontalDistanceThreshold &&
          Math.abs( start.coords[ 1 ] - stop.coords[ 1 ] ) < $.event.special.swipe.verticalDistanceThreshold ) {
          var direction = start.coords[0] > stop.coords[ 0 ] ? "swipeleft" : "swiperight";

          triggerCustomEvent( thisObject, "swipe", $.Event( "swipe", { target: origTarget, swipestart: start, swipestop: stop }), true );
          triggerCustomEvent( thisObject, direction,$.Event( direction, { target: origTarget, swipestart: start, swipestop: stop } ), true );
          return true;
        }
        return false;

      },

      // This serves as a flag to ensure that at most one swipe event event is
      // in work at any given time
      eventInProgress: false,

      setup: function() {
        var events,
          thisObject = this,
          $this = $( thisObject ),
          context = {};

        // Retrieve the events data for this element and add the swipe context
        events = $.data( this, "mobile-events" );
        if ( !events ) {
          events = { length: 0 };
          $.data( this, "mobile-events", events );
        }
        events.length++;
        events.swipe = context;

        context.start = function( event ) {

          // Bail if we're already working on a swipe event
          if ( $.event.special.swipe.eventInProgress ) {
            return;
          }
          $.event.special.swipe.eventInProgress = true;

          var stop,
            start = $.event.special.swipe.start( event ),
            origTarget = event.target,
            emitted = false;

          context.move = function( event ) {
            if ( !start ) {
              return;
            }

            stop = $.event.special.swipe.stop( event );
            if ( !emitted ) {
              emitted = $.event.special.swipe.handleSwipe( start, stop, thisObject, origTarget );
              if ( emitted ) {

                // Reset the context to make way for the next swipe event
                $.event.special.swipe.eventInProgress = false;
              }
            }
            // prevent scrolling
            if ( Math.abs( start.coords[ 0 ] - stop.coords[ 0 ] ) > $.event.special.swipe.scrollSupressionThreshold ) {
              event.preventDefault();
            }
          };

          context.stop = function() {
              emitted = true;

              // Reset the context to make way for the next swipe event
              $.event.special.swipe.eventInProgress = false;
              $document.off( touchMoveEvent, context.move );
              context.move = null;
          };

          $document.on( touchMoveEvent, context.move )
            .one( touchStopEvent, context.stop );
        };
        $this.on( touchStartEvent, context.start );
      },

      teardown: function() {
        var events, context;

        events = $.data( this, "mobile-events" );
        if ( events ) {
          context = events.swipe;
          delete events.swipe;
          events.length--;
          if ( events.length === 0 ) {
            $.removeData( this, "mobile-events" );
          }
        }

        if ( context ) {
          if ( context.start ) {
            $( this ).off( touchStartEvent, context.start );
          }
          if ( context.move ) {
            $document.off( touchMoveEvent, context.move );
          }
          if ( context.stop ) {
            $document.off( touchStopEvent, context.stop );
          }
        }
      }
    };
    $.each({
      scrollstop: "scrollstart",
      taphold: "tap",
      swipeleft: "swipe",
      swiperight: "swipe"
    }, function( event, sourceEvent ) {

      $.event.special[ event ] = {
        setup: function() {
          $( this ).bind( sourceEvent, $.noop );
        },
        teardown: function() {
          $( this ).unbind( sourceEvent );
        }
      };
    });

  })( jQuery, this );

  (function( $, undefined ) {
    var props = {
        "animation": {},
        "transition": {}
      },
      testElement = document.createElement( "a" ),
      vendorPrefixes = [ "", "webkit-", "moz-", "o-" ];

    $.each( [ "animation", "transition" ], function( i, test ) {

      // Get correct name for test
      var testName = ( i === 0 ) ? test + "-" + "name" : test;

      $.each( vendorPrefixes, function( j, prefix ) {
        if ( testElement.style[ $.camelCase( prefix + testName ) ] !== undefined ) {
           props[ test ][ "prefix" ] = prefix;
          return false;
        }
      });

      // Set event and duration names for later use
      props[ test ][ "duration" ] =
        $.camelCase( props[ test ][ "prefix" ] + test + "-" + "duration" );
      props[ test ][ "event" ] =
        $.camelCase( props[ test ][ "prefix" ] + test + "-" + "end" );

      // All lower case if not a vendor prop
      if ( props[ test ][ "prefix" ] === "" ) {
        props[ test ][ "event" ] = props[ test ][ "event" ].toLowerCase();
      }
    });

    // If a valid prefix was found then the it is supported by the browser
    $.support.cssTransitions = ( props[ "transition" ][ "prefix" ] !== undefined );
    $.support.cssAnimations = ( props[ "animation" ][ "prefix" ] !== undefined );

    // Remove the testElement
    $( testElement ).remove();

    // Animation complete callback
    $.fn.animationComplete = function( callback, type, fallbackTime ) {
      var timer, duration,
        that = this,
        animationType = ( !type || type === "animation" ) ? "animation" : "transition";

      // Make sure selected type is supported by browser
      if ( ( $.support.cssTransitions && animationType === "transition" ) ||
        ( $.support.cssAnimations && animationType === "animation" ) ) {

        // If a fallback time was not passed set one
        if ( fallbackTime === undefined ) {

          // Make sure the was not bound to document before checking .css
          if ( $( this ).context !== document ) {

            // Parse the durration since its in second multiple by 1000 for milliseconds
            // Multiply by 3 to make sure we give the animation plenty of time.
            duration = parseFloat(
              $( this ).css( props[ animationType ].duration )
            ) * 3000;
          }

          // If we could not read a duration use the default
          if ( duration === 0 || duration === undefined || isNaN( duration ) ) {
            duration = $.fn.animationComplete.defaultDuration;
          }
        }

        // Sets up the fallback if event never comes
        timer = setTimeout( function() {
          $( that ).off( props[ animationType ].event );
          callback.apply( that );
        }, duration );

        // Bind the event
        return $( this ).one( props[ animationType ].event, function() {

          // Clear the timer so we dont call callback twice
          clearTimeout( timer );
          callback.call( this, arguments );
        });
      } else {

        // CSS animation / transitions not supported
        // Defer execution for consistency between webkit/non webkit
        setTimeout( $.proxy( callback, this ), 0 );
        return $( this );
      }
    };

    // Allow default callback to be configured on mobileInit
    $.fn.animationComplete.defaultDuration = 1000;
  })( jQuery );


  (function( $, undefined ) {

  // Deprecated in 1.4
  $.fn.fieldcontain = function(/* options */) {
    return this.addClass( "ui-field-contain" );
  };

  })( jQuery );


  (function( $, undefined ) {
      var path, $base, dialogHashKey = "&ui-state=dialog";

      $.mobile.path = path = {
        uiStateKey: "&ui-state",

        // This scary looking regular expression parses an absolute URL or its relative
        // variants (protocol, site, document, query, and hash), into the various
        // components (protocol, host, path, query, fragment, etc that make up the
        // URL as well as some other commonly used sub-parts. When used with RegExp.exec()
        // or String.match, it parses the URL into a results array that looks like this:
        //
        //     [0]: http://jblas:password@mycompany.com:8080/mail/inbox?msg=1234&type=unread#msg-content
        //     [1]: http://jblas:password@mycompany.com:8080/mail/inbox?msg=1234&type=unread
        //     [2]: http://jblas:password@mycompany.com:8080/mail/inbox
        //     [3]: http://jblas:password@mycompany.com:8080
        //     [4]: http:
        //     [5]: //
        //     [6]: jblas:password@mycompany.com:8080
        //     [7]: jblas:password
        //     [8]: jblas
        //     [9]: password
        //    [10]: mycompany.com:8080
        //    [11]: mycompany.com
        //    [12]: 8080
        //    [13]: /mail/inbox
        //    [14]: /mail/
        //    [15]: inbox
        //    [16]: ?msg=1234&type=unread
        //    [17]: #msg-content
        //
        urlParseRE: /^\s*(((([^:\/#\?]+:)?(?:(\/\/)((?:(([^:@\/#\?]+)(?:\:([^:@\/#\?]+))?)@)?(([^:\/#\?\]\[]+|\[[^\/\]@#?]+\])(?:\:([0-9]+))?))?)?)?((\/?(?:[^\/\?#]+\/+)*)([^\?#]*)))?(\?[^#]+)?)(#.*)?/,

        // Abstraction to address xss (Issue #4787) by removing the authority in
        // browsers that auto decode it. All references to location.href should be
        // replaced with a call to this method so that it can be dealt with properly here
        getLocation: function( url ) {
          var uri = url ? this.parseUrl( url ) : location,
            hash = this.parseUrl( url || location.href ).hash;

          // mimic the browser with an empty string when the hash is empty
          hash = hash === "#" ? "" : hash;

          // Make sure to parse the url or the location object for the hash because using location.hash
          // is autodecoded in firefox, the rest of the url should be from the object (location unless
          // we're testing) to avoid the inclusion of the authority
          return uri.protocol + "//" + uri.host + uri.pathname + uri.search + hash;
        },

        //return the original document url
        getDocumentUrl: function( asParsedObject ) {
          return asParsedObject ? $.extend( {}, path.documentUrl ) : path.documentUrl.href;
        },

        parseLocation: function() {
          return this.parseUrl( this.getLocation() );
        },

        //Parse a URL into a structure that allows easy access to
        //all of the URL components by name.
        parseUrl: function( url ) {
          // If we're passed an object, we'll assume that it is
          // a parsed url object and just return it back to the caller.
          if ( $.type( url ) === "object" ) {
            return url;
          }

          var matches = path.urlParseRE.exec( url || "" ) || [];

            // Create an object that allows the caller to access the sub-matches
            // by name. Note that IE returns an empty string instead of undefined,
            // like all other browsers do, so we normalize everything so its consistent
            // no matter what browser we're running on.
            return {
              href:         matches[  0 ] || "",
              hrefNoHash:   matches[  1 ] || "",
              hrefNoSearch: matches[  2 ] || "",
              domain:       matches[  3 ] || "",
              protocol:     matches[  4 ] || "",
              doubleSlash:  matches[  5 ] || "",
              authority:    matches[  6 ] || "",
              username:     matches[  8 ] || "",
              password:     matches[  9 ] || "",
              host:         matches[ 10 ] || "",
              hostname:     matches[ 11 ] || "",
              port:         matches[ 12 ] || "",
              pathname:     matches[ 13 ] || "",
              directory:    matches[ 14 ] || "",
              filename:     matches[ 15 ] || "",
              search:       matches[ 16 ] || "",
              hash:         matches[ 17 ] || ""
            };
        },

        //Turn relPath into an asbolute path. absPath is
        //an optional absolute path which describes what
        //relPath is relative to.
        makePathAbsolute: function( relPath, absPath ) {
          var absStack,
            relStack,
            i, d;

          if ( relPath && relPath.charAt( 0 ) === "/" ) {
            return relPath;
          }

          relPath = relPath || "";
          absPath = absPath ? absPath.replace( /^\/|(\/[^\/]*|[^\/]+)$/g, "" ) : "";

          absStack = absPath ? absPath.split( "/" ) : [];
          relStack = relPath.split( "/" );

          for ( i = 0; i < relStack.length; i++ ) {
            d = relStack[ i ];
            switch ( d ) {
              case ".":
                break;
              case "..":
                if ( absStack.length ) {
                  absStack.pop();
                }
                break;
              default:
                absStack.push( d );
                break;
            }
          }
          return "/" + absStack.join( "/" );
        },

        //Returns true if both urls have the same domain.
        isSameDomain: function( absUrl1, absUrl2 ) {
          return path.parseUrl( absUrl1 ).domain === path.parseUrl( absUrl2 ).domain;
        },

        //Returns true for any relative variant.
        isRelativeUrl: function( url ) {
          // All relative Url variants have one thing in common, no protocol.
          return path.parseUrl( url ).protocol === "";
        },

        //Returns true for an absolute url.
        isAbsoluteUrl: function( url ) {
          return path.parseUrl( url ).protocol !== "";
        },

        //Turn the specified realtive URL into an absolute one. This function
        //can handle all relative variants (protocol, site, document, query, fragment).
        makeUrlAbsolute: function( relUrl, absUrl ) {
          if ( !path.isRelativeUrl( relUrl ) ) {
            return relUrl;
          }

          if ( absUrl === undefined ) {
            absUrl = this.documentBase;
          }

          var relObj = path.parseUrl( relUrl ),
            absObj = path.parseUrl( absUrl ),
            protocol = relObj.protocol || absObj.protocol,
            doubleSlash = relObj.protocol ? relObj.doubleSlash : ( relObj.doubleSlash || absObj.doubleSlash ),
            authority = relObj.authority || absObj.authority,
            hasPath = relObj.pathname !== "",
            pathname = path.makePathAbsolute( relObj.pathname || absObj.filename, absObj.pathname ),
            search = relObj.search || ( !hasPath && absObj.search ) || "",
            hash = relObj.hash;

          return protocol + doubleSlash + authority + pathname + search + hash;
        },

        //Add search (aka query) params to the specified url.
        addSearchParams: function( url, params ) {
          var u = path.parseUrl( url ),
            p = ( typeof params === "object" ) ? $.param( params ) : params,
            s = u.search || "?";
          return u.hrefNoSearch + s + ( s.charAt( s.length - 1 ) !== "?" ? "&" : "" ) + p + ( u.hash || "" );
        },

        convertUrlToDataUrl: function( absUrl ) {
          var u = path.parseUrl( absUrl );
          if ( path.isEmbeddedPage( u ) ) {
            // For embedded pages, remove the dialog hash key as in getFilePath(),
            // and remove otherwise the Data Url won't match the id of the embedded Page.
            return u.hash
              .split( dialogHashKey )[0]
              .replace( /^#/, "" )
              .replace( /\?.*$/, "" );
          } else if ( path.isSameDomain( u, this.documentBase ) ) {
            return u.hrefNoHash.replace( this.documentBase.domain, "" ).split( dialogHashKey )[0];
          }

          return window.decodeURIComponent(absUrl);
        },

        //get path from current hash, or from a file path
        get: function( newPath ) {
          if ( newPath === undefined ) {
            newPath = path.parseLocation().hash;
          }
          return path.stripHash( newPath ).replace( /[^\/]*\.[^\/*]+$/, "" );
        },

        //set location hash to path
        set: function( path ) {
          location.hash = path;
        },

        //test if a given url (string) is a path
        //NOTE might be exceptionally naive
        isPath: function( url ) {
          return ( /\// ).test( url );
        },

        //return a url path with the window's location protocol/hostname/pathname removed
        clean: function( url ) {
          return url.replace( this.documentBase.domain, "" );
        },

        //just return the url without an initial #
        stripHash: function( url ) {
          return url.replace( /^#/, "" );
        },

        stripQueryParams: function( url ) {
          return url.replace( /\?.*$/, "" );
        },

        //remove the preceding hash, any query params, and dialog notations
        cleanHash: function( hash ) {
          return path.stripHash( hash.replace( /\?.*$/, "" ).replace( dialogHashKey, "" ) );
        },

        isHashValid: function( hash ) {
          return ( /^#[^#]+$/ ).test( hash );
        },

        //check whether a url is referencing the same domain, or an external domain or different protocol
        //could be mailto, etc
        isExternal: function( url ) {
          var u = path.parseUrl( url );
          return u.protocol && u.domain !== this.documentUrl.domain ? true : false;
        },

        hasProtocol: function( url ) {
          return ( /^(:?\w+:)/ ).test( url );
        },

        isEmbeddedPage: function( url ) {
          var u = path.parseUrl( url );

          //if the path is absolute, then we need to compare the url against
          //both the this.documentUrl and the documentBase. The main reason for this
          //is that links embedded within external documents will refer to the
          //application document, whereas links embedded within the application
          //document will be resolved against the document base.
          if ( u.protocol !== "" ) {
            return ( !this.isPath(u.hash) && u.hash && ( u.hrefNoHash === this.documentUrl.hrefNoHash || ( this.documentBaseDiffers && u.hrefNoHash === this.documentBase.hrefNoHash ) ) );
          }
          return ( /^#/ ).test( u.href );
        },

        squash: function( url, resolutionUrl ) {
          var href, cleanedUrl, search, stateIndex,
            isPath = this.isPath( url ),
            uri = this.parseUrl( url ),
            preservedHash = uri.hash,
            uiState = "";

          // produce a url against which we can resole the provided path
          resolutionUrl = resolutionUrl || (path.isPath(url) ? path.getLocation() : path.getDocumentUrl());

          // If the url is anything but a simple string, remove any preceding hash
          // eg #foo/bar -> foo/bar
          //    #foo -> #foo
          cleanedUrl = isPath ? path.stripHash( url ) : url;

          // If the url is a full url with a hash check if the parsed hash is a path
          // if it is, strip the #, and use it otherwise continue without change
          cleanedUrl = path.isPath( uri.hash ) ? path.stripHash( uri.hash ) : cleanedUrl;

          // Split the UI State keys off the href
          stateIndex = cleanedUrl.indexOf( this.uiStateKey );

          // store the ui state keys for use
          if ( stateIndex > -1 ) {
            uiState = cleanedUrl.slice( stateIndex );
            cleanedUrl = cleanedUrl.slice( 0, stateIndex );
          }

          // make the cleanedUrl absolute relative to the resolution url
          href = path.makeUrlAbsolute( cleanedUrl, resolutionUrl );

          // grab the search from the resolved url since parsing from
          // the passed url may not yield the correct result
          search = this.parseUrl( href ).search;

          // TODO all this crap is terrible, clean it up
          if ( isPath ) {
            // reject the hash if it's a path or it's just a dialog key
            if ( path.isPath( preservedHash ) || preservedHash.replace("#", "").indexOf( this.uiStateKey ) === 0) {
              preservedHash = "";
            }

            // Append the UI State keys where it exists and it's been removed
            // from the url
            if ( uiState && preservedHash.indexOf( this.uiStateKey ) === -1) {
              preservedHash += uiState;
            }

            // make sure that pound is on the front of the hash
            if ( preservedHash.indexOf( "#" ) === -1 && preservedHash !== "" ) {
              preservedHash = "#" + preservedHash;
            }

            // reconstruct each of the pieces with the new search string and hash
            href = path.parseUrl( href );
            href = href.protocol + "//" + href.host + href.pathname + search + preservedHash;
          } else {
            href += href.indexOf( "#" ) > -1 ? uiState : "#" + uiState;
          }

          return href;
        },

        isPreservableHash: function( hash ) {
          return hash.replace( "#", "" ).indexOf( this.uiStateKey ) === 0;
        },

        // Escape weird characters in the hash if it is to be used as a selector
        hashToSelector: function( hash ) {
          var hasHash = ( hash.substring( 0, 1 ) === "#" );
          if ( hasHash ) {
            hash = hash.substring( 1 );
          }
          return ( hasHash ? "#" : "" ) + hash.replace( /([!"#$%&'()*+,./:;<=>?@[\]^`{|}~])/g, "\\$1" );
        },

        // return the substring of a filepath before the sub-page key, for making
        // a server request
        getFilePath: function( path ) {
          var splitkey = "&" + $.mobile.subPageUrlKey;
          return path && path.split( splitkey )[0].split( dialogHashKey )[0];
        },

        // check if the specified url refers to the first page in the main
        // application document.
        isFirstPageUrl: function( url ) {
          // We only deal with absolute paths.
          var u = path.parseUrl( path.makeUrlAbsolute( url, this.documentBase ) ),

            // Does the url have the same path as the document?
            samePath = u.hrefNoHash === this.documentUrl.hrefNoHash ||
              ( this.documentBaseDiffers &&
                u.hrefNoHash === this.documentBase.hrefNoHash ),

            // Get the first page element.
            fp = $.mobile.firstPage,

            // Get the id of the first page element if it has one.
            fpId = fp && fp[0] ? fp[0].id : undefined;

          // The url refers to the first page if the path matches the document and
          // it either has no hash value, or the hash is exactly equal to the id
          // of the first page element.
          return samePath &&
            ( !u.hash ||
              u.hash === "#" ||
              ( fpId && u.hash.replace( /^#/, "" ) === fpId ) );
        },

        // Some embedded browsers, like the web view in Phone Gap, allow
        // cross-domain XHR requests if the document doing the request was loaded
        // via the file:// protocol. This is usually to allow the application to
        // "phone home" and fetch app specific data. We normally let the browser
        // handle external/cross-domain urls, but if the allowCrossDomainPages
        // option is true, we will allow cross-domain http/https requests to go
        // through our page loading logic.
        isPermittedCrossDomainRequest: function( docUrl, reqUrl ) {
          return $.mobile.allowCrossDomainPages &&
            (docUrl.protocol === "file:" || docUrl.protocol === "content:") &&
            reqUrl.search( /^https?:/ ) !== -1;
        }
      };

      path.documentUrl = path.parseLocation();

      $base = $( "head" ).find( "base" );

      path.documentBase = $base.length ?
        path.parseUrl( path.makeUrlAbsolute( $base.attr( "href" ), path.documentUrl.href ) ) :
        path.documentUrl;

      path.documentBaseDiffers = (path.documentUrl.hrefNoHash !== path.documentBase.hrefNoHash);

      //return the original document base url
      path.getDocumentBase = function( asParsedObject ) {
        return asParsedObject ? $.extend( {}, path.documentBase ) : path.documentBase.href;
      };

      // DEPRECATED as of 1.4.0 - remove in 1.5.0
      $.extend( $.mobile, {

        //return the original document url
        getDocumentUrl: path.getDocumentUrl,

        //return the original document base url
        getDocumentBase: path.getDocumentBase
      });
  })( jQuery );



  (function( $, undefined ) {
    $.mobile.History = function( stack, index ) {
      this.stack = stack || [];
      this.activeIndex = index || 0;
    };

    $.extend($.mobile.History.prototype, {
      getActive: function() {
        return this.stack[ this.activeIndex ];
      },

      getLast: function() {
        return this.stack[ this.previousIndex ];
      },

      getNext: function() {
        return this.stack[ this.activeIndex + 1 ];
      },

      getPrev: function() {
        return this.stack[ this.activeIndex - 1 ];
      },

      // addNew is used whenever a new page is added
      add: function( url, data ) {
        data = data || {};

        //if there's forward history, wipe it
        if ( this.getNext() ) {
          this.clearForward();
        }

        // if the hash is included in the data make sure the shape
        // is consistent for comparison
        if ( data.hash && data.hash.indexOf( "#" ) === -1) {
          data.hash = "#" + data.hash;
        }

        data.url = url;
        this.stack.push( data );
        this.activeIndex = this.stack.length - 1;
      },

      //wipe urls ahead of active index
      clearForward: function() {
        this.stack = this.stack.slice( 0, this.activeIndex + 1 );
      },

      find: function( url, stack, earlyReturn ) {
        stack = stack || this.stack;

        var entry, i, length = stack.length, index;

        for ( i = 0; i < length; i++ ) {
          entry = stack[i];

          if ( decodeURIComponent(url) === decodeURIComponent(entry.url) ||
            decodeURIComponent(url) === decodeURIComponent(entry.hash) ) {
            index = i;

            if ( earlyReturn ) {
              return index;
            }
          }
        }

        return index;
      },

      closest: function( url ) {
        var closest, a = this.activeIndex;

        // First, take the slice of the history stack before the current index and search
        // for a url match. If one is found, we'll avoid avoid looking through forward history
        // NOTE the preference for backward history movement is driven by the fact that
        //      most mobile browsers only have a dedicated back button, and users rarely use
        //      the forward button in desktop browser anyhow
        closest = this.find( url, this.stack.slice(0, a) );

        // If nothing was found in backward history check forward. The `true`
        // value passed as the third parameter causes the find method to break
        // on the first match in the forward history slice. The starting index
        // of the slice must then be added to the result to get the element index
        // in the original history stack :( :(
        //
        // TODO this is hyper confusing and should be cleaned up (ugh so bad)
        if ( closest === undefined ) {
          closest = this.find( url, this.stack.slice(a), true );
          closest = closest === undefined ? closest : closest + a;
        }

        return closest;
      },

      direct: function( opts ) {
        var newActiveIndex = this.closest( opts.url ), a = this.activeIndex;

        // save new page index, null check to prevent falsey 0 result
        // record the previous index for reference
        if ( newActiveIndex !== undefined ) {
          this.activeIndex = newActiveIndex;
          this.previousIndex = a;
        }

        // invoke callbacks where appropriate
        //
        // TODO this is also convoluted and confusing
        if ( newActiveIndex < a ) {
          ( opts.present || opts.back || $.noop )( this.getActive(), "back" );
        } else if ( newActiveIndex > a ) {
          ( opts.present || opts.forward || $.noop )( this.getActive(), "forward" );
        } else if ( newActiveIndex === undefined && opts.missing ) {
          opts.missing( this.getActive() );
        }
      }
    });
  })( jQuery );



  (function( $, undefined ) {
    var path = $.mobile.path,
      initialHref = location.href;

    $.mobile.Navigator = function( history ) {
      this.history = history;
      this.ignoreInitialHashChange = true;

      $.mobile.window.bind({
        "popstate.history": $.proxy( this.popstate, this ),
        "hashchange.history": $.proxy( this.hashchange, this )
      });
    };

    $.extend($.mobile.Navigator.prototype, {
      squash: function( url, data ) {
        var state, href, hash = path.isPath(url) ? path.stripHash(url) : url;

        href = path.squash( url );

        // make sure to provide this information when it isn't explicitly set in the
        // data object that was passed to the squash method
        state = $.extend({
          hash: hash,
          url: href
        }, data);

        // replace the current url with the new href and store the state
        // Note that in some cases we might be replacing an url with the
        // same url. We do this anyways because we need to make sure that
        // all of our history entries have a state object associated with
        // them. This allows us to work around the case where $.mobile.back()
        // is called to transition from an external page to an embedded page.
        // In that particular case, a hashchange event is *NOT* generated by the browser.
        // Ensuring each history entry has a state object means that onPopState()
        // will always trigger our hashchange callback even when a hashchange event
        // is not fired.
        window.history.replaceState( state, state.title || document.title, href );

        return state;
      },

      hash: function( url, href ) {
        var parsed, loc, hash, resolved;

        // Grab the hash for recording. If the passed url is a path
        // we used the parsed version of the squashed url to reconstruct,
        // otherwise we assume it's a hash and store it directly
        parsed = path.parseUrl( url );
        loc = path.parseLocation();

        if ( loc.pathname + loc.search === parsed.pathname + parsed.search ) {
          // If the pathname and search of the passed url is identical to the current loc
          // then we must use the hash. Otherwise there will be no event
          // eg, url = "/foo/bar?baz#bang", location.href = "http://example.com/foo/bar?baz"
          hash = parsed.hash ? parsed.hash : parsed.pathname + parsed.search;
        } else if ( path.isPath(url) ) {
          resolved = path.parseUrl( href );
          // If the passed url is a path, make it domain relative and remove any trailing hash
          hash = resolved.pathname + resolved.search + (path.isPreservableHash( resolved.hash )? resolved.hash.replace( "#", "" ) : "");
        } else {
          hash = url;
        }

        return hash;
      },

      // TODO reconsider name
      go: function( url, data, noEvents ) {
        var state, href, hash, popstateEvent,
          isPopStateEvent = $.event.special.navigate.isPushStateEnabled();

        // Get the url as it would look squashed on to the current resolution url
        href = path.squash( url );

        // sort out what the hash sould be from the url
        hash = this.hash( url, href );

        // Here we prevent the next hash change or popstate event from doing any
        // history management. In the case of hashchange we don't swallow it
        // if there will be no hashchange fired (since that won't reset the value)
        // and will swallow the following hashchange
        if ( noEvents && hash !== path.stripHash(path.parseLocation().hash) ) {
          this.preventNextHashChange = noEvents;
        }

        // IMPORTANT in the case where popstate is supported the event will be triggered
        //      directly, stopping further execution - ie, interupting the flow of this
        //      method call to fire bindings at this expression. Below the navigate method
        //      there is a binding to catch this event and stop its propagation.
        //
        //      We then trigger a new popstate event on the window with a null state
        //      so that the navigate events can conclude their work properly
        //
        // if the url is a path we want to preserve the query params that are available on
        // the current url.
        this.preventHashAssignPopState = true;
        window.location.hash = hash;

        // If popstate is enabled and the browser triggers `popstate` events when the hash
        // is set (this often happens immediately in browsers like Chrome), then the
        // this flag will be set to false already. If it's a browser that does not trigger
        // a `popstate` on hash assignement or `replaceState` then we need avoid the branch
        // that swallows the event created by the popstate generated by the hash assignment
        // At the time of this writing this happens with Opera 12 and some version of IE
        this.preventHashAssignPopState = false;

        state = $.extend({
          url: href,
          hash: hash,
          title: document.title
        }, data);

        if ( isPopStateEvent ) {
          popstateEvent = new $.Event( "popstate" );
          popstateEvent.originalEvent = {
            type: "popstate",
            state: null
          };

          this.squash( url, state );

          // Trigger a new faux popstate event to replace the one that we
          // caught that was triggered by the hash setting above.
          if ( !noEvents ) {
            this.ignorePopState = true;
            $.mobile.window.trigger( popstateEvent );
          }
        }

        // record the history entry so that the information can be included
        // in hashchange event driven navigate events in a similar fashion to
        // the state that's provided by popstate
        this.history.add( state.url, state );
      },

      // This binding is intended to catch the popstate events that are fired
      // when execution of the `$.navigate` method stops at window.location.hash = url;
      // and completely prevent them from propagating. The popstate event will then be
      // retriggered after execution resumes
      //
      // TODO grab the original event here and use it for the synthetic event in the
      //      second half of the navigate execution that will follow this binding
      popstate: function( event ) {
        var hash, state;

        // Partly to support our test suite which manually alters the support
        // value to test hashchange. Partly to prevent all around weirdness
        if ( !$.event.special.navigate.isPushStateEnabled() ) {
          return;
        }

        // If this is the popstate triggered by the actual alteration of the hash
        // prevent it completely. History is tracked manually
        if ( this.preventHashAssignPopState ) {
          this.preventHashAssignPopState = false;
          event.stopImmediatePropagation();
          return;
        }

        // if this is the popstate triggered after the `replaceState` call in the go
        // method, then simply ignore it. The history entry has already been captured
        if ( this.ignorePopState ) {
          this.ignorePopState = false;
          return;
        }

        // If there is no state, and the history stack length is one were
        // probably getting the page load popstate fired by browsers like chrome
        // avoid it and set the one time flag to false.
        // TODO: Do we really need all these conditions? Comparing location hrefs
        // should be sufficient.
        if ( !event.originalEvent.state &&
          this.history.stack.length === 1 &&
          this.ignoreInitialHashChange ) {
          this.ignoreInitialHashChange = false;

          if ( location.href === initialHref ) {
            event.preventDefault();
            return;
          }
        }

        // account for direct manipulation of the hash. That is, we will receive a popstate
        // when the hash is changed by assignment, and it won't have a state associated. We
        // then need to squash the hash. See below for handling of hash assignment that
        // matches an existing history entry
        // TODO it might be better to only add to the history stack
        //      when the hash is adjacent to the active history entry
        hash = path.parseLocation().hash;
        if ( !event.originalEvent.state && hash ) {
          // squash the hash that's been assigned on the URL with replaceState
          // also grab the resulting state object for storage
          state = this.squash( hash );

          // record the new hash as an additional history entry
          // to match the browser's treatment of hash assignment
          this.history.add( state.url, state );

          // pass the newly created state information
          // along with the event
          event.historyState = state;

          // do not alter history, we've added a new history entry
          // so we know where we are
          return;
        }

        // If all else fails this is a popstate that comes from the back or forward buttons
        // make sure to set the state of our history stack properly, and record the directionality
        this.history.direct({
          url: (event.originalEvent.state || {}).url || hash,

          // When the url is either forward or backward in history include the entry
          // as data on the event object for merging as data in the navigate event
          present: function( historyEntry, direction ) {
            // make sure to create a new object to pass down as the navigate event data
            event.historyState = $.extend({}, historyEntry);
            event.historyState.direction = direction;
          }
        });
      },

      // NOTE must bind before `navigate` special event hashchange binding otherwise the
      //      navigation data won't be attached to the hashchange event in time for those
      //      bindings to attach it to the `navigate` special event
      // TODO add a check here that `hashchange.navigate` is bound already otherwise it's
      //      broken (exception?)
      hashchange: function( event ) {
        var history, hash;

        // If hashchange listening is explicitly disabled or pushstate is supported
        // avoid making use of the hashchange handler.
        if (!$.event.special.navigate.isHashChangeEnabled() ||
          $.event.special.navigate.isPushStateEnabled() ) {
          return;
        }

        // On occasion explicitly want to prevent the next hash from propogating because we only
        // with to alter the url to represent the new state do so here
        if ( this.preventNextHashChange ) {
          this.preventNextHashChange = false;
          event.stopImmediatePropagation();
          return;
        }

        history = this.history;
        hash = path.parseLocation().hash;

        // If this is a hashchange caused by the back or forward button
        // make sure to set the state of our history stack properly
        this.history.direct({
          url: hash,

          // When the url is either forward or backward in history include the entry
          // as data on the event object for merging as data in the navigate event
          present: function( historyEntry, direction ) {
            // make sure to create a new object to pass down as the navigate event data
            event.hashchangeState = $.extend({}, historyEntry);
            event.hashchangeState.direction = direction;
          },

          // When we don't find a hash in our history clearly we're aiming to go there
          // record the entry as new for future traversal
          //
          // NOTE it's not entirely clear that this is the right thing to do given that we
          //      can't know the users intention. It might be better to explicitly _not_
          //      support location.hash assignment in preference to $.navigate calls
          // TODO first arg to add should be the href, but it causes issues in identifying
          //      embeded pages
          missing: function() {
            history.add( hash, {
              hash: hash,
              title: document.title
            });
          }
        });
      }
    });
  })( jQuery );



  (function( $, undefined ) {
    // TODO consider queueing navigation activity until previous activities have completed
    //      so that end users don't have to think about it. Punting for now
    // TODO !! move the event bindings into callbacks on the navigate event
    $.mobile.navigate = function( url, data, noEvents ) {
      $.mobile.navigate.navigator.go( url, data, noEvents );
    };

    // expose the history on the navigate method in anticipation of full integration with
    // existing navigation functionalty that is tightly coupled to the history information
    $.mobile.navigate.history = new $.mobile.History();

    // instantiate an instance of the navigator for use within the $.navigate method
    $.mobile.navigate.navigator = new $.mobile.Navigator( $.mobile.navigate.history );

    var loc = $.mobile.path.parseLocation();
    $.mobile.navigate.history.add( loc.href, {hash: loc.hash} );
  })( jQuery );



  (function( $, undefined ) {

    // existing base tag?
    var baseElement = $( "head" ).children( "base" ),

    // base element management, defined depending on dynamic base tag support
    // TODO move to external widget
    base = {

      // define base element, for use in routing asset urls that are referenced
      // in Ajax-requested markup
      element: ( baseElement.length ? baseElement :
        $( "<base>", { href: $.mobile.path.documentBase.hrefNoHash } ).prependTo( $( "head" ) ) ),

      linkSelector: "[src], link[href], a[rel='external'], :jqmData(ajax='false'), a[target]",

      // set the generated BASE element's href to a new page's base path
      set: function( href ) {

        // we should do nothing if the user wants to manage their url base
        // manually
        if ( !$.mobile.dynamicBaseEnabled ) {
          return;
        }

        // we should use the base tag if we can manipulate it dynamically
        if ( $.support.dynamicBaseTag ) {
          base.element.attr( "href",
            $.mobile.path.makeUrlAbsolute( href, $.mobile.path.documentBase ) );
        }
      },

      rewrite: function( href, page ) {
        var newPath = $.mobile.path.get( href );

        page.find( base.linkSelector ).each(function( i, link ) {
          var thisAttr = $( link ).is( "[href]" ) ? "href" :
            $( link ).is( "[src]" ) ? "src" : "action",
          thisUrl = $( link ).attr( thisAttr );

          // XXX_jblas: We need to fix this so that it removes the document
          //            base URL, and then prepends with the new page URL.
          // if full path exists and is same, chop it - helps IE out
          thisUrl = thisUrl.replace( location.protocol + "//" +
            location.host + location.pathname, "" );

          if ( !/^(\w+:|#|\/)/.test( thisUrl ) ) {
            $( link ).attr( thisAttr, newPath + thisUrl );
          }
        });
      },

      // set the generated BASE element's href to a new page's base path
      reset: function(/* href */) {
        base.element.attr( "href", $.mobile.path.documentBase.hrefNoSearch );
      }
    };

    $.mobile.base = base;

  })( jQuery );


  /*!
   * jQuery UI Widget c0ab71056b936627e8a7821f03c044aec6280a40
   * http://jqueryui.com
   *
   * Copyright 2013 jQuery Foundation and other contributors
   * Released under the MIT license.
   * http://jquery.org/license
   *
   * http://api.jqueryui.com/jQuery.widget/
   */
  (function( $, undefined ) {

  var uuid = 0,
    slice = Array.prototype.slice,
    _cleanData = $.cleanData;
  $.cleanData = function( elems ) {
    for ( var i = 0, elem; (elem = elems[i]) != null; i++ ) {
      try {
        $( elem ).triggerHandler( "remove" );
      // http://bugs.jquery.com/ticket/8235
      } catch( e ) {}
    }
    _cleanData( elems );
  };

  $.widget = function( name, base, prototype ) {
    var fullName, existingConstructor, constructor, basePrototype,
      // proxiedPrototype allows the provided prototype to remain unmodified
      // so that it can be used as a mixin for multiple widgets (#8876)
      proxiedPrototype = {},
      namespace = name.split( "." )[ 0 ];

    name = name.split( "." )[ 1 ];
    fullName = namespace + "-" + name;

    if ( !prototype ) {
      prototype = base;
      base = $.Widget;
    }

    // create selector for plugin
    $.expr[ ":" ][ fullName.toLowerCase() ] = function( elem ) {
      return !!$.data( elem, fullName );
    };

    $[ namespace ] = $[ namespace ] || {};
    existingConstructor = $[ namespace ][ name ];
    constructor = $[ namespace ][ name ] = function( options, element ) {
      // allow instantiation without "new" keyword
      if ( !this._createWidget ) {
        return new constructor( options, element );
      }

      // allow instantiation without initializing for simple inheritance
      // must use "new" keyword (the code above always passes args)
      if ( arguments.length ) {
        this._createWidget( options, element );
      }
    };
    // extend with the existing constructor to carry over any static properties
    $.extend( constructor, existingConstructor, {
      version: prototype.version,
      // copy the object used to create the prototype in case we need to
      // redefine the widget later
      _proto: $.extend( {}, prototype ),
      // track widgets that inherit from this widget in case this widget is
      // redefined after a widget inherits from it
      _childConstructors: []
    });

    basePrototype = new base();
    // we need to make the options hash a property directly on the new instance
    // otherwise we'll modify the options hash on the prototype that we're
    // inheriting from
    basePrototype.options = $.widget.extend( {}, basePrototype.options );
    $.each( prototype, function( prop, value ) {
      if ( !$.isFunction( value ) ) {
        proxiedPrototype[ prop ] = value;
        return;
      }
      proxiedPrototype[ prop ] = (function() {
        var _super = function() {
            return base.prototype[ prop ].apply( this, arguments );
          },
          _superApply = function( args ) {
            return base.prototype[ prop ].apply( this, args );
          };
        return function() {
          var __super = this._super,
            __superApply = this._superApply,
            returnValue;

          this._super = _super;
          this._superApply = _superApply;

          returnValue = value.apply( this, arguments );

          this._super = __super;
          this._superApply = __superApply;

          return returnValue;
        };
      })();
    });
    constructor.prototype = $.widget.extend( basePrototype, {
      // TODO: remove support for widgetEventPrefix
      // always use the name + a colon as the prefix, e.g., draggable:start
      // don't prefix for widgets that aren't DOM-based
      widgetEventPrefix: existingConstructor ? (basePrototype.widgetEventPrefix || name) : name
    }, proxiedPrototype, {
      constructor: constructor,
      namespace: namespace,
      widgetName: name,
      widgetFullName: fullName
    });

    // If this widget is being redefined then we need to find all widgets that
    // are inheriting from it and redefine all of them so that they inherit from
    // the new version of this widget. We're essentially trying to replace one
    // level in the prototype chain.
    if ( existingConstructor ) {
      $.each( existingConstructor._childConstructors, function( i, child ) {
        var childPrototype = child.prototype;

        // redefine the child widget using the same prototype that was
        // originally used, but inherit from the new version of the base
        $.widget( childPrototype.namespace + "." + childPrototype.widgetName, constructor, child._proto );
      });
      // remove the list of existing child constructors from the old constructor
      // so the old child constructors can be garbage collected
      delete existingConstructor._childConstructors;
    } else {
      base._childConstructors.push( constructor );
    }

    $.widget.bridge( name, constructor );

    return constructor;
  };

  $.widget.extend = function( target ) {
    var input = slice.call( arguments, 1 ),
      inputIndex = 0,
      inputLength = input.length,
      key,
      value;
    for ( ; inputIndex < inputLength; inputIndex++ ) {
      for ( key in input[ inputIndex ] ) {
        value = input[ inputIndex ][ key ];
        if ( input[ inputIndex ].hasOwnProperty( key ) && value !== undefined ) {
          // Clone objects
          if ( $.isPlainObject( value ) ) {
            target[ key ] = $.isPlainObject( target[ key ] ) ?
              $.widget.extend( {}, target[ key ], value ) :
              // Don't extend strings, arrays, etc. with objects
              $.widget.extend( {}, value );
          // Copy everything else by reference
          } else {
            target[ key ] = value;
          }
        }
      }
    }
    return target;
  };

  $.widget.bridge = function( name, object ) {
    var fullName = object.prototype.widgetFullName || name;
    $.fn[ name ] = function( options ) {
      var isMethodCall = typeof options === "string",
        args = slice.call( arguments, 1 ),
        returnValue = this;

      // allow multiple hashes to be passed on init
      options = !isMethodCall && args.length ?
        $.widget.extend.apply( null, [ options ].concat(args) ) :
        options;

      if ( isMethodCall ) {
        this.each(function() {
          var methodValue,
            instance = $.data( this, fullName );
          if ( options === "instance" ) {
            returnValue = instance;
            return false;
          }
          if ( !instance ) {
            return $.error( "cannot call methods on " + name + " prior to initialization; " +
              "attempted to call method '" + options + "'" );
          }
          if ( !$.isFunction( instance[options] ) || options.charAt( 0 ) === "_" ) {
            return $.error( "no such method '" + options + "' for " + name + " widget instance" );
          }
          methodValue = instance[ options ].apply( instance, args );
          if ( methodValue !== instance && methodValue !== undefined ) {
            returnValue = methodValue && methodValue.jquery ?
              returnValue.pushStack( methodValue.get() ) :
              methodValue;
            return false;
          }
        });
      } else {
        this.each(function() {
          var instance = $.data( this, fullName );
          if ( instance ) {
            instance.option( options || {} )._init();
          } else {
            $.data( this, fullName, new object( options, this ) );
          }
        });
      }

      return returnValue;
    };
  };

  $.Widget = function( /* options, element */ ) {};
  $.Widget._childConstructors = [];

  $.Widget.prototype = {
    widgetName: "widget",
    widgetEventPrefix: "",
    defaultElement: "<div>",
    options: {
      disabled: false,

      // callbacks
      create: null
    },
    _createWidget: function( options, element ) {
      element = $( element || this.defaultElement || this )[ 0 ];
      this.element = $( element );
      this.uuid = uuid++;
      this.eventNamespace = "." + this.widgetName + this.uuid;
      this.options = $.widget.extend( {},
        this.options,
        this._getCreateOptions(),
        options );

      this.bindings = $();
      this.hoverable = $();
      this.focusable = $();

      if ( element !== this ) {
        $.data( element, this.widgetFullName, this );
        this._on( true, this.element, {
          remove: function( event ) {
            if ( event.target === element ) {
              this.destroy();
            }
          }
        });
        this.document = $( element.style ?
          // element within the document
          element.ownerDocument :
          // element is window or document
          element.document || element );
        this.window = $( this.document[0].defaultView || this.document[0].parentWindow );
      }

      this._create();
      this._trigger( "create", null, this._getCreateEventData() );
      this._init();
    },
    _getCreateOptions: $.noop,
    _getCreateEventData: $.noop,
    _create: $.noop,
    _init: $.noop,

    destroy: function() {
      this._destroy();
      // we can probably remove the unbind calls in 2.0
      // all event bindings should go through this._on()
      this.element
        .unbind( this.eventNamespace )
        .removeData( this.widgetFullName )
        // support: jquery <1.6.3
        // http://bugs.jquery.com/ticket/9413
        .removeData( $.camelCase( this.widgetFullName ) );
      this.widget()
        .unbind( this.eventNamespace )
        .removeAttr( "aria-disabled" )
        .removeClass(
          this.widgetFullName + "-disabled " +
          "ui-state-disabled" );

      // clean up events and states
      this.bindings.unbind( this.eventNamespace );
      this.hoverable.removeClass( "ui-state-hover" );
      this.focusable.removeClass( "ui-state-focus" );
    },
    _destroy: $.noop,

    widget: function() {
      return this.element;
    },

    option: function( key, value ) {
      var options = key,
        parts,
        curOption,
        i;

      if ( arguments.length === 0 ) {
        // don't return a reference to the internal hash
        return $.widget.extend( {}, this.options );
      }

      if ( typeof key === "string" ) {
        // handle nested keys, e.g., "foo.bar" => { foo: { bar: ___ } }
        options = {};
        parts = key.split( "." );
        key = parts.shift();
        if ( parts.length ) {
          curOption = options[ key ] = $.widget.extend( {}, this.options[ key ] );
          for ( i = 0; i < parts.length - 1; i++ ) {
            curOption[ parts[ i ] ] = curOption[ parts[ i ] ] || {};
            curOption = curOption[ parts[ i ] ];
          }
          key = parts.pop();
          if ( value === undefined ) {
            return curOption[ key ] === undefined ? null : curOption[ key ];
          }
          curOption[ key ] = value;
        } else {
          if ( value === undefined ) {
            return this.options[ key ] === undefined ? null : this.options[ key ];
          }
          options[ key ] = value;
        }
      }

      this._setOptions( options );

      return this;
    },
    _setOptions: function( options ) {
      var key;

      for ( key in options ) {
        this._setOption( key, options[ key ] );
      }

      return this;
    },
    _setOption: function( key, value ) {
      this.options[ key ] = value;

      if ( key === "disabled" ) {
        this.widget()
          .toggleClass( this.widgetFullName + "-disabled", !!value );
        this.hoverable.removeClass( "ui-state-hover" );
        this.focusable.removeClass( "ui-state-focus" );
      }

      return this;
    },

    enable: function() {
      return this._setOptions({ disabled: false });
    },
    disable: function() {
      return this._setOptions({ disabled: true });
    },

    _on: function( suppressDisabledCheck, element, handlers ) {
      var delegateElement,
        instance = this;

      // no suppressDisabledCheck flag, shuffle arguments
      if ( typeof suppressDisabledCheck !== "boolean" ) {
        handlers = element;
        element = suppressDisabledCheck;
        suppressDisabledCheck = false;
      }

      // no element argument, shuffle and use this.element
      if ( !handlers ) {
        handlers = element;
        element = this.element;
        delegateElement = this.widget();
      } else {
        // accept selectors, DOM elements
        element = delegateElement = $( element );
        this.bindings = this.bindings.add( element );
      }

      $.each( handlers, function( event, handler ) {
        function handlerProxy() {
          // allow widgets to customize the disabled handling
          // - disabled as an array instead of boolean
          // - disabled class as method for disabling individual parts
          if ( !suppressDisabledCheck &&
              ( instance.options.disabled === true ||
                $( this ).hasClass( "ui-state-disabled" ) ) ) {
            return;
          }
          return ( typeof handler === "string" ? instance[ handler ] : handler )
            .apply( instance, arguments );
        }

        // copy the guid so direct unbinding works
        if ( typeof handler !== "string" ) {
          handlerProxy.guid = handler.guid =
            handler.guid || handlerProxy.guid || $.guid++;
        }

        var match = event.match( /^(\w+)\s*(.*)$/ ),
          eventName = match[1] + instance.eventNamespace,
          selector = match[2];
        if ( selector ) {
          delegateElement.delegate( selector, eventName, handlerProxy );
        } else {
          element.bind( eventName, handlerProxy );
        }
      });
    },

    _off: function( element, eventName ) {
      eventName = (eventName || "").split( " " ).join( this.eventNamespace + " " ) + this.eventNamespace;
      element.unbind( eventName ).undelegate( eventName );
    },

    _delay: function( handler, delay ) {
      function handlerProxy() {
        return ( typeof handler === "string" ? instance[ handler ] : handler )
          .apply( instance, arguments );
      }
      var instance = this;
      return setTimeout( handlerProxy, delay || 0 );
    },

    _hoverable: function( element ) {
      this.hoverable = this.hoverable.add( element );
      this._on( element, {
        mouseenter: function( event ) {
          $( event.currentTarget ).addClass( "ui-state-hover" );
        },
        mouseleave: function( event ) {
          $( event.currentTarget ).removeClass( "ui-state-hover" );
        }
      });
    },

    _focusable: function( element ) {
      this.focusable = this.focusable.add( element );
      this._on( element, {
        focusin: function( event ) {
          $( event.currentTarget ).addClass( "ui-state-focus" );
        },
        focusout: function( event ) {
          $( event.currentTarget ).removeClass( "ui-state-focus" );
        }
      });
    },

    _trigger: function( type, event, data ) {
      var prop, orig,
        callback = this.options[ type ];

      data = data || {};
      event = $.Event( event );
      event.type = ( type === this.widgetEventPrefix ?
        type :
        this.widgetEventPrefix + type ).toLowerCase();
      // the original event may come from any element
      // so we need to reset the target on the new event
      event.target = this.element[ 0 ];

      // copy original event properties over to the new event
      orig = event.originalEvent;
      if ( orig ) {
        for ( prop in orig ) {
          if ( !( prop in event ) ) {
            event[ prop ] = orig[ prop ];
          }
        }
      }

      this.element.trigger( event, data );
      return !( $.isFunction( callback ) &&
        callback.apply( this.element[0], [ event ].concat( data ) ) === false ||
        event.isDefaultPrevented() );
    }
  };

  $.each( { show: "fadeIn", hide: "fadeOut" }, function( method, defaultEffect ) {
    $.Widget.prototype[ "_" + method ] = function( element, options, callback ) {
      if ( typeof options === "string" ) {
        options = { effect: options };
      }
      var hasOptions,
        effectName = !options ?
          method :
          options === true || typeof options === "number" ?
            defaultEffect :
            options.effect || defaultEffect;
      options = options || {};
      if ( typeof options === "number" ) {
        options = { duration: options };
      }
      hasOptions = !$.isEmptyObject( options );
      options.complete = callback;
      if ( options.delay ) {
        element.delay( options.delay );
      }
      if ( hasOptions && $.effects && $.effects.effect[ effectName ] ) {
        element[ method ]( options );
      } else if ( effectName !== method && element[ effectName ] ) {
        element[ effectName ]( options.duration, options.easing, callback );
      } else {
        element.queue(function( next ) {
          $( this )[ method ]();
          if ( callback ) {
            callback.call( element[ 0 ] );
          }
          next();
        });
      }
    };
  });

  })( jQuery );

  (function( $, undefined ) {

  var rcapitals = /[A-Z]/g,
    replaceFunction = function( c ) {
      return "-" + c.toLowerCase();
    };

  $.extend( $.Widget.prototype, {
    _getCreateOptions: function() {
      var option, value,
        elem = this.element[ 0 ],
        options = {};

      //
      if ( !$.mobile.getAttribute( elem, "defaults" ) ) {
        for ( option in this.options ) {
          value = $.mobile.getAttribute( elem, option.replace( rcapitals, replaceFunction ) );

          if ( value != null ) {
            options[ option ] = value;
          }
        }
      }

      return options;
    }
  });

  //TODO: Remove in 1.5 for backcompat only
  $.mobile.widget = $.Widget;

  })( jQuery );

  (function( $, undefined ) {
  $.mobile.widgets = {};

  var originalWidget = $.widget,

    // Record the original, non-mobileinit-modified version of $.mobile.keepNative
    // so we can later determine whether someone has modified $.mobile.keepNative
    keepNativeFactoryDefault = $.mobile.keepNative;

  $.widget = (function( orig ) {
    return function() {
      var constructor = orig.apply( this, arguments ),
        name = constructor.prototype.widgetName;

      constructor.initSelector = ( ( constructor.prototype.initSelector !== undefined ) ?
        constructor.prototype.initSelector : ":jqmData(role='" + name + "')" );

      $.mobile.widgets[ name ] = constructor;

      return constructor;
    };
  })( $.widget );

  // Make sure $.widget still has bridge and extend methods
  $.extend( $.widget, originalWidget );

  // For backcompat remove in 1.5
  $.mobile.document.on( "create", function( event ) {
    $( event.target ).enhanceWithin();
  });

  $.widget( "mobile.page", {
    options: {
      theme: "a",
      domCache: false,

      // Deprecated in 1.4 remove in 1.5
      keepNativeDefault: $.mobile.keepNative,

      // Deprecated in 1.4 remove in 1.5
      contentTheme: null,
      enhanced: false
    },

    // DEPRECATED for > 1.4
    // TODO remove at 1.5
    _createWidget: function() {
      $.Widget.prototype._createWidget.apply( this, arguments );
      this._trigger( "init" );
    },

    _create: function() {
      // If false is returned by the callbacks do not create the page
      if ( this._trigger( "beforecreate" ) === false ) {
        return false;
      }

      if ( !this.options.enhanced ) {
        this._enhance();
      }

      this._on( this.element, {
        pagebeforehide: "removeContainerBackground",
        pagebeforeshow: "_handlePageBeforeShow"
      });

      this.element.enhanceWithin();
      // Dialog widget is deprecated in 1.4 remove this in 1.5
      if ( $.mobile.getAttribute( this.element[0], "role" ) === "dialog" && $.mobile.dialog ) {
        this.element.dialog();
      }
    },

    _enhance: function () {
      var attrPrefix = "data-" + $.mobile.ns,
        self = this;

      if ( this.options.role ) {
        this.element.attr( "data-" + $.mobile.ns + "role", this.options.role );
      }

      this.element
        .attr( "tabindex", "0" )
        .addClass( "ui-page ui-page-theme-" + this.options.theme );

      // Manipulation of content os Deprecated as of 1.4 remove in 1.5
      this.element.find( "[" + attrPrefix + "role='content']" ).each( function() {
        var $this = $( this ),
          theme = this.getAttribute( attrPrefix + "theme" ) || undefined;
          self.options.contentTheme = theme || self.options.contentTheme || ( self.options.dialog && self.options.theme ) || ( self.element.jqmData("role") === "dialog" &&  self.options.theme );
          $this.addClass( "ui-content" );
          if ( self.options.contentTheme ) {
            $this.addClass( "ui-body-" + ( self.options.contentTheme ) );
          }
          // Add ARIA role
          $this.attr( "role", "main" ).addClass( "ui-content" );
      });
    },

    bindRemove: function( callback ) {
      var page = this.element;

      // when dom caching is not enabled or the page is embedded bind to remove the page on hide
      if ( !page.data( "mobile-page" ).options.domCache &&
        page.is( ":jqmData(external-page='true')" ) ) {

        // TODO use _on - that is, sort out why it doesn't work in this case
        page.bind( "pagehide.remove", callback || function( e, data ) {

          //check if this is a same page transition and if so don't remove the page
          if( !data.samePage ){
            var $this = $( this ),
              prEvent = new $.Event( "pageremove" );

            $this.trigger( prEvent );

            if ( !prEvent.isDefaultPrevented() ) {
              $this.removeWithDependents();
            }
          }
        });
      }
    },

    _setOptions: function( o ) {

    },

    _handlePageBeforeShow: function(/* e */) {
      this.setContainerBackground();
    },
    // Deprecated in 1.4 remove in 1.5
    removeContainerBackground: function() {
      this.element.closest( ":mobile-pagecontainer" ).pagecontainer({ "theme": "none" });
    },
    // Deprecated in 1.4 remove in 1.5
    // set the page container background to the page theme
    setContainerBackground: function( theme ) {
      this.element.parent().pagecontainer( { "theme": theme || this.options.theme } );
    },
    // Deprecated in 1.4 remove in 1.5
    keepNativeSelector: function() {
      var options = this.options,
        keepNative = $.trim( options.keepNative || "" ),
        globalValue = $.trim( $.mobile.keepNative ),
        optionValue = $.trim( options.keepNativeDefault ),

        // Check if $.mobile.keepNative has changed from the factory default
        newDefault = ( keepNativeFactoryDefault === globalValue ?
          "" : globalValue ),

        // If $.mobile.keepNative has not changed, use options.keepNativeDefault
        oldDefault = ( newDefault === "" ? optionValue : "" );

      // Concatenate keepNative selectors from all sources where the value has
      // changed or, if nothing has changed, return the default
      return ( ( keepNative ? [ keepNative ] : [] )
        .concat( newDefault ? [ newDefault ] : [] )
        .concat( oldDefault ? [ oldDefault ] : [] )
        .join( ", " ) );
    }
  });
  })( jQuery );


  (function( $, window, undefined ) {

    // TODO remove direct references to $.mobile and properties, we should
    //      favor injection with params to the constructor
    $.mobile.Transition = function() {
      this.init.apply( this, arguments );
    };

    $.extend($.mobile.Transition.prototype, {
      toPreClass: " ui-page-pre-in",

      init: function( name, reverse, $to, $from ) {
        $.extend(this, {
          name: name,
          reverse: reverse,
          $to: $to,
          $from: $from,
          deferred: new $.Deferred()
        });
      },

      cleanFrom: function() {
        this.$from
          .removeClass( $.mobile.activePageClass + " out in reverse " + this.name )
          .height( "" );
      },

      // NOTE overridden by child object prototypes, noop'd here as defaults
      beforeDoneIn: function() {},
      beforeDoneOut: function() {},
      beforeStartOut: function() {},

      doneIn: function() {
        this.beforeDoneIn();

        this.$to.removeClass( "out in reverse " + this.name ).height( "" );

        this.toggleViewportClass();

        // In some browsers (iOS5), 3D transitions block the ability to scroll to the desired location during transition
        // This ensures we jump to that spot after the fact, if we aren't there already.
        if ( $.mobile.window.scrollTop() !== this.toScroll ) {
          this.scrollPage();
        }
        if ( !this.sequential ) {
          this.$to.addClass( $.mobile.activePageClass );
        }
        this.deferred.resolve( this.name, this.reverse, this.$to, this.$from, true );
      },

      doneOut: function( screenHeight, reverseClass, none, preventFocus ) {
        this.beforeDoneOut();
        this.startIn( screenHeight, reverseClass, none, preventFocus );
      },

      hideIn: function( callback ) {
        // Prevent flickering in phonegap container: see comments at #4024 regarding iOS
        this.$to.css( "z-index", -10 );
        callback.call( this );
        this.$to.css( "z-index", "" );
      },

      scrollPage: function() {
        // By using scrollTo instead of silentScroll, we can keep things better in order
        // Just to be precautios, disable scrollstart listening like silentScroll would
        $.event.special.scrollstart.enabled = false;
        //if we are hiding the url bar or the page was previously scrolled scroll to hide or return to position
        if ( $.mobile.hideUrlBar || this.toScroll !== $.mobile.defaultHomeScroll ) {
          window.scrollTo( 0, this.toScroll );
        }

        // reenable scrollstart listening like silentScroll would
        setTimeout( function() {
          $.event.special.scrollstart.enabled = true;
        }, 150 );
      },

      startIn: function( screenHeight, reverseClass, none, preventFocus ) {
        this.hideIn(function() {
          this.$to.addClass( $.mobile.activePageClass + this.toPreClass );

          // Send focus to page as it is now display: block
          if ( !preventFocus ) {
            $.mobile.focusPage( this.$to );
          }

          // Set to page height
          this.$to.height( screenHeight + this.toScroll );

                  if ( !none ) {
                      this.scrollPage();
                  }
        });

        this.$to
          .removeClass( this.toPreClass )
          .addClass( this.name + " in " + reverseClass );

        if ( !none ) {
          this.$to.animationComplete( $.proxy(function() {
            this.doneIn();
          }, this ));
        } else {
          this.doneIn();
        }

      },

      startOut: function( screenHeight, reverseClass, none ) {
        this.beforeStartOut( screenHeight, reverseClass, none );

        // Set the from page's height and start it transitioning out
        // Note: setting an explicit height helps eliminate tiling in the transitions
        this.$from
          .height( screenHeight + $.mobile.window.scrollTop() )
          .addClass( this.name + " out" + reverseClass );
      },

      toggleViewportClass: function() {
        $.mobile.pageContainer.toggleClass(this.name );
      },

      transition: function() {
        // NOTE many of these could be calculated/recorded in the constructor, it's my
        //      opinion that binding them as late as possible has value with regards to
        //      better transitions with fewer bugs. Ie, it's not guaranteed that the
        //      object will be created and transition will be run immediately after as
        //      it is today. So we wait until transition is invoked to gather the following
        var none,
          reverseClass = this.reverse ? " reverse" : "",
          screenHeight = $.mobile.getScreenHeight(),
          maxTransitionOverride = $.mobile.maxTransitionWidth !== false &&
            $.mobile.window.width() > $.mobile.maxTransitionWidth;

        this.toScroll = $.mobile.navigate.history.getActive().lastScroll || $.mobile.defaultHomeScroll;

        none = !$.support.cssTransitions || !$.support.cssAnimations ||
          maxTransitionOverride || !this.name || this.name === "none" ||
          Math.max( $.mobile.window.scrollTop(), this.toScroll ) >
            $.mobile.getMaxScrollForTransition();

        this.toggleViewportClass();

        if ( this.$from && !none ) {
          this.startOut( screenHeight, reverseClass, none );
        } else {
          this.doneOut( screenHeight, reverseClass, none, true );
        }

        return this.deferred.promise();
      }
    });
  })( jQuery, this );


  (function( $ ) {

    $.mobile.SerialTransition = function() {
      this.init.apply(this, arguments);
    };

    $.extend($.mobile.SerialTransition.prototype, $.mobile.Transition.prototype, {
      sequential: true,

      beforeDoneOut: function() {
        if ( this.$from ) {
          this.cleanFrom();
        }
      },

      beforeStartOut: function( screenHeight, reverseClass, none ) {
        this.$from.animationComplete($.proxy(function() {
          this.doneOut( screenHeight, reverseClass, none );
        }, this ));
      }
    });

  })( jQuery );


  (function( $ ) {

    $.mobile.ConcurrentTransition = function() {
      this.init.apply(this, arguments);
    };

    $.extend($.mobile.ConcurrentTransition.prototype, $.mobile.Transition.prototype, {
      sequential: false,

      beforeDoneIn: function() {
        if ( this.$from ) {
          this.cleanFrom();
        }
      },

      beforeStartOut: function( screenHeight, reverseClass, none ) {
        this.doneOut( screenHeight, reverseClass, none );
      }
    });

  })( jQuery );


  (function( $ ) {

    // generate the handlers from the above
    var defaultGetMaxScrollForTransition = function() {
      return $.mobile.getScreenHeight() * 3;
    };

    //transition handler dictionary for 3rd party transitions
    $.mobile.transitionHandlers = {
      "sequential": $.mobile.SerialTransition,
      "simultaneous": $.mobile.ConcurrentTransition
    };

    // Make our transition handler the public default.
    $.mobile.defaultTransitionHandler = $.mobile.transitionHandlers.sequential;

    $.mobile.transitionFallbacks = {};

    // If transition is defined, check if css 3D transforms are supported, and if not, if a fallback is specified
    $.mobile._maybeDegradeTransition = function( transition ) {
      if ( transition && !$.support.cssTransform3d && $.mobile.transitionFallbacks[ transition ] ) {
        transition = $.mobile.transitionFallbacks[ transition ];
      }

      return transition;
    };

    // Set the getMaxScrollForTransition to default if no implementation was set by user
    $.mobile.getMaxScrollForTransition = $.mobile.getMaxScrollForTransition || defaultGetMaxScrollForTransition;

  })( jQuery );

  (function( $, undefined ) {

    $.widget( "mobile.pagecontainer", {
      options: {
        theme: "a"
      },

      initSelector: false,

      _create: function() {
        this.setLastScrollEnabled = true;

        this._on( this.window, {
          // disable an scroll setting when a hashchange has been fired,
          // this only works because the recording of the scroll position
          // is delayed for 100ms after the browser might have changed the
          // position because of the hashchange
          navigate: "_disableRecordScroll",

          // bind to scrollstop for the first page, "pagechange" won't be
          // fired in that case
          scrollstop: "_delayedRecordScroll"
        });

        // TODO consider moving the navigation handler OUT of widget into
        //      some other object as glue between the navigate event and the
        //      content widget load and change methods
        this._on( this.window, { navigate: "_filterNavigateEvents" });

        // TODO move from page* events to content* events
        this._on({ pagechange: "_afterContentChange" });

        // handle initial hashchange from chrome :(
        this.window.one( "navigate", $.proxy(function() {
          this.setLastScrollEnabled = true;
        }, this));
      },

      _setOptions: function( options ) {
        this._super( options );
      },

      _disableRecordScroll: function() {
        this.setLastScrollEnabled = false;
      },

      _enableRecordScroll: function() {
        this.setLastScrollEnabled = true;
      },

      // TODO consider the name here, since it's purpose specific
      _afterContentChange: function() {
        // once the page has changed, re-enable the scroll recording
        this.setLastScrollEnabled = true;

        // remove any binding that previously existed on the get scroll
        // which may or may not be different than the scroll element
        // determined for this page previously
        this._off( this.window, "scrollstop" );

        // determine and bind to the current scoll element which may be the
        // window or in the case of touch overflow the element touch overflow
        this._on( this.window, { scrollstop: "_delayedRecordScroll" });
      },

      _recordScroll: function() {
        // this barrier prevents setting the scroll value based on
        // the browser scrolling the window based on a hashchange
        if ( !this.setLastScrollEnabled ) {
          return;
        }

        var active = this._getActiveHistory(),
          currentScroll, minScroll, defaultScroll;

        if ( active ) {
          currentScroll = this._getScroll();
          minScroll = this._getMinScroll();
          defaultScroll = this._getDefaultScroll();

          // Set active page's lastScroll prop. If the location we're
          // scrolling to is less than minScrollBack, let it go.
          active.lastScroll = currentScroll < minScroll ? defaultScroll : currentScroll;
        }
      },

      _delayedRecordScroll: function() {
        setTimeout( $.proxy(this, "_recordScroll"), 100 );
      },

      _getScroll: function() {
        return this.window.scrollTop();
      },

      _getMinScroll: function() {
        return $.mobile.minScrollBack;
      },

      _getDefaultScroll: function() {
        return $.mobile.defaultHomeScroll;
      },

      _filterNavigateEvents: function( e, data ) {
        var url;

        if ( e.originalEvent && e.originalEvent.isDefaultPrevented() ) {
          return;
        }

        url = e.originalEvent.type.indexOf( "hashchange" ) > -1 ? data.state.hash : data.state.url;

        if ( !url ) {
          url = this._getHash();
        }

        if ( !url || url === "#" || url.indexOf( "#" + $.mobile.path.uiStateKey ) === 0 ) {
          url = location.href;
        }

        this._handleNavigate( url, data.state );
      },

      _getHash: function() {
        return $.mobile.path.parseLocation().hash;
      },

      // TODO active page should be managed by the container (ie, it should be a property)
      getActivePage: function() {
        return this.activePage;
      },

      // TODO the first page should be a property set during _create using the logic
      //      that currently resides in init
      _getInitialContent: function() {
        return $.mobile.firstPage;
      },

      // TODO each content container should have a history object
      _getHistory: function() {
        return $.mobile.navigate.history;
      },

      // TODO use _getHistory
      _getActiveHistory: function() {
        return $.mobile.navigate.history.getActive();
      },

      // TODO the document base should be determined at creation
      _getDocumentBase: function() {
        return $.mobile.path.documentBase;
      },

      back: function() {
        this.go( -1 );
      },

      forward: function() {
        this.go( 1 );
      },

      go: function( steps ) {

        //if hashlistening is enabled use native history method
        if ( $.mobile.hashListeningEnabled ) {
          window.history.go( steps );
        } else {

          //we are not listening to the hash so handle history internally
          var activeIndex = $.mobile.navigate.history.activeIndex,
            index = activeIndex + parseInt( steps, 10 ),
            url = $.mobile.navigate.history.stack[ index ].url,
            direction = ( steps >= 1 )? "forward" : "back";

          //update the history object
          $.mobile.navigate.history.activeIndex = index;
          $.mobile.navigate.history.previousIndex = activeIndex;

          //change to the new page
          this.change( url, { direction: direction, changeHash: false, fromHashChange: true } );
        }
      },

      // TODO rename _handleDestination
      _handleDestination: function( to ) {
        var history;

        // clean the hash for comparison if it's a url
        if ( $.type(to) === "string" ) {
          to = $.mobile.path.stripHash( to );
        }

        if ( to ) {
          history = this._getHistory();

          // At this point, 'to' can be one of 3 things, a cached page
          // element from a history stack entry, an id, or site-relative /
          // absolute URL. If 'to' is an id, we need to resolve it against
          // the documentBase, not the location.href, since the hashchange
          // could've been the result of a forward/backward navigation
          // that crosses from an external page/dialog to an internal
          // page/dialog.
          //
          // TODO move check to history object or path object?
          to = !$.mobile.path.isPath( to ) ? ( $.mobile.path.makeUrlAbsolute( "#" + to, this._getDocumentBase() ) ) : to;

          // If we're about to go to an initial URL that contains a
          // reference to a non-existent internal page, go to the first
          // page instead. We know that the initial hash refers to a
          // non-existent page, because the initial hash did not end
          // up in the initial history entry
          // TODO move check to history object?
          if ( to === $.mobile.path.makeUrlAbsolute( "#" + history.initialDst, this._getDocumentBase() ) &&
            history.stack.length &&
            history.stack[0].url !== history.initialDst.replace( $.mobile.dialogHashKey, "" ) ) {
            to = this._getInitialContent();
          }
        }
        return to || this._getInitialContent();
      },

      _handleDialog: function( changePageOptions, data ) {
        var to, active, activeContent = this.getActivePage();

        // If current active page is not a dialog skip the dialog and continue
        // in the same direction
        if ( activeContent && !activeContent.hasClass( "ui-dialog" ) ) {
          // determine if we're heading forward or backward and continue
          // accordingly past the current dialog
          if ( data.direction === "back" ) {
            this.back();
          } else {
            this.forward();
          }

          // prevent changePage call
          return false;
        } else {
          // if the current active page is a dialog and we're navigating
          // to a dialog use the dialog objected saved in the stack
          to = data.pageUrl;
          active = this._getActiveHistory();

          // make sure to set the role, transition and reversal
          // as most of this is lost by the domCache cleaning
          $.extend( changePageOptions, {
            role: active.role,
            transition: active.transition,
            reverse: data.direction === "back"
          });
        }

        return to;
      },

      _handleNavigate: function( url, data ) {
        //find first page via hash
        // TODO stripping the hash twice with handleUrl
        var to = $.mobile.path.stripHash( url ), history = this._getHistory(),

          // transition is false if it's the first page, undefined
          // otherwise (and may be overridden by default)
          transition = history.stack.length === 0 ? "none" : undefined,

          // default options for the changPage calls made after examining
          // the current state of the page and the hash, NOTE that the
          // transition is derived from the previous history entry
          changePageOptions = {
            changeHash: false,
            fromHashChange: true,
            reverse: data.direction === "back"
          };

        $.extend( changePageOptions, data, {
          transition: ( history.getLast() || {} ).transition || transition
        });

        // TODO move to _handleDestination ?
        // If this isn't the first page, if the current url is a dialog hash
        // key, and the initial destination isn't equal to the current target
        // page, use the special dialog handling
        if ( history.activeIndex > 0 &&
          to.indexOf( $.mobile.dialogHashKey ) > -1 &&
          history.initialDst !== to ) {

          to = this._handleDialog( changePageOptions, data );

          if ( to === false ) {
            return;
          }
        }

        this._changeContent( this._handleDestination( to ), changePageOptions );
      },

      _changeContent: function( to, opts ) {
        $.mobile.changePage( to, opts );
      },

      _getBase: function() {
        return $.mobile.base;
      },

      _getNs: function() {
        return $.mobile.ns;
      },

      _enhance: function( content, role ) {
        // TODO consider supporting a custom callback, and passing in
        // the settings which includes the role
        return content.page({ role: role });
      },

      _include: function( page, settings ) {
        // append to page and enhance
        page.appendTo( this.element );

        // use the page widget to enhance
        this._enhance( page, settings.role );

        // remove page on hide
        page.page( "bindRemove" );
      },

      _find: function( absUrl ) {
        // TODO consider supporting a custom callback
        var fileUrl = this._createFileUrl( absUrl ),
          dataUrl = this._createDataUrl( absUrl ),
          page, initialContent = this._getInitialContent();

        // Check to see if the page already exists in the DOM.
        // NOTE do _not_ use the :jqmData pseudo selector because parenthesis
        //      are a valid url char and it breaks on the first occurence
        page = this.element
          .children( "[data-" + this._getNs() +"url='" + dataUrl + "']" );

        // If we failed to find the page, check to see if the url is a
        // reference to an embedded page. If so, it may have been dynamically
        // injected by a developer, in which case it would be lacking a
        // data-url attribute and in need of enhancement.
        if ( page.length === 0 && dataUrl && !$.mobile.path.isPath( dataUrl ) ) {
          page = this.element.children( $.mobile.path.hashToSelector("#" + dataUrl) )
            .attr( "data-" + this._getNs() + "url", dataUrl )
            .jqmData( "url", dataUrl );
        }

        // If we failed to find a page in the DOM, check the URL to see if it
        // refers to the first page in the application. Also check to make sure
        // our cached-first-page is actually in the DOM. Some user deployed
        // apps are pruning the first page from the DOM for various reasons.
        // We check for this case here because we don't want a first-page with
        // an id falling through to the non-existent embedded page error case.
        if ( page.length === 0 &&
          $.mobile.path.isFirstPageUrl( fileUrl ) &&
          initialContent &&
          initialContent.parent().length ) {
          page = $( initialContent );
        }

        return page;
      },

      _getLoader: function() {
        return $.mobile.loading();
      },

      _showLoading: function( delay, theme, msg, textonly ) {
        // This configurable timeout allows cached pages a brief
        // delay to load without showing a message
        if ( this._loadMsg ) {
          return;
        }

        this._loadMsg = setTimeout($.proxy(function() {
          this._getLoader().loader( "show", theme, msg, textonly );
          this._loadMsg = 0;
        }, this), delay );
      },

      _hideLoading: function() {
        // Stop message show timer
        clearTimeout( this._loadMsg );
        this._loadMsg = 0;

        // Hide loading message
        this._getLoader().loader( "hide" );
      },

      _showError: function() {
        // make sure to remove the current loading message
        this._hideLoading();

        // show the error message
        this._showLoading( 0, $.mobile.pageLoadErrorMessageTheme, $.mobile.pageLoadErrorMessage, true );

        // hide the error message after a delay
        // TODO configuration
        setTimeout( $.proxy(this, "_hideLoading"), 1500 );
      },

      _parse: function( html, fileUrl ) {
        // TODO consider allowing customization of this method. It's very JQM specific
        var page, all = $( "<div></div>" );

        //workaround to allow scripts to execute when included in page divs
        all.get( 0 ).innerHTML = html;

        page = all.find( ":jqmData(role='page'), :jqmData(role='dialog')" ).first();

        //if page elem couldn't be found, create one and insert the body element's contents
        if ( !page.length ) {
          page = $( "<div data-" + this._getNs() + "role='page'>" +
            ( html.split( /<\/?body[^>]*>/gmi )[1] || "" ) +
            "</div>" );
        }

        // TODO tagging a page with external to make sure that embedded pages aren't
        // removed by the various page handling code is bad. Having page handling code
        // in many places is bad. Solutions post 1.0
        page.attr( "data-" + this._getNs() + "url", $.mobile.path.convertUrlToDataUrl(fileUrl) )
          .attr( "data-" + this._getNs() + "external-page", true );

        return page;
      },

      _setLoadedTitle: function( page, html ) {
        //page title regexp
        var newPageTitle = html.match( /<title[^>]*>([^<]*)/ ) && RegExp.$1;

        if ( newPageTitle && !page.jqmData("title") ) {
          newPageTitle = $( "<div>" + newPageTitle + "</div>" ).text();
          page.jqmData( "title", newPageTitle );
        }
      },

      _isRewritableBaseTag: function() {
        return $.mobile.dynamicBaseEnabled && !$.support.dynamicBaseTag;
      },

      _createDataUrl: function( absoluteUrl ) {
        return $.mobile.path.convertUrlToDataUrl( absoluteUrl );
      },

      _createFileUrl: function( absoluteUrl ) {
        return $.mobile.path.getFilePath( absoluteUrl );
      },

      _triggerWithDeprecated: function( name, data, page ) {
        var deprecatedEvent = $.Event( "page" + name ),
          newEvent = $.Event( this.widgetName + name );

        // DEPRECATED
        // trigger the old deprecated event on the page if it's provided
        ( page || this.element ).trigger( deprecatedEvent, data );

        // use the widget trigger method for the new content* event
        this.element.trigger( newEvent, data );

        return {
          deprecatedEvent: deprecatedEvent,
          event: newEvent
        };
      },

      // TODO it would be nice to split this up more but everything appears to be "one off"
      //      or require ordering such that other bits are sprinkled in between parts that
      //      could be abstracted out as a group
      _loadSuccess: function( absUrl, triggerData, settings, deferred ) {
        var fileUrl = this._createFileUrl( absUrl ),
          dataUrl = this._createDataUrl( absUrl );

        return $.proxy(function( html, textStatus, xhr ) {
          //pre-parse html to check for a data-url,
          //use it as the new fileUrl, base path, etc
          var content,

            // TODO handle dialogs again
            pageElemRegex = new RegExp( "(<[^>]+\\bdata-" + this._getNs() + "role=[\"']?page[\"']?[^>]*>)" ),

            dataUrlRegex = new RegExp( "\\bdata-" + this._getNs() + "url=[\"']?([^\"'>]*)[\"']?" );

          // data-url must be provided for the base tag so resource requests
          // can be directed to the correct url. loading into a temprorary
          // element makes these requests immediately
          if ( pageElemRegex.test( html ) &&
            RegExp.$1 &&
            dataUrlRegex.test( RegExp.$1 ) &&
            RegExp.$1 ) {
            fileUrl = $.mobile.path.getFilePath( $("<div>" + RegExp.$1 + "</div>").text() );
          }

          //dont update the base tag if we are prefetching
          if ( settings.prefetch === undefined ) {
            this._getBase().set( fileUrl );
          }

          content = this._parse( html, fileUrl );

          this._setLoadedTitle( content, html );

          // Add the content reference and xhr to our triggerData.
          triggerData.xhr = xhr;
          triggerData.textStatus = textStatus;

          // DEPRECATED
          triggerData.page = content;

          triggerData.content = content;

          // If the default behavior is prevented, stop here!
          // Note that it is the responsibility of the listener/handler
          // that called preventDefault(), to resolve/reject the
          // deferred object within the triggerData.
          if ( !this._trigger( "load", undefined, triggerData ) ) {
            return;
          }

          // rewrite src and href attrs to use a base url if the base tag won't work
          if ( this._isRewritableBaseTag() && content ) {
            this._getBase().rewrite( fileUrl, content );
          }

          this._include( content, settings );

          // Enhancing the content may result in new dialogs/sub content being inserted
          // into the DOM. If the original absUrl refers to a sub-content, that is the
          // real content we are interested in.
          if ( absUrl.indexOf( "&" + $.mobile.subPageUrlKey ) > -1 ) {
            content = this.element.children( "[data-" + this._getNs() +"url='" + dataUrl + "']" );
          }

          // Remove loading message.
          if ( settings.showLoadMsg ) {
            this._hideLoading();
          }

          // BEGIN DEPRECATED ---------------------------------------------------
          // Let listeners know the content loaded successfully.
          this.element.trigger( "pageload" );
          // END DEPRECATED -----------------------------------------------------

          deferred.resolve( absUrl, settings, content );
        }, this);
      },

      _loadDefaults: {
        type: "get",
        data: undefined,

        // DEPRECATED
        reloadPage: false,

        reload: false,

        // By default we rely on the role defined by the @data-role attribute.
        role: undefined,

        showLoadMsg: false,

        // This delay allows loads that pull from browser cache to
        // occur without showing the loading message.
        loadMsgDelay: 50
      },

      load: function( url, options ) {
        // This function uses deferred notifications to let callers
        // know when the content is done loading, or if an error has occurred.
        var deferred = ( options && options.deferred ) || $.Deferred(),

          // The default load options with overrides specified by the caller.
          settings = $.extend( {}, this._loadDefaults, options ),

          // The DOM element for the content after it has been loaded.
          content = null,

          // The absolute version of the URL passed into the function. This
          // version of the URL may contain dialog/subcontent params in it.
          absUrl = $.mobile.path.makeUrlAbsolute( url, this._findBaseWithDefault() ),
          fileUrl, dataUrl, pblEvent, triggerData;

        // DEPRECATED reloadPage
        settings.reload = settings.reloadPage;

        // If the caller provided data, and we're using "get" request,
        // append the data to the URL.
        if ( settings.data && settings.type === "get" ) {
          absUrl = $.mobile.path.addSearchParams( absUrl, settings.data );
          settings.data = undefined;
        }

        // If the caller is using a "post" request, reload must be true
        if ( settings.data && settings.type === "post" ) {
          settings.reload = true;
        }

        // The absolute version of the URL minus any dialog/subcontent params.
        // In otherwords the real URL of the content to be loaded.
        fileUrl = this._createFileUrl( absUrl );

        // The version of the Url actually stored in the data-url attribute of
        // the content. For embedded content, it is just the id of the page. For
        // content within the same domain as the document base, it is the site
        // relative path. For cross-domain content (Phone Gap only) the entire
        // absolute Url is used to load the content.
        dataUrl = this._createDataUrl( absUrl );

        content = this._find( absUrl );

        // If it isn't a reference to the first content and refers to missing
        // embedded content reject the deferred and return
        if ( content.length === 0 &&
          $.mobile.path.isEmbeddedPage(fileUrl) &&
          !$.mobile.path.isFirstPageUrl(fileUrl) ) {
          deferred.reject( absUrl, settings );
          return;
        }

        // Reset base to the default document base
        // TODO figure out why we doe this
        this._getBase().reset();

        // If the content we are interested in is already in the DOM,
        // and the caller did not indicate that we should force a
        // reload of the file, we are done. Resolve the deferrred so that
        // users can bind to .done on the promise
        if ( content.length && !settings.reload ) {
          this._enhance( content, settings.role );
          deferred.resolve( absUrl, settings, content );

          //if we are reloading the content make sure we update
          // the base if its not a prefetch
          if ( !settings.prefetch ) {
            this._getBase().set(url);
          }

          return;
        }

        triggerData = {
          url: url,
          absUrl: absUrl,
          dataUrl: dataUrl,
          deferred: deferred,
          options: settings
        };

        // Let listeners know we're about to load content.
        pblEvent = this._triggerWithDeprecated( "beforeload", triggerData );

        // If the default behavior is prevented, stop here!
        if ( pblEvent.deprecatedEvent.isDefaultPrevented() ||
          pblEvent.event.isDefaultPrevented() ) {
          return;
        }

        if ( settings.showLoadMsg ) {
          this._showLoading( settings.loadMsgDelay );
        }

        // Reset base to the default document base.
        // only reset if we are not prefetching
        if ( settings.prefetch === undefined ) {
          this._getBase().reset();
        }

        if ( !( $.mobile.allowCrossDomainPages ||
          $.mobile.path.isSameDomain($.mobile.path.documentUrl, absUrl ) ) ) {
          deferred.reject( absUrl, settings );
          return;
        }

        // Load the new content.
        $.ajax({
          url: fileUrl,
          type: settings.type,
          data: settings.data,
          contentType: settings.contentType,
          dataType: "html",
          success: this._loadSuccess( absUrl, triggerData, settings, deferred ),
          error: this._loadError( absUrl, triggerData, settings, deferred )
        });
      },

      _loadError: function( absUrl, triggerData, settings, deferred ) {
        return $.proxy(function( xhr, textStatus, errorThrown ) {
          //set base back to current path
          this._getBase().set( $.mobile.path.get() );

          // Add error info to our triggerData.
          triggerData.xhr = xhr;
          triggerData.textStatus = textStatus;
          triggerData.errorThrown = errorThrown;

          // Let listeners know the page load failed.
          var plfEvent = this._triggerWithDeprecated( "loadfailed", triggerData );

          // If the default behavior is prevented, stop here!
          // Note that it is the responsibility of the listener/handler
          // that called preventDefault(), to resolve/reject the
          // deferred object within the triggerData.
          if ( plfEvent.deprecatedEvent.isDefaultPrevented() ||
            plfEvent.event.isDefaultPrevented() ) {
            return;
          }

          // Remove loading message.
          if ( settings.showLoadMsg ) {
            this._showError();
          }

          deferred.reject( absUrl, settings );
        }, this);
      },

      _getTransitionHandler: function( transition ) {
        transition = $.mobile._maybeDegradeTransition( transition );

        //find the transition handler for the specified transition. If there
        //isn't one in our transitionHandlers dictionary, use the default one.
        //call the handler immediately to kick-off the transition.
        return $.mobile.transitionHandlers[ transition ] || $.mobile.defaultTransitionHandler;
      },

      // TODO move into transition handlers?
      _triggerCssTransitionEvents: function( to, from, prefix ) {
        var samePage = false;

        prefix = prefix || "";

        // TODO decide if these events should in fact be triggered on the container
        if ( from ) {

          //Check if this is a same page transition and tell the handler in page
          if( to[0] === from[0] ){
            samePage = true;
          }

          //trigger before show/hide events
          // TODO deprecate nextPage in favor of next
          this._triggerWithDeprecated( prefix + "hide", { nextPage: to, samePage: samePage }, from );
        }

        // TODO deprecate prevPage in favor of previous
        this._triggerWithDeprecated( prefix + "show", { prevPage: from || $( "" ) }, to );
      },

      // TODO make private once change has been defined in the widget
      _cssTransition: function( to, from, options ) {
        var transition = options.transition,
          reverse = options.reverse,
          deferred = options.deferred,
          TransitionHandler,
          promise;

        this._triggerCssTransitionEvents( to, from, "before" );

        // TODO put this in a binding to events *outside* the widget
        this._hideLoading();

        TransitionHandler = this._getTransitionHandler( transition );

        promise = ( new TransitionHandler( transition, reverse, to, from ) ).transition();

        // TODO temporary accomodation of argument deferred
        promise.done(function() {
          deferred.resolve.apply( deferred, arguments );
        });

        promise.done($.proxy(function() {
          this._triggerCssTransitionEvents( to, from );
        }, this));
      },

      _releaseTransitionLock: function() {
        //release transition lock so navigation is free again
        isPageTransitioning = false;
        if ( pageTransitionQueue.length > 0 ) {
          $.mobile.changePage.apply( null, pageTransitionQueue.pop() );
        }
      },

      _removeActiveLinkClass: function( force ) {
        //clear out the active button state
        $.mobile.removeActiveLinkClass( force );
      },

      _loadUrl: function( to, triggerData, settings ) {
        // preserve the original target as the dataUrl value will be
        // simplified eg, removing ui-state, and removing query params
        // from the hash this is so that users who want to use query
        // params have access to them in the event bindings for the page
        // life cycle See issue #5085
        settings.target = to;
        settings.deferred = $.Deferred();

        this.load( to, settings );

        settings.deferred.done($.proxy(function( url, options, content ) {
          isPageTransitioning = false;

          // store the original absolute url so that it can be provided
          // to events in the triggerData of the subsequent changePage call
          options.absUrl = triggerData.absUrl;

          this.transition( content, triggerData, options );
        }, this));

        settings.deferred.fail($.proxy(function(/* url, options */) {
          this._removeActiveLinkClass( true );
          this._releaseTransitionLock();
          this._triggerWithDeprecated( "changefailed", triggerData );
        }, this));
      },

      _triggerPageBeforeChange: function( to, triggerData, settings ) {
        var pbcEvent = new $.Event( "pagebeforechange" );

        $.extend(triggerData, { toPage: to, options: settings });

        // NOTE: preserve the original target as the dataUrl value will be
        // simplified eg, removing ui-state, and removing query params from
        // the hash this is so that users who want to use query params have
        // access to them in the event bindings for the page life cycle
        // See issue #5085
        if ( $.type(to) === "string" ) {
          // if the toPage is a string simply convert it
          triggerData.absUrl = $.mobile.path.makeUrlAbsolute( to, this._findBaseWithDefault() );
        } else {
          // if the toPage is a jQuery object grab the absolute url stored
          // in the loadPage callback where it exists
          triggerData.absUrl = settings.absUrl;
        }

        // Let listeners know we're about to change the current page.
        this.element.trigger( pbcEvent, triggerData );

        // If the default behavior is prevented, stop here!
        if ( pbcEvent.isDefaultPrevented() ) {
          return false;
        }

        return true;
      },

      change: function( to, options ) {
        // If we are in the midst of a transition, queue the current request.
        // We'll call changePage() once we're done with the current transition
        // to service the request.
        if ( isPageTransitioning ) {
          pageTransitionQueue.unshift( arguments );
          return;
        }

        var settings = $.extend( {}, $.mobile.changePage.defaults, options ),
          triggerData = {};

        // Make sure we have a fromPage.
        settings.fromPage = settings.fromPage || this.activePage;

        // if the page beforechange default is prevented return early
        if ( !this._triggerPageBeforeChange(to, triggerData, settings) ) {
          return;
        }

        // We allow "pagebeforechange" observers to modify the to in
        // the trigger data to allow for redirects. Make sure our to is
        // updated. We also need to re-evaluate whether it is a string,
        // because an object can also be replaced by a string
        to = triggerData.toPage;

        // If the caller passed us a url, call loadPage()
        // to make sure it is loaded into the DOM. We'll listen
        // to the promise object it returns so we know when
        // it is done loading or if an error ocurred.
        if ( $.type(to) === "string" ) {
          // Set the isPageTransitioning flag to prevent any requests from
          // entering this method while we are in the midst of loading a page
          // or transitioning.
          isPageTransitioning = true;

          this._loadUrl( to, triggerData, settings );
        } else {
          this.transition( to, triggerData, settings );
        }
      },

      transition: function( toPage, triggerData, settings ) {
        var fromPage, url, pageUrl, fileUrl,
          active, activeIsInitialPage,
          historyDir, pageTitle, isDialog,
          alreadyThere, newPageTitle,
          params, cssTransitionDeferred,
          beforeTransition;

        // If we are in the midst of a transition, queue the current request.
        // We'll call changePage() once we're done with the current transition
        // to service the request.
        if ( isPageTransitioning ) {
          // make sure to only queue the to and settings values so the arguments
          // work with a call to the change method
          pageTransitionQueue.unshift( [toPage, settings] );
          return;
        }

        // DEPRECATED - this call only, in favor of the before transition
        // if the page beforechange default is prevented return early
        if ( !this._triggerPageBeforeChange(toPage, triggerData, settings) ) {
          return;
        }

        // if the (content|page)beforetransition default is prevented return early
        // Note, we have to check for both the deprecated and new events
        beforeTransition = this._triggerWithDeprecated( "beforetransition", triggerData );
        if (beforeTransition.deprecatedEvent.isDefaultPrevented() ||
          beforeTransition.event.isDefaultPrevented() ) {
          return;
        }

        // Set the isPageTransitioning flag to prevent any requests from
        // entering this method while we are in the midst of loading a page
        // or transitioning.
        isPageTransitioning = true;

        // If we are going to the first-page of the application, we need to make
        // sure settings.dataUrl is set to the application document url. This allows
        // us to avoid generating a document url with an id hash in the case where the
        // first-page of the document has an id attribute specified.
        if ( toPage[ 0 ] === $.mobile.firstPage[ 0 ] && !settings.dataUrl ) {
          settings.dataUrl = $.mobile.path.documentUrl.hrefNoHash;
        }

        // The caller passed us a real page DOM element. Update our
        // internal state and then trigger a transition to the page.
        fromPage = settings.fromPage;
        url = ( settings.dataUrl && $.mobile.path.convertUrlToDataUrl(settings.dataUrl) ) ||
          toPage.jqmData( "url" );

        // The pageUrl var is usually the same as url, except when url is obscured
        // as a dialog url. pageUrl always contains the file path
        pageUrl = url;
        fileUrl = $.mobile.path.getFilePath( url );
        active = $.mobile.navigate.history.getActive();
        activeIsInitialPage = $.mobile.navigate.history.activeIndex === 0;
        historyDir = 0;
        pageTitle = document.title;
        isDialog = ( settings.role === "dialog" ||
          toPage.jqmData( "role" ) === "dialog" ) &&
          toPage.jqmData( "dialog" ) !== true;

        // By default, we prevent changePage requests when the fromPage and toPage
        // are the same element, but folks that generate content
        // manually/dynamically and reuse pages want to be able to transition to
        // the same page. To allow this, they will need to change the default
        // value of allowSamePageTransition to true, *OR*, pass it in as an
        // option when they manually call changePage(). It should be noted that
        // our default transition animations assume that the formPage and toPage
        // are different elements, so they may behave unexpectedly. It is up to
        // the developer that turns on the allowSamePageTransitiona option to
        // either turn off transition animations, or make sure that an appropriate
        // animation transition is used.
        if ( fromPage && fromPage[0] === toPage[0] &&
          !settings.allowSamePageTransition ) {

          isPageTransitioning = false;
          this._triggerWithDeprecated( "transition", triggerData );
          this.element.trigger( "pagechange", triggerData );

          // Even if there is no page change to be done, we should keep the
          // urlHistory in sync with the hash changes
          if ( settings.fromHashChange ) {
            $.mobile.navigate.history.direct({ url: url });
          }

          return;
        }

        // We need to make sure the page we are given has already been enhanced.
        toPage.page({ role: settings.role });

        // If the changePage request was sent from a hashChange event, check to
        // see if the page is already within the urlHistory stack. If so, we'll
        // assume the user hit the forward/back button and will try to match the
        // transition accordingly.
        if ( settings.fromHashChange ) {
          historyDir = settings.direction === "back" ? -1 : 1;
        }

        // Kill the keyboard.
        // XXX_jblas: We need to stop crawling the entire document to kill focus.
        //            Instead, we should be tracking focus with a delegate()
        //            handler so we already have the element in hand at this
        //            point.
        // Wrap this in a try/catch block since IE9 throw "Unspecified error" if
        // document.activeElement is undefined when we are in an IFrame.
        try {
          if ( document.activeElement &&
            document.activeElement.nodeName.toLowerCase() !== "body" ) {

            $( document.activeElement ).blur();
          } else {
            $( "input:focus, textarea:focus, select:focus" ).blur();
          }
        } catch( e ) {}

        // Record whether we are at a place in history where a dialog used to be -
        // if so, do not add a new history entry and do not change the hash either
        alreadyThere = false;

        // If we're displaying the page as a dialog, we don't want the url
        // for the dialog content to be used in the hash. Instead, we want
        // to append the dialogHashKey to the url of the current page.
        if ( isDialog && active ) {
          // on the initial page load active.url is undefined and in that case
          // should be an empty string. Moving the undefined -> empty string back
          // into urlHistory.addNew seemed imprudent given undefined better
          // represents the url state

          // If we are at a place in history that once belonged to a dialog, reuse
          // this state without adding to urlHistory and without modifying the
          // hash. However, if a dialog is already displayed at this point, and
          // we're about to display another dialog, then we must add another hash
          // and history entry on top so that one may navigate back to the
          // original dialog
          if ( active.url &&
            active.url.indexOf( $.mobile.dialogHashKey ) > -1 &&
            this.activePage &&
            !this.activePage.hasClass( "ui-dialog" ) &&
            $.mobile.navigate.history.activeIndex > 0 ) {

            settings.changeHash = false;
            alreadyThere = true;
          }

          // Normally, we tack on a dialog hash key, but if this is the location
          // of a stale dialog, we reuse the URL from the entry
          url = ( active.url || "" );

          // account for absolute urls instead of just relative urls use as hashes
          if ( !alreadyThere && url.indexOf("#") > -1 ) {
            url += $.mobile.dialogHashKey;
          } else {
            url += "#" + $.mobile.dialogHashKey;
          }

          // tack on another dialogHashKey if this is the same as the initial hash
          // this makes sure that a history entry is created for this dialog
          if ( $.mobile.navigate.history.activeIndex === 0 && url === $.mobile.navigate.history.initialDst ) {
            url += $.mobile.dialogHashKey;
          }
        }

        // if title element wasn't found, try the page div data attr too
        // If this is a deep-link or a reload ( active === undefined ) then just
        // use pageTitle
        newPageTitle = ( !active ) ? pageTitle : toPage.jqmData( "title" ) ||
          toPage.children( ":jqmData(role='header')" ).find( ".ui-title" ).text();
        if ( !!newPageTitle && pageTitle === document.title ) {
          pageTitle = newPageTitle;
        }
        if ( !toPage.jqmData( "title" ) ) {
          toPage.jqmData( "title", pageTitle );
        }

        // Make sure we have a transition defined.
        settings.transition = settings.transition ||
          ( ( historyDir && !activeIsInitialPage ) ? active.transition : undefined ) ||
          ( isDialog ? $.mobile.defaultDialogTransition : $.mobile.defaultPageTransition );

        //add page to history stack if it's not back or forward
        if ( !historyDir && alreadyThere ) {
          $.mobile.navigate.history.getActive().pageUrl = pageUrl;
        }

        // Set the location hash.
        if ( url && !settings.fromHashChange ) {

          // rebuilding the hash here since we loose it earlier on
          // TODO preserve the originally passed in path
          if ( !$.mobile.path.isPath( url ) && url.indexOf( "#" ) < 0 ) {
            url = "#" + url;
          }

          // TODO the property names here are just silly
          params = {
            transition: settings.transition,
            title: pageTitle,
            pageUrl: pageUrl,
            role: settings.role
          };

          if ( settings.changeHash !== false && $.mobile.hashListeningEnabled ) {
            $.mobile.navigate( url, params, true);
          } else if ( toPage[ 0 ] !== $.mobile.firstPage[ 0 ] ) {
            $.mobile.navigate.history.add( url, params );
          }
        }

        //set page title
        document.title = pageTitle;

        //set "toPage" as activePage deprecated in 1.4 remove in 1.5
        $.mobile.activePage = toPage;

        //new way to handle activePage
        this.activePage = toPage;

        // If we're navigating back in the URL history, set reverse accordingly.
        settings.reverse = settings.reverse || historyDir < 0;

        cssTransitionDeferred = $.Deferred();

        this._cssTransition(toPage, fromPage, {
          transition: settings.transition,
          reverse: settings.reverse,
          deferred: cssTransitionDeferred
        });

        cssTransitionDeferred.done($.proxy(function( name, reverse, $to, $from, alreadyFocused ) {
          $.mobile.removeActiveLinkClass();

          //if there's a duplicateCachedPage, remove it from the DOM now that it's hidden
          if ( settings.duplicateCachedPage ) {
            settings.duplicateCachedPage.remove();
          }

          // despite visibility: hidden addresses issue #2965
          // https://github.com/jquery/jquery-mobile/issues/2965
          if ( !alreadyFocused ) {
            $.mobile.focusPage( toPage );
          }

          this._releaseTransitionLock();
          this.element.trigger( "pagechange", triggerData );
          this._triggerWithDeprecated( "transition", triggerData );
        }, this));
      },

      // determine the current base url
      _findBaseWithDefault: function() {
        var closestBase = ( this.activePage &&
        $.mobile.getClosestBaseUrl( this.activePage ) );
      return closestBase || $.mobile.path.documentBase.hrefNoHash;
      }
    });

    // The following handlers should be bound after mobileinit has been triggered
    // the following deferred is resolved in the init file
    $.mobile.navreadyDeferred = $.Deferred();

    //these variables make all page containers use the same queue and only navigate one at a time
    // queue to hold simultanious page transitions
    var pageTransitionQueue = [],

      // indicates whether or not page is in process of transitioning
      isPageTransitioning = false;

  })( jQuery );

  (function( $, undefined ) {

      // resolved on domready
    var domreadyDeferred = $.Deferred(),

      // resolved and nulled on window.load()
      loadDeferred = $.Deferred(),
      documentUrl = $.mobile.path.documentUrl,

      // used to track last vclicked element to make sure its value is added to form data
      $lastVClicked = null;

    /* Event Bindings - hashchange, submit, and click */
    function findClosestLink( ele ) {
      while ( ele ) {
        // Look for the closest element with a nodeName of "a".
        // Note that we are checking if we have a valid nodeName
        // before attempting to access it. This is because the
        // node we get called with could have originated from within
        // an embedded SVG document where some symbol instance elements
        // don't have nodeName defined on them, or strings are of type
        // SVGAnimatedString.
        if ( ( typeof ele.nodeName === "string" ) && ele.nodeName.toLowerCase() === "a" ) {
          break;
        }
        ele = ele.parentNode;
      }
      return ele;
    }

    $.mobile.loadPage = function( url, opts ) {
      var container;

      opts = opts || {};
      container = ( opts.pageContainer || $.mobile.pageContainer );

      // create the deferred that will be supplied to loadPage callers
      // and resolved by the content widget's load method
      opts.deferred = $.Deferred();

      // Preferring to allow exceptions for uninitialized opts.pageContainer
      // widgets so we know if we need to force init here for users
      container.pagecontainer( "load", url, opts );

      // provide the deferred
      return opts.deferred.promise();
    };

    //define vars for interal use

    /* internal utility functions */

    // NOTE Issue #4950 Android phonegap doesn't navigate back properly
    //      when a full page refresh has taken place. It appears that hashchange
    //      and replacestate history alterations work fine but we need to support
    //      both forms of history traversal in our code that uses backward history
    //      movement
    $.mobile.back = function() {
      var nav = window.navigator;

      // if the setting is on and the navigator object is
      // available use the phonegap navigation capability
      if ( this.phonegapNavigationEnabled &&
        nav &&
        nav.app &&
        nav.app.backHistory ) {
        nav.app.backHistory();
      } else {
        $.mobile.pageContainer.pagecontainer( "back" );
      }
    };

    // Direct focus to the page title, or otherwise first focusable element
    $.mobile.focusPage = function ( page ) {
      var autofocus = page.find( "[autofocus]" ),
        pageTitle = page.find( ".ui-title:eq(0)" );

      if ( autofocus.length ) {
        autofocus.focus();
        return;
      }

      if ( pageTitle.length ) {
        pageTitle.focus();
      } else{
        page.focus();
      }
    };

    // No-op implementation of transition degradation
    $.mobile._maybeDegradeTransition = $.mobile._maybeDegradeTransition || function( transition ) {
      return transition;
    };

    // Exposed $.mobile methods

    $.mobile.changePage = function( to, options ) {
      $.mobile.pageContainer.pagecontainer( "change", to, options );
    };

    $.mobile.changePage.defaults = {
      transition: undefined,
      reverse: false,
      changeHash: false,
      fromHashChange: false,
      role: undefined, // By default we rely on the role defined by the @data-role attribute.
      duplicateCachedPage: undefined,
      pageContainer: undefined,
      showLoadMsg: false, //loading message shows by default when pages are being fetched during changePage
      dataUrl: undefined,
      fromPage: undefined,
      allowSamePageTransition: false
    };

    $.mobile._registerInternalEvents = function() {
      var getAjaxFormData = function( $form, calculateOnly ) {
        var url, ret = true, formData, vclickedName, method;
        if ( !$.mobile.ajaxEnabled ||
            // test that the form is, itself, ajax false
            $form.is( ":jqmData(ajax='false')" ) ||
            // test that $.mobile.ignoreContentEnabled is set and
            // the form or one of it's parents is ajax=false
            !$form.jqmHijackable().length ||
            $form.attr( "target" ) ) {
          return false;
        }

        url = ( $lastVClicked && $lastVClicked.attr( "formaction" ) ) ||
          $form.attr( "action" );
        method = ( $form.attr( "method" ) || "get" ).toLowerCase();

        // If no action is specified, browsers default to using the
        // URL of the document containing the form. Since we dynamically
        // pull in pages from external documents, the form should submit
        // to the URL for the source document of the page containing
        // the form.
        if ( !url ) {
          // Get the @data-url for the page containing the form.
          url = $.mobile.getClosestBaseUrl( $form );

          // NOTE: If the method is "get", we need to strip off the query string
          // because it will get replaced with the new form data. See issue #5710.
          if ( method === "get" ) {
            url = $.mobile.path.parseUrl( url ).hrefNoSearch;
          }

          if ( url === $.mobile.path.documentBase.hrefNoHash ) {
            // The url we got back matches the document base,
            // which means the page must be an internal/embedded page,
            // so default to using the actual document url as a browser
            // would.
            url = documentUrl.hrefNoSearch;
          }
        }

        url = $.mobile.path.makeUrlAbsolute(  url, $.mobile.getClosestBaseUrl( $form ) );

        if ( ( $.mobile.path.isExternal( url ) && !$.mobile.path.isPermittedCrossDomainRequest( documentUrl, url ) ) ) {
          return false;
        }

        if ( !calculateOnly ) {
          formData = $form.serializeArray();

          if ( $lastVClicked && $lastVClicked[ 0 ].form === $form[ 0 ] ) {
            vclickedName = $lastVClicked.attr( "name" );
            if ( vclickedName ) {
              // Make sure the last clicked element is included in the form
              $.each( formData, function( key, value ) {
                if ( value.name === vclickedName ) {
                  // Unset vclickedName - we've found it in the serialized data already
                  vclickedName = "";
                  return false;
                }
              });
              if ( vclickedName ) {
                formData.push( { name: vclickedName, value: $lastVClicked.attr( "value" ) } );
              }
            }
          }

          ret = {
            url: url,
            options: {
              type:   method,
              data:   $.param( formData ),
              transition: $form.jqmData( "transition" ),
              reverse:  $form.jqmData( "direction" ) === "reverse",
              reloadPage: true
            }
          };
        }

        return ret;
      };

      //bind to form submit events, handle with Ajax
      $.mobile.document.delegate( "form", "submit", function( event ) {
        var formData;

        if ( !event.isDefaultPrevented() ) {
          formData = getAjaxFormData( $( this ) );
          if ( formData ) {
            $.mobile.changePage( formData.url, formData.options );
         
          }
        }
      });

      //add active state on vclick
      $.mobile.document.bind( "vclick", function( event ) {
        var $btn, btnEls, target = event.target, needClosest = false;
        // if this isn't a left click we don't care. Its important to note
        // that when the virtual event is generated it will create the which attr
        if ( event.which > 1 || !$.mobile.linkBindingEnabled ) {
          return;
        }

        // Record that this element was clicked, in case we need it for correct
        // form submission during the "submit" handler above
        $lastVClicked = $( target );

        // Try to find a target element to which the active class will be applied
        if ( $.data( target, "mobile-button" ) ) {
          // If the form will not be submitted via AJAX, do not add active class
          if ( !getAjaxFormData( $( target ).closest( "form" ), true ) ) {
            return;
          }
          // We will apply the active state to this button widget - the parent
          // of the input that was clicked will have the associated data
          if ( target.parentNode ) {
            target = target.parentNode;
          }
        } else {
          target = findClosestLink( target );
          if ( !( target && $.mobile.path.parseUrl( target.getAttribute( "href" ) || "#" ).hash !== "#" ) ) {
            return;
          }

          // TODO teach $.mobile.hijackable to operate on raw dom elements so the
          // link wrapping can be avoided
          if ( !$( target ).jqmHijackable().length ) {
            return;
          }
        }

        // Avoid calling .closest by using the data set during .buttonMarkup()
        // List items have the button data in the parent of the element clicked
        if ( !!~target.className.indexOf( "ui-link-inherit" ) ) {
          if ( target.parentNode ) {
            btnEls = $.data( target.parentNode, "buttonElements" );
          }
        // Otherwise, look for the data on the target itself
        } else {
          btnEls = $.data( target, "buttonElements" );
        }
        // If found, grab the button's outer element
        if ( btnEls ) {
          target = btnEls.outer;
        } else {
          needClosest = true;
        }

        $btn = $( target );
        // If the outer element wasn't found by the our heuristics, use .closest()
        if ( needClosest ) {
          $btn = $btn.closest( ".ui-btn" );
        }

        if ( $btn.length > 0 &&
          !( $btn.hasClass( "ui-state-disabled" ||

            // DEPRECATED as of 1.4.0 - remove after 1.4.0 release
            // only ui-state-disabled should be present thereafter
            $btn.hasClass( "ui-disabled" ) ) ) ) {
          $.mobile.removeActiveLinkClass( true );
          $.mobile.activeClickedLink = $btn;
          $.mobile.activeClickedLink.addClass( $.mobile.activeBtnClass );
        }
      });

      // click routing - direct to HTTP or Ajax, accordingly
      $.mobile.document.bind( "click", function( event ) {
        if ( !$.mobile.linkBindingEnabled || event.isDefaultPrevented() ) {
          return;
        }

        var link = findClosestLink( event.target ),
          $link = $( link ),

          //remove active link class if external (then it won't be there if you come back)
          httpCleanup = function() {
            window.setTimeout(function() { $.mobile.removeActiveLinkClass( true ); }, 200 );
          },
          baseUrl, href,
          useDefaultUrlHandling, isExternal,
          transition, reverse, role;

        // If a button was clicked, clean up the active class added by vclick above
        if ( $.mobile.activeClickedLink &&
          $.mobile.activeClickedLink[ 0 ] === event.target.parentNode ) {
          httpCleanup();
        }

        // If there is no link associated with the click or its not a left
        // click we want to ignore the click
        // TODO teach $.mobile.hijackable to operate on raw dom elements so the link wrapping
        // can be avoided
        if ( !link || event.which > 1 || !$link.jqmHijackable().length ) {
          return;
        }

        //if there's a data-rel=back attr, go back in history
        if ( $link.is( ":jqmData(rel='back')" ) ) {
          $.mobile.back();
          return false;
        }

        baseUrl = $.mobile.getClosestBaseUrl( $link );

        //get href, if defined, otherwise default to empty hash
        href = $.mobile.path.makeUrlAbsolute( $link.attr( "href" ) || "#", baseUrl );

        //if ajax is disabled, exit early
        if ( !$.mobile.ajaxEnabled && !$.mobile.path.isEmbeddedPage( href ) ) {
          httpCleanup();
          //use default click handling
          return;
        }

        // XXX_jblas: Ideally links to application pages should be specified as
        //            an url to the application document with a hash that is either
        //            the site relative path or id to the page. But some of the
        //            internal code that dynamically generates sub-pages for nested
        //            lists and select dialogs, just write a hash in the link they
        //            create. This means the actual URL path is based on whatever
        //            the current value of the base tag is at the time this code
        //            is called. For now we are just assuming that any url with a
        //            hash in it is an application page reference.
        if ( href.search( "#" ) !== -1 ) {
          href = href.replace( /[^#]*#/, "" );
          if ( !href ) {
            //link was an empty hash meant purely
            //for interaction, so we ignore it.
    
            return;
          } else if ( $.mobile.path.isPath( href ) ) {
            //we have apath so make it the href we want to load.
            href = $.mobile.path.makeUrlAbsolute( href, baseUrl );
          } else {
            //we have a simple id so use the documentUrl as its base.
            href = $.mobile.path.makeUrlAbsolute( "#" + href, documentUrl.hrefNoHash );
          }
        }

        // Should we handle this link, or let the browser deal with it?
        useDefaultUrlHandling = $link.is( "[rel='external']" ) || $link.is( ":jqmData(ajax='false')" ) || $link.is( "[target]" );

        // Some embedded browsers, like the web view in Phone Gap, allow cross-domain XHR
        // requests if the document doing the request was loaded via the file:// protocol.
        // This is usually to allow the application to "phone home" and fetch app specific
        // data. We normally let the browser handle external/cross-domain urls, but if the
        // allowCrossDomainPages option is true, we will allow cross-domain http/https
        // requests to go through our page loading logic.

        //check for protocol or rel and its not an embedded page
        //TODO overlap in logic from isExternal, rel=external check should be
        //     moved into more comprehensive isExternalLink
        isExternal = useDefaultUrlHandling || ( $.mobile.path.isExternal( href ) && !$.mobile.path.isPermittedCrossDomainRequest( documentUrl, href ) );

        if ( isExternal ) {
          httpCleanup();
          //use default click handling
          return;
        }

        //use ajax
        transition = $link.jqmData( "transition" );
        reverse = $link.jqmData( "direction" ) === "reverse" ||
              // deprecated - remove by 1.0
              $link.jqmData( "back" );

        //this may need to be more specific as we use data-rel more
        role = $link.attr( "data-" + $.mobile.ns + "rel" ) || undefined;

        $.mobile.changePage( href, { transition: transition, reverse: reverse, role: role, link: $link } );
      
      });

      //prefetch pages when anchors with data-prefetch are encountered


      // TODO ensure that the navigate binding in the content widget happens at the right time
      $.mobile.pageContainer.pagecontainer();

      //set page min-heights to be device specific
      $.mobile.document.bind( "pageshow", function() {

        // We need to wait for window.load to make sure that styles have already been rendered,
        // otherwise heights of external toolbars will have the wrong value
        if ( loadDeferred ) {
          loadDeferred.done( $.mobile.resetActivePageHeight );
        } else {
          $.mobile.resetActivePageHeight();
        }
      });
      $.mobile.window.bind( "throttledresize", $.mobile.resetActivePageHeight );

    };//navreadyDeferred done callback

    $( function() { domreadyDeferred.resolve(); } );

    $.mobile.window.load( function() {

      // Resolve and null the deferred
      loadDeferred.resolve();
      loadDeferred = null;
    });

    $.when( domreadyDeferred, $.mobile.navreadyDeferred ).done( function() { $.mobile._registerInternalEvents(); } );
  })( jQuery );


  (function( $ ) {
    // TODO move loader class down into the widget settings
    var loaderClass = "ui-loader", $html = $( "html" );

    $.widget( "mobile.loader", {
      // NOTE if the global config settings are defined they will override these
      //      options
      options: {
        // the theme for the loading message
        theme: "a",

        // whether the text in the loading message is shown
        textVisible: false,

        // custom html for the inner content of the loading message
        html: "",

        // the text to be displayed when the popup is shown
        text: "loading"
      },

      defaultHtml: "",

      // For non-fixed supportin browsers. Position at y center (if scrollTop supported), above the activeBtn (if defined), or just 100px from top
      fakeFixLoader: function() {
        var activeBtn = $( "." + $.mobile.activeBtnClass ).first();

        this.element
          .css({
            top: $.support.scrollTop && this.window.scrollTop() + this.window.height() / 2 ||
              activeBtn.length && activeBtn.offset().top || 100
          });
      },

      // check position of loader to see if it appears to be "fixed" to center
      // if not, use abs positioning
      checkLoaderPosition: function() {
        var offset = this.element.offset(),
          scrollTop = this.window.scrollTop(),
          screenHeight = $.mobile.getScreenHeight();

        if ( offset.top < scrollTop || ( offset.top - scrollTop ) > screenHeight ) {
          this.element.addClass( "ui-loader-fakefix" );
          this.fakeFixLoader();
          this.window
            .unbind( "scroll", this.checkLoaderPosition )
            .bind( "scroll", $.proxy( this.fakeFixLoader, this ) );
        }
      },

      resetHtml: function() {
        this.element.html( $( this.defaultHtml ).html() );
      },

      // Turn on/off page loading message. Theme doubles as an object argument
      // with the following shape: { theme: '', text: '', html: '', textVisible: '' }
      // NOTE that the $.mobile.loading* settings and params past the first are deprecated
      // TODO sweet jesus we need to break some of this out
      show: function( theme, msgText, textonly ) {
        var textVisible, message, loadSettings;

        this.resetHtml();

        // use the prototype options so that people can set them globally at
        // mobile init. Consistency, it's what's for dinner
        if ( $.type( theme ) === "object" ) {
          loadSettings = $.extend( {}, this.options, theme );

          theme = loadSettings.theme;
        } else {
          loadSettings = this.options;

          // here we prefer the theme value passed as a string argument, then
          // we prefer the global option because we can't use undefined default
          // prototype options, then the prototype option
          theme = theme || loadSettings.theme;
        }

        // set the message text, prefer the param, then the settings object
        // then loading message
        message = msgText || ( loadSettings.text === false ? "" : loadSettings.text );

        // prepare the dom
        $html.addClass( "ui-loading" );

        textVisible = loadSettings.textVisible;

        // add the proper css given the options (theme, text, etc)
        // Force text visibility if the second argument was supplied, or
        // if the text was explicitly set in the object args
        this.element.attr("class", loaderClass +
          " ui-corner-all ui-body-" + theme +
          " ui-loader-" + ( textVisible || msgText || theme.text ? "verbose" : "default" ) +
          ( loadSettings.textonly || textonly ? " ui-loader-textonly" : "" ) );

        // TODO verify that jquery.fn.html is ok to use in both cases here
        //      this might be overly defensive in preventing unknowing xss
        // if the html attribute is defined on the loading settings, use that
        // otherwise use the fallbacks from above
        if ( loadSettings.html ) {
          this.element.html( loadSettings.html );
        } else {
          this.element.find( "h1" ).text( message );
        }

        // attach the loader to the DOM
        this.element.appendTo( $.mobile.pageContainer );

        // check that the loader is visible
        this.checkLoaderPosition();

        // on scroll check the loader position
        this.window.bind( "scroll", $.proxy( this.checkLoaderPosition, this ) );
      },

      hide: function() {
        $html.removeClass( "ui-loading" );

        if ( this.options.text ) {
          this.element.removeClass( "ui-loader-fakefix" );
        }

        $.mobile.window.unbind( "scroll", this.fakeFixLoader );
        $.mobile.window.unbind( "scroll", this.checkLoaderPosition );
      }
    });

  })(jQuery, this);


  (function( $, window, undefined ) {
    var $html = $( "html" ),
      $window = $.mobile.window;

    //remove initial build class (only present on first pageshow)
    function hideRenderingClass() {
      $html.removeClass( "" );
    }

    // trigger mobileinit event - useful hook for configuring $.mobile settings before they're used
    $( window.document ).trigger( "mobileinit" );

    // support conditions
    // if device support condition(s) aren't met, leave things as they are -> a basic, usable experience,
    // otherwise, proceed with the enhancements
    if ( !$.mobile.gradeA() ) {
      return;
    }

    // override ajaxEnabled on platforms that have known conflicts with hash history updates
    // or generally work better browsing in regular http for full page refreshes (BB5, Opera Mini)
    if ( $.mobile.ajaxBlacklist ) {
      $.mobile.ajaxEnabled = false;
    }

    // Add mobile, initial load "rendering" classes to docEl
    $html.addClass( "" );

    // This is a fallback. If anything goes wrong (JS errors, etc), or events don't fire,
    // this ensures the rendering class is removed after 5 seconds, so content is visible and accessible
    setTimeout( hideRenderingClass, 5000 );

    $.extend( $.mobile, {
      // find and enhance the pages in the dom and transition to the first page.
      initializePage: function() {
        // find present pages
        var path = $.mobile.path,
          $pages = $( ":jqmData(role='page'), :jqmData(role='dialog')" ),
          hash = path.stripHash( path.stripQueryParams(path.parseLocation().hash) ),
          hashPage = document.getElementById( hash );

        // if no pages are found, create one with body's inner html
        if ( !$pages.length ) {
          $pages = $( "body" ).wrapInner( "<div data-" + $.mobile.ns + "role='page'></div>" ).children( 0 );
        }

        // add dialogs, set data-url attrs
        $pages.each(function() {
          var $this = $( this );

          // unless the data url is already set set it to the pathname
          if ( !$this[ 0 ].getAttribute( "data-" + $.mobile.ns + "url" ) ) {
            $this.attr( "data-" + $.mobile.ns + "url", $this.attr( "id" ) || location.pathname + location.search );
          }
        });

        // define first page in dom case one backs out to the directory root (not always the first page visited, but defined as fallback)
        $.mobile.firstPage = $pages.first();

        // define page container
        $.mobile.pageContainer = $.mobile.firstPage
          .parent()
          .addClass( "" )
          .pagecontainer();

        // initialize navigation events now, after mobileinit has occurred and the page container
        // has been created but before the rest of the library is alerted to that fact
        $.mobile.navreadyDeferred.resolve();

        // alert listeners that the pagecontainer has been determined for binding
        // to events triggered on it
        $window.trigger( "pagecontainercreate" );

        // cue page loading message
        $.mobile.loading( "show" );

        //remove initial build class (only present on first pageshow)
        hideRenderingClass();

        // if hashchange listening is disabled, there's no hash deeplink,
        // the hash is not valid (contains more than one # or does not start with #)
        // or there is no page with that hash, change to the first page in the DOM
        // Remember, however, that the hash can also be a path!
        if ( ! ( $.mobile.hashListeningEnabled &&
          $.mobile.path.isHashValid( location.hash ) &&
          ( $( hashPage ).is( ":jqmData(role='page')" ) ||
            $.mobile.path.isPath( hash ) ||
            hash === $.mobile.dialogHashKey ) ) ) {

          // Store the initial destination
          if ( $.mobile.path.isHashValid( location.hash ) ) {
            $.mobile.navigate.history.initialDst = hash.replace( "#", "" );
          }

          // make sure to set initial popstate state if it exists
          // so that navigation back to the initial page works properly
          if ( $.event.special.navigate.isPushStateEnabled() ) {
            $.mobile.navigate.navigator.squash( path.parseLocation().href );
          }

          $.mobile.changePage( $.mobile.firstPage, {
            transition: "none",
            reverse: false,
            changeHash: false,
            fromHashChange: false
          });
        } else {
          // trigger hashchange or navigate to squash and record the correct
          // history entry for an initial hash path
          if ( !$.event.special.navigate.isPushStateEnabled() ) {
            $window.trigger( "hashchange", [true] );
          } else {
            // TODO figure out how to simplify this interaction with the initial history entry
            // at the bottom js/navigate/navigate.js
            $.mobile.navigate.history.stack = [];
            $.mobile.navigate( $.mobile.path.isPath( location.hash ) ? location.hash : location.href );
          }
        }
      }
    });

    $(function() {
      //Run inlineSVG support test
      $.support.inlineSVG();

      // check which scrollTop value should be used by scrolling to 1 immediately at domready
      // then check what the scroll top is. Android will report 0... others 1
      // note that this initial scroll won't hide the address bar. It's just for the check.

      // hide iOS browser chrome on load if hideUrlBar is true this is to try and do it as soon as possible
      if ( $.mobile.hideUrlBar ) {
        window.scrollTo( 0, 1 );
      }

      // if defaultHomeScroll hasn't been set yet, see if scrollTop is 1
      // it should be 1 in most browsers, but android treats 1 as 0 (for hiding addr bar)
      // so if it's 1, use 0 from now on
      $.mobile.defaultHomeScroll = ( !$.support.scrollTop || $.mobile.window.scrollTop() === 1 ) ? 0 : 1;

      //dom-ready inits
      if ( $.mobile.autoInitializePage ) {
        $.mobile.initializePage();
      }

      // window load event
      // hide iOS browser chrome on load if hideUrlBar is true this is as fall back incase we were too early before
      if ( $.mobile.hideUrlBar ) {
        $window.load( $.mobile.silentScroll );
      }

      if ( !$.support.cssPointerEvents ) {
        // IE and Opera don't support CSS pointer-events: none that we use to disable link-based buttons
        // by adding the 'ui-disabled' class to them. Using a JavaScript workaround for those browser.
        // https://github.com/jquery/jquery-mobile/issues/3558

        // DEPRECATED as of 1.4.0 - remove ui-disabled after 1.4.0 release
        // only ui-state-disabled should be present thereafter
        $.mobile.document.delegate( ".ui-state-disabled,.ui-disabled", "vclick",
          function( e ) {
            e.preventDefault();
            e.stopImmediatePropagation();
          }
        );
      }
    });
  }( jQuery, this ));

  (function( $, undefined ) {

  $.mobile.behaviors.addFirstLastClasses = {
    _getVisibles: function( $els, create ) {
      var visibles;

      if ( create ) {
        visibles = $els.not( ".ui-screen-hidden" );
      } else {
        visibles = $els.filter( ":visible" );
        if ( visibles.length === 0 ) {
          visibles = $els.not( ".ui-screen-hidden" );
        }
      }

      return visibles;
    },

    _addFirstLastClasses: function( $els, $visibles, create ) {
      $els.removeClass( "ui-first-child ui-last-child" );
      $visibles.eq( 0 ).addClass( "ui-first-child" ).end().last().addClass( "ui-last-child" );
      if ( !create ) {
        this.element.trigger( "updatelayout" );
      }
    },

    _removeFirstLastClasses: function( $els ) {
      $els.removeClass( "ui-first-child ui-last-child" );
    }
  };

  })( jQuery );

  (function( $, undefined ) {

  $.widget( "mobile.controlgroup", $.extend( {
    options: {
      enhanced: false,
      theme: null,
      shadow: false,
      corners: true,
      excludeInvisible: true,
      type: "vertical",
      mini: false
    },

    _create: function() {
      var elem = this.element,
        opts = this.options;

      // Run buttonmarkup
      if ( $.fn.buttonMarkup ) {
        this.element.find( $.fn.buttonMarkup.initSelector ).buttonMarkup();
      }
      // Enhance child widgets
      $.each( this._childWidgets, $.proxy( function( number, widgetName ) {
        if ( $.mobile[ widgetName ] ) {
          this.element.find( $.mobile[ widgetName ].initSelector ).not( $.mobile.page.prototype.keepNativeSelector() )[ widgetName ]();
        }
      }, this ));

      $.extend( this, {
        _ui: null,
        _initialRefresh: true
      });

      if ( opts.enhanced ) {
        this._ui = {
          groupLegend: elem.children( ".ui-controlgroup-label" ).children(),
          childWrapper: elem.children( ".ui-controlgroup-controls" )
        };
      } else {
        this._ui = this._enhance();
      }

    },

    _childWidgets: [ "checkboxradio", "selectmenu", "button" ],

    _themeClassFromOption: function( value ) {
      return ( value ? ( value === "none" ? "" : "ui-group-theme-" + value ) : "" );
    },

    _enhance: function() {
      var elem = this.element,
        opts = this.options,
        ui = {
          groupLegend: elem.children( "legend" ),
          childWrapper: elem
            .addClass( "ui-controlgroup " +
              "ui-controlgroup-" +
                ( opts.type === "horizontal" ? "horizontal" : "vertical" ) + " " +
              this._themeClassFromOption( opts.theme ) + " " +
              ( opts.corners ? "ui-corner-all " : "" ) +
              ( opts.mini ? "ui-mini " : "" ) )
            .wrapInner( "<div " +
              "class='ui-controlgroup-controls " +
                ( opts.shadow === true ? "ui-shadow" : "" ) + "'></div>" )
            .children()
        };

      if ( ui.groupLegend.length > 0 ) {
        $( "<div role='heading' class='ui-controlgroup-label'></div>" )
          .append( ui.groupLegend )
          .prependTo( elem );
      }

      return ui;
    },

    _init: function() {
      this.refresh();
    },

    _setOptions: function( options ) {
      var callRefresh, returnValue,
        elem = this.element;

      // Must have one of horizontal or vertical
      if ( options.type !== undefined ) {
        elem
          .removeClass( "ui-controlgroup-horizontal ui-controlgroup-vertical" )
          .addClass( "ui-controlgroup-" + ( options.type === "horizontal" ? "horizontal" : "vertical" ) );
        callRefresh = true;
      }

      if ( options.theme !== undefined ) {
        elem
          .removeClass( this._themeClassFromOption( this.options.theme ) )
          .addClass( this._themeClassFromOption( options.theme ) );
      }

      if ( options.corners !== undefined ) {
        elem.toggleClass( "ui-corner-all", options.corners );
      }

      if ( options.mini !== undefined ) {
        elem.toggleClass( "ui-mini", options.mini );
      }

      if ( options.shadow !== undefined ) {
        this._ui.childWrapper.toggleClass( "ui-shadow", options.shadow );
      }

      if ( options.excludeInvisible !== undefined ) {
        this.options.excludeInvisible = options.excludeInvisible;
        callRefresh = true;
      }

      returnValue = this._super( options );

      if ( callRefresh ) {
        this.refresh();
      }

      return returnValue;
    },

    container: function() {
      return this._ui.childWrapper;
    },

    refresh: function() {
      var $el = this.container(),
        els = $el.find( ".ui-btn" ).not( ".ui-slider-handle" ),
        create = this._initialRefresh;
      if ( $.mobile.checkboxradio ) {
        $el.find( ":mobile-checkboxradio" ).checkboxradio( "refresh" );
      }
      this._addFirstLastClasses( els,
        this.options.excludeInvisible ? this._getVisibles( els, create ) : els,
        create );
      this._initialRefresh = false;
    },

    // Caveat: If the legend is not the first child of the controlgroup at enhance
    // time, it will be after _destroy().
    _destroy: function() {
      var ui, buttons,
        opts = this.options;

      if ( opts.enhanced ) {
        return this;
      }

      ui = this._ui;
      buttons = this.element
        .removeClass( "ui-controlgroup " +
          "ui-controlgroup-horizontal ui-controlgroup-vertical ui-corner-all ui-mini " +
          this._themeClassFromOption( opts.theme ) )
        .find( ".ui-btn" )
        .not( ".ui-slider-handle" );

      this._removeFirstLastClasses( buttons );

      ui.groupLegend.unwrap();
      ui.childWrapper.children().unwrap();
    }
  }, $.mobile.behaviors.addFirstLastClasses ) );

  })(jQuery);

  (function( $, undefined ) {

  $.widget( "mobile.button", {

    initSelector: "input[type='button'], input[type='submit'], input[type='reset']",

    options: {
      theme: null,
      icon: null,
      iconpos: "left",
      iconshadow: false, /* TODO: Deprecated in 1.4, remove in 1.5. */
      corners: true,
      shadow: true,
      inline: null,
      mini: null,
      wrapperClass: null,
      enhanced: false
    },

    _create: function() {

      if ( this.element.is( ":disabled" ) ) {
        this.options.disabled = true;
      }

      if ( !this.options.enhanced ) {
        this._enhance();
      }

      $.extend( this, {
        wrapper: this.element.parent()
      });

      this._on( {
        focus: function() {
          this.widget().addClass( $.mobile.focusClass );
        },

        blur: function() {
          this.widget().removeClass( $.mobile.focusClass );
        }
      });

      this.refresh( true );
    },

    _enhance: function() {
      this.element.wrap( this._button() );
    },

    _button: function() {
      var options = this.options,
        iconClasses = this._getIconClasses( this.options );

      return $("<div class='ui-btn ui-input-btn" +
        ( options.wrapperClass ? " " + options.wrapperClass : "" ) +
        ( options.theme ? " ui-btn-" + options.theme : "" ) +
        ( options.corners ? " ui-corner-all" : "" ) +
        ( options.shadow ? " ui-shadow" : "" ) +
        ( options.inline ? " ui-btn-inline" : "" ) +
        ( options.mini ? " ui-mini" : "" ) +
        ( options.disabled ? " ui-state-disabled" : "" ) +
        ( iconClasses ? ( " " + iconClasses ) : "" ) +
        "' >" + this.element.val() + "</div>" );
    },

    widget: function() {
      return this.wrapper;
    },

    _destroy: function() {
        this.element.insertBefore( this.button );
        this.button.remove();
    },

    _getIconClasses: function( options ) {
      return ( options.icon ? ( "ui-icon-" + options.icon +
        ( options.iconshadow ? " ui-shadow-icon" : "" ) + /* TODO: Deprecated in 1.4, remove in 1.5. */
        " ui-btn-icon-" + options.iconpos ) : "" );
    },

    _setOptions: function( options ) {
      var outer = this.widget();

      if ( options.theme !== undefined ) {
        outer
          .removeClass( this.options.theme )
          .addClass( "ui-btn-" + options.theme );
      }
      if ( options.corners !== undefined ) {
        outer.toggleClass( "ui-corner-all", options.corners );
      }
      if ( options.shadow !== undefined ) {
        outer.toggleClass( "ui-shadow", options.shadow );
      }
      if ( options.inline !== undefined ) {
        outer.toggleClass( "ui-btn-inline", options.inline );
      }
      if ( options.mini !== undefined ) {
        outer.toggleClass( "ui-mini", options.mini );
      }
      if ( options.disabled !== undefined ) {
        this.element.prop( "disabled", options.disabled );
        outer.toggleClass( "ui-state-disabled", options.disabled );
      }

      if ( options.icon !== undefined ||
          options.iconshadow !== undefined || /* TODO: Deprecated in 1.4, remove in 1.5. */
          options.iconpos !== undefined ) {
        outer
          .removeClass( this._getIconClasses( this.options ) )
          .addClass( this._getIconClasses(
            $.extend( {}, this.options, options ) ) );
      }

      this._super( options );
    },

    refresh: function( create ) {
      var originalElement,
        isDisabled = this.element.prop( "disabled" );

      if ( this.options.icon && this.options.iconpos === "notext" && this.element.attr( "title" ) ) {
        this.element.attr( "title", this.element.val() );
      }
      if ( !create ) {
        originalElement = this.element.detach();
        $( this.wrapper ).text( this.element.val() ).append( originalElement );
      }
      if ( this.options.disabled !== isDisabled ) {
        this._setOptions({ disabled: isDisabled });
      }
    }
  });

  })( jQuery );

  (function( $, undefined ) {

  $.mobile.behaviors.formReset = {
    _handleFormReset: function() {
      this._on( this.element.closest( "form" ), {
        reset: function() {
          this._delay( "_reset" );
        }
      });
    }
  };

  })( jQuery );

  /*
  * "checkboxradio" plugin
  */

  (function( $, undefined ) {

  var escapeId = $.mobile.path.hashToSelector;

  $.widget( "mobile.checkboxradio", $.extend( {

    initSelector: "input:not( :jqmData(role='flipswitch' ) )[type='checkbox']:not('.switch'),input[type='radio']:not( :jqmData(role='flipswitch' ))",

    options: {
      theme: "inherit",
      mini: false,
      wrapperClass: null,
      enhanced: false,
      iconpos: "left"

    },
    _create: function() {
      var input = this.element,
        o = this.options,
        inheritAttr = function( input, dataAttr ) {
          return input.jqmData( dataAttr ) ||
            input.closest( "form, fieldset" ).jqmData( dataAttr );
        },
        // NOTE: Windows Phone could not find the label through a selector
        // filter works though.
        parentLabel = input.closest( "label" ),
        label = parentLabel.length ? parentLabel :
          input
            .closest( "form, fieldset, :jqmData(role='page'), :jqmData(role='dialog')" )
            .find( "label" )
            .filter( "[for='" + escapeId( input[0].id ) + "']" )
            .first(),
        inputtype = input[0].type,
        checkedClass = "ui-" + inputtype + "-on",
        uncheckedClass = "ui-" + inputtype + "-off";

      if ( inputtype !== "checkbox" && inputtype !== "radio" ) {
        return;
      }

      if ( this.element[0].disabled ) {
        this.options.disabled = true;
      }

      o.iconpos = inheritAttr( input, "iconpos" ) || label.attr( "data-" + $.mobile.ns + "iconpos" ) || o.iconpos,

      // Establish options
      o.mini = inheritAttr( input, "mini" ) || o.mini;

      // Expose for other methods
      $.extend( this, {
        input: input,
        label: label,
        parentLabel: parentLabel,
        inputtype: inputtype,
        checkedClass: checkedClass,
        uncheckedClass: uncheckedClass
      });

      if ( !this.options.enhanced ) {
        this._enhance();
      }

      this._on( label, {
        vmouseover: "_handleLabelVMouseOver",
        vclick: "_handleLabelVClick"
      });

      this._on( input, {
        vmousedown: "_cacheVals",
        vclick: "_handleInputVClick",
        focus: "_handleInputFocus",
        blur: "_handleInputBlur"
      });

      this._handleFormReset();
      this.refresh();
    },

    _enhance: function() {
      this.label.addClass( "ui-btn ui-corner-all");

      if ( this.parentLabel.length > 0 ) {
        this.input.add( this.label ).wrapAll( this._wrapper() );
      } else {
        //this.element.replaceWith( this.input.add( this.label ).wrapAll( this._wrapper() ) );
        this.element.wrap( this._wrapper() );
        this.element.parent().prepend( this.label );
      }

      // Wrap the input + label in a div

      this._setOptions({
        "theme": this.options.theme,
        "iconpos": this.options.iconpos,
        "mini": this.options.mini
      });

    },

    _wrapper: function() {
      return $( "<div class='"  +
        ( this.options.wrapperClass ? this.options.wrapperClass : "" ) +
        " ui-" + this.inputtype +
        ( this.options.disabled ? " ui-state-disabled" : "" ) + "' ></div>" );
    },

    _handleInputFocus: function() {
      this.label.addClass( $.mobile.focusClass );
    },

    _handleInputBlur: function() {
      this.label.removeClass( $.mobile.focusClass );
    },

    _handleInputVClick: function() {
      // Adds checked attribute to checked input when keyboard is used
      this.element.prop( "checked", this.element.is( ":checked" ) );
      this._getInputSet().not( this.element ).prop( "checked", false );
      this._updateAll();
    },

    _handleLabelVMouseOver: function( event ) {
      if ( this.label.parent().hasClass( "ui-state-disabled" ) ) {
        event.stopPropagation();
      }
    },

    _handleLabelVClick: function( event ) {
      var input = this.element;

      if ( input.is( ":disabled" ) ) {
        event.preventDefault();
        return;
      }

      this._cacheVals();

      input.prop( "checked", this.inputtype === "radio" && true || !input.prop( "checked" ) );

      // trigger click handler's bound directly to the input as a substitute for
      // how label clicks behave normally in the browsers
      // TODO: it would be nice to let the browser's handle the clicks and pass them
      //       through to the associate input. we can swallow that click at the parent
      //       wrapper element level
      input.triggerHandler( "click" );

      // Input set for common radio buttons will contain all the radio
      // buttons, but will not for checkboxes. clearing the checked status
      // of other radios ensures the active button state is applied properly
      this._getInputSet().not( input ).prop( "checked", false );

      this._updateAll();
      return false;
    },

    _cacheVals: function() {
      this._getInputSet().each( function() {
        $( this ).attr("data-" + $.mobile.ns + "cacheVal", this.checked );
      });
    },

    // Returns those radio buttons that are supposed to be in the same group as
    // this radio button. In the case of a checkbox or a radio lacking a name
    // attribute, it returns this.element.
    _getInputSet: function() {
      var selector, formId,
        radio = this.element[ 0 ],
        name = radio.name,
        form = radio.form,
        doc = this.element.parents().last().get( 0 ),

        // A radio is always a member of its own group
        radios = this.element;

      // Only start running selectors if this is an attached radio button with a name
      if ( name && this.inputtype === "radio" && doc ) {
        selector = "input[type='radio'][name='" + escapeId( name ) + "']";

        // If we're inside a form
        if ( form ) {
          formId = form.id;

          // If the form has an ID, collect radios scattered throught the document which
          // nevertheless are part of the form by way of the value of their form attribute
          if ( formId ) {
            radios = $( selector + "[form='" + escapeId( formId ) + "']", doc );
          }

          // Also add to those the radios in the form itself
          radios = $( form ).find( selector ).filter( function() {

            // Some radios inside the form may belong to some other form by virtue of
            // having a form attribute defined on them, so we must filter them out here
            return ( this.form === form );
          }).add( radios );

        // If we're outside a form
        } else {

          // Collect all those radios which are also outside of a form and match our name
          radios = $( selector, doc ).filter( function() {
            return !this.form;
          });
        }
      }
      return radios;
    },

    _updateAll: function() {
      var self = this;

      this._getInputSet().each( function() {
        var $this = $( this );

        if ( this.checked || self.inputtype === "checkbox" ) {
          $this.trigger( "change" );
        }
      })
      .checkboxradio( "refresh" );
    },

    _reset: function() {
      this.refresh();
    },

    // Is the widget supposed to display an icon?
    _hasIcon: function() {
      var controlgroup, controlgroupWidget,
        controlgroupConstructor = $.mobile.controlgroup;

      // If the controlgroup widget is defined ...
      if ( controlgroupConstructor ) {
        controlgroup = this.element.closest(
          ":mobile-controlgroup," +
          controlgroupConstructor.prototype.initSelector );

        // ... and the checkbox is in a controlgroup ...
        if ( controlgroup.length > 0 ) {

          // ... look for a controlgroup widget instance, and ...
          controlgroupWidget = $.data( controlgroup[ 0 ], "mobile-controlgroup" );

          // ... if found, decide based on the option value, ...
          return ( ( controlgroupWidget ? controlgroupWidget.options.type :

            // ... otherwise decide based on the "type" data attribute.
            controlgroup.attr( "data-" + $.mobile.ns + "type" ) ) !== "horizontal" );
        }
      }

      // Normally, the widget displays an icon.
      return true;
    },

    refresh: function() {
      var hasIcon = this._hasIcon(),
        isChecked = this.element[ 0 ].checked,
        active = $.mobile.activeBtnClass,
        iconposClass = "ui-btn-icon-" + this.options.iconpos,
        addClasses = [],
        removeClasses = [];

      if ( hasIcon ) {
        removeClasses.push( active );
        addClasses.push( iconposClass );
      } else {
        removeClasses.push( iconposClass );
        ( isChecked ? addClasses : removeClasses ).push( active );
      }

      if ( isChecked ) {
        addClasses.push( this.checkedClass );
        removeClasses.push( this.uncheckedClass );
      } else {
        addClasses.push( this.uncheckedClass );
        removeClasses.push( this.checkedClass );
      }

      this.label
        .addClass( addClasses.join( " " ) )
        .removeClass( removeClasses.join( " " ) );
    },

    widget: function() {
      return this.label.parent();
    },

    _setOptions: function( options ) {
      var label = this.label,
        currentOptions = this.options,
        outer = this.widget(),
        hasIcon = this._hasIcon();

      if ( options.disabled !== undefined ) {
        this.input.prop( "disabled", !!options.disabled );
        outer.toggleClass( "ui-state-disabled", !!options.disabled );
      }
      if ( options.mini !== undefined ) {
        outer.toggleClass( "ui-mini", !!options.mini );
      }
      if ( options.theme !== undefined ) {
        label
          .removeClass( "ui-btn-" + currentOptions.theme )
          .addClass( "ui-btn-" + options.theme );
      }
      if ( options.wrapperClass !== undefined ) {
        outer
          .removeClass( currentOptions.wrapperClass )
          .addClass( options.wrapperClass );
      }
      if ( options.iconpos !== undefined && hasIcon ) {
        label.removeClass( "ui-btn-icon-" + currentOptions.iconpos ).addClass( "ui-btn-icon-" + options.iconpos );
      } else if ( !hasIcon ) {
        label.removeClass( "ui-btn-icon-" + currentOptions.iconpos );
      }
      this._super( options );
    }

  }, $.mobile.behaviors.formReset ) );

  })( jQuery );

  (function( $, undefined ) {

  var getAttr = $.mobile.getAttribute;

  $.widget( "mobile.listview", $.extend( {

    options: {
      theme: null,
      countTheme: null, /* Deprecated in 1.4 */
      dividerTheme: null,
      icon: "carat-r",
      splitIcon: "carat-r",
      splitTheme: null,
      corners: true,
      shadow: true,
      inset: false
    },

    _create: function() {
      var t = this,
        listviewClasses = "";

      listviewClasses += t.options.inset ? " ui-listview-inset" : "";

      if ( !!t.options.inset ) {
        listviewClasses += t.options.corners ? " ui-corner-all" : "";
        listviewClasses += t.options.shadow ? " ui-shadow" : "";
      }

      // create listview markup
      t.element.addClass( " ui-listview" + listviewClasses );

      t.refresh( true );
    },

    // TODO: Remove in 1.5
    _findFirstElementByTagName: function( ele, nextProp, lcName, ucName ) {
      var dict = {};
      dict[ lcName ] = dict[ ucName ] = true;
      while ( ele ) {
        if ( dict[ ele.nodeName ] ) {
          return ele;
        }
        ele = ele[ nextProp ];
      }
      return null;
    },
    // TODO: Remove in 1.5
    _addThumbClasses: function( containers ) {
      var i, img, len = containers.length;
      for ( i = 0; i < len; i++ ) {
        img = $( this._findFirstElementByTagName( containers[ i ].firstChild, "nextSibling", "img", "IMG" ) );
        if ( img.length ) {
          $( this._findFirstElementByTagName( img[ 0 ].parentNode, "parentNode", "li", "LI" ) ).addClass( img.hasClass( "ui-li-icon" ) ? "ui-li-has-icon" : "ui-li-has-thumb" );
        }
      }
    },

    _getChildrenByTagName: function( ele, lcName, ucName ) {
      var results = [],
        dict = {};
      dict[ lcName ] = dict[ ucName ] = true;
      ele = ele.firstChild;
      while ( ele ) {
        if ( dict[ ele.nodeName ] ) {
          results.push( ele );
        }
        ele = ele.nextSibling;
      }
      return $( results );
    },

    _beforeListviewRefresh: $.noop,
    _afterListviewRefresh: $.noop,

    refresh: function( create ) {
      var buttonClass, pos, numli, item, itemClass, itemTheme, itemIcon, icon, a,
        isDivider, startCount, newStartCount, value, last, splittheme, splitThemeClass, spliticon,
        altButtonClass, dividerTheme, li,
        o = this.options,
        $list = this.element,
        ol = !!$.nodeName( $list[ 0 ], "ol" ),
        start = $list.attr( "start" ),
        itemClassDict = {},
        countBubbles = $list.find( ".ui-li-count" ),
        countTheme = getAttr( $list[ 0 ], "counttheme" ) || this.options.countTheme,
        countThemeClass = countTheme ? "ui-body-" + countTheme : "ui-body-inherit";

      if ( o.theme ) {
        $list.addClass( "ui-group-theme-" + o.theme );
      }

      // Check if a start attribute has been set while taking a value of 0 into account
      if ( ol && ( start || start === 0 ) ) {
        startCount = parseInt( start, 10 ) - 1;
        $list.css( "counter-reset", "listnumbering " + startCount );
      }

      this._beforeListviewRefresh();

      li = this._getChildrenByTagName( $list[ 0 ], "li", "LI" );

      for ( pos = 0, numli = li.length; pos < numli; pos++ ) {
        item = li.eq( pos );
        itemClass = "";

        if ( create || item[ 0 ].className.search( /\bui-li-static\b|\bui-li-divider\b/ ) < 0 ) {
          a = this._getChildrenByTagName( item[ 0 ], "a", "A" );
          isDivider = ( getAttr( item[ 0 ], "role" ) === "list-divider" );
          value = item.attr( "value" );
          itemTheme = getAttr( item[ 0 ], "theme" );

          if ( a.length && a[ 0 ].className.search( /\bui-btn\b/ ) < 0 && !isDivider ) {
            itemIcon = getAttr( item[ 0 ], "icon" );
            icon = ( itemIcon === false ) ? false : ( itemIcon || o.icon );

            // TODO: Remove in 1.5 together with links.js (links.js / .ui-link deprecated in 1.4)
            a.removeClass( "ui-link" );

            buttonClass = "ui-btn";

            if ( itemTheme ) {
              buttonClass += " ui-btn-" + itemTheme;
            }

            if ( a.length > 1 ) {
              itemClass = "ui-li-has-alt";

              last = a.last();
              splittheme = getAttr( last[ 0 ], "theme" ) || o.splitTheme || getAttr( item[ 0 ], "theme", true );
              splitThemeClass = splittheme ? " ui-btn-" + splittheme : "";
              spliticon = getAttr( last[ 0 ], "icon" ) || getAttr( item[ 0 ], "icon" ) || o.splitIcon;
              altButtonClass = "ui-btn ui-btn-icon-notext ui-icon-" + spliticon + splitThemeClass;

              last
                .attr( "title", $.trim( last.getEncodedText() ) )
                .addClass( altButtonClass )
                .empty();
            } else if ( icon ) {
              buttonClass += " ui-btn-icon-right ui-icon-" + icon;
            }

            a.first().addClass( buttonClass );
          } else if ( isDivider ) {
            dividerTheme = ( getAttr( item[ 0 ], "theme" ) || o.dividerTheme || o.theme );

            itemClass = "ui-li-divider ui-bar-" + ( dividerTheme ? dividerTheme : "inherit" );

            item.attr( "role", "heading" );
          } else if ( a.length <= 0 ) {
            itemClass = "ui-li-static ui-body-" + ( itemTheme ? itemTheme : "inherit" );
          }
          if ( ol && value ) {
            newStartCount = parseInt( value , 10 ) - 1;

            item.css( "counter-reset", "listnumbering " + newStartCount );
          }
        }

        // Instead of setting item class directly on the list item
        // at this point in time, push the item into a dictionary
        // that tells us what class to set on it so we can do this after this
        // processing loop is finished.

        if ( !itemClassDict[ itemClass ] ) {
          itemClassDict[ itemClass ] = [];
        }

        itemClassDict[ itemClass ].push( item[ 0 ] );
      }

      // Set the appropriate listview item classes on each list item.
      // The main reason we didn't do this
      // in the for-loop above is because we can eliminate per-item function overhead
      // by calling addClass() and children() once or twice afterwards. This
      // can give us a significant boost on platforms like WP7.5.

      for ( itemClass in itemClassDict ) {
        $( itemClassDict[ itemClass ] ).addClass( itemClass );
      }

      countBubbles.each( function() {
        $( this ).closest( "li" ).addClass( "ui-li-has-count" );
      });
      if ( countThemeClass ) {
        countBubbles.addClass( countThemeClass );
      }

      // Deprecated in 1.4. From 1.5 you have to add class ui-li-has-thumb or ui-li-has-icon to the LI.
      this._addThumbClasses( li );
      this._addThumbClasses( li.find( ".ui-btn" ) );

      this._afterListviewRefresh();

      this._addFirstLastClasses( li, this._getVisibles( li, create ), create );
    }
  }, $.mobile.behaviors.addFirstLastClasses ) );

  })( jQuery );


  })); 


}