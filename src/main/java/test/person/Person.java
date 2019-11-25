package test.person;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Person {

	@Getter
	@Setter
	private String personId;

	@Getter
	@Setter
	private String name;
}
