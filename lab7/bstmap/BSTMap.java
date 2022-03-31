package bstmap;

import java.util.HashSet;
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
        Set<K> set = new HashSet<>();
        for (K key : this) {
            set.add(key);
        }
        return set;
    }

    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (containsKey(key)) {
            size--;
            root = remove(root, key);
            return get(key);
        }
        return null;
    }

    @Override
    public V remove(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (containsKey(key)) {
            V v = get(key);
            if (v.equals(value)) {
                size--;
                root = remove(root, key);
                return v;
            }
        }
        return null;
    }

    private Node remove(Node node, K key) {
        if (node == null) {
            return null;
        }
        if (key == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key);
        } else if (cmp > 0) {
            node.right = remove(node.right, key);
        } else {
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            Node curNode = node;
            node = getSwapNode(node.right);
            node.left = curNode.left;
            node.right = remove(curNode.right, node.key);
        }
        return node;
    }

    private Node getSwapNode(Node node) {
        if (node.left == null) {
            return node;
        }
        return getSwapNode(node.left);
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
