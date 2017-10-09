import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class AStarAgent {
    private final static int MOVE_COST = 10;
    private int numberOperations;
    private PriorityQueue<State> openList;
    private LinkedList<State> closeList;

    public AStarAgent(String stateSpace) {
        numberOperations = 0;
        openList = new PriorityQueue<>(10, Comparator.comparing(State::getTotalCost));
        closeList = new LinkedList<>();
    }


    public void run() {

    }

    public State generateSolution() {
        while(openList.size() > 0) {
            State currentState = openList.poll();
            closeList.add(currentState);
            if (currentState.isGoal()) return currentState;

            for (State child:currentState.getChilds()) {
                // consider close list
                if(closeList.contains(child)) continue;

                // consider goal state
                if(child.isGoal()) return child;

                // consider open list
                if(openList.contains(child)) {
                    if(currentState.getfCost() + MOVE_COST < child.getfCost()) {
                        child.setParent(currentState);
                    }
                } else {
                    openList.add(child);
                }

            }

        }
        return null;
    }


    // convert string to states
    public void getStateSpace(String stateSpace) {

    }


}
