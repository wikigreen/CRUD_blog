package model;


public class Region {
    private String name;
    private Long id;

    public Region (int id, String name){
        this.id = (long)id;
        this.name = name;
    }

    public Region (Long id, String name){
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Region region = (Region) o;

        if (!id.equals(region.id)) return false;
        return name != null ? name.equals(region.name) : region.name == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }

    public String toString(){
        return this.id + "," + this.name;
    }
}
