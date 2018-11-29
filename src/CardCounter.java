class CardCounter extends Player{
    protected int count = 0;    //holds the current count of the game

    public CardCounter(){
        super();
    }

    
    public boolean wantsNext(){
        if (this.score <= 10) {
            return true;
        }
        else if (this.score > 10 && count > 0) {
            return false;
        }
        else if(this.score > 10 && count <= 0) {
            return true;
        } else{
            return false;
        }
    }

    
    public void pulled(int value){
        super.pulled(value);

        if (value == 1 || value == 10) {
            count--;
        } else if ( 1 < value && value <= 6) {
            count++;
        }
    }

    
    public int getBet(){
        if (count > 0) {
            betSize++;
        } else if (count < 0) {
            betSize--;
        }

        return betSize;
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

    public void resetCount(){
        this.count = 0;
    }

}