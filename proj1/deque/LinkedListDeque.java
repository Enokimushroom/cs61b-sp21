package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private class Node {
        private Node prev;
        private final T item;
        private Node next;

        Node(Node m, T i, Node n) {
            prev = m;
            item = i;
            next = n;
        }
    }

    private final Node sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel.next = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(T item) {
        Node temp = sentinel.next;
        sentinel.next = new Node(sentinel, item, temp);
        temp.prev = sentinel.next;
        size += 1;
    }

    @Override
    public void addLast(T item) {
        Node temp = sentinel.prev;
        sentinel.prev.next = new Node(temp, item, sentinel);
        sentinel.prev = temp.next;
        size += 1;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        } else {
            T item = sentinel.next.item;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            size -= 1;
            return item;
        }
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        } else {
            T item = sentinel.prev.item;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
            size -= 1;
            return item;
        }
    }

    @Override
    public T get(int index) {
        if (index <= size / 2) {
            int i = 0;
            Node p = sentinel.next;
            while (i < index) {
                p = p.next;
                i++;
            }
            return p.item;
        } else {
            int i = 0;
            Node p = sentinel.prev;
            while (i < size - index - 1) {
                p = p.prev;
                i++;
            }
            return p.item;
        }
    }

    public Iterator<T> iterator() {
        return new LinkedIterator();
    }

    private class LinkedIterator implements Iterator<T> {
        private int pos;

        LinkedIterator() {
            pos = 0;
        }

        public boolean hasNext() {
            return pos < size;
        }

        public T next() {
            T nextItem = get(pos);
            pos++;
            return nextItem;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof LinkedListDeque)) {
            return false;
        }
        LinkedListDeque<?> p = (LinkedListDeque<?>) o;
        if (p.size() != size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (p.get(i) != get(i)) {
                return false;
            }
        }
        return true;
    }

    public T getRecursive(int index) {
        return help(index, sentinel.next);
    }

    /** help getRecursive to find item using recursive method. */
    private T help(int index, Node p) {
        if (index == 0) {
            return p.item;
        }
        return help(index - 1, p.next);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        Node p = sentinel;
        while (p.next != sentinel) {
            p = p.next;
            System.out.print(p.item + " ");
        }
        System.out.println();
    }

    private static void main(String[] args) {
        LinkedListDeque<Integer> l = new LinkedListDeque<>();
        l.addFirst(3);
        l.addLast(4);
        l.addFirst(5);
        System.out.println(l.size());
        System.out.println(l.getRecursive(2));
        System.out.println(l.removeFirst());
        System.out.println(l.removeLast());
    }
}
