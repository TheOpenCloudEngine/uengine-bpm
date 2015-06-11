package org.uengine.processdesigner.mapper.transformers;

import java.awt.Container;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JRootPane;

import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.metaworks.inputter.SelectInput;
import org.metaworks.inputter.TextInput;
import org.uengine.kernel.ProcessInstance;
import org.uengine.persistence.dao.DAOFactory;
import org.uengine.persistence.dao.KeyGeneratorDAO;
import org.uengine.processdesigner.mapper.Transformer;

/**
 * MySql 에서는 sequence.nextVal 을 사용하지 못한다. 때문에 select (max(columnName) + 1) from
 * tableName; 의 형식으로 해야 할듯 한데
 * 
 * 문제는 테이블명을 과 컬럼 명을 받아와야 한다는 것이고, 장점은 프라이머리 키가 아닌 컬럼에도 적용이 가능하다는 것이다.
 * 
 * @author erim
 * 
 */
public class SequenceGeneratorTransformer extends Transformer {

	String sequence;

	public SequenceGeneratorTransformer() {
		setName("Sequence");
	}


	public static void metaworksCallback_changeMetadata(Type type) {

		FieldDescriptor fdSequence = type.getFieldDescriptor("Sequence");
		fdSequence.setInputter(new SelectInput(
				new Object[] {"BPM_ACLTABLE", "BPM_PROCDEF", "BPM_PROCDEFVER", "BPM_PROCINST", "BPM_PROCVAR", "BPM_ROLEMAPPING", "BPM_WORKITEM"}, 
				new Object[] {"ACLTABLE", "PROCDEF", "PROCDEFVER", "PROCINST", "PROCVAR", "ROLEMAPPING", "WORKITEM"})
		);
		
		FieldDescriptor fdType = type.getFieldDescriptor("sequence");
		fdType.setInputter(new TextInput());
	}


	public String getSequence() {
		return sequence;
	}


	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	@Override
	public Object transform(ProcessInstance instance, Map parameterMap, Map options) {
		Object result = null;
		try {
			DAOFactory df = DAOFactory.getInstance(instance.getProcessTransactionContext());

			KeyGeneratorDAO kgd = df.createKeyGenerator(getSequence(), new HashMap());
			kgd.select();
			kgd.next();
			result = kgd.getKeyNumber();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}


	@Override
	public String[] getInputArguments() {
		return new String[] {};
	}


}
