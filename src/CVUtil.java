import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

public class CVUtil {
	public Mat rotateImage(Mat src, double angle) {
		Mat dst = new Mat();
		
		Point rotPoint = new Point(src.cols() / 2, src.rows() / 2);

		// Create Rotation Matrix
		Mat rotMat = Imgproc.getRotationMatrix2D(rotPoint, angle, 1);
		
		// Apply Affine Transformation
		Imgproc.warpAffine(src, dst, rotMat, src.size(), Imgproc.INTER_CUBIC, Core.BORDER_REPLICATE);
		
		return dst;
	}
}
