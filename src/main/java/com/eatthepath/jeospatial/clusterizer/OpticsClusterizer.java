package com.eatthepath.jeospatial.clusterizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.PriorityQueue;

import com.eatthepath.jeospatial.GeospatialIndex;
import com.eatthepath.jeospatial.GeospatialPoint;
import com.eatthepath.jeospatial.HaversineDistanceFunction;
import com.eatthepath.jeospatial.VPTreeGeospatialPointIndex;


public class OpticsClusterizer <E extends GeospatialPoint> {

    private final int minimumPointsInCluster;
    private final double epsilon;

    private class ReachabilityQueueEntry implements Comparable<ReachabilityQueueEntry> {
        private final E point;
        private final double reachabilityDistance;

        public ReachabilityQueueEntry(final E point, final double reachabilityDistance) {
            this.point = point;
            this.reachabilityDistance = reachabilityDistance;
        }

        public E getPoint() {
            return this.point;
        }

        public double getReachabilityDistance() {
            return this.reachabilityDistance;
        }

        public int compareTo(final ReachabilityQueueEntry otherEntry) {
            if (this.getReachabilityDistance() < otherEntry.getReachabilityDistance()) {
                return -1;
            } else if (this.getReachabilityDistance() > otherEntry.getReachabilityDistance()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public OpticsClusterizer(final int minimumPointsInCluster, final double epsilon) {
        this.minimumPointsInCluster = minimumPointsInCluster;
        this.epsilon = epsilon;
    }

    public List<E> getOrderedPoints(final Collection<E> points, final GeospatialIndex<E> index) {
        final HaversineDistanceFunction distanceFunction = new HaversineDistanceFunction();

        final IdentityHashMap<E, Object> processedPoints = new IdentityHashMap<E, Object>(points.size());
        final IdentityHashMap<E, ReachabilityQueueEntry> queueEntries =
                new IdentityHashMap<E, ReachabilityQueueEntry>(points.size());

        final List<E> orderedPoints = new ArrayList<E>(points.size());

        for (final E point : points) {
            if (!processedPoints.containsKey(point)) {

                final PriorityQueue<ReachabilityQueueEntry> queue = new PriorityQueue<ReachabilityQueueEntry>();
                queue.add(new ReachabilityQueueEntry(point, Double.POSITIVE_INFINITY));

                while (!queue.isEmpty()) {
                    final ReachabilityQueueEntry queueEntry = queue.poll();

                    processedPoints.put(queueEntry.getPoint(), null);
                    orderedPoints.add(queueEntry.getPoint());
                    queueEntries.remove(queueEntry).getPoint();

                    final List<E> neighbors = index.getAllWithinRange(queueEntry.getPoint(), this.epsilon);
                    neighbors.remove(queueEntry.getPoint());

                    if (neighbors.size() >= this.minimumPointsInCluster) {
                        final double coreDistance = distanceFunction.getDistance(
                                queueEntry.getPoint(), neighbors.get(this.minimumPointsInCluster - 1));

                        for (final E neighbor : neighbors) {
                            if (!processedPoints.containsKey(neighbor)) {
                                final double reachabilityDistance = Math.max(
                                        coreDistance, distanceFunction.getDistance(queueEntry.getPoint(), neighbor));

                                if (queueEntries.containsKey(neighbor)) {
                                    queue.remove(queueEntries.get(neighbor));
                                }

                                final ReachabilityQueueEntry entry = new ReachabilityQueueEntry(neighbor, reachabilityDistance);

                                queueEntries.put(neighbor, entry);
                                queue.add(entry);
                            }
                        }
                    }
                }
            }
        }

        return orderedPoints;
    }
}
