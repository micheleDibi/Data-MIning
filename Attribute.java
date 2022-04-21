class Attribute {

    private String name;
    private int index;

    Attribute(String nm, int idx) {
        name = nm;
        index = idx;
    }

    String getName() {return name;}
    int getIndex() {return index;}

    public String toString() {
        return ("nome: " + name + "\nindex: " + index);
    }

    /*
    public static void main(String[] args) {
        Attribute a = new Attribute("test", 1);
        System.out.println(a.toString());
    }
    */

}