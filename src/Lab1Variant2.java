/*
Iryna Velychko, group IP-03, variant 5
А=В*МС+D*MT;
MА= min(D)*MC*ME+MZ*MT.
*/
import java.util.Arrays;
import java.util.Scanner;
import java.util.Comparator;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

public class Lab1Variant2 implements Runnable{
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

    // shared variables
    private float[] shared1;
    private float[] shared2;
    private float[][] shared3;
    private float[][] shared4;

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
        Thread t = Thread.currentThread();
        StringBuilder matrix = new StringBuilder("\nThread: " + t.getName() + ", " + info + "\n");
        for (float[] floats : A) {
            matrix.append(Arrays.toString(floats)).append("\n");
        }
        System.out.println(matrix);
        syncFileWriter(matrix.toString());
    }

    public void printVector(float[] A, String info) {
        Thread t = Thread.currentThread();
        StringBuilder vector = new StringBuilder("\nThread: " + t.getName() + ", " + info + "\n");
        vector.append(Arrays.toString(A)).append("\n");
        System.out.println(vector);
        syncFileWriter(vector.toString());
    }

    public void printMatrix(long[][] A, String info) {
        Thread t = Thread.currentThread();
        StringBuilder matrix = new StringBuilder("\nThread: " + t.getName() + ", " + info + "\n");
        for (long[] longs : A) {
            matrix.append(Arrays.toString(longs)).append("\n");
        }
        System.out.println(matrix);
        syncFileWriter(matrix.toString());
    }

    public float[] Function1() {
        int n = (int) (double) (dim / 2);
        shared1 = new float[dim];
        shared2 = new float[dim];

        Thread t1 = new Thread(() -> {
            float[][] MC_part1 = MathUtils.splitMatrix(MC, 0, n);
            float[] res1 = MathUtils.multiplyMatrixOnVector(MC_part1, B);
            synchronized(shared1) {
                if (n >= 0)
                    System.arraycopy(res1, 0, shared1, 0, n);
            }
        });

        Thread t2 = new Thread(() -> {
            float[][] MC_part2 = MathUtils.splitMatrix(MC, n, MC.length);
            float[] res1 = MathUtils.multiplyMatrixOnVector(MC_part2, B);
            synchronized(shared1) {
                if (n >= 0)
                    System.arraycopy(res1, 0, shared1, n, dim);
            }
        });

        Thread t3 = new Thread(() -> shared2 = MathUtils.multiplyMatrixOnVector(MT, D));

        t1.start();
        t2.start();
        t3.start();
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            System.out.println("Concurrency exception");
        }
        return MathUtils.addVectors(shared1, shared2);
    }

    public float[][] Function2() {
        float minD = MathUtils.findMinValue(D);
        float[][] res1 = MathUtils.multiplyMatrixByValue(MC, minD);

        int n = (int) (double) (dim / 2);
        shared3 = new float[dim][dim];
        shared4 = new float[dim][dim];

        Thread t1 = new Thread(() -> {
            float[][] ME_part1 = MathUtils.splitMatrix(ME, 0, n);
            float[][] res2 = MathUtils.multiplyMatrices(res1, ME_part1);
            synchronized(shared3) {
                for (int i = 0; i < res2.length; i++) {
                    if (n >= 0)
                        System.arraycopy(res2[i], 0, shared3[i], 0, n);
                }
            }
        });

        Thread t2 = new Thread(() -> {
            float[][] ME_part2 = MathUtils.splitMatrix(ME, n, ME.length);
            float[][] res2 = MathUtils.multiplyMatrices(res1, ME_part2);
            synchronized(shared3) {
                for (int i = 0; i < res2.length; i++) {
                    if (n >= 0) System.arraycopy(res2[i], 0, shared3[i], n, n);
                }
            }
        });

        Thread t3 = new Thread(() -> shared4 = MathUtils.multiplyMatrices(MZ, MT));

        t1.start();
        t2.start();
        t3.start();
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            System.out.println("Concurrency exception");
        }
        return MathUtils.addMatrices(shared3,shared4);
    }

    public void run() {
        long startTime = System.nanoTime();
        float[] A = Function1();
        long calcTime = System.nanoTime() - startTime;
        func1DimTime[iteration][0] = dim;
        func1DimTime[iteration][1] = calcTime;
        printVector(A, "Vector A: ");
    }

    public static void main(String[] args) {
        Lab1Variant2 lab2 = new Lab1Variant2();

        try {
            lab2.resultFileWriter = new FileWriter("variant2_result.txt");
        } catch (IOException e) {
            System.out.println("File Writer IO exception");
        }

        lab2.func1DimTime = new long[50][2];
        lab2.func2DimTime = new long[50][2];

        for (int i = 0; i < 50; i++) {
            lab2.generateInputData();
            lab2.parseInputData();
            lab2.iteration = i;

            Thread t2 = new Thread(lab2);
            t2.start();

            long startTime = System.nanoTime();
            float[][] MA = lab2.Function2();
            long calcTime = System.nanoTime() - startTime;
            lab2.printMatrix(MA, "Matrix MA: ");
            lab2.func2DimTime[i][0] = lab2.dim;
            lab2.func2DimTime[i][1] = calcTime;

            try {
                t2.join();
            } catch (InterruptedException e) {
                System.out.println("Concurrency exception.");
            }
        }

        Arrays.sort(lab2.func1DimTime, Comparator.comparingDouble(a -> a[0]));
        lab2.printMatrix(lab2.func1DimTime, "Function 1 arrays [dimension,runtime]:");

        Arrays.sort(lab2.func2DimTime, Comparator.comparingDouble(a -> a[0]));
        lab2.printMatrix(lab2.func2DimTime, "Function 2 arrays [dimension,runtime]:");

        lab2.syncFileWriter("Function 1 points:\n");
        for (int i = 0; i < lab2.func1DimTime.length; i++) {
            lab2.syncFileWriter("{x: " + lab2.func1DimTime[i][0] + ", y: " + lab2.func1DimTime[i][1] + "}\n");
        }
        lab2.syncFileWriter("Function 2 points:\n");
        for (int i = 0; i < lab2.func1DimTime.length; i++) {
            lab2.syncFileWriter("{x: " + lab2.func2DimTime[i][0] + ", y: " + lab2.func2DimTime[i][1] + "}\n");
        }

        try {
            lab2.resultFileWriter.close();
        } catch (IOException e) {
            System.out.println("File Writer IO exception");
        }

    }
}
