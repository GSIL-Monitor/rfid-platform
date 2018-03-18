package com.casesoft.dmc.extend.mirror;
import java.awt.Graphics;
import java.awt.Image;  
import java.awt.Rectangle;  
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;  
import java.awt.image.AffineTransformOp;  
import java.awt.image.BufferedImage;  
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;  
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;  
import java.io.InputStream;  
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Iterator;  
  







import javax.imageio.ImageIO;  
import javax.imageio.ImageReadParam;  
import javax.imageio.ImageReader;
import javax.imageio.plugins.jpeg.JPEGImageReadParam;
import javax.imageio.stream.ImageInputStream;  

import com.casesoft.dmc.core.util.CommonUtil;
  
public class ImageCutUtil {  
	
	
	public static final long minLength = 200*1024;
	
	/**
	 * 压缩图片原尺寸不变
	 * @param inputDir
	 * @param outputDir
	 * 
	 * */
	public static void commpressPic(String inputDir,String outputDir){
		File f = new File(inputDir);
		if(f.length() < minLength){
			return;
		}
		try {
			Image img = ImageIO.read(f);
			int newWidth = img.getWidth(null);
			int newHeigth = img.getHeight(null);
			BufferedImage tag = new BufferedImage((int) newWidth,(int) newHeigth
					                              ,BufferedImage.TYPE_INT_BGR);
			tag.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeigth
					                       , Image.SCALE_SMOOTH), 0, 0, null);
			tag.getGraphics().dispose();
			ImageIO.write(tag, "jpg", new File(outputDir));
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
	}
    /* 
     * 根据尺寸图片居中裁剪 
     */  
     public static void cutCenterImage(String src,String dest,int w,int h) throws IOException{   
         Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");   
         ImageReader reader = (ImageReader)iterator.next();   
         InputStream in=new FileInputStream(src);  
         ImageInputStream iis = ImageIO.createImageInputStream(in);   
         reader.setInput(iis, true);   
         ImageReadParam param = reader.getDefaultReadParam();   
         int imageIndex = 0;   
         Rectangle rect = new Rectangle((reader.getWidth(imageIndex)-w)/2, (reader.getHeight(imageIndex)-h)/2, w, h);    
         param.setSourceRegion(rect);   
         BufferedImage bi = reader.read(0,param);     
         ImageIO.write(bi, "jpg", new File(dest));             
    
     }  
    /* 
     * 图片裁剪二分之一 
     */  
     public static void cutHalfImage(String src,String dest) throws IOException{   
         Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");   
         ImageReader reader = (ImageReader)iterator.next();   
         InputStream in=new FileInputStream(src);  
         ImageInputStream iis = ImageIO.createImageInputStream(in);   
         reader.setInput(iis, true);   
         ImageReadParam param = reader.getDefaultReadParam();   
         int imageIndex = 0;   
         int width = reader.getWidth(imageIndex)/2;   
         int height = reader.getHeight(imageIndex)/2;   
         Rectangle rect = new Rectangle(width/2, height/2, width, height);   
         param.setSourceRegion(rect);   
         BufferedImage bi = reader.read(0,param);     
         ImageIO.write(bi, "jpg", new File(dest));     
     }  
    /* 
     * 图片裁剪通用接口 
     */  
  
    public static void cutImage(String src,String dest,int x,int y,int w,int h) throws IOException{   
    	 try {
             // 读取源图像
             BufferedImage bi = ImageIO.read(new File(src));
             int srcWidth = bi.getWidth(); 
             int srcHeight = bi.getHeight();
             if (srcWidth > 0 && srcHeight > 0) {
                 Image image = bi.getScaledInstance(srcWidth, srcHeight,
                         Image.SCALE_SMOOTH);
                 // 四个参数分别为图像起点坐标和宽高
                 // 即: CropImageFilter(int x,int y,int width,int height)
                 ImageFilter cropFilter = new CropImageFilter(x, y, w, h);
                 Image img = Toolkit.getDefaultToolkit().createImage(
                         new FilteredImageSource(image.getSource(),
                                 cropFilter));
                 BufferedImage tag = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                 Graphics g = tag.getGraphics();
                 g.drawImage(img, 0, 0, w, h, null); // 绘制切割后的图
                 g.dispose();
                 // 输出为文件
                 ImageIO.write(tag, "jpg", new File(dest));
             }
         } catch (Exception e) {
             e.printStackTrace();
         }  
    	
    	
  
    }  
    
    public static void cutImage(String path, ImageCut cut) throws Exception {    	
    	/*cutImage(path,path.substring(0,path.lastIndexOf("."))+cut.getWidth()+"-"+cut.getHeight()+"裁剪.jpg",cut.getLeftX(),cut.getLeftY(),cut.getWidth(),cut.getHeight()); */
        cutImage(path,path,cut.getLeftX(),cut.getLeftY(),cut.getWidth(),cut.getHeight());
	}
    
    /* 
     * 图片缩放 
     */  
    public static void zoomImage(String src,String dest,int w,int h) throws Exception {  
        double wr=0,hr=0;  
        File srcFile = new File(src);  
        File destFile = new File(dest);  
        BufferedImage bufImg = ImageIO.read(srcFile);  
        Image Itemp = bufImg.getScaledInstance(w, h, bufImg.SCALE_SMOOTH);  
        wr=w*1.0/bufImg.getWidth();  
        hr=h*1.0 / bufImg.getHeight();  
        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);  
        Itemp = ato.filter(bufImg, null);  
        try {  
            ImageIO.write((BufferedImage) Itemp,dest.substring(dest.lastIndexOf(".")+1), destFile);  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }  
          
    }
    
    /* 
     * 图片缩放 
     */  
    public static void zoomImageByScale(String src,String dest,double scale) throws Exception {  
        double wr=0,hr=0;  
        File srcFile = new File(src);  
        File destFile = new File(dest);  
        BufferedImage bufImg = ImageIO.read(srcFile);  
        int width = (int) (bufImg.getWidth()/scale);
        int height = (int) (bufImg.getHeight()/scale);
        zoomImage(src,dest,width,height);
          
    }
    public static Double getScale(File file,Double width) throws Exception{
    	 
    	 BufferedImage bufImg = ImageIO.read(file); 
    	 Double scale;
    	 if(width == 0){
    		scale = 1.0;
    	 }else{
    		 DecimalFormat df = new DecimalFormat("######0.00000000");          	 
    		 scale = Double.parseDouble(df.format(bufImg.getWidth()))/Double.parseDouble(df.format(width)); 
    	 }    	   	 
    	 return scale;
    	 
    }
    /**
     * 
     * */
	public static void covertImage(File f, Double width, ImageCut cut) throws Exception {
		
		Double scale = ImageCutUtil.getScale(f, width);	
	    ImageCutUtil.zoomImageByScale(f.getPath(),f.getPath(),scale);
		if(CommonUtil.isNotBlank(cut)){
			ImageCutUtil.cutImage(f.getPath(),cut);			
		}	
		
			
	}
	
}  