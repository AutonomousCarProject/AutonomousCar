/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group5;

import group1.IImage;
import group1.IPixel;
import group2.IBlobDetection;
import group2.Blob;
import group3.MovingBlob;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Create only ONE IImageBoxDrawer and as long as you send all draw(..) method 
 * calls to this one object, you should be fine. If you create a million IImage 
 * Box Drawers, you're fked because each one is a jframe
 * @author peter_000
 */
public class IImageBoxDrawer implements IImageDrawing
{
    public static final int DEFAULT_LINE_THICKNESS = 3;
    public static final Color DEFAULT_STILL_COLOR = Color.WHITE;
    public static final Color DEFAULT_FAST_COLOR = Color.WHITE;
    public static final double DEFAULT_MAX_VELOCITY = 5;
    
    
    private BufferedImage currentImage;
    private boolean useBasicColors = false;
    private Color stillColor;
    private Color fastColor;
    private double maxVelocity;
    public IImageBoxDrawer(Color still, Color fast, double maxVelocity)
    {
        this.stillColor = still;
        this.fastColor = fast;
        this.maxVelocity = maxVelocity;
    }
    public IImageBoxDrawer()
    {
        this(DEFAULT_STILL_COLOR,DEFAULT_FAST_COLOR,DEFAULT_MAX_VELOCITY);
    }
    public BufferedImage getCurrentImage()
    {
        return currentImage;
    }
    
    public boolean isUsingBasicColors()
    {
        return useBasicColors;
    }
    public void setUsingBasicColors(boolean b)
    {
        useBasicColors = b;
    }
    public void draw(IImage image, List<MovingBlob> iBlobs)
    {
        Rectangle[] rectangles = findRectangles(image,iBlobs);
        BufferedImage b = new BufferedImage(image.getImage()[0].length,image.getImage().length,BufferedImage.TYPE_INT_ARGB);
        setPixels(b,image.getImage());
        
        
        drawLines(rectangles,b,stillColor,fastColor,DEFAULT_LINE_THICKNESS);
        currentImage = b;
    }
    public void draw2(IImage image, List<MovingBlob> iBlobs,List<MovingBlob> iBlobs2)
    {
    	 Rectangle[] rectangles = findRectangles(image,iBlobs);
    	 Rectangle[] rectangles2 = findRectangles(image, iBlobs2);
         BufferedImage b = new BufferedImage(image.getImage()[0].length,image.getImage().length,BufferedImage.TYPE_INT_ARGB);
         setPixels(b,image.getImage());
         
         
         drawLines(rectangles,b,Color.RED,Color.RED,DEFAULT_LINE_THICKNESS);
         drawLines(rectangles2, b, Color.BLUE, Color.BLUE, DEFAULT_LINE_THICKNESS);
         currentImage = b;
    }

    public void drawRisk(IImage image, List<MovingBlob> iBlobs)
    {
        Rectangle[] rectangles = findRectangles(image, iBlobs);
        BufferedImage b = new BufferedImage(image.getImage()[0].length, image.getImage().length,
                BufferedImage.TYPE_INT_ARGB);
        setPixels(b, image.getImage());

        Graphics2D g = (Graphics2D) b.getGraphics();
        BasicStroke bs = new BasicStroke(DEFAULT_LINE_THICKNESS);
        
        Point center = new Point((image.getImage()[0].length / 2f), (image.getImage().length / 2f));

        for (Rectangle rect : rectangles)
        {
            for (int i = 0; i < 4; i++)
            {
                int indexOfNextPoint;
                if (i == 3)
                {
                    indexOfNextPoint = 0;
                }
                else
                {
                    indexOfNextPoint = i + 1;
                }
                Point start = rect.getPoints()[i];
                Point end = rect.getPoints()[indexOfNextPoint];

                g.setStroke(bs);

                double risk = getRisk(rect.getBlob(), 10, center);
                
                final int maxGreen = 180;
                
                Color lineColor = findColor(Color.RED, new Color(255 - maxGreen, maxGreen, 0), risk);

                g.setColor(lineColor);
                g.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY());
            }
        }
        currentImage = b;
        

    }
    
    // On scale from 0 to 1 -- WHERE 0 IS MAX
    private double getRisk(MovingBlob blob, int iterations, Point center)
    {
        double risk = 0;
        double mult = 1;
        double maxDist = dist(center, new Point(0, 0));
        for(int i = 0; i < iterations; i++)
        {
            if(i != iterations - 1) mult /= 2;
            float x = blob.x + (blob.width / 2f);
            float y = blob.y + (blob.height / 2f);

            x += blob.velocityX * iterations;
            y += blob.velocityY * iterations;
            
            double dist = dist(new Point(x, y), center);
            risk += mult * (dist / maxDist);
        }
        
        return risk;
    }
    
    private double dist(Point point, Point point2)
    {
        double xSquared = (point.x - point2.x) * (point.x - point2.x);
        double ySquared = (point.y - point2.y) * (point.y - point2.y);

        return Math.sqrt(xSquared + ySquared);
    }
    
    protected Color findColor(Color min, Color max, double percent)
    {
        int deltaRed = max.getRed() - min.getRed();
        int deltaGreen = max.getGreen() - min.getGreen();
        int deltaBlue = max.getBlue() - min.getBlue();
        
        int changeRed = (int)(deltaRed * percent);
        int changeGreen = (int)(deltaGreen * percent);
        int changeBlue = (int)(deltaBlue * percent);
        
        int newRed = min.getRed() + changeRed;
        int newGreen = min.getGreen() + changeGreen;
        int newBlue = min.getBlue() + changeBlue;
        
        if(newRed > 255)newRed = 255;
        if(newRed < 0) newRed = 0;
        if(newGreen > 255)newGreen = 255;
        if(newGreen < 0) newGreen = 0;
        if(newBlue > 255)newBlue = 255;
        if(newBlue < 0) newBlue = 0;
        return new Color(newRed,newGreen,newBlue);
    }
    protected Rectangle[] findRectangles(IImage image, List<MovingBlob> iBlobs)
    {
        //...
        Rectangle[] rectangles = new Rectangle[iBlobs.size()];
        for(int i = 0; i < iBlobs.size(); i++)
        {
            MovingBlob b = iBlobs.get(i);
            Point[] points = new Point[4];

            points[0] = new Point(b.x,b.y);
            points[1] = new Point(b.x+b.width,b.y);
            points[2] = new Point(b.x+b.width,b.y+b.height);
            points[3] = new Point(b.x,b.y+b.height);
            
            rectangles[i] = new Rectangle(points,b);
        }
        return rectangles;
    }
    protected void setPixels(BufferedImage b, IPixel[][] pixels)
    {
        //int[] pixelColors1D = new int[pixels.length * pixels[0].length];
        //int i = 0;
        //System.out.println("rows: " + pixels.length + " cols: " + pixels[0].length);
        //System.out.println("BI: rows: " + b.getHeight() + );
        for(int r = 0; r < pixels.length; r++)
        {
            if(useBasicColors)
            {
                for(int c = 0; c < pixels[0].length; c++)
                {

                    try
                    {

                        int colorNumber = pixels[r][c].getColor();
                        Color theColor;
                        if(colorNumber == 0)
                        {
                            theColor = Color.RED;
                        }
                        else if(colorNumber == 1)
                        {
                            theColor = Color.GREEN;
                        }
                        else if(colorNumber == 2)
                        {
                            theColor = Color.BLUE;
                        }
                        else if(colorNumber == 3)
                        {
                            theColor = Color.GRAY;
                        }
                        else if(colorNumber == 4)
                        {
                            theColor = Color.BLACK;
                        }
                        else if(colorNumber == 5)
                        {
                            theColor = Color.WHITE;
                        }
                        else
                        {
                            throw new RuntimeException()
                            {
                                public String toString()
                                {
                                    return "you are stupid";
                                }
                            };
                        }



                        int p = (255/*alpha*/ << 24) | (theColor.getRed() << 16) | (theColor.getGreen() << 8) | theColor.getBlue();
                        b.setRGB(c,r,p);
                    }
                    catch(ArrayIndexOutOfBoundsException e)
                    {

                    }


                }
            }
            else
            {
                for(int c = 0; c < pixels[0].length; c++)
                {

                    try
                    {


                        short red = pixels[r][c].getRed();
                        short green = pixels[r][c].getGreen();
                        short blue = pixels[r][c].getBlue();



                        int p = (255/*alpha*/ << 24) | (red << 16) | (green << 8) | blue;
                        b.setRGB(c,r,p);
                    }
                    catch(ArrayIndexOutOfBoundsException e)
                    {

                    }


                }
            }
            
        }
        
    }
    protected void drawLines(Rectangle[] rects, BufferedImage image, Color still, Color moving, int lineThickness)
    {
        Graphics2D g = (Graphics2D)image.getGraphics();
        BasicStroke bs = new BasicStroke(lineThickness);
        for(Rectangle r : rects)
        {
            for(int i = 0; i < 4; i++)
            {
                int indexOfNextPoint;
                if(i == 3)
                {
                    indexOfNextPoint = 0;
                }
                else
                {
                    indexOfNextPoint = i+1;
                }
                Point start = r.getPoints()[i];
                Point end = r.getPoints()[indexOfNextPoint];
                
                g.setStroke(bs);
                
                double velocity = Math.sqrt( (r.getBlob().velocityX)*(r.getBlob().velocityX) + (r.getBlob().velocityY)*(r.getBlob().velocityY) );
                
                Color lineColor = findColor(still,moving,velocity/maxVelocity);
                
                g.setColor(lineColor);
                g.drawLine((int)start.getX(),(int)start.getY(),(int)end.getX(),(int)end.getY());
            }
        }
    }
    
    
    public class Point
    {
        private float x,y;
        public Point(float x, float y)
        {
            this.x = x;
            this.y = y;
        }
        public float getX(){return x;}
        public float getY(){return y;}
    }
    public class Rectangle
    {
        private Point[] points;
        private MovingBlob b;
        public Rectangle(Point[] points, MovingBlob b)
        {
            this.points = points;
            this.b = b;
        }
        public Point[] getPoints()
        {
            return points;
        }
        public MovingBlob getBlob()
        {
            return b;
        }
    }
    
    
}