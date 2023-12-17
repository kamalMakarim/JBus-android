
# JBus Android Application

## Overview

JBus is an Android application designed to streamline the process of bus reservation and management. This project, authored by Kamal Makarim, offers a user-friendly interface for customers and bus operators alike, allowing for efficient handling of bookings, schedules, and payments.

The back-end code for JBus is hosted on GitHub and can be accessed [here](https://github.com/kamalMakarim/JBus).

## Project Structure

The project is structured into two main packages:

### `model`
Contains all the data models used within the application, representing entities such as accounts, buses, and schedules.

#### Account
Stores user credentials and profile information, necessary for user authentication and identification within the app.

#### BaseResponse
A generic class that serves as a template for server responses, ensuring a consistent data format for API calls.

#### Bus
Defines the properties of a bus, including its type, seating capacity, and availability.

#### BusType
Enumerates the different categories of buses offered, such as luxury, double-decker, etc.

#### City
Captures details about the cities serviced by the bus routes.

#### Facility
Lists the amenities provided on each bus, enhancing customer experience.

#### Invoice
Handles the generation and management of invoices for completed bookings.

#### Payment
Processes financial transactions, keeping track of payments made for bookings.

#### Price
Maintains pricing details for different bus routes and services.

#### Renter
Manages information related to individuals or companies renting buses.

#### Schedule
Organizes the bus schedules, including timings and route details.

#### Station
Manages bus station details, including their geographic coordinates and facilities.

#### Serializable
An interface indicating that a class can be serialized for purposes such as intent data passing or storing object states.

### `request`
Consists of classes responsible for network communication between the app and the server.

#### BaseApiService
The core interface for defining the REST API endpoints used by the application.

#### RetrofitClient
A class that creates an instance of the Retrofit client, setting up the base URL and the necessary converters for network communication.

#### UtilsApi
Provides utility methods to access the BaseApiService instance throughout the application.

## Activities
The `activities` folder contains all the Android activities, each representing a different screen within the app.

For instance, `LoginActivity` manages user logins, `RegisterActivity` handles new user registrations, and `MainActivity` is the central hub after a successful login.
![image](https://github.com/kamalMakarim/JBus-android/assets/61099157/543011bc-8404-48fe-81b8-80e7b6ffe964)
![image](https://github.com/kamalMakarim/JBus-android/assets/61099157/18380e87-d764-4442-b07c-2600598b226c)
![image](https://github.com/kamalMakarim/JBus-android/assets/61099157/5f982185-6d81-45fe-b975-8211359d7ad9)
![image](https://github.com/kamalMakarim/JBus-android/assets/61099157/e7c32b6a-8775-4b03-a239-cac41191a249)
![image](https://github.com/kamalMakarim/JBus-android/assets/61099157/9b0887a9-3188-4a71-93b0-89ad8bd87343)
![image](https://github.com/kamalMakarim/JBus-android/assets/61099157/da164c77-1c15-41ae-861c-35dca272f022)
![image](https://github.com/kamalMakarim/JBus-android/assets/61099157/8ed20e60-f62e-4b78-994c-0e3e31ed84a6)
![image](https://github.com/kamalMakarim/JBus-android/assets/61099157/97e166e9-bdac-4d43-a2fe-841227c3ba42)
![image](https://github.com/kamalMakarim/JBus-android/assets/61099157/7f49d7de-f7af-4053-8244-ed92bec1ff05)
![image](https://github.com/kamalMakarim/JBus-android/assets/61099157/0498093b-a383-44bf-ae41-d0a6b147cf55)
![image](https://github.com/kamalMakarim/JBus-android/assets/61099157/e966c751-021e-40da-be9a-5fe97cba35b9)
![image](https://github.com/kamalMakarim/JBus-android/assets/61099157/51e08cc0-8444-488c-89bf-aa03fcbd09db)
![image](https://github.com/kamalMakarim/JBus-android/assets/61099157/bfa077bf-0c01-4b27-819b-4b30fa8d4375)

## Installation

Provide instructions on how to set up and run the project locally. This should include steps like cloning the repository, opening the project in Android Studio, building the project, and running it on an emulator or a device.

## Contributing

Encourage contributions by specifying how others can contribute to the project. Outline the process for submitting pull requests, reporting bugs, or requesting features.

## License

Include details about the project's license here, if applicable.
