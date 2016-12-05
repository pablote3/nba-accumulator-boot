package com.rossotti.basketball.jpa.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="official", uniqueConstraints=@UniqueConstraint(columnNames={"lastName", "firstName", "fromDate", "toDate"}))
public class Official extends AbstractDomainClass {

	public Official() {
		setStatusCode(StatusCodeDAO.Found);
	}

	public Official(StatusCodeDAO statusCode) {
		setStatusCode(statusCode);
	}

//	@OneToMany(mappedBy="official", fetch = FetchType.LAZY)
//	private List<GameOfficial> gameOfficials = new ArrayList<GameOfficial>();
//	private List<GameOfficial> getGameOfficials() {
//		return gameOfficials;
//	}
//	@JsonManagedReference(value="gameOfficial-to-official")
//	public void setGameOfficials(List<GameOfficial> gameOfficials) {
//		this.gameOfficials = gameOfficials;
//	}
//	public void addGameOfficial(GameOfficial gameOfficial) {
//		this.getGameOfficials().add(gameOfficial);
//	}
//	public void removeGameOfficial(GameOfficial gameOfficial) {
//		this.getGameOfficials().remove(gameOfficial);
//	}

	@Column(name="lastName", length=25, nullable=false)
	private String lastName;
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name="firstName", length=25, nullable=false)
	private String firstName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name="fromDate", nullable=false)
	private LocalDate fromDate;
	public LocalDate getFromDate()  {
		return fromDate;
	}
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	@Column(name="toDate", nullable=false)
	private LocalDate toDate;
	public LocalDate getToDate()  {
		return toDate;
	}
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	@Column(name="number", length=3, nullable=false)
	private String number;
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}

	public String toString() {
		return ("\r" + "  id: " + this.id + "\n") +
				"  lastName: " + this.lastName + "\n" +
				"  firstName: " + this.firstName + "\n" +
				"  fromDate: " + this.fromDate + "\n" +
				"  toDate: " + this.toDate + "\n";
	}
}