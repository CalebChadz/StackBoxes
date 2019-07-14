//Caleb Chadderton
//id: 1328518

public class Box {
    //all the dimensions of a box.
    int width;
    int length;
    int height;
    int id;
    boolean used, inStack;

    //constructor for a box to be created.
    public Box(int _width, int _length, int _height, int _id) {
        width = _width;
        length = _length;
        height = _height;
        id = _id;
        used = false;
        inStack = false;
    }

    public String print() {
        return width + " " + length + " " + height + " " ;
    }
}