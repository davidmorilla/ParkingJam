package es.upm.pproject.parkingjam.parking_jam;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import es.upm.pproject.parkingjam.parking_jam.utilities.Coordinates;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;

@DisplayName("Tests related to testing the classes Pair and Coordinates ")
@Nested
public class UtilitiesTest {
	
	@DisplayName ("Tests related to testing the class Coordinates")
	@Nested
	class CoordinatesTests {
		
		@Test
		void testGetX() {
			Coordinates coordsTest = new Coordinates(3,4);
			assertSame(3, coordsTest.getX());
		}
		
		@Test
		void testGetY() {
			Coordinates coordsTest = new Coordinates(3,4);
			assertSame(4, coordsTest.getY());
		}
		
		@Test
		void testSetX() {
			Coordinates coordsTest = new Coordinates(3,4);
			Coordinates coordsCreated = new Coordinates(2,4);
			coordsCreated.setX(3);
			
			assertEquals(coordsTest.getX(), coordsCreated.getX());
		}
		
		@Test
		void testSetY() {
			Coordinates coordsTest = new Coordinates(3,4);
			Coordinates coordsCreated = new Coordinates(3,1);
			coordsCreated.setY(4);
			
			assertEquals(coordsTest.getY(), coordsCreated.getY());
		}
		
		@Test
		void testSetCoordinates() {
			Coordinates coordsTest = new Coordinates(3,4);
			Coordinates coordsCreated = new Coordinates(1,1);
			coordsCreated.setCoordinates(3,4);
			
			assertEquals(coordsTest.getX(), coordsCreated.getX());
			assertEquals(coordsTest.getY(), coordsCreated.getY());
		}
	}
	
	@DisplayName ("Tests related to testing the class Pair")
	@Nested
	class PairTests {
		
		@Test
		void testLeftNotNull() {
			Pair<String,Integer> pairTest = new Pair<>("Cadena", 10);
			pairTest.setLeft("Nueva cadena");
			
			String cadenaTest = "Nueva cadena";
			assertEquals(cadenaTest, pairTest.getLeft());
		}
		
		@Test
		void testLeftNull() {
			Pair<String,Integer> pairTest = new Pair<>("Cadena", 10);
			pairTest.setLeft(null);
			
			String cadenaTest = null;
			assertEquals(cadenaTest, pairTest.getLeft());
		}
		
		@Test
		void testRightNotNull() {
			Pair<String,Integer> pairTest = new Pair<>("Cadena", 10);
			pairTest.setRight(20);
			
			Integer numeroTest = 20;
			assertEquals(numeroTest, pairTest.getRight());
		}
		
		@Test
		void testRightNull() {
			Pair<String,Integer> pairTest = new Pair<>("Cadena", 10);
			pairTest.setRight(null);
			
			Integer numeroTest = null;
			assertEquals(numeroTest, pairTest.getRight());
		}
	}
}