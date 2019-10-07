package ar.edu.itba.sia;

import java.util.List;


/**
 *  Simple POJO to group all the borders restrictions
**/
public class Restrictions {
    private List<Integer> up;
    private List<Integer> right;
    private List<Integer> bottom;
    private List<Integer> left;

    public Restrictions(List<Integer> up, List<Integer> right, List<Integer> bottom, List<Integer> left) {
        this.up = up;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public List<Integer> getUp() {
        return up;
    }

    public List<Integer> getRight() {
        return right;
    }

    public List<Integer> getBottom() {
        return bottom;
    }

    public List<Integer> getLeft() {
        return left;
    }
}
