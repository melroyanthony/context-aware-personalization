package com.example.aero.localife.create_profile;

public class ProfileListActivity {

 private String profileName;

 public ProfileListActivity(String profileName) {
   this.profileName = profileName;
 }

 //setter method
 public void setName(String profileName) {
  this.profileName = profileName;
 }

 //getter method
 public String getName() {
  return profileName;
 }
}
