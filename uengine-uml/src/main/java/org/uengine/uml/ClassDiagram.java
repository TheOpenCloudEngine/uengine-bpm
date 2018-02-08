package org.uengine.uml;

import org.uengine.modeling.IModel;
import org.uengine.modeling.Relation;
import org.uengine.uml.model.ClassDefinition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by uengine on 2018. 2. 3..
 */
public class ClassDiagram implements IModel, Serializable {

    String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    String name;
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


    List<ClassDefinition> classDefinitions = new ArrayList<>();
        public List<ClassDefinition> getClassDefinitions() {
            return classDefinitions;
        }
        public void setClassDefinitions(List<ClassDefinition> classDefinitions) {
            this.classDefinitions = classDefinitions;
        }


    List<Relation> relations = new ArrayList<>();
        public List<Relation> getRelations() {
            return relations;
        }
        public void setRelations(List<Relation> relations) {
            this.relations = relations;
        }

}
