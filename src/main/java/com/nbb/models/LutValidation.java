package com.nbb.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the lut_staus database table.
 * 
 */
@Entity
@Table(name="lut_validation")
@NamedQuery(name="LutValidation.findAll", query="SELECT l FROM LutValidation l")
public class LutValidation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private long balanceid;
	private long userid;
	private String code1;
	private String code2;
	private long isformula1;
	private long isformula2;
	private String position1;
	private String position2;
	private String title1;
	private String title2;
	private long valid;

	public LutValidation() {
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getBalanceid() {
		return balanceid;
	}

	public void setBalanceid(long balanceid) {
		this.balanceid = balanceid;
	}

	public String getCode1() {
		return code1;
	}

	public void setCode1(String code1) {
		this.code1 = code1;
	}

	public String getCode2() {
		return code2;
	}

	public void setCode2(String code2) {
		this.code2 = code2;
	}

	public long getIsformula1() {
		return isformula1;
	}

	public void setIsformula1(long isformula1) {
		this.isformula1 = isformula1;
	}

	public long getIsformula2() {
		return isformula2;
	}

	public void setIsformula2(long isformula2) {
		this.isformula2 = isformula2;
	}

	public String getPosition1() {
		return position1;
	}

	public void setPosition1(String position1) {
		this.position1 = position1;
	}

	public String getPosition2() {
		return position2;
	}

	public void setPosition2(String position2) {
		this.position2 = position2;
	}

	public String getTitle1() {
		return title1;
	}

	public void setTitle1(String title1) {
		this.title1 = title1;
	}

	public String getTitle2() {
		return title2;
	}

	public void setTitle2(String title2) {
		this.title2 = title2;
	}

	public long getValid() {
		return valid;
	}

	public void setValid(long valid) {
		this.valid = valid;
	}
}