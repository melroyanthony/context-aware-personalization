package com.example.aero.localife;

public class ProfileListActivity {

 private String profileName;
 private String latitudeValue;
 private String longitudeValue;

 public ProfileListActivity(String profileName, String latitudeValue, String longitudeValue) {
   this.profileName = profileName;
   this.latitudeValue = latitudeValue;
   this.longitudeValue = longitudeValue;
 }

 //setter method
 public void setProfileName(String profileName) {
  this.profileName = profileName;
 }

 //getter method
 public String getProfileName() {
  return profileName;
 }

 //setter method
 public void setLatitudeValue(String latitudeValue) {
  this.latitudeValue = latitudeValue;
 }

 //getter method
 public String getLatitudeValue() {
  return latitudeValue;
 }

 //setter method
 public void setLongitudeValue(String longitudeValue){
  this.longitudeValue = longitudeValue;
 }

 //getter method
 public String getLongitudeValue(){
  return longitudeValue;
 }

}
