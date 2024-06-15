package es.upm.pproject.parkingjam.parking_jam;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import es.upm.pproject.parkingjam.parking_jam.utilities.Car;

@DisplayName ("Tests related to testing the class Car")
@Nested
class CarTest {
	@Test
	void getSymbolTest() {
		Car car = new Car('a', 1, 5, 3, 'V');
		assertEquals('a', car.getSymbol());
	}
	
	@Test
	void getOrientationTest() {
		Car car = new Car('a', 1, 5, 3, 'V');
		assertEquals('V', car.getOrientation());
	}
	
	@Test
	void getCoordinatesTest() {
		Car car = new Car('a', 1, 5, 3, 'V');
		assertEquals(1, car.getCoordinates().getX());
		assertEquals(5, car.getCoordinates().getY());
	}
	
	@Test
	void getLengthTest() {
		Car car = new Car('a', 1, 5, 3, 'V');
		assertEquals(3, car.getLength());
	}
	
	@Test
	void setSymbolTest() {
		Car car = new Car('a', 1, 5, 3, 'V');
		car.setSymbol('b');
		assertEquals('b', car.getSymbol());
	}
	
	@Test
	void setOrientationTest() {
		Car car = new Car('a', 1, 5, 3, 'V');
		car.setOrientation('H');
		assertEquals('H', car.getOrientation());
	}
	
	@Test
	void setCoordinatesTest() {
		Car car = new Car('a', 1, 5, 3, 'V');
		car.setCoordinates(3, 2);
		assertEquals(3, car.getCoordinates().getX());
		assertEquals(2, car.getCoordinates().getY());
	}
	
	@Test
	void setLengthTest() {
		Car car = new Car('a', 1, 5, 3, 'V');
		car.setLength(5);
		assertEquals(5, car.getLength());
	}
	
	@Test
	void equalsTest() {
		Car car1 = new Car('a', 1, 5, 3, 'V');
		Car car2 = new Car('a', 1, 5, 3, 'V');
		assertEquals(car1, car2);
		car2.setLength(2);
		assertNotEquals(car1, car2);
	}
	
	@Test
	void hashCodeTest() {
		Car car1 = new Car('a', 1, 5, 3, 'V');
		Car car2 = new Car('a', 1, 5, 3, 'V');
		assertEquals(car1.hashCode(), car2.hashCode());
		car2.setLength(2);
		assertNotEquals(car1.hashCode(), car2.hashCode());
	}
}
