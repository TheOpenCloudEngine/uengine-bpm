var org_uengine_kernel_bpmn_face_ProcessVariablePanel = function(objectId, className){
    //debugger;
    this.objectId = objectId;
    this.object = mw3.objects[this.objectId];

    console.log(this.object);

    var processVariableList = this.object.processVariableList.face.elements;
    var data = [];

    for(var k=0; k<processVariableList.length; k++) {
        var thisPV = [];
        thisPV[0] = processVariableList[k].value.name;
        thisPV[1] = processVariableList[k].value.typeClassName.value;

        thisPV[2] = processVariableList[k].value.global;
        thisPV[3] = processVariableList[k].value.defaultValue;

        data[k] = thisPV;
    }

    $("#listGrid_"+this.objectId).jqGrid({
        datatype: "local",
        url:"/test.do",
        editurl:'clientArray',
        data : mydata,
        rowNum : 10,
        gridview : true,
        sortable:true,
        multiselect:true,
        sortname:"name",
        viewrecords:true,
        height: 250,
        width:500,
        colNames: ['name', 'type', 'global', 'defaultValue'],
        colModel: [
            {
                name: 'name',
                index: 'name',
                width: '50%',
                editable: true,
                editrules:{required:true}},
            {
                name: 'type',
                index: 'type',
                width: '50%',
                editable: true,
                editrules:{required:true},
                edittype:"select",
                editoptions:{value:"java.lang.String:java.lang.String;java.lang.Long:java.lang.Long;java.lang.Double:java.lang.Double;org.uengine.social.RoleUser:org.uengine.social.RoleUser"}},
            {name:'global',index:'global', width: '50%', editable: true,edittype:"checkbox",editoptions: {value:"true:false"}},
            {
                name: 'defaultValue',
                index: 'defaultValue',
                width: '50%',
                editable: true,
                editrules:{required:true}},
        ],
        pager: '#page_'+this.objectId
    });

    $(window).resize(function() {
        $("#listGrid_"+this.objectId).setGridWidth($(this).width() * .100);
    });

    var names = ["name", "type", "global", "defaultValue"];
    var mydata = [];

    for (var i = 0; i < processVariableList.length; i++) {
        mydata[i] = {};
        for (var j = 0; j < data[i].length; j++) {
            mydata[i][names[j]] = data[i][j];
        }
    }
    for (var i = 0; i <= mydata.length; i++) {
        $("#listGrid_"+this.objectId).jqGrid('addRowData', i + 1, mydata[i]);
    }

    $("#listGrid_"+this.objectId).navGrid('#page_'+this.objectId,{edit:false,add:false,del:true});
    $("#listGrid_"+this.objectId).jqGrid('inlineNav','#page_'+this.objectId);
};

org_uengine_kernel_bpmn_face_ProcessVariablePanel.prototype.getValue = function() {

    var gridData = $("#listGrid_"+this.objectId).jqGrid('getGridParam','data');
    this.object = {
        __className: "org.uengine.kernel.bpmn.face.ProcessVariablePanel",
        processVariableList: null
    }

    var objectFace = {
        __className: "org.metaworks.FaceWrapped",
        face:null
    }

    objectFace.faceClass = "org.uengine.kernel.bpmn.face.ProcessVariableListFace";


    var listFace = {
        __className: "org.uengine.kernel.bpmn.face.ProcessVariableListFace",
        elements:[]

    }

    listFace.className = "org.uengine.kernel.ProcessVariable";


    for(var i=0;  i<gridData.length; i++){
        listFace.elements[i] = {
            __className: "org.metaworks.model.MetaworksElement",
            value:null
        }

        listFace.elements[i].value = {
            __className: "org.uengine.kernel.ProcessVariable",
        }

        listFace.elements[i].value.name = gridData[i].name;
        listFace.elements[i].value.typeClassName=gridData[i].type;
        listFace.elements[i].value.global=gridData[i].global;
        listFace.elements[i].value.defaultValue=gridData[i].defaultValue;
    }

    objectFace.face = listFace;

    this.object.processVariableList = objectFace;

    return this.object;
};
