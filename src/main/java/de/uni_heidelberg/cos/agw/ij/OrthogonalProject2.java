package de.uni_heidelberg.cos.agw.ij;

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import net.imglib2.img.ImagePlusAdapter;
import net.imglib2.meta.CalibratedAxis;
import net.imglib2.meta.CalibratedSpace;
import net.imglib2.meta.ImgPlus;
import net.imglib2.realtransform.Scale;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.NumericType;

public class OrthogonalProject2<T extends NumericType<T> & NativeType<T>> implements PlugInFilter {

    private ImagePlus inImp;

    private boolean doScale = true;

    @Override
    public int setup(final String arg, final ImagePlus imp) {
        inImp = imp;
        return STACK_REQUIRED + DOES_ALL;
    }

    @Override
    public void run(final ImageProcessor ip) {
        final ImgPlus<T> in = ImagePlusAdapter.wrapImgPlus(inImp);
        if (doScale) {
            final Scale scale = new Scale();
            scale.set(getAnisotropy(in));
        }
    }

    public ImgPlus<T> compute(final ImgPlus<T> in) {
        final long[] dims = new long[in.numDimensions()];
        in.dimensions(dims);

        final ImgPlus<T> out = new ImgPlus<T>(in.factory().create(dims, in.firstElement()));
        return out;
    }

    private double[] getAnisotropy(final CalibratedSpace<? extends CalibratedAxis> in) {
        final double[] anisotropy = new double[in.numDimensions()];
        double smallest = Double.MAX_VALUE;
        for (int d = 0; d < anisotropy.length; ++d) {
            anisotropy[d] = in.averageScale(d);
            if (anisotropy[d] < smallest) {
                smallest = anisotropy[d];
            }
        }
        for (int d = 0; d < anisotropy.length; ++d) {
            anisotropy[d] /= smallest;
        }
        return anisotropy;
    }
}
