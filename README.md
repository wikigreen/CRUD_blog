# CRUD_blog
Studing console CRUD application

I want to present you my studing console CRUD application. App represents database for blog. 

## Table of Contents
* [Problem Statemen](#problem-statemen)
* [Requirements](#requirements)
* [How to run](#how-to-run)

## Problem Statemen
Problem Statement: I must implement console CRUD aplication. It should have following entities: 

* User (id, firstName, lastName, List<Post> posts, Region region, Role role)
* Post (id, content, created, updated)
* Region (id, name)
* Role (enum ADMIN, MODERATOR, USER)

As a database should be used .txt files:

* users.txt
* posts.txt
* regions.txt

User from should be able to create, get, update and delete entities.

Layers:
* model - POJO classes
* repository - classes that implement access to .txt files
* controller - classes that timplements processing of requests from user
* view - classes that handle console.

For example: User, UserRepository, UserController, UserView etc.


It is advisable to user basic interface for repository layer:
interface GenericRepository<T,ID>

class UserRepository implements GenericRepository<User, Long>

## Requirements
* IDE
* Java 14 or newer
* Git

## How to run 
* git clone https://github.com/wikigreen/CRUD_blog
* go to CRUD_blog.src.main.java.com.vladimir.crudblog.view
* run Main.java
