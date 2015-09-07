package de.uni_heidelberg.cos.agw.ij.transform;

import net.imglib2.RealLocalizable;
import net.imglib2.RealPositionable;
import net.imglib2.realtransform.*;

public class InvertibleRotatableTranslatableRealTransform3D implements InvertibleRealTransform {

    protected final InvertibleRealTransform transform;
    private final AffineTransform3D rotation;
    private final Translation3D translation;
    private final InvertibleRealTransformSequence transformSequence;
    private InverseRealTransform inverse;

    public InvertibleRotatableTranslatableRealTransform3D(final InvertibleRealTransform transform) {
        this.transform = transform;
        transformSequence = new InvertibleRealTransformSequence();
        transformSequence.add(this.transform);
        rotation = new AffineTransform3D();
        transformSequence.add(rotation);
        translation = new Translation3D();
        transformSequence.add(translation);
    }

    public void setRotation(final double... radians) {
        final AffineTransform3D rot = new AffineTransform3D();
        for (int d = 0; d < radians.length; ++d) {
            rot.rotate(d, radians[d]);
        }
        rotation.set(rot);
    }

    public void setTranslation(final double... vector) {
        translation.set(vector);
    }

    @Override
    public void apply(final double[] source, final double[] target) {
        transformSequence.apply(source, target);
    }

    @Override
    public void apply(final float[] source, final float[] target) {
        transformSequence.apply(source, target);
    }

    @Override
    public void apply(final RealLocalizable source, final RealPositionable target) {
        transformSequence.apply(source, target);
    }

    @Override
    public void applyInverse(final double[] source, final double[] target) {
        transformSequence.applyInverse(source, target);
    }

    @Override
    public void applyInverse(final float[] source, final float[] target) {
        transformSequence.applyInverse(source, target);
    }

    @Override
    public void applyInverse(final RealPositionable source, final RealLocalizable target) {
        transformSequence.applyInverse(source, target);
    }

    @Override
    public int numSourceDimensions() {
        return transform.numSourceDimensions();
    }

    @Override
    public int numTargetDimensions() {
        return transform.numTargetDimensions();
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
