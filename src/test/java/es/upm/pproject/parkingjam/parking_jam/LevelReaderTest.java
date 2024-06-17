package es.upm.pproject.parkingjam.parking_jam;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
	void getLevelNumberTest() {
		lr = new LevelReader();
		lr.readMap(-1,false);
		Assertions.assertEquals(1, lr.getLevelNumber());
	}
	
	@DisplayName("In this parameterized test, all levels are incorrect (in different ways):\n"
			+ "- Level without a levelName\n"
			+ "- Level without dimensions\n"
			+ "- Level with wrong dimensions format\n"
			+ "- Level with missing lines\n"
			+ "- Level too small\n"
			+ "- 2 levels with no concordant dimensions\n"
			+ "- A non existent level\n"
			+ "- 3 levels with wrong level number format")
	@ParameterizedTest
    @MethodSource("provideInvalidLevelNumbers")
    void testReadInvalidLevels(int levelNumber) {
        lr = new LevelReader();
        assertArrayEquals(new char[0][0], lr.readMap(levelNumber, false));
    }

    static Stream<Arguments> provideInvalidLevelNumbers() {
        return Stream.of(
                Arguments.of(-8),
                Arguments.of(-9),
                Arguments.of(-10),
                Arguments.of(-11),
                Arguments.of(-12),
                Arguments.of(-14),
                Arguments.of(-15),
                Arguments.of(-150),
                Arguments.of(-16),
                Arguments.of(-17),
                Arguments.of(-18)
        );
    }
}
