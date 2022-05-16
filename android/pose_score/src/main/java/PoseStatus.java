enum Status
{
    Current,Last,Middle,Fail
}

public class PoseStatus
{
    Status status;
    double score;
    PoseStatus(Status status,double score)
    {
        this.status = status;
        this.score = score;
    }
}
