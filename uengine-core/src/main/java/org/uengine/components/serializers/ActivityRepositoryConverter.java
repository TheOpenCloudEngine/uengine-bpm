package org.uengine.components.serializers;
import org.uengine.kernel.ActivityRepository;

import com.thoughtworks.xstream.mapper.Mapper;

public class ActivityRepositoryConverter extends com.thoughtworks.xstream.converters.collections.CollectionConverter{
	    public ActivityRepositoryConverter(Mapper mapper) {
	        super(mapper);
	    }

	    public boolean canConvert(Class type) {
	        return ActivityRepository.class == type;
	    }

 }