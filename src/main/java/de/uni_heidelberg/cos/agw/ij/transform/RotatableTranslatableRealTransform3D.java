package de.uni_heidelberg.cos.agw.ij.transform;

import net.imglib2.RealLocalizable;
import net.imglib2.RealPositionable;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.realtransform.RealTransform;
import net.imglib2.realtransform.RealTransformSequence;
import net.imglib2.realtransform.Translation3D;

public class RotatableTranslatableRealTransform3D implements RealTransform {

    private final RealTransform transform;
    private final AffineTransform3D rotation;
    private final Translation3D translation;
    private final RealTransformSequence transformSequence;

    public RotatableTranslatableRealTransform3D(final RealTransform transform) {
        this.transform = transform;
        transformSequence = new RealTransformSequence();
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
