import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;


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

    private char stateSpace[][];
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
    }


    public void run() {

    }

    public State generateSolution() {
        while(openList.size() > 0) {
            State currentState = openList.poll();
            closeList.add(currentState);
            if (currentState.isGoal()) return currentState;

            for (State child:currentState.getChilds()) {
                numberOperations++;
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
    public void getStateSpace(String filename) {
        // read input and get the number of row and column
        Scanner scanner = new Scanner(filename);
        int y = scanner.nextInt();
        int x = scanner.nextInt();
        scanner.nextLine();
        stateSpace = new char[y][x];

        for (int currentY = 0; currentY < y; currentY++) {
            String line = scanner.nextLine();
            for (int currentX = 0; currentX < x; currentX++) {
                stateSpace[currentY][currentX] = line.charAt(currentX);
                if(line.charAt(currentX) == 'h') {
                    initialState = new State(null, currentX, currentY, 0, false);
                } else if (line.charAt(currentX) == '!') {
                    goalStates.add(new State(null, currentX, currentY, 0, true));
                }
            }
        }

    }


}
