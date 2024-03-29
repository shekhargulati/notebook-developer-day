package com.summit.notebook.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.springframework.social.connect.UserProfile;

@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NotNull
    private String username;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 6, max = 10)
    private String password;

    @NotNull
    private String fullName;

    @Version
    @Column(name = "version")
    private Integer version;

    public Profile() {
        // TODO Auto-generated constructor stub
    }

    Profile(UserProfile userProfile) {
        this.username = userProfile.getUsername();
        this.email = userProfile.getEmail();
        this.fullName = userProfile.getFirstName() + " " + userProfile.getLastName();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Author [id=" + id + ", username=" + username + ", email="
                + email + ", password=" + password + ", fullName=" + fullName
                + ", version=" + version + "]";
    }

    public static Profile fromTwitterProfile(UserProfile fetchUserProfile) {
        Profile profile = new Profile(fetchUserProfile);
        return profile;
    }

}
