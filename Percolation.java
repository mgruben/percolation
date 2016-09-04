
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

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
 * @written 2016-09-03
 * 
 * Class for studying percolation in an arbitrarily-large grid of closed sites.
 */
public class Percolation {
    private WeightedQuickUnionUF uf;
    private boolean[] open;
    private boolean[] toTop;
    private boolean[] toBottom;
    private boolean percolates;
    private int n;
    private int openTotal;
    
    /**
     * Initializes an n*n grid of blocked sites, to be opened in the
     * search for a grid that percolates.
     * 
     * @param n, the dimension of the n*n grid to initialize
     * @throws IllegalArgumentException 
     */
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be positive");
        this.n = n;
        this.uf = new WeightedQuickUnionUF(this.n*this.n + 2);
        this.open = new boolean[n * n];
        this.toTop = new boolean[n * n];
        this.toBottom = new boolean[n * n];
        this.percolates = false;
        for (int i = 0; i < n * n; i++) { // main n*n grid
            open[i] = false;
            toTop[i] = false;
            toBottom[i] = false;
        }
    }
    
    /**
     * Given the index of a site and the index of its directly-adjacent
     * neighbor, checks to see if either site is connected to the top
     * or to the bottom, unions the site and its neighbor, and returns
     * connection flags as a boolean array.
     * 
     * @param index
     * @param neighborIndex 
     */
    private boolean[] checkNeighbor(int index, int neighbor, boolean[] tb) {
        if (isConnTop(index) || isConnTop(neighbor))
            tb[0] = true;
        if (isConnBottom(index) || isConnBottom(neighbor))
            tb[1] = true;
        uf.union(index, neighbor);
        return tb;
    }
    
    /**
     * Returns whether the root of the given index is connected to the top.
     * 
     * @param index
     * @return 
     */
    private boolean isConnTop(int index) {
        return toTop[uf.find(index)];
    }
    
    /**
     * Returns whether the root of the given index is connected to the bottom.
     * 
     * @param index
     * @return 
     */
    private boolean isConnBottom(int index) {
        return toBottom[uf.find(index)];
    }
    
    /**
     * Sets whether the root of the given index is connected to the top.
     * 
     * @param index
     * @param connected
     * @return 
     */
    private void setTop(int index, boolean connected) {
        toTop[uf.find(index)] = connected;
    }
    
    /**
     * Sets whether the root of the given index is connected to the bottom.
     * 
     * @param index
     * @param connected
     * @return 
     */
    private void setBottom(int index, boolean connected) {
        toBottom[uf.find(index)] = connected;
    }
    
    /**
     * Returns the initial ID of the site at the coordinate
     * (row i, column j).
     * 
     * @param i
     * @param j
     * @return 
     */
    private int ijTo1D(int i, int j) {
        return (i - 1)*n + (j - 1);
    }
    
    /**
     * Ensures that the given (i, j) tuple is valid within the established
     * n-by-n grid.
     * @param i
     * @param j
     * @return 
     */
    private void validateIndex(int i, int j) {
        if (i < 1 && j < 1)
            throw new IndexOutOfBoundsException("i and j must be positive");
        if (i < 1) throw new IndexOutOfBoundsException("i must be positive");
        if (j < 1) throw new IndexOutOfBoundsException("j must be positive");
        if (i > n) throw new IndexOutOfBoundsException("i is too large");
        if (j > n) throw new IndexOutOfBoundsException("j is too large");
    }
    
    /**
     * Changes the state of block (row i, column j) to open, if closed.
     * 
     * @param i
     * @param j
     * @throws IndexOutOfBoundsException 
     */
    public void open(int i, int j) {
        validateIndex(i, j);
        int index = ijTo1D(i, j);
        if (!this.open[index]) {
            boolean[] topBottom = {false, false};
            this.open[index] = true;
            this.openTotal++;
            if (i == 1) topBottom[0] = true;
            if (i == n) topBottom[1] = true;
            
            if ((i - 1 > 0) && (isOpen(i - 1, j))) {
                int neighborIndex = ijTo1D(i - 1, j);
                topBottom = checkNeighbor(index, neighborIndex, topBottom);
            }
            if ((i + 1 <= n) && (isOpen(i + 1, j))) {
                int neighborIndex = ijTo1D(i + 1, j);
                topBottom = checkNeighbor(index, neighborIndex, topBottom);
            }
            if ((j - 1 > 0) && (isOpen(i, j - 1))) {
                int neighborIndex = ijTo1D(i, j - 1);
                topBottom = checkNeighbor(index, neighborIndex, topBottom);
            }
            if ((j + 1 <= n) && (isOpen(i, j + 1))) {
                int neighborIndex = ijTo1D(i, j + 1);
                topBottom = checkNeighbor(index, neighborIndex, topBottom);
            }
            setTop(index, topBottom[0]);
            setBottom(index, topBottom[1]);
            if (isConnTop(index) && isConnBottom(index)) this.percolates = true;
        }
    }
    
    
    /**
     * Returns whether block (row i, column j) is open.
     * 
     * @param i
     * @param j
     * @return
     * @throws IndexOutOfBoundsException 
     */
    public boolean isOpen(int i, int j) {
        validateIndex(i, j);
        return this.open[ijTo1D(i, j)];
    }
    
    /**
     * Returns whether block (row i, column j) is full.
     * That is, whether that block can be connected to an open site
     * in the top row via a chain of neighboring open sites.
     * 
     * @param i
     * @param j
     * @return
     * @throws IndexOutOfBoundsException 
     */
    public boolean isFull(int i, int j) {
        validateIndex(i, j);
        return isConnTop(ijTo1D(i, j));
    }
    
    /**
     * Return whether the n*n grid percolates.
     * That is, whether there is a full site in the bottom row.
     * 
     * @return 
     */
    public boolean percolates() {
        return this.percolates;
    }
    
    /**
     * Instantiates a Percolation object, then randomly opens sites until
     * percolation occurs.
     * Then, prints the total number of open sites, the width
     * (or, equivalently, the height) of the grid, and the threshold fraction
     * at which percolation first occurred.
     * 
     * @param args n
     */
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        Percolation p = new Percolation(n);
        while (!p.percolates()) {
            int i = StdRandom.uniform(1, n + 1);
            int j = StdRandom.uniform(1, n + 1);
            String o = String.format("Opening (%d, %d)", i, j);
            System.out.println(o);
            p.open(i, j);
        }
        double threshold = (double) p.openTotal / (double) (n*n);
        System.out.println(p.openTotal);
        System.out.println(n);
        System.out.println(threshold);
    }
}
