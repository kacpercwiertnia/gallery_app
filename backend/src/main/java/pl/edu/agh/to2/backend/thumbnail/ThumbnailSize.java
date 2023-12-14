package pl.edu.agh.to2.backend.thumbnail;

public enum ThumbnailSize {
    SMALL(300),
    MEDIUM(500),
    LARGE(800);

    private int size;

    private ThumbnailSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public static ThumbnailSize fromString(String size) {
        return switch (size) {
            case "small" -> SMALL;
            case "medium" -> MEDIUM;
            case "large" -> LARGE;
            default -> null;
        };
    }
}
