package com.c123.demo.utils;

import java.math.BigDecimal;
import java.util.Random;

import com.c123.demo.model.facts.ActionState;

public class GameDataGenerator {

	public static int generateCustomerID(){
		Random rand = new Random();
	    int randomNum = rand.nextInt(99999999)+1000000;
	    return randomNum;
	}
	
	/*
	public static int generateAmount(){
		Random rand = new Random();
	    int randomNum = rand.nextInt(200)+1;
	    return randomNum;
	}
	*/
	
	public static BigDecimal generateAmount(){
		Random rand = new Random();
		BigDecimal val = new BigDecimal( rand.nextDouble()*500);
	    return val;
	}
	

	

	public static int generateNetworkID(){
		Random rand = new Random();
		int randomNum = rand.nextInt(3)+1;
	    return randomNum;
	}
	
	public static int generateID(int max){
		Random rand = new Random();
		int randomNum = rand.nextInt(max)+1;
	    return randomNum;
	}
}
