package jmdevall.aann;

import org.junit.jupiter.api.Test;

public class WeightsGeneratorTest {

	@Test
	public void weigthsGeneratorGenerateRandomPrompt() {
		WeightsGenerator sut=SetupUtil.getWeightsGenerator();

		System.out.println(sut.generateRandomWeight());
	}
	
}
