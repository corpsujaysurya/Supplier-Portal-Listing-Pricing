package com.kpmg.te.retail.supplierportal.listingpricing.entity;

public class DateAndTime {

	public Integer year;
	public Integer month;
	public Integer date;
	public Integer hour;
	public Integer minute;
	public Integer second;
	public String timestamp;

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getDate() {
		return date;
	}

	public void setDate(Integer date) {
		this.date = date;
	}

	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public Integer getMinute() {
		return minute;
	}

	public void setMinute(Integer minute) {
		this.minute = minute;
	}

	public Integer getSecond() {
		return second;
	}

	public void setSecond(Integer second) {
		this.second = second;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "DateAndTime [year=" + year + ", month=" + month + ", date=" + date + ", hour=" + hour + ", minute="
				+ minute + ", second=" + second + ", timestamp=" + timestamp + "]";
	}

}
