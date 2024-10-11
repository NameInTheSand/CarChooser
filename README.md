# Car Selection App

## Overview

This project is a native Android application developed in Kotlin that allows users to select a unique combination of car manufacturer, model, and year. Users can view a summary of their selected data at the end of the selection process. The data is fetched from a web service, and the app includes features such as pagination and searching capabilities.

## Features

- **Car Data Selection**: Users can select a unique combination of manufacturer, model, and year.
- **Pagination**: The manufacturers list is paginated, displaying 15 elements per page.
- **Search Functionality**: Users can filter models based on text input.
- **Summary Screen**: A summary screen displays all the selected information at the end of the selection process.
- **State Management**: Previously selected values (Manufacturer, Model) are retained when navigating to the next screen.
- **Unit Tests**: Includes unit tests to validate presentation logic , data logic
- **Production Ready**: The application is stable and free from crashes or easily identifiable bugs.
- **Jetpack Compose**: The application is fully written on Compose

## Installation

To build and run this application, follow these steps:

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd <repository-folder>
   ```
2. Create a secret.properties file in the root directory with the following content: <br>
ACCESS_TOKEN= YOUR ACCESS TOKEN without brackets <br>
BASE_URL= BASE URL from documentation without protocol (example: api-test-eu.example.com) <br>

## Testing
  ```bash
  ./gradlew test
 ```

## NOTES
Notes from the author:
Requirements are not fully described, so some moments like navigation back, summary screen, and design so I interpret them freely.  <br>
Also, maybe I added a lot of not described functionality to make the app prettier and more logical <br>
Tablet design may look ugly, but I don't want to prevent a screen rotation to make user experience better




