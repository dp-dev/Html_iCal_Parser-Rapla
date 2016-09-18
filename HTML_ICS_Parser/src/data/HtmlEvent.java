package data;
import java.util.ArrayList;

public class HtmlEvent {
	private String begindate, enddate, begintime, endtime, categorie, title;
	private ArrayList<String> rooms = new ArrayList<>();
	
	public HtmlEvent(String begindate, String enddate, String begintime, String endtime) {
		this.begindate = begindate;
		this.enddate = enddate;
		this.begintime = begintime;
		this.endtime = endtime;
	}
	
	public void setEventTitle(String title) {
		this.title = title;
	}
	
	public void addEventRoom(String room) {
		this.rooms.add(room);
	}

	public String getCategorie() {
		return categorie;
	}
	
	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	public String getBegindate() {
		return begindate;
	}

	public String getEnddate() {
		return enddate;
	}

	public String getBegintime() {
		return begintime;
	}

	public String getEndtime() {
		return endtime;
	}

	public String getTitle() {
		return title;
	}

	public ArrayList<String> getRooms() {
		return rooms;
	}

}
