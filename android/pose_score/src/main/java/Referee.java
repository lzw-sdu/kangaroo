import org.ejml.simple.SimpleMatrix;

import java.util.ArrayDeque;

public class Referee
{
    int count = 0;
    int score = 0;
    double base = 1.0;
    ArrayDeque<Pose> buffer;
    int rows;
    int cols;

    Referee(int max_buff, Pose pose_start)
    {
        this.buffer = new ArrayDeque<Pose>(max_buff);
        buffer.add(pose_start);
    }

    // norm2 with proximity
    double N2P(Pose std_pose, Pose user_pose)
    {
        if (std_pose == null)
        {
            return 0;
        }
        SimpleMatrix pow_diff_coord = std_pose.rel_coord_mat.minus(user_pose.rel_coord_mat);
        for (int i = 0; i < rows * cols; i++)
        {
            double v = pow_diff_coord.get(i);
            pow_diff_coord.set(i, v * v);
        }
        SimpleMatrix avg_rel = (std_pose.approx_vec.plus(user_pose.rel_coord_mat)).divide(2);


        return 1 - ((pow_diff_coord.mult(avg_rel)).elementSum()) / (avg_rel.elementSum());
    }

    double IoU(Pose std_pose, Pose user_pose)
    {
        if (std_pose == null)
        {
            return 0;
        }
        double[] std_l = std_pose.getBBox(), user_l = user_pose.getBBox();

        double p1_x = std_l[0], p1_y = std_l[1];
        double p2_x = std_l[2], p2_y = std_l[3];
        double p3_x = user_l[0], p3_y = user_l[1];
        double p4_x = user_l[2], p4_y = user_l[3];

        if (p1_x > p4_x || p2_x < p3_x || p1_y > p4_y || p2_y < p3_y)
        {
            return 0;
        }
        double Len = Math.min(p2_x, p4_x) - Math.max(p1_x, p3_x);
        double Wid = Math.min(p2_y, p4_y) - Math.max(p1_y, p3_y);

        double s_std = (std_l[1] - std_l[0]) * (std_l[3] - std_l[2]);
        double s_user = (user_l[1] - user_l[0]) * (user_l[3] - user_l[2]);
        return (2 * Len * Wid) / (s_std + s_user);

    }

    PoseStatus judge_edge(Pose std_pose, Pose user_pose)
    {
        count += 1;
        double sim_last = N2P(buffer.getFirst(), user_pose);
        double sim_this = N2P(std_pose, user_pose);
        PoseStatus poseStatus;

        if (sim_this >= sim_last)
        {
            buffer.clear();
            buffer.add(std_pose);
            score += sim_this;

            poseStatus = new PoseStatus(Status.Current, sim_this);

        } else
        {
            score += sim_last * Math.pow(base, buffer.size() - 1);
            buffer.removeFirst();
            buffer.add(std_pose);

            poseStatus = new PoseStatus(Status.Last, sim_last);
        }

        return poseStatus;
    }

    double get_score()
    {
        return score / (double) count;
    }


    public static void main(String[] args)
    {

    }
}
