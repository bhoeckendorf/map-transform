package de.uni_heidelberg.cos.agw.ij.transform;

import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.RealPositionable;
import net.imglib2.realtransform.InverseRealTransform;
import net.imglib2.realtransform.InvertibleRealTransform;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InvertibleRotatableTranslatableRealTransform3DTest {

    private final double PREC_DOUBLE = 1E-14;
    private final float PREC_FLOAT = 1E-5f;
    protected double[][] cases3d;

    private static String arrayToString(final double[] array) {
        String txt = String.format("{%.2f", array[0]);
        for (int i = 1; i < array.length; ++i) {
            txt += String.format(", %.2f", array[i]);
        }
        return txt + "}";
    }

    @Before
    public void setUp() {
        // source, rotation, translation, target
        cases3d = new double[][]{
                {0, 0, 0}, {0, 0, 0}, {-5, 0, 0}, {0, 0, 0},
                {0, 0, 0}, {0, 0, 0}, {0, -5, 0}, {5, -5, 0},
                {0, 0, 0}, {0, 0, 0}, {0, 0, -5}, {5, 0, -5},
                {0, 0, 0}, {0, 0, 0}, {-5, -5, -5}, {0, -5, -5},
                {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {5, 0, 0},
                {0, 0, 0}, {0, 0, 0}, {5, 0, 0}, {10, 0, 0},
                {0, 0, 0}, {0, 0, 0}, {0, 5, 0}, {5, 5, 0},
                {0, 0, 0}, {0, 0, 0}, {0, 0, 5}, {5, 0, 5},
                {0, 0, 0}, {0, 0, 0}, {5, 5, 5}, {10, 5, 5},
                {0, 10, 0}, {0, 0, 0}, {0, 0, 0}, {5, 10, 0},
                {0, 10, 0}, {0, 0, 0}, {0, 0, 5}, {5, 10, 5},

                {0, 0, 0}, {0.5 * Math.PI, 0, 0}, {-5, 0, 0}, {0, 0, 0},
                {0, 0, 0}, {0.5 * Math.PI, 0, 0}, {0, -5, 0}, {5, -5, 0},
                {0, 0, 0}, {0.5 * Math.PI, 0, 0}, {0, 0, -5}, {5, 0, -5},
                {0, 0, 0}, {0.5 * Math.PI, 0, 0}, {-5, -5, -5}, {0, -5, -5},
                {0, 0, 0}, {0.5 * Math.PI, 0, 0}, {0, 0, 0}, {5, 0, 0},
                {0, 0, 0}, {0.5 * Math.PI, 0, 0}, {5, 0, 0}, {10, 0, 0},
                {0, 0, 0}, {0.5 * Math.PI, 0, 0}, {0, 5, 0}, {5, 5, 0},
                {0, 0, 0}, {0.5 * Math.PI, 0, 0}, {0, 0, 5}, {5, 0, 5},
                {0, 0, 0}, {0.5 * Math.PI, 0, 0}, {5, 5, 5}, {10, 5, 5},
                {0, 10, 0}, {0.5 * Math.PI, 0, 0}, {0, 0, 0}, {5, 0, 10},
                {0, 10, 0}, {0.5 * Math.PI, 0, 0}, {0, 0, 5}, {5, 0, 15},

                {0, 0, 0}, {0, 0.5 * Math.PI, 0}, {-5, 0, 0}, {-5, 0, -5},
                {0, 0, 0}, {0, 0.5 * Math.PI, 0}, {0, -5, 0}, {0, -5, -5},
                {0, 0, 0}, {0, 0.5 * Math.PI, 0}, {0, 0, -5}, {0, 0, -10},
                {0, 0, 0}, {0, 0.5 * Math.PI, 0}, {-5, -5, -5}, {-5, -5, -10},
                {0, 0, 0}, {0, 0.5 * Math.PI, 0}, {0, 0, 0}, {0, 0, -5},
                {0, 0, 0}, {0, 0.5 * Math.PI, 0}, {5, 0, 0}, {5, 0, -5},
                {0, 0, 0}, {0, 0.5 * Math.PI, 0}, {0, 5, 0}, {0, 5, -5},
                {0, 0, 0}, {0, 0.5 * Math.PI, 0}, {0, 0, 5}, {0, 0, 0},
                {0, 0, 0}, {0, 0.5 * Math.PI, 0}, {5, 5, 5}, {5, 5, 0},
                {0, 10, 0}, {0, 0.5 * Math.PI, 0}, {0, 0, 0}, {0, 10, -5},
                {0, 10, 0}, {0, 0.5 * Math.PI, 0}, {0, 0, 5}, {0, 10, 0},

                {0, 0, 0}, {0, 0, 0.5 * Math.PI}, {-5, 0, 0}, {-5, 5, 0},
                {0, 0, 0}, {0, 0, 0.5 * Math.PI}, {0, -5, 0}, {0, 0, 0},
                {0, 0, 0}, {0, 0, 0.5 * Math.PI}, {0, 0, -5}, {0, 5, -5},
                {0, 0, 0}, {0, 0, 0.5 * Math.PI}, {-5, -5, -5}, {-5, 0, -5},
                {0, 0, 0}, {0, 0, 0.5 * Math.PI}, {0, 0, 0}, {0, 5, 0},
                {0, 0, 0}, {0, 0, 0.5 * Math.PI}, {5, 0, 0}, {5, 5, 0},
                {0, 0, 0}, {0, 0, 0.5 * Math.PI}, {0, 5, 0}, {0, 10, 0},
                {0, 0, 0}, {0, 0, 0.5 * Math.PI}, {0, 0, 5}, {0, 5, 5},
                {0, 0, 0}, {0, 0, 0.5 * Math.PI}, {5, 5, 5}, {5, 10, 5},
                {0, 10, 0}, {0, 0, 0.5 * Math.PI}, {0, 0, 0}, {-10, 5, 0},
                {0, 10, 0}, {0, 0, 0.5 * Math.PI}, {0, 0, 5}, {-10, 5, 5},

                {0, 0, 0}, {0.5 * Math.PI, 0.5 * Math.PI, 0}, {0, 0, 0}, {0, 0, -5},
                {0, 0, 0}, {0, 0.5 * Math.PI, 0.5 * Math.PI}, {0, 0, 0}, {0, 0, -5},
                {0, 0, 0}, {0.5 * Math.PI, 0.5 * Math.PI, 0.5 * Math.PI}, {0, 0, 0}, {0, 0, -5},
                {0, 10, 0}, {0.5 * Math.PI, 0.5 * Math.PI, 0}, {0, 0, 0}, {10, 0, -5},
                {0, 10, 0}, {0, 0.5 * Math.PI, 0.5 * Math.PI}, {0, 0, 0}, {-10, 0, -5},
                {0, 10, 0}, {0.5 * Math.PI, 0.5 * Math.PI, 0.5 * Math.PI}, {0, 0, 0}, {0, 10, -5},

                {0, 0, 0}, {0.5 * Math.PI, 0.5 * Math.PI, 0}, {0, 0, -10}, {0, 0, -15},
                {0, 0, 0}, {0, 0.5 * Math.PI, 0.5 * Math.PI}, {0, 0, -10}, {0, 0, -15},
                {0, 0, 0}, {0.5 * Math.PI, 0.5 * Math.PI, 0.5 * Math.PI}, {0, 0, -10}, {0, 0, -15},
                {0, 10, 0}, {0.5 * Math.PI, 0.5 * Math.PI, 0}, {0, 0, -10}, {10, 0, -15},
                {0, 10, 0}, {0, 0.5 * Math.PI, 0.5 * Math.PI}, {0, 0, -10}, {-10, 0, -15},
                {0, 10, 0}, {0.5 * Math.PI, 0.5 * Math.PI, 0.5 * Math.PI}, {0, 0, -10}, {0, 10, -15}
        };
    }

    @Test
    public void testApplyInverse_doubleArr_doubleArr() {
        System.out.println("applyInverse(double[], double[])");
        final InvertibleRotatableTranslatableRealTransform3D instance = new InvertibleRotatableTranslatableRealTransform3D(new Translate5x(3));
        final double[] source = new double[instance.numSourceDimensions()];
        for (int i = 0; i < cases3d.length; i += 4) {
            System.out.format("    src=%s, rot=%s, tsl=%s, tgt=%s\n", arrayToString(cases3d[i]), arrayToString(cases3d[i + 1]), arrayToString(cases3d[i + 2]), arrayToString(cases3d[i + 3]));
            final double[] truth = cases3d[i];
            instance.setRotation(cases3d[i + 1]);
            instance.setTranslation(cases3d[i + 2]);
            final double[] target = cases3d[i + 3];
            instance.applyInverse(source, target);
            System.out.println("        tgt->src=" + arrayToString(source));
            Assert.assertArrayEquals(truth, source, PREC_DOUBLE);
        }
    }

    @Test
    public void testApplyInverse_floatArr_floatArr() {
        System.out.println("applyInverse(float[], float[])");
        final InvertibleRotatableTranslatableRealTransform3D instance = new InvertibleRotatableTranslatableRealTransform3D(new Translate5x(3));
        final float[] source = new float[instance.numSourceDimensions()];
        final float[] truth = new float[instance.numSourceDimensions()];
        final float[] target = new float[instance.numSourceDimensions()];
        for (int i = 0; i < cases3d.length; i += 4) {
            instance.setRotation(cases3d[i + 1]);
            instance.setTranslation(cases3d[i + 2]);
            for (int d = 0; d < instance.numSourceDimensions(); ++d) {
                truth[d] = (float) cases3d[i][d];
                target[d] = (float) cases3d[i + 3][d];
            }
            instance.applyInverse(source, target);
            Assert.assertArrayEquals(truth, source, PREC_FLOAT);
        }
    }

    @Test
    public void testApply_doubleArr_doubleArr() {
        System.out.println("apply(double[], double[])");
        final InvertibleRotatableTranslatableRealTransform3D instance = new InvertibleRotatableTranslatableRealTransform3D(new Translate5x(3));
        final double[] target = new double[instance.numTargetDimensions()];
        for (int i = 0; i < cases3d.length; i += 4) {
            System.out.format("    src=%s, rot=%s, tsl=%s, tgt=%s\n", arrayToString(cases3d[i]), arrayToString(cases3d[i + 1]), arrayToString(cases3d[i + 2]), arrayToString(cases3d[i + 3]));
            final double[] source = cases3d[i];
            instance.setRotation(cases3d[i + 1]);
            instance.setTranslation(cases3d[i + 2]);
            final double[] truth = cases3d[i + 3];
            instance.apply(source, target);
            System.out.println("        src->tgt=" + arrayToString(target));
            Assert.assertArrayEquals(truth, target, PREC_DOUBLE);
        }
    }

    @Test
    public void testApply_floatArr_floatArr() {
        System.out.println("apply(float[], float[])");
        final InvertibleRotatableTranslatableRealTransform3D instance = new InvertibleRotatableTranslatableRealTransform3D(new Translate5x(3));
        final float[] source = new float[instance.numSourceDimensions()];
        final float[] target = new float[instance.numTargetDimensions()];
        final float[] truth = new float[instance.numTargetDimensions()];
        for (int i = 0; i < cases3d.length; i += 4) {
            instance.setRotation(cases3d[i + 1]);
            instance.setTranslation(cases3d[i + 2]);
            for (int d = 0; d < instance.numSourceDimensions(); ++d) {
                source[d] = (float) cases3d[i][d];
                truth[d] = (float) cases3d[i + 3][d];
            }
            instance.apply(source, target);
            Assert.assertArrayEquals(truth, target, PREC_FLOAT);
        }
    }

    @Test
    public void testApply_RealLocalizable_RealPositionable() {
        System.out.println("apply(RealLocalizable, RealPositionable)");
        final InvertibleRotatableTranslatableRealTransform3D instance = new InvertibleRotatableTranslatableRealTransform3D(new Translate5x(3));
        final RealLocalizable source = new RealPoint(instance.numSourceDimensions());
        final RealPositionable target = new RealPoint(instance.numTargetDimensions());
        final double[] targetArray = new double[instance.numTargetDimensions()];
        for (int i = 0; i < cases3d.length; i += 4) {
            ((RealPoint) source).setPosition(cases3d[i]);
            instance.setRotation(cases3d[i + 1]);
            instance.setTranslation(cases3d[i + 2]);
            final double[] truth = cases3d[i + 3];
            instance.apply(source, target);
            ((RealPoint) target).localize(targetArray);
            Assert.assertArrayEquals(truth, targetArray, PREC_DOUBLE);
        }
    }

    @Test
    public void testApplyInverse_RealPositionable_RealLocalizable() {
        System.out.println("apply(RealPositionable, RealLocalizable)");
        final InvertibleRotatableTranslatableRealTransform3D instance = new InvertibleRotatableTranslatableRealTransform3D(new Translate5x(3));
        final RealPositionable source = new RealPoint(instance.numSourceDimensions());
        final RealLocalizable target = new RealPoint(instance.numTargetDimensions());
        final double[] sourceArray = new double[instance.numTargetDimensions()];
        for (int i = 0; i < cases3d.length; i += 4) {
            final double[] truth = cases3d[i];
            instance.setRotation(cases3d[i + 1]);
            instance.setTranslation(cases3d[i + 2]);
            ((RealPoint) target).setPosition(cases3d[i + 3]);
            instance.applyInverse(source, target);
            ((RealPoint) source).localize(sourceArray);
            Assert.assertArrayEquals(truth, sourceArray, PREC_DOUBLE);
        }
    }
}

class Translate5x implements InvertibleRealTransform {

    private final int nDims;
    private InvertibleRealTransform inverse;

    public Translate5x(final int numDimensions) {
        nDims = numDimensions;
    }

    @Override
    public void apply(final double[] source, final double[] target) {
        System.arraycopy(source, 0, target, 0, nDims);
        target[0] += 5;
    }

    @Override
    public void apply(final float[] source, final float[] target) {
        System.arraycopy(source, 0, target, 0, nDims);
        target[0] += 5;
    }

    @Override
    public void apply(final RealLocalizable source, final RealPositionable target) {
        target.setPosition(source);
        target.setPosition(source.getDoublePosition(0) + 5, 0);
    }

    @Override
    public void applyInverse(final double[] source, final double[] target) {
        System.arraycopy(target, 0, source, 0, nDims);
        source[0] -= 5;
    }

    @Override
    public void applyInverse(final float[] source, final float[] target) {
        System.arraycopy(target, 0, source, 0, nDims);
        source[0] -= 5;
    }

    @Override
    public void applyInverse(final RealPositionable source, final RealLocalizable target) {
        source.setPosition(target);
        source.setPosition(target.getDoublePosition(0) - 5, 0);
    }

    @Override
    public int numSourceDimensions() {
        return nDims;
    }

    @Override
    public int numTargetDimensions() {
        return nDims;
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