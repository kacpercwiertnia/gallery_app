# Changelog

## 12.12.2023

### Database

- Created database diagram 
![Database](./resources/database_diagram.png)
- Created Enum that defines thumbnail sizes
    ```Java
    public enum ThumbnailSize {
        SMALL(300),
        MEDIUM(500),
        LARGE(800);
    }
    ```
- Created classes according to diagram:
    - Image
    - Queue
    - Thumbnail

### API
Our application use 2 endpoints for communication: 
- */image/post_image* which accepts image encoded in Base64
- */image/get_images/{size}* which returns list of encoded in Base64 images

