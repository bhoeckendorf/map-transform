package de.uni_heidelberg.cos.agw.ij.transform;

import net.imglib2.RealLocalizable;
import net.imglib2.RealPositionable;
import net.imglib2.realtransform.InverseRealTransform;
import net.imglib2.realtransform.InvertibleRealTransform;

public class PolarToCartesianTransform implements InvertibleRealTransform {

    private final double[] temp = new double[2];
    private InverseRealTransform inverse;

    @Override
    public void apply(final double[] source, final double[] target) {
        polarToCartesian(source[0], source[1], target);
    }

    @Override
    public void apply(final float[] source, final float[] target) {
        polarToCartesian(source[0], source[1], temp);
        for (int d = 0; d < temp.length; ++d) {
            target[d] = (float) temp[d];
        }
    }

    @Override
    public void apply(final RealLocalizable source, final RealPositionable target) {
        polarToCartesian(source.getDoublePosition(0), source.getDoublePosition(1), temp);
        // Manual copy prevents ArrayIndexOutOfBoundsException when extending to >2 dimensions, as in CylindricalCoordinates.
        for (int d = 0; d < temp.length; ++d) {
            target.setPosition(temp[d], d);
        }
    }

    @Override
    public void applyInverse(final double[] source, final double[] target) {
        cartesianToPolar(target[0], target[1], source);
    }

    @Override
    public void applyInverse(final float[] source, final float[] target) {
        cartesianToPolar(target[0], target[1], temp);
        for (int d = 0; d < temp.length; ++d) {
            source[d] = (float) temp[d];
        }
    }

    @Override
    public void applyInverse(final RealPositionable source, final RealLocalizable target) {
        cartesianToPolar(target.getDoublePosition(0), target.getDoublePosition(1), temp);
        // Manual copy prevents ArrayIndexOutOfBoundsException when extending to >2 dimensions, as in CylindricalCoordinates.
        for (int d = 0; d < temp.length; ++d) {
            source.setPosition(temp[d], d);
        }
    }

    private void polarToCartesian(final double radius, final double azimuth, final double[] target) {
        target[0] = radius * Math.cos(azimuth);
        target[1] = radius * Math.sin(azimuth);
    }

    private void cartesianToPolar(final double x, final double y, final double[] target) {
        target[0] = Math.sqrt(x * x + y * y);
        if (x == 0 && y == 0) {
            target[1] = 0;
        } else if (x >= 0) {
            target[1] = Math.asin(y / target[0]);
        } else { // if (x < 0)
            target[1] = -Math.asin(y / target[0]) + Math.PI;
        }
        if (target[1] < 0) {
            target[1] += 2d * Math.PI;
        }
    }

    @Override
    public int numSourceDimensions() {
        return 2;
    }

    @Override
    public int numTargetDimensions() {
        return 2;
    }

    @Override
    public InvertibleRealTransform inverse() {
        if (inverse == null) {
            inverse = new InverseRealTransform(this);
        }
        return inverse;
    }

    @Override
    public InvertibleRealTransform copy() {
        return this;
    }
}
