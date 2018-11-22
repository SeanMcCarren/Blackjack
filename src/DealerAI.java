public class DealerAI extends Player {

    public boolean wantsNext() {
        return this.score <= 16 ? true: false;
    }
}