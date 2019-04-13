package application;


import static java.util.Collections.reverseOrder;
import static java.util.Comparator.comparing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Pair;

public class FXController 
{

	@FXML
	private ImageView ivAdaptiveThreshold;
	
	@FXML
	private Label lblCannyEdgeDetection;
	
	@FXML
	private ImageView ivCannyEdgeDetection;
	
	@FXML
	private Button btnSetTrainingDir;
	
	@FXML
	private Button btnSetTestingDir;
		
	@FXML
	private Button btnClassifyTestData;
	
	@FXML
	private ImageView ivOriginalImage;
	
	@FXML
	private Label  lblTitle;
	
	@FXML
	private Label lblDisplayImages;
	
	@FXML
	private Label lblCurrency;
	
	@FXML
	private Label lblHistogramEqualisation;
	
	@FXML
	private ImageView ivHistogramEqualisation;
	
	@FXML
	private Label lblAdaptiveThreshold;
	
	@FXML
	private Label lblOriginalImage;
	
	@FXML
	private Label lblEstimatedImage;
	
	@FXML
	private Label lblImageValue;
	
	@FXML
	private Label lblMedianBlur;
	
	@FXML
	private ImageView ivMedianBlurImage;
	
	@FXML
	private Label lblNoteValue;
	
	@FXML
	private Label lblOriginalColourImage;
	
	@FXML
	private ImageView ivOriginalColourImage;
	
	public static void HuMoments_TrainingSet()
	{
		
		List<String> imageNames = new ArrayList<String>();
		String location = "";
		try
		{
			
			DirectoryChooser fc = new DirectoryChooser();
			File selectedFile = fc.showDialog(null);

			if (selectedFile != null) {

			    System.out.println("File selected: " + selectedFile.getName());
			}

			location = selectedFile.getPath();

	        
			File[] files = new File(location).listFiles(); 

			for (File file : files) {
			    if (file.isFile()) {
			    	imageNames.add(file.getName());
			    }
			}
			
		System.out.println(imageNames);
		}
		catch(Exception e) {System.out.println(e.toString());}
				
			
	        PrintWriter writer;
			try
			{
				writer = new PrintWriter("TrainingResults.txt", "UTF-8");	
				
				
				for(String item:imageNames)
				{
				        
				        double [] fv = new double [7];
				        

						//System.out.println("Reading Image: "+item);
						Mat source = Imgcodecs.imread(location+"/"+item, 0);
						
						source = PreProcessed_Matix(source,item);
						
						
						
						Moments m = new Moments();
					    m = Imgproc.moments(source, false);
					    Mat hu = new Mat();
					 
					    Imgproc.HuMoments(m, hu);
					        
					    int count = 0;
					        for(int row = 0; row < hu.rows(); row++)
					        {
					        	for(int col  = 0; col < hu.cols(); col++)
					        	{
					        		fv[count] = (hu.get(row, col)[0]);
					        		count++;
					        	}
					        }
					        
				        
				        
				       // System.out.println(item+" :   "+Arrays.toString(fv)+"..."+imageNames.indexOf(item)+"/"+imageNames.size());
				        writer.println(item+" :   "+Arrays.toString(fv));
				        
				}
				
				
				writer.close();
			        
				
			} 
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	   
	}
	
	public static void HuMoments_Test()
	{
		
		List<String> imageNames = new ArrayList<String>();//Testing
		String location = "";
		try
		{
			FileChooser fileChooser = new FileChooser();
			File selectedFile = fileChooser.showOpenDialog(null); 

			if (selectedFile != null) {

			    System.out.println("File selected: " + selectedFile.getName());
			}

			location = selectedFile.getPath();
			

			imageNames.add(selectedFile.getName());
			
		}
		catch(Exception e) {System.out.println(e.toString());}

		
		PrintWriter writer;
		try
		{
			writer = new PrintWriter("TestingResults.txt", "UTF-8");	
			
			
			for(String item:imageNames)
			{
				double [] testfv = new double [7];
		        
		        //System.out.println("Reading Image: "+item);
		        System.out.println(location+item);
		        
				Mat source = Imgcodecs.imread(location, 0);
				
				Mat colour = Imgcodecs.imread(location);
				Imgcodecs.imwrite("OriginalColour.jpg", colour);

				source = PreProcessed_Test(source,item);
		        
		        		        
		        //System.out.println("Calculating Hu Moments");
				Moments m = new Moments();
			    m = Imgproc.moments(source, false);
			    Mat hu = new Mat();
			   
			    Imgproc.HuMoments(m, hu);
			       
			    int count = 0;
			        for(int row = 0; row < hu.rows(); row++)
			        {
			        	for(int col  = 0; col < hu.cols(); col++)
			        	{
			        		testfv[count] = (hu.get(row, col)[0]);
			        		count++;
			        	}
			        }
		        
		      
		        //System.out.println(item+" :   "+Arrays.toString(testfv));
		        writer.println(item+" :   "+Arrays.toString(testfv));
		        
			       
			}
			
			
			writer.close();
		        
			
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
 
	}
	
	public static Mat PreProcessed_Matix(Mat source, String imageName)
	{
		
		//System.out.println("Median Image: "+imageName);
		Mat destination = new Mat(source.rows(),source.cols(),source.type());
       
	    //System.out.println("equalizeHist Image: "+imageName);
        Imgproc.equalizeHist(source, source);
        
        //System.out.println("adaptiveThreshold Image: "+imageName);
        Imgproc.adaptiveThreshold(source, source, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 9, 1);
       
        Imgproc.medianBlur(source, destination, 1);
        Core.addWeighted(source, 1.5, destination, -0.5, 0, destination);
       
        //System.out.println("Canny Image: "+imageName);
        
        Imgproc.Canny(source, source, 10, 100, 3, false);
        Core.bitwise_not(source, source);
       
       
       
       return source;
	}
	
	public static Mat PreProcessed_Test(Mat source, String imageName)
	{
		
		Imgcodecs.imwrite("Original.jpg", source);
		
		
		
		//System.out.println("MedianBlur Image: "+imageName);
		Mat destination = new Mat(source.rows(),source.cols(),source.type());
        Imgproc.GaussianBlur(source, destination, new Size(3,3), 1);
        Core.addWeighted(source, 1.5, destination, -0.5, 0, destination);
        Imgcodecs.imwrite("MedianBlur.jpg", source);
        
		//System.out.println("equalizeHist Image: "+imageName);
       Imgproc.equalizeHist(source, source);
       Imgcodecs.imwrite("HistoEq.jpg", source);
       
       
       //System.out.println("adaptiveThreshold Image: "+imageName);
       Imgproc.adaptiveThreshold(source, source, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 9, 1);
       Imgcodecs.imwrite("AdThreshold.jpg", source);

       
       
         //System.out.println("Canny Image: "+imageName);
         Imgproc.Canny(source, source, 10, 100, 3, false);
         Core.bitwise_not(source, source);
         
         Imgcodecs.imwrite("CannyEdge.jpg", source);
              
       return source;
	}
	
	public static String Distance_Data(String imageName, double [] testfv)
	{
		List<Pair<String, Double>> sortedDist = new ArrayList<Pair<String, Double>>();
        String result = "";
		try
		{
			ArrayList <String> distances = new ArrayList <String>();
			
			String bestFit = "";
			double bestFitVal = 0;
						
			File file = new File("TrainingResults.txt");
			 
			 BufferedReader br = new BufferedReader(new FileReader(file));
			 
			  String st;
			  int count = 0;
			  while ((st = br.readLine()) != null)
			  {
				  String name = (st.substring(0, st.indexOf(" ")));
				  
				  String values = st.substring(st.indexOf("[")+1, st.lastIndexOf("]"));
				  
				  String [] temp = values.split(", ");
				  
				  double tempValue = 0;
				  for(int feature = 0; feature < temp.length; feature++)
				  {
					  //calc abs v1 -v2
					  
					  tempValue += Math.abs(Double.parseDouble(temp[feature]) - testfv[feature]);
				  }
				  
				  if(count == 0)
				  {
					  bestFit = name;
					  bestFitVal = tempValue; 
				  }
				  else
				  {
					  if(tempValue < bestFitVal)
					  {
						  bestFitVal = tempValue;
						  bestFit = name;
					  }
				  }
				  
				  sortedDist.add(new Pair<String, Double>(name,tempValue));
				  distances.add(name+"  :  "+tempValue);
				  count++;
			  }
			  br.close();
			  
			  System.out.println("-----Distances----");
			  
			  final Comparator<Pair<String, Double>> c = reverseOrder(comparing(Pair::getValue));
			  Collections.sort(sortedDist, c);
			  
			  for(Pair<String, Double> item:sortedDist)
			  {
				  System.out.println(item);
			  }
			  
			 
			  
			  result = bestFit.substring(bestFit.indexOf("#")+1);
			  
			  result = result.substring(0,result.indexOf("#"));
		
			  
		}
		catch(Exception e)
		{
			
			System.out.println(e.toString());
		}
		
		return result;
		
	}
	
	public static String Classification()
	{
		String result = "";
		try
		{
			File file = new File("TestingResults.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String st;
			
			 while ((st = br.readLine()) != null)
			 {
				 String name = (st.substring(0, st.indexOf(" ")));
				  
				  String values = st.substring(st.indexOf("[")+1, st.lastIndexOf("]"));
				  
				  String [] temp = values.split(", ");
				  
				  double[] fv = Arrays.stream(temp)
	                        .mapToDouble(Double::parseDouble)
	                        .toArray();
				  
				 result = Distance_Data(name, fv);
				 
			 }
			 br.close();
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	
	@FXML
	public void btnSetTrainingDir_OnAction()
	{
		HuMoments_TrainingSet();
		
		
		btnSetTestingDir.setDisable(false);
	}
	
	@FXML
	public void btnSetTestingDir_OnAction()
	{
		 HuMoments_Test();
		
		File file = new File("Original.jpg"); // saved as original image in Processed image folder
        Image image = new Image(file.toURI().toString());
        ivOriginalImage.setImage(image);
        
        file = new File("MedianBlur.jpg");
        image = new Image(file.toURI().toString());
        ivMedianBlurImage.setImage(image);
        
        file = new File("HistoEq.jpg");
        image = new Image(file.toURI().toString());
        ivHistogramEqualisation.setImage(image);
        
        file = new File("AdThreshold.jpg");
        image = new Image(file.toURI().toString());
        ivAdaptiveThreshold.setImage(image);
        
        file = new File("CannyEdge.jpg");
        image = new Image(file.toURI().toString());
        ivCannyEdgeDetection.setImage(image);
        
        file = new File("OriginalColour.jpg");
        image = new Image(file.toURI().toString());
        ivOriginalColourImage.setImage(image);
        
        
        
        
        btnClassifyTestData.setDisable(false);
	}
	
	@FXML
	public void btnClassifyTestData_OnAction()
	{
		lblNoteValue.setText(Classification());
		
	}
}
