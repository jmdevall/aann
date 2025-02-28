package jmdevall.aann;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;


public class TrainingTest {

	TrainingDataLoader trainingDataLoader=SetupUtil.getTrainingDataLoader();
	NetworkLayerFactory nlf=SetupUtil.getNetworkLayerFactory();
	
	@Test
	public void trainFullNetwork() {

		List<NetworkLayer> layers = Arrays.asList(
			nlf.newNetworkLayer(5), // input Layer
			nlf.newNetworkLayer(7), // hidden layer
			nlf.newNetworkLayer(3), // other hidden layer
			nlf.newNetworkLayer(2)  // output layer (the size MUST be the same as the 
		);
		Network sut=new Network(layers);
		
		List<TrainingData> trainingData=trainingDataLoader.readParquet();
		
		sut.logWeights();
		for(int trainingStrep=0;trainingStrep<6;trainingStrep++) {
			TrainingData data=trainingData.get(trainingStrep);
			sut.train(Arrays.asList(data.getQuestion()),
				 Arrays.asList(data.getSolution(),data.getThinking()));
			System.out.println("end of training step "+trainingStrep);
			sut.logWeights();
		}
	}
	
	@Test
	public void trainSinglePerceptron() {
		PerceptronAgent sut=SetupUtil.newPerceptronAgent();
		
		List<TrainingData> trainingData=trainingDataLoader.readParquet();
		
		for(int td=0;td<6;td++) {
			TrainingData data=trainingData.get(td);
			sut.train(Arrays.asList(data.getQuestion()), data.getSolution());
			System.out.println("==== STEP === "+td);
			System.out.println("weight "+sut.getWeights());
		}
	}
}
