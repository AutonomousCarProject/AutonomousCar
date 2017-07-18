package group4;

import group3.MovingBlob;
import group4.IMovingBlobReduction;

import java.util.List;

public class BlobFilter implements IMovingBlobReduction
{
	/**
	 * Constant thresholds which do all the magic
	 * All inclusive thresholds
	 */

	//the maximum ratio of w/h the blob can be to be considered valid
	private static final double WIDTH_HEIGHT_RATIO_MAX = 1;
	//the minimum age (in frames) the blob must be to be considered valid
	private static final short AGE_MIN = 10;
	//the maximum X velocity the blob can have to be considered valid
	private static final short X_VELOCITY_MAX = 10;
	
	/**
	 * Checks the list of potential pedestrian blobs to distinguish pedestrians from non-pedestrians.
	 * Non-pedestrians are removed from the list of blobs.
	 * 
	 * @param blobs 	the list of potential pedestrian blobs
	 * @return			the list of blobs determined to be pedestrians
	 */
	public List<MovingBlob> reduce(List<MovingBlob> blobs)
	{
		for (int i = 0; i < blobs.size(); i++)
		{
			if (!isPedestrian(blobs.get(i)))
			{
				blobs.remove(i--);
			}
		}
		return blobs;
	}
	
	/**
	 * Checks an individual blob to determine if it is a pedestrian or non-pedestrian. Determination
	 * is made based on blob width vs. height, blob 'age' on screen, and blob xVelocity.
	 * 
	 * @param blob 		the blob being checked
	 * @return 			if the blob is a pedestrian
	 */
	private boolean isPedestrian(MovingBlob blob)
	{
		//lol formatting wut
		return  blob.width <= blob.height * WIDTH_HEIGHT_RATIO_MAX
				&& blob.age >= AGE_MIN
				&& Math.abs(blob.movementX) <= X_VELOCITY_MAX;
	}
}