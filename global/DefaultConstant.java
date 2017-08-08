package global;

public class DefaultConstant {

    /**
	 * MovingBlobDetection
	 */

	//maximum time before unmatched MovingBlob is deleted
	public static int MAX_TIME_OFF_SCREEN = 0;
	
	//maximum distance in pixels between blobs that can be matched
	public static int DISTANCE_LIMIT_X = 40;
	public static int DISTANCE_LIMIT_Y = 20;
	
	//maximum size difference in pixels between blobs that can be matched
	public static int MAX_CHANGE_WIDTH = 29;
	public static int MAX_CHANGE_HEIGHT = 34;
	
	//maximum distance between edges to unify
	public static int X_EDGE_DISTANCE_LIMIT = 15;
	public static int Y_EDGE_DISTANCE_LIMIT = 25;
	public static float X_OVERLAP_PERCENT = 0.1f;
	public static float Y_OVERLAP_PERCENT = 0.1f;
	
	//maximum difference in velocity to unify
	public static int UNIFY_VELOCITY_LIMIT_X = 10;
	public static int UNIFY_VELOCITY_LIMIT_Y = 25;
	public static float VELOCITY_LIMIT_INCREASE_X = 0.75f;
	public static float VELOCITY_LIMIT_INCREASE_Y = 0.3f;

	/**
	 * BlobFilter
	 */

	//regular filters

	//Minimum age to not be filtered
	public static int AGE_MIN = 3;
	
	//Maximum 
	public static int VELOCITY_X_MAX = 100;
	public static int VELOCITY_Y_MAX = 50;
	public static float MAX_VELOCITY_CHANGE_X = 5;
	public static float MAX_VELOCITY_CHANGE_Y = 20;
	//Unified Blob filters

	//stuff
	public static float MAX_WIDTH_HEIGHT_RATIO = .75f;
	public static int MAX_WIDTH = 100000;
	public static int MAX_HEIGHT = 100000;
	public static float MIN_SCALED_VELOCITY_X = 0.75f;
	public static int MIN_SCALED_VELOCITY_Y = 0;
	
	/*
    Image
    FileImage
	 */
	
	//constants for color margin calibrations
	//ratio of absolute average deviation to greyMargin
	public static float GREY_RATIO = 0.75f;
	
	//how far to set black and white margins from mean
	public static int BLACK_RANGE = 100;
	public static int WHITE_RANGE = 200;
	

        public static <T extends Number> T getVariable(int index){
		switch(index){
		case 1: return (T)(Number)MAX_TIME_OFF_SCREEN ;
		case 2: return (T)(Number)DISTANCE_LIMIT_X;
		case 3: return (T)(Number)DISTANCE_LIMIT_Y;
		case 4: return (T)(Number)MAX_CHANGE_WIDTH;
		case 5: return (T)(Number)MAX_CHANGE_HEIGHT;
		case 6: return (T)(Number)X_EDGE_DISTANCE_LIMIT;
		case 7: return (T)(Number)Y_EDGE_DISTANCE_LIMIT;
		case 8: return (T)(Number)X_OVERLAP_PERCENT;
		case 9: return (T)(Number)Y_OVERLAP_PERCENT;
		case 10: return (T)(Number)UNIFY_VELOCITY_LIMIT_X;
		case 11: return (T)(Number)UNIFY_VELOCITY_LIMIT_Y;
		case 12: return (T)(Number)VELOCITY_LIMIT_INCREASE_X;
		case 13: return (T)(Number)VELOCITY_LIMIT_INCREASE_Y;
		case 14: return (T)(Number)AGE_MIN;
		case 15: return (T)(Number)VELOCITY_X_MAX;
		case 16: return (T)(Number)VELOCITY_Y_MAX;
		case 17: return (T)(Number)MAX_VELOCITY_CHANGE_X;
		case 18: return (T)(Number)MAX_VELOCITY_CHANGE_Y;
		case 19: return (T)(Number)MAX_WIDTH_HEIGHT_RATIO;
		case 20: return (T)(Number)MAX_WIDTH;
		case 21: return (T)(Number)MAX_HEIGHT;
		//case 22: return MAX_SCALED_VELOCITY_X;
		//case 23: return MAX_SCALED_VELOCITY_Y;


		}
                return (T)(Number)0;
        }

}