public class TeamBox {
	private double projectionScore;
	String name;
	private double averageScore;
	double accuracy;
	
	public TeamBox(String name, double projectionScore, double averageScore) {
		this.name = name;
		this.projectionScore = projectionScore;
		this.averageScore = averageScore;
	}
	
	public void addProjectionScore(double projectionScore) {
		this.projectionScore += projectionScore;
		if((this.projectionScore + this.averageScore) != 0) {
			accuracy = this.projectionScore/(this.projectionScore + this.averageScore);
		}
		
		
	}
	public void addAverageScore(double averageScore){
		this.averageScore += averageScore;
		if((this.projectionScore + this.averageScore) != 0) {
			accuracy = this.projectionScore/(this.projectionScore + this.averageScore);
		}
	}
	
	public double getAccuracy() {
		return Math.floor(accuracy * 100) / 100;
		
	}
	
	public String toString(){
		return name + ": proj " + this.projectionScore + ", ave " + this.averageScore + " acc " + this.accuracy;
		
	}
	
}
