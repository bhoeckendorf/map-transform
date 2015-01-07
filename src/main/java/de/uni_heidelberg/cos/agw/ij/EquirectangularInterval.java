package de.uni_heidelberg.cos.agw.ij;

import net.imglib2.Interval;
import net.imglib2.Positionable;
import net.imglib2.RealPositionable;

public class EquirectangularInterval implements Interval {

    private final long[] dimensions;

    public EquirectangularInterval(final double innerRadius, final double outerRadius, final double stdRadiusOffset, final double scale) {
        dimensions = new long[numDimensions()];
        dimensions[0] = Math.round(scale * stdRadiusOffset * 2 * Math.PI); // 0-2PI, excl. 2PI
        dimensions[1] = Math.round(scale * stdRadiusOffset * Math.PI) + 1; // 0-PI, incl. PI
        dimensions[2] = Math.round(scale * (outerRadius - innerRadius + 1));
    }

    @Override
    public long min(final int d) {
        return 0;
    }

    @Override
    public void min(final long[] min) {
        for (int d = 0; d < numDimensions(); ++d) {
            min[d] = min(d);
        }
    }

    @Override
    public void min(final Positionable min) {
        for (int d = 0; d < numDimensions(); ++d) {
            min.setPosition(min(d), d);
        }
    }

    @Override
    public long max(final int d) {
        return dimensions[d];
    }

    @Override
    public void max(final long[] max) {
        for (int d = 0; d < numDimensions(); ++d) {
            max[d] = max(d);
        }
    }

    @Override
    public void max(final Positionable max) {
        for (int d = 0; d < numDimensions(); ++d) {
            max.setPosition(max(d), d);
        }
    }

    @Override
    public void dimensions(final long[] dimensions) {
        for (int d = 0; d < numDimensions(); ++d) {
            dimensions[d] = dimension(d);
        }
    }

    @Override
    public long dimension(final int d) {
        return max(d) - min(d);
    }

    @Override
    public double realMin(final int d) {
        return 0;
    }

    @Override
    public void realMin(final double[] min) {
        for (int d = 0; d < numDimensions(); ++d) {
            min[d] = realMin(d);
        }
    }

    @Override
    public void realMin(final RealPositionable min) {
        for (int d = 0; d < numDimensions(); ++d) {
            min.setPosition(realMin(d), d);
        }
    }

    @Override
    public double realMax(final int d) {
        return dimensions[d];
    }

    @Override
    public void realMax(final double[] max) {
        for (int d = 0; d < numDimensions(); ++d) {
            max[d] = realMax(d);
        }
    }

    @Override
    public void realMax(final RealPositionable max) {
        for (int d = 0; d < numDimensions(); ++d) {
            max.setPosition(realMax(d), d);
        }
    }

    @Override
    public int numDimensions() {
        return 3;
    }
}
