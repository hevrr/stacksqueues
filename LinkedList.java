import java.util.Iterator;

public class LinkedList<Item> implements Iterable<Item> {

    /* variables */
    private Node top;
    private int size;

    /* constructor */
    public LinkedList() {
        top = null;
        size = 0;
    }

    /* adds item */
    public void add(Item item) {
        if (top == null) {
            top = new Node();
            top.item = item;
        } else {
            Node temp = top;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = new Node();
            temp.next.item = item;
        }
        size++;
    }

    /* gets item at index */
    public Item get(int index) {
        if (size() - 1 < index) {
            System.out.println("Out of bounds.");
        } else {
            Node temp = top;
            for (int i = 0; i < index; i++)
                temp = temp.next;
            return temp.item;
        }
        return null;
    }

    /* sets item at index */
    public void set(int index, Item item) {
        if (size() - 1 < index) {
            System.out.println("Out of bounds.");
        } else {
            Node temp = top;
            for (int i = 0; i < index; i++)
                temp = temp.next;
            temp.item = item;
        }
    }

    /* clears list */
    public void clear() {
        top = null;
    }

    /* returns size */
    public int size() {
        return size;
    }

    /* if is empty */
    public Boolean isEmpty() {
        return top == null;
    }

    /* iterator */
    @Override
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {

        private Node temp = top;

        public boolean hasNext() {
            return temp != null;
        }

        public void remove() {
        }

        public Item next() {
            Item item = temp.item;
            temp = temp.next;
            return item;
        }
    }

    /* wrapper for recursive reversal */
    public void recursivelyReverse() {
        top = recursivelyReverse(top);
    }

    /* wrapper for iterative reversal */
    public void iterativelyReverse() {
        top = iterativelyReverse(top);
    }

    /* reverses iteratively */
    private Node iterativelyReverse(Node current) {
        Node previous = null;
        Node next = null;
        while (current != null) {
            next = current.next;
            current.next = previous;
            previous = current;
            current = next;
        }
        return previous;
    }

    /* reverses recursively */
    private Node recursivelyReverse(Node n) {
        if (n != null && n.next == null)
            return n;
        else if (n != null) {
            Node temp = recursivelyReverse(n.next);
            n.next.next = n;
            n.next = null;
            return temp;
        }
        return null;
    }

    /* prints in string */
    public String toString() {
        String returned = "";
        Node temp = top;
        while (temp != null) {
            returned += temp.item + " ";
            temp = temp.next;
        }
        return returned;
    }

    /* node */
    private class Node {
        Item item;
        Node next;
    }
}
