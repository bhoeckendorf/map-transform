package de.uni_heidelberg.cos.agw.ij.transform;

import net.imglib2.RealLocalizable;
import net.imglib2.RealPositionable;
import net.imglib2.realtransform.InverseRealTransform;
import net.imglib2.realtransform.InvertibleRealTransform;

public class SphericalToCartesianTransform implements InvertibleRealTransform {

    private final double[] temp = new double[3];
    private InverseRealTransform inverse;

    @Override
    public void apply(final float[] source, final float[] target) {
        sphericalToCartesian(source[0], source[1], source[2], temp);
        for (int i = 0; i < temp.length; ++i) {
            target[i] = (float) temp[i];
        }
    }

    @Override
    public void apply(final double[] source, final double[] target) {
        sphericalToCartesian(source[0], source[1], source[2], target);
    }

    @Override
    public void apply(final RealLocalizable source, final RealPositionable target) {
        sphericalToCartesian(source.getDoublePosition(0), source.getDoublePosition(1), source.getDoublePosition(2), temp);
        target.setPosition(temp);
    }

    @Override
    public void applyInverse(final float[] source, final float[] target) {
        cartesianToSpherical(target[0], target[1], target[2], temp);
        for (int i = 0; i < temp.length; ++i) {
            source[i] = (float) temp[i];
        }
    }

    @Override
    public void applyInverse(final double[] source, final double[] target) {
        cartesianToSpherical(target[0], target[1], target[2], source);
    }

    @Override
    public void applyInverse(final RealPositionable source, final RealLocalizable target) {
        cartesianToSpherical(target.getDoublePosition(0), target.getDoublePosition(1), target.getDoublePosition(2), temp);
        source.setPosition(temp);
    }

    private void sphericalToCartesian(final double radius, final double polar, final double azimuth, final double[] target) {
        final double radiusSinPolar = radius * Math.sin(polar);
        target[0] = radiusSinPolar * Math.cos(azimuth);
        target[1] = radiusSinPolar * Math.sin(azimuth);
        target[2] = radius * Math.cos(polar);
    }

    // TODO: Resulting spherical coordinates should be unique and all positive.
    private void cartesianToSpherical(final double x, final double y, final double z, final double[] target) {
        target[0] = Math.sqrt(x * x + y * y + z * z);
        if (target[0] == 0) {
            target[1] = 0;
            target[2] = 0; // This is needed to avoid division by 0 below.
            return;
        }
        target[1] = Math.atan2(y, x);
        target[2] = Math.acos(z / target[0]);
    }

    @Override
    public int numSourceDimensions() {
        return 3;
    }

    @Override
    public int numTargetDimensions() {
        return 3;
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
