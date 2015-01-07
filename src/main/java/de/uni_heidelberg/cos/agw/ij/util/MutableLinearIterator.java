package de.uni_heidelberg.cos.agw.ij.util;

import java.util.Iterator;

public class MutableLinearIterator implements Iterable<double[]> {

    private final int nDims;
    private final double[] start, end, current, step;
    private long totalSteps, currentStep;
    private Iterator<double[]> it;

    public MutableLinearIterator(final int nDimensions) {
        nDims = nDimensions;
        start = new double[nDims];
        end = new double[nDims];
        current = new double[nDims];
        step = new double[nDims];
        currentStep = 0;
    }

    public void set(final double[] start, final double[] end) {
        System.arraycopy(start, 0, this.start, 0, nDims);
        System.arraycopy(start, 0, current, 0, nDims);
        System.arraycopy(end, 0, this.end, 0, nDims);
        computeStep();
        currentStep = 0;
    }

    private void computeStep() {
        for (int i = 0; i < nDims; ++i) {
            step[i] = end[i] - start[i];
        }
        double max = step[0] > 0 ? step[0] : -step[0];
        for (int i = 1; i < nDims; ++i) {
            double next = step[i] > 0 ? step[i] : -step[i];
            if (next > max) {
                max = next;
            }
        }
        totalSteps = Math.round(max + .5);
        for (int i = 0; i < nDims; ++i) {
            step[i] /= totalSteps;
            current[i] -= step[i];
        }
        totalSteps++;
    }

    public double getNRemainingSteps() {
        return totalSteps - currentStep;
    }

    @Override
    public Iterator<double[]> iterator() {
        if (it == null) {
            it = new Iterator<double[]>() {
                @Override
                public boolean hasNext() {
                    return getNRemainingSteps() > 0;
                }

                @Override
                public double[] next() {
                    for (int i = 0; i < nDims; ++i) {
                        current[i] += step[i];
                    }
                    currentStep++;
                    return current;
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException(".iterator().remove() is not supported.");
                }
            };
        }
        return it;
    }
}
