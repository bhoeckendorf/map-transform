package de.uni_heidelberg.cos.agw.ij.transform;

import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.RealPositionable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CylindricalToCartesianTransformTest {

    private PolarToCartesianTransformTest polarToCartesianTransformTest;
    private double[] heightCases;

    @Before
    public void setUp() {
        polarToCartesianTransformTest = new PolarToCartesianTransformTest();
        polarToCartesianTransformTest.setUp();
        heightCases = new double[]{-5, 0, 5};
    }

    @Test
    public void testApply_floatArr_floatArr() {
        System.out.println("apply(float[], float[])");
        CylindricalToCartesianTransform instance = new CylindricalToCartesianTransform();
        final float[] source = new float[instance.numSourceDimensions()];
        final float[] target = new float[instance.numTargetDimensions()];
        final float[] truth = new float[instance.numTargetDimensions()];
        for (final double height : heightCases) {
            for (int i = 0; i < polarToCartesianTransformTest.polarToCartesianCases.length; i += 2) {
                for (int j = 0; j < 2; ++j) {
                    source[j] = (float) polarToCartesianTransformTest.polarToCartesianCases[i][j];
                    truth[j] = (float) polarToCartesianTransformTest.polarToCartesianCases[i + 1][j];
                }
                source[2] = (float) height;
                truth[2] = (float) height;
                instance.apply(source, target);
                Assert.assertArrayEquals(truth, target, polarToCartesianTransformTest.PREC_FLOAT);
            }
        }
    }

    @Test
    public void testApply_doubleArr_doubleArr() {
        System.out.println("apply(double[], double[])");
        CylindricalToCartesianTransform instance = new CylindricalToCartesianTransform();
        final double[] source = new double[instance.numSourceDimensions()];
        final double[] target = new double[instance.numTargetDimensions()];
        final double[] truth = new double[instance.numTargetDimensions()];
        for (final double height : heightCases) {
            for (int i = 0; i < polarToCartesianTransformTest.polarToCartesianCases.length; i += 2) {
                for (int j = 0; j < 2; ++j) {
                    source[j] = polarToCartesianTransformTest.polarToCartesianCases[i][j];
                    truth[j] = polarToCartesianTransformTest.polarToCartesianCases[i + 1][j];
                }
                source[2] = height;
                truth[2] = height;
                instance.apply(source, target);
                Assert.assertArrayEquals(truth, target, polarToCartesianTransformTest.PREC_DOUBLE);
            }
        }
    }

    @Test
    public void testApply_RealLocalizable_RealPositionable() {
        System.out.println("apply(RealLocalizable, RealPositionable)");
        CylindricalToCartesianTransform instance = new CylindricalToCartesianTransform();
        final RealLocalizable source = new RealPoint(instance.numSourceDimensions());
        final RealPositionable target = new RealPoint(instance.numTargetDimensions());
        final double[] targetArray = new double[instance.numTargetDimensions()];
        final double[] truth = new double[instance.numTargetDimensions()];
        for (final double height : heightCases) {
            for (int i = 0; i < polarToCartesianTransformTest.polarToCartesianCases.length; i += 2) {
                for (int j = 0; j < 2; ++j) {
                    ((RealPoint) source).setPosition(polarToCartesianTransformTest.polarToCartesianCases[i][j], j);
                    truth[j] = polarToCartesianTransformTest.polarToCartesianCases[i + 1][j];
                }
                ((RealPoint) source).setPosition(height, 2);
                truth[2] = height;
                instance.apply(source, target);
                ((RealPoint) target).localize(targetArray);
                Assert.assertArrayEquals(truth, targetArray, polarToCartesianTransformTest.PREC_FLOAT);
            }
        }
    }

    @Test
    public void testApplyInverse_doubleArr_doubleArr() {
        System.out.println("applyInverse(double[], double[])");
        CylindricalToCartesianTransform instance = new CylindricalToCartesianTransform();
        final double[] source = new double[instance.numSourceDimensions()];
        final double[] target = new double[instance.numTargetDimensions()];
        final double[] truth = new double[instance.numSourceDimensions()];
        for (final double height : heightCases) {
            for (int i = 0; i < polarToCartesianTransformTest.cartesianToPolarCases.length; i += 2) {
                for (int j = 0; j < 2; ++j) {
                    target[j] = polarToCartesianTransformTest.cartesianToPolarCases[i][j];
                    truth[j] = polarToCartesianTransformTest.cartesianToPolarCases[i + 1][j];
                }
                target[2] = height;
                truth[2] = height;
                instance.applyInverse(source, target);
                Assert.assertArrayEquals(truth, source, polarToCartesianTransformTest.PREC_DOUBLE);
            }
        }
    }

    @Test
    public void testApplyInverse_floatArr_floatArr() {
        System.out.println("applyInverse(float[], float[])");
        CylindricalToCartesianTransform instance = new CylindricalToCartesianTransform();
        final float[] source = new float[instance.numSourceDimensions()];
        final float[] target = new float[instance.numTargetDimensions()];
        final float[] truth = new float[instance.numSourceDimensions()];
        for (final double height : heightCases) {
            for (int i = 0; i < polarToCartesianTransformTest.cartesianToPolarCases.length; i += 2) {
                for (int j = 0; j < 2; ++j) {
                    target[j] = (float) polarToCartesianTransformTest.cartesianToPolarCases[i][j];
                    truth[j] = (float) polarToCartesianTransformTest.cartesianToPolarCases[i + 1][j];
                }
                target[2] = (float) height;
                truth[2] = (float) height;
                instance.applyInverse(source, target);
                Assert.assertArrayEquals(truth, source, polarToCartesianTransformTest.PREC_FLOAT);
            }
        }
    }

    @Test
    public void testApplyInverse_RealPositionable_RealLocalizable() {
        System.out.println("applyInverse(RealPositionable, RealLocalizable)");
        CylindricalToCartesianTransform instance = new CylindricalToCartesianTransform();
        final RealPoint source = new RealPoint(instance.numSourceDimensions());
        final double[] sourceArray = new double[instance.numSourceDimensions()];
        final RealPoint target = new RealPoint(instance.numTargetDimensions());
        final double[] truth = new double[instance.numSourceDimensions()];
        for (final double height : heightCases) {
            for (int i = 0; i < polarToCartesianTransformTest.cartesianToPolarCases.length; i += 2) {
                for (int j = 0; j < 2; ++j) {
                    target.setPosition(polarToCartesianTransformTest.cartesianToPolarCases[i][j], j);
                    truth[j] = polarToCartesianTransformTest.cartesianToPolarCases[i + 1][j];
                }
                target.setPosition(height, 2);
                truth[2] = height;
                instance.applyInverse(source, target);
                source.localize(sourceArray);
                Assert.assertArrayEquals(truth, sourceArray, polarToCartesianTransformTest.PREC_DOUBLE);
            }
        }
    }
}