package de.uni_heidelberg.cos.agw.ij.transform;

import net.imglib2.RealLocalizable;
import net.imglib2.RealPositionable;
import net.imglib2.realtransform.*;

public class InvertibleRotatableTranslatableRealTransform2D implements InvertibleRealTransform {

    protected final InvertibleRealTransform transform;
    private final AffineTransform2D rotation;
    private final Translation2D translation;
    private final InvertibleRealTransformSequence transformSequence;
    private InverseRealTransform inverse;

    public InvertibleRotatableTranslatableRealTransform2D(final InvertibleRealTransform transform) {
        this.transform = transform;
        transformSequence = new InvertibleRealTransformSequence();
        transformSequence.add(this.transform);
        rotation = new AffineTransform2D();
        transformSequence.add(rotation);
        translation = new Translation2D();
        transformSequence.add(translation);
    }

    public void setRotation(final double radians) {
        final AffineTransform2D rot = new AffineTransform2D();
        rot.rotate(radians);
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