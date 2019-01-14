class BadCardCounter extends Player{
    protected int count = 0;    //holds the current count of the game
    protected double betSize = 1;

    public BadCardCounter(){
        super();
    }

    //implemented the basic strategy for blackjack
    public boolean wantsNext(int card){

        int score = getScore();

        return score <= 16;
    }

    public boolean wantsSplit(int playerCard, int dealerCard){
        if ((playerCard == 2 || playerCard == 3) && 4 <= dealerCard && dealerCard <= 7){
            return true;
        } else if (playerCard == 2 || playerCard == 3){
            return false;
        } else if (playerCard == 4 || playerCard == 5 || playerCard == 10){
            return false;
        } else if (playerCard == 6 && (dealerCard == 2 || dealerCard == 1 || 7 <= dealerCard)){
            return false;
        } else if (playerCard == 6){
            return true;
        } else if (playerCard == 7 && (dealerCard == 1 || 8 <= dealerCard)){
            return false;
        } else{
            return true;
        }
    }

    public boolean wantsDouble(int playerTotal, int dealerCard){
        if(this.amountOfAcesInHand == 0){
            if (playerTotal == 9 && (3 <= dealerCard && dealerCard <= 6)){
                return true;
            } else if (playerTotal == 10 && (2 <= dealerCard && dealerCard <= 9)){
                return true;
            } else if (playerTotal == 11 && (2 <= dealerCard && dealerCard <= 10)){
                return true;
            } else{
                return false;
            }
        } else{
            if ((playerTotal == 13 || playerTotal == 14) && (dealerCard == 5 || dealerCard == 6)){
                return true;
            } else if ((playerTotal == 15 || playerTotal == 16) && (4 <= dealerCard && dealerCard <= 6)){
                return true;
            } else if ((playerTotal == 17 || playerTotal == 18) && (3 <= dealerCard && dealerCard <= 6)){
                return true;
            } else{
                return false;
            }
        }
    }
    
    public void pulled(int value, boolean seen){
        super.pulled(value);

        if (!seen){
            notice(value);
        }
    }
   
    public void setBet(int decks){
        /*if (count > 1){
            if(( (100.0*count)/decks - 1.0) > 0){
                betSize = (count/decks - 1.0);
            } else{
                betSize = 100;
            }
        }
        else if ( count < 0){
            betSize = 100/count;
        } else if ( count == 0){
            betSize = 100;
        }*/

        this.betSize = 1;
    }

    public void notice(int value){
        if (value == 1 || value == 10) {
            count--;
        } else if ( 1 < value && value <= 6) {
            count++;
        }
    }

    public int getCount(){
        return count;
    }

    public double getBet(){
        return betSize; 
    }

    public void resetCount(){
        this.count = 0;
    }

}