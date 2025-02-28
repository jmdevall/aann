package jmdevall.aann;

import java.util.ArrayList;
import java.util.List;

public class NetworkLayer {
	
	private List <PerceptronAgent> neurons;
	private InputUnificator inputUnificator;
	
	
	public NetworkLayer(List<PerceptronAgent> neurons, InputUnificator inputUnificator) {
		super();
		this.neurons = neurons;
		this.inputUnificator = inputUnificator;
	}

	private List<String> lastinputs;

	public List<String> forward(List<String> inputs) {
		ArrayList<String> outputs=new ArrayList<>();
		for(PerceptronAgent n:neurons) {
			outputs.add(n.forward(inputs));
		}
		this.lastinputs=inputs;
		return outputs;
	}
	
	public List<String> backward(List<String> expectedOutputs) {
		if(expectedOutputs.size()!=neurons.size()) {
			throw new IllegalArgumentException("expectedOutputs size != neurons.size");
		}
		
		List<List<String>> outputsOfEachNeuron=new ArrayList<List<String>>();
		
		for(int i=0;i<expectedOutputs.size();i++) {
			List<String> shouldBeInputs=this.neurons.get(i).backpropage(lastinputs,expectedOutputs.get(i));
			outputsOfEachNeuron.add(shouldBeInputs);
		}
		
		return unificate(ListTrasposer.transpose(outputsOfEachNeuron));
		
	}

	private List<String> unificate(List<List<String>> responses) {
		List<String> unification=new ArrayList<String>();
		
		for(List<String> divereInputsDesiredByEachNeuron:responses) {
			unification.add(inputUnificator.unifica(divereInputsDesiredByEachNeuron));
		}
		return unification;
	}

	public void logWeights() {
		for(int n=0;n<neurons.size();n++) {
			System.out.println("neuron "+n+": "+neurons.get(n).getWeights());
		}
	}
	
}
