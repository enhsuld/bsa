package com.nbb.models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 * The persistent class for the FILE_UPLOAD database table.
 * 
 */
@Entity
@Table(name="file_converted")
@NamedQuery(name="FileConverted.findAll", query="SELECT f FROM FileConverted f")
public class FileConverted implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String name;
	@Column(name="flurl")
	private String flurl;
	@Column(name="fsize")
	private double fsize;
	@Column(name="fdate")
	private String fdate;
	@Column(name="userid")
	private long userid;

	@OneToMany(mappedBy="fileConverted")
	private List<StsCheckVariable> stsCheckVariable;

    public FileConverted() {
        // Default Constructor
    }

	public List<StsCheckVariable> getStsCheckVariable() {
		return stsCheckVariable;
	}

	public void setStsCheckVariable(List<StsCheckVariable> stsCheckVariable) {
		this.stsCheckVariable = stsCheckVariable;
	}

	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getFlurl() {
		return flurl;
	}


	public void setFlurl(String flurl) {
		this.flurl = flurl;
	}


	public double getFsize() {
		return fsize;
	}


	public void setFsize(double fsize) {
		this.fsize = fsize;
	}


	public String getFdate() {
		return fdate;
	}


	public void setFdate(String fdate) {
		this.fdate = fdate;
	}


	public long getUserid() {
		return userid;
	}


	public void setUserid(long userid) {
		this.userid = userid;
	}
	  
   
	
}