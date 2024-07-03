public class MaxRectangle {
    public int maximalRectangle(char[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }

        int rows = matrix.length;
        int cols = matrix[0].length;
        int maxArea = 0;

        int[] left = new int[cols];
        int[] right = new int[cols];
        int[] height = new int[cols];

        //right boundary
        for (int i = 0; i < cols; i++) {
            right[i] = cols;
        }

        for (int i = 0; i < rows; i++) {
            int curLeft = 0, curRight = cols;

            //update height
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == '1') {
                    height[j]++;
                } else {
                    height[j] = 0;
                }
            }

            //update left boundary
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == '1') {
                    left[j] = Math.max(left[j], curLeft);
                } else {
                    left[j] = 0;
                    curLeft = j + 1;
                }
            }

            for (int j = cols - 1; j >= 0; j--) {
                if (matrix[i][j] == '1') {
                    right[j] = Math.min(right[j], curRight);
                } else {
                    right[j] = cols;
                    curRight = j;
                }
            }

            for (int j = 0; j < cols; j++) {
                maxArea = Math.max(maxArea, (right[j] - left[j]) * height[j]);
            }
        }

        return maxArea;
    }

    public static void main(String[] args) {
        MaxRectangle solution = new MaxRectangle();
        char[][] matrix = {
                {'1', '0', '1', '0', '0'},
                {'1', '0', '1', '1', '1'},
                {'1', '1', '1', '1', '1'},
                {'1', '0', '0', '1', '0'}
        };
        System.out.println("The maximal rectangle area is: " + solution.maximalRectangle(matrix)); // Output: 6

        char[][] matrix1 = {
                {'1'},
        };
        System.out.println("The maximal rectangle area is: " + solution.maximalRectangle(matrix1)); // Output: 1

        char[][] matrix2 = {
                {'0'},
        };
        System.out.println("The maximal rectangle area is: " + solution.maximalRectangle(matrix2)); // Output: 0
    }
}
