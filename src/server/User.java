package server;


public class User {
    private int id;
    private String username;
    private int age;
    private String location;
    public User(int id, String username, int age, String location) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.location = location;
    }

    public int getId(){
        return this.id;
    }
    public int getAge(){ return this.age;}
    public String getLocation() {return this.location;}
    public String getUsername(){return this.username;}
}
