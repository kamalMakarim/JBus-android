
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

## Installation

Provide instructions on how to set up and run the project locally. This should include steps like cloning the repository, opening the project in Android Studio, building the project, and running it on an emulator or a device.

## Contributing

Encourage contributions by specifying how others can contribute to the project. Outline the process for submitting pull requests, reporting bugs, or requesting features.

## License

Include details about the project's license here, if applicable.
