public class State {

    private State parent;
    private int x;
    private int y;
    private int fCost;
    private int hCost;
    private int timesVisited;

    public State(State parent, int x, int y, int fCost) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.fCost = fCost;
        timesVisited = 0;
    }


    public void calculateCost(State goalState) {
        double goalX = (double)goalState.getX();
        double goalY = (double)goalState.getY();
        double heuristicValue = Math.sqrt(Math.pow(goalX, 2) + Math.pow(goalY, 2));

        hCost = (int)(heuristicValue);
    }

    /*setters*/
    public void setParent(State state) { parent = state; }

    /*getters*/
    public int getX() { return x; }
    public int getY() { return y; }
    public int getTotalCost() { return fCost + hCost; }
    public int getTimesVisited() { return timesVisited; }
    public State getParent() { return parent; }
}
