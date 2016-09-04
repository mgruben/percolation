
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/*
 * Copyright (C) 2016 Michael <GrubenM@GMail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * @author Michael <GrubenM@GMail.com>
 * @written 2016-09-04
 * 
 * Class for generating statistics about multiple Percolation trials.
 */
public class PercolationStats {
    private final int n;
    private final int trials;
    private final double[] thresholds;
    private int openTotal = 0;
    
    /**
     * Completes `trials` trials of `n`-by-`n` Percolation grids,
     * recording the threshold fraction at which the grid first percolates.
     * 
     * @param n
     * @param trials
     * @throws IllegalArgumentException 
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0) throw new IllegalArgumentException("n must be positive");
        if (trials <= 0)
            throw new IllegalArgumentException("trials must be positive");
        this.n = n;
        this.trials = trials;
        this.thresholds = new double[this.trials];
        for (int k = 0; k < this.trials; k++) {
            Percolation perc = new Percolation(this.n);
            this.openTotal = 0;
            while (!perc.percolates()) {
                int i = StdRandom.uniform(1, n + 1);
                int j = StdRandom.uniform(1, n + 1);
                if (!perc.isOpen(i, j)) {
                    perc.open(i, j);
                    this.openTotal++;
                }
            }
            thresholds[k] = this.openTotal / (double) (n*n);
        }
    }
    
    /**
     * Calculates and returns the mean of the threshold fractions at which
     * the grids first percolated in the given trial set.
     * 
     * @return 
     */
    public double mean() {
        return StdStats.mean(this.thresholds);
    }
    
    /**
     * Calculates and returns the standard deviation of the threshold fractions
     * at which the grids first percolated in the given trial set.
     * 
     * @return 
     */
    public double stddev() {
        return StdStats.stddevp(this.thresholds);
    }
    
    /**
     * Calculates and returns the lower bound of the 95% confidence interval
     * for the mean value calculated above.
     * 
     * @return 
     */
    public double confidenceLo() {
        return mean() - (1.96 * stddev()) / Math.sqrt(this.trials);
    }
    
    /**
     * Calculates and returns the upper bound of the 95% confidence interval
     * for the mean values calculated above.
     * 
     * @return 
     */
    public double confidenceHi() {
        return mean() + (1.96 * stddev()) / Math.sqrt(this.trials);
    }
    
    /**
     * Conducts a number of trials on a specified grid size of Percolation
     * objects.  Then, prints statistics regarding the threshold fraction
     * at which percolation first occurred: first mean, then standard deviation,
     * then the low and high 95% confidence intervals.
     * 
     * @param args n, trials
     */
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);
        System.out.println("mean = " + ps.mean());
        System.out.println("stddev = " + ps.stddev());
        System.out.println("95% confidence interval = "
            + ps.confidenceLo() + ", " + ps.confidenceHi());
    }
}
