/*
Iryna Velychko, group IP-03, variant 5
А=В*МС+D*MT;
MА= min(D)*MC*ME+MZ*MT.
*/
import java.util.concurrent.ThreadLocalRandom;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Comparator;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;


public class Lab1Variant1 implements Runnable {
    private int dim;
    private float[] B;
    private float[] D;
    private float[][] MC;
    private float[][] MT;
    private float[][] MZ;
    private float[][] ME;

    private long[][] func1DimTime;
    private long[][] func2DimTime;
    private int iteration;
    FileWriter resultFileWriter;

    public synchronized void syncFileWriter(String text) {
        try {
            resultFileWriter.write(text);
        } catch (IOException e) {
            System.out.println("FileWriter IO exception.");
        }
    }

    public void generateInputData() {
        dim = ThreadLocalRandom.current().nextInt(100, 1001);

        // matrices and vectors
        B = new float[dim];
        MathUtils.generateVector(B);
        D = new float[dim];
        MathUtils.generateVector(D);
        MC = new float[dim][dim];
        MathUtils.generateMatrix(MC);
        MT = new float[dim][dim];
        MathUtils.generateMatrix(MT);
        MZ = new float[dim][dim];
        MathUtils.generateMatrix(MZ);
        ME = new float[dim][dim];
        MathUtils.generateMatrix(ME);

        try {
            FileWriter fileWriter = writeInputData();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("generateInputData() IO exception");
        }
    }

    private FileWriter writeInputData() throws IOException {
        FileWriter fileWriter = new FileWriter("input.txt");
        fileWriter.write(Arrays.toString(B) + "\n");
        fileWriter.write(Arrays.toString(D) + "\n");
        for (int i = 0; i < MC.length; i++) {
            fileWriter.write(Arrays.toString(MC[i]));
            if (i == MC.length - 1)
                fileWriter.write("\n");
            else
                fileWriter.write("\n");
        }
        for (int i = 0; i < MT.length; i++) {
            fileWriter.write(Arrays.toString(MT[i]));
            if (i == MT.length - 1)
                fileWriter.write("\n");
            else
                fileWriter.write("\n");
        }
        for (int i = 0; i < MZ.length; i++) {
            fileWriter.write(Arrays.toString(MZ[i]));
            if (i == MZ.length - 1)
                fileWriter.write("\n");
            else
                fileWriter.write("\n");
        }
        for (int i = 0; i < ME.length; i++) {
            fileWriter.write(Arrays.toString(ME[i]));
            if (i == ME.length - 1)
                fileWriter.write("\n");
            else
                fileWriter.write("\n");
        }
        return fileWriter;
    }

    public void parseInputData() {
        try {
            File file = new File("input.txt");
            Scanner scanner = new Scanner(file);
            int i = 0;

            while (scanner.hasNextLine()) {
                i++;
                String data = scanner.nextLine();
                data = data.substring(1, data.length() - 1);
                String[] nums = data.split(", ");
                if (i == 1) {
                    dim = nums.length;
                    B = new float[dim];
                    D = new float[dim];
                    MC = new float[dim][dim];
                    MT = new float[dim][dim];
                    MZ = new float[dim][dim];
                    ME = new float[dim][dim];
                    for (int j = 0; j < dim; j++) {
                        B[j] = Float.parseFloat(nums[j]);
                    }
                } else if (i == 2) {
                    for (int j = 0; j < dim; j++) {
                        D[j] = Float.parseFloat(nums[j]);
                    }
                } else if (i > 2 && i <= 2 + dim) {
                    for (int j = 0; j < dim; j++) {
                        MC[i - 3][j] = Float.parseFloat(nums[j]);
                    }
                } else if (i > 2 + dim && i <= 2 + 2 * dim) {
                    for (int j = 0; j < dim; j++) {
                        MT[i - 3 - dim][j] = Float.parseFloat(nums[j]);
                    }
                } else if (i > 2 + 2 * dim && i <= 2 + 3 * dim) {
                    for (int j = 0; j < dim; j++) {
                        MZ[i - 3 - 2 * dim][j] = Float.parseFloat(nums[j]);
                    }
                } else if (i > 2 + 3 * dim) {
                    for (int j = 0; j < dim; j++) {
                        ME[i - 3 - 3 * dim][j] = Float.parseFloat(nums[j]);
                    }
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("parseInputData() IO exception");
        }
    }

    public void printMatrix(float[][] A, String info) {
        StringBuilder matrix = new StringBuilder("\n" + info + "\n");
        for (float[] floats : A) {
            matrix.append(Arrays.toString(floats)).append("\n");
        }
        System.out.println(matrix);
        syncFileWriter(matrix.toString());
    }

    public void printVector(float[] A, String info) {
        StringBuilder vector = new StringBuilder("\n" + info + "\n");
        vector.append(Arrays.toString(A)).append("\n");
        System.out.println(vector);
        syncFileWriter(vector.toString());
    }

    public void printMatrix(long[][] A, String info) {
        StringBuilder matrix = new StringBuilder("\n" + info + "\n");
        for (long[] longs : A) {
            matrix.append(Arrays.toString(longs)).append("\n");
        }
        System.out.println(matrix);
        syncFileWriter(matrix.toString());
    }

    public float[] Function1(float[] B, float[] D, float[][] MC, float[][] MT) {
        float[] res1 = MathUtils.multiplyMatrixOnVector(MC, B);
        float[] res2 = MathUtils.multiplyMatrixOnVector(MT, D);
        return MathUtils.addVectors(res1, res2);
    }

    public float[][] Function2(float[] D, float[][] MC, float[][] MT, float[][] ME, float[][] MZ) {
        float minD = MathUtils.findMinValue(D);
        float[][] res1 = MathUtils.multiplyMatrixByValue(MC, minD);
        float[][] res2 = MathUtils.multiplyMatrices(res1, ME);
        float[][] res3 = MathUtils.multiplyMatrices(MZ, MT);
        return MathUtils.addMatrices(res2, res3);
    }

    public void run() {
        long startTime = System.nanoTime();
        float[] A = Function1(B, D, MC, MT);
        long calcTime = System.nanoTime() - startTime;
        func1DimTime[iteration][0] = dim;
        func1DimTime[iteration][1] = calcTime;
        printVector(A, "Vector A: ");
    }

    public static void main(String[] args) {
        Lab1Variant1 lab1 = new Lab1Variant1();

        try {
            lab1.resultFileWriter = new FileWriter("variant1_result.txt");
        } catch (IOException e) {
            System.out.println("File Writer IO exception");
        }

        lab1.func1DimTime = new long[50][2];
        lab1.func2DimTime = new long[50][2];

        for (int i = 0; i < 50; i++) {
            lab1.generateInputData();
            lab1.parseInputData();
            lab1.iteration = i;

            Thread t2 = new Thread(lab1);
            t2.start();

            long startTime = System.nanoTime();
            float[][] MA = lab1.Function2(lab1.D, lab1.MC, lab1.MT, lab1.ME, lab1.MZ);
            long calcTime = System.nanoTime() - startTime;
            lab1.printMatrix(MA, "Matrix MA: ");
            lab1.func2DimTime[i][0] = lab1.dim;
            lab1.func2DimTime[i][1] = calcTime;

            try {
                t2.join();
            } catch (InterruptedException e) {
                System.out.println("Concurrency exception.");
            }
        }

        Arrays.sort(lab1.func1DimTime, Comparator.comparingDouble(a -> a[0]));
        lab1.printMatrix(lab1.func1DimTime, "Function 1 arrays [dimension,runtime]:");

        Arrays.sort(lab1.func2DimTime, Comparator.comparingDouble(a -> a[0]));
        lab1.printMatrix(lab1.func2DimTime, "Function 2 arrays [dimension,runtime]:");


        lab1.syncFileWriter("Function 1 points:\n");
        for (int i = 0; i < lab1.func1DimTime.length; i++) {
            lab1.syncFileWriter("{x: " + lab1.func1DimTime[i][0] + ", y: " + lab1.func1DimTime[i][1] + "}\n");
        }
        lab1.syncFileWriter("Function 2 points:\n");
        for (int i = 0; i < lab1.func1DimTime.length; i++) {
            lab1.syncFileWriter("{x: " + lab1.func2DimTime[i][0] + ", y: " + lab1.func2DimTime[i][1] + "}\n");
        }

        try {
            lab1.resultFileWriter.close();
        } catch (IOException e) {
            System.out.println("File Writer IO exception");
        }

    }

}