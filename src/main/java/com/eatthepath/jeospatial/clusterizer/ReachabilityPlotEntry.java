package com.eatthepath.jeospatial.clusterizer;

import com.eatthepath.jeospatial.GeospatialPoint;

public class ReachabilityPlotEntry<T extends GeospatialPoint> {

    private final T point;
    private final Double reachabilityDistance;

    public ReachabilityPlotEntry(final T point, final Double reachabilityDistance) {
        this.point = point;
        this.reachabilityDistance = reachabilityDistance;
    }
}
