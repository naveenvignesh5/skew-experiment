import java.util.ArrayList;
import java.util.HashMap;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class LiTextSkewDetector {
	static final Scalar COLOR_WHITE = new Scalar(255, 255, 255);
	static final Scalar COLOR_RED = new Scalar(0, 0, 255);
	
	static int Haar_Iters = 2;
	
	static final String DEST_PATH = "images/litextskew/";
	
	public int skewDetectImageRotation(Mat mat) {
		int[] projections = null;
		HashMap<Integer, Double> angle_measure=new HashMap<Integer, Double>();
		
		for(int theta=-15;theta<=15;theta=theta+1)
		{	
			if(theta == 0 || theta == -1 || theta == 1)
				continue;
			Mat rotImage=RotateImage(mat,-theta);
			projections=new int[mat.rows()];
			for(int i=0;i<mat.rows();i++)
			{
				double[] pixVal;
				for(int j=0;j<mat.cols();j++)
				{
					
					pixVal= rotImage.get(i, j);
					if(pixVal[0]==255)
					{
						projections[i]++;
					}
				}
			}
			Mat tempMat=rotImage;
			for(int r=0;r<mat.rows();r++)
			{				
				DrawProjection(r,projections[r],tempMat);				
			}	
			
			Imgcodecs.imwrite(DEST_PATH+theta+".jpg",tempMat);
			angle_measure.put(theta, criterion_func(projections));
		}
		int angle=0;
		double val=0;
		for(int k: angle_measure.keySet())
		{
			if(val<angle_measure.get(k))
			{
				val=angle_measure.get(k);
				angle=k;
			}
		}
		return angle;
	}
	
    public ArrayList<double[][]> test_ordered_haar(double[][] data, int n, int num_steps_forward, int num_steps_back) {
        ArrayList<double[][]> transform = TwoDHaar.orderedFastHaarWaveletTransformForNumIters(data, n, num_steps_forward);     
        return transform;   
    }
    
	private Mat RotateImage(Mat rotImg,double theta)
	{
		double angleToRot=theta;	
		
		Mat rotatedImage = new Mat();
		if(angleToRot>=92 && angleToRot<=93)
		{		
			Core.transpose(rotImg, rotatedImage);
		}
		else
		{
			org.opencv.core.Point center = new org.opencv.core.Point(rotImg.cols()/2, rotImg.rows()/2);
			Mat rotImage = Imgproc.getRotationMatrix2D(center, angleToRot, 1.0);
			
			Imgproc.warpAffine(rotImg, rotatedImage, rotImage, rotImg.size());		
		}
		
		return rotatedImage;
			
	}

	private double criterion_func(int[] projections) {
		double max=0;		
		
		for(int i=0;i<projections.length;i++)
		{
			if(max<projections[i])
			{
				max=projections[i];
			}
		}
		
		return max;
	}

	private void DrawProjection(int rownum,int projCount,Mat image) {
		final Point pt1 = new Point(0, -1);
        final Point pt2 = new Point();        
        pt1.y = rownum;
        pt2.x = projCount;
        pt2.y = rownum;
        Imgproc.line(image, pt1, pt2, COLOR_WHITE);
	}
}
