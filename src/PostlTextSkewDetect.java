import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class PostlTextSkewDetect {
	static final Scalar COLOR_GREEN = new Scalar(0, 255, 0);
	static final Scalar COLOR_RED = new Scalar(0, 0, 255);
	
	public int skewDetectImageRotation(Mat mat) {
		int[] projections = null;
		int[] angle_measure=new int[181];
		int angle=0;
		
		try{
			for(int theta=0;theta<=180;theta=theta+5)
			{	
				Mat rotImage = RotateImage(mat,-theta);
				projections = new int[mat.rows()];
				
				for(int i=0;i<mat.rows();i++) {
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
				
				Mat tempMat=mat.clone();
				
				for(int r=0; r < mat.rows(); r++) {				
					DrawProjection(r,projections[r],tempMat);				
				}			
				
				angle_measure[theta] = criterion_func(projections);
			}
			
			
			int val = 0;
			
			for(int i = 0; i < angle_measure.length; i++) {
				if(val<angle_measure[i]) {
					val = angle_measure[i];
					angle = i;
					System.out.println(angle);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return angle;
	}
	
	private Mat RotateImage(Mat rotImg,double theta) {
		Mat rotatedImage = new Mat();
		try{
			double angleToRot=theta;	
			
			
			if(angleToRot >= 92 && angleToRot <= 93) {		
				Core.transpose(rotImg, rotatedImage);
			} else {
				Point center = new Point(rotImg.cols()/2, rotImg.rows()/2);
				Mat rotImage = Imgproc.getRotationMatrix2D(center, angleToRot, 1.0);
				
				Imgproc.warpAffine(rotImg, rotatedImage, rotImage, rotImg.size());		
			}
		} catch(Exception e) {}
		
		return rotatedImage;
			
	}

	private int criterion_func(int[] projections) {
		int max=0;		
		//use below code for image rotation
		//for(int i=0;i<projections.length-1;i++)
			//result+=Math.pow((projections[i+1]-projections[i]), 2);
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
        Imgproc.line(image, pt1, pt2, COLOR_GREEN);
	}
}
