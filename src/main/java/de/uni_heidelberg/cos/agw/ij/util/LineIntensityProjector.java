package de.uni_heidelberg.cos.agw.ij.util;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealRandomAccess;
import net.imglib2.interpolation.InterpolatorFactory;
import net.imglib2.ops.operation.BinaryOperation;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.view.Views;

public class LineIntensityProjector<T extends NumericType<T>> {

    private final MutableLinearIterator iterator;
    private final RealRandomAccess<T> ra;

    @SuppressWarnings("unchecked")
    public LineIntensityProjector(RandomAccessibleInterval<T> img, InterpolatorFactory interpolation) {
        iterator = new MutableLinearIterator(img.numDimensions());
        ra = Views.interpolate(Views.extendZero(img), interpolation).realRandomAccess();
    }

    public void set(final double[] start, final double[] end) {
        iterator.set(start, end);
    }

    // This method returns null if no point of the line is within the volume.
    public T compute(final BinaryOperation<T, T, T> op) {
        T value = null;
        for (final double[] point : iterator) {
            ra.setPosition(point);
            if (value == null) {
                try {
                    value = ra.get().copy();
                } catch (ArrayIndexOutOfBoundsException ex) {
                }
            } else {
                try {
                    op.compute(ra.get(), value, value);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    return value;
                }
            }
        }
        return value;
    }
}