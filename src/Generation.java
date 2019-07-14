//Caleb Chadderton
//id: 1328518

import java.util.ArrayList;
import java.util.Random;

public class Generation {

    private Box[] boxes;
    private ArrayList<Candidate> candidateList;
    private Random random;
    private Candidate fittestCandidate;

    //create a generation, give it the boxes that we are working with.
    public Generation(Box[] _boxes, int size) {
        random = new Random();
        boxes = _boxes;
        candidateList = new ArrayList<>();
        fittestCandidate = null;
        //create the appropriate number of candidates.
        for(int n = 0; n < size; n++) {

            //create a candidate to add to this generation
            int[] candidateDNA = new int[boxes.length];

            //create all the initial candidates using random numbers.
            for (int i = 0; i < boxes.length; i++) {

                //produce a random number, iether 1 or 0.
                candidateDNA[i] = random.nextInt(2);
            }
            candidateList.add(new Candidate(boxes, candidateDNA));
        }
        evaluateGeneration();
    }

    public Generation(ArrayList<Candidate> _candidateList, Box[] _boxes) {
        random = new Random();
        boxes = _boxes;
        candidateList = _candidateList;
        fittestCandidate = null;
        evaluateGeneration();
    }

    //method to produce some mutated candidates to try help diversity.
    public ArrayList<Candidate> developMutants(int numMutants) {
        int count = 0;
        ArrayList<Candidate> mutantSurvivors;
        ArrayList<Candidate> childrenSurvivors;

        mutantSurvivors = selectSurvivors(numMutants/2);
        childrenSurvivors = selectSurvivors(numMutants /2);

        //loop through the number of mutants to create.
        while(count < (numMutants/2)) {
            int percent = (int)(fittestCandidate.getDNA().length *.1);
            if (percent == 0){percent = 1;}
            int num = 0;
            while(num < percent) {
                //choose a random bit in the DNA to flip
                int randomBit1 = random.nextInt(fittestCandidate.getDNA().length);
                int randomBit2 = random.nextInt(fittestCandidate.getDNA().length);
                mutantSurvivors.get(count).flipDNA(randomBit1);
                childrenSurvivors.get(count).flipDNA(randomBit2);
                num++;
            }
            count++;
        }
        mutantSurvivors.addAll(childrenSurvivors);

        return mutantSurvivors;
    }

    //method to make children based doign reproduction
    public ArrayList<Candidate> makeChildren(int numChildren) {
        ArrayList<Candidate> children = new ArrayList<>();

        //for each child to reproduce
        for(int i = 0; i < numChildren; i++) {
            //set up its dna and parents
            int[] DNA = new int[boxes.length];
            int[] DNA2 = new int[boxes.length];
            int[] parent1 = null;
            int[] parent2 = null;
            int count = 0;

            //perform a one point split
            int split = random.nextInt(boxes.length);
            while(count < 2) {

                //add the fittest parent to always be a parent and another random selection
                parent2 = fittestCandidate.getDNA();
                parent1 = candidateList.get(random.nextInt(candidateList.size())).getDNA();
                count++;
            }

            //get the split point and add the corresponding data.
            for(int j = 0; j < boxes.length; j++) {
                if(j < split) {
                    DNA[j] = parent1[j];
                    DNA2[j] = parent2[j];
                }
                else {
                    DNA[j] = parent2[j];
                    DNA2[j] = parent1[j];
                }
            }

            //get the fittest child
            Candidate candidate1 = new Candidate(boxes, DNA);
            Candidate candidate2 = new Candidate(boxes, DNA2);
            if(candidate1.getFitness() > candidate2.getFitness()) {
                children.add(candidate1);
            }
            else {
                children.add(candidate2);
            }
        }
        return children;
    }


    //method to choose survivors to exist in this generation.
    public ArrayList<Candidate> selectSurvivors(int numSurvivors) {
        Candidate winner = null;
        ArrayList<Candidate> survivors = new ArrayList<>(numSurvivors);
        ArrayList<Candidate> battlers = new ArrayList<>();


        for(int i = 0; i < numSurvivors; i++) {
            for(int j = 0; j < 5; j++) {
                battlers.add(candidateList.get(random.nextInt(candidateList.size())));
            }
            for(Candidate c : battlers) {
                if(winner == null) {
                    winner = c;
                }
                else if(c.getFitness() > winner.getFitness()) {
                    winner = c;
                }
            }
            survivors.add(winner);
            winner = null;
            battlers.clear();
        }
        return survivors;
    }

    public void print() {
        for(Candidate c : candidateList) {
            c.print();
        }
    }

    public int evaluateGeneration() {
        Candidate bestCandidate = null;
        int generationFitness = 0;

        for(Candidate c : candidateList) {
            if(bestCandidate == null) {
                bestCandidate = c;
            }
            else if(c.getFitness() > bestCandidate.getFitness()) {
                bestCandidate = c;
            }
            generationFitness += c.getFitness();
        }
        fittestCandidate = bestCandidate;
        return generationFitness;
    }

    public Candidate getFittestCandidate() {
        return fittestCandidate;
    }

    public ArrayList<Candidate> getCandidateList() {
        return candidateList;
    }
}
