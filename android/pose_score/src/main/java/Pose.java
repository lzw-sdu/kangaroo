import org.ejml.simple.SimpleMatrix;

public class Pose
{
    SimpleMatrix rel_coord_mat;
    SimpleMatrix approx_vec;
    double x_min, x_max, y_min, y_max;


    Pose(SimpleMatrix rel_coord_mat, SimpleMatrix approx_vec)
    {
        this.rel_coord_mat = rel_coord_mat;
        this.approx_vec = approx_vec;
        calBBox();
    }

    private void calBBox()
    {
        SimpleMatrix col_x = rel_coord_mat.extractVector(false, 0);
        double max = 0, min = 10;
        for (int i = 0; i < col_x.getNumElements(); i++)
        {
            double x = col_x.get(i);
            if (x > max)
            {
                max = x;
            }
            if (x < min)
            {
                min = x;
            }
        }
        x_min = min;
        x_max = max;

        SimpleMatrix col_y = rel_coord_mat.extractVector(false, 1);
        max = 0;
        min = 10;
        for (int i = 0; i < col_y.getNumElements(); i++)
        {
            double y = col_y.get(i);
            if (y > max)
            {
                max = y;
            }
            if (y < min)
            {
                min = y;
            }
        }
        y_min = min;
        y_max = max;
    }

    double[] getBBox()
    {
        return new double[]{x_min, x_max, y_min, y_max};
    }

    public static void main(String[] args)
    {

    }
}
