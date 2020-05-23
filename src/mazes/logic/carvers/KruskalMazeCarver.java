package mazes.logic.carvers;

import graphs.EdgeWithData;
import graphs.minspantrees.MinimumSpanningTree;
import graphs.minspantrees.MinimumSpanningTreeFinder;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.logic.MazeGraph;



import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver extends MazeCarver {
    MinimumSpanningTreeFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder;
    private final Random rand;

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random();
    }

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder,
                             long seed) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random(seed);
    }

    protected Set<Wall> chooseWallsToRemove(Set<Wall> walls) { //takes in edges representing walls
        HashSet<Wall> wally = new HashSet<Wall>();
        List<EdgeWithData<Room, Wall>> edges = walls.stream()
            .map(wall -> new EdgeWithData<>(wall.getRoom1(), wall.getRoom2(), rand.nextDouble(), wall))
            .collect(Collectors.toList());
       MinimumSpanningTree<Room, EdgeWithData<Room, Wall>> hello = this.minimumSpanningTreeFinder
           .findMinimumSpanningTree(new MazeGraph(edges)); //Collection<EdgeWithData<Room, Wall>>
        for (EdgeWithData<Room, Wall> e:hello.edges()) {
            wally.add(e.data());
        }
        return wally;
    }
}
