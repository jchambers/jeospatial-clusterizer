package com.eatthepath.jeospatial.clusterizer;

import java.util.Iterator;
import java.util.List;


public class Cluster<T> extends ReachabilityPlot<T> implements Iterable<T> {

    private final double threshold;

    public Cluster(final List<ReachabilityPlotEntry<T>> entries, final double threshold) {
        super(entries);

        this.threshold = threshold;
    }

    public double getThreshold() {
        return this.threshold;
    }

    public Iterator<T> iterator() {
        final Iterator<ReachabilityPlotEntry<T>> entryIterator = this.getEntries().iterator();

        return new Iterator<T>() {

            public boolean hasNext() {
                return entryIterator.hasNext();
            }

            public T next() {
                return entryIterator.next().getPoint();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
