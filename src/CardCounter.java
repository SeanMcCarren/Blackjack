class CardCounter extends Player{
    protected int count = 0;    //holds the current count of the game
    protected int betSize = 1;

    public CardCounter(){
        super();
    }

    
    public boolean wantsNext(int card){
        /**if (this.score <= 10) {
            return true;
        }
        else if(this.score > 21){
            return false;
        }
        else if (this.score > 10 && count > 0) {
            return false;
        }
        else if(this.score > 10 && this.score < 17 && count <= 0) {
            return true;
        } 

        return false;
        */

        if( card < 6 && this.score > 12){
            return false;
        } 
        else if( card < 6 && this.score <= 12){
            return true;
        }
        else if(this.score >= 17){
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

        if (value == 1 || value == 10) {
            count--;
        } else if ( 1 < value && value <= 6) {
            count++;
        }
    }

    
    public void setBet(){
        if(betSize == 1){
            return;
        }
        /**if (count > 0) {
            betSize++;
        } else if (count < 0) {
            betSize--;
        }*/
        if(count > 1){
            betSize = (count - 1);
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

    public int getBet(){
        return betSize; 
    }

    public void resetCount(){
        this.count = 0;
    }

}