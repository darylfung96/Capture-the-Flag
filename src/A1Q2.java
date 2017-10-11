import java.io.File;
import java.util.Scanner;

//TODO: change reading to System.in
//TODO: handle fail case for unable to capture the flag

public class A1Q2 {


    public static void main(String[] args) {
        AStarAgent agent = new AStarAgent("./src/input.txt");
        agent.run();
    }

}
