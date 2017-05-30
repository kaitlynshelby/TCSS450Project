What's in My Fridge?

1) Bugs from Phase 1 fixed:
- back button from the LandingPageActivity no longer goes to the previous Login/SignUp fragments

-------------------------------------------------------------------------------

2) User Stories
IMPLEMENTED
- As a registered user, I can manually add an item to the inventory list. 
- As a registered user, I can delete an item from the inventory list and optionally add it to their shopping list.
- As a registered user, I can search for an item in my inventory by name.
- As an unregistered user, I can create an account.
- As a registered user, I can sign into my account.
- As a registered user, I can check off an item on my shopping list to be automatically removed once purchased and added into my inventory.
- As a registered user, I can add details about a food item such as date purchased, expiration date, purchase price, quantity, and an image.
- As a registered user, I can easily see which items in my inventory are expired or about to expire by a color-coding system
- As a registered user, I can edit the details of an item in my inventory or shopping list
	- changed from "As a registered user, I can mark an item indicate that it is a staple item.", we felt it wasn't relevant and asked Menaka if we could change it to something else and she approved.
- As a registered user, I can share my shopping list as a text or email.

NOT IMPLEMENTED
- As a registered user, I can define categories to organize the inventory list.

-------------------------------------------------------------------------------

3) Device Storage
- Uses SharedPreferences to keep user logged in until they choose to log out.
- Uses SQLite to locally store both the inventory and the shopping lists items along iwth their details.

-------------------------------------------------------------------------------

4) Web Services
- Used web services to allow items to be retrieved from the database, added to the database, updated, and deleted. 

-------------------------------------------------------------------------------

5) Content Sharing:
- Can be found in ShoppingListFragment under the onOptionsItemSelected method. It shares the shopping list via text message.

-------------------------------------------------------------------------------

6) Sign-in
- Implemented sign-in using custom accounts

-------------------------------------------------------------------------------

7) Graphics
- Created a logo and a launcher icon

-------------------------------------------------------------------------------

8) Testing




