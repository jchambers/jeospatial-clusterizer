package com.eatthepath.jeospatial.clusterizer;

class ReachabilityPlotEntry<T> {
    private final T point;
    private final double reachabilityDistance;

    public ReachabilityPlotEntry(final T point, final double reachabilityDistance) {
        this.point = point;
        this.reachabilityDistance = reachabilityDistance;
    }

    public T getPoint() {
        return this.point;
    }

    public double getReachabilityDistance() {
        return this.reachabilityDistance;
    }
}
