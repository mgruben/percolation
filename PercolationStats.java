
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import static java.lang.Math.sqrt;
import java.util.ArrayList;

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
 *
 * @author Michael <GrubenM@GMail.com>
 */
public class PercolationStats {
    private final int n;
    private final int trials;
    private final double[] thresholds;
    
    public PercolationStats(int n, int trials) throws IllegalArgumentException {
        if (n <= 0) throw new IllegalArgumentException("n must be positive");
        if (trials <= 0)
            throw new IllegalArgumentException("trials must be positive");
        this.n = n;
        this.trials = trials;
        this.thresholds = new double[this.trials];
        for (int i = 0; i < this.trials; i++) {
            Percolation perc = new Percolation(this.n);
            while (!perc.percolates()) {
                int this_i = StdRandom.uniform(1, n + 1);
                int this_j = StdRandom.uniform(1, n + 1);
                perc.open(this_i, this_j);
            }
            thresholds[i] = perc.getOpenTotal() / (double)(n*n);
        }
    }
    
    public double mean() {
        return StdStats.mean(this.thresholds);
    }
    
    public double stddev() {
        return StdStats.stddevp(this.thresholds);
    }
    
    public double confidenceLo() {
        return mean() - (1.96 * stddev()) / sqrt(this.trials);
    }
    
    public double confidenceHi() {
        return mean() + (1.96 * stddev()) / sqrt(this.trials);
    }
    
    public static void main(String[] args) {
        int n = 2;
        int trials = 10000;
        PercolationStats ps = new PercolationStats(n, trials);
        System.out.println("mean = " + ps.mean());
        System.out.println("stddev = " + ps.stddev());
        System.out.println("95% confidence interval = "
            + ps.confidenceLo() + ", " + ps.confidenceHi());
    }
}
