package org.uengine.kernel;

import java.util.List;
import java.util.Map;

public class ActivityUtil {
	
	public ActivityUtil() {
		// TODO Auto-generated constructor stub
	}

	public boolean isMatchHiddenActivityTypes(Activity child, Map options) {
		boolean isMatch = child.isHidden();
		List listClsActivityTypes = (List<Class>) options.get("hiddenActivityTypes");
		
		for (int i = 0; (i < listClsActivityTypes.size() && !isMatch); i++) {
			Class<Activity> clsActivityType = (Class<Activity>) listClsActivityTypes.get(i);
			
			if (	clsActivityType.isAssignableFrom(child.getClass())) {
				isMatch = true;
			}
		}
		
		return isMatch;
	}
	
	
	
	public boolean isVisible(Activity child, Map options) {
		boolean isHidden = child.isHidden();
		
		if (options.containsKey("show hidden activity")) {
			isHidden = false;
		} else if (
				!isHidden &&
				options.containsKey("hiddenActivityTypes")
		) {
			
			if (ComplexActivity.class.isAssignableFrom(child.getClass())) {
				isHidden = isVisibleComplexWidthParent(child, options);
			} else {
				isHidden = isMatchHiddenActivityTypes(child, options);
			}
		}
		
		return isHidden;
	}

	
	
	private boolean isVisibleComplexWidthParent(Activity child, Map options) {
		ComplexActivity activityTmp = (ComplexActivity) child;
		if (SequenceActivity.class.isAssignableFrom(child.getClass())) {
			activityTmp = (ComplexActivity) child.getParentActivity();
		}
		
		return isVisibleComplex(activityTmp, options);
	}
	
	
	
	private boolean isVisibleComplex(Activity child, Map options) {
		boolean isHidden = true;
		
		if (ComplexActivity.class.isAssignableFrom(child.getClass())) {
			ComplexActivity cActivity = (ComplexActivity) child;
			List<Activity> childActivities = cActivity.getChildActivities();
			for(Activity activityTmpChild : childActivities){
				ActivityUtil activityUtil = new ActivityUtil();
				if (!activityUtil.isVisibleComplex(activityTmpChild, options)) {
					isHidden = false;
					break;
				}
			}
			
		} else if (
				!EmptyActivity.class.isAssignableFrom(child.getClass()) &&
				!isMatchHiddenActivityTypes(child, options)
		) {
			isHidden = false;
		}
		
		return isHidden;
	}

}
