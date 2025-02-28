package jmdevall.aann;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Network {

	private List<NetworkLayer> layers;
	
	public void train(List<String> input, List<String> expectedOutput) {
		
	    // Forward pass
	    List<String> currentInput = new ArrayList<String>(input);
	    for (NetworkLayer l : layers) {
	        currentInput=l.forward(currentInput);
	    }

	    List<String> expected=expectedOutput;
  
	    // Backward pass
	    for (int l = layers.size() - 1; l >= 0; l--) {
	        NetworkLayer layer = layers.get(l);
	        expected = layer.backward(expected);
	    }
	}
	
	public void logWeights() {
		for(int l=0;l<layers.size();l++) {
			System.out.println("Layer "+l);
			layers.get(l).logWeights();
				
		}
		
	}
}
