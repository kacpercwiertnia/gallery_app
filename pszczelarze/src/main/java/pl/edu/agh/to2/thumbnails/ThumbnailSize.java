package pl.edu.agh.to2.thumbnails;

public enum ThumbnailSize {
    SMALL,
    MEDIUM,
    LARGE;

    private static ThumbnailSize fromString(String size){
        return switch(size){
            case "SMALL" -> SMALL;
            case "MEDIUM" -> MEDIUM;
            case "LARGE" -> LARGE;
            default -> null;
        };
    }
}