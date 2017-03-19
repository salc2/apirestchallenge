package ve.salc2.challenge.domain;

import java.io.Serializable;

public class Statistic implements Serializable {
    public final double sum;
    public final double avg;
    public final double max;
    public final double min;
    public final long count;

    public Statistic(double sum, double avg, double max, double min, long count) {
        this.sum = sum;
        this.avg = avg;
        this.max = max;
        this.min = min;
        this.count = count;
    }
}
