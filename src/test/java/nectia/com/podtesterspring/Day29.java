package nectia.com.podtesterspring;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * UNIQUE PATHS
 *
 * A robot is located at the top-left corner of a m x n grid (marked 'Start' in the diagram below).
 *
 * The robot can only move either down or right at any point in time.
 * The robot is trying to reach the bottom-right corner of the grid (marked 'Finish' in the diagram below).
 *
 * How many possible unique paths are there?
 *
 * --------------------------------------------------------
 * Start  |       |       |       |       |       |       |
 * --------------------------------------------------------
 *        |       |       |       |       |       |       |
 * --------------------------------------------------------
 *        |       |       |       |       |       |  End  |
 * --------------------------------------------------------
 *
 * Above is a 7 x 3 grid. How many possible unique paths are there?
 *
 *
 * Example 1:
 * - Input: m = 3, n = 2
 * - Output: 3
 * Explanation:
 * From the top-left corner, there are a total of 3 ways to reach the bottom-right corner:
 * 1. Right -> Right -> Down
 * 2. Right -> Down -> Right
 * 3. Down -> Right -> Right
 *
 * Example 2:
 * - Input: m = 7, n = 3
 * - Output: 28
 *
 * Constraints:
 * - 1 <= m, n <= 100
 * - It's guaranteed that the answer will be less than or equal to 2 * 10 ^ 9.
 */
public class Day29 {

    static Stream<Arguments> testParameters () {
        return Stream.of(
                Arguments.of(3, 2, 3),
                Arguments.of(2, 3, 3),
                Arguments.of(7, 3, 28)
        ); } // end static Stream<Arguments> testParameters ()

    @ParameterizedTest(name = "{index} => m = {0}, n = {1}, expected = {2}")
    @MethodSource("testParameters")
    public void should_get_unique_paths(int m, int n, int expectedOutput) {
        assertThat(uniquePaths(m, n), is(expectedOutput));
    } // end void should_get_unique_paths(int m, int n, int expectedOutput)

    /**
     * Runtime: 0 ms
     * Memory Usage: 36.4 MB
     */
    private int uniquePaths(int m, int n) {
        if ( m == 1  ||  n == 1 ) {
            return 1;
        }

        int[][] dp = new int[m][n];

        for(int i=0; i<m-1; ++i) {
            dp[i][n-1] = 1;
        } // end last column iteration
        for(int j=0; j<n-1; ++j) {
            dp[m-1][j] = 1;
        } // end last row iteration

        for(int i=m-2; i>=0; --i) {
            for(int j=n-2; j>=0; --j) {
                dp[i][j] = dp[i+1][j] + dp[i][j+1];
            } // end cols iteration
        } // end rows iteration

        return dp[0][0];
    } // end int uniquePaths(int m, int n)
} // end class Day29