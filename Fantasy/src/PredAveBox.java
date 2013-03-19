
public class PredAveBox {
	public double predScore;
	public double aveScore;
	
	public double getAccuracy() {
		return (predScore/(predScore+aveScore));
	}
	
}
