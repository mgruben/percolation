
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
 *
 * @author Michael <GrubenM@GMail.com>
 */
public class Percolation {
    private WeightedQuickUnionUF uf;
    private boolean[][] open;
    private int n;
    
    /**
     * Initializes an n*n grid of blocked sites, to be opened in the
     * search for a grid that percolates.
     * 
     * @param n, the dimension of the n*n grid to initialize
     * @throws IllegalArgumentException 
     */
    public Percolation(int n) throws IllegalArgumentException {
        if (n <= 0) throw new IllegalArgumentException("n must be positive");
        this.n = n;
        this.uf = new WeightedQuickUnionUF(this.n+2);
        open = new boolean[n+2][n+1];
                
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) { // main n*n grid
                open[i][j] = false;
            }
        }
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
        return (i-1)*n + j;
    }
    
    
    /**
     * Changes the state of block (row i, column j) to open, if closed.
     * 
     * @param i
     * @param j
     * @throws IndexOutOfBoundsException 
     */
    public void open(int i, int j) throws IndexOutOfBoundsException {
        if (i < 1 && j < 1)
            throw new IndexOutOfBoundsException("i and j must be positive");
        if (i < 1) throw new IndexOutOfBoundsException("i must be positive");
        if (j < 1) throw new IndexOutOfBoundsException("j must be positive");
        if (this.open[i][j] == false) {
            this.open[i][j] = true;
            if (i == 1) uf.union(ijTo1D(i,j), 0);
            if (i == n) uf.union(ijTo1D(i,j), n+1);
            try {   // check left
                if (isOpen(i-1,j)) uf.union(ijTo1D(i-1,j), ijTo1D(i,j));
            } catch (IndexOutOfBoundsException e) { }
            try {   // check right
                if (isOpen(i+1,j)) uf.union(ijTo1D(i+1,j), ijTo1D(i,j));
            } catch (IndexOutOfBoundsException e) { }
            try {   // check down
                if (isOpen(i,j-1)) uf.union(ijTo1D(i,j-1), ijTo1D(i,j));
            } catch (IndexOutOfBoundsException e) { }
            try {   // check up
                if (isOpen(i,j+1)) uf.union(ijTo1D(i,j+1), ijTo1D(i,j));
            } catch (IndexOutOfBoundsException e) { }
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
    public boolean isOpen(int i, int j) throws IndexOutOfBoundsException {
        if (i < 1 && j < 1)
            throw new IndexOutOfBoundsException("i and j must be positive");
        if (i < 1) throw new IndexOutOfBoundsException("i must be positive");
        if (j < 1) throw new IndexOutOfBoundsException("j must be positive");
        return this.open[i][j];
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
    public boolean isFull(int i, int j) throws IndexOutOfBoundsException {
        if (i < 1 && j < 1)
            throw new IndexOutOfBoundsException("i and j must be positive");
        if (i < 1) throw new IndexOutOfBoundsException("i must be positive");
        if (j < 1) throw new IndexOutOfBoundsException("j must be positive");
        return uf.connected(ijTo1D(i,j),0);
    }
    
    
    /**
     * Return whether the n*n grid percolates.
     * That is, whether there is a full site in the bottom row.
     * 
     * @return 
     */
    public boolean percolates() {
        return uf.connected(this.n+1,0);
    }
    
    public static void main(String[] args) {

    }
}
