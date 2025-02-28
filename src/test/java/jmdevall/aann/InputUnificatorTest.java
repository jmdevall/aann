package jmdevall.aann;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import jmdevall.aann.InputUnificator;

public class InputUnificatorTest {

	@Test
	public void unificate() {

		List<String> inputs=Arrays.asList(
				"The debate will be held at 7 PM in the Main Hall. Topics include economic policy and social reforms.",
				"The forum is scheduled for 7 PM in the Auditorium. We will discuss fiscal policy and healthcare reform.",
				"The discussion is at 7 PM in the Conference Room. Topics include economic strategy and social programs.",
				"The panel is at 7 PM in the Main Hall. Key points: economic policy and healthcare reform.",
				"The event is at 7 PM in the Auditorium. Focus on fiscal strategy and social reforms."
		);
		
		InputUnificator sut=SetupUtil.getInputUnificator();
		String result=sut.unifica(inputs);
		System.out.println("result="+result);
	}
	
}
