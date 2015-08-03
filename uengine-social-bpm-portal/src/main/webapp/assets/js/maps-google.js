$(function () {

    var simple_map;
    var styled_map;
    var route_map;
    var cluster_map;
    var geocoding_map;

    if($("#simple-map").length){
            simple_map = new GMaps({
            el: '#simple-map',
            lat: -12.043333,
            lng: -77.028333,
            zoomControl: true,
            zoomControlOpt: {
                style: 'SMALL',
                position: 'TOP_LEFT'
            },
            panControl: false,
            streetViewControl: false,
            mapTypeControl: false,
            overviewMapControl: false
        });
        simple_map.addMarker({
            lat: -12.042,
            lng: -77.028333,
            title: 'Marker with InfoWindow',
            infoWindow: {
                content: '<p>Here we are!</p>'
            }
        });
    }

    
    if($("#style-map").length){
        styled_map = new GMaps({
            el: '#style-map',
            lat: -12.043333,
            lng: -77.028333,
            zoomControl: true,
            zoomControlOpt: {
                style: 'SMALL',
                position: 'TOP_LEFT'
            },
            panControl: false,
            streetViewControl: false,
            mapTypeControl: false,
            overviewMapControl: false,
            styles: [{
                "featureType": "water",
                "elementType": "geometry",
                "stylers": [{
                    "color": "#193341"
                }]
            }, {
                "featureType": "landscape",
                "elementType": "geometry",
                "stylers": [{
                    "color": "#2c5a71"
                }]
            }, {
                "featureType": "road",
                "elementType": "geometry",
                "stylers": [{
                    "color": "#29768a"
                }, {
                    "lightness": -37
                }]
            }, {
                "featureType": "poi",
                "elementType": "geometry",
                "stylers": [{
                    "color": "#406d80"
                }]
            }, {
                "featureType": "transit",
                "elementType": "geometry",
                "stylers": [{
                    "color": "#406d80"
                }]
            }, {
                "elementType": "labels.text.stroke",
                "stylers": [{
                    "visibility": "on"
                }, {
                    "color": "#3e606f"
                }, {
                    "weight": 2
                }, {
                    "gamma": 0.84
                }]
            }, {
                "elementType": "labels.text.fill",
                "stylers": [{
                    "color": "#ffffff"
                }]
            }, {
                "featureType": "administrative",
                "elementType": "geometry",
                "stylers": [{
                    "weight": 0.6
                }, {
                    "color": "#1a3541"
                }]
            }, {
                "elementType": "labels.icon",
                "stylers": [{
                    "visibility": "off"
                }]
            }, {
                "featureType": "poi.park",
                "elementType": "geometry",
                "stylers": [{
                    "color": "#2c5a71"
                }]
            }]
        });
    }

    if($("#route-map").length){
         map = new GMaps({
            el: '#route-map',
            zoom: 13,
            lat: -12.043333,
            lng: -77.028333
        });
        map.travelRoute({
            origin: [-12.044012922866312, -77.02470665341184],
            destination: [-12.090814532191756, -77.02271108990476],
            travelMode: 'driving',
            step: function (e) {
                $('#instructions').append('<li>' + e.instructions + '</li>');
                $('#instructions li:eq(' + e.step_number + ')').delay(450 * e.step_number).fadeIn(200, function () {
                    map.drawPolyline({
                        path: e.path,
                        strokeColor: '#131540',
                        strokeOpacity: 0.6,
                        strokeWeight: 6
                    });
                });
            }
        });
    }



    if($("#cluster-map").length){
        cluster_map = new GMaps({
            div: '#cluster-map',
            lat: -12.043333,
            lng: -77.028333,
            markerClusterer: function (map) {
                return new MarkerClusterer(map);
            }
        });

        var lat_span = -12.035988012939503 - -12.050677786181573;
        var lng_span = -77.01528673535154 - -77.04137926464841;

        for (var i = 0; i < 100; i++) {
            var latitude = Math.random() * (lat_span) + -12.050677786181573;
            var longitude = Math.random() * (lng_span) + -77.04137926464841;

            cluster_map.addMarker({
                lat: latitude,
                lng: longitude,
                title: 'Marker #' + i
            });
        };

        geocoding_map = new GMaps({
            el: '#geocoding-map',
            lat: -12.043333,
            lng: -77.028333
        });
    }

    if($("#geocoding_form").length){

        $('#geocoding_form').submit(function (e) {
            e.preventDefault();
            GMaps.geocode({
                address: $('#address').val().trim(),
                callback: function (results, status) {
                    if (status == 'OK') {
                        var latlng = results[0].geometry.location;
                        geocoding_map.setCenter(latlng.lat(), latlng.lng());
                        geocoding_map.addMarker({
                            lat: latlng.lat(),
                            lng: latlng.lng()
                        });
                    }
                }
            });
        });
         
    }

});