import java.util.LinkedList;

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


    public void calculateCost(State goalState) {
        double goalX = (double)goalState.getX();
        double goalY = (double)goalState.getY();
        double heuristicValue = Math.sqrt(Math.pow(goalX, 2) + Math.pow(goalY, 2));

        hCost = (int)(heuristicValue);
    }

    /*setters*/
    public void setParent(State state) { parent = state; }
    public void increaseVisited() { timesVisited++; }
    public void setfCost(int fCost) { this.fCost = fCost; }

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
