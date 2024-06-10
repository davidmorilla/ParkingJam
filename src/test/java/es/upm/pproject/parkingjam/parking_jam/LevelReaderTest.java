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
	public void readOKLevelTest() {
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
	public void readLevelWithNoNameTest() {
		lr = new LevelReader();
		Assertions.assertNull(lr.readMap(-8,false));
	}
	@Test
	public void readLevelWithNoDimensionsTest() {
		lr = new LevelReader();
		Assertions.assertNull(lr.readMap(-9,false));
	}
	@Test
	public void readLevelWithWrongDimensionsFormatTest() {
		lr = new LevelReader();
		Assertions.assertNull(lr.readMap(-10,false));
	}
	@Test
	public void readLevelWithMissingLinesTest() {
		lr = new LevelReader();
		Assertions.assertNull(lr.readMap(-11,false));
	}
	@Test
	public void readTooSmallLevelTest() {
		lr = new LevelReader();
		Assertions.assertNull(lr.readMap(-12,false));
	}

}
