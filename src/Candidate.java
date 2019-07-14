//Caleb Chadderton
//id: 1328518

import java.util.ArrayList;
import java.util.Arrays;

public class Candidate {
    private Box[] boxes;
    private int[] DNA;
    private int fitness;
    private ArrayList<int[]> stack;



    public Candidate(Box[] _boxes, int[] _DNA) {
        //set this candidates DNA, and list of Boxes to play with. Make sure it is a whole new set and not just a reference.
        boxes = new Box[_boxes.length];
        for(int i  = 0; i < _boxes.length; i++)
        {
            boxes[i] = _boxes[i];
        }
        DNA = _DNA;
        stack = new ArrayList<>();
        fitness = calculateFitness();
    }


    //calculates the fitness of this candidate
    private int calculateFitness() {
        int stackHeight = 0, currWidth = 0, currLength = 0;
        Box currBox;

        for(int i = 0; i < DNA.length; i++) {
            //if this Box in encoded
            if(DNA[i] == 1) {

                //get the box
                currBox = boxes[i];

                //see if it will fit width ways
                if(currBox.width < currWidth || currWidth == 0) {

                    //if it's width fits then check that it's length fits.
                    if(currBox.length < currLength || currLength == 0) {

                        //finally if an iteration of this box doesn't already exist
                        if(!currBox.used) {

                            //if both of the dimensions fit then include them in the stack.
                            stackHeight += currBox.height;

                            currWidth = currBox.width;
                            currLength = currBox.length;
                            setInStack(currBox.id);
                            currBox.inStack = true;
                            stack.add(new int[]{currBox.width, currBox.length, currBox.height, stackHeight});

                        }
                    }
                }
            }
        }
        undoStack();
        return stackHeight;
    }


    //method to set a box with an specific ID to being in the stack.
    private void setInStack(int id) {

        //find all occurrences of this box and set it to be in the stack already.
        for(Box b : boxes) {
            if(b.id == id) {
                b.used = true;
            }
        }
    }


    //undo the stack that was built
    private void undoStack() {
        for(Box b : boxes) {
            b.used = false;
        }
    }

    //brint this candidate DNA as a string
    public void print() {
        System.out.println(Arrays.toString(DNA) + " " + fitness);
    }


    //print this candidates evaluated stack.
    public void printStack() {
        for(int[] box : stack) {
            System.out.println(padRight(box[0], 5) + padRight(box[1],5) + padRight(box[2],5) + padRight(box[3],5));
        }
    }


    public static String padRight(int s, int n) {
        return String.format("%-" + n + "s", s);
    }


    public int getFitness() {
        return fitness;
    }


    public  int[] getDNA() {
        return DNA;
    }

    //used to flip a dna position.
    public void flipDNA(int index) {
        if(DNA[index] == 1) {
            DNA[index] = 0;
        }
        else {
            DNA[index] = 1;
        }
    }
}
