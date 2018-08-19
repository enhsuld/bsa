package com.nbb.models;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;



/**
 * The persistent class for the STS_CHECK_VARIABLES database table.
 * 
 */
@Entity
@Table(name="sts_check_variable")
@NamedQuery(name="StsCheckVariable.findAll", query="SELECT l FROM StsCheckVariable l")
public class StsCheckVariable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	private String data1;

	private long data10;

	private String data2;

	private long data3;

	private String data4;

	private String data5;

	private long data6;

	private String data7;

	private String data8;

	private long data9;
	
	private long orgcatid;

	private String orgcode;

	private long stepid;
	
	private long planid;
	
	private Long valid;
	
	//bi-directional many-to-one association to MainAuditRegistration
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="planid",nullable = false,insertable=false,updatable=false)
	@JsonBackReference
	private FileConverted fileConverted;

	public StsCheckVariable() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getData1() {
		return this.data1;
	}

	public void setData1(String data1) {
		this.data1 = data1;
	}

	public long getData10() {
		return this.data10;
	}

	public void setData10(long data10) {
		this.data10 = data10;
	}

	public String getData2() {
		return this.data2;
	}

	public void setData2(String data2) {
		this.data2 = data2;
	}

	public long getData3() {
		return this.data3;
	}

	public void setData3(long data3) {
		this.data3 = data3;
	}

	public String getData4() {
		return this.data4;
	}

	public void setData4(String data4) {
		this.data4 = data4;
	}

	public String getData5() {
		return this.data5;
	}

	public void setData5(String data5) {
		this.data5 = data5;
	}

	public long getData6() {
		return this.data6;
	}

	public void setData6(long data6) {
		this.data6 = data6;
	}

	public String getData7() {
		return this.data7;
	}

	public void setData7(String data7) {
		this.data7 = data7;
	}

	public String getData8() {
		return this.data8;
	}

	public void setData8(String data8) {
		this.data8 = data8;
	}

	public long getData9() {
		return this.data9;
	}

	public void setData9(long data9) {
		this.data9 = data9;
	}

	public long getOrgcatid() {
		return orgcatid;
	}

	public void setOrgcatid(long orgcatid) {
		this.orgcatid = orgcatid;
	}

	public String getOrgcode() {
		return orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public long getStepid() {
		return stepid;
	}

	public void setStepid(long stepid) {
		this.stepid = stepid;
	}

	public long getPlanid() {
		return planid;
	}

	public void setPlanid(long planid) {
		this.planid = planid;
	}
		
	public Long getValid() {
		return valid;
	}

	public void setValid(Long valid) {
		this.valid = valid;
	}

	public FileConverted getFileConverted() {
		return fileConverted;
	}

	public void setFileConverted(FileConverted fileConverted) {
		this.fileConverted = fileConverted;
	}
}