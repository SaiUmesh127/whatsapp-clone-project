// package com.accenture.whatsapp.entity;

// import java.util.Set;

// import javax.persistence.JoinColumns;

// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinTable;
// import jakarta.persistence.ManyToMany;
// import jakarta.persistence.Table;

// @Entity
// @Table(name = "groups")
// public class Group {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private String name;
//     private String description;

//     @ManyToMany
//     @JoinTable(
//         name = "group_members",
//         joinColumns = JoinColumns(name = "group_id"),
//         inverseJoinColumns = JoinColumns(name = "user_id")
//     )
//     private Set<User> members;

//     // ✅ Constructors
//     public Group() {}

//     public Group(String name, String description) {
//         this.name = name;
//         this.description = description;
//     }

//     // ✅ Getters and Setters
//     public Long getId() {
//         return id;
//     }

//     public void setId(Long id) {
//         this.id = id;
//     }

//     public String getName() {
//         return name;
//     }

//     public void setName(String name) {
//         this.name = name;
//     }

//     public String getDescription() {
//         return description;
//     }

//     public void setDescription(String description) {
//         this.description = description;
//     }

//     public Set<User> getMembers() {
//         return members;
//     }

//     public void setMembers(Set<User> members) {
//         this.members = members;
//     }
// }