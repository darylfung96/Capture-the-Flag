import java.io.File;
import java.io.IOException;
import java.util.*;


/*
* AStarAgent    (Capture The Flags)
*   Intro:      A hero/heroine is provided in a fully observable state space
*               and has to capture all the flags in the shortest time possible.
*
*   Purpose:    A agent will be used to calculate and generate the shortest possible amount
*               of time to help the hero/heroine captures all the flags.
*
*
*
*
* */
public class AStarAgent {
    private final static int MOVE_COST = 10;
    private int numberOperations;
    private PriorityQueue<State> openList;
    private LinkedList<State> closeList;

    private HashMap<String, State> stateSpace;
    private State initialState;
    private State currentGoal;
    private LinkedList<State> goalStates;

    private int mapX;   // this is the total x size of the map
    private int mapY;   // this is the total y size of the map

    /*
    * A constructor
    *
    * Args: A filename for the map states is provided.
    *
    * Generates two lists and initialize the numberOperations to 0.
    * The two lists generated are openList and closeList.
    *
    * openList refers to states that are being considered.
    * closeList refers to states which has been checked.
    *
    *
    * States in the openList will have its parent changed if a better parent with a lower cost
    * value is in sight.
    *
    * States in closeList will not be checked.
    *
    * */
    public AStarAgent() {
        numberOperations = 0;
        openList = new PriorityQueue<>(10, Comparator.comparing(State::getTotalCost));
        closeList = new LinkedList<>();
        goalStates = new LinkedList<>();
        stateSpace = new HashMap<>();

        //generate the states and store them in a coordinate hashmap
        getStateSpace();    // generate the state space by reading the file

        // get the children for each states available
        getChildren();

    }

    /*
    * run:
    *
    * purpose:
    *       Run the agent and help it find the closest goal state.
    *       Once the closest goal state was found, we trace it back to the current position,
    *       adding all the states on its way the number of times visited by one.
    *
    *       Then we start from the current goal state and get to the next nearest goal state
    *       and repeat the same thing until all the goal states have been achieved.
    *
    * */
    public void run() {
        while(goalStates.size() > 0) {
            getNearestGoal();
            initialState.setParent(null);
            openList.add(initialState);
            State currentGoalState = generateSolution();
            if (currentGoalState == null) break; // if we find no solution we break out of loop
            currentGoalState.setGoal(false);
            initialState = currentGoalState;

            // track back to the previous initial position, adding all the states visited by one
            // on its way
            while (currentGoalState.getParent() != null) {
                currentGoalState.increaseVisited();
                currentGoalState.resetCost();   // reset their total cost value
                currentGoalState = currentGoalState.getParent();
            }

            openList.clear();
            closeList.clear();
        }

        // after finished print out the maze
        printMaze();

        // handle fail case
        if (goalStates.size() > 0) {
            System.out.println("Unable to capture all the flags.");
            System.out.println("Number of flags not captured: " + goalStates.size());
        } else {
            System.out.println("Successfully captured all the flags");
        }


    }

    /*
    * setNearestGoal
    *
    * Purpose:  Find the nearest goal and set it as the hero/heroine target.
    *
    *
    *
    * */
    public void getNearestGoal() {
        int closestHeuristic = -1;
        for (State goalState : goalStates) {
            int currentHeuristic = initialState.calculateCost(goalState);

            // set currentGoal if the heuristic for the current goal state is lower than
            // the closestHeuristic value
            if(closestHeuristic < 0 || closestHeuristic > currentHeuristic) {
                closestHeuristic = currentHeuristic;
                currentGoal = goalState;
            }
        }
    }


    /*
    * Generate solution from the initial state to get to the nearest goal state.
    *
    * return:   The goal state.
    *
    * */
    private State generateSolution() {
        while(openList.size() > 0) {
            State currentState = openList.poll();
            closeList.add(currentState);
            if (currentState.isGoal()) {
                goalStates.remove(currentState);
                return currentState;
            }

            for (State child:currentState.getChildren()) {
                numberOperations++;
                // consider close list
                if(closeList.contains(child)) continue;

                // consider goal state
                if(child.isGoal()) {
                    goalStates.remove(child);
                    child.setParent(currentState);
                    child.setfCost(currentState.getfCost() + MOVE_COST);
                    return child;
                }

                // consider open list
                if(openList.contains(child)) {
                    if(currentState.getfCost() + MOVE_COST < child.getfCost()) {
                        child.setParent(currentState);
                        child.setfCost(currentState.getfCost() + MOVE_COST);
                    }
                } else {
                    child.setParent(currentState);
                    child.setfCost(currentState.getfCost() + MOVE_COST);
                    child.calculateCost(currentGoal);
                    openList.add(child);
                }

            }

        }
        return null;
    }


    public boolean isGoal(State currentState) {
        return (currentState.getX() == currentGoal.getX() && currentState.getY() == currentGoal.getY());
    }




    // get state spaces
    /*
    * Read a filename adn generate the state space from there.
    *
    * Arg: filename - the file to read.
    *
    *
    * set initialState if a 'h' was found.
    * add goalStates if '!' were found.
    *
    * */
    private void getStateSpace() {
            // read input and get the number of row and column
            Scanner scanner = new Scanner(System.in);
            int y = scanner.nextInt();
            mapY = y;
            int x = scanner.nextInt();
            mapX = x;
            scanner.nextLine();

            for (int currentY = 0; currentY < y; currentY++) {
                String line = scanner.nextLine();
                for (int currentX = 0; currentX < x; currentX++) {
                    char current = line.charAt(currentX);
                    String currentKey = Integer.toString(currentX) + "," + Integer.toString(currentY);
                    if (current == 'h') {
                        initialState = new State(null, currentX, currentY, 0, false);
                        initialState.increaseVisited(); // since we start here, we set its times visited starting as one
                        stateSpace.put(currentKey, initialState);
                    } else if (current == '!') {
                        State state = new State(null, currentX, currentY, 0, true);
                        stateSpace.put(currentKey, state);
                        goalStates.add(state);
                    } else if (current == '.') {
                        State state = new State(null, currentX, currentY, 0, false);
                        stateSpace.put(currentKey, state);
                    }

                }
            }



    }


    /////////////////////// get children ////////////////////////////////
    /*
    * Produce children for the all the states available in the hashmap
    *
    * */
    private void getChildren() {
        for (Map.Entry<String, State> entry : stateSpace.entrySet()) {
            State state = entry.getValue();
            getChildrenHelper(state, state.getX(), state.getY());
        }
    }

    /*
    * getChildsHelper       (getChildren helper)
    *           Purpose:    Help the states to find its children.
    *
    *            Example:   ######
    *                       ...h..
    *                       ......
    *            In state h, we will get the coordinates of its available movement
    *            and check if they can be h's next state or not. If it can then we
    *            add those states to h's childs.
    *
    *            args:
    *                   state:  the current state to deal with
    *                   x:      the current x position for this state
    *                   y:      the current y position for this state
    *
    *
    *
    * */
    private void getChildrenHelper(State state, int x, int y) {
        // 4 different possible children
        String firstChild = Integer.toString(x-1)+","+Integer.toString(y);
        String secondChild = Integer.toString(x+1)+","+Integer.toString(y);
        String thirdChild = Integer.toString(x)+","+Integer.toString(y-1);
        String fourthChild = Integer.toString(x)+","+Integer.toString(y+1);

        if (stateSpace.containsKey(firstChild))  state.addChild(stateSpace.get(firstChild));
        if (stateSpace.containsKey(secondChild)) state.addChild(stateSpace.get(secondChild));
        if (stateSpace.containsKey(thirdChild))  state.addChild(stateSpace.get(thirdChild));
        if (stateSpace.containsKey(fourthChild)) state.addChild(stateSpace.get(fourthChild));

        String currentKey = Integer.toString(x) + "," + Integer.toString(y);
        stateSpace.put(currentKey, state);
    }




    /*
    *
    * printMaze
    *           Purpose:    Print out the solution obtained by the agent
    *
    * */
    private void printMaze() {
        int timesVisited = 0;
        for (int y = 0; y < mapY; y++) {
            StringBuilder currentLine = new StringBuilder("");
            for (int x = 0; x < mapX; x++) {
                State state = stateSpace.get(Integer.toString(x)+","+Integer.toString(y));
                // if state is available to be traversed
                if(state == null) {
                    currentLine.append("#");
                } else {
                    currentLine.append(Integer.toString(state.getTimesVisited()));
                    timesVisited += state.getTimesVisited();
                }
            }
            System.out.println(currentLine);
        } // end of for loop

        System.out.println("Total squares visited: " + Integer.toString(timesVisited));
        System.out.println("Operations considered: " + numberOperations);
    }

}
