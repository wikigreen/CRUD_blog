package com.vladimir.crudblog.model;

import java.util.List;

public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private List<Post> posts;
    private Region region;
    private Role role;

    public User(Long id, String firstName, String lastName,List<Post> posts, Region region, Role role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.posts = posts;
        this.region = region;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) return false;
        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
        if (posts != null ? !posts.equals(user.posts) : user.posts != null) return false;
        if (region != null ? !region.equals(user.region) : user.region != null) return false;
        return role == user.role;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (posts != null ? posts.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String str =
                "User`s id: " + id + "\n" +
                "First name: " + firstName + "\n" +
                "Last Name: " + lastName +  "\n" +
                "List of posts: ";
                str += getPosts().stream()
                        .filter(p -> p != null)
                        .map(p -> p.getId() + ", ")
                        .reduce((a, b) -> a + b)
                        .orElse("List of posts is empty    ");
                str = str.substring(0, str.length() - 2) + "\n";
                str += "Region: " + (region == null ? "null" : region) + "\n" +
                "Role: " + role.toString();
        return str;
    }
}
