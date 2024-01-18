package pl.edu.agh.to2.backend.directory;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.Id;



@Entity
@Table(indexes = @Index(columnList = "path", unique = true))
public class Directory {
    @Id
    @GeneratedValue
    private int directoryId;
    private String path;
    private String parent;

    public Directory(){}

    public Directory(String path, String parent){
        this.path = path;
        this.parent = parent;
    }

    public String getPath() {
        return path;
    }

    public String getParent(){
        return parent;
    }

    public int getFolderId() {
        return directoryId;
    }

    public void setParent(String parent){
        this.parent = parent;
    }

    public void setPath(String path){
        this.path = path;
    }
}
