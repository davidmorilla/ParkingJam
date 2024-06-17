package es.upm.pproject.parkingjam.parking_jam;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import es.upm.pproject.parkingjam.parking_jam.model.LevelReader;

@DisplayName ("Tests related to testing the class LevelReader")
@Nested
class LevelReaderTest {
	private LevelReader lr;

	@Test
	void readOKLevelTest() {
		lr = new LevelReader();
		char[][] board = lr.readMap(-1,false);
		char[][] expectedBoard={{'+','+','+','+','+','+','+','+'},
				{'+','a','a','b','b','b','c','+'},
				{'+',' ',' ',' ','*',' ','c','+'},
				{'+','d',' ',' ','*',' ',' ','+'},
				{'+','d',' ','f','f','f',' ','+'},
				{'+','d','e',' ',' ',' ',' ','+'},
				{'+',' ','e',' ','g','g','g','+'},
				{'+','+','+','+','@','+','+','+'}};
		assertArrayEquals(board, expectedBoard);
	}
	
	@Test
	void readLevelWithNoNameTest() {
		lr = new LevelReader();
		assertArrayEquals(new char[0][0], lr.readMap(-8,false));
	}
	
	@Test
	void getLevelNumberTest() {
		lr = new LevelReader();
		lr.readMap(-1,false);
		Assertions.assertEquals(1, lr.getLevelNumber());
	}
	
	@Test
	void readLevelWithNoDimensionsTest() {
		lr = new LevelReader();
		assertArrayEquals(new char[0][0], lr.readMap(-9,false));
	}
	
	@Test
	void readLevelWithWrongDimensionsFormatTest() {
		lr = new LevelReader();
		assertArrayEquals(new char[0][0], lr.readMap(-10,false));
	}
	
	@Test
	void readLevelWithMissingLinesTest() {
		lr = new LevelReader();
		assertArrayEquals(new char[0][0], lr.readMap(-11,false));
	}
	
	@Test
	void readTooSmallLevelTest() {
		lr = new LevelReader();
		assertArrayEquals(new char[0][0], lr.readMap(-12,false));
	}
	
	@Test
	void readLevelWithNonConcordantGreaterDimensionsTest() {
		lr = new LevelReader();
		assertArrayEquals(new char[0][0], lr.readMap(-14,false));
	}
	
	@Test
	void readLevelWithNonConcordantSmallerDimensionsTest() {
		lr = new LevelReader();
		assertArrayEquals(new char[0][0], lr.readMap(-15,false));
	}
	
	@Test
	void readNonExistentLevel() {
		lr = new LevelReader();
		assertArrayEquals(new char[0][0], lr.readMap(-150,false));
	}
	
	@Test
	void readLevelWithWrongLevelNumberFormat1() {
		lr = new LevelReader();
		assertArrayEquals(new char[0][0], lr.readMap(-16,false));
	}
	
	@Test
	void readLevelWithWrongLevelNumberFormat2() {
		lr = new LevelReader();
		assertArrayEquals(new char[0][0], lr.readMap(-17,false));
	}
	
	@Test
	void readLevelWithWrongLevelNumberFormat3() {
		lr = new LevelReader();
		assertArrayEquals(new char[0][0], lr.readMap(-18,false));
	}
}
