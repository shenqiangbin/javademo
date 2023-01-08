//package PDF;
//
//
//import java.awt.*;
//import java.awt.image.*;
//
//import com.sun.media.jai.codecimpl.TIFFImage;
//
//import java.awt.image.renderable.ParameterBlock;
//import java.io.*;
//import java.util.Vector;
//
//import com.sun.media.imageio.plugins.tiff.TIFFTag;
//import com.sun.media.jai.codec.*;
//import com.sun.media.jai.codecimpl.TIFFImageDecoder;
//import com.sun.media.jai.codecimpl.TIFFImageEncoder;
//
//import javax.media.jai.*;
//
//
//public class TiffOutput {
//
//    public static void TiffOutput(RenderedImage image, OutputStream stream, int dpi) throws IOException {
//
//        if (image != null) {
//            TIFFEncodeParam param = new TIFFEncodeParam();
//
//            //param.setCompression(TIFFEncodeParam.COMPRESSION_NONE);
//            param.setCompression(TIFFEncodeParam.COMPRESSION_DEFLATE); // 高压缩
//            //param.setCompression(TIFFEncodeParam.COMPRESSION_GROUP3_1D); // 中压缩
//            // param.setCompression(TIFFEncodeParam.COMPRESSION_GROUP4); // 低压缩
//            //param.setCompression(TIFFEncodeParam.COMPRESSION_LZW); // LZW 压缩
//            TIFFField[] extras = new TIFFField[2];
//            extras[0] = new TIFFField(282, TIFFTag.TIFF_RATIONAL, 1, (Object) new long[][]{{(long) dpi, 1}, {0, 0}});
//            extras[1] = new TIFFField(283, TIFFTag.TIFF_RATIONAL, 1, (Object) new long[][]{{(long) dpi, 1}, {0, 0}});
//            //param.setExtraFields(extras);
//
//            TIFFImageEncoder encoder = new TIFFImageEncoder(stream, param);
//
////            ByteArraySeekableStream byteArraySeekableStream = new ByteArraySeekableStream(image.getData())
////            //FileSeekableStream stream = new FileSeekableStream("D:\\srcPic.tif");
////
////            PlanarImage in = JAI.create("stream", byteArraySeekableStream);
////
////            ParameterBlock pb = (new ParameterBlock());
////
////            pb.addSource(in);
////            pb.add(0f);
////            pb.add((float) (in.getHeight() / 3));
////            pb.add((float) in.getWidth());
////            pb.add((float) ((in.getHeight()) * 2 / 3));
////            PlanarImage out = JAI.create("crop", pb, null);
//
//            //https://blog.csdn.net/qq_51017736/article/details/113054265
//            //https://blog.csdn.net/zmx729618/article/details/58588118
//
//            encoder.encode(image);
//            //encoder.encode(out);
//
//            stream.flush();
//            stream.close();
//        }
//
//    }
//
//    public static void main(String[] args) throws IOException {
//
//        String path = "/Users/adminqian/shenqb/some/";
//
//        TIFFEncodeParam param = new TIFFEncodeParam();
//        //param.setExtraImages(vector.listIterator(1));
//        OutputStream output = new FileOutputStream(path + "test1.tif");
//        TIFFField[] extras = new TIFFField[2];
//        extras[0] = new TIFFField(282, TIFFTag.TIFF_RATIONAL, 1, (Object) new long[][]{{(long) 300, 1}, {0, 0}});
//        extras[1] = new TIFFField(283, TIFFTag.TIFF_RATIONAL, 1, (Object) new long[][]{{(long) 300, 1}, {0, 0}});
//        param.setExtraFields(extras);
//        param.setLittleEndian(true);
//        //param.setWriteTiled(true);
//        //param.setTileSize(1419, 5);
//        //param.setDeflateLevel();
//        param.setDeflateLevel(9);//压缩等级
//        param.setReverseFillOrder(true);
//        //param.setCompression(TIFFEncodeParam.COMPRESSION_PACKBITS);
//        param.setCompression(TIFFEncodeParam.COMPRESSION_GROUP4);
//        //TIFFImageEncoder encoder = new TIFFImageEncoder(output, param);
//
//
//        FileSeekableStream stream = new FileSeekableStream(path + "gmarbles.tif");
//        //stream = new FileSeekableStream(path + "gmarbles-apache.tif");
//        stream = new FileSeekableStream(path + "gmarbles-12-monkey-2(3).tif");
//        //stream = new FileSeekableStream(path + "gmarbles 2.tif");
//        TIFFDecodeParam decodeParam = new TIFFDecodeParam();
//        TIFFImageDecoder decoder = new TIFFImageDecoder(stream, decodeParam);
//        //int pageNum = decoder.getNumPages();
//        //RenderedImage renderedImage = decoder.decodeAsRenderedImage(0);
//
//        RenderedImage renderedImage = decoder.decodeAsRenderedImage();
//        TIFFImage tiffImage = (TIFFImage) renderedImage;
//
//        ColorModel colorModel = renderedImage.getColorModel();
//        int pixelSize = colorModel.getPixelSize();
//
//        FileSeekableStream stream2 = new FileSeekableStream(path + "gmarbles.tif");
//        stream2 = new FileSeekableStream(path + "gmarbles 2.tif");
//
//        TIFFDecodeParam decodeParam2 = new TIFFDecodeParam();
//        TIFFImageDecoder decoder2 = new TIFFImageDecoder(stream2, decodeParam2);
//        //int pageNum = decoder.getNumPages();
//        //RenderedImage renderedImage = decoder.decodeAsRenderedImage(0);
//
//        RenderedImage renderedImage2 = decoder2.decodeAsRenderedImage();
//        TIFFImage tiffImage2 = (TIFFImage) renderedImage2;
//
//
//        RenderedImage biLevelImage = RGBToBilevel(renderedImage, true);
//
//
//        int _width = tiffImage.getWidth();
//        int _height = tiffImage.getHeight();
//        //tiffImage.tile
//
//        // imageType = 2 TYPE_GRAY
//        // compression COMP_LZW
//        // isTiled = false
//        //
//
//
//        int size = renderedImage.getData().getDataBuffer().getSize();
//        byte[] data = new byte[stream.available()];
//        stream.readFully(data);
//
//        int width = renderedImage.getWidth() / 4;
//        int height = renderedImage.getHeight() / 4;
//
//        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
//        byte[] rasterData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
//        System.arraycopy(data, 0, rasterData, 0, data.length); // A LOT faster than 'setData()'
//
//        //encoder.encode(renderedImage);
//        //encoder.encode(biLevelImage);
//
//
//
//
//
//        /*
//        encoder 也可以使用这种方式构建
//        * ImageEncoder encoder = ImageCodec.createImageEncoder("tiff", out, params);
//         * */
//    }
//
//    private static RenderedImage RGBToBilevel(RenderedImage srcImg,
//                                              boolean isErrorDiffusion) {
//        // Load the ParameterBlock for the dithering operation
//        // and set the operation name.
//        RenderedImage res = null;
//        ParameterBlock pb = new ParameterBlock();
//        pb.addSource(srcImg);
//        String opName = null;
//        if (isErrorDiffusion) {
//            opName = "errordiffusion";
//            LookupTableJAI lut = new LookupTableJAI(new byte[][]{
//                    {(byte) 0x00, (byte) 0xff}, {(byte) 0x00, (byte) 0xff},
//                    {(byte) 0x00, (byte) 0xff}});
//            pb.add(lut);
//            pb.add(KernelJAI.ERROR_FILTER_FLOYD_STEINBERG);
//        } else {
//            opName = "ordereddither";
//            ColorCube cube = ColorCube.createColorCube(DataBuffer.TYPE_BYTE, 0,
//                    new int[]{2, 2, 2});
//            pb.add(cube);
//            pb.add(KernelJAI.DITHER_MASK_443);
//        }
//        // Create a layout containing an IndexColorModel which maps
//        // zero to zero and unity to 255; force SampleModel to be bilevel.
//        ImageLayout layout = new ImageLayout();
//        byte[] map = new byte[]{(byte) 0x00, (byte) 0xff};
//        ColorModel cm = new IndexColorModel(1, 2, map, map, map);
//        layout.setColorModel(cm);
//        SampleModel sm = new MultiPixelPackedSampleModel(DataBuffer.TYPE_BYTE,
//                srcImg.getWidth(), srcImg.getHeight(), 1);
//        layout.setSampleModel(sm);
//        // Create a hint containing the layout.
//        RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);
//        // Dither the image.
//        res = JAI.create(opName, pb, hints);
//        return res;
//
//    }
//
//}
//
//
///*
//
//https://zh.wikipedia.org/wiki/Tagged_Image_File_Format
//https://docs.oracle.com/cd/E17802_01/products/products/java-media/jai/forDevelopers/jai-apidocs/com/sun/media/jai/codec/TIFFEncodeParam.html
//
//*/