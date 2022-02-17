package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int size;
    private int firstPos;
    private int lastPos;
    private int defaultCap = 8;

    public ArrayDeque() {
        items = (T[]) new Object[defaultCap];
        firstPos = items.length / 2;
        lastPos = items.length / 2 - 1;
        size = 0;
    }

    private void resize(int cap) {
        T[] a = (T[]) new Object[cap];
        int des = (cap - size) / 2;
        int count = des;
        for (int i = firstPos; count < des + size; i = (i + 1) % items.length) {
            a[count] = items[i];
            count++;
        }
        items = a;
        firstPos = des;
        lastPos = des + size - 1;
    }

    @Override
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        firstPos = (firstPos - 1 + items.length) % items.length;
        items[firstPos] = item;
        size++;
    }

    @Override
    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        lastPos = (lastPos + 1) % items.length;
        items[lastPos] = item;
        size++;
    }

    @Override
    public T get(int index) {
        int position = (firstPos + index) % items.length;
        return items[position];
    }

    public Iterator<T> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<T> {
        private int pos;

        public ArrayIterator() {
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
        if (getClass() != o.getClass()) {
            return false;
        }
        ArrayDeque<T> p = (ArrayDeque<T>) o;
        if (p.size() != size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
           if (p.get(i) != get(i)) {
               return false;
           }
        }
        return true;
    }

    @Override
    public T removeFirst() {
        if (size < items.length / 4 && size > defaultCap) {
            resize(items.length / 4);
        }
        if (isEmpty()) {
            return null;
        } else {
            size--;
            T x = get(firstPos);
            items[firstPos] = null;
            firstPos = (firstPos + 1) % items.length;
            return x;
        }
    }

    @Override
    public T removeLast() {
        if (size < items.length / 4 && size > defaultCap) {
            resize(items.length / 4);
        }
        if (isEmpty()) {
            return null;
        } else {
            size--;
            T x = get(lastPos);
            items[lastPos] = null;
            lastPos = (lastPos - 1 + items.length) % items.length;
            return x;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        int count = 0;
        for (int i = firstPos; count < size; i = (i + 1) % items.length) {
            System.out.print(items[i] + " ");
            count++;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        ArrayDeque<Integer> l = new ArrayDeque<>();
        //for (int i = 0; i < 20; i++) {
        //    l.addLast(i);
        //}
        //System.out.println(l.removeFirst());
        l.addFirst(3);
        l.addLast(4);
        l.addFirst(5);
        l.addFirst(6);
        l.addLast(8);
        System.out.println(l.get(4));
        //l.printDeque();
    }
}
