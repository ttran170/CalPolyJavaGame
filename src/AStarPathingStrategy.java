import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{
    private Comparator<Point> compare = (Point a, Point b) -> a.getF()-b.getF();

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        PriorityQueue<Point> open = new PriorityQueue<Point>(compare);
        List<Point> closed = new LinkedList<Point>();
        List<Point> path = new LinkedList<Point>();
        List<Point> reversedPath = new LinkedList<>();
        start.calcF(0,end);
        open.add(start);
        boolean pathFound = false;
        while (!open.isEmpty())
        {
            Point q = open.poll();
            closed.add(q);
            reversedPath.add(0,q);
            List<Point> successors = potentialNeighbors.apply(q)
                    .filter(canPassThrough)
                    .collect(Collectors.toList());
            for (Point succ : successors) {
                if (closed.contains(succ))
                {
                    continue;
                }
                if (succ.adjacent(end)) {
                    succ.setPrev(q);
                    reversedPath.add(0, succ);
                    pathFound = true;
                    break;
                }
                if (!open.contains(succ))
                {
                    succ.calcF(q.getG() + 1, end);
                    succ.setPrev(q);
                    open.add(succ);
                }
            }
            if (pathFound)
            {
                break;
            }
        }
        if (reversedPath.isEmpty() || !reversedPath.get(0).adjacent(end))
        {
            return path;
        }
        Point pt = reversedPath.get(0);
        while (!pt.equals(start))
        {
            path.add(0,pt);
            pt = pt.getPrev();
        }
        return path;

    }
}
