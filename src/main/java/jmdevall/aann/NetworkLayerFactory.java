package jmdevall.aann;

import java.util.ArrayList;
import java.util.List;

import dev.langchain4j.model.chat.ChatLanguageModel;
import jmdevall.aann.thinkresponseextractor.ThinkResponseExtractor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NetworkLayerFactory {
	ChatLanguageModel llm;
	WeightsGenerator wg;
	InputUnificator iu;
	ThinkResponseExtractor thinkResponseExtractor;
	
	public NetworkLayer newNetworkLayer(int size) {
		List<PerceptronAgent> neurons=new ArrayList<>();
		
		for(int i=0;i<size;i++) {
			neurons.add(new PerceptronAgent(llm, wg.generateRandomWeight(),thinkResponseExtractor));
		}
		
		return new NetworkLayer(neurons,this.iu);	
	}
}
