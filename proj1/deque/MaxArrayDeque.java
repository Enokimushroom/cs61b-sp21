package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T>{
    private final Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        comparator = c;
    }

    public T max() {
        return max(comparator);
    }

   public T max(Comparator<T> c) {
       if (isEmpty()) {
           return null;
       }
       int maxDex = 0;
       for (int i = 0; i < size(); i++) {
           if (c.compare(get(i), get(maxDex)) > 0) {
               maxDex = i;
           }
       }
       return get(maxDex);
   }

   @Override
    public boolean equals(Object o) {
       if (this == o) {
           return true;
       }
       if (o == null) {
           return false;
       }
       if (!(o instanceof MaxArrayDeque)) {
           return false;
       }
        if (((MaxArrayDeque<T>) o).max() != max()) {
            return false;
        }
       return super.equals(o);
    }
}
