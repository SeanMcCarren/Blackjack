public class Main {
    public static void main(String[] args) {

        CardCounter player = new CardCounter();
        DealerAI dealer = new DealerAI();

        for(int i = 0; i < 10000; i++){
            playGame(1, player, dealer, 10);
            player.resetCount();
        }

    }



    public static void playGame(int decks, CardCounter player, DealerAI dealer, int maxDepth) {
        Stack stack = new Stack(decks);

        while(stack.getCards() > maxDepth){
            //new game, so reset scores
            player.resetScore();
            dealer.resetScore();

            //if count of player is positive, we bet more
            //if the count is negative, we bet less
            player.setBet();
            int betSize = player.getBet();

            //player gets two cards in the beginning
            player.pulled(stack.pull());
            player.pulled(stack.pull());

            //dealer gets one open card and one closed card
            int card1 = stack.pull();
            player.notice(card1);
            dealer.pulled(card1);
            
            int card2 = stack.pull();
            dealer.pulled(card2);

            //actaul game starts now
            //System.out.println("Game started!");

            //If player has a blackjack, 21, then the game is already over
            if(player.getScore() == 21) {
                if(dealer.getScore() == 21) {
                    //System.out.println("Player and dealer have blackjack");
                    continue;
                }
                else { 
                    //System.out.println("Player has blackjack");
                    player.addWinnings(1.5 * betSize);
                    continue;
                }
            }

             //Player's turn
            //System.out.println("Player starts");

            while(player.wantsNext()){
                //int tempCard1 = stack.pull();
                //System.out.println("pulled a " + tempCard1 + ".");

                player.pulled(stack.pull());
            }

            //the closed card is open to the player
            //System.out.println("Player is done, card is flipped");
            player.notice(card2);

            //Dealer's turn
            //System.out.println("Dealer starts");
        
            while(dealer.wantsNext()){
                int tempCard2 = stack.pull();
                //System.out.println("pulled a " + tempCard2 + ".");

                player.notice(tempCard2);
                dealer.pulled(tempCard2);
            }

            //System.out.print("Decide who wins, current score is: ");
            //System.out.println("Player has: " + player.getScore() + ", Dealer has: " + dealer.getScore() + ".");

            //the game has been played, checking who wins
            int playerScore = player.getScore();
            int dealerScore = dealer.getScore();

            if(playerScore > 21){
                player.addWinnings(-1.0);
                continue;
            } else if(dealerScore > 21){
                player.addWinnings(betSize);
                continue;
            } else if(playerScore < dealerScore){
                player.addWinnings(-1.0 * betSize);
                continue;
            } else if(playerScore > dealerScore){
                player.addWinnings(betSize);
            }

        }

        System.out.println(player.getWinnings());
    }

}