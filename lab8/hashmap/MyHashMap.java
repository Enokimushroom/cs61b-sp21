package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private static int CAPACITY = 16;
    private static double MAX_LOAD = 0.75;
    private int size;
    private Collection<Node>[] buckets;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        this(CAPACITY, MAX_LOAD);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, MAX_LOAD);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        CAPACITY = initialSize;
        MAX_LOAD = maxLoad;
        size = 0;
        buckets = createTable(CAPACITY);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i] = createBucket();
        }
        return table;
    }

    @Override
    public void clear() {
        size = 0;
        buckets = createTable(CAPACITY);
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        return getNode(buckets, key) != null;
    }

    private Node getNode(Collection<Node>[] buckets, K key) {
        if (buckets == null || key == null) {
            throw new IllegalArgumentException();
        }
        int i = hash(key);
        for (Node node : buckets[i]) {
            if (node.key.equals(key)) {
                return node;
            }
        }
        return null;
    }

    private int hash(K key) {
        return hash(buckets, key);
    }

    private int hash(Collection<Node>[] buckets, K key) {
        return Math.floorMod(key.hashCode(), buckets.length);
    }

    @Override
    public V get(K key) {
        return get(buckets, key);
    }

    private V get(Collection<Node>[] buckets, K key) {
        if (buckets == null || key == null) {
            throw new IllegalArgumentException();
        }
        Node node = getNode(buckets, key);
        if (node != null) {
            return node.value;
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        double load = (double) size / buckets.length;
        if (load >= MAX_LOAD) {
            resize(buckets.length * 2);
        }
        Node node = getNode(buckets, key);
        if (node == null) {
            int i = hash(key);
            buckets[i] = put(buckets[i], key, value);
        } else {
            node.value = value;
        }
    }

    private Collection<Node> put(Collection<Node> bucket, K key, V value) {
        Node node = createNode(key, value);
        bucket.add(node);
        size++;
        return bucket;
    }

    private void resize(int capacity) {
        Collection<Node>[] newBuckets = createTable(capacity);
        Iterator<Node> nodeIter = new nodeIterator();
        while (nodeIter.hasNext()) {
            Node node = nodeIter.next();
            int i = hash(newBuckets, node.key);
            newBuckets[i].add(node);
        }
        buckets = newBuckets;
    }

    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        Iterator<Node> nodeIter = new nodeIterator();
        while (nodeIter.hasNext()) {
            Node node = nodeIter.next();
            set.add(node.key);
        }
        return set;
    }

    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        Node node = getNode(buckets, key);
        if (node == null) {
            return null;
        }
        V val = node.value;
        int i = hash(key);
        buckets[i].remove(node);
        return val;
    }

    @Override
    public V remove(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        Node node = getNode(buckets, key);
        if (node == null) {
            return null;
        }
        V val = node.value;
        int i = hash(key);
        if (val.equals(value)) {
            buckets[i].remove(node);
            return val;
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return new MyhashmapIter();
    }

    private class MyhashmapIter implements Iterator<K> {

        private nodeIterator nodeIter = new nodeIterator();

        @Override
        public boolean hasNext() {
            return nodeIter.hasNext();
        }

        @Override
        public K next() {
            return nodeIter.next().key;
        }
    }

    private class nodeIterator implements Iterator<Node> {

        private final Iterator<Collection<Node>> bucketIter = Arrays.stream(buckets).iterator();
        private Iterator<Node> curIter;
        private int nodeNum = size;

        @Override
        public boolean hasNext() {
            return nodeNum > 0;
        }

        @Override
        public Node next() {
            if (curIter == null  || !curIter.hasNext()) {
                Collection<Node> curBucket = bucketIter.next();
                while (curBucket.size() == 0) {
                    curBucket = bucketIter.next();
                }
                curIter = curBucket.iterator();
            }
            nodeNum--;
            return curIter.next();
        }
    }

}
