public class Main {
    public static void main(String[] args) {
        Stack stack = new Stack(6); //Init a stack of 6 decks of cards
        stack.pullRandom(); 

        DealerAI player1 = new DealerAI();
        player1.pulled(16);
        System.out.println("Player wants?: " + player1.wantsNext());
        player1.pulled(1);
        System.out.println("Player wants?: " + player1.wantsNext());
    }
}