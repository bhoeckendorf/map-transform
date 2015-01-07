package de.uni_heidelberg.cos.agw.ij.transform;

import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.RealPositionable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RotatableTranslatableRealTransform3DTest {

    private final double PREC_DOUBLE = 1E-14;
    private final float PREC_FLOAT = 1E-5f;
    private double[][] cases3d;

    @Before
    public void setUp() {
        final InvertibleRotatableTranslatableRealTransform3DTest test = new InvertibleRotatableTranslatableRealTransform3DTest();
        test.setUp();
        cases3d = test.cases3d;
    }

    @Test
    public void testApply_doubleArr_doubleArr() {
        System.out.println("apply(double[], double[])");
        final RotatableTranslatableRealTransform3D instance = new RotatableTranslatableRealTransform3D(new Translate5x(3));
        final double[] target = new double[instance.numTargetDimensions()];
        for (int i = 0; i < cases3d.length; i += 4) {
            final double[] source = cases3d[i];
            instance.setRotation(cases3d[i + 1]);
            instance.setTranslation(cases3d[i + 2]);
            final double[] truth = cases3d[i + 3];
            instance.apply(source, target);
            Assert.assertArrayEquals(truth, target, PREC_DOUBLE);
        }
    }

    @Test
    public void testApply_floatArr_floatArr() {
        System.out.println("apply(float[], float[])");
        final RotatableTranslatableRealTransform3D instance = new RotatableTranslatableRealTransform3D(new Translate5x(3));
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
        final RotatableTranslatableRealTransform3D instance = new RotatableTranslatableRealTransform3D(new Translate5x(3));
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
}