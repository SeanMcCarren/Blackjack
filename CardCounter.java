class CardCounter extends Player{
    protected int count = 0;    //holds the current count of the game

    public CardCounter(){
        super();
    }

    @override
    public boolean wantsNext(){
        if (this.score <= 10) {
            return true;
        }
        else if (this.score > 10 && count > 0) {
            return false;
        }
        else if (this.score > 10 && count <= 0) {
            return true;
        }
    }

    @override
    public void pulled(int value){
        super(value);

        if (value == 1 || value == 10) {
            count--;
        } else if ( 1 < value <= 6) {
            count++;
        }
    }

    @override
    public int getBet(){
        if (count > 0) {
            betSize++;
        } else if (count < 0) {
            betSize--;
        }

        return betSize;
    }

}