import java.util.LinkedList;


/*
* State:    Purpose is to store the x and y, its parent, its costs, and the number of
*           times this state was visited.
*           There is a isGoal boolean which determine if this state is the goal state.
*
*
*
* */
public class State {

    private State parent;
    private LinkedList<State> childs;
    private int x;
    private int y;
    private int fCost;
    private int hCost;
    private int timesVisited;

    private boolean isGoal;


    public State(State parent, int x, int y, int fCost, boolean isGoal) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.fCost = fCost;
        timesVisited = 0;
        this.isGoal = isGoal;
        childs = new LinkedList<>();
    }

    public void addChild(State state) {
        childs.add(state);
    }


    public int calculateCost(State goalState) {
        double goalX = (double)goalState.getX();
        double goalY = (double)goalState.getY();
        double heuristicValue = Math.sqrt(Math.pow(goalX, 2) + Math.pow(goalY, 2));

        hCost = (int)(heuristicValue);
        return hCost;
    }

    /*setters*/
    public void setParent(State state) { parent = state; }
    public void increaseVisited() { timesVisited++; }
    public void setfCost(int fCost) { this.fCost = fCost; }
    public void setGoal(boolean isGoal) { this.isGoal = isGoal; }
    public void resetCost() { fCost = 0; hCost = 0; }

    /*getters*/
    public int getX() { return x; }
    public int getY() { return y; }
    public int getfCost() { return fCost; }
    public int getTotalCost() { return fCost + hCost; }
    public int getTimesVisited() { return timesVisited; }
    public State getParent() { return parent; }
    public LinkedList<State> getChildren() { return childs; }
    public boolean isGoal() { return isGoal; }

    // string
    public String toString() {
        String string = "";
        string += Integer.toString(getX()) + " " + Integer.toString(getY());
        return string;
    }
}
