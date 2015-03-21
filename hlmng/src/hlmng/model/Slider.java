package hlmng.model;

public class Slider {
	private int sliderID;
	private String name;
	private int weigth;
	private int votingIDFK;
	public Slider(){
		
	}
	public Slider(int sliderID, String name, int weigth, int votingIDFK){
		this.sliderID=sliderID;
		this.name=name;
		this.weigth=weigth;
		this.votingIDFK=votingIDFK;
	}
	public Slider(String name, int weigth, int votingIDFK){
		this.name=name;
		this.weigth=weigth;
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
	public int getWeigth() {
		return weigth;
	}
	public void setWeigth(int weigth) {
		this.weigth = weigth;
	}
	public int getVotingIDFK() {
		return votingIDFK;
	}
	public void setVotingIDFK(int votingIDFK) {
		this.votingIDFK = votingIDFK;
	}
	
}
