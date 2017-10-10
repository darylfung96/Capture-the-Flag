import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class A1Q2 {


    public static void main(String[] args) {
        AStarAgent agent = new AStarAgent("input.txt");
        agent.run();
    }

}
