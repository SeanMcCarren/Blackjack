public class Main {
    public static void main(String[] args) {
        
        playFullGames();
        
    }

    public static void playFullGames() {
        CardCounter player = new CardCounter();
        DealerAI dealer = new DealerAI();

        int amountOfGames = 1000000;
        int finalScore = 21; //vary the end score to win something
        int amountOfDecks = 6;

        int progressBarLength = 20;
        int progressIncrement = 0;
        int progress = 0;
        int rounds = 0;

        for(int i = 0; i < amountOfGames; i++){
            rounds += playGame(amountOfDecks, player, dealer, 60, finalScore);
            player.resetCount();
            if (i == progressIncrement) {
                progressIncrement = ++progress * amountOfGames / progressBarLength;
                System.out.print("|" + repeat("=", progress-1) + repeat(" ", progressBarLength-progress) + "|\r");
            }
        }

        double result1 = player.getWinnings()/amountOfGames;
        double result2 = player.getWinnings()/rounds;
        //double result2 = player.getAmountOfWins()/ (double) amountOfGames; 

        System.out.println("Money won, in earnings / total games played: " + result1 + ".");
        System.out.println("Money won, in earnings / total rounds played: " + result2 + ".");
        //System.out.println("Win percentage, i.e. games won/ total games played: " + result2 + ".");
        System.out.println("Amount of games played: " + amountOfGames + ".");

    }

    public static String repeat(String a, int n) {
        String res = "";
        for (int i = 0; i < n; i++) {
            res += a;
        }
        return res;
    }



    public static int playGame(int decks, CardCounter player, DealerAI dealer, int maxDepth, int finalScore) {
        Stack stack = new Stack(decks);
        int rounds = 0;

        while(stack.getCards() > maxDepth){
            //new game, so reset scores
            player.resetScore();
            dealer.resetScore();
            rounds++;

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
            if(player.getScore() == finalScore) {
                if(dealer.getScore() == finalScore) {
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
                player.addWinnings(1.0 * betSize);
                player.won();
                continue;
            } else if(playerScore < dealerScore){
                player.addWinnings(-1.0 * betSize);
                continue;
            } else if(playerScore > dealerScore){
                player.addWinnings(1.0 * betSize);
                player.won();
            }

        }
        return rounds;
        //System.out.println(player.getWinnings());
    }

}