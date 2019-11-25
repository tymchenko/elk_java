package test.person;

import java.io.IOException;

public class Main {



	public static void main(String[] args) throws IOException {
		PersonManager personManager = new PersonManager();

		personManager.makeConnection();

		System.out.println("Inserting a new Person with name Shubham...");
		Person person = new Person();
		person.setName("Shubham");
		person = personManager.insertPerson(person);
		System.out.println("Person inserted --> " + person);

		System.out.println("Changing name to `Shubham Aggarwal`...");
		person.setName("Shubham Aggarwal");
		personManager.updatePersonById(person.getPersonId(), person);
		System.out.println("Person updated  --> " + person);

		System.out.println("Getting Shubham...");
		Person personFromDB = personManager.getPersonById(person.getPersonId());
		System.out.println("Person from DB  --> " + personFromDB);

		System.out.println("Deleting Shubham...");
		personManager.deletePersonById(personFromDB.getPersonId());
		System.out.println("Person Deleted");

		personManager.closeConnection();
	}
}
