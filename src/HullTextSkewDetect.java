import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class HullTextSkewDetect {

	static final String SOURCE_PATH="images/skew.jpg";
	static final String DEST_PATH="images/";
	
	static final Scalar COLOR_GREEN = new Scalar(0, 255, 0);
	static final Scalar COLOR_RED = new Scalar(0, 0, 255);
	
	
	public int skewDetectPixelRotation(Mat mat) {
		int[] projections = null;
		int[] angle_measure=new int[181];

		for(int theta=0;theta<=180;theta=theta+5)
		{	
			projections=new int[mat.rows()];
			for(int i=0;i<mat.rows();i++)
			{				
				double[] pixVal;				
				for(int j=0;j<mat.cols();j++)
				{
					pixVal= mat.get(i, j);
					if(pixVal[0]==0)//black pixel
					{
						int new_row=rotate(i,j,theta,mat);
						if(new_row>=0 && new_row<mat.rows())
							projections[new_row]++;
					}
				}
			}
			Mat tempMat=mat.clone();
			for(int r=0;r<mat.rows();r++)
			{				
				DrawProjection(r,projections[r],tempMat);				
			}			
			//Highgui.imwrite(DEST_PATH+"/out_"+theta+".jpg",tempMat);
			angle_measure[theta]=criterion_func(projections);
			
		}
		int angle=0;
		int val=0;
		for(int i=0;i<angle_measure.length;i++)
		{
			if(val<angle_measure[i])
			{
				val=angle_measure[i];
				angle=i;
			}
		}
		return angle;
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

	
	//Rotation about the center of the image
	private int rotate(double y1, double x1, int theta,Mat mat) {
		int x0=mat.cols()/2;
		int y0=mat.rows()/2;
		
		int new_col=(int) ((x1-x0)*Math.cos(Math.toRadians(theta))-(y1-y0)*Math.sin(Math.toRadians(theta))+x0);
		int new_row=(int) ((x1-x0)*Math.sin(Math.toRadians(theta))+(y1-y0)*Math.cos(Math.toRadians(theta))+y0);
		
		return new_row;
		
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