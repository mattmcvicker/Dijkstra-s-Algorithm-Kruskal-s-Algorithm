package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;
    Map<T, Integer> map;
    int indexTracker;
    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public UnionBySizeCompressingDisjointSets() {
        this.map = new HashMap<T, Integer>();
        this.pointers = new ArrayList<Integer>();
        this.indexTracker = 0;
    }

    @Override
    public void makeSet(T item) {
        map.put(item, indexTracker);
        pointers.add(indexTracker, -1);
        indexTracker++;
    }

    @Override
    public int findSet(T item) {
        if (!map.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        int position = map.get(item); //position of item passed in
        if (pointers.get(position) > -1) {
            Set<Integer> savePosition = new HashSet<Integer>();
            savePosition.add(position); //save the position of the item passed in to the hashset
            while (pointers.get(position) >= 0) {
                position = pointers.get(position); //update current position to parent's index
                if (!(pointers.get(position) < 0)) {
                    savePosition.add(position);
                }
            }
            for (int a : savePosition) {
                pointers.set(a, position);
                pointers.set(position, pointers.get(position) - 1);
            }
        }
        return position;
    }

    @Override
    public boolean union(T item1, T item2) {
        int one = findSet(item1); //find parents of both item1 and item 2
        int two = findSet(item2);
        if (pointers.get(map.get(item1)) == pointers.get(map.get(item2))
            && pointers.get(map.get(item1)) > -1 && pointers.get(map.get(item2)) > -1) {
            return false;
        } else if (item1 == item2) {
            return false;
        } else if (pointers.get(map.get(item1)) == two ||
                    pointers.get(map.get(item2)) == one) {
            return false;
        }
        int oldSize = 0;
        if (pointers.get(one) < pointers.get(two)) {
            oldSize = pointers.get(two);
            pointers.set(two, one);
            pointers.set(one, (pointers.get(one) + oldSize));
        } else {
            oldSize = pointers.get(one);
            pointers.set(one, two);
            pointers.set(two, (pointers.get(two) + oldSize));
        }
        return true;
    }
}
