package de.uni_heidelberg.cos.agw.ij.transform;

import net.imglib2.RealLocalizable;
import net.imglib2.RealPositionable;
import net.imglib2.realtransform.AffineTransform2D;
import net.imglib2.realtransform.RealTransform;
import net.imglib2.realtransform.RealTransformSequence;
import net.imglib2.realtransform.Translation2D;

public class RotatableTranslatableRealTransform2D implements RealTransform {

    private final RealTransform transform;
    private final AffineTransform2D rotation;
    private final Translation2D translation;
    private final RealTransformSequence transformSequence;

    public RotatableTranslatableRealTransform2D(final RealTransform transform) {
        this.transform = transform;
        transformSequence = new RealTransformSequence();
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
    public int numSourceDimensions() {
        return transform.numSourceDimensions();
    }

    @Override
    public int numTargetDimensions() {
        return transform.numTargetDimensions();
    }

    @Override
    public RealTransform copy() {
        return this;
    }
}