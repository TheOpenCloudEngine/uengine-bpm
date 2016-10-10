//alert('here');

/**
 * BPMN : Start Event Shape
 *
 * @class
 * @extends OG.shape.GeomShape
 * @requires OG.common.*, OG.geometry.*
 *
 * @param {String} label 라벨 [Optional]
 * @author <a href="mailto:hrkenshin@gmail.com">Seungbaek Lee</a>
 */
OG.shape.bpmn.ReceiveErrorEventView = function (label) {
    OG.shape.bpmn.ReceiveErrorEventView.superclass.call(this);

    this.SHAPE_ID = 'OG.shape.bpmn.ReceiveErrorEventView';
    this.label = label;
    this.inclusion = false;
};
OG.shape.bpmn.ReceiveErrorEventView.prototype = new OG.shape.bpmn.Event();
OG.shape.bpmn.ReceiveErrorEventView.superclass = OG.shape.bpmn.Event;
OG.shape.bpmn.ReceiveErrorEventView.prototype.constructor = OG.shape.bpmn.E_Start;
OG.ReceiveErrorEventView = OG.shape.bpmn.ReceiveErrorEventView;

/**
 * 드로잉할 Shape 을 생성하여 반환한다.
 *
 * @return {OG.geometry.Geometry} Shape 정보
 * @override
 */
OG.shape.bpmn.ReceiveErrorEventView.prototype.createShape = function () {
    if (this.geom) {
        return this.geom;
    }

    this.geom = new OG.geometry.Circle([50, 50], 50);
    this.geom.style = new OG.geometry.Style({
        'label-position': 'bottom',
        "stroke-width" : 1.5
    });

    return this.geom;
};

/**
 * Shape 간의 연결을 위한 Terminal 을 반환한다.
 *
 * @return {OG.Terminal[]} Terminal
 * @override
 */
OG.shape.bpmn.ReceiveErrorEventView.prototype.createTerminal = function () {
    if (!this.geom) {
        return [];
    }

    var envelope = this.geom.getBoundary();

    return [
        new OG.Terminal(envelope.getCentroid(), OG.Constants.TERMINAL_TYPE.C, OG.Constants.TERMINAL_TYPE.OUT),
        new OG.Terminal(envelope.getRightCenter(), OG.Constants.TERMINAL_TYPE.E, OG.Constants.TERMINAL_TYPE.OUT),
        new OG.Terminal(envelope.getLeftCenter(), OG.Constants.TERMINAL_TYPE.W, OG.Constants.TERMINAL_TYPE.OUT),
        new OG.Terminal(envelope.getLowerCenter(), OG.Constants.TERMINAL_TYPE.S, OG.Constants.TERMINAL_TYPE.OUT),
        new OG.Terminal(envelope.getUpperCenter(), OG.Constants.TERMINAL_TYPE.N, OG.Constants.TERMINAL_TYPE.OUT)
    ];
};




var org_uengine_kernel_bpmn_view_ReceiveErrorEventView = function(objectId, className){
    org_uengine_kernel_view_ActivityView(objectId, className);

}
extend(org_uengine_kernel_bpmn_view_ReceiveErrorEventView, "org_uengine_kernel_view_ActivityView");



