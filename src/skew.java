import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class skew {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public static void main(String[] args) {
		CVUtil cvUtil = new CVUtil();

		int angle = 0;

			
		// papandreo technique
		PapandreoTechnique papandreoTechnique = new PapandreoTechnique();
		angle = papandreoTechnique.skewDetectImageRotation(Imgcodecs.imread("images/skew.jpg"));
		
		System.out.println("Papandreo Technique - angle: " + angle);
		
		Mat rotated = new Mat();
		
		// rotate image
		rotated = cvUtil.rotateImage(Imgcodecs.imread("images/skew.jpg"), angle);
		Imgcodecs.imwrite("images/results/papandreo-correct.jpg", rotated);
		
//		// hull technique
		HullTextSkewDetect hullTextSkewDetect = new HullTextSkewDetect();
		angle = hullTextSkewDetect.skewDetectPixelRotation(Imgcodecs.imread("images/skew.jpg"));
		
		System.out.println("Hull Text Technique - angle: " + angle);

//		// rotate image
		rotated = cvUtil.rotateImage(Imgcodecs.imread("images/skew.jpg"), angle);
		Imgcodecs.imwrite("images/results/hull-correct.jpg", rotated);
		
		// postltextskew technique
		PostlTextSkewDetect postlTextSkewDetect = new PostlTextSkewDetect();
		angle = postlTextSkewDetect.skewDetectImageRotation(Imgcodecs.imread("images/skew.jpg"));
		
		System.out.println("Postl Text Skew Technique - angle: " + angle);

//		// rotate image
		rotated = cvUtil.rotateImage(Imgcodecs.imread("images/skew.jpg"), angle);
		Imgcodecs.imwrite("images/results/postl-correct.jpg", rotated);

		// litextskew technique
		LiTextSkewDetector liTextSkewDetector = new LiTextSkewDetector();
		angle = liTextSkewDetector.skewDetectImageRotation(Imgcodecs.imread("images/skew.jpg"));
		
		System.out.println("LiText Skew Technique - angle: " + angle);
//
//		// rotate image
		rotated = cvUtil.rotateImage(Imgcodecs.imread("images/skew.jpg"), angle);
		Imgcodecs.imwrite("images/results/litext-correct.jpg", rotated);
				
		HistogramSkew histogramSkew = new HistogramSkew();
		
		Mat res = histogramSkew.correctSkew(Imgcodecs.imread("images/skew.jpg"));
		Imgcodecs.imwrite("images/results/histogram-correct.jpg", res);
		
		System.out.println("Processing done !!!");
	}
}
