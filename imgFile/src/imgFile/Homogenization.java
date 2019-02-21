package imgFile;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class Homogenization extends JFrame {

//保存當前操作的象素矩陣
private int currentPixArray[]=null;

//圖片路徑
private String fileString=null;
//用於顯示影像的標籤
private JLabel imageLabel=null;

//加載圖片
private BufferedImage newImage;

//圖片的寬跟高
private int h,w;

//保存歷史操作
private LinkedList<int[]> imageStack=new LinkedList<int[]>();
private LinkedList<int[]> tempImageStack=new LinkedList<int[]>();

 

 

public Homogenization(String title){
    super(title);
    this.setSize(800,600);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
    //create menu
    JMenuBar jb=new JMenuBar();
    JMenu fileMenu=new JMenu("文件");
    jb.add(fileMenu);
 
    JMenuItem openImageMenuItem=new JMenuItem("打開圖片");
    fileMenu.add(openImageMenuItem);
    openImageMenuItem.addActionListener(new OpenListener());
    
 
    JMenuItem exitMenu=new JMenuItem("退出");
    fileMenu.add(exitMenu);
    exitMenu.addActionListener(new ActionListener(){
     public void actionPerformed(ActionEvent e){
      System.exit(0);
     }
    });
 
    JMenu operateMenu=new JMenu("影像處理");
    jb.add(operateMenu);
 
    JMenuItem RGBtoGrayMenuItem=new JMenuItem("灰階轉換");
    operateMenu.add(RGBtoGrayMenuItem);
    RGBtoGrayMenuItem.addActionListener(new RGBtoGrayActionListener());
 
    JMenuItem balanceMenuItem=new JMenuItem("均衡化");
    operateMenu.add(balanceMenuItem);
    balanceMenuItem.addActionListener(new BalanceActionListener());
    
    JMenuItem equalizationMenuItem=new JMenuItem("直方圖等化(修正後)");
    operateMenu.add(equalizationMenuItem);
    equalizationMenuItem.addActionListener(new EqualizationActionListener());
 
    JMenuItem histogramMenuItem=new JMenuItem("轉換成直方圖");
    operateMenu.add(histogramMenuItem);
    histogramMenuItem.addActionListener(new HistogramActionListener());
    
    
    JMenu hideMenu=new JMenu("資料隱藏");
    jb.add(hideMenu);
    
    JMenuItem hide=new JMenuItem("處理");
    hideMenu.add(hide);
    hide.addActionListener(new ProcessActionListener());
    this.setJMenuBar(jb);
 
    imageLabel=new JLabel("");
    JScrollPane pane = new    JScrollPane(imageLabel);
    this.add(pane,BorderLayout.CENTER);
 
    this.setVisible(true);
 
}
//開始檔案
private class OpenListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
     JFileChooser jc=new JFileChooser();
     int returnValue=jc.showOpenDialog(null);
     if (returnValue ==    JFileChooser.APPROVE_OPTION) {
      File selectedFile =    jc.getSelectedFile();                      
      if (selectedFile != null) {                         
       fileString=selectedFile.getAbsolutePath();
       try{
        newImage =ImageIO.read(new File(fileString));
        w=newImage.getWidth();
        h=newImage.getHeight();
        currentPixArray=getPixArray(newImage,w,h);
        imageStack.clear();
        tempImageStack.clear();
        imageStack.addLast(currentPixArray);
        imageLabel.setIcon(new ImageIcon(newImage));
     
       }catch(IOException ex){
        System.out.println(ex);
       }
    
      }               
     }
     Homogenization.this.repaint();
     //MyShowImage.this.pack();
    }
}

//菜單竊聽
private class RGBtoGrayActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     int[] resultArray=RGBtoGray(currentPixArray);
     imageStack.addLast(resultArray);
     currentPixArray=resultArray;
     showImage(resultArray);
     tempImageStack.clear();
    }
 
}

private class BalanceActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     int[] resultArray=balance(currentPixArray);
     imageStack.addLast(resultArray);
     currentPixArray=resultArray;
     showImage(resultArray);
     tempImageStack.clear();
    }
 
}

private class EqualizationActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     int[] resultArray=equalization(currentPixArray);
     imageStack.addLast(resultArray);
     currentPixArray=resultArray;
     showImage(resultArray);
     tempImageStack.clear();
    }
 
}

private class HistogramActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
    BufferedImage resultArray=turnHistogram(currentPixArray);
    showHis(resultArray);
     tempImageStack.clear();
    }
 
}

private class ProcessActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     int[] resultArray=process(currentPixArray);
     imageStack.addLast(resultArray);
     currentPixArray=resultArray;
     showImage(resultArray);
     tempImageStack.clear();
    }
 
}




//獲取圖像像素矩陣
private int[]getPixArray(Image im,int w,int h){
       int[] pix=new int[w*h];
       PixelGrabber pg=null;
       try{
         pg = new PixelGrabber(im, 0, 0, w, h, pix, 0, w);
         if(pg.grabPixels()!=true)
           try{
             throw new java.awt.AWTException("pg error"+pg.status());
           }catch(Exception eq){
                   eq.printStackTrace();
           }
       } catch(Exception ex){
               ex.printStackTrace();

       }
      return pix;
    }

  

//顯示圖片
private void showImage(int[] srcPixArray){
    Image pic=createImage(new MemoryImageSource(w,h,srcPixArray,0,w));
       ImageIcon ic=new ImageIcon(pic);
       imageLabel.setIcon(ic);
       imageLabel.repaint();
}
//顯示直方圖圖片
private void showHis(BufferedImage srcPixArray){
	
	ImageIcon ic=new ImageIcon(srcPixArray);
	imageLabel.setIcon(ic);
	imageLabel.repaint();
}



//灰度轉換
private int[] RGBtoGray(int[] ImageSource){
    int[]grayArray=new int[h*w];
    ColorModel colorModel=ColorModel.getRGBdefault();
    int i ,j,k,r,g,b;
    for(i = 0; i < h;i++){
     for(j = 0;j < w;j++){
      k = i*w+j; 
      r = colorModel.getRed(ImageSource[k]);
      g = colorModel.getGreen(ImageSource[k]);
      b = colorModel.getBlue(ImageSource[k]);
      int gray=(int)(r*0.3+g*0.59+b*0.11);
      r=g=b=gray;
      grayArray[i*w+j]=(255 << 24) | (r << 16) | (g << 8 )| b;
     }
    }
    return grayArray;
}


//直方圖等化(修正後)
private int[] balance(int[] srcPixArray){
      int[] histogram=new int[256];
      int[] dinPixArray=new int[w*h];
       
      for(int i=0;i<h;i++){
       for(int j=0;j<w;j++){
        int grey=srcPixArray[i*w+j]&0xff;
        histogram[grey]++;
       }
      }
      double a=(double)255/(w*h);
      double[] c=new double[256];
      c[0]=(a*histogram[0]);
      for(int i=1;i<256;i++){
       c[i]=c[i-1]+(int)(a*histogram[i]);
      }
      for(int i=0;i<h;i++){
       for(int j=0;j<w;j++){
        int grey=srcPixArray[i*w+j]&0x0000ff;
        int hist=(int)c[grey];
     
        dinPixArray[i*w+j]=255<<24|hist<<16|hist<<8|hist;
       }
      }
      return dinPixArray;
}
//直方圖等化(wiki作法)
private int[] equalization(int[] srcPixArray){
    int[] histogram=new int[256];
    int[] dinPixArray=new int[w*h];
     
    //計算每一個灰度級的像素數
    for(int i=0;i<h;i++){
     for(int j=0;j<w;j++){
      int grey=srcPixArray[i*w+j]&0xff;
      histogram[grey]++;
     }
    }
  //直方圖均衡化
    double a=(double)255/(w*h);
    double[] c=new double[256];
    c[0]=(a*histogram[0]);
    for(int i=1;i<256;i++){
    	if(c[i-1]+Math.round(a*(histogram[i]))>255) {
    		c[i]=255;
    	}else {
    		c[i]=c[i-1]+Math.round(a*(histogram[i]));
    	}
    }
    //轉換成圖片
    for(int i=0;i<h;i++){
     for(int j=0;j<w;j++){
      int grey=srcPixArray[i*w+j]&0x0000ff;
      int hist=(int)c[grey];
   
      dinPixArray[i*w+j]=255<<24|hist<<16|hist<<8|hist;
     }
    }

    return dinPixArray;
}
//匯出直方圖
private BufferedImage turnHistogram(int[] ImageSource) {
	int[] histogram=new int[256];
	int[] dinPixArray=new int[w*h];
	
	for(int i=0;i<h;i++){
	     for(int j=0;j<w;j++){
	      int grey=ImageSource[i*w+j]&0xff;
	      histogram[grey]++;
	     }
	    }
    int size=300;   
    BufferedImage pic = new BufferedImage(size,size, BufferedImage.TYPE_4BYTE_ABGR);
    Graphics2D g2d = pic.createGraphics();  
    g2d.setPaint(Color.white);  
    g2d.fillRect(0, 0, size, size);  
    g2d.setPaint(Color.black);  
    g2d.drawLine(5, 250, 265, 250);  	
    g2d.drawLine(5, 250, 5, 5); 	 
    g2d.setPaint(Color.black);

	int max = findMaxValue(histogram);
	float rate = 200.0f/((float)max);  
    int offset = 2;  
    for(int i=0; i<histogram.length; i++) {  
        int frequency = Math.round((histogram[i] * rate));  
        g2d.drawLine(5 + offset + i, 250, 5 + offset + i, 250-frequency);  
    }  
       
    g2d.setPaint(Color.RED);  
    g2d.drawString("", 100, 270); 
    
    return pic;

}
//轉成二進位
private int[] process(int[] srcPixArray){
  int[] histogram=new int[256];
  int[] dinPixArray=new int[w*h];
   
  //計算每一個灰度級的像素數
  for(int i=0;i<h;i++){
   for(int j=0;j<w;j++){
    int grey=srcPixArray[i*w+j]&0xff;
    histogram[grey]++;
   }
  }
  //等化後的數值轉成二進位取最後一個值
  double a=(double)255/(w*h);
  double[] c=new double[256];
  c[0]=(a*histogram[0]);
  for(int i=1;i<256;i++){
  	if(c[i-1]+Math.round(a*(histogram[i]))>255) {
  		c[i]=255;
  	}else {
  		c[i]=c[i-1]+Math.round(a*(histogram[i]));
  		System.out.println(c[i]);
  	}
  	
  }
  //轉換成圖片
  for(int i=0;i<h;i++){
   for(int j=0;j<w;j++){
    int grey=srcPixArray[i*w+j]&0x0000ff;
    int hist=(int)c[grey];
 
    dinPixArray[i*w+j]=255<<24|hist<<16|hist<<8|hist;
   }
  }

  return dinPixArray;
}
private int findMaxValue(int[] histogram) {
	int max = -1;
	for(int i=0; i<histogram.length; i++) {
		if(max < histogram[i]) {
			max = histogram[i];
		}
	}
	return max;
}
public static void main(String[] args) {
    new Homogenization("ShowImage");
}

}