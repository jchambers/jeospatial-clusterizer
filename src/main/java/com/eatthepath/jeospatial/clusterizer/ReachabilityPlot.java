package com.eatthepath.jeospatial.clusterizer;

import java.util.ArrayList;
import java.util.List;

public class ReachabilityPlot<T> {

    private final List<ReachabilityPlotEntry> entries;

    private class ReachabilityPlotEntry {
        private final T point;
        private final Double reachabilityDistance;

        public ReachabilityPlotEntry(final T point, final Double reachabilityDistance) {
            this.point = point;
            this.reachabilityDistance = reachabilityDistance;
        }
    }

    public ReachabilityPlot() {
        this.entries = new ArrayList<ReachabilityPlotEntry>();
    }

    public void add(final T point, final double reachabilityDistance) {
        this.entries.add(new ReachabilityPlotEntry(point, reachabilityDistance));
    }
}
