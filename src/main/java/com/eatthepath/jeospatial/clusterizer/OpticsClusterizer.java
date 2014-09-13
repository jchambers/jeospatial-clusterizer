package com.eatthepath.jeospatial.clusterizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;

import com.eatthepath.jeospatial.GeospatialIndex;
import com.eatthepath.jeospatial.GeospatialPoint;
import com.eatthepath.jeospatial.HaversineDistanceFunction;
import com.eatthepath.jeospatial.VPTreeGeospatialPointIndex;


public class OpticsClusterizer <E extends GeospatialPoint> {

    private final int minimumPointsInCluster;
    private final double epsilon;

    private static final HaversineDistanceFunction DISTANCE_FUNCTION = new HaversineDistanceFunction();

    private class ReachabilityQueueEntry implements Comparable<ReachabilityQueueEntry> {
        private final E point;
        private final double reachabilityDistance;

        public ReachabilityQueueEntry(final E point, final double reachabilityDistance) {
            this.point = point;
            this.reachabilityDistance = reachabilityDistance;
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

    public List<E> getOrderedPoints(final Collection<E> points) {
        final GeospatialIndex<E> index = new VPTreeGeospatialPointIndex<E>(points);

        final IdentityHashMap<E, Object> processedPoints = new IdentityHashMap<E, Object>(points.size());
        final List<E> orderedPoints = new ArrayList<E>(points.size());

        for (final E point : points) {
            if (!processedPoints.containsKey(point)) {
                final List<E> neighbors = index.getAllWithinRange(point, this.epsilon);

                processedPoints.put(point, null);
                orderedPoints.add(point);
            }
        }

        return orderedPoints;
    }

    private Double getCoreDistance(final E point, final GeospatialIndex<E> index) {
        final List<E> neighbors = index.getAllWithinRange(point, epsilon);

        return (neighbors.size() < minimumPointsInCluster) ?
                null : DISTANCE_FUNCTION.getDistance(point, neighbors.get(minimumPointsInCluster - 1));
    }
}
