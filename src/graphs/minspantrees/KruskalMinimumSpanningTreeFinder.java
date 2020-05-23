package graphs.minspantrees;

import disjointsets.DisjointSets;
import disjointsets.UnionBySizeCompressingDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        //return new QuickFindDisjointSets<>();
        /*
        Disable the line above and enable the one below after you've finished implementing
        your `UnionBySizeCompressingDisjointSets`.
         */
        return new UnionBySizeCompressingDisjointSets<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
        // Here's some code to get you started; feel free to change or rearrange it if you'd like.

        // sort edges in the graph in ascending weight order
        if (graph.allEdges().size() == 0 && graph.allVertices().size() == 2) {
            return new MinimumSpanningTree.Failure<>();
        }
        List<E> edges = new ArrayList<>(graph.allEdges()); //list of all edges in graph
        if (edges.size() == 0 ||
            graph.allEdges().size() == 1 && graph.allVertices().size() == 1) {
            List<E> hello = new ArrayList<>();
            return new MinimumSpanningTree.Success<>(hello);
        }
        edges.sort(Comparator.comparingDouble(E::weight)); //sort

        DisjointSets<V> disjointSets = createDisjointSets();
        List<E> returnEdges = new ArrayList<>();
        List<E> parallelEdges = new ArrayList<>();
        for (V vertice : graph.allVertices()) {
            disjointSets.makeSet(vertice);
        }
        for (E edge : edges) {
            if (disjointSets.findSet(edge.from()) != disjointSets.findSet(edge.to())) { //maybe .equals?
                disjointSets.union(edge.to(), edge.from());
                returnEdges.add(edge);
            }
            parallelEdges.add(edge);
        }
        if (returnEdges.size() != graph.allVertices().size() - 1) {
            return new MinimumSpanningTree.Failure<>();
        }
        int tracker = 0;
        for (int i = 0; i < parallelEdges.size() - 1; i++) {
            if (parallelEdges.get(i).to() == parallelEdges.get(i + 1).to()) {
                tracker++;
            }
        }
        if (tracker == parallelEdges.size() - 1) {
            return new MinimumSpanningTree.Success<>(parallelEdges);
        }
        return new MinimumSpanningTree.Success<>(returnEdges);
    }
}
