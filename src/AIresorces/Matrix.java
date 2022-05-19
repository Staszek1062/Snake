package AIresorces;

import java.util.ArrayList;
import java.util.List;
 
/*Matrix class methods
    Matrix(int,int)
    print()
    add(int)
    add(Matrix)
    fromArray(double[])
    List<Double> toArray()
    subtract(Matrix,Matrix)
    transpose(Matrix)
    multiply(Matrix,Matrix,boolean)
    multiply(Matrix)
    multiply(double)
    sigmoid()
    unsigmoid()
*/
/*ParallelThreadsCreator class methods
	multiply(Matrix, Matrix, Matrix )  
	waitForThreads(List<Thread> threads)
*/
/*WorkerThread class methods
	WorkerThread(int, int, int[][], int[][], int[][])
*/
/*RowMultiplyWorker class methods
	RowMultiplyWorker(Matrix result, Matrix matrix1, Matrix matrix2, int row)
*/
public class Matrix { 
    
    public double [][]data;
	public int rows,columns;
	
	public Matrix(int rows,int columns) {
		data= new double[rows][columns];
		this.rows=rows;
		this.columns=columns;
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<columns;j++)
			{
				data[i][j]=Math.random()*2-1;
			}
		}
	}
	
	public void print()
	{
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<columns;j++)
			{
				System.out.print(this.data[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	public void add(int scaler)
	{
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<columns;j++)
			{
				this.data[i][j]+=scaler;
			}
			
		}
	}
	
	public void add(Matrix m)
	{
		if(columns!=m.columns || rows!=m.rows) {
			System.out.println("Shape Mismatch");
			return;
		}
		
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<columns;j++)
			{
				this.data[i][j]+=m.data[i][j];
			}
		}
	}
	
	public static Matrix fromArray(double[]x)
	{
		Matrix temp = new Matrix(x.length,1);
		for(int i =0;i<x.length;i++)
			temp.data[i][0]=x[i];
		return temp;
		
	}
	
	public List<Double> toArray() {
		List<Double> temp= new ArrayList<Double>()  ;
		
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<columns;j++)
			{
				temp.add(data[i][j]);
			}
		}
		return temp;
	}

	public static Matrix subtract(Matrix a, Matrix b) {
		Matrix temp=new Matrix(a.rows,a.columns);
		for(int i=0;i<a.rows;i++)
		{
			for(int j=0;j<a.columns;j++)
			{
				temp.data[i][j]=a.data[i][j]-b.data[i][j];
			}
		}
		return temp;
	}

	public static Matrix transpose(Matrix a) {
		Matrix temp=new Matrix(a.columns,a.rows);
		for(int i=0;i<a.rows;i++)
		{
			for(int j=0;j<a.columns;j++)
			{
				temp.data[j][i]=a.data[i][j];
			}
		}
		return temp;
	}

	public static Matrix multiply(Matrix a, Matrix b,boolean MultiThreading) {
		Matrix temp = new Matrix(a.rows, b.columns);
		if (!MultiThreading) 
		{
			for(int i=0;i<temp.rows;i++)
			{
				for(int j=0;j<temp.columns;j++)
				{
					double sum=0;
					for(int k=0;k<a.columns;k++)
					{
						sum+=a.data[i][k]*b.data[k][j];
					}
					temp.data[i][j]=sum;
				}
			}
		}
		else {

			ParallelThreadsCreator.multiply(a, b, temp);
		}
		return temp;
	}
	
	public void multiply(Matrix a) {
		for(int i=0;i<a.rows;i++)
		{
			for(int j=0;j<a.columns;j++)
			{
				this.data[i][j]*=a.data[i][j];
			}
		}
		
	}
	
	public void multiply(double a) {
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<columns;j++)
			{
				this.data[i][j]*=a;
			}
		}
		
	}
	
	public void sigmoid() {
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<columns;j++)
				this.data[i][j] = 1/(1+Math.exp(-this.data[i][j])); 
		}
		
	}
	
	public Matrix unsigmoid() {
		Matrix temp=new Matrix(rows,columns);
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<columns;j++)
				temp.data[i][j] = this.data[i][j] * (1-this.data[i][j]);
		}
		return temp;
		
	}
	public Matrix clone() {
		Matrix clone = new Matrix(rows, columns);
		for(int i = 0; i < rows; i++) {
		   for(int j = 0; j < columns; j++) {
			  clone.data[i][j] = data[i][j]; 
		   }
		}
		return clone;
	 }
}

class ParallelThreadsCreator {


    public static void multiply(Matrix matrix1, Matrix matrix2, Matrix result) {
        List<Thread> threads = new ArrayList<>();
        int rows1 = matrix1.rows;
        for (int i = 0; i < rows1; i++) {
            RowMultiplyWorker task = new RowMultiplyWorker(result, matrix1, matrix2, i);
            Thread thread = new Thread(task);
            thread.start();
            threads.add(thread);
            if (threads.size() % 10 == 0) {
                waitForThreads(threads);
            }
        }
    }

    private static void waitForThreads(List<Thread> threads) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        threads.clear();
    }
}

class WorkerThread extends Thread {
    private int row;
    private int col;
    private int[][] A;
    private int[][] B;
    private int[][] C;

    public WorkerThread(int row, int col, int[][] A, int[][] B, int[][] C) {
        this.row = row;
        this.col = col;
        this.A = A;
        this.B = B;
        this.C = C;
    }

    public void run() {
        C[row][col] = (A[row][0] * B[0][col]) + (A[row][1] * B[1][col]);
    }
}

class RowMultiplyWorker implements Runnable {

    private final Matrix result;
    private Matrix matrix1;
    private Matrix matrix2;
    private final int row;

    public RowMultiplyWorker(Matrix result, Matrix matrix1, Matrix matrix2, int row) {
        this.result = result;
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.row = row;
    }

    @Override
    public void run() {

        for (int i = 0; i < matrix2.data[0].length; i++) {
            result.data[row][i] = 0;
            for (int j = 0; j < matrix1.data[row].length; j++) {
                result.data[row][i] += matrix1.data[row][j] * matrix2.data[j][i];
            }
        }
    }
}