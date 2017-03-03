
import java.util.*;

public class proj1 {

    public static void main(String[] args) {

        PriorityQueue<EightPuzzle> h1 = new PriorityQueue<EightPuzzle>();
        PriorityQueue<EightPuzzle> h2 = new PriorityQueue<EightPuzzle>();
        int[] g = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        EightPuzzle goal1 = new EightPuzzle(g, 1, 0);
        EightPuzzle goal2 = new EightPuzzle(g, 2, 0);
        int[] z = new int[9];
        System.out.println("Enter 0 to enter your own puzzle");
        Scanner kb = new Scanner(System.in);
        int generated = kb.nextInt();
        if (generated == 0) {
            System.out.println("Please enter nine digits in the order which you prefer:\n");
            for (int i = 0; i < 9; i++) {
                z[i] = kb.nextInt();
            }
            h1.add(new EightPuzzle(z, 1, 0));
            h2.add(new EightPuzzle(z, 2, 0));
        }
        while (!h2.isEmpty()) {
            System.out.println("Attempting to complete using hueristic 1... ");
            System.out.println("The Nodes Initial State is: ");
            System.out.print(h1.peek().toString() + "\n");

            astar(h1.poll(), goal1, (long) System.currentTimeMillis());
            System.out.println("\nAttempting to complet using hueristic 2... \n");
            System.out.print(h2.peek().toString() + "\n");
            astar(h2.poll(), goal2, (long) System.currentTimeMillis());
        }

    }

    public static int[] shuffle() {
        Random gen = new Random();
        int set[] = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        int goal[] = set.clone();
        boolean con = true;
        while (con) {
            for (int i = 0; i < set.length; i++) {
                int ranPos = gen.nextInt(set.length);
                int temp = set[i];
                set[i] = set[ranPos];
                set[ranPos] = temp;
            }
            if (EightPuzzle.solvable(set)) {
                int temp = solvableDepth(new EightPuzzle(set, 2, 0), new EightPuzzle(goal, 2, 0));
                if (temp < 21) {
                    con = false;
                }
            }
        }
        return set;
    }

    public static boolean contains(Iterator<EightPuzzle> x, EightPuzzle s) {
        while (x.hasNext()) {
            if (x.next().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static int solvableDepth(EightPuzzle start, EightPuzzle goal) {

        LinkedList<EightPuzzle> closedset = new LinkedList<EightPuzzle>();

        PriorityQueue<EightPuzzle> openset = new PriorityQueue<EightPuzzle>();

        openset.add(start);

        while (openset.size() > 0) {

            EightPuzzle x = openset.peek();

            if (x.mapEquals(goal)) {

                return x.getG_n();

            }

            closedset.add(openset.poll());
            LinkedList<EightPuzzle> neighbor = x.getChildren();

            while (neighbor.size() > 0) {
                EightPuzzle y = neighbor.removeFirst();

                if (contains(closedset.iterator(), y)) {

                    continue;
                }

                if (!contains(openset.iterator(), y)) {

                    openset.add(y);

                }

            }
        }
        return 99;
    }

    public static void astar(EightPuzzle start, EightPuzzle goal, long startTime) {
        if (start.inversions() % 2 == 1) {
            System.out.println("Is Unsolvable!");
            return;
        }

        LinkedList<EightPuzzle> closedset = new LinkedList<EightPuzzle>();

        PriorityQueue<EightPuzzle> openset = new PriorityQueue<EightPuzzle>();

        openset.add(start);

        while (openset.size() > 0) {

            EightPuzzle x = openset.peek();

            if (x.mapEquals(goal)) {
                long endTime = (System.currentTimeMillis() - startTime);

                Stack<EightPuzzle> toDisplay = reconstruct(x);
                print(toDisplay);
                System.out.println("G(n): " + x.getG_n() + "\nOpenset size: " + openset.size() + "\nClosedset Size: " + closedset.size() + "\ntime of completion:" + endTime + "(ms)\n");

                return;

            }

            closedset.add(openset.poll());
            LinkedList<EightPuzzle> neighbor = x.getChildren();

            while (neighbor.size() > 0) {
                EightPuzzle y = neighbor.removeFirst();

                if (contains(closedset.iterator(), y)) {

                    continue;
                }

                if (!contains(openset.iterator(), y)) {

                    openset.add(y);

                }

            }

        }
    }

    public static void print(Stack<EightPuzzle> x) {
        while (!x.isEmpty()) {
            EightPuzzle temp = x.pop();
            System.out.println(temp.toString());
        }
    }

    public static Stack<EightPuzzle> reconstruct(EightPuzzle winner) {
        Stack<EightPuzzle> correctOutput = new Stack<EightPuzzle>();

        while (winner.getParent() != null) {
            correctOutput.add(winner);
            winner = winner.getParent();
        }

        return correctOutput;
    }

}
