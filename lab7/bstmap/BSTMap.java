package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private Node root;
    private int size;

    private class Node {
        private K key;
        private V val;
        private Node left, right;

        Node(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public boolean containsKey(K key) {
        return find(root, key) != null;
    }

    private K find(Node x, K key) {
        if (x == null) {
            return null;
        }
        if (key == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return find(x.left, key);
        } else if (cmp > 0) {
            return find(x.right, key);
        } else {
            return x.key;
        }
    }

    @Override
    public V get(K key) {
        return get(root, key);
    }

    private V get(Node x, K key) {
        if (x == null) {
            return null;
        }
        if (key == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return get(x.left, key);
        } else if (cmp > 0) {
            return get(x.right, key);
        } else {
            return x.val;
        }
    }

    @Override
    public int size() {
        if (root == null) {
            return 0;
        } else {
            return size;
        }
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            return;
        }
        root = put(root, key, value);
    }

    private Node put(Node x, K key, V value) {
        if (x == null) {
            x = new Node(key, value);
            size++;
        } else {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) {
                x.left = put(x.left, key, value);
            } else if (cmp > 0) {
                x.right = put(x.right, key, value);
            } else {
                x.val = value;
            }
        }
        return x;
    }

    public void printInOrder() {
        print(root);
    }

    private void print(Node x) {
        if (x == null) {
            return;
        }
        print(x.left);
        System.out.println(x.key.toString() + "-->" + x.val.toString());
        print(x.right);
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
}
