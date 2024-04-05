import java.util.Random;

public class MathUtils {
    public static void generateMatrix(float[][] matrix) {
        for (float[] floats : matrix) {
            generateVector(floats);
        }
    }

    public static void generateVector(float[] vector) {
        Random r = new Random();
        for (int i = 0; i < vector.length; i++) {
            vector[i] = 4 + r.nextFloat() * 5;
        }
    }
    public static float[] addVectors(float[] A, float[] B) {
        float[] result = new float[A.length];
        for (int i = 0; i < A.length; i++) {
            result[i] = KahanAlgorithm(new float[] { A[i], B[i] });
        }
        return result;
    }

    public static float[] multiplyMatrixOnVector(float[][] MA, float[] A) {
        int rows = MA[0].length;
        int cols = A.length;
        float[] result = new float[rows];
        for (int i = 0; i < rows; i++) {
            float sum = 0;
            for (int j = 0; j < cols; j++) {
                sum += MA[i][j] * A[j];
            }
            result[i] = sum;
        }
        return result;
    }


    public static float[][] multiplyMatrices(float[][] A, float[][] B) {
        float[][] result = new float[A.length][B[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < B[0].length; j++) {
                result[i][j] = 0;
                for (int k = 0; k < A[i].length; k++) {
                    result[i][j] = KahanAlgorithm(new float[] { result[i][j], A[i][k] * B[k][j] });
                }
            }
        }
        return result;
    }

    public static float[][] multiplyMatrixByValue(float[][] A, float a) {
        float[][] result = new float[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                result[i][j] = a * A[i][j];
            }
        }
        return result;
    }

    public static float findMinValue(float[] A) {
        float minValue = A[0];
        for (int i = 1; i < A.length; i++) {
            minValue = Math.min(minValue, A[i]);
        }
        return minValue;
    }

    public static float[][] addMatrices(float[][] A, float[][] B) {
        float[][] result = new float[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                result[i][j] = KahanAlgorithm(new float[] { A[i][j], B[i][j] });
            }
        }
        return result;
    }

    public static float KahanAlgorithm(float[] obj) {
        float sum = 0.0f;
        float err = 0.0f;
        for (float item : obj) {
            float y = item - err;
            float t = sum + y;
            err = (t - sum) - y;
            sum = t;
        }
        return sum;
    }

    public static float[][] splitMatrix(float[][] matrix, int start, int n) {
        float[][] res = new float[matrix.length][n - start];
        for (int i = 0; i < matrix.length; i++) {
            if (n - start >= 0)
                System.arraycopy(matrix[i], start, res[i], 0, n - start);
        }
        return res;
    }
}
