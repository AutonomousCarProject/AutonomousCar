/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autonomouscarfinalprogram2;

import com.looi.looi.LooiObject;
import com.looi.looi.gui_essentials.Background;
import com.looi.looi.gui_essentials.Slider;
import group1.FileImage;
import group2.Blob;
import group2.BlobDetection3;
import group3.MovingBlob;
import group3.MovingBlobDetection;
import group4.BlobFilter;
import group5.IImageBoxDrawer;
import java.awt.image.BufferedImage;
import java.util.List;
import java.awt.Color;

/**
 *
 * @author peter_000
 */
public class Control extends LooiObject
{
    private BlobDetection3 blobDetection;
    private MovingBlobDetection movingBlobDetection;
    private BlobFilter blobFilter;
    private IImageBoxDrawer boxDrawer;
    private FileImage currentImage;
    private Slider slider;
    private BufferedImage testBI = new BufferedImage(10,10,BufferedImage.TYPE_INT_ARGB);
    {
        for(int r = 0; r < testBI.getHeight(); r++)
        {
            for(int c = 0; c < testBI.getWidth(); c++)
            {
                int p = (255/*alpha*/ << 24) | (255 << 16) | (255 << 8) | 255;
                testBI.setRGB(c,r,p);
            }
        }
    }
    
    private int previousFrame;
    public static boolean keepGoing;
    
    private int frameDelayInMS;
    
    public Control(int frameDelay)
    {
        blobDetection = new BlobDetection3();
        movingBlobDetection = new MovingBlobDetection();
        blobFilter = new BlobFilter();
        currentImage = new FileImage();
        boxDrawer = new IImageBoxDrawer();
        boxDrawer.setUsingBasicColors(true);
        slider = new Slider(10,10,300,30,new Background(Color.WHITE));
        
    }
    /**
     * This method runs 60 timer per sec
     */
    protected void looiStep()
    {
        //BlobFilter.WIDTH_HEIGHT_RATIO_MAX = slider.getPercentage();
    	if(keepGoing){
	        currentImage.readCam();
	        
	        previousFrame++;
	        
	        if(currentImage.getFrameNo()==previousFrame){
	        	previousFrame = 0;
	        	currentImage.finish();
	            currentImage = new FileImage();
	        	blobDetection = new BlobDetection3();
	            movingBlobDetection = new MovingBlobDetection();
	            blobFilter = new BlobFilter();
	            boxDrawer.setUsingBasicColors(true);
	            currentImage.readCam();
	        }
	        
	        List<Blob> knownBlobs = blobDetection.getBlobs(currentImage);
	        
	        List<MovingBlob> movingBlobs = movingBlobDetection.getMovingBlobs(knownBlobs);
	        //System.out.println(movingBlobs.size());
	        List<MovingBlob> filteredBlobs = blobFilter.reduce(movingBlobs);
	        boxDrawer.draw(currentImage,movingBlobs);   
	        
	        long time1 = System.currentTimeMillis();
	        long time2 = System.currentTimeMillis();
	        
	        while(time2-time1 < frameDelayInMS){
	        	time2 = System.currentTimeMillis();
	        }
    	}   
    }
    
    public static void pauseUnpause(){
    	keepGoing = !keepGoing;
    }
    
    protected void looiPaint()
    {
        drawImage(boxDrawer.getCurrentImage(),0,0,getInternalWidth(),getInternalHeight());
        //drawImage(testBI,0,0,getInternalWidth(),getInternalHeight());
    }
}
