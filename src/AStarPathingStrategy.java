import java.awt.geom.Point2D;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        List<Point> path = new LinkedList<Point>();

        HashMap<Integer, Point> open = new HashMap<>();
        ArrayList closed = new ArrayList();


        Point current = start;
        open.put(current.hashCode(), current);


        while (open.size() > 0){

            if (current.adjacent(end)) {
                //  System.out.println("IN HERE");
                path.add(current);


                Point before = current.prev;
                while (!(before.equals(start))) {
                    path.add(before);
                    before = before.prev;
                    if ( before == null){ break;}

                }

                for (int x = 0, y = path.size() - 1; x < y; x++) {
                    path.add(x, path.remove(y));
                }
                return path;
            }


            List<Point> near = potentialNeighbors.apply(current).filter(p -> !(closed.contains(p))).filter(canPassThrough).collect(Collectors.toList());




            closed.add(current);

            for (Point p: near){

                p.g = current.g + 1;


                if (!(open.containsKey(p.hashCode()))){
                    //                System.out.println("HERE");
                    p.h = (int) Point2D.distance(p.x, p.y, end.x, end.y);

                    p.f = p.g + p.h;
                    //   current = p.prior;
                    p.prev = current;
                    open.put(p.hashCode(), p);
                }
                else if(open.get(p.hashCode()).g < p.g) {
                        p.h = (int) Point2D.distance(p.x, p.y, end.x, end.y);

                        p.f = p.g + p.h;

                        open.put(p.hashCode(), p);
                        p.prev = current;



                }


            }


            open.remove(current.hashCode());

            if (open.size() == 0){
                break;
            }

            Set<Integer> ans = open.keySet();


            Point smallest = new Point(1, 1);
            smallest.f = 99999999;


            for (int key : ans){
                if (open.get(key).f < smallest.f){
                    smallest = new Point(1, 1);
                    smallest.f = open.get(key).f;
                    current = open.get(key);
                }


                //          System.out.println(closed);
            }






        }





        return path;
    }
}
