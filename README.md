Original App Design Project - README Template
===

# Real Estate App (temp title)

## Table of Contents
1. [Overview](#Overview)
2. [Product Spec](#Product-Spec)
3. [Wireframes](#Wireframes)
4. [Schema](#Schema)

## Overview
### Description
Android mobile application that will allow users to sign in to an account and then using a desired location parameter will be able to view and gain information regarding real estate properties that fit their desired parameter. User will also be able to view additional screens to include property descriptions and details provided from the listing agent's brokerage.

### App Evaluation
- **Category:** House & Homes 
- **Mobile:** This app would be developed for mobile but the functionality would also be possible on a computer, such as Zillow, Realtor.com, or other similar apps. The mobile app is appealing for when the user is out looking at different properties and need to look at addresses or is interested in comparing property details on the spot. 
- **Story:** Lets users find listings of properties in their desired area, view the listing details, and save the properties to their wishlist.
- **Market:** Anyone looking to find a property to buy could find this app useful. Anyone interested in knowing house prices around them could find this app useful.
- **Habit:** The user would be compelled to use this app on a regular basis, especially if they are actively looking to buy.
- **Scope:** We would be able to complete this app in one month. We would start with the basic funtionality of the app first and then move in to the optional stories if we have time.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

- [x] Users can sign up for a new account using Parse authentication.
      <details>
        <summary>Video Walkthrough</summary>
          <img src='https://github.com/OSU-CodePath-Android-Spring-2022/real-estate-app/blob/main/demos/signup.gif' title='Signup Video' width=''>
      </details>
- [x] Users can log in and log out of his/her account. 
      <details>
        <summary>Video Walkthrough</summary>
          <img src='https://github.com/OSU-CodePath-Android-Spring-2022/real-estate-app/blob/main/demos/login.gif' title='Login Video' width=''>
          <img src='https://github.com/OSU-CodePath-Android-Spring-2022/real-estate-app/blob/main/demos/logout.gif' title='Logout Video' width=''>
      </details>
- [x] Users can enter an address they are curious about and the app will show them related listings within that town and/or other parameter.
      <details>
        <summary>Video Walkthrough</summary>
          <img src='https://github.com/OSU-CodePath-Android-Spring-2022/real-estate-app/blob/main/demos/search-by-address.gif' title='Search by Address Video' width=''>
      </details>
- [x] Users can save and create lists of the properties they have viewed and add to their "wish list".
      <details>
        <summary>Video Walkthrough</summary>
          <img src='https://github.com/OSU-CodePath-Android-Spring-2022/real-estate-app/blob/main/demos/save-to-wishlist.gif' title='Save to Wishlist Video' width=''>
      </details>
- [x] Users are able to click on a listing to bring them to a new screen that contains the specific details about that property.
- [x] Users can switch between the search, results, and wishlist tabs using fragments and a Bottom Navigation View.
      <details>
        <summary>Video Walkthrough</summary>
          <img src='https://github.com/OSU-CodePath-Android-Spring-2022/real-estate-app/blob/main/demos/bottom-nav.gif' title='Signup Video' width=''>
      </details>

**Optional Nice-to-have Stories**

- [x] Users can search for properties in their city (US only).
- [ ] Users are able to add comments about a listing in a comment box.
- [ ] When viewing the details for a listing, user is able to be redirected to the listing agents website so that they may contact them about listing. 
- [x] Users can hold down a Listing to open the pinned location in Google Maps.
      <details>
        <summary>Video Walkthrough</summary>
          <img src='https://github.com/OSU-CodePath-Android-Spring-2022/real-estate-app/blob/main/demos/gmaps-persistence.gif' title='Signup Video' width=''>
      </details>
- [x] Most recent search results persist in local storage across app restarts until logging out.
      <details>
        <summary>Video Walkthrough</summary>
          <img src='https://github.com/OSU-CodePath-Android-Spring-2022/real-estate-app/blob/main/demos/gmaps-persistence.gif' title='Signup Video' width=''>
      </details>

### 2. Screen Archetypes

* Login Screen - main screen if user is not logged in
   * Upon opening the app for the first time or after logging out, the user is taken to the login screen.
   * Login allows user to access data associated with their account (such as their wishlist).
   * If they do not have an account, user should be prompted to navigate to the register screen.
* Register Screen - can be accessed via a link or button at the bottom of the login screen
   * Similar to the login screen, but allows users to sign up for an account using their name, email, and password.
* Search Screen - main screen upon login
   * Upon entering an address, user will be navigated to a results screen.
* Results Screen - can be accessed by making a search or previous results can be seen by tapping the results tab
   * Allows user to view the results of their search (if any are found).
   * A listing includes data such as price, bedrooms, sq. footage, and photos.
   * Allows users to add a listing to their wishlist.
   * Allows user to see more details about a listing by tapping it.
* Detail Screen - can be accessed by tapping a listing in the results screen
   * Displays an expanded view of details about a property.
* Wishlist Screen - can be accessed by tapping the tab
   * Displays listings that were saved by the user.

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Search
* Wishlist
* Results
* Signout

**Flow Navigation** (Screen to Screen)

* Login Page
   * Log In -> Sign up if not login is known
   * Log In -> Search
  
* Search
   * search homes (after inputting address) -> Results
   
* Results
  * heart -> adds to Wishlist
  * tap home picture -> goes to Details screen which contains more pictures, price, and additional info

* Wishlist
  * Contains all homes that were put on the wishlist by the user

* Sign Out
  * Sign Out -> Login Page

## Wireframes
<img src="assets/handsketched-wireframes.jpg" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
#### Post

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | propertyId    | String   | unique id for the property, pulled from API |
   | primaryPhoto  | String   | URL for main photo of property |
   | photos        | Array    | array of URL strings for photos of property |
   | listPrice     | Number   | price of property |
   | yearBuilt     | Number   | year property was built |
   | baths         | Number   | number of bathrooms |
   | stories       | Number   | number of stories|
   | beds          | Number   | number of bedrooms|
   | type          | String   | type of property |
   | sqft          | Number   | square footage of property |
   | lotSqft       | Number   | square footage of property lot |
   | postalCode    | String   | property's zip code |
   | city          | String   | property's city |
   | stateCode     | String   | property's state |
   | streetAddr    | String   | street address of property |
   | savedAt       | DateTime | date when property was saved (default field) |

#### User

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | userId        | String   | unique id for the user (default field) |
   | username      | String   | username |
   | password      | String   | password |
   | wishlist      | Array    | an array that holds strings of property IDs the user saved |
   | createdAt     | DateTime | date when user was created (default field) |
   | updatedAt     | DateTime | date when user data was modified (default field) |
   
### Networking
* Login Screen
  * (Create/POST) Successful login creates a new session for existing user
  ```
      private fun loginUser(username: String, password: String) {
        ParseUser.logInInBackground(username, password, ({ user, e ->
            if (user != null) {
                Log.i(TAG, "Successfully logged in user ${username}")
            } else {
                e.printStackTrace()
                Toast.makeText(this, "Error logging in", Toast.LENGTH_SHORT).show()
              }})
          )
      }
   ```
* Signup Screen
  * (Create/POST) Registers a new user object
  ```
      private fun signupUser(username: String, password: String) {
        val user = ParseUser()

        user.setUsername(username)
        user.setPassword(password)

        user.signUpInBackground { e ->
            if (e == null) {
                Log.i(TAG, "Successfully signed up user ${username}")
                Toast.makeText(this, "${username} successfully registered", Toast.LENGTH_SHORT).show()
            } else {
                e.printStackTrace()
                Toast.makeText(this, "Error signing up", Toast.LENGTH_SHORT).show()
              }
          }
      }
    ```
* Search Screen
  * (Read/GET) Queries US Real Estate API for homes on sale near given address
  ```
    private fun getProperties() {
      val client = OkHttpClient()

      val request = Request.Builder()
        .url("https://us-real-estate.p.rapidapi.com/v2/for-sale?offset=0&limit=42&state_code=MI&city=Detroit&sort=newest")
        .get()
        .addHeader("X-RapidAPI-Host", "us-real-estate.p.rapidapi.com")
        .addHeader("X-RapidAPI-Key", "318a58784emsh066d9c1dc428aebp16adfejsnae96b9c83a63")
        .build()

      val response = client.newCall(request).execute()
    }
  ```
* Results Screen and Details Screen
  * (Create/POST) Add new listing object to store property details
  * (Update/PUT) Add a listing ID or remove a listing ID from the user's wishlist
* Wishlist Screen
  * (Read/GET) Query all listing objects with the IDs in the user's wishlist
  * (Update/PUT) Remove a listing ID from the user's wishlist

### Existing API Endpoints
US Real Estate API
- Base URL - [https://rapidapi.com/datascraper/api/us-real-estate/](https://rapidapi.com/datascraper/api/us-real-estate/)

| HTTP Verb | Endpoint | Description |
| --------- | -------- | ----------- |
| GET | /v2/for-sale/?city=city&state_code=state_code&limit=limit&offset=offset | Get homes for sale given a city, state, limit on results returned, and results offset

## Video Walkthrough

Sprint-1 Build Progress:

<img src='https://github.com/OSU-CodePath-Android-Spring-2022/real-estate-app/blob/main/demos/sprint-1.gif' title='Signup Video' width=''>

GIF created with [ScreenToGif](https://www.screentogif.com/).

Sprint-2 Build Progress:

<img src='https://github.com/OSU-CodePath-Android-Spring-2022/real-estate-app/blob/main/demos/sprint-2.gif' title='View Listings and Save/Remove from Wishlist' width=''>

GIF created with [LICEcap](https://www.cockos.com/licecap/).

Sprint-3 Build Progress:

<img src='https://github.com/OSU-CodePath-Android-Spring-2022/real-estate-app/blob/main/demos/sprint-3.gif' title='Signup Video' width=''>

## Open-source libraries used

- [Android Async HTTP](https://github.com/codepath/CPAsyncHttpClient) - Simple asynchronous HTTP requests with JSON parsing
