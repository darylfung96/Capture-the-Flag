import java.io.File;
import java.util.Scanner;

//TODO: change reading to System.in

public class A1Q2 {


    public static void main(String[] args) {
        AStarAgent agent = new AStarAgent("./src/input.txt");
        agent.run();
    }

}
