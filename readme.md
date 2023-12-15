# Changelog

- To run application execute command `./gradlew run` in backend directory

## 12.12.2023

### Database
- H2 Database is locally stored 

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
- */image/post_image* which accepts list of images encoded in Base64
- */thumbnails/get_thumbnails?size=* which takes request param {size:"small"|"medium"|"large"} returns list of encoded in Base64 images

## 12.12.2023

- Added AppConfig - factory for Beans

- Added ImageScaler - Class responsible for scaling images to 3 sizes, described in ThumbnailSize

- Added Scheduler - Background task, who's responsible for checking the queue for waiting images to be scaled. Checks every 2 secs

- Added ThumbnailService - Service responsible for:
    - Adding scaled thumbnails to DataBase
    - Removing image from queue

## 15.12.2023

- Added tests for ImageControler and ImageScaler
- Validation for sent encodend payloads
- Acceptation of multiple images at once