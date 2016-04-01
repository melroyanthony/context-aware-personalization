package com.example.aero.localife;

/**
 This activity handles the list of Profiles created
 **/
public class ProfileListActivity {
 private String profileName;
 public ProfileListActivity(String profileName) {
   this.profileName = profileName;
 }

 public String getName() {
  return profileName;
 }
 public void setName(String profileName) {
  this.profileName = profileName;
 }
}
