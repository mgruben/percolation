
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
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
    private final ArrayList<Double> thresholds = new ArrayList<>();
    
    public PercolationStats(int n, int trials) throws IllegalArgumentException {
        if (n <= 0) throw new IllegalArgumentException("n must be positive");
        if (trials <= 0)
            throw new IllegalArgumentException("trials must be positive");
        this.n = n;
        this.trials = trials;
        for (int i = 0; i < this.trials; i++) {
            Percolation perc = new Percolation(this.n);
            while (!perc.percolates()) {
                int this_i = StdRandom.uniform(1, n + 1);
                int this_j = StdRandom.uniform(1, n + 1);
                perc.open(this_i, this_j);
            }
            thresholds.add(perc.getOpenTotal() / (double)(n*n));
        }
    }
    
    public double mean() {
        double sum = 0;
        for (double i: this.thresholds) {
            sum += i;
        }
        return sum / this.trials;
    }
    
    public double stddev() {
        return 1.0;
    }
    
    public double confidenceLo() {
        return 1.0;
    }
    
    public double confidenceHi() {
        return 1.0;
    }
    
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);
    }
}
