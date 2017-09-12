package online.pizzacrust.rmc;

import java.io.File;

public class Compiler {

    public static void main(String... args) throws Exception {
        File directory = new File(args[0]);
        File outputRbxmx = new File("built.rbxmx");
        RecursiveAssembler recursiveAssembler = new RecursiveAssembler(directory, outputRbxmx);
        System.out.println("Assembling...");
        long ms = System.currentTimeMillis();
        recursiveAssembler.compile();
        System.out.println("Assembled to \"built.rbxmx\".");
        System.out.println("Took " + (System.currentTimeMillis() - ms) + " milliseconds.");
    }

}
