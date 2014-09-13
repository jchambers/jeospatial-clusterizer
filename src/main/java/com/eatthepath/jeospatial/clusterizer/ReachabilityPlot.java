package com.eatthepath.jeospatial.clusterizer;

import java.util.ArrayList;
import java.util.List;

public class ReachabilityPlot<T> {

    private final List<ReachabilityPlotEntry<T>> entries;

    protected ReachabilityPlot() {
        this(new ArrayList<ReachabilityPlotEntry<T>>());
    }

    protected ReachabilityPlot(List<ReachabilityPlotEntry<T>> entries) {
        this.entries = new ArrayList<ReachabilityPlotEntry<T>>(entries);
    }

    protected void add(final T point, final double reachabilityDistance) {
        this.entries.add(new ReachabilityPlotEntry<T>(point, reachabilityDistance));
    }

    protected List<ReachabilityPlotEntry<T>> getEntries() {
        return this.entries;
    }

    public int size() {
        return this.entries.size();
    }

    public List<Cluster<T>> getClustersWithReachabilityDistanceThreshold(double threshold) {
        final ArrayList<Cluster<T>> clusters = new ArrayList<Cluster<T>>();
        final ArrayList<ReachabilityPlotEntry<T>> collectedEntries = new ArrayList<ReachabilityPlotEntry<T>>();

        for (final ReachabilityPlotEntry<T> entry : this.entries) {
            if (entry.getReachabilityDistance() > threshold) {
                if (!collectedEntries.isEmpty()) {
                    clusters.add(new Cluster<T>(collectedEntries, threshold));
                }

                collectedEntries.clear();
            }

            collectedEntries.add(entry);
        }

        clusters.add(new Cluster<T>(collectedEntries, threshold));

        return clusters;
    }
}
