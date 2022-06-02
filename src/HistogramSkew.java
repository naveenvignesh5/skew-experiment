import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

public class HistogramSkew {
	private int determineScore(Mat src, double angle) {
		Mat rotated = rotateImage(src, angle);
		
		int cols = rotated.cols();
		
		double[] histogram = new double[cols];
		
		for (int i = 0; i < cols; i++) {
			DoubleStream stream = DoubleStream.of(rotated.get(new int[] {i}));
			histogram[i] = stream.sum();
		}
		
		int score = 0;
		
		for (int i = 1; i < cols; i++) {
			score += Math.pow(histogram[i] + histogram[i - 1], 2);
		}
		
		return score;
	}
	
	private Mat rotateImage(Mat src, double angle) {
		Mat dst = new Mat();
		
		Point rotPoint = new Point(src.cols() / 2, src.rows() / 2);

		// Create Rotation Matrix
		Mat rotMat = Imgproc.getRotationMatrix2D(rotPoint, angle, 1);
		
		// Apply Affine Transformation
		Imgproc.warpAffine(src, dst, rotMat, src.size(), Imgproc.INTER_CUBIC, Core.BORDER_REPLICATE);
		
		return dst;
	}
	
	public Mat correctSkew(Mat src) {
		Mat dst = new Mat();
		
		Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2GRAY);
		Imgproc.threshold(dst, dst, 0, 255, Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);
		
		List<Integer> scores = new ArrayList<>();
		
		int[] angles = IntStream.rangeClosed(-5, 5).toArray();
		
		for (int angle: angles) {
			scores.add(determineScore(rotateImage(dst, angle), angle));
		}
		
		int maxIndex = scores.indexOf(Collections.max(scores, null));
		
		int correctAngle = angles[maxIndex];
		
		System.out.println("Correct Angle: " + correctAngle);
		
		return rotateImage(src, correctAngle);
	}
}
