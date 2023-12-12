package pl.edu.agh.to2.backend.thumbnail;

public enum ThumbnailSize {
    SMALL(300),
    MEDIUM(500),
    LARGE(800);

    private int size;
    private ThumbnailSize(int size){
        this.size = size;
    }
    public int getSize(){
        return size;
    }
}
