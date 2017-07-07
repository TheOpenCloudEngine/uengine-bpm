package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.Activity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.AdapterUtil;
import org.uengine.processpublisher.Index;

import java.util.Hashtable;

/**
 * Created by uengine on 2017. 6. 16..
 */
public class ProcessDefinitionAdapter implements Adapter<ProcessDefinition, ProcessDefinition>{

    public ProcessDefinitionAdapter(){}

    @Override
    public ProcessDefinition convert(ProcessDefinition src, Hashtable keyedContext) throws Exception {


        ProcessDefinition processDefinition = new ProcessDefinition();
        // 기존의 process definitino에서 새로 만들 process definition의 변수로 세팅한다.
        processDefinition.setName(src.getName());
        processDefinition.setProcessVariables(src.getProcessVariables());
        Index.indexX.set(0);
        Index.indexY.set(0);
        // index 초기화
        for(Activity activity : src.getChildActivities()){
            keyedContext.put("root", processDefinition);
            Adapter adapter = AdapterUtil.getAdapter(activity.getClass(), getClass());
            processDefinition = (ProcessDefinition) adapter.convert(activity, keyedContext);
            // 돌때마다 y값을 초기화 해주는게 맞는건지...
            Index.indexY.set(0);
        }

        // thread-safe를 위해 기존의 값을 삭제한다.
        Index.indexX.remove();
        Index.indexY.remove();
        return processDefinition;
    }
}
