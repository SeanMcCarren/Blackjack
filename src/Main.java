public class Main {
    public static void main(String[] args) {

        CardCounter player = new CardCounter();
        DealerAI dealer = new DealerAI();

        int amountOfGames = 10000000;
        int finalScore = 20; //vary the end score to win something
        int amountOfDecks = 2;

        for(int i = 0; i < amountOfGames; i++){
            playGame(amountOfDecks, player, dealer, 10, finalScore);
            player.resetCount();
        }

        double result1 = player.getWinnings()/amountOfGames;
        //double result2 = player.getAmountOfWins()/ (double) amountOfGames; 

        System.out.println("Money won, in earnings / total games played: " + result1 + ".");
        //System.out.println("Win percentage, i.e. games won/ total games played: " + result2 + ".");
        System.out.println("Amount of games played: " + amountOfGames + ".");

    }



    public static void playGame(int decks, CardCounter player, DealerAI dealer, int maxDepth, int finalScore) {
        Stack stack = new Stack(decks);

        while(stack.getCards() > maxDepth){
            //new game, so reset scores
            player.resetScore();
            dealer.resetScore();

            //if count of player is positive, we bet more
            //if the count is negative, we bet less
            //int decksRemaining = (int) Math.floor( stack.getCards()/(52.0));
            player.setBet();
            double betSize = player.getBet();

            //player gets two cards in the beginning
            player.pulled(stack.pull());
            player.pulled(stack.pull());

            //dealer gets one open card and one closed card
            int card1 = stack.pull();
            player.notice(card1);
            dealer.pulled(card1);
            
            int card2 = stack.pull();
            dealer.pulled(card2);

            //actual game starts now
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
                    player.won();
                    continue;
                }
            }

             //Player's turn
            //System.out.println("Player starts");

            while(player.wantsNext(card1)){
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

            if(playerScore > finalScore){
                player.addWinnings(-1.0 * betSize);
                continue;
            } else if(dealerScore > finalScore){
                player.addWinnings(betSize);
                player.won();
                continue;
            } else if(playerScore < dealerScore){
                player.addWinnings(-1.0 * betSize);
                continue;
            } else if(playerScore > dealerScore){
                player.addWinnings(betSize);
                player.won();
            }

        }

        //System.out.println(player.getWinnings());
    }

}