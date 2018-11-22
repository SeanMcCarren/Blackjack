public class Stack {
    private final int decks;

    public Stack(int decks) {
        this.decks = decks;
    }

    public void pullRandom() {
        System.out.println("Pulled a random card");
    }
}