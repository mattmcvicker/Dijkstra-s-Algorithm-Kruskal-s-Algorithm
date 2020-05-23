package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see ShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    implements ShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        }

    @Override
    public ShortestPath<V, E> findShortestPath(G graph, V start, V end) {
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }

        ExtrinsicMinPQ<V> storage = createMinPQ();
        HashMap<V, Double> distToV = new HashMap<>();
        HashMap<V, E> edgeToV = new HashMap<>();
        HashSet<V> p = new HashSet<>();
        HashSet<V> v = new HashSet<>();
        storage.add(start, 0);
        distToV.put(start, 0.0);
        v.add(start);
        while (!storage.isEmpty()) {
            V u = storage.removeMin();
            for (E edge : graph.outgoingEdgesFrom(u)) {
                double oldDistance = 0.0;
                double newDistance = 0.0;
                V to = edge.to();
                if (!p.contains(to)) {
                    if (!v.contains(to)) {
                        newDistance = edge.weight() + distToV.get(u);
                        v.add(to);
                        distToV.put(to, newDistance);
                        storage.add(to, newDistance);
                        edgeToV.put(to, edge);
                    } else {
                        newDistance = distToV.get(u) + edge.weight();
                        oldDistance = distToV.get(to);
                        if (newDistance < oldDistance) {
                            distToV.put(to, newDistance);
                            edgeToV.put(to, edge);
                            storage.changePriority(to, newDistance);
                        }
                    }
                }
            }
            p.add(u);
            if (p.contains(end)) {
                break;
            }
        }
        List<E> list = new ArrayList<>();
        if (p.contains(end)) {
            list.add(0, edgeToV.get(end));
            V vertex = edgeToV.get(end).from();
            while (!vertex.equals(start)) {
                list.add(0, edgeToV.get(vertex));
                vertex = edgeToV.get(vertex).from();
            }
            return new ShortestPath.Success<>(list);
        }
        return new ShortestPath.Failure<>();
    }
}
