var org_uengine_social_RoleUser = function(objectId, className){
    this.objectId = objectId;
    this.className = className;
    this.divId = mw3._getObjectDivId(this.objectId);

    var roleUser = mw3.objects[objectId];
    if(roleUser == null) {
        roleUser = {
            __className: 'org.uengine.social.RoleUser',
            user:{
                __className: 'org.uengine.codi.mw3.model.User',
                metaworksContext: {
                    when: 'edit'
                }

            }
        };

        mw3.setObject(objectId, roleUser);
    }
};