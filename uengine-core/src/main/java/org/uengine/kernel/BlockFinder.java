package org.uengine.kernel;

import org.uengine.kernel.graph.Transition;

import java.util.*;

public class BlockFinder {

    Activity joinActivity;
        public Activity getJoinActivity() {
            return joinActivity;
        }

        public void setJoinActivity(Activity joinActivity) {
            this.joinActivity = joinActivity;
        }


    List<Activity> blockMembers = new ArrayList<Activity>();
        public List<Activity> getBlockMembers() {
            return blockMembers;
        }

    int branch = 1;

    private BlockFinder(Activity joinActivity){
        setJoinActivity(joinActivity);

        visitForDepthAndVisitCountSetting(joinActivity);
        visitToLineUp(joinActivity);
        findBlockMembers();
    }

//    protected void find(Activity activity){
//        branch += (activity.getIncomingTransitions().size()-1);
//
//        //Since queue is a interface
//        Queue<Activity> queue = new LinkedList<Activity>();
//
//        if(activity == null) return;
//
//        //Adds to end of queue
//        queue.add(activity);
//
//        while(!queue.isEmpty())
//        {
//            //removes from front of queue
//            Activity r = queue.remove();
//
//            blockMembers.put(r.getTracingTag(), r);
//
//            //Visit child first before grandchild
//            for(Transition transition : r.getIncomingTransitions())
//            {
//                Activity sourceActivity = transition.getSourceActivity();
//
//                if(!blockMembers.containsKey(sourceActivity.getTracingTag()))
//                {
//                    blockMembers.put(sourceActivity.getTracingTag(), sourceActivity);
//
//                    queue.add(sourceActivity);
//                }
//            }
//
//            if(queue.size()==1) return;
//        }
//
//    }

    Integer depth = 0;
    Map<Integer, List<Activity>> activitiesByDistanceMap = new HashMap<Integer, List<Activity>>();
    Map<String, Integer> distancesByActivity = new HashMap<String, Integer>();
    Map<String, Integer> visitCount = new HashMap<String, Integer>();

    protected void visitForDepthAndVisitCountSetting(Activity activity){

        depth ++;

        for(Transition transition : activity.getIncomingTransitions()) {
            Activity sourceActivity = transition.getSourceActivity();

            Integer visitCountForThisActivity = 0;
            if(visitCount.containsKey(sourceActivity.getTracingTag())){
                visitCountForThisActivity = visitCount.get(sourceActivity.getTracingTag());
            }

            visitCountForThisActivity++;

            visitCount.put(sourceActivity.getTracingTag(), visitCountForThisActivity);

            visitForDepthAndVisitCountSetting(sourceActivity);

            distancesByActivity.put(sourceActivity.getTracingTag(), depth);
        }

        depth --;
    }

    public void visitToLineUp(Activity activity){

        for(Transition transition : activity.getIncomingTransitions()) {
            Activity sourceActivity = transition.getSourceActivity();

            Integer distanceOfThis = distancesByActivity.get(sourceActivity.getTracingTag());

            if (!activitiesByDistanceMap.containsKey(distanceOfThis)) {
                activitiesByDistanceMap.put(distanceOfThis, new ArrayList<Activity>());
            }

            if(!activitiesByDistanceMap.get(distanceOfThis).contains(sourceActivity))
                activitiesByDistanceMap.get(distanceOfThis).add(sourceActivity);

            visitToLineUp(sourceActivity);
        }

    }

    protected void findBlockMembers(){
        blockMembers = new ArrayList<Activity>();
        int branch = joinActivity.getIncomingTransitions().size();

        for(int i=1; activitiesByDistanceMap.containsKey(i) && activitiesByDistanceMap.get(i).size()>0; i++){

            List<Activity> activitiesInDepth = activitiesByDistanceMap.get(i);
            if(activitiesInDepth.size()==1){

                Activity activity = activitiesInDepth.get(0);
                blockMembers.add(activity);
                int visitedCountForThis = visitCount.get(activity.getTracingTag());

                if(branch == visitedCountForThis){
                    break;
                }
            }else {
                for (Activity activity : activitiesInDepth){
                    branch += activity.getIncomingTransitions().size() -1;

                    blockMembers.add(activity);
                }
            }

        }


    }


    public static List<Activity> getPossibleBlockMembers(List<Activity> blockMembers, ProcessInstance instance) throws Exception {
        Activity theLastBlockMember = blockMembers.get(blockMembers.size()-1);
        List possibleBlockMembers = new ArrayList<Activity>();

        possibleBlockMembers.add(theLastBlockMember);

        BlockFinder.visitForPossibleNodes(theLastBlockMember, blockMembers, instance, possibleBlockMembers);

        return possibleBlockMembers;
    }

    private static void visitForPossibleNodes(Activity activity, List<Activity> blockMembers, ProcessInstance instance, List<Activity>possibleNodes) throws Exception {

        for(Activity next : activity.getPossibleNextActivities(instance, "")){
            if(blockMembers.contains(next)){
                possibleNodes.add(next);
                visitForPossibleNodes(next, blockMembers, instance, possibleNodes);
            }
        }
    }


    public static List<Activity> getBlockMembers(Activity joinActivity){
        return new BlockFinder(joinActivity).getBlockMembers();
    }




}
