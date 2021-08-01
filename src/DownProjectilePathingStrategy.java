import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DownProjectilePathingStrategy extends ProjectilePathingStrategy
{

    @Override
    public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough, BiPredicate<Point, Point> withinReach, Function<Point, Stream<Point>> potentialNeighbors) {
        Point nextPos = new Point(start.getX(), (start.getY() + 1));
        List<Point> nPos = new LinkedList<Point>();
        nPos.add(nextPos);
        return nPos;
    }
}
