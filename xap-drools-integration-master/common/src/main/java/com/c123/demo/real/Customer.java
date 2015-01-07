package com.c123.demo.real;

import java.io.Serializable;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;


@SpaceClass(persist=true)
public class Customer implements Serializable{

	private static final long serialVersionUID = 2205250824357279376L;
	private Integer id;
    private String name;
    private String address;
    private String mobilePhone;
    private Integer networkId;

	public Customer(){}
    

	@SpaceId(autoGenerate=false)
	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getMobilePhone() {
		return mobilePhone;
	}



	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}


	@SpaceRouting
	public Integer getNetworkId() {
		return networkId;
	}



	public void setNetworkId(Integer networkId) {
		this.networkId = networkId;
	}



	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", address=" + address
				+ ", mobilePhone=" + mobilePhone + ", networkId=" + networkId
				+ "]";
	}
	
}
