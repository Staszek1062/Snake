package AIresorces;

import java.util.ArrayList;
import java.util.List;

/*NeuralNetwork methods
    NeuralNetwork(int,int,int)
    predict(double[]) 
    fit(double[][],double[][],int)
    train(double [],double [])
*/
public class NeuralNetwork {

    Matrix weights_ih, weights_ho , bias_h, bias_o;	
	double l_rate=0.01;
	boolean MultiThreading = false;

	public NeuralNetwork(int i, int h, int o) {

		weights_ih = new Matrix(h, i);
		weights_ho = new Matrix(o, h);

		bias_h = new Matrix(h, 1);
		bias_o = new Matrix(o, 1);

	}

	public NeuralNetwork(int i, int h, int o, boolean MultiThreading) {
		weights_ih = new Matrix(h, i);
		weights_ho = new Matrix(o, h);

		bias_h = new Matrix(h, 1);
		bias_o = new Matrix(o, 1);

		this.MultiThreading = MultiThreading;

	}
	public NeuralNetwork(int i, int h, int o, double l_rate) {
        weights_ih = new Matrix(h, i);
        weights_ho = new Matrix(o, h);

        bias_h = new Matrix(h, 1);
        bias_o = new Matrix(o, 1);

        this.l_rate = l_rate;

    }

	public NeuralNetwork(int i, int h, int o, double l_rate, boolean MultiThreading) {
		weights_ih = new Matrix(h, i);
		weights_ho = new Matrix(o, h);

		bias_h = new Matrix(h, 1);
		bias_o = new Matrix(o, 1);

		this.l_rate = l_rate;
		this.MultiThreading = MultiThreading;

	}
	
	public List<Double> predict(double[] X)
	{
		Matrix input = Matrix.fromArray(X);
		Matrix hidden = Matrix.multiply(weights_ih, input, MultiThreading);
		hidden.add(bias_h);
		hidden.sigmoid();

		Matrix output = Matrix.multiply(weights_ho, hidden, MultiThreading);
		output.add(bias_o);
		output.sigmoid();

		return output.toArray();
	}
	

	
	public void fit(double[][]X,double[][]Y,int epochs)
	{
		for(int i=0;i<epochs;i++)
		{	
			int sampleN =  (int)(Math.random() * X.length );
			this.train(X[sampleN], Y[sampleN],false);
		}
	}

	public void fit(double[][] X, double[][] Y, int epochs, int verbose) {
		switch (verbose) {

			case 0: {
				System.out.println("Staring training with " + epochs + " epochs");
				long start = System.currentTimeMillis();
				for (int i = 0; i < epochs; i++) {
					int sampleN = (int) (Math.random() * X.length);
					this.train(X[sampleN], Y[sampleN], i + 1 == epochs);
				}
				long end = System.currentTimeMillis();
				long elapsedTime = end - start;
				System.out.println("Training took : " + (elapsedTime / 1000) + "s");

				break;
			}

			case 1: {
				System.out.println("Staring training with " + epochs + " epochs");
				long start = System.currentTimeMillis();
				for (int i = 0; i < epochs; i++) {
					System.out.println("Epoch: " + (i + 1));
					int sampleN = (int) (Math.random() * X.length);
					this.train(X[sampleN], Y[sampleN], true);
				}
				long end = System.currentTimeMillis();
				long elapsedTime = end - start;
				System.out.println("Training took : " + (elapsedTime / 1000) + "s");

				break;
			}
		}
	}

	public void train(double [] X,double [] Y, Boolean showLoss)
	{
		Matrix input = Matrix.fromArray(X);
		Matrix hidden = Matrix.multiply(weights_ih, input, MultiThreading);
		hidden.add(bias_h);
		hidden.sigmoid();


		Matrix output = Matrix.multiply(weights_ho,hidden, MultiThreading);
		output.add(bias_o);
		output.sigmoid();

		Matrix target = Matrix.fromArray(Y);
		
		Matrix error = Matrix.subtract(target, output);
		Matrix gradient = output.unsigmoid();
		gradient.multiply(error);
		gradient.multiply(l_rate);

		if(showLoss)
			printLoss(error);

		Matrix hidden_T = Matrix.transpose(hidden);
		Matrix who_delta =  Matrix.multiply(gradient, hidden_T, MultiThreading);

		weights_ho.add(who_delta);
		bias_o.add(gradient);

		Matrix who_T = Matrix.transpose(weights_ho);
		Matrix hidden_errors = Matrix.multiply(who_T, error, MultiThreading);
		
		Matrix h_gradient = hidden.unsigmoid();
		h_gradient.multiply(hidden_errors);
		h_gradient.multiply(l_rate);

		Matrix i_T = Matrix.transpose(input);
		Matrix wih_delta = Matrix.multiply(h_gradient, i_T, MultiThreading);
		
		weights_ih.add(wih_delta);
		bias_h.add(h_gradient);
        
		
	}
	public void printLoss(Matrix error) {
        double avg = 0.0;

        for (int i = 0; i < error.rows; i++) {
            for (int j = 0; j < error.columns; j++) {
                avg += error.data[i][j];
            }
        }

        System.out.print("Average Error: " + avg + "\n");
    }


}
 