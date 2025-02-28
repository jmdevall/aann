package jmdevall.aann;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import jmdevall.aann.ListTrasposer;

public class ListTrasposerTest {
	
	@Test
	public void traspose() {
		// Ejemplo de lista de listas
		List<List<String>> outputsOfEachNeuron = new ArrayList<>();
		outputsOfEachNeuron.add(List.of("a1", "a2", "a3"));
		outputsOfEachNeuron.add(List.of("b1", "b2", "b3"));
		outputsOfEachNeuron.add(List.of("c1", "c2", "c3"));

		// Trasponer la lista de listas
		List<List<String>> transposedList = ListTrasposer.transpose(outputsOfEachNeuron);

		// Imprimir la lista traspuesta
		for (List<String> row : transposedList) {
			System.out.println(row);
		}
	}
}
