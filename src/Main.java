public class Main {
    public static void main(String[] args) {

        CardCounter player = new CardCounter();
        DealerAI dealer = new DealerAI();

        for(int i = 0; i < 100; i++){
            playGame(1, player, dealer, 10);
            player.resetCount();
        }

    }



    public static void playGame(int decks, CardCounter player, DealerAI dealer, int maxDepth) {
        Stack stack = new Stack(decks);
        double runningWinnings = 0;


        while(stack.getCards() > maxDepth){
            player.resetScore();
            dealer.resetScore();


            player.pulled(stack.pull());
            player.pulled(stack.pull());

            int card1 = stack.pull();
            player.notice(card1);
            dealer.pulled(card1);
            
            int card2 = stack.pull();
            dealer.pulled(card2);

            System.out.println("Game started!");

            //mocht de player 21 hebben, stop nu!
            if(player.getScore() == 21){
                if(dealer.getScore() == 21){
                    System.out.println("Player and dealer have blackjack");
                    continue;
                }
                else{
                    System.out.println("Player has blackjack");
                    player.addWinnings(1.5);
                    continue;
                }
            }

            System.out.println("Player starts");

            //player speelt zijn beurt
            while(player.wantsNext()){
                int tempCard1 = stack.pull();
                System.out.println("pulled a " + tempCard1 + ".");
                player.pulled(tempCard1);
            }

            System.out.println("Player is done, card is flipped");

            //de dealer draait de tweede kaart om
            player.notice(card2);

            System.out.println("Dealer starts");

            //de dealer speelt zijn beurt
            while(dealer.wantsNext()){
                
                int tempCard2 = stack.pull();
                player.notice(tempCard2);
                dealer.pulled(tempCard2);
            }

            System.out.println("Decide who wins");


            if(player.getScore() > 21){
                player.addWinnings(-1.0);
                continue;
            } else if(dealer.getScore() > 21){
                player.addWinnings(1.0);
                continue;
            } else if(player.getScore() <dealer.getScore()){
                player.addWinnings(-1.0);
            } else if(player.getScore() > dealer.getScore()){
                player.addWinnings(1.0);
            }

        }

        System.out.println(player.getWinnings());

    }


}