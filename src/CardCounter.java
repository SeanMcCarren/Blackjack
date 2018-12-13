class CardCounter extends Player{
    protected int count = 0;    //holds the current count of the game
    protected double betSize = 1;

    public CardCounter(){
        super();
    }

    //implemented the basic strategy for blackjack
    public boolean wantsNext(int card){

        if( card < 6 && getScore() > 12){
            return false;
        } 
        else if( card < 6 && getScore() <= 12){
            return true;
        }
        else if(getScore() >= 17){
            return false;
        }
        else if(card > 6){
            return true;
        }
    
        return false;
        //return this.score <= 16 ? true: false;
    }
    
    public void pulled(int value){
        super.pulled(value);

        notice(value);

    }
   
    public void setBet(){
        if(count > 1){
            betSize = ( count - 1.0);
        }
        else if ( count <= 0){
            betSize = 1;
        }
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