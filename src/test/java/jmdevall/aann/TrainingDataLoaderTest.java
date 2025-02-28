package jmdevall.aann;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class TrainingDataLoaderTest {

	@Test
	public void canReadParquetFile() {
		TrainingDataLoader sut=SetupUtil.getTrainingDataLoader();
		
		List<TrainingData> trainingDataSet=sut.readParquet();
		assertEquals(1000,trainingDataSet.size());
	}
}
