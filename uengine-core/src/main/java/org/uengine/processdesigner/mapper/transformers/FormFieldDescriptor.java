package org.uengine.processdesigner.mapper.transformers;

import org.metaworks.FieldDescriptor;
import org.metaworks.Instance;
import org.metaworks.Type;
import org.metaworks.inputter.Inputter;
import org.metaworks.validator.Validator;

public class FormFieldDescriptor extends FieldDescriptor{
	public static void metaworksCallback_changeMetadata(Type type){
		
		FieldDescriptor fd;

		type.removeFieldDescriptor("Savable");
		type.removeFieldDescriptor("Loadable");
		type.removeFieldDescriptor("Updatable");
		type.removeFieldDescriptor("ForeignKey");
		type.removeFieldDescriptor("Type");

		fd = type.getFieldDescriptor("Inputter");
		fd.setValidators(new Validator[]{
			new Validator(){

				public String validate(Object data, Instance instance) {
					Inputter inputter = (Inputter)data;
					Class type = (Class)instance.getFieldValue("Type");

					if( inputter.isEligibleType(type)) return null;

					return "This inputter is not eligible for " + type;
				}
				
			}
		});

	}
}
