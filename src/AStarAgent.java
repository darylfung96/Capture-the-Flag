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
    private LinkedList<State> goalStates;

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
    public AStarAgent(String filename) {
        numberOperations = 0;
        openList = new PriorityQueue<>(10, Comparator.comparing(State::getTotalCost));
        closeList = new LinkedList<>();
        goalStates = new LinkedList<>();
        stateSpace = new HashMap<>();

        //generate the states and store them in a coordinate hashmap
        getStateSpace(filename);    // generate the state space by reading the file

        // get the children for each states available
        getChildren();

    }


    public void run() {
        openList.add(initialState);
        State gotState = generateSolution();

        while(gotState != null && gotState.getParent() != null) {
            System.out.println(gotState);
            gotState = gotState.getParent();
        }
    }

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
                    openList.add(child);
                }

            }

        }
        return null;
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
    private void getStateSpace(String filename) {
        try {
            // read input and get the number of row and column
            Scanner scanner = new Scanner(new File(filename));
            int y = scanner.nextInt();
            int x = scanner.nextInt();
            scanner.nextLine();

            for (int currentY = 0; currentY < y; currentY++) {
                String line = scanner.nextLine();
                for (int currentX = 0; currentX < x; currentX++) {
                    char current = line.charAt(currentX);
                    String currentKey = Integer.toString(currentX) + "," + Integer.toString(currentY);
                    if (current == 'h') {
                        initialState = new State(null, currentX, currentY, 0, false);
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

        } catch(IOException e) {  }

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

        if (stateSpace.containsKey(firstChild)) state.addChild(stateSpace.get(firstChild));
        if (stateSpace.containsKey(secondChild)) state.addChild(stateSpace.get(secondChild));
        if (stateSpace.containsKey(thirdChild)) state.addChild(stateSpace.get(thirdChild));
        if (stateSpace.containsKey(fourthChild)) state.addChild(stateSpace.get(fourthChild));

        String currentKey = Integer.toString(x) + "," + Integer.toString(y);
        stateSpace.put(currentKey, state);
    }



}
