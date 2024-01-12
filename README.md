# HOW TO START APPLICATION
1. First go to /backend and execute `./gradlew run`
2. In new terminal windows go to /frontend directory and also run `./gradlew run`

Alternatively you can open these 2 Gradle Projects in 2 IntelliJ windows and run them from IntelliJ

# Changelog

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

## 19.12.2023
- Made all final fields final
- Replaced SOUT with Logger
- Fixed endpoints names
- Put streams in try-with-resources blocks

## 22.12.2023
- Changed polling logic:
  - Created endpoint returning all images IDs in Image Controler
  - Created endpoint returning requested thumbnails (by ImageId and Size) in Thumbnails Controler
  - Added flag isSuccessful to Thumbnail
- Fixed Image Scaler
- Created Request and Response classes for endpoints
- Changed test names

## 04.01.2024
- Created beautiful UI which can send many photos (i.e. one photo)
- Added new endpoint for getting original image size
- Implemented polling mechanism which asks for any new images, and updates
- Frontend Cache mechanism which prevents from disappearing thumbnails when size changes.
- When thumbnail clicked, new window with original image appears.
- New images have thumbnail_placeholder which get updated as soon as polling mechanism gets scaled thumbnail.

## 11.01.2024
- Frontend allows to remove uploaded photos
- Frontend now don't send request when no photo is uploaded
- Frontend displays failed images and don't poll for them
- Polling scheduler don't use Thread.sleep() (even though I liked this)
- ChoiceBox now uses Enum to display possible options of size
- God Controller no more 

