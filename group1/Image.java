package group1;

import java.io.*;
//import fly2cam.FlyCamera;
import group1.fly0cam.FlyCamera;

//Defines image as an 2d array of pixels
class Image implements IImage{
	
	private int height;
	private int width;

	private int frameRate = 3;
	private FlyCamera flyCam = new FlyCamera();
	
	//307200
	private byte[] camBytes = new byte[2457636];
	private IPixel[][] image;

	Image(int width, int height){

		this.height = height;
		this.width = width;
		image = new Pixel[width][height];


	}

	
	public IPixel[][] getImage(){
		return image;
	}
	

	//gets a single frame
	public void readCam(){

		System.out.println(flyCam.Connect(frameRate));
		System.out.println(flyCam.errn);
		flyCam.NextFrame(camBytes);
		System.out.println(flyCam.errn);
		byteConvert();

	}

	public void finish(){

		flyCam.Finish();

	}

	private void byteConvert(){

		int pos = 0;
		System.out.println("Position: " + pos);

		for(int i = 0 ; i < height * 2 ; i += 2){


			for(int j = 0 ; j < width * 2 ; j += 2){

				System.out.println(camBytes[pos]+"  "+camBytes[pos+1]+"  "+camBytes[pos + 1 + width * 2]);
				image[j/2][i/2] = new Pixel((short)(camBytes[pos]+128), (short)(camBytes[pos + 1]+128), (short)(camBytes[pos + 1 + width * 2]+128));

				pos += 2;

			}

			pos += width * 2;

		}

	}
	
}