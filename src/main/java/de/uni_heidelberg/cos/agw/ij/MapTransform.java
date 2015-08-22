package de.uni_heidelberg.cos.agw.ij;

import de.uni_heidelberg.cos.agw.ij.transform.AzimuthalEquidistantToCartesianTransform;
import de.uni_heidelberg.cos.agw.ij.transform.CylindricalToCartesianIntervalTransform;
import de.uni_heidelberg.cos.agw.ij.transform.EquirectangularToCartesianTransform;
import de.uni_heidelberg.cos.agw.ij.transform.RotatableTranslatableRealTransform3D;
import net.imagej.Dataset;
import net.imagej.DefaultDataset;
import net.imagej.ImgPlus;
import net.imglib2.Cursor;
import net.imglib2.Interval;
import net.imglib2.RealRandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.interpolation.InterpolatorFactory;
import net.imglib2.interpolation.randomaccess.LanczosInterpolatorFactory;
import net.imglib2.interpolation.randomaccess.NLinearInterpolatorFactory;
import net.imglib2.interpolation.randomaccess.NearestNeighborInterpolatorFactory;
import net.imglib2.realtransform.RealTransform;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Command.class, menuPath = "Plugins>Transform>Map Transform")
public class MapTransform<T extends RealType<T> & NativeType<T>, V extends RealTransform & Interval> implements Command {

    @Parameter
    private Dataset in;

    @Parameter
    private double centerX = 600, centerY = 600, centerZ = 425;

    @Parameter
    private double rotationX = 0, rotationY = 0, rotationSelf = 0;

    @Parameter
    private double innerRadius = 200, outerRadius = 425;

    @Parameter(min = "0.0", max = "1.0")
    private double stdRadiusOffset = 0.7;

    @Parameter(min = "0.1", max = "1.0")
    private double scale = 1;

    @Parameter
    private double cylinderHeight = 512;

    @Parameter(choices = {"Equirectangular", "Azimuthal Equidistant", "Cylindrical"})
    private String transformation = "Equirectangular";

    @Parameter(choices = {"Nearest Neighbor", "Linear", "Lanczos"})
    private String interpolation = "Linear";

    @Parameter(choices = {"Max projection", "Min projection", "Volume"})
    private String output = "Max projection";

    @Parameter(type = ItemIO.OUTPUT)
    private Dataset out;

    @Override
    public void run() {
        cylinderHeight = in.dimension(0);

        final double[] translation = {centerX, centerY, centerZ};
        final double[] rotation = {rotationX, rotationY, rotationSelf};

        InterpolatorFactory interpol = null;
        if (interpolation.equals("Nearest Neighbor"))
            interpol = new NearestNeighborInterpolatorFactory<T>();
        else if (interpolation.equals("Lanczos"))
            interpol = new LanczosInterpolatorFactory<T>();
        else
            interpol = new NLinearInterpolatorFactory<T>();

        final RealRandomAccess inRRa = Views.interpolate(Views.extendZero((Img<T>) in.getImgPlus()), interpol).realRandomAccess();

        V transform = null;
        if (transformation.equals("Azimuthal Equidistant"))
            transform = (V) (new AzimuthalEquidistantToCartesianTransform(innerRadius, outerRadius, stdRadiusOffset, scale));
        else if (transformation.equals("Cylindrical"))
            transform = (V) (new CylindricalToCartesianIntervalTransform(cylinderHeight, innerRadius, outerRadius, stdRadiusOffset, scale));
        else
            transform = (V) (new EquirectangularToCartesianTransform(innerRadius, outerRadius, stdRadiusOffset, scale));

        final Transformation transformation = new Transformation(transform, translation, rotation, inRRa);
        final String filenameParams = String.format(
                "-%s-cx%.2f-cy%.2f-cz%.2f-rx%.2f-ry%.2f-rs%.2f-ri%.2f-ro%.2f-sr%.2f-sc%.2f",
                this.transformation, centerX, centerY, centerZ,
                rotationX, rotationY, rotationSelf,
                innerRadius, outerRadius, stdRadiusOffset, scale);
        final String fileName = addToFilename(in.getName(), filenameParams);

        final Img<T> outImg = transformation.compute(in.getImgPlus().factory(), in.firstElement());
        out = new DefaultDataset(in.context(), new ImgPlus(outImg));
        out.setName(fileName);
    }

    private String addToFilename(final String filename, final String addition) {
        final int dotIdx = filename.lastIndexOf(".");
        if (dotIdx < 0 || dotIdx < filename.length() - 6) {
            return filename + addition;
        }
        return filename.substring(0, dotIdx) + addition + filename.substring(dotIdx);
    }
}

class Projection<T extends NumericType<T>> {

}

class Transformation<T extends NumericType<T> & RealType<T> & NativeType<T>, V extends RealTransform & Interval> {

    private final long[] outputDimensions;
    private final RotatableTranslatableRealTransform3D transform;
    private final RealRandomAccess<T> inputRa;

    public Transformation(final V transformInterval, final double[] translation, final double[] rotation, final RealRandomAccess<T> sourceRa) {
        outputDimensions = new long[transformInterval.numDimensions()];
        transformInterval.dimensions(outputDimensions);
        transform = new RotatableTranslatableRealTransform3D(transformInterval);
        transform.setTranslation(translation);
        transform.setRotation(rotation);
        inputRa = sourceRa;
    }

    public Img<T> compute(final ImgFactory<T> factory, final T element) {
        final Img<T> outputImg = factory.create(outputDimensions, element);
        final Cursor<T> outputCur = outputImg.localizingCursor();
        while (outputCur.hasNext()) {
            outputCur.fwd();
            transform.apply(outputCur, inputRa);
            outputCur.get().set(inputRa.get());
        }
        return outputImg;
    }
}
