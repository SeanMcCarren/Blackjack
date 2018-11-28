abstract class Player {
    protected int score;
    protected int count;

    public Player() {
        this.score = 0;
        this.count = 0;
    }

    public void pulled(int value) {
        this.score += value;
    }

    public int getScore() {
        return this.score;
    }

    public abstract int addToCount(){
        
    }

    public abstract boolean wantsNext(int score){

    }


}