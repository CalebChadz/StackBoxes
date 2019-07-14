//Caleb Chadderton
//id: 1328518

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;


public class NPStack {

    private static  int idCount = 0;
    private static Random random = new Random();
    private static int generationSize;
    private static ArrayList<Box> boxList = new ArrayList<>();
    private static Box[] sortedBoxes;
    private static Candidate bestCandidate = null;



    ///main entry-point for the program.
    public static void main(String[] args) throws IOException {
        int candidates = 0;
        int processedCandidates = 0;


        if(args.length == 2) {

            //Second number is candidate quantity
            candidates = Integer.parseInt(args[1]);
            generationSize = (int)(candidates * 0.10) ;
            //open the file to read the possible boxes
            BufferedReader reader = new BufferedReader(new FileReader(new File(args[0])));

            //Read in the Box data
            getBoxes(reader);

            //sort the boxes by surface area
            sortBoxes();

            for(Box b : sortedBoxes) {
                System.out.println(b.print());
            }

            //We are going with a genetic algorithm approach. Create a random initial generation.
            Generation generation = new Generation(sortedBoxes, generationSize);
            //generation.print();

            while(processedCandidates < candidates) {
                generation = geneticAlgorithm(generation);
                processedCandidates += generationSize;
            }
            bestCandidate.printStack();



        }
        else {
            //otherwise the arguments were entered incorrectly.
            System.out.println("Incorrect usage: NPStack <filename.txt> <Max number of tries>");
            System.exit(1);
        }
    }

    private static Generation geneticAlgorithm(Generation generation) {
        Generation newGeneration;
        ArrayList<Candidate> survivors;
        ArrayList<Candidate> children;
        ArrayList<Candidate> mutants;

        //step1: Evaluate the fitness of the candidates.
        System.out.println("This Generations Fitness: " + generation.evaluateGeneration());
        System.out.println("Fittest Candidate of this generation: " + generation.getFittestCandidate().getFitness());
//        for(Candidate c : generation.getCandidateList()) {
//            System.out.println(c.getFitness());
//        }

        //see if there is a new candidate that is better.
        if(bestCandidate == null) {
            bestCandidate = generation.getFittestCandidate();
        }
        else if(generation.getFittestCandidate().getFitness() > bestCandidate.getFitness()) {
            bestCandidate = generation.getFittestCandidate();
        }

        //DO some reproduction using certain candidates as pairs. 25 children
        children = generation.makeChildren(generationSize/4);

        //select the fittest of the candidates to survive to the next generation. chooses half of the population via tournament selection. 50 survivor.
        survivors = generation.selectSurvivors(generationSize/2);

        //do a little mutation.
        mutants = generation.developMutants(generationSize/4);


        survivors.addAll(children);

        //add the mutants
        survivors.addAll(mutants);
        //in case we are working with strange numbers or ratios integer division.
        while(survivors.size() < generation.getCandidateList().size()) {
            survivors.add(new Candidate(sortedBoxes, bestCandidate.getDNA()));
        }

        newGeneration = new Generation(survivors, sortedBoxes);


        return newGeneration;
    }



    private static void sortBoxes() {
        //Use sorting method to sort th boxes by surface area, width then depth.
        boxList.sort(new Comparator<Box>() {
            @Override
            public int compare(Box b1, Box b2) {
                if(b1.width < b2.width) {
                    return 1;
                }
                if(b1.width > b2.width) {
                    return -1;
                }
                if(b1.length < b2.length) {
                    return 1;
                }
                if(b1.length > b2.length) {
                    return -1;
                }
                if(b1.height < b2.height) {
                    return 1;
                }
                if(b1.height > b2.height) {
                    return -1;
                }

                return 0;
            }
        });

        sortedBoxes = boxList.toArray(new Box[0]);
    }

    private static void getBoxes(BufferedReader reader) throws IOException {

        //Variables
        String line;
        String[] array;
        int width = 0, length = 0, height = 0;

        //loop through every line of input.
        while((line = reader.readLine()) != null) {
            array = line.split(" ");

            //makes sure there was correct text file input
            if(array.length == 3) {

                //get the input
                width = Integer.parseInt(array[0]);
                length = Integer.parseInt(array[1]);
                height = Integer.parseInt(array[2]);

                //Check if there are any negative numbers.
                if (width > 0 && length > 0 && height > 0) {

                    //will create the correct number of orientations of the box.
                    createBoxes(width, length, height, idCount++);
                }
            }
        }
    }

    private static void createBoxes(int width, int length, int height, int id) {

        //if all three dimensions are equal this is a cube so only one needs to be created.
        if(width == length && length == height)
        {
            //a=b && b=c -> a=c
            boxList.add(new Box(width, length, height, id));
        }
        else if (width != length && length != height && width != height) {

            //if no sides are equivalent then this box has three different size faces.
            boxList.add(new Box(width, length, height, id));
            boxList.add(new Box(length, height, width, id));
            boxList.add(new Box(height, width, length, id));
        }
        else {
            //add the possible orientations, two different faces exist.
            boxList.add(new Box(width, length, height, id));
            if(width == length) {
                boxList.add(new Box(width, height, length, id));
            }
            else {
                boxList.add(new Box(height, length, width, id));
            }
        }
    }
}
