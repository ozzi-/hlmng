package hlmng.model;

public class Slider {
	private int sliderID;
	private String name;
	private int weight;
	private int votingIDFK;
	public Slider(){
		
	}
	public Slider(int sliderID, String name, int weight, int votingIDFK){
		this.sliderID=sliderID;
		this.name=name;
		this.weight=weight;
		this.votingIDFK=votingIDFK;
	}
	public Slider(String name, int weight, int votingIDFK){
		this.name=name;
		this.weight=weight;
		this.votingIDFK=votingIDFK;
	}
	public int getSliderID() {
		return sliderID;
	}
	public void setSliderID(int sliderID) {
		this.sliderID = sliderID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getVotingIDFK() {
		return votingIDFK;
	}
	public void setVotingIDFK(int votingIDFK) {
		this.votingIDFK = votingIDFK;
	}
	
}
