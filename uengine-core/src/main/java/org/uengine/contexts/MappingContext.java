package org.uengine.contexts;

import java.io.Serializable;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.metaworks.annotation.Id;
import org.metaworks.component.Tree;
import org.metaworks.component.TreeNode;
import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.kernel.Activity;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ParameterContext;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.designer.MappingCanvas;


public class MappingContext implements Serializable {

    public MappingContext() {
    }

    public MappingContext(Activity activity, ProcessInstance instance, ParameterContext[] mappingElements) {

        MappingTree leftTree;
        MappingTree rightTree;

        leftTree = new MappingTree();
        leftTree.setId(TreeNode.ALIGN_LEFT);
        leftTree.setAlign(TreeNode.ALIGN_LEFT);

        MetaworksRemoteService.autowire(leftTree);

        rightTree = new MappingTree();
        rightTree.setId(TreeNode.ALIGN_RIGHT);
        rightTree.setAlign(TreeNode.ALIGN_RIGHT);

        MetaworksRemoteService.autowire(rightTree);
        MappingCanvas canvas = new MappingCanvas();
        canvas.setCanvasId("mappingCanvas");

        //mappingElements 세팅
        if (mappingElements != null) {
            canvas.setMappingElements(mappingElements);
        }
        try {
            leftTree.init();
            rightTree.init();
            ObjectMapper mapper = new ObjectMapper();
            Map leftMap = mapper.convertValue(leftTree, Map.class);
            Map rightMap = mapper.convertValue(rightTree, Map.class);
            String leftJson = mapper.writeValueAsString(leftMap);
            String rightJson = mapper.writeValueAsString(rightMap);

            canvas.setLeftTreeJson(leftJson);
            canvas.setRightTreeJson(rightJson);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setMappingCanvas(canvas);
        //setMappingTreeLeft(leftTree);
        //setMappingTreeRight(rightTree);
    }


    private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

    ParameterContext[] mappingElements;

    public ParameterContext[] getMappingElements() {
        return mappingElements;
    }

    public void setMappingElements(ParameterContext[] mappingElements) {
        this.mappingElements = mappingElements;
    }


    String id;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    transient MappingTree mappingTreeLeft;

    public MappingTree getMappingTreeLeft() {
        return mappingTreeLeft;
    }

    public void setMappingTreeLeft(MappingTree mappingTreeLeft) {
        this.mappingTreeLeft = mappingTreeLeft;
    }

    transient MappingTree mappingTreeRight;

    public MappingTree getMappingTreeRight() {
        return mappingTreeRight;
    }

    public void setMappingTreeRight(MappingTree mappingTreeRight) {
        this.mappingTreeRight = mappingTreeRight;
    }

    MappingCanvas mappingCanvas;

    public MappingCanvas getMappingCanvas() {
        return mappingCanvas;
    }

    public void setMappingCanvas(MappingCanvas mappingCanvas) {
        this.mappingCanvas = mappingCanvas;
    }

}