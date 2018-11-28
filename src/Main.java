public class Main {
    public static void main(String[] args) {
        Stack stack = new Stack(6);
        while (stack.isNext()) {
            System.out.println(stack.pull());
        }
    }
}