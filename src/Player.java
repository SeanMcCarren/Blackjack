abstract class Player {
    protected int score;
    protected int count;

    public Player() {
        this.score = 0; // Score in hand
        this.count = 0; // Count in head
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